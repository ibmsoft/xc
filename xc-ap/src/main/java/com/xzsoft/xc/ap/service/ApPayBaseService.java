/**
 * @Title:ApPayBaseService.java
 * @package:com.xzsoft.xc.ap.service
 * @Description:TODO
 * @date:2016年8月5日下午6:03:04
 * @version V1.0
 */
package com.xzsoft.xc.ap.service;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

/**
 * @ClassName: ApPayBaseService
 * @Description: 付款单相关操作
 * @author:zhenghy
 * @date: 2016年8月5日 下午6:03:04
 */
public interface ApPayBaseService {
	/**
	 * 
	 * @Title: saveApPay  
	 * @Description: 保存单据（新增和修改）  
	 * @param apPayH
	 * @param newPayLs
	 * @param updatePayLs
	 * @param deletePayLs
	 * @param opType
	 * @return
	 * @throws Exception     
	 * @return: JSONObject    
	 *
	 */
	public JSONObject saveApPay(String apPayH,String newPayLs,String updatePayLs,String deletePayLs,String opType,String language) throws Exception;
	/**
	 * 
	 * @Title: deleteApPay  
	 * @Description: 删除付款单
	 * @param apPayHIds
	 * @return
	 * @throws Exception     
	 * @return: JSONObject    
	 *
	 */
	public JSONObject deleteApPay(String deleteRecs,String language) throws Exception;
	/**
	 * @Title: updatePayAmt
	 * @Description: 重新计算付款单金额
	 * @param list 付款单id集合
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 */
	public void updatePayAmt(List<String> list) throws Exception;
}
