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
package com.cubedrive.sheet.service.sheet._export.dataformat;

import java.util.HashMap;
import java.util.Map;

public class LocaleMoney {
    
    private static final String currency_format = "[$%s]";
    
    private static final Map<String,String> _money_table= new HashMap<>();
    
    static {
        _money_table.put("$", "[$$-409]"); //dollar
//        _money_table.put("\u00a5", "[$¥-804]");//rmb;
        _money_table.put("\u00a5", "[$\u00A5-804]");//rmb;
        _money_table.put("\u20ac", "[$\u20AC-2]");// euro
        _money_table.put("£", "[$£-809]"); //pound
        _money_table.put("\u20b9", "[$\u20b9-4009]");//Rupee
        _money_table.put("C$", "[$$-1009]");//Canadian Dollar
        _money_table.put("Kr", "[$kr-41D]");//Swede
        _money_table.put("A$", "[$$-C09]"); //AUD
        _money_table.put("₩", "[$₩-412]");//Korean
        _money_table.put("zł","[$zł-415]"); //Poland
        _money_table.put("₽", "[$₽-419]"); //Russian
        _money_table.put("₺", "[$₺-41F]"); //Turkey 
        _money_table.put("Rp", "[$Rp-421]"); //Indonesia
        _money_table.put("R","[$R-1C09]"); //South Africa 
    }
    
    public static String getCurrencyDataFormat(String currency,int scale, boolean negative){
        StringBuilder buf = new StringBuilder();
        String dataFormat = _money_table.get(currency);
        if(dataFormat == null){
            dataFormat = String.format(currency_format,currency);
        }
        buf.append(dataFormat).append("\\ ");
        buf.append("#,##0");
        if(scale !=0){
           buf.append(".");
           for(int i=0;i<scale;i++){
               buf.append("0");
           }
        }
//        if(!negative)
//            buf.append("_);");
//        else{
//            String currentFormat = buf.toString();
//            String negCurrentFormat = currentFormat.replace("]\\ ", "]\\ \\-");
//            buf.append(";").append(negCurrentFormat);
//        }
        
        if(negative)
        {
            String currentFormat = buf.toString();
            String negCurrentFormat = currentFormat.replace("]\\ ", "]\\ \\-");
            buf.append(";").append(negCurrentFormat);
        }
        return buf.toString();
    }
    
    
}
