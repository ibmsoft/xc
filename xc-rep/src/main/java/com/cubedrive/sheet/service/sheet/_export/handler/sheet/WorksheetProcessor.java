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
package com.cubedrive.sheet.service.sheet._export.handler.sheet;

import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;

public abstract class WorksheetProcessor {
	
	protected final XSSFWorksheetExportContext _xssfWorksheetExportContext;

	public WorksheetProcessor(XSSFWorksheetExportContext xssfWorksheetExportContext) {
		_xssfWorksheetExportContext = xssfWorksheetExportContext;
	}

	public XSSFWorksheetExportContext xssfWorksheetExportContext() {
		return _xssfWorksheetExportContext;
	}

	public abstract void process();

}
