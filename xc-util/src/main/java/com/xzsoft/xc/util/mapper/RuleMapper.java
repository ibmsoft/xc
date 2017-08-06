package com.xzsoft.xc.util.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Rule;
import com.xzsoft.xc.util.modal.RuleDtl;
import com.xzsoft.xc.util.modal.RuleMaxNum;

/**
 * @ClassName: RuleMapper 
 * @Description: 凭证规则处理Mapper
 * @author linp
 * @date 2016年1月1日 下午11:10:42 
 *
 */
public interface RuleMapper {
	
	/**
	 * @Title: getRule 
	 * @Description: 获取规则基础信息
	 * @param ruleCode
	 * @return
	 */
	public Rule getRule(String ruleCode) ;
	
	/**
	 * @Title: getRuleDtl 
	 * @Description: 获取规则明细信息
	 * @param ruleCode
	 * @return
	 */
	public List<RuleDtl> getRuleDtl(String ruleCode) ;
	
	/**
	 * @Title: getRuleCode 
	 * @Description: 获取当前规则的最大流水号
	 * @param ledgerId
	 * @return
	 */
	public RuleMaxNum getRuleMaxNum(HashMap<String,String> map) ;
	
	/**
	 * @Title: updateRuleMaxNum 
	 * @Description: 更新最大流水号
	 * @param rmn
	 */
	public void updateRuleMaxNum(RuleMaxNum rmn) ;
	
	/**
	 * @Title: insertRuleMaxNum 
	 * @Description: 插入最大流水号
	 * @param rmn
	 */
	public void insertRuleMaxNum(RuleMaxNum rmn) ;
	
	/**
	 * @Title: getLegerById 
	 * @Description: 查询账簿信息
	 * @param ledgerId
	 * @return    设定文件
	 */
	public Ledger getLegerById(String ledgerId) ;
	
	/**
	 * @Title: getCatInfo 
	 * @Description: 根据凭证分录ID查询凭证分类信息
	 * @param catId
	 * @return    设定文件
	 */
	public HashMap<String,String> getCatInfo(String catId) ;
	
}
