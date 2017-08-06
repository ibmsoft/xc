package com.xzsoft.xc.po.mapper;

import java.util.HashMap;

/** 
 * @ClassName: PoAuditMapper
 * @Description: 更新采购模块单据流程信息Mapper
 * @author weicw
 * @date 2016年12月9日 上午10:56:01 
 * 
 */
public interface PoAuditMapper {
	/**
	 * 
	  * @Title updatePoInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param map
	  * @return void 返回类型
	 */
	public void updatePoInsCode(HashMap<String, String> map);
	/**
	 * 
	  * @Title updatePoAuditStatus 方法名
	  * @Description 更新审批流程审批状态
	  * @param map
	  * @return void 返回类型
	 */
	public void updatePoAuditStatus(HashMap<String, String> map);
	/**
	 * 
	  * @Title updatePoAuditDate 方法名
	  * @Description 更新流程审批通过日期
	  * @param map
	  * @return void 返回类型
	 */
	public void updatePoAuditDate(HashMap<String, String> map);
	/**
	 * 
	  * @Title revokeCompleteProcess 方法名
	  * @Description 审批通过之后执行撤回，更新单据信息
	  * @param map
	  * @return void 返回类型
	 */
	public void revokeCompleteProcess(HashMap<String, String> map);
}
