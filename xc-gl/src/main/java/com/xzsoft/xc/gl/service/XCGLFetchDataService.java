package com.xzsoft.xc.gl.service;

import java.util.HashMap;

/**
 *@fileName XCGLFetchDataService 
 *@desc (总账对内对外取数接口，包括账簿信息、科目辅助段信息、凭证、总账以及现金流等) 
 *@author tangxl
 *@date 2016年3月16日
 */
public interface XCGLFetchDataService {
	
	/**
	 * @desc    获取账簿信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   lastUpdateDate
	 * @param   userName
	 * @param   userPwd
	 * @param fetchType
	 * @return  HashMap<String, String> key:synchronizeDate -- 同步日期 
	 *                                  key:data            -- 返回的数据 
	 * @throws Exception
	 */
	public HashMap<String, String> fecthLedgerData(String lastUpdateDate, String userName, String userPwd,String fetchType) throws Exception;
	
	/**
	 * @desc  获取科目段信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param dimType
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @param fetchType
	 * @return  HashMap<String, String> key:synchronizeDate -- 同步日期 
	 *                                  key:data            -- 返回的数据 
	 * @throws Exception
	 */
	public HashMap<String, String> fecthDimensionsData(String dimType, String lastUpdateDate, String ledgerId, String userName, String userPwd,String fetchType) throws Exception;

	/**
	 * @Title: fecthAccountData 
	 * @Description: 科目信息同步处理
	 * @param dimType
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @param fetchType
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String, String> fecthAccountData(String lastUpdateDate, String hrcyCode, String userName, String userPwd,String fetchType) throws Exception;
	
	/**
	 * @desc  获取余额信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param lastUpdateDate
	 * @param ledgerCode
	 * @param userName
	 * @param userPwd
	 * @param fetchType
	 * @return  HashMap<String, String> key:synchronizeDate -- 同步日期 
	 *                                  key:data            -- 返回的数据 
	 * @throws Exception
	 */
	public HashMap<String, String> fecthBalancesData(String lastUpdateDate, String ledgerCode, String userName, String userPwd,String fetchType) throws Exception;
	
	/**
	 * @desc  获取科目组合信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param lastUpdateDate
	 * @param ledgerCode
	 * @param userName
	 * @param userPwd
	 * @param fetchType
	 * @return  HashMap<String, String> key:synchronizeDate -- 同步日期 
	 *                                  key:data            -- 返回的数据 
	 * @throws Exception
	 */
	public HashMap<String, String> fecthCcidData(String lastUpdateDate, String ledgerCode, String userName, String userPwd,String fetchType) throws Exception;
	
	/**
	 * @desc  获取凭证信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @param fetchType
	 * @return  HashMap<String, String> key:synchronizeDate -- 同步日期 
	 *                                  key:data            -- 返回的数据 
	 * @throws Exception
	 */
	public HashMap<String, String> fecthVoucherData(String lastUpdateDate, String ledgerCode, String userName, String userPwd,String fetchType) throws Exception;
	
	/**
	 * @desc  获取现金流信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param lastUpdateDate
	 * @param ledgerId
	 * @param userName
	 * @param userPwd
	 * @param fetchType
	 * @return  HashMap<String, String> key:synchronizeDate -- 同步日期 
	 *                                  key:data            -- 返回的数据 
	 * @throws Exception
	 */
	public HashMap<String, String> fecthCashSumData(String lastUpdateDate, String ledgerCode, String userName, String userPwd,String fetchType) throws Exception;
	
	/**
	 * @desc    获取现金流项目信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   lastUpdateDate
	 * @param   userName
	 * @param   userPwd
	 * @param   fetchType
	 * @return  HashMap<String, String> key:synchronizeDate -- 同步日期 
	 *                                  key:data            -- 返回的数据 
	 * @throws Exception
	 */
	public HashMap<String, String> fetchCashItemDate(String lastUpdateDate, String userName, String userPwd,String fetchType) throws Exception;
}
