/**
 * 
 */
package com.xzsoft.xc.bg.service;

import java.util.HashMap;

import net.sf.json.JSONObject;


/**
 * @className BgValidationService
 * @describe  预算校验取数接口
 * @author tangxl
 *
 */
public interface BgValidationService {
	/**
	 * 
	 * @methodName  checkFeeBgIsValid
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    费用预算校验
	 * @param map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public JSONObject checkFeeBgIsValid(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  checkProjectBgIsValid
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    项目预算校验
	 * @param map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public JSONObject checkProjectBgIsValid(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  changeFeeBudgetVersion
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    切换预算版本
	 * @param       bgVersionId
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public JSONObject changeFeeBudgetVersion(String bgVersionId,String ledgerId) throws Exception;
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
	public String queryBudgetDetailBills(HashMap<String, String> paramsMap) throws Exception;
	/**
	 * 
	 * @methodName  checkBgItemBeforeDelete
	 * @author      tangxl
	 * @date        2016年7月12日
	 * @describe    删除预算项目前的校验
	 * @param       bgItemsId
	 * @param       ledgerId
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public JSONObject checkBgItemBeforeDelete(String bgItemsId,String ledgerId) throws Exception;
	/**
	 * 
	 * @methodName  cancelBudgetBal
	 * @author      tangxl
	 * @date        2016年6月21日
	 * @describe    预算单回退
	 * @param map
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public JSONObject cancelBudgetBal(HashMap<String, String> map) throws Exception;

}
