package com.xzsoft.xc.ar.service;

import org.codehaus.jettison.json.JSONObject;

/** 
 * @className ArContractBaseService
 * @Description 合同管理service层
 * @author 韦才文
 * @version 创建时间：2016年7月7日 下午4:22:33 
 */
public interface ArContractBaseService {
	/**
	 * 
	  * @Title saveArContractInfo 方法名
	  * @Description 新增、修改、变更合同
	  * @param info
	  * @param opType	add：新增合同	edit：修改合同	change：变更合同
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject saveArContractInfo(String info,String opType) throws Exception;
	/**
	 * @Title:delContractInfo
	 * @Description: 删除合同
	 * 参数格式:
	 * @param  java.util.List
	 * @return 
	 * @throws 
	 */
	public JSONObject delContractInfo(String idsArray,String lang) throws Exception;
}
