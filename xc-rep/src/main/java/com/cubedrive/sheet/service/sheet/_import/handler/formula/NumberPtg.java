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


public class NumberPtg extends ValuePtg {

    private final double _value;

    private NumberPtg(double value) {
        _value = value;
    }

    public Double getValue() {
        return _value;
    }
    
    @Override
    public String stringValue(String... subStrings) {
        return String.valueOf(_value);
    }

    public static NumberPtg newInstance(String value) {
        return new NumberPtg(Double.parseDouble(value));
    }

    public static NumberPtg newInstance(double value) {
        return new NumberPtg(value);
    }

   

}
