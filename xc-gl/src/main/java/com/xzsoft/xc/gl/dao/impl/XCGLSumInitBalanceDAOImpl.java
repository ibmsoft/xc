package com.xzsoft.xc.gl.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.mapper.SumBalanceMapper;
import com.xzsoft.xc.gl.mapper.SumInitBalanceMapper;
import com.xzsoft.xc.gl.modal.Period;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

@Repository("xcglSumInitBalanceDAOImpl")
public class XCGLSumInitBalanceDAOImpl extends XCGLSumBalanceDAOImpl {
	// 日志记录器
	private static final Logger log = Logger.getLogger(XCGLSumInitBalanceDAOImpl.class.getName()) ;
	
	@Resource
	private SumInitBalanceMapper sumInitBalanceMapper ;
	@Resource
	private SumBalanceMapper sumBalanceMapper ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: createAccount</p> 
	 * <p>Description: 账簿建账 
	 * 		map : ledgerId,periodCode,prePeriodCode,hrcyId,userId,cnyCode
	 * </p> 
	 * @param map
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#sumInitBalances(java.util.HashMap)
	 */
	public void createAccount(HashMap<String,String> map)throws Exception {
		try {
			// 账簿ID
			String ledgerId = map.get("ledgerId") ;
			
			// 删除科目余额数据
			sumInitBalanceMapper.delInitAccBalances(map);
			
			// 将临时表中本位币数据导入正式表
			HashMap<String,Object> paramMap = new HashMap<String,Object>() ;
			paramMap.put("ledgerId", map.get("ledgerId")) ;
			paramMap.put("cnyCode", map.get("cnyCode")) ;
			paramMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			paramMap.put("userId", map.get("userId")) ;
			paramMap.put("isForeign", "N") ;
			sumInitBalanceMapper.impInitAccBalances(paramMap);
			
			// 将临时表中原币数据导入正式表
			paramMap.put("isForeign", "Y") ;
			sumInitBalanceMapper.impInitAccBalances(paramMap);
			
			// 计算下属期间
			HashMap<String,String> periodMap = new HashMap<String,String>() ;
			periodMap.put("ledgerId", ledgerId) ;
			periodMap.put("periodCode", map.get("ldStartPeriod")) ;
			List<Period> periods = sumBalanceMapper.getAfterPeriods4CreateAccount(periodMap) ;
			
			// 科目余额汇总处理
			this.sumInitAccBalances(map,periods);
			
			// 更新临时表数据状态信息
			this.updateAccBalancesStatus(ledgerId, "Y");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: cancelAccount</p> 
	 * <p>Description: 取消建账 </p> 
	 * @param map
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#cancelAccount(java.util.HashMap)
	 */
	public void cancelAccount(HashMap<String,String> map)throws Exception {
		try {
			// 账簿ID
			String ledgerId = map.get("ledgerId") ;
			// 当前期间
			String periodCode = map.get("periodCode") ;
			
			// 删除科目余额数据
			sumInitBalanceMapper.delInitAccBalances(map);
			
			// 计算下属期间
			List<Period> periods = this.getAfterPeriods(ledgerId, periodCode) ;
			
			// 科目余额汇总处理
			this.sumInitAccBalances(map,periods);
			
			// 更新临时表数据状态信息
			this.updateAccBalancesStatus(ledgerId, "N");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * @Title: getInitAccounts 
	 * @Description: 查询初始化数据中待汇总的科目信息
	 * @param ledgerId
	 * @param isForeign
	 * @return
	 * @throws Exception    设定文件
	 */
	private List<HashMap<String,String>> getInitAccounts(String ledgerId,String isForeign)throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("isForeign", isForeign) ;
		
		return sumInitBalanceMapper.getSumAccounts(map) ;
	}
	
	/**
	 * @Title: sumInitAccBalances 
	 * @Description: 科目余额汇总处理 
	 * @param map
	 * @param periods
	 * @throws Exception    设定文件
	 */
	private void sumInitAccBalances(HashMap<String,String> map,List<Period> periods)throws Exception {
		// 账簿ID
		String ledgerId = map.get("ledgerId") ;
				
		// 汇总科目本币余额
		List<HashMap<String,String>> accounts = this.getInitAccounts(ledgerId, "Y") ;
		if(accounts != null && accounts.size()>0){
			for(HashMap<String,String> paramMap : accounts){
				paramMap.put("hrcyId", map.get("hrcyId")) ;
				paramMap.put("userId", map.get("userId")) ;
				paramMap.put("isCanceled", map.get("isCanceled")) ;
				paramMap.put("leafAccId", paramMap.get("accId")) ;
				paramMap.put("isLeaf", "Y") ;
				paramMap.put("bPeriod", map.get("bPeriod")) ;
				paramMap.put("periodCode", map.get("periodCode")) ;
				
				// 备份期间信息
				List<Period> copyPeriods = new ArrayList<Period>() ;
				copyPeriods.addAll(periods) ;
				
				this.sumInitBalancesByPeriods(paramMap, copyPeriods);
			}
		}
		
		// 汇总科目外币余额
		List<HashMap<String,String>> fAccounts = this.getInitAccounts(ledgerId, "N") ;
		if(fAccounts != null && fAccounts.size()>0){
			for(HashMap<String,String> paramMap : fAccounts){
				paramMap.put("hrcyId", map.get("hrcyId")) ;
				paramMap.put("userId", map.get("userId")) ;
				paramMap.put("isCanceled", map.get("isCanceled")) ;
				paramMap.put("leafAccId", paramMap.get("accId")) ;
				paramMap.put("isLeaf", "Y") ;
				paramMap.put("bPeriod", map.get("bPeriod")) ;
				paramMap.put("periodCode", map.get("periodCode")) ;
				
				// 备份期间信息
				List<Period> copyPeriods = new ArrayList<Period>() ;
				copyPeriods.addAll(periods) ;
				
				this.sumInitBalancesByPeriods(paramMap, copyPeriods);
			}
		}
	}
	
	/**
	 * @Title: sumInitBalancesByPeriods 
	 * @Description: 按期间逐个汇总科目余额
	 * @param paramMap
	 * @param periods
	 * @throws Exception    设定文件
	 */
	private void sumInitBalancesByPeriods(HashMap<String,String> map,List<Period> periods)throws Exception {
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
			// 年初期间
			map.put("yearStartPeriodCode", periodCode.substring(0, 4).concat("-01")) ;
			
			// 判断上年年末是否跨年: Y-跨年,N-不跨年
			if(periodCode.substring(0, 4).equals(preYearCode.substring(0, 4))){
				map.put("isDiffYear", "N") ; //
			}else{
				map.put("isDiffYear", "Y") ;
			}
			
			// 按科目层级汇总
			this.sumInitBalancesByAcc(map); 
			
			// 按期间递归
			if(periods != null && periods.size()>0){
				for(int i=0; i<periods.size(); i++){
					Period period = periods.get(i) ;
					map.put("periodCode", period.getPeriodCode()) ;
					map.put("accId", map.get("leafAccId")) ;
					map.put("isLeaf", "Y") ;
					periods.remove(i) ;
					// 递归汇总
					this.sumInitBalancesByPeriods(map,periods);
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: sumInitBalancesByAcc 
	 * @Description: 按科目层级汇总
	 * @param paramMap
	 * @throws Exception    设定文件
	 */
	private void sumInitBalancesByAcc(HashMap<String,String> map)throws Exception {
		try {
			// 计算科目余额信息
			String periodCode = map.get("periodCode") ;
			String bPeriod = map.get("bPeriod") ;
			String isCanceled = map.get("isCanceled") ;
			String isLeaf = map.get("isLeaf") ;
			// 建账操作时期初末级科目不需要执行汇总
			if(!(bPeriod.equals(periodCode) && "N".equals(isCanceled) && "Y".equals(isLeaf))){
				if(bPeriod.equals(periodCode)){
					map.put("isInitPeriod", "Y") ;
				}else{
					map.put("isInitPeriod", "N") ;
				}
				this.sumInitAccBalances(map);
			}
			
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
					this.sumInitBalancesByAcc(map);
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: sumInitAccBalances 
	 * @Description: 按单科目汇总数据
	 * @param map
	 * @throws Exception    设定文件
	 */
	private void sumInitAccBalances(HashMap<String,String> map)throws Exception {
		try {
			// 科目组合全编码
			String ccidKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CCID, map.get("ledgerId"))  ;
			Object json = this.getJsonStr(ccidKey, map.get("ccid")) ;
			if(json != null){
				JSONObject jo = new JSONObject(String.valueOf(json)) ;
				String fullCode = jo.getString("code") ;
				map.put("fullCode", fullCode.substring(fullCode.indexOf("/"))) ;
			}
			
			// 保存参数
			HashMap<String,Object> resultMap = new HashMap<String,Object>() ;
			resultMap.put("ledgerId", map.get("ledgerId")) ;
			resultMap.put("periodCode", map.get("periodCode")) ;
			resultMap.put("ccid", map.get("ccid")) ;
			resultMap.put("cnyCode", map.get("cnyCode")) ;
			resultMap.put("userId", map.get("userId")) ;
			resultMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			
			// 计算期初借方和贷方余额
			sumB(map, resultMap);
			
			// 计算本期借方和贷方发生额
			sumPTD(map, resultMap);
			
			// 计算年初借方和贷方余额
			sumY(map, resultMap);
			
			// 计算本年借方和贷方发生额
			sumYTD(map, resultMap);
			
			// 计算期末借方和贷方余额
			sumPJTD(map, resultMap);
			
			// 处理CCID
			this.handlerAccCCID(map, resultMap);
			
			// 保存科目余额
			this.saveInitAccBalances(resultMap);
			 
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: sumB 
	 * @Description: 计算期初借方和贷方余额
	 * @param map
	 * @param resultMap    设定文件
	 */
	private void sumB(HashMap<String, String> map, HashMap<String, Object> resultMap) {
		HashMap<String,Object> bmap = sumInitBalanceMapper.sumB(map) ;
		if(bmap != null && bmap.size()>0){
			Object b_dr = bmap.get("B_DR") ;
			Object b_cr = bmap.get("B_CR") ;
			Object t_b_dr = bmap.get("T_B_DR") ;
			Object t_b_cr = bmap.get("T_B_CR") ;				
			Object amt_b_dr = bmap.get("AMT_B_DR") ;
			Object amt_b_cr = bmap.get("AMT_B_CR") ;
			Object amt_t_b_dr = bmap.get("AMT_T_B_DR") ;
			Object amt_t_b_cr = bmap.get("AMT_T_B_CR") ;		
			
			resultMap.put("B_DR", b_dr==null ? 0:new BigDecimal(String.valueOf(b_dr)).toPlainString()) ;
			resultMap.put("B_CR", b_cr==null ? 0:new BigDecimal(String.valueOf(b_cr)).toPlainString()) ;
			resultMap.put("T_B_DR", t_b_dr==null ? 0:new BigDecimal(String.valueOf(t_b_dr)).toPlainString()) ;
			resultMap.put("T_B_CR", t_b_cr==null ? 0:new BigDecimal(String.valueOf(t_b_cr)).toPlainString()) ;
			resultMap.put("AMT_B_DR", amt_b_dr==null ? 0:new BigDecimal(String.valueOf(amt_b_dr)).toPlainString()) ;
			resultMap.put("AMT_B_CR", amt_b_cr==null ? 0:new BigDecimal(String.valueOf(amt_b_cr)).toPlainString()) ;
			resultMap.put("AMT_T_B_DR", amt_t_b_dr==null ? 0:new BigDecimal(String.valueOf(amt_t_b_dr)).toPlainString()) ;
			resultMap.put("AMT_T_B_CR", amt_t_b_cr==null ? 0:new BigDecimal(String.valueOf(amt_t_b_cr)).toPlainString()) ;
			
		}else{
			resultMap.put("B_DR", 0) ;
			resultMap.put("B_CR", 0) ;
			resultMap.put("T_B_DR", 0) ;
			resultMap.put("T_B_CR", 0) ;
			resultMap.put("AMT_B_DR", 0) ;
			resultMap.put("AMT_B_CR", 0) ;
			resultMap.put("AMT_T_B_DR", 0) ;
			resultMap.put("AMT_T_B_CR", 0) ;
		}
	}
	
	/**
	 * @Title: sumPTD 
	 * @Description: 计算本期借方和贷方发生额
	 * @param map
	 * @param resultMap    设定文件
	 */
	private void sumPTD(HashMap<String, String> map, HashMap<String, Object> resultMap) throws Exception {
		HashMap<String,Object> ptdmap = sumInitBalanceMapper.sumPTD(map) ;
		if(ptdmap != null && ptdmap.size()>0){
			Object ptd_dr = ptdmap.get("PTD_DR") ;
			Object ptd_cr = ptdmap.get("PTD_CR") ;
			Object t_ptd_dr = ptdmap.get("T_PTD_DR") ;
			Object t_ptd_cr = ptdmap.get("T_PTD_CR") ;				
			Object amt_ptd_dr = ptdmap.get("AMT_PTD_DR") ;
			Object amt_ptd_cr = ptdmap.get("AMT_PTD_CR") ;
			Object amt_t_ptd_dr = ptdmap.get("AMT_T_PTD_DR") ;
			Object amt_t_ptd_cr = ptdmap.get("AMT_T_PTD_CR") ;		
			
			resultMap.put("PTD_DR", ptd_dr==null ? 0:new BigDecimal(String.valueOf(ptd_dr)).toPlainString()) ;
			resultMap.put("PTD_CR", ptd_cr==null ? 0:new BigDecimal(String.valueOf(ptd_cr)).toPlainString()) ;
			resultMap.put("T_PTD_DR", t_ptd_dr==null ? 0:new BigDecimal(String.valueOf(t_ptd_dr)).toPlainString()) ;
			resultMap.put("T_PTD_CR", t_ptd_cr==null ? 0:new BigDecimal(String.valueOf(t_ptd_cr)).toPlainString()) ;
			resultMap.put("AMT_PTD_DR", amt_ptd_dr==null ? 0:new BigDecimal(String.valueOf(amt_ptd_dr)).toPlainString()) ;
			resultMap.put("AMT_PTD_CR", amt_ptd_cr==null ? 0:new BigDecimal(String.valueOf(amt_ptd_cr)).toPlainString()) ;
			resultMap.put("AMT_T_PTD_DR", amt_t_ptd_dr==null ? 0:new BigDecimal(String.valueOf(amt_t_ptd_dr)).toPlainString()) ;
			resultMap.put("AMT_T_PTD_CR", amt_t_ptd_cr==null ? 0:new BigDecimal(String.valueOf(amt_t_ptd_cr)).toPlainString()) ;
			
		}else{
			resultMap.put("PTD_DR", 0) ;
			resultMap.put("PTD_CR", 0) ;
			resultMap.put("T_PTD_DR", 0) ;
			resultMap.put("T_PTD_CR", 0) ;
			resultMap.put("AMT_PTD_DR", 0) ;
			resultMap.put("AMT_PTD_CR", 0) ;
			resultMap.put("AMT_T_PTD_DR", 0) ;
			resultMap.put("AMT_T_PTD_CR", 0) ;
		}
	}

	/**
	 * @Title: sumY 
	 * @Description: 计算年初借方和贷方余额
	 * @param map
	 * @param resultMap    设定文件
	 */
	private void sumY(HashMap<String, String> map, HashMap<String, Object> resultMap) throws Exception {
		HashMap<String,Object> ymap = sumInitBalanceMapper.sumY(map) ;
		if(ymap != null && ymap.size()>0){
			Object y_dr = ymap.get("Y_DR") ;
			Object y_cr = ymap.get("Y_CR") ;
			Object t_y_dr = ymap.get("T_Y_DR") ;
			Object t_y_cr = ymap.get("T_Y_CR") ;				
			Object amt_y_dr = ymap.get("AMT_Y_DR") ;
			Object amt_y_cr = ymap.get("AMT_Y_CR") ;
			Object amt_t_y_dr = ymap.get("AMT_T_Y_DR") ;
			Object amt_t_y_cr = ymap.get("AMT_T_Y_CR") ;		
			
			resultMap.put("Y_DR", y_dr==null ? 0:new BigDecimal(String.valueOf(y_dr)).toPlainString()) ;
			resultMap.put("Y_CR", y_cr==null ? 0:new BigDecimal(String.valueOf(y_cr)).toPlainString()) ;
			resultMap.put("T_Y_DR", t_y_dr==null ? 0:new BigDecimal(String.valueOf(t_y_dr)).toPlainString()) ;
			resultMap.put("T_Y_CR", t_y_cr==null ? 0:new BigDecimal(String.valueOf(t_y_cr)).toPlainString()) ;
			resultMap.put("AMT_Y_DR", amt_y_dr==null ? 0:new BigDecimal(String.valueOf(amt_y_dr)).toPlainString()) ;
			resultMap.put("AMT_Y_CR", amt_y_cr==null ? 0:new BigDecimal(String.valueOf(amt_y_cr)).toPlainString()) ;
			resultMap.put("AMT_T_Y_DR", amt_t_y_dr==null ? 0:new BigDecimal(String.valueOf(amt_t_y_dr)).toPlainString()) ;
			resultMap.put("AMT_T_Y_CR", amt_t_y_cr==null ? 0:new BigDecimal(String.valueOf(amt_t_y_cr)).toPlainString()) ;
			
		}else{
			resultMap.put("Y_DR", 0) ;
			resultMap.put("Y_CR", 0) ;
			resultMap.put("T_Y_DR", 0) ;
			resultMap.put("T_Y_CR", 0) ;
			resultMap.put("AMT_Y_DR", 0) ;
			resultMap.put("AMT_Y_CR", 0) ;
			resultMap.put("AMT_T_Y_DR", 0) ;
			resultMap.put("AMT_T_Y_CR", 0) ;
		}
	}

	/**
	 * @Title: sumYTD 
	 * @Description: 计算本年借方和贷方发生额
	 * @param map
	 * @param resultMap    设定文件
	 */
	private void sumYTD(HashMap<String, String> map, HashMap<String, Object> resultMap) throws Exception {
		HashMap<String,Object> ytdmap = sumInitBalanceMapper.sumYTD(map) ;
		if(ytdmap != null && ytdmap.size()>0){
			Object ytd_dr = ytdmap.get("YTD_DR") ;
			Object ytd_cr = ytdmap.get("YTD_CR") ;
			Object t_ytd_dr = ytdmap.get("T_YTD_DR") ;
			Object t_ytd_cr = ytdmap.get("T_YTD_CR") ;				
			Object amt_ytd_dr = ytdmap.get("AMT_YTD_DR") ;
			Object amt_ytd_cr = ytdmap.get("AMT_YTD_CR") ;
			Object amt_t_ytd_dr = ytdmap.get("AMT_T_YTD_DR") ;
			Object amt_t_ytd_cr = ytdmap.get("AMT_T_YTD_CR") ;		
			
			resultMap.put("YTD_DR", ytd_dr==null ? 0:new BigDecimal(String.valueOf(ytd_dr)).toPlainString()) ;
			resultMap.put("YTD_CR", ytd_cr==null ? 0:new BigDecimal(String.valueOf(ytd_cr)).toPlainString()) ;
			resultMap.put("T_YTD_DR", t_ytd_dr==null ? 0:new BigDecimal(String.valueOf(t_ytd_dr)).toPlainString()) ;
			resultMap.put("T_YTD_CR", t_ytd_cr==null ? 0:new BigDecimal(String.valueOf(t_ytd_cr)).toPlainString()) ;
			resultMap.put("AMT_YTD_DR", amt_ytd_dr==null ? 0:new BigDecimal(String.valueOf(amt_ytd_dr)).toPlainString()) ;
			resultMap.put("AMT_YTD_CR", amt_ytd_cr==null ? 0:new BigDecimal(String.valueOf(amt_ytd_cr)).toPlainString()) ;
			resultMap.put("AMT_T_YTD_DR", amt_t_ytd_dr==null ? 0:new BigDecimal(String.valueOf(amt_t_ytd_dr)).toPlainString()) ;
			resultMap.put("AMT_T_YTD_CR", amt_t_ytd_cr==null ? 0:new BigDecimal(String.valueOf(amt_t_ytd_cr)).toPlainString()) ;
			
		}else{
			resultMap.put("YTD_DR", 0) ;
			resultMap.put("YTD_CR", 0) ;
			resultMap.put("T_YTD_DR", 0) ;
			resultMap.put("T_YTD_CR", 0) ;
			resultMap.put("AMT_YTD_DR", 0) ;
			resultMap.put("AMT_YTD_CR", 0) ;
			resultMap.put("AMT_T_YTD_DR", 0) ;
			resultMap.put("AMT_T_YTD_CR", 0) ;
		}
	}

	/**
	 * @Title: sumPJTD 
	 * @Description: 计算期末借方和贷方余额
	 * @param map
	 * @param resultMap    设定文件
	 */
	private void sumPJTD(HashMap<String, String> map, HashMap<String, Object> resultMap)  throws Exception {
		if("Y".equals(map.get("isInitPeriod"))){
			HashMap<String,Object> pjtdmap = sumInitBalanceMapper.sumPJTD(map) ;
			if(pjtdmap !=null && pjtdmap.size()>0){
				Object pjtd_dr = pjtdmap.get("PJTD_DR") ;
				Object pjtd_cr = pjtdmap.get("PJTD_CR") ;
				Object t_pjtd_dr = pjtdmap.get("T_PJTD_DR") ;
				Object t_pjtd_cr = pjtdmap.get("T_PJTD_CR") ;				
				Object amt_pjtd_dr = pjtdmap.get("AMT_PJTD_DR") ;
				Object amt_pjtd_cr = pjtdmap.get("AMT_PJTD_CR") ;
				Object amt_t_pjtd_dr = pjtdmap.get("AMT_T_PJTD_DR") ;
				Object amt_t_pjtd_cr = pjtdmap.get("AMT_T_PJTD_CR") ;		
				
				resultMap.put("PJTD_DR", pjtd_dr==null ? 0:new BigDecimal(String.valueOf(pjtd_dr)).toPlainString()) ;
				resultMap.put("PJTD_CR", pjtd_cr==null ? 0:new BigDecimal(String.valueOf(pjtd_cr)).toPlainString()) ;
				resultMap.put("T_PJTD_DR", t_pjtd_dr==null ? 0:new BigDecimal(String.valueOf(t_pjtd_dr)).toPlainString()) ;
				resultMap.put("T_PJTD_CR", t_pjtd_cr==null ? 0:new BigDecimal(String.valueOf(t_pjtd_cr)).toPlainString()) ;
				resultMap.put("AMT_PJTD_DR", amt_pjtd_dr==null ? 0:new BigDecimal(String.valueOf(amt_pjtd_dr)).toPlainString()) ;
				resultMap.put("AMT_PJTD_CR", amt_pjtd_cr==null ? 0:new BigDecimal(String.valueOf(amt_pjtd_cr)).toPlainString()) ;
				resultMap.put("AMT_T_PJTD_DR", amt_t_pjtd_dr==null ? 0:new BigDecimal(String.valueOf(amt_t_pjtd_dr)).toPlainString()) ;
				resultMap.put("AMT_T_PJTD_CR", amt_t_pjtd_cr==null ? 0:new BigDecimal(String.valueOf(amt_t_pjtd_cr)).toPlainString()) ;
			}else{
				resultMap.put("PJTD_DR", 0) ;
				resultMap.put("PJTD_CR", 0) ;
				resultMap.put("AMT_PJTD_DR", 0) ;
				resultMap.put("AMT_PJTD_CR", 0) ;
				resultMap.put("T_PJTD_DR", 0) ;
				resultMap.put("T_PJTD_CR", 0) ;
				resultMap.put("AMT_T_PJTD_DR", 0) ;
				resultMap.put("AMT_T_PJTD_CR",0) ;
			}
			
		}else{
			BigDecimal y_dr = new BigDecimal(String.valueOf(resultMap.get("Y_DR"))) ;
			BigDecimal ytd_dr = new BigDecimal(String.valueOf(resultMap.get("YTD_DR"))) ;
			BigDecimal pjtd_dr = y_dr.add(ytd_dr) ;
			
			BigDecimal y_cr = new BigDecimal(String.valueOf(resultMap.get("Y_CR"))) ;
			BigDecimal ytd_cr = new BigDecimal(String.valueOf(resultMap.get("YTD_CR"))) ;
			BigDecimal pjtd_cr = y_cr.add(ytd_cr) ;
			
			BigDecimal amt_y_dr = new BigDecimal(String.valueOf(resultMap.get("AMT_Y_DR"))) ;
			BigDecimal amt_ytd_dr = new BigDecimal(String.valueOf(resultMap.get("AMT_YTD_DR"))) ;
			BigDecimal amt_pjtd_dr = amt_y_dr.add(amt_ytd_dr) ;
			
			BigDecimal amt_y_cr = new BigDecimal(String.valueOf(resultMap.get("AMT_Y_CR"))) ;
			BigDecimal amt_ytd_cr = new BigDecimal(String.valueOf(resultMap.get("AMT_YTD_CR"))) ;
			BigDecimal amt_pjtd_cr = amt_y_cr.add(amt_ytd_cr) ;
			
			BigDecimal t_y_dr = new BigDecimal(String.valueOf(resultMap.get("T_Y_DR"))) ;
			BigDecimal t_ytd_dr = new BigDecimal(String.valueOf(resultMap.get("T_YTD_DR"))) ;
			BigDecimal t_pjtd_dr = t_y_dr.add(t_ytd_dr) ;
			
			BigDecimal t_y_cr = new BigDecimal(String.valueOf(resultMap.get("T_Y_CR"))) ;
			BigDecimal t_ytd_cr = new BigDecimal(String.valueOf(resultMap.get("T_YTD_CR"))) ;
			BigDecimal t_pjtd_cr = t_y_cr.add(t_ytd_cr) ;
			
			BigDecimal amt_t_y_dr = new BigDecimal(String.valueOf(resultMap.get("AMT_T_Y_DR"))) ;
			BigDecimal amt_t_ytd_dr = new BigDecimal(String.valueOf(resultMap.get("AMT_T_YTD_DR"))) ;
			BigDecimal amt_t_pjtd_dr = amt_t_y_dr.add(amt_t_ytd_dr) ;
			
			BigDecimal amt_t_y_cr = new BigDecimal(String.valueOf(resultMap.get("AMT_T_Y_CR"))) ;
			BigDecimal amt_t_ytd_cr = new BigDecimal(String.valueOf(resultMap.get("AMT_T_YTD_CR"))) ;
			BigDecimal amt_t_pjtd_cr = amt_t_y_cr.add(amt_t_ytd_cr) ;
			
			resultMap.put("PJTD_DR", pjtd_dr.toPlainString()) ;
			resultMap.put("PJTD_CR", pjtd_cr.toPlainString()) ;
			resultMap.put("AMT_PJTD_DR", amt_pjtd_dr.toPlainString()) ;
			resultMap.put("AMT_PJTD_CR", amt_pjtd_cr.toPlainString()) ;
			resultMap.put("T_PJTD_DR", t_pjtd_dr.toPlainString()) ;
			resultMap.put("T_PJTD_CR", t_pjtd_cr.toPlainString()) ;
			resultMap.put("AMT_T_PJTD_DR", amt_t_pjtd_dr.toPlainString()) ;
			resultMap.put("AMT_T_PJTD_CR",amt_t_pjtd_cr.toPlainString()) ;
		}
	}

	/**
	 * @Title: saveInitAccBalances 
	 * @Description: 保存科目余额数据
	 * @param map
	 * @throws Exception    设定文件
	 */
	private void saveInitAccBalances(HashMap<String,Object> map)throws Exception {
		// 保存数据
		try {
			sumInitBalanceMapper.insInitAccBalances(map);
			
		} catch (Exception e) {
			sumInitBalanceMapper.updInitAccBalances(map);
		}
		
		// 清除余额为零的数据
		sumInitBalanceMapper.delInitAccBalancesByZero(map);
	}
	
	/**
	 * @Title: updateAccBalancesStatus 
	 * @Description: 更新临时表数据状态信息
	 * @param ledgerId
	 * @param isValid
	 * @throws Exception    设定文件
	 */
	private void updateAccBalancesStatus(String ledgerId,String isVaild)throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("isValid", isVaild) ;
		
		sumInitBalanceMapper.updateTmpAccBalancesStatus(map);
	}
}
