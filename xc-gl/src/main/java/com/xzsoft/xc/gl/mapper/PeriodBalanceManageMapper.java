package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.modal.BalanceForExcel;
import com.xzsoft.xc.gl.modal.CashForExcel;

/**
 * @ClassName: PeriodBalanceManageMapper 
 * @Description: 余额管理取数接口
 * @author tangxl
 * @date 2015年12月29日 下午6:15:39 
 *
 */
public interface PeriodBalanceManageMapper {
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
	public void insertExcelDataToBalanceImp(List<BalanceForExcel> balanceList) throws Exception;
	/**
	 * 
	 * @Title: checkAccExist 
	 * @Description: TODO(校验科目是否存在) 
	 * @param @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void checkAccExist(HashMap<String, String> map);
	/**
	 * @Title: checkVendorExist 
	 * @Description: TODO(校验供应商是否存在) 
	 * @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void checkVendorExist(HashMap<String, String> map);
	/**
	 * @Title: checkCustomerExist 
	 * @Description: TODO(校验客户是否存在) 
	 * @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void checkCustomerExist(HashMap<String, String> map);
	/**
	 * @Title: checkProductExist 
	 * @Description: TODO(校验产品信息是否存在) 
	 * @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void checkProductExist(HashMap<String, String> map);
	
	/**
	 * @Title: checkInnerOrgExist 
	 * @Description: TODO(校验内部往来是否存在) 
	 * @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void checkInnerOrgExist(HashMap<String, String> map);
	/**
	 * @Title: checkPersonalExist 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void checkPersonalExist(HashMap<String, String> map);
	/***
	 * 
	 * @Title: checkCostExist 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void checkCostExist(HashMap<String, String> map);
	/**
	 * 
	 * @Title: checkProjectExist 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sessionId    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void checkProjectExist(HashMap<String, String> map);
	/**
	 *自定义段信息校验 
	 */
	public void checkSD01Exist(HashMap<String, String> map);
	
	public void checkSD02Exist(HashMap<String, String> map);
	
	public void checkSD03Exist(HashMap<String, String> map);
	
	public void checkSD04Exist(HashMap<String, String> map);
	/**
	 * 
	 * @Title: getInvalidImportData 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param sessionId
	 * @return List<BalanceForExcel>    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public List<HashMap> getInvalidImportData(HashMap<String, String> map);
	/**
	 * 
	 * @Title: deletePeriodBalances 
	 * @Description: TODO(删除余额临时导入表中的数据) 
	 * @param  map    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void deletePeriodBalances(HashMap<String, String> map);
	/**
	 * 
	 * @Title: insertDaraFromImp 
	 * @Description: TODO(将校验通过的Excel数据插入到临时表中) 
	 * @param map    设定文件 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public void insertDataFromImp(HashMap<String, String> map);
	/**
	 * 
	 * @Title: getExportBalanceData 
	 * @Description: TODO(获取余额导出的数据) 
	 * @param map
	 * @return List<BalanceForExcel>    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	public List<HashMap> getExportBalanceData(HashMap<String, String> map);
	/**
	 *@Title: deleteCashTmp
	 *@Description: TODO(删除沉淀冗余数据)
	 *@param 2016年3月9日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	public void deleteCashTmp(HashMap<String,String> map);

	/**
	 *@Title: insertCashTmp
	 *@Description: TODO(将excel中的数据都插入到临时表中去)
	 *@param 2016年3月10日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	public void insertCashTmp(List<CashForExcel> list);
	/**
	 *@Title: getInvalidFromCashTmp
	 *@Description: TODO(获取到无效的数据)
	 *@param 2016年3月10日  设定文件
	 *@return List<HashMap> 返回类型
	 *@author lizhh
	 *@throws
	 */
	public List<HashMap> getInvalidFromCashTmp(HashMap<String,String> map);
	/**
	 *@Title: checkforCashTmp
	 *@Description: TODO(如果会计科目和现金流量项都存在，设置为flag="0"反之为"1")
	 *@param 2016年3月10日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	public void setCaIdAndAccIdByCode(HashMap<String,String> map);
	/**
	 *@Title: checkCaExists
	 *@Description: TODO(设置是否有效)
	 *@param 2016年3月10日  设定文件
	 *@return void 返回类型
	 *@throws
	 */
	 public void checkCaExists(HashMap<String,String> map);

	/**
	 * @Title: checkCaExists
	 * @Description: TODO(设置是否有效)
	 * @param 2016年3月10日 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void checkAccExists(HashMap<String, String> map);

	/**
	 * @Title: saveSumFormTmp
	 * @Description: TODO(将临时表中的有效数据插入到sum表中)
	 * @param 2016年3月11日 设定文件
	 * @return String 返回类型
	 * @author lizhh
	 * @throws
	 */
	public void saveSumFormTmp(HashMap<String, String> map);
	/**
	 * @Title: updateSumIfHave
	 * @Description: TODO(如何sum表已经存在，则进行更新)
	 * @param 2016年3月11日 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void updateSumIfHave(HashMap<String, String> map);

	/**
	 * @Title: getStartPeriod_codeByLedger_id
	 * @Description: TODO(根据账簿id获取到账簿的开始会计期)
	 * @param 2016年3月22日 设定文件
	 * @return void 返回类型
	 * @author lizhh
	 * @throws
	 */
	public String getStartPeriod_codeByLedger_id(String ledgerId);
	/**
	 * 
	 * @title:  getCheckBalanceList
	 * @description: 获取待检查的余额，只校验未建账的数据
	 * @author  tangxl
	 * @date    2016年4月22日
	 * @param map
	 * @return
	 */
	public List<BalanceForExcel> getCheckBalanceList(HashMap<String, String> map);	
}
