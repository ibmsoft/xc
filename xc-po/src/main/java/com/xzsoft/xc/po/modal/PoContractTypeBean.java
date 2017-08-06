package com.xzsoft.xc.po.modal;

import java.util.Date;

/** 
 * @ClassName: PoContractTypeBean 
 * @Description: 合同类型实体类
 * @author weicw 
 * @date 2016年11月28日 下午3:22:46 
 * 
 * 
 */
public class PoContractTypeBean {
	private String CONTRACT_TYPE_ID;
	private String CONTRACT_TYPE_CODE;
	private String CONTRACT_TYPE_NAME;
	private String DESCRIPTION;
	private String STATUS;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public PoContractTypeBean() {
		super();
	}
	public String getCONTRACT_TYPE_ID() {
		return CONTRACT_TYPE_ID;
	}
	public void setCONTRACT_TYPE_ID(String cONTRACT_TYPE_ID) {
		CONTRACT_TYPE_ID = cONTRACT_TYPE_ID;
	}
	public String getCONTRACT_TYPE_CODE() {
		return CONTRACT_TYPE_CODE;
	}
	public void setCONTRACT_TYPE_CODE(String cONTRACT_TYPE_CODE) {
		CONTRACT_TYPE_CODE = cONTRACT_TYPE_CODE;
	}
	public String getCONTRACT_TYPE_NAME() {
		return CONTRACT_TYPE_NAME;
	}
	public void setCONTRACT_TYPE_NAME(String cONTRACT_TYPE_NAME) {
		CONTRACT_TYPE_NAME = cONTRACT_TYPE_NAME;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
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

