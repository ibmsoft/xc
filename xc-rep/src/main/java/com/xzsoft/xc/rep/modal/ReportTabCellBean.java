/**
 * 
 */
package com.xzsoft.xc.rep.modal;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @describe 报表模板单元格类
 * @author tangxl
 *
 */
public class ReportTabCellBean {
	private String cellId;
	private int sheet;                   //xc_rep_tab表的TAB_ORDER字段
	private int row;                     //xc_rep_tab_cell表 X
	private int col;                     //xc_rep_tab_cell表Y
	private String json;                 //xc_rep_tab_cell表CONTENT
	private int cal;                     //xc_rep_tab_cell表IS_CAL
	@JsonIgnore
	private String tabId;
	@JsonIgnore
	private String rawData;             
	@JsonIgnore
	private String cellType;
	@JsonIgnore
	private String comment;
	@JsonIgnore
	private Date creationDate;
	@JsonIgnore
	private String createdBy;
	@JsonIgnore
	private Date lastUpdateDate;
	@JsonIgnore
	private String lastUpdatedBy;
	/**
	 * @return the cellId
	 */
	public String getCellId() {
		return cellId;
	}
	/**
	 * @param cellId the cellId to set
	 */
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	/**
	 * @return the sheet
	 */
	public int getSheet() {
		return sheet;
	}
	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(int sheet) {
		this.sheet = sheet;
	}
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}
	/**
	 * @return the col
	 */
	public int getCol() {
		return col;
	}
	/**
	 * @param col the col to set
	 */
	public void setCol(int col) {
		this.col = col;
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
	 * @return the cal
	 */
	public int getCal() {
		return cal;
	}
	/**
	 * @param cal the cal to set
	 */
	public void setCal(int cal) {
		this.cal = cal;
	}
	/**
	 * @return the rawData
	 */
	public String getRawData() {
		return rawData;
	}
	/**
	 * @param rawData the rawData to set
	 */
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	/**
	 * @return the cellType
	 */
	public String getCellType() {
		return cellType;
	}
	/**
	 * @param cellType the cellType to set
	 */
	public void setCellType(String cellType) {
		this.cellType = cellType;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
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
