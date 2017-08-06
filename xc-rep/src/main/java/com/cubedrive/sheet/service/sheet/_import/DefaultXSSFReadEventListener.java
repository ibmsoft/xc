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
package com.cubedrive.sheet.service.sheet._import;

import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.RowImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;
import com.cubedrive.sheet.service.sheet._import.handler.CellImportHandler;
import com.cubedrive.sheet.service.sheet._import.handler.RowImportHandler;
import com.cubedrive.sheet.service.sheet._import.handler.SheetImportHandler;
import com.cubedrive.sheet.service.sheet._import.handler.WorkbookImportHandler;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventCell;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventListener;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventRow;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventWorkbook;

public class DefaultXSSFReadEventListener implements XSSFReadEventListener {

    public final SheetImportEnvironment sheetImportEnvironment;

    private WorkbookImportContext workbookImportContext;
    private SheetImportContext sheetImportContext;
    private RowImportContext rowImportContext;
    private CellImportContext cellImportContext;


    public DefaultXSSFReadEventListener(SheetImportEnvironment sheetImportEnvironment) {
        this.sheetImportEnvironment = sheetImportEnvironment;
    }


    @Override
    public void onStartWorkbook(XSSFReadEventWorkbook workbook) {
        workbookImportContext = new WorkbookImportContext(sheetImportEnvironment, workbook);
        WorkbookImportHandler.instance().startWorkbook(workbookImportContext);


    }

    @Override
    public void onEndWorkbook(XSSFReadEventWorkbook workbook) {
        WorkbookImportHandler.instance().endWorkbook(workbookImportContext);
        workbookImportContext.dispose();
        workbookImportContext = null;

    }

    @Override
    public void onStartSheet(XSSFReadEventSheet sheet) {
        sheetImportContext = new SheetImportContext(sheetImportEnvironment, sheet, workbookImportContext);
        SheetImportHandler.instance().startSheet(sheetImportContext);
    }

    @Override
    public void onEndSheet(XSSFReadEventSheet sheet) {
        SheetImportHandler.instance().endSheet(sheetImportContext);
        sheetImportContext.dispose();
        sheetImportContext = null;

    }

    @Override
    public void onStartRow(XSSFReadEventRow row) {
    	int rowIndex;
    	if(row.rowIndex() != null){
    		rowIndex =row.rowIndex().intValue();
    	}else{
    		rowIndex = sheetImportContext.getRowIndex() + 1;
    	}
        rowImportContext = new RowImportContext(sheetImportEnvironment, row, rowIndex, sheetImportContext);
        RowImportHandler.instance().startRow(rowImportContext);

    }

    @Override
    public void onEndRow(XSSFReadEventRow row) {
        RowImportHandler.instance().endRow(rowImportContext);
        sheetImportContext.setRowIndex(rowImportContext.getRowIndex());
        rowImportContext.dispose();
        rowImportContext = null;
    }

    @Override
    public void onStartCell(XSSFReadEventCell cell) {
    	int rowIndex = rowImportContext.getRowIndex();
    	int columnIndex = rowImportContext.getCellIndex() + 1;
        cellImportContext = new CellImportContext(sheetImportEnvironment, cell, rowIndex, columnIndex,rowImportContext);
        cellImportContext.preHandleCell(cell);
        CellImportHandler.instance().startCell(cellImportContext);
        rowImportContext.increCellCountOfRow();

    }

    @Override
    public void onEndCell(XSSFReadEventCell cell) {
    	rowImportContext.setCellIndex(cell.getCellReference().getCol()+1);
		CellImportHandler.instance().endCell(cellImportContext);
		cellImportContext.postHandleCell(cell);
        cellImportContext.dispose();
        cellImportContext = null;
    }
}
