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
package com.cubedrive.sheet.service.sheet._import.handler;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;

import com.cubedrive.base.utils.DoSomethingUtil;
import com.cubedrive.base.utils.DoSomethingUtil.OnException;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.RowImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;
import com.cubedrive.sheet.service.sheet._import.handler.cell.ReadEventCellStyle;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventRow;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;

public class RowImportHandler {
    
    private static final RowImportHandler _instance = new RowImportHandler();
    
    private RowImportHandler(){
        
    }

    public void startRow(final RowImportContext rowImportContext) {
    	
        DoSomethingUtil.doSomething( 
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    setRowHeight(rowImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            },
            
            new DoSomethingUtil.DoSomething() {
				
				@Override
				public void doIt() {
					registerRowGroup(rowImportContext);
				}
				
				@Override
				public OnException onException() {
					return DoSomethingUtil.OnException.ignore;
				}
			}
         );

    }

    public void endRow(RowImportContext rowImportContext) {

    }
    
    
    private void setRowHeight(RowImportContext rowImportContext){
        XSSFReadEventRow readEventRow = rowImportContext.row();
        SheetImportContext sheetImportContext = rowImportContext.parentContext();
        WorkbookImportContext workbookImportContext = sheetImportContext.parentContext();
        CTSheet ctSheet = sheetImportContext.sheet().sheet();
        SheetTab sheetTab = workbookImportContext.getSheetTabById((int) ctSheet.getSheetId());
        XSSFCellStyle rowCellStyle = rowImportContext.getCellStyle();
        Map<String,Object> rowSettings = new HashMap<>();
        if(rowCellStyle != null){
	        ReadEventCellStyle readEventCellStyle = ReadEventCellStyle.build(rowImportContext.getCellStyle(), -1, rowImportContext);
			Map<String, Object> styleSettings = readEventCellStyle.getStyles();
			if(!styleSettings.isEmpty()){
	        	rowSettings.putAll(styleSettings);
	        	String dsd = (String)styleSettings.get("dsd");
	        	if("".equals(dsd)){
	        		sheetImportContext.addNoProtectedRow(readEventRow.rowIndex());
	        	}
	        }
        }
        
        int height =  (int)(readEventRow.rowHeight()*4/3);
        if(height != SpreadsheetImportHelper.default_row_height_px){
            rowSettings.put("height", height);
            rowSettings.put("hoff", 0);
        }
//        if(readEventRow.hidden() && readEventRow.outlineLevel()==null){
//            rowSettings.put("hidden",true);
//        }

        if(readEventRow.hidden()){
            sheetImportContext.addHiddenRow(rowImportContext.getRowIndex());
        }

        if(!rowSettings.isEmpty()){
//            Integer styleId = workbookImportContext.getWorkbookStylesTable().getOrCreateStyle(rowSettings);
            Map<String,Object> contentMap = new HashMap<>();
//            contentMap.put("style", styleId);
            contentMap.putAll(rowSettings);
            
            
            
            
            SheetCell sheetCell = new SheetCell();
            sheetCell.setX(rowImportContext.getRowIndex());
            sheetCell.setY(0);
            sheetCell.setContent(JsonUtil.toJson(contentMap));
	        /**
	         * start
	         * added by zhousr 2016-2-19 [添加报表3.0新代码]为SheetCell增加3个属性赋值FORMAT_ID，CREATION_DATE，CREATED_BY
	         */
			sheetCell.setFORMAT_ID(UUID.randomUUID().toString());
			sheetCell.setCREATION_DATE(sheetTab.getDocumentFile().getCREATION_DATE());
			sheetCell.setCREATED_BY(sheetTab.getDocumentFile().getCREATED_BY());
	        /**
	         * end 
	         * added by zhousr 2016-2-19 
	         */
            sheetImportContext.addSheetRow(sheetCell);
        }
        
    }
    
    private void registerRowGroup(RowImportContext rowImportContext){
    	XSSFReadEventRow readEventRow = rowImportContext.row();
    	rowImportContext.parentContext().getReadEventSheetRowGroup().register(readEventRow);
    }
    
    public static RowImportHandler instance(){
        return _instance;
    }
}
