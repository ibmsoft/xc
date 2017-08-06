package com.xzsoft.xc.po.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.po.dao.PoAuditDao;
import com.xzsoft.xc.po.dao.PoOrderHDao;
import com.xzsoft.xc.po.dao.PoPurApHDao;
import com.xzsoft.xc.po.modal.PoOrderHBean;
import com.xzsoft.xc.po.modal.PoOrderHHisBean;
import com.xzsoft.xc.po.modal.PoOrderLBean;
import com.xzsoft.xc.po.modal.PoOrderLHisBean;
import com.xzsoft.xc.po.modal.PoPurApHBean;
import com.xzsoft.xc.po.modal.PoPurApLBean;
import com.xzsoft.xc.po.service.PoAuditService;
import com.xzsoft.xc.po.util.XCPOConstants;
import com.xzsoft.xc.util.service.XCWFService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.dao.engine.ProcessInstanceDAO;

/** 
 * @ClassName: PoAuditServiceImpl
 * @Description: 流程信息业务处理层实现类
 * @author weicw
 * @date 2016年12月9日 上午10:41:52 
 * 
 */
@Service("poAuditService")
public class PoAuditServiceImpl implements PoAuditService{
	private static final Logger log = Logger.getLogger(PoAuditServiceImpl.class.getName());
	@Resource
	private PoAuditDao poAuditDao;
	@Resource
	private ProcessInstanceDAO processInstanceDAO;
	@Resource
	private XCWFService xcwfService;
	@Resource
	private PoPurApHDao poPurApHDao;
	@Resource
	private PoOrderHDao poOrderHDao;
	/*
	 * 
	  * <p>Title updatePoInsCode</p>
	  * <p>Description 更新审批流程实例编码</p>
	  * @param poCatCode
	  * @param insCode
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.po.service.PoAuditService#updatePoInsCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updatePoInsCode(String poCatCode,String insCode,String bizId) throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCPOConstants.CGSQ.equals(poCatCode)){	//操作的是采购申请
				tableName = "xc_po_pur_ap_h";
				priKey = "PUR_AP_H_ID";
			}
			else if(XCPOConstants.CGDD.equals(poCatCode)){	//操作的是采购订单
				tableName = "xc_po_order_h";
				priKey = "ORDER_H_ID";
			}
			else if("CGDD_BG".equals(poCatCode)){	//操作的是采购订单历史表
				tableName = "xc_po_order_h_his";
				priKey = "ORDER_H_HIS_ID";
			}
			else if("DHD".equals(poCatCode)){	//操作的是到货单
				tableName = "xc_po_arrival_h";
				priKey = "ARRIVAL_H_ID";
			}
			else if("THD".equals(poCatCode)){//操作的是退货单
				tableName = "xc_po_return_h";
				priKey = "RETURN_H_ID";
			}
			poAuditDao.updatePoInsCode(tableName, insCode, priKey, bizId);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title updatePoAuditStatus</p>
	  * <p>Description 更新审批流程审批状态</p>
	  * @param poCatCode
	  * @param bizStatusCat
	  * @param bizStatusCatDesc
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.po.service.PoAuditService#updatePoAuditStatus(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updatePoAuditStatus(String poCatCode, String bizStatusCat,
			String bizStatusCatDesc, String bizId) throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCPOConstants.CGSQ.equals(poCatCode)){	//操作的是采购申请
				tableName = "xc_po_pur_ap_h";
				priKey = "PUR_AP_H_ID";
			}
			else if(XCPOConstants.CGDD.equals(poCatCode)){	//操作的是采购订单
				tableName = "xc_po_order_h";
				priKey = "ORDER_H_ID";
			}
			else if("CGDD_BG".equals(poCatCode)){	//操作的是采购订单历史表
				tableName = "xc_po_order_h_his";
				priKey = "ORDER_H_HIS_ID";
			}
			else if("DHD".equals(poCatCode)){	//操作的是到货单
				tableName = "xc_po_arrival_h";
				priKey = "ARRIVAL_H_ID";
			}
			else if("THD".equals(poCatCode)){//操作的是退货单
				tableName = "xc_po_return_h";
				priKey = "RETURN_H_ID";
			}
			poAuditDao.updatePoAuditStatus(tableName, bizStatusCat, bizStatusCatDesc, priKey, bizId);
			
			// 当采购订单审批通过之后，往历史表中添加数据，版本0
			if (XCPOConstants.CGDD.equals(poCatCode)) {
				if ("E".equalsIgnoreCase(bizStatusCat)) {
					//1、查询订单数据（主数据、行数据）
					PoOrderHBean poOrderHBean = poOrderHDao.getOrderHInfoByHId(bizId);
					List<PoOrderLBean> poOrderLBeans = poOrderHDao.getOrderLInfoByHId2(bizId);
					//2、复制数据
					PoOrderHHisBean poOrderHHisBean = new PoOrderHHisBean();
					List<PoOrderLHisBean> poOrderLHisBeans = new ArrayList<>();
					copyOrderH2OrderHistoryH(poOrderHBean, poOrderHHisBean);
					copyOrderL2OrderHistoryL(poOrderLBeans, poOrderLHisBeans, poOrderHHisBean);
					//3、将数据新增到历史表中
					poOrderHDao.addOrderHHisInfo(poOrderHHisBean);
					for (PoOrderLHisBean poOrderLHisBean : poOrderLHisBeans) {
						poOrderHDao.addOrderLHisInfo(poOrderLHisBean);
					}
				}
			}
			
			
			if ("CGDD_BG".equals(poCatCode)) {
				//采购订单变更审批中时，采购订单业务状态为变更中
				if("C".equalsIgnoreCase(bizStatusCat)){
					PoOrderHHisBean hisBean = poOrderHDao.ifExistHisH(bizId);
					PoOrderHBean bean = new PoOrderHBean();
					bean.setSYS_AUDIT_STATUS("B");
					bean.setSYS_AUDIT_STATUS_DESC("变更中");
					bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					bean.setORDER_H_ID(hisBean.getORDER_H_ID());
					poOrderHDao.updateSysStatus(bean);
				}
				/* 采购订单（变更） 审批通过时，
				 * 修改采购订单信息与采购订单（变更）一致 ,
				 * 若有删除行信息，还需要修改相关采购申请的行信息
				 * */ 
				if ("E".equalsIgnoreCase(bizStatusCat)) {
					// 1、查找采购定单（变更）数据（主数据，行数据），采购订单数据（主数据，行数据）
					PoOrderHHisBean poOrderHHisBean = poOrderHDao.getOrderHHisById(bizId); // 采购订单（变更）主数据
					List<PoOrderLHisBean> poOrderLHisBeans = poOrderHDao.getOrderLHisByHId(bizId);//  采购订单（变更）行数据
					String orderHId = poOrderHHisBean.getORDER_H_ID();
					PoOrderHBean poOrderHBean = poOrderHDao.getOrderHInfoByHId(orderHId);// 采购订单主数据
					List<PoOrderLBean> poOrderLBeans = poOrderHDao.getOrderLInfoByHId2(orderHId);//采购订单行数据
					PoPurApLBean purbean = new PoPurApLBean(); // 采购申请对象
					// 2、删除全部采购订单行数据,修改相关采购申请的信息
					List<String> poOrderLIds = new ArrayList<String>();// 采购订单行id集合
					for (PoOrderLBean poOrderLBean : poOrderLBeans) {
						poOrderLIds.add(poOrderLBean.getORDER_L_ID());
						String purApLId = poOrderLBean.getAP_PUR_TYPE_ID();
						if (!(purApLId == null || "".equals(purApLId))) {
							purbean.setPUR_AP_L_ID(purApLId);
							purbean.setPO_ORDER_H_ID("");
							purbean.setPO_ORDER_L_ID("");
							poOrderHDao.editPurInfo(purbean);//修改相关采购申请信息
						}
					}
					//删除采购申请所有行数据
					poOrderHDao.deleteOrderLInfo(poOrderLIds);

					// 3、复制数据 从采购订单（变更）到采购订单
					copyOrderHHis2OrderH(poOrderHHisBean, poOrderHBean);
					poOrderLBeans.clear();
					copyOrderLHis2OrderL(poOrderLHisBeans, poOrderLBeans, poOrderHBean);
					// 4、修改采购订单主数据，新增采购订单行数据，修改相关采购申请信息
					poOrderHDao.editOrderHInfo(poOrderHBean);
					for (PoOrderLBean poOrderLBean : poOrderLBeans) {
						poOrderHDao.addOrderLInfo(poOrderLBean);
						String purApLId = poOrderLBean.getAP_PUR_TYPE_ID();
						if (!(purApLId == null || "".equals(purApLId))) {
							purbean.setPUR_AP_L_ID(purApLId);
							purbean.setPO_ORDER_H_ID(poOrderLBean.getORDER_H_ID());
							purbean.setPO_ORDER_L_ID(poOrderLBean.getORDER_L_ID());
							poOrderHDao.editPurInfo(purbean);//修改相关采购申请信息
						}
					}

					
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		
	}
	/*
	 * 
	  * <p>Title updatePoAuditDate</p>
	  * <p>Description 更新流程审批通过日期</p>
	  * @param poCatCode
	  * @param bizId
	  * @throws Exception
	  * @see com.xzsoft.xc.po.service.PoAuditService#updatePoAuditDate(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updatePoAuditDate(String poCatCode, String bizId)
			throws Exception {
		try {
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCPOConstants.CGSQ.equals(poCatCode)){	//操作的是采购申请
				tableName = "xc_po_pur_ap_h";
				priKey = "PUR_AP_H_ID";
			}
			else if(XCPOConstants.CGDD.equals(poCatCode)){	//操作的是采购订单
				tableName = "xc_po_order_h";
				priKey = "ORDER_H_ID";
			}
			else if("CGDD_BG".equals(poCatCode)){	//操作的是采购订单历史表
				tableName = "xc_po_order_h_his";
				priKey = "ORDER_H_HIS_ID";
				poOrderHDao.changePoOrder(bizId);//回写采购订单
			}
			else if("DHD".equals(poCatCode)){	//操作的是到货单
				tableName = "xc_po_arrival_h";
				priKey = "ARRIVAL_H_ID";
			}
			else if("THD".equals(poCatCode)){//操作的是退货单
				tableName = "xc_po_return_h";
				priKey = "RETURN_H_ID";
			}
			poAuditDao.updatePoAuditDate(tableName, priKey, bizId);
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
	public void revokeCompleteProcess(String docId, String docCat,
			String cancelReason, String language) throws Exception {
		try {
			String insCode  = null;
			String tableName = null;	//表名
			String priKey = null;		//主键
			if(XCPOConstants.CGSQ.equals(docCat)){	//操作的是采购申请
				//通过采购申请ID得到采购申请信息
				PoPurApHBean poPurApHBean = poPurApHDao.ifExistH(docId);
				insCode = poPurApHBean.getINS_CODE();
				tableName = "xc_po_pur_ap_h";
				priKey = "PUR_AP_H_ID";
			}
			else if(XCPOConstants.CGDD.equals(docCat)){	//操作的是采购订单
				//通过采购订单ID得到采购订单信息
				PoOrderHBean poOrderHBean = poOrderHDao.ifExistH(docId);
				insCode = poOrderHBean.getINS_CODE();
				tableName = "xc_po_order_h";
				priKey = "ORDER_H_ID";
			}else if("CGDD_BG".equals(docCat)){//操作的是采购订单变更
				//通过采购订单历史ID得到采购订单历史信息
				PoOrderHHisBean  poOrderHHisBean = poOrderHDao.ifExistHisH(docId);
				insCode = poOrderHHisBean.getINS_CODE();
				tableName = "xc_po_order_his_h";
				priKey = "ORDER_H_HIS_ID";
			}
			poAuditDao.revokeCompleteProcess(tableName, priKey, docId);
			
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
	
	public void copyOrderH2OrderHistoryH(PoOrderHBean poOrderHBean,PoOrderHHisBean poOrderHHisBean) throws Exception{
		poOrderHHisBean.setORDER_H_HIS_ID(java.util.UUID.randomUUID().toString());
		poOrderHHisBean.setORDER_H_ID(poOrderHBean.getORDER_H_ID());
		poOrderHHisBean.setORDER_H_CODE(poOrderHBean.getORDER_H_CODE());
		poOrderHHisBean.setORDER_H_NAME(poOrderHBean.getORDER_H_NAME());
		poOrderHHisBean.setORDER_H_TYPE(poOrderHBean.getORDER_H_NAME());
		poOrderHHisBean.setPO_DOC_CAT_CODE(poOrderHBean.getPO_DOC_CAT_CODE());
		poOrderHHisBean.setCONTRACT_ID(poOrderHBean.getCONTRACT_ID());
		poOrderHHisBean.setVENDOR_ID(poOrderHBean.getVENDOR_ID());
		poOrderHHisBean.setDEPT_ID(poOrderHBean.getDEPT_ID());
		poOrderHHisBean.setPURCHASE_DEPT_ID(poOrderHBean.getPURCHASE_DEPT_ID());
		poOrderHHisBean.setPURCHASE_PERSON(poOrderHBean.getPURCHASE_PERSON());
		poOrderHHisBean.setPROJECT_ID(poOrderHBean.getPROJECT_ID());
		poOrderHHisBean.setVERSION(0);
		poOrderHHisBean.setORG_ID(poOrderHBean.getORG_ID());
		poOrderHHisBean.setLEDGER_ID(poOrderHBean.getLEDGER_ID());
		poOrderHHisBean.setIS_CLOSE(poOrderHBean.getIS_CLOSE());
		poOrderHHisBean.setAMOUNT(poOrderHBean.getAMOUNT());
		poOrderHHisBean.setINV_AMOUNT(poOrderHBean.getINV_AMOUNT());
		poOrderHHisBean.setINS_CODE(poOrderHBean.getINS_CODE());
		poOrderHHisBean.setAUDIT_STATUS(poOrderHBean.getAUDIT_STATUS());
		poOrderHHisBean.setAUDIT_STATUS_DESC(poOrderHBean.getAUDIT_STATUS_DESC());
		poOrderHHisBean.setAUDIT_DATE(poOrderHBean.getAUDIT_DATE());
		poOrderHHisBean.setSYS_AUDIT_STATUS(poOrderHBean.getSYS_AUDIT_STATUS());
		poOrderHHisBean.setSYS_AUDIT_STATUS_DESC(poOrderHBean.getSYS_AUDIT_STATUS_DESC());
		poOrderHHisBean.setDOCUMENT_DATE(poOrderHBean.getDOCUMENT_DATE());
		poOrderHHisBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
		poOrderHHisBean.setCREATED_BY(CurrentSessionVar.getUserId());
		poOrderHHisBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
		poOrderHHisBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
	}
	
	public void copyOrderL2OrderHistoryL(List<PoOrderLBean> poOrderLBeans,List<PoOrderLHisBean> poOrderLHisBeans,PoOrderHHisBean poOrderHHisBean){
		for (PoOrderLBean poOrderLBean : poOrderLBeans) {
			PoOrderLHisBean poOrderLHisBean = new PoOrderLHisBean();
			poOrderLHisBean.setORDER_L_HIS_ID(java.util.UUID.randomUUID().toString());
			poOrderLHisBean.setORDER_H_HIS_ID(poOrderHHisBean.getORDER_H_HIS_ID());
			poOrderLHisBean.setORDER_L_ID(poOrderLBean.getORDER_L_ID());
			poOrderLHisBean.setORDER_H_ID(poOrderLBean.getORDER_H_ID());
			poOrderLHisBean.setORDER_L_NUM(poOrderLBean.getORDER_L_NUM());
			poOrderLHisBean.setMATERIAL_ID(poOrderLBean.getMATERIAL_ID());
			poOrderLHisBean.setAP_PUR_TYPE_ID(poOrderLBean.getAP_PUR_TYPE_ID());
			poOrderLHisBean.setPUR_AP_L_ID(poOrderLBean.getPUR_AP_L_ID());
			poOrderLHisBean.setDEPT_ID(poOrderLBean.getDEPT_ID());
			poOrderLHisBean.setPROJECT_ID(poOrderLBean.getPROJECT_ID());
			poOrderLHisBean.setDIM_ID(poOrderLBean.getDIM_ID());
			poOrderLHisBean.setQTY(poOrderLBean.getQTY());
			poOrderLHisBean.setTAX(poOrderLBean.getTAX());
			poOrderLHisBean.setPRICE(poOrderLBean.getPRICE());
			poOrderLHisBean.setINV_PRICE(poOrderLBean.getINV_PRICE());
			poOrderLHisBean.setIS_CLOSE(poOrderLBean.getIS_CLOSE());
			poOrderLHisBean.setAMOUNT(poOrderLBean.getAMOUNT());
			poOrderLHisBean.setINV_AMOUNT(poOrderLBean.getINV_AMOUNT());
			poOrderLHisBean.setORG_ID(poOrderLBean.getORG_ID());
			poOrderLHisBean.setLEDGER_ID(poOrderLBean.getLEDGER_ID());
			poOrderLHisBean.setDESCRIPTION(poOrderLBean.getDESCRIPTION());
			poOrderLHisBean.setVERSION(poOrderLBean.getVERSION());
			poOrderLHisBean.setCREATION_DATE(poOrderLBean.getCREATION_DATE());
			poOrderLHisBean.setCREATED_BY(poOrderLBean.getCREATED_BY());
			poOrderLHisBean.setLAST_UPDATE_DATE(poOrderLBean.getLAST_UPDATE_DATE());
			poOrderLHisBean.setLAST_UPDATED_BY(poOrderLBean.getLAST_UPDATED_BY());
			poOrderLHisBeans.add(poOrderLHisBean);
		}
	}
	
	public void copyOrderHHis2OrderH(PoOrderHHisBean poOrderHHisBean,PoOrderHBean poOrderHBean) throws Exception{
		//poOrderHBean.setORDER_H_ID(poOrderHHisBean.getORDER_H_ID());
		//poOrderHBean.setORDER_H_CODE(poOrderHHisBean.getORDER_H_CODE());
		poOrderHBean.setORDER_H_NAME(poOrderHHisBean.getORDER_H_NAME());
		//poOrderHBean.setORDER_H_TYPE(poOrderHHisBean.getORDER_H_NAME());
		//poOrderHBean.setPO_DOC_CAT_CODE(poOrderHHisBean.getPO_DOC_CAT_CODE());
		//poOrderHBean.setCONTRACT_ID(poOrderHHisBean.getCONTRACT_ID());
		//poOrderHBean.setVENDOR_ID(poOrderHHisBean.getVENDOR_ID());
		//poOrderHBean.setDEPT_ID(poOrderHHisBean.getDEPT_ID());
		//poOrderHBean.setPURCHASE_DEPT_ID(poOrderHHisBean.getPURCHASE_DEPT_ID());
		//poOrderHBean.setPURCHASE_PERSON(poOrderHHisBean.getPURCHASE_PERSON());
		//poOrderHBean.setPROJECT_ID(poOrderHHisBean.getPROJECT_ID());
		//poOrderHBean.setVERSION(poOrderHHisBean.getVERSION());
		//poOrderHBean.setORG_ID(poOrderHHisBean.getORG_ID());
		//poOrderHBean.setLEDGER_ID(poOrderHHisBean.getLEDGER_ID());
		//poOrderHBean.setIS_CLOSE(poOrderHHisBean.getIS_CLOSE());
		poOrderHBean.setAMOUNT(poOrderHHisBean.getAMOUNT());
		poOrderHBean.setINV_AMOUNT(poOrderHHisBean.getINV_AMOUNT());
		//poOrderHBean.setINS_CODE(poOrderHHisBean.getINS_CODE());
		//poOrderHBean.setAUDIT_STATUS(poOrderHHisBean.getAUDIT_STATUS());
		//poOrderHBean.setAUDIT_STATUS_DESC(poOrderHHisBean.getAUDIT_STATUS_DESC());
		//poOrderHBean.setAUDIT_DATE(poOrderHHisBean.getAUDIT_DATE());
		//poOrderHBean.setSYS_AUDIT_STATUS(poOrderHHisBean.getSYS_AUDIT_STATUS());
		//poOrderHBean.setSYS_AUDIT_STATUS_DESC(poOrderHHisBean.getSYS_AUDIT_STATUS_DESC());
		//poOrderHBean.setDOCUMENT_DATE(poOrderHHisBean.getDOCUMENT_DATE());
		//poOrderHBean.setCREATION_DATE(poOrderHHisBean.getCREATION_DATE());
		//poOrderHBean.setCREATED_BY(poOrderHHisBean.getCREATED_BY());
		poOrderHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
		poOrderHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
	}
	
	public void copyOrderLHis2OrderL(List<PoOrderLHisBean> poOrderLHisBeans,List<PoOrderLBean> poOrderLBeans,PoOrderHBean poOrderHBean) throws Exception{
		for (PoOrderLHisBean poOrderHisLBean : poOrderLHisBeans) {
			PoOrderLBean orderLBean = new PoOrderLBean();
			orderLBean.setORDER_L_ID(poOrderHisLBean.getORDER_L_ID());
			orderLBean.setORDER_H_ID(poOrderHisLBean.getORDER_H_ID());
			orderLBean.setORDER_L_NUM(poOrderHisLBean.getORDER_L_NUM());
			orderLBean.setMATERIAL_ID(poOrderHisLBean.getMATERIAL_ID());
			orderLBean.setAP_PUR_TYPE_ID(poOrderHisLBean.getAP_PUR_TYPE_ID());
			orderLBean.setPUR_AP_L_ID(poOrderHisLBean.getPUR_AP_L_ID());
			orderLBean.setDEPT_ID(poOrderHisLBean.getDEPT_ID());
			orderLBean.setPROJECT_ID(poOrderHisLBean.getPROJECT_ID());
			orderLBean.setDIM_ID(poOrderHisLBean.getDIM_ID());
			orderLBean.setQTY(poOrderHisLBean.getQTY());
			orderLBean.setTAX(poOrderHisLBean.getTAX());
			orderLBean.setPRICE(poOrderHisLBean.getPRICE());
			orderLBean.setINV_PRICE(poOrderHisLBean.getINV_PRICE());
			orderLBean.setIS_CLOSE(poOrderHisLBean.getIS_CLOSE());
			orderLBean.setAMOUNT(poOrderHisLBean.getAMOUNT());
			orderLBean.setINV_AMOUNT(poOrderHisLBean.getINV_AMOUNT());
			orderLBean.setORG_ID(poOrderHisLBean.getORG_ID());
			orderLBean.setLEDGER_ID(poOrderHisLBean.getLEDGER_ID());
			orderLBean.setDESCRIPTION(poOrderHisLBean.getDESCRIPTION());
			orderLBean.setVERSION(poOrderHisLBean.getVERSION());
			orderLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
			orderLBean.setCREATED_BY(CurrentSessionVar.getUserId());
			orderLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			orderLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			poOrderLBeans.add(orderLBean);
		}
	}
}
