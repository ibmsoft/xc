package com.xzsoft.xc.ap.service;

import java.util.HashMap;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ap.modal.ApInvGlAdj;
import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
 
/** 
 * 
 * @author 韦才文
 * @version 2016年7月20日
 */

public interface ApInvGlAdjService {
	/**
	 * @Title saveInvAdj
	 * @Description: 保存调整单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject saveInvAdj(String params,String lang) throws Exception;
	
	/**
	 * @Title deleteAdj
	 * @Description: 删除调整单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject deleteAdj(String rows,String arInfo,String lang) throws Exception;
	
	/**
	 * @Title checkArVoucher
	 * @Description: 审核
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject checkArVoucher(String arInfo,String lang) throws Exception;
	
	/**
	 * @Title cancelCheckArVoucher
	 * @Description: 取消审核
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject cancelCheckArVoucher(String arInfo,String lang) throws Exception;
}

