package com.xzsoft.xc.rep.modal;

import java.util.Date;

/**
 * @ClassName: RepSheetBean 
 * @Description: 报表基础信息
 * @author linp
 * @date 2016年8月5日 上午10:02:38 
 *
 */
public class RepSheetBean {

	private String sheetId ;
	private String tabId ;
	private String ledgerId ;
	private String orgId ;
	private String periodCode ;
	private String cnyCode ;
	private String sheetDesc ;
	
	private String appStatus ;		// 报表状态：A-已生成；C-审批中；D-驳回；E-已锁定
	private String instanceCode ;	// 流程实例
	private String auditStatus ;	// 审批状态
	private String auditStatusDesc ;
	private Date auditDate ;		// 审批日期
	
	private String accHrcyId ;
	private int tabOrder ;
	private String tabName ;
	
	private String oldAppStatus ;
	
	private String createdBy ;
	private Date creationDate ;
	private String lastUpdatedBy ;
	private Date lastUpdateDate ;
	
	public String getSheetId() {
		return sheetId;
	}
	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
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
	public String getSheetDesc() {
		return sheetDesc;
	}
	public void setSheetDesc(String sheetDesc) {
		this.sheetDesc = sheetDesc;
	}
	public String getAppStatus() {
		return appStatus;
	}
	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}
	public String getInstanceCode() {
		return instanceCode;
	}
	public void setInstanceCode(String instanceCode) {
		this.instanceCode = instanceCode;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}
	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public String getAccHrcyId() {
		return accHrcyId;
	}
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	public int getTabOrder() {
		return tabOrder;
	}
	public void setTabOrder(int tabOrder) {
		this.tabOrder = tabOrder;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public String getOldAppStatus() {
		return oldAppStatus;
	}
	public void setOldAppStatus(String oldAppStatus) {
		this.oldAppStatus = oldAppStatus;
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
