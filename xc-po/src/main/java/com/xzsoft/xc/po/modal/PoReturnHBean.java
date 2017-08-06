package com.xzsoft.xc.po.modal;

import java.util.Date;

/** 
 * @ClassName: PoReturnHBean 
 * @Description: 退货通知单表实体类
 * @author weicw 
 * @date 2016年11月29日 上午9:30:59 
 * 
 * 
 */
public class PoReturnHBean {
	private String RETURN_H_ID;
	private String RETURN_H_CODE;
	private String RETURN_H_NAME;
	private String PO_DOC_CAT_CODE;
	private String ENTRY_H_ID;
	private String ORDER_H_ID;
	private String CONTRACT_ID;
	private String VENDOR_ID;
	private String DEPT_ID;
	private String PROJECT_ID;
	private Date ARRIVAL_DATE;
	private Date DOCUMENT_DATE;
	private String PURCHASE_PERSON;
	private String WAREHOUSE_ID;
	private Double AMOUNT;
	private Double INV_AMOUNT;
	private String ORG_ID;
	private String LEDGER_ID;
	private String INS_CODE;
	private String AUDIT_STATUS;
	private String AUDIT_STATUS_DESC;
	private Date AUDIT_DATE;
	private String SYS_AUDIT_STATUS;
	private String SYS_AUDIT_STATUS_DESC;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public PoReturnHBean() {
		super();
	}
	public String getRETURN_H_ID() {
		return RETURN_H_ID;
	}
	public void setRETURN_H_ID(String rETURN_H_ID) {
		RETURN_H_ID = rETURN_H_ID;
	}
	public String getRETURN_H_CODE() {
		return RETURN_H_CODE;
	}
	public void setRETURN_H_CODE(String rETURN_H_CODE) {
		RETURN_H_CODE = rETURN_H_CODE;
	}
	public String getRETURN_H_NAME() {
		return RETURN_H_NAME;
	}
	public void setRETURN_H_NAME(String rETURN_H_NAME) {
		RETURN_H_NAME = rETURN_H_NAME;
	}
	public String getENTRY_H_ID() {
		return ENTRY_H_ID;
	}
	public void setENTRY_H_ID(String eNTRY_H_ID) {
		ENTRY_H_ID = eNTRY_H_ID;
	}
	public String getORDER_H_ID() {
		return ORDER_H_ID;
	}
	public void setORDER_H_ID(String oRDER_H_ID) {
		ORDER_H_ID = oRDER_H_ID;
	}
	public String getCONTRACT_ID() {
		return CONTRACT_ID;
	}
	public void setCONTRACT_ID(String cONTRACT_ID) {
		CONTRACT_ID = cONTRACT_ID;
	}
	public String getVENDOR_ID() {
		return VENDOR_ID;
	}
	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
	}
	public Date getARRIVAL_DATE() {
		return ARRIVAL_DATE;
	}
	public void setARRIVAL_DATE(Date aRRIVAL_DATE) {
		ARRIVAL_DATE = aRRIVAL_DATE;
	}
	public String getPURCHASE_PERSON() {
		return PURCHASE_PERSON;
	}
	public void setPURCHASE_PERSON(String pURCHASE_PERSON) {
		PURCHASE_PERSON = pURCHASE_PERSON;
	}
	public String getWAREHOUSE_ID() {
		return WAREHOUSE_ID;
	}
	public void setWAREHOUSE_ID(String wAREHOUSE_ID) {
		WAREHOUSE_ID = wAREHOUSE_ID;
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
	public String getPO_DOC_CAT_CODE() {
		return PO_DOC_CAT_CODE;
	}
	public void setPO_DOC_CAT_CODE(String pO_DOC_CAT_CODE) {
		PO_DOC_CAT_CODE = pO_DOC_CAT_CODE;
	}
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public Date getDOCUMENT_DATE() {
		return DOCUMENT_DATE;
	}
	public void setDOCUMENT_DATE(Date dOCUMENT_DATE) {
		DOCUMENT_DATE = dOCUMENT_DATE;
	}
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public Double getINV_AMOUNT() {
		return INV_AMOUNT;
	}
	public void setINV_AMOUNT(Double iNV_AMOUNT) {
		INV_AMOUNT = iNV_AMOUNT;
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
	public String getPROJECT_ID() {
		return PROJECT_ID;
	}
	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}
	
}

