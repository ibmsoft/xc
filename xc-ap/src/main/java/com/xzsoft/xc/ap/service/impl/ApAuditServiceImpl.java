package com.xzsoft.xc.ap.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ap.dao.ApAuditDao;
import com.xzsoft.xc.ap.dao.ApContractDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.modal.ApContractBean;
import com.xzsoft.xc.ap.modal.ApContractHisBean;
import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.service.ApAuditService;
import com.xzsoft.xc.ap.service.ApInvGlBaseService;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.util.service.XCWFService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.dao.engine.ProcessInstanceDAO;
/**
 * 
  * @ClassName ApAuditServiceImpl
  * @Description 流程信息业务处理层实现类
  * @author 任建建
  * @date 2016年4月26日 上午11:15:05
 */
@Service("apAuditService")
public class ApAuditServiceImpl implements ApAuditService {
	private static final Logger log = Logger.getLogger(ApAuditServiceImpl.class.getName());
	@Resource
	private ApAuditDao apAuditDao;
	@Resource
	private XCAPCommonDao xcapCommonDao;
	@Resource
	private ProcessInstanceDAO processInstanceDAO;
	@Resource
	private XCWFService xcwfService;
	@Resource
	private ApInvGlBaseService apInvGlBaseService;
	@Resource
	private ApContractDao apContractDao;
	/*
	 * 
	  * <p>Title updateApInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param apCatCode
	  * @param insCode
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApAuditService#updateApInsCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateApInsCode(String apCatCode, String insCode, String bizId) throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCAPConstants.CGFP.equals(apCatCode)){	//操作的是采购发票表
				tableName = "xc_ap_invoice_h";
				priKey = "AP_INV_H_ID";
			}
			else if(XCAPConstants.FKSQD.equals(apCatCode)){	//操作的是付款申请单
				tableName = "xc_ap_pay_req_h";
				priKey = "AP_PAY_REQ_H_ID";
			}
			else if("CGHT".equals(apCatCode)){	//操作的是采购合同
				tableName = "xc_ap_contract";
				priKey = "AP_CONTRACT_ID";
			}
			else if("CGHTBG".equals(apCatCode)){//操作的是采购合同变更
				tableName = "xc_ap_contract_his";
				priKey = "AP_CONTRACT_HIS_ID";
			}
			apAuditDao.updateApInsCode(tableName, insCode, priKey, bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateApAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param apCatCode
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApAuditService#updateApAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateApAuditStatus(String apCatCode, String bizStatusCat,String bizStatusCatDesc, String bizId) throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCAPConstants.CGFP.equals(apCatCode)){	//操作的是采购发票表
				tableName = "xc_ap_invoice_h";
				priKey = "AP_INV_H_ID";
			}
			else if(XCAPConstants.FKSQD.equals(apCatCode)){	//操作的是付款申请单
				tableName = "xc_ap_pay_req_h";
				priKey = "AP_PAY_REQ_H_ID";
			}
			else if("CGHT".equals(apCatCode)){	//操作的是采购合同
				tableName = "xc_ap_contract";
				priKey = "AP_CONTRACT_ID";
			}
			else if("CGHTBG".equals(apCatCode)){//操作的是采购合同变更
				tableName = "xc_ap_contract_his";
				priKey = "AP_CONTRACT_HIS_ID";
				//合同变更审批中的时候，需要把合同表的合同状态改为变更中
				if("C".equals(bizStatusCat)){
					//审批通过之后回写合同表
					ApContractHisBean bean = apContractDao.getContractHisInfo(bizId);
					ApContractBean  vbean = new ApContractBean();
					vbean.setSYS_AUDIT_STATUS("B");
					vbean.setSYS_AUDIT_STATUS_DESC("变更中");
					vbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					vbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					vbean.setAP_CONTRACT_ID(bean.getAP_CONTRACT_ID());
					apContractDao.updateSysAuditStatus(vbean);//修改合同的状态
				}
			}
			apAuditDao.updateApAuditStatus(tableName, bizStatusCat, bizStatusCatDesc, priKey, bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updateApAuditDate</p>
	  * <p>Description 更新流程审批通过日期</p>
	  * @param apCatCode
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApAuditService#updateApAuditDate(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updateApAuditDate(String apCatCode, String bizId) throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCAPConstants.CGFP.equals(apCatCode)){	//操作的是采购发票表
				tableName = "xc_ap_invoice_h";
				priKey = "AP_INV_H_ID";
			}
			else if(XCAPConstants.FKSQD.equals(apCatCode)){	//操作的是付款申请单
				tableName = "xc_ap_pay_req_h";
				priKey = "AP_PAY_REQ_H_ID";
			}
			else if("CGHT".equals(apCatCode)){	//操作的是采购合同
				tableName = "xc_ap_contract";
				priKey = "AP_CONTRACT_ID";
			}
			else if("CGHTBG".equals(apCatCode)){//操作的是采购合同变更
				tableName = "xc_ap_contract_his";
				priKey = "AP_CONTRACT_HIS_ID";
				apContractDao.changeApContract(bizId);
			}
			apAuditDao.updateApAuditDate(tableName, priKey, bizId);
			if(XCAPConstants.CGFP.equals(apCatCode))//采购发票
				//记录预算占用数
				apInvGlBaseService.recordBudgetOccupancyAmount(bizId,apCatCode);
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
	  * @see com.xzsoft.xc.ap.service.ApAuditService#revokeCompleteProcess(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void revokeCompleteProcess(String docId,String docCat,String cancelReason,String language) throws Exception {
		try {
			String insCode  = null;
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCAPConstants.CGFP.equals(docCat)){	//操作的是采购发票表
				//通过采购发票ID得到采购发票信息
				ApDocumentHBean apInvoiceHBean = xcapCommonDao.getApInvoiceH(docId);
				insCode = apInvoiceHBean.getINS_CODE();
				tableName = "xc_ap_invoice_h";
				priKey = "AP_INV_H_ID";
				if(apInvoiceHBean.getFIN_USER_ID() != null && !"".equals(apInvoiceHBean.getFIN_USER_ID())){
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PROCESS_RETURN_EXCEPTION_01", null));//采购发票已复核不能执行撤回处理！
				}
			}
			else if(XCAPConstants.FKSQD.equals(docCat)){	//操作的是付款申请单
				//通过付款申请ID得到付款申请信息
				ApPayReqHBean apPayReqHBean = xcapCommonDao.getApPayReqH(docId);
				insCode = apPayReqHBean.getINS_CODE();
				tableName = "xc_ap_pay_req_h";
				priKey = "AP_PAY_REQ_H_ID";
			}
			apAuditDao.revokeCompleteProcess(tableName, priKey, docId);
			
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
			if(XCAPConstants.CGFP.equals(docCat))//采购发票
				//删除预算占用数
				apInvGlBaseService.deleteBudgetOccupancyAmount(docId,docCat);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}