package com.xzsoft.xc.ex.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ex.dao.AuditDAO;
import com.xzsoft.xc.ex.service.AuditService;
import com.xzsoft.xc.ex.service.EXDocBaseService;
import com.xzsoft.xip.wf.common.util.WorkFlowConstants;

/**
 * @ClassName: AuditServiceImpl 
 * @Description: 报销单据流程审批处理接口实现类
 * @author linp
 * @date 2016年3月11日 上午9:35:12 
 *
 */
@Service("auditService")
public class AuditServiceImpl implements AuditService {
	@Resource
	private AuditDAO auditDAO ; 
	@Resource
	private EXDocBaseService exDocBaseService ;

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
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateInsCode(String insCode, String bizId, String loginName) throws Exception {
		try {
			// 回写业务表的流程实例编码
			auditDAO.updateInsCode(insCode, bizId, loginName) ;
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditStatus</p> 
	 * <p>Description: 更新审批流程审批状态 </p> 
	 * @param bizId
	 * @param bizStatusCat
	 * @param bizStatusCatDesc
	 * @param catCode
	 * @param applyDocId
	 * @param loginName
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.AuditService#updateAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateAuditStatus(String bizId, String bizStatusCat, String bizStatusCatDesc, String catCode, String applyDocId, String loginName) throws Exception {
		try {
			// 更新审批流程审批状态
			auditDAO.updateAuditStatus(bizId, bizStatusCat, bizStatusCatDesc, loginName) ;
			
			// 如果流程被驳回或撤回时，则需要删除预算占用数
			switch(bizStatusCat){
			case WorkFlowConstants.BIZ_STATUS_R :	// 撤回
				exDocBaseService.deleteBudgetOccupancyAmount(bizId, catCode, applyDocId);
				break ;
			case WorkFlowConstants.BIZ_STATUS_D :	// 驳回
				exDocBaseService.deleteBudgetOccupancyAmount(bizId, catCode, applyDocId);
				break ;
			default :
				break ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
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
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateAuditDate(String bizId, String loginName) throws Exception {
		try {
			// 更新流程审批通过日期
			auditDAO.updateAuditDate(bizId, loginName) ;
			
		} catch (Exception e) {
			throw e ;
		}
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
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateAuditUsers(String bizId, String nodeApprovers, String loginName) throws Exception {
		try {
			// 更新当前审批人信息
			auditDAO.updateAuditUsers(bizId, nodeApprovers, loginName) ;
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: recordBudgetFact</p> 
	 * <p>Description: 记录预算占用数 </p> 
	 * @param bizId
	 * @param loginName
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.AuditService#recordBudgetFact(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void recordBudgetFact(String bizId, String loginName)throws Exception {
		try {
			// 记录预算占用数
			exDocBaseService.recordBudgetOccupancyAmount(bizId);
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: updateAuditDateAndBudgetFact</p> 
	 * <p>Description: </p> 
	 * @param bizId
	 * @param loginName
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.AuditService#updateAuditDateAndBudgetFact(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateAuditDateAndBudgetFact(String bizId, String loginName)throws Exception {
		try {
			// 更新流程审批通过日期
			auditDAO.updateAuditDate(bizId, loginName) ;
			
			// 记录预算占用数
			exDocBaseService.recordBudgetOccupancyAmount(bizId);
			
		} catch (Exception e) {
			throw e ;
		}
	}
}
