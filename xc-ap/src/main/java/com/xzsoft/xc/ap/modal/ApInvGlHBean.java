package com.xzsoft.xc.ap.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName: ApInvGlHBean
  * @Description: 应付单主表实体类
  * @author 任建建
  * @date 2016年4月6日 下午4:54:45
 */
public class ApInvGlHBean {
	private String AP_INV_GL_H_ID;		//主键
	private String AP_INV_GL_H_CODE;	//单据号
	private String AP_INV_H_ID;			//采购发票主ID
	private String AP_DOC_CAT_CODE;		//单据类型
	private String LEDGER_ID;			//账簿ID
	private Date GL_DATE;				//入账日期
	private String VENDOR_ID;			//供应商
	private String PROJECT_ID;			//项目ID
	private String DEPT_ID;				//成本中心ID
	private String AP_CONTRACT_ID;		//所属合同
	private double AMOUNT;				//金额
	private double TAX_AMT;				//税额
	private double TAX_RATE;				//税率
	private double INV_AMOUNT;			//含税金额
	private double CANCEL_AMT;			//核销金额
	private double PAID_AMT;				//金额
	private double NO_PAY_AMT;			//未付金额
	private double OCCUPY_AMT;			//占用金额
	private double ADJ_AMT;				//调整金额
	private double REQ_AMT;				//已申请付款金额
	private double NO_REQ_AMT;			//未申请付款金额
	private String AP_ACC_ID_DEBT;		//应付科目
	private String AP_ACC_ID_TAX;		//进项税科目
	private String AP_CCID_DEBT;		//应付科目组合
	private String AP_CCID_TAX;			//进项税科目组合
	private String INIT;				//期初: 0-未拆分 1-已拆分 2-正常单据
	private String SOURCE;				//0-手工 1-采购发票
	private String V_HEAD_ID;			//凭证头ID
	private String V_STATUS;			//凭证状态
	private String DESCRIPTION;			//摘要
	private String VERIFIER_ID;			//审核人
	private Date VERFY_DATE;			//审核日期
	private Date CREATION_DATE;			//创建日期
	private String CREATED_BY;			//创建人
	private Date LAST_UPDATE_DATE;		//最后更新日期
	private String LAST_UPDATED_BY;		//最后更新人
	
	private List<ApInvGlLBean> apInvGlL;	//应付单明细表

	public ApInvGlHBean() {
		super();
	}

	public String getAP_INV_H_ID() {
		return AP_INV_H_ID;
	}

	public void setAP_INV_H_ID(String aP_INV_H_ID) {
		AP_INV_H_ID = aP_INV_H_ID;
	}

	public String getAP_INV_GL_H_ID() {
		return AP_INV_GL_H_ID;
	}

	public void setAP_INV_GL_H_ID(String aP_INV_GL_H_ID) {
		AP_INV_GL_H_ID = aP_INV_GL_H_ID;
	}

	public String getAP_INV_GL_H_CODE() {
		return AP_INV_GL_H_CODE;
	}

	public void setAP_INV_GL_H_CODE(String aP_INV_GL_H_CODE) {
		AP_INV_GL_H_CODE = aP_INV_GL_H_CODE;
	}

	public String getAP_DOC_CAT_CODE() {
		return AP_DOC_CAT_CODE;
	}

	public void setAP_DOC_CAT_CODE(String aP_DOC_CAT_CODE) {
		AP_DOC_CAT_CODE = aP_DOC_CAT_CODE;
	}

	public String getLEDGER_ID() {
		return LEDGER_ID;
	}

	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}

	public Date getGL_DATE() {
		return GL_DATE;
	}

	public void setGL_DATE(Date gL_DATE) {
		GL_DATE = gL_DATE;
	}

	public String getVENDOR_ID() {
		return VENDOR_ID;
	}

	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
	}

	public String getPROJECT_ID() {
		return PROJECT_ID;
	}

	public void setPROJECT_ID(String pROJECT_ID) {
		PROJECT_ID = pROJECT_ID;
	}

	public String getDEPT_ID() {
		return DEPT_ID;
	}

	public void setDEPT_ID(String dEPT_ID) {
		DEPT_ID = dEPT_ID;
	}

	public String getAP_CONTRACT_ID() {
		return AP_CONTRACT_ID;
	}

	public void setAP_CONTRACT_ID(String aP_CONTRACT_ID) {
		AP_CONTRACT_ID = aP_CONTRACT_ID;
	}

	public double getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public double getTAX_AMT() {
		return TAX_AMT;
	}

	public void setTAX_AMT(double tAX_AMT) {
		TAX_AMT = tAX_AMT;
	}

	public double getTAX_RATE() {
		return TAX_RATE;
	}

	public void setTAX_RATE(double tAX_RATE) {
		TAX_RATE = tAX_RATE;
	}

	public double getINV_AMOUNT() {
		return INV_AMOUNT;
	}

	public void setINV_AMOUNT(double iNV_AMOUNT) {
		INV_AMOUNT = iNV_AMOUNT;
	}

	public double getCANCEL_AMT() {
		return CANCEL_AMT;
	}

	public void setCANCEL_AMT(double cANCEL_AMT) {
		CANCEL_AMT = cANCEL_AMT;
	}

	public double getPAID_AMT() {
		return PAID_AMT;
	}

	public void setPAID_AMT(double pAID_AMT) {
		PAID_AMT = pAID_AMT;
	}

	public double getNO_PAY_AMT() {
		return NO_PAY_AMT;
	}

	public void setNO_PAY_AMT(double nO_PAY_AMT) {
		NO_PAY_AMT = nO_PAY_AMT;
	}

	public double getOCCUPY_AMT() {
		return OCCUPY_AMT;
	}

	public void setOCCUPY_AMT(double oCCUPY_AMT) {
		OCCUPY_AMT = oCCUPY_AMT;
	}

	public double getADJ_AMT() {
		return ADJ_AMT;
	}

	public void setADJ_AMT(double aDJ_AMT) {
		ADJ_AMT = aDJ_AMT;
	}

	public double getREQ_AMT() {
		return REQ_AMT;
	}

	public void setREQ_AMT(double rEQ_AMT) {
		REQ_AMT = rEQ_AMT;
	}

	public double getNO_REQ_AMT() {
		return NO_REQ_AMT;
	}

	public void setNO_REQ_AMT(double nO_REQ_AMT) {
		NO_REQ_AMT = nO_REQ_AMT;
	}

	public String getAP_ACC_ID_DEBT() {
		return AP_ACC_ID_DEBT;
	}

	public void setAP_ACC_ID_DEBT(String aP_ACC_ID_DEBT) {
		AP_ACC_ID_DEBT = aP_ACC_ID_DEBT;
	}

	public String getAP_ACC_ID_TAX() {
		return AP_ACC_ID_TAX;
	}

	public void setAP_ACC_ID_TAX(String aP_ACC_ID_TAX) {
		AP_ACC_ID_TAX = aP_ACC_ID_TAX;
	}

	public String getAP_CCID_DEBT() {
		return AP_CCID_DEBT;
	}

	public void setAP_CCID_DEBT(String aP_CCID_DEBT) {
		AP_CCID_DEBT = aP_CCID_DEBT;
	}

	public String getAP_CCID_TAX() {
		return AP_CCID_TAX;
	}

	public void setAP_CCID_TAX(String aP_CCID_TAX) {
		AP_CCID_TAX = aP_CCID_TAX;
	}

	public String getINIT() {
		return INIT;
	}

	public void setINIT(String iNIT) {
		INIT = iNIT;
	}

	public String getSOURCE() {
		return SOURCE;
	}

	public void setSOURCE(String sOURCE) {
		SOURCE = sOURCE;
	}

	public String getV_HEAD_ID() {
		return V_HEAD_ID;
	}

	public void setV_HEAD_ID(String v_HEAD_ID) {
		V_HEAD_ID = v_HEAD_ID;
	}

	public String getV_STATUS() {
		return V_STATUS;
	}

	public void setV_STATUS(String v_STATUS) {
		V_STATUS = v_STATUS;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getVERIFIER_ID() {
		return VERIFIER_ID;
	}

	public void setVERIFIER_ID(String vERIFIER_ID) {
		VERIFIER_ID = vERIFIER_ID;
	}

	public Date getVERFY_DATE() {
		return VERFY_DATE;
	}

	public void setVERFY_DATE(Date vERFY_DATE) {
		VERFY_DATE = vERFY_DATE;
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

	public List<ApInvGlLBean> getApInvGlL() {
		return apInvGlL;
	}

	public void setApInvGlL(List<ApInvGlLBean> apInvGlL) {
		this.apInvGlL = apInvGlL;
	}
}
