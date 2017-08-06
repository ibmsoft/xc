package com.xzsoft.xc.po.modal;

import java.util.Date;

/** 
 * @ClassName: PoOrderLBean 
 * @Description: 采购订单行表实体类
 * @author weicw 
 * @date 2016年11月28日 下午5:04:39 
 * 
 * 
 */
public class PoOrderLBean {
	private String ORDER_L_ID;
	private String ORDER_H_ID;
	private int ORDER_L_NUM;
	private String AP_PUR_TYPE_ID;
	private String MATERIAL_ID;
	private String PUR_AP_L_ID;
	private String DEPT_ID;
	private String PROJECT_ID;
	private String IS_CLOSE;
	private String DIM_ID;
	private Double QTY;
	private Double TAX;
	private Double PRICE;
	private Double INV_PRICE;
	private Double AMOUNT;
	private Double INV_AMOUNT;
	private String ORG_ID;
	private String LEDGER_ID;
	private String DESCRIPTION;
	private int VERSION;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public PoOrderLBean() {
		super();
	}
	public String getORDER_L_ID() {
		return ORDER_L_ID;
	}
	public void setORDER_L_ID(String oRDER_L_ID) {
		ORDER_L_ID = oRDER_L_ID;
	}
	public String getORDER_H_ID() {
		return ORDER_H_ID;
	}
	public void setORDER_H_ID(String oRDER_H_ID) {
		ORDER_H_ID = oRDER_H_ID;
	}
	
	public int getORDER_L_NUM() {
		return ORDER_L_NUM;
	}
	public void setORDER_L_NUM(int oRDER_L_NUM) {
		ORDER_L_NUM = oRDER_L_NUM;
	}
	
	public String getAP_PUR_TYPE_ID() {
		return AP_PUR_TYPE_ID;
	}
	public void setAP_PUR_TYPE_ID(String aP_PUR_TYPE_ID) {
		AP_PUR_TYPE_ID = aP_PUR_TYPE_ID;
	}
	public String getMATERIAL_ID() {
		return MATERIAL_ID;
	}
	public void setMATERIAL_ID(String mATERIAL_ID) {
		MATERIAL_ID = mATERIAL_ID;
	}
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getPROJECT_ID() {
		return PROJECT_ID;
	}
	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}
	
	public Double getQTY() {
		return QTY;
	}
	public void setQTY(Double qTY) {
		QTY = qTY;
	}
	public Double getTAX() {
		return TAX;
	}
	public void setTAX(Double tAX) {
		TAX = tAX;
	}
	public Double getPRICE() {
		return PRICE;
	}
	public void setPRICE(Double pRICE) {
		PRICE = pRICE;
	}
	public Double getINV_PRICE() {
		return INV_PRICE;
	}
	public void setINV_PRICE(Double iNV_PRICE) {
		INV_PRICE = iNV_PRICE;
	}
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public Double getINV_AMOUNT() {
		return INV_AMOUNT;
	}
	public void setINV_AMOUNT(Double iNV_AMOUNT) {
		INV_AMOUNT = iNV_AMOUNT;
	}
	public String getORG_ID() {
		return ORG_ID;
	}
	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}
	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public int getVERSION() {
		return VERSION;
	}
	public void setVERSION(int vERSION) {
		VERSION = vERSION;
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
	public String getDIM_ID() {
		return DIM_ID;
	}
	public void setDIM_ID(String dIM_ID) {
		DIM_ID = dIM_ID;
	}
	public String getIS_CLOSE() {
		return IS_CLOSE;
	}
	public void setIS_CLOSE(String iS_CLOSE) {
		IS_CLOSE = iS_CLOSE;
	}
	public String getPUR_AP_L_ID() {
		return PUR_AP_L_ID;
	}
	public void setPUR_AP_L_ID(String pUR_AP_L_ID) {
		PUR_AP_L_ID = pUR_AP_L_ID;
	}
	
}

