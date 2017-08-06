package com.xzsoft.xc.apar.modal;

import java.util.Date;

/**
 * 
  * @ClassName: ApInvTransBean
  * @Description: 交易明细表
  * @author 韦才文
  * @date 2016年6月14日 下午5:28:48
 */
public class ApInvTransBean {
	private String TRANS_ID;
	private String SOURCE_ID;
	private String SOURCE_DTL_ID;
	private String AP_INV_GL_H_ID;
	private String AP_PAY_H_ID;
	private Date GL_DATE;
	private String SOURCE_TAB;
	private String AP_DOC_CAT_CODE;
	private String AP_DOC_CODE;
	private String DESCRIPTION;
	private String AP_CONTRACT_ID;
	private String VENDOR_ID;
	private double DR_AMT;
	private double CR_AMT;
	private double REQ_AMT;
	private String TRANS_STATUS;
	private String CCID;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	
	public ApInvTransBean() {
		super();
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

	public String getAP_INV_GL_H_ID() {
		return AP_INV_GL_H_ID;
	}

	public void setAP_INV_GL_H_ID(String aP_INV_GL_H_ID) {
		AP_INV_GL_H_ID = aP_INV_GL_H_ID;
	}

	public String getAP_PAY_H_ID() {
		return AP_PAY_H_ID;
	}

	public void setAP_PAY_H_ID(String aP_PAY_H_ID) {
		AP_PAY_H_ID = aP_PAY_H_ID;
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

	public String getAP_DOC_CAT_CODE() {
		return AP_DOC_CAT_CODE;
	}

	public void setAP_DOC_CAT_CODE(String aP_DOC_CAT_CODE) {
		AP_DOC_CAT_CODE = aP_DOC_CAT_CODE;
	}

	public String getAP_DOC_CODE() {
		return AP_DOC_CODE;
	}

	public void setAP_DOC_CODE(String aP_DOC_CODE) {
		AP_DOC_CODE = aP_DOC_CODE;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getAP_CONTRACT_ID() {
		return AP_CONTRACT_ID;
	}

	public void setAP_CONTRACT_ID(String aP_CONTRACT_ID) {
		AP_CONTRACT_ID = aP_CONTRACT_ID;
	}

	public String getVENDOR_ID() {
		return VENDOR_ID;
	}

	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
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

	public double getREQ_AMT() {
		return REQ_AMT;
	}

	public void setREQ_AMT(double rEQ_AMT) {
		REQ_AMT = rEQ_AMT;
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
