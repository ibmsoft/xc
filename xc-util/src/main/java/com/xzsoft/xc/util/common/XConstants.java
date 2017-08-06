package com.xzsoft.xc.util.common;

public class XConstants {
	
	/*--------------------------------------------
	 * 辅助核算段编码信息
	 --------------------------------------------*/
	// 科目段
	public static final String 	XC_GL_ACCOUNTS = "XC_GL_ACCOUNTS" ;
	// 供应商
	public static final String 	XC_AP_VENDORS = "XC_AP_VENDORS" ;
	// 客户
	public static final String 	XC_AR_CUSTOMERS = "XC_AR_CUSTOMERS" ;
	// 个人往来
	public static final String 	XIP_PUB_EMPS = "XIP_PUB_EMPS" ;
	// 内部往来
	public static final String 	XIP_PUB_ORGS = "XIP_PUB_ORGS" ;
	// 产品
	public static final String 	XC_GL_PRODUCTS = "XC_GL_PRODUCTS" ;
	// 成本中心
	public static final String 	XIP_PUB_DETPS = "XIP_PUB_DEPTS" ;
	// 项目
	public static final String 	XC_PM_PROJECTS = "XC_PM_PROJECTS" ;
	// 自定义1
	public static final String 	XC_GL_CUSTOM1 = "XC_GL_CUSTOM1" ;
	// 自定义2
	public static final String 	XC_GL_CUSTOM2 = "XC_GL_CUSTOM2" ;
	// 自定义3
	public static final String 	XC_GL_CUSTOM3 = "XC_GL_CUSTOM3" ;
	// 自定义4
	public static final String 	XC_GL_CUSTOM4 = "XC_GL_CUSTOM4" ;
	// 科目组合ID
	public static final String XC_GL_CCID = "XC_GL_CCID" ;
	// 现金流量项目
	public static final String XC_GL_CASH_ITEMS = "XC_GL_CASH_ITEMS" ;
	// 账簿信息
	public static final String XC_GL_LEDGERS = "XC_GL_LEDGERS" ;
	// 凭证分类信息
	public static final String XC_GL_V_CATEGORY = "XC_GL_V_CATEGORY" ;
	
	// 总账模块
	public static final String PROJECT_NAME = "XCGL";
	// 报表模块 
	public static final String REP_PRJ_NAME = "XCREP" ;
	
	// 余额类型枚举值(不含未过账)
	public static final String B_DR = "B_DR" ;		// 期初借方发生额
	public static final String B_CR = "B_CR" ;		// 期初贷方发生额
	public static final String B_JE = "B_JE" ;		// 期初余额
	public static final String PTD_DR = "PTD_DR" ;	// 本期借方发生额
	public static final String PTD_CR = "PTD_CR" ;	// 本期贷方发生额
	public static final String PTJ_JE = "PTJ_JE" ;	// 本期净额
	public static final String Y_DR = "Y_DR" ;		// 年初借方发生额
	public static final String Y_CR = "Y_CR" ;		// 年初贷方发生额
	public static final String Y_JE = "Y_JE" ;		// 年初余额
	public static final String YTD_DR = "YTD_DR" ;	// 本年借方发生额
	public static final String YTD_CR = "YTD_CR" ;	// 本年贷方发生额
	public static final String YTD_JE = "YTD_JE" ;	// 本年净额
	public static final String PJTD_DR = "PJTD_DR" ;// 期末借方发生额
	public static final String PJTD_CR = "PJTD_CR" ;// 期末贷方发生额
	public static final String PJTD_JE = "PJTD_JE" ;// 期末余额
	
	// 余额类型枚举值(含未过账)
	public static final String T_B_DR = "T_B_DR" ;		// 期初借方发生额(含未过账)
	public static final String T_B_CR = "T_B_CR" ;		// 期初贷方发生额(含未过账)
	public static final String T_B_JE = "T_B_JE" ;		// 期初余额(含未过账)
	public static final String T_PTD_DR = "T_PTD_DR" ;	// 本期借方发生额(含未过账)
	public static final String T_PTD_CR = "T_PTD_CR" ;	// 本期贷方发生额(含未过账)
	public static final String T_PTJ_JE = "T_PTJ_JE" ;	// 本期净额(含未过账)
	public static final String T_Y_DR = "T_Y_DR" ;		// 年初借方发生额(含未过账)
	public static final String T_Y_CR = "T_Y_CR" ;		// 年初贷方发生额(含未过账)
	public static final String T_Y_JE = "T_Y_JE" ;		// 年初余额(含未过账)
	public static final String T_YTD_DR = "T_YTD_DR" ;	// 本年借方发生额(含未过账)
	public static final String T_YTD_CR = "T_YTD_CR" ;	// 本年贷方发生额(含未过账)
	public static final String T_YTD_JE = "T_YTD_JE" ;	// 本年净额(含未过账)
	public static final String T_PJTD_DR = "T_PJTD_DR" ;// 期末借方发生额(含未过账)
	public static final String T_PJTD_CR = "T_PJTD_CR" ;// 期末贷方发生额(含未过账)
	public static final String T_PJTD_JE = "T_PJTD_JE" ;// 期末余额(含未过账)

	/*--------------------------------------------
	 * 规则流水依据
	 * 
	 *	LD_YYYY			按账簿+年流水
	 *	LD_YYYY_MM		按账簿+年月流水
	 *	LD				按账簿流水
     *
	 --------------------------------------------*/
	public static final String SERIAL_TYPE_LD = "LD" ;
	public static final String SERIAL_TYPE_LD_Y = "LD_YYYY" ;
	public static final String SERIAL_TYPE_LD_YM = "LD_YYYY_MM" ;
	
	
	/*--------------------------------------------
	 * 规则明细项
	 * 
	 *	YYYY		4位年
	 *	V_CATEGORY	凭证分类
	 *	YY			两位年
	 *	LEDGER		账簿
	 *	MM			月
	 --------------------------------------------*/
	public static final String RULE_DTL_YYYY = "YYYY" ;
	public static final String RULE_DTL_YY = "YY" ;
	public static final String RULE_DTL_MM = "MM" ;
	public static final String RULE_DTL_LEDGER = "LEDGER" ;
	public static final String RULE_DTL_CATEGORY = "V_CATEGORY" ;
	
	// 语言信息项枚举
	public static final String ZH = "zh" ;
	public static final String EN = "en" ;
}
