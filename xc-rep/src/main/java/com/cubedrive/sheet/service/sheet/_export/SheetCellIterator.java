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
package com.cubedrive.sheet.service.sheet._export;


import java.util.Iterator;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;

class SheetCellIterator implements Iterator<SheetCell> {

    private final SheetTab _sheetTab;

    private final SheetCellMapper _sheetCellMapper;

    private final int _limit;
    
    private int _offset = 0;
    
    private Iterator<SheetCell> _iterator;

    

    SheetCellIterator(SheetTab sheetTab, SheetCellMapper sheetCellMapper, int limit) {
        _sheetTab = sheetTab;
        _sheetCellMapper = sheetCellMapper;
        _limit = limit;
        _iterator = _sheetCellMapper.findCellsByTabOnExport(_sheetTab.getId(),
        		_sheetTab.getCellTable(), _offset, _limit).iterator();
    }


    @Override
    public boolean hasNext() {
    	boolean _hasNext = _iterator.hasNext();
        if (!_hasNext) {
            _offset += 50;
            _iterator = _sheetCellMapper.findCellsByTabOnExport(_sheetTab.getId(),
            		_sheetTab.getCellTable(), _offset, _limit).iterator();
            _hasNext = _iterator.hasNext();
        }
        return _hasNext;
    }

    @Override
    public SheetCell next() {
    	SheetCell sheetCell = _iterator.next();
    	return sheetCell;
    }

    @Override
    public void remove() {
        _iterator.remove();
    }
}
