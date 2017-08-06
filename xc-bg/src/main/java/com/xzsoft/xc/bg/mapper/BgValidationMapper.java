package com.xzsoft.xc.bg.mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgItemBean;
import com.xzsoft.xc.util.modal.Project;

/**
 * 
  * @className: BgValidationMapper
  * @describe:  预算校验取数接口
  * @author     tangxl
  * @date       2016年6月21日 上午14:39:27
 */
public interface BgValidationMapper {
	/**
	 * 
	 * @methodName  getLedgerContralCycle
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    获取账簿控制周期
	 * @param ledgerId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public String getLedgerContralCycle(@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @methodName  getItemContralDimension
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    获取预算项目控制维度
	 * @param       ledgerId
	 * @param       itemId
	 * @param       dbType
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public HashMap<String, Object> getItemContralDimension(@Param(value="ledgerId")String ledgerId,@Param(value="itemId")String itemId);
	/**
	 * 
	 * @methodName  getCostBudgetVal
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    根据条件获取费用预算总额
	 * @param map
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public BigDecimal getCostBudgetVal(@Param(value="ledgerId")String ledgerId,@Param(value="whereSql")String whereSql);
	/**
	 * 
	 * @methodName  getProjectBudgetVal
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    根据条件获取项目预算总额
	 * @param map
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public BigDecimal getProjectBudgetVal(@Param(value="ledgerId")String ledgerId,@Param(value="whereSql")String whereSql);
	/**
	 * 
	 * @methodName  getProjectDimension
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    获取项目控制维度
	 * @param       projectId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public HashMap<String, Object> getProjectDimension(@Param(value="projectId")String projectId);
	/**
	 * 
	 * @methodName  getFeeBgFacttVal
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    根据条件获取费用项目占用数和实际发生数
	 * @param       whereSql
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public BigDecimal getFeeBgFactVal(@Param(value="whereSql")String whereSql);
	/**
	 * 
	 * @methodName  getAllNameById
	 * @author      tangxl
	 * @date        2016年7月1日
	 * @describe    根据id获取name(名称)
	 * @param whereSql
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public String getAllNameById(@Param(value="whereSql")String whereSql);
	/**
	 * 
	 * @methodName  getSummayFeeBal
	 * @author      tangxl
	 * @date        2016年7月1日
	 * @describe    根据预算版本Id获取待统计的费用预算申请单
	 * @param       bgVersionId
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<BgBillBalance> getSummayFeeBal(@Param(value="bgVersionId")String bgVersionId);
	/**
	 * 
	 * @methodName  updateFeeBgVersion
	 * @author      tangxl
	 * @date        2016年7月4日
	 * @describe    启用新的预算版本
	 * @param       map
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateFeeBgVersion(HashMap<String, String> map);
	/**
	 * 
	 * @methodName  annulsFeeBgVersion
	 * @author      tangxl
	 * @date        2016年7月4日
	 * @describe    失效原始预算版本
	 * @param       map
	 * @变动说明       
	 * @version     1.0
	 */
	public void annulsFeeBgVersion(@Param(value="bgVersionId")String bgVersionId,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @methodName  delOldVersionFeeBal
	 * @author      tangxl
	 * @date        2016年7月4日
	 * @describe    TODO
	 * @变动说明       
	 * @version     1.0
	 */
	public void delOldVersionFeeBal();
	/**
	 * 
	 * @methodName  insertFeeMysql
	 * @author      tangxl
	 * @date        2016年7月4日
	 * @describe    TODO
	 * @param summaryList
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertFeeMysql(List<BgBillBalance> summaryList);
	/**
	 * 
	 * @methodName  insertFeeOracle
	 * @author      tangxl
	 * @date        2016年7月4日
	 * @describe    TODO
	 * @param summaryList
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertFeeOracle(List<BgBillBalance> summaryList);
	/**
	 * 
	 * @methodName  getProjectInfoById
	 * @author      tangxl
	 * @date        2016年7月6日
	 * @describe    获取项目信息
	 * @param       projectId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	
	public Project getProjectInfoById(@Param(value="projectId")String projectId);
	/**
	 * @methodName  getBgBillTypeList
	 * @author      tangxl
	 * @date        2016年7月6日
	 * @describe    TODO
	 * @param paramsMap
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<String> getBgBillTypeList(HashMap<String, String> paramsMap);
	/**
	 * 
	 * @methodName  getValidateBgItemList
	 * @author      tangxl
	 * @date        2016年7月12日
	 * @describe    删除预算项目前校验预算项目是否已经发生实际预算业务
	 * @param       bgItemsId
	 * @param       ledgerId
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<BgItemBean> getValidateBgItemList(@Param(value="bgItemsId")String bgItemsId,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @methodName  delLedgerBgItems
	 * @author      tangxl
	 * @date        2016年7月12日
	 * @describe    删除账簿预算项目
	 * @param       bgItemsId
	 * @param       ledgerId
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void delLedgerBgItems(@Param(value="bgItemsId")String bgItemsId,@Param(value="ledgerId")String ledgerId);
	/**
	 * 
	 * @methodName  deletePrjBal
	 * @author      tangxl
	 * @date        2016年9月18日
	 * @describe    回退预算申请单<删除项目预算申请单的值>
	 * @param 		map
	 * @变动说明       
	 * @version     1.0
	 */
	public void deletePrjBal(HashMap<String, Object> map);
	/**
	 * 
	 * @methodName  deleteCostBal
	 * @author      tangxl
	 * @date        2016年9月18日
	 * @describe    回退预算申请单<删除费用预算申请单的值>
	 * @param 		map
	 * @变动说明       
	 * @version     1.0
	 */
	public void deleteCostBal(HashMap<String, Object> map);
	/**
	 * 
	 * @methodName  updateBgBillStatus
	 * @author      tangxl
	 * @date        2016年9月18日
	 * @describe    修改预算单状态
	 * @param 		bgDocId
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateBgBillStatus(@Param(value="bgDocId")String bgDocId);
}
