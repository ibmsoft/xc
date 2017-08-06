package com.xzsoft.xc.bg.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName: BgItemBean
  * @Description: 预算项目实体类
  * @author 任建建
  * @date 2016年3月29日 下午8:38:43
 */
public class BgItemBean {
	private String bgItemId;
	private String bgHrcyId;
	private String bgItemCode;
	private String bgItemName;
	private String bgItemDesc;
	private String bgItemUpId;
	private String bgItemProp;
	private String bgItemType;
	private String startDate;
	private String endDate;
	private Date creationDate;
	private String createdBy;
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	
	private List<BgLdItemBean> ldItem;

	public BgItemBean() {
		super();
	}

	public BgItemBean(String bgItemId, String bgHrcyId, String bgItemCode,
			String bgItemName, String bgItemDesc, String bgItemUpId,
			String bgItemProp, String bgItemType, String startDate,
			String endDate, Date creationDate, String createdBy,
			Date lastUpdateDate, String lastUpdatedBy, List<BgLdItemBean> ldItem) {
		super();
		this.bgItemId = bgItemId;
		this.bgHrcyId = bgHrcyId;
		this.bgItemCode = bgItemCode;
		this.bgItemName = bgItemName;
		this.bgItemDesc = bgItemDesc;
		this.bgItemUpId = bgItemUpId;
		this.bgItemProp = bgItemProp;
		this.bgItemType = bgItemType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.ldItem = ldItem;
	}

	@Override
	public String toString() {
		return "BgItemBean [bgItemId=" + bgItemId + ", bgHrcyId=" + bgHrcyId
				+ ", bgItemCode=" + bgItemCode + ", bgItemName=" + bgItemName
				+ ", bgItemDesc=" + bgItemDesc + ", bgItemUpId=" + bgItemUpId
				+ ", bgItemProp=" + bgItemProp + ", bgItemType=" + bgItemType
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", creationDate=" + creationDate + ", createdBy=" + createdBy
				+ ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy="
				+ lastUpdatedBy + ", ldItem=" + ldItem + "]";
	}

	public String getBgItemId() {
		return bgItemId;
	}

	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
	}

	public String getBgHrcyId() {
		return bgHrcyId;
	}

	public void setBgHrcyId(String bgHrcyId) {
		this.bgHrcyId = bgHrcyId;
	}

	public String getBgItemCode() {
		return bgItemCode;
	}

	public void setBgItemCode(String bgItemCode) {
		this.bgItemCode = bgItemCode;
	}

	public String getBgItemName() {
		return bgItemName;
	}

	public void setBgItemName(String bgItemName) {
		this.bgItemName = bgItemName;
	}

	public String getBgItemDesc() {
		return bgItemDesc;
	}

	public void setBgItemDesc(String bgItemDesc) {
		this.bgItemDesc = bgItemDesc;
	}

	public String getBgItemUpId() {
		return bgItemUpId;
	}

	public void setBgItemUpId(String bgItemUpId) {
		this.bgItemUpId = bgItemUpId;
	}

	public String getBgItemProp() {
		return bgItemProp;
	}

	public void setBgItemProp(String bgItemProp) {
		this.bgItemProp = bgItemProp;
	}

	public String getBgItemType() {
		return bgItemType;
	}

	public void setBgItemType(String bgItemType) {
		this.bgItemType = bgItemType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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

	public List<BgLdItemBean> getLdItem() {
		return ldItem;
	}

	public void setLdItem(List<BgLdItemBean> ldItem) {
		this.ldItem = ldItem;
	}
}
