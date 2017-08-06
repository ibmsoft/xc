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

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

public class CellTouchObj {
	
	@JsonIgnore
	private Integer id;
	private Integer sheet;
	private Integer row;
	private Integer col;
	private Map<String, Object> json;
	
	public CellTouchObj(Integer x, Integer y, Integer tabId, Map<String, Object> json) {
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

	public Map<String, Object> getJson() {
		return json;
	}

	public void setJson(Map<String, Object> json) {
		this.json = json;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
