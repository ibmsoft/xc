package com.xzsoft.xc.po.mapper;

import java.util.HashMap;

/** 
 * @ClassName: PoCommonMapper
 * @Description: 采购管理公共mapper层 
 * @author weicw
 * @date 2016年12月12日 上午9:35:46 
 * 
 */
public interface PoCommonMapper {
	/**
	 * @Title:getLdCatInfo
	 * @Description:获取账簿费用单据信息
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	public HashMap<String, String> getLdCatInfo(HashMap<String, String> map);
	/**
	 * @Title:getGbCatInfo
	 * @Description:获取全局费用单据类型信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	public HashMap<String, String> getGbCatInfo(String docCat);
}
