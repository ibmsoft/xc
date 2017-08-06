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
package com.cubedrive.sheet.service.sheet._export.handler.cell;

import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;

public abstract class CellProcessor {

	protected final XSSFCellExportContext _xssfCellExportContext;

	public CellProcessor(XSSFCellExportContext xssfCellExportContext) {
		this._xssfCellExportContext = xssfCellExportContext;
	}

	public XSSFCellExportContext xssfCellExportContext() {
		return _xssfCellExportContext;
	}

	public abstract void process();
}
