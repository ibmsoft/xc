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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;

import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.service.sheet._import.SheetImportEnvironment;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventWorkbook;


public class WorkbookImportContext extends SSImportContext {

    private static final String sheet_tab_id_prefix = "sheettab_id-";

    private static final String sheet_tab_name_prefix = "sheettab_name-";
    
    private static final String sheet_tab_order_prefix = "sheettab_order-";

    private final XSSFReadEventWorkbook _workbook;

    private final Collection<String> _definedNames = new HashSet<>();
    
    private final DataFormatter _dataFormatter;
    
    private boolean _date1904 = false;
    
    private final StyleSettingsCache _styleSettingsCache = new StyleSettingsCache();
    
    private final WorkbookStylesTable _workbookStylesTable;
    
    public WorkbookImportContext(SheetImportEnvironment environment, XSSFReadEventWorkbook workbook) {
        super(environment);
        _workbook = workbook;
        _dataFormatter = new DataFormatter(environment.locale());
        _workbookStylesTable = new WorkbookStylesTable(environment);
    }

    public XSSFReadEventWorkbook workbook() {
        return this._workbook;
    }

    public DataFormatter dataFormatter(){
        return this._dataFormatter;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public WorkbookImportContext parentContext() {
        return null;
    }

    public WorkbookImportContext workbookImportContext(){
    	return this;
    }
    
    @Override
    public void dispose() {
    	_styleSettingsCache.clear();
//    	_workbookStylesTable.flushAll();
        super.dispose();
    }

    public void addSheetTab(int sheetId, SheetTab sheetTab) {
        String idKey = sheet_tab_id_prefix + sheetId;
        putVariable(idKey, sheetTab);
        String nameKey = sheet_tab_name_prefix + sheetTab.getName();
        putVariable(nameKey, sheetTab);
        String orderKey = sheet_tab_order_prefix + sheetTab.getTabOrder();
        putVariable(orderKey, sheetTab);
    }

    public SheetTab getSheetTabById(int sheetId) {
        String key = sheet_tab_id_prefix + sheetId;
        return (SheetTab) getVariable(key);
    }

    public SheetTab getSheetTabByName(String name) {
        if (name != null && name.length() > 0) {
            String key = sheet_tab_name_prefix + name;
            return (SheetTab) getVariable(key);
        }
        return null;
    }
    
    public SheetTab getSheetTabByOrder(int order){
    	String key = sheet_tab_order_prefix+(order+1);
    	return (SheetTab)getVariable(key);
    }

    public void addDefinedName(String ...name){
        for(String _name:name){
            _definedNames.add(_name);
        }
    }

    public boolean isDefinedName(String name){
        return _definedNames.contains(name);
    }

    public void setDate1904(boolean date1904){
    	this._date1904 = date1904;
    }
    
    public boolean isDate1904(){
    	return _date1904;
    }
    
//    public boolean isApachePoiCreated(){
//    	
//    }
    
    public Map<String,Object> getCachedCellStyleMap(short styleIndex){
    	return _styleSettingsCache.cellStyles.get(styleIndex);
    }
    
    public void cacheCellStyleMap(short styleIndex,Map<String,Object> styleMap){
    	_styleSettingsCache.cellStyles.put(styleIndex, styleMap);
    }
    
    public Map<String,Object> getCachedRowOrColumnStyleMap(short styleIndex){
    	return _styleSettingsCache.rowOrColumnStyles.get(styleIndex);
    }
    
    public void cacheRowOrColumnStyleMap(short styleIndex, Map<String,Object> styleMap){
    	_styleSettingsCache.rowOrColumnStyles.put(styleIndex, styleMap);
    }
    
    public String getCacheNumericValueHandler(int dataFormatIndex){
    	return _styleSettingsCache.numericHandlers.get(dataFormatIndex);
    }
    
    public void cachedNumericValueHandler(int dataFormatIndex, String handlerName){
    	_styleSettingsCache.numericHandlers.put(dataFormatIndex, handlerName);
    }
    
    public WorkbookStylesTable getWorkbookStylesTable(){
        return _workbookStylesTable;
    }
    
    private static class StyleSettingsCache{
    	private final Map<Short,Map<String,Object>> cellStyles = new HashMap<>();
    	private final Map<Short,Map<String,Object>> rowOrColumnStyles = new HashMap<>();
    	private final Map<Integer,String> numericHandlers = new HashMap<>();
    	
    	private void clear(){
    		cellStyles.clear();
    		rowOrColumnStyles.clear();
    		numericHandlers.clear();
    	}
    	
    }
}
