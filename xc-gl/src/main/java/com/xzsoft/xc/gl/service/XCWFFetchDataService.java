package com.xzsoft.xc.gl.service;

import java.util.HashMap;


/**
 *@fileName XCGLFetchDataService 
 *@describe 云ERP获取内置流程的阈值
 *@author   tangxl
 *@date     2016年5月23日
 */
public interface XCWFFetchDataService {
	
	/**
	 * @methodName getBillQuota
	 * @describe   获取账簿信息
	 * @author     tangxl
	 * @date       2016年5月23日
	 * @param      ledgerId
	 * @param      billType
	 * @param      actCode
	 * @throws     Exception
	 */
	public HashMap<String,Object> getBillQuota(String ledgerId, String billType, String actCode) throws Exception;
	/**
     * 
     * @methodName  方法名
     * @author      tangxl
     * @date        2016年7月26日
     * @describe    获取预算单审批表单
     * @param 		bgCatCode 预算单类型 BG01-费用预算；BG02-成本预算；BG03-专项预算；BG04-资金预算 @see XCBGConstants
     * @param 		formType 1-电脑端审批表单；2-电脑端填报表单；3-移动端审批表单；4-移动端填报表单
     * @return
     * @变动说明       
     * @version     1.0
     */
    public String getBgBillAuditPage(String bgCatCode,String formType) throws Exception;
}
