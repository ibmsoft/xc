package com.xzsoft.xc.po.modal;

import java.util.Date;

/** 
 * @ClassName: PoPurApHBean 
 * @Description: 采购申请主表实体类
 * @author weicw 
 * @date 2016年11月29日 上午9:19:14 
 * 
 * 
 */
public class PoPurApHBean {
	private String PUR_AP_H_ID;
	private String ORG_ID;
	private String LEDGER_ID;
	private String PUR_AP_H_CODE;
	private String PUR_AP_H_NAME;
	private String PO_DOC_CAT_CODE;
	private String PROJECT_ID;
	private String DEPT_ID;
	private Double AMOUNT;
	private String IS_CLOSE;
	private String INS_CODE;
	private String AUDIT_STATUS;
	private String AUDIT_STATUS_DESC;
	private String SYS_AUDIT_STATUS;
	private String SYS_AUDIT_STATUS_DESC;
	private Date AUDIT_DATE;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	private Date DOCUMENT_DATE;
	public PoPurApHBean() {
		super();
	}
	public String getPUR_AP_H_ID() {
		return PUR_AP_H_ID;
	}
	public void setPUR_AP_H_ID(String pUR_AP_H_ID) {
		PUR_AP_H_ID = pUR_AP_H_ID;
	}
	public String getORG_ID() {
		return ORG_ID;
	}
	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}
	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}
	public String getPUR_AP_H_CODE() {
		return PUR_AP_H_CODE;
	}
	public void setPUR_AP_H_CODE(String pUR_AP_H_CODE) {
		PUR_AP_H_CODE = pUR_AP_H_CODE;
	}
	public String getPUR_AP_H_NAME() {
		return PUR_AP_H_NAME;
	}
	public void setPUR_AP_H_NAME(String pUR_AP_H_NAME) {
		PUR_AP_H_NAME = pUR_AP_H_NAME;
	}
	public String getPROJECT_ID() {
		return PROJECT_ID;
	}
	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
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
	public Date getDOCUMENT_DATE() {
		return DOCUMENT_DATE;
	}
	public void setDOCUMENT_DATE(Date dOCUMENT_DATE) {
		DOCUMENT_DATE = dOCUMENT_DATE;
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
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getIS_CLOSE() {
		return IS_CLOSE;
	}
	public void setIS_CLOSE(String iS_CLOSE) {
		IS_CLOSE = iS_CLOSE;
	}
	public String getPO_DOC_CAT_CODE() {
		return PO_DOC_CAT_CODE;
	}
	public void setPO_DOC_CAT_CODE(String pO_DOC_CAT_CODE) {
		PO_DOC_CAT_CODE = pO_DOC_CAT_CODE;
	}
	
}

