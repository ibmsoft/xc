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


public class MissingArgPtg extends ValuePtg {

    public static final MissingArgPtg INSTANCE = new MissingArgPtg();

    private MissingArgPtg() {

    }
    
    public String getValue(){
        return "";
    }

    @Override
    public String stringValue(String... subStrings) {
        return "";
    }
    
   

}
