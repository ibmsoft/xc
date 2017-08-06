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


public class AreaPtg extends Ptg {

    private final String _firstSheetName;
    private final String _firstRef;
    private final String _lastSheetName;
    private final String _lastRef;


    private AreaPtg(String firstSheetName, String firstRef, String lastSheetName, String lastRef) {
        _firstSheetName = firstSheetName;
        _firstRef = firstRef;
        _lastSheetName = lastSheetName;
        _lastRef = lastRef;
    }


    public String getValue() {
        StringBuilder sb = new StringBuilder();
        if (_firstSheetName != null)
            sb.append(_firstSheetName).append("!");
        if (_firstRef != null)
            sb.append(_firstRef).append(":");
        if (_lastSheetName != null)
            sb.append(_lastSheetName).append("!");
        if (_lastRef != null)
            sb.append(_lastRef);
        return sb.toString();
    }

    @Override
    public String stringValue(String... subStrings) {
        return getValue();
    }
    
    
    String getFirstSheetName(){
        return _firstSheetName;
    }
    
    String getFirstRef(){
        return _firstRef;
    }
    
    String getLastSheetName(){
        return _lastSheetName;
    }
    
    String getLastRef(){
        return _lastRef;
    }


    public static AreaPtg newInstance(String firstSheetName, String firstRef, String lastSheetName, String lastRef) {
        return new AreaPtg(firstSheetName, firstRef, lastSheetName, lastRef);
    }


 

}
