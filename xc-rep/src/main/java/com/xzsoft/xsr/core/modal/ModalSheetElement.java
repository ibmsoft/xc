package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ModalSheetElement {

	//{"sheet":903722,"name":"903722$2$2$2$4","ftype":"meg","json":"[2,2,2,4]"}
	
	@JsonIgnore
	private String elementId; //ELEMENT_ID
	//private String sheet; //MSFORMAT_ID
	private Integer sheet; //MSFORMAT_ID
	private String name; //NAME
	private String ftype; //ETYPE
	private String json; //CONTENT
	@JsonIgnore
	private Timestamp creationDate; //CREATION_DATE
	@JsonIgnore
	private String createdBy; //CREATED_BY
	
	public ModalSheetElement() {
		
	}
	
/*	public ModalSheetElement(String sheet, String name, String ftype, String json) {
		this.sheet = sheet;
		this.name = name;
		this.ftype = ftype;
		this.json = json;
	}*/
	
	public ModalSheetElement(Integer sheet, String name, String ftype, String json) {
		this.sheet = sheet;
		this.name = name;
		this.ftype = ftype;
		this.json = json;
	}

/*	public ModalSheetElement(String id, String sheet, String name, String ftype, String json, Timestamp creationDate, String userId) {
		this.elementId = id;
		this.sheet = sheet;
		this.name = name;
		this.ftype = ftype;
		this.json = json;
		this.creationDate = creationDate;
		this.createdBy = userId;
	}*/
	
	public ModalSheetElement(String id, Integer sheet, String name, String ftype, String json, Timestamp creationDate, String userId) {
		this.elementId = id;
		this.sheet = sheet;
		this.name = name;
		this.ftype = ftype;
		this.json = json;
		this.creationDate = creationDate;
		this.createdBy = userId;
	}
	
/*	public String getSheet() {
		return sheet;
	}
	public void setSheet(String sheet) {
		this.sheet = sheet;
	}*/
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
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "ModalSheetElement [sheet=" + sheet + ", name=" + name
				+ ", ftype=" + ftype + ", json=" + json + "]";
	}
	
}
