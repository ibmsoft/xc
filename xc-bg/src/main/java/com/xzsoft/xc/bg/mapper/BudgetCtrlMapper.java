package com.xzsoft.xc.bg.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.bg.modal.BgLdItemBean;

/**
 * @ClassName: BudgetCtrlMapper 
 * @Description: 预算控制处理Mapper
 * @author linp
 * @date 2016年3月10日 上午9:41:05 
 *
 */
public interface BudgetCtrlMapper {
	/**
	 * 
	  * @Title: getBgLdItem
	  * @Description: 根据预算项ID查询账簿级预算项信息
	  * @param @param map
	  * @param @return	设定文件
	  * @return List<BgLdItemBean>	返回类型
	 */
	public List<BgLdItemBean> getBgLdItem(HashMap<String,String> map);
	
	/**
	 * @Title: getPrjBgTotalBal 
	 * @Description: 计算项目预算余额 
	 * @param factBean
	 * @return    设定文件
	 */
	public double getPrjBgTotalBal(BgFactBean factBean) ;
	
	/**
	 * @Title: getPrjBgFactBal 
	 * @Description: 计算项目预算占用/发生数
	 * @param factBean
	 * @return    设定文件
	 */
	public double getPrjBgFactBal(BgFactBean factBean) ;
	
	/**
	 * @Title: getCostBgTotalBal 
	 * @Description: 计算费用预算总额
	 * @param factBean
	 * @return    设定文件
	 */
	public double getCostBgTotalBal(BgFactBean factBean) ;
	
	/**
	 * @Title: getCostBgFactBal 
	 * @Description: 计算费用预算发占用/发生数
	 * @param factBean
	 * @return    设定文件
	 */
	public double getCostBgFactBal(BgFactBean factBean) ;
}
