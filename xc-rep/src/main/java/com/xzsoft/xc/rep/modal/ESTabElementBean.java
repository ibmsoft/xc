package com.xzsoft.xc.rep.modal;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @ClassName: ESTabElementBean 
 * @Description: EnterpriseSheet tab element 对象
 * @author linp
 * @date 2016年9月1日 下午3:57:37 
 *
 */
public class ESTabElementBean implements Serializable {
	private static final long serialVersionUID = 1L;
	@JsonIgnore
	private String elementId ;
	@JsonIgnore
	private String tabId ;
	private String name ;
	private String etype ;
	private String content ;
	
	@JsonIgnore
	private String createdBy ;
	@JsonIgnore
	private Date creationDate ;
	@JsonIgnore
	private String lastUpdatedBy;
	@JsonIgnore
	private Date lastUpdateDate ;
	
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEtype() {
		return etype;
	}
	public void setEtype(String etype) {
		this.etype = etype;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
