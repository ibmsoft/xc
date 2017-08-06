package com.xzsoft.xc.rep.modal;

import java.util.Date;

/**
 * @ClassName: CellValueBean 
 * @Description: 报表固定行指标信息
 * @author linp
 * @date 2016年8月5日 上午10:03:38 
 *
 */
public class CellValueBean {
	
	private String valId ;
	private String ledgerId ;
	private String orgId ;
	private String rowItemId ;
	private String colItemId ;
	private String periodCode ;
	private String cnyCode ;
	private String rowItemCode ;
	private String colItemCode ;
	private double cellV ;
	private String cellTextV ;
	
	private String createdBy ;
	private Date creationDate ;
	private String lastUpdatedBy ;
	private Date lastUpdateDate ;
	
	
	public String getValId() {
		return valId;
	}
	public void setValId(String valId) {
		this.valId = valId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getCnyCode() {
		return cnyCode;
	}
	public void setCnyCode(String cnyCode) {
		this.cnyCode = cnyCode;
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
	public double getCellV() {
		return cellV;
	}
	public void setCellV(double cellV) {
		this.cellV = cellV;
	}
	public String getCellTextV() {
		return cellTextV;
	}
	public void setCellTextV(String cellTextV) {
		this.cellTextV = cellTextV;
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
