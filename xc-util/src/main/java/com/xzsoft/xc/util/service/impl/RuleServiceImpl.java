package com.xzsoft.xc.util.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.util.dao.RuleDAO;
import com.xzsoft.xc.util.modal.Rule;
import com.xzsoft.xc.util.modal.RuleDtl;
import com.xzsoft.xc.util.service.RuleService;

/**
 * @ClassName: RuleServiceImpl 
 * @Description: 云ERP规则处理服务接口实现类
 * @author linp
 * @date 2016年3月9日 下午10:10:21 
 *
 */
@Service("ruleService")
public class RuleServiceImpl implements RuleService {
	
	// 日志记录器
	private static final Logger log = Logger.getLogger(RuleServiceImpl.class.getName()) ;
	
	@Resource
	private RuleDAO ruleDAO ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getNextSerialNum</p> 
	 * <p>Description:  获取下一个编号 </p> 
	 * @param ruleCode
	 * @param ledgerId
	 * @param categoryId
	 * @param year
	 * @param month
	 * @param userId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.util.service.RuleService#getNextSerialNum(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String getNextSerialNum(String ruleCode,String ledgerId,String categoryId,String year,String month,String userId) throws Exception {
		String serialNum = null ;
		
		try {
			// 获取当前凭证规则信息
			Rule rule = ruleDAO.getRule(ruleCode) ;
			List<RuleDtl> ruleDtls = ruleDAO.getRuleDtls(ruleCode) ;
			rule.setRuleDtls(ruleDtls);
			
			// 获取凭证编号信息
			serialNum = ruleDAO.getRuleMaxNum(ledgerId, categoryId, year, month, rule, userId) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return serialNum ;
	}
}
