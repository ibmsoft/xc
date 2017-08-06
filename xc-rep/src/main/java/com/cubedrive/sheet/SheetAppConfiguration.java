package com.cubedrive.sheet;

import com.cubedrive.base.BaseAppConfiguration;

public class SheetAppConfiguration {
	
	private static final SheetAppConfiguration _instance = new SheetAppConfiguration();
	
	private static final String default_sheet_cell_table_prefix = "sheet_cell_";
	
	private static final String default_sheet_cell_table_template = "sheet_cell_template";
	
	private static final String default_sheet_cell_table_size = "1000000";
	
	private BaseAppConfiguration _baseAppConfiguration;
	
	private String _sheetCellTablePrefix;
	
	private String _sheetCellTableTemplate;

	private int _sheetCellTableSize;
	
	private SheetAppConfiguration(){
		
	}
	
	void init(BaseAppConfiguration baseAppConfiguration){
		this._baseAppConfiguration = baseAppConfiguration;
//		String sheetCellTablePrefix = baseAppConfiguration.getPropertyByKey("app.sheet.sheetCellTablePrefix");
//		if(sheetCellTablePrefix == null){
//			sheetCellTablePrefix = default_sheet_cell_table_prefix;
//		}
		_sheetCellTablePrefix = default_sheet_cell_table_prefix;
		
		_sheetCellTableTemplate = default_sheet_cell_table_template;
//		String sheetCellTableSize = baseAppConfiguration.getPropertyByKey("app.sheet.sheetCellTableSize");
//		if(sheetCellTableSize == null){
//			sheetCellTableSize = default_sheet_cell_table_size;
//		}
		_sheetCellTableSize = Integer.parseInt(default_sheet_cell_table_size);
	}
	
	public BaseAppConfiguration baseAppConfiguration(){
		return _baseAppConfiguration;
	}
	
	public String getSheetCellTablePrefix() {
		return _sheetCellTablePrefix;
	}
	
	public String getSheetCellTableTemplate(){
	    return _sheetCellTableTemplate;
	}

	public int getSheetCellTableSize() {
		return _sheetCellTableSize;
	}
	
	public static SheetAppConfiguration instance(){
		return _instance;
	}

}
