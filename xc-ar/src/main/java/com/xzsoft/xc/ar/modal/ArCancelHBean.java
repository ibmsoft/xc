package com.xzsoft.xc.ar.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName：ArCancelHBean
  * @Description：核销单主表
  * @author：任建建
  * @date：2016年7月6日 上午11:17:38
 */
public class ArCancelHBean {
	private String AR_CANCEL_H_ID;
	private String AR_CANCEL_H_CODE;
	private String LEDGER_ID;
	private String AR_CANCEL_TYPE;
	private Date GL_DATE;
	private String SRC_ID;
	private Double SRC_AMT;
	private String DESCRIPTION;
	private String V_HEAD_ID;
	private String V_STATUS;
	private String VERIFIER_ID;
	private Date VERFY_DATE;
	private Date CREATION_DATE;
	private String CREATED_BY;
	private Date LAST_UPDATE_DATE;
	private String LAST_UPDATED_BY;
	
	private List<ArCancelLBean> arCancelL;
	public ArCancelHBean(String aR_CANCEL_H_ID, String aR_CANCEL_H_CODE,
			String lEDGER_ID, String aR_CANCEL_TYPE, Date gL_DATE,
			String sRC_ID, Double sRC_AMT, String dESCRIPTION, String v_HEAD_ID,
			String v_STATUS, String vERIFIER_ID, Date vERFY_DATE,
			Date cREATION_DATE, String cREATED_BY, Date lAST_UPDATE_DATE,
			String lAST_UPDATED_BY, List<ArCancelLBean> arCancelL) {
		super();
		AR_CANCEL_H_ID = aR_CANCEL_H_ID;
		AR_CANCEL_H_CODE = aR_CANCEL_H_CODE;
		LEDGER_ID = lEDGER_ID;
		AR_CANCEL_TYPE = aR_CANCEL_TYPE;
		GL_DATE = gL_DATE;
		SRC_ID = sRC_ID;
		SRC_AMT = sRC_AMT;
		DESCRIPTION = dESCRIPTION;
		V_HEAD_ID = v_HEAD_ID;
		V_STATUS = v_STATUS;
		VERIFIER_ID = vERIFIER_ID;
		VERFY_DATE = vERFY_DATE;
		CREATION_DATE = cREATION_DATE;
		CREATED_BY = cREATED_BY;
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
		LAST_UPDATED_BY = lAST_UPDATED_BY;
		this.arCancelL = arCancelL;
	}
	public ArCancelHBean() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getLEDGER_ID() {
		return LEDGER_ID;
	}

	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}

	public String getAR_CANCEL_TYPE() {
		return AR_CANCEL_TYPE;
	}

	public void setAR_CANCEL_TYPE(String aR_CANCEL_TYPE) {
		AR_CANCEL_TYPE = aR_CANCEL_TYPE;
	}

	public Date getGL_DATE() {
		return GL_DATE;
	}

	public void setGL_DATE(Date gL_DATE) {
		GL_DATE = gL_DATE;
	}

	public String getSRC_ID() {
		return SRC_ID;
	}

	public void setSRC_ID(String sRC_ID) {
		SRC_ID = sRC_ID;
	}

	public Double getSRC_AMT() {
		return SRC_AMT;
	}

	public void setSRC_AMT(Double sRC_AMT) {
		SRC_AMT = sRC_AMT;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
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

	public List<ArCancelLBean> getArCancelL() {
		return arCancelL;
	}

	public void setArCancelL(List<ArCancelLBean> arCancelL) {
		this.arCancelL = arCancelL;
	}
}
