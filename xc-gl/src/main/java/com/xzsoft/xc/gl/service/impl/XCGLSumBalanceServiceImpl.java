package com.xzsoft.xc.gl.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO;
import com.xzsoft.xc.gl.modal.Period;
import com.xzsoft.xc.gl.service.LedgerService;
import com.xzsoft.xc.gl.service.XCGLSumBalanceService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: SumBalanceServiceImpl 
 * @Description: 余额汇总处理服务接口实现类
 * @author linp
 * @date 2016年1月7日 上午10:47:43 
 *
 */
@Service("xcglSumBalanceService")
public class XCGLSumBalanceServiceImpl implements XCGLSumBalanceService {

	// 日志记录器
	private static final Logger log = Logger.getLogger(XCGLSumBalanceServiceImpl.class.getName()) ;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
	@Resource
	private XCGLSumBalanceDAO xcglSumBalanceDAOImpl ;
	@Resource
	private XCGLSumBalanceDAO xcglSumInitBalanceDAOImpl ;
	@Resource
	private XCGLSumBalanceDAO xcglSumInitCashDAOImpl ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	@Resource
	private LedgerService ledgerService ;

	/*
	 * (非 Javadoc) 
	 * <p>Title: sumGLBalances</p> 
	 * <p>Description: 余额汇总处理  </p> 
	 * @param ja		// 凭证头ID数组
	 * @param isAccount
	 * @param isSumCash
	 * @param userId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLSumBalanceService#sumGLBalances(org.codehaus.jettison.json.JSONArray, java.lang.String, java.lang.String, java.lang.String)
	 */
	public JSONObject sumGLBalances(JSONArray ja, String isAccount, String isSumCash, String userId, String isCanceled) throws Exception {
		JSONObject obj = new JSONObject() ;
		try {
			if(ja != null && ja.length() > 0){
				for(int i=0; i<ja.length(); i++){
					JSONObject jo = ja.getJSONObject(i) ;
					// 凭证头ID
					String headId = jo.getString("V_HEAD_ID") ;
					
					// 科目体系ID
					String hrcyId = xcglSumBalanceDAOImpl.getHrcyIdByHeadId(headId) ;
					
					// 按凭证汇总数据
					XCGLSumBalanceService service = (XCGLSumBalanceService) AppContext.getApplicationContext().getBean("xcglSumBalanceService") ;
					service.sumDataByVoucher(headId, hrcyId, isAccount, isSumCash, userId, isCanceled);
				}
				
				obj.put("flag", "0") ;
				obj.put("msg", "") ;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			obj.put("flag", "1") ;
			obj.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return obj;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: sumDataByVoucher</p> 
	 * <p>Description: 按凭证汇总数据</p> 
	 * @param headId
	 * @param hrcyId
	 * @param isAccount
	 * @param isSumCash
	 * @param userId
	 * @param isCanceled
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLSumBalanceService#sumDataByVoucher(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void sumDataByVoucher(String headId,String hrcyId ,String isAccount, String isSumCash, String userId, String isCanceled) throws Exception {
		
		// 计算凭证下待汇总的所有科目信息(本位币)
		List<HashMap<String,String>> maps = xcglSumBalanceDAOImpl.getSumAccounts(headId,"N",isAccount) ;
		if(maps != null && maps.size() >0){
			// 获取账簿本位币信息
			String currencyCode = xcglSumBalanceDAOImpl.getCurrency4Ledger(headId) ;
			
			for(int j=0; j<maps.size(); j++){
				HashMap<String,String> map = maps.get(j) ;
				map.put("hrcyId", hrcyId) ;
				map.put("headId", headId) ;
				map.put("isAccount", isAccount) ;
				map.put("isForeign", "N") ;
				map.put("cnyCode", currencyCode) ;
				map.put("userId", userId) ;
				map.put("isSumCash", isSumCash) ;
				
				log.debug("科目：acc_id = " + map.get("accId") + "汇总开始");
				
				// 按期间汇总处理
				this.sumVoucherDataByPeriod(map);
				
				log.debug("科目：acc_id = " + map.get("accId") + "汇总结束");
			}
		}
		
		// 计算凭证下待汇总的所有外币科目信息(原币)
		List<HashMap<String,String>> fmaps = xcglSumBalanceDAOImpl.getSumAccounts(headId,"Y",isAccount) ;
		if(fmaps != null && fmaps.size() >0){
			for(int j=0; j<fmaps.size(); j++){
				HashMap<String,String> map = fmaps.get(j) ;
				
				// 查询科目的默认币种
				String accCnyCode = xcglSumBalanceDAOImpl.getCurrency4Acc(map.get("accId"), map.get("ledgerId")) ;
				
				map.put("hrcyId", hrcyId) ;
				map.put("headId", headId) ;
				map.put("isAccount", isAccount) ;
				map.put("isForeign", "Y") ;
				map.put("cnyCode", accCnyCode) ;
				map.put("userId", userId) ;
				map.put("isSumCash", "BA") ;	// 值处理总账余额汇总,不计算现金流量项目
				
				// 按期间汇总处理
				this.sumVoucherDataByPeriod(map);
			}
		}
		
		// 处理凭证汇总标志信息
		if("ALL".equals(isSumCash) || "BA".equals(isSumCash)){  
			xcglSumBalanceDAOImpl.handlerSumFlag(headId,userId,isCanceled);
		}
	}
	
	/**
	 * @Title: sumVoucherDataByPeriod 
	 * @Description: 按期间汇总处理
	 * @param map
	 * @throws Exception    设定文件
	 */
	private void sumVoucherDataByPeriod(HashMap<String,String> map) throws Exception { 
		// 封装参数
		map.put("accCode", "") ;	
		map.put("isLeaf", "Y") ;
		map.put("leafAccId", map.get("accId")) ; 	// 当前末级科目ID
		map.put("dbType", PlatformUtil.getDbType()) ;
		
		// 当前账簿向下开启的期间
		List<Period> periods = xcglSumBalanceDAOImpl.getAfterPeriods(map.get("ledgerId"), map.get("periodCode")) ;
		
		// 备份期间信息
		List<Period> clonePeriods = new ArrayList<Period>() ;
		clonePeriods.addAll(periods) ;
		
		// 是否统计现金流量信息
		String isSumCash = map.get("isSumCash") ;
		
		// 执行余额汇总和统计现金流量项目本期发生数
		if("ALL".equals(isSumCash)){
			// 开始汇总期间
			String startPeirodCode = map.get("periodCode") ;
			String leafAccId = map.get("leafAccId") ;
			
			// 汇总凭证余额
			xcglSumBalanceDAOImpl.sumAccDataByPeriod(map,periods);
			
			// 统计现金流量项目
			map.put("periodCode", startPeirodCode) ;
			map.put("accId", leafAccId) ;
			xcglSumBalanceDAOImpl.sumCashDataByPeirod(map);
			
		}else if("CA".equals(isSumCash)){ 
			
			// 统计现金流量项目
			xcglSumBalanceDAOImpl.sumCashDataByPeirod(map);
			
		}else if("BA".equals(isSumCash)){
			
			// 汇总凭证余额
			xcglSumBalanceDAOImpl.sumAccDataByPeriod(map,periods);
			
		}else{
			// 扩展使用
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: sumInitBalances</p> 
	 * <p>Description: 建账与取消建账时修改科目期初余额 </p> 
	 * @param map
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLSumBalanceService#createOrCancelAccount(org.codehaus.jettison.json.JSONObject)
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject sumInitBalances(HashMap<String,String> map) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// N-建账, Y-取消建账
			String isCanceled = map.get("isCanceled");
			// 账簿ID
			String ledgerId = map.get("ledgerId") ;
			// 查询账簿基本信息
			Ledger ledger = xcglCommonDAO.getLedger(ledgerId) ;
			// 计算上期期间
			String prePeriodCode = xcglSumBalanceDAOImpl.getPrePeriodCode(ledger.getStartPeriodCode()) ;
			
			// 处理参数
			map.put("prePeriodCode", prePeriodCode) ;
			map.put("bPeriod", prePeriodCode) ;
			map.put("hrcyId", ledger.getAccHrcyId()) ;
			map.put("cnyCode", ledger.getCnyCode()) ;
			map.put("ldStartPeriod", ledger.getStartPeriodCode()) ;
			
			if("N".equals(isCanceled)){
				map.put("periodCode", prePeriodCode) ;
				
				// 执行建账
				xcglSumInitBalanceDAOImpl.createAccount(map);
				
				// 写入建账标志: 1-已建账,0-未建账
				ledgerService.updateCreateAccountFlag(ledgerId, "1", "BA");
				
				// 提示信息
				jo.put("flag", "0");
				//jo.put("msg", "建账成功!") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_JZCG1", null)) ;
				
			}else{ 
				map.put("periodCode", ledger.getStartPeriodCode()) ;
				
				// 取消建账
				xcglSumInitBalanceDAOImpl.cancelAccount(map);
			
				// 写入建账标志: 1-已建账,0-未建账
				ledgerService.updateCreateAccountFlag(ledgerId, "0", "BA");
				
				// 提示信息
				jo.put("flag", "0");
				//jo.put("msg", "取消建账成功!") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_QXJZCG1", null)) ;
				
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: sumInitCash</p> 
	 * <p>Description: 统计现金流量项目</p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLSumBalanceService#sumInitCash(java.lang.String)
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject sumInitCash(String ledgerId,String isCanceled) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 建账执行现金流量统计处理
			if("N".equals(isCanceled)){
				// 执行统计
				xcglSumInitCashDAOImpl.sumInitCashByCALevel(ledgerId);
				
				// 更新现金流量临时表数据状态信息
				xcglSumBalanceDAOImpl.updateCashBalStatus(ledgerId, "Y");
				
				// 写入建账标志: 1-已建账,0-未建账
				ledgerService.updateCreateAccountFlag(ledgerId, "1", "CA");
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "建账成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_JZCG1", null)) ;
				
				
			}else{
				// 取消建账
				Ledger ledger = xcglCommonDAO.getLedger(ledgerId) ;
				String startPeriodCode = ledger.getStartPeriodCode() ;
				
				// 计算上期期间
				String prePeriodCode = xcglSumBalanceDAOImpl.getPrePeriodCode(startPeriodCode) ;
				
				// 删除期初建账数据
				xcglSumInitCashDAOImpl.delInitCash(ledgerId, prePeriodCode);
				
				// 更新现金流量临时表数据状态信息
				xcglSumBalanceDAOImpl.updateCashBalStatus(ledgerId, "N");
				
				// 写入建账标志: 1-已建账,0-未建账
				ledgerService.updateCreateAccountFlag(ledgerId, "0", "CA");
	
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "取消建账成功") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_QXJZCG1", null)) ;
			}
			
		} catch (Exception e) {
			jo.put("flag", "1") ;
			if("N".equals(isCanceled)){
				//jo.put("msg", "建账失败:"+e.getMessage()) ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_JZSB", null)+e.getMessage());
			}else{
				//jo.put("msg", "取消建账失败:"+e.getMessage()) ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_QXJZSB", null)+e.getMessage());
			}
		}
		
		return jo ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: sumGLBal</p> 
	 * <p>Description: 汇总总账余额表 </p> 
	 * @param ja
	 * @param hrcyId
	 * @param isAccount
	 * @param isSumCash
	 * @param userId
	 * @param isCanceled
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLSumBalanceService#sumGLBal(org.codehaus.jettison.json.JSONArray, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sumGLBal(JSONArray ja, String hrcyId, String isAccount, String isSumCash, String userId, String isCanceled) throws Exception {
		if(ja != null && ja.length() > 0){
			log.debug("本次汇总共 "+ja.length()+" 条凭证");
			for(int i=0; i<ja.length(); i++){
				
				log.debug("汇总第 "+i+" 条凭证开始 "+sdf.format(new Date()));
				
				JSONObject jo = ja.getJSONObject(i) ;
				// 凭证头ID
				String headId = jo.getString("V_HEAD_ID") ;
				
				log.debug("V_HEAD_ID = " + headId);
				
				XCGLSumBalanceService service = (XCGLSumBalanceService) AppContext.getApplicationContext().getBean("xcglSumBalanceService") ;
				service.sumDataByVoucher(headId, hrcyId, isAccount, isSumCash, userId, isCanceled);
				
				log.debug("汇总第 "+i+" 条凭证结束 "+sdf.format(new Date()));
			}
		}
		
	}
	
}
