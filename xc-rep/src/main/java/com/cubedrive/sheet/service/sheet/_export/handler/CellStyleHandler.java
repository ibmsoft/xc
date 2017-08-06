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
package com.cubedrive.sheet.service.sheet._export.handler;

import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellProtection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;

public class CellStyleHandler {

    private static final CellStyleHandler _instance = new CellStyleHandler();


    public XSSFCellStyle getOrCreateCellStyle(XSSFCellExportContext xssfCellExportContext) {
        XSSFFont font = getOrCreateFont(xssfCellExportContext);
        HorizontalAlignment ha = getHorizontalAlignment(xssfCellExportContext);
        VerticalAlignment va = getVerticalAlignment(xssfCellExportContext);
        Integer textRotation = getTextRotation(xssfCellExportContext);
        boolean wrapText = isWrapText(xssfCellExportContext);
        boolean locked = isLocked(xssfCellExportContext);
        BorderStyleAndColor leftBorder = getBorderStyleBySide('l', xssfCellExportContext);
        BorderStyleAndColor rightBorder = getBorderStyleBySide('r', xssfCellExportContext);
        BorderStyleAndColor topBorder = getBorderStyleBySide('t', xssfCellExportContext);
        BorderStyleAndColor bottomBorder = getBorderStyleBySide('b', xssfCellExportContext);
        String bgc = getBackgroundColor(xssfCellExportContext);

        Map<String, Object> cellStyleProps = new SpreadsheetExportHelper.ExtHashMap<>();
        if (font != null) {
            cellStyleProps.put("font", font.getIndex());
        }
        if (ha != null) {
            cellStyleProps.put("ha", ha);
        }
        if (va != null) {
            cellStyleProps.put("va", va);
        }
        if (textRotation != null){
        	cellStyleProps.put("textRotation", textRotation);
        }
        if (wrapText){
        	cellStyleProps.put("wrapText", wrapText);
        }
        if (locked){
        	cellStyleProps.put("locked", locked);
        }
        if (leftBorder != BorderStyleAndColor.NONE) {
            cellStyleProps.put("leftBorder", leftBorder);
        }
        if (rightBorder != BorderStyleAndColor.NONE) {
            cellStyleProps.put("rightBorder", rightBorder);
        }
        if (topBorder != BorderStyleAndColor.NONE) {
            cellStyleProps.put("topBorder", topBorder);
        }
        if (bottomBorder != BorderStyleAndColor.NONE) {
            cellStyleProps.put("bottomBorder", bottomBorder);
        }
        
        if (bgc != null && bgc.length()>0){
            cellStyleProps.put("backgroundColor", bgc);
        }
        
        short dataFormatIndex = DataFormatHandler.instance().getOrCreateDataFormat(xssfCellExportContext);
        if(dataFormatIndex != DataFormatHandler.NO_FMT){
        	cellStyleProps.put("dfIndex", dataFormatIndex);
        }
        
        
        XSSFCellStyle cellStyle= null;
        
        if(! cellStyleProps.isEmpty()){
	        int cellStylePropsHash = cellStyleProps.hashCode();
	        Map<Integer,XSSFCellStyle> cellStyles =xssfCellExportContext.
	        		parentContext().parentContext().cellStyles();
	        cellStyle = cellStyles.get(cellStylePropsHash);
	        
	        if(cellStyle == null){
	        	cellStyle = xssfCellExportContext.parentContext().
	        			parentContext().xssfWorkbook().createCellStyle();
	        	if(font != null){
	        		cellStyle.setFont(font);
	        	}
	        	if(ha != null){
	        		cellStyle.setAlignment(ha);
	        	}
	        	if(va != null){
	        		cellStyle.setVerticalAlignment(va);
	        	}
	        	if(textRotation != null){
	        		cellStyle.setRotation(textRotation.shortValue());
	        	}
	        	if(wrapText){
	        		cellStyle.setWrapText(wrapText);
	        	}
	        	
	        	if(locked){
	        		cellStyle.getCoreXf().setApplyProtection(true);
	        		// add protected cell background cell
//	        		XSSFColor lockedColor =  SpreadsheetExportHelper.colorAsXssfColor(SpreadsheetExportHelper.lock_cell_color);
//                    cellStyle.setFillForegroundColor(lockedColor);
//                    cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	        		
	        	}else if(xssfCellExportContext.parentContext().isSheetNeedProtection()){
	        		CTXf xf = cellStyle.getCoreXf();
	                xf.setApplyProtection(true);
	                CTCellProtection protection = xf.addNewProtection();
	                protection.setLocked(false);
	        	}
	        	
	        	if(leftBorder != BorderStyleAndColor.NONE){
	        		if(leftBorder._borderStyle != BorderStyle.NONE){
	        			cellStyle.setBorderLeft(leftBorder._borderStyle);
	        		}
	        		if(leftBorder._color != null){
	        			XSSFColor color = SpreadsheetExportHelper.colorAsXssfColor(leftBorder._color);
	        			if(color != null)
	        				cellStyle.setBorderColor(BorderSide.LEFT, color);
	        		}
	        	}
	        	if(rightBorder != BorderStyleAndColor.NONE){
	        		if(rightBorder._borderStyle != BorderStyle.NONE){
	        			cellStyle.setBorderRight(rightBorder._borderStyle);
	        		}
	        		if(rightBorder._color != null){
	        			XSSFColor color = SpreadsheetExportHelper.colorAsXssfColor(rightBorder._color);
	        			if(color != null)
	        				cellStyle.setBorderColor(BorderSide.RIGHT, color);
	        		}
	        	}
	        	if(topBorder != BorderStyleAndColor.NONE){
	        		if(topBorder._borderStyle != BorderStyle.NONE){
	        			cellStyle.setBorderTop(topBorder._borderStyle);
	        		}
	        		if(topBorder._color != null){
	        			XSSFColor color = SpreadsheetExportHelper.colorAsXssfColor(topBorder._color);
	        			if(color != null)
	        				cellStyle.setBorderColor(BorderSide.TOP, color);
	        		}
	        	}
	        	if(bottomBorder != BorderStyleAndColor.NONE){
	        		if(bottomBorder._borderStyle != BorderStyle.NONE){
	        			cellStyle.setBorderBottom(bottomBorder._borderStyle);
	        		}
	        		if(bottomBorder._color != null){
	        			XSSFColor color = SpreadsheetExportHelper.colorAsXssfColor(bottomBorder._color);
	        			if(color != null)
	        				cellStyle.setBorderColor(BorderSide.BOTTOM, color);
	        		}
	        	}
	        	if(bgc != null){
	        		XSSFColor color =  SpreadsheetExportHelper.colorAsXssfColor(bgc);
	        		if(color != null){
		        		cellStyle.setFillForegroundColor(color);
		        		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
	        		}
	        	}
	        	if(dataFormatIndex != DataFormatHandler.NO_FMT){
	        		 cellStyle.setDataFormat(dataFormatIndex);
	        	}
	        	cellStyles.put(cellStylePropsHash, cellStyle);
	        }
        }
        return cellStyle;
        
    }


    private HorizontalAlignment getHorizontalAlignment(XSSFCellExportContext xssfCellExportContext) {
        String ta = (String) lookupStyle("ta",xssfCellExportContext);
        if (ta == null)
            return null;
        switch (ta) {
            case "left":
                return HorizontalAlignment.LEFT;
            case "center":
                return HorizontalAlignment.CENTER;
            case "right":
                return HorizontalAlignment.RIGHT;
            default:
                return null;
        }
    }


    private VerticalAlignment getVerticalAlignment(XSSFCellExportContext xssfCellExportContext) {
        String va = (String) lookupStyle("va",xssfCellExportContext);
        if (va == null)
            return VerticalAlignment.TOP;
        switch (va) {
            case "top":
                return VerticalAlignment.TOP;
            case "middle":
                return VerticalAlignment.CENTER;
            case "bottom":
                return VerticalAlignment.BOTTOM;
            default:
                return null;
        }
    }
    
    private Integer getTextRotation(XSSFCellExportContext xssfCellExportContext) {
    	Integer rotation = (Integer)lookupStyle("rotation", xssfCellExportContext);
    	Integer textRotation = null;
    	if(rotation !=null){
    		switch(rotation){
    			case 90:textRotation =180; break;
    			case 270:textRotation =90; break;
    			default:
    				textRotation = rotation;break;
    		}
    		
    	}
    	return textRotation;
    }

    private boolean isWrapText(XSSFCellExportContext xssfCellExportContext) {
        String ww = (String)lookupStyle("ww",xssfCellExportContext);
        if (!Strings.isNullOrEmpty(ww)) {
            return true;
        } else {
            return false;
        }

    }
    
    private boolean isLocked(XSSFCellExportContext xssfCellExportContext){
    	XSSFWorksheetExportContext worksheetExportContext = xssfCellExportContext.parentContext();
    	SheetCell sheetCell = xssfCellExportContext.sheetCell();
    	String disabled =(String) lookupStyle("dsd",xssfCellExportContext);
    	Boolean locked =null;
    	if("".equals(disabled)){
			locked = false;
			if(sheetCell.getX()!=0 && sheetCell.getY()==0){
				worksheetExportContext.addNoLockedRow(sheetCell.getX());
			}else if(sheetCell.getX() ==0 && sheetCell.getY() !=0){
				worksheetExportContext.addNoLockedColumn(sheetCell.getY());
			}
		}else if("ed".equals(disabled) ){
    		xssfCellExportContext.parentContext().lockCell(sheetCell);
    		locked = true;
    	}
    	if(locked == null){
	    	if(worksheetExportContext.isLockedAll()){
	    		 if(worksheetExportContext.isRowNoLocked(sheetCell.getX()) || worksheetExportContext.isColumnNoLocked(sheetCell.getY())){
	    			locked = false;
	    		}else{
	    			locked = true;
	    		}
	    	}else { 
	    		if((!(worksheetExportContext.isRowNoLocked(sheetCell.getX()) || worksheetExportContext.isColumnNoLocked(sheetCell.getY()))) &&
		    			worksheetExportContext.isRowLocked(sheetCell.getX()) || worksheetExportContext.isColumnLocked(sheetCell.getY())){
		    		locked = true;
		    	}else {
		    		locked = false;
		    	}
	    	}
    	}
    	return locked;
    }

    private String getBackgroundColor(XSSFCellExportContext xssfCellExportContext) {
        return (String) lookupStyle("bgc",xssfCellExportContext);
    }


    private BorderStyleAndColor getBorderStyleBySide(char side, XSSFCellExportContext xssfCellExportContext) {
        BorderStyleAndColor borderStyleAndColor;

        String _bxs = String.format("b%ss", side);
        String _bxt = String.format("b%st", side);
        String _bxf = String.format("b%sf", side);
        String _bxw = String.format("b%sw", side);
        String _px = String.format("p%s", side);
        String _bxc = String.format("b%sc", side);
		Object oo = lookupStyle(_bxf,xssfCellExportContext);
//System.out.println(_bxf + " oo: " + oo);
		if(null != oo && "".equals(oo.toString())) {
			oo = null;
		}
		Integer bxf = (Integer) oo;
    	//Integer bxf = (Integer) lookupStyle(_bxf,xssfCellExportContext);
    	Object bxtValue =  lookupStyle(_bxt,xssfCellExportContext);
        String bxt = bxtValue != null ? bxtValue.toString(): null;
        Boolean wrap = (Boolean)lookupStyle("wrap", xssfCellExportContext);
        String bxc = (String) lookupStyle(_bxc,xssfCellExportContext);
        if((bxc!=null && bxc.length()>0)){
//        if((bxc!=null && bxc.length()>0) && ((wrap!=null && wrap && bxt!=null && bxt.length()>0) || (bxf != null && bxf > 0))){
	        String bxs = (String) lookupStyle(_bxs,xssfCellExportContext);
	        Integer bxw = (Integer) lookupStyle(_bxw, xssfCellExportContext);
	        Integer px = (Integer) lookupStyle(_px,xssfCellExportContext);
            BorderStyle borderStyle = BorderStyle.THIN;
            if ("false".equals(bxt)) {
                if ("dotted".equals(bxs)) {
                    borderStyle = BorderStyle.DOTTED;
                } else if ("dashed".equals(bxs)) {
                    borderStyle = BorderStyle.DASHED;
                } else if ("solid".equals(bxs)) {
                    if (bxf != null || bxw != null) {
                        borderStyle = BorderStyle.MEDIUM;
                    } else {
                        borderStyle = BorderStyle.THIN;
                    }
                }
            } else if ("double".equals(bxt)) {
                borderStyle = BorderStyle.DOUBLE;
            } else if ("solid".equals(bxt)) {
                borderStyle = BorderStyle.THICK;
            }

            borderStyleAndColor = new BorderStyleAndColor(borderStyle, bxc);
        } else {
            borderStyleAndColor = BorderStyleAndColor.NONE;
        }
        return borderStyleAndColor;
    }


    private XSSFFont getOrCreateFont(XSSFCellExportContext xssfCellExportContext) {
        Map<String, Object> fontProps = new SpreadsheetExportHelper.ExtHashMap<>();

        String fontColor = (String)lookupStyle("color", xssfCellExportContext);
        if (!Strings.isNullOrEmpty(fontColor)) {
            fontProps.put("fontColor", fontColor);
        }

        String fontName = (String) lookupStyle("ff", xssfCellExportContext);
        if (!Strings.isNullOrEmpty(fontName)) {
            fontProps.put("fontName", fontName);
        }

        Object fz = lookupStyle("fz", xssfCellExportContext);
        Integer fontSize =null;
        if(fz instanceof String){
        	fontSize = Ints.tryParse((String)fz);
        }else if(fz instanceof Integer){
        	fontSize = (Integer)fz;
        }
        if (fontSize != null && fontSize != 0) {
            fontProps.put("fontSize", fontSize);
        }

        String strikeout = (String) lookupStyle("s", xssfCellExportContext);
        if (!Strings.isNullOrEmpty(strikeout)) {
            fontProps.put("strikeout", strikeout);
        }

        String underline = (String) lookupStyle("u",xssfCellExportContext);
        if (!Strings.isNullOrEmpty(underline)) {
            fontProps.put("underline", underline);
        }

        String italic = (String) lookupStyle("fs",xssfCellExportContext);
        if (!Strings.isNullOrEmpty(italic)) {
            fontProps.put("italic", italic);
        }

        String bold = (String) lookupStyle("fw", xssfCellExportContext);
        if (!Strings.isNullOrEmpty(bold)) {
            fontProps.put("bold", bold);
        }

        int fontHashCode = fontProps.hashCode();
        if (fontHashCode != 0) {
            Map<Integer, XSSFFont> fonts = xssfCellExportContext.parentContext().parentContext().fonts();
            XSSFFont font = fonts.get(fontHashCode);
            if (font == null) {
                font = xssfCellExportContext.parentContext().parentContext().xssfWorkbook().createFont();
                if (!Strings.isNullOrEmpty(fontColor)) {
                    XSSFColor xssfColor = SpreadsheetExportHelper.colorAsXssfColor(fontColor);
                    if(xssfColor != null)
                    	font.setColor(xssfColor);
                }
                if (!Strings.isNullOrEmpty(fontName)) {
                    font.setFontName(fontName);
                }
                if (fontSize != null && fontSize != 0) {
                    font.setFontHeightInPoints(fontSize.shortValue());
                }
                if (!Strings.isNullOrEmpty(strikeout)) {
                    font.setStrikeout(true);
                }
                if (!Strings.isNullOrEmpty(underline)) {
                    font.setUnderline(Font.U_SINGLE);
                }
                if (!Strings.isNullOrEmpty(italic)) {
                    font.setItalic(true);
                }
                if (!Strings.isNullOrEmpty(bold)) {
                    font.setBold(true);
                }
                fonts.put(fontHashCode, font);

            }
            return font;

        } else {
            return null;
        }

    }
    
    private Object lookupStyle(String styleKey, XSSFCellExportContext xssfCellExportContext){
    	XSSFCellExportContext.SheetCellObjectType sheetCellObjectType = xssfCellExportContext.getSheetCellObjectType();
    	XSSFWorksheetExportContext xssfWorksheetExportContext = xssfCellExportContext.parentContext();
    	Map<String,Object> styleSettingMap = xssfCellExportContext.contentMap();
    	Object styleValue = null;
    	switch(sheetCellObjectType){
    		case sheet:{
    			styleValue = styleSettingMap.get(styleKey);
    			break;
    		}
    		case column: 			
    		case row:{
    			if(styleSettingMap.containsKey(styleKey)){
    				styleValue = styleSettingMap.get(styleKey);
    			}else{
    				styleValue = xssfWorksheetExportContext.getSheetStyleSetting().get(styleKey);
    			}
    			break;
    		}
    		case cell:{
    			if(styleSettingMap.containsKey(styleKey)){
    				styleValue = styleSettingMap.get(styleKey);
    			}else{
    				int rowIndex = xssfCellExportContext.sheetCell().getX();
    				Map<String,Object> rowStyleSetting = xssfWorksheetExportContext.getRowStyleSetting(rowIndex);
    				if(rowStyleSetting != null && rowStyleSetting.containsKey(styleKey)){
    					styleValue = rowStyleSetting.get(styleKey);
    				}else{
    					int columnIndex = xssfCellExportContext.sheetCell().getY();
    					Map<String,Object> columnStyleSetting = xssfWorksheetExportContext.getColumnStyleSetting(columnIndex);
    					if(columnStyleSetting != null && columnStyleSetting.containsKey(styleKey)){
    						styleValue = columnStyleSetting.get(styleKey);
    					}else{
    						styleValue = xssfWorksheetExportContext.getSheetStyleSetting().get(styleKey);
    					}
    				}
    			}
    			break;
    		}
    		
    	}
    	return styleValue;
    }


    public static class BorderStyleAndColor {

        public static final BorderStyleAndColor NONE = new BorderStyleAndColor(BorderStyle.NONE, null);

        public final BorderStyle _borderStyle;
        public final String _color;

        public BorderStyleAndColor(BorderStyle borderStyle, String color) {
            _borderStyle = borderStyle;
            _color = color;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof BorderStyleAndColor)) return false;
            BorderStyleAndColor other = (BorderStyleAndColor) o;
            return Objects.equals(_borderStyle, other._borderStyle) &&
                    Objects.equals(_color, other._color);
        }

        public int hashCode() {
            return Objects.hash(_borderStyle, _color);
        }

    }


    public static CellStyleHandler instance() {
        return _instance;
    }

}
