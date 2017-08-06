package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class ModalType {

	private String MODALTYPE_ID;
	private String SUIT_ID;
	private String MODALTYPE_CODE;
	private String MODALTYPE_NAME;
	private String PERIOD_TYPE;
	private String CHK_SCHEME_ID;
	private Timestamp CREATION_DATE; //CREATION_DATE
	private String CREATED_BY; //CREATED_BY
	private Timestamp LAST_UPDATE_DATE; //CREATION_DATE
	private String LAST_UPDATED_BY; //CREATED_BY
	
	public String getMODALTYPE_ID() {
		return MODALTYPE_ID;
	}
	public void setMODALTYPE_ID(String mODALTYPE_ID) {
		MODALTYPE_ID = mODALTYPE_ID;
	}
	public String getSUIT_ID() {
		return SUIT_ID;
	}
	public void setSUIT_ID(String sUIT_ID) {
		SUIT_ID = sUIT_ID;
	}
	public String getMODALTYPE_CODE() {
		return MODALTYPE_CODE;
	}
	public void setMODALTYPE_CODE(String mODALTYPE_CODE) {
		MODALTYPE_CODE = mODALTYPE_CODE;
	}
	public String getMODALTYPE_NAME() {
		return MODALTYPE_NAME;
	}
	public void setMODALTYPE_NAME(String mODALTYPE_NAME) {
		MODALTYPE_NAME = mODALTYPE_NAME;
	}
	public String getPERIOD_TYPE() {
		return PERIOD_TYPE;
	}
	public void setPERIOD_TYPE(String pERIOD_TYPE) {
		PERIOD_TYPE = pERIOD_TYPE;
	}
	public String getCHK_SCHEME_ID() {
		return CHK_SCHEME_ID;
	}
	public void setCHK_SCHEME_ID(String cHK_SCHEME_ID) {
		CHK_SCHEME_ID = cHK_SCHEME_ID;
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
	
}
