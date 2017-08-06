package com.xzsoft.xc.ap.modal;
import java.util.Date;
/**
 * 
  * @ClassName: ApPayBankModeBean
  * @Description: 账簿开户行
  * @author 任建建
  * @date 2016年4月11日 下午1:25:25
 */
public class ApPayBankModeBean {

	private String depositBankId;	//开户ID
	private String ledgerId;		//账簿ID
	private String accId;			//科目ID
	private String cnyCode;			//币种
	private String accountName;		//户名
	private String bankName;		//银行名称
	private String depositBankName;	//开户行名称
	private String bankAccount;		//银行账号
	private String correspondent;	//联行号
	private Date startDate;			//开始日期
	private Date endDate;			//结束日期
	private String depositBankDesc;	//描述
	private Date creationDate;		//创建日期
	private String createdBy;		//申请人
	private Date lastUpdateDate;	//最后更新日期
	private String lastUpdatedBy;	//最后更新人
	public String getDepositBankId() {
		return depositBankId;
	}
	public void setDepositBankId(String depositBankId) {
		this.depositBankId = depositBankId;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getCnyCode() {
		return cnyCode;
	}
	public void setCnyCode(String cnyCode) {
		this.cnyCode = cnyCode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getDepositBankName() {
		return depositBankName;
	}
	public void setDepositBankName(String depositBankName) {
		this.depositBankName = depositBankName;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getCorrespondent() {
		return correspondent;
	}
	public void setCorrespondent(String correspondent) {
		this.correspondent = correspondent;
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
	public String getDepositBankDesc() {
		return depositBankDesc;
	}
	public void setDepositBankDesc(String depositBankDesc) {
		this.depositBankDesc = depositBankDesc;
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