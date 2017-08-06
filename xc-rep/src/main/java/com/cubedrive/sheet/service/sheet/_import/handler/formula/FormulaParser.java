/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package com.cubedrive.sheet.service.sheet._import.handler.formula;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.util.CellReference;

import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;

public class FormulaParser {

    private static final ParseNode EMPTY_PARSE_NODE = new ParseNode(EmptyPtg.INSTANCE);

    private static final Pattern CELL_REF_PATTERN = Pattern.compile("(\\$?[A-Za-z]+)?(\\$?[0-9]+)?");

    private final static char SPACE = ' ';

    private final static char TAB = '\t';

    private final static char CR = '\r';

    private final static char LF = '\n';

    private final CellImportContext _context;

    private final String _formulaString;

    private final int _formulaLength;

    private int _pointer;

    private ParseNode _rootNode;

    /**
     * Lookahead Character.
     * gets value '\0' when the input string is exhausted
     */
    private char look;


    private FormulaParser(String formula, CellImportContext context) {
        _formulaString = formula;
        _pointer = 0;
        _formulaLength = _formulaString.length();
        _context = context;
    }

    public static ParseNode parse(String formula, CellImportContext context) {
        FormulaParser fp = new FormulaParser(formula, context);
        fp.doParse();
        return fp._rootNode;

    }

    private void doParse() {
        _pointer = 0;
        getChar();
        _rootNode = comparisonExpression();
        if (_pointer <= _formulaLength) {
            String msg = "Unused input [" + _formulaString.substring(_pointer - 1)
                    + "] after attempting to parse the formula [" + _formulaString + "]";
            throw new RuntimeException(msg);
        }
    }

    private void getChar() {
        if (_pointer > _formulaLength) {
            throw new RuntimeException("too far");
        }
        if (_pointer < _formulaLength) {
            look = _formulaString.charAt(_pointer);
        } else {
            look = (char) 0;
        }
        _pointer++;

    }

    private void resetPointer(int ptr) {
        _pointer = ptr;
        if (_pointer <= _formulaLength) {
            look = _formulaString.charAt(_pointer - 1);
        } else {
            look = (char) 0;
        }
    }

    private RuntimeException expected(String s) {
        String msg;
        if (look == '=' && _formulaString.substring(0, _pointer - 1).trim().length() < 1) {
            msg = "The specified formula '" + _formulaString + "' starts with an equals sign which is not allowed";
        } else {
            msg = "Parse error near char " + (_pointer - 1) + " '" + look + "'" + " in specified formula '" + _formulaString + "'. Expected " + s;
        }
        return new RuntimeException(msg);
    }


    private static boolean isAlpha(char c) {
        return Character.isLetter(c) || c == '$' || c == '_';
    }

    private static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private static boolean isWhite(char c) {
        return c == SPACE || c == TAB || c == CR || c == LF;
    }

    private void skipWhite() {
        while (isWhite(look)) {
            getChar();
        }
    }

    private void match(char x) {
        if (look != x) {
            throw expected("'" + x + "'");
        }
        getChar();
    }


    private ParseNode parseRangeExpression() {
        ParseNode result = parseRangeable();
        while (look == ':') {
            getChar();
            ParseNode nextPart = parseRangeable();

            ParseNode[] children = {result, nextPart,};
            result = new ParseNode(RangePtg.INSTANCE, children);
        }
        return result;
    }


    private ParseNode parseRangeable() {
        skipWhite();
        int savePointer = _pointer;
        SheetIdentifier sheetIden = parseSheetName();
        if (sheetIden == null) {
            resetPointer(savePointer);
        } else {
            skipWhite();
            savePointer = _pointer;
        }

        SimpleRangePart part1 = parseSimpleRangePart();
        if (part1 == null) {
            if (sheetIden != null) {
                if (look == '#')
                    return new ParseNode(parseErrorLiteral());
                else
                    throw new RuntimeException("Cell reference expected after sheet name at index "
                            + _pointer + ".");
            }
            return parseNonRange(savePointer);
        }
        boolean whiteAfterPart1 = isWhite(look);
        if (whiteAfterPart1)
            skipWhite();

        if (look == ':') {
            int colonPos = _pointer;
            getChar();
            skipWhite();
            SimpleRangePart part2 = parseSimpleRangePart();
            if (part2 != null && !part1.isCompatibleForArea(part2))
                part2 = null;

            if (part2 == null) {
                resetPointer(colonPos);
                if (!part1.isCell()) {
                    String prefix;
                    if (sheetIden == null)
                        prefix = "";
                    else
                        prefix = "'" + sheetIden.getSheetIdentifier().getName() + '!';
                    throw new RuntimeException(prefix + part1.getRep() + "' is not a proper reference.");
                }
                return createAreaRefParseNode(sheetIden, part1, part2);
            }
            return createAreaRefParseNode(sheetIden, part1, part2);
        }

        if (look == '.') {
            getChar();
            int dotCount = 1;
            while (look == '.') {
                dotCount++;
                getChar();
            }
            boolean whiteBeforePart2 = isWhite(look);
            skipWhite();
            SimpleRangePart part2 = parseSimpleRangePart();
            String part1And2 = _formulaString.substring(savePointer - 1, _pointer - 1);
            if (part2 == null) {
                if (sheetIden != null)
                    throw new RuntimeException("Complete area reference expected after sheet name at index "
                            + _pointer + ".");
                return parseNonRange(savePointer);
            }
            if (whiteAfterPart1 || whiteBeforePart2) {
                if (part1.isRowOrColumn() || part2.isRowOrColumn()) {
                    // "A .. B" not valid syntax for "A:B"
                    // and there's no other valid expression that fits this grammar
                    throw new RuntimeException("Dotted range (full row or column) expression '"
                            + part1And2 + "' must not contain whitespace.");
                }
                return createAreaRefParseNode(sheetIden, part1, part2);
            }
            if (dotCount == 1 && part1.isRow() && part2.isRow()) {
                // actually, this is looking more like a number
                return parseNonRange(savePointer);
            }

            if (part1.isRowOrColumn() || part2.isRowOrColumn()) {
                if (dotCount != 2)
                    throw new RuntimeException("Dotted range (full row or column) expression '" + part1And2
                            + "' must have exactly 2 dots.");
            }
            return createAreaRefParseNode(sheetIden, part1, part2);
        }

        if (part1.isCell() && isValidCellReference(part1.getRep()))
            return createAreaRefParseNode(sheetIden, part1, null);

        if (sheetIden != null)
            throw new RuntimeException("Second part of cell reference expected after sheet name at index "
                    + _pointer + ".");

        return parseNonRange(savePointer);

    }

    /**
     * @param sheetIden may be <code>null</code>
     * @param part1
     * @param part2     may be <code>null</code>
     */
    private ParseNode createAreaRefParseNode(SheetIdentifier sheetIden, SimpleRangePart part1, SimpleRangePart part2) {
        Ptg ptg;
        String sheetName = sheetIden != null ? sheetIden.getSheetIdentifier().getName() : null;
        if (part2 == null)
            ptg = RefPtg.newInstance(sheetName, part1.getRep());
        else
            ptg = AreaPtg.newInstance(sheetName, part1.getRep(), sheetName, part2.getRep());
        return new ParseNode(ptg);
    }


    private ParseNode parseNonRange(int savePointer) {
        resetPointer(savePointer);
        if (Character.isDigit(look)) {
            Ptg numberPtg = parseNumber();
            return new ParseNode(numberPtg);
        }
        if (look == '"') {
            Ptg stringPtg = StringPtg.newInstance(parseStringLiteral());
            return new ParseNode(stringPtg);
        }
        // from now on we can only be dealing with non-quoted identifiers
        // which will either be named ranges or functions
        StringBuilder sb = new StringBuilder();

        // defined names may begin with a letter or underscore
        if (!Character.isLetter(look) && look != '_') {
            throw expected("number, string, or defined name");
        }
        while (isValidDefinedNameChar(look)) {
            sb.append(look);
            getChar();
        }
        skipWhite();
        String name = sb.toString();
        if (look == '(')
            return function(name);

        if (name.equalsIgnoreCase("TRUE"))
            return new ParseNode(BoolPtg.TRUE);
        else if (name.equalsIgnoreCase("FALSE"))
            return new ParseNode(BoolPtg.FALSE);
        else if (isDefinedName(name)) {
            NamePtg namePtg = NamePtg.newInstance(name);
            return new ParseNode(namePtg);
        } else {
            throw new RuntimeException("Undefined variable,name '" + name + "'");
        }
    }

    private boolean isDefinedName(String name) {
        if (_context != null) {
            WorkbookImportContext workContext = _context.parentContext().parentContext().parentContext();
            return workContext.isDefinedName(name);
        }
        return false;
    }


    private ParseNode function(String name) {
        FuncVarPtg funcPtg = FunctionPtgRegistry.getFunctionByName(name);
        if (funcPtg == null) {
            funcPtg = new FuncVarPtg(name, -1, -1);
        }
        match('(');
        ParseNode[] args = getFuncArguments();
        match(')');

        return new ParseNode(funcPtg, args);

    }


    private SimpleRangePart parseSimpleRangePart() {
        int ptr = _pointer - 1;
        boolean hasDigits = false;
        boolean hasLetters = false;
        while (ptr < _formulaLength) {
            char ch = _formulaString.charAt(ptr);
            if (Character.isDigit(ch))
                hasDigits = true;
            else if (Character.isLetter(ch))
                hasLetters = true;
            else if (ch == '$' || ch == '_') {
                //
            } else {
                break;
            }
            ptr++;

        }
        if (ptr <= _pointer - 1) {
            return null;
        }
        String rep = _formulaString.substring(_pointer - 1, ptr);
        if (!CELL_REF_PATTERN.matcher(rep).matches()) {
            return null;
        }
        // check range bounds against grid max
        if (hasLetters && hasDigits) {
            if (!isValidCellReference(rep))
                return null;
        } else if (hasLetters) {
            if (!CellReference.isColumnWithnRange(rep.replace("$", ""), SpreadsheetVersion.EXCEL2007))
                return null;
        } else if (hasDigits) {
            int i;
            try {
                i = Integer.parseInt(rep.replace("$", ""));
            } catch (NumberFormatException e) {
                return null;
            }
            if (i < 1 || i > 65536) {
                return null;
            }
        } else {
            return null;
        }

        resetPointer(ptr + 1);
        return new SimpleRangePart(rep, hasLetters, hasDigits);
    }


    private SheetIdentifier parseSheetName() {

        String bookName;
        if (look == '[') {
            StringBuilder sb = new StringBuilder();
            getChar();
            while (look != ']') {
                sb.append(look);
                getChar();
            }
            getChar();
            bookName = sb.toString();
        } else {
            bookName = null;
        }

        if (look == '\'') {
            StringBuilder sb = new StringBuilder();
            match('\'');
            boolean done = look == '\'';
            while (!done) {
                sb.append(look);
                getChar();
                if (look == '\'') {
                    match('\'');
                    done = look != '\'';
                }
            }
            Identifier iden = new Identifier(sb.toString(), true);
            skipWhite();
            if (look == '!') {
                getChar();
                return new SheetIdentifier(bookName, iden);
            }
            return null;
        }
        //unquoted sheet names must start with underscore or a letter
        if (look == '_' || Character.isLetter(look)) {
            StringBuilder sb = new StringBuilder();
            // concatenate idens with dots
            while (isUnquotedSheetNameChar(look)) {
                sb.append(look);
                getChar();
            }
            skipWhite();
            if (look == '!') {
                getChar();
                return new SheetIdentifier(bookName, new Identifier(sb.toString(), false));
            }
            return null;
        }
        return null;
    }

    private static boolean isUnquotedSheetNameChar(char ch) {
        if (Character.isLetterOrDigit(ch))
            return true;
        switch (ch) {
            case '.':
            case '_':
                return true;
        }
        return false;

    }

    private String parseStringLiteral() {
        match('"');
        StringBuilder token = new StringBuilder();
        while (true) {
            if (look == '"') {
                getChar();
                if (look != '"')
                    break;
            }
            token.append(look);
            getChar();
        }
        return token.toString();
    }

    private Boolean parseBooleanLiteral() {
        String iden = parseUnquotedIdentifier();
        if ("True".equalsIgnoreCase(iden))
            return Boolean.TRUE;
        if ("FALSE".equalsIgnoreCase(iden))
            return Boolean.FALSE;
        throw expected("'TRUE' or 'FALSE'");
    }


    private ErrPtg parseErrorLiteral() {
        match('#');
        String part1 = parseUnquotedIdentifier().toUpperCase();
        if (part1 == null)
            throw expected("remainder of error constant literal");

        switch (part1.charAt(0)) {
            case 'V':
                if (part1.equals("VALUE")) {
                    match('!');
                    return ErrPtg.ERROR_VALUE;
                }
                throw expected("#VALUE!");
            case 'R':
                if (part1.equals("REF")) {
                    match('!');
                    return ErrPtg.ERROR_REF;
                }
                throw expected("#REF!");
            case 'D':
                if (part1.equals("DIV")) {
                    match('/');
                    match('0');
                    match('!');
                    return ErrPtg.ERROR_DIV_0;
                }
                throw expected("#DIV/0");
            case 'N':
                if (part1.equals("NAME")) {
                    match('?');
                    return ErrPtg.ERROR_NAME;
                }
                if (part1.equals("NUM")) {
                    match('!');
                    return ErrPtg.ERROR_NUM;
                }
                if (part1.equals("NULL")) {
                    match('!');
                    return ErrPtg.ERROR_NULL;
                }
                if (part1.equals("N")) {
                    match('/');
                    if (look != 'A' && look != 'a')
                        throw expected("#N/A");
                    match(look);
                    return ErrPtg.ERROR_NA;

                }
                throw expected("#NAME?, #NUM!, #NULL! or #N/A");
        }
        throw expected("#VALUE!, #REF!, #DIV/0!, #NAME?, #NUM!, #NULL! or #N/A");

    }

    private String parseUnquotedIdentifier() {
        if (look == '\'')
            throw expected("unquoted identifier");

        StringBuilder sb = new StringBuilder();
        while (Character.isLetterOrDigit(look) || look == '.') {
            sb.append(look);
            getChar();
        }
        if (sb.length() < 1)
            return null;

        return sb.toString();
    }


    private boolean isValidCellReference(String str) {
        //check range bounds against grid max
        boolean result = CellReference.classifyCellReference(str, SpreadsheetVersion.EXCEL2007) == CellReference.NameType.CELL;
        if (result) {
            boolean isFunc = FunctionPtgRegistry.getFunctionByName(str) != null;
            if (isFunc) {
                int savePointer = _pointer;
                resetPointer(_pointer + str.length());
                skipWhite();
                // open bracket indicates that the argument is a function,
                // the returning value should be false, i.e. "not a valid cell reference"
                result = look != '(';
                resetPointer(savePointer);
            }
        }
        return result;
    }


    private Ptg parseNumber() {
        String number1 = getNum();
        String number2 = null;
        String exponent = null;

        if (look == '.') {
            getChar();
            number2 = getNum();
        }

        if (look == 'E') {
            getChar();
            String sign = "";
            if (look == '+') {
                getChar();
            } else if (look == '-') {
                getChar();
                sign = "-";
            }

            String number = getNum();
            if (number == null) {
                throw expected("Integer");
            }
            exponent = sign + number;
        }
        if (number1 == null && number2 == null)
            throw expected("Integer");

        return getNumberPtgFromString(number1, number2, exponent);
    }

    private String getNum() {
        StringBuilder value = new StringBuilder();
        while (isDigit(look)) {
            value.append(look);
            getChar();
        }
        return value.length() == 0 ? null : value.toString();
    }


    private ParseNode[] getFuncArguments() {
        List<ParseNode> temp = new ArrayList<>();
        skipWhite();
        if (look == ')') {
            return ParseNode.EMPTY_ARRAY;
        }

        boolean missedPrevArg = true;
        while (true) {
            skipWhite();
            if (isArgumentDelimiter(look)) {
                if (missedPrevArg)
                    temp.add(new ParseNode(MissingArgPtg.INSTANCE));

                if (look == ')')
                    break;

                match(',');
                missedPrevArg = true;
                continue;
            }
            temp.add(comparisonExpression());
            missedPrevArg = false;
            skipWhite();
            if (!isArgumentDelimiter(look)) {
                throw expected("',' or ')'");
            }
        }
        ParseNode[] result = new ParseNode[temp.size()];
        temp.toArray(result);
        return result;
    }


    private ParseNode comparisonExpression() {
        ParseNode result = concatExpression();
        while (true) {
            skipWhite();
            switch (look) {
                case '=':
                case '>':
                case '<':
                    Ptg comparisonToken = getComparisonToken();
                    ParseNode other = concatExpression();
                    result = new ParseNode(comparisonToken, result, other);
                    continue;
            }
            return result; // finished with predicate expression
        }
    }

    private ParseNode concatExpression() {
        ParseNode result = additiveExpression();
        while (true) {
            skipWhite();
            if (look != '&')
                break;

            match('&');
            ParseNode other = additiveExpression();
            result = new ParseNode(ValueOperatorPtg.CONCAT, result, other);
        }
        return result;
    }

    private ParseNode additiveExpression() {
        ParseNode result = term();
        while (true) {
            skipWhite();
            Ptg operator;
            switch (look) {
                case '+':
                    match('+');
                    operator = ValueOperatorPtg.ADD;
                    break;
                case '-':
                    match('-');
                    operator = ValueOperatorPtg.SUBTRACT;
                    break;
                default:
                    return result;
            }
            ParseNode other = term();
            result = new ParseNode(operator, result, other);
        }

    }

    private ParseNode term() {
        ParseNode result = powerFactor();
        while (true) {
            skipWhite();
            Ptg operator;
            switch (look) {
                case '*':
                    match('*');
                    operator = ValueOperatorPtg.MULTIPLY;
                    break;
                case '/':
                    match('/');
                    operator = ValueOperatorPtg.DIVIDE;
                    break;
                default:
                    return result;
            }
            ParseNode other = powerFactor();
            result = new ParseNode(operator, result, other);
        }

    }

    private ParseNode powerFactor() {
        ParseNode result = percentFactor();
        while (true) {
            skipWhite();
            if (look != '^')
                return result;
            match('^');
            ParseNode other = percentFactor();
            result = new ParseNode(ValueOperatorPtg.POWER, result, other);
        }

    }

    private ParseNode percentFactor() {
        ParseNode result = parseSimpleFactor();
        while (true) {
            skipWhite();
            if (look != '%')
                return result;
            match('%');
            result = new ParseNode(ValueOperatorPtg.MOD, result, EMPTY_PARSE_NODE);
        }

    }

    private ParseNode parseSimpleFactor() {
        skipWhite();
        switch (look) {
            case '#':
                return new ParseNode(parseErrorLiteral());
            case '-':
                match('-');
                return parseUnary(false);
            case '+':
                match('+');
                return parseUnary(true);
            case '(':
                match('(');
                ParseNode inside = comparisonExpression();
                match(')');
                return new ParseNode(ParenthesisPtg.INSTANCE, inside);
            case '"':
                return new ParseNode(StringPtg.newInstance(parseStringLiteral()));
            case '{':
                match('{');
                ParseNode arrayNode = parseArray();
                match('}');
                return arrayNode;
        }
        if (isAlpha(look) || Character.isDigit(look) || look == '\'' || look == '[')
            return parseRangeExpression();

        if (look == '.')
            return new ParseNode(parseNumber());

        throw expected("cell ref or constant literal");

    }

    private ParseNode parseUnary(boolean isPlus) {
        boolean numberFollows = isDigit(look) || look == '.';
        ParseNode factor = powerFactor();

        if (numberFollows) {
            Ptg token = factor.getToken();
            if (token instanceof NumberPtg) {
                if (isPlus)
                    return factor;
                token = NumberPtg.newInstance(-((NumberPtg) token).getValue());
                return new ParseNode(token);
            }

            if (token instanceof IntPtg) {
                if (isPlus)
                    return new ParseNode(ValueOperatorPtg.ADD, EMPTY_PARSE_NODE, factor);
                else {
                    token = IntPtg.newInstance(-((IntPtg) token).getValue());
                    return new ParseNode(token);
                }
            }
        }
        return new ParseNode(isPlus ? ValueOperatorPtg.ADD : ValueOperatorPtg.SUBTRACT, EMPTY_PARSE_NODE, factor);
    }

    private ParseNode parseArray() {
        List<Object[]> rowsData = new ArrayList<>();
        while (true) {
            Object[] singleRowData = parseArrayRow();
            rowsData.add(singleRowData);
            if (look == '}')
                break;

            if (look != ';')
                throw expected("'}' or ':'");

            match(';');
        }
        int nRows = rowsData.size();
        Object[][] values2d = new Object[nRows][];
        rowsData.toArray(values2d);
        int nColumns = values2d[0].length;
        checkRowLength(values2d, nColumns);
        return new ParseNode(ArrayPtg.newInstance(values2d));
    }

    private void checkRowLength(Object[][] values2d, int nColumns) {
        for (int i = 0; i < values2d.length; i++) {
            int rowLen = values2d[i].length;
            if (rowLen != nColumns) {
                throw new RuntimeException("Array row " + i + " has length " + rowLen
                        + " but row 0 has length " + nColumns);
            }
        }
    }

    private Object[] parseArrayRow() {
        List<Object> temp = new ArrayList<>();
        while (true) {
            temp.add(parseArrayItem());
            skipWhite();
            switch (look) {
                case '}':
                case ';':
                    break;
                case ',':
                    match(',');
                    continue;
                default:
                    throw expected("'}' or ','");
            }
            break;
        }
        Object[] result = new Object[temp.size()];
        temp.toArray(result);
        return result;

    }

    private Object parseArrayItem() {
        skipWhite();
        switch (look) {
            case '"':
                return parseStringLiteral();
            case '#':
                return parseErrorLiteral();
            case 'F':
            case 'f':
            case 'T':
            case 't':
                return parseBooleanLiteral();
            case '-':
                match('-');
                skipWhite();
                return convertArrayNumber(parseNumber(), false);
        }
        return convertArrayNumber(parseNumber(), true);
    }


    private Ptg getComparisonToken() {
        if (look == '=') {
            match(look);
            return ValueOperatorPtg.EQUAL;
        }
        boolean isGreater = look == '>';
        match(look);
        if (isGreater) {
            if (look == '=') {
                match('=');
                return ValueOperatorPtg.GREATER_EQUAL;
            }
            return ValueOperatorPtg.GREATER;
        }
        switch (look) {
            case '=':
                match('=');
                return ValueOperatorPtg.LESS_EQUAL;
            case '>':
                match('>');
                return ValueOperatorPtg.NOT_EQUAL;
        }
        return ValueOperatorPtg.LESS;
    }


    private static Ptg getNumberPtgFromString(String number1, String number2, String exponent) {
        StringBuilder number = new StringBuilder();
        if (number2 == null) {
            number.append(number1);
            if (exponent != null) {
                number.append('E');
                number.append(exponent);
            }

            String numberStr = number.toString();
            int intVal;
            try {
                intVal = Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                return NumberPtg.newInstance(numberStr);
            }
            if (intVal >= 0 && intVal <= 65536)
                return IntPtg.newInstance(intVal);

            return NumberPtg.newInstance(numberStr);
        }

        if (number1 != null) {
            number.append(number1);
        }
        number.append('.');
        number.append(number2);
        if (exponent != null) {
            number.append('E');
            number.append(exponent);
        }
        return NumberPtg.newInstance(number.toString());

    }

    private static boolean isValidDefinedNameChar(char ch) {
        if (Character.isLetterOrDigit(ch)) {
            return true;
        }
        switch (ch) {
            case '.':
            case '_':
            case '?':
            case '\\': // of all things
                return true;
        }
        return false;
    }

    private static boolean isArgumentDelimiter(char ch) {
        return ch == ',' || ch == ')';
    }

    private static Double convertArrayNumber(Ptg ptg, boolean isPositive) {
        String value;
        if (ptg instanceof IntPtg)
            value = ((IntPtg) ptg).stringValue();
        else if (ptg instanceof NumberPtg)
            value = ((NumberPtg) ptg).stringValue();
        else
            throw new RuntimeException("Unexpected ptg (" + ptg.getClass().getName() + ")");

        if (!isPositive) {
            value = "-" + value;
        }
        return Double.parseDouble(value);
    }

    private static final class Identifier {
        private final String _name;
        private final boolean _isQuoted;

        private Identifier(String name, boolean isQuoted) {
            _name = name;
            _isQuoted = isQuoted;
        }

        public String getName() {
            return _name;
        }

        public boolean isQuoted() {
            return _isQuoted;
        }

    }


    private static final class SheetIdentifier {

        private final String _bookName;
        private final Identifier _sheetIdentifier;

        private SheetIdentifier(String bookName, Identifier sheetIdentifier) {
            _bookName = bookName;
            _sheetIdentifier = sheetIdentifier;
        }

        public String getBookName() {
            return _bookName;
        }

        public Identifier getSheetIdentifier() {
            return _sheetIdentifier;
        }

    }


    private static final class SimpleRangePart {
        private enum Type {
            CELL, ROW, COLUMN;

            public static Type get(boolean hasLetters, boolean hasDigits) {
                if (hasLetters)
                    return hasDigits ? CELL : COLUMN;
                if (!hasDigits)
                    throw new IllegalArgumentException("must have either letters or digits");
                return ROW;
            }
        }

        private final Type _type;
        private final String _rep;

        private SimpleRangePart(String rep, boolean hasLetters, boolean hasDigits) {
            _rep = rep;
            _type = Type.get(hasLetters, hasDigits);
        }

        public boolean isCell() {
            return _type == Type.CELL;
        }

        public boolean isColumn() {
            return _type == Type.COLUMN;
        }

        public boolean isRow() {
            return _type == Type.ROW;
        }

        public boolean isRowOrColumn() {
            return _type != Type.CELL;
        }

        public CellReference getCellReference() {
            if (_type != Type.CELL) {
                throw new IllegalStateException("Not applicable to this type");
            }
            return new CellReference(_rep);
        }

        public String getRep() {
            return _rep;
        }

        public boolean isCompatibleForArea(SimpleRangePart part2) {
            return _type == part2._type;
        }

    }

}
