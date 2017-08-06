package com.xzsoft.xc.util.modal;

import java.util.Date;

/**
 * @ClassName: Ledger 
 * @Description: 账簿信息
 * @author linp
 * @date 2015年12月30日 上午11:45:15 
 *
 */
public class Ledger {
	private String ledgerId ;
	private String ledgerCode ;
	private String ledgerName ;
	private String accHrcyId ;
	private String startPeriodCode ;
	private String cnyCode ;
	private String orgId ;
	private String orgCode ;
	private String retainedAccId ;	// 留存收益科目
	private String exchangeAccId ;	// 汇兑损益科目
	private String ledgerDesc ;		// 账簿描述
	
	private String isValid ;		// 是否生效:Y-是，N-否
	private String bgHrcyId ;		// 预算体系ID
	private String bgIsChk ;		// 预算检查：Y-是，N-否
	private String bgExPeriod ;		// 费用预算控制周期：1-按年控制，2-按月累计控制
	private String bgIsSpecial ;	// 是否启用专项预算：Y-是，N-否
	private String bgIsCash ;		// 是否启用现金预算：Y-是，N-否
	private String exHrcyId ;		// 费用项目体系ID
	private String exIaAccId ;		// 备用金科目(末级科目)
	private String exCrAccId ;		// 挂账科目(末级科目)
	private String isCrPay ;		// 是否允许挂账支付：Y-是，N-否
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	private Date maxUpdateDate ;     //同步日期
	
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getLedgerCode() {
		return ledgerCode;
	}
	public void setLedgerCode(String ledgerCode) {
		this.ledgerCode = ledgerCode;
	}
	public String getLedgerName() {
		return ledgerName;
	}
	public void setLedgerName(String ledgerName) {
		this.ledgerName = ledgerName;
	}
	public String getAccHrcyId() {
		return accHrcyId;
	}
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	public String getStartPeriodCode() {
		return startPeriodCode;
	}
	public void setStartPeriodCode(String startPeriodCode) {
		this.startPeriodCode = startPeriodCode;
	}
	public String getCnyCode() {
		return cnyCode;
	}
	public void setCnyCode(String cnyCode) {
		this.cnyCode = cnyCode;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getRetainedAccId() {
		return retainedAccId;
	}
	public void setRetainedAccId(String retainedAccId) {
		this.retainedAccId = retainedAccId;
	}
	public String getExchangeAccId() {
		return exchangeAccId;
	}
	public void setExchangeAccId(String exchangeAccId) {
		this.exchangeAccId = exchangeAccId;
	}
	public String getLedgerDesc() {
		return ledgerDesc;
	}
	public void setLedgerDesc(String ledgerDesc) {
		this.ledgerDesc = ledgerDesc;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getBgHrcyId() {
		return bgHrcyId;
	}
	public void setBgHrcyId(String bgHrcyId) {
		this.bgHrcyId = bgHrcyId;
	}
	public String getBgIsChk() {
		return bgIsChk;
	}
	public void setBgIsChk(String bgIsChk) {
		this.bgIsChk = bgIsChk;
	}
	public String getBgExPeriod() {
		return bgExPeriod;
	}
	public void setBgExPeriod(String bgExPeriod) {
		this.bgExPeriod = bgExPeriod;
	}
	public String getBgIsSpecial() {
		return bgIsSpecial;
	}
	public void setBgIsSpecial(String bgIsSpecial) {
		this.bgIsSpecial = bgIsSpecial;
	}
	public String getBgIsCash() {
		return bgIsCash;
	}
	public void setBgIsCash(String bgIsCash) {
		this.bgIsCash = bgIsCash;
	}
	public String getExHrcyId() {
		return exHrcyId;
	}
	public void setExHrcyId(String exHrcyId) {
		this.exHrcyId = exHrcyId;
	}
	public String getExIaAccId() {
		return exIaAccId;
	}
	public void setExIaAccId(String exIaAccId) {
		this.exIaAccId = exIaAccId;
	}
	public String getExCrAccId() {
		return exCrAccId;
	}
	public void setExCrAccId(String exCrAccId) {
		this.exCrAccId = exCrAccId;
	}
	public String getIsCrPay() {
		return isCrPay;
	}
	public void setIsCrPay(String isCrPay) {
		this.isCrPay = isCrPay;
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
	public Date getMaxUpdateDate() {
		return maxUpdateDate;
	}
	public void setMaxUpdateDate(Date maxUpdateDate) {
		this.maxUpdateDate = maxUpdateDate;
	}
	
}
