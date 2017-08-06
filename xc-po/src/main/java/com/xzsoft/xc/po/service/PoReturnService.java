package com.xzsoft.xc.po.service;

import org.codehaus.jettison.json.JSONObject;

/** 
 * @ClassName: PoReturnService
 * @Description: 退货单Service层接口
 * @author weicw
 * @date 2016年12月28日 上午11:09:40 
 * 
 */
public interface PoReturnService {
	/**
	 * @Title:saveReturnData
	 * @Description:保存退货单
	 * 参数格式:
	 * @param  java.lang.String(主信息) java.lang.String(行信息) java.lang.String(删除的主ID) java.lang.String(行号)
	 * @return 
	 * @throws 
	 */
	public JSONObject saveReturnData(String formStr,String gridStr,String deleteIds,String lineNums) throws Exception;
	/**
	 * @Title:delReturnData
	 * @Description:删除退货单
	 * 参数格式:
	 * @param  java.lang.String(主信息ID) 
	 * @return 
	 * @throws 
	 */
	public JSONObject delReturnData(String ids) throws Exception;
}
