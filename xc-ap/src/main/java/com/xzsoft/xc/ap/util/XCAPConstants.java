package com.xzsoft.xc.ap.util;
/**
 * 
  * @ClassName: XCAPConstants
  * @Description: 应付模块管理常量
  * @author 任建建
  * @date 2016年4月7日 上午8:58:57
 */
public class XCAPConstants {
	/*
	 * XC_AP_DOC_CLASS			票据大类
	 * 需要提交流程的表
	 * CGFP						采购发票
	 * FKSQD					付款申请单
	 * 需要生成凭证的单据
	 * YFD						应付单
	 * FKD						付款单
	 * HXD						核销单
	 * YUETZD					余额调整单
	 */
	public static final String CGFP = "CGFP";
	public static final String FKSQD = "FKSQD";
	//需要生成凭证的单据
	public static final String YFD = "YFD";
	public static final String FKD = "FKD";
	public static final String HXD = "HXD";
	public static final String YUETZD = "YUETZD";
	
	/*
	 * 采购发票中的单据类型
	 * PTFP						普通发票
	 * ZZSFP					增值税专用发票
	 * PTFP-H					普通发票-红字
	 * ZZSFP-H					增值税专用发票-红字
	 * 
	 * CGFP						采购发票
	 * CGFP-H					采购发票-红字
	 */
	public static final String PTFP = "PTFP";
	public static final String ZZSFP = "ZZSFP";
	public static final String PTFP_H = "PTFP-H";
	public static final String ZZSFP_H = "ZZSFP-H";
	
	public static final String CGFP_H = "CGFP-H";
	public static final String CGFP_CGDD = "CGFP-CGDD";
	public static final String CGFP_CGDD_H = "CGFP-CGDD-H";
	
	/*
	 * 应付单的单据类型
	 * YFD 						应付单
	 * YFD-H 					应付单红
	 */
	public static final String YFD_YFD = "YFD";
	public static final String YFD_YFD_H = "YFD-H";
	/*
	 * 付款单中的单据类型
	 * FKD_CGJS					采购结算
	 * FKD_QTFK					其它付款
	 * FKD_YUFK					预付款
	 * FKD_CGJS-H				采购结算-红字
	 * FKD_QTFK-H				其它付款-红字
	 * FKD_YUFK-H				预付款-红字
	 */
	public static final String FKD_CGJS = "FKD_CGJS";
	public static final String FKD_QTFK = "FKD_QTFK";
	public static final String FKD_YUFK = "FKD_YUFK";
	public static final String FKD_CGJS_H = "FKD_CGJS-H";
	public static final String FKD_QTFK_H = "FKD_QTFK-H";
	public static final String FKD_YUFK_H = "FKD_YUFK-H";

	/*
	 * 付款申请单中的单据类型
	 * CGJS					采购结算
	 * QTFK					其它付款
	 * YUFK					预付款
	 */
	public static final String CGJS = "CGJS";
	public static final String QTFK = "QTFK";
	public static final String YUFK = "YUFK";

	/*
	 * 核销单中的单据类型
	 * YFHYS					应付核应收
	 * YFHLDC					应付红蓝对冲
	 * YFHYUF					应付核销预付
	 * YUFHLDC					预付红蓝对冲
	 */
	public static final String HXD_YFHYS = "YFHYS";
	public static final String HXD_YFHLDC = "YFHLDC";
	public static final String HXD_YFHYUF = "YFHYUF";
	public static final String HXD_YUFHLDC = "YUFHLDC";

	/*
	 * 应付单中的单据类型
	 * YFD_PTFP					普通发票
	 * YFD_ZZSFP				增值税专用发票
	 * YFD_PTFP-H				普通发票-红字
	 * YFD_ZZSFP-H				增值税专用发票-红字
	 */
	public static final String YFD_PTFP = "YFD_PTFP";
	public static final String YFD_ZZSFP = "YFD_ZZSFP";
	public static final String YFD_PTFP_H = "YFD_PTFP-H";
	public static final String YFD_ZZSFP_H = "YFD_ZZSFP-H";
	
	/*
	 * XC_AP_AGING_TYPE			账龄大类
	 * YQZL						远期账龄
	 * JQZL						近期账龄
	 */
	public static final String YQZL = "YQZL";
	public static final String JQZL = "JQZL";
	
	/*
	 * XC_AP_CT_TYPE			合同类型
	 * KKHT						开口合同
	 * BKHT						闭口合同
	 */
	public static final String KKHT = "KKHT";
	public static final String BKHT = "BKHT";
	
	/*
	 * 凭证来源
	 */
	public static final String XC_AP_PAY = "XC_AP_PAY";
	
	/*
	 * 付款方式：
	 * cash						现金
	 * bank						银行
	 * check					支票
	 * other					其他
	 */
	public static final String PAY_TYPE_CASH = "cash";
	public static final String PAY_TYPE_BANK = "bank";
	public static final String PAY_TYPE_CHECK = "check";
	public static final String PAY_TYPE_OTHER = "other";
	/*
	 * AP应付单据业务状态
	 * A						草稿
	 * C						审批中
	 * E						审批完成
	 * F						已复核
	 */
	public static final String STATUS_A = "A";
	public static final String STATUS_C = "C";
	public static final String STATUS_E = "E";
	public static final String STATUS_F = "F";
	
	/*
	 * AP模块单据编码规则
	 * AP_CANCEL				应付核销单编码规则
	 * AP_INV					应付采购发票编码规则
	 * AP_INV_GL				应付应付单编码规则
	 * AP_PAY					应付付款单编码规则
	 * AP_PAY_REQ				应付申请单编码规则
	 */
	public static final String RULE_CODE_AP_CANCEL	= "AP_CANCEL";
	public static final String RULE_CODE_AP_INV 	= "AP_INV";
	public static final String RULE_CODE_AP_INV_GL	= "AP_INV_GL";
	public static final String RULE_CODE_AP_PAY 	= "AP_PAY";
	public static final String RULE_CODE_AP_PAY_REQ = "AP_PAY_REQ";
	
	/*
	 * AP模块付款单来源
	 * GL_H						应付单（红字）
	 * GL_L						应付单（蓝字）
	 * REQ						申请单
	 * PAY						付款单
	 * TEMP						手工模版
	 */
	public static final String PAY_SOURCE_GL_H = "GL_H";
	public static final String PAY_SOURCE_GL_L = "GL_L";
	public static final String PAY_SOURCE_REQ  = "REQ";
	public static final String PAY_SOURCE_PAY  = "PAY";
	public static final String PAY_SOURCE_TEMP = "TEMP";
}