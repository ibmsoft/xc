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


public class ErrPtg extends ValuePtg {

    public static final ErrPtg ERROR_NULL = new ErrPtg("#NULL!");

    public static final ErrPtg ERROR_DIV_0 = new ErrPtg("#DIV/0!");

    public static final ErrPtg ERROR_VALUE = new ErrPtg("#VALUE!");

    public static final ErrPtg ERROR_REF = new ErrPtg("#REF!");

    public static final ErrPtg ERROR_NAME = new ErrPtg("#NAME?");

    public static final ErrPtg ERROR_NUM = new ErrPtg("#NUM!");

    public static final ErrPtg ERROR_NA = new ErrPtg("#N/A");


    private final String _value;

    private ErrPtg(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    @Override
    public String stringValue(String... subStrings) {
        return getValue();
    }

}
