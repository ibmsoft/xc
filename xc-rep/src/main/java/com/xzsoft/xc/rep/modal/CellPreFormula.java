package com.xzsoft.xc.rep.modal;

import java.util.Date;

/**
 * @describe 单元格预处理公式类
 * @author tangxl
 * @date 2016-08-05
 */
public class CellPreFormula {
	//主键
	private String preFormulaId;
	//公式分类：CAL-计算公式；CHK-稽核公式
	private String formulaCat;
	//稽核公式左右表达式：L-左表达式；R-右表达式
	private String expressionCat;
	//公式ID
	private String formulaId;
	//公式内容
	private String formulaStr;
	//处理后的公式串
	private String formulaJson;
	//公式类型
	private String formulaType;
	//排序号
	private int orderBy;
	private Date creationDate; 
	private String createdBy; 
	private Date lastUpdateDate; 
	private String lastUpdatedBy;
	/**
	 * @return the preFormulaId
	 */
	public String getPreFormulaId() {
		return preFormulaId;
	}
	/**
	 * @param preFormulaId the preFormulaId to set
	 */
	public void setPreFormulaId(String preFormulaId) {
		this.preFormulaId = preFormulaId;
	}
	/**
	 * @return the formulaCat
	 */
	public String getFormulaCat() {
		return formulaCat;
	}
	/**
	 * @param formulaCat the formulaCat to set
	 */
	public void setFormulaCat(String formulaCat) {
		this.formulaCat = formulaCat;
	}
	/**
	 * @return the expressionCat
	 */
	public String getExpressionCat() {
		return expressionCat;
	}
	/**
	 * @param expressionCat the expressionCat to set
	 */
	public void setExpressionCat(String expressionCat) {
		this.expressionCat = expressionCat;
	}
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
	 * @return the formulaStr
	 */
	public String getFormulaStr() {
		return formulaStr;
	}
	/**
	 * @param formulaStr the formulaStr to set
	 */
	public void setFormulaStr(String formulaStr) {
		this.formulaStr = formulaStr;
	}
	/**
	 * @return the formulaJson
	 */
	public String getFormulaJson() {
		return formulaJson;
	}
	/**
	 * @param formulaJson the formulaJson to set
	 */
	public void setFormulaJson(String formulaJson) {
		this.formulaJson = formulaJson;
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
	
}
