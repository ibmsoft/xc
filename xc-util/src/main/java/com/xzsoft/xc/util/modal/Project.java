package com.xzsoft.xc.util.modal;

import java.util.Date;
import java.util.List;

public class Project {
	
	private String projectId ;
	private String projectCode ;
	private String projectName ;
	private String ledgerId ;
	private String upPorjectId ;
	
	private Date startDate ;			// 开始日期
	private Date endDate ;				// 结束日期
	private String prjCtrlDim ;			// 控制维度：1-项目，2-项目+预算项目，3-项目+预算项目+成本中心
	private String prjCtrlMode ;		// 控制方式：1-不控制，2-预警控制，3-绝对控制
	private String isBgCtrl ;			// 是否控制预算：Y-是，N-否
	private List<ProjectDept> depts ;	// 成本中心
	
	private String upProjectCode;
	private String ledgerCode;
	private Date synchronizeDate;
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getUpPorjectId() {
		return upPorjectId;
	}
	public void setUpPorjectId(String upPorjectId) {
		this.upPorjectId = upPorjectId;
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
	public String getPrjCtrlDim() {
		return prjCtrlDim;
	}
	public void setPrjCtrlDim(String prjCtrlDim) {
		this.prjCtrlDim = prjCtrlDim;
	}
	public String getPrjCtrlMode() {
		return prjCtrlMode;
	}
	public void setPrjCtrlMode(String prjCtrlMode) {
		this.prjCtrlMode = prjCtrlMode;
	}
	public String getIsBgCtrl() {
		return isBgCtrl;
	}
	public void setIsBgCtrl(String isBgCtrl) {
		this.isBgCtrl = isBgCtrl;
	}
	public List<ProjectDept> getDepts() {
		return depts;
	}
	public void setDepts(List<ProjectDept> depts) {
		this.depts = depts;
	}
	public String getUpProjectCode() {
		return upProjectCode;
	}
	public void setUpProjectCode(String upProjectCode) {
		this.upProjectCode = upProjectCode;
	}
	public String getLedgerCode() {
		return ledgerCode;
	}
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
	public Date getSynchronizeDate() {
		return synchronizeDate;
	}
	public void setSynchronizeDate(Date synchronizeDate) {
		this.synchronizeDate = synchronizeDate;
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
