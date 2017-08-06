package com.xzsoft.xc.gl.report.modal;

import java.util.Date;

public class VDataReport {
    
	private String DIMENSION_NAME;//数量科目组合名称
	private String V_HEAD_ID;//凭证头ID
	private String LEDGER_NAME;//账簿名称
	private String ORG_NAME;//组织名称
	private String V_ATTCH_TOTAL;//附件数
	private String V_SERIAL_NUM;//凭证号
	private Date CREATION_DATE;//制单日期
	private String SUMMARY;//摘要
	private String CCID_NAME;//科目组合完整名称(要配上/币种)
	private Double ACCOUNT_DR;//折后借方
	private Double ACCOUNT_CR;//折后贷方
	private String CREATED_NAME;//制单人
	private String VERIFIER;//审核人
	private String SIGNATORY;//签字人
	private String BOOKKEEPER;//记账人
	private double AMOUNT;    //数量
	private double ENTER_DR;  //原币借方
	private double ENTER_CR;  //原币贷方
	private double EXCHANGE_RATE;
	private double T_ACCOUNT_DR;
	private double T_ACCOUNT_CR;
	private double T_ENTER_DR;  
	private double T_ENTER_CR;  
	private double T_AMOUNT;
	private double ENTER_AMOUNT;  //原币金额
	private double T_ENTER_AMOUNT;  //原币金额
	private String BALANCE_DIRECTION;
	private double sumDebit;       //合计借方
	private double sumCrebit;      //合计贷方
	private double sumTDebit;      //合计借方<未过账>
	private double sumTCrebit;     //合计贷方<未过账>
	private String isShow;
	private String FOREIGN_NAME;  //外币凭证显示的科目组合名称
	private String V_CATEGORY_NAME;//凭证类别
	public String getV_HEAD_ID() {
		return V_HEAD_ID;
	}
	public void setV_HEAD_ID(String v_HEAD_ID) {
		V_HEAD_ID = v_HEAD_ID;
	}
	public String getLEDGER_NAME() {
		return LEDGER_NAME;
	}
	public void setLEDGER_NAME(String lEDGER_NAME) {
		LEDGER_NAME = lEDGER_NAME;
	}
	public String getV_ATTCH_TOTAL() {
		return V_ATTCH_TOTAL;
	}
	public void setV_ATTCH_TOTAL(String v_ATTCH_TOTAL) {
		V_ATTCH_TOTAL = v_ATTCH_TOTAL;
	}
	public String getV_SERIAL_NUM() {
		return V_SERIAL_NUM;
	}
	public void setV_SERIAL_NUM(String v_SERIAL_NUM) {
		V_SERIAL_NUM = v_SERIAL_NUM;
	}
	public Date getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Date cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public String getSUMMARY() {
		return SUMMARY;
	}
	public void setSUMMARY(String sUMMARY) {
		SUMMARY = sUMMARY;
	}
	public String getCCID_NAME() {
		return CCID_NAME;
	}
	public void setCCID_NAME(String cCID_NAME) {
		CCID_NAME = cCID_NAME;
	}
	public Double getACCOUNT_DR() {
		return ACCOUNT_DR;
	}
	public void setACCOUNT_DR(Double aCCOUNT_DR) {
		ACCOUNT_DR = aCCOUNT_DR;
	}
	public Double getACCOUNT_CR() {
		return ACCOUNT_CR;
	}
	public void setACCOUNT_CR(Double aCCOUNT_CR) {
		ACCOUNT_CR = aCCOUNT_CR;
	}
	public String getCREATED_NAME() {
		return CREATED_NAME;
	}
	public void setCREATED_NAME(String cREATED_NAME) {
		CREATED_NAME = cREATED_NAME;
	}
	public String getVERIFIER() {
		return VERIFIER;
	}
	public void setVERIFIER(String vERIFIER) {
		VERIFIER = vERIFIER;
	}
	public String getSIGNATORY() {
		return SIGNATORY;
	}
	public void setSIGNATORY(String sIGNATORY) {
		SIGNATORY = sIGNATORY;
	}
	public String getBOOKKEEPER() {
		return BOOKKEEPER;
	}
	public void setBOOKKEEPER(String bOOKKEEPER) {
		BOOKKEEPER = bOOKKEEPER;
	}
	public String getORG_NAME() {
		return ORG_NAME;
	}
	public void setORG_NAME(String oRG_NAME) {
		ORG_NAME = oRG_NAME;
	}
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public double getENTER_DR() {
		return ENTER_DR;
	}
	public void setENTER_DR(double eNTER_DR) {
		ENTER_DR = eNTER_DR;
	}
	public double getENTER_CR() {
		return ENTER_CR;
	}
	public void setENTER_CR(double eNTER_CR) {
		ENTER_CR = eNTER_CR;
	}
	public double getEXCHANGE_RATE() {
		return EXCHANGE_RATE;
	}
	public void setEXCHANGE_RATE(double eXCHANGE_RATE) {
		EXCHANGE_RATE = eXCHANGE_RATE;
	}
	public double getT_ACCOUNT_DR() {
		return T_ACCOUNT_DR;
	}
	public void setT_ACCOUNT_DR(double t_ACCOUNT_DR) {
		T_ACCOUNT_DR = t_ACCOUNT_DR;
	}
	public double getT_ACCOUNT_CR() {
		return T_ACCOUNT_CR;
	}
	public void setT_ACCOUNT_CR(double t_ACCOUNT_CR) {
		T_ACCOUNT_CR = t_ACCOUNT_CR;
	}
	public double getT_ENTER_DR() {
		return T_ENTER_DR;
	}
	public void setT_ENTER_DR(double t_ENTER_DR) {
		T_ENTER_DR = t_ENTER_DR;
	}
	public double getT_ENTER_CR() {
		return T_ENTER_CR;
	}
	public void setT_ENTER_CR(double t_ENTER_CR) {
		T_ENTER_CR = t_ENTER_CR;
	}
	public double getT_AMOUNT() {
		return T_AMOUNT;
	}
	public void setT_AMOUNT(double t_AMOUNT) {
		T_AMOUNT = t_AMOUNT;
	}
	public double getENTER_AMOUNT() {
		return ENTER_AMOUNT;
	}
	public void setENTER_AMOUNT(double eNTER_AMOUNT) {
		ENTER_AMOUNT = eNTER_AMOUNT;
	}
	public double getT_ENTER_AMOUNT() {
		return T_ENTER_AMOUNT;
	}
	public void setT_ENTER_AMOUNT(double t_ENTER_AMOUNT) {
		T_ENTER_AMOUNT = t_ENTER_AMOUNT;
	}
	public String getBALANCE_DIRECTION() {
		return BALANCE_DIRECTION;
	}
	public void setBALANCE_DIRECTION(String bALANCE_DIRECTION) {
		BALANCE_DIRECTION = bALANCE_DIRECTION;
	}
	public double getSumDebit() {
		return sumDebit;
	}
	public void setSumDebit(double sumDebit) {
		this.sumDebit = sumDebit;
	}
	public double getSumCrebit() {
		return sumCrebit;
	}
	public void setSumCrebit(double sumCrebit) {
		this.sumCrebit = sumCrebit;
	}
	public double getSumTDebit() {
		return sumTDebit;
	}
	public void setSumTDebit(double sumTDebit) {
		this.sumTDebit = sumTDebit;
	}
	public double getSumTCrebit() {
		return sumTCrebit;
	}
	public void setSumTCrebit(double sumTCrebit) {
		this.sumTCrebit = sumTCrebit;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getFOREIGN_NAME() {
		return FOREIGN_NAME;
	}
	public void setFOREIGN_NAME(String fOREIGN_NAME) {
		FOREIGN_NAME = fOREIGN_NAME;
	}
	public String getDIMENSION_NAME() {
		return DIMENSION_NAME;
	}
	public void setDIMENSION_NAME(String dIMENSION_NAME) {
		DIMENSION_NAME = dIMENSION_NAME;
	}
	/**
	 * @return the v_CATEGORY_NAME
	 */
	public String getV_CATEGORY_NAME() {
		return V_CATEGORY_NAME;
	}
	/**
	 * @param v_CATEGORY_NAME the v_CATEGORY_NAME to set
	 */
	public void setV_CATEGORY_NAME(String v_CATEGORY_NAME) {
		V_CATEGORY_NAME = v_CATEGORY_NAME;
	}
	
	
}
