package com.xzsoft.xc.util.service;

/**
 * @ClassName: RuleService 
 * @Description: 云ERP规则处理服务接口
 * @author linp
 * @date 2016年3月9日 下午10:09:47 
 *
 */
public interface RuleService {

	/**
	 * @Title: getNextSerialNum 
	 * @Description: 获取下一个编号
	 * @param ruleCode
	 * @param ledgerId
	 * @param categoryId
	 * @param year
	 * @param month
	 * @param userId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getNextSerialNum(String ruleCode,String ledgerId,String categoryId,String year,String month,String userId)throws Exception ;
	
}
