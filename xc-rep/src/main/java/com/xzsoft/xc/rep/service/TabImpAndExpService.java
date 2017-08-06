package com.xzsoft.xc.rep.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: TabImpAndExpService 
 * @Description: 模板导出与接收处理
 * @author linp
 * @date 2016年10月19日 下午4:23:07 
 *
 */
public interface TabImpAndExpService {

	/**
	 * @Title: expTabs 
	 * @Description: 模板导出
	 * @param accHrcyId
	 * @param tabs
	 * @throws Exception    设定文件
	 */
	public Map<String,Object> expTabs(String accHrcyId, List<String> tabs) throws Exception ;
	
	/**
	 * @Title: impTabs 
	 * @Description: 模板接收
	 * @param map
	 * @throws Exception    设定文件
	 */
	public void impTabs(Map<String,Object> map) throws Exception ;
	
}
