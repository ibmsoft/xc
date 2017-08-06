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
package com.cubedrive.sheet.service.sheet._export.handler.sheet;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext.TableInfo;

public class TableProcessor extends WorksheetProcessor{

	private final TableInfo _tableInfo;
	
	private final int _workbookTableIndex;
	
	private final int _sheetTableIndex;
	
	private TableProcessor(TableInfo tableInfo,int workbookTableIndex, int sheetTableIndex,XSSFWorksheetExportContext xssfWorksheetExportContext) {
		super(xssfWorksheetExportContext);
		_tableInfo = tableInfo;
		_workbookTableIndex = workbookTableIndex;
		_sheetTableIndex = sheetTableIndex;
		
	}

	@Override
	public void process() {
		String ref = SpreadsheetExportHelper.spanToSqref(_tableInfo.getSpan(), false);
		XSSFSheet xssfSheet = _xssfWorksheetExportContext.sheet().getXSSFSheet();
		XSSFTable xssfTable = xssfSheet.createTable();
		CTTable ctTable = xssfTable.getCTTable();
		ctTable.setDisplayName("Table"+ _sheetTableIndex+"_" + _xssfWorksheetExportContext.parentContext().xssfWorkbook().getSheetIndex(xssfSheet));
		ctTable.setId(_workbookTableIndex);
		xssfTable.setName("table"+ _sheetTableIndex);
		ctTable.setRef(ref);
		ctTable.setTotalsRowShown(false);
		
		ctTable.addNewAutoFilter().setRef(ref);
		CTTableColumns tableColumns = ctTable.getTableColumns();
		if(tableColumns == null)
		    tableColumns = ctTable.addNewTableColumns();
		
		String tplId = _tableInfo.getId();
		String[] headers = _tableInfo.getHeaders();
		tableColumns.setCount(headers.length);
		for(int i=0;i<headers.length;i++){
		    CTTableColumn tableColumn = tableColumns.addNewTableColumn();
		    tableColumn.setId(i+1);
		    tableColumn.setName(headers[i]);
		}
		CTTableStyleInfo tableStyleInfo;
		if(ctTable.isSetTableStyleInfo()){
		    tableStyleInfo= ctTable.getTableStyleInfo();
		}else{
		    tableStyleInfo = ctTable.addNewTableStyleInfo();
		}
		
		String styleName = getStyle(tplId);
		if(styleName != null)
			tableStyleInfo.setName(styleName);
		tableStyleInfo.setShowColumnStripes(false);
		tableStyleInfo.setShowFirstColumn(false);
		tableStyleInfo.setShowLastColumn(false);
		tableStyleInfo.setShowRowStripes(true);
	}
	
	private String getStyle(String tplId){
		String styleName = null;
		try{
			int id = Integer.parseInt(tplId.split("_", 2)[1]);
			if(id == 0){
				styleName = null;
			}else if(id <= 20){
				styleName = "TableStyleLight" + (id+1);
			}else if(id <= 48){
				styleName = "TableStyleMedium" + (id-20);
			}else if(id <= 59){
				styleName = "TableStyleDark" + (id-48);
			}
		}catch(Exception ex){
			
		}
//		if(styleName == null){
//			styleName = "TableStyleMedium9";
//		}
		return styleName;
	}
		
	public static TableProcessor build(TableInfo tableInfo,int workbookTableIndex, int sheetTableIndex, XSSFWorksheetExportContext xssfWorksheetExportContext){
		TableProcessor tableProcessor = new TableProcessor(tableInfo,workbookTableIndex, sheetTableIndex,xssfWorksheetExportContext);
		return tableProcessor;
	}
	
	
	

	

}
