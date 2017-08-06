package com.xzsoft.xc.gl.service;

import net.sf.json.JSONObject;


/**
 * @ClassName: LedgerPeriodManageService 
 * @Description: 账簿会计期处理接口
 * @author tangxl
 * @date 2016年3月7日 上午10:47:30 
 *
 */
public interface LedgerPeriodManageService {
	
	/**
	 * @Title: appendledgerPeriod 
	 * @Description: (追加账簿的会计期间，同时处理追加期间后的余额记录)
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   ledgerId
	 * @param   currentPeriod
	 * @throws  Exception
	 */
	public void appendledgerPeriod(String ledgerId,String currentPeriod,String startPeriod) throws Exception;

	/**
	 * @Title: getAccountDate 
	 * @Description: 计算凭证录入、复制和冲销时的会计日期
	 * <p>
	 * 会计日期：
	 * 		1、如果当前日期所在账簿会计期为打开状态, 则取当前日期。
	 * 		2、如果没有打开, 则取小于当前日期的最大打开会计期最后一天作为会计日期。
	 * 		3、如果上述两种情况都不满足，则会计日期为空。
	 * </p>
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getAccountDate(String ledgerId) throws Exception ;
	/**
	 * 
	 * @title:  appendAdjustPeriod
	 * @Description:   追加账簿的调整会计期
	 * @author  tangxl
	 * @date    2016年3月28日
	 * @param   ledgerId
	 * @param   currentPeriod
	 * @param   startPeriod
	 * @throws  Exception
	 */
	public void appendAdjustPeriod(String ledgerId,String currentPeriod,String startPeriod) throws Exception;
	/**
	 * 
	 * @methodName  carryoverBalances
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    期末余额结转
	 * @param       jzTplId
	 * @param       periodCode
	 * @param       voucherDate
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public JSONObject carryoverBalances(String jzTplId,String periodCode,String voucherDate) throws Exception;
	
}
