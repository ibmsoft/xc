package com.xzsoft.xc.ap.modal;

import java.sql.Date;

/** 
 * @ClassName: ApVendorBean 
 * @Description: 应付供应商实体类
 * @author weicw 
 * @date 2016年11月23日 下午5:22:32 
 * 
 * 
 */
public class ApVendorBean {
	private String VENDOR_ID;
	private String VENDOR_CODE;
	private String VENDOR_NAME;
	private String TAX_NO;
	private String ADDRESS;
	private String VENDOR_TYPE;
	private String CONTACT;
	private String TEL;
	private String MOBILE;
	private String QQ;
	private String WECHAT;
	private String EMAIL;
	private String CUSTOMER_ID;
	private Date START_DATE;
	private Date END_DATE;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	
	public ApVendorBean() {
		super();
	}
	public String getVENDOR_ID() {
		return VENDOR_ID;
	}
	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
	}
	public String getVENDOR_CODE() {
		return VENDOR_CODE;
	}
	public void setVENDOR_CODE(String vENDOR_CODE) {
		VENDOR_CODE = vENDOR_CODE;
	}
	public String getVENDOR_NAME() {
		return VENDOR_NAME;
	}
	public void setVENDOR_NAME(String vENDOR_NAME) {
		VENDOR_NAME = vENDOR_NAME;
	}
	public String getTAX_NO() {
		return TAX_NO;
	}
	public void setTAX_NO(String tAX_NO) {
		TAX_NO = tAX_NO;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getVENDOR_TYPE() {
		return VENDOR_TYPE;
	}
	public void setVENDOR_TYPE(String vENDOR_TYPE) {
		VENDOR_TYPE = vENDOR_TYPE;
	}
	public String getCONTACT() {
		return CONTACT;
	}
	public void setCONTACT(String cONTACT) {
		CONTACT = cONTACT;
	}
	public String getTEL() {
		return TEL;
	}
	public void setTEL(String tEL) {
		TEL = tEL;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getQQ() {
		return QQ;
	}
	public void setQQ(String qQ) {
		QQ = qQ;
	}
	public String getWECHAT() {
		return WECHAT;
	}
	public void setWECHAT(String wECHAT) {
		WECHAT = wECHAT;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
	}
	public Date getSTART_DATE() {
		return START_DATE;
	}
	public void setSTART_DATE(Date sTART_DATE) {
		START_DATE = sTART_DATE;
	}
	public Date getEND_DATE() {
		return END_DATE;
	}
	public void setEND_DATE(Date eND_DATE) {
		END_DATE = eND_DATE;
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

