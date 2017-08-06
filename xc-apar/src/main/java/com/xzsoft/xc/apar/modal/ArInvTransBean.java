package com.xzsoft.xc.apar.modal;

import java.util.Date;

/**
 * 
  * @ClassName：ArInvTransBean
  * @Description：应收模块交易明细表
  * @author：任建建
  * @date：2016年7月6日 下午1:00:44
 */
public class ArInvTransBean {
	private String TRANS_ID;
	private String SOURCE_ID;
	private String SOURCE_DTL_ID;
	private String AR_INV_GL_H_ID;
	private String AR_PAY_H_ID;
	private Date GL_DATE;
	private String SOURCE_TAB;
	private String AR_DOC_CAT_CODE;
	private String AR_DOC_CODE;
	private String DESCRIPTION;
	private String AR_CONTRACT_ID;
	private String CUSTOMER_ID;
	private double DR_AMT;
	private double CR_AMT;
	private double AMOUNT;
	private String TRANS_STATUS;
	private String CCID;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public ArInvTransBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ArInvTransBean(String tRANS_ID, String sOURCE_ID,
			String sOURCE_DTL_ID, String aR_INV_GL_H_ID, String aR_PAY_H_ID,
			Date gL_DATE, String sOURCE_TAB, String aR_DOC_CAT_CODE,
			String aR_CONTRACT_ID, String cUSTOMER_ID, double dR_AMT,
			double cR_AMT, double aMOUNT, String tRANS_STATUS, String cCID,
			Date cREATION_DATE, String cREATED_BY, Date lAST_UPDATE_DATE,
			String lAST_UPDATED_BY) {
		super();
		TRANS_ID = tRANS_ID;
		SOURCE_ID = sOURCE_ID;
		SOURCE_DTL_ID = sOURCE_DTL_ID;
		AR_INV_GL_H_ID = aR_INV_GL_H_ID;
		AR_PAY_H_ID = aR_PAY_H_ID;
		GL_DATE = gL_DATE;
		SOURCE_TAB = sOURCE_TAB;
		AR_DOC_CAT_CODE = aR_DOC_CAT_CODE;
		AR_CONTRACT_ID = aR_CONTRACT_ID;
		CUSTOMER_ID = cUSTOMER_ID;
		DR_AMT = dR_AMT;
		CR_AMT = cR_AMT;
		AMOUNT = aMOUNT;
		TRANS_STATUS = tRANS_STATUS;
		CCID = cCID;
		CREATION_DATE = cREATION_DATE;
		CREATED_BY = cREATED_BY;
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}
	public String getTRANS_ID() {
		return TRANS_ID;
	}
	public void setTRANS_ID(String tRANS_ID) {
		TRANS_ID = tRANS_ID;
	}
	public String getSOURCE_ID() {
		return SOURCE_ID;
	}
	public void setSOURCE_ID(String sOURCE_ID) {
		SOURCE_ID = sOURCE_ID;
	}
	public String getSOURCE_DTL_ID() {
		return SOURCE_DTL_ID;
	}
	public void setSOURCE_DTL_ID(String sOURCE_DTL_ID) {
		SOURCE_DTL_ID = sOURCE_DTL_ID;
	}
	public String getAR_INV_GL_H_ID() {
		return AR_INV_GL_H_ID;
	}
	public void setAR_INV_GL_H_ID(String aR_INV_GL_H_ID) {
		AR_INV_GL_H_ID = aR_INV_GL_H_ID;
	}
	public String getAR_PAY_H_ID() {
		return AR_PAY_H_ID;
	}
	public void setAR_PAY_H_ID(String aR_PAY_H_ID) {
		AR_PAY_H_ID = aR_PAY_H_ID;
	}
	public Date getGL_DATE() {
		return GL_DATE;
	}
	public void setGL_DATE(Date gL_DATE) {
		GL_DATE = gL_DATE;
	}
	public String getSOURCE_TAB() {
		return SOURCE_TAB;
	}
	public void setSOURCE_TAB(String sOURCE_TAB) {
		SOURCE_TAB = sOURCE_TAB;
	}
	public String getAR_DOC_CAT_CODE() {
		return AR_DOC_CAT_CODE;
	}
	public void setAR_DOC_CAT_CODE(String aR_DOC_CAT_CODE) {
		AR_DOC_CAT_CODE = aR_DOC_CAT_CODE;
	}
	public String getAR_DOC_CODE() {
		return AR_DOC_CODE;
	}
	public void setAR_DOC_CODE(String aR_DOC_CODE) {
		AR_DOC_CODE = aR_DOC_CODE;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getAR_CONTRACT_ID() {
		return AR_CONTRACT_ID;
	}
	public void setAR_CONTRACT_ID(String aR_CONTRACT_ID) {
		AR_CONTRACT_ID = aR_CONTRACT_ID;
	}
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
	}
	public double getDR_AMT() {
		return DR_AMT;
	}
	public void setDR_AMT(double dR_AMT) {
		DR_AMT = dR_AMT;
	}
	public double getCR_AMT() {
		return CR_AMT;
	}
	public void setCR_AMT(double cR_AMT) {
		CR_AMT = cR_AMT;
	}
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getTRANS_STATUS() {
		return TRANS_STATUS;
	}
	public void setTRANS_STATUS(String tRANS_STATUS) {
		TRANS_STATUS = tRANS_STATUS;
	}
	public String getCCID() {
		return CCID;
	}
	public void setCCID(String cCID) {
		CCID = cCID;
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
