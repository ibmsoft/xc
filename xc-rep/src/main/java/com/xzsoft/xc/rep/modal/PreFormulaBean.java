package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: PreFormulaBean 
 * @Description: 预处理后的公式信息
 * @author linp
 * @date 2016年8月4日 下午4:23:02 
 *
 */
public class PreFormulaBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String preFormulaId ;
	@JsonIgnore
	private String formulaId ;
	
	private String formulaCat ;
	private String chkFormulaType ;
	private String formulaStr ;
	private String formulaJson ;
	private String formulaType ;
	private int orderby ;
	
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String lastUpdatedBy ;
	@JsonIgnore
	private Date lastUpdateDate ;
	
	public String getPreFormulaId() {
		return preFormulaId;
	}
	public void setPreFormulaId(String preFormulaId) {
		this.preFormulaId = preFormulaId;
	}
	public String getFormulaCat() {
		return formulaCat;
	}
	public void setFormulaCat(String formulaCat) {
		this.formulaCat = formulaCat;
	}
	public String getChkFormulaType() {
		return chkFormulaType;
	}
	public void setChkFormulaType(String chkFormulaType) {
		this.chkFormulaType = chkFormulaType;
	}
	public String getFormulaId() {
		return formulaId;
	}
	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}
	public String getFormulaStr() {
		return formulaStr;
	}
	public void setFormulaStr(String formulaStr) {
		this.formulaStr = formulaStr;
	}
	public String getFormulaJson() {
		return formulaJson;
	}
	public void setFormulaJson(String formulaJson) {
		this.formulaJson = formulaJson;
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
	
}
