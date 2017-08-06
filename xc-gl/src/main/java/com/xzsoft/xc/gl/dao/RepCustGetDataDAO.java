package com.xzsoft.xc.gl.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: RepCustGetDataDAO 
 * @Description: 云ERP报表模块自定义取数数据接口类
 * @author linp
 * @date 2016年7月22日 上午9:12:35 
 *
 */
public interface RepCustGetDataDAO {

	/**
	 * @Title: getGLBalanceValue 
	 * @Description: 获取总账余额表数据
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,Object> getGLBalanceValue(Map<String,Object> map) throws Exception ;
	
	/**
	 * @Title: getGlCashSumValue 
	 * @Description: 获取总账现金流量汇总余额
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,Object> getGlCashSumValue(Map<String,Object> map) throws Exception ;
}
