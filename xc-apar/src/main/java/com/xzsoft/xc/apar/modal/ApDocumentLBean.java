package com.xzsoft.xc.apar.modal;

import java.util.Date;

/**
 * 
  * @ClassName: ApDocumentLBean
  * @Description: 应付模块所有需要生成凭证的单据行表实体
  * @author 任建建
  * @date 2016年4月7日 上午11:04:32
 */
public class ApDocumentLBean {
	//采购发票行信息
	private String AP_INV_L_ID;			//采购发票行ID
	private String AP_INV_H_ID;			//采购发票主ID
	private String L_AP_INV_L_ID;		//蓝票采购发票行ID
	private String L_AP_INV_H_ID;		//蓝票采购发票主ID
	private String ORDER_H_ID;			//采购订单主ID
	private String ORDER_L_ID;			//采购订单行ID
	private String MATERIAL_ID;			//物料ID
	
	//应付单行表信息
	private String AP_INV_GL_L_ID;		//主键
	private String AP_INV_GL_H_ID;		//应付单主ID
	private String AP_PUR_TYPE_ID;		//采购类型ID
	private String L_AP_INV_GL_H_ID;	//蓝字应付单主ID
	private String L_AP_INV_GL_L_ID;	//蓝字应付单行ID
	private String ACC_ID;				//科目id
	private String CCID;				//科目组合ID
	private String BG_ITEM_ID;			//预算项目ID
	private double AMOUNT;				//金额(无税)
	private double QTY;					//数量
	private String DIM_CODE;			//计量单位
	private String DESCRIPTION;			//摘要
	private Date CREATION_DATE;			//创建日期
	private String CREATED_BY;			//创建人
	private Date LAST_UPDATE_DATE;		//最后更新日期
	private String LAST_UPDATED_BY;		//最后更新人
	
	//付款单行表信息
	private String AP_PAY_L_ID;
	private String AP_PAY_H_ID;
	private String AP_PAY_REQ_H_ID;
	
	//核销单行信息
	private String AP_CANCEL_L_ID;
	private String AP_CANCEL_H_ID;
	private String AP_CANCEL_TYPE;
	private String TARGET_ID;
	private double TARGET_AMT;
	public ApDocumentLBean() {
		super();
	}
	public String getAP_INV_GL_L_ID() {
		return AP_INV_GL_L_ID;
	}
	public void setAP_INV_GL_L_ID(String aP_INV_GL_L_ID) {
		AP_INV_GL_L_ID = aP_INV_GL_L_ID;
	}
	public String getAP_INV_GL_H_ID() {
		return AP_INV_GL_H_ID;
	}
	public void setAP_INV_GL_H_ID(String aP_INV_GL_H_ID) {
		AP_INV_GL_H_ID = aP_INV_GL_H_ID;
	}
	public String getL_AP_INV_GL_H_ID() {
		return L_AP_INV_GL_H_ID;
	}
	public void setL_AP_INV_GL_H_ID(String l_AP_INV_GL_H_ID) {
		L_AP_INV_GL_H_ID = l_AP_INV_GL_H_ID;
	}
	public String getL_AP_INV_GL_L_ID() {
		return L_AP_INV_GL_L_ID;
	}
	public void setL_AP_INV_GL_L_ID(String l_AP_INV_GL_L_ID) {
		L_AP_INV_GL_L_ID = l_AP_INV_GL_L_ID;
	}
	public String getAP_PUR_TYPE_ID() {
		return AP_PUR_TYPE_ID;
	}
	public void setAP_PUR_TYPE_ID(String aP_PUR_TYPE_ID) {
		AP_PUR_TYPE_ID = aP_PUR_TYPE_ID;
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
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
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
	public String getAP_PAY_L_ID() {
		return AP_PAY_L_ID;
	}
	public void setAP_PAY_L_ID(String aP_PAY_L_ID) {
		AP_PAY_L_ID = aP_PAY_L_ID;
	}
	public String getAP_PAY_H_ID() {
		return AP_PAY_H_ID;
	}
	public void setAP_PAY_H_ID(String aP_PAY_H_ID) {
		AP_PAY_H_ID = aP_PAY_H_ID;
	}
	public String getAP_PAY_REQ_H_ID() {
		return AP_PAY_REQ_H_ID;
	}
	public void setAP_PAY_REQ_H_ID(String aP_PAY_REQ_H_ID) {
		AP_PAY_REQ_H_ID = aP_PAY_REQ_H_ID;
	}
	public String getAP_CANCEL_L_ID() {
		return AP_CANCEL_L_ID;
	}
	public void setAP_CANCEL_L_ID(String aP_CANCEL_L_ID) {
		AP_CANCEL_L_ID = aP_CANCEL_L_ID;
	}
	public String getAP_CANCEL_H_ID() {
		return AP_CANCEL_H_ID;
	}
	public void setAP_CANCEL_H_ID(String aP_CANCEL_H_ID) {
		AP_CANCEL_H_ID = aP_CANCEL_H_ID;
	}
	public String getAP_CANCEL_TYPE() {
		return AP_CANCEL_TYPE;
	}
	public void setAP_CANCEL_TYPE(String aP_CANCEL_TYPE) {
		AP_CANCEL_TYPE = aP_CANCEL_TYPE;
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
	public String getAP_INV_L_ID() {
		return AP_INV_L_ID;
	}
	public void setAP_INV_L_ID(String aP_INV_L_ID) {
		AP_INV_L_ID = aP_INV_L_ID;
	}
	public String getAP_INV_H_ID() {
		return AP_INV_H_ID;
	}
	public void setAP_INV_H_ID(String aP_INV_H_ID) {
		AP_INV_H_ID = aP_INV_H_ID;
	}
	public String getL_AP_INV_L_ID() {
		return L_AP_INV_L_ID;
	}
	public void setL_AP_INV_L_ID(String l_AP_INV_L_ID) {
		L_AP_INV_L_ID = l_AP_INV_L_ID;
	}
	public String getL_AP_INV_H_ID() {
		return L_AP_INV_H_ID;
	}
	public void setL_AP_INV_H_ID(String l_AP_INV_H_ID) {
		L_AP_INV_H_ID = l_AP_INV_H_ID;
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
	public String getMATERIAL_ID() {
		return MATERIAL_ID;
	}
	public void setMATERIAL_ID(String mATERIAL_ID) {
		MATERIAL_ID = mATERIAL_ID;
	}
}
