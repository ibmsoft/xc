/**
 * 
 */
package com.xzsoft.xc.gl.modal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 结转模板明细POJO
 * @author tangxl
 *
 */
public class GlJzTplDtl {
	private String jzTplId;
	private String tplDtlId;
	private String zcAccId;
	private String sumAccId;
	private String balRule;
	private String balDirection;
	private String accSegment;
	private String summary;
	private BigDecimal convertAmount;
	private String ccid;
	private String ccidCode;
	private String accCode;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    //取数科目CCID、CCID_CODE、AMOUNT
    private List<HashMap<String, Object>> ccidCombineList;
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
	 * @return the tplDtlId
	 */
	public String getTplDtlId() {
		return tplDtlId;
	}
	/**
	 * @param tplDtlId the tplDtlId to set
	 */
	public void setTplDtlId(String tplDtlId) {
		this.tplDtlId = tplDtlId;
	}
	/**
	 * @return the zcAccId
	 */
	public String getZcAccId() {
		return zcAccId;
	}
	/**
	 * @param zcAccId the zcAccId to set
	 */
	public void setZcAccId(String zcAccId) {
		this.zcAccId = zcAccId;
	}
	/**
	 * @return the sumAccId
	 */
	public String getSumAccId() {
		return sumAccId;
	}
	/**
	 * @param sumAccId the sumAccId to set
	 */
	public void setSumAccId(String sumAccId) {
		this.sumAccId = sumAccId;
	}
	/**
	 * @return the balRule
	 */
	public String getBalRule() {
		return balRule;
	}
	/**
	 * @param balRule the balRule to set
	 */
	public void setBalRule(String balRule) {
		this.balRule = balRule;
	}
	/**
	 * @return the balDirection
	 */
	public String getBalDirection() {
		return balDirection;
	}
	/**
	 * @param balDirection the balDirection to set
	 */
	public void setBalDirection(String balDirection) {
		this.balDirection = balDirection;
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
	 * @return the convertAmount
	 */
	public BigDecimal getConvertAmount() {
		return convertAmount;
	}
	/**
	 * @param convertAmount the convertAmount to set
	 */
	public void setConvertAmount(BigDecimal convertAmount) {
		this.convertAmount = convertAmount;
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
	 * @return the accCode
	 */
	public String getAccCode() {
		return accCode;
	}
	/**
	 * @param accCode the accCode to set
	 */
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	/**
	 * @return the ccidCode
	 */
	public String getCcidCode() {
		return ccidCode;
	}
	/**
	 * @param ccidCode the ccidCode to set
	 */
	public void setCcidCode(String ccidCode) {
		this.ccidCode = ccidCode;
	}
	/**
	 * @return the ccidCombineList
	 */
	public List<HashMap<String, Object>> getCcidCombineList() {
		return ccidCombineList;
	}
	/**
	 * @param ccidCombineList the ccidCombineList to set
	 */
	public void setCcidCombineList(List<HashMap<String, Object>> ccidCombineList) {
		this.ccidCombineList = ccidCombineList;
	}
}
