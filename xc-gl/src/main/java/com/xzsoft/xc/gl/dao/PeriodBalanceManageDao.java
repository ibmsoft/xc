package com.xzsoft.xc.gl.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.BalanceForExcel;
import com.xzsoft.xc.gl.modal.CashForExcel;
import com.xzsoft.xc.util.modal.Ledger;


/**
 * 
* @Title: PeriodBalanceManageDao.java
* @Description:账簿余额管理取数接口
* @author tangxl  
* @date 2016年1月20日 上午9:43:49
* @version V1.0
 */
public interface PeriodBalanceManageDao {
	/**
	 * 
	 * @Title: getLedgerInfoById 
	 * @Description: TODO(根据账簿ID获取账簿的相关信息) 
	 * @param ledgerId
	 * @return Ledger    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public Ledger getLedgerInfoById(String ledgerId);
	/**
	 * 
	 * @Title: insertExcelDataToBalanceImp 
	 * @Description: TODO(将数据插入到临时导入表中) 
	 * @param balanceList
	 * @throws Exception    设定文件 
	 * @return void         返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void insertExcelDataToBalanceImp(List<BalanceForExcel> balanceList,String sessionId,String ledgerId) throws Exception;
	/***
	 * @Title: getInvalidImportData 
	 * @Description: TODO(获取Excel余额导入的不合法数据) 
	 * @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public List<HashMap> getInvalidImportData(HashMap<String, String> map);
	/**
	 * 
	 * @Title: instoreExcelData 
	 * @Description: TODO(将余额导入表中的数据转存到临时余额表中) 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void instoreExcelData(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @Title: deletePeriodBalances 
	 * @Description: TODO(删除临时表中过期数据) 
	 * @param ledgerId
	 * @param sessionId
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void deletePeriodBalances(String ledgerId,String sessionId) throws Exception;
	/**
	 * 
	 * @Title: getExportBalanceData 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return  List<HashMap>    返回类型 
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
	 *@Title: insertCashTmp
	 *@Description: TODO(将excel中的数据都插入到临时表中去)
	 *@param 2016年3月10日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	public void insertCashTmp(List<CashForExcel> list)throws Exception;
	/**
	 *@Title: getInvalidFromCashTmp
	 *@Description: TODO(获取到无效的数据)
	 *@param 2016年3月10日  设定文件
	 *@return List<HashMap> 返回类型
	 *@author lizhh
	 *@throws
	 */
	public List<HashMap> getInvalidFromCashTmp(HashMap<String,String> map)throws Exception;
	/**
	 *@Title: SetCaIdAndAccIdByCode
	 *@Description: TODO(根据会计科目和现金流量项的编码设置id)
	 *@param 2016年3月10日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	public void setCaIdAndAccIdByCode(List<CashForExcel> list)throws Exception;
	/**
	 *@Title: setFlagById
	 *@Description: TODO(设置是否有效)
	 *@param 2016年3月10日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	 public void setFlag(String ledgerId,String session_id)throws Exception;
		/**
		 *@Title: saveSumFormTmp
		 *@Description: TODO(将临时表中的有效数据插入到sum表中)
		 *@param 2016年3月11日  设定文件
		 *@return String 返回类型
		 *@author lizhh
		 *@throws
		 */
	 public void saveSumFormTmp(HashMap<String, String> map)throws Exception;
	 
	/**
	 *@Title: updateSumIfHave
	 *@Description: TODO(如何sum表已经存在，则进行更新)
	 *@param 2016年3月11日  设定文件
	 *@return void 返回类型
	 *@throws
	 */
	public void updateSumIfHave(HashMap<String, String> map)throws Exception;
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
	 * @title:  getCheckBalanceList
	 * @description:   获取待验证的余额记录
	 * @author  tangxl
	 * @date    2016年4月22日
	 * @param map  ledgerId  periodCode
	 * @return
	 * @throws Exception
	 */
	public List<BalanceForExcel> getCheckBalanceList(HashMap<String, String> map) throws Exception;
}
