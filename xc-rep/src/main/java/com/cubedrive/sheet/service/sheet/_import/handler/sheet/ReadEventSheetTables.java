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
package com.cubedrive.sheet.service.sheet._import.handler.sheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;

import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;

public class ReadEventSheetTables{
    
    private final List<ReadEventSheetTable> _tables = new ArrayList<>();
    
    private ReadEventSheetTables(){
        
    }
    
    public ReadEventSheetTable getReadEventSheetTable(CellReference cellReference){
        for(ReadEventSheetTable table:_tables){
            if(table.isTableCell(cellReference)){
                return table;
            }
        }
        return null;
    }
    
    public static ReadEventSheetTables build(SheetImportContext sheetImportContext){
        ReadEventSheetTables readEventSheetTableFilter = new ReadEventSheetTables();
        Collection<XSSFTable> xssfTables =sheetImportContext.sheet().tables().values();
        for(XSSFTable xssfTable:xssfTables){
            ReadEventSheetTable readEventSheetTable = ReadEventSheetTable.build(xssfTable, sheetImportContext);
            readEventSheetTableFilter._tables.add(readEventSheetTable);
        }
        return readEventSheetTableFilter;
    }
    

    public static class ReadEventSheetTable {
    
//        private String _tpl;
    	private Map<String,Object> _tplMap;
        private CTTable _ctTable;
        private CellReference _startCellReference;
        private CellReference _endCellReference;
    
        private ReadEventSheetTable() {
    
        }
    
        private boolean isTableCell(CellReference cellReference) {
            if (_startCellReference.getRow() <= cellReference.getRow()
                    && _startCellReference.getCol() <= cellReference.getCol()
                    && _endCellReference.getRow() >= cellReference.getRow()
                    && _endCellReference.getCol() >= cellReference.getCol()){
                return true;
            }
            return false;
        }
        
        public boolean isTableHeader(CellReference cellReference){
        	if(_startCellReference.getRow() == cellReference.getRow()){
        		return true;
        	}else{
        		return false;
        	}
        }
        
        public CTTable ctTable(){
            return _ctTable;
        }
        
        public Map<String,Object> tpl(){
        	return _tplMap;
        }
        
        static ReadEventSheetTable build(XSSFTable xssfTable, SheetImportContext sheetImportContext){
            
            ReadEventSheetTable readEventSheetTable = new ReadEventSheetTable();
            CTTable ctTable = xssfTable.getCTTable();
            String styleName = ctTable.getTableStyleInfo().getName();
            String id = "tpl_" + styleIdByName(styleName);
            CellReference startCellReference = xssfTable.getStartCellReference();
            CellReference endCellReference = xssfTable.getEndCellReference();
            int[] span = new int[]{ sheetImportContext.getTabId(),
                    startCellReference.getRow()+1, startCellReference.getCol()+1,
                    endCellReference.getRow()+1,endCellReference.getCol()+1 };
            Map<String,Object> setting = new HashMap<>();
            setting.put("id", id);
            setting.put("span", span);
            readEventSheetTable._tplMap = Collections.unmodifiableMap(setting);
            readEventSheetTable._ctTable = ctTable;
            readEventSheetTable._startCellReference = startCellReference;
            readEventSheetTable._endCellReference = endCellReference;
            return readEventSheetTable;
        }
        
        private static int styleIdByName(String styleName){
        	int styleId = 0;
	        if(styleName != null && styleName.length()>0){
	        	if(styleName.startsWith("TableStyleLight")){
	        		styleId = Integer.parseInt(styleName.substring("TableStyleLight".length())) - 1;
	        	}else if(styleName.startsWith("TableStyleMedium")){
	        		styleId = Integer.parseInt(styleName.substring("TableStyleMedium".length())) + 20;
	        	}else if(styleName.startsWith("TableStyleDark")){
	        		styleId = Integer.parseInt(styleName.substring("TableStyleDark".length())) + 48;
	        	}
        	}
        	return styleId;
        	
        }
    }
    

}
