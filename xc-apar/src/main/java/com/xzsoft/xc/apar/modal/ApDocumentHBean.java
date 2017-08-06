package com.xzsoft.xc.apar.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName: ApDocumentHBean
  * @Description: 应付模块所有需要生成凭证的单据主表实体
  * @author 任建建
  * @date 2016年4月7日 上午11:04:32
 */
public class ApDocumentHBean {
	//采购发票信息
	private String AP_INV_H_CODE;		//单据号
	private String L_AP_INV_H_ID;		//蓝单采购发票主ID
	private Date BIZ_DATE;				//业务日期
	private String SYS_AUDIT_STATUS;	//业务审批状态
	private String SYS_AUDIT_STATUS_DESC;//业务审批状态描述
	private String INS_CODE;			//流程实例编码
	private String AUDIT_STATUS;		//审批状态
	private String AUDIT_STATUS_DESC;	//审批状态描述
	private String AUDIT_DATE;			//审批通过日期
	private String FIN_USER_ID;			//复核人
	private Date FIN_DATE;				//复核日期
	
	//应付单表信息
	private String AP_INV_GL_H_ID;		//应付单主ID
	private String AP_INV_GL_H_CODE;	//单据号
	private String AP_INV_H_ID;			//采购发票主ID
	private String AP_DOC_CAT_CODE;		//单据类型
	private String L_AP_INV_GL_H_ID;	//蓝字应付单ID
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
	private String INV_NO;				//原始发票号
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
	
	//付款单
	private String AP_PAY_H_ID;			//付款单主ID
	private String AP_PAY_H_CODE;		//单据号
	private String AP_PAY_H_DESC;		//单据类型
	private String PAY_TYPE;			//付款方式：1-现金，2-银行 
	private String DEPOSIT_BANK_ID;		//付款账户
	private String PAY_ACC_ID;			//付款科目
	private String PAY_CCID;			//付款组合科目
	private String CA_ID;				//现金流量项
	private String ACCOUNT_NAME;		//开户名
	private String DEPOSIT_BANK_NAME;	//开户行
	private String BANK_ACCOUNT;		//账户
	private String SIGN_USER_ID;		//原始单据签收人
	private String SIGN_STATUS;			//签收状态：1-待签收，2-已签收，3-拒签
	private Date SIGN_DATE;				//原始单据签收日期
	
	//核销单
	private String AP_CANCEL_H_ID;		//核销主表ID
	private String AP_CANCEL_H_CODE;	//核销单号
	private String AP_CANCEL_TYPE;		//核销类型
	private String SRC_ID;				//核销源单号
	private double SRC_AMT;				//核销源单金额
	
	//应付单余额调整表
	private String GL_ADJ_ID;			//余额调整ID
	private String GL_ADJ_CODE;			//调整单号
	private String TO_CCID;				//调整科目
	private String DR_OR_CR;			//借/贷: 借-DR 贷-CR
	
	//单据行表信息
	private List<ApDocumentLBean> apDocumentLBean;

	//增加单据行表信息
	private List<ApDocumentLBean> addDocument;
	//更新单据行信息
	private List<ApDocumentLBean> updateDocument;
	//删除单据行信息
	private List<String> deleteDocument;
	
	public List<ApDocumentLBean> getApDocumentLBean() {
		return apDocumentLBean;
	}
	public void setApDocumentLBean(List<ApDocumentLBean> apDocumentLBean) {
		this.apDocumentLBean = apDocumentLBean;
	}
	
	public List<ApDocumentLBean> getAddDocument() {
		return addDocument;
	}
	public void setAddDocument(List<ApDocumentLBean> addDocument) {
		this.addDocument = addDocument;
	}
	public List<ApDocumentLBean> getUpdateDocument() {
		return updateDocument;
	}
	public void setUpdateDocument(List<ApDocumentLBean> updateDocument) {
		this.updateDocument = updateDocument;
	}
	public List<String> getDeleteDocument() {
		return deleteDocument;
	}
	public void setDeleteDocument(List<String> deleteDocument) {
		this.deleteDocument = deleteDocument;
	}
	public ApDocumentHBean() {
		super();
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
	public String getAP_INV_H_ID() {
		return AP_INV_H_ID;
	}
	public void setAP_INV_H_ID(String aP_INV_H_ID) {
		AP_INV_H_ID = aP_INV_H_ID;
	}
	public String getAP_DOC_CAT_CODE() {
		return AP_DOC_CAT_CODE;
	}
	public void setAP_DOC_CAT_CODE(String aP_DOC_CAT_CODE) {
		AP_DOC_CAT_CODE = aP_DOC_CAT_CODE;
	}
	public String getL_AP_INV_GL_H_ID() {
		return L_AP_INV_GL_H_ID;
	}
	public void setL_AP_INV_GL_H_ID(String l_AP_INV_GL_H_ID) {
		L_AP_INV_GL_H_ID = l_AP_INV_GL_H_ID;
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
	public String getINV_NO() {
		return INV_NO;
	}
	public void setINV_NO(String iNV_NO) {
		INV_NO = iNV_NO;
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
	public String getAP_PAY_H_ID() {
		return AP_PAY_H_ID;
	}
	public void setAP_PAY_H_ID(String aP_PAY_H_ID) {
		AP_PAY_H_ID = aP_PAY_H_ID;
	}
	public String getAP_PAY_H_CODE() {
		return AP_PAY_H_CODE;
	}
	public void setAP_PAY_H_CODE(String aP_PAY_H_CODE) {
		AP_PAY_H_CODE = aP_PAY_H_CODE;
	}
	public String getAP_PAY_H_DESC() {
		return AP_PAY_H_DESC;
	}
	public void setAP_PAY_H_DESC(String aP_PAY_H_DESC) {
		AP_PAY_H_DESC = aP_PAY_H_DESC;
	}
	public String getPAY_TYPE() {
		return PAY_TYPE;
	}
	public void setPAY_TYPE(String pAY_TYPE) {
		PAY_TYPE = pAY_TYPE;
	}
	public String getDEPOSIT_BANK_ID() {
		return DEPOSIT_BANK_ID;
	}
	public void setDEPOSIT_BANK_ID(String dEPOSIT_BANK_ID) {
		DEPOSIT_BANK_ID = dEPOSIT_BANK_ID;
	}
	public String getPAY_ACC_ID() {
		return PAY_ACC_ID;
	}
	public void setPAY_ACC_ID(String pAY_ACC_ID) {
		PAY_ACC_ID = pAY_ACC_ID;
	}
	public String getPAY_CCID() {
		return PAY_CCID;
	}
	public void setPAY_CCID(String pAY_CCID) {
		PAY_CCID = pAY_CCID;
	}
	public String getCA_ID() {
		return CA_ID;
	}
	public void setCA_ID(String cA_ID) {
		CA_ID = cA_ID;
	}
	public String getACCOUNT_NAME() {
		return ACCOUNT_NAME;
	}
	public void setACCOUNT_NAME(String aCCOUNT_NAME) {
		ACCOUNT_NAME = aCCOUNT_NAME;
	}
	public String getDEPOSIT_BANK_NAME() {
		return DEPOSIT_BANK_NAME;
	}
	public void setDEPOSIT_BANK_NAME(String dEPOSIT_BANK_NAME) {
		DEPOSIT_BANK_NAME = dEPOSIT_BANK_NAME;
	}
	public String getBANK_ACCOUNT() {
		return BANK_ACCOUNT;
	}
	public void setBANK_ACCOUNT(String bANK_ACCOUNT) {
		BANK_ACCOUNT = bANK_ACCOUNT;
	}
	public String getSIGN_USER_ID() {
		return SIGN_USER_ID;
	}
	public void setSIGN_USER_ID(String sIGN_USER_ID) {
		SIGN_USER_ID = sIGN_USER_ID;
	}
	public String getSIGN_STATUS() {
		return SIGN_STATUS;
	}
	public void setSIGN_STATUS(String sIGN_STATUS) {
		SIGN_STATUS = sIGN_STATUS;
	}
	public Date getSIGN_DATE() {
		return SIGN_DATE;
	}
	public void setSIGN_DATE(Date sIGN_DATE) {
		SIGN_DATE = sIGN_DATE;
	}
	public String getAP_CANCEL_H_ID() {
		return AP_CANCEL_H_ID;
	}
	public void setAP_CANCEL_H_ID(String aP_CANCEL_H_ID) {
		AP_CANCEL_H_ID = aP_CANCEL_H_ID;
	}
	public String getAP_CANCEL_H_CODE() {
		return AP_CANCEL_H_CODE;
	}
	public void setAP_CANCEL_H_CODE(String aP_CANCEL_H_CODE) {
		AP_CANCEL_H_CODE = aP_CANCEL_H_CODE;
	}
	public String getAP_CANCEL_TYPE() {
		return AP_CANCEL_TYPE;
	}
	public void setAP_CANCEL_TYPE(String aP_CANCEL_TYPE) {
		AP_CANCEL_TYPE = aP_CANCEL_TYPE;
	}
	public String getSRC_ID() {
		return SRC_ID;
	}
	public void setSRC_ID(String sRC_ID) {
		SRC_ID = sRC_ID;
	}
	public double getSRC_AMT() {
		return SRC_AMT;
	}
	public void setSRC_AMT(double sRC_AMT) {
		SRC_AMT = sRC_AMT;
	}
	public String getGL_ADJ_ID() {
		return GL_ADJ_ID;
	}
	public void setGL_ADJ_ID(String gL_ADJ_ID) {
		GL_ADJ_ID = gL_ADJ_ID;
	}
	public String getGL_ADJ_CODE() {
		return GL_ADJ_CODE;
	}
	public void setGL_ADJ_CODE(String gL_ADJ_CODE) {
		GL_ADJ_CODE = gL_ADJ_CODE;
	}
	public String getTO_CCID() {
		return TO_CCID;
	}
	public void setTO_CCID(String tO_CCID) {
		TO_CCID = tO_CCID;
	}
	public String getDR_OR_CR() {
		return DR_OR_CR;
	}
	public void setDR_OR_CR(String dR_OR_CR) {
		DR_OR_CR = dR_OR_CR;
	}
	public String getAP_INV_H_CODE() {
		return AP_INV_H_CODE;
	}
	public void setAP_INV_H_CODE(String aP_INV_H_CODE) {
		AP_INV_H_CODE = aP_INV_H_CODE;
	}
	public String getL_AP_INV_H_ID() {
		return L_AP_INV_H_ID;
	}
	public void setL_AP_INV_H_ID(String l_AP_INV_H_ID) {
		L_AP_INV_H_ID = l_AP_INV_H_ID;
	}
	public Date getBIZ_DATE() {
		return BIZ_DATE;
	}
	public void setBIZ_DATE(Date bIZ_DATE) {
		BIZ_DATE = bIZ_DATE;
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
}
