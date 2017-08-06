package com.xzsoft.xc.ex.mapper;

import java.util.HashMap;

/**
 * @ClassName: AuditMapper 
 * @Description: 报销单据流程审批处理Mapper
 * @author linp
 * @date 2016年3月11日 上午9:42:51 
 *
 */
public interface AuditMapper {
	
	/**
	 * @Title: updateInsCode 
	 * @Description: 更新审批流程实例编码
	 * @param map
	 */
	public void updateInsCode(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updateAuditStatus 
	 * @Description: 更新审批流程审批状态 
	 * @param map
	 */
	public void updateAuditStatus(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updateAuditDate 
	 * @Description:  更新流程审批通过日期
	 * @param map
	 */
	public void updateAuditDate(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updateAuditUsers 
	 * @Description: 更新当前审批人信息 
	 * @param map
	 */
	public void updateAuditUsers(HashMap<String,Object> map) ;

}
