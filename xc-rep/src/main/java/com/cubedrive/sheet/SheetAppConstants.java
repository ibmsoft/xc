package com.cubedrive.sheet;

public class SheetAppConstants {

	// set max empty row cell - any empty cell after column 64 will be ignore
	// during import.
	public final static int SHEET_COLUMN_MAX_EMPTY_CELL = 100;
	
	public final static int SHEET_CELL_CACHE_SIZE = 500;

	public static String USER_EXCEL_DIR = "excel";
	
	public static String SHEET_ATTACHMENT_DIR = "sheet_attachment";

	public static String EXCEL_IMPORT_ERROR_DIR = "excel_import_error";

}
