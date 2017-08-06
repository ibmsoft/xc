package com.xzsoft.xc.bg.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.bg.dao.BgAuditDao;
import com.xzsoft.xc.bg.service.BgAuditService;
/**
 * 
  * @ClassName: BgAuditServiceImpl
  * @Description: 预算单流程信息业务处理层实现类
  * @author 任建建
  * @date 2016年3月28日 上午9:37:51
 */
@Service("bgAuditService")
public class BgAuditServiceImpl implements BgAuditService {
	private static final Logger log = Logger.getLogger(BgAuditServiceImpl.class.getName()) ;
	@Resource
	private BgAuditDao bgAuditDao;
	/*
	 * 
	  * <p>Title: updateBgInsCode</p>
	  * <p>Description: 更新审批流程实例编码</p>
	  * @param insCode			实例编码
	  * @param bizId			业务主键ID（预算单ID）
	  * @param loginName		当前登录人
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.service.BgAuditService#updateBgInsCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateBgInsCode(String insCode, String bizId, String loginName)
			throws Exception {
		try {
			bgAuditDao.updateBgInsCode(insCode, bizId, loginName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
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
	  * @see com.xzsoft.xc.bg.service.BgAuditService#updateBgAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateBgAuditStatus(String bizId, String bizStatusCat, String bizStatusCatDesc, String loginName) throws Exception {
		try {
			bgAuditDao.updateBgAuditStatus(bizId, bizStatusCat, bizStatusCatDesc, loginName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	/*
	 * 
	  * <p>Title: updateBgAuditDate</p>
	  * <p>Description: 更新流程审批通过日期</p>
	  * @param bizId			业务主键ID（预算单ID）
	  * @param loginName		当前登录人
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.service.BgAuditService#updateBgAuditDate(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateBgAuditDate(String bizId, String loginName)
			throws Exception {
		try {
			bgAuditDao.updateBgAuditDate(bizId, loginName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	/*
	 * 
	  * <p>Title: updateBgAuditUsers</p>
	  * <p>Description: 更新当前审批人信息</p>
	  * @param bizId			业务主键ID（预算单ID）
	  * @param nodeApprovers	节点执行人
	  * @param loginName		当前登录人
	  * @throws Exception
	  * @see com.xzsoft.xc.bg.service.BgAuditService#updateBgAuditUsers(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateBgAuditUsers(String bizId, String nodeApprovers,
			String loginName) throws Exception {
		try {
			bgAuditDao.updateBgAuditUsers(bizId, nodeApprovers, loginName);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}