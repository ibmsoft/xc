package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: ESTabCellBean 
 * @Description: EnterpriseSheet Tab Cell 对象
 * @author linp
 * @date 2016年9月1日 下午3:58:14 
 *
 */
public class ESTabCellBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private String cellId ;
	@JsonIgnore
	private String tabId ;
	
	private int x ;
	private int y ;
	private String content ;
	private int isCal ;
	private String rawData ;
	private String cellType ;
	private String comment ;
	
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String lastUpdatedBy;
	@JsonIgnore
	private Date lastUpdateDate ;
	
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIsCal() {
		return isCal;
	}
	public void setIsCal(int isCal) {
		this.isCal = isCal;
	}
	public String getRawData() {
		return rawData;
	}
	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
	public String getCellType() {
		return cellType;
	}
	public void setCellType(String cellType) {
		this.cellType = cellType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	
}
