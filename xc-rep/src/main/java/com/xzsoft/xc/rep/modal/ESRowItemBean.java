package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: ESRowItemBean 
 * @Description: 行指标信息表
 * @author linp
 * @date 2016年9月8日 下午6:13:23 
 *
 */
public class ESRowItemBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String rowitemId ;
	@JsonIgnore
	private String accHrcyId ;
	
	private String rowitemCode ;
	private String rowitemName ;
	private String rowitemDesc ;
	private String rowitemAlias ;
	private String upcode ;
	
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date lastUpdateDate ;
	@JsonIgnore
	private String lastUpdatedBy ;
	
	public String getRowitemId() {
		return rowitemId;
	}
	public void setRowitemId(String rowitemId) {
		this.rowitemId = rowitemId;
	}
	public String getAccHrcyId() {
		return accHrcyId;
	}
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	public String getRowitemCode() {
		return rowitemCode;
	}
	public void setRowitemCode(String rowitemCode) {
		this.rowitemCode = rowitemCode;
	}
	public String getRowitemName() {
		return rowitemName;
	}
	public void setRowitemName(String rowitemName) {
		this.rowitemName = rowitemName;
	}
	public String getRowitemDesc() {
		return rowitemDesc;
	}
	public void setRowitemDesc(String rowitemDesc) {
		this.rowitemDesc = rowitemDesc;
	}
	public String getRowitemAlias() {
		return rowitemAlias;
	}
	public void setRowitemAlias(String rowitemAlias) {
		this.rowitemAlias = rowitemAlias;
	}
	public String getUpcode() {
		return upcode;
	}
	public void setUpcode(String upcode) {
		this.upcode = upcode;
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
