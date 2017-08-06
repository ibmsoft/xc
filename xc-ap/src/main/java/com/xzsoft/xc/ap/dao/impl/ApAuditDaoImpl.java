package com.xzsoft.xc.ap.dao.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.ap.dao.ApAuditDao;
import com.xzsoft.xc.ap.mapper.ApAuditMapper;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName ApAuditDaoImpl
  * @Description 流程信息Dao层实现类
  * @author 任建建
  * @date 2016年4月26日 上午11:11:06
 */
@Repository("apAuditDao")
public class ApAuditDaoImpl implements ApAuditDao {
	@Resource
	private ApAuditMapper apAuditMapper;
	/*
	 * 
	  * <p>Title updateApInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param tableName
	  * @param insCode
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApAuditDao#updateApInsCode(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateApInsCode(String tableName, String insCode, String priKey, String bizId) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tableName",tableName);
			map.put("insCode",insCode);
			map.put("priKey",priKey);
			map.put("bizId",bizId);
			apAuditMapper.updateApInsCode(map);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateApAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param tableName
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApAuditDao#updateApAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateApAuditStatus(String tableName, String bizStatusCat, String bizStatusCatDesc, String priKey, String bizId) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tableName",tableName);
			map.put("bizStatusCat",bizStatusCat);
			map.put("bizStatusCatDesc",bizStatusCatDesc);
			map.put("priKey",priKey);
			map.put("bizId",bizId);
			apAuditMapper.updateApAuditStatus(map);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateApAuditDate</p>
	  * <p>Description 更新流程审批通过日期</p>
	  * @param tableName
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApAuditDao#updateApAuditDate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateApAuditDate(String tableName, String priKey, String bizId) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tableName",tableName);
			map.put("priKey",priKey);
			map.put("bizId",bizId);
			map.put("dbType",PlatformUtil.getDbType());
			apAuditMapper.updateApAuditDate(map);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title revokeCompleteProcess</p>
	  * <p>Description 审批通过之后执行撤回，更新单据信息</p>
	  * @param tableName
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.dao.ApAuditDao#revokeCompleteProcess(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void revokeCompleteProcess(String tableName, String priKey, String bizId) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tableName",tableName);
			map.put("priKey",priKey);
			map.put("bizId",bizId);
			apAuditMapper.revokeCompleteProcess(map);
		} catch (Exception e) {
			throw e;
		}
	}
}