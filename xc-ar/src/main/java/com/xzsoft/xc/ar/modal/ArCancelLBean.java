package com.xzsoft.xc.ar.modal;

import java.util.Date;

/**
 * 
  * @ClassName：ArCancelLBean
  * @Description：核销单行表
  * @author：任建建
  * @date：2016年7月6日 上午11:18:34
 */
public class ArCancelLBean {
	private String AR_CANCEL_L_ID;
	private String AR_CANCEL_H_ID;
	private String AR_CANCEL_TYPE;
	private String TARGET_ID;
	private Double TARGET_AMT;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public ArCancelLBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAR_CANCEL_L_ID() {
		return AR_CANCEL_L_ID;
	}
	public void setAR_CANCEL_L_ID(String aR_CANCEL_L_ID) {
		AR_CANCEL_L_ID = aR_CANCEL_L_ID;
	}
	public String getAR_CANCEL_H_ID() {
		return AR_CANCEL_H_ID;
	}
	public void setAR_CANCEL_H_ID(String aR_CANCEL_H_ID) {
		AR_CANCEL_H_ID = aR_CANCEL_H_ID;
	}
	public String getAR_CANCEL_TYPE() {
		return AR_CANCEL_TYPE;
	}
	public void setAR_CANCEL_TYPE(String aR_CANCEL_TYPE) {
		AR_CANCEL_TYPE = aR_CANCEL_TYPE;
	}
	public String getTARGET_ID() {
		return TARGET_ID;
	}
	public void setTARGET_ID(String tARGET_ID) {
		TARGET_ID = tARGET_ID;
	}
	public Double getTARGET_AMT() {
		return TARGET_AMT;
	}
	public void setTARGET_AMT(Double tARGET_AMT) {
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
