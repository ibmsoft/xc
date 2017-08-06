package com.xzsoft.xc.bg.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgCostPrjBalBean;
import com.xzsoft.xc.bg.modal.BgFactBalance;
import com.xzsoft.xc.util.modal.Project;

/**
 * @ClassName: BudgetDataHandlerMapper
 * @Description: 预算余额处理Mapper
 * @author linp
 * @date 2016年3月10日 上午9:41:10
 *
 */
public interface BudgetDataHandlerMapper {
	/**
	 * 
	 * @Title: budgetDataSelect
	 * @Description: 查询预算余额汇总是否有相同的数据
	 * @param @param bgCostPrjBal
	 * @param @return 设定文件
	 * @return float 返回类型
	 */
	public float budgetDataSelect(BgCostPrjBalBean bgCostPrjBal);// 查询预算余额汇总是否有相同的数据

	/**
	 * 
	 * @Title: budgetDataInsert
	 * @Description: 插入预算余额表
	 * @param @param bgCostPrjBal 设定文件
	 * @return void 返回类型
	 */
	public void budgetDataInsert(BgCostPrjBalBean bgCostPrjBal);// 插入预算余额表

	/**
	 * 
	 * @Title: budgetDataUpdate
	 * @Description: 更新预算余额表
	 * @param @param bgCostPrjBal 设定文件
	 * @return void 返回类型
	 */
	public void budgetDataUpdate(BgCostPrjBalBean bgCostPrjBal);// 更新预算余额表

	/**
	 * 
	 * @methodName getBudgetBalance
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 获取费用预算/项目预算的余额
	 * @param map
	 *            费用预算：ledgerId periodCode bgYear；项目预算：ledgerId projectId
	 * @return
	 * @变动说明
	 * @version 1.0
	 */
	public List<BgBillBalance> getBudgetBalance(HashMap<String, String> map);

	/**
	 * 
	 * @methodName insertCostBgBalMysql
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 新增预算余额
	 * @param insertList
	 * @变动说明
	 * @version 1.0
	 */
	public void insertCostBgBalMysql(List<BgBillBalance> insertList);

	/**
	 * 
	 * @methodName insertCostBgBalOracle
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 新增预算余额
	 * @param insertList
	 * @变动说明
	 * @version 1.0
	 */
	public void insertCostBgBalOracle(List<BgBillBalance> insertList);

	/**
	 * 
	 * @methodName insertProjectBgBalMysql
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 新增预算余额
	 * @param insertList
	 * @变动说明
	 * @version 1.0
	 */
	public void insertProjectBgBalMysql(List<BgBillBalance> insertList);

	/**
	 * 
	 * @methodName insertProjectBgBalOracle
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 新增预算余额
	 * @param insertList
	 * @变动说明
	 * @version 1.0
	 */
	public void insertProjectBgBalOracle(List<BgBillBalance> insertList);

	/**
	 * 
	 * @methodName updateCostBgBalMysql
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 新增预算余额
	 * @param updateList
	 * @变动说明
	 * @version 1.0
	 */
	public void updateCostBgBalMysql(List<BgBillBalance> updateList);

	/**
	 * 
	 * @methodName updateCostBgBalOracle
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 新增预算余额
	 * @param updateList
	 * @变动说明
	 * @version 1.0
	 */
	public void updateCostBgBalOracle(List<BgBillBalance> updateList);

	/**
	 * 
	 * @methodName updateProjectBgBalMysql
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 新增预算余额
	 * @param updateList
	 * @变动说明
	 * @version 1.0
	 */
	public void updateProjectBgBalMysql(List<BgBillBalance> updateList);

	/**
	 * 
	 * @methodName updateProjectBgBalOracle
	 * @author tangxl
	 * @date 2016年6月20日
	 * @describe 新增预算余额
	 * @param updateList
	 * @变动说明
	 * @version 1.0
	 */
	public void updateProjectBgBalOracle(List<BgBillBalance> updateList);

	/**
	 * 
	 * @methodName 方法名
	 * @author tangxl
	 * @date 2016年6月22日
	 * @describe 添加预算
	 * @param map
	 * @变动说明
	 * @version 1.0
	 */
	public void increaseBudgetMysql(HashMap<String,Object> map);

	/**
	 * 
	 * @methodName 方法名
	 * @author tangxl
	 * @date 2016年6月22日
	 * @describe 添加预算
	 * @param map
	 * @变动说明
	 * @version 1.0
	 */
	public void increaseBudgetOracle(HashMap<String,Object> map);

	/**
	 * 
	 * @methodName updateFactBudget
	 * @author tangxl
	 * @date 2016年6月22日
	 * @describe TODO
	 * @param srcId
	 * @param targetType
	 * @param originalType
	 * @变动说明
	 * @version 1.0
	 */
	public void updateFactBudget(@Param(value = "srcId") String srcId,
			@Param(value = "targetType") String targetType,
			@Param(value = "originalType") String originalType,
			@Param(value = "dbType") String dbType);
	/**
	 * 
	 * @methodName  updateFactBudget
	 * @author      tangxl
	 * @date        2016年6月22日
	 * @describe    TODO
	 * @param       srcId
	 * @param       factType
	 * @变动说明       
	 * @version     1.0
	 */
	public void deleteFactBudget(@Param(value = "srcId") String srcId,@Param(value = "factType") String factType);
	/**
	 * 
	 * @methodName  getFactBalance
	 * @author      tangxl
	 * @date        2016年6月24日
	 * @describe    获取汇总余额
	 * @param       bgItemType
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<BgFactBalance> getFactBalance(HashMap<String, String> map);
	/**
	 * 
	 * @methodName  getProjectById
	 * @author      tangxl
	 * @date        2016年6月24日
	 * @describe    TODO
	 * @param bgItemType
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public Project getProjectById(@Param(value = "projectId")String projectId);

}
