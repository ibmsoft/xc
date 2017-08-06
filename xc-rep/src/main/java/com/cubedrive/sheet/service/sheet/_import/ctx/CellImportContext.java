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


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._import.SheetImportEnvironment;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventCell;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventWorkbook;

public class CellImportContext extends SSImportContext {
    
    public final static int CELL_TYPE_NUMERIC = 0;

    public final static int CELL_TYPE_STRING = 1;

    public final static int CELL_TYPE_FORMULA = 2;

    public final static int CELL_TYPE_BLANK = 3;

    public final static int CELL_TYPE_BOOLEAN = 4;

    public final static int CELL_TYPE_ERROR = 5;

    private final RowImportContext _rowImportContext;

    private final XSSFReadEventCell _cell;
    
    private SheetCell _sheetCell;
    
    private int _cellType = -1;
    
    private Object _data;
    
    private Object _rawData;
    
    private Map<String,Object> _styleSettings = new HashMap<>();
    
//    private boolean _skipped = false;

    public CellImportContext(SheetImportEnvironment sheetImportEnvironment,
                             XSSFReadEventCell cell,
                             int rowIndex,
                             int cellIndex,
                             RowImportContext rowImportContext) {
        super(sheetImportEnvironment);
        this._cell = cell;
        this._rowImportContext = rowImportContext;
        CellReference cellRef;
        if(cell.reference() !=null && cell.reference().length()>0){
        	cellRef= new CellReference(cell.reference());
        }else{
        	cellRef = new CellReference(rowIndex-1, cellIndex-1, true,true);
        }
        cell.setCellReference(cellRef);
    }


    public XSSFReadEventCell cell() {
        return _cell;
    }


    @Override
    @SuppressWarnings("unchecked")
    public RowImportContext parentContext() {
        return _rowImportContext;
    }
    
    public WorkbookImportContext workbookImportContext(){
    	return _rowImportContext.parentContext().parentContext();
    }

    public CellReference cellReference() {
        return _cell.getCellReference();
    }

    public void preHandleCell(XSSFReadEventCell cell) {
        parentContext().preHandleCell(cell);
    }

    public void postHandleCell(XSSFReadEventCell cell) {
        parentContext().postHandleCell(cell);
    }

    public void setSheetCell(SheetCell sheetCell){
    	_sheetCell = sheetCell;
    }
    
    public SheetCell getSheetCell(){
    	return _sheetCell;
    }
    
    @Override
    public void dispose() {
        _data = null;
        _rawData = null;
        _styleSettings.clear();
        super.dispose();
    }

    public XSSFCellStyle getCellStyle() {
        SheetImportContext sheetImportContext = _rowImportContext.parentContext();
        WorkbookImportContext workbookImportContext = sheetImportContext.parentContext();
        XSSFReadEventWorkbook workbook = workbookImportContext.workbook();
        int idx = _cell.styleIndex();
        XSSFCellStyle style = workbook.stylesSource().getStyleAt(idx);
        return style;
    }

    public DataFormatter getDataFormatter() {
        SheetImportContext sheetImportContext = _rowImportContext.parentContext();
        WorkbookImportContext workbookImportContext = sheetImportContext.parentContext();
        return workbookImportContext.dataFormatter();
    }

    public Locale getLocale() {
        return _sheetImportEnvironment.locale();
    }

    public int getCellType(){
        if(_cellType == -1){
            _cellType = doGetCellType();
        }
        return _cellType;
    }
    
    private int doGetCellType() {
        SheetImportContext sheetContext = _rowImportContext.parentContext();
        if (_cell.formula() != null || sheetContext.getArrayFormulaRangeAddress(_cell.getCellReference()) != null) {
            return CELL_TYPE_FORMULA;
        }

        switch (_cell.dataType()) {
            case "b":
                return CELL_TYPE_BOOLEAN;
            case "n":
                if (_cell.val() == null) {
                    // ooxml does have a separate cell type of 'blank'.  A blank cell gets encoded as
                    // (either not present or) a numeric cell with no value set.
                    // The formula evaluator (and perhaps other clients of this interface) needs to
                    // distinguish blank values which sometimes get translated into zero and sometimes
                    // empty string, depending on context
                    return CELL_TYPE_BLANK;
                }
                return CELL_TYPE_NUMERIC;
            case "e":
                return CELL_TYPE_ERROR;
            case "s": // String is in shared strings
            case "str": // String is inline in cell
            case "inlineStr":
                return CELL_TYPE_STRING;
            default:
                throw new IllegalStateException("Illegal cell type: " + _cell.dataType());
        }
    }


    public Object getData() {
        return _data;
    }


    public void setData(Object data) {
        this._data = data;
    }


    public Object getRawData() {
        return _rawData;
    }


    public void setRawData(Object rawData) {
        this._rawData = rawData;
    }


    public Map<String, Object> getStyleSettings() {
        return _styleSettings;
    }


    

}
