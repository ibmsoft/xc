package com.xzsoft.xc.ap.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName: ApPayHBean
  * @Description: 付款单主表
  * @author 任建建
  * @date 2016年4月6日 下午5:28:02
 */
public class ApPayHBean {
	private String AP_PAY_H_ID;
	private String AP_PAY_H_CODE;
	private String AP_PAY_VENDOR_H_ID;
	private String AP_DOC_CAT_CODE;
	private String LEDGER_ID;
	private Date GL_DATE;
	private String VENDOR_ID;
	private String PROJECT_ID;
	private String DEPT_ID;
	private String AP_CONTRACT_ID;
	private double AMOUNT;
	private String SOURCE;
	private String PAY_TYPE;
	private String DEPOSIT_BANK_ID;
	private String PAY_ACC_ID;
	private String PAY_CCID;
	private String CA_ID;
	private String ACCOUNT_NAME;
	private String DEPOSIT_BANK_NAME;
	private String BANK_ACCOUNT;
	private String V_HEAD_ID;
	private String V_STATUS;
	private String DESCRIPTION;
	private String INIT;
	private String VERIFIER_ID;
	private Date VERFY_DATE;
	private String SIGN_USER_ID;
	private Date SIGN_DATE;
	private String SIGN_STATUS;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	
	private List<ApPayLBean> apPayL;

	public ApPayHBean() {
		super();
	}

	public String getDEPOSIT_BANK_ID() {
		return DEPOSIT_BANK_ID;
	}

	public void setDEPOSIT_BANK_ID(String dEPOSIT_BANK_ID) {
		DEPOSIT_BANK_ID = dEPOSIT_BANK_ID;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getSIGN_USER_ID() {
		return SIGN_USER_ID;
	}

	public void setSIGN_USER_ID(String sIGN_USER_ID) {
		SIGN_USER_ID = sIGN_USER_ID;
	}

	public String getSIGN_STATUS() {
		return SIGN_STATUS;
	}

	public void setSIGN_STATUS(String sIGN_STATUS) {
		SIGN_STATUS = sIGN_STATUS;
	}

	public String getAP_PAY_H_ID() {
		return AP_PAY_H_ID;
	}

	public void setAP_PAY_H_ID(String aP_PAY_H_ID) {
		AP_PAY_H_ID = aP_PAY_H_ID;
	}

	public String getAP_PAY_H_CODE() {
		return AP_PAY_H_CODE;
	}

	public void setAP_PAY_H_CODE(String aP_PAY_H_CODE) {
		AP_PAY_H_CODE = aP_PAY_H_CODE;
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

	public Date getGL_DATE() {
		return GL_DATE;
	}

	public void setGL_DATE(Date gL_DATE) {
		GL_DATE = gL_DATE;
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

	public double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public String getSOURCE() {
		return SOURCE;
	}

	public void setSOURCE(String sOURCE) {
		SOURCE = sOURCE;
	}

	public String getPAY_TYPE() {
		return PAY_TYPE;
	}

	public void setPAY_TYPE(String pAY_TYPE) {
		PAY_TYPE = pAY_TYPE;
	}

	public String getPAY_ACC_ID() {
		return PAY_ACC_ID;
	}

	public void setPAY_ACC_ID(String pAY_ACC_ID) {
		PAY_ACC_ID = pAY_ACC_ID;
	}

	public String getPAY_CCID() {
		return PAY_CCID;
	}

	public void setPAY_CCID(String pAY_CCID) {
		PAY_CCID = pAY_CCID;
	}

	public String getCA_ID() {
		return CA_ID;
	}

	public void setCA_ID(String cA_ID) {
		CA_ID = cA_ID;
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

	public String getV_HEAD_ID() {
		return V_HEAD_ID;
	}

	public void setV_HEAD_ID(String v_HEAD_ID) {
		V_HEAD_ID = v_HEAD_ID;
	}

	public String getV_STATUS() {
		return V_STATUS;
	}

	public void setV_STATUS(String v_STATUS) {
		V_STATUS = v_STATUS;
	}

	public String getVERIFIER_ID() {
		return VERIFIER_ID;
	}

	public void setVERIFIER_ID(String vERIFIER_ID) {
		VERIFIER_ID = vERIFIER_ID;
	}

	public Date getVERFY_DATE() {
		return VERFY_DATE;
	}

	public void setVERFY_DATE(Date vERFY_DATE) {
		VERFY_DATE = vERFY_DATE;
	}

	public Date getSIGN_DATE() {
		return SIGN_DATE;
	}

	public void setSIGN_DATE(Date sIGN_DATE) {
		SIGN_DATE = sIGN_DATE;
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

	public List<ApPayLBean> getApPayL() {
		return apPayL;
	}

	public void setApPayL(List<ApPayLBean> apPayL) {
		this.apPayL = apPayL;
	}

	public String getAP_PAY_VENDOR_H_ID() {
		return AP_PAY_VENDOR_H_ID;
	}

	public void setAP_PAY_VENDOR_H_ID(String aP_PAY_VENDOR_H_ID) {
		AP_PAY_VENDOR_H_ID = aP_PAY_VENDOR_H_ID;
	}

	public String getINIT() {
		return INIT;
	}

	public void setINIT(String iNIT) {
		INIT = iNIT;
	}
	
}
