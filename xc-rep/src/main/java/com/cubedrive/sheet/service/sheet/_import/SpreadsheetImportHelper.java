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
package com.cubedrive.sheet.service.sheet._import;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;


public class SpreadsheetImportHelper {
    
    public static final int default_row_height_px = 18;
    
    public static final int default_column_width_px = 72;
    
    public static final String cdt_default_cbgc = "rgb(248,105,107)";
    
    public static final String cdt_default_ccolor = "rgb(150,0,0)";
    
    
    public static String ctColorToRgbFormat(CTColor ctColor,boolean pos){
        XSSFColor color = new XSSFColor(ctColor);
        byte[] colorBytes = color.getRGB();
        if(colorBytes == null || colorBytes.length!=3)
            return null;
        return colorBytesToRgbFormat(colorBytes,pos);
    }
    
    public static String colorBytesToRgbFormat(byte[] colorBytes,boolean pos){
        StringBuilder buf = new StringBuilder("rgb(");
        byte r = colorBytes[0];
        int rInt = r<0 ? 256+r:r;
        buf.append(pos ? rInt:255-rInt).append(",");
        byte g = colorBytes[1];
        int gInt = g<0? 256+g:g;
        buf.append(pos ? gInt:255-gInt).append(",");
        byte b = colorBytes[2];
        int bInt = b<0 ? 256+b:b;
        buf.append(pos ? bInt:256-bInt);
        buf.append(")");
        return buf.toString();
    }
    
    
    public static int[] colorBytesToInts(byte[] colorBytes){
        byte r = colorBytes[0];
        int rInt = r<0 ? 256+r:r;
        byte g = colorBytes[1];
        int gInt = g<0? 256+g:g;
        byte b = colorBytes[2];
        int bInt = b<0 ? 256+b:b;
        return new int[]{rInt,gInt,bInt};
    }
    
    
    public static String colorHexString(byte[] rgb) {
        StringBuilder colorBuffer = new StringBuilder("#");
        colorBuffer.append(String.format("%02X", rgb[0] & 0xFF));
        colorBuffer.append(String.format("%02X", rgb[1] & 0xFF));
        colorBuffer.append(String.format("%02X", rgb[2] & 0xff));
        String colorHex = colorBuffer.toString();
        return colorHex;
    }
    
    public static Map<String,Object> getSpanMap(int tabId, String cellRef){
    	Map<String,Object> spanMap = new HashMap<>();
		List<Integer> span = new ArrayList<>();
		CellRangeAddress sqrefCellRangeAddress = CellRangeAddress.valueOf(cellRef);
		span.add(tabId);
		span.add(sqrefCellRangeAddress.getFirstRow() + 1);
		span.add(sqrefCellRangeAddress.getFirstColumn() + 1);
		span.add(sqrefCellRangeAddress.getLastRow() + 1);
		span.add(sqrefCellRangeAddress.getLastColumn() + 1);
		spanMap.put("span", span);
		spanMap.put("type", 1);
		return spanMap;
    }

    // Need handle case. 
    // EMPTYEMPTY -> EMPTY\u00a0 - FOR LAST ONE EMPTY.
    public static String toJavascriptCompatibleString(String s){
    	if(s != null && s.length()>0 ){
    		if (s.endsWith(" ")) s = s.substring(0, s.length() - 1) + "\u00a0";
    		if (s.startsWith(" ")) s = "\u00a0" + s.substring(1, s.length());
    		return s;
    	}
    	return s;
    }

    public static String toJavascriptCompatibleStringFormula(String s){
    	if(s != null && s.length()>0){
    		return s.replaceAll("  ", " \u00a0");
    	}
    	return s;
    	
    }

}
