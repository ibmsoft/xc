package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class Item_Property {
	
	private String ITEM_PRO_ID;
	private String SUIT_ID;
	private String MODALTYPE_ID;
	private String MODALSHEET_ID;
	private String ROWITEM_ID;
	private String ROWITEM_CODE;
	private String COLITEM_ID;
	private String COLITEM_CODE;
	private String PRO_TYPE;
	private String PRO_VALUE;
	private String ATTR1;
	private String ATTR2;
	private String ATTR3;
	private String ATTR4;
	private String ATTR5;
	private String ORDERBY;
	private Timestamp CREATION_DATE;
	private String CREATED_BY; 
	private Timestamp LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public String getITEM_PRO_ID() {
		return ITEM_PRO_ID;
	}
	public void setITEM_PRO_ID(String iTEM_PRO_ID) {
		ITEM_PRO_ID = iTEM_PRO_ID;
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
	public String getCOLITEM_ID() {
		return COLITEM_ID;
	}
	public void setCOLITEM_ID(String cOLITEM_ID) {
		COLITEM_ID = cOLITEM_ID;
	}
	public String getPRO_TYPE() {
		return PRO_TYPE;
	}
	public void setPRO_TYPE(String pRO_TYPE) {
		PRO_TYPE = pRO_TYPE;
	}
	public String getPRO_VALUE() {
		return PRO_VALUE;
	}
	public void setPRO_VALUE(String pRO_VALUE) {
		PRO_VALUE = pRO_VALUE;
	}
	
	public String getATTR1() {
		return ATTR1;
	}
	public void setATTR1(String aTTR1) {
		ATTR1 = aTTR1;
	}
	public String getATTR2() {
		return ATTR2;
	}
	public void setATTR2(String aTTR2) {
		ATTR2 = aTTR2;
	}
	public String getATTR3() {
		return ATTR3;
	}
	public void setATTR3(String aTTR3) {
		ATTR3 = aTTR3;
	}
	public String getATTR4() {
		return ATTR4;
	}
	public void setATTR4(String aTTR4) {
		ATTR4 = aTTR4;
	}
	public String getATTR5() {
		return ATTR5;
	}
	public void setATTR5(String aTTR5) {
		ATTR5 = aTTR5;
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
	public String getROWITEM_CODE() {
		return ROWITEM_CODE;
	}
	public void setROWITEM_CODE(String rOWITEM_CODE) {
		ROWITEM_CODE = rOWITEM_CODE;
	}
	public String getCOLITEM_CODE() {
		return COLITEM_CODE;
	}
	public void setCOLITEM_CODE(String cOLITEM_CODE) {
		COLITEM_CODE = cOLITEM_CODE;
	}
	@Override
	public String toString() {
		return "Item_Property [ITEM_PRO_ID=" + ITEM_PRO_ID + ", SUIT_ID=" + SUIT_ID + ", MODALTYPE_ID=" + MODALTYPE_ID
				+ ", MODALSHEET_ID=" + MODALSHEET_ID + ", ROWITEM_ID=" + ROWITEM_ID + ", COLITEM_ID=" + COLITEM_ID
				+ ", PRO_TYPE=" + PRO_TYPE + ", PRO_VALUE=" + PRO_VALUE + ", ATTR1=" + ATTR1 + ", ATTR2=" + ATTR2
				+ ", ATTR3=" + ATTR3 + ", ATTR4=" + ATTR4 + ", ATTR5=" + ATTR5 + ", ORDERBY=" + ORDERBY
				+ ", CREATION_DATE=" + CREATION_DATE + ", CREATED_BY=" + CREATED_BY + ", LAST_UPDATE_DATE="
				+ LAST_UPDATE_DATE + ", LAST_UPDATED_BY=" + LAST_UPDATED_BY + "]";
	}
	
}
