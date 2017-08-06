package com.xzsoft.xc.util.dao;

import java.util.List;

import com.xzsoft.xc.util.modal.Rule;
import com.xzsoft.xc.util.modal.RuleDtl;

/**
 * @ClassName: RuleDAO 
 * @Description: 云ERP规则处理数据层接口
 * @author linp
 * @date 2016年3月9日 下午10:10:37 
 *
 */
public interface RuleDAO {
	
	/**
	 * @Title: getRule 
	 * @Description: 获取规则信息
	 * @param ruleCode
	 * @return
	 * @throws Exception
	 */
	public Rule getRule(String ruleCode) throws Exception ;

	/**
	 * @Title: getRuleDtl 
	 * @Description: 获取规则明细信息
	 * @param ruleCode
	 * @return
	 * @throws Exception
	 */
	public List<RuleDtl> getRuleDtls(String ruleCode) throws Exception ;

	/**
	 * @Title: getRuleMaxNum 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param ledgerId
	 * @param categoryId
	 * @param year
	 * @param month
	 * @param rule
	 * @param userId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getRuleMaxNum(String ledgerId,String categoryId,String year,String month,Rule rule,String userId) throws Exception ;
}
