package com.xzsoft.xc.fa.modal;

import java.util.Date;

public class XcFaDepr {
	// 计提折旧ID
	private String deprId;
	// 账簿ID
	private String ledgerId;
	// 期间编码
	private String periodCode;
	// 资产ID
	private String assetId;
	// 折旧方法
	private String zjff;
	// 原值
	private double yz;
	// 减值准备
	private double jzzb;
	// 残值率
	private double czl;
	// 累计折旧或累计摊销
	private double ljzjljtx;
	// 本期折旧或本期摊销
	private double bqzj;
	// 折旧率
	private double zjl;
	// 创建人
	private String createdBy;
	// 创建日期
	private Date creationDate;
	// 最后跟新人
	private String lastUpdateBy;
	// 最后更新日期
	private Date lastUpdateDate;

	public String getDeprId() {
		return deprId;
	}

	public void setDeprId(String deprId) {
		this.deprId = deprId;
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

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getZjff() {
		return zjff;
	}

	public void setZjff(String zjff) {
		this.zjff = zjff;
	}

	public double getYz() {
		return yz;
	}

	public void setYz(double yz) {
		this.yz = yz;
	}

	public double getJzzb() {
		return jzzb;
	}

	public void setJzzb(double jzzb) {
		this.jzzb = jzzb;
	}

	public double getCzl() {
		return czl;
	}

	public void setCzl(double czl) {
		this.czl = czl;
	}

	public double getLjzjljtx() {
		return ljzjljtx;
	}

	public void setLjzjljtx(double ljzjljtx) {
		this.ljzjljtx = ljzjljtx;
	}

	public double getBqzj() {
		return bqzj;
	}

	public void setBqzj(double bqzj) {
		this.bqzj = bqzj;
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

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public double getZjl() {
		return zjl;
	}

	public void setZjl(double zjl) {
		this.zjl = zjl;
	}

}