package com.xzsoft.xc.gl.modal;

import java.util.Date;

public class BalanceForExcel {
	//主键
	private String id;
	//账簿期间
	private String periodCode;
	//操作者sessionId
	private String sessionId;
	//错误信息
	private String errorMsg;
	//科目id
	private String accId;
	//11个科目段
	private String segment01;
	private String segment01Name;
	private String segment02;
	private String segment02Name;
	private String segment03;
	private String segment03Name;
	private String segment04;
	private String segment04Name;
	private String segment05;
	private String segment05Name;
	private String segment06;
	private String segment06Name;
	private String segment07;
	private String segment07Name;
	private String segment08;
	private String segment08Name;
	private String segment09;
	private String segment09Name;
	private String segment10;
	private String segment10Name;
	private String segment11;
	private String segment11Name;
	private String segment12;
	private String segment12Name;
	//账簿信息
	private String ledgerId;
	private String currencyCode;
	private String enterCnyCode; //原币币种
	//科目组合及科目信息
	private String ccid;
	private String ccCode;
	private String actualCode;
	//年初余额、数目、原币信息
	private double yearDBalance;
	private double yearCBalance;
	private double yearAmount;
	private double yearDBalanceO;
	private double yearCBalanceO;
	//借方余额、数量、原币信息
	private double debitBalance;
	private double debitAmount;
	private double debitBalanceO;
	//贷方余额、数量、原币信息
	private double crebitBalance;
	private double crebitAmount;
	private double crebitBalanceO;
	//期末余额、数量、原币信息
	private double periodDBalance;
	private double periodCBalance;
	private double periodAmount;
	private double periodDBalanceO;
	private double periodCBalanceO;
	//期初金额、数量、原币
	private double initDBalance;
	private double initCBalance;
	private double initDAmount;
	private double initCAmount;
	private double initDOBalance;
	private double initCOBalance;
	//其他信息
	private String isValid;
	private Date creationDate;   
	private Date lastUpdateDate;
	private String createdBy;
	private String lastUpdatedBy;
	private String accCategoryName;
	private String sumBalancesFlag;
	private String balanceDirection;
	
	/**
	 * @return the initDBalance
	 */
	public double getInitDBalance() {
		return initDBalance;
	}
	/**
	 * @param initDBalance the initDBalance to set
	 */
	public void setInitDBalance(double initDBalance) {
		this.initDBalance = initDBalance;
	}
	/**
	 * @return the initCBalance
	 */
	public double getInitCBalance() {
		return initCBalance;
	}
	/**
	 * @param initCBalance the initCBalance to set
	 */
	public void setInitCBalance(double initCBalance) {
		this.initCBalance = initCBalance;
	}
	/**
	 * @return the initDAmount
	 */
	public double getInitDAmount() {
		return initDAmount;
	}
	/**
	 * @param initDAmount the initDAmount to set
	 */
	public void setInitDAmount(double initDAmount) {
		this.initDAmount = initDAmount;
	}
	/**
	 * @return the initCAmount
	 */
	public double getInitCAmount() {
		return initCAmount;
	}
	/**
	 * @param initCAmount the initCAmount to set
	 */
	public void setInitCAmount(double initCAmount) {
		this.initCAmount = initCAmount;
	}
	/**
	 * @return the initDOBalance
	 */
	public double getInitDOBalance() {
		return initDOBalance;
	}
	/**
	 * @param initDOBalance the initDOBalance to set
	 */
	public void setInitDOBalance(double initDOBalance) {
		this.initDOBalance = initDOBalance;
	}
	/**
	 * @return the initCOBalance
	 */
	public double getInitCOBalance() {
		return initCOBalance;
	}
	/**
	 * @param initCOBalance the initCOBalance to set
	 */
	public void setInitCOBalance(double initCOBalance) {
		this.initCOBalance = initCOBalance;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSegment01() {
		return segment01;
	}
	public void setSegment01(String segment01) {
		this.segment01 = segment01;
	}
	public String getSegment02() {
		return segment02;
	}
	public void setSegment02(String segment02) {
		this.segment02 = segment02;
	}
	public String getSegment03() {
		return segment03;
	}
	public void setSegment03(String segment03) {
		this.segment03 = segment03;
	}
	public String getSegment04() {
		return segment04;
	}
	public void setSegment04(String segment04) {
		this.segment04 = segment04;
	}
	public String getSegment05() {
		return segment05;
	}
	public void setSegment05(String segment05) {
		this.segment05 = segment05;
	}
	public String getSegment06() {
		return segment06;
	}
	public void setSegment06(String segment06) {
		this.segment06 = segment06;
	}
	public String getSegment07() {
		return segment07;
	}
	public void setSegment07(String segment07) {
		this.segment07 = segment07;
	}
	public String getSegment08() {
		return segment08;
	}
	public void setSegment08(String segment08) {
		this.segment08 = segment08;
	}
	public String getSegment09() {
		return segment09;
	}
	public void setSegment09(String segment09) {
		this.segment09 = segment09;
	}
	public String getSegment10() {
		return segment10;
	}
	public void setSegment10(String segment10) {
		this.segment10 = segment10;
	}
	public String getSegment11() {
		return segment11;
	}
	public void setSegment11(String segment11) {
		this.segment11 = segment11;
	}
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCcid() {
		return ccid;
	}
	public void setCcid(String ccid) {
		this.ccid = ccid;
	}
	public String getCcCode() {
		return ccCode;
	}
	public void setCcCode(String ccCode) {
		this.ccCode = ccCode;
	}
	public String getActualCode() {
		return actualCode;
	}
	public void setActualCode(String actualCode) {
		this.actualCode = actualCode;
	}
	public double getYearDBalance() {
		return yearDBalance;
	}
	public void setYearDBalance(double yearDBalance) {
		this.yearDBalance = yearDBalance;
	}
	public double getYearCBalance() {
		return yearCBalance;
	}
	public void setYearCBalance(double yearCBalance) {
		this.yearCBalance = yearCBalance;
	}
	public double getYearAmount() {
		return yearAmount;
	}
	public void setYearAmount(double yearAmount) {
		this.yearAmount = yearAmount;
	}
	public double getYearDBalanceO() {
		return yearDBalanceO;
	}
	public void setYearDBalanceO(double yearDBalanceO) {
		this.yearDBalanceO = yearDBalanceO;
	}
	public double getYearCBalanceO() {
		return yearCBalanceO;
	}
	public void setYearCBalanceO(double yearCBalanceO) {
		this.yearCBalanceO = yearCBalanceO;
	}
	public double getDebitBalance() {
		return debitBalance;
	}
	public void setDebitBalance(double debitBalance) {
		this.debitBalance = debitBalance;
	}
	public double getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(double debitAmount) {
		this.debitAmount = debitAmount;
	}
	public double getDebitBalanceO() {
		return debitBalanceO;
	}
	public void setDebitBalanceO(double debitBalanceO) {
		this.debitBalanceO = debitBalanceO;
	}
	public double getCrebitBalance() {
		return crebitBalance;
	}
	public void setCrebitBalance(double crebitBalance) {
		this.crebitBalance = crebitBalance;
	}
	public double getCrebitAmount() {
		return crebitAmount;
	}
	public void setCrebitAmount(double crebitAmount) {
		this.crebitAmount = crebitAmount;
	}
	public double getCrebitBalanceO() {
		return crebitBalanceO;
	}
	public void setCrebitBalanceO(double crebitBalanceO) {
		this.crebitBalanceO = crebitBalanceO;
	}
	public double getPeriodDBalance() {
		return periodDBalance;
	}
	public void setPeriodDBalance(double periodDBalance) {
		this.periodDBalance = periodDBalance;
	}
	public double getPeriodCBalance() {
		return periodCBalance;
	}
	public void setPeriodCBalance(double periodCBalance) {
		this.periodCBalance = periodCBalance;
	}
	public double getPeriodAmount() {
		return periodAmount;
	}
	public void setPeriodAmount(double periodAmount) {
		this.periodAmount = periodAmount;
	}
	public double getPeriodDBalanceO() {
		return periodDBalanceO;
	}
	public void setPeriodDBalanceO(double periodDBalanceO) {
		this.periodDBalanceO = periodDBalanceO;
	}
	public double getPeriodCBalanceO() {
		return periodCBalanceO;
	}
	public void setPeriodCBalanceO(double periodCBalanceO) {
		this.periodCBalanceO = periodCBalanceO;
	}
	public String getSegment12() {
		return segment12;
	}
	public void setSegment12(String segment12) {
		this.segment12 = segment12;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public String getEnterCnyCode() {
		return enterCnyCode;
	}
	public void setEnterCnyCode(String enterCnyCode) {
		this.enterCnyCode = enterCnyCode;
	}
	public String getSegment01Name() {
		return segment01Name;
	}
	public void setSegment01Name(String segment01Name) {
		this.segment01Name = segment01Name;
	}
	public String getSegment02Name() {
		return segment02Name;
	}
	public void setSegment02Name(String segment02Name) {
		this.segment02Name = segment02Name;
	}
	public String getSegment03Name() {
		return segment03Name;
	}
	public void setSegment03Name(String segment03Name) {
		this.segment03Name = segment03Name;
	}
	public String getSegment04Name() {
		return segment04Name;
	}
	public void setSegment04Name(String segment04Name) {
		this.segment04Name = segment04Name;
	}
	public String getSegment05Name() {
		return segment05Name;
	}
	public void setSegment05Name(String segment05Name) {
		this.segment05Name = segment05Name;
	}
	public String getSegment06Name() {
		return segment06Name;
	}
	public void setSegment06Name(String segment06Name) {
		this.segment06Name = segment06Name;
	}
	public String getSegment07Name() {
		return segment07Name;
	}
	public void setSegment07Name(String segment07Name) {
		this.segment07Name = segment07Name;
	}
	public String getSegment08Name() {
		return segment08Name;
	}
	public void setSegment08Name(String segment08Name) {
		this.segment08Name = segment08Name;
	}
	public String getSegment09Name() {
		return segment09Name;
	}
	public void setSegment09Name(String segment09Name) {
		this.segment09Name = segment09Name;
	}
	public String getSegment10Name() {
		return segment10Name;
	}
	public void setSegment10Name(String segment10Name) {
		this.segment10Name = segment10Name;
	}
	public String getSegment11Name() {
		return segment11Name;
	}
	public void setSegment11Name(String segment11Name) {
		this.segment11Name = segment11Name;
	}
	public String getSegment12Name() {
		return segment12Name;
	}
	public void setSegment12Name(String segment12Name) {
		this.segment12Name = segment12Name;
	}
	public String getAccCategoryName() {
		return accCategoryName;
	}
	public void setAccCategoryName(String accCategoryName) {
		this.accCategoryName = accCategoryName;
	}
	public String getSumBalancesFlag() {
		return sumBalancesFlag;
	}
	public void setSumBalancesFlag(String sumBalancesFlag) {
		this.sumBalancesFlag = sumBalancesFlag;
	}
	public String getBalanceDirection() {
		return balanceDirection;
	}
	public void setBalanceDirection(String balanceDirection) {
		this.balanceDirection = balanceDirection;
	}

}
