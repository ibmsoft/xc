package com.xzsoft.xc.util.mapper;

import java.util.HashMap;

/**
 * @ClassName: XCWFMapper 
 * @Description: 云ERP对工作流操作Mapper
 * @author linp
 * @date 2016年6月28日 下午1:50:55 
 *
 */
public interface XCWFMapper {
	
	/**
	 * @Title: insertInstance 
	 * @Description: 复制流程实例
	 * @param instanceId
	 * @throws Exception    设定文件
	 */
	public void insertInstance(String instanceId) throws Exception ;
	
	public void insertInsProperties(String instanceId) throws Exception ;
	
	public void insertInsAttrValue(String instanceId) throws Exception ;
	
	public void insertInsActs(String instanceId) throws Exception ;
	
	public void insertInsActProps(String instanceId) throws Exception ;
	
	public void insertInsActState(String instanceId) throws Exception ;
	
	public void insertInsActPVEnum(String instanceId) throws Exception ;
	
	public void insertInsConditionGroup(String instanceId) throws Exception ;
	
	public void insertInsConditions(String instanceId) throws Exception ;
	
	public void insertInsTransitions(String instanceId) throws Exception ;
	
	public void insertInsTask(String instanceId) throws Exception ;
	
	/**
	 * @Title: deleteArchInsTask 
	 * @Description: 删除归档实例信息
	 * @param instanceid
	 * @throws Exception    设定文件
	 */
	public void deleteArchInsTask(String instanceId) throws Exception ;
	
	public void deleteArchInsConditions(String instanceId) throws Exception ;
	
	public void deleteArchInsConditionGroup(String instanceId) throws Exception ;
	
	public void deleteArchInsActState(String instanceId) throws Exception ;
	
	public void deleteArchInsActPVEnum(String instanceId) throws Exception ;
	
	public void deleteArchInsActProperties(String instanceId) throws Exception ;
	
	public void deleteArchInsActs(String instanceId) throws Exception ;
	
	public void deleteArchInsTransState(String instanceId) throws Exception ;
	
	public void deleteArchInsTransitions(String instanceId) throws Exception ;
	
	public void deleteArchInsAttrValues(String instanceId) throws Exception ;
	
	public void deleteArchInsProperties(String instanceId) throws Exception ;
	
	public void deleteArchProcessInstance(String instanceId) throws Exception ;
	
	/**
	 * @Title: deleteInsTransState 
	 * @Description: 删流程连线状态
	 * @param instanceid
	 * @throws Exception    设定文件
	 */
	public void updateInsActState2Started(HashMap<String, Object> map) throws Exception ;
	
	public void updateProcessInstanceStatus(HashMap<String, Object> map) throws Exception ;
	
	public void updateInsAttrValues(HashMap<String, Object> map) throws Exception ;
	
	public void insertCancelTask(HashMap<String, Object> map) throws Exception ;
	
	public void disabledAllTasks(HashMap<String, Object> map) throws Exception ;

}
