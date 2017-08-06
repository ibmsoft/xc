package com.xzsoft.xc.rep.util;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.xzsoft.xc.gl.util.XCGLCacheUtil;
import com.xzsoft.xc.rep.dao.XCRepCommonDAO;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.service.XCRepCacheDataService;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: XCRepRedisCachedVars 
 * @Description: 获取云ERP报表Redis缓存数据信息
 * @author linp
 * @date 2016年8月8日 上午11:38:30 
 *
 */
public class XCRepCacheVarsUtil {
	
	// 获取IOC容器对象
	private static XCRepCacheDataService getXCRepCacheDataService(){
		return (XCRepCacheDataService) AppContext.getApplicationContext().getBean("xcRepCacheDataService") ; 
	}
	private static RedisDao getRedisDao(){
		return (RedisDao) AppContext.getApplicationContext().getBean("redisDaoImpl") ;
	}
	private static XCRepCommonDAO getXCRepCommonDAO(){
		return (XCRepCommonDAO) AppContext.getApplicationContext().getBean("xcRepCommonDAO") ;
	}
	

	/**
	 * @Title: getCachedData 
	 * @Description: 获取云ERP缓存数据信息
	 * @param cacheType	 ->  缓存数据分类
	 * @param key	-> 缓存数据主Key
	 * @param hashKey -> 数据ID
	 * @return
	 * @throws Exception    设定文件
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getCachedData(String cacheType, String key, String hashKey) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// 排除属性
		JsonConfig jsonConfig = new JsonConfig() ;
		jsonConfig.setExcludes(new String[]{
			"createdBy","creationDate","lastUpdatedBy","lastUpdateDate"
		});
		
		// 获取缓存数据
		Object valJson = null ;
		switch(cacheType){
		case XConstants.XIP_PUB_ORGS :		// 组织
			String entityKey = XCRedisKeys.getGlobalSegKey(XConstants.XIP_PUB_ORGS) ;
			valJson = getRedisDao().hashGet(entityKey, hashKey) ;
			// 加载缓存
			if(valJson == null || "".equals(valJson)){
				XCGLCacheUtil.syncBaseDataToRedis(null, null, XConstants.XIP_PUB_ORGS );
				valJson = getRedisDao().hashGet(entityKey, hashKey) ;
			}
			break ;
			
		case XCRepConstants.XC_REP_ROWITEM :	// 行指标
			String rowItemKey = XCRedisKeys.getRepCacheKey(cacheType, key) ;
			valJson = getRedisDao().hashGet(rowItemKey, hashKey) ;
			// 加载缓存
			if(valJson == null || "".equals(valJson)){
				ESRowItemBean rowItemBean = getXCRepCommonDAO().getRowItemByCode(key, hashKey) ;
				if(rowItemBean != null){
					jo = JSONObject.fromObject(rowItemBean, jsonConfig) ;	// 行指标信息
					if(PlatformUtil.getRedisTemplate().hasKey(rowItemKey)){
						getXCRepCacheDataService().doCacheByObject(XCRepConstants.XC_REP_ROWITEM, rowItemBean, key) ;
					}else{
						getXCRepCacheDataService().doCacheItems(key, XCRepConstants.XC_REP_ROWITEM);
					}
				}
			}
			break ;
			
		case XCRepConstants.XC_REP_COLITEM :	// 列指标
			String colItemKey = XCRedisKeys.getRepCacheKey(cacheType, key) ;
			valJson = getRedisDao().hashGet(colItemKey, hashKey) ;
			if(valJson == null || "".equals(valJson)){
				ESColItemBean colItemBean = getXCRepCommonDAO().getColItemByCode(key, hashKey) ;
				if(colItemBean != null){
					jo = JSONObject.fromObject(colItemBean, jsonConfig) ;	// 列指标信息
					if(PlatformUtil.getRedisTemplate().hasKey(colItemKey)){
						getXCRepCacheDataService().doCacheByObject(XCRepConstants.XC_REP_COLITEM, colItemBean, key) ;
					}else{
						getXCRepCacheDataService().doCacheItems(key, XCRepConstants.XC_REP_COLITEM);
					}
				}
			}
			break ;
			
		case XCRepConstants.XC_REP_FORMULA :
			String formulaKey = XCRedisKeys.getRepCacheKey(cacheType, key) ;
			valJson = getRedisDao().hashGet(formulaKey, hashKey) ;
			if(valJson == null || "".equals(valJson)){
				if(PlatformUtil.getRedisTemplate().hasKey(formulaKey)){
					
				}else{ // 批量加载
					getXCRepCacheDataService().doCacheFormulas(key);
				}
			}
			break ;
			
		default :
			break ;
		}	
		
		// 返回值
		if(valJson != null){
			jo = JSONObject.fromObject(valJson) ;
		}
		
		return jo ;
	}
}
