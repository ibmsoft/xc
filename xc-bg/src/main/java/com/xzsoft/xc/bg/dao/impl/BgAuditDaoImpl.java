package com.xzsoft.xc.bg.dao.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.bg.dao.BgAuditDao;
import com.xzsoft.xc.bg.mapper.BgAuditMapper;
import com.xzsoft.xip.platform.util.PlatformUtil;
/**
 * 
  * @ClassName: BgAuditDaoImpl
  * @Description: 预算单流程信息Dao层实现类
  * @author 任建建
  * @date 2016年3月28日 上午9:24:57
 */
@Repository("bgAuditDao")
public class BgAuditDaoImpl implements BgAuditDao {
	@Resource
	private BgAuditMapper bgAuditMapper;
	/*
	 * 
	  * <p>Title: updateBgInsCode</p>
	  * <p>Description: 更新审批流程实例编码</p>
	  * @param insCode			实例编码
	  * @param bizId			业务主键ID（预算单ID）
	  * @param loginName		当前登录人
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.dao.BgAuditDao#updateBgInsCode(java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public void updateBgInsCode(String insCode,String bizId,String loginName)
			throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("insCode",insCode);
		map.put("bizId",bizId);
		map.put("loginName",loginName);
		bgAuditMapper.updateBgInsCode(map);
	}
	/*
	 * 
	  * <p>Title: updateBgAuditStatus</p>
	  * <p>Description: 更新审批流程审批状态</p>
	  * @param bizStatusCat		流程状态
	  * @param bizStatusCatDesc	流程状态描述
	  * @param bizId			业务主键ID（预算单ID）
	  * @param loginName		当前登录人
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.dao.BgAuditDao#updateBgAuditStatus(java.lang.String,java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public void updateBgAuditStatus(String bizId,String bizStatusCat,String bizStatusCatDesc,
			String loginName) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("bizId",bizId);
		map.put("bizStatusCat",bizStatusCat);
		map.put("bizStatusCatDesc",bizStatusCatDesc);
		map.put("loginName",loginName);
		bgAuditMapper.updateBgAuditStatus(map);
	}
	/*
	 * 
	  * <p>Title: updateBgAuditDate</p>
	  * <p>Description: 更新流程审批通过日期</p>
	  * @param bizId			业务主键ID（预算单ID）
	  * @param loginName		当前登录人
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.dao.BgAuditDao#updateBgAuditDate(java.lang.String,java.lang.String)
	 */
	@Override
	public void updateBgAuditDate(String bizId,String loginName)
			throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("bizId",bizId);
		map.put("loginName",loginName);
		map.put("dbType", PlatformUtil.getDbType());
		bgAuditMapper.updateBgAuditDate(map);
	}
	/*
	 * 
	  * <p>Title: updateBgAuditUsers</p>
	  * <p>Description: 更新当前审批人信息</p>
	  * @param bizId			业务主键ID（预算单ID）
	  * @param nodeApprovers	节点执行人
	  * @param loginName		当前登录人
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.dao.BgAuditDao#updateBgAuditUsers(java.lang.String,java.lang.String,java.lang.String)
	 */
	@Override
	public void updateBgAuditUsers(String bizId,String nodeApprovers,
			String loginName) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("bizId",bizId);
		map.put("nodeApprovers",nodeApprovers);
		map.put("loginName",loginName);
		bgAuditMapper.updateBgAuditUsers(map);
	}

}
