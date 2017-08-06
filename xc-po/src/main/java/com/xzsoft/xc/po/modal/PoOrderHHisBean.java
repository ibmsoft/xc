package com.xzsoft.xc.po.modal;

import java.util.Date;

/** 
 * @ClassName: PoOrderHHisBean 
 * @Description: 采购订单历史实体类
 * @author weicw 
 * @date 2016年11月28日 下午5:12:13 
 * 
 * 
 */
public class PoOrderHHisBean {
	private String ORDER_H_HIS_ID;
	private String ORDER_H_ID;
	private String ORDER_H_CODE;
	private String ORDER_H_NAME;
	private String ORDER_H_TYPE;
	private String PO_DOC_CAT_CODE;
	private String CONTRACT_ID;
	private String VENDOR_ID;
	private String DEPT_ID;
	private String PURCHASE_DEPT_ID;
	private String PURCHASE_PERSON;
	private String PROJECT_ID;
	private int VERSION;
	private String ORG_ID;
	private String LEDGER_ID;
	private String IS_CLOSE;
	private Double AMOUNT;
	private Double INV_AMOUNT;
	private String INS_CODE;
	private String AUDIT_STATUS;
	private String AUDIT_STATUS_DESC;
	private String SYS_AUDIT_STATUS;
	private String SYS_AUDIT_STATUS_DESC;
	private Date AUDIT_DATE;
	private Date DOCUMENT_DATE;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public PoOrderHHisBean() {
		super();
	}
	
	public String getORDER_H_HIS_ID() {
		return ORDER_H_HIS_ID;
	}

	public void setORDER_H_HIS_ID(String oRDER_H_HIS_ID) {
		ORDER_H_HIS_ID = oRDER_H_HIS_ID;
	}

	public String getORDER_H_ID() {
		return ORDER_H_ID;
	}
	public void setORDER_H_ID(String oRDER_H_ID) {
		ORDER_H_ID = oRDER_H_ID;
	}
	public String getORDER_H_CODE() {
		return ORDER_H_CODE;
	}
	public void setORDER_H_CODE(String oRDER_H_CODE) {
		ORDER_H_CODE = oRDER_H_CODE;
	}
	public String getORDER_H_NAME() {
		return ORDER_H_NAME;
	}
	public void setORDER_H_NAME(String oRDER_H_NAME) {
		ORDER_H_NAME = oRDER_H_NAME;
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

	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getPURCHASE_DEPT_ID() {
		return PURCHASE_DEPT_ID;
	}
	public void setPURCHASE_DEPT_ID(String pURCHASE_DEPT_ID) {
		PURCHASE_DEPT_ID = pURCHASE_DEPT_ID;
	}
	public String getPURCHASE_PERSON() {
		return PURCHASE_PERSON;
	}
	public void setPURCHASE_PERSON(String pURCHASE_PERSON) {
		PURCHASE_PERSON = pURCHASE_PERSON;
	}
	public String getPROJECT_ID() {
		return PROJECT_ID;
	}
	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}
	
	public int getVERSION() {
		return VERSION;
	}

	public void setVERSION(int vERSION) {
		VERSION = vERSION;
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
	public Date getDOCUMENT_DATE() {
		return DOCUMENT_DATE;
	}
	public void setDOCUMENT_DATE(Date dOCUMENT_DATE) {
		DOCUMENT_DATE = dOCUMENT_DATE;
	}

	public String getORDER_H_TYPE() {
		return ORDER_H_TYPE;
	}

	public void setORDER_H_TYPE(String oRDER_H_TYPE) {
		ORDER_H_TYPE = oRDER_H_TYPE;
	}

	public String getPO_DOC_CAT_CODE() {
		return PO_DOC_CAT_CODE;
	}

	public void setPO_DOC_CAT_CODE(String pO_DOC_CAT_CODE) {
		PO_DOC_CAT_CODE = pO_DOC_CAT_CODE;
	}

	public String getIS_CLOSE() {
		return IS_CLOSE;
	}

	public void setIS_CLOSE(String iS_CLOSE) {
		IS_CLOSE = iS_CLOSE;
	}
	
}

