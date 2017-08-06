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


public class StringPtg extends ValuePtg {

    private final String _value;

    private StringPtg(String value) {
        _value = value;
    }
    
    public String getValue(){
        return _value;
    }

    @Override
    public String stringValue(String... subStrings) {
        return "\""+_value+"\"";
    }
   

    public static StringPtg newInstance(String value) {
        return new StringPtg(value);
    }

  
}
