package com.xzsoft.xsr.core.service;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;

public interface ImpAndExpExcelService {

	public void importXlsx(DocumentFile documentFile, File xlsxFile, Locale userLocale) throws Exception;
	
	public File exportSpreadsheet(DocumentFile documentFile, Locale userLocale) throws Exception;
	
	public List<SheetCell> getFixedCells(SheetTab sheetTab);
	
	public List<SheetCell> getFixedAndFloatCells(SheetTab sheetTab);
	
	public List<SheetTabElement> getFloatModalMergeCells(SheetTab sheetTab);
}
