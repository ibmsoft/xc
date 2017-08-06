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


public class IntPtg extends ValuePtg {

    private final int _value;

    private IntPtg(int value) {
        _value = value;
    }

    public Integer getValue() {
        return _value;
    }

    @Override
    public String stringValue(String... subStrings) {
        return String.valueOf(_value);
    }

    public static IntPtg newInstance(String value) {
        return new IntPtg(Integer.parseInt(value));
    }

    public static IntPtg newInstance(int value) {
        return new IntPtg(value);
    }

    
}
