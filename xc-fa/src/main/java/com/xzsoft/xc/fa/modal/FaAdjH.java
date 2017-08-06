package com.xzsoft.xc.fa.modal;

import java.util.Date;

/**
 * ClassName:FaAdjH
 * Function: 资产调整头表
 *
 * @author   q p
 * @Date	 2016	2016年7月28日		下午2:28:18
 *
 */
public class FaAdjH {
	// 资产调整头ID
	private String adjHid;
	// 账簿ID
	private String ledgerId;
	// 调整日期
	private String adjDate;
	// 期间编码
	private String periodCode;
	// 调整单编码
	private String docNum;
	// 调整原因
	private String bzsm;
	// 创建人
	private String createdBy;
	// 创建日期
	private Date creationDate;
	// 最后更新人
	private String lastUpdatedBy;
	// 最后更新日期
	private Date lastUpdateDate;
	// 计提折旧前后 B:计提前 A:计提后
	private String jtzjType;
	// 调整标记 C起草  S提交
	private String auditStatus;
	
	public String getJtzjType() {
		return jtzjType;
	}
	public void setJtzjType(String jtzjType) {
		this.jtzjType = jtzjType;
	}
	public String getAdjHid() {
		return adjHid;
	}
	public void setAdjHid(String adjHid) {
		this.adjHid = adjHid;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getAdjDate() {
		return adjDate;
	}
	public void setAdjDate(String adjDate) {
		this.adjDate = adjDate;
	}
	public String getDocNum() {
		return docNum;
	}
	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}
	public String getBzsm() {
		return bzsm;
	}
	public void setBzsm(String bzsm) {
		this.bzsm = bzsm;
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
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	
}
