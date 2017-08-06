package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ModalSheetFormat {

	@JsonIgnore
	private String formatId; //FORMAT_ID
	//private String sheet; //MSFORMAT_ID
	private Integer sheet; //MSFORMAT_ID
	private Integer row; //X
	private Integer col; //Y
	private String json; //CONTENT
	private Boolean cal; //IS_CAL
	@JsonIgnore
	private String rawData; //RAW_DATA
	@JsonIgnore
	private String cell_comment; //CELL_COMMENT
	@JsonIgnore
	private String cell_comment_type; //CELL_COMMENT_TYPE
	@JsonIgnore
	private Timestamp creationDate; //CREATION_DATE
	@JsonIgnore
	private String createdBy; //CREATED_BY

/*	public ModalSheetFormat(String id, String modalsheet_id, Integer x, Integer y,
			String content, Boolean cal, Timestamp creationDate, String userId) {
		this.formatId = id;
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = modalsheet_id;
		this.cal = cal;
		this.creationDate = creationDate;
		this.createdBy = userId;
	}*/
	
	public ModalSheetFormat(String id, Integer sheetId, Integer x, Integer y,
			String content, Boolean cal, Timestamp creationDate, String userId) {
		this.formatId = id;
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = sheetId;
		this.cal = cal;
		this.creationDate = creationDate;
		this.createdBy = userId;
	}
	
/*	public ModalSheetFormat(String id, String modalsheet_id, Integer x, Integer y,
			String content, Boolean cal, Timestamp creationDate, String userId, 
			String cell_comment, String cell_comment_type) {
		this.formatId = id;
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = modalsheet_id;
		this.cal = cal;
		this.creationDate = creationDate;
		this.createdBy = userId;
		this.cell_comment = cell_comment;
		this.cell_comment_type = cell_comment_type;
	}*/
	
	public ModalSheetFormat(String id, Integer sheetId, Integer x, Integer y,
			String content, Boolean cal, Timestamp creationDate, String userId, 
			String cell_comment, String cell_comment_type) {
		this.formatId = id;
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = sheetId;
		this.cal = cal;
		this.creationDate = creationDate;
		this.createdBy = userId;
		this.cell_comment = cell_comment;
		this.cell_comment_type = cell_comment_type;
	}

/*	public ModalSheetFormat(String modalsheet_id, Integer x, Integer y,
			String content, String data, Boolean cal, String cell_comment) {
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = modalsheet_id;
		this.rawData = data;
		this.cal = cal;
		this.cell_comment = cell_comment;
	}*/
	
	public ModalSheetFormat(Integer sheetId, Integer x, Integer y,
			String content, String data, Boolean cal, String cell_comment) {
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = sheetId;
		this.rawData = data;
		this.cal = cal;
		this.cell_comment = cell_comment;
	}

/*	public ModalSheetFormat(String modalsheet_id, Integer x, Integer y,
			String content, Boolean cal) {
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = modalsheet_id;
		this.cal = cal;
	}*/
	
	public ModalSheetFormat(Integer sheetId, Integer x, Integer y,
			String content, Boolean cal) {
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = sheetId;
		this.cal = cal;
	}

/*	public ModalSheetFormat(String modalsheet_id, Integer x, Integer y,
			String content, Boolean cal, String cell_comment_type) {
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = modalsheet_id;
		this.cal = cal;
		this.cell_comment_type = cell_comment_type;
	}*/
	
	public ModalSheetFormat(Integer sheetId, Integer x, Integer y,
			String content, Boolean cal, String cell_comment_type) {
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = sheetId;
		this.cal = cal;
		this.cell_comment_type = cell_comment_type;
	}

/*	public ModalSheetFormat(String modalsheet_id, Integer x, Integer y,
			String content, Boolean cal, String cell_comment,
			String cell_comment_type) {
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = modalsheet_id;
		this.cal = cal;
		this.cell_comment = cell_comment;
		this.cell_comment_type = cell_comment_type;
	}*/
	
	public ModalSheetFormat(Integer sheetId, Integer x, Integer y,
			String content, Boolean cal, String cell_comment,
			String cell_comment_type) {
		this.row = x;
		this.col = y;
		this.json = content;
		this.sheet = sheetId;
		this.cal = cal;
		this.cell_comment = cell_comment;
		this.cell_comment_type = cell_comment_type;
	}

	public ModalSheetFormat() {
	}

	public String getFormatId() {
		return formatId;
	}

	public void setFormatId(String formatId) {
		this.formatId = formatId;
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

	public Boolean getCal() {
		return cal;
	}

	public void setCal(Boolean cal) {
		this.cal = cal;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getCell_comment() {
		return cell_comment;
	}

	public void setCell_comment(String cell_comment) {
		this.cell_comment = cell_comment;
	}

	public String getCell_comment_type() {
		return cell_comment_type;
	}

	public void setCell_comment_type(String cell_comment_type) {
		this.cell_comment_type = cell_comment_type;
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

}
