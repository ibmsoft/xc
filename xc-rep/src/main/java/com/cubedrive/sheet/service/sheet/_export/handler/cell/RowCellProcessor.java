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

import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.handler.CellStyleHandler;

public class RowCellProcessor extends CellProcessor {
    
   
	
	private RowCellProcessor(XSSFCellExportContext xssfCellExportContext){
		super(xssfCellExportContext);
	}
    
    public void process(){
        SheetCell sheetCell = _xssfCellExportContext.sheetCell();
    	Map<String,Object> contentMap = _xssfCellExportContext.contentMap();
    	XSSFWorksheetExportContext sheetExportContext = _xssfCellExportContext.parentContext();
        SXSSFRow row = sheetExportContext.getCurrentRow();
        Integer height = (Integer)contentMap.remove("height");
        if(height != null){
            sheetExportContext.setRowHeight(sheetCell.getX(), height);
            row.setHeight((short)((height * 3.0 /4.0) * 20)); //change px to excel unit
        }
        boolean hidden = sheetExportContext.isRowHidden(sheetCell.getX());
        if(hidden){
            row.setHidden(true);
            sheetExportContext.setRowHeight(sheetCell.getX(), 0);
        }
        XSSFCellStyle cellStyle = getCellStyle();
        if(cellStyle != null)
        	row.setRowStyle(cellStyle);
    }
    
    private XSSFCellStyle getCellStyle(){
    	XSSFCellStyle cellStyle = CellStyleHandler.instance().getOrCreateCellStyle(_xssfCellExportContext);
    	return cellStyle;
    }
    
    
    public static RowCellProcessor build(XSSFCellExportContext xssfCellExportContext){
    	RowCellProcessor rowConfigCellProcessor = new RowCellProcessor(xssfCellExportContext);
    	return rowConfigCellProcessor;
    }

}
