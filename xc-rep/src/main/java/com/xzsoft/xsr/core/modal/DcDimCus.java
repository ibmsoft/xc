package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class DcDimCus {

	private String DIM_CUS_ID;
	private String DC_ID;
	private String code; //DIM_CUS_CODE
	private String name; //DIM_CUS_NAME
	private String DESCRIPTION;
	private String PARENT_ID;
	private String upCode; //PARENT_CODE
	private String DIMCLASS;
	private String DIMTYPE;
	private String ISCAL;
	private String ISSUM;
	private String ENABLED;
	private String ledgerCode; //ATTR1
	private Timestamp CREATION_DATE; 
	private Timestamp LAST_UPDATE_DATE;
	private String tableName;
	
	public String getDIM_CUS_ID() {
		return DIM_CUS_ID;
	}
	public void setDIM_CUS_ID(String dIM_CUS_ID) {
		DIM_CUS_ID = dIM_CUS_ID;
	}
	public String getDC_ID() {
		return DC_ID;
	}
	public void setDC_ID(String dC_ID) {
		DC_ID = dC_ID;
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
	public String getUpCode() {
		return upCode;
	}
	public void setUpCode(String upCode) {
		this.upCode = upCode;
	}
	public String getDIMCLASS() {
		return DIMCLASS;
	}
	public void setDIMCLASS(String dIMCLASS) {
		DIMCLASS = dIMCLASS;
	}
	public String getDIMTYPE() {
		return DIMTYPE;
	}
	public void setDIMTYPE(String dIMTYPE) {
		DIMTYPE = dIMTYPE;
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
	public String getLedgerCode() {
		return ledgerCode;
	}
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
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
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Override
	public String toString() {
		return "DcDimCus [code=" + code + ", name=" + name + ", upCode="
				+ upCode + ", ledgerCode=" + ledgerCode + "]";
	}
	
}
