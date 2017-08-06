package com.xzsoft.xc.rep.modal;

import java.util.Date;
/**
 * @describe 单元格公式类
 * @author tangxl
 * @date 2016-08-04
 */
public class CellFormula {
	private String formulaId;
	private String accHrcyId;
	private String ledgerId;
	private String rowitemId;
	private String colitemId;
	private String formula;
	private String formulaDesc;
	private String formulaType;        //取数运算类型字段:REP-表间取数;MIXED-混合公式
	private String formulaLevel;
	private int orderBy;              //公式排序字段
	/**
	 * @return the formulaId
	 */
	public String getFormulaId() {
		return formulaId;
	}
	/**
	 * @param formulaId the formulaId to set
	 */
	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}
	/**
	 * @return the accHrcyId
	 */
	public String getAccHrcyId() {
		return accHrcyId;
	}
	/**
	 * @param accHrcyId the accHrcyId to set
	 */
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	/**
	 * @return the ledgerId
	 */
	public String getLedgerId() {
		return ledgerId;
	}
	/**
	 * @param ledgerId the ledgerId to set
	 */
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	/**
	 * @return the rowitemId
	 */
	public String getRowitemId() {
		return rowitemId;
	}
	/**
	 * @param rowitemId the rowitemId to set
	 */
	public void setRowitemId(String rowitemId) {
		this.rowitemId = rowitemId;
	}
	/**
	 * @return the colitemId
	 */
	public String getColitemId() {
		return colitemId;
	}
	/**
	 * @param colitemId the colitemId to set
	 */
	public void setColitemId(String colitemId) {
		this.colitemId = colitemId;
	}
	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}
	/**
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}
	/**
	 * @return the formulaDesc
	 */
	public String getFormulaDesc() {
		return formulaDesc;
	}
	/**
	 * @param formulaDesc the formulaDesc to set
	 */
	public void setFormulaDesc(String formulaDesc) {
		this.formulaDesc = formulaDesc;
	}
	/**
	 * @return the formulaType
	 */
	public String getFormulaType() {
		return formulaType;
	}
	/**
	 * @param formulaType the formulaType to set
	 */
	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}
	/**
	 * @return the formulaLevel
	 */
	public String getFormulaLevel() {
		return formulaLevel;
	}
	/**
	 * @param formulaLevel the formulaLevel to set
	 */
	public void setFormulaLevel(String formulaLevel) {
		this.formulaLevel = formulaLevel;
	}
	/**
	 * @return the orderBy
	 */
	public int getOrderBy() {
		return orderBy;
	}
	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	private Date creationDate; 
	private String createdBy; 
	private Date lastUpdateDate; 
	private String lastUpdatedBy;
}
