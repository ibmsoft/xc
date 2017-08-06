package com.xzsoft.xc.ex.modal;

import java.util.Date;

/**
 * @ClassName: PayDocDtlBean 
 * @Description: 支付单明细
 * @author linp
 * @date 2016年3月17日 上午10:27:39 
 *
 */
public class PayDocDtlBean {
	private String exPayDtlId;
	private String exPayId;
	private String exDocId;
	private double payAmt;
	private Date creationDate;
	private String createdBy;
	private Date lastUpdateDate;
	private String lastUpdatedBy;
	
	public PayDocDtlBean() {
		super();
	}
	public PayDocDtlBean(String exPayDtlId, String exPayId, String exDocId,
			float payAmt, Date creationDate, String createdBy,
			Date lastUpdateDate, String lastUpdatedBy) {
		super();
		this.exPayDtlId = exPayDtlId;
		this.exPayId = exPayId;
		this.exDocId = exDocId;
		this.payAmt = payAmt;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
	}
	@Override
	public String toString() {
		return "PayDocDtlBean [exPayDtlId=" + exPayDtlId + ", exPayId="
				+ exPayId + ", exDocId=" + exDocId + ", payAmt=" + payAmt
				+ ", creationDate=" + creationDate + ", createdBy=" + createdBy
				+ ", lastUpdateDate=" + lastUpdateDate + ", lastUpdatedBy="
				+ lastUpdatedBy + "]";
	}
	public String getExPayDtlId() {
		return exPayDtlId;
	}
	public void setExPayDtlId(String exPayDtlId) {
		this.exPayDtlId = exPayDtlId;
	}
	public String getExPayId() {
		return exPayId;
	}
	public void setExPayId(String exPayId) {
		this.exPayId = exPayId;
	}
	public String getExDocId() {
		return exDocId;
	}
	public void setExDocId(String exDocId) {
		this.exDocId = exDocId;
	}
	public double getPayAmt() {
		return payAmt;
	}
	public void setPayAmt(double payAmt) {
		this.payAmt = payAmt;
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
