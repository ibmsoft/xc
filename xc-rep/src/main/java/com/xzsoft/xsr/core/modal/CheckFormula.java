package com.xzsoft.xsr.core.modal;

import java.sql.Timestamp;


public class CheckFormula implements Comparable<CheckFormula> {

	private String CHK_FORMULA_ID; //公式id
	private String SUIT_ID; //表套id
	private String entityId; //公司id
	private String entityName; //公司name
	private String modalsheet_id; //模板id
	private String MODALSHEET_NAME; //模板名称
	private String ROWITEM_ID; //行指标
	private String ROWITEM_CODE;
	private String rowitem_desc; //行指标翻译
	private String COLITEM_ID;
	private String COLITEM_CODE;
	private String colitem_desc; //列指标翻译
	private String F_LEFT; //公式左边
	private String F_RIGHT; //公式右边
	private String F_EXP; //运算符
	private String F_DESC; //中文描述
	private String ERR_SCOPE;
	private String DESCRIPTION; //公式说明
	private String F_SETTYPE; //公式类型
	private String LOGIC_FLAG;
	private String LOGIC_NAME; //逻辑公式
	private String ZL_FLAG;
	private String CHK_TYPE;
	private String CHK_LEVEL;
	private String ENABL_FLAG;
	private String PERIOD_FLAG;
	private String leftValue; //公式左边值
	private String rightValue; //公式右边值
	private String leftParsedStr; //公式左边表达式
	private String rightParsedStr; //公式右边表达式
	private String banlance; //差额
	private Integer ROWNUMDTL; //浮动行明细行号； 固定行默认为-1
	private Timestamp CREATION_DATE; //CREATION_DATE
	private String CREATED_BY; //CREATED_BY
	private Timestamp LAST_UPDATE_DATE; //CREATION_DATE
	private String LAST_UPDATED_BY; //CREATED_BY
	
	public Integer getROWNUMDTL() {
		return ROWNUMDTL;
	}
	public void setROWNUMDTL(Integer rOWNUMDTL) {
		ROWNUMDTL = rOWNUMDTL;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getBanlance() {
		return banlance;
	}
	public void setBanlance(String banlance) {
		this.banlance = banlance;
	}
	public String getLeftValue() {
		return leftValue;
	}
	public void setLeftValue(String leftValue) {
		this.leftValue = leftValue;
	}
	public String getRightValue() {
		return rightValue;
	}
	public void setRightValue(String rightValue) {
		this.rightValue = rightValue;
	}
	public String getLeftParsedStr() {
		return leftParsedStr;
	}
	public void setLeftParsedStr(String leftParsedStr) {
		this.leftParsedStr = leftParsedStr;
	}
	public String getRightParsedStr() {
		return rightParsedStr;
	}
	public void setRightParsedStr(String rightParsedStr) {
		this.rightParsedStr = rightParsedStr;
	}
	public String getModalsheet_id() {
		return modalsheet_id;
	}
	public void setModalsheet_id(String modalsheet_id) {
		this.modalsheet_id = modalsheet_id;
	}
	public String getMODALSHEET_NAME() {
		return MODALSHEET_NAME;
	}
	public void setMODALSHEET_NAME(String mODALSHEET_NAME) {
		MODALSHEET_NAME = mODALSHEET_NAME;
	}
	public String getRowitem_desc() {
		return rowitem_desc;
	}
	public void setRowitem_desc(String rowitem_desc) {
		this.rowitem_desc = rowitem_desc;
	}
	public String getColitem_desc() {
		return colitem_desc;
	}
	public void setColitem_desc(String colitem_desc) {
		this.colitem_desc = colitem_desc;
	}
	public String getCHK_FORMULA_ID() {
		return CHK_FORMULA_ID;
	}
	public void setCHK_FORMULA_ID(String cHK_FORMULA_ID) {
		CHK_FORMULA_ID = cHK_FORMULA_ID;
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
	public String getROWITEM_CODE() {
		return ROWITEM_CODE;
	}
	public void setROWITEM_CODE(String rOWITEM_CODE) {
		ROWITEM_CODE = rOWITEM_CODE;
	}
	public String getCOLITEM_ID() {
		return COLITEM_ID;
	}
	public void setCOLITEM_ID(String cOLITEM_ID) {
		COLITEM_ID = cOLITEM_ID;
	}
	public String getCOLITEM_CODE() {
		return COLITEM_CODE;
	}
	public void setCOLITEM_CODE(String cOLITEM_CODE) {
		COLITEM_CODE = cOLITEM_CODE;
	}
	public String getF_LEFT() {
		return F_LEFT;
	}
	public void setF_LEFT(String f_LEFT) {
		F_LEFT = f_LEFT;
	}
	public String getF_RIGHT() {
		return F_RIGHT;
	}
	public void setF_RIGHT(String f_RIGHT) {
		F_RIGHT = f_RIGHT;
	}
	public String getF_EXP() {
		return F_EXP;
	}
	public void setF_EXP(String f_EXP) {
		F_EXP = f_EXP;
	}
	public String getF_DESC() {
		return F_DESC;
	}
	public void setF_DESC(String f_DESC) {
		F_DESC = f_DESC;
	}
	public String getERR_SCOPE() {
		return ERR_SCOPE;
	}
	public void setERR_SCOPE(String eRR_SCOPE) {
		ERR_SCOPE = eRR_SCOPE;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getF_SETTYPE() {
		return F_SETTYPE;
	}
	public void setF_SETTYPE(String f_SETTYPE) {
		F_SETTYPE = f_SETTYPE;
	}
	public String getLOGIC_FLAG() {
		return LOGIC_FLAG;
	}
	public void setLOGIC_FLAG(String lOGIC_FLAG) {
		LOGIC_FLAG = lOGIC_FLAG;
	}
	public String getLOGIC_NAME() {
		return LOGIC_NAME;
	}
	public void setLOGIC_NAME(String lOGIC_NAME) {
		LOGIC_NAME = lOGIC_NAME;
	}
	public String getZL_FLAG() {
		return ZL_FLAG;
	}
	public void setZL_FLAG(String zL_FLAG) {
		ZL_FLAG = zL_FLAG;
	}
	public String getCHK_TYPE() {
		return CHK_TYPE;
	}
	public void setCHK_TYPE(String cHK_TYPE) {
		CHK_TYPE = cHK_TYPE;
	}
	public String getCHK_LEVEL() {
		return CHK_LEVEL;
	}
	public void setCHK_LEVEL(String cHK_LEVEL) {
		CHK_LEVEL = cHK_LEVEL;
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
	public String getENABL_FLAG() {
		return ENABL_FLAG;
	}
	public void setENABL_FLAG(String eNABL_FLAG) {
		ENABL_FLAG = eNABL_FLAG;
	}
	public String getPERIOD_FLAG() {
		return PERIOD_FLAG;
	}
	public void setPERIOD_FLAG(String pERIOD_FLAG) {
		PERIOD_FLAG = pERIOD_FLAG;
	}
	
	@Override
	public int compareTo(CheckFormula cf) {
		if(null != cf.getModalsheet_id()) {
			return modalsheet_id.compareTo(cf.getModalsheet_id());
		} else {
			return 0;
		}
	}
	@Override
	public String toString() {
		return "CheckFormula [CHK_FORMULA_ID=" + CHK_FORMULA_ID
				+ ", ROWITEM_ID=" + ROWITEM_ID + ", ROWITEM_CODE="
				+ ROWITEM_CODE + ", COLITEM_ID=" + COLITEM_ID
				+ ", COLITEM_CODE=" + COLITEM_CODE + ", leftValue=" + leftValue
				+ ", rightValue=" + rightValue + ", ROWNUMDTL=" + ROWNUMDTL
				+ "]";
	}

	
}
