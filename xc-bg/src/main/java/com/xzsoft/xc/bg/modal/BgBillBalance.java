package com.xzsoft.xc.bg.modal;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @className BgBillBalance名
 * @author    tangxl
 * @date      2016年6月17日
 * @describe  预算余额POJO
 * @变动说明
 */
public class BgBillBalance {
	private String ledgerId;
	private String periodCode;
	private String bgYear;
	private String bgItemId;
	private String bgItemName;
	private String deptId;
	private BigDecimal bgAmount;
	private Date creationDate;
	private String createdBy;
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	private String projectId;
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
	/**
	 * @return the bgItemId
	 */
	public String getBgItemId() {
		return bgItemId;
	}
	/**
	 * @param bgItemId the bgItemId to set
	 */
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}
	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
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
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the bgItemName
	 */
	public String getBgItemName() {
		return bgItemName;
	}
	/**
	 * @param bgItemName the bgItemName to set
	 */
	public void setBgItemName(String bgItemName) {
		this.bgItemName = bgItemName;
	}
}
