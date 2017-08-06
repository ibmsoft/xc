package com.xzsoft.xc.gl.modal;
/**
 * @desc (按照期间的开始时间来对账簿的会计期间按照自然会计科目顺序进行排序)
 * @desc (使用时将日期转成数字形式，如:2015-07-01转为:20150701) 
 * @author tangxl
 * @date 2016年3月20日
 */
public class LedgerPeriod {
	private String ledgerId;
	private String periodCode;
	private String startDate;
	private String endDate;
	public String getLedgerId() {
		return ledgerId;
	}
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
