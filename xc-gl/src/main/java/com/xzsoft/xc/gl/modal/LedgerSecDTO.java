package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * @ClassName: LedgerSecDTO 
 * @Description: 账簿授权信息
 * @author linp
 * @date 2016年5月13日 下午3:48:39 
 *
 */
public class LedgerSecDTO {

	private String ldSecId ;
	private String userId ;
	private String roleId ;
	private String ledgerId ;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	public String getLdSecId() {
		return ldSecId;
	}
	public void setLdSecId(String ldSecId) {
		this.ldSecId = ldSecId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
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
