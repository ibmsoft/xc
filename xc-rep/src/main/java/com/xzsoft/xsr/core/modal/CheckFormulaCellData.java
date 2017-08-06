package com.xzsoft.xsr.core.modal;

/**
 * 报表稽核时，查询cellvalue封装的java bean
 * @author ZhouSuRong
 */
public class CheckFormulaCellData {
	
	private String SUIT_ID;
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
	private String CELLV; 
	private String CELLTEXTV; 
	private String DATATYPE;
	private Integer MAXNUM;
	
	public Integer getMAXNUM() {
		return MAXNUM;
	}
	public void setMAXNUM(Integer mAXNUM) {
		MAXNUM = mAXNUM;
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
	public String getCELLV() {
		return CELLV;
	}
	public void setCELLV(String cELLV) {
		CELLV = cELLV;
	}
	public String getCELLTEXTV() {
		return CELLTEXTV;
	}
	public void setCELLTEXTV(String cELLTEXTV) {
		CELLTEXTV = cELLTEXTV;
	}
	public String getDATATYPE() {
		return DATATYPE;
	}
	public void setDATATYPE(String dATATYPE) {
		DATATYPE = dATATYPE;
	}
	@Override
	public String toString() {
		return "CellData [ROWITEM_CODE="
				+ ROWITEM_CODE + ", COLITEM_CODE=" + COLITEM_CODE + ", CELLV="
				+ CELLV + ", CELLTEXTV=" + CELLTEXTV + ", DATATYPE=" + DATATYPE
				+ ", MAXNUM=" + MAXNUM + "]";
	}

	
}
