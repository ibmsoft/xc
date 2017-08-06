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
package com.cubedrive.sheet.service.sheet._import.handler.sheet;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;

public class ReadEventSheetFormatPreference {
    
    private static final int default_col_width = 72;
    
    private static final int default_row_height = 18;
    
    private int _defaultColWidth;
    
    private int _defaultRowHeight;
    
    private String _tabColor;
    
    private ReadEventSheetFormatPreference(){
        
    }
    
    public int getDefaultColWidth(){
        return _defaultColWidth;
    }
    
    public int getDefaultRowHeight(){
        return _defaultRowHeight;
    }
    
    public String getTabColor(){
    	return _tabColor;
    }
    
    public static ReadEventSheetFormatPreference build(SheetImportContext sheetImportContext){
        String appVersion =sheetImportContext.parentContext().workbook().appVersion();
        String majorVersion = "";
        if(appVersion != null && appVersion.length()>=2){
            majorVersion = appVersion.substring(0,2);
        }
        CTWorksheet worksheet = sheetImportContext.sheet().worksheet();
        int defaultColWidth=0,defaultRowHeight=0;
        String tabColorHex = null;
        if(worksheet.isSetSheetPr()){
            CTSheetFormatPr sheetFormatPr =  worksheet.getSheetFormatPr();
            if(worksheet.getSheetPr().isSetTabColor()){
            	CTColor tabColor = worksheet.getSheetPr().getTabColor();
            	XSSFColor xssfTabColor = new XSSFColor(tabColor); 
            	if(tabColor.isSetTheme()){
            		sheetImportContext.parentContext().workbook().theme().inheritFromThemeAsRequired(xssfTabColor);
            	}
        		byte[] rgb = xssfTabColor.getRGB();
        		if(rgb != null){
        			tabColorHex = SpreadsheetImportHelper.colorHexString(rgb);
        		}
            	
            }
            
            if(sheetFormatPr.isSetDefaultColWidth()){
                defaultColWidth = (int)(sheetFormatPr.getDefaultColWidth() * 8);
            }
            defaultRowHeight = (int)(sheetFormatPr.getDefaultRowHeight() *4 /3);
        }
        if(defaultColWidth == 0){
            defaultColWidth = colWidthByVersion(majorVersion);
        }
        if(defaultRowHeight == 0){
            defaultRowHeight = rowHeightByVersion(majorVersion);
        }
        ReadEventSheetFormatPreference preference = new ReadEventSheetFormatPreference();
        preference._defaultColWidth = defaultColWidth;
        preference._defaultRowHeight = defaultRowHeight;
        preference._tabColor = tabColorHex;
        return preference;
    }
    
    
    private static int colWidthByVersion(String majorVersion){
        switch(majorVersion){
            case "12": return 72;
            case "14": return 72;
            case "15": return 72;
        }
        return default_col_width;
    }
    
    private static int rowHeightByVersion(String majorVersion){
        switch(majorVersion){
            case "12":return 18;
            case "14":return 18;
            case "15":return 18;
        }
        return default_row_height;
    }

}
