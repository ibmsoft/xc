package com.xzsoft.xc.bg.modal;

import java.util.Date;

/**
 * @ClassName: BgFactBean 
 * @Description: 预算发生明细(占用与发生)
 * @author linp
 * @date 2016年4月6日 下午8:42:34 
 *
 */
public class BgFactBean {
	
	private String factId ;		// 预算发生与占用ID
	private String ledgerId ;	// 账簿ID
	private String projectId ;	// PA项目ID
	private String bgItemId ;   // 预算项目ID
	private String deptId ;		// 成本中心ID
	private Date factDate ;		// 业务日期
	private String factType ;	// 发生类型：1-预算占用,2-实际发生
	private double amount ;		// 单据金额
	private String srcId ;		// 来源ID
	private String srcTab ; 	// 来源表
	private String periodCode ; // 会计期
	private String year ;		// 会计年度

	private double bgBal ; 		// 预算余额
	
	private String ctrlMode ;   // 预算控制方式>> 1-不控制，2-预警控制，3-绝对控制
	private String ctrlDim ;	// 预算控制维>> 项目预算控制维度：【1-项目，2-项目+预算项目，3-项目+预算项目+成本中心】 ; 费用预算控制维度：【1-预算项目，2-预算项目+成本中心】
	private String ctrlPeriod ; // 费用预算控制周期：1-按年控制，2-按月累计控制
	
	private String prjName ;	// PA项目名称
	private String deptName ;	// 成本中心名称
	private String bgItemName ; // 预算项目名称
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdateBy ;
	
	public String getFactId() {
		return factId;
	}
	public void setFactId(String factId) {
		this.factId = factId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getBgItemId() {
		return bgItemId;
	}
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public Date getFactDate() {
		return factDate;
	}
	public void setFactDate(Date factDate) {
		this.factDate = factDate;
	}
	public String getFactType() {
		return factType;
	}
	public void setFactType(String factType) {
		this.factType = factType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getSrcTab() {
		return srcTab;
	}
	public void setSrcTab(String srcTab) {
		this.srcTab = srcTab;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public double getBgBal() {
		return bgBal;
	}
	public void setBgBal(double bgBal) {
		this.bgBal = bgBal;
	}
	public String getCtrlMode() {
		return ctrlMode;
	}
	public void setCtrlMode(String ctrlMode) {
		this.ctrlMode = ctrlMode;
	}
	public String getCtrlDim() {
		return ctrlDim;
	}
	public String getCtrlPeriod() {
		return ctrlPeriod;
	}
	public void setCtrlPeriod(String ctrlPeriod) {
		this.ctrlPeriod = ctrlPeriod;
	}
	public void setCtrlDim(String ctrlDim) {
		this.ctrlDim = ctrlDim;
	}
	public String getPrjName() {
		return prjName;
	}
	public void setPrjName(String prjName) {
		this.prjName = prjName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBgItemName() {
		return bgItemName;
	}
	public void setBgItemName(String bgItemName) {
		this.bgItemName = bgItemName;
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
	public String getLastUpdateBy() {
		return lastUpdateBy;
	}
	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

}
