package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * @ClassName: AccHrcy 
 * @Description: 科目体系信息
 * @author linp
 * @date 2015年12月30日 上午11:48:10 
 *
 */
public class AccHrcy {
	private String accHrcyId ;
	private String accHrcyCode ;
	private String accHrcyName ;
	private String accHrcyDesc ;
	private String isPrepared ;
	private int orderby ;
	
	private Date startDate ;
	private Date endDate ;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	public String getAccHrcyId() {
		return accHrcyId;
	}
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	public String getAccHrcyCode() {
		return accHrcyCode;
	}
	public void setAccHrcyCode(String accHrcyCode) {
		this.accHrcyCode = accHrcyCode;
	}
	public String getAccHrcyName() {
		return accHrcyName;
	}
	public void setAccHrcyName(String accHrcyName) {
		this.accHrcyName = accHrcyName;
	}
	public String getAccHrcyDesc() {
		return accHrcyDesc;
	}
	public void setAccHrcyDesc(String accHrcyDesc) {
		this.accHrcyDesc = accHrcyDesc;
	}
	public String getIsPrepared() {
		return isPrepared;
	}
	public void setIsPrepared(String isPrepared) {
		this.isPrepared = isPrepared;
	}
	public int getOrderby() {
		return orderby;
	}
	public void setOrderby(int orderby) {
		this.orderby = orderby;
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
