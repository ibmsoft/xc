package com.xzsoft.xc.bg.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgCostPrjBalBean;
import com.xzsoft.xc.bg.modal.BgFactBalance;
import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.util.modal.Project;

/**
 * @ClassName: BudgetDataHandlerDAO 
 * @Description: 预算余额处理数据层接口
 * @author linp
 * @date 2016年3月10日 上午9:40:16 
 *
 */
public interface BudgetDataHandlerDAO {
	
	/**
	 * @Title: budgetDataSelect 
	 * @Description: 查询预算余额汇总是否有相同的数据
	 * @param bgCostPrjBal
	 * @return
	 * @throws Exception    设定文件
	 */
	public float budgetDataSelect(BgCostPrjBalBean bgCostPrjBal) throws Exception;
	
	/**
	 * @Title: budgetDataInsert 
	 * @Description: 插入预算余额表
	 * @param bgCostPrjBalLists
	 * @throws Exception    设定文件
	 */
	public void budgetDataInsert(List<BgCostPrjBalBean> bgCostPrjBalLists) throws Exception;
	
	/**
	 * @Title: budgetDataUpdate 
	 * @Description: 更新预算余额表
	 * @param bgCostPrjBalLists
	 * @throws Exception    设定文件
	 */
	public void budgetDataUpdate(List<BgCostPrjBalBean> bgCostPrjBalLists) throws Exception;
	/**
	 * 
	 * @methodName  getBudgetBalance
	 * @author      tangxl
	 * @date        2016年6月17日
	 * @describe    获取预算余额信息
	 * @param map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<BgBillBalance> getBudgetBalance(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  updateBgItemBalances
	 * @author      tangxl
	 * @date        2016年6月20日
	 * @describe    更新预算项目余额
	 * @param updateList
	 * @param insertList
	 * @param bgItemType
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateBgItemBalances(List<BgBillBalance> updateList,List<BgBillBalance> insertList,String bgItemType) throws Exception;
	/**
	 * 
	 * @methodName  incrOccupancyBudget
	 * @author      tangxl
	 * @date        2016年6月22日
	 * @describe    增加预算占用数
	 * @param       list
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void incrOccupancyBudget(List<BgFactBean> list) throws Exception;
	/**
	 * @Title: updOccupancyBudget 
	 * @Description:         修改预算占用数
	 * @param   srcId
	 * @param   targetType    修改后的状态
	 * @param   originalType  要修改的状态
	 * @throws Exception      设定文件
	 */
	public void updateFactBudget(String srcId,String targetType,String originalType) throws Exception ;
	
	/**
	 * @Title: delOccupancyBudget 
	 * @Description: 删除预算占用数
	 * @param srcId
	 * @param factType   要删除的预算数类型
	 * @throws Exception    设定文件
	 */
	public void deleteFactBudget(String srcId,String factType) throws Exception ;
	/**
	 * 
	 * @methodName  getFactBudgetAmount
	 * @author      tangxl
	 * @date        2016年6月24日
	 * @describe    获取预算实际发生额度
	 * @param       map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<BgFactBalance> getFactBudgetAmount(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  getProjectById
	 * @author      tangxl
	 * @date        2016年6月24日
	 * @describe    TODO
	 * @param       projectId
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public Project getProjectById(String projectId) throws Exception;
	
}
