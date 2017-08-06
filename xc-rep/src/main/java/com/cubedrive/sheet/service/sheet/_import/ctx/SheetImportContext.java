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


import java.util.*;

import com.cubedrive.base.utils.Pair;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;

import com.cubedrive.sheet.SheetAppConstants;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._import.SheetImportEnvironment;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetAutoFilter;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetControl;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetRowGroup;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ReadEventSheetTables;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventCell;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventFormula;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;

public class SheetImportContext extends SSImportContext {

    private final WorkbookImportContext _workbookImportContext;

    private final XSSFReadEventSheet _sheet;

    private final Map<CellRangeAddress, XSSFReadEventFormula> _arrayFormulas = new HashMap<>();

    private final Map<Integer, XSSFReadEventFormula> _sharedFormulas = new HashMap<>();
    
    private final List<SheetCell> _sheetRows = new ArrayList<>(SheetAppConstants.SHEET_CELL_CACHE_SIZE*2);
    
    private final List<SheetCell> _sheetCells = new ArrayList<>(SheetAppConstants.SHEET_CELL_CACHE_SIZE*2);

    private final Integer _tabId;
    
    private ReadEventSheetTables _readEventSheetTables;
    
    private ReadEventSheetRowGroup _readEventSheetRowGroup;
    
    private ReadEventSheetAutoFilter _readEventSheetAutoFilter;
    
    private ReadEventSheetControl _readEventSheetControl;

    private boolean _sheetProtection = false;
    
    private Set<Integer> _noProtectedRows = new HashSet<>();
    
    private Set<Integer> _noProtectedColumns = new HashSet<>();
    
    private int _rowIndex=0;
    
    private Map<String,Object> _sheetStyleSetting = null;

    private final HiddenRanges _rowHiddenRanges = new HiddenRanges();

    private final HiddenRanges _columnHiddenRanges = new HiddenRanges();

    
    public SheetImportContext(SheetImportEnvironment sheetImportEnvironment,
                              XSSFReadEventSheet sheet,
                              WorkbookImportContext workbookImportContext) {
        super(sheetImportEnvironment);
        this._sheet = sheet;
        this._workbookImportContext = workbookImportContext;
        this._tabId = workbookImportContext.getSheetTabById((int) _sheet.sheet().getSheetId()).getId();
        this._readEventSheetRowGroup = ReadEventSheetRowGroup.build(this);
    }

    public XSSFReadEventSheet sheet() {
        return _sheet;
    }

    
    @Override
    @SuppressWarnings("unchecked")
    public WorkbookImportContext parentContext() {
        return _workbookImportContext;
    }

    public WorkbookImportContext workbookImportContext(){
    	return _workbookImportContext;
    }
    
    public void preHandleCell(XSSFReadEventCell cell) {
        XSSFReadEventFormula formula = cell.formula();
        CellReference cellRef = cell.getCellReference();
        if (formula != null) {
            if (formula.reference() != null) {
                CellRangeAddress formulaRef = CellRangeAddress.valueOf(formula.reference());
                if ("shared".equals(formula.formulaType()) && formula.val() != null) {
                    XSSFReadEventFormula copyFormula;
                    if (cellRef.getCol() > formulaRef.getFirstColumn() || cellRef.getRow() > formulaRef.getFirstRow()) {
                        String effectiveRef = new CellRangeAddress(
                                Math.max(cellRef.getRow(), formulaRef.getFirstRow()), formulaRef.getLastRow(),
                                Math.max(cellRef.getCol(), formulaRef.getFirstColumn()), formulaRef.getLastColumn()).formatAsString();
                        copyFormula = formula.copyWithReference(effectiveRef);
                    } else {
                        copyFormula = formula.copy();
                    }
                    _sharedFormulas.put(formula.sharedGroupIndex(), copyFormula);
                } else if ("array".equals(formula.formulaType())) {
                    _arrayFormulas.put(formulaRef, null);
                }
            }
            for (CellRangeAddress range : _arrayFormulas.keySet()) {
                if (cellRef.getRow() == range.getFirstRow() && cellRef.getCol() == range.getFirstColumn()) {
                    _arrayFormulas.put(range, formula);
                    break;
                }
            }

        }
        _workbookImportContext.preHandleCell(cell);
    }

    public void postHandleCell(XSSFReadEventCell cell) {
        _workbookImportContext.postHandleCell(cell);
    }


    public CellRangeAddress getArrayFormulaRangeAddress(CellReference cellReference) {
        for (CellRangeAddress range : _arrayFormulas.keySet()) {
            if (range.isInRange(cellReference.getRow(), cellReference.getCol())) {
                return range;
            }
        }
        return null;
    }

    public XSSFReadEventFormula getArrayFormula(CellRangeAddress cellRangeAddress) {
        return _arrayFormulas.get(cellRangeAddress);
    }

    public XSSFReadEventFormula getSharedFormula(Integer si) {
        return _sharedFormulas.get(si);
    }

    public int getTabId(){
        return _tabId.intValue();
    }
    
    public void addSheetCell(SheetCell sheetCell) {
        sheetCell.setTabId(_tabId);
        _sheetCells.add(sheetCell);
        if (_sheetCells.size() == SheetAppConstants.SHEET_CELL_CACHE_SIZE) {
        	sheetImportEnvironment().sheetCellMapper().batchInsertSheetCell2(_sheetCells, sheetImportEnvironment().cellTable());
        	_sheetCells.clear();
        }
    }
    
    public void addSheetRow(SheetCell rowCell){
    	rowCell.setTabId(_tabId);
    	_sheetRows.add(rowCell);
    	if(_sheetRows.size() == SheetAppConstants.SHEET_CELL_CACHE_SIZE){
    		sheetImportEnvironment().sheetCellMapper().batchInsertSheetCell2(_sheetRows, sheetImportEnvironment().cellTable());
    		_sheetRows.clear();
    	}
    }
    

    public XSSFHyperlink getHyperlink(String cellRef) {
        for(XSSFHyperlink hyperlink : _sheet.hyperlinks()) {
            if(hyperlink.getCellRef().equals(cellRef)) {
                return hyperlink;
            }
        }
        return null;
    }
    
    public void setReadEventSheetTables(ReadEventSheetTables readEventSheetTables){
        _readEventSheetTables = readEventSheetTables;
    }
    
    public ReadEventSheetTables getReadEventSheetTables(){
        return _readEventSheetTables;
    }
    
    public ReadEventSheetRowGroup getReadEventSheetRowGroup(){
    	return _readEventSheetRowGroup;
    }

    public ReadEventSheetAutoFilter getReadEventSheetAutoFilter(){
    	return _readEventSheetAutoFilter;
    }
    
    public void setReadEventSheetAutoFilter(ReadEventSheetAutoFilter readEventSheetAutoFilter){
    	_readEventSheetAutoFilter = readEventSheetAutoFilter;
    }
    
    public ReadEventSheetControl getReadEventSheetControl(){
        return _readEventSheetControl;
    }
    
    public void setReadEventSheetControl(ReadEventSheetControl readEventSheetControl){
        _readEventSheetControl = readEventSheetControl;
    }
    
    
    public void setSheetProtection(boolean sheetProtection){
    	this._sheetProtection = sheetProtection;
    }
    
    public boolean isSheetProtection(){
    	return _sheetProtection;
    }
    
    public void addNoProtectedRow(int rowIndex){
    	_noProtectedRows.add(rowIndex);
    }
    
    public boolean isNoProtectdRow(int rowIndex){
    	return _noProtectedRows.contains(rowIndex);
    }
    
    public void addNoProtectedColumn(int columnIndex){
    	_noProtectedColumns.add(columnIndex);
    }
    
    public boolean isNoProtectedColumn(int columnIndex){
    	return _noProtectedColumns.contains(columnIndex);
    }
    
    public int getRowIndex(){
    	return _rowIndex;
    }
    
    public void setRowIndex(int rowIndex){
    	this._rowIndex = rowIndex;
    }
    
    
    public Map<String,Object> getSheetStyleSetting() {
		return _sheetStyleSetting;
	}

	public void setSheetStyleSetting(Map<String,Object> sheetStyleSetting) {
		this._sheetStyleSetting = sheetStyleSetting;
	}

    public void addHiddenRow(int rowIndex){
        _rowHiddenRanges.addIndex(rowIndex);
    }

    public void addHiddenColumns(int columnIndexStart, int columnIndexEnd){
        _columnHiddenRanges.addRange(columnIndexStart, columnIndexEnd);
    }

    public List<Pair<Integer,Integer>> getRowHiddenRanges(){
        return _rowHiddenRanges.getRanges();
    }

    public List<Pair<Integer,Integer>> getColumnHiddenRanges(){
        return _columnHiddenRanges.getRanges();
    }

    private static class HiddenRanges{

        private List<Pair<Integer, Integer>> _ranges = new ArrayList<>();
        private int _currentRangeStart = -1;
        private int _currentRangeEnd = -1;

        private void addIndex(int index){
            if(_currentRangeEnd == -1){
                _currentRangeStart = _currentRangeEnd = index;
            }else{
                if(_currentRangeEnd == index -1){
                    _currentRangeEnd = index;
                }else{
                    _ranges.add(new Pair<Integer, Integer>(_currentRangeStart, _currentRangeEnd));
                    _currentRangeStart = _currentRangeEnd = index;
                }
            }
        }

        private void addRange(int start, int end){
            _ranges.add(new Pair<Integer, Integer>(start, end));
        }

        private List<Pair<Integer, Integer>> getRanges(){
            List<Pair<Integer, Integer>> ranges = new ArrayList<>(_ranges);
            if(_currentRangeStart != -1){
                ranges.add(new Pair<Integer, Integer>(_currentRangeStart, _currentRangeEnd));
            }
            return ranges;
        }

    }
	
	
	@Override
    public void dispose() {
        if (!_sheetCells.isEmpty()) {
            sheetImportEnvironment().sheetCellMapper().batchInsertSheetCell2(_sheetCells, sheetImportEnvironment().cellTable());
            _sheetCells.clear();
        }
        if(!_sheetRows.isEmpty()){
    		sheetImportEnvironment().sheetCellMapper().batchInsertSheetCell2(_sheetRows, sheetImportEnvironment().cellTable());
    		_sheetRows.clear();
    	}
        
        
        _arrayFormulas.clear();
        _sharedFormulas.clear();
        super.dispose();

    }

}
