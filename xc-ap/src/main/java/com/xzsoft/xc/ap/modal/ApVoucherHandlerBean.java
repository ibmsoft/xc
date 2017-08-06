package com.xzsoft.xc.ap.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName: ApVoucherHandlerBean
  * @Description: 应付模块（应付单、付款申请单、付款单）凭证处理的实体类
  * @author 任建建
  * @date 2016年4月7日 上午10:27:56
 */
public class ApVoucherHandlerBean {
	private String ledgerId; 		//账簿ID
	private String apId;			//单据ID
	private String apCode;			//单据号
	private String apCatCode;		//单据类型
	private int attchTotal;			//附件张数
	private Date accDate; 			//凭证会计日期、入账日期
	private String summary;			//摘要
	private List<String> apDtlIds; 	//单据明细ID
	
	private String tableName;		//操作的表
	private String priKey;			//操作的主键ID
	private String headId;			//凭证头ID
	private String vStatus;			//凭证状态
	private String verifierId;		//审核人
	private Date verifyDate;		//审核日期
	private String signUserId;		//原始单据签收人
	private Date signDate;			//签收日期
	private String signStatus;		//签收状态
	private Date lastUpdateDate;	//最后更新日期
	private String lastUpdatedBy;	//最后更新人
	private double AMOUNT;
	
	public String getPriKey() {
		return priKey;
	}
	public String getSignUserId() {
		return signUserId;
	}
	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}
	public Date getSignDate() {
		return signDate;
	}
	public String getvStatus() {
		return vStatus;
	}
	public void setvStatus(String vStatus) {
		this.vStatus = vStatus;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}
	public String getVerifierId() {
		return verifierId;
	}
	public void setVerifierId(String verifierId) {
		this.verifierId = verifierId;
	}
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
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
	public ApVoucherHandlerBean() {
		super();
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getApId() {
		return apId;
	}
	public void setApId(String apId) {
		this.apId = apId;
	}
	public String getApCode() {
		return apCode;
	}
	public void setApCode(String apCode) {
		this.apCode = apCode;
	}
	public String getApCatCode() {
		return apCatCode;
	}
	public void setApCatCode(String apCatCode) {
		this.apCatCode = apCatCode;
	}
	public int getAttchTotal() {
		return attchTotal;
	}
	public void setAttchTotal(int attchTotal) {
		this.attchTotal = attchTotal;
	}
	public Date getAccDate() {
		return accDate;
	}
	public void setAccDate(Date accDate) {
		this.accDate = accDate;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public List<String> getApDtlIds() {
		return apDtlIds;
	}
	public void setApDtlIds(List<String> apDtlIds) {
		this.apDtlIds = apDtlIds;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double amount) {
		AMOUNT = amount;
	}
}
