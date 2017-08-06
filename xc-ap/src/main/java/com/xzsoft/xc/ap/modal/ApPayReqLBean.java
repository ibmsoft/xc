package com.xzsoft.xc.ap.modal;

import java.util.Date;

/**
 * 
  * @ClassName: ApPayReqLBean
  * @Description: 付款申请单行表
  * @author 任建建
  * @date 2016年4月6日 下午5:27:22
 */
public class ApPayReqLBean {
	private String AP_PAY_REQ_L_ID;
	private String AP_PAY_REQ_H_ID;
	private String AP_INV_GL_H_ID;
	private String AP_CONTRACT_ID;
	private String AP_PUR_TYPE_ID;
	private String BG_ITEM_ID;
	private Double AMOUNT;
	private Double PAID_AMT;
	private Double NO_PAY_AMT;
	private String DESCRIPTION;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public ApPayReqLBean() {
		super();
	}
	public String getAP_PAY_REQ_L_ID() {
		return AP_PAY_REQ_L_ID;
	}
	public void setAP_PAY_REQ_L_ID(String aP_PAY_REQ_L_ID) {
		AP_PAY_REQ_L_ID = aP_PAY_REQ_L_ID;
	}
	public String getAP_PAY_REQ_H_ID() {
		return AP_PAY_REQ_H_ID;
	}
	public void setAP_PAY_REQ_H_ID(String aP_PAY_REQ_H_ID) {
		AP_PAY_REQ_H_ID = aP_PAY_REQ_H_ID;
	}
	public String getAP_INV_GL_H_ID() {
		return AP_INV_GL_H_ID;
	}
	public void setAP_INV_GL_H_ID(String aP_INV_PAY_H_ID) {
		AP_INV_GL_H_ID = aP_INV_PAY_H_ID;
	}
	public String getAP_CONTRACT_ID() {
		return AP_CONTRACT_ID;
	}
	public void setAP_CONTRACT_ID(String aP_CONTRACT_ID) {
		AP_CONTRACT_ID = aP_CONTRACT_ID;
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
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
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
	
}
