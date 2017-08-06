package com.xzsoft.xc.gl.modal;

import java.util.Date;

public class VInterface {
	// 接口Id
	private String vInterfaceId;
	// 账簿Id
	private String ledgerId;
	// 期间
	private String periodCode;
	// 事务编码
	private String transCode;
	// 成本中心
	private String deptId;
	// 项目Id
	private String projectId;
	// 借方科目编码
	private String drAcct;
	// 贷方科目编码
	private String crAcct;
	// 借方科目组合ID
	private String drCcid;
	// 贷方科目组合ID
	private String crCcid;
	// 金额
	private double amt;
	// 摘要
	private String summary;
	// 业务编码
	private String busNo;
	// 来源主Id
	private String srcHid;
	// 来源子Id
	private String srcLid;
	// 借方凭证行Id
	private String drVLineId;
	// 贷方凭证行Id
	private String crVLineId;
	// 凭证头Id
	private String iHeadId;
	// 创建日期
	private Date creationDate;
	// 创建人
	private String createdBy;
	// 最后更新日期
	private Date lastUpdateDate;
	// 最后更新人
	private String lastUpdateBy;

	public String getvInterfaceId() {
		return vInterfaceId;
	}

	public void setvInterfaceId(String vInterfaceId) {
		this.vInterfaceId = vInterfaceId;
	}

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

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getDrAcct() {
		return drAcct;
	}

	public void setDrAcct(String drAcct) {
		this.drAcct = drAcct;
	}

	public String getCrAcct() {
		return crAcct;
	}

	public void setCrAcct(String crAcct) {
		this.crAcct = crAcct;
	}

	public String getDrCcid() {
		return drCcid;
	}

	public void setDrCcid(String drCcid) {
		this.drCcid = drCcid;
	}

	public String getCrCcid() {
		return crCcid;
	}

	public void setCrCcid(String crCcid) {
		this.crCcid = crCcid;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getBusNo() {
		return busNo;
	}

	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}

	public String getSrcHid() {
		return srcHid;
	}

	public void setSrcHid(String srcHid) {
		this.srcHid = srcHid;
	}

	public String getSrcLid() {
		return srcLid;
	}

	public void setSrcLid(String srcLid) {
		this.srcLid = srcLid;
	}

	public String getDrVLineId() {
		return drVLineId;
	}

	public void setDrVLineId(String drVLineId) {
		this.drVLineId = drVLineId;
	}

	public String getCrVLineId() {
		return crVLineId;
	}

	public void setCrVLineId(String crVLineId) {
		this.crVLineId = crVLineId;
	}

	public String getiHeadId() {
		return iHeadId;
	}

	public void setiHeadId(String iHeadId) {
		this.iHeadId = iHeadId;
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
