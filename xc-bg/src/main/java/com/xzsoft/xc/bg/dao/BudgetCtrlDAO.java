package com.xzsoft.xc.bg.dao;

import com.xzsoft.xc.bg.modal.BgFactBean;


/**
 * @ClassName: BudgetCtrlDAO 
 * @Description: 预算控制处理数据层接口
 * @author linp
 * @date 2016年3月10日 上午9:39:37 
 *
 */
public interface BudgetCtrlDAO {
	
	/**
	 * @Title: getProjectBudgetBal 
	 * @Description: 取得项目预算余额
	 * @param factBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public double getProjectBudgetBal(BgFactBean factBean) throws Exception ;
	
	/**
	 * @Title: getCostBudgetBal 
	 * @Description: 取得费用预算余额
	 * @param factBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public double getCostBudgetBal(BgFactBean factBean) throws Exception ;
	
}
