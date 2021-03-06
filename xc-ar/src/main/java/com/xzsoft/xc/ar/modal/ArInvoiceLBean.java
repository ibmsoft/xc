package com.xzsoft.xc.ar.modal;

import java.util.Date;

/**
 * 
  * @ClassName：ArInvoiceLBean
  * @Description：销售发票发票行表实体类
  * @author：任建建
  * @date：2016年7月6日 上午11:04:43
 */
public class ArInvoiceLBean {
	private String AR_INV_L_ID;			//销售发票行ID
	private String AR_INV_H_ID;			//销售发票主ID
	private String L_AR_INV_H_ID;		//蓝票销售发票行ID
	private String L_AR_INV_L_ID;		//蓝票销售发票主ID
	private String AR_SALE_TYPE_ID;		//销售类型ID
	private String PRODUCT_ID;			//产品ID
	private String MODEL;				//规格型号
	private double PRICE;				//单价(无税)
	private double INV_PRICE;			//单价(含税)
	private double AMOUNT;				//金额(无税)
	private double INV_AMOUNT;			//金额(含税)
	private double TAX_AMT;				//税额
	private double TAX;					//税率
	private double QTY;					//数量
	private String DIM_CODE;			//计量单位
	private String DESCRIPTION;			//说明
	private Date CREATION_DATE;			//创建日期
	private String CREATED_BY;			//创建人
	private Date LAST_UPDATE_DATE;		//最后更新日期
	private String LAST_UPDATED_BY;		//最后更新人
	public ArInvoiceLBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ArInvoiceLBean(String aR_INV_L_ID, String aR_INV_H_ID,
			String l_AR_INV_H_ID, String l_AR_INV_L_ID, String aR_SALE_TYPE_ID,
			String pRODUCT_ID, String mODEL, double pRICE, double iNV_PRICE,
			double aMOUNT, double iNV_AMOUNT, double tAX_AMT, double tAX, double qTY,
			String dIM_CODE, String dESCRIPTION, Date cREATION_DATE,
			String cREATED_BY, Date lAST_UPDATE_DATE, String lAST_UPDATED_BY) {
		super();
		AR_INV_L_ID = aR_INV_L_ID;
		AR_INV_H_ID = aR_INV_H_ID;
		L_AR_INV_H_ID = l_AR_INV_H_ID;
		L_AR_INV_L_ID = l_AR_INV_L_ID;
		AR_SALE_TYPE_ID = aR_SALE_TYPE_ID;
		PRODUCT_ID = pRODUCT_ID;
		MODEL = mODEL;
		PRICE = pRICE;
		INV_PRICE = iNV_PRICE;
		AMOUNT = aMOUNT;
		INV_AMOUNT = iNV_AMOUNT;
		TAX_AMT = tAX_AMT;
		TAX = tAX;
		QTY = qTY;
		DIM_CODE = dIM_CODE;
		DESCRIPTION = dESCRIPTION;
		CREATION_DATE = cREATION_DATE;
		CREATED_BY = cREATED_BY;
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}
	public String getAR_INV_L_ID() {
		return AR_INV_L_ID;
	}
	public void setAR_INV_L_ID(String aR_INV_L_ID) {
		AR_INV_L_ID = aR_INV_L_ID;
	}
	public String getAR_INV_H_ID() {
		return AR_INV_H_ID;
	}
	public void setAR_INV_H_ID(String aR_INV_H_ID) {
		AR_INV_H_ID = aR_INV_H_ID;
	}
	public String getL_AR_INV_H_ID() {
		return L_AR_INV_H_ID;
	}
	public void setL_AR_INV_H_ID(String l_AR_INV_H_ID) {
		L_AR_INV_H_ID = l_AR_INV_H_ID;
	}
	public String getL_AR_INV_L_ID() {
		return L_AR_INV_L_ID;
	}
	public void setL_AR_INV_L_ID(String l_AR_INV_L_ID) {
		L_AR_INV_L_ID = l_AR_INV_L_ID;
	}
	public String getAR_SALE_TYPE_ID() {
		return AR_SALE_TYPE_ID;
	}
	public void setAR_SALE_TYPE_ID(String aR_SALE_TYPE_ID) {
		AR_SALE_TYPE_ID = aR_SALE_TYPE_ID;
	}
	public String getPRODUCT_ID() {
		return PRODUCT_ID;
	}
	public void setPRODUCT_ID(String pRODUCT_ID) {
		PRODUCT_ID = pRODUCT_ID;
	}
	public String getMODEL() {
		return MODEL;
	}
	public void setMODEL(String mODEL) {
		MODEL = mODEL;
	}
	public double getPRICE() {
		return PRICE;
	}
	public void setPRICE(double pRICE) {
		PRICE = pRICE;
	}
	public double getINV_PRICE() {
		return INV_PRICE;
	}
	public void setINV_PRICE(double iNV_PRICE) {
		INV_PRICE = iNV_PRICE;
	}
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public double getINV_AMOUNT() {
		return INV_AMOUNT;
	}
	public void setINV_AMOUNT(double iNV_AMOUNT) {
		INV_AMOUNT = iNV_AMOUNT;
	}
	public double getTAX_AMT() {
		return TAX_AMT;
	}
	public void setTAX_AMT(double tAX_AMT) {
		TAX_AMT = tAX_AMT;
	}
	public double getTAX() {
		return TAX;
	}
	public void setTAX(double tAX) {
		TAX = tAX;
	}
	public double getQTY() {
		return QTY;
	}
	public void setQTY(double qTY) {
		QTY = qTY;
	}
	public String getDIM_CODE() {
		return DIM_CODE;
	}
	public void setDIM_CODE(String dIM_CODE) {
		DIM_CODE = dIM_CODE;
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
}
