package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;
/**
 * 
  * @ClassName ApAuditMapper
  * @Description 更行应付模块单据流程信息Mapper
  * @author 任建建
  * @date 2016年4月26日 上午11:08:08
 */
public interface ApAuditMapper {
	/**
	 * 
	  * @Title updateApInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param map
	  * @return void 返回类型
	 */
	public void updateApInsCode(HashMap<String, String> map);
	/**
	 * 
	  * @Title updateApAuditStatus 方法名
	  * @Description 更新审批流程审批状态
	  * @param map
	  * @return void 返回类型
	 */
	public void updateApAuditStatus(HashMap<String, String> map);
	/**
	 * 
	  * @Title updateApAuditDate 方法名
	  * @Description 更新流程审批通过日期
	  * @param map
	  * @return void 返回类型
	 */
	public void updateApAuditDate(HashMap<String, String> map);
	/**
	 * 
	  * @Title revokeCompleteProcess 方法名
	  * @Description 审批通过之后执行撤回，更新单据信息
	  * @param map
	  * @return void 返回类型
	 */
	public void revokeCompleteProcess(HashMap<String, String> map);
}