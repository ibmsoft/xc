package com.xzsoft.xc.ap.modal;

import java.util.Date;

/**
 * 
  * @ClassName: ApInvGlAdj
  * @Description: 调整单表(应付单余额调整)
  * @author 任建建
  * @date 2016年4月6日 下午4:54:45
 */
public class ApInvGlAdj {
	private String GL_ADJ_ID;			//余额调整ID
	private String GL_ADJ_CODE;			//调整单号
	private String LEDGER_ID;			//账簿
	private String VENDOR_ID;			//供应商
	private String AP_INV_GL_H_ID;		//应付单号
	private Date GL_DATE;				//入账日期
	private String TO_CCID;				//调整科目
	private String DR_OR_CR;			//借/贷: 借-DR 贷-CR
	private Double ADJ_AMT;				//调整额
	private String V_HEAD_ID;			//凭证头ID
	private String V_STATUS;			//凭证状态
	private String DESCRIPTION;			//摘要
	private String VERIFIER_ID;			//审核人
	private Date VERFY_DATE;			//审核日期
	private Date CREATION_DATE;			//创建日期
	private String CREATED_BY;			//创建人
	private Date LAST_UPDATE_DATE;		//最后更新日期
	private String LAST_UPDATED_BY;		//最后更新人
	public ApInvGlAdj() {
		super();
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
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}
	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}
	public String getVENDOR_ID() {
		return VENDOR_ID;
	}
	public void setVENDOR_ID(String vENDOR_ID) {
		VENDOR_ID = vENDOR_ID;
	}
	public String getAP_INV_GL_H_ID() {
		return AP_INV_GL_H_ID;
	}
	public void setAP_INV_GL_H_ID(String aP_INV_GL_H_ID) {
		AP_INV_GL_H_ID = aP_INV_GL_H_ID;
	}
	public Date getGL_DATE() {
		return GL_DATE;
	}
	public void setGL_DATE(Date gL_DATE) {
		GL_DATE = gL_DATE;
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
	public Double getADJ_AMT() {
		return ADJ_AMT;
	}
	public void setADJ_AMT(Double aDJ_AMT) {
		ADJ_AMT = aDJ_AMT;
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
}
