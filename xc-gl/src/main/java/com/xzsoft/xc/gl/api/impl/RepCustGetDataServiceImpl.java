package com.xzsoft.xc.gl.api.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.xzsoft.xc.gl.api.RepCustGetDataService;
import com.xzsoft.xc.gl.dao.RepCustGetDataDAO;
import com.xzsoft.xc.gl.util.XCGLCacheUtil;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.util.XipUtil;


/**
 * @ClassName: RepCustGetDataServiceImpl 
 * @Description: 报表自定义取数服务接口类实现
 * @author linp
 * @date 2016年7月21日 下午4:56:30 
 *
 */
@Service("repCustGetDataService") 
public class RepCustGetDataServiceImpl implements RepCustGetDataService {
	
	private RedisDao getRedisDao(){
		return (RedisDao) AppContext.getApplicationContext().getBean("redisDaoImpl") ;
	}
	private RepCustGetDataDAO getRepCustGetDataDAO(){
		return (RepCustGetDataDAO) AppContext.getApplicationContext().getBean("repCustGetDataDAO") ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getGLBalValue</p> 
	 * <p>Description: 获取云ERP总账余额表数据</p> 
	 * @param map
	 * <p>
	 * 	map参数构成说明：
	 * 		键					值描述
	 * 
	 *  	LEDGER_ID			账簿ID
	 *  	PERIOD				期间编码
	 *  	CURRENCY			币种编码
	 *  	BAL_TYPE			余额类型
	 *  	XC_GL_ACCOUNTS		科目
	 *  	XC_AP_VENDORS		供应商
	 *  	XC_AR_CUSTOMERS		客户
	 *  	XIP_PUB_ORGS		内部往来
	 *  	XIP_PUB_EMPS		个人往来
	 *  	XC_GL_PRODUCTS		产品
	 *  	XC_PM_PROJECTS		项目
	 *  	XIP_PUB_DEPTS		成本中心
	 *  	XC_GL_CUSTOM1		自定义维度1
	 *  	XC_GL_CUSTOM2		自定义维度2
	 *  	XC_GL_CUSTOM3		自定义维度3
	 *  	XC_GL_CUSTOM4		自定义维度4
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.api.RepCustGetDataService#getGLBalValue(java.util.Map)
	 */
	@Override
	public String getGLBalValue(Map<String, Object> map) throws Exception {
		String val = "0" ;
		String lang = XCUtil.getLang();
		try {
			// 校验参数是否合法
			if(!map.containsKey("ledgerId")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_NO_NULL", null))  ;
			if(!map.containsKey("periodCode")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_PERIOD_NO_NULL", null))  ;
			if(!map.containsKey("cnyCode")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CURRENCY_NO_NULL", null))  ;
			if(!map.containsKey("BAL_TYPE")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_BALANCE_NO_NULL", null))  ;
			if(!map.containsKey("XC_GL_ACCOUNTS")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCOUNT_NO_NULL", null))  ;
			
			// 参数转换
			this.convertParams(map);
			
			// 计算指标数据
			HashMap<String,Object> result = this.getRepCustGetDataDAO().getGLBalanceValue(map) ;
			
			if(result != null && result.containsKey("VAL")){
				Object value = result.get("VAL") ;
				BigDecimal bd = new BigDecimal(String.valueOf(value)).setScale(2, BigDecimal.ROUND_HALF_UP) ;
				val = bd.toPlainString() ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return val ;
	}
	
	/**
	 * @Title: convertParams 
	 * @Description: 参数转换
	 * @param map
	 * @throws Exception    设定文件
	 */
	private void convertParams(Map<String, Object> map) throws Exception {
		// 账簿信息
		String ledgerId = String.valueOf(map.get("ledgerId")) ;
		
		// 科目体系ID
		String hrcyId = String.valueOf(map.get("accHrcyId")) ;
		
		// 科目ID
		String accCode = String.valueOf(map.get("XC_GL_ACCOUNTS"));
		String accKey = XCRedisKeys.getAccKey(hrcyId) ;
		Object accJson = this.getRedisDao().hashGet(accKey, accCode) ;
		String accId = null ;
		if(accJson != null){
			JSONObject accJo = JSONObject.fromObject(String.valueOf(accJson)) ;
			accId = accJo.getString("id") ;
			map.put("accId", accId) ;
		}
		// 供应商
		String vendorId = null ;
		if(map.containsKey("XC_AP_VENDORS")){
			vendorId =this.getValueId(map, XConstants.XC_AP_VENDORS, null) ;	
			map.put("vendorId", vendorId) ;
		}else{
			map.put("vendorId", "00") ;
		}
		// 客户ID
		String customerId = null ;
		if(map.containsKey("XC_AR_CUSTOMERS")){
			customerId =this.getValueId(map, XConstants.XC_AR_CUSTOMERS, null) ;
			map.put("customerId", customerId) ;
		}else{
			map.put("customerId", "00") ;
		}
		// 内部往来ID
		String orgId = null ;
		if(map.containsKey("XIP_PUB_ORGS")){
			orgId =this.getValueId(map, XConstants.XIP_PUB_ORGS, null) ;		
			map.put("orgId", orgId) ;
		}else{
			map.put("orgId", "00") ;
		}
		// 个人往来ID
		String empId = null ;
		if(map.containsKey("XIP_PUB_EMPS")){
			empId =this.getValueId(map, XConstants.XIP_PUB_EMPS, null) ;		
			map.put("empId", empId) ;
		}else{
			map.put("empId", "00") ;
		}
		// 产品ID
		String prodId = null ;
		if(map.containsKey("XC_GL_PRODUCTS")){
			prodId = this.getValueId(map, XConstants.XC_GL_PRODUCTS, null) ;		
			map.put("prodId", prodId) ;
		}else{
			map.put("prodId", "00") ;
		}
		// 项目ID
		String prjId = null ;
		if(map.containsKey("XC_PM_PROJECTS")){
			prjId = this.getValueId(map, XConstants.XC_PM_PROJECTS, ledgerId) ;		
			map.put("prjId", prjId) ;
		}else{
			map.put("prjId", "00") ;
		}
		// 成本中心ID
		String deptId = null ;
		if(map.containsKey("XIP_PUB_DEPTS")){
			deptId = this.getValueId(map, XConstants.XIP_PUB_DETPS, ledgerId) ;
			map.put("deptId", deptId) ;
		}else{
			map.put("deptId", "00") ;
		}
		// 自定义维度1
		String cust1Id = null ;
		if(map.containsKey("XC_GL_CUSTOM1")){
			cust1Id = this.getValueId(map, XConstants.XC_GL_CUSTOM1, ledgerId) ;
			map.put("cust1Id", cust1Id) ;
		}else{
			map.put("cust1Id", "00") ;
		}
		// 自定义维度2
		String cust2Id = null ;
		if(map.containsKey("XC_GL_CUSTOM2")){
			cust2Id = this.getValueId(map, XConstants.XC_GL_CUSTOM2, ledgerId) ;
			map.put("cust2Id", cust2Id) ;
		}else{
			map.put("cust2Id", "00") ;
		}
		// 自定义维度3
		String cust3Id = null ;
		if(map.containsKey("XC_GL_CUSTOM3")){
			cust3Id = this.getValueId(map, XConstants.XC_GL_CUSTOM3, ledgerId) ;
			map.put("cust3Id", cust3Id) ;
		}else{
			map.put("cust3Id", "00") ;
		}
		// 自定义维度4
		String cust4Id = null ;
		if(map.containsKey("XC_GL_CUSTOM4")){
			cust4Id = this.getValueId(map, XConstants.XC_GL_CUSTOM4, ledgerId) ;
			map.put("cust4Id", cust4Id) ;
		}else{
			map.put("cust4Id", "00") ;
		}
	}
	
	/**
	 * @Title: getValueId 
	 * @Description: 获取辅助段值ID
	 * @param map
	 * @param segCode
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	private String getValueId(Map<String, Object> map, String segCode, String ledgerId)throws Exception {
		String valueId = "00" ;
		
		String valKey = (String) map.get(segCode) ;
		
		if(valKey == null || "".equals(valKey) || "00".equals(valKey)) return valueId ;
		
		String hashKey = null ;
		if(ledgerId != null){
			hashKey = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
		}else{
			hashKey = XCRedisKeys.getGlobalSegKey(segCode) ;
		}
		
		Object dataJson = this.getRedisDao().hashGet(hashKey, valKey) ;
		if(dataJson != null){
			JSONObject dataJo = JSONObject.fromObject(String.valueOf(dataJson)) ;
			valueId = dataJo.getString("id") ;
		}
		
		return valueId ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getGLCashValue</p> 
	 * <p>Description: 获取云ERP现金流量汇总数据 </p> 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.api.RepCustGetDataService#getGLCashValue(java.util.Map)
	 */
	@Override
	public String getGLCashValue(Map<String, Object> map) throws Exception {
		String val = "0" ;
		String lang = XCUtil.getLang();
		try {
			// 校验参数是否合法
			if(!map.containsKey("ledgerId")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_NO_NULL ", null))  ;
			if(!map.containsKey("periodCode")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_PERIOD_NO_NULL ", null))  ;
			if(!map.containsKey("CA_TYPE")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CASH_TYPE_NO_NULL", null))  ;
			if(!map.containsKey("XC_GL_CASH_ITEMS")) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CASH_ITEM_NO_NULL", null))  ;
			
			// 现金流量项目ID
			String cashCode = String.valueOf(map.get("XC_GL_CASH_ITEMS")) ;
			String cashKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_CASH_ITEMS) ;
			Object cashJson = this.getRedisDao().hashGet(cashKey, cashCode) ;
			String caId = null ;
			// 同步现金流量缓存数据
			if(cashJson == null || "".equals(cashJson)){
				XCGLCacheUtil.syncCashItem2Redis();
				cashJson = this.getRedisDao().hashGet(cashKey, cashCode) ;
			}
			
			try {
				JSONObject cashJo = JSONObject.fromObject(String.valueOf(cashJson)) ;
				caId = cashJo.getString("id") ;
				map.put("caId", caId) ;
				
			} catch (Exception e) {
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_CASH_ITEM", null)+"【"+cashCode+"】"+XipUtil.getMessage(lang, "XC_GL_SYSTEM_NO_EXIST", null)) ;
			}
			
			// 计算指标数据
			HashMap<String,Object> result = this.getRepCustGetDataDAO().getGlCashSumValue(map) ;
			
			if(result != null && result.containsKey("VAL")){
				Object value = result.get("VAL") ;
				BigDecimal bd = new BigDecimal(String.valueOf(value)).setScale(2, BigDecimal.ROUND_HALF_UP) ;
				val = bd.toPlainString() ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return val ;
	}
	
}
