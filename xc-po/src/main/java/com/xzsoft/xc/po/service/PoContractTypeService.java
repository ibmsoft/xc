package com.xzsoft.xc.po.service;

import org.codehaus.jettison.json.JSONObject;

/** 
 * @ClassName: PoContractTypeService 
 * @Description: 合同类型Service层
 * @author weicw 
 * @date 2016年11月29日 下午4:45:17 
 * 
 * 
 */
public interface PoContractTypeService {
	/**
	 * @Title:saveContractType
	 * @Description:保存合同类型
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public JSONObject saveContractType(String recordsStr,String deleteRecords) throws Exception;
}

