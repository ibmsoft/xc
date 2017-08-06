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
import java.util.Map;

import com.cubedrive.sheet.service.sheet._import.SheetImportEnvironment;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventCell;

public abstract class SSImportContext {

    private final Map<String, Object> _variables = new HashMap<>();
    protected final SheetImportEnvironment _sheetImportEnvironment;

    public SSImportContext(SheetImportEnvironment sheetImportEnvironment) {
        this._sheetImportEnvironment = sheetImportEnvironment;
    }

    public SheetImportEnvironment sheetImportEnvironment() {
        return _sheetImportEnvironment;
    }

    public void putVariable(String name, Object variable) {
        this._variables.put(name, variable);
    }

    public Object getVariable(String name) {
        return (Object) _variables.get(name);
    }
    
    
    public void preHandleCell(XSSFReadEventCell cell){
        
    }
    
    public void postHandleCell(XSSFReadEventCell cell){
        
    }

    public abstract <T extends SSImportContext> T parentContext();
    
    public abstract WorkbookImportContext workbookImportContext();

    public void dispose(){
        _variables.clear();
    }
}
