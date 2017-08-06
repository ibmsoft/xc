package com.xzsoft.xc.ap.modal;

import java.util.Date;

/**
 * 
  * @ClassName: ApInvGlLBean
  * @Description: 应付单行表实体类
  * @author 任建建
  * @date 2016年4月6日 下午4:54:45
 */
public class ApInvGlLBean {
	private String AP_INV_GL_L_ID;		//主键
	private String AP_INV_GL_H_ID;		//应付单主ID
	private String AP_PUR_TYPE_ID;		//采购类型ID
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
	public ApInvGlLBean() {
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
}
