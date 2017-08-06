package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;

public class CellFormula {
	private String FORMULA_ID;
	private String SUIT_ID;
	private String ROWITEM_ID;
	private String ROWITEM_CODE;
	private String COLITEM_ID;
	private String COLITEM_CODE;
	private String ENTITY_ID;//公司ID，-1表示公共级
	private String DATAFORMULA;//采集公式
	private String FORMULA_DESC;//公式描述信息
	private String F_CALTYPE;//取数运算类型字段:REP-表间取数;XLS-表内公式
	private int ORDERBY_FLAG;//公式排序字段
	private Timestamp CREATION_DATE; //CREATION_DATE
	private String CREATED_BY; //CREATED_BY
	private Timestamp LAST_UPDATE_DATE; //CREATION_DATE
	private String LAST_UPDATED_BY; //CREATED_BY
	public String getFORMULA_ID() {
		return FORMULA_ID;
	}
	public void setFORMULA_ID(String fORMULA_ID) {
		FORMULA_ID = fORMULA_ID;
	}
	public String getSUIT_ID() {
		return SUIT_ID;
	}
	public void setSUIT_ID(String sUIT_ID) {
		SUIT_ID = sUIT_ID;
	}
	public String getROWITEM_ID() {
		return ROWITEM_ID;
	}
	public void setROWITEM_ID(String rOWITEM_ID) {
		ROWITEM_ID = rOWITEM_ID;
	}
	public String getCOLITEM_ID() {
		return COLITEM_ID;
	}
	public void setCOLITEM_ID(String cOLITEM_ID) {
		COLITEM_ID = cOLITEM_ID;
	}
	public String getENTITY_ID() {
		return ENTITY_ID;
	}
	public void setENTITY_ID(String eNTITY_ID) {
		ENTITY_ID = eNTITY_ID;
	}
	public String getDATAFORMULA() {
		return DATAFORMULA;
	}
	public void setDATAFORMULA(String dATAFORMULA) {
		DATAFORMULA = dATAFORMULA;
	}
	public String getFORMULA_DESC() {
		return FORMULA_DESC;
	}
	public void setFORMULA_DESC(String fORMULA_DESC) {
		FORMULA_DESC = fORMULA_DESC;
	}
	public String getF_CALTYPE() {
		return F_CALTYPE;
	}
	public void setF_CALTYPE(String f_CALTYPE) {
		F_CALTYPE = f_CALTYPE;
	}
	public int getORDERBY_FLAG() {
		return ORDERBY_FLAG;
	}
	public void setORDERBY_FLAG(int oRDERBY_FLAG) {
		ORDERBY_FLAG = oRDERBY_FLAG;
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
	public String getROWITEM_CODE() {
		return ROWITEM_CODE;
	}
	public void setROWITEM_CODE(String rOWITEM_CODE) {
		ROWITEM_CODE = rOWITEM_CODE;
	}
	public String getCOLITEM_CODE() {
		return COLITEM_CODE;
	}
	public void setCOLITEM_CODE(String cOLITEM_CODE) {
		COLITEM_CODE = cOLITEM_CODE;
	}

}
