package com.xzsoft.xc.ar.service;

import java.util.HashMap;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.modal.ArInvGlAdj;
import com.xzsoft.xc.ar.modal.ArInvGlHBean;
 
/** 
 * 
 * @author 张龙
 * @version 2016年7月20日
 */

public interface ArInvGlAdjService {
	/*
	 *调整单保存
	 */
	public JSONObject saveInvAdj(String params,String lang) throws Exception;
	
	/*
	 * 删除调整单
	 */
	public JSONObject deleteAdj(String rows,String arInfo,String lang) throws Exception;
	/*
	 * 审核
	 */
	public JSONObject checkArVoucher(String arInfo,String lang) throws Exception;
	/*
	 * 取消审核
	 */
	public JSONObject cancelCheckArVoucher(String arInfo,String lang) throws Exception;
}

