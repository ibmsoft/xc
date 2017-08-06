/**
 * @Title:ArPayBaseService.java
 * @package:com.xzsoft.xc.ar.service
 * @Description:TODO
 * @date:2016年7月11日上午9:17:46
 * @version V1.0
 */
package com.xzsoft.xc.ar.service;

import org.codehaus.jettison.json.JSONObject;

/**
 * @ClassName: ArPayBaseService
 * @Description: TODO
 * @author:zhenghy
 * @date 2016年7月11日 上午9:17:46
 */

public interface ArPayBaseService {

	/**
	 * @Title: ArPayBaseService
	 * @Description: 删除收款单（包括明细）
	 * @param @param deleteRecs
	 * @param @return
	 * @param @throws Exception 
	 * @return 
	 * @throws
	 */
	public JSONObject deleteArPay(String deleteRecs,String language) throws Exception;
	/**
	 * @Title: saveArPay
	 * @Description: 保存收款单
	 * @param opType
	 * @param arPayH
	 * @param newRecs
	 * @param updateRecs
	 * @param deleteRecs
	 * @return
	 * @throws Exception  设定文件 
	 * @return  JSONObject 返回类型 
	 */
	public JSONObject saveArPay(String opType,String arPayH,String newRecs,String updateRecs,String deleteRecs,String language) throws Exception;
}
