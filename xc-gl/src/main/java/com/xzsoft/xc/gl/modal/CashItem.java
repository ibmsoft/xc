package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * @ClassName: CashItem 
 * @Description: 现金流量项目信息
 * @author linp
 * @date 2016年1月15日 上午9:28:57 
 *
 */
public class CashItem {
	private String caId ;
	private String caCode ;
	private String caName ;
	private String upId ;
	private String upCode;
	private String caDirection ;	// 方向 ，流入（1），流出（-1）
	private String assKey;
	private String caLevel ;		// 层级
	private String isLeaf ; 		// 是否末级
	private Date startDate ;
	private Date endDate ;
	private String caDesc ;
	private int orderby ;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	private Date synchronizeDate;
	public String getCaId() {
		return caId;
	}
	public void setCaId(String caId) {
		this.caId = caId;
	}
	public String getCaCode() {
		return caCode;
	}
	public void setCaCode(String caCode) {
		this.caCode = caCode;
	}
	public String getCaName() {
		return caName;
	}
	public void setCaName(String caName) {
		this.caName = caName;
	}
	public String getUpId() {
		return upId;
	}
	public void setUpId(String upId) {
		this.upId = upId;
	}
	public String getCaDirection() {
		return caDirection;
	}
	public void setCaDirection(String caDirection) {
		this.caDirection = caDirection;
	}
	public String getCaLevel() {
		return caLevel;
	}
	public void setCaLevel(String caLevel) {
		this.caLevel = caLevel;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCaDesc() {
		return caDesc;
	}
	public void setCaDesc(String caDesc) {
		this.caDesc = caDesc;
	}
	public int getOrderby() {
		return orderby;
	}
	public void setOrderby(int orderby) {
		this.orderby = orderby;
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
	public String getUpCode() {
		return upCode;
	}
	public void setUpCode(String upCode) {
		this.upCode = upCode;
	}
	public Date getSynchronizeDate() {
		return synchronizeDate;
	}
	public void setSynchronizeDate(Date synchronizeDate) {
		this.synchronizeDate = synchronizeDate;
	}
	public String getAssKey() {
		return assKey;
	}
	public void setAssKey(String assKey) {
		this.assKey = assKey;
	}
	
}
