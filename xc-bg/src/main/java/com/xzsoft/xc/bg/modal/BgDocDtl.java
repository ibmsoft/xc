package com.xzsoft.xc.bg.modal;

import java.math.BigDecimal;
import java.util.Date;
/**
 * @className  BgDocDtl
 * @describe   预算单信息
 * @变动说明               增加预算单类型  账簿ID 预算年 流程实例编码 
 * @modifyDate 2016-06-17
 * @version    1.1
 */

public class BgDocDtl {
	private String bgDocId;
	private String bgDtlId;
	private String bgItemId;
	private String projectId;
	private String deptId;
	private String periodCode;
	private String bgYear;
	private String amount;
	private String bgDtlDesc;
	private Date creationDate;
	private String createdBy;
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	private String bgCatCode;      //预算单类型 BG01-费用预算；BG-02-项目预算  
	private String ledgerId;       //账簿ID
	private String insCode;        //流程实例编码
	private BigDecimal bgAmount;   //申请金额BigDecimal类型
	public String getBgDocId() {
		return bgDocId;
	}
	public void setBgDocId(String bgDocId) {
		this.bgDocId = bgDocId;
	}
	public String getBgDtlId() {
		return bgDtlId;
	}
	public void setBgDtlId(String bgDtlId) {
		this.bgDtlId = bgDtlId;
	}
	public String getBgItemId() {
		return bgItemId;
	}
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	
	/**
	 * @return the bgYear
	 */
	public String getBgYear() {
		return bgYear;
	}
	/**
	 * @param bgYear the bgYear to set
	 */
	public void setBgYear(String bgYear) {
		this.bgYear = bgYear;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBgDtlDesc() {
		return bgDtlDesc;
	}
	public void setBgDtlDesc(String bgDtlDesc) {
		this.bgDtlDesc = bgDtlDesc;
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
	/**
	 * @return the bgCatCode
	 */
	public String getBgCatCode() {
		return bgCatCode;
	}
	/**
	 * @param bgCatCode the bgCatCode to set
	 */
	public void setBgCatCode(String bgCatCode) {
		this.bgCatCode = bgCatCode;
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
	 * @return the insCode
	 */
	public String getInsCode() {
		return insCode;
	}
	/**
	 * @param insCode the insCode to set
	 */
	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}
	/**
	 * @return the bgAmount
	 */
	public BigDecimal getBgAmount() {
		return bgAmount;
	}
	/**
	 * @param bgAmount the bgAmount to set
	 */
	public void setBgAmount(BigDecimal bgAmount) {
		this.bgAmount = bgAmount;
	}
	
}
