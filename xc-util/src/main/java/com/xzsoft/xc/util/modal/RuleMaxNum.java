package com.xzsoft.xc.util.modal;

import java.util.Date;

/**
 * @ClassName: RuleMaxNum 
 * @Description: 最大流水号
 * @author linp
 * @date 2016年1月1日 下午10:48:10 
 *
 */
public class RuleMaxNum {

	private String maxNumId ;
	private String ledgerId ;
	private String ruleCode ;
	private String fixedPrefix ;
	private String dtlPrefixUnion ;
	private String year ;
	private String month ;
	private int maxNum ;

	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	public String getMaxNumId() {
		return maxNumId;
	}
	public void setMaxNumId(String maxNumId) {
		this.maxNumId = maxNumId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getFixedPrefix() {
		return fixedPrefix;
	}
	public void setFixedPrefix(String fixedPrefix) {
		this.fixedPrefix = fixedPrefix;
	}
	public String getDtlPrefixUnion() {
		return dtlPrefixUnion;
	}
	public void setDtlPrefixUnion(String dtlPrefixUnion) {
		this.dtlPrefixUnion = dtlPrefixUnion;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
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
	
}
