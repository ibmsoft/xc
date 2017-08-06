package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * 
 * @author admin
 *
 */
public class Period {
	private String periodCode;
	private int year;
	private int quarter;
	private Date startDate;
	private Date endDate;
	private int isEnabled;
	private String isAdjPeriod;
	
	private String ledgerId ;
	private String periodStatus ;
	
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
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getPeriodStatus() {
		return periodStatus;
	}
	public void setPeriodStatus(String periodStatus) {
		this.periodStatus = periodStatus;
	}
}
