package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class DcCaItem {

	private String ACC_ID;
	private String DC_ID;
	private String caCode; //ACC_CODE
	private String caName; //ACC_NAME
	private String caDesc; //DESCRIPTION
	private String PARENT_ID;
	private String upCode; //PARENT_CODE
	private String ACC_CLASS; 
	private String ACC_TYPE;
	private String ISCAL;
	private String ISSUM;
	private String ENABLED;
	private String ledgerCode; //ATTR1, 现金流量项目该字段记录-1
	private String caDirection; //ATTR2
	private String isLeaf; //ATTR3
	private String orderby; //ATTR4
	private String caLevel; //ATTR5
	private Timestamp CREATION_DATE; 
	private Timestamp LAST_UPDATE_DATE;
	
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
	public String getCaCode() {
		return caCode;
	}
	public void setCaCode(String caCode) {
		this.caCode = caCode;
	}
	public String getCaName() {
		return caName;
	}
	public void setCaName(String caName) {
		this.caName = caName;
	}
	public String getCaDesc() {
		return caDesc;
	}
	public void setCaDesc(String caDesc) {
		this.caDesc = caDesc;
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
	public String getLedgerCode() {
		return ledgerCode;
	}
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
	public String getCaDirection() {
		return caDirection;
	}
	public void setCaDirection(String caDirection) {
		this.caDirection = caDirection;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}
	public String getCaLevel() {
		return caLevel;
	}
	public void setCaLevel(String caLevel) {
		this.caLevel = caLevel;
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
	

	/**
	 * {
		"assKey":"",
		"caCode":"X", // 用
		"caDesc":"", // 用
		"caDirection":"1", // 用
		"caId":"801ea541-a46d-4578-bb2d-a86de1005d4d",
		"caLevel":"1", // 用
		"caName":"", // 用
		"createdBy":"",
		"creationDate":"2016-03-22 10:17:29",
		"endDate":"2022-09-30 00:00:00",
		"isLeaf":"Y", // 用
		"lastUpdateDate":"2016-03-22 13:50:55",
		"lastUpdatedBy":"",
		"orderby":14, // 用
		"startDate":"2015-04-30 00:00:00",
		"upCode":"", // 用
		"upId":""
		}
	 */
	
}
