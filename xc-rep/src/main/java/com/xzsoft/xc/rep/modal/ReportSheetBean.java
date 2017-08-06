/**
 * 
 */
package com.xzsoft.xc.rep.modal;

import java.util.Date;

/**
 * @author tangxl
 * @table  xc_rep_sheet
 */
public class ReportSheetBean {
	//id和name为Enterprise Sheet 的内置字段
	private int id;              
	private String name;
	private String sheetId;
	private String tabId;
	private String tabName;
	private String ledgerId;
	private String ledgerName;
	private String orgId;
	private String orgName;
	private String periodCode;
	private String cnyCode;
	private String sheetDesc;
	private String appStatus;
	private String auditStatus;
	private String auditStatusDesc;
	private String instanceCode;
	private String createdBy ;
	private Date creationDate ;
	private String lastUpdatedBy ;
	private Date lastUpdateDate ;
	//报表导出时所在Excel文件中的页签名称，这个字段为报表导出服务，查询时无需设置该字段的值
	private String exportTabName;                   
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sheetId
	 */
	public String getSheetId() {
		return sheetId;
	}
	/**
	 * @param sheetId the sheetId to set
	 */
	public void setSheetId(String sheetId) {
		this.sheetId = sheetId;
	}
	/**
	 * @return the tabId
	 */
	public String getTabId() {
		return tabId;
	}
	/**
	 * @param tabId the tabId to set
	 */
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	/**
	 * @return the ledgerId
	 */
	public String getLedgerId() {
		return ledgerId;
	}
	/**
	 * @param ledgerId the ledgerId to set
	 */
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	/**
	 * @return the ledgerName
	 */
	public String getLedgerName() {
		return ledgerName;
	}
	/**
	 * @param ledgerName the ledgerName to set
	 */
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return the periodCode
	 */
	public String getPeriodCode() {
		return periodCode;
	}
	/**
	 * @param periodCode the periodCode to set
	 */
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	/**
	 * @return the cnyCode
	 */
	public String getCnyCode() {
		return cnyCode;
	}
	/**
	 * @param cnyCode the cnyCode to set
	 */
	public void setCnyCode(String cnyCode) {
		this.cnyCode = cnyCode;
	}
	/**
	 * @return the sheetDesc
	 */
	public String getSheetDesc() {
		return sheetDesc;
	}
	/**
	 * @param sheetDesc the sheetDesc to set
	 */
	public void setSheetDesc(String sheetDesc) {
		this.sheetDesc = sheetDesc;
	}
	/**
	 * @return the appStatus
	 */
	public String getAppStatus() {
		return appStatus;
	}
	/**
	 * @param appStatus the appStatus to set
	 */
	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}
	/**
	 * @return the auditStatus
	 */
	public String getAuditStatus() {
		return auditStatus;
	}
	/**
	 * @param auditStatus the auditStatus to set
	 */
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	/**
	 * @return the auditStatusDesc
	 */
	public String getAuditStatusDesc() {
		return auditStatusDesc;
	}
	/**
	 * @param auditStatusDesc the auditStatusDesc to set
	 */
	public void setAuditStatusDesc(String auditStatusDesc) {
		this.auditStatusDesc = auditStatusDesc;
	}
	/**
	 * @return the instanceCode
	 */
	public String getInstanceCode() {
		return instanceCode;
	}
	/**
	 * @param instanceCode the instanceCode to set
	 */
	public void setInstanceCode(String instanceCode) {
		this.instanceCode = instanceCode;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * @return the tabName
	 */
	public String getTabName() {
		return tabName;
	}
	/**
	 * @param tabName the tabName to set
	 */
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}
	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * @return the exportTabName
	 */
	public String getExportTabName() {
		return exportTabName;
	}
	/**
	 * @param exportTabName the exportTabName to set
	 */
	public void setExportTabName(String exportTabName) {
		this.exportTabName = exportTabName;
	}  
	
}
