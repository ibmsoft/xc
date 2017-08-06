package com.xzsoft.xc.po.service;

import java.util.HashMap;

import org.codehaus.jettison.json.JSONObject;

/** 
 * @ClassName: PoPurApHService
 * @Description: 采购申请Service接口
 * @author weicw
 * @date 2016年12月5日 上午10:59:54 
 * 
 */
public interface PoPurApHService {
	/**
	 * @Title:saveInfo
	 * @Description:保存采购申请
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public JSONObject saveInfo(String formStr,String gridStr,String deleteIds) throws Exception;
	/**
	 * @Title:deleteInfo
	 * @Description:删除采购申请
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public JSONObject deleteInfo(String ids) throws Exception;
	/**
	 * @Title:editPurIsClose
	 * @Description:关闭或打开采购申请单 
	 * 参数格式:
	 * @param  java.lang.String(申请单ID数组) java.lang.String(操作：关闭，打开)
	 * @return 
	 * @throws 
	 */
	public JSONObject editPurIsClose(String ids,String operate)throws Exception;
}
