package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: ESColItemRefBean 
 * @Description: 列指标引用关系表
 * @author linp
 * @date 2016年9月8日 下午6:13:49 
 *
 */
public class ESColItemRefBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private String colRefId ;
	@JsonIgnore
	private String tabId ;
	@JsonIgnore
	private String colitemId ;
	
	private int colno ;
	private int lanno ;
	private String enabled ;
	
	private String colitemCode ;
	
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date lastUpdateDate ;
	@JsonIgnore
	private String lastUpdatedBy ;
	
	public String getColRefId() {
		return colRefId;
	}
	public void setColRefId(String colRefId) {
		this.colRefId = colRefId;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public String getColitemId() {
		return colitemId;
	}
	public void setColitemId(String colitemId) {
		this.colitemId = colitemId;
	}
	public int getColno() {
		return colno;
	}
	public void setColno(int colno) {
		this.colno = colno;
	}
	public int getLanno() {
		return lanno;
	}
	public void setLanno(int lanno) {
		this.lanno = lanno;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getColitemCode() {
		return colitemCode;
	}
	public void setColitemCode(String colitemCode) {
		this.colitemCode = colitemCode;
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
