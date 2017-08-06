package com.xzsoft.xc.bg.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONObject;

import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgDocDtl;
import com.xzsoft.xc.bg.modal.BgItemBean;

/**
 * 
  * @className: BgValidationDAO
  * @description: 预算校验取数接口
  * @author tangxl
  * @date 2016年6月21日 上午14:16:36
 */
public interface BgValidationDAO {
	/**
	 * 
	 * @methodName  getTotalBudgetVal
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    根据条件获取总的预算余额
	 * @param map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public  HashMap<String,Object> getTotalBudgetVal(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  getFeeFactAmount
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    根据条件获取费用预算发生数和占用数
	 * @param map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public BigDecimal getFeeFactAmount(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  getProjectFactAmount
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    根据条件获取项目预算发生数和占用数
	 * @param map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public BigDecimal getProjectFactAmount(HashMap<String, String> map) throws Exception;
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
	public List<BgBillBalance> getSummayFeeBal(String bgVersionId) throws Exception;
	/**
	 * 
	 * @methodName  updateFeeBgVersion
	 * @author      tangxl
	 * @date        2016年7月4日
	 * @describe    修改预算版本
	 * @param       bgVersionId
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateFeeBgVersion(String bgVersionId,String ledgerId) throws Exception;
	/**
	 * 
	 * @methodName  inserNewVersionFee
	 * @author      tangxl
	 * @date        2016年7月4日
	 * @describe    插入新的预算版本余额记录
	 * @param       summaryList
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void  inserNewVersionFee(List<BgBillBalance> summaryList) throws Exception;
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年7月6日
	 * @describe    查看预算执行下的单据明细信息
	 * @param       paramsMap
	 * @return      单据明细信息集合的sql
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<String> getBgBillTypeList(HashMap<String, String> paramsMap) throws Exception;
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
	public List<BgItemBean> getValidateBgItemList(String bgItemsId,String ledgerId) throws Exception;
	/**
	 * 
	 * @methodName  cancelBudgetBal
	 * @author      tangxl
	 * @date        2016年9月18日
	 * @describe    回退预算申请
	 * @param 		list
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public JSONObject cancelBudgetBal(List<BgDocDtl> list) throws Exception;
	
}
