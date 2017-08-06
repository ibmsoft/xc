package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;
/**
 * @project 报表3.0
 * @version v1.0
 * @author songyh
 * @Date 2016-01-27
 * @desc 报表表：xsr_rep_cellvalue
 */
public class CellValue {
	private String CELLV_ID;      //指标值id
	private String SUIT_ID;
	private String LEDGER_ID ; 
	private String ENTITY_ID;
	private String PERIOD_ID;
	private String ROWITEM_ID;
	private String COLITEM_ID;
	private String CURRENCY_ID; 
	private String ENTITY_CODE; 
	private String PERIOD_CODE; 
	private String ROWITEM_CODE; 
	private String COLITEM_CODE;
	
	private String CURRENCY_CODE; 
	private Double CELLV;            //指标值
	private String CELLTEXTV;        //指标文本值
	private String FORMULAV;         //复合公式运算结果串
	private Timestamp CREATION_DATE; //CREATION_DATE
	private String CREATED_BY; //CREATED_BY
	private Timestamp LAST_UPDATE_DATE; //CREATION_DATE
	private String LAST_UPDATED_BY; //CREATED_BY
	
	//=====================add by sld=============================
	private Integer ROWNO;
	private Integer COLNO;
	private Integer DATATYPE;
	
	public CellValue() {
		super();
	}
	
	public String getLEDGER_ID() {
		return LEDGER_ID;
	}

	public void setLEDGER_ID(String lEDGER_ID) {
		LEDGER_ID = lEDGER_ID;
	}

	public String getCELLV_ID() {
		return CELLV_ID;
	}
	public void setCELLV_ID(String cELLV_ID) {
		CELLV_ID = cELLV_ID;
	}
	public String getSUIT_ID() {
		return SUIT_ID;
	}
	public void setSUIT_ID(String sUIT_ID) {
		SUIT_ID = sUIT_ID;
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
	public String getCURRENCY_ID() {
		return CURRENCY_ID;
	}
	public void setCURRENCY_ID(String cURRENCY_ID) {
		CURRENCY_ID = cURRENCY_ID;
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
	public String getCURRENCY_CODE() {
		return CURRENCY_CODE;
	}
	public void setCURRENCY_CODE(String cURRENCY_CODE) {
		CURRENCY_CODE = cURRENCY_CODE;
	}
	public Double getCELLV() {
		return CELLV;
	}
	public void setCELLV(Double cELLV) {
		CELLV = cELLV;
	}
	public String getCELLTEXTV() {
		return CELLTEXTV;
	}
	public void setCELLTEXTV(String cELLTEXTV) {
		CELLTEXTV = cELLTEXTV;
	}
	public String getFORMULAV() {
		return FORMULAV;
	}
	public void setFORMULAV(String fORMULAV) {
		FORMULAV = fORMULAV;
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

	public Integer getROWNO() {
		return ROWNO;
	}

	public void setROWNO(Integer rOWNO) {
		ROWNO = rOWNO;
	}

	public Integer getCOLNO() {
		return COLNO;
	}

	public void setCOLNO(Integer cOLNO) {
		COLNO = cOLNO;
	}

	public Integer getDATATYPE() {
		return DATATYPE;
	}

	public void setDATATYPE(Integer dATATYPE) {
		DATATYPE = dATATYPE;
	}
	
	
}
