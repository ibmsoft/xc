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
package com.cubedrive.sheet.service.sheet._import.ctx;


import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.cubedrive.sheet.service.sheet._import.SheetImportEnvironment;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventCell;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventRow;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventWorkbook;

public class RowImportContext extends SSImportContext {

    private final SheetImportContext _sheetImportContext;

    private final XSSFReadEventRow _row;
    
    private final int _rowIndex;
    
    private int _cellCountOfRow = 0;
    
    private int _cellIndex = 0;

    public RowImportContext(SheetImportEnvironment sheetImportEnvironment,
                            XSSFReadEventRow row, int rowIndex,
                            SheetImportContext sheetImportContext) {
        super(sheetImportEnvironment);
        this._row = row;
        this._rowIndex = rowIndex;
        this._sheetImportContext = sheetImportContext;
    }

    public XSSFReadEventRow row() {
        return _row;
    }
    
    public XSSFCellStyle getCellStyle() {
        WorkbookImportContext workbookImportContext = _sheetImportContext.parentContext();
        XSSFReadEventWorkbook workbook = workbookImportContext.workbook();
        Integer idx = _row.styleIndex();
        XSSFCellStyle style = null;
        if(idx != null){
        	style = workbook.stylesSource().getStyleAt(idx);
        }
        return style;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SheetImportContext parentContext() {
        return _sheetImportContext;
    }
    
    public WorkbookImportContext workbookImportContext(){
    	return _sheetImportContext.parentContext();
    }

    public void increCellCountOfRow(){
    	_cellCountOfRow++;
    }
    
    public int getCellCountOfRow(){
    	return _cellCountOfRow;
    }
    
    public int getCellIndex(){
    	return _cellIndex;
    }
    
    public void setCellIndex(int cellIndex){
    	this._cellIndex = cellIndex;
    }
    
    public void preHandleCell(XSSFReadEventCell cell) {
        parentContext().preHandleCell(cell);
    }

    public void postHandleCell(XSSFReadEventCell cell) {
        parentContext().postHandleCell(cell);
    }

    public int getRowIndex(){
    	return _rowIndex;
    }
    

    @Override
    public void dispose() {
        super.dispose();

    }
}
