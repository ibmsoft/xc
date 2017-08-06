package com.xzsoft.xc.util.modal;

import java.util.Date;

/**
 * @ClassName: RuleDtl 
 * @Description: 规则明细
 * @author linp
 * @date 2016年1月1日 下午10:33:32 
 */
public class RuleDtl {
	
	private String ruleCode ;
	private String dtlId ;
	private String dtlCode ;
	private String dtlSeparator ;
	private int orderby ;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	public String getRuleCode() {
		return ruleCode;
	}
	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}
	public String getDtlId() {
		return dtlId;
	}
	public void setDtlId(String dtlId) {
		this.dtlId = dtlId;
	}
	public String getDtlCode() {
		return dtlCode;
	}
	public void setDtlCode(String dtlCode) {
		this.dtlCode = dtlCode;
	}
	public String getDtlSeparator() {
		return dtlSeparator;
	}
	public void setDtlSeparator(String dtlSeparator) {
		this.dtlSeparator = dtlSeparator;
	}
	public int getOrderby() {
		return orderby;
	}
	public void setOrderby(int orderby) {
		this.orderby = orderby;
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
