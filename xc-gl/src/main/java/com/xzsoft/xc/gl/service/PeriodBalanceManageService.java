package com.xzsoft.xc.gl.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.GlBalance;

/**
 * @ClassName: PeriodBalanceManageService 
 * @Description: 账簿余额计算处理接口
 * @author tangxl
 * @date 2016年1月7日 上午10:47:30 
 *
 */
public interface PeriodBalanceManageService {
	/**
	 * 
	 * @Title: saveGlPeriodBalances 
	 * @Description: TODO(将excel中的数据保存到临时账簿余额表) 
	 * @param inputStream
	 * @param ledgerId
	 * @param balanceType
	 * @param periodCode
	 * @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public String saveGlPeriodBalances(InputStream inputStream,String ledgerId,String balanceType,String periodCode,String excelVersion) throws Exception;
	/**
	 * 
	 * @Title: deletePeriodBalances 
	 * @Description: TODO(根据账簿id和sessionId<当前活跃session>删除临时导入表中的过期数据) 
	 * @param @param ledgerId
	 * @param @param sessionId
	 * @param @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void deletePeriodBalances(String ledgerId,String sessionId) throws Exception;
	/**
	 * 
	 * @Title: checkExcelBalanceData 
	 * @Description: TODO(校验导入数据的合法性) 
	 * @param @param map
	 * @throws Exception    设定文件 
	 * @return String    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void fillExcelDataCCID(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @Title: insertExcelDataToTmp 
	 * @Description: TODO(插入Excel数据到余额临时表中) 
	 * @throws Exception    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 */
	public void  insertExcelDataToTmp(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @Title: getExportBalanceData 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return List<HashMap>    返回类型 
	 * @throws Exception    设定文件 
	 * @auther tangxl
	 * @throws
	 */
	public List<HashMap> getExportBalanceData(HashMap<String, String> map) throws Exception;
	/**
	 *@Title: deleteCashTmp
	 *@Description: TODO(删除沉淀的冗余数据)
	 *@param 2016年3月9日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	public void deleteCashTmp(String ledgerId,String sessionId) throws Exception;
	/**
	 * @throws Exception 
	 *@Title: saveCashTmp
	 *@Description: TODO(保存到临时表中)
	 *@param 2016年3月9日  设定文件
	 *@return String 返回类型
	 *@author lizhh
	 *@throws
	 */
	public String saveCashTmp(InputStream inputStream,String ledgerId,String excelVersion,String session_id) throws Exception;
	/**
	 *@Title: fillCashItemId
	 *@Description: TODO(更新现金流项目的ca_id和会计科目的acc_id)
	 *@param 2016年3月11日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	public void fillCashItemId(HashMap<String, String> map) throws Exception;
	
	/**
	 *@Title: getStartPeriod_codeByLedger_id
	 *@Description: TODO(根据账簿id获取到要插入的期初余额的会计期)
	 *@param 2016年3月22日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	public String getStartPeriod_codeByLedger_id(String ledgerId);
	/**
	 * 
	 * @title:         checkTheBalance
	 * @description:   检查余额是否平等
	 * @author  tangxl
	 * @date    2016年4月22日
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public String checkTheBalance(HashMap<String, String> map) throws Exception;

}
