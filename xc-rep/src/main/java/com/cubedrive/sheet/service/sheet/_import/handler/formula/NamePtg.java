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

public class NamePtg extends ValuePtg {

    private final String _name;

    private NamePtg(String name) {
        _name = name;
    }

    public String getValue() {
        return _name;
    }

    @Override
    public String stringValue(String... subStrings) {
        return _name;
    }

    public static NamePtg newInstance(String name) {
        return new NamePtg(name);
    }

   
}
