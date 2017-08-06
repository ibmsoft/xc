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

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cubedrive.base.domain.BaseDomain;

public class SheetTabElement extends BaseDomain {

	private static final long serialVersionUID = -1061507017182935906L;

	@JsonIgnore
	private Integer id;
	
	private SheetTab tab;
	
	private String name;
	
	private String etype;
	
	private String content;
	
	//added by zhousr 2016-2-19
	@JsonIgnore
	private String ELEMENT_ID; 
	@JsonIgnore
	private Timestamp CREATION_DATE;
	@JsonIgnore
	private String CREATED_BY;
	public String getELEMENT_ID() {
		return ELEMENT_ID;
	}
	public void setELEMENT_ID(String eLEMENT_ID_ID) {
		ELEMENT_ID = eLEMENT_ID_ID;
	}
	public Timestamp getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Timestamp cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	//added by zhousr 2016-2-19 end
	
	public SheetTabElement() {
		
	}
	
	public SheetTabElement(String name, String etype, String content) {
		this.name = name;
		this.etype = etype;
		this.content = content;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SheetTab getTab() {
		return tab;
	}

	public void setTab(SheetTab tab) {
		this.tab = tab;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEtype() {
		return etype;
	}

	public void setEtype(String etype) {
		this.etype = etype;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SheetTabElement other = (SheetTabElement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
