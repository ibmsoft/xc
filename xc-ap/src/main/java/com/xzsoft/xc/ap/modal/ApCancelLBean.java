package com.xzsoft.xc.ap.modal;

import java.util.Date;

/**
 * 
  * @ClassName: ApCancelLBean
  * @Description: 核销单行表
  * @author 任建建
  * @date 2016年4月6日 下午5:29:00
 */
public class ApCancelLBean {
	private String AP_CANCEL_L_ID;
	private String AP_CANCEL_H_ID;
	private String AP_CANCEL_TYPE;
	private String TARGET_ID;
	private double TARGET_AMT;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public ApCancelLBean() {
		super();
	}
	public String getAP_CANCEL_L_ID() {
		return AP_CANCEL_L_ID;
	}
	public void setAP_CANCEL_L_ID(String aP_CANCEL_L_ID) {
		AP_CANCEL_L_ID = aP_CANCEL_L_ID;
	}
	public String getAP_CANCEL_H_ID() {
		return AP_CANCEL_H_ID;
	}
	public void setAP_CANCEL_H_ID(String aP_CANCEL_H_ID) {
		AP_CANCEL_H_ID = aP_CANCEL_H_ID;
	}
	public String getAP_CANCEL_TYPE() {
		return AP_CANCEL_TYPE;
	}
	public void setAP_CANCEL_TYPE(String aP_CANCEL_TYPE) {
		AP_CANCEL_TYPE = aP_CANCEL_TYPE;
	}
	public String getTARGET_ID() {
		return TARGET_ID;
	}
	public void setTARGET_ID(String tARGET_ID) {
		TARGET_ID = tARGET_ID;
	}

	public double getTARGET_AMT() {
		return TARGET_AMT;
	}
	public void setTARGET_AMT(double tARGET_AMT) {
		TARGET_AMT = tARGET_AMT;
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
