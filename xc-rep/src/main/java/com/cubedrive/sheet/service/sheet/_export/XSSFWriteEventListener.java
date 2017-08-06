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

import java.io.IOException;
import java.io.OutputStream;

import com.cubedrive.base.domain.document.DocumentConfig;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;

public interface XSSFWriteEventListener {

	void onStartWorkbook();
	
	void onWorkbookElement(DocumentConfig documentConfig);

	void onEndWorkbook();

	void onStartSheet(SheetTab sheetTab);

	void onSheetCell(SheetCell sheetCell);
	
	void afterIterateSheetCell();

	void onSheetTabElement(SheetTabElement sheetTabElement);
	
	void afterIterateSheetTabElement();

	void onEndSheet();
	
	void writeOutputStream(OutputStream outputStream) throws IOException;

}
