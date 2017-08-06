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


public class EmptyPtg extends Ptg {

    public static final EmptyPtg INSTANCE = new EmptyPtg();

    private EmptyPtg() {

    }

    public String getValue() {
        return "";
    }

    @Override
    public String stringValue(String... subStrings) {
        return getValue();
    }



}
