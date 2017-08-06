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


public class ValueOperatorPtg extends OperatorPtg {

    private final String _value;

    private ValueOperatorPtg(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }
    
    @Override
    public String stringValue(String... subStrings) {
        return subStrings[0] + _value + subStrings[1];
    }
    

    public static final ValueOperatorPtg EQUAL = new ValueOperatorPtg("=");

    public static final ValueOperatorPtg GREATER_EQUAL = new ValueOperatorPtg(">=");

    public static final ValueOperatorPtg GREATER = new ValueOperatorPtg(">");

    public static final ValueOperatorPtg LESS_EQUAL = new ValueOperatorPtg("<=");

    public static final ValueOperatorPtg NOT_EQUAL = new ValueOperatorPtg("<>");

    public static final ValueOperatorPtg LESS = new ValueOperatorPtg("<");

    public static final ValueOperatorPtg ADD = new ValueOperatorPtg("+");

    public static final ValueOperatorPtg SUBTRACT = new ValueOperatorPtg("-");

    public static final ValueOperatorPtg MULTIPLY = new ValueOperatorPtg("*");

    public static final ValueOperatorPtg DIVIDE = new ValueOperatorPtg("/");

    public static final ValueOperatorPtg POWER = new ValueOperatorPtg("^");

    public static final ValueOperatorPtg MOD = new ValueOperatorPtg("%");

    public static final ValueOperatorPtg CONCAT = new ValueOperatorPtg("&");

   
}
