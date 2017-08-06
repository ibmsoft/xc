package com.xzsoft.xc.ex.dao.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ex.dao.AuditDAO;
import com.xzsoft.xc.ex.mapper.AuditMapper;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: AuditDAOImpl 
 * @Description: 报销单据流程审批处理数据层接口实现类
 * @author linp
 * @date 2016年3月11日 上午9:54:21 
 *
 */
@Repository("auditDAO")
public class AuditDAOImpl implements AuditDAO {
	@Resource
	private AuditMapper auditMapper ; 

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateInsCode</p> 
	 * <p>Description: 更新审批流程实例编码 </p> 
	 * @param insCode
	 * @param bizId
	 * @param loginName
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.AuditService#updateInsCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateInsCode(String insCode, String bizId, String loginName) throws Exception {
		
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("insCode", insCode) ;
		map.put("bizId", bizId) ;
		map.put("loginName", loginName) ;
		map.put("cdate", CurrentUserUtil.getCurrentDate()) ;
		map.put("userId", CurrentSessionVar.getUserId()) ;
		
		auditMapper.updateInsCode(map) ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditStatus</p> 
	 * <p>Description: 更新审批流程审批状态 </p> 
	 * @param bizId
	 * @param bizStatusCat
	 * @param bizStatusCatDesc
	 * @param loginName
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.AuditService#updateAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateAuditStatus(String bizId, String bizStatusCat, String bizStatusCatDesc, String loginName) throws Exception {
		
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("bizId", bizId) ;
		map.put("bizStatusCat", bizStatusCat) ;
		map.put("bizStatusCatDesc", bizStatusCatDesc) ;
		map.put("loginName", loginName) ;
		map.put("cdate", CurrentUserUtil.getCurrentDate()) ;
		map.put("userId", CurrentSessionVar.getUserId()) ;
		
		auditMapper.updateAuditStatus(map);
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditDate</p> 
	 * <p>Description: 更新流程审批通过日期 </p> 
	 * @param bizId
	 * @param loginName
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.AuditService#updateAuditDate(java.lang.String, java.lang.String)
	 */
	@Override
	public void updateAuditDate(String bizId, String loginName) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("bizId", bizId) ;
		map.put("loginName", loginName) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		map.put("cdate", CurrentUserUtil.getCurrentDate()) ;
		map.put("userId", CurrentSessionVar.getUserId()) ;
		
		auditMapper.updateAuditDate(map);
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditUsers</p> 
	 * <p>Description: 更新当前审批人信息 </p> 
	 * @param bizId
	 * @param nodeApprovers
	 * @param loginName
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.AuditService#updateAuditUsers(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateAuditUsers(String bizId, String nodeApprovers, String loginName) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("bizId", bizId) ;
		map.put("nodeApprovers", nodeApprovers) ;
		map.put("loginName", loginName) ;
		map.put("cdate", CurrentUserUtil.getCurrentDate()) ;
		map.put("userId", CurrentSessionVar.getUserId()) ;
		
		auditMapper.updateAuditUsers(map);
	}

}
