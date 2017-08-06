package com.xzsoft.xc.ex.modal;

import java.util.Date;

/**
 * @ClassName: IAFactBean 
 * @Description: 备用金明细表
 * @author linp
 * @date 2016年3月10日 下午6:42:01 
 *
 */
public class IAFactBean {

	private String iaFactId;		// 备用金明细ID
	private String ledgerId;		// 账簿ID
	private String exUserId;		// 备用金所属人
	private String docId;			// 单据ID
	private String exIAType;		// 备用金类型：1-核销款，2-借款，3-还款
	private String operationType;   // 操作类型:1-查询备用金 ,2-新增操作,3-修改操作,4-删除操作
	private double IAFactAmt;		// 单据金额
	private double IABal ;			// 备用金余额
	private Date creationDate ;
	private String createdBy;
	private Date lastUpdateDate;
	private String lastUpdatedBy ;
	private int orderBy;

	
	
	public String getIaFactId() {
		return iaFactId;
	}
	public void setIaFactId(String iaFactId) {
		this.iaFactId = iaFactId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getExUserId() {
		return exUserId;
	}
	public void setExUserId(String exUserId) {
		this.exUserId = exUserId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getExIAType() {
		return exIAType;
	}
	public void setExIAType(String exIAType) {
		this.exIAType = exIAType;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public double getIAFactAmt() {
		return IAFactAmt;
	}
	public void setIAFactAmt(double iAFactAmt) {
		IAFactAmt = iAFactAmt;
	}
	public double getIABal() {
		return IABal;
	}
	public void setIABal(double iABal) {
		IABal = iABal;
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
	public int getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}
}
