package com.xzsoft.xc.ex.modal;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: PayDocBean 
 * @Description: 支付单
 * @author linp
 * @date 2016年3月17日 上午10:28:12 
 *
 */
public class PayDocBean {
	private String exPayId;			// 支付单ID
	private String ledgerId;		// 账簿ID
	private String exPayCode;		// 支付单编号
	private String exPayDesc;		// 付款说明
	private String depositBankId;	// 付款账号
	private Date exPayDate;			// 付款日期
	private float exPayTAmt;		// 本次支付金额
	private String vHeadId;			// 凭证头ID 
	private String vStatus;			// 凭证状态
	private String payStatus;		// 付款状态
	private String payMethod;		// 支付方式
	private String payCat ; 		// 支付单生成方式
	private String payType;			// 付款方式
	private String caId;			// 现金流量项目ID
	private String finUserId;		// 复核人(凭证制单人)
	private String finUserName ;	// 复核人名称
	private Date finDate;			// 复核日期
	private String verifierId;		// 审核人(凭证审核人)
	private Date verfyDate;			// 审核日期
	private String signatoryId;		// 签字人ID
	private String signatory;		// 签字人
	private Date signDate;			// 签字日期
	private Date creationDate;		// 创建日期
	private String createdBy;		// 创建人
	private Date lastUpdateDate;	// 最后更新日期
	private String lastUpdatedBy;	// 最后更新人
	
	private List<PayDocDtlBean> payDtl;	// 支付单明细
	
	private List<PayDocDtlBean>	addPayDtlBeans ;// 新增报销单
	private List<String> delPayDtlIds ;			// 删除报销单

	public String getExPayId() {
		return exPayId;
	}

	public void setExPayId(String exPayId) {
		this.exPayId = exPayId;
	}

	public String getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}

	public String getExPayCode() {
		return exPayCode;
	}

	public void setExPayCode(String exPayCode) {
		this.exPayCode = exPayCode;
	}

	public String getExPayDesc() {
		return exPayDesc;
	}

	public void setExPayDesc(String exPayDesc) {
		this.exPayDesc = exPayDesc;
	}

	public String getDepositBankId() {
		return depositBankId;
	}

	public void setDepositBankId(String depositBankId) {
		this.depositBankId = depositBankId;
	}

	public Date getExPayDate() {
		return exPayDate;
	}

	public void setExPayDate(Date exPayDate) {
		this.exPayDate = exPayDate;
	}

	public float getExPayTAmt() {
		return exPayTAmt;
	}

	public void setExPayTAmt(float exPayTAmt) {
		this.exPayTAmt = exPayTAmt;
	}

	public String getvHeadId() {
		return vHeadId;
	}

	public void setvHeadId(String vHeadId) {
		this.vHeadId = vHeadId;
	}

	public String getvStatus() {
		return vStatus;
	}

	public void setvStatus(String vStatus) {
		this.vStatus = vStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPayCat() {
		return payCat;
	}

	public void setPayCat(String payCat) {
		this.payCat = payCat;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getCaId() {
		return caId;
	}

	public void setCaId(String caId) {
		this.caId = caId;
	}

	public String getFinUserId() {
		return finUserId;
	}

	public void setFinUserId(String finUserId) {
		this.finUserId = finUserId;
	}

	public String getFinUserName() {
		return finUserName;
	}

	public void setFinUserName(String finUserName) {
		this.finUserName = finUserName;
	}

	public Date getFinDate() {
		return finDate;
	}

	public void setFinDate(Date finDate) {
		this.finDate = finDate;
	}

	public String getVerifierId() {
		return verifierId;
	}

	public void setVerifierId(String verifierId) {
		this.verifierId = verifierId;
	}

	public Date getVerfyDate() {
		return verfyDate;
	}

	public void setVerfyDate(Date verfyDate) {
		this.verfyDate = verfyDate;
	}

	public String getSignatoryId() {
		return signatoryId;
	}

	public void setSignatoryId(String signatoryId) {
		this.signatoryId = signatoryId;
	}

	public String getSignatory() {
		return signatory;
	}

	public void setSignatory(String signatory) {
		this.signatory = signatory;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
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

	public List<PayDocDtlBean> getPayDtl() {
		return payDtl;
	}

	public void setPayDtl(List<PayDocDtlBean> payDtl) {
		this.payDtl = payDtl;
	}

	public List<PayDocDtlBean> getAddPayDtlBeans() {
		return addPayDtlBeans;
	}

	public void setAddPayDtlBeans(List<PayDocDtlBean> addPayDtlBeans) {
		this.addPayDtlBeans = addPayDtlBeans;
	}

	public List<String> getDelPayDtlIds() {
		return delPayDtlIds;
	}

	public void setDelPayDtlIds(List<String> delPayDtlIds) {
		this.delPayDtlIds = delPayDtlIds;
	}

	@Override
	public String toString() {
		return "PayDocBean [exPayId=" + exPayId + ", ledgerId=" + ledgerId
				+ ", exPayCode=" + exPayCode + ", exPayDesc=" + exPayDesc
				+ ", depositBankId=" + depositBankId + ", exPayDate="
				+ exPayDate + ", exPayTAmt=" + exPayTAmt + ", vHeadId="
				+ vHeadId + ", vStatus=" + vStatus + ", payStatus=" + payStatus
				+ ", payType=" + payType + ", caId=" + caId + ", finUserId="
				+ finUserId + ", finDate=" + finDate + ", verifierId="
				+ verifierId + ", verfyDate=" + verfyDate + ", signatoryId="
				+ signatoryId + ", signatory=" + signatory + ", signDate="
				+ signDate + ", creationDate=" + creationDate + ", createdBy="
				+ createdBy + ", lastUpdateDate=" + lastUpdateDate
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", payDtl=" + payDtl
				+ "]";
	}

	public PayDocBean(String exPayId, String ledgerId, String exPayCode,
			String exPayDesc, String depositBankId, Date exPayDate,
			float exPayTAmt, String vHeadId, String vStatus, String payStatus,
			String payType, String caId, String finUserId, Date finDate,
			String verifierId, Date verfyDate, String signatoryId,
			String signatory, Date signDate, Date creationDate,
			String createdBy, Date lastUpdateDate, String lastUpdatedBy,
			List<PayDocDtlBean> payDtl) {
		super();
		this.exPayId = exPayId;
		this.ledgerId = ledgerId;
		this.exPayCode = exPayCode;
		this.exPayDesc = exPayDesc;
		this.depositBankId = depositBankId;
		this.exPayDate = exPayDate;
		this.exPayTAmt = exPayTAmt;
		this.vHeadId = vHeadId;
		this.vStatus = vStatus;
		this.payStatus = payStatus;
		this.payType = payType;
		this.caId = caId;
		this.finUserId = finUserId;
		this.finDate = finDate;
		this.verifierId = verifierId;
		this.verfyDate = verfyDate;
		this.signatoryId = signatoryId;
		this.signatory = signatory;
		this.signDate = signDate;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.payDtl = payDtl;
	}

	public PayDocBean() {
		super();
	}

}
