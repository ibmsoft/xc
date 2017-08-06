package com.xzsoft.xc.ar.util;
/**
 * 
  * @ClassName：XCARConstants
  * @Description：应收模块管理常量
  * @author：任建建
  * @date：2016年7月6日 下午1:44:09
 */
public class XCARConstants {
	/*
	 * XC_AP_DOC_CLASS			票据大类
	 * 需要提交流程的表
	 * XSFP						销售发票
	 * 需要生成凭证的单据
	 * YSD						应收单
	 * SKD						收款单
	 * HXD						核销单
	 * YUETZD					余额调整单
	 */
	public static final String XSFP = "XSFP";
	//需要生成凭证的单据
	public static final String YSD = "YSD";
	public static final String SKD = "SKD";
	public static final String HXD = "HXD";
	public static final String TZD = "TZD";
	public static final String YUETZD = "YUETZD";
	
	/*
	 * 销售发票中的单据类型
	 * ZZS						增值税专用发票
	 * ZZSPT					增值税普通发票
	 * ZZS-HZ					增值税专用发票-红字
	 * ZZSPT-HZ					增值税普通发票-红字
	 */
	public static final String ZZS = "ZZS";
	public static final String ZZSPT = "ZZSPT";
	public static final String ZZS_HZ = "ZZS-HZ";
	public static final String ZZSPT_HZ = "ZZSPT-HZ";
	
	/*
	 * 付款单中的单据类型
	 * SKD_JSK					结算款
	 * SKD_QTSK					其它收款
	 * SKD_YUSK					预收款
	 * SKD_JSK-H				结算款-红字
	 * SKD_QTSK-H				其它收款-红字
	 * SKD_YUSK-H				预收款-红字
	 */
	public static final String SKD_JSK = "SKD_JSK";
	public static final String SKD_QTSK = "SKD_QTSK";
	public static final String SKD_YUSK = "SKD_YUSK";
	public static final String SKD_JSK_H = "SKD_JSK-H";
	public static final String SKD_QTSK_H = "SKD_QTSK-H";
	public static final String SKD_YUSK_H = "SKD_YUSK-H";

	/*
	 * 核销单中的单据类型
	 * YSHYUS					应收核预收
	 * YSHYF					应收核应付
	 * YSHLDC					应收红蓝对冲
	 * YUSHLDC					预收红蓝对冲
	 */
	public static final String YSHYUS = "YSHYUS";
	public static final String YSHYF = "YSHYF";
	public static final String YSHLDC = "YSHLDC";
	public static final String YUSHLDC = "YUSHLDC";

	/*
	 * 应付单中的单据类型
	 * YSD						应收单
	 * YSD-HZ					应收单-红字
	 */
	public static final String YSD_HZ = "YSD-HZ";
	
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
	public static final String XC_AR_PAY = "XC_AR_PAY";
	
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
	 * AR模块单据编码规则
	 * AR_CANCEL				应收核销单编码规则
	 * AR_INV					应收采购发票编码规则
	 * AR_INV_GL				应收应付单编码规则
	 * AR_PAY					应收付款单编码规则
	 */
	public static final String RULE_CODE_AR_CANCEL	= "AR_CANCEL";
	public static final String RULE_CODE_AR_INV 	= "AR_INV";
	public static final String RULE_CODE_AR_INV_GL	= "AR_INV_GL";
	public static final String RULE_CODE_AR_PAY 	= "AR_PAY";

}