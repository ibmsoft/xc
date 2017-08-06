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


public class ParenthesisPtg extends ControlPtg {

    public static final ParenthesisPtg INSTANCE = new ParenthesisPtg();

    private ParenthesisPtg() {

    }
    
    public String getValue(){
        throw new UnsupportedOperationException();
    }

    @Override
    public String stringValue(String... subStrings) {
       return "(" + subStrings[0] + ")";
    }
    
    

}
