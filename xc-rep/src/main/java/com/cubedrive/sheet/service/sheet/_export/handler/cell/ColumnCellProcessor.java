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
package com.cubedrive.sheet.service.sheet._export.handler.cell;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.handler.CellStyleHandler;

public class ColumnCellProcessor extends CellProcessor{
	
	private ColumnCellProcessor(XSSFCellExportContext xssfCellExportContext){
		super(xssfCellExportContext);
	}
    
    public void process(){
    	SheetCell sheetCell = _xssfCellExportContext.sheetCell();
        Map<String,Object> contentMap = _xssfCellExportContext.contentMap();
        Integer width = (Integer)contentMap.remove("width");
        boolean hidden = _xssfCellExportContext.parentContext().isColHidden(sheetCell.getY());
        Short styleId = getStyleId();
        int colWidth = width == null ? 0: width; 
        XSSFWorksheetExportContext.ColumnSetting columnSetting = new XSSFWorksheetExportContext.ColumnSetting();
        columnSetting.setMin(sheetCell.getY());
        columnSetting.setMax(sheetCell.getY());
        columnSetting.setWidth(colWidth);
        columnSetting.setStyleId(styleId);
        columnSetting.setHidden(hidden);

        Map<Integer,XSSFWorksheetExportContext.ColumnSetting> cols = _xssfCellExportContext.parentContext().cols();
        int colIndex = sheetCell.getY();
        if(colIndex != 1){
            int lastColIndex = colIndex -1;
            XSSFWorksheetExportContext.ColumnSetting lastColumnSetting = cols.get(lastColIndex);
            if(lastColumnSetting!= null && lastColumnSetting.equals(columnSetting)){
                lastColumnSetting.setMax(colIndex);
                cols.remove(lastColIndex);
                cols.put(colIndex, lastColumnSetting);
                return ;
            }
        }
        if(width!= null || hidden || styleId!=null){
            cols.put(colIndex, columnSetting);
        }
        
        
    }
    
    
    private Short getStyleId(){
    	XSSFCellStyle cellStyle = CellStyleHandler.instance().getOrCreateCellStyle(_xssfCellExportContext);
    	if(cellStyle == null){
    		return null;
    	}else{
    		return cellStyle.getIndex();
    	}
    }
    
    
    
    public static ColumnCellProcessor build(XSSFCellExportContext xssfCellExportContext){
    	ColumnCellProcessor columnConfigCellProcessor = new ColumnCellProcessor(xssfCellExportContext);
    	return columnConfigCellProcessor;
    }

}
