package com.xzsoft.xc.ar.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName：ArInvoiceHBean
  * @Description：销售发票主表
  * @author：任建建
  * @date：2016年7月6日 上午10:58:56
 */
public class ArInvoiceHBean {
	private String AR_INV_H_ID;
	private String AR_INV_H_CODE;
	private String L_AR_INV_H_ID;
	private String AR_DOC_CAT_CODE;
	private String LEDGER_ID;
	private Date BIZ_DATE;
	private String CUSTOMER_ID;
	private String PROJECT_ID;
	private String DEPT_ID;
	private String AR_CONTRACT_ID;
	private double AMOUNT;
	private double TAX_AMT;
	private double TAX_RATE;
	private double INV_AMOUNT;
	private double CANCEL_AMT;
	private String INV_NO;
	private String DESCRIPTION;
	private String SYS_AUDIT_STATUS;
	private String SYS_AUDIT_STATUS_DESC;
	private String INS_CODE;
	private String AUDIT_STATUS;
	private String AUDIT_STATUS_DESC;
	private Date AUDIT_DATE;
	private String FIN_USER_ID;
	private Date FIN_DATE;
	private String PRINT_USER_ID;
	private Date PRINT_DATE;
	private String PRINT_STATUS;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;

	private List<ArInvoiceLBean> addInvoiceL;	//增加明细行
	private List<ArInvoiceLBean> updateInvoiceL;//更新明细行
	private List<String> deleteInvoiceLs;		//删除明细行
	public ArInvoiceHBean() {
		super();
	}
	public ArInvoiceHBean(String aR_INV_H_ID, String aR_INV_H_CODE,
			String l_AR_INV_H_ID, String aR_DOC_CAT_CODE, String lEDGER_ID,
			Date bIZ_DATE, String cUSTOMER_ID, String pROJECT_ID,
			String dEPT_ID, String aR_CONTRACT_ID, double aMOUNT, double tAX_AMT,
			double tAX_RATE, double iNV_AMOUNT, double cANCEL_AMT, String iNV_NO,
			String dESCRIPTION, String sYS_AUDIT_STATUS,
			String sYS_AUDIT_STATUS_DESC, String iNS_CODE, String aUDIT_STATUS,
			String aUDIT_STATUS_DESC, Date aUDIT_DATE, String fIN_USER_ID,
			Date fIN_DATE, String pRINT_USER_ID, Date pRINT_DATE,
			String pRINT_STATUS, Date cREATION_DATE, String cREATED_BY,
			Date lAST_UPDATE_DATE, String lAST_UPDATED_BY) {
		super();
		AR_INV_H_ID = aR_INV_H_ID;
		AR_INV_H_CODE = aR_INV_H_CODE;
		L_AR_INV_H_ID = l_AR_INV_H_ID;
		AR_DOC_CAT_CODE = aR_DOC_CAT_CODE;
		LEDGER_ID = lEDGER_ID;
		BIZ_DATE = bIZ_DATE;
		CUSTOMER_ID = cUSTOMER_ID;
		PROJECT_ID = pROJECT_ID;
		DEPT_ID = dEPT_ID;
		AR_CONTRACT_ID = aR_CONTRACT_ID;
		AMOUNT = aMOUNT;
		TAX_AMT = tAX_AMT;
		TAX_RATE = tAX_RATE;
		INV_AMOUNT = iNV_AMOUNT;
		CANCEL_AMT = cANCEL_AMT;
		INV_NO = iNV_NO;
		DESCRIPTION = dESCRIPTION;
		SYS_AUDIT_STATUS = sYS_AUDIT_STATUS;
		SYS_AUDIT_STATUS_DESC = sYS_AUDIT_STATUS_DESC;
		INS_CODE = iNS_CODE;
		AUDIT_STATUS = aUDIT_STATUS;
		AUDIT_STATUS_DESC = aUDIT_STATUS_DESC;
		AUDIT_DATE = aUDIT_DATE;
		FIN_USER_ID = fIN_USER_ID;
		FIN_DATE = fIN_DATE;
		PRINT_USER_ID = pRINT_USER_ID;
		PRINT_DATE = pRINT_DATE;
		PRINT_STATUS = pRINT_STATUS;
		CREATION_DATE = cREATION_DATE;
		CREATED_BY = cREATED_BY;
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}
	public String getAR_INV_H_ID() {
		return AR_INV_H_ID;
	}
	public void setAR_INV_H_ID(String aR_INV_H_ID) {
		AR_INV_H_ID = aR_INV_H_ID;
	}
	public String getAR_INV_H_CODE() {
		return AR_INV_H_CODE;
	}
	public void setAR_INV_H_CODE(String aR_INV_H_CODE) {
		AR_INV_H_CODE = aR_INV_H_CODE;
	}
	public String getL_AR_INV_H_ID() {
		return L_AR_INV_H_ID;
	}
	public void setL_AR_INV_H_ID(String l_AR_INV_H_ID) {
		L_AR_INV_H_ID = l_AR_INV_H_ID;
	}
	public String getAR_DOC_CAT_CODE() {
		return AR_DOC_CAT_CODE;
	}
	public void setAR_DOC_CAT_CODE(String aR_DOC_CAT_CODE) {
		AR_DOC_CAT_CODE = aR_DOC_CAT_CODE;
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
	public String getCUSTOMER_ID() {
		return CUSTOMER_ID;
	}
	public void setCUSTOMER_ID(String cUSTOMER_ID) {
		CUSTOMER_ID = cUSTOMER_ID;
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
	public String getAR_CONTRACT_ID() {
		return AR_CONTRACT_ID;
	}
	public void setAR_CONTRACT_ID(String aR_CONTRACT_ID) {
		AR_CONTRACT_ID = aR_CONTRACT_ID;
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
	public Date getAUDIT_DATE() {
		return AUDIT_DATE;
	}
	public void setAUDIT_DATE(Date aUDIT_DATE) {
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
	public String getPRINT_USER_ID() {
		return PRINT_USER_ID;
	}
	public void setPRINT_USER_ID(String pRINT_USER_ID) {
		PRINT_USER_ID = pRINT_USER_ID;
	}
	public Date getPRINT_DATE() {
		return PRINT_DATE;
	}
	public void setPRINT_DATE(Date pRINT_DATE) {
		PRINT_DATE = pRINT_DATE;
	}
	public String getPRINT_STATUS() {
		return PRINT_STATUS;
	}
	public void setPRINT_STATUS(String pRINT_STATUS) {
		PRINT_STATUS = pRINT_STATUS;
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
	public List<ArInvoiceLBean> getAddInvoiceL() {
		return addInvoiceL;
	}
	public void setAddInvoiceL(List<ArInvoiceLBean> addInvoiceL) {
		this.addInvoiceL = addInvoiceL;
	}
	public List<ArInvoiceLBean> getUpdateInvoiceL() {
		return updateInvoiceL;
	}
	public void setUpdateInvoiceL(List<ArInvoiceLBean> updateInvoiceL) {
		this.updateInvoiceL = updateInvoiceL;
	}
	public List<String> getDeleteInvoiceLs() {
		return deleteInvoiceLs;
	}
	public void setDeleteInvoiceLs(List<String> deleteInvoiceLs) {
		this.deleteInvoiceLs = deleteInvoiceLs;
	}
}