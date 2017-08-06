package com.xzsoft.xc.ex.util;

/**
 * @ClassName: XCEXConstants 
 * @Description: 费用报销管理常量
 * @author linp
 * @date 2016年3月10日 下午6:13:39 
 *
 */
public class XCEXConstants {

	/*-------------------------------------------
	 * 单据类型：
	 * 	EX01	费用申请单
	 *	EX02	借款单
	 *	EX03	差旅报销单
	 *	EX04	费用报销单
	 *	EX05	还款单
	 *	EX06 	支付单
	 * ------------------------------------------*/
	public static final String EX_DOC_EX01 = "EX01" ;
	public static final String EX_DOC_EX02 = "EX02" ;
	public static final String EX_DOC_EX03 = "EX03" ;
	public static final String EX_DOC_EX04 = "EX04" ;
	public static final String EX_DOC_EX05 = "EX05" ;
	public static final String EX_DOC_EX06 = "EX06" ;
	
	/*-------------------------------------------
	 * 支付方式：
	 * 	1-立即支付
	 *  2-挂账支付
	 * ------------------------------------------*/
	public static final String PAY_METHOD_ONCE = "1" ;
	public static final String PAY_METHOD_CREDIT = "2" ;
	
	/*-------------------------------------------
	 * 付款方式：
	 * cash-现金
	 * bank-银行
	 * check-支票
	 * other-其他
	 * ------------------------------------------*/
	public static final String PAY_TYPE_CASH = "cash" ;
	public static final String PAY_TYPE_BANK = "bank" ;
	public static final String PAY_TYPE_CHECK = "check" ;
	public static final String PAY_TYPE_OTHER = "other" ;
	
	/*-------------------------------------------
	 * 凭证来源：
	 * XC_EX_DOCS-报销
	 * XC_EX_PAY-支付
	 * ------------------------------------------*/
	public static final String SRC_CODE_EX = "XC_EX_DOCS" ;
	public static final String SRC_CODE_PAY = "XC_EX_PAY" ;
	
	/*-------------------------------------------
	 * 付款单创建模式：
	 * check	-	复核报销单时生成付款单（针对立即支付）
	 * payment	-	新建付款单（针对挂账支付）
	 * ------------------------------------------*/
	public static final String PAY_MODE_CHECK = "check" ;
	public static final String PAY_MODE_PAYMENT = "payment" ;
	
	
}
