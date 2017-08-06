package com.xzsoft.xc.util.dao.impl;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.util.dao.XCWFDAO;
import com.xzsoft.xc.util.mapper.XCWFMapper;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: XCWFDAOImpl 
 * @Description: 云ERP对工作流操作数据层接口实现类
 * @author linp
 * @date 2016年6月28日 下午1:49:15 
 *
 */
@Repository("xcwfDAO")
public class XCWFDAOImpl implements XCWFDAO {
	@Resource
	private XCWFMapper xcwfMapper ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: delArchInstance</p> 
	 * <p>Description: </p> 
	 * @param instanceId
	 * @throws Exception 
	 * @see com.xzsoft.xc.util.dao.XCWFDAO#delArchInstance(java.lang.String)
	 */
	@Override
	public void delArchInstance(String instanceId) throws Exception {
		try {
			xcwfMapper.deleteArchInsTask(instanceId);
		} catch (Exception arg13) {
			throw new Exception("删除归档流程实例待办失败:" + arg13.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsConditions(instanceId);
		} catch (Exception arg12) {
			throw new Exception("删除归档流程实例条件失败:" + arg12.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsConditionGroup(instanceId);
		} catch (Exception arg11) {
			throw new Exception("删除归档流程实例条件组失败:" + arg11.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsActState(instanceId);
		} catch (Exception arg10) {
			throw new Exception("删除归档流程实例节点状态失败:" + arg10.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsActPVEnum(instanceId);
		} catch (Exception arg9) {
			throw new Exception("删除归档流程实例节点枚举失败:" + arg9.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsActProperties(instanceId);
		} catch (Exception arg8) {
			throw new Exception("删除归档流程实例节点属性失败:" + arg8.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsActs(instanceId);
		} catch (Exception arg7) {
			throw new Exception("删除归档流程实例节点失败:" + arg7.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsTransState(instanceId);
		} catch (Exception arg6) {
			throw new Exception("删除归档流程实例连线状态失败:" + arg6.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsTransitions(instanceId);
		} catch (Exception arg5) {
			throw new Exception("删除归档流程实例连线失败:" + arg5.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsAttrValues(instanceId);
		} catch (Exception arg4) {
			throw new Exception("删除归档流程实例实体属性失败:" + arg4.getMessage());
		}
		try {
			xcwfMapper.deleteArchInsProperties(instanceId);
		} catch (Exception arg3) {
			throw new Exception("删除归档流程实例属性失败:" + arg3.getMessage());
		}
		try {
			xcwfMapper.deleteArchProcessInstance(instanceId);
		} catch (Exception arg2) {
			throw new Exception("删除归档流程实例失败:" + arg2.getMessage());
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: copyArchInstance</p> 
	 * <p>Description: 复制归档实例 </p> 
	 * @param instanceId
	 * @throws Exception 
	 * @see com.xzsoft.xc.util.dao.XCWFDAO#copyArchInstance(java.lang.String)
	 */
	@Override
	public void copyArchInstance(String instanceId) throws Exception {
		try {
			xcwfMapper.insertInstance(instanceId);
		} catch (Exception arg13) {
			throw new Exception("复制归档流程实例失败:" + arg13.getMessage());
		}

		try {
			xcwfMapper.insertInsProperties(instanceId);
		} catch (Exception arg12) {
			throw new Exception("复制归档流程实例属性失败:" + arg12.getMessage());
		}

		try {
			xcwfMapper.insertInsAttrValue(instanceId);
		} catch (Exception arg11) {
			throw new Exception("复制归档流程实例实体属性失败:" + arg11.getMessage());
		}

		try {
			xcwfMapper.insertInsActs(instanceId);
		} catch (Exception arg10) {
			throw new Exception("复制归档流程实例节点失败:" + arg10.getMessage());
		}

		try {
			xcwfMapper.insertInsActProps(instanceId);
		} catch (Exception arg9) {
			throw new Exception("复制归档流程实例节点属性失败:" + arg9.getMessage());
		}

		try {
			xcwfMapper.insertInsActState(instanceId);
		} catch (Exception arg8) {
			throw new Exception("复制归档流程实例节点状态失败:" + arg8.getMessage());
		}

		try {
			xcwfMapper.insertInsActPVEnum(instanceId);
		} catch (Exception arg7) {
			throw new Exception("复制归档流程实例节点枚举失败:" + arg7.getMessage());
		}

		try {
			xcwfMapper.insertInsConditionGroup(instanceId);
		} catch (Exception arg6) {
			throw new Exception("复制归档流程实例条件组失败:" + arg6.getMessage());
		}

		try {
			xcwfMapper.insertInsConditions(instanceId);
		} catch (Exception arg5) {
			throw new Exception("复制归档流程实例条件信息失败:" + arg5.getMessage());
		}

		try {
			xcwfMapper.insertInsTransitions(instanceId);
		} catch (Exception arg4) {
			throw new Exception("复制归档流程实例联系失败:" + arg4.getMessage());
		}

		try {
			xcwfMapper.insertInsTask(instanceId);
		} catch (Exception arg2) {
			throw new Exception("复制归档流程实例待办失败:" + arg2.getMessage());
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: convertCompletedInstance2Reject</p> 
	 * <p>Description: 将审批完成的的实例转为驳回状态 </p> 
	 * @param insId
	 * @throws Exception 
	 * @see com.xzsoft.xc.util.dao.XCWFDAO#convertCompletedInstance2Reject(java.lang.String)
	 */
	@Override
	public void convertCompletedInstance2Reject(String instanceId,String cancelReason) throws Exception {
		// 参数信息
		HashMap<String, Object> map = new HashMap<String, Object>() ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		map.put("userId", CurrentSessionVar.getUserId()) ;
		map.put("cdate", CurrentUserUtil.getCurrentDate()) ;
		map.put("instanceId", instanceId) ;
		map.put("newTaskId", UUID.randomUUID().toString()) ;
		map.put("result", cancelReason) ;
		
		try {
			// 失效待办
			xcwfMapper.disabledAllTasks(map);
			// 插入驳回待办
			xcwfMapper.insertCancelTask(map);
		} catch (Exception arg2) {
			throw new Exception("失效流程实例待办失败:" + arg2.getMessage());
		}
		try {
			xcwfMapper.updateInsActState2Started(map);
		} catch (Exception arg2) {
			throw new Exception("更新流程实例节点状态失败:" + arg2.getMessage());
		}
		try {
			xcwfMapper.updateInsAttrValues(map);
		} catch (Exception arg2) {
			throw new Exception("更新流程实例属性失败:" + arg2.getMessage());
		}
		try {
			xcwfMapper.updateProcessInstanceStatus(map);
		} catch (Exception arg2) {
			throw new Exception("更新流程实例状态失败:" + arg2.getMessage());
		}

	}
}
