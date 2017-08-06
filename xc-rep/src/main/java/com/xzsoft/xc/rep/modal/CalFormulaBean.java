package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: CalFormulaBean 
 * @Description: 报表运算公式Bean
 * @author linp
 * @date 2016年8月4日 下午4:17:14 
 *
 */
public class CalFormulaBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String formulaId ;
	@JsonIgnore
	private String ledgerId ;
	@JsonIgnore
	private String accHrcyId ;
	@JsonIgnore
	private String rowItemId ;
	@JsonIgnore
	private String colItemId ;
	
	private String formula ;
	private String formulaDesc ;
	private String formulaType ;
	private int orderby ;
	private String formulaLevel ;
	
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String lastUpdatedBy ;
	@JsonIgnore
	private Date lastUpdateDate ;
	
	private String rowItemCode ;
	private String colItemCode ;
	private List<PreFormulaBean> preFormulaBeans ;	// 公式预处理信息

	public String getFormulaId() {
		return formulaId;
	}

	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}

	public String getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getAccHrcyId() {
		return accHrcyId;
	}

	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}

	public String getRowItemId() {
		return rowItemId;
	}

	public void setRowItemId(String rowItemId) {
		this.rowItemId = rowItemId;
	}

	public String getColItemId() {
		return colItemId;
	}

	public void setColItemId(String colItemId) {
		this.colItemId = colItemId;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFormulaDesc() {
		return formulaDesc;
	}

	public void setFormulaDesc(String formulaDesc) {
		this.formulaDesc = formulaDesc;
	}

	public String getFormulaType() {
		return formulaType;
	}

	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}

	public int getOrderby() {
		return orderby;
	}

	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}

	public String getFormulaLevel() {
		return formulaLevel;
	}

	public void setFormulaLevel(String formulaLevel) {
		this.formulaLevel = formulaLevel;
	}

	public String getRowItemCode() {
		return rowItemCode;
	}

	public void setRowItemCode(String rowItemCode) {
		this.rowItemCode = rowItemCode;
	}

	public String getColItemCode() {
		return colItemCode;
	}

	public void setColItemCode(String colItemCode) {
		this.colItemCode = colItemCode;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public List<PreFormulaBean> getPreFormulaBeans() {
		return preFormulaBeans;
	}

	public void setPreFormulaBeans(List<PreFormulaBean> preFormulaBeans) {
		this.preFormulaBeans = preFormulaBeans;
	}
	
}
