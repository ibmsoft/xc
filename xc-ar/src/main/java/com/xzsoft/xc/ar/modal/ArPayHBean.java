package com.xzsoft.xc.ar.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName：ArPayHBean
  * @Description：收款单主表
  * @author：任建建
  * @date：2016年7月6日 上午11:21:20
 */
public class ArPayHBean {
	private String AR_PAY_H_ID;
	private String AR_PAY_H_CODE;
	private String AR_PAY_CUSTOMER_H_ID;
	private String AR_DOC_CAT_CODE;
	private String LEDGER_ID;
	private Date GL_DATE;
	private String CUSTOMER_ID;
	private String PROJECT_ID;
	private String DEPT_ID;
	private String AR_CONTRACT_ID;
	private Double AMOUNT;
	private String SOURCE;
	private String DESCRIPTION;
	private String PAY_TYPE;
	private String PAY_NO;
	private Date PAY_DATE;
	private String SALE_ACC_ID;
	private String SALE_CCID;
	private String CA_ID;
	private String BANK_ACCOUNT;
	private String V_HEAD_ID;
	private String V_STATUS;
	private String VERIFIER_ID;
	private Date VERFY_DATE;
	private String SIGNATORY_ID;
	private String SIGNATORY;
	private String INIT;
	private Date SIGN_DATE;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	
	private List<ArPayLBean> newList;
	private List<ArPayLBean> updateList;
	private List<ArPayLBean> deleteList;
	
	public ArPayHBean(){
		
	}
	
	public String getAR_PAY_H_ID() {
		return AR_PAY_H_ID;
	}
	public void setAR_PAY_H_ID(String aR_PAY_H_ID) {
		AR_PAY_H_ID = aR_PAY_H_ID;
	}
	public String getAR_PAY_H_CODE() {
		return AR_PAY_H_CODE;
	}
	public void setAR_PAY_H_CODE(String aR_PAY_H_CODE) {
		AR_PAY_H_CODE = aR_PAY_H_CODE;
	}
	public String getAR_DOC_CAT_CODE() {
		return AR_DOC_CAT_CODE;
	}
	public void setAR_DOC_CAT_CODE(String aR_DOC_CAT_CODE) {
		AR_DOC_CAT_CODE = aR_DOC_CAT_CODE;
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
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
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
	public String getAR_CONTRACT_ID() {
		return AR_CONTRACT_ID;
	}
	public void setAR_CONTRACT_ID(String aR_CONTRACT_ID) {
		AR_CONTRACT_ID = aR_CONTRACT_ID;
	}
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getSOURCE() {
		return SOURCE;
	}
	public void setSOURCE(String sOURCE) {
		SOURCE = sOURCE;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getPAY_TYPE() {
		return PAY_TYPE;
	}
	public void setPAY_TYPE(String pAY_TYPE) {
		PAY_TYPE = pAY_TYPE;
	}
	public String getPAY_NO() {
		return PAY_NO;
	}
	public void setPAY_NO(String pAY_NO) {
		PAY_NO = pAY_NO;
	}
	public Date getPAY_DATE() {
		return PAY_DATE;
	}
	public void setPAY_DATE(Date pAY_DATE) {
		PAY_DATE = pAY_DATE;
	}
	public String getSALE_ACC_ID() {
		return SALE_ACC_ID;
	}
	public void setSALE_ACC_ID(String sALE_ACC_ID) {
		SALE_ACC_ID = sALE_ACC_ID;
	}
	public String getSALE_CCID() {
		return SALE_CCID;
	}
	public void setSALE_CCID(String sALE_CCID) {
		SALE_CCID = sALE_CCID;
	}
	public String getCA_ID() {
		return CA_ID;
	}
	public void setCA_ID(String cA_ID) {
		CA_ID = cA_ID;
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
	public String getSIGNATORY_ID() {
		return SIGNATORY_ID;
	}
	public void setSIGNATORY_ID(String sIGNATORY_ID) {
		SIGNATORY_ID = sIGNATORY_ID;
	}
	public String getSIGNATORY() {
		return SIGNATORY;
	}
	public void setSIGNATORY(String sIGNATORY) {
		SIGNATORY = sIGNATORY;
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
	public List<ArPayLBean> getNewList() {
		return newList;
	}
	public void setNewList(List<ArPayLBean> newList) {
		this.newList = newList;
	}
	public List<ArPayLBean> getUpdateList() {
		return updateList;
	}
	public void setUpdateList(List<ArPayLBean> updateList) {
		this.updateList = updateList;
	}
	public List<ArPayLBean> getDeleteList() {
		return deleteList;
	}
	public void setDeleteList(List<ArPayLBean> deleteList) {
		this.deleteList = deleteList;
	}
	public String getAR_PAY_CUSTOMER_H_ID() {
		return AR_PAY_CUSTOMER_H_ID;
	}
	public void setAR_PAY_CUSTOMER_H_ID(String aR_PAY_CUSTOMER_H_ID) {
		AR_PAY_CUSTOMER_H_ID = aR_PAY_CUSTOMER_H_ID;
	}
	public String getINIT() {
		return INIT;
	}
	public void setINIT(String iNIT) {
		INIT = iNIT;
	}
	
}
