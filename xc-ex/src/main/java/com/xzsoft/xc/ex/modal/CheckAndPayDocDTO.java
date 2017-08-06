package com.xzsoft.xc.ex.modal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @ClassName: CheckAndPayDocDTO
 * @Description: 单据复核、支付和入账数据处理
 * @author linp
 * @date 2016年3月25日 下午2:25:20
 *
 */
public class CheckAndPayDocDTO {
	
	private String ledgerId; 		// 账簿ID
	private Date accDate; 			// 凭证会计日期、支付日期
	private String exPayDesc ;		// 付款说明
	private String payMethod; 		// 支付方式：1-立即支付,2-挂账支付
	private String payCat ; 		// 支付单生成方式 ： new-手工创建,auto-自动生成
	private String payType; 		// 付款方式
	private String bankId; 			// 付款银行账号
	private String cashId; 			// 现金流量项目ID
	private String exCatCode ;		// 单据类型
	private String exDocId ;		// 待复核单据ID
	private List<String> exDocIds; 	// 待复核单据列表
	private float cancelAmt; 		// 核销金额
	private float payAmt; 			// 本次支付金额、支付总额
	private float amount ; 			// 单据金额
	private String opType ;			// 操作模式：check-复核,payment-支付
	private String payId ;
	private String payCode ;
	private String headId ;
	private HashMap<String,Object> amtMap ; // 报销单与支付金额

	
	public HashMap<String, Object> getAmtMap() {
		return amtMap;
	}

	public void setAmtMap(HashMap<String, Object> amtMap) {
		this.amtMap = amtMap;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getHeadId() {
		return headId;
	}

	public void setHeadId(String headId) {
		this.headId = headId;
	}

	public String getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}

	public Date getAccDate() {
		return accDate;
	}

	public void setAccDate(Date accDate) {
		this.accDate = accDate;
	}

	public String getExPayDesc() {
		return exPayDesc;
	}

	public void setExPayDesc(String exPayDesc) {
		this.exPayDesc = exPayDesc;
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

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getCashId() {
		return cashId;
	}

	public void setCashId(String cashId) {
		this.cashId = cashId;
	}

	public String getExCatCode() {
		return exCatCode;
	}

	public void setExCatCode(String exCatCode) {
		this.exCatCode = exCatCode;
	}

	public String getExDocId() {
		return exDocId;
	}

	public void setExDocId(String exDocId) {
		this.exDocId = exDocId;
	}

	public List<String> getExDocIds() {
		return exDocIds;
	}

	public void setExDocIds(List<String> exDocIds) {
		this.exDocIds = exDocIds;
	}

	public float getCancelAmt() {
		return cancelAmt;
	}

	public void setCancelAmt(float cancelAmt) {
		this.cancelAmt = cancelAmt;
	}

	public float getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(float payAmt) {
		this.payAmt = payAmt;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

}
