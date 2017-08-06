package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: ESTabBean 
 * @Description: EnterpriseSheet Tab 对象
 * @author linp
 * @date 2016年8月31日 下午5:14:05 
 *
 */
public class ESTabBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private String tabId ;			// 模板ID
	@JsonIgnore
	private String accHrcyId ;		// 科目体系ID
	
	private String tabCode ;		// 模板编码
	private String tabName ;		// 模板名称
	
	@JsonIgnore
	private int tabOrder ;			// 页签序号
	@JsonIgnore
	private int isActive ;			// 是否为活动页签：0-非活动，1-活动页签
	@JsonIgnore
	private String color ;			// 页签标题颜色
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String lastUpdatedBy;
	@JsonIgnore
	private Date lastUpdateDate ;
	
	private List<ESTabCellBean> cells; 
	private List<ESTabElementBean> elements ;
	@JsonIgnore
	private List<ESRowItemBean> rowItems ;
	private List<ESRowItemRefBean> rowItemsRef ;
	@JsonIgnore
	private List<ESColItemBean> colitems ;
	private List<ESColItemRefBean> colItemsRef ;
	
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public String getAccHrcyId() {
		return accHrcyId;
	}
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	public String getTabCode() {
		return tabCode;
	}
	public void setTabCode(String tabCode) {
		this.tabCode = tabCode;
	}
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public int getTabOrder() {
		return tabOrder;
	}
	public void setTabOrder(int tabOrder) {
		this.tabOrder = tabOrder;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
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
	public List<ESTabCellBean> getCells() {
		return cells;
	}
	public void setCells(List<ESTabCellBean> cells) {
		this.cells = cells;
	}
	public List<ESTabElementBean> getElements() {
		return elements;
	}
	public void setElements(List<ESTabElementBean> elements) {
		this.elements = elements;
	}
	public List<ESRowItemBean> getRowItems() {
		return rowItems;
	}
	public void setRowItems(List<ESRowItemBean> rowItems) {
		this.rowItems = rowItems;
	}
	public List<ESRowItemRefBean> getRowItemsRef() {
		return rowItemsRef;
	}
	public void setRowItemsRef(List<ESRowItemRefBean> rowItemsRef) {
		this.rowItemsRef = rowItemsRef;
	}
	public List<ESColItemBean> getColitems() {
		return colitems;
	}
	public void setColitems(List<ESColItemBean> colitems) {
		this.colitems = colitems;
	}
	public List<ESColItemRefBean> getColItemsRef() {
		return colItemsRef;
	}
	public void setColItemsRef(List<ESColItemRefBean> colItemsRef) {
		this.colItemsRef = colItemsRef;
	}
	
}
