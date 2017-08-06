/**
 * 
 */
package com.xzsoft.xc.rep.modal;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @describe 报表模板类
 * @author tangxl
 *
 */
public class ReportTabBean {
	private int id;            //对应xc_rep_tab表 -- TAB_ORDER 字段,EnterpriseSheet的内置字段
	private String name;       //对应xc_rep_tab表 -- TAB_NAME 字段,EnterpriseSheet的内置字段
	@JsonIgnore
	private String tabCode;
	@JsonIgnore
	private String tabId;
	@JsonIgnore
	private String accHrcyId;
	@JsonIgnore
	private int isActive;
	@JsonIgnore
	private String color;
	@JsonIgnore
	private Date creationDate;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private Date lastUpdateDate;
	@JsonIgnore
	private String lastUpdatedBy;
	/**
	 * default construct
	 */
	public ReportTabBean(){
		
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the tabCode
	 */
	public String getTabCode() {
		return tabCode;
	}
	/**
	 * @param tabCode the tabCode to set
	 */
	public void setTabCode(String tabCode) {
		this.tabCode = tabCode;
	}
	/**
	 * @return the tabId
	 */
	public String getTabId() {
		return tabId;
	}
	/**
	 * @param tabId the tabId to set
	 */
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	/**
	 * @return the accHrcyId
	 */
	public String getAccHrcyId() {
		return accHrcyId;
	}
	/**
	 * @param accHrcyId the accHrcyId to set
	 */
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	/**
	 * @return the isActive
	 */
	public int getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

}
