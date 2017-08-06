package com.xzsoft.xc.po.service;

import org.codehaus.jettison.json.JSONObject;

/** 
 * @ClassName: PoArrivalService
 * @Description: 到货单Service层接口
 * @author weicw
 * @date 2016年12月22日 下午1:53:13 
 * 
 */
public interface PoArrivalService {
	/**
	 * 
	  * @Title saveArrivalInfo 方法名
	  * @Description 保存到货单
	  * @param java.lang.String(主信息) java.lang.String(行信息) java.lang.String(删除的行ID) java.lang.String(行号)
	  * @return org.codehaus.jettison.json.JSONObject
	 */
	public JSONObject saveArrivalInfo(String formStr,String gridStr,String deleteIds,String lineNums) throws Exception;
	/**
	 * 
	  * @Title deleteArrivalInfo 方法名
	  * @Description 删除到货单
	  * @param java.lang.String(删除的主ID)
	  * @return org.codehaus.jettison.json.JSONObject
	 */
	public JSONObject deleteArrivalInfo(String deleteIds) throws Exception;
}
