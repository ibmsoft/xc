package com.xzsoft.xc.ar.mapper;

import java.util.HashMap;
/**
 * 
  * @ClassName ArAuditMapper
  * @Description  更行应收模块单据流程信息Mapper
  * @author 任建建
  * @date 2016年7月7日 下午12:38:13
 */
public interface ArAuditMapper {
	/**
	 * 
	  * @Title updateArInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param map
	  * @return void 返回类型
	 */
	public void updateArInsCode(HashMap<String, String> map);
	/**
	 * 
	  * @Title updateArAuditStatus 方法名
	  * @Description 更新审批流程审批状态
	  * @param map
	  * @return void 返回类型
	 */
	public void updateArAuditStatus(HashMap<String, String> map);
	/**
	 * 
	  * @Title updateArAuditDate 方法名
	  * @Description 更新流程审批通过日期
	  * @param map
	  * @return void 返回类型
	 */
	public void updateArAuditDate(HashMap<String, String> map);
	/**
	 * 
	  * @Title revokeCompleteProcess 方法名
	  * @Description 审批通过之后执行撤回，更新单据信息
	  * @param map
	  * @return void 返回类型
	 */
	public void revokeCompleteProcess(HashMap<String, String> map);
}
