package com.xzsoft.xc.gl.api;

import java.util.Map;

/**
 * @ClassName: RepCustGetDataService 
 * @Description: 报表自定义取数服务接口类
 * @author linp
 * @date 2016年7月21日 下午2:33:35 
 *
 */
public interface RepCustGetDataService {

	/**
	 * @Title: getGLBalValue 
	 * @Description: 获取云ERP总账余额表数据
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getGLBalValue(Map<String,Object> map) throws Exception ;
	
	/**
	 * @Title: getGLCashValue 
	 * @Description: 获取云ERP现金流量汇总数据
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getGLCashValue(Map<String,Object> map) throws Exception ;
	
}
