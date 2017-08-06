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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSSFReadEventUtil {

    private static final Pattern utfPtrn = Pattern.compile("_x([0-9A-F]{4})_");
    
    public static final double default_row_height = 13.5d;

    public static final int default_style_index = 0;

    public static final String default_cell_type = "n";

    public static final String default_formula_type = "normal";

    public static final String error_ref = "#REF!";
    
    
    public static String utfDecode(String value){
        if(value == null) return null;
        
        StringBuffer buf = new StringBuffer();
        Matcher m = utfPtrn.matcher(value);
        int idx = 0;
        while(m.find()) {
            int pos = m.start();
            if( pos > idx) {
                buf.append(value.substring(idx, pos));
            }

            String code = m.group(1);
            int icode = Integer.decode("0x" + code);
            buf.append((char)icode);

            idx = m.end();
        }
        buf.append(value.substring(idx));
        return buf.toString();
    }
    

}
