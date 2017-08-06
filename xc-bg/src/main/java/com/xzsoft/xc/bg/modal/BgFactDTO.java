package com.xzsoft.xc.bg.modal;

import java.util.Date;

/**
 * @ClassName: BgFactDTO 
 * @Description: 预算检查参数
 * @author linp
 * @date 2016年4月27日 上午9:59:53 
 *
 */
public class BgFactDTO {
	
	private String ledgerId ;	// 账簿ID
	private String projectId ;	// PA项目ID
	private String bgItemId ;   // 预算项目ID
	private String deptId ;		// 成本中心ID
	private Date factDate ;		// 业务日期
	private double amount ;		// 单据金额
	private String srcId ;		// 来源ID
	private String srcTab ; 	// 来源表
	
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
}
