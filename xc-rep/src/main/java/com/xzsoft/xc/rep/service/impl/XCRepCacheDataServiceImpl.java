package com.xzsoft.xc.rep.service.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.rep.dao.XCRepCommonDAO;
import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.ESColItemBean;
import com.xzsoft.xc.rep.modal.ESRowItemBean;
import com.xzsoft.xc.rep.modal.PreFormulaBean;
import com.xzsoft.xc.rep.service.XCRepCacheDataService;
import com.xzsoft.xc.rep.util.XCRepConstants;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.dao.RedisDao;


/**
 * @ClassName: XCRepCacheDataServiceImpl 
 * @Description: 云ERP报表模块数据缓存服务接口实现类
 * @author linp
 * @date 2016年8月9日 下午1:54:25 
 *
 */
@Service("xcRepCacheDataService")
public class XCRepCacheDataServiceImpl implements XCRepCacheDataService {
	// 日志记录器
	private static Logger log = LoggerFactory.getLogger(XCRepCacheDataService.class) ;
	@Autowired
	private XCRepCommonDAO xcRepCommonDAO ;
	@Autowired
	private RedisDao redisDaoImpl ;
	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doCacheItems</p> 
	 * <p>Description: 缓存行列指标信息  </p> 
	 * @param suitId
	 * @param itemType
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepCacheDataService#doCacheItems(java.lang.String, java.lang.String)
	 */
	@Override
	public void doCacheItems(String accHrcyId,String itemType) throws Exception {
		String language = XCUtil.getLang();
		try {
			switch(itemType){
			case XCRepConstants.XC_REP_ROWITEM :
				List<ESRowItemBean> rowitems = xcRepCommonDAO.getAllRowItems(accHrcyId) ;
				this.doBatchCache(itemType, rowitems, accHrcyId);
				break ;
				
			case XCRepConstants.XC_REP_COLITEM :
				List<ESColItemBean> colitems = xcRepCommonDAO.getAllColItems(accHrcyId) ;
				this.doBatchCache(itemType, colitems, accHrcyId);
				break;
				
			default :
				break ;
			}
			
		} catch (Exception e) {
			if(XConstants.ZH.equals(language)){
				log.error("行列指标信息缓存失败>>"+e.getMessage());
			}else{
				log.error("Fail to put row items and column items into cache,cause:>>"+e.getMessage());
			}
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doCacheFormulas</p> 
	 * <p>Description: 缓存指标计算公式信息 </p> 
	 * @param accHrcyId
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepCacheDataService#doCacheFormulas(java.lang.String)
	 */
	@Override
	public void doCacheFormulas(String accHrcyId) throws Exception {
		String language = XCUtil.getLang();
		try {
			List<CalFormulaBean> formulas = xcRepCommonDAO.getRepAllCalFormulas(accHrcyId) ;
			if(formulas != null && formulas.size() >0){
				this.doBatchCache(XCRepConstants.XC_REP_FORMULA, formulas, accHrcyId);
			}
		} catch (Exception e) {
			if(XConstants.ZH.equals(language)){
				log.error("取数公式信息缓存失败>>"+e.getMessage());
			}else{
				log.error("Fail to put data formulas into cache,cause:>>"+e.getMessage());
			}
			
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: doBatchCache</p> 
	 * <p>Description: 批量执行数据缓存处理 </p> 
	 * @param cacheType
	 * @param list
	 * @param key
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepCacheDataService#doCache(java.lang.String, java.util.List, net.sf.json.JSONObject)
	 */
	@Override
	public void doBatchCache(String cacheType,List<?> list, String key) throws Exception {
		if(list != null && list.size() >0){
			switch(cacheType){
			case XCRepConstants.XC_REP_ROWITEM :	// 行指标
				// 删除缓存
				String rowItemKey = XCRedisKeys.getRepCacheKey(cacheType, key) ;
				redisDaoImpl.del(rowItemKey);
				// 批量加载缓存
				for(Object obj : list){
					this.doCacheByObject(cacheType, obj, key);
				}
				break ;
				
			case XCRepConstants.XC_REP_COLITEM :	// 列指标
				// 删除缓存
				String colItemKey = XCRedisKeys.getRepCacheKey(cacheType, key) ;
				redisDaoImpl.del(colItemKey);
				// 批量加载缓存
				for(Object obj : list){
					this.doCacheByObject(cacheType, obj, key);
				}				
				break ;
				
			case XCRepConstants.XC_REP_FORMULA :	// 计算公式
				String formulaKey = XCRedisKeys.getRepCacheKey(cacheType, key) ;
				redisDaoImpl.del(formulaKey);
				// 批量加载缓存
				for(Object obj : list){
					this.doCacheByObject(cacheType, obj, key);
				}
				break ;
				
			default :
				break ;
			}			
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: doCacheByObject</p> 
	 * <p>Description: 执行单条数据缓存处理 </p> 
	 * @param cacheType
	 * @param obj
	 * @param keyJo
	 * @throws Exception 
	 * @see com.xzsoft.xc.rep.service.XCRepCacheDataService#doCacheByObject(java.lang.String, java.lang.Object, net.sf.json.JSONObject)
	 */
	@Override
	public void doCacheByObject(String cacheType, Object obj, String key) throws Exception {
		if(obj != null){
			// 排除属性
			JsonConfig cfg = new JsonConfig() ;
			cfg.setExcludes(new String[]{
				"createdBy","creationDate","lastUpdatedBy","lastUpdateDate"
			});
			
			// 缓存内容
			String val = null ;
			String masterKey = XCRedisKeys.getRepCacheKey(cacheType, key) ;
			
			switch(cacheType){
			case XCRepConstants.XC_REP_ROWITEM :	// 行指标
				ESRowItemBean rowItem = (ESRowItemBean) obj ;
				val = JSONObject.fromObject(rowItem, cfg).toString() ;
				redisDaoImpl.hashPut(masterKey, rowItem.getRowitemId(), val);
				redisDaoImpl.hashPut(masterKey, rowItem.getRowitemCode(), val);
				break ;
				
			case XCRepConstants.XC_REP_COLITEM :	// 列指标
				ESColItemBean colItem = (ESColItemBean) obj ;
				val = JSONObject.fromObject(colItem, cfg).toString() ;
				redisDaoImpl.hashPut(masterKey, colItem.getColitemId(), val);
				redisDaoImpl.hashPut(masterKey, colItem.getColitemCode(), val);
				break ;
				
			case XCRepConstants.XC_REP_FORMULA :	// 取数公式
				CalFormulaBean formula = (CalFormulaBean) obj ;
				JSONObject formulaJo = JSONObject.fromObject(formula, cfg) ;
				// 预处理信息
				List<PreFormulaBean> preFormulas = formula.getPreFormulaBeans() ;
				if(preFormulas != null && preFormulas.size()>0){
					formulaJo.put("preFormulaBeans", JSONArray.fromObject(preFormulas, cfg)) ;
				}
				redisDaoImpl.hashPut(masterKey, formula.getFormulaId(), formulaJo.toString());
				break ;
				
			default :
				break ;
			}
		}
	}
}
