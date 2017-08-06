package com.xzsoft.xc.posost.modal;

import java.util.Date;

/**
 * 
  * @ClassName XcStTransBean
  * @Description 库存事务处理记录
  * @author RenJianJian
  * @date 2016年12月2日 下午2:45:23
 */
public class XcStTransBean {
	private String TRANS_ID;
	private String MATERIAL_ID;
	private String ENTRY_H_ID;
	private String ENTRY_L_ID;
	private String BUSINESS_H_ID;
	private String BUSINESS_L_ID;
	private String BUSINESS_TYPE_ID;
	private String DOC_CAT_CODE;
	private double QTY;
	private double PRICE;
	private double AMOUNT;
	private double ORIGINAL_QTY;
	private double ORIGINAL_PRICE;
	private double ORIGINAL_AMOUNT;
	private double NEWEST_QTY;
	private double NEWEST_PRICE;
	private double NEWEST_AMOUNT;
	private int COEFFICIENT;
	private String WAREHOUSE_ID;
	private String PERIOD_CODE;
	private String STATUS;
	private String LOCATIONS;
	private String SERIALS;
	private String LEDGER_ID;
	private String ORG_ID;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public XcStTransBean() {
		super();
	}
	public String getTRANS_ID() {
		return TRANS_ID;
	}
	public void setTRANS_ID(String tRANS_ID) {
		TRANS_ID = tRANS_ID;
	}
	public String getMATERIAL_ID() {
		return MATERIAL_ID;
	}
	public void setMATERIAL_ID(String mATERIAL_ID) {
		MATERIAL_ID = mATERIAL_ID;
	}
	public String getENTRY_H_ID() {
		return ENTRY_H_ID;
	}
	public void setENTRY_H_ID(String eNTRY_H_ID) {
		ENTRY_H_ID = eNTRY_H_ID;
	}
	public String getENTRY_L_ID() {
		return ENTRY_L_ID;
	}
	public void setENTRY_L_ID(String eNTRY_L_ID) {
		ENTRY_L_ID = eNTRY_L_ID;
	}
	public String getBUSINESS_H_ID() {
		return BUSINESS_H_ID;
	}
	public void setBUSINESS_H_ID(String bUSINESS_H_ID) {
		BUSINESS_H_ID = bUSINESS_H_ID;
	}
	public String getBUSINESS_L_ID() {
		return BUSINESS_L_ID;
	}
	public void setBUSINESS_L_ID(String bUSINESS_L_ID) {
		BUSINESS_L_ID = bUSINESS_L_ID;
	}
	public String getBUSINESS_TYPE_ID() {
		return BUSINESS_TYPE_ID;
	}
	public void setBUSINESS_TYPE_ID(String bUSINESS_TYPE_ID) {
		BUSINESS_TYPE_ID = bUSINESS_TYPE_ID;
	}
	public String getDOC_CAT_CODE() {
		return DOC_CAT_CODE;
	}
	public void setDOC_CAT_CODE(String dOC_CAT_CODE) {
		DOC_CAT_CODE = dOC_CAT_CODE;
	}
	public double getQTY() {
		return QTY;
	}
	public void setQTY(double qTY) {
		QTY = qTY;
	}
	public double getPRICE() {
		return PRICE;
	}
	public void setPRICE(double pRICE) {
		PRICE = pRICE;
	}
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public double getORIGINAL_QTY() {
		return ORIGINAL_QTY;
	}
	public void setORIGINAL_QTY(double oRIGINAL_QTY) {
		ORIGINAL_QTY = oRIGINAL_QTY;
	}
	public double getORIGINAL_PRICE() {
		return ORIGINAL_PRICE;
	}
	public void setORIGINAL_PRICE(double oRIGINAL_PRICE) {
		ORIGINAL_PRICE = oRIGINAL_PRICE;
	}
	public double getORIGINAL_AMOUNT() {
		return ORIGINAL_AMOUNT;
	}
	public void setORIGINAL_AMOUNT(double oRIGINAL_AMOUNT) {
		ORIGINAL_AMOUNT = oRIGINAL_AMOUNT;
	}
	public double getNEWEST_QTY() {
		return NEWEST_QTY;
	}
	public void setNEWEST_QTY(double nEWEST_QTY) {
		NEWEST_QTY = nEWEST_QTY;
	}
	public double getNEWEST_PRICE() {
		return NEWEST_PRICE;
	}
	public void setNEWEST_PRICE(double nEWEST_PRICE) {
		NEWEST_PRICE = nEWEST_PRICE;
	}
	public double getNEWEST_AMOUNT() {
		return NEWEST_AMOUNT;
	}
	public void setNEWEST_AMOUNT(double nEWEST_AMOUNT) {
		NEWEST_AMOUNT = nEWEST_AMOUNT;
	}
	public int getCOEFFICIENT() {
		return COEFFICIENT;
	}
	public void setCOEFFICIENT(int cOEFFICIENT) {
		COEFFICIENT = cOEFFICIENT;
	}
	public String getWAREHOUSE_ID() {
		return WAREHOUSE_ID;
	}
	public void setWAREHOUSE_ID(String wAREHOUSE_ID) {
		WAREHOUSE_ID = wAREHOUSE_ID;
	}
	public String getPERIOD_CODE() {
		return PERIOD_CODE;
	}
	public void setPERIOD_CODE(String pERIOD_CODE) {
		PERIOD_CODE = pERIOD_CODE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getLOCATIONS() {
		return LOCATIONS;
	}
	public void setLOCATIONS(String lOCATIONS) {
		LOCATIONS = lOCATIONS;
	}
	public String getSERIALS() {
		return SERIALS;
	}
	public void setSERIALS(String sERIALS) {
		SERIALS = sERIALS;
	}
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}
	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}
	public String getORG_ID() {
		return ORG_ID;
	}
	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}
	public Date getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Date cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	public Date getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}
	public void setLAST_UPDATE_DATE(Date lAST_UPDATE_DATE) {
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
	}
	public String getLAST_UPDATED_BY() {
		return LAST_UPDATED_BY;
	}
	public void setLAST_UPDATED_BY(String lAST_UPDATED_BY) {
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}
}
