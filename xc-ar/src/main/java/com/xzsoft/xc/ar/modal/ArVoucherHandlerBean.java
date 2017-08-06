package com.xzsoft.xc.ar.modal;

import java.util.Date;
import java.util.List;

/**
 * 
  * @ClassName：ArVoucherHandlerBean
  * @Description：应收模块（应收单、收款单、核销单、余额调整单）凭证处理的实体类
  * @author：任建建
  * @date：2016年7月8日 下午3:08:31
 */
public class ArVoucherHandlerBean {
	private String ledgerId; 		//账簿ID
	private String arId;			//单据ID
	private String arCode;			//单据号
	private String arCatCode;		//单据类型
	private int attchTotal;			//附件张数
	private Date accDate; 			//凭证会计日期、入账日期
	private String summary;			//摘要
	private List<String> arDtlIds; 	//单据明细ID
	
	private String tableName;		//操作的表
	private String priKey;			//操作的主键ID
	private String headId;			//凭证头ID
	private String vStatus;			//凭证状态
	private String verifierId;		//审核人
	private Date verifyDate;		//审核日期
	private String signUserId;		//签字人ID
	private Date signDate;			//签字日期
	private String signatory;		//签字人
	private Date lastUpdateDate;	//最后更新日期
	private String lastUpdatedBy;	//最后更新人
	public ArVoucherHandlerBean() {
		super();
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getArId() {
		return arId;
	}
	public void setArId(String arId) {
		this.arId = arId;
	}
	public String getArCode() {
		return arCode;
	}
	public void setArCode(String arCode) {
		this.arCode = arCode;
	}
	public String getArCatCode() {
		return arCatCode;
	}
	public void setArCatCode(String arCatCode) {
		this.arCatCode = arCatCode;
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
	public List<String> getArDtlIds() {
		return arDtlIds;
	}
	public void setArDtlIds(List<String> arDtlIds) {
		this.arDtlIds = arDtlIds;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPriKey() {
		return priKey;
	}
	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}
	public String getHeadId() {
		return headId;
	}
	public void setHeadId(String headId) {
		this.headId = headId;
	}
	public String getvStatus() {
		return vStatus;
	}
	public void setvStatus(String vStatus) {
		this.vStatus = vStatus;
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
	public String getSignUserId() {
		return signUserId;
	}
	public void setSignUserId(String signUserId) {
		this.signUserId = signUserId;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getSignatory() {
		return signatory;
	}
	public void setSignatory(String signatory) {
		this.signatory = signatory;
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
