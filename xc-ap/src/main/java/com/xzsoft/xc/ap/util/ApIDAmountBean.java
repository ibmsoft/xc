package com.xzsoft.xc.ap.util;

/**
 * @ClassName: ApIDAmontBean 
 * @Description: 修改单据金额对应的单据id和修改金额
 * @author zhenghy
 * @date 2016年8月26日 下午4:12:21
 */
public class ApIDAmountBean {

	private String AP_DOC_ID;
	private double AMOUNT;
	public String getAP_DOC_ID() {
		return AP_DOC_ID;
	}
	public void setAP_DOC_ID(String aP_DOC_ID) {
		AP_DOC_ID = aP_DOC_ID;
	}
	public double getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(double aMOUNT) {
		AMOUNT = aMOUNT;
	}
	
	
}
