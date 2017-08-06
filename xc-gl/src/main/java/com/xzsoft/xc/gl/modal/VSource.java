package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * @ClassName: VSource 
 * @Description: 凭证来源信息
 * @author linp
 * @date 2016年3月16日 下午4:30:50 
 *
 */
public class VSource {

	private String srcCode ;
	private String srcUrl ;
	private String srcDesc ;
	private String isSys ;
	private String vCat ;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	public String getSrcCode() {
		return srcCode;
	}
	public void setSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}
	public String getSrcUrl() {
		return srcUrl;
	}
	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}
	public String getSrcDesc() {
		return srcDesc;
	}
	public void setSrcDesc(String srcDesc) {
		this.srcDesc = srcDesc;
	}
	public String getIsSys() {
		return isSys;
	}
	public void setIsSys(String isSys) {
		this.isSys = isSys;
	}
	public String getvCat() {
		return vCat;
	}
	public void setvCat(String vCat) {
		this.vCat = vCat;
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
