package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ModalSheet {

	private int id; //对应xsr_rep_modalsheet表的format_id字段
	private String name; //对应xsr_rep_modalsheet表的modalsheet_name字段
	private boolean actived;
	@JsonIgnore
	private String code; //对应xsr_rep_modalsheet表的modalsheet_code字段

	@JsonIgnore
	private String MODALSHEET_ID;
	@JsonIgnore
	private String SUIT_ID;
	@JsonIgnore
	private String MODALTYPE_ID;
	@JsonIgnore
	private String MODALTYPE_CODE;
	@JsonIgnore
	private String SHORTNAME;
	@JsonIgnore
	private String DESCRIPTION;
	@JsonIgnore
	private String SORTNUM;
	@JsonIgnore
	private String VERIFIER_ID;
	@JsonIgnore
	private String CLASSIFICATION;
	@JsonIgnore
	private String FRAQ_CODE;
	@JsonIgnore
	private String FJITEM_NO;
	@JsonIgnore
	private String ENABLED;
	@JsonIgnore
	private String ROWUPCODE;
	@JsonIgnore
	private String COLUPCODE;
	@JsonIgnore
	private String TYPE;
	@JsonIgnore
	private String XLS_BLOB;
	@JsonIgnore
	private String TITLE;
	@JsonIgnore
	private Timestamp STARTDATE;
	@JsonIgnore
	private Timestamp ENDDATE;
	@JsonIgnore
	private String FJ_FLAG;
	@JsonIgnore
	private Timestamp LAST_UPDATE_DATE;
	@JsonIgnore
	private String LAST_UPDATED_BY;
	@JsonIgnore
	private Timestamp CREATION_DATE;
	@JsonIgnore
	private String CREATED_BY;
	@JsonIgnore
	private Integer TITLE_MAX_ROW;
	
	public ModalSheet() {
		
	}
	
	public ModalSheet(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public ModalSheet(int id, String name, boolean actived) {
		this.id = id;
		this.name = name;
		this.actived = actived;
	}
	
	public boolean isActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getMODALSHEET_ID() {
		return MODALSHEET_ID;
	}

	public void setMODALSHEET_ID(String mODALSHEET_ID) {
		MODALSHEET_ID = mODALSHEET_ID;
	}

	public String getSUIT_ID() {
		return SUIT_ID;
	}

	public void setSUIT_ID(String sUIT_ID) {
		SUIT_ID = sUIT_ID;
	}

	public String getMODALTYPE_ID() {
		return MODALTYPE_ID;
	}

	public void setMODALTYPE_ID(String mODALTYPE_ID) {
		MODALTYPE_ID = mODALTYPE_ID;
	}

	public String getMODALTYPE_CODE() {
		return MODALTYPE_CODE;
	}

	public void setMODALTYPE_CODE(String mODALTYPE_CODE) {
		MODALTYPE_CODE = mODALTYPE_CODE;
	}

	public String getSHORTNAME() {
		return SHORTNAME;
	}

	public void setSHORTNAME(String sHORTNAME) {
		SHORTNAME = sHORTNAME;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getSORTNUM() {
		return SORTNUM;
	}

	public void setSORTNUM(String sORTNUM) {
		SORTNUM = sORTNUM;
	}

	public String getVERIFIER_ID() {
		return VERIFIER_ID;
	}

	public void setVERIFIER_ID(String vERIFIER_ID) {
		VERIFIER_ID = vERIFIER_ID;
	}

	public String getCLASSIFICATION() {
		return CLASSIFICATION;
	}

	public void setCLASSIFICATION(String cLASSIFICATION) {
		CLASSIFICATION = cLASSIFICATION;
	}

	public String getFRAQ_CODE() {
		return FRAQ_CODE;
	}

	public void setFRAQ_CODE(String fRAQ_CODE) {
		FRAQ_CODE = fRAQ_CODE;
	}

	public String getFJITEM_NO() {
		return FJITEM_NO;
	}

	public void setFJITEM_NO(String fJITEM_NO) {
		FJITEM_NO = fJITEM_NO;
	}

	
	public String getENABLED() {
		return ENABLED;
	}

	public void setENABLED(String eNABLED) {
		ENABLED = eNABLED;
	}

	public String getROWUPCODE() {
		return ROWUPCODE;
	}

	public void setROWUPCODE(String rOWUPCODE) {
		ROWUPCODE = rOWUPCODE;
	}

	public String getCOLUPCODE() {
		return COLUPCODE;
	}

	public void setCOLUPCODE(String cOLUPCODE) {
		COLUPCODE = cOLUPCODE;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getXLS_BLOB() {
		return XLS_BLOB;
	}

	public void setXLS_BLOB(String xLS_BLOB) {
		XLS_BLOB = xLS_BLOB;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public Timestamp getSTARTDATE() {
		return STARTDATE;
	}

	public void setSTARTDATE(Timestamp sTARTDATE) {
		STARTDATE = sTARTDATE;
	}

	public Timestamp getENDDATE() {
		return ENDDATE;
	}

	public void setENDDATE(Timestamp eNDDATE) {
		ENDDATE = eNDDATE;
	}

	public String getFJ_FLAG() {
		return FJ_FLAG;
	}

	public void setFJ_FLAG(String fJ_FLAG) {
		FJ_FLAG = fJ_FLAG;
	}

	public Timestamp getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}

	public void setLAST_UPDATE_DATE(Timestamp lAST_UPDATE_DATE) {
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
	}

	public String getLAST_UPDATED_BY() {
		return LAST_UPDATED_BY;
	}

	public void setLAST_UPDATED_BY(String lAST_UPDATED_BY) {
		LAST_UPDATED_BY = lAST_UPDATED_BY;
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

	public Integer getTITLE_MAX_ROW() {
		return TITLE_MAX_ROW;
	}

	public void setTITLE_MAX_ROW(Integer tITLE_MAX_ROW) {
		TITLE_MAX_ROW = tITLE_MAX_ROW;
	}

	@Override
	public String toString() {
		return "ModalSheet [id=" + id + ", name=" + name + ", actived="
				+ actived + ", code=" + code + ", MODALSHEET_ID="
				+ MODALSHEET_ID + ", SUIT_ID=" + SUIT_ID + ", MODALTYPE_ID="
				+ MODALTYPE_ID + ", MODALTYPE_CODE=" + MODALTYPE_CODE
				+ ", SHORTNAME=" + SHORTNAME + ", DESCRIPTION=" + DESCRIPTION
				+ ", SORTNUM=" + SORTNUM + ", VERIFIER_ID=" + VERIFIER_ID
				+ ", CLASSIFICATION=" + CLASSIFICATION + ", FRAQ_CODE="
				+ FRAQ_CODE + ", FJITEM_NO=" + FJITEM_NO + ", ENABLED="
				+ ENABLED + ", ROWUPCODE=" + ROWUPCODE + ", COLUPCODE="
				+ COLUPCODE + ", TYPE=" + TYPE + ", XLS_BLOB=" + XLS_BLOB
				+ ", TITLE=" + TITLE + ", STARTDATE=" + STARTDATE
				+ ", ENDDATE=" + ENDDATE + ", FJ_FLAG=" + FJ_FLAG
				+ ", LAST_UPDATE_DATE=" + LAST_UPDATE_DATE
				+ ", LAST_UPDATED_BY=" + LAST_UPDATED_BY + ", CREATION_DATE="
				+ CREATION_DATE + ", CREATED_BY=" + CREATED_BY + "]";
	}
	
	

	
	
}
