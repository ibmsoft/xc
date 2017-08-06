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

public class SheetTabElementObj {
	
	private Integer id;
	private Integer sheet;	
	private String name;	
	private String ftype;
	private String json;
	
	public SheetTabElementObj(SheetTabElement element) {
		this.id = element.getId();
		this.sheet = element.getTab().getId();
		this.name = element.getName();
		this.json = element.getContent();
		this.ftype = element.getEtype();
	}
	
	public SheetTabElementObj(Integer sheetId, String json) {
		this.sheet = sheetId;
		this.json = json;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSheet() {
		return sheet;
	}

	public void setSheet(Integer sheet) {
		this.sheet = sheet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFtype() {
		return ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	
}
