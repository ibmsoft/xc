package com.xzsoft.xc.rep.service;

import java.util.List;

/**
 * @ClassName: XCRepCacheDataService 
 * @Description: 云ERP报表模块数据缓存服务接口类
 * @author linp
 * @date 2016年8月8日 上午11:51:25 
 *
 */
public interface XCRepCacheDataService {
	
	/**
	 * @Title: doCacheItems 
	 * @Description: 缓存行列指标信息 
	 * @param accHrcyId
	 * @param itemType
	 * @throws Exception    设定文件
	 */
	public void doCacheItems(String accHrcyId,String itemType) throws Exception ;
	
	/**
	 * @Title: doCacheFormulas 
	 * @Description: 缓存指标计算公式信息
	 * @param accHrcyId
	 * @throws Exception    设定文件
	 */
	public void doCacheFormulas(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: doBatchCache 
	 * @Description: 批量执行数据缓存处理
	 * @param cacheType
	 * @param list
	 * @param key
	 * @throws Exception    设定文件
	 */
	public void doBatchCache(String cacheType,List<?> list, String key) throws Exception ;
	
	/**
	 * @Title: doCacheByObject 
	 * @Description: 执行单条数据缓存处理
	 * @param cacheType
	 * @param obj
	 * @param key
	 * @throws Exception    设定文件
	 */
	public void doCacheByObject(String cacheType, Object obj, String key) throws Exception ;
	
}
