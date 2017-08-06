/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.sheet.service.sheet._import.handler.formula;


import org.apache.poi.ss.util.NumberToTextConverter;

public class ArrayPtg extends Ptg {

    private final int _nColumns;
    private final int _nRows;
    private final Object[] _tokens;

    private ArrayPtg(Object[][] values2d){
        _nColumns  = values2d[0].length;
        _nRows  = values2d.length;
        _tokens = new Object[_nColumns * _nRows];
        for( int r=0; r<_nRows; r++){
            Object[] rowData = values2d[r];
            for(int c=0; c<_nColumns; c++){
                _tokens[tokenIndex(c, r)] = rowData[c];
            }
        }
    }

    public Object[][] getTokens2d(){
        Object[][] result = new Object[_nRows][_nColumns];
        for(int r=0; r<_nRows; r++){
            Object[] rowData = result[r];
            for(int c=0; c < _nColumns; c++){
                rowData[c] = _tokens[tokenIndex(c, r)];
            }
        }
        return result;
    }

    public Object getTokens(){
        return _tokens;
    }

    public int getRowCount(){
        return _nRows;
    }

    public int getColumnCount(){
        return _nColumns;
    }

    private int tokenIndex(int colIx, int rowIx){
        return rowIx* _nColumns + colIx;
    }
    
    
    @Override
    public Object[][] getValue() {
        return getTokens2d();
    }

    @Override
    public String stringValue(String... subStrings) {
        StringBuilder sb = new StringBuilder("{");
        for(int i=0;i<_tokens.length;i++){
            sb.append(_tokens[i]);
            if(i % _nColumns ==0){
                sb.append(";");
            }else{
                sb.append(",");
            }
        }
        sb.setCharAt(sb.length()-1, '}');
        return sb.toString();
    }
    
    public static ArrayPtg newInstance(Object[][] tokens2d){
        return new ArrayPtg(tokens2d);
    }

    public static String tokenText(Object token){
        if(token == null)
            throw new RuntimeException("Array item cannot be null");
        if (token instanceof String)
            return "\"" + (String)token +"\"";
        if (token instanceof Double)
            return NumberToTextConverter.toText((Double)token);
        if (token instanceof Boolean)
            return ((Boolean)token).booleanValue() ? "TRUE" : "FALSE";
        if (token instanceof ErrPtg){
            return ((ErrPtg)token).stringValue();
        }
        throw new IllegalArgumentException("Unexpected constant class (" + token.getClass().getName() + ")");
    }



  


}
