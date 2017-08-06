package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class Colitem {
	
	private String SUIT_ID;
	private String MODALTYPE_ID;
	private String MODALTYPE_CODE;
	private String MODALSHEET_CODE;
	private String COLREF_ID;
	private String FJITEM_SET_ID;
	//****************************与页面json相关***********************************
	private String MODALSHEET_ID;
	private String COLITEM_ID;
	private String COLITEM_CODE;
	private String COLITEM_NAME;
	private Integer COLNO;
	private Integer LANNO;
	private String REF_FLAG;
	private Boolean ISNEW;
	private Integer ROW;
	private Integer COL;
	private Integer DATATYPE;
	private Boolean ISSEL;
	//****************************与页面json相关***********************************
	private String DATA_COL;
	private String UPCODE;
	private String ITEMALIAS;
	private String ORDERBY;
	private String ITEMUNIT;
	private String DIRECTION;
	private String DESCRIPTION;
	private Timestamp CREATION_DATE; //CREATION_DATE
	private String CREATED_BY; //CREATED_BY
	private Timestamp LAST_UPDATE_DATE; //CREATION_DATE
	private String LAST_UPDATED_BY; //CREATED_BY
	
	public Colitem() {}
	
	public Colitem(String COLITEM_ID, String SUIT_ID, String COLITEM_CODE, String COLITEM_NAME, String ITEMALIAS, 
			String UPCODE, Timestamp CREATION_DATE, String CREATED_BY, Timestamp LAST_UPDATE_DATE, 
			String LAST_UPDATED_BY) {
		this.COLITEM_ID = COLITEM_ID;
		this.SUIT_ID = SUIT_ID;
		this.COLITEM_CODE = COLITEM_CODE;
		this.COLITEM_NAME = COLITEM_NAME;
		this.ITEMALIAS = ITEMALIAS;
		this.UPCODE = UPCODE;
		this.CREATION_DATE = CREATION_DATE;
		this.CREATED_BY = CREATED_BY;
		this.LAST_UPDATE_DATE = LAST_UPDATE_DATE;
		this.LAST_UPDATED_BY = LAST_UPDATED_BY;
	}
	
	public String getMODALSHEET_ID() {
		return MODALSHEET_ID;
	}
	public void setMODALSHEET_ID(String mODALSHEET_ID) {
		MODALSHEET_ID = mODALSHEET_ID;
	}
	public String getCOLITEM_ID() {
		return COLITEM_ID;
	}
	public void setCOLITEM_ID(String cOLITEM_ID) {
		COLITEM_ID = cOLITEM_ID;
	}
	public String getCOLITEM_CODE() {
		return COLITEM_CODE;
	}
	public void setCOLITEM_CODE(String cOLITEM_CODE) {
		COLITEM_CODE = cOLITEM_CODE;
	}
	public String getCOLITEM_NAME() {
		return COLITEM_NAME;
	}
	public void setCOLITEM_NAME(String cOLITEM_NAME) {
		COLITEM_NAME = cOLITEM_NAME;
	}
	public Integer getCOLNO() {
		return COLNO;
	}
	public void setCOLNO(Integer cOLNO) {
		COLNO = cOLNO;
	}
	public Integer getLANNO() {
		return LANNO;
	}
	public void setLANNO(Integer lANNO) {
		LANNO = lANNO;
	}
	public String getREF_FLAG() {
		return REF_FLAG;
	}

	public void setREF_FLAG(String rEF_FLAG) {
		REF_FLAG = rEF_FLAG;
	}
	public Boolean getISNEW() {
		return ISNEW;
	}
	public void setISNEW(Boolean iSNEW) {
		ISNEW = iSNEW;
	}
	public Integer getROW() {
		return ROW;
	}
	public void setROW(Integer rOW) {
		ROW = rOW;
	}
	public Integer getCOL() {
		return COL;
	}
	public void setCOL(Integer cOL) {
		COL = cOL;
	}
	public Integer getDATATYPE() {
		return DATATYPE;
	}
	public void setDATATYPE(Integer dATATYPE) {
		DATATYPE = dATATYPE;
	}
	public Boolean getISSEL() {
		return ISSEL;
	}
	public void setISSEL(Boolean iSSEL) {
		ISSEL = iSSEL;
	}
	public String getUPCODE() {
		return UPCODE;
	}
	public void setUPCODE(String uPCODE) {
		UPCODE = uPCODE;
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
	public String getSUIT_ID() {
		return SUIT_ID;
	}
	public void setSUIT_ID(String sUIT_ID) {
		SUIT_ID = sUIT_ID;
	}
	public String getITEMALIAS() {
		return ITEMALIAS;
	}
	public void setITEMALIAS(String iTEMALIAS) {
		ITEMALIAS = iTEMALIAS;
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
	public String getMODALSHEET_CODE() {
		return MODALSHEET_CODE;
	}
	public void setMODALSHEET_CODE(String mODALSHEET_CODE) {
		MODALSHEET_CODE = mODALSHEET_CODE;
	}
	public String getORDERBY() {
		return ORDERBY;
	}
	public void setORDERBY(String oRDERBY) {
		ORDERBY = oRDERBY;
	}
	public String getCOLREF_ID() {
		return COLREF_ID;
	}
	public void setCOLREF_ID(String cOLREF_ID) {
		COLREF_ID = cOLREF_ID;
	}
	public String getDATA_COL() {
		return DATA_COL;
	}
	public void setDATA_COL(String dATA_COL) {
		DATA_COL = dATA_COL;
	}
	public String getITEMUNIT() {
		return ITEMUNIT;
	}
	public void setITEMUNIT(String iTEMUNIT) {
		ITEMUNIT = iTEMUNIT;
	}
	public String getDIRECTION() {
		return DIRECTION;
	}
	public void setDIRECTION(String dIRECTION) {
		DIRECTION = dIRECTION;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getFJITEM_SET_ID() {
		return FJITEM_SET_ID;
	}
	public void setFJITEM_SET_ID(String fJITEM_SET_ID) {
		FJITEM_SET_ID = fJITEM_SET_ID;
	}

	@Override
	public String toString() {
		return "Colitem [SUIT_ID=" + SUIT_ID + ", MODALTYPE_ID=" + MODALTYPE_ID
				+ ", MODALTYPE_CODE=" + MODALTYPE_CODE + ", MODALSHEET_CODE="
				+ MODALSHEET_CODE + ", COLREF_ID=" + COLREF_ID
				+ ", MODALSHEET_ID=" + MODALSHEET_ID + ", COLITEM_ID="
				+ COLITEM_ID + ", COLITEM_CODE=" + COLITEM_CODE
				+ ", COLITEM_NAME=" + COLITEM_NAME + ", COLNO=" + COLNO
				+ ", LANNO=" + LANNO + ", ISNEW=" + ISNEW + ", ROW=" + ROW
				+ ", COL=" + COL + ", DATATYPE=" + DATATYPE + ", ISSEL="
				+ ISSEL + ", UPCODE=" + UPCODE + ", ITEMALIAS=" + ITEMALIAS
				+ ", ORDERBY=" + ORDERBY + ", CREATION_DATE=" + CREATION_DATE
				+ ", CREATED_BY=" + CREATED_BY + ", LAST_UPDATE_DATE="
				+ LAST_UPDATE_DATE + ", LAST_UPDATED_BY=" + LAST_UPDATED_BY
				+ "]";
	}

}
