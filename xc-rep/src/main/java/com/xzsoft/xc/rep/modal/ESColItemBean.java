package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: ESColItemBean 
 * @Description: 列指标信息表
 * @author linp
 * @date 2016年9月8日 下午6:12:08 
 *
 */
public class ESColItemBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String colitemId ;
	@JsonIgnore
	private String accHrcyId ;
	
	private String colitemCode ;
	private String colitemName ;
	private String colitemDesc ;
	private String colitemAlias ;
	private String upcode ;
	
	private String datatype ;
	private int orderby  ;
	
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date lastUpdateDate ;
	@JsonIgnore
	private String lastUpdatedBy ;
	
	public String getColitemId() {
		return colitemId;
	}
	public void setColitemId(String colitemId) {
		this.colitemId = colitemId;
	}
	public String getAccHrcyId() {
		return accHrcyId;
	}
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	public String getColitemCode() {
		return colitemCode;
	}
	public void setColitemCode(String colitemCode) {
		this.colitemCode = colitemCode;
	}
	public String getColitemName() {
		return colitemName;
	}
	public void setColitemName(String colitemName) {
		this.colitemName = colitemName;
	}
	public String getColitemDesc() {
		return colitemDesc;
	}
	public void setColitemDesc(String colitemDesc) {
		this.colitemDesc = colitemDesc;
	}
	public String getColitemAlias() {
		return colitemAlias;
	}
	public void setColitemAlias(String colitemAlias) {
		this.colitemAlias = colitemAlias;
	}
	public String getUpcode() {
		return upcode;
	}
	public void setUpcode(String upcode) {
		this.upcode = upcode;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
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
	
}
