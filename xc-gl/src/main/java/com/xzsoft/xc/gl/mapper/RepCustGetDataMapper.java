package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: RepCustGetDataMapper 
 * @Description: 云ERP报表模块自定义取数Mapper 
 * @author linp
 * @date 2016年7月22日 上午9:22:41 
 *
 */
public interface RepCustGetDataMapper {

	/**
	 * @Title: getBalance 
	 * @Description: 获取总账科目余额数据
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> getBalance(Map<String,Object> map)  ;
	
	/**
	 * @Title: getCashPTD 
	 * @Description: 获取总账现金流量项本期汇总数 
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> getCashPTD(Map<String,Object> map)  ;
	
	/**
	 * @Title: getCashYTD 
	 * @Description: 获取总账现金流量项本年汇总数 
	 * @param map
	 * @return    设定文件
	 */
	public HashMap<String,Object> getCashYTD(Map<String,Object> map)  ;
	
}
