package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
 * @ClassName: Account 
 * @Description: 会计科目体系信息
 * @author linp
 * @date 2015年12月29日 下午6:52:09 
 *
 */
public class Account {

	private String accId ;
	private String accCode ;
	private String accName ;
	private String upAccCode ;
	private String upAccId ;
	private String accCategoryCode ;
	private String accGroupId ;
	private String accHrcyId ;
	private int balanceDirection ;
	private String isBankAcc ;
	private String isCashAcc ;
	private String isCashItem ;
	private Date startDate ;
	private Date endDate ;
	private String isLeaf ;
	private int accLevel;
	
	private String ledgerId ;		// 账簿ID
	private String ledgerCode;      // 账簿编码
	private String isForeignCny ;	// 是否外币核算
	private String defaultCny ;		// 默认币种
	private String isAmount ;		// 是否数量核算
	private String dimId ;			// 计量单位
	private String bgItemId ;		// 预算项目
	
	private Date creationDate ;
	private String createdBy ;
	private Date lastUpdateDate ;
	private String lastUpdatedBy ;
	private Date maxUpdateDate;
	
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getAccCode() {
		return accCode;
	}
	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getUpAccCode() {
		return upAccCode;
	}
	public void setUpAccCode(String upAccCode) {
		this.upAccCode = upAccCode;
	}
	public String getUpAccId() {
		return upAccId;
	}
	public void setUpAccId(String upAccId) {
		this.upAccId = upAccId;
	}
	public String getAccCategoryCode() {
		return accCategoryCode;
	}
	public void setAccCategoryCode(String accCategoryCode) {
		this.accCategoryCode = accCategoryCode;
	}
	public String getAccGroupId() {
		return accGroupId;
	}
	public void setAccGroupId(String accGroupId) {
		this.accGroupId = accGroupId;
	}
	public String getAccHrcyId() {
		return accHrcyId;
	}
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	public int getBalanceDirection() {
		return balanceDirection;
	}
	public void setBalanceDirection(int balanceDirection) {
		this.balanceDirection = balanceDirection;
	}
	public String getIsBankAcc() {
		return isBankAcc;
	}
	public void setIsBankAcc(String isBankAcc) {
		this.isBankAcc = isBankAcc;
	}
	public String getIsCashAcc() {
		return isCashAcc;
	}
	public void setIsCashAcc(String isCashAcc) {
		this.isCashAcc = isCashAcc;
	}
	public String getIsCashItem() {
		return isCashItem;
	}
	public void setIsCashItem(String isCashItem) {
		this.isCashItem = isCashItem;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public int getAccLevel() {
		return accLevel;
	}
	public void setAccLevel(int accLevel) {
		this.accLevel = accLevel;
	}
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
	public String getIsForeignCny() {
		return isForeignCny;
	}
	public void setIsForeignCny(String isForeignCny) {
		this.isForeignCny = isForeignCny;
	}
	public String getDefaultCny() {
		return defaultCny;
	}
	public void setDefaultCny(String defaultCny) {
		this.defaultCny = defaultCny;
	}
	public String getIsAmount() {
		return isAmount;
	}
	public void setIsAmount(String isAmount) {
		this.isAmount = isAmount;
	}
	public String getDimId() {
		return dimId;
	}
	public void setDimId(String dimId) {
		this.dimId = dimId;
	}
	public String getBgItemId() {
		return bgItemId;
	}
	public void setBgItemId(String bgItemId) {
		this.bgItemId = bgItemId;
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
