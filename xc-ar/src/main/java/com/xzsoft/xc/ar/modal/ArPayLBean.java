package com.xzsoft.xc.ar.modal;

import java.util.Date;


/**
 * @ClassName：ArPayLBean
 * @Description：收款单行表
 * @author zhenghy
 * @date：2016年7月7日 上午10:58:20
 */
public class ArPayLBean {

	private String AR_PAY_L_ID;//收款单行ID
	private String AR_PAY_H_ID;//收款单主ID
	private String L_AR_PAY_H_ID;//蓝色收款单主ID
	private String L_AR_PAY_L_ID;//蓝色收款单行ID
	private String AR_INV_GL_H_ID;//应收单ID
	private String AR_SALE_TYPE_ID;//销售类型ID
	private String PRODUCT_ID;//产品ID
	private String BG_ITEM_ID;//预算项目ID
	private String PROJECT_ID;//项目
	private String AR_CONTRACT_ID;//合同
	private String DEPT_ID;//成本中心
	private String AR_DOC_CAT_CODE;//单据类型
	private Double AMOUNT;//收款金额
	private Double CANCEL_AMT;//核销金额
	private Double NO_CANCEL_AMT;//未核销金额（余额）
	private String DESCRIPTION;//摘要
	private String ACC_ID;//科目
	private String CCID;//科目组合
	private String AR_CREATED_BY_NAME;//业务员(冗余)
	private Date CREATION_DATE;//创建日期
	private String CREATED_BY;//创建人
	private Date LAST_UPDATE_DATE;//最后更新日期
	private String LAST_UPDATED_BY;//最后更新人
	
	public ArPayLBean(){}

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

	public String getAR_INV_GL_H_ID() {
		return AR_INV_GL_H_ID;
	}

	public void setAR_INV_GL_H_ID(String aR_INV_GL_H_ID) {
		AR_INV_GL_H_ID = aR_INV_GL_H_ID;
	}

	public String getAR_SALE_TYPE_ID() {
		return AR_SALE_TYPE_ID;
	}

	public void setAR_SALE_TYPE_ID(String aR_PUR_TYPE_ID) {
		AR_SALE_TYPE_ID = aR_PUR_TYPE_ID;
	}

	public String getPRODUCT_ID() {
		return PRODUCT_ID;
	}

	public void setPRODUCT_ID(String pRODUCT_ID) {
		PRODUCT_ID = pRODUCT_ID;
	}

	public String getBG_ITEM_ID() {
		return BG_ITEM_ID;
	}

	public void setBG_ITEM_ID(String bG_ITEM_ID) {
		BG_ITEM_ID = bG_ITEM_ID;
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

	public Double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(Double aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
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

	public String getAR_CREATED_BY_NAME() {
		return AR_CREATED_BY_NAME;
	}

	public void setAR_CREATED_BY_NAME(String aR_CREATED_BY_NAME) {
		AR_CREATED_BY_NAME = aR_CREATED_BY_NAME;
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

	public Double getCANCEL_AMT() {
		return CANCEL_AMT;
	}

	public void setCANCEL_AMT(Double cANCEL_AMT) {
		CANCEL_AMT = cANCEL_AMT;
	}

	public Double getNO_CANCEL_AMT() {
		return NO_CANCEL_AMT;
	}

	public void setNO_CANCEL_AMT(Double nO_CANCEL_AMT) {
		NO_CANCEL_AMT = nO_CANCEL_AMT;
	}
	
	
	
	
}
