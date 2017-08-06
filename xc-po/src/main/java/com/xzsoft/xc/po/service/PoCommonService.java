package com.xzsoft.xc.po.service;


import org.codehaus.jettison.json.JSONObject;

/** 
 * @ClassName: PoCommonService
 * @Description: 采购管理公共Service
 * @author weicw
 * @date 2016年12月12日 上午9:40:31 
 * 
 */
public interface PoCommonService {
	/**
	 * @Title:getLdCatInfo
	 * @Description:根据采购单据类型获取审批流程和审批表单信息 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public JSONObject getProcAndFormsByCat(String ledgerId,String docCat) throws Exception;
	
}
