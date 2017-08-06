package com.xzsoft.xc.fa.modal;

import java.util.Date;

public class FaAdjL {
	// 资产调整行ID
	private String adjDid;
	// 资产调整头ID
	private String adjHid;
	// 资产ID
	private String assetId;
	// 资产编码
	private String assetCode;
	// 资产名称
	private String assetName;
	// 调整类型
	private String adjType;
	// 调整类型显示值
	private String adjViewType;
	// 调整前值
	private String oldVal;
	// 调整前显示值
	private String oldViewVal;
	// 调整后值
	private String newVal;
	// 调整后显示值
	private String newViewVal;
	// 对方科目
	private String dfkm;
	// 对方科目
	private String dfkmView;
	// 是否录入凭证
	private String vouCherFlag;
	// 创建人
	private String createdBy;
	// 创建日期
	private Date creationDate;
	// 最后更新人
	private String lastUpdatedBy;
	// 最后更新日期
	private Date lastUpdateDate;
	// 调整来源 U:用户录入 S:系统生成
	private String adjSource;
	
	public String getAdjDid() {
		return adjDid;
	}
	public void setAdjDid(String adjDid) {
		this.adjDid = adjDid;
	}
	public String getAdjHid() {
		return adjHid;
	}
	public void setAdjHid(String adjHid) {
		this.adjHid = adjHid;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getAdjType() {
		return adjType;
	}
	public void setAdjType(String adjType) {
		this.adjType = adjType;
	}
	public String getOldVal() {
		return oldVal;
	}
	public void setOldVal(String oldVal) {
		this.oldVal = oldVal;
	}
	public String getNewVal() {
		return newVal;
	}
	public void setNewVal(String newVal) {
		this.newVal = newVal;
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
	public String getDfkm() {
		return dfkm;
	}
	public void setDfkm(String dfkm) {
		this.dfkm = dfkm;
	}
	public String getOldViewVal() {
		if(oldViewVal==null){
			return "";
		}
		return oldViewVal;
	}
	public void setOldViewVal(String oldViewVal) {
		this.oldViewVal = oldViewVal;
	}
	public String getNewViewVal() {
		return newViewVal;
	}
	public void setNewViewVal(String newViewVal) {
		this.newViewVal = newViewVal;
	}
	public String getAssetCode() {
		return assetCode;
	}
	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}
	public String getAdjViewType() {
		return adjViewType;
	}
	public void setAdjViewType(String adjViewType) {
		this.adjViewType = adjViewType;
	}
	public String getVouCherFlag() {
		return vouCherFlag;
	}
	public void setVouCherFlag(String vouCherFlag) {
		this.vouCherFlag = vouCherFlag;
	}
	public String getAdjSource() {
		return adjSource;
	}
	public void setAdjSource(String adjSource) {
		this.adjSource = adjSource;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getDfkmView() {
		return dfkmView;
	}
	public void setDfkmView(String dfkmView) {
		this.dfkmView = dfkmView;
	}
	
}
