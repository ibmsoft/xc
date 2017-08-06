package com.xzsoft.xc.po.modal;

import java.util.Date;

/** 
 * @ClassName: PoLdDocCatBean 
 * @Description: 账簿下单据类型实体类
 * @author weicw 
 * @date 2016年11月28日 下午4:55:54 
 * 
 * 
 */
public class PoLdDocCatBean {
	private String LD_DOC_CAT_ID;
	private String LEDGER_ID;
	private String PO_DOC_CAT_CODE;
	private String RULE_CODE;
	private String PROCESS_ID;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public PoLdDocCatBean() {
		super();
	}
	public String getLD_DOC_CAT_ID() {
		return LD_DOC_CAT_ID;
	}
	public void setLD_DOC_CAT_ID(String lD_DOC_CAT_ID) {
		LD_DOC_CAT_ID = lD_DOC_CAT_ID;
	}
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}
	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}
	public String getPO_DOC_CAT_CODE() {
		return PO_DOC_CAT_CODE;
	}
	public void setPO_DOC_CAT_CODE(String pO_DOC_CAT_CODE) {
		PO_DOC_CAT_CODE = pO_DOC_CAT_CODE;
	}
	public String getRULE_CODE() {
		return RULE_CODE;
	}
	public void setRULE_CODE(String rULE_CODE) {
		RULE_CODE = rULE_CODE;
	}
	public String getPROCESS_ID() {
		return PROCESS_ID;
	}
	public void setPROCESS_ID(String pROCESS_ID) {
		PROCESS_ID = pROCESS_ID;
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

