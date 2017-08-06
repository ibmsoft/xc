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
package com.cubedrive.sheet.domain.sheet;

import org.codehaus.jackson.annotate.JsonIgnore;

public class SheetCellObj {
	
	@JsonIgnore
	private Integer id;
	private Integer sheet;
	private Integer row;
	private Integer col;
	private String json;
	
	public SheetCellObj(SheetCell cell) {
		this.sheet = cell.getTabId();
		this.row = cell.getX();
		this.col = cell.getY();
		this.json = cell.getContent();
		this.id = cell.getId();
	}
	
//	public SheetCellObj(SheetCell cell, Integer tabId) {
//		this.sheet = tabId;
//		this.row = cell.getX();
//		this.col = cell.getY();
//		this.json = cell.getContent();
//		this.id = cell.getId();
//	}

	public SheetCellObj(Integer x, Integer y, Integer tabId) {
		this.row = x;
		this.col = y;
		this.sheet = tabId;
		this.json = null;
	}
	
	public SheetCellObj(Integer x, Integer y, Integer tabId, String json) {
		this.row = x;
		this.col = y;
		this.sheet = tabId;
		this.json = json;
	}

	public Integer getSheet() {
		return sheet;
	}

	public void setSheet(Integer sheet) {
		this.sheet = sheet;
	}

	public Integer getRow() {
		return row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}
	

    public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
