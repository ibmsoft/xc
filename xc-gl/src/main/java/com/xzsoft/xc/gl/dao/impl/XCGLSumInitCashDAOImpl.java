package com.xzsoft.xc.gl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.mapper.SumBalanceMapper;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

@Repository("xcglSumInitCashDAOImpl")
public class XCGLSumInitCashDAOImpl extends XCGLSumBalanceDAOImpl {
	
	// 日志记录器
	private static final Logger log = Logger.getLogger(XCGLSumInitCashDAOImpl.class.getName()) ;
	@Resource
	private SumBalanceMapper sumBalanceMapper ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	
	
	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: sumInitCashByCALevel</p> 
	 * <p>Description: 期初现金流量余额统计 </p> 
	 * @param ledgerId
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLSumBalanceDAO#sumInitCash(java.util.HashMap)
	 */
	/* (非 Javadoc) 
	 * <p>Title: sumInitCashByCALevel</p> 
	 * <p>Description: </p> 
	 * @param ledgerId
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.impl.XCGLSumBalanceDAOImpl#sumInitCashByCALevel(java.lang.String) 
	 */
	public void sumInitCashByCALevel(String ledgerId)throws Exception {
		try {
			// 账簿开始期间
			Ledger ledger = xcglCommonDAO.getLedger(ledgerId) ;
			String startPeriodCode = ledger.getStartPeriodCode() ;
			
			// 计算上期期间
			String prePeriodCode = this.getPrePeriodCode(startPeriodCode) ;
			
			// 删除期初建账数据
			this.delInitCash(ledgerId, prePeriodCode);
			
			// 将临时表数据拷贝到正式表
			HashMap<String,Object> copyMap = new HashMap<String,Object>();
			copyMap.put("ledgerId", ledgerId) ;
			copyMap.put("userId", CurrentSessionVar.getUserId()) ;
			copyMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
			sumBalanceMapper.copyTmpInitCash(copyMap);
			
			// 按现金流量项目循环统计
			HashMap<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("ledgerId", ledgerId) ;
			paramMap.put("periodCode", prePeriodCode) ;
			List<HashMap<String,Object>> maps = sumBalanceMapper.getInitCash(paramMap) ;
			if(maps != null && maps.size()>0){
				String accId = null ;
				String upCaId = null ;
				for(int i=0;i<maps.size();i++){
					HashMap<String,Object> map = maps.get(i) ;
					// 当前科目ID和现金流量项目父级ID
					String newAccId = String.valueOf(map.get("accId")) ;
					String newUpCaId = String.valueOf(map.get("upCaId")) ;
					
					if(newAccId.equals(accId) && newUpCaId.equals(upCaId)){
						continue ;
					}else{
						map.put("isLeaf", "Y") ;
						// 按现金流量项目层级执行统计
						this.sumInitCash(map);
					}
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: sumInitCash 
	 * @Description: 统计非末级的现金流量项目层级期初余额
	 * @param map
	 * @throws Exception    设定文件
	 */
	private void sumInitCash(HashMap<String,Object> map)throws Exception {
		try {
			// 是否末级
			String isLeaf = String.valueOf(map.get("isLeaf")) ;
			if("N".equals(isLeaf)){
				// 汇总现金流
				HashMap<String,Object> resultMap = sumBalanceMapper.getInitCashByAcc(map) ;
				
				// 设置更新时间和更新人
				resultMap.put("isAccount", "Y") ;
				resultMap.put("sumId", UUID.randomUUID().toString()) ;
				resultMap.put("caId", map.get("caId")) ;
				resultMap.put("userId", CurrentSessionVar.getUserId()) ;
				resultMap.put("cdate", CurrentUserUtil.getCurrentDate()) ;
				
				// 执行保存
				try {
					sumBalanceMapper.insInitCashBalances(resultMap);
					
				} catch (Exception e) {
					sumBalanceMapper.updInitCashBalances(resultMap);
				}
			}
			
			// 逐级统计
			String key = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_CASH_ITEMS) ;
			String caJson = this.getJsonStr(key, String.valueOf(map.get("caId"))) ;
			JSONObject caJo = new JSONObject(caJson) ;
			if(caJo != null){
				// 递归汇总上级现金流量余额
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
					// 方向不为空时执行统计
					if(direction != null){
						map.put("caId", upCaJo.getString("id")) ;
						map.put("isLeaf", "N") ;
						
						// 递归处理
						this.sumInitCash(map);
					}
				}
			}	
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e; 
		}
	}
}
