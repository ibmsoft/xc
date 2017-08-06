package com.xzsoft.xc.ar.dao.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ar.dao.ArAuditDao;
import com.xzsoft.xc.ar.mapper.ArAuditMapper;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName ArAuditDaoImpl
  * @Description 流程信息Dao层实现类
  * @author 任建建
  * @date 2016年7月7日 下午12:40:06
 */
@Repository("arAuditDao")
public class ArAuditDaoImpl implements ArAuditDao {
	@Resource
	private ArAuditMapper arAuditMapper;
	/*
	 * 
	  * <p>Title updateArInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param tableName
	  * @param insCode
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArAuditDao#updateArInsCode(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateArInsCode(String tableName, String insCode, String priKey, String bizId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("tableName",tableName);
		map.put("insCode",insCode);
		map.put("priKey",priKey);
		map.put("bizId",bizId);
		arAuditMapper.updateArInsCode(map);
	}
	/*
	 * 
	  * <p>Title updateArAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param tableName
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArAuditDao#updateArAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateArAuditStatus(String tableName, String bizStatusCat, String bizStatusCatDesc, String priKey, String bizId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("tableName",tableName);
		map.put("bizStatusCat",bizStatusCat);
		map.put("bizStatusCatDesc",bizStatusCatDesc);
		map.put("priKey",priKey);
		map.put("bizId",bizId);
		arAuditMapper.updateArAuditStatus(map);
	}
	/*
	 * 
	  * <p>Title updateArAuditDate</p>
	  * <p>Description 更新流程审批通过日期</p>
	  * @param tableName
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArAuditDao#updateArAuditDate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateArAuditDate(String tableName, String priKey, String bizId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("tableName",tableName);
		map.put("priKey",priKey);
		map.put("bizId",bizId);
		map.put("dbType",PlatformUtil.getDbType()) ;
		arAuditMapper.updateArAuditDate(map);
	}
	/*
	 * 
	  * <p>Title revokeCompleteProcess</p>
	  * <p>Description 流程通过，执行退回</p>
	  * @param tableName
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.dao.ArAuditDao#revokeCompleteProcess(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void revokeCompleteProcess(String tableName, String priKey, String bizId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("tableName",tableName);
		map.put("priKey",priKey);
		map.put("bizId",bizId);
		arAuditMapper.revokeCompleteProcess(map);
	}
}