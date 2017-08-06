package com.xzsoft.xc.ap.modal;

import java.util.Date;

/**
 * 
  * @ClassName: ApPayLBean
  * @Description: 付款单行表
  * @author 任建建
  * @date 2016年4月6日 下午5:28:08
 */
public class ApPayLBean {
	private String AP_PAY_L_ID;
	private String AP_PAY_H_ID;
	private String AP_PAY_REQ_H_ID;
	private String AP_PAY_REQ_L_ID;
	private String AP_INV_GL_H_ID;
	private String AP_PUR_TYPE_ID;
	private String L_AP_PAY_H_ID;
	private String L_AP_PAY_L_ID;
	private String PROJECT_ID;
	private String DEPT_ID;
	private String AP_CONTRACT_ID;
	private String AP_DOC_CAT_CODE;
	private String BG_ITEM_ID;
	private double AMOUNT;
	private double CANCEL_AMT;
	private double NO_CANCEL_AMT;
	private String ACC_ID;
	private String CCID;
	private String DESCRIPTION;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public ApPayLBean() {
		super();
	}
	public String getAP_INV_GL_H_ID() {
		return AP_INV_GL_H_ID;
	}
	public void setAP_INV_GL_H_ID(String aP_INV_GL_H_ID) {
		AP_INV_GL_H_ID = aP_INV_GL_H_ID;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getAP_PAY_L_ID() {
		return AP_PAY_L_ID;
	}
	public void setAP_PAY_L_ID(String aP_PAY_L_ID) {
		AP_PAY_L_ID = aP_PAY_L_ID;
	}
	public String getAP_PAY_H_ID() {
		return AP_PAY_H_ID;
	}
	public void setAP_PAY_H_ID(String aP_PAY_H_ID) {
		AP_PAY_H_ID = aP_PAY_H_ID;
	}
	public String getAP_PAY_REQ_H_ID() {
		return AP_PAY_REQ_H_ID;
	}
	public void setAP_PAY_REQ_H_ID(String aP_PAY_REQ_H_ID) {
		AP_PAY_REQ_H_ID = aP_PAY_REQ_H_ID;
	}
	public String getAP_PUR_TYPE_ID() {
		return AP_PUR_TYPE_ID;
	}
	public void setAP_PUR_TYPE_ID(String aP_PUR_TYPE_ID) {
		AP_PUR_TYPE_ID = aP_PUR_TYPE_ID;
	}
	public String getBG_ITEM_ID() {
		return BG_ITEM_ID;
	}
	public void setBG_ITEM_ID(String bG_ITEM_ID) {
		BG_ITEM_ID = bG_ITEM_ID;
	}
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getACC_ID() {
		return ACC_ID;
	}
	public void setACC_ID(String aCC_ID) {
		ACC_ID = aCC_ID;
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
	public String getAP_PAY_REQ_L_ID() {
		return AP_PAY_REQ_L_ID;
	}
	public void setAP_PAY_REQ_L_ID(String aP_PAY_REQ_L_ID) {
		AP_PAY_REQ_L_ID = aP_PAY_REQ_L_ID;
	}
	public String getL_AP_PAY_H_ID() {
		return L_AP_PAY_H_ID;
	}
	public void setL_AP_PAY_H_ID(String l_AP_PAY_H_ID) {
		L_AP_PAY_H_ID = l_AP_PAY_H_ID;
	}
	public String getL_AP_PAY_L_ID() {
		return L_AP_PAY_L_ID;
	}
	public void setL_AP_PAY_L_ID(String l_AP_PAY_L_ID) {
		L_AP_PAY_L_ID = l_AP_PAY_L_ID;
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
	public String getAP_DOC_CAT_CODE() {
		return AP_DOC_CAT_CODE;
	}
	public void setAP_DOC_CAT_CODE(String aP_DOC_CAT_CODE) {
		AP_DOC_CAT_CODE = aP_DOC_CAT_CODE;
	}
	public double getCANCEL_AMT() {
		return CANCEL_AMT;
	}
	public void setCANCEL_AMT(double cANCEL_AMT) {
		CANCEL_AMT = cANCEL_AMT;
	}
	public double getNO_CANCEL_AMT() {
		return NO_CANCEL_AMT;
	}
	public void setNO_CANCEL_AMT(double nO_CANCEL_AMT) {
		NO_CANCEL_AMT = nO_CANCEL_AMT;
	}
	
}
