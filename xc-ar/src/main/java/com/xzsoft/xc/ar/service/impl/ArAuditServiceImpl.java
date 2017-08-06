package com.xzsoft.xc.ar.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ar.dao.ArAuditDao;
import com.xzsoft.xc.ar.dao.ArContractBaseDao;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.modal.ArContractBean;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;
import com.xzsoft.xc.ar.service.ArAuditService;
import com.xzsoft.xc.ar.util.XCARConstants;
import com.xzsoft.xc.util.service.XCWFService;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.dao.engine.ProcessInstanceDAO;
/**
 * 
  * @ClassName ArAuditServiceImpl
  * @Description 流程信息业务处理层实现类
  * @author 任建建
  * @date 2016年7月7日 下午12:42:22
 */
@Service("arAuditService")
public class ArAuditServiceImpl implements ArAuditService {
	private static final Logger log = Logger.getLogger(ArAuditServiceImpl.class.getName()) ;
	@Resource
	private ArAuditDao arAuditDao;
	@Resource
	private XCARCommonDao xcarCommonDao;
	@Resource
	private ProcessInstanceDAO processInstanceDAO;
	@Resource
	private XCWFService xcwfService;
	@Resource
	private ArContractBaseDao arContractBaseDao;
	/*
	 * 
	  * <p>Title updateArInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param arCatCode
	  * @param insCode
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArAuditService#updateArInsCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateArInsCode(String arCatCode, String insCode, String bizId) throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCARConstants.XSFP.equals(arCatCode)){	//操作的是销售发票表
				tableName = "xc_ar_invoice_h";
				priKey = "AR_INV_H_ID";
			}
			else if("XSHT".equals(arCatCode)){	//操作的是销售合同
				tableName = "xc_ar_contract";
				priKey = "AR_CONTRACT_ID";
			}
			else if("XSHTBG".equals(arCatCode)){	//操作的是销售合同变更表
				tableName = "xc_ar_contract_his";
				priKey = "AR_CONTRACT_HIS_ID";
			}
			arAuditDao.updateArInsCode(tableName, insCode, priKey, bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateArAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param arCatCode
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArAuditService#updateArAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateArAuditStatus(String arCatCode, String bizStatusCat, String bizStatusCatDesc, String bizId) throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCARConstants.XSFP.equals(arCatCode)){	//操作的是销售发票表
				tableName = "xc_ar_invoice_h";
				priKey = "AR_INV_H_ID";
			}
			else if("XSHT".equals(arCatCode)){	//操作的是销售合同
				tableName = "xc_ar_contract";
				priKey = "AR_CONTRACT_ID";
			}
			else if("XSHTBG".equals(arCatCode)){	//操作的是销售合同变更表
				tableName = "xc_ar_contract_his";
				priKey = "AR_CONTRACT_HIS_ID";
				ArContractBean arContractBean = arContractBaseDao.getArContractHisByHisId(bizId);
				ArContractBean arContractStatus = new ArContractBean();
				arContractStatus.setAR_CONTRACT_ID(arContractBean.getAR_CONTRACT_ID());
				//合同变更审批中的时候，需要把合同表的合同状态改为变更中
				if("C".equals(bizStatusCat)){
					arContractStatus.setSYS_AUDIT_STATUS("B");
					arContractStatus.setSYS_AUDIT_STATUS_DESC("变更中");
					arContractBaseDao.updateArContractStatus(arContractStatus);
				}
				//合同变更撤回、驳回的时候，需要把合同表的合同状态改为审批通过
				if("R".equals(bizStatusCat) || "D".equals(bizStatusCat)){
					arContractStatus.setSYS_AUDIT_STATUS("E");
					arContractStatus.setSYS_AUDIT_STATUS_DESC("审批通过");
					arContractBaseDao.updateArContractStatus(arContractStatus);
				}
			}
			arAuditDao.updateArAuditStatus(tableName, bizStatusCat, bizStatusCatDesc, priKey, bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateArAuditDate</p>
	  * <p>Description 更新流程审批通过日期</p>
	  * @param arCatCode
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArAuditService#updateArAuditDate(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateArAuditDate(String arCatCode, String bizId) throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCARConstants.XSFP.equals(arCatCode)){	//操作的是销售发票表
				tableName = "xc_ar_invoice_h";
				priKey = "AR_INV_H_ID";
			}
			else if("XSHT".equals(arCatCode)){	//操作的是销售合同
				tableName = "xc_ar_contract";
				priKey = "AR_CONTRACT_ID";
			}
			else if("XSHTBG".equals(arCatCode)){	//操作的是销售合同变更表
				tableName = "xc_ar_contract_his";
				priKey = "AR_CONTRACT_HIS_ID";
				//审批通过之后回写合同表
				ArContractBean arContractBean = arContractBaseDao.getArContractHisByHisId(bizId);
				arContractBaseDao.editContractInfo(arContractBean);
			}
			arAuditDao.updateArAuditDate(tableName, priKey, bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title revokeCompleteProcess</p>
	  * <p>Description 流程审批通过之后再次撤回</p>
	  * @param docId
	  * @param docCat
	  * @param cancelReason
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArAuditService#revokeCompleteProcess(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void revokeCompleteProcess(String docId, String docCat, String cancelReason,String language) throws Exception {
		try {
			String insCode  = null;
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCARConstants.XSFP.equals(docCat)){	//操作的是销售发票表
				tableName = "xc_ar_invoice_h";
				priKey = "AR_INV_H_ID";
				//通过销售发票ID得到销售发票信息
				ArInvoiceHBean arInvoiceHBean = xcarCommonDao.getArInvoiceH(docId);
				insCode = arInvoiceHBean.getINS_CODE();
				if(arInvoiceHBean.getFIN_USER_ID() != null && !"".equals(arInvoiceHBean.getFIN_USER_ID())){
					throw new Exception(XipUtil.getMessage(language, "XC_AR_PROCESS_RETURN_EXCEPTION_01", null));//销售发票已复核不能执行撤回处理！
				}
			}
			arAuditDao.revokeCompleteProcess(tableName, priKey, docId);
			
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