/**
 * 
 */
package com.xzsoft.xc.bg.modal;

import java.math.BigDecimal;

/**
 * @className BgBillBalance名
 * @author    tangxl
 * @date      2016年6月17日
 * @describe  预算余额POJO
 * @变动说明
 */
public class BgFactBalance {
	private String ledgerId;
	private String projectId;
	private String bgItemId;
	private String deptId;
	private BigDecimal bgAmount;
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
	
}
