package com.xzsoft.xc.bg.modal;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 
  * @ClassName: BgCostBal
  * @Description: 费用(项目)预算汇总表实体类
  * @author 任建建
  * @date 2016年3月10日 下午1:46:45
 */
public class BgCostPrjBalBean {
	private String ledgerId;			//账簿
	private String periodCode;			//会计期
	private String bgYear;				//年度
	private String bgItemId;			//预算项目
	private String projectId;			//项目ID
	private String deptId;				//成本中心
	private BigDecimal bgAmt;			//预算金额
	private Date creationDate;			//创建日期
	private String createdBy;			//创建人
	private Date lastUpdateDate;		//最后更新日期
	private String lastUpdatedBy;		//最后更新人
	private String opType; 				//操作类型 : 1-预算费用余额汇总插入,2-预算项目余额汇总插入
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getBgYear() {
		return bgYear;
	}
	public void setBgYear(String bgYear) {
		this.bgYear = bgYear;
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
	public BigDecimal getBgAmt() {
		return bgAmt;
	}
	public void setBgAmt(BigDecimal bgAmt) {
		this.bgAmt = bgAmt;
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
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public BgCostPrjBalBean(String ledgerId, String periodCode, String bgYear,
			String bgItemId, String projectId, String deptId, BigDecimal bgAmt,
			Date creationDate, String createdBy, Date lastUpdateDate,
			String lastUpdatedBy, String opType) {
		super();
		this.ledgerId = ledgerId;
		this.periodCode = periodCode;
		this.bgYear = bgYear;
		this.bgItemId = bgItemId;
		this.projectId = projectId;
		this.deptId = deptId;
		this.bgAmt = bgAmt;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.opType = opType;
	}
	public BgCostPrjBalBean() {
		super();
	}
}