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

import java.util.HashMap;
import java.util.Map;

import com.cubedrive.sheet.service.sheet._export.SheetExportEnvironment;

public abstract class SSExportContext {

	private final Map<String, Object> _variables = new HashMap<>();

	protected final SheetExportEnvironment _sheetExportEnvironment;

	public SSExportContext(SheetExportEnvironment sheetExportEnvironment) {
		_sheetExportEnvironment = sheetExportEnvironment;
	}

	public void init(){}
	
	public void putVariable(String name, Object variable) {
		this._variables.put(name, variable);
	}

	public Object getVariable(String name) {
		return (Object) _variables.get(name);
	}
	
	public SheetExportEnvironment sheetExportEnvironment(){
		return _sheetExportEnvironment;
	}

	public abstract <T extends SSExportContext> T parentContext();

	public void dispose() {
		_variables.clear();
	}

}
