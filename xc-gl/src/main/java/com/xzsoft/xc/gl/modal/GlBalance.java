package com.xzsoft.xc.gl.modal;

import java.util.Date;

/**
* @Title: GlBalances.java
* @Package com.xzsoft.xc.gl.modal
* @Description: 混合科目表（用于总账期初余额迭代计算，各项的说明详见各属性的秒数）
* @author tangxl  
* @date 2016年1月20日 上午9:53:28
* @version V1.0
 */
public class GlBalance implements Cloneable{
    //账簿信息
	private String id;              //临时余额表主键
	private String ledgerId;        //账簿id
	private String accHrcyId;       //账簿科目体系id(账簿中ACC_HRCY_ID)
	private String standardCurrency;//账簿本位币种(如果有原币，该值与currencyCode<账簿科目币种>会不一致，否则为一致)
	private String ccid;            //账簿组合科目id
	private String ccidOld;         //账簿科目组合id（修改账簿科目组合前的科目组合id）
	private String periodCode;      //账簿会计期间
	//账簿会计科目信息
	private String accId;           //账簿会计科目Id
	private int isForeginCny;       //是否外币核算
	private int isForeginCnyOld;    //是否外币核算(修改科目组合前的外币核算)
	private String currencyCode;    //账簿科目币种
	private int isAmount;           //是否数量核算(根据该值决定是否录入数量)
	private String balanceDirection;//科目方向('1'--借方,'-1'--贷方)
	//==========================数据录入项====================================
	/*
	 *期初开启的账簿只有期初余额
	 *期中开启的账簿包含累借、累贷和期初余额
	 */
	//年初余额选项
	private double yearAmoutD;       //年初借方数量(AMT_Y_DR)
	private double yearAmoutC;       //年初贷方数量(AMT_Y_CR)
	private double yearBalanceD;     //年初借方余额Y_DR
	private double yearBalanceC;     //年初贷方余额Y_CR
	private double yearOBalanceD;    //年初原币借方余额Y_DR
	private double yearOBalanceC;    //年初原币贷方余额Y_CR
	//累计借方
	private double calBalanceD;      //累计借方金额(YTD_DR)
	private double calAmoutD;        //累计借方数量(AMT_YTD_DR)
	private double calOBalanceD;     //累计借方原币金额
	//累计贷方
	private double calBalanceC;      //累计贷方(YTD_CR)
	private double calAmoutC;        //累计贷方数量(AMT_YTD_CR)
	private double calOBalanceC;     //累计贷方原币金额
	//期初余额(期初是指期间开启的期初，实际上是上一期间的期末余额)
	private double periodBalanceD;  //期初借方余额(实际上是指上一期间的期末余额)PJTD_DR
	private double periodBalanceC;  //期初贷方余额(实际上是指上一期间的期末余额)PJTD_CR
	private double periodAmountD;   //数量期初借方余额(实际上为上一期间的期末余额)AMT_PJTD_DR
	private double periodAmountC;   //数量期贷借方余额(实际上为上一期间的期末余额)AMT_PJTD_CR
	private double periodOBalanceD; //期初原币借方余额(实际上为上一期间的期末余额)AMT_PJTD_DR
	private double periodOBalanceC; //期初原币借方余额(实际上为上一期间的期末余额)AMT_PJTD_CR
	//辅助信息
	private Date creationDate;   
	private Date lastUpdateDate;
	private String createdBy;
	private String lastUpdatedBy;
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getAccHrcyId() {
		return accHrcyId;
	}
	public void setAccHrcyId(String accHrcyId) {
		this.accHrcyId = accHrcyId;
	}
	public String getStandardCurrency() {
		return standardCurrency;
	}
	public void setStandardCurrency(String standardCurrency) {
		this.standardCurrency = standardCurrency;
	}
	public String getCcid() {
		return ccid;
	}
	public void setCcid(String ccid) {
		this.ccid = ccid;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
	}
	public int getIsForeginCny() {
		return isForeginCny;
	}
	public void setIsForeginCny(int isForeginCny) {
		this.isForeginCny = isForeginCny;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public int getIsAmount() {
		return isAmount;
	}
	public void setIsAmount(int isAmount) {
		this.isAmount = isAmount;
	}
	public String getBalanceDirection() {
		return balanceDirection;
	}
	public void setBalanceDirection(String balanceDirection) {
		this.balanceDirection = balanceDirection;
	}
	public double getYearAmoutD() {
		return yearAmoutD;
	}
	public void setYearAmoutD(double yearAmoutD) {
		this.yearAmoutD = yearAmoutD;
	}
	public double getYearAmoutC() {
		return yearAmoutC;
	}
	public void setYearAmoutC(double yearAmoutC) {
		this.yearAmoutC = yearAmoutC;
	}
	public double getYearBalanceD() {
		return yearBalanceD;
	}
	public void setYearBalanceD(double yearBalanceD) {
		this.yearBalanceD = yearBalanceD;
	}
	public double getYearBalanceC() {
		return yearBalanceC;
	}
	public void setYearBalanceC(double yearBalanceC) {
		this.yearBalanceC = yearBalanceC;
	}
	public double getYearOBalanceD() {
		return yearOBalanceD;
	}
	public void setYearOBalanceD(double yearOBalanceD) {
		this.yearOBalanceD = yearOBalanceD;
	}
	public double getYearOBalanceC() {
		return yearOBalanceC;
	}
	public void setYearOBalanceC(double yearOBalanceC) {
		this.yearOBalanceC = yearOBalanceC;
	}
	public double getCalBalanceD() {
		return calBalanceD;
	}
	public void setCalBalanceD(double calBalanceD) {
		this.calBalanceD = calBalanceD;
	}
	public double getCalAmoutD() {
		return calAmoutD;
	}
	public void setCalAmoutD(double calAmoutD) {
		this.calAmoutD = calAmoutD;
	}
	public double getCalOBalanceD() {
		return calOBalanceD;
	}
	public void setCalOBalanceD(double calOBalanceD) {
		this.calOBalanceD = calOBalanceD;
	}
	public double getCalBalanceC() {
		return calBalanceC;
	}
	public void setCalBalanceC(double calBalanceC) {
		this.calBalanceC = calBalanceC;
	}
	public double getCalAmoutC() {
		return calAmoutC;
	}
	public void setCalAmoutC(double calAmoutC) {
		this.calAmoutC = calAmoutC;
	}
	public double getCalOBalanceC() {
		return calOBalanceC;
	}
	public void setCalOBalanceC(double calOBalanceC) {
		this.calOBalanceC = calOBalanceC;
	}
	public double getPeriodBalanceD() {
		return periodBalanceD;
	}
	public void setPeriodBalanceD(double periodBalanceD) {
		this.periodBalanceD = periodBalanceD;
	}
	public double getPeriodBalanceC() {
		return periodBalanceC;
	}
	public void setPeriodBalanceC(double periodBalanceC) {
		this.periodBalanceC = periodBalanceC;
	}
	public double getPeriodAmountD() {
		return periodAmountD;
	}
	public void setPeriodAmountD(double periodAmountD) {
		this.periodAmountD = periodAmountD;
	}
	public double getPeriodAmountC() {
		return periodAmountC;
	}
	public void setPeriodAmountC(double periodAmountC) {
		this.periodAmountC = periodAmountC;
	}
	public double getPeriodOBalanceD() {
		return periodOBalanceD;
	}
	public void setPeriodOBalanceD(double periodOBalanceD) {
		this.periodOBalanceD = periodOBalanceD;
	}
	public double getPeriodOBalanceC() {
		return periodOBalanceC;
	}
	public void setPeriodOBalanceC(double periodOBalanceC) {
		this.periodOBalanceC = periodOBalanceC;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCcidOld() {
		return ccidOld;
	}
	public void setCcidOld(String ccidOld) {
		this.ccidOld = ccidOld;
	}
	public int getIsForeginCnyOld() {
		return isForeginCnyOld;
	}
	public void setIsForeginCnyOld(int isForeginCnyOld) {
		this.isForeginCnyOld = isForeginCnyOld;
	}
	//类的克隆方法
	@Override  
    public Object clone() throws CloneNotSupportedException  
    {  
        return super.clone();  
    }
}
