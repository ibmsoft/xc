/**
 * 
 */
package com.xzsoft.xc.rep.modal;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @describe xc_rep_tab_element
 * @author tangxl
 *
 */
public class ReportTabElementBean {
	private String elementId;
	private String sheet;            //xc_rep_tab 字段 TAB_ORDER
	private String name;
	private String ftype;           //ETYPE   字段
	private String json;            //CONTENT 字段
	private String tabId;
	@JsonIgnore
	private Date creationDate;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private Date lastUpdateDate;
	@JsonIgnore
	private String lastUpdatedBy;
	/**
	 * @return the elementId
	 */
	public String getElementId() {
		return elementId;
	}
	/**
	 * @param elementId the elementId to set
	 */
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	/**
	 * @return the sheet
	 */
	public String getSheet() {
		return sheet;
	}
	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(String sheet) {
		this.sheet = sheet;
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
	 * @return the ftype
	 */
	public String getFtype() {
		return ftype;
	}
	/**
	 * @param ftype the ftype to set
	 */
	public void setFtype(String ftype) {
		this.ftype = ftype;
	}
	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}
	/**
	 * @param json the json to set
	 */
	public void setJson(String json) {
		this.json = json;
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
}
