package com.xzsoft.xc.apar.modal;

import java.util.Date;

/**
 * 
  * @ClassName：ArDocumentLBean
  * @Description：应收模块所有需要生成凭证的单据行表实体
  * @author：任建建
  * @date：2016年7月8日 上午10:49:20
 */
public class ArDocumentLBean {
	//应收单行表信息
	private String AR_INV_GL_L_ID;		//主键
	private String AR_INV_GL_H_ID;		//应收单主ID
	private String L_AR_INV_GL_L_ID;	//应收单蓝单行ID
	private String L_AR_INV_GL_H_ID;	//应收单蓝单主ID
	private String AR_SALE_TYPE_ID;		//销售类型ID
	private String PRODUCT_ID;			//产品ID
	private String ACC_ID;				//科目id
	private String CCID;				//科目组合ID
	private String BG_ITEM_ID;			//预算项目ID
	private double PRICE;				//单价(无税)
	private double INV_PRICE;			//单价(含税)
	private double AMOUNT;				//金额(无税)
	private double INV_AMOUNT;			//金额(含税)
	private double TAX_AMT;				//税额
	private double TAX;					//税率
	private double QTY;					//数量
	private String DIM_CODE;			//计量单位
	private String DESCRIPTION;			//摘要
	private Date CREATION_DATE;			//创建日期
	private String CREATED_BY;			//创建人
	private Date LAST_UPDATE_DATE;		//最后更新日期
	private String LAST_UPDATED_BY;		//最后更新人
	
	//收款单行表信息
	private String AR_PAY_L_ID;			//收款单行ID
	private String AR_PAY_H_ID;			//收款单主ID
	private String L_AR_PAY_H_ID;		//蓝色收款单主ID
	private String L_AR_PAY_L_ID;		//蓝色收款单行ID
	private String PROJECT_ID;			//项目
	private String AR_CONTRACT_ID;		//合同
	private String DEPT_ID;				//成本中心
	private String AR_DOC_CAT_CODE;		//单据类型
	private String AR_CREATED_BY_NAME;	//业务员(冗余)
	
	//核销单行信息
	private String AR_CANCEL_L_ID;
	private String AR_CANCEL_H_ID;
	private String AR_CANCEL_TYPE;
	private String TARGET_ID;
	private double TARGET_AMT;
	public ArDocumentLBean() {
		super();
	}
	public String getAR_INV_GL_L_ID() {
		return AR_INV_GL_L_ID;
	}
	public void setAR_INV_GL_L_ID(String aR_INV_GL_L_ID) {
		AR_INV_GL_L_ID = aR_INV_GL_L_ID;
	}
	public String getAR_INV_GL_H_ID() {
		return AR_INV_GL_H_ID;
	}
	public void setAR_INV_GL_H_ID(String aR_INV_GL_H_ID) {
		AR_INV_GL_H_ID = aR_INV_GL_H_ID;
	}
	public String getL_AR_INV_GL_L_ID() {
		return L_AR_INV_GL_L_ID;
	}
	public void setL_AR_INV_GL_L_ID(String l_AR_INV_GL_L_ID) {
		L_AR_INV_GL_L_ID = l_AR_INV_GL_L_ID;
	}
	public String getL_AR_INV_GL_H_ID() {
		return L_AR_INV_GL_H_ID;
	}
	public void setL_AR_INV_GL_H_ID(String l_AR_INV_GL_H_ID) {
		L_AR_INV_GL_H_ID = l_AR_INV_GL_H_ID;
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
	public String getACC_ID() {
		return ACC_ID;
	}
	public void setACC_ID(String aCC_ID) {
		ACC_ID = aCC_ID;
	}
	public String getCCID() {
		return CCID;
	}
	public void setCCID(String cCID) {
		CCID = cCID;
	}
	public String getBG_ITEM_ID() {
		return BG_ITEM_ID;
	}
	public void setBG_ITEM_ID(String bG_ITEM_ID) {
		BG_ITEM_ID = bG_ITEM_ID;
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
	public String getAR_PAY_L_ID() {
		return AR_PAY_L_ID;
	}
	public void setAR_PAY_L_ID(String aR_PAY_L_ID) {
		AR_PAY_L_ID = aR_PAY_L_ID;
	}
	public String getAR_PAY_H_ID() {
		return AR_PAY_H_ID;
	}
	public void setAR_PAY_H_ID(String aR_PAY_H_ID) {
		AR_PAY_H_ID = aR_PAY_H_ID;
	}
	public String getL_AR_PAY_H_ID() {
		return L_AR_PAY_H_ID;
	}
	public void setL_AR_PAY_H_ID(String l_AR_PAY_H_ID) {
		L_AR_PAY_H_ID = l_AR_PAY_H_ID;
	}
	public String getL_AR_PAY_L_ID() {
		return L_AR_PAY_L_ID;
	}
	public void setL_AR_PAY_L_ID(String l_AR_PAY_L_ID) {
		L_AR_PAY_L_ID = l_AR_PAY_L_ID;
	}
	public String getPROJECT_ID() {
		return PROJECT_ID;
	}
	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}
	public String getAR_CONTRACT_ID() {
		return AR_CONTRACT_ID;
	}
	public void setAR_CONTRACT_ID(String aR_CONTRACT_ID) {
		AR_CONTRACT_ID = aR_CONTRACT_ID;
	}
	public String getDEPT_ID() {
		return DEPT_ID;
	}
	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}
	public String getAR_DOC_CAT_CODE() {
		return AR_DOC_CAT_CODE;
	}
	public void setAR_DOC_CAT_CODE(String aR_DOC_CAT_CODE) {
		AR_DOC_CAT_CODE = aR_DOC_CAT_CODE;
	}
	public String getAR_CREATED_BY_NAME() {
		return AR_CREATED_BY_NAME;
	}
	public void setAR_CREATED_BY_NAME(String aR_CREATED_BY_NAME) {
		AR_CREATED_BY_NAME = aR_CREATED_BY_NAME;
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
	public double getTARGET_AMT() {
		return TARGET_AMT;
	}
	public void setTARGET_AMT(double tARGET_AMT) {
		TARGET_AMT = tARGET_AMT;
	}
}