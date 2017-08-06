/**
 * 
 */
package com.xzsoft.xc.gl.modal;

import java.util.Date;
import java.util.List;

/**
 * 结转模板POJO
 * @author tangxl
 *
 */
public class GlJzTpl {
	private String jzTplId;
	private String jzTplCode;
	private String jzTplName;
	private String ledgerId;
	private String ledgerCode;
	private String vCategoryId;
	private String categoryCode;
	private String zrAccId;
	private String zrAccCode;
	private String ccid;
	private String accSegment;             //科目辅助段启用名称
	private List<String> accSegmentCode;   //科目辅助启用断编码
	private String zrAccDirection;         //转入科目方向
	private String summary;
	private List<GlJzTplDtl> jzDtlList;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
	/**
	 * @return the jzTplId
	 */
	public String getJzTplId() {
		return jzTplId;
	}
	/**
	 * @param jzTplId the jzTplId to set
	 */
	public void setJzTplId(String jzTplId) {
		this.jzTplId = jzTplId;
	}
	/**
	 * @return the jzTplCode
	 */
	public String getJzTplCode() {
		return jzTplCode;
	}
	/**
	 * @param jzTplCode the jzTplCode to set
	 */
	public void setJzTplCode(String jzTplCode) {
		this.jzTplCode = jzTplCode;
	}
	/**
	 * @return the jzTplName
	 */
	public String getJzTplName() {
		return jzTplName;
	}
	/**
	 * @param jzTplName the jzTplName to set
	 */
	public void setJzTplName(String jzTplName) {
		this.jzTplName = jzTplName;
	}
	/**
	 * @return the ledgerId
	 */
	public String getLedgerId() {
		return ledgerId;
	}
	/**
	 * @param ledgerId the ledgerId to set
	 */
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	/**
	 * @return the vCategoryId
	 */
	public String getvCategoryId() {
		return vCategoryId;
	}
	/**
	 * @param vCategoryId the vCategoryId to set
	 */
	public void setvCategoryId(String vCategoryId) {
		this.vCategoryId = vCategoryId;
	}
	/**
	 * @return the zrAccId
	 */
	public String getZrAccId() {
		return zrAccId;
	}
	/**
	 * @param zrAccId the zrAccId to set
	 */
	public void setZrAccId(String zrAccId) {
		this.zrAccId = zrAccId;
	}
	/**
	 * @return the accSegment
	 */
	public String getAccSegment() {
		return accSegment;
	}
	/**
	 * @param accSegment the accSegment to set
	 */
	public void setAccSegment(String accSegment) {
		this.accSegment = accSegment;
	}
	/**
	 * @return the zrAccDirection
	 */
	public String getZrAccDirection() {
		return zrAccDirection;
	}
	/**
	 * @param zrAccDirection the zrAccDirection to set
	 */
	public void setZrAccDirection(String zrAccDirection) {
		this.zrAccDirection = zrAccDirection;
	}
	/**
	 * @return the summary
	 */
	public String getSummary() {
		return summary;
	}
	/**
	 * @param summary the summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}
	/**
	 * @return the jzDtlList
	 */
	public List<GlJzTplDtl> getJzDtlList() {
		return jzDtlList;
	}
	/**
	 * @param jzDtlList the jzDtlList to set
	 */
	public void setJzDtlList(List<GlJzTplDtl> jzDtlList) {
		this.jzDtlList = jzDtlList;
	}
	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}
	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	/**
	 * @return the lastUpdatedBy
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	/**
	 * @param lastUpdatedBy the lastUpdatedBy to set
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	/**
	 * @return the ledgerCode
	 */
	public String getLedgerCode() {
		return ledgerCode;
	}
	/**
	 * @param ledgerCode the ledgerCode to set
	 */
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
	/**
	 * @return the categoryCode
	 */
	public String getCategoryCode() {
		return categoryCode;
	}
	/**
	 * @param categoryCode the categoryCode to set
	 */
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	/**
	 * @return the zrAccCode
	 */
	public String getZrAccCode() {
		return zrAccCode;
	}
	/**
	 * @param zrAccCode the zrAccCode to set
	 */
	public void setZrAccCode(String zrAccCode) {
		this.zrAccCode = zrAccCode;
	}
	/**
	 * @return the ccid
	 */
	public String getCcid() {
		return ccid;
	}
	/**
	 * @param ccid the ccid to set
	 */
	public void setCcid(String ccid) {
		this.ccid = ccid;
	}
	/**
	 * @return the accSegmentCode
	 */
	public List<String> getAccSegmentCode() {
		return accSegmentCode;
	}
	/**
	 * @param accSegmentCode the accSegmentCode to set
	 */
	public void setAccSegmentCode(List<String> accSegmentCode) {
		this.accSegmentCode = accSegmentCode;
	}
    
}
