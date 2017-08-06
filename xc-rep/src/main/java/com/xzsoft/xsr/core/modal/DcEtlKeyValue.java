package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class DcEtlKeyValue {

	private String ETL_KEY_ID;
	private String DC_ID;
	private String ENTITY_ID;
	private String ETL_KEY_TYPE;
	private String ETL_KEY_MOD;
	private Timestamp DATE_VALUE;
	private Timestamp CREATION_DATE;
	private Timestamp LAST_UPDATE_DATE;
	
	public String getETL_KEY_ID() {
		return ETL_KEY_ID;
	}
	public void setETL_KEY_ID(String eTL_KEY_ID) {
		ETL_KEY_ID = eTL_KEY_ID;
	}
	public String getDC_ID() {
		return DC_ID;
	}
	public void setDC_ID(String dC_ID) {
		DC_ID = dC_ID;
	}
	public String getENTITY_ID() {
		return ENTITY_ID;
	}
	public void setENTITY_ID(String eNTITY_ID) {
		ENTITY_ID = eNTITY_ID;
	}
	public String getETL_KEY_TYPE() {
		return ETL_KEY_TYPE;
	}
	public void setETL_KEY_TYPE(String eTL_KEY_TYPE) {
		ETL_KEY_TYPE = eTL_KEY_TYPE;
	}
	public String getETL_KEY_MOD() {
		return ETL_KEY_MOD;
	}
	public void setETL_KEY_MOD(String eTL_KEY_MOD) {
		ETL_KEY_MOD = eTL_KEY_MOD;
	}
	public Timestamp getDATE_VALUE() {
		return DATE_VALUE;
	}
	public void setDATE_VALUE(Timestamp dATE_VALUE) {
		DATE_VALUE = dATE_VALUE;
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
	
}
