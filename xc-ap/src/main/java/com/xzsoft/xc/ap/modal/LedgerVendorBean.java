package com.xzsoft.xc.ap.modal;

import java.util.Date;

/** 
 * @ClassName: LedgerVendorBean 
 * @Description: 账簿下供应商实体类
 * @author weicw 
 * @date 2016年11月24日 上午9:30:23 
 * 
 * 
 */
public class LedgerVendorBean {
	private String LD_VENDOR_ID;
	private String LEDGER_ID;
	private String VENDOR_ID;
	private int IS_ENABLED;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	private String ORG_ID;
	public LedgerVendorBean() {
		super();
	}
	public String getLD_VENDOR_ID() {
		return LD_VENDOR_ID;
	}
	public void setLD_VENDOR_ID(String lD_VENDOR_ID) {
		LD_VENDOR_ID = lD_VENDOR_ID;
	}
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}
	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}
	public String getVENDOR_ID() {
		return VENDOR_ID;
	}
	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
	}
	public int getIS_ENABLED() {
		return IS_ENABLED;
	}
	public void setIS_ENABLED(int iS_ENABLED) {
		IS_ENABLED = iS_ENABLED;
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
	public String getORG_ID() {
		return ORG_ID;
	}
	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}
	
	
}

