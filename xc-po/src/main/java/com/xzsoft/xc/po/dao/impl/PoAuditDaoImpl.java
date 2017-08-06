package com.xzsoft.xc.po.dao.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.po.dao.PoAuditDao;
import com.xzsoft.xc.po.mapper.PoAuditMapper;
import com.xzsoft.xip.platform.util.PlatformUtil;

/** 
 * @ClassName: PoAuditDaoImpl
 * @Description: 更新采购模块单据流程信息Dao层 实现类 
 * @author weicw
 * @date 2016年12月9日 上午11:04:49 
 * 
 */
@Repository("poAuditDao")
public class PoAuditDaoImpl implements PoAuditDao {
	@Resource
	PoAuditMapper poAuditMapper;
	/*
	 * 
	  * <p>Title updatePoInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param tableName
	  * @param insCode
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.po.dao.PoAuditDao#updatePoInsCode(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updatePoInsCode(String tableName, String insCode, String priKey, String bizId) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tableName",tableName);
			map.put("insCode",insCode);
			map.put("priKey",priKey);
			map.put("bizId",bizId);
			poAuditMapper.updatePoInsCode(map);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updatePoAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param tableName
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.po.dao.PoAuditDao#updatePoAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updatePoAuditStatus(String tableName, String bizStatusCat, String bizStatusCatDesc, String priKey, String bizId) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tableName",tableName);
			map.put("bizStatusCat",bizStatusCat);
			map.put("bizStatusCatDesc",bizStatusCatDesc);
			map.put("priKey",priKey);
			map.put("bizId",bizId);
			poAuditMapper.updatePoAuditStatus(map);
		} catch (Exception e) {
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updatePoAuditDate</p>
	  * <p>Description 更新流程审批通过日期</p>
	  * @param tableName
	  * @param priKey
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.po.dao.PoAuditDao#updatePoAuditDate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updatePoAuditDate(String tableName, String priKey, String bizId) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tableName",tableName);
			map.put("priKey",priKey);
			map.put("bizId",bizId);
			map.put("dbType",PlatformUtil.getDbType());
			poAuditMapper.updatePoAuditDate(map);
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
	  * @see com.xzsoft.xc.po.dao.PoAuditDao#revokeCompleteProcess(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void revokeCompleteProcess(String tableName, String priKey, String bizId) throws Exception {
		try {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("tableName",tableName);
			map.put("priKey",priKey);
			map.put("bizId",bizId);
			poAuditMapper.revokeCompleteProcess(map);
		} catch (Exception e) {
			throw e;
		}
	}

}
