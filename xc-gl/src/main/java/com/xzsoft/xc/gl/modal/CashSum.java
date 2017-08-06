package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * 
 * @desc 现金流量统计表
 * @author tangxl
 * @date 2016年3月18日
 *
 */
public class CashSum {
	private String sumId;
	private String ledgerId;
	private String ledgerCode;
	private String periodCode;
	private String accId;
	private String accCode;
	private String currencyCode ;
	private String caId;
	private String caCode;
	private double caSum;
	private double tCaSum;
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	private Date synchronizeDate;
	
	public String getSumId() {
		return sumId;
	}
	public void setSumId(String sumId) {
		this.sumId = sumId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getLedgerCode() {
		return ledgerCode;
	}
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getAccCode() {
		return accCode;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCaId() {
		return caId;
	}
	public void setCaId(String caId) {
		this.caId = caId;
	}
	
	public String getCaCode() {
		return caCode;
	}
	public void setCaCode(String caCode) {
		this.caCode = caCode;
	}
	public double getCaSum() {
		return caSum;
	}
	public void setCaSum(double caSum) {
		this.caSum = caSum;
	}
	public double gettCaSum() {
		return tCaSum;
	}
	public void settCaSum(double tCaSum) {
		this.tCaSum = tCaSum;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public Date getSynchronizeDate() {
		return synchronizeDate;
	}
	public void setSynchronizeDate(Date synchronizeDate) {
		this.synchronizeDate = synchronizeDate;
	}
}
