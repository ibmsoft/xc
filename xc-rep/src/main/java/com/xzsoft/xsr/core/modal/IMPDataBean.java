package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class IMPDataBean {

	private String RPT_TYPE;//报表时段： 如：月报
	private String RPT_PRIOD;//报表时间： 如：2011-09
	private String PUBLISH_TIME;//上报时间
	private String PUBLISHER;//上报人
	private String RPT_NAME;//表名
	private String RPT_CODE;//报表代码
	private String ENTITY_NAME;//单位名称
	private String ROW_NO;//行次
	private String COL_NO;//列次
	private String DATA_TYPE;//数据类型
	private String NO;//序号
	private String CELLVALUE;//值
	private int FLAG;//主表数据：1，附加行数据：2
	
	private int LINENO;//在数据文件中的行号
	
	private String SUIT_ID;
	private String MODALTYPE_ID;
	private String MODALTYPE_CODE;
	private String SESSIONID;
	private String USER_ID;

	
	//====================接收选中报表需要用到的字段==================
	private String MODALSHEET_ID;
	private String MODALSHEET_CODE;
	private String MODALSHEET_NAME;
	private String ENTITY_ID;
	private String ENTITY_CODE;
	private String PERIOD_ID;
	private String CURRENCY_ID;
	private String CURRENCY_CODE;
	private String ROWITEM_ID;
	private String ROWITEM_CODE;
	private String COLITEM_ID;
	private String COLITEM_CODE;
	//================插入固定行表数据时需要用到
	private String CELLTEXTV;
	private String CELLV_ID;
	private Timestamp LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	private Timestamp CREATION_DATE;
	private String CREATED_BY;
	public String getRPT_TYPE() {
		return RPT_TYPE;
	}

	public void setRPT_TYPE(String rPT_TYPE) {
		RPT_TYPE = rPT_TYPE;
	}

	public String getRPT_PRIOD() {
		return RPT_PRIOD;
	}

	public void setRPT_PRIOD(String rPT_PRIOD) {
		RPT_PRIOD = rPT_PRIOD;
	}

	public String getPUBLISH_TIME() {
		return PUBLISH_TIME;
	}

	public void setPUBLISH_TIME(String pUBLISH_TIME) {
		PUBLISH_TIME = pUBLISH_TIME;
	}

	public String getPUBLISHER() {
		return PUBLISHER;
	}

	public void setPUBLISHER(String pUBLISHER) {
		PUBLISHER = pUBLISHER;
	}

	public String getRPT_NAME() {
		return RPT_NAME;
	}

	public void setRPT_NAME(String rPT_NAME) {
		RPT_NAME = rPT_NAME;
	}

	public String getRPT_CODE() {
		return RPT_CODE;
	}

	public void setRPT_CODE(String rPT_CODE) {
		RPT_CODE = rPT_CODE;
	}

	public String getENTITY_NAME() {
		return ENTITY_NAME;
	}

	public void setENTITY_NAME(String eNTITY_NAME) {
		ENTITY_NAME = eNTITY_NAME;
	}

	public String getROW_NO() {
		return ROW_NO;
	}

	public void setROW_NO(String rOW_NO) {
		ROW_NO = rOW_NO;
	}

	public String getCOL_NO() {
		return COL_NO;
	}

	public void setCOL_NO(String cOL_NO) {
		COL_NO = cOL_NO;
	}

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	

	
	public String getCELLVALUE() {
		return CELLVALUE;
	}

	public void setCELLVALUE(String cELLVALUE) {
		CELLVALUE = cELLVALUE;
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

	public String getSESSIONID() {
		return SESSIONID;
	}

	public void setSESSIONID(String sESSIONID) {
		SESSIONID = sESSIONID;
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getMODALSHEET_ID() {
		return MODALSHEET_ID;
	}

	public void setMODALSHEET_ID(String mODALSHEET_ID) {
		MODALSHEET_ID = mODALSHEET_ID;
	}

	public String getENTITY_ID() {
		return ENTITY_ID;
	}

	public void setENTITY_ID(String eNTITY_ID) {
		ENTITY_ID = eNTITY_ID;
	}

	public String getPERIOD_ID() {
		return PERIOD_ID;
	}

	public void setPERIOD_ID(String pERIOD_ID) {
		PERIOD_ID = pERIOD_ID;
	}

	public String getCURRENCY_ID() {
		return CURRENCY_ID;
	}

	public void setCURRENCY_ID(String cURRENCY_ID) {
		CURRENCY_ID = cURRENCY_ID;
	}

	public String getMODALSHEET_NAME() {
		return MODALSHEET_NAME;
	}

	public void setMODALSHEET_NAME(String mODALSHEET_NAME) {
		MODALSHEET_NAME = mODALSHEET_NAME;
	}

	public String getMODALSHEET_CODE() {
		return MODALSHEET_CODE;
	}

	public void setMODALSHEET_CODE(String mODALSHEET_CODE) {
		MODALSHEET_CODE = mODALSHEET_CODE;
	}

	public String getENTITY_CODE() {
		return ENTITY_CODE;
	}

	public void setENTITY_CODE(String eNTITY_CODE) {
		ENTITY_CODE = eNTITY_CODE;
	}

	public String getCURRENCY_CODE() {
		return CURRENCY_CODE;
	}

	public void setCURRENCY_CODE(String cURRENCY_CODE) {
		CURRENCY_CODE = cURRENCY_CODE;
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

	public String getNO() {
		return NO;
	}

	public void setNO(String nO) {
		NO = nO;
	}

	public int getFLAG() {
		return FLAG;
	}

	public void setFLAG(int fLAG) {
		FLAG = fLAG;
	}

	public int getLINENO() {
		return LINENO;
	}

	public void setLINENO(int lINENO) {
		LINENO = lINENO;
	}

	public String getCELLTEXTV() {
		return CELLTEXTV;
	}

	public void setCELLTEXTV(String cELLTEXTV) {
		CELLTEXTV = cELLTEXTV;
	}

	public String getCELLV_ID() {
		return CELLV_ID;
	}

	public void setCELLV_ID(String cELLV_ID) {
		CELLV_ID = cELLV_ID;
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
	
	
}
