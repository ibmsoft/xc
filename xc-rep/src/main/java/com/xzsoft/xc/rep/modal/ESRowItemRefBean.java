package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: ESRowItemRefBean 
 * @Description: 行指标引用关系表
 * @author linp
 * @date 2016年9月8日 下午6:13:36 
 *
 */
public class ESRowItemRefBean implements Serializable{
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private String rmRefId ;
	@JsonIgnore
	private String tabId ;
	@JsonIgnore
	private String rowitemId ;
	
	private int rowno ;
	private int lanno ;
	private int x ;
	private int y ;
	private String enabled ;
	
	private String rowitemCode ;
	
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date lastUpdateDate ;
	@JsonIgnore
	private String lastUpdatedBy ;
	
	public String getRmRefId() {
		return rmRefId;
	}
	public void setRmRefId(String rmRefId) {
		this.rmRefId = rmRefId;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public String getRowitemId() {
		return rowitemId;
	}
	public void setRowitemId(String rowitemId) {
		this.rowitemId = rowitemId;
	}
	public int getRowno() {
		return rowno;
	}
	public void setRowno(int rowno) {
		this.rowno = rowno;
	}
	public int getLanno() {
		return lanno;
	}
	public void setLanno(int lanno) {
		this.lanno = lanno;
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
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getRowitemCode() {
		return rowitemCode;
	}
	public void setRowitemCode(String rowitemCode) {
		this.rowitemCode = rowitemCode;
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
