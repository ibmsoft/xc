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
package com.cubedrive.sheet.service.sheet._import.xssf;


import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

public class XSSFReadEventRichString {
    
    List<String> _strings = new ArrayList<>();

    public String getStringValue() {
        StringBuilder sb = new StringBuilder();
        for(String s:_strings){
            sb.append(s);
        }
        return sb.toString();
    }


    private XSSFReadEventRichString() {

    }

    static XSSFReadEventRichString build(Attributes attributes) {
        return new XSSFReadEventRichString();
    }
    
    
    
    

}
