package com.xzsoft.xc.po.modal;

import java.util.Date;

/** 
 * @ClassName: PoReturnLBean 
 * @Description: 退货通知单明细表实体类
 * @author weicw 
 * @date 2016年11月29日 上午9:36:54 
 * 
 * 
 */
public class PoReturnLBean {
	private String RETURN_L_ID;
	private String RETURN_H_ID;
	private int RETURN_L_NUM;
	private String MATERIAL_ID;
	private String ORDER_L_ID;
	private String ENTRY_L_ID;
	private String DIM_ID;
	private Double QTY;
	private Double ARRIVAL_QTY;
	private Double RETURN_QTY;
	private Double TAX;
	private Double PRICE;
	private Double INV_PRICE;
	private Double AMOUNT;
	private Double INV_AMOUNT;
	private String ORG_ID;
	private String LEDGER_ID;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	public PoReturnLBean() {
		super();
	}
	public String getRETURN_L_ID() {
		return RETURN_L_ID;
	}
	public void setRETURN_L_ID(String rETURN_L_ID) {
		RETURN_L_ID = rETURN_L_ID;
	}
	public String getRETURN_H_ID() {
		return RETURN_H_ID;
	}
	public void setRETURN_H_ID(String rETURN_H_ID) {
		RETURN_H_ID = rETURN_H_ID;
	}
	public int getRETURN_L_NUM() {
		return RETURN_L_NUM;
	}
	public void setRETURN_L_NUM(int rETURN_L_NUM) {
		RETURN_L_NUM = rETURN_L_NUM;
	}
	public String getMATERIAL_ID() {
		return MATERIAL_ID;
	}
	public void setMATERIAL_ID(String mATERIAL_ID) {
		MATERIAL_ID = mATERIAL_ID;
	}
	public String getORDER_L_ID() {
		return ORDER_L_ID;
	}
	public void setORDER_L_ID(String oRDER_L_ID) {
		ORDER_L_ID = oRDER_L_ID;
	}
	public String getENTRY_L_ID() {
		return ENTRY_L_ID;
	}
	public void setENTRY_L_ID(String eNTRY_L_ID) {
		ENTRY_L_ID = eNTRY_L_ID;
	}
	
	public Double getQTY() {
		return QTY;
	}
	public void setQTY(Double qTY) {
		QTY = qTY;
	}
	public Double getARRIVAL_QTY() {
		return ARRIVAL_QTY;
	}
	public void setARRIVAL_QTY(Double aRRIVAL_QTY) {
		ARRIVAL_QTY = aRRIVAL_QTY;
	}
	public Double getRETURN_QTY() {
		return RETURN_QTY;
	}
	public void setRETURN_QTY(Double rETURN_QTY) {
		RETURN_QTY = rETURN_QTY;
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
	
}

