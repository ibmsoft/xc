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

import org.apache.poi.xssf.streaming.SXSSFRow;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.handler.cell.CellProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.cell.ColumnCellProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.cell.ConfigCellProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.cell.DataCellProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.cell.RowCellProcessor;

public class CellExportHandler {
	
	private static final CellExportHandler _instance = new CellExportHandler();
	
	public void handleCell(XSSFCellExportContext xssfCellExportContext){
		
	    XSSFWorksheetExportContext xssfWorksheetExportContext = xssfCellExportContext.parentContext();
	    CellProcessor cellProcessor = getCellProcessor(xssfCellExportContext);
	    if(cellProcessor != null)
	    	cellProcessor.process();
	    xssfWorksheetExportContext.setLastSheetCell(xssfCellExportContext.sheetCell());
	}
	
	private CellProcessor getCellProcessor(XSSFCellExportContext xssfCellExportContext){
	    XSSFWorksheetExportContext xssfWorksheetExportContext = xssfCellExportContext.parentContext();
	    SheetCell lastSheetCell = xssfWorksheetExportContext.getLastSheetCell();
	    SheetCell sheetCell = xssfCellExportContext.sheetCell();
	    if((lastSheetCell == null && sheetCell.getX() !=0) || 
	            (lastSheetCell != null &&  sheetCell.getX() > lastSheetCell.getX())){
	        int lastRowIndex = lastSheetCell == null ? 1: lastSheetCell.getX()+1;
	        int currentRowIndex = sheetCell.getX();
	        SXSSFRow row = null;
	        for(int i=lastRowIndex; i<=currentRowIndex; i++){
	            row = (SXSSFRow)xssfWorksheetExportContext.sheet().createRow(i-1);
	            boolean hidden = xssfWorksheetExportContext.isRowHidden(i);
	            if(hidden){
	                row.setHidden(true);
	                xssfWorksheetExportContext.setRowHeight(i, 0);
	            }
	            if(xssfWorksheetExportContext.getRowGroupProcessor() != null){
	                xssfWorksheetExportContext.getRowGroupProcessor().setRowOutlineLevel(row, i);
	            }
	        }
	        xssfWorksheetExportContext.setCurrentRow(row);
	    }
	    
	    CellProcessor cellProcessor;
	    int x = sheetCell.getX(); //row index
	    int y = sheetCell.getY(); //column index
	    
	    Map<String,Object> contentMap = xssfCellExportContext.contentMap();
	    XSSFCellExportContext.SheetCellObjectType sheetObjectType;
	    if(x ==0 && y==0){
	    	ConfigCellProcessor configCellProcessor = ConfigCellProcessor.build(xssfCellExportContext);
	    	xssfWorksheetExportContext.setSheetStyleSetting(contentMap);
	    	xssfWorksheetExportContext.setConfigCellProcessor(configCellProcessor);
	    	sheetObjectType = XSSFCellExportContext.SheetCellObjectType.sheet;
	        cellProcessor = null;
	    }else if(x==0 && y>0){
	        cellProcessor = ColumnCellProcessor.build(xssfCellExportContext);
	        xssfWorksheetExportContext.putColumnStyleSetting(y, contentMap);
	        sheetObjectType = XSSFCellExportContext.SheetCellObjectType.column;
	    }else if(x>0 && y==0 ){
	        cellProcessor = RowCellProcessor.build(xssfCellExportContext);
	        xssfWorksheetExportContext.putRowStyleSetting(x, contentMap);
	        sheetObjectType = XSSFCellExportContext.SheetCellObjectType.row;
	    }else {
	        cellProcessor = DataCellProcessor.build(xssfCellExportContext);
	        sheetObjectType = XSSFCellExportContext.SheetCellObjectType.cell;
	    }
	    xssfCellExportContext.setSheetCellObjectType(sheetObjectType);
	    return cellProcessor;
	}
	
	
	
	
	
	public static CellExportHandler instance(){
		return _instance;
	}

	
	
	
}
