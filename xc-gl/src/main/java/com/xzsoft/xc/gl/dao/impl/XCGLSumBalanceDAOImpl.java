package com.xzsoft.xc.gl.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO;
import com.xzsoft.xc.gl.mapper.SumBalanceMapper;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.CCID;
import com.xzsoft.xc.gl.modal.CashItem;
import com.xzsoft.xc.gl.modal.Period;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: SumBalanceServiceDAOImpl 
 * @Description: 按凭证执行余额汇总处理数据层接口实现类
 * @author linp
 * @date 2016年1月7日 上午10:48:29 
 */
@Repository("xcglSumBalanceDAOImpl")
public class XCGLSumBalanceDAOImpl implements XCGLSumBalanceDAO {
	// 日志记录器
	private static final Logger log = Logger.getLogger(XCGLSumBalanceDAOImpl.class.getName()) ;
	@Resource
	private SumBalanceMapper sumBalanceMapper ;
	@Resource
	private RedisDao redisDaoImpl ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: getSumAccounts</p> 
	 * <p>Description: 凭证下待汇总的科目信息 </p> 
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.SumBalanceServiceDAO#getSumAccounts(java.lang.String)
	 */
	@Override
	public List<HashMap<String,String>> getSumAccounts(String headId,String isContainForeign,String isAccount) throws Exception {
		// 参数信息
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("headId", headId) ;
		map.put("isContainForeign", isContainForeign) ;
		map.put("isAccount", isAccount) ;
		
		// 判断当前凭证下的所有科目
		return sumBalanceMapper.getAllSumAccounts(map) ; 
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAfterPeriods</p> 
	 * <p>Description: 获取当前期间向下的期间 </p> 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#getAfterPeriods(java.util.HashMap)
	 */
	@Override
	public List<Period> getAfterPeriods(String ledgerId,String periodCode)throws Exception {
		HashMap<String,String> paramMap = new HashMap<String,String>() ;
		paramMap.put("ledgerId", ledgerId) ;
		paramMap.put("periodCode", periodCode) ;
		paramMap.put("dbType", PlatformUtil.getDbType()) ;
		return sumBalanceMapper.getAfterPeriods(paramMap) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCurrency4Ledger</p> 
	 * <p>Description: 获取账簿的本位币 </p> 
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#getCurrency4Ledger(java.lang.String)
	 */
	@Override
	public String getCurrency4Ledger(String headId) throws Exception {
		return sumBalanceMapper.getCurrency4Ledger(headId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCurrency4Acc</p> 
	 * <p>Description: 获取科目的默认币种 </p> 
	 * @param accId
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#getCurrency4Acc(java.lang.String, java.lang.String)
	 */
	@Override
	public String getCurrency4Acc(String accId,String ledgerId)throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("accId", accId) ;
		return sumBalanceMapper.getCurrency4Acc(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getHrcyIdByHeadId</p> 
	 * <p>Description: 根据凭证头ID查询科目体系ID </p> 
	 * @param headId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.SumBalanceServiceDAO#getHrcyIdByHeadId(java.lang.String)
	 */
	@Override
	public String getHrcyIdByHeadId(String headId) throws Exception {
		return sumBalanceMapper.getHrcyIdByHeadId(headId) ;
	}

	/**
	 * @Title: getJsonStr 
	 * @Description: 从Redis数据库中获取缓存信息
	 * @param key
	 * @param hashKey
	 * @return
	 * @throws Exception    设定文件
	 */

	public String getJsonStr(String key,String hashKey) throws Exception {
		String json = null ;
		try {
//				redis.clients.jedis.Jedis jedis = com.xzsoft.xc.gl.util.JedisUtil.getJedis() ;
//				json = String.valueOf(jedis.hget(key, hashKey)) ;
//				json = json.substring(json.indexOf("{")) ;
			
			Object obj = redisDaoImpl.hashGet(key, hashKey) ;
			
			// 刷新会计科目、现金流量项目和CCID缓存信息
			if(obj == null){
				if(key.indexOf(XConstants.XC_GL_ACCOUNTS) != -1){	// 科目
					// 查询科目信息
					String hrcyId = key.substring(key.lastIndexOf(".")+1, key.length()) ;
					List<Account> accounts = xcglCommonDAO.getAllAccountsByHrcy(hrcyId) ;
					
					// 将科目信息同步到Redis数据库
					if(accounts != null && accounts.size() >0){
						// 批量清除某个会计科目体系信息
//						redisDaoImpl.del(key);
						
						for(Account acc : accounts){
							JSONObject jo = new JSONObject() ;
							jo.put("id", acc.getAccId()) ;
							jo.put("code", acc.getAccCode()) ;
							jo.put("name", acc.getAccName()) ;
							jo.put("upId", acc.getUpAccId()) ;
							
							// 判断科目是否为银行或现金科目
							if("Y".equals(acc.getIsBankAcc()) || "Y".equals(acc.getIsCashAcc())){
								jo.put("isCash", "Y") ;
							}else{
								jo.put("isCash", "N") ;
							}
							jo.put("isLeaf", acc.getIsLeaf()) ;
							
							String valJson = jo.toString() ;
							redisDaoImpl.hashPut(key, acc.getAccId(), valJson);
							redisDaoImpl.hashPut(key, acc.getAccCode(), valJson);
						}
					}else{
						// 批量清除某个会计科目体系信息
						redisDaoImpl.del(key);
					}
					
					
				}else if(key.indexOf(XConstants.XC_GL_CCID) != -1){ 
					// 查询CCID信息
					String ldId = key.substring(key.lastIndexOf(".")+1, key.length()) ;
					List<CCID> ccids = xcglCommonDAO.getCCID4Ledger(ldId) ;
					
					if(ccids != null && ccids.size() >0){
						// 执行缓冲清除处理
//						redisDaoImpl.del(key);
						
						// 执行数据同步处理
						for(CCID ccid : ccids){
							JSONObject jo = new JSONObject();
							jo.put("id", ccid.getCcid()) ;
							jo.put("code", ccid.getCcidCode()) ;
							jo.put("name", ccid.getCcidName()) ;
							String valueJson = jo.toString() ;
							
							redisDaoImpl.hashPut(key, ccid.getCcidCode(), valueJson);
							redisDaoImpl.hashPut(key, ccid.getCcid(), valueJson);
						}
					}else{
						// 执行缓冲清除处理
						redisDaoImpl.del(key);
					}				
					
					
				}else if(key.indexOf(XConstants.XC_GL_CASH_ITEMS) != -1){ // 现金流量项目
					// 查询现金流量项目信息
					List<CashItem> items = xcglCommonDAO.getCashItems() ;
					if(items != null && items.size() >0){
						// 批量清除现金流量项目
//						redisDaoImpl.del(key);
						
						// 批量缓存现金流量项目
						for(CashItem item : items){
							JSONObject jo = new JSONObject() ;
							jo.put("id", item.getCaId()) ;
							jo.put("code", item.getCaCode()) ;
							jo.put("name", item.getCaName()) ;
							jo.put("upId", item.getUpId()) ;
							jo.put("direction", item.getCaDirection()) ;
							String valueJson = jo.toString() ;
							
							// 按照现金流量项目ID
							redisDaoImpl.hashPut(key, item.getCaId(), valueJson);
							// 按现金流量项目编码
							redisDaoImpl.hashPut(key, item.getCaCode(), valueJson);
						}
						
						
					}else{
						// 批量清除现金流量项目
						redisDaoImpl.del(key);
					}
				}else{
					
				}
				// 刷新后再获取数据
				obj = redisDaoImpl.hashGet(key, hashKey) ;
			}
			
			// 返回值
			if(obj == null){
				json = null ;
			}else{
				json = String.valueOf(obj) ;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return json ;
	}

	/**
	 * @Title: getPrePeriodCode 
	 * @Description: 计算上期期间 
	 * @param periodCode
	 * @return
	 * @throws Exception    设定文件
	 */
	@Override
	public String getPrePeriodCode(String periodCode)throws Exception {
		String prePeriodCode = null ;
		String year = periodCode.substring(0,4);
		String month = periodCode.substring(5,7);
		int m = Integer.parseInt(month) ;
		if(m > 1){
			if(m>1 && m<11){
				// 2到10月份
				prePeriodCode = year.concat("-0").concat(String.valueOf(m-1)) ;
				
			}else{
				// 11和12月份
				prePeriodCode = year.concat("-").concat(String.valueOf(m-1)) ;
			}
		}else{
			prePeriodCode = String.valueOf(Integer.parseInt(year)-1).concat("-12") ;
		}
		
		return prePeriodCode ;
	}

	/**
	 * @Title: getPrePeriodCode 
	 * @Description: 计算上期期间
	 * @param periodCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getPrePeriodCode(String periodCode, String ledgerId) throws Exception {
		// 上期期间
		String prePeriodCode = null ;
		// 年份和月份
		String year = periodCode.trim().substring(0,4) ;
		String month = periodCode.trim().substring(5,7) ;
		// 计算上期期间
		if(month.equals("Q1")){
			prePeriodCode = year.concat("-").concat("03") ;
			
		}else if(month.equals("Q2")){
			prePeriodCode = year.concat("-").concat("06") ;
			
		}else if(month.equals("Q3")){
			prePeriodCode = year.concat("-").concat("09") ;
			
		}else if(month.equals("Q4")){
			prePeriodCode = year.concat("-").concat("12") ;
			
		}else{
			// 非调整期间
			int m = Integer.parseInt(month) ;
			if(m == 1){
				// 上年度
				String preYear = String.valueOf(Integer.parseInt(year)-1) ;
				// 计算上期期间
				HashMap<String,String> map =  new HashMap<String,String>() ;
				map.put("ledgerId", ledgerId) ;
				map.put("periodCode", preYear.concat("-Q4")) ;
				String isEnableAdj = sumBalanceMapper.isEnabeQ4Adj(map) ;
				if("1".equals(isEnableAdj)){
					prePeriodCode = preYear.concat("-Q4") ;
				}else{
					prePeriodCode = preYear.concat("-12") ;
				}				
				
			}else if(m>1 && m<11){
				// 2到10月份
				prePeriodCode = year.concat("-0").concat(String.valueOf(m-1)) ;
				
			}else{
				// 11和12月份
				prePeriodCode = year.concat("-").concat(String.valueOf(m-1)) ;
			}
		}
		
		return prePeriodCode ;
	}

	/**
	 * @Title: getYearStartDate 
	 * @Description: 计算本年开始日期
	 * @param periodCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getYearStartDate(String periodCode) throws Exception {
		String year = periodCode.trim().substring(0,4) ;
		return year.concat("-01-01") ;
	}

	/**
	 * @Title: getYearEndDate 
	 * @Description: 计算本年结束日期
	 * @param periodCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getYearEndDate(String periodCode) throws Exception {
		return sumBalanceMapper.getPeriodEndDate(periodCode) ;
	}

	/**
	 * @Title: getPreYearCode 
	 * @Description: 计算上年期末期间
	 * @param periodCode
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getPreYearCode(String periodCode,String ledgerId) throws Exception {
		// 上年期末
		String preYearCode = null ;
		
		// 判断当前账簿是否为年中开启
		String startPeriodCode = sumBalanceMapper.getStartPeriod(ledgerId) ;
		String startYear = startPeriodCode.trim().substring(0, 4) ;
		String startMonth = startPeriodCode.trim().substring(5, 7) ; 
		int sm = Integer.parseInt(startMonth) ;
		
		// 本年度
		String year = periodCode.trim().substring(0,4) ;
		// 上年度
		String preYear = String.valueOf(Integer.parseInt(year)-1) ;
		
		if(year.equals(startYear)){// 如果账簿是本年开账
			if(sm == 1){
				// 1月份
				preYearCode = preYear.concat("-12") ;
				
			}else if(sm > 1 && sm<11){
				// 2到10月份
				preYearCode = year.concat("-0").concat(String.valueOf(sm-1)) ;
				
			}else{
				// 11-12月份
				preYearCode = year.concat(String.valueOf(sm-1)) ;
			}
			
		}else{ // 非本年开账
			
			// 判断上年是否启用调整期
			HashMap<String,String> map =  new HashMap<String,String>() ;
			map.put("ledgerId", ledgerId) ;
			map.put("periodCode", preYear.concat("-Q4")) ;
			String isEnableAdj = sumBalanceMapper.isEnabeQ4Adj(map) ;
			if("1".equals(isEnableAdj)){
				preYearCode = preYear.concat("-Q4") ;
			}else{
				preYearCode = preYear.concat("-12") ;
			}			
		}
		
		return preYearCode ;
	}

	/**
	 * @Title: handlerAccCCID 
	 * @Description: 处理科目的CCID
	 * @param paramMap
	 * @param resultMap
	 * @throws Exception    设定文件
	 */
	public void handlerAccCCID(HashMap<String,String> paramMap,HashMap<String,Object> resultMap)throws Exception {
		if("Y".equals(paramMap.get("isLeaf"))){
			paramMap.put("ccid", String.valueOf(resultMap.get("ccid"))) ;
			
		}else{
			// 取得直接下级科目的CCID
			String ccidKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CCID, paramMap.get("ledgerId"))  ;
			String ccid = paramMap.get("ccid") ;
			String json = this.getJsonStr(ccidKey, ccid) ;
			if(json != null){
				JSONObject jo = new JSONObject(json) ;
				String ccidCode = jo.getString("code") ;
				String accCode = paramMap.get("accCode") ; // 本级科目编码
				String fullCode = accCode.concat(ccidCode.substring(ccidCode.indexOf("/"))) ; // 本级科目组合全编码
				
				// 取得本机科目的CCID
				String upAccCCID = this.getJsonStr(ccidKey, fullCode) ;
				JSONObject upJo =  new JSONObject(String.valueOf(upAccCCID)) ;
				
				resultMap.put("ccid", upJo.get("id")) ;
			}
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: sumDataByPeirod</p> 
	 * <p>Description: 按期间汇总科目余额 </p> 
	 * @param map
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#sumDataByPeirod(java.util.HashMap, java.util.List)
	 */
	@Override
	public void sumAccDataByPeriod(HashMap<String,String> map, List<Period> periods) throws Exception {
		try {
			// 当前期间
			String periodCode = map.get("periodCode") ;
			String ledgerId = map.get("ledgerId") ;
			// 上期期间
			String prePeriodCode = this.getPrePeriodCode(periodCode,ledgerId) ;
			map.put("prePeriodCode", prePeriodCode) ;
			// 年初开始和结束日期
			String yearStartDate = this.getYearStartDate(periodCode) ;
			String yearEndDate = this.getYearEndDate(periodCode) ;
			map.put("yearStartDate", yearStartDate) ;
			map.put("yearEndDate", yearEndDate) ;
			// 上年年末期间
			String preYearCode = this.getPreYearCode(periodCode,ledgerId) ;
			map.put("preYearCode", preYearCode) ;
			
			// 判断上年年末是否跨年: Y-跨年,N-不跨年
			if(periodCode.substring(0, 4).equals(preYearCode.substring(0, 4))){
				map.put("isDiffYear", "N") ; //
			}else{
				map.put("isDiffYear", "Y") ;
			}
			
			log.debug("期间 "+periodCode+"汇总开始");
			
			// 按科目层级执行汇总
			this.sumDataByAcc(map);
			
			log.debug("期间 "+periodCode+"汇总结束");
			
			// 按期间循环
			if(periods != null && periods.size()>0){
				for(int i=0; i<periods.size(); i++){
					Period period  =periods.get(i) ;
					map.put("periodCode", period.getPeriodCode()) ;
					map.put("accId", map.get("leafAccId")) ; // 重置末级科目ID
					map.put("accCode", "") ;	
					map.put("isLeaf", "Y") ;
//					map.remove("ccid") ;
					periods.remove(period) ;
					// 递归汇总
					this.sumAccDataByPeriod(map,periods);
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/**
	 * @Title: sumDataByAccount 
	 * @Description: 按科目层级汇总数据
	 * @param ledgerId
	 * @param accId
	 * @param periodCode
	 * @throws Exception
	 */
	private void sumDataByAcc(HashMap<String,String> map) throws Exception {
		try {
			// 汇总单科目余额
			this.sumData(map);
			
			// 按科目层级汇总
			String key = XCRedisKeys.getAccKey(map.get("hrcyId")) ;
			String accJson = this.getJsonStr(key, map.get("accId")) ;
			JSONObject accJo = new JSONObject(accJson) ;
			if(accJo != null){
				// 递归汇总上级科目余额
				String upAccId = accJo.getString("upId") ;
				if(!"-1".equals(upAccId)){
					// 直接上级科目的ID和编码
					String upAccJson = this.getJsonStr(key, upAccId) ;
					JSONObject upAccJo = new JSONObject(upAccJson) ;
					map.put("accId", upAccJo.getString("id")) ;
					map.put("accCode", upAccJo.getString("code")) ;
					map.put("isLeaf", "N") ;
					
					// 递归处理
					this.sumDataByAcc(map);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/**
	 * @Title: calB 
	 * @Description: 计算期初借方和贷方余额 (上期期末借方和贷方余额)
	 * @param paramMap
	 * @param resultMap
	 * @throws Exception    设定文件
	 */
	private void calB(HashMap<String,String> paramMap,HashMap<String,Object> resultMap) throws Exception {
		HashMap<String,Object> bMap = sumBalanceMapper.calB(paramMap) ;
		if(bMap != null && bMap.size()>0){
			
			if("Y".equals(paramMap.get("isAccount"))){
				// 不含未过账数据
				Object amt_b_dr = bMap.get("AMT_B_DR") ;
				Object amt_b_cr = bMap.get("AMT_B_CR") ;
				Object b_dr = bMap.get("B_DR") ;
				Object b_cr = bMap.get("B_CR") ;
				
				resultMap.put("AMT_B_DR", amt_b_dr==null ? 0:String.valueOf(amt_b_dr)) ;
				resultMap.put("AMT_B_CR", amt_b_cr==null ? 0:String.valueOf(amt_b_cr)) ;
				resultMap.put("B_DR", b_dr==null ? 0:String.valueOf(b_dr)) ;
				resultMap.put("B_CR", b_cr==null ? 0:String.valueOf(b_cr)) ;
				resultMap.put("AMT_T_B_DR", 0) ;
				resultMap.put("AMT_T_B_CR", 0) ;
				resultMap.put("T_B_DR", 0) ;
				resultMap.put("T_B_CR", 0) ;
			}else{
				// 含未过账数据
				Object amt_t_b_dr = bMap.get("AMT_T_B_DR") ;
				Object amt_t_b_cr = bMap.get("AMT_T_B_CR") ;
				Object t_b_dr = bMap.get("T_B_DR") ;
				Object t_b_cr = bMap.get("T_B_CR") ;
				
				resultMap.put("AMT_T_B_DR", amt_t_b_dr==null ? 0:String.valueOf(amt_t_b_dr)) ;
				resultMap.put("AMT_T_B_CR", amt_t_b_cr==null ? 0:String.valueOf(amt_t_b_cr)) ;
				resultMap.put("T_B_DR", t_b_dr==null ? 0:String.valueOf(t_b_dr)) ;
				resultMap.put("T_B_CR", t_b_cr==null ? 0:String.valueOf(t_b_cr)) ;
				resultMap.put("AMT_B_DR", 0) ;
				resultMap.put("AMT_B_CR", 0) ;
				resultMap.put("B_DR", 0) ;
				resultMap.put("B_CR", 0) ;
			}				
		}else{
			resultMap.put("AMT_B_DR", 0) ;
			resultMap.put("AMT_B_CR", 0) ;
			resultMap.put("B_DR", 0) ;
			resultMap.put("B_CR", 0) ;
			resultMap.put("AMT_T_B_DR", 0) ;
			resultMap.put("AMT_T_B_CR", 0) ;
			resultMap.put("T_B_DR", 0) ;
			resultMap.put("T_B_CR", 0) ;
		}
		
		log.debug("计算期初借方和贷方余额开始");

		log.debug("currency_code = " + resultMap.get("currency_code"));
		log.debug("ccid = " + resultMap.get("ccid"));
		log.debug("B_DR = " + resultMap.get("B_DR"));
		log.debug("B_CR = " + resultMap.get("B_CR"));
		log.debug("AMT_B_DR = " + resultMap.get("AMT_B_DR"));
		log.debug("AMT_B_CR = " + resultMap.get("AMT_B_CR"));
		log.debug("T_B_DR = " + resultMap.get("T_B_DR"));
		log.debug("T_B_CR = " + resultMap.get("T_B_CR"));
		log.debug("AMT_T_B_DR = " + resultMap.get("AMT_T_B_DR"));
		log.debug("AMT_T_B_CR = " + resultMap.get("AMT_T_B_CR"));		
		
		log.debug("计算期初借方和贷方余额结束");
	}
	
	/**
	 * @Title: calPTD 
	 * @Description: 计算本期借方和贷方发生额
	 * @param paramMap
	 * @param resultMap
	 * @throws Exception    设定文件
	 */
	private void calPTD(HashMap<String,String> paramMap,HashMap<String,Object> resultMap)throws Exception{
		// 计算处理
		HashMap<String,Object> ptdMap = sumBalanceMapper.calPTD(paramMap);
		if(ptdMap != null && ptdMap.size()>0){
			
			Object amt_ptd_dr = ptdMap.get("AMT_PTD_DR") ;
			Object amt_ptd_cr = ptdMap.get("AMT_PTD_CR") ;
			Object ptd_dr = ptdMap.get("PTD_DR") ;
			Object ptd_cr = ptdMap.get("PTD_CR") ;
			
			resultMap.put("PTD_DR", ptd_dr==null ? 0:String.valueOf(ptd_dr)) ;
			resultMap.put("PTD_CR", ptd_cr==null ? 0:String.valueOf(ptd_cr)) ;
			resultMap.put("T_PTD_DR", ptd_dr==null ? 0:String.valueOf(ptd_dr)) ;
			resultMap.put("T_PTD_CR", ptd_cr==null ? 0:String.valueOf(ptd_cr)) ;
			
			resultMap.put("AMT_PTD_DR", amt_ptd_dr==null ? 0:String.valueOf(amt_ptd_dr)) ;
			resultMap.put("AMT_PTD_CR", amt_ptd_cr==null ? 0:String.valueOf(amt_ptd_cr)) ;
			resultMap.put("AMT_T_PTD_DR", amt_ptd_dr==null ? 0:String.valueOf(amt_ptd_dr)) ;
			resultMap.put("AMT_T_PTD_CR", amt_ptd_cr==null ? 0:String.valueOf(amt_ptd_cr)) ;

		}else{
			resultMap.put("AMT_PTD_DR", 0) ;
			resultMap.put("AMT_PTD_CR", 0) ;
			resultMap.put("PTD_DR", 0) ;
			resultMap.put("PTD_CR", 0) ;
			resultMap.put("AMT_T_PTD_DR", 0) ;
			resultMap.put("AMT_T_PTD_CR", 0) ;
			resultMap.put("T_PTD_DR", 0) ;
			resultMap.put("T_PTD_CR", 0) ;
		}
		
		log.debug("计算本期借方和贷方发生额开始");

		log.debug("currency_code = " + resultMap.get("currency_code"));
		log.debug("ccid = " + resultMap.get("ccid"));
		log.debug("PTD_DR = " + resultMap.get("PTD_DR"));
		log.debug("PTD_CR = " + resultMap.get("PTD_CR"));
		log.debug("AMT_PTD_DR = " + resultMap.get("AMT_PTD_DR"));
		log.debug("AMT_PTD_CR = " + resultMap.get("AMT_PTD_CR"));
		log.debug("T_PTD_DR = " + resultMap.get("T_PTD_DR"));
		log.debug("T_PTD_CR = " + resultMap.get("T_PTD_CR"));
		log.debug("AMT_T_PTD_DR = " + resultMap.get("AMT_T_PTD_DR"));
		log.debug("AMT_T_PTD_CR = " + resultMap.get("AMT_T_PTD_CR"));		
		
		log.debug("计算本期借方和贷方发生额结束");
	}
	
	/**
	 * @Title: calY 
	 * @Description: 计算年初借方和贷方余额 (上年期末的借方和贷方余额)
	 * @param paramMap
	 * @param resultMap
	 * @throws Exception    设定文件
	 */
	private void calY(HashMap<String,String> paramMap,HashMap<String,Object> resultMap)throws Exception{
		HashMap<String,Object> yMap = sumBalanceMapper.calY(paramMap) ;
		if(yMap != null && yMap.size()>0){
			
			if("Y".equals(paramMap.get("isAccount"))){
				// 不含未过账数据
				Object y_dr = yMap.get("Y_DR") ;
				Object y_cr = yMap.get("Y_CR");
				Object amt_y_dr = yMap.get("AMT_Y_DR") ;
				Object amt_y_cr = yMap.get("AMT_Y_CR") ;
				
				resultMap.put("Y_DR", y_dr==null ? 0:String.valueOf(y_dr)) ;
				resultMap.put("Y_CR", y_cr==null ? 0:String.valueOf(y_cr)) ;
				resultMap.put("AMT_Y_DR", amt_y_dr==null ? 0:String.valueOf(amt_y_dr)) ;
				resultMap.put("AMT_Y_CR", amt_y_cr==null ? 0:String.valueOf(amt_y_cr)) ;
				resultMap.put("T_Y_DR", 0) ;
				resultMap.put("T_Y_CR", 0) ;
				resultMap.put("AMT_T_Y_DR", 0) ;
				resultMap.put("AMT_T_Y_CR", 0) ;
			}else{
				// 含未过账数据
				Object t_y_dr = yMap.get("T_Y_DR") ;
				Object t_y_cr = yMap.get("T_Y_CR") ;
				Object amt_t_y_dr =yMap.get("AMT_T_Y_DR") ;
				Object amt_t_y_cr = yMap.get("AMT_T_Y_CR") ;
				
				resultMap.put("T_Y_DR", t_y_dr==null ? 0:String.valueOf(t_y_dr)) ;
				resultMap.put("T_Y_CR", t_y_cr==null ? 0:String.valueOf(t_y_cr)) ;
				resultMap.put("AMT_T_Y_DR", amt_t_y_dr==null ? 0:String.valueOf(amt_t_y_dr)) ;
				resultMap.put("AMT_T_Y_CR", amt_t_y_cr==null ? 0:String.valueOf(amt_t_y_cr)) ;
				resultMap.put("Y_DR", 0) ;
				resultMap.put("Y_CR", 0) ;
				resultMap.put("AMT_Y_DR", 0) ;
				resultMap.put("AMT_Y_CR", 0) ;
			}	
			
		}else{
			resultMap.put("Y_DR", 0) ;
			resultMap.put("Y_CR", 0) ;
			resultMap.put("AMT_Y_DR", 0) ;
			resultMap.put("AMT_Y_CR", 0) ;
			resultMap.put("T_Y_DR", 0) ;
			resultMap.put("T_Y_CR", 0) ;
			resultMap.put("AMT_T_Y_DR", 0) ;
			resultMap.put("AMT_T_Y_CR", 0) ;
		}
		
		log.debug("计算年初借方和贷方余额开始");

		log.debug("currency_code = " + resultMap.get("currency_code"));
		log.debug("ccid = " + resultMap.get("ccid"));
		log.debug("Y_DR = " + resultMap.get("Y_DR"));
		log.debug("Y_CR = " + resultMap.get("Y_CR"));
		log.debug("AMT_Y_DR = " + resultMap.get("AMT_Y_DR"));
		log.debug("AMT_Y_CR = " + resultMap.get("AMT_Y_CR"));
		log.debug("T_Y_DR = " + resultMap.get("T_Y_DR"));
		log.debug("T_Y_CR = " + resultMap.get("T_Y_CR"));
		log.debug("AMT_T_Y_DR = " + resultMap.get("AMT_T_Y_DR"));
		log.debug("AMT_T_Y_CR = " + resultMap.get("AMT_T_Y_CR"));		
		
		log.debug("计算年初借方和贷方余额结束");
	}
	
	/**
	 * @Title: calYTD 
	 * @Description: 计算本年借方和贷方发生额 
	 * @param paramMap
	 * @param resultMap
	 * @throws Exception    设定文件
	 */
	private void calYTD(HashMap<String,String> paramMap,HashMap<String,Object> resultMap)throws Exception{
		HashMap<String,Object> ytdMap = sumBalanceMapper.calYTD(paramMap) ;
		if(ytdMap != null && ytdMap.size()>0){
			
			Object ytd_dr = ytdMap.get("YTD_DR") ;
			Object ytd_cr = ytdMap.get("YTD_CR") ;
			Object amt_ytd_dr = ytdMap.get("AMT_YTD_DR") ;
			Object amt_ytd_cr = ytdMap.get("AMT_YTD_CR") ;
			
			resultMap.put("YTD_DR", ytd_dr==null ? 0:String.valueOf(ytd_dr)) ;
			resultMap.put("YTD_CR", ytd_cr==null ? 0:String.valueOf(ytd_cr)) ;
			resultMap.put("AMT_YTD_DR", amt_ytd_dr==null ? 0:String.valueOf(amt_ytd_dr)) ;
			resultMap.put("AMT_YTD_CR", amt_ytd_cr==null ? 0:String.valueOf(amt_ytd_cr)) ;
			resultMap.put("T_YTD_DR", ytd_dr==null ? 0:String.valueOf(ytd_dr)) ;
			resultMap.put("T_YTD_CR", ytd_cr==null ? 0:String.valueOf(ytd_cr)) ;
			resultMap.put("AMT_T_YTD_DR", amt_ytd_dr==null ? 0:String.valueOf(amt_ytd_dr)) ;
			resultMap.put("AMT_T_YTD_CR", amt_ytd_cr==null ? 0:String.valueOf(amt_ytd_cr)) ;
			
		}else{
			resultMap.put("YTD_DR", 0) ;
			resultMap.put("YTD_CR", 0) ;
			resultMap.put("AMT_YTD_DR", 0) ;
			resultMap.put("AMT_YTD_CR", 0) ;
			resultMap.put("T_YTD_DR", 0) ;
			resultMap.put("T_YTD_CR", 0) ;
			resultMap.put("AMT_T_YTD_DR", 0) ;
			resultMap.put("AMT_T_YTD_CR", 0) ;
		}
		
		log.debug("计算本年借方和贷方发生额 开始");

		log.debug("currency_code = " + resultMap.get("currency_code"));
		log.debug("ccid = " + resultMap.get("ccid"));
		log.debug("YTD_DR = " + resultMap.get("YTD_DR"));
		log.debug("YTD_CR = " + resultMap.get("YTD_CR"));
		log.debug("AMT_YTD_DR = " + resultMap.get("AMT_YTD_DR"));
		log.debug("AMT_YTD_CR = " + resultMap.get("AMT_YTD_CR"));
		log.debug("T_YTD_DR = " + resultMap.get("T_YTD_DR"));
		log.debug("T_YTD_CR = " + resultMap.get("T_YTD_CR"));
		log.debug("AMT_T_YTD_DR = " + resultMap.get("AMT_T_YTD_DR"));
		log.debug("AMT_T_YTD_CR = " + resultMap.get("AMT_T_YTD_CR"));		
		
		log.debug("计算本年借方和贷方发生额 结束");
	}
	
	/**
	 * @Title: calPJTD 
	 * @Description: 计算期末借方和贷方余额 (年初余额+本年发生额)
	 * @param paramMap
	 * @param resultMap
	 * @throws Exception    设定文件
	 */
	private void calPJTD(HashMap<String,String> paramMap,HashMap<String,Object> resultMap)throws Exception{
		if("Y".equals(paramMap.get("isAccount"))){
			// 不执行未过账数据
			String y_dr = String.valueOf(resultMap.get("Y_DR")) ;
			String ytd_dr = String.valueOf(resultMap.get("YTD_DR")) ;
			String y_cr = String.valueOf(resultMap.get("Y_CR")) ;
			String ytd_cr = String.valueOf(resultMap.get("YTD_CR")) ;
			
			String amt_y_dr = String.valueOf(resultMap.get("AMT_Y_DR")) ;
			String amt_ytd_dr = String.valueOf(resultMap.get("AMT_YTD_DR")) ;
			String amt_y_cr = String.valueOf(resultMap.get("AMT_Y_CR")) ;
			String amt_ytd_cr = String.valueOf(resultMap.get("AMT_YTD_CR")) ;
			
			BigDecimal pjtd_dr = new BigDecimal(y_dr).add(new BigDecimal(ytd_dr)) ;
			BigDecimal pjtd_cr = new BigDecimal(y_cr).add(new BigDecimal(ytd_cr)) ;
			BigDecimal amt_pjtd_dr = new BigDecimal(amt_y_dr).add(new BigDecimal(amt_ytd_dr)) ;
			BigDecimal amt_pjtd_cr = new BigDecimal(amt_y_cr).add(new BigDecimal(amt_ytd_cr)) ;
			
			resultMap.put("PJTD_DR", pjtd_dr.toPlainString()) ;
			resultMap.put("PJTD_CR", pjtd_cr.toPlainString()) ;
			resultMap.put("AMT_PJTD_DR", amt_pjtd_dr.toPlainString()) ;
			resultMap.put("AMT_PJTD_CR", amt_pjtd_cr.toPlainString()) ;
			resultMap.put("T_PJTD_DR", 0) ;
			resultMap.put("T_PJTD_CR", 0) ;
			resultMap.put("AMT_T_PJTD_DR", 0) ;
			resultMap.put("AMT_T_PJTD_CR",0) ;			
			
		}else{
			// 含未过账数据
			String t_y_dr = String.valueOf(resultMap.get("T_Y_DR")) ;
			String t_ytd_dr = String.valueOf(resultMap.get("T_YTD_DR")) ;
			String t_y_cr = String.valueOf(resultMap.get("T_Y_CR")) ;
			String t_ytd_cr = String.valueOf(resultMap.get("T_YTD_CR")) ;
			String amt_t_y_dr = String.valueOf(resultMap.get("AMT_T_Y_DR")) ;
			String amt_t_ytd_dr = String.valueOf(resultMap.get("AMT_T_YTD_DR")) ;
			String amt_t_y_cr = String.valueOf(resultMap.get("AMT_T_Y_CR")) ;
			String amt_t_ytd_cr = String.valueOf(resultMap.get("AMT_T_YTD_CR")) ;
			
			BigDecimal t_pjtd_dr = new BigDecimal(t_y_dr).add(new BigDecimal(t_ytd_dr)) ;
			BigDecimal t_pjtd_cr = new BigDecimal(t_y_cr).add(new BigDecimal(t_ytd_cr)) ;
			BigDecimal amt_t_pjtd_dr = new BigDecimal(amt_t_y_dr).add(new BigDecimal(amt_t_ytd_dr)) ;
			BigDecimal amt_t_pjtd_cr = new BigDecimal(amt_t_y_cr).add(new BigDecimal(amt_t_ytd_cr)) ;
			
			resultMap.put("T_PJTD_DR", t_pjtd_dr.toPlainString()) ;
			resultMap.put("T_PJTD_CR", t_pjtd_cr.toPlainString()) ;
			resultMap.put("AMT_T_PJTD_DR", amt_t_pjtd_dr.toPlainString()) ;
			resultMap.put("AMT_T_PJTD_CR",amt_t_pjtd_cr.toPlainString()) ;
			resultMap.put("PJTD_DR", 0) ;
			resultMap.put("PJTD_CR", 0) ;
			resultMap.put("AMT_PJTD_DR", 0) ;
			resultMap.put("AMT_PJTD_CR", 0) ;
		}
		
		log.debug("计算期末借方和贷方余额 开始");

		log.debug("currency_code = " + resultMap.get("currency_code"));
		log.debug("ccid = " + resultMap.get("ccid"));
		log.debug("PJTD_DR = " + resultMap.get("PJTD_DR"));
		log.debug("PJTD_CR = " + resultMap.get("PJTD_CR"));
		log.debug("AMT_PJTD_DR = " + resultMap.get("AMT_PJTD_DR"));
		log.debug("AMT_PJTD_CR = " + resultMap.get("AMT_PJTD_CR"));
		log.debug("T_PJTD_DR = " + resultMap.get("T_PJTD_DR"));
		log.debug("T_PJTD_CR = " + resultMap.get("T_PJTD_CR"));
		log.debug("AMT_T_PJTD_DR = " + resultMap.get("AMT_T_PJTD_DR"));
		log.debug("AMT_T_PJTD_CR = " + resultMap.get("AMT_T_PJTD_CR"));		
		
		log.debug("计算期末借方和贷方余额 结束");
	}
	
	/**
	 * @Title: sumData 
	 * @Description: 数据汇总处理 
	 * @param ledgerId
	 * @param hrcyId
	 * @param accId
	 * @param periodCode
	 * @param type
	 * @throws Exception
	 */
	private void sumData(HashMap<String,String> map) throws Exception {
		try {
			// 科目组合全编码
			String ccidKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CCID, map.get("ledgerId"))  ;
			Object json = this.getJsonStr(ccidKey, map.get("ccid")) ;
			if(json != null){
				JSONObject jo = new JSONObject(String.valueOf(json)) ;
				String fullCode = jo.getString("code") ;
				map.put("fullCode", fullCode.substring(fullCode.indexOf("/"))) ;
			}
			
			// 保存参数信息
			HashMap<String,Object> resultMap = new HashMap<String,Object>() ;
			resultMap.put("ledger_id", map.get("ledgerId")) ;
			resultMap.put("period_code", map.get("periodCode")) ;
			resultMap.put("isAccount", map.get("isAccount")) ;
			resultMap.put("userId", map.get("userId")) ;
			resultMap.put("vStatus", map.get("vStatus")) ;
			resultMap.put("currency_code", map.get("cnyCode")) ;
			resultMap.put("ccid", map.get("ccid")) ;
			
			// 计算期初借方和贷方余额 (上期期末借方和贷方余额)
			this.calB(map, resultMap) ;
			
			// 计算本期借方和贷方发生额
			this.calPTD(map, resultMap);
			
			// 计算年初借方和贷方余额 (上年期末的借方和贷方余额)
			this.calY(map, resultMap);
			
			// 计算本年借方和贷方发生额 
			this.calYTD(map, resultMap);
			
			// 计算期末借方和贷方余额 (年初余额+本年发生额)
			this.calPJTD(map, resultMap);
			
			// 处理科目的CCID
			this.handlerAccCCID(map, resultMap); 
			
			// 汇总余额信息
			this.saveBalances(resultMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: saveBalances 
	 * @Description: 保存汇总余额信息
	 * @param results
	 * @param currencyCode
	 * @throws Exception    设定文件
	 */
	private void saveBalances(HashMap<String,Object> resultMap) throws Exception {
		try {
			if(resultMap != null && resultMap.size()>0){
				
				// 清除数据参数
				HashMap<String,String> delMap = new HashMap<String,String>() ;
				delMap.put("ledgerId", String.valueOf(resultMap.get("ledger_id"))) ;
				delMap.put("ccid", String.valueOf(resultMap.get("ccid"))) ;
				delMap.put("periodCode", String.valueOf(resultMap.get("period_code"))) ;
				delMap.put("cnyCode", String.valueOf(resultMap.get("currency_code"))) ;
				
				// 设置更新时间和更新人
				resultMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
				
				// 执行保存
				if("Y".equals(resultMap.get("isAccount"))){
					// 不含未过账数据处理
					try {
						sumBalanceMapper.insBalances(resultMap);
						
					} catch (Exception e) {
						sumBalanceMapper.updBalances(resultMap);
					}
					
				}else{
					// 含未过账数据处理
					try {
						sumBalanceMapper.insBalances(resultMap);
						
					} catch (Exception e) {
						sumBalanceMapper.updBalances(resultMap);
					}
				}
				
				// 清除余额为0的垃圾数据
				sumBalanceMapper.delAccBalance(delMap);
				
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: sumCashDataByPeirod 
	 * @Description: 统计现金流量项目
	 * @param map
	 * @param periods
	 * @throws Exception    设定文件
	 */
	public void sumCashDataByPeirod(HashMap<String,String> map) throws Exception {
		// 统计参数
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("ledgerId", map.get("ledgerId")) ;
		paramMap.put("periodCode", map.get("periodCode")) ;
		paramMap.put("accId", map.get("accId")) ;
		paramMap.put("hrcyId", map.get("hrcyId")) ;
		paramMap.put("headId", map.get("headId")) ;
		paramMap.put("isAccount", map.get("isAccount")) ;
		paramMap.put("userId", map.get("userId")) ;
		paramMap.put("vStatus", map.get("vStatus")) ;
		
		// 判断当前科目是否需要执行现金流量项目统计处理
		String key = XCRedisKeys.getAccKey(map.get("hrcyId")) ;
		String hashKey = map.get("accId") ;
		String accJson = this.getJsonStr(key, hashKey) ;
		if(accJson != null){
			JSONObject jo = new JSONObject(accJson) ;
			// 现金或银行科目
			if("Y".equals(jo.getString("isCash"))){	
				// 计算现金流量项目ID
				List<HashMap<String,String>> items = sumBalanceMapper.getAllCashItems(map) ;
				if(items != null && items.size()>0){
					for(HashMap<String,String> item : items){
						paramMap.put("caId", item.get("caId")) ;
						paramMap.put("leafCaId", item.get("caId")) ;
						paramMap.put("isLeaf", "Y") ;
						
						log.debug("现金流量项目 ca_id = " + item.get("caId") + " 统计开始");
						
						// 执行现金流量项统计处理
						this.sumCashByCALevel(paramMap);
						
						log.debug("现金流量项目 ca_id = " + item.get("caId") + " 统计结束");
					}
				}
			}		
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: sumCashByCALevel</p> 
	 * <p>Description: 按层级统计现金流量项目 </p> 
	 * @param map
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#sumCashByPeriod(java.util.HashMap)
	 */
	private void sumCashByCALevel(HashMap<String,String> map) throws Exception {
		try {
			// 统计现金流量项
			this.sumCash(map);
			
			// 按现金流量项目层级汇总
			String key = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_CASH_ITEMS) ;
			String caJson = this.getJsonStr(key, map.get("caId")) ;
			JSONObject caJo = new JSONObject(caJson) ;
			if(caJo != null){
				// 递归汇总上级科目余额
				String upCaId = caJo.getString("upId") ;
				if(!"-1".equals(upCaId)){
					// 直接上级科目的ID和编码
					String upCaJson = this.getJsonStr(key, upCaId) ;
					JSONObject upCaJo = new JSONObject(upCaJson) ;
					
					// 取得现金流量方向
					Object direction = null ;
					try {
						direction = upCaJo.get("direction") ;
					} catch (Exception e) {
						direction = null ;
					}
					// 方向不为空时执行非末级汇总
					if(direction != null){
						map.put("caId", upCaJo.getString("id")) ;
						map.put("isLeaf", "N") ;
						
						// 递归处理
						this.sumCashByCALevel(map);
					}
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}		
	}
	
	/**
	 * @Title: sumCash 
	 * @Description: 统计现金流量项目汇总数
	 * @param map
	 * @throws Exception    设定文件
	 */
	private void sumCash(HashMap<String,String> map)throws Exception {
		try {
			// 保存参数信息
			HashMap<String,Object> resultMap = new HashMap<String,Object>() ;
			resultMap.put("ledgerId", map.get("ledgerId")) ;
			resultMap.put("periodCode", map.get("periodCode")) ;
			resultMap.put("isAccount", map.get("isAccount")) ;
			resultMap.put("accId", map.get("accId")) ;
			resultMap.put("caId", map.get("caId")) ;
			resultMap.put("userId", map.get("userId")) ;
			resultMap.put("vStatus", map.get("vStatus")) ;
			
			// 计算本期借方和贷方发生额
			HashMap<String,Object> cashMap = sumBalanceMapper.calPTD4Cash(map) ;
			if(cashMap != null){
				resultMap.put("ptd", cashMap.get("ptd")) ;
			}else{
				resultMap.put("ptd", 0) ;
			}
			
			// 汇总余额信息
			this.saveCashBalances(resultMap);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: saveCashBalances 
	 * @Description: 保存现金流量项目统计信息
	 * @param resultMap
	 * @throws Exception    设定文件
	 */
	private void saveCashBalances(HashMap<String,Object> resultMap) throws Exception {
		try {
			if(resultMap != null && resultMap.size()>0){
				
				// 清除数据参数
				HashMap<String,String> delMap = new HashMap<String,String>() ;
				delMap.put("ledgerId", String.valueOf(resultMap.get("ledgerId"))) ;
				delMap.put("periodCode", String.valueOf(resultMap.get("periodCode"))) ;
				delMap.put("accId", String.valueOf(resultMap.get("accId"))) ;
				delMap.put("caId", String.valueOf(resultMap.get("caId"))) ;
				
				// 设置更新时间和更新人
				resultMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
				resultMap.put("isInit", "N") ;
				
				// 执行保存
				if("Y".equals(resultMap.get("isAccount"))){
					// 不含未过账数据处理
					try {
						resultMap.put("sumId", UUID.randomUUID().toString()) ;
						sumBalanceMapper.insCashBalances(resultMap);
						
					} catch (Exception e) {
						sumBalanceMapper.updCashBalances(resultMap);
					}
					
				}else{
					// 含未过账数据处理
					try {
						resultMap.put("sumId", UUID.randomUUID().toString()) ;
						sumBalanceMapper.insCashBalances(resultMap);
						
					} catch (Exception e) {
						sumBalanceMapper.updCashBalances(resultMap);
					}
				}
				
				// 清除余额为0的垃圾数据
				sumBalanceMapper.delCashBalances(delMap);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}		
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: handlerSumFlagAndSerialNo</p> 
	 * <p>Description: 修改凭证汇总标志及凭证号 </p> 
	 * @param headId
	 * @param userId
	 * @param serialNo
	 * @param isCanceled
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#handlerSumFlagAndSerialNo(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void handlerSumFlag(String headId, String userId, String isCanceled) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("headId", headId) ;
		map.put("userId", userId) ;
		map.put("isCanceled", isCanceled) ;
		map.put("cdate", CurrentUserUtil.getCurrentDate()) ;
		
		sumBalanceMapper.handlerSumFlag(map);
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: sumInitCashByCALevel</p> 
	 * <p>Description: 期初现金流量余额统计 </p> 
	 * @param ledgerId
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#sumInitCash(java.util.HashMap)
	 */
	@Override
	public void sumInitCashByCALevel(String ledgerId)throws Exception {}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: sumInitCashByCALevel</p> 
	 * <p>Description: 删除期初现金流量项目 </p> 
	 * @param ledgerId
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#sumInitCash(java.util.HashMap)
	 */
	@Override
	public void delInitCash(String ledgerId,String periodCode)throws Exception {
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("ledgerId", ledgerId) ;
		paramMap.put("periodCode", periodCode) ;
		
		sumBalanceMapper.delInitCash(paramMap);
	} 
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: createAccount</p> 
	 * <p>Description: 账簿建账 </p> 
	 * @param map
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#sumInitBalances(java.util.HashMap)
	 */
	@Override
	public void createAccount(HashMap<String,String> map)throws Exception {}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: cancelAccount</p> 
	 * <p>Description: 取消建账 </p> 
	 * @param map
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#cancelAccount(java.util.HashMap)
	 */
	@Override
	public void cancelAccount(HashMap<String,String> map)throws Exception {}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: updateCashBalStatus</p> 
	 * <p>Description: 更新现金流量临时表数据状态信息 </p> 
	 * @param ledgerId
	 * @param isVaild
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#updateCashBalStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateCashBalStatus(String ledgerId,String isVaild)throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("isValid", isVaild) ;
		map.put("userId", CurrentSessionVar.getUserId()) ;
		map.put("cdate", CurrentUserUtil.getCurrentDate()) ;
		
		sumBalanceMapper.updateTmpCashBalStatus(map);
	}
	
}
