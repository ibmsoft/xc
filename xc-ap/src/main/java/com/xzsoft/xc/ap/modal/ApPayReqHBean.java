package com.xzsoft.xc.ap.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName: ApPayReqHBean
  * @Description: 付款申请单主表
  * @author 任建建
  * @date 2016年4月6日 下午5:27:10
 */
public class ApPayReqHBean {
	private String AP_PAY_REQ_H_ID;
	private String AP_PAY_REQ_H_CODE;
	private String AP_DOC_CAT_CODE;
	private String LEDGER_ID;
	private Date BIZ_DATE;
	private String VENDOR_ID;
	private String PROJECT_ID;
	private String DEPT_ID;
	private String AP_CONTRACT_ID;
	private Double AMOUNT;
	private Double PAID_AMT;
	private Double NO_PAY_AMT;
	private Double OCCUPY_AMT;
	private String PAY_TYPE;
	private String ACCOUNT_NAME;
	private String DEPOSIT_BANK_NAME;
	private String BANK_ACCOUNT;
	private String IS_CLOSE;
	private String DESCRIPTION;
	private String SYS_AUDIT_STATUS;
	private String SYS_AUDIT_STATUS_DESC;
	private String INS_CODE;
	private String AUDIT_STATUS;
	private String AUDIT_STATUS_DESC;
	private Date AUDIT_DATE;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	
	private List<ApPayReqLBean> apPayReqL;

	public ApPayReqHBean() {
		super();
	}

	public String getAP_PAY_REQ_H_ID() {
		return AP_PAY_REQ_H_ID;
	}

	public void setAP_PAY_REQ_H_ID(String aP_PAY_REQ_H_ID) {
		AP_PAY_REQ_H_ID = aP_PAY_REQ_H_ID;
	}

	public String getAP_PAY_REQ_H_CODE() {
		return AP_PAY_REQ_H_CODE;
	}

	public void setAP_PAY_REQ_H_CODE(String aP_PAY_REQ_H_CODE) {
		AP_PAY_REQ_H_CODE = aP_PAY_REQ_H_CODE;
	}

	public String getAP_DOC_CAT_CODE() {
		return AP_DOC_CAT_CODE;
	}

	public void setAP_DOC_CAT_CODE(String aP_DOC_CAT_CODE) {
		AP_DOC_CAT_CODE = aP_DOC_CAT_CODE;
	}

	public String getLEDGER_ID() {
		return LEDGER_ID;
	}

	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}

	public Date getBIZ_DATE() {
		return BIZ_DATE;
	}

	public void setBIZ_DATE(Date bIZ_DATE) {
		BIZ_DATE = bIZ_DATE;
	}

	public String getVENDOR_ID() {
		return VENDOR_ID;
	}

	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
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

	public String getAP_CONTRACT_ID() {
		return AP_CONTRACT_ID;
	}

	public void setAP_CONTRACT_ID(String aP_CONTRACT_ID) {
		AP_CONTRACT_ID = aP_CONTRACT_ID;
	}

	public Double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public Double getPAID_AMT() {
		return PAID_AMT;
	}

	public void setPAID_AMT(Double pAID_AMT) {
		PAID_AMT = pAID_AMT;
	}

	public Double getNO_PAY_AMT() {
		return NO_PAY_AMT;
	}

	public void setNO_PAY_AMT(Double nO_PAY_AMT) {
		NO_PAY_AMT = nO_PAY_AMT;
	}

	public Double getOCCUPY_AMT() {
		return OCCUPY_AMT;
	}

	public void setOCCUPY_AMT(Double oCCUPY_AMT) {
		OCCUPY_AMT = oCCUPY_AMT;
	}

	public String getPAY_TYPE() {
		return PAY_TYPE;
	}

	public void setPAY_TYPE(String pAY_TYPE) {
		PAY_TYPE = pAY_TYPE;
	}

	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}

	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}

	public String getDEPOSIT_BANK_NAME() {
		return DEPOSIT_BANK_NAME;
	}

	public void setDEPOSIT_BANK_NAME(String dEPOSIT_BANK_NAME) {
		DEPOSIT_BANK_NAME = dEPOSIT_BANK_NAME;
	}

	public String getBANK_ACCOUNT() {
		return BANK_ACCOUNT;
	}

	public void setBANK_ACCOUNT(String bANK_ACCOUNT) {
		BANK_ACCOUNT = bANK_ACCOUNT;
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

	public List<ApPayReqLBean> getApPayReqL() {
		return apPayReqL;
	}

	public void setApPayReqL(List<ApPayReqLBean> apPayReqL) {
		this.apPayReqL = apPayReqL;
	}
}
