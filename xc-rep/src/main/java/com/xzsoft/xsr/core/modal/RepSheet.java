package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;
/**
 * @project 报表3.0
 * @version v1.0
 * @author songyh
 * @Date 2016-01-27
 * @desc 报表表：xsr_rep_sheets
 */
public class RepSheet {
	private String SHEET_ID;
	private String SUIT_ID;
	private String MODALTYPE_ID;
	private String MODALSHEET_ID;
	private String ENTITY_ID;
	private String PERIOD_ID;
	private String CURRENCY_ID; 
	private String MODALTYPE_CODE; 
	private String MODALSHEET_CODE; 
	private String ENTITY_CODE; 
	private String PERIOD_CODE;
	
	private String CURRENCY_CODE; 
	private String APP_STATUS; 
	private String DESCRIPTION; 
	private String COMMIT_STATUS;
	private String SUB_ENTITY_ID; 
	private String VERIFY_STATUS; 
	private String IMPTYPE; 
	private int IMPTIMES;
	private Timestamp IMPLASTDATE; 
	private String IMPER; 
	private int INER_IMPTIMES; 
	private Timestamp INER_IMPLASTDATE;
	private String INER_IMPER; 
	private String ITEM_KEY; 
	private String AUDIT_STATUS; 
	private Timestamp AUDIT_DATE;
	private Timestamp CREATION_DATE; //CREATION_DATE
	private String CREATED_BY; //CREATED_BY
	private Timestamp LAST_UPDATE_DATE; //CREATION_DATE
	private String LAST_UPDATED_BY; //CREATED_BY
	public String getSHEET_ID() {
		return SHEET_ID;
	}
	public void setSHEET_ID(String sHEET_ID) {
		SHEET_ID = sHEET_ID;
	}
	public String getSUIT_ID() {
		return SUIT_ID;
	}
	public void setSUIT_ID(String sUIT_ID) {
		SUIT_ID = sUIT_ID;
	}
	public String getMODALTYPE_ID() {
		return MODALTYPE_ID;
	}
	public void setMODALTYPE_ID(String mODALTYPE_ID) {
		MODALTYPE_ID = mODALTYPE_ID;
	}
	public String getMODALSHEET_ID() {
		return MODALSHEET_ID;
	}
	public void setMODALSHEET_ID(String modalsheetId) {
		MODALSHEET_ID = modalsheetId;
	}
	public String getENTITY_ID() {
		return ENTITY_ID;
	}
	public void setENTITY_ID(String eNTITY_ID) {
		ENTITY_ID = eNTITY_ID;
	}
	public String getPERIOD_ID() {
		return PERIOD_ID;
	}
	public void setPERIOD_ID(String pERIOD_ID) {
		PERIOD_ID = pERIOD_ID;
	}
	public String getCURRENCY_ID() {
		return CURRENCY_ID;
	}
	public void setCURRENCY_ID(String cURRENCY_ID) {
		CURRENCY_ID = cURRENCY_ID;
	}
	public String getMODALTYPE_CODE() {
		return MODALTYPE_CODE;
	}
	public void setMODALTYPE_CODE(String mODALTYPE_CODE) {
		MODALTYPE_CODE = mODALTYPE_CODE;
	}
	public String getMODALSHEET_CODE() {
		return MODALSHEET_CODE;
	}
	public void setMODALSHEET_CODE(String mODALSHEET_CODE) {
		MODALSHEET_CODE = mODALSHEET_CODE;
	}
	public String getENTITY_CODE() {
		return ENTITY_CODE;
	}
	public void setENTITY_CODE(String eNTITY_CODE) {
		ENTITY_CODE = eNTITY_CODE;
	}
	public String getPERIOD_CODE() {
		return PERIOD_CODE;
	}
	public void setPERIOD_CODE(String pERIOD_CODE) {
		PERIOD_CODE = pERIOD_CODE;
	}
	public String getCURRENCY_CODE() {
		return CURRENCY_CODE;
	}
	public void setCURRENCY_CODE(String cURRENCY_CODE) {
		CURRENCY_CODE = cURRENCY_CODE;
	}
	public String getAPP_STATUS() {
		return APP_STATUS;
	}
	public void setAPP_STATUS(String aPP_STATUS) {
		APP_STATUS = aPP_STATUS;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getCOMMIT_STATUS() {
		return COMMIT_STATUS;
	}
	public void setCOMMIT_STATUS(String cOMMIT_STATUS) {
		COMMIT_STATUS = cOMMIT_STATUS;
	}
	public String getSUB_ENTITY_ID() {
		return SUB_ENTITY_ID;
	}
	public void setSUB_ENTITY_ID(String sUB_ENTITY_ID) {
		SUB_ENTITY_ID = sUB_ENTITY_ID;
	}
	public String getVERIFY_STATUS() {
		return VERIFY_STATUS;
	}
	public void setVERIFY_STATUS(String vERIFY_STATUS) {
		VERIFY_STATUS = vERIFY_STATUS;
	}
	public String getIMPTYPE() {
		return IMPTYPE;
	}
	public void setIMPTYPE(String iMPTYPE) {
		IMPTYPE = iMPTYPE;
	}
	public int getIMPTIMES() {
		return IMPTIMES;
	}
	public void setIMPTIMES(int iMPTIMES) {
		IMPTIMES = iMPTIMES;
	}
	public Timestamp getIMPLASTDATE() {
		return IMPLASTDATE;
	}
	public void setIMPLASTDATE(Timestamp iMPLASTDATE) {
		IMPLASTDATE = iMPLASTDATE;
	}
	public String getIMPER() {
		return IMPER;
	}
	public void setIMPER(String iMPER) {
		IMPER = iMPER;
	}
	public int getINER_IMPTIMES() {
		return INER_IMPTIMES;
	}
	public void setINER_IMPTIMES(int iNER_IMPTIMES) {
		INER_IMPTIMES = iNER_IMPTIMES;
	}
	public Timestamp getINER_IMPLASTDATE() {
		return INER_IMPLASTDATE;
	}
	public void setINER_IMPLASTDATE(Timestamp iNER_IMPLASTDATE) {
		INER_IMPLASTDATE = iNER_IMPLASTDATE;
	}
	public String getINER_IMPER() {
		return INER_IMPER;
	}
	public void setINER_IMPER(String iNER_IMPER) {
		INER_IMPER = iNER_IMPER;
	}
	public String getITEM_KEY() {
		return ITEM_KEY;
	}
	public void setITEM_KEY(String iTEM_KEY) {
		ITEM_KEY = iTEM_KEY;
	}
	public String getAUDIT_STATUS() {
		return AUDIT_STATUS;
	}
	public void setAUDIT_STATUS(String aUDIT_STATUS) {
		AUDIT_STATUS = aUDIT_STATUS;
	}
	public Timestamp getAUDIT_DATE() {
		return AUDIT_DATE;
	}
	public void setAUDIT_DATE(Timestamp aUDIT_DATE) {
		AUDIT_DATE = aUDIT_DATE;
	}
	public Timestamp getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Timestamp cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	public Timestamp getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}
	public void setLAST_UPDATE_DATE(Timestamp lAST_UPDATE_DATE) {
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
	}
	public String getLAST_UPDATED_BY() {
		return LAST_UPDATED_BY;
	}
	public void setLAST_UPDATED_BY(String lAST_UPDATED_BY) {
		LAST_UPDATED_BY = lAST_UPDATED_BY;
	}
	
}
