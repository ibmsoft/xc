package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * @ClassName: LedgerSegments 
 * @Description: 账簿辅助核算段启用信息
 * @author linp
 * @date 2015年12月30日 下午12:29:21 
 *
 */
public class LedgerSegment {
	private String ldSegId ;
	private String ledgerId ;
	private String ledgerName ;
	private String segCode ;
	private String segName ;
	private String ldSegAlias ;
	private String isEnabled ;
	private String ldSegDesc ;
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	
	public String getLdSegId() {
		return ldSegId;
	}
	public void setLdSegId(String ldSegId) {
		this.ldSegId = ldSegId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public String getSegCode() {
		return segCode;
	}
	public void setSegCode(String segCode) {
		this.segCode = segCode;
	}
	public String getSegName() {
		return segName;
	}
	public void setSegName(String segName) {
		this.segName = segName;
	}
	public String getLdSegAlias() {
		return ldSegAlias;
	}
	public void setLdSegAlias(String ldSegAlias) {
		this.ldSegAlias = ldSegAlias;
	}
	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getLdSegDesc() {
		return ldSegDesc;
	}
	public void setLdSegDesc(String ldSegDesc) {
		this.ldSegDesc = ldSegDesc;
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
