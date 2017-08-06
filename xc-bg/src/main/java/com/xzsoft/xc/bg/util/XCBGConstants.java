package com.xzsoft.xc.bg.util;

import java.util.HashMap;

/**
 * 
  * @ClassName: XCBGConstants
  * @Description: 费用预算常量
  * @author 任建建
  * @date 2016年3月30日 上午10:54:34
 */
public class XCBGConstants {
	/*
	 * 预算项目分类
	 * SY		损益类
	 * CA		现金类
	 */
	public static final String BG_ITEM_TYPE01 = "SY";
	public static final String BG_ITEM_TYPE02 = "CA";
	
	/*
	 * 费用预算控制维度
	 * 1		预算项目
	 * 2		预算项目+成本中心
	 */
	public static final String BG_EX_CTRL_DIM01 = "1";
	public static final String BG_EX_CTRL_DIM02 = "2";
	
	/*
	 * 项目预算控制维度
	 * 1		项目
	 * 2		项目+预算项目
	 * 3		项目+预算项目+成本中心
	 */
	
	public static final String BG_PRJ_CTRL_DIM01 = "1";
	public static final String BG_PRJ_CTRL_DIM02 = "2";
	public static final String BG_PRJ_CTRL_DIM03 = "3";
	
	/*
	 * 费用与项目预算控制方式
	 * 1		不控制
	 * 2		预警控制
	 * 3		绝对控制
	 */
	public static final String BG_CTRL_MODE01 = "1";
	public static final String BG_CTRL_MODE02 = "2";
	public static final String BG_CTRL_MODE03 = "3";
	/*
	 *预算单类型
	 *BG01     费用预算单
	 *BG02    项目预算单 
	 */
	public static final String BG_COST_TYPE = "BG01";
	public static final String BG_PRO_TYPE = "BG02";
	/*
	 *预算发生类型
	 *1  预算占用
	 *2  实际发生
	 */
	public static final String BG_OCCUR_TYPE = "1";
	public static final String BG_FACT_TYPE = "2";
	/*
	 *账簿周期控制
	 *1  按年控制
	 *2  按月累计控制
	 */
	public static final String LEDGER_YEAR_CONTROL = "1";
	public static final String LEDGER_MONTH_CONTROL = "2";
	/*
	 *预算执行单据类型 
	 *  
	 */
	@SuppressWarnings("serial")
	public static final HashMap<String, String> BUDGET_BILL_TYPE = new HashMap<String, String>(){
		{
		   put("XC_GL", "总账类单据");	
		   put("XC_EX", "报销类单据");
		   put("XC_AP", "应付类单据");
		   put("XC_AR", "应收类单据");
		   put("XC_BG", "预算类单据");
		   put("XC_FA", "固定资产类单据");
		}
	};
	
}