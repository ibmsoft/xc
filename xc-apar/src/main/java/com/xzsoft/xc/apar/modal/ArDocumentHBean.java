package com.xzsoft.xc.apar.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName：ArDocumentHBean
  * @Description：应收模块所有需要生成凭证的单据主表实体
  * @author：任建建
  * @date：2016年7月8日 上午10:42:22
 */
public class ArDocumentHBean {
	//应收单表信息
	private String AR_INV_GL_H_ID;		//主键
	private String AR_INV_GL_H_CODE;	//单据号
	private String AR_INV_H_ID;			//发票主ID
	private String L_AR_INV_GL_H_ID;	//蓝票应付单主ID
	private String AR_DOC_CAT_CODE;		//单据类型
	private String LEDGER_ID;			//账簿ID
	private Date GL_DATE;				//入账日期
	private String CUSTOMER_ID;			//供应商
	private String PROJECT_ID;			//项目ID
	private String DEPT_ID;				//成本中心ID
	private String AR_CONTRACT_ID;		//所属合同
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
	private String AR_ACC_ID_DEBT;		//应收科目
	private String AR_ACC_ID_TAX;		//销项税科目
	private String AR_CCID_DEBT;		//应收科目组合
	private String AR_CCID_TAX;			//销项税科目组合
	private String INIT;				//期初: 0-未拆分 1-已拆分 2-正常单据
	private String SOURCE;				//来源 TEMP-手工销售类型 GL-手工应收单 INV-销售发票
	private String V_HEAD_ID;			//凭证头ID
	private String V_STATUS;			//凭证状态
	private String DESCRIPTION;			//摘要
	private String VERIFIER_ID;			//审核人
	private Date VERFY_DATE;			//审核日期
	private Date CREATION_DATE;			//创建日期
	private String CREATED_BY;			//创建人
	private Date LAST_UPDATE_DATE;		//最后更新日期
	private String LAST_UPDATED_BY;		//最后更新人
	
	//收款单
	private String AR_PAY_H_ID;
	private String AR_PAY_H_CODE;
	private String PAY_TYPE;
	private String PAY_NO;
	private Date PAY_DATE;
	private String SALE_ACC_ID;
	private String SALE_CCID;
	private String CA_ID;
	private String BANK_ACCOUNT;
	private String SIGNATORY_ID;
	private String SIGNATORY;
	private Date SIGN_DATE;
	
	//核销单
	private String AR_CANCEL_H_ID;
	private String AR_CANCEL_H_CODE;
	private String AR_CANCEL_TYPE;
	private String SRC_ID;
	private double SRC_AMT;
	
	//余额调整表
	private String GL_ADJ_ID;			//余额调整ID
	private String GL_ADJ_CODE;			//调整单号
	private String TO_CCID;				//调整科目
	private String DR_OR_CR;			//借/贷: 借-DR 贷-CR
	
	//增加单据行表信息
	private List<ArDocumentLBean> addDocument;
	//更新单据行信息
	private List<ArDocumentLBean> updateDocument;
	//删除单据行信息
	private List<String> deleteDocument;
	
	public ArDocumentHBean() {
		super();
	}
	public String getAR_INV_GL_H_ID() {
		return AR_INV_GL_H_ID;
	}
	public void setAR_INV_GL_H_ID(String aR_INV_GL_H_ID) {
		AR_INV_GL_H_ID = aR_INV_GL_H_ID;
	}
	public String getAR_INV_GL_H_CODE() {
		return AR_INV_GL_H_CODE;
	}
	public void setAR_INV_GL_H_CODE(String aR_INV_GL_H_CODE) {
		AR_INV_GL_H_CODE = aR_INV_GL_H_CODE;
	}
	public String getAR_INV_H_ID() {
		return AR_INV_H_ID;
	}
	public void setAR_INV_H_ID(String aR_INV_H_ID) {
		AR_INV_H_ID = aR_INV_H_ID;
	}
	public String getL_AR_INV_GL_H_ID() {
		return L_AR_INV_GL_H_ID;
	}
	public void setL_AR_INV_GL_H_ID(String l_AR_INV_GL_H_ID) {
		L_AR_INV_GL_H_ID = l_AR_INV_GL_H_ID;
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
	public Date getGL_DATE() {
		return GL_DATE;
	}
	public void setGL_DATE(Date gL_DATE) {
		GL_DATE = gL_DATE;
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
	public String getAR_ACC_ID_DEBT() {
		return AR_ACC_ID_DEBT;
	}
	public void setAR_ACC_ID_DEBT(String aR_ACC_ID_DEBT) {
		AR_ACC_ID_DEBT = aR_ACC_ID_DEBT;
	}
	public String getAR_ACC_ID_TAX() {
		return AR_ACC_ID_TAX;
	}
	public void setAR_ACC_ID_TAX(String aR_ACC_ID_TAX) {
		AR_ACC_ID_TAX = aR_ACC_ID_TAX;
	}
	public String getAR_CCID_DEBT() {
		return AR_CCID_DEBT;
	}
	public void setAR_CCID_DEBT(String aR_CCID_DEBT) {
		AR_CCID_DEBT = aR_CCID_DEBT;
	}
	public String getAR_CCID_TAX() {
		return AR_CCID_TAX;
	}
	public void setAR_CCID_TAX(String aR_CCID_TAX) {
		AR_CCID_TAX = aR_CCID_TAX;
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
	public String getAR_PAY_H_ID() {
		return AR_PAY_H_ID;
	}
	public void setAR_PAY_H_ID(String aR_PAY_H_ID) {
		AR_PAY_H_ID = aR_PAY_H_ID;
	}
	public String getAR_PAY_H_CODE() {
		return AR_PAY_H_CODE;
	}
	public void setAR_PAY_H_CODE(String aR_PAY_H_CODE) {
		AR_PAY_H_CODE = aR_PAY_H_CODE;
	}
	public String getPAY_TYPE() {
		return PAY_TYPE;
	}
	public void setPAY_TYPE(String pAY_TYPE) {
		PAY_TYPE = pAY_TYPE;
	}
	public String getPAY_NO() {
		return PAY_NO;
	}
	public void setPAY_NO(String pAY_NO) {
		PAY_NO = pAY_NO;
	}
	public Date getPAY_DATE() {
		return PAY_DATE;
	}
	public void setPAY_DATE(Date pAY_DATE) {
		PAY_DATE = pAY_DATE;
	}
	public String getSALE_ACC_ID() {
		return SALE_ACC_ID;
	}
	public void setSALE_ACC_ID(String sALE_ACC_ID) {
		SALE_ACC_ID = sALE_ACC_ID;
	}
	public String getSALE_CCID() {
		return SALE_CCID;
	}
	public void setSALE_CCID(String sALE_CCID) {
		SALE_CCID = sALE_CCID;
	}
	public String getCA_ID() {
		return CA_ID;
	}
	public void setCA_ID(String cA_ID) {
		CA_ID = cA_ID;
	}
	public String getBANK_ACCOUNT() {
		return BANK_ACCOUNT;
	}
	public void setBANK_ACCOUNT(String bANK_ACCOUNT) {
		BANK_ACCOUNT = bANK_ACCOUNT;
	}
	public String getSIGNATORY_ID() {
		return SIGNATORY_ID;
	}
	public void setSIGNATORY_ID(String sIGNATORY_ID) {
		SIGNATORY_ID = sIGNATORY_ID;
	}
	public String getSIGNATORY() {
		return SIGNATORY;
	}
	public void setSIGNATORY(String sIGNATORY) {
		SIGNATORY = sIGNATORY;
	}
	public Date getSIGN_DATE() {
		return SIGN_DATE;
	}
	public void setSIGN_DATE(Date sIGN_DATE) {
		SIGN_DATE = sIGN_DATE;
	}
	public String getAR_CANCEL_H_ID() {
		return AR_CANCEL_H_ID;
	}
	public void setAR_CANCEL_H_ID(String aR_CANCEL_H_ID) {
		AR_CANCEL_H_ID = aR_CANCEL_H_ID;
	}
	public String getAR_CANCEL_H_CODE() {
		return AR_CANCEL_H_CODE;
	}
	public void setAR_CANCEL_H_CODE(String aR_CANCEL_H_CODE) {
		AR_CANCEL_H_CODE = aR_CANCEL_H_CODE;
	}
	public String getAR_CANCEL_TYPE() {
		return AR_CANCEL_TYPE;
	}
	public void setAR_CANCEL_TYPE(String aR_CANCEL_TYPE) {
		AR_CANCEL_TYPE = aR_CANCEL_TYPE;
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
	public List<ArDocumentLBean> getAddDocument() {
		return addDocument;
	}
	public void setAddDocument(List<ArDocumentLBean> addDocument) {
		this.addDocument = addDocument;
	}
	public List<ArDocumentLBean> getUpdateDocument() {
		return updateDocument;
	}
	public void setUpdateDocument(List<ArDocumentLBean> updateDocument) {
		this.updateDocument = updateDocument;
	}
	public List<String> getDeleteDocument() {
		return deleteDocument;
	}
	public void setDeleteDocument(List<String> deleteDocument) {
		this.deleteDocument = deleteDocument;
	}
}