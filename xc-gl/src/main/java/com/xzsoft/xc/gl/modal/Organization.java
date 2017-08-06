package com.xzsoft.xc.gl.modal;

import java.util.Date;

public class Organization {
	private String orgId ;
	private String orgCode ;
	private String orgName ;
	private String orgShortName ;
	private String upOrgId ;
	
	private String upOrgCode;
	private Date synchronizeDate;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgShortName() {
		return orgShortName;
	}
	public String getUpOrgId() {
		return upOrgId;
	}
	public void setUpOrgId(String upOrgId) {
		this.upOrgId = upOrgId;
	}
	public void setOrgShortName(String orgShortName) {
		this.orgShortName = orgShortName;
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
	public String getUpOrgCode() {
		return upOrgCode;
	}
	public void setUpOrgCode(String upOrgCode) {
		this.upOrgCode = upOrgCode;
	}
	public Date getSynchronizeDate() {
		return synchronizeDate;
	}
	public void setSynchronizeDate(Date synchronizeDate) {
		this.synchronizeDate = synchronizeDate;
	}
	
}
