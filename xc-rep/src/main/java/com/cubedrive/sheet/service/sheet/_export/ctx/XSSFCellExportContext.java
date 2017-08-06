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
package com.cubedrive.sheet.service.sheet._export.ctx;

import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._export.SheetExportEnvironment;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper.DataType;

public class XSSFCellExportContext extends SSExportContext{
	
	public enum SheetCellObjectType{
		sheet,row,column,cell
	}
	
	private final XSSFWorksheetExportContext _worksheetExportContext;
	
	private final SheetCell _sheetCell;
	
	private final Map<String,Object> _contentMap;
	
	private SXSSFCell _excelCell;
	
	private DataType _dataType;
	
	private String _data;
	
	private SheetCellObjectType _sheetCellObjectType;
	
	
	public XSSFCellExportContext(SheetExportEnvironment sheetExportEnvironment,
			XSSFWorksheetExportContext worksheetExportContext,
			SheetCell sheetCell,Map<String,Object> contentMap){
		super(sheetExportEnvironment);
		_worksheetExportContext = worksheetExportContext;
		_sheetCell = sheetCell;
		_contentMap = contentMap;
	}

	@Override
	public XSSFWorksheetExportContext parentContext() {
		return _worksheetExportContext;
	}
	
	public SheetCell sheetCell(){
		return _sheetCell;
	}
	
	public Map<String,Object> contentMap(){
		return _contentMap;
	}
	
	public SXSSFCell getExcelCell(){
		if(_excelCell == null){
			SXSSFRow row  = _worksheetExportContext.getCurrentRow();
			_excelCell =(SXSSFCell)row.createCell(_sheetCell.getY() -1);
		}
		return _excelCell;
	}
	
	public void setDataType(DataType dataType){
	   _dataType = dataType;
	}
	
	public DataType getDataType(){
	    return _dataType;
	}
	
	public void setData(String data){
	    _data = data;
	}
	
	public String getData(){
	    return _data;
	}

	public Object getFormulaValue(){
		return (Object)_contentMap.get("value");
	}
	
	public void setSheetCellObjectType(SheetCellObjectType sheetCellObjectType){
		_sheetCellObjectType = sheetCellObjectType;
	}
	
	public SheetCellObjectType getSheetCellObjectType(){
		return _sheetCellObjectType;
	}
}
