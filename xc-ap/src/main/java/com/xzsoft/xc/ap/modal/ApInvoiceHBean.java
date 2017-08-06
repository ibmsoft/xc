package com.xzsoft.xc.ap.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName：ApInvoiceHBean
  * @Description：采购发票主表实体类
  * @author：任建建
  * @date：2016年6月14日 下午2:44:37
 */
public class ApInvoiceHBean {
	private String AP_INV_H_ID;			//主键
	private String AP_INV_H_CODE;		//单据号
	private String AP_DOC_CAT_CODE;		//单据类型
	private String L_AP_INV_H_ID;		//蓝单采购发票主ID
	private String LEDGER_ID;			//账簿ID
	private Date BIZ_DATE;				//业务日期
	private String VENDOR_ID;			//供应商
	private String PROJECT_ID;			//项目ID
	private String DEPT_ID;				//成本中心ID
	private String AP_CONTRACT_ID;		//所属合同
	private double AMOUNT;				//金额(不含税)
	private double TAX_AMT;				//税额
	private double TAX_RATE;			//税率
	private double INV_AMOUNT;			//含税金额
	private double CANCEL_AMT;			//核销预付金额(行汇总，冗余)
	private String INV_NO;				//原始发票号
	private String DESCRIPTION;			//摘要
	private String VERIFIER_ID;			//审核人
	private String SYS_AUDIT_STATUS;	//业务审批状态
	private String SYS_AUDIT_STATUS_DESC;//业务审批状态描述
	private String INS_CODE;			//流程实例编码
	private String AUDIT_STATUS;		//审批状态
	private String AUDIT_STATUS_DESC;	//审批状态描述
	private String AUDIT_DATE;			//审批通过日期
	private String FIN_USER_ID;			//复核人
	private Date FIN_DATE;				//复核日期
	private String SIGN_USER_ID;		//原始单据签收人
	private Date SIGN_DATE;				//原始单据签收日期
	private String SIGN_STATUS;			//签收状态：1-待签收，2-已签收，3-拒签
	private Date CREATION_DATE;			//创建日期
	private String CREATED_BY;			//创建人
	private Date LAST_UPDATE_DATE;		//最后更新日期
	private String LAST_UPDATED_BY;		//最后更新人
	private String SOURCE;				//来源：应付系统、采购系统
	
	private List<ApInvoiceLBean> apInvoiceL;	//应付单明细表
	private List<ApInvoiceLBean> updateInvoiceL;//更新明细行
	private List<String> apInvoiceLs;
	public ApInvoiceHBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAP_INV_H_ID() {
		return AP_INV_H_ID;
	}
	public void setAP_INV_H_ID(String aP_INV_H_ID) {
		AP_INV_H_ID = aP_INV_H_ID;
	}
	public String getAP_INV_H_CODE() {
		return AP_INV_H_CODE;
	}
	public void setAP_INV_H_CODE(String aP_INV_H_CODE) {
		AP_INV_H_CODE = aP_INV_H_CODE;
	}
	public String getAP_DOC_CAT_CODE() {
		return AP_DOC_CAT_CODE;
	}
	public void setAP_DOC_CAT_CODE(String aP_DOC_CAT_CODE) {
		AP_DOC_CAT_CODE = aP_DOC_CAT_CODE;
	}
	public String getL_AP_INV_H_ID() {
		return L_AP_INV_H_ID;
	}
	public void setL_AP_INV_H_ID(String l_AP_INV_H_ID) {
		L_AP_INV_H_ID = l_AP_INV_H_ID;
	}
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}
	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}
	public Date getBIZ_DATE() {
		return BIZ_DATE;
	}
	public void setBIZ_DATE(Date bIZ_DATE) {
		BIZ_DATE = bIZ_DATE;
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
	public String getINV_NO() {
		return INV_NO;
	}
	public void setINV_NO(String iNV_NO) {
		INV_NO = iNV_NO;
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
	public String getSYS_AUDIT_STATUS() {
		return SYS_AUDIT_STATUS;
	}
	public void setSYS_AUDIT_STATUS(String sYS_AUDIT_STATUS) {
		SYS_AUDIT_STATUS = sYS_AUDIT_STATUS;
	}
	public String getSYS_AUDIT_STATUS_DESC() {
		return SYS_AUDIT_STATUS_DESC;
	}
	public void setSYS_AUDIT_STATUS_DESC(String sYS_AUDIT_STATUS_DESC) {
		SYS_AUDIT_STATUS_DESC = sYS_AUDIT_STATUS_DESC;
	}
	public String getINS_CODE() {
		return INS_CODE;
	}
	public void setINS_CODE(String iNS_CODE) {
		INS_CODE = iNS_CODE;
	}
	public String getAUDIT_STATUS() {
		return AUDIT_STATUS;
	}
	public void setAUDIT_STATUS(String aUDIT_STATUS) {
		AUDIT_STATUS = aUDIT_STATUS;
	}
	public String getAUDIT_STATUS_DESC() {
		return AUDIT_STATUS_DESC;
	}
	public void setAUDIT_STATUS_DESC(String aUDIT_STATUS_DESC) {
		AUDIT_STATUS_DESC = aUDIT_STATUS_DESC;
	}
	public String getAUDIT_DATE() {
		return AUDIT_DATE;
	}
	public void setAUDIT_DATE(String aUDIT_DATE) {
		AUDIT_DATE = aUDIT_DATE;
	}
	public String getFIN_USER_ID() {
		return FIN_USER_ID;
	}
	public void setFIN_USER_ID(String fIN_USER_ID) {
		FIN_USER_ID = fIN_USER_ID;
	}
	public Date getFIN_DATE() {
		return FIN_DATE;
	}
	public void setFIN_DATE(Date fIN_DATE) {
		FIN_DATE = fIN_DATE;
	}
	public String getSIGN_USER_ID() {
		return SIGN_USER_ID;
	}
	public void setSIGN_USER_ID(String sIGN_USER_ID) {
		SIGN_USER_ID = sIGN_USER_ID;
	}
	public Date getSIGN_DATE() {
		return SIGN_DATE;
	}
	public void setSIGN_DATE(Date sIGN_DATE) {
		SIGN_DATE = sIGN_DATE;
	}
	public String getSIGN_STATUS() {
		return SIGN_STATUS;
	}
	public void setSIGN_STATUS(String sIGN_STATUS) {
		SIGN_STATUS = sIGN_STATUS;
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
	public List<ApInvoiceLBean> getApInvoiceL() {
		return apInvoiceL;
	}
	public void setApInvoiceL(List<ApInvoiceLBean> apInvoiceL) {
		this.apInvoiceL = apInvoiceL;
	}
	public List<ApInvoiceLBean> getUpdateInvoiceL() {
		return updateInvoiceL;
	}
	public void setUpdateInvoiceL(List<ApInvoiceLBean> updateInvoiceL) {
		this.updateInvoiceL = updateInvoiceL;
	}
	public List<String> getApInvoiceLs() {
		return apInvoiceLs;
	}
	public void setApInvoiceLs(List<String> apInvoiceLs) {
		this.apInvoiceLs = apInvoiceLs;
	}
	public String getSOURCE() {
		return SOURCE;
	}
	public void setSOURCE(String sOURCE) {
		SOURCE = sOURCE;
	}
}
