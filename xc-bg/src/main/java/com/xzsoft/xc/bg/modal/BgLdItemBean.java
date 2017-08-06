package com.xzsoft.xc.bg.modal;

import java.util.Date;

/**
 * 
  * @ClassName: BgLdItemBean
  * @Description: 账簿级预算项目
  * @author 任建建
  * @date 2016年3月29日 下午8:46:00
 */
public class BgLdItemBean {
	private String bgLdItemId;
	private String ledgerId;
	private String bgItemId;
	private String bgCtrlDim;
	private String bgCtrlMode;
	private String isEnabled;
	private Date creationDate;
	private String createdBy;
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	
	public BgLdItemBean() {
		super();
	}
	public BgLdItemBean(String bgLdItemId, String ledgerId, String bgItemId,
			String bgCtrlDim, String bgCtrlMode, String isEnabled,
			Date creationDate, String createdBy, Date lastUpdateDate,
			String lastUpdatedBy) {
		super();
		this.bgLdItemId = bgLdItemId;
		this.ledgerId = ledgerId;
		this.bgItemId = bgItemId;
		this.bgCtrlDim = bgCtrlDim;
		this.bgCtrlMode = bgCtrlMode;
		this.isEnabled = isEnabled;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getBgLdItemId() {
		return bgLdItemId;
	}
	public void setBgLdItemId(String bgLdItemId) {
		this.bgLdItemId = bgLdItemId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getBgItemId() {
		return bgItemId;
	}
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}
	public String getBgCtrlDim() {
		return bgCtrlDim;
	}
	public void setBgCtrlDim(String bgCtrlDim) {
		this.bgCtrlDim = bgCtrlDim;
	}
	public String getBgCtrlMode() {
		return bgCtrlMode;
	}
	public void setBgCtrlMode(String bgCtrlMode) {
		this.bgCtrlMode = bgCtrlMode;
	}
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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
}
