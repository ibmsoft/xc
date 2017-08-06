package com.xzsoft.xc.util.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.dao.RuleDAO;
import com.xzsoft.xc.util.mapper.RuleMapper;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Rule;
import com.xzsoft.xc.util.modal.RuleDtl;
import com.xzsoft.xc.util.modal.RuleMaxNum;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: RuleDAOImpl 
 * @Description: 云ERP规则处理数据层接口实现类 
 * @author linp
 * @date 2016年3月9日 下午10:11:00 
 *
 */
@Repository("ruleDAO")
public class RuleDAOImpl implements RuleDAO {
	
	// 日志记录器
	private static final Logger log = Logger.getLogger(RuleDAOImpl.class.getName()) ;
	
	@Resource
	private RuleMapper ruleMapper ; 
	@Resource
	private RedisDao redisDaoImpl ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getRule</p> 
	 * <p>Description: 获取规则信息 </p> 
	 * @param ruleCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.RuleDAO#getRule(java.lang.String)
	 */
	@Override
	public Rule getRule(String ruleCode) throws Exception {
		Rule rule = new Rule() ;
		
		try {
			// 查询规则基础信息
			rule = ruleMapper.getRule(ruleCode) ;
			
			// 查询基础明细信息
			rule.setRuleDtls(ruleMapper.getRuleDtl(ruleCode));
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return rule ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getRuleDtl</p> 
	 * <p>Description: 获取规则明细信息 </p> 
	 * @param ruleCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.RuleDAO#getRuleDtl(java.lang.String)
	 */
	@Override
	public List<RuleDtl> getRuleDtls(String ruleCode) throws Exception {
		List<RuleDtl> ruleDtls = new ArrayList<RuleDtl>();
		
		try {
			// 查询规则明细信息
			ruleDtls = ruleMapper.getRuleDtl(ruleCode) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return ruleDtls ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getRuleMaxNum</p> 
	 * <p>Description: 获取规则最大流水号 </p> 
	 * @param ledgerId
	 * @param categoryId
	 * @param year
	 * @param month
	 * @param rule
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.RuleDAO#getRuleMaxNum(java.lang.String, java.lang.String, java.lang.String, com.xzsoft.xc.gl.modal.Rule)
	 */
	@Override
	public synchronized String getRuleMaxNum(String ledgerId,String categoryId,String year,String month,Rule rule,String userId) throws Exception {
		String serialNum = null ;
		
		try {
			// 获取当前账簿下凭证的最大流水号
			List<RuleDtl> ruleDtls = rule.getRuleDtls() ;
			StringBuffer str = new StringBuffer() ;
			if(ruleDtls != null && ruleDtls.size()>0){
				for(int i=0; i<ruleDtls.size(); i++){
					RuleDtl dtl = ruleDtls.get(i) ;
					String dtlCode = dtl.getDtlCode() ;
					str.append("[").append(dtlCode).append("]");
					if(i != ruleDtls.size()-1){
						str.append("-") ;
					}
				}
			}
			String dtlPrefixUnion = str.toString() ;
			
			// 查询参数
			HashMap<String,String> map = new HashMap<String,String>() ;
			map.put("ledgerId", ledgerId) ;
			map.put("ruleCode", rule.getRuleCode()) ;
			map.put("fixedPrefix", rule.getFixedPrefix()) ;
			map.put("flag", rule.getFixedPrefix()==null ? "NE":"E") ;
			map.put("dtlPrefixUnion", dtlPrefixUnion) ;
			map.put("serialCategory", rule.getSerialCategory()) ;
			map.put("year", year);
			map.put("month", month);
			
			// 执行查询
			RuleMaxNum rmn = ruleMapper.getRuleMaxNum(map) ;
			
			// 计算下一个编号
			int nextNum = 1 ;
			if(rmn != null) nextNum = rmn.getMaxNum()+1 ;
			
			// 计算凭证编号信息
			serialNum = this.getNextSerialNum(ledgerId, categoryId, year, month, nextNum, rule) ;
			
			if(rmn != null){
				// 更新最大流水号
				rmn.setMaxNum(nextNum);
				rmn.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
				rmn.setLastUpdatedBy(userId);
				
				ruleMapper.updateRuleMaxNum(rmn);
				
			}else{
				// 记录最大流水号
				RuleMaxNum ruleMaxNum = new RuleMaxNum() ;
				
				ruleMaxNum.setMaxNumId(UUID.randomUUID().toString());
				ruleMaxNum.setLedgerId(ledgerId);
				ruleMaxNum.setRuleCode(rule.getRuleCode());
				ruleMaxNum.setDtlPrefixUnion(dtlPrefixUnion);
				ruleMaxNum.setFixedPrefix(rule.getFixedPrefix());
				if(rule.getSerialCategory().equals(XConstants.SERIAL_TYPE_LD_YM)){
					ruleMaxNum.setYear(year);
					ruleMaxNum.setMonth(month);
				}else if(rule.getSerialCategory().equals(XConstants.SERIAL_TYPE_LD_Y)){
					ruleMaxNum.setYear(year);
				}else{
				}
				ruleMaxNum.setMaxNum(nextNum);
				ruleMaxNum.setCreatedBy(userId);
				ruleMaxNum.setCreationDate(CurrentUserUtil.getCurrentDate());
				
				ruleMapper.insertRuleMaxNum(ruleMaxNum);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return serialNum;
	}
	
	/**
	 * @Title: getNextSerialNum 
	 * @Description: 计算下一个凭证编号
	 * @param ledgerId
	 * @param categoryId
	 * @param year
	 * @param month
	 * @param maxNum
	 * @param rule
	 * @return
	 * @throws Exception
	 */
	private String getNextSerialNum(String ledgerId,String categoryId,String year,String month, int nextNum,Rule rule)throws Exception {
		String serialNum = null ;
		
		try {
			// 固定前缀
			String fixedPrefix = rule.getFixedPrefix() ;
					
			// 规则明细
			StringBuffer str = new StringBuffer() ;
			List<RuleDtl> dtls = rule.getRuleDtls() ;
			if(dtls != null && dtls.size() >0){
				
				for(int i=0; i<dtls.size(); i++){
					RuleDtl dtl = dtls.get(i) ;
					String dtlCode = dtl.getDtlCode() ; 
					String dtlAcutalVal = this.getDtlAcutalValue(ledgerId,categoryId, dtlCode, year, month) ;
					str.append(dtlAcutalVal).append(dtl.getDtlSeparator()==null ? "":dtl.getDtlSeparator()) ;
				}
			}
			
			// 凭证编号组合前缀
			String prefixUnion = (fixedPrefix ==null ? "" :fixedPrefix).concat(str.toString()) ;

			// 当前流水号
			String serialNo = String.valueOf(nextNum) ;
			
			// 如果当前流水号未到达规则流水号约定长度,则执行补0处理
			int len = rule.getSerialLength() ;
			int slen = serialNo.length() ; 
			if(len > slen){
				for(int i=0; i<len-slen; i++){
					serialNo = "0".concat(serialNo) ;
				}
			}
			
			// 凭证编号
			serialNum = prefixUnion.concat(serialNo) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return serialNum ;
	}
	
	/**
	 * @Title: getDtlAcutalValue 
	 * @Description: 获取明细项目实际值
	 * 
	 * YYYY
	 * YY
	 * MM
	 * LEDGER
	 * V_CATEGORY
	 * 
	 * @param dtlCode
	 * @return
	 * @throws Exception
	 */
	private String getDtlAcutalValue(String ledgerId, String categoryId, String dtlCode,String year,String month) throws Exception {
		String val = null ;
		
		if(dtlCode.equals(XConstants.RULE_DTL_YYYY)){
			val = year ;
			
		}else if(dtlCode.equals(XConstants.RULE_DTL_YY)){
			val = year.substring(2) ;
			
		}else if(dtlCode.equals(XConstants.RULE_DTL_MM)){
			val = month ;
			
		}else if(dtlCode.equals(XConstants.RULE_DTL_LEDGER)){
			// 计算账簿编码
			String ldKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_LEDGERS) ;
			Object ldJson = redisDaoImpl.hashGet(ldKey, ledgerId) ;
			if(ldJson != null && !"".equals(ldJson)){
				//从缓存获取账簿编码
				JSONObject jo = new JSONObject(String.valueOf(ldJson)) ;
				val = jo.getString("code") ;
			}else{
				// 从数据库获取账簿编码
				Ledger ledger = ruleMapper.getLegerById(ledgerId) ;
				val = ledger.getLedgerCode() ;
				
				// 将当前账簿信息同步到Redis缓存
				JSONObject valJo = new JSONObject() ;
				valJo.put("id", ledger.getLedgerId()) ;
				valJo.put("code", ledger.getLedgerCode()) ;
				valJo.put("name", ledger.getLedgerName()) ;
				valJo.put("hrcyId", ledger.getAccHrcyId()) ;			
				valJo.put("orgId", ledger.getAccHrcyId()) ;		
				
				redisDaoImpl.hashPut(ldKey, ledger.getLedgerId(), valJo.toString());
				redisDaoImpl.hashPut(ldKey, ledger.getLedgerCode(), valJo.toString());
			}
			
		}else if(dtlCode.equals(XConstants.RULE_DTL_CATEGORY)){
			// 计算凭证分类
			String catKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_V_CATEGORY) ;
			Object catJson = redisDaoImpl.hashGet(catKey, categoryId) ;
			if(catJson != null && !"".equals(catJson)){
				// 从缓存中获取凭证分类编码
				JSONObject jo = new JSONObject(String.valueOf(catJson)) ;
				val = jo.getString("code") ;
			}else{
				// 从数据库中获取凭证分类编码
				HashMap<String,String> catMap = ruleMapper.getCatInfo(categoryId) ;
				String id = catMap.get("CAT_ID") ;
				String code = catMap.get("CAT_CODE") ;
				String name = catMap.get("CAT_NAME") ;
				
				val = code ;
				
				// 将当前凭证分类信息同步到Redis缓存
				JSONObject valJo = new JSONObject() ;
				valJo.put("id", id) ;
				valJo.put("code", code) ;
				valJo.put("name", name) ;
				
				redisDaoImpl.hashPut(catKey, id, valJo.toString());
				redisDaoImpl.hashPut(catKey, code, valJo.toString());
			}
			
		}else{
			// 扩展使用
		}
		
		return val ;
	}
}
