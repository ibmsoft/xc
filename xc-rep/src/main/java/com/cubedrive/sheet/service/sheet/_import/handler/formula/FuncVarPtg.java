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


public class FuncVarPtg extends OperatorPtg {

    private final String _name;

    private final int _minParamNum;

    private final int _maxParamNum;


    FuncVarPtg(String name, int minParamNum, int maxParamNum) {
        _name = name;
        _minParamNum = minParamNum;
        _maxParamNum = maxParamNum;
    }

    public final String getName() {
        return _name;
    }

    public final int getMinParamNum() {
        return _minParamNum;
    }

    public final int getMaxParamNum() {
        return _maxParamNum;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String stringValue(String... subStrings) {
        StringBuilder sb = new StringBuilder(_name);
        sb.append("(");
        for(String subString:subStrings){
            sb.append(subString);
            sb.append(",");
        }
        if(subStrings.length>0)
            sb.setCharAt(sb.length()-1, ')');
        else
            sb.append(")");
        return sb.toString();
    }
    


}
