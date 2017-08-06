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


public class RefPtg extends Ptg {

    private final String _sheetName;

    private final String _ref;

    private RefPtg(String sheetName, String ref) {
        _sheetName = sheetName;
        _ref = ref;
    }

    public String getValue() {
        StringBuilder sb = new StringBuilder();
        if (_sheetName != null)
            sb.append(_sheetName).append("!");
        if (_ref != null)
            sb.append(_ref);

        return sb.toString();
    }
    
    @Override
    public String stringValue(String... subStrings) {
        return getValue();
    }
    
    String getSheetName(){
        return _sheetName;
    }
    
    String getRef(){
        return _ref;
    }
    

    public static RefPtg newInstance(String sheetName, String ref) {
        return new RefPtg(sheetName, ref);
    }

    
}
