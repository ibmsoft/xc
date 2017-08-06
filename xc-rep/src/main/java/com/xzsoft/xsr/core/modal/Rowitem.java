package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;


public class Rowitem {
	/** 行指标窗口，保存时传递的json参数格式：
	 * json: [
	 * {"MODALSHEET_ID":3,
	 *  "ROWITEM_ID":"",
	 *  "ROWITEM_CODE":"",
	 *  "ROWITEM_NAME":"\u4e00\u3001\u8425\u4e1a\u603b\u6536\u5165",
	 *  "ROWNO":36,
	 *  "LANNO":1,
	 *  "ISFJITEM":false,
	 *  "DIRECTION":"JF",
	 *  "ISNEW":true}]
	 **/
	private String SUIT_ID;
	private String MODALTYPE_ID;
	private String MODALTYPE_CODE;
	private String MODALSHEET_ID; //java.lang.Integer to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private String MODALSHEET_CODE;
	private String ROWREF_ID;
	private String ROWITEM_ID; //java.lang.String to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private String ROWITEM_CODE; //java.lang.String to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private String ROWITEM_NAME; //java.lang.String to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private Integer ROWNO; //java.lang.Integer to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private Integer LANNO;
	private String REF_FLAG;//java.lang.String to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private Integer ROW;
	private Integer COL;
	private Boolean	ISFJITEM; //java.lang.Boolean to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private String DIRECTION; //java.lang.String to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private Boolean	ISNEW; //java.lang.Boolean to bean of class com.xzsoft.xsr.core.modal.Rowitem
	private Boolean ISICP;
	private String DESCRIPTION;
	private Boolean ISSUM;
	private String ITEMALIAS;
	private String ITEMUNIT;
	private String UPCODE;
	private String ORDERBY;
	private Timestamp CREATION_DATE; //CREATION_DATE
	private String CREATED_BY; //CREATED_BY
	private Timestamp LAST_UPDATE_DATE; //CREATION_DATE
	private String LAST_UPDATED_BY; //CREATED_BY
	
	public Rowitem() {}
	
	public Rowitem(String ROWITEM_ID, String SUIT_ID, String ROWITEM_CODE, String ROWITEM_NAME, String ITEMALIAS, 
			String UPCODE, Boolean ISICP, Timestamp CREATION_DATE, String CREATED_BY, Timestamp LAST_UPDATE_DATE, 
			String LAST_UPDATED_BY) {
		this.ROWITEM_ID = ROWITEM_ID; 
		this.SUIT_ID = SUIT_ID; 
		this.ROWITEM_CODE = ROWITEM_CODE; 
		this.ROWITEM_NAME = ROWITEM_NAME; 
		this.ITEMALIAS = ITEMALIAS; 
		this.UPCODE = UPCODE; 
		this.ISICP = ISICP; 
		this.CREATION_DATE = CREATION_DATE; 
		this.CREATED_BY = CREATED_BY; 
		this.LAST_UPDATE_DATE = LAST_UPDATE_DATE; 
		this.LAST_UPDATED_BY = LAST_UPDATED_BY; 
	}
	
	public String getSUIT_ID() {
		return SUIT_ID;
	}
	public void setSUIT_ID(String sUIT_ID) {
		SUIT_ID = sUIT_ID;
	}
	public String getMODALSHEET_ID() {
		return MODALSHEET_ID;
	}
	public void setMODALSHEET_ID(String mODALSHEET_ID) {
		MODALSHEET_ID = mODALSHEET_ID;
	}
	public String getROWITEM_ID() {
		return ROWITEM_ID;
	}
	public void setROWITEM_ID(String rOWITEM_ID) {
		ROWITEM_ID = rOWITEM_ID;
	}
	public String getROWITEM_CODE() {
		return ROWITEM_CODE;
	}
	public void setROWITEM_CODE(String rOWITEM_CODE) {
		ROWITEM_CODE = rOWITEM_CODE;
	}
	public String getROWITEM_NAME() {
		return ROWITEM_NAME;
	}
	public void setROWITEM_NAME(String rOWITEM_NAME) {
		ROWITEM_NAME = rOWITEM_NAME;
	}
	public Integer getROWNO() {
		return ROWNO;
	}
	public void setROWNO(Integer rOWNO) {
		ROWNO = rOWNO;
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

	public Boolean getISFJITEM() {
		return ISFJITEM;
	}
	public void setISFJITEM(Boolean iSFJITEM) {
		ISFJITEM = iSFJITEM;
	}
	public String getDIRECTION() {
		return DIRECTION;
	}
	public void setDIRECTION(String dIRECTION) {
		DIRECTION = dIRECTION;
	}
	public Boolean getISNEW() {
		return ISNEW;
	}
	public void setISNEW(Boolean iSNEW) {
		ISNEW = iSNEW;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public Boolean getISSUM() {
		return ISSUM;
	}
	public void setISSUM(Boolean iSSUM) {
		ISSUM = iSSUM;
	}
	public String getITEMALIAS() {
		return ITEMALIAS;
	}
	public void setITEMALIAS(String iTEMALIAS) {
		ITEMALIAS = iTEMALIAS;
	}
	public String getITEMUNIT() {
		return ITEMUNIT;
	}
	public void setITEMUNIT(String iTEMUNIT) {
		ITEMUNIT = iTEMUNIT;
	}
	public String getORDERBY() {
		return ORDERBY;
	}
	public void setORDERBY(String oRDERBY) {
		ORDERBY = oRDERBY;
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
	public Boolean getISICP() {
		return ISICP;
	}

	public void setISICP(Boolean iSICP) {
		ISICP = iSICP;
	}

	public String getUPCODE() {
		return UPCODE;
	}

	public void setUPCODE(String uPCODE) {
		UPCODE = uPCODE;
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
	public String getROWREF_ID() {
		return ROWREF_ID;
	}

	public void setROWREF_ID(String rOWREF_ID) {
		ROWREF_ID = rOWREF_ID;
	}

	@Override
	public String toString() {
		return "Rowitem [ROWITEM_CODE=" + ROWITEM_CODE + ", ROWITEM_NAME="
				+ ROWITEM_NAME + ", ROW=" + ROW + ", COL=" + COL + "]";
	}

}
