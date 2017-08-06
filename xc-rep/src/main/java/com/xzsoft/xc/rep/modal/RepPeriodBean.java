package com.xzsoft.xc.rep.modal;

import java.util.Date;

/**
 * @ClassName: RepPeriodBean 
 * @Description: 报表期间信息
 * @author linp
 * @date 2016年8月8日 下午2:01:52 
 *
 */
public class RepPeriodBean {

	private String periodCode ;
	private int year ;
	private int quarter ; 
	private Date startDate ;		// 开始日期
	private Date endDate ;			// 结束日期
	private int isEnabled ;
	private String isAdjPeriod ;
	
	private String createdBy ;
	private Date creationDate ;
	private String lastUpdatedBy ;
	private Date lastUpdateDate ;
	
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getQuarter() {
		return quarter;
	}
	public void setQuarter(int quarter) {
		this.quarter = quarter;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(int isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getIsAdjPeriod() {
		return isAdjPeriod;
	}
	public void setIsAdjPeriod(String isAdjPeriod) {
		this.isAdjPeriod = isAdjPeriod;
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
