package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class DcAccount {

	private String ACC_ID;
	private String DC_ID;
	private String code; //ACC_CODE
	private String name; //ACC_NAME
	private String DESCRIPTION;
	private String PARENT_ID;
	private String upCode; //PARENT_CODE
	private String ACC_CLASS;
	private String ACC_TYPE;
	private String ISCAL;
	private String ISSUM;
	private String ENABLED;
	private String ledgerCode; //ATTR1
	private Timestamp CREATION_DATE; 
	private Timestamp LAST_UPDATE_DATE;
	
	public Timestamp getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Timestamp cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public Timestamp getLAST_UPDATE_DATE() {
		return LAST_UPDATE_DATE;
	}
	public void setLAST_UPDATE_DATE(Timestamp lAST_UPDATE_DATE) {
		LAST_UPDATE_DATE = lAST_UPDATE_DATE;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUpCode() {
		return upCode;
	}
	public void setUpCode(String upCode) {
		this.upCode = upCode;
	}
	public String getLedgerCode() {
		return ledgerCode;
	}
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
	public String getACC_ID() {
		return ACC_ID;
	}
	public void setACC_ID(String aCC_ID) {
		ACC_ID = aCC_ID;
	}
	public String getDC_ID() {
		return DC_ID;
	}
	public void setDC_ID(String dC_ID) {
		DC_ID = dC_ID;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getPARENT_ID() {
		return PARENT_ID;
	}
	public void setPARENT_ID(String pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}

	public String getACC_CLASS() {
		return ACC_CLASS;
	}
	public void setACC_CLASS(String aCC_CLASS) {
		ACC_CLASS = aCC_CLASS;
	}
	public String getACC_TYPE() {
		return ACC_TYPE;
	}
	public void setACC_TYPE(String aCC_TYPE) {
		ACC_TYPE = aCC_TYPE;
	}
	public String getISCAL() {
		return ISCAL;
	}
	public void setISCAL(String iSCAL) {
		ISCAL = iSCAL;
	}
	public String getISSUM() {
		return ISSUM;
	}
	public void setISSUM(String iSSUM) {
		ISSUM = iSSUM;
	}
	public String getENABLED() {
		return ENABLED;
	}
	public void setENABLED(String eNABLED) {
		ENABLED = eNABLED;
	}
	@Override
	public String toString() {
		return "DcAccount [code=" + code + ", name=" + name + ", upCode="
				+ upCode + ", ledgerCode=" + ledgerCode + "]";
	}
	

	
}
