package com.xzsoft.xc.po.modal;

import java.util.Date;

/** 
 * @ClassName: PoPurApLBean 
 * @Description: 采购申请明细表实体类
 * @author weicw 
 * @date 2016年11月29日 上午9:24:47 
 * 
 * 
 */
public class PoPurApLBean {
	private String PUR_AP_L_ID;
	private String PUR_AP_H_ID;
	private String MATERIAL_ID;
	private String DIM_ID;
	private int QTY;
	private Double PRICE;
	private Double AMOUNT;
	private Date REQUIRED_DATE;
	private String PURCHASE_REASON;
	private String ORG_ID;
	private String LEDGER_ID;
	private String PO_ORDER_L_ID;
	private String PO_ORDER_H_ID;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public PoPurApLBean() {
		super();
	}
	public String getPUR_AP_L_ID() {
		return PUR_AP_L_ID;
	}
	public void setPUR_AP_L_ID(String pUR_AP_L_ID) {
		PUR_AP_L_ID = pUR_AP_L_ID;
	}
	public String getPUR_AP_H_ID() {
		return PUR_AP_H_ID;
	}
	public void setPUR_AP_H_ID(String pUR_AP_H_ID) {
		PUR_AP_H_ID = pUR_AP_H_ID;
	}
	public String getMATERIAL_ID() {
		return MATERIAL_ID;
	}
	public void setMATERIAL_ID(String mATERIAL_ID) {
		MATERIAL_ID = mATERIAL_ID;
	}
	
	public String getDIM_ID() {
		return DIM_ID;
	}
	public void setDIM_ID(String dIM_ID) {
		DIM_ID = dIM_ID;
	}
	public int getQTY() {
		return QTY;
	}
	public void setQTY(int qTY) {
		QTY = qTY;
	}
	public Double getPRICE() {
		return PRICE;
	}
	public void setPRICE(Double pRICE) {
		PRICE = pRICE;
	}
	public Double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public Date getREQUIRED_DATE() {
		return REQUIRED_DATE;
	}
	public void setREQUIRED_DATE(Date rEQUIRED_DATE) {
		REQUIRED_DATE = rEQUIRED_DATE;
	}
	public String getPURCHASE_REASON() {
		return PURCHASE_REASON;
	}
	public void setPURCHASE_REASON(String pURCHASE_REASON) {
		PURCHASE_REASON = pURCHASE_REASON;
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
	public String getPO_ORDER_L_ID() {
		return PO_ORDER_L_ID;
	}
	public void setPO_ORDER_L_ID(String pO_ORDER_L_ID) {
		PO_ORDER_L_ID = pO_ORDER_L_ID;
	}
	public String getPO_ORDER_H_ID() {
		return PO_ORDER_H_ID;
	}
	public void setPO_ORDER_H_ID(String pO_ORDER_H_ID) {
		PO_ORDER_H_ID = pO_ORDER_H_ID;
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

