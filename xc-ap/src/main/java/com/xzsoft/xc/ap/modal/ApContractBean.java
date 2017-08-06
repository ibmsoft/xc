package com.xzsoft.xc.ap.modal;

import java.util.Date;

/** 
* @ClassName: ApContractBean 
* @Description: 应付合同管理实体类
* @author weicw 
* @date 2016年8月19日 下午2:28:18 
* 
* 
*/
public class ApContractBean {
	private String AP_CONTRACT_ID;
	private String AP_CONTRACT_CODE;
	private String AP_CONTRACT_NAME;
	private String AP_CONTRACT_TYPE;
	private String LEDGER_ID;
	private Date START_DATE;
	private Date END_DATE;
	private String VENDOR_ID;
	private Double AMOUNT;
	private String DEPT_ID;
	private String PROJECT_ID;
	private String CONTRACT_USER;
	private String IS_CLOSE;
	private String DESCRIPTION;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	private String CONTRACT_CLASSIFY;
	private String ORG_ID;
	private Double TAX_AMT;
	private Double INV_AMOUNT;
	private int VERSION;
	private String INS_CODE;
	private String AUDIT_STATUS;
	private String AUDIT_STATUS_DESC;
	private Date AUDIT_DATE;
	private String SYS_AUDIT_STATUS;
	private String SYS_AUDIT_STATUS_DESC;
	public String getCONTRACT_CLASSIFY() {
		return CONTRACT_CLASSIFY;
	}
	public void setCONTRACT_CLASSIFY(String cONTRACT_CLASSIFY) {
		CONTRACT_CLASSIFY = cONTRACT_CLASSIFY;
	}
	public String getORG_ID() {
		return ORG_ID;
	}
	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}
	public Double getTAX_AMT() {
		return TAX_AMT;
	}
	public void setTAX_AMT(Double tAX_AMT) {
		TAX_AMT = tAX_AMT;
	}
	public Double getINV_AMOUNT() {
		return INV_AMOUNT;
	}
	public void setINV_AMOUNT(Double iNV_AMOUNT) {
		INV_AMOUNT = iNV_AMOUNT;
	}
	public int getVERSION() {
		return VERSION;
	}
	public void setVERSION(int vERSION) {
		VERSION = vERSION;
	}
	public String getINS_CODE() {
		return INS_CODE;
	}
	public void setINS_CODE(String iNS_CODE) {
		INS_CODE = iNS_CODE;
	}
	public String getAUDIT_STATUS() {
		return AUDIT_STATUS;
	}
	public void setAUDIT_STATUS(String aUDIT_STATUS) {
		AUDIT_STATUS = aUDIT_STATUS;
	}
	public String getAUDIT_STATUS_DESC() {
		return AUDIT_STATUS_DESC;
	}
	public void setAUDIT_STATUS_DESC(String aUDIT_STATUS_DESC) {
		AUDIT_STATUS_DESC = aUDIT_STATUS_DESC;
	}
	public Date getAUDIT_DATE() {
		return AUDIT_DATE;
	}
	public void setAUDIT_DATE(Date aUDIT_DATE) {
		AUDIT_DATE = aUDIT_DATE;
	}
	public ApContractBean() {
		super();
	}
	public String getAP_CONTRACT_ID() {
		return AP_CONTRACT_ID;
	}
	public void setAP_CONTRACT_ID(String aP_CONTRACT_ID) {
		AP_CONTRACT_ID = aP_CONTRACT_ID;
	}
	public String getAP_CONTRACT_CODE() {
		return AP_CONTRACT_CODE;
	}
	public void setAP_CONTRACT_CODE(String aP_CONTRACT_CODE) {
		AP_CONTRACT_CODE = aP_CONTRACT_CODE;
	}
	public String getAP_CONTRACT_NAME() {
		return AP_CONTRACT_NAME;
	}
	public void setAP_CONTRACT_NAME(String aP_CONTRACT_NAME) {
		AP_CONTRACT_NAME = aP_CONTRACT_NAME;
	}
	public String getAP_CONTRACT_TYPE() {
		return AP_CONTRACT_TYPE;
	}
	public void setAP_CONTRACT_TYPE(String aP_CONTRACT_TYPE) {
		AP_CONTRACT_TYPE = aP_CONTRACT_TYPE;
	}
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}
	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
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
	public String getVENDOR_ID() {
		return VENDOR_ID;
	}
	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
	}
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getPROJECT_ID() {
		return PROJECT_ID;
	}
	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}
	public String getCONTRACT_USER() {
		return CONTRACT_USER;
	}
	public void setCONTRACT_USER(String cONTRACT_USER) {
		CONTRACT_USER = cONTRACT_USER;
	}
	public String getIS_CLOSE() {
		return IS_CLOSE;
	}
	public void setIS_CLOSE(String iS_CLOSE) {
		IS_CLOSE = iS_CLOSE;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
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
	public String getSYS_AUDIT_STATUS() {
		return SYS_AUDIT_STATUS;
	}
	public void setSYS_AUDIT_STATUS(String sYS_AUDIT_STATUS) {
		SYS_AUDIT_STATUS = sYS_AUDIT_STATUS;
	}
	public String getSYS_AUDIT_STATUS_DESC() {
		return SYS_AUDIT_STATUS_DESC;
	}
	public void setSYS_AUDIT_STATUS_DESC(String sYS_AUDIT_STATUS_DESC) {
		SYS_AUDIT_STATUS_DESC = sYS_AUDIT_STATUS_DESC;
	}
	
}

