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
package com.cubedrive.sheet.service.sheet._import.handler.cell;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;

import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.RowImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.SSImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;

public class ReadEventCellStyle {

    private final Map<String, Object> _styles = new HashMap<>();

    private ReadEventCellStyle() {

    }

    public Map<String, Object> getStyles() {
        return _styles;
    }

    private static Map<String, Object> doGetFontSettings(
            XSSFCellStyle xssfCellStyle) {
        Map<String, Object> settings = new HashMap<>();
        XSSFFont font = xssfCellStyle.getFont();
        if (font.getBold()) {
            settings.put("fw", "bold");
        }
        if (font.getItalic()) {
            settings.put("fs", "italic");
        }
        if (font.getUnderline() > 0) {
            settings.put("u", "underline");
        }
        if (font.getStrikeout()) {
            settings.put("s", "line-through");
        }
        if (!font.getFontName().equalsIgnoreCase(XSSFFont.DEFAULT_FONT_NAME)) {
            settings.put("ff", font.getFontName());
        }
        if (font.getFontHeightInPoints() != XSSFFont.DEFAULT_FONT_SIZE) {
            settings.put("fz", font.getFontHeightInPoints());
        }
        XSSFColor xssfFontColor = font.getXSSFColor();
        byte[] rgb = xssfFontColor != null ? xssfFontColor.getRGB(): null;
        if (rgb != null) {
            String fontColor = SpreadsheetImportHelper.colorHexString(rgb);
        	settings.put("color", fontColor);
        }else{
        	settings.put("color", "#000000");
        }

        return settings;
    }

    private static String doGetBackgroundColor(XSSFCellStyle xssfCellStyle) {
    	String bgc = null;
    	XSSFColor color = xssfCellStyle.getFillForegroundXSSFColor();
        if(color == null){
        	bgc = "";
        }else{
            bgc = SpreadsheetImportHelper.colorHexString(color.getRGB());
        }
        return bgc;
    }

    private static Map<String, Object> doGetAlignmentSettings(XSSFCellStyle xssfCellStyle, int cellType) {
        Map<String, Object> settings = new HashMap<>();
        CTXf ctXf = xssfCellStyle.getCoreXf();
        String ta = null;
        String va = "bottom";
        Integer rotation = null;
        if (ctXf.isSetAlignment()) {
            CTCellAlignment align = xssfCellStyle.getCoreXf().getAlignment();
            if (align.isSetHorizontal()) {
                HorizontalAlignment hAlignment = HorizontalAlignment.values()[align
                        .getHorizontal().intValue() - 1];
                switch (hAlignment) {
                case LEFT:
                    ta = "left";
                    break;
                case CENTER:
                    ta = "center";
                    break;
                case RIGHT:
                    ta = "right";
                    break;
                default:
                    break;
                }
            }
            if (align.isSetVertical()) {
                VerticalAlignment vAlignment = VerticalAlignment.values()[align.getVertical().intValue() - 1];
                switch (vAlignment) {
                case TOP:
                    va = "top";
                    break;
                case CENTER:
                    va = "middle";
                    break;
                case BOTTOM:
                    va = "bottom";
                    break;
                default:
                    va = null;
                    break;
                }
            }
            
            if(align.isSetTextRotation()){
            	int textRotation =(int)align.getTextRotation();
            	switch(textRotation){
            		case 45: rotation = 45;break;
            		case 135: rotation = 135;break;
            		case 255: rotation = 255;break;
            		case 90: rotation = 270;break;
            		case 180: rotation = 90; break;
            	}
            }
            
        }
        if(ta == null){
        	switch(cellType){
        		case CellImportContext.CELL_TYPE_NUMERIC: ta = "right";break;
        		case CellImportContext.CELL_TYPE_STRING:ta = "left"; break;
        		case CellImportContext.CELL_TYPE_ERROR:ta = "center";break;
        		case CellImportContext.CELL_TYPE_BOOLEAN:ta = "center";break;
        	}
        }
        
        if(ta != null){
        	settings.put("ta", ta);
        }
        if (va != null) {
            settings.put("va", va);
        }
        
        if(rotation != null){
        	settings.put("rotation", rotation);
        }
        
        if (xssfCellStyle.getWrapText()) {
            settings.put("ws", "normal");
            settings.put("ww", "break-word");
        }
        
        
        return settings;
    }

    private static Map<String, Object> doGetBorderSettings(
            XSSFCellStyle xssfCellStyle) {
        Map<String, Object> settings = new HashMap<>();
        int bl_code = xssfCellStyle.getBorderLeft();
        if (bl_code != 0) {
            settings.put("blf", 1);
            Map<String, Object> borderLeftStyle = borderStyle(bl_code, 'l',
                    xssfCellStyle);
            settings.putAll(borderLeftStyle);
        }

        int br_code = xssfCellStyle.getBorderRight();
        if (br_code != 0) {
            Map<String, Object> borderRightStyle = borderStyle(br_code, 'r',
                    xssfCellStyle);
            settings.putAll(borderRightStyle);
        }

        int bt_code = xssfCellStyle.getBorderTop();
        if (bt_code != 0) {
            settings.put("btf", 1);
            Map<String, Object> borderTopStyle = borderStyle(bt_code, 't',
                    xssfCellStyle);
            settings.putAll(borderTopStyle);
        }
        int bb_code = xssfCellStyle.getBorderBottom();
        if (bb_code != 0) {
            Map<String, Object> borderBottomStyle = borderStyle(bb_code, 'b',
                    xssfCellStyle);
            settings.putAll(borderBottomStyle);
        }
        return settings;
    }

    private static Map<String, Object> borderStyle(int borderStyleCode,
            char borderPos, XSSFCellStyle xssfCellStyle) {
        Map<String, Object> bsSetting = new HashMap<>();
        String bxs = String.format("b%ss", borderPos);
        String bxt = String.format("b%st", borderPos);
        String bxf = String.format("b%sf", borderPos);
        String bxw = String.format("b%sw", borderPos);
        String px = String.format("p%s", borderPos);
        String bxc = String.format("b%sc", borderPos);
        BorderStyle borderStyle = BorderStyle.values()[borderStyleCode];
        switch (borderStyle) {
        case THIN: {
            bsSetting.put(bxs, "solid");
            bsSetting.put(bxt, false);
            break;
        }
        case DOTTED: {
            bsSetting.put(bxs, "dotted");
            bsSetting.put(bxt, false);
            break;
        }
        case DASHED: {
            bsSetting.put(bxs, "dashed");
            bsSetting.put(bxt, false);
            break;
        }
        case MEDIUM: {
            bsSetting.put(bxs, "solid");
            bsSetting.put(bxt, false);
            if (borderPos == 't' || borderPos == 'l')
                bsSetting.put(bxf, 2);
            else {
                bsSetting.put(bxw, 2);
                bsSetting.put(px, 1);
            }
            break;
        }
        case DOUBLE: {
            bsSetting.put(bxs, "solid");
            bsSetting.put(bxt, "double");
            if (borderPos == 't' || borderPos == 'l') {
                bsSetting.put(bxw, 1);
            }
            bsSetting.put("wrap", true);
            bsSetting.put(px, 1);
            break;
        }
        case THICK: {
            bsSetting.put(bxs, "solid");
            bsSetting.put(bxt, "solid");
            if (borderPos == 't' || borderPos == 'l') {
                bsSetting.put(bxw, 1);
            } else {
                bsSetting.put(bxw, 2);
            }
            bsSetting.put("wrap", true);
            bsSetting.put(px, 1);
            break;
        }
        default:
            break;
        }

        XSSFColor color;
        switch (borderPos) {
        case 'l':
            color = xssfCellStyle.getLeftBorderXSSFColor();
            break;
        case 'r':
            color = xssfCellStyle.getRightBorderXSSFColor();
            break;
        case 't':
            color = xssfCellStyle.getTopBorderXSSFColor();
            break;
        case 'b':
            color = xssfCellStyle.getBottomBorderXSSFColor();
            break;
        default:
            color = null;
        }
        byte[] rgb = color != null? color.getRGB() : null;
        if (rgb != null) {
            String colorRgb = SpreadsheetImportHelper.colorHexString(rgb);
            bsSetting.put(bxc, colorRgb);
        } else {
            bsSetting.put(bxc, "black");
        }
        return bsSetting;
    }

    private static String dsd(XSSFCellStyle xssfCellStyle, SSImportContext ssImportContext) {
        String _dsd = null; 
        SheetImportContext sheetImportContext = null;
        if(ssImportContext instanceof SheetImportContext){
        	sheetImportContext = (SheetImportContext)ssImportContext;
        }else if(ssImportContext instanceof RowImportContext){
        	sheetImportContext = ((RowImportContext)ssImportContext).parentContext();
        }else if(ssImportContext instanceof CellImportContext){
        	sheetImportContext = ((CellImportContext)ssImportContext).parentContext().parentContext();
        }
        
        
    	if(sheetImportContext != null && sheetImportContext.isSheetProtection()){
    		if(xssfCellStyle.getCoreXf().isSetApplyProtection() && xssfCellStyle.getCoreXf().getApplyProtection() 
    				&& xssfCellStyle.getCoreXf().getProtection() != null && xssfCellStyle.getCoreXf().getProtection().isSetLocked() 
    				&& !xssfCellStyle.getCoreXf().getProtection().getLocked()){
    			_dsd = "";
    		}else{
    			_dsd = "ed";
    		}
    		if("ed".equals(_dsd) && ssImportContext instanceof CellImportContext){
    			CellImportContext cellImportContext = (CellImportContext)ssImportContext;
    			
    			if(sheetImportContext.isNoProtectdRow(cellImportContext.cellReference().getRow()+1) 
    					|| sheetImportContext.isNoProtectedColumn(cellImportContext.cellReference().getCol()+1)){
    				_dsd = "";
    			}
    		}
    	}
    	return _dsd;
    }

    

    public static ReadEventCellStyle build(XSSFCellStyle cellStyle, int cellType, SSImportContext ssImportContext) {
    	ReadEventCellStyle readEventCellStyle = new ReadEventCellStyle();
    	WorkbookImportContext workbookImportContext = ssImportContext.workbookImportContext();
        short styleIndex = cellStyle.getIndex();
        Map<String,Object> styleMap = null;
        if(cellType != -1){
        	styleMap = workbookImportContext.getCachedCellStyleMap(styleIndex);
        }else{
        	styleMap = workbookImportContext.getCachedRowOrColumnStyleMap(styleIndex);
        }
        if(styleMap == null){
	        Map<String, Object> fontSettings = doGetFontSettings(cellStyle);
	        String backgroundColor = doGetBackgroundColor(cellStyle);
	        if (backgroundColor != null ) {
	            readEventCellStyle._styles.put("bgc", backgroundColor);
	        }
	        readEventCellStyle._styles.putAll(fontSettings);
	        Map<String, Object> alignmentSettings = doGetAlignmentSettings(cellStyle, cellType);
	        readEventCellStyle._styles.putAll(alignmentSettings);
	        
	        String dsdValue =  dsd(cellStyle, ssImportContext);
	        if(dsdValue != null){
	        	readEventCellStyle._styles.put("dsd",dsdValue);
	        }
	        
	        if(cellType !=-1 ){ // cell type:-1 stands for column or row
		        Map<String, Object> borderSettings = doGetBorderSettings(cellStyle);
		        readEventCellStyle._styles.putAll(borderSettings);
		        workbookImportContext.cacheCellStyleMap(styleIndex, readEventCellStyle._styles);
	        }else{
	        	workbookImportContext.cacheRowOrColumnStyleMap(styleIndex, readEventCellStyle._styles);
	        }
	        
        }else{
        	readEventCellStyle._styles.putAll(styleMap);
        }
        return readEventCellStyle;
    }
    
    
    
    

}
