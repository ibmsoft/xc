package com.xzsoft.xc.st.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.st.dao.XcStAuditDao;
import com.xzsoft.xc.st.dao.XcStUseApplyDao;
import com.xzsoft.xc.st.modal.Dto;
import com.xzsoft.xc.st.service.XcStAuditService;
import com.xzsoft.xc.util.service.XCWFService;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.dao.engine.ProcessInstanceDAO;
/**
 * 
  * @ClassName XcStAuditServiceImpl
  * @Description 流程信息业务处理层实现类
  * @author RenJianJian
  * @date 2016年12月8日 下午3:24:02
 */
@Service("xcStAuditService")
public class XcStAuditServiceImpl implements XcStAuditService {
	private static final Logger log = Logger.getLogger(XcStAuditServiceImpl.class.getName()) ;
	@Resource
	private XcStAuditDao xcStAuditDao;
	@Resource
	private ProcessInstanceDAO processInstanceDAO;
	@Resource
	private XCWFService xcwfService;
	@Resource
	private XcStUseApplyDao xcStUseApplyDao;
	/*
	 * 
	  * <p>Title updateXcStInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param arCatCode
	  * @param insCode
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStAuditService#updateXcStInsCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateXcStInsCode(String arCatCode, String insCode, String bizId) throws Exception {
		try {
			xcStAuditDao.updateXcStInsCode(insCode,bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateXcStAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param arCatCode
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStAuditService#updateXcStAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateXcStAuditStatus(String arCatCode, String bizStatusCat, String bizStatusCatDesc, String bizId) throws Exception {
		try {
			xcStAuditDao.updateXcStAuditStatus(bizStatusCat, bizStatusCatDesc, bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateXcStAuditDate</p>
	  * <p>Description 更新流程审批通过日期</p>
	  * @param arCatCode
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStAuditService#updateXcStAuditDate(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateXcStAuditDate(String arCatCode, String bizId) throws Exception {
		try {
			xcStAuditDao.updateXcStAuditDate(bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title revokeXcStCompleteProcess</p>
	  * <p>Description 流程审批通过之后再次撤回</p>
	  * @param docId
	  * @param docCat
	  * @param cancelReason
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.st.service.XcStAuditService#revokeXcStCompleteProcess(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void revokeXcStCompleteProcess(String docId, String docCat, String cancelReason,String language) throws Exception {
		try {
			Dto xcStUseApplyHBean = xcStUseApplyDao.selectXcStUseApplyH(docId);
			xcStAuditDao.revokeXcStCompleteProcess(docId);
			String insCode  = xcStUseApplyHBean.getAsString("INS_CODE");
			//重置流程实例(将归档流程实例重置运行时)
			if(insCode != null && !"".equals(insCode)){
				String insId = processInstanceDAO.getArchInsIdByCode(insCode);
				if(insId != null && !"".equals(insId)){
					xcwfService.copyArchInstance(insId);
					xcwfService.convertCompletedInstance2Reject(insId,cancelReason);
					xcwfService.delArchInstance(insId);
					
				}else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PROCESS_RETURN_EXCEPTION_02", null)+"【"+insCode+"】");//流程实例不存在, 实例编码为
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}