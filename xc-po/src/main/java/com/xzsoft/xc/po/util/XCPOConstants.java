package com.xzsoft.xc.po.util;
/** 
 * @ClassName: XCPOConstants
 * @Description: 采购模块管理常量
 * @author weicw
 * @date 2016年12月9日 上午11:16:32 
 * 
 */
public class XCPOConstants {
	/*
	 * XC_PO_DOC_CLASS			票据大类
	 * 需要提交流程的表
	 * CGSQ						采购申请
	 * CGDD						采购订单
	 * DHD						到货单
	 * THD						退货单
	 */
	public static final String CGSQ = "CGSQ";
	public static final String CGDD = "CGDD";
	public static final String DHD = "DHD";
	public static final String THD = "THD";
	
	/*
	 * PO采购单据业务状态
	 * A						草稿
	 * C						审批中
	 * E						审批完成
	 * B						变更中
	 */
	public static final String STATUS_A = "A";
	public static final String STATUS_C = "C";
	public static final String STATUS_E = "E";
	public static final String STATUS_B = "B";
	
	/*
	 * PO模块单据编码规则
	 * PO_PUR_AP				采购申请编码规则
	 * PO_ORDER					采购订单编码规则
	 * PO_ARRIVAL				到货单编码规则
	 * PO_RETURN				退货单编码规则
	 */
	public static final String RULE_CODE_PO_PUR_AP	= "PO_PUR_AP";
	public static final String RULE_CODE_PO_ORDER	= "PO_ORDER";
	public static final String RULE_CODE_PO_ARRIVAL	= "PO_ARRIVAL";
	public static final String RULE_CODE_PO_RETURN	= "PO_RETURN";
	
}
