package com.xzsoft.xc.po.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.po.dao.PoOrderHDao;
import com.xzsoft.xc.po.modal.PoOrderHBean;
import com.xzsoft.xc.po.modal.PoOrderHHisBean;
import com.xzsoft.xc.po.modal.PoOrderLBean;
import com.xzsoft.xc.po.modal.PoOrderLHisBean;
import com.xzsoft.xc.po.modal.PoPurApLBean;
import com.xzsoft.xc.po.service.PoOrderService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/** 
 * @ClassName: PoOrderServiceImpl
 * @Description: 采购订单Service接口实现类
 * @author weicw
 * @date 2016年12月15日 下午3:04:52 
 * 
 */
@Service("poOrderService")
public class PoOrderServiceImpl implements PoOrderService {
	@Resource
	PoOrderHDao dao;
	@Resource
	RuleService ruleService;
	//日志记录
	Logger log = Logger.getLogger(PoOrderServiceImpl.class.getName());
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * @Title:saveOrder
	 * @Description:保存采购订单  
	 * 参数格式:
	 * @param  java.lang.String(主信息) java.lang.String(行信息) java.lang.String(删除的行)
	 * @return 
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject saveOrder(String formStr, String gridStr, String deleteIds) // ,String lineNums
			throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONObject formJo = new JSONObject(formStr);//主信息json
			JSONArray gridArray = new JSONArray(gridStr);//行信息json组
			JSONArray idArray = new JSONArray(deleteIds);//删除的行
			//JSONArray lineArray = new JSONArray(lineNums);//行号数组
			String orderHId = formJo.getString("ORDER_H_ID");//采购订单主ID
			String orderHHisId = formJo.getString("ORDER_H_HIS_ID");//采购订单历史主ID
			String orderHCode = formJo.getString("ORDER_H_CODE");//采购订单编号
			String orderHName = formJo.getString("ORDER_H_NAME");//采购订单说明
			String orderHType = formJo.getString("ORDER_H_TYPE");//采购订单类型
			String poDocCatCode = formJo.getString("PO_DOC_CAT_CODE");//采购单据类型
			String contractId = formJo.getString("AP_CONTRACT_ID");//采购合同ID
			String vendorId = formJo.getString("VENDOR_ID");//供应商ID
			String purchaseDeptId = formJo.getString("PURCHASE_DEPT_ID");//采购部门ID
			String deptId = formJo.getString("DEPARTMENT_ID");//成本中心ID
			String projectId = formJo.getString("PROJECT_ID");//项目ID
			String purchasePerson = formJo.getString("PURCHASE_PERSON");//采购人
			String version = formJo.getString("VERSION");//版本
			String orgId = formJo.getString("ORG_ID");//组织ID
			String ledgerId = formJo.getString("LEDGER_ID");//账簿ID
			String isClose = formJo.getString("IS_CLOSE");//是否关闭
			String amount = formJo.getString("AMOUNT");//不含税金额
			String invAmount = formJo.getString("INV_AMOUNT");//含税金额
			String insCode = formJo.getString("INS_CODE");//实例编码
			String auditStatus = formJo.getString("AUDIT_STATUS");//工作流状态
			String auditStatusDesc = formJo.getString("AUDIT_STATUS_DESC");//工作流状态描述
			String sysAuditStatus = formJo.getString("SYS_AUDIT_STATUS");//业务状态
			String sysAuditStatusDesc = formJo.getString("SYS_AUDIT_STATUS_DESC");//业务状态描述
			String documentDate = formJo.getString("DOCUMENT_DATE");//单据日期
			if(dao.ifExistH(orderHId)==null){//新增订单
				PoOrderHBean hbean = new PoOrderHBean();
				String ruleCode = "PO_ORDER";
				String year = sdf.format(CurrentUserUtil.getCurrentDate()).substring(0,4);	//年
				String month = sdf.format(CurrentUserUtil.getCurrentDate()).substring(5,7);	//月
				//得到单据编码
				orderHCode = ruleService.getNextSerialNum(ruleCode, ledgerId, "", year, month, CurrentSessionVar.getUserId());
				if(orderHCode!=null||orderHCode!=""){
					hbean.setORDER_H_CODE(orderHCode);
				}
				hbean.setORDER_H_ID(orderHId);
				hbean.setORDER_H_NAME(orderHName);
				hbean.setORDER_H_TYPE(orderHType);
				hbean.setPO_DOC_CAT_CODE(poDocCatCode);
				hbean.setCONTRACT_ID(contractId);
				hbean.setVENDOR_ID(vendorId);
				hbean.setPURCHASE_DEPT_ID(purchaseDeptId);
				hbean.setDEPT_ID(deptId);
				hbean.setPROJECT_ID(projectId);
				hbean.setPURCHASE_PERSON(purchasePerson);
				hbean.setVERSION(Integer.valueOf(version));
				hbean.setORG_ID(orgId);
				hbean.setLEDGER_ID(ledgerId);
				hbean.setIS_CLOSE(isClose);
				hbean.setAMOUNT(Double.valueOf(amount));
				hbean.setINV_AMOUNT(Double.valueOf(invAmount));
				hbean.setINS_CODE(insCode);
				hbean.setAUDIT_STATUS(auditStatus);
				hbean.setAUDIT_STATUS_DESC(auditStatusDesc);
				hbean.setSYS_AUDIT_STATUS(sysAuditStatus);
				hbean.setSYS_AUDIT_STATUS_DESC(sysAuditStatusDesc);
				hbean.setDOCUMENT_DATE(sdf.parse(documentDate));
				hbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				hbean.setCREATED_BY(CurrentSessionVar.getUserId());
				hbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				hbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.addOrderHInfo(hbean);//新增主信息
					for (int i = 0; i < gridArray.length(); i++) {
						JSONObject gridJo = gridArray.getJSONObject(i);
						PoOrderLBean lbean = new PoOrderLBean();
						lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
						lbean.setORDER_H_ID(orderHId);
						//lbean.setORDER_L_NUM(lineArray.getInt(i));
						lbean.setORDER_L_NUM(Integer.valueOf(gridJo.getString("ORDER_L_NUM")));
						lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
						lbean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
						lbean.setDEPT_ID(deptId);
						lbean.setPROJECT_ID(projectId);
						lbean.setDIM_ID(gridJo.getString("DIM_ID"));
						lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
						lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
						lbean.setIS_CLOSE(gridJo.getString("IS_CLOSE"));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
						lbean.setORG_ID(orgId);
						lbean.setLEDGER_ID(ledgerId);
						lbean.setDESCRIPTION(gridJo.getString("DESCRIPTION"));
						lbean.setVERSION(Integer.valueOf(version));
						lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setCREATED_BY(CurrentSessionVar.getUserId());
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.addOrderLInfo(lbean);//新增行信息
						PoPurApLBean purbean = new PoPurApLBean();
						purbean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
						purbean.setPO_ORDER_H_ID(orderHId);
						purbean.setPO_ORDER_L_ID(gridJo.getString("ORDER_L_ID"));
						dao.editPurInfo(purbean);//修改采购申请行表
					}	
			}else{
				if(!"".equalsIgnoreCase(orderHHisId)){//采购订单历史表主ID不为空
					if(dao.ifExistHisH(orderHHisId)==null){//新增采购订单历史信息
						addOrderHHisInfo(formStr, gridStr); //, lineNums
					}else{//修改采购订单历史信息
						editOrderHHisInfo(formStr, gridStr, deleteIds);//, lineNums
					}
				}else{//修改订单
					PoOrderHBean hbean = new PoOrderHBean();
					hbean.setORDER_H_ID(orderHId);
					hbean.setORDER_H_CODE(orderHCode);
					hbean.setORDER_H_NAME(orderHName);
					hbean.setORDER_H_TYPE(orderHType);
					hbean.setCONTRACT_ID(contractId);
					hbean.setVENDOR_ID(vendorId);
					hbean.setPURCHASE_DEPT_ID(purchaseDeptId);
					hbean.setDEPT_ID(deptId);
					hbean.setPROJECT_ID(projectId);
					hbean.setPURCHASE_PERSON(purchasePerson);
					hbean.setVERSION(Integer.valueOf(version));
					hbean.setORG_ID(orgId);
					hbean.setLEDGER_ID(ledgerId);
					hbean.setAMOUNT(Double.valueOf(amount));
					hbean.setINV_AMOUNT(Double.valueOf(invAmount));
					hbean.setINS_CODE(insCode);
					hbean.setSYS_AUDIT_STATUS(sysAuditStatus);
					hbean.setSYS_AUDIT_STATUS_DESC(sysAuditStatusDesc);
					hbean.setDOCUMENT_DATE(sdf.parse(documentDate));
					hbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					hbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.editOrderHInfo(hbean);//修改订单主信息
						List<String> delOrderList = new ArrayList<String>();//删除订单行信息的List
						List<String> delPurList = new ArrayList<String>();//删除申请行信息的List
						for (int i = 0; i < idArray.length(); i++) {
							delOrderList.add(idArray.getJSONObject(i).getString("ORDER_L_ID"));
							delPurList.add(idArray.getJSONObject(i).getString("PUR_AP_L_ID"));
						}
						if(delOrderList.size()>0){//delList的长度不为0
							dao.deleteOrderLInfo(delOrderList);//删除行信息
						}
						for(int m=0;m<delPurList.size();m++){//修改采购申请行表
							PoPurApLBean purbean = new PoPurApLBean();
							purbean.setPUR_AP_L_ID(delPurList.get(m));
							purbean.setPO_ORDER_H_ID("");
							purbean.setPO_ORDER_L_ID("");
							dao.editPurInfo(purbean);
						}
						for (int j = 0; j < gridArray.length(); j++) {
							JSONObject gridJo = gridArray.getJSONObject(j);
							if(dao.ifExistL(gridJo.getString("ORDER_L_ID"))==null){//新增行信息
								PoOrderLBean lbean = new PoOrderLBean();
								lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
								lbean.setORDER_H_ID(orderHId);
								//lbean.setORDER_L_NUM(lineArray.getInt(j));
								lbean.setORDER_L_NUM(Integer.valueOf(gridJo.getString("ORDER_L_NUM")));
								lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
								lbean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
								lbean.setDEPT_ID(deptId);
								lbean.setPROJECT_ID(projectId);
								lbean.setDIM_ID(gridJo.getString("DIM_ID"));
								lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
								lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
								lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
								lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
								lbean.setIS_CLOSE(gridJo.getString("IS_CLOSE"));
								lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
								lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
								lbean.setORG_ID(orgId);
								lbean.setLEDGER_ID(ledgerId);
								lbean.setDESCRIPTION(gridJo.getString("DESCRIPTION"));
								lbean.setVERSION(Integer.valueOf(version));
								lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
								lbean.setCREATED_BY(CurrentSessionVar.getUserId());
								lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
								lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
								dao.addOrderLInfo(lbean);//新增行信息
								if(!"".equals(gridJo.getString("PUR_AP_L_ID"))){
									PoPurApLBean purbean = new PoPurApLBean();
									purbean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
									purbean.setPO_ORDER_H_ID(orderHId);
									purbean.setPO_ORDER_L_ID(gridJo.getString("ORDER_L_ID"));
									dao.editPurInfo(purbean);//修改采购申请行表
								}
								
							}else{//修改行信息
								PoOrderLBean lbean = new PoOrderLBean();
								lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
								//lbean.setORDER_L_NUM(lineArray.getInt(j));
								lbean.setORDER_L_NUM(Integer.valueOf(gridJo.getString("ORDER_L_NUM")));
								lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
								lbean.setDEPT_ID(deptId);
								lbean.setPROJECT_ID(projectId);
								lbean.setDIM_ID(gridJo.getString("DIM_ID"));
								lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
								lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
								lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
								lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
								lbean.setIS_CLOSE(gridJo.getString("IS_CLOSE"));
								lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
								lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
								lbean.setORG_ID(orgId);
								lbean.setLEDGER_ID(ledgerId);
								lbean.setDESCRIPTION(gridJo.getString("DESCRIPTION"));
								lbean.setVERSION(Integer.valueOf(version));
								lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
								lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
								dao.editOrderLInfo(lbean);
							}
						}
				}
				
				
			}
			jo.put("flag", "0");//正常情况
		} catch (Exception e) {//异常
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		
		return jo;
	}
	
	public JSONObject saveOrder2() throws Exception {
		return null;
	}
	/**
	 * @Title:updateOrerHStatus
	 * @Description:更改采购订单主信息是否关闭的状态 
	 * 参数格式:
	 * @param  java.lang.String(主信息ID) java.lang.String(是否关闭)
	 * @return 
	 * @throws 
	 */
	@Override
	public JSONObject updateOrerHStatus(String ids,String operate) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(ids);//存储ID数组的json
			List<PoOrderHBean> list = new ArrayList<PoOrderHBean>();//存储订单主信息实体类的List
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < idArray.length(); i++) {
				PoOrderHBean bean = new PoOrderHBean();
				bean.setORDER_H_ID(idArray.getJSONObject(i).getString("ORDER_H_ID"));
				bean.setIS_CLOSE(operate);
				list.add(bean);
			}
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			dao.updateOrerHStatus(map);//更新主信息
			dao.updateOrerLStatus(map);//更新行信息
			jo.put("flag", "0");//正常情况
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title:updateOrerLStatusByLId
	 * @Description:更改采购订单行信息是否关闭的状态 (根据行信息ID)
	 * 参数格式:
	 * @param  java.lang.String(行信息ID) java.lang.String(是否关闭)
	 * @return 
	 * @throws 
	 */
	@Override
	public JSONObject updateOrerLStatusByLId(String ids,String operate) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(ids);//存储ID数组的json
			List<PoOrderLBean> list = new ArrayList<PoOrderLBean>();//存储订单行信息实体类的List
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < idArray.length(); i++) {
				PoOrderLBean bean = new PoOrderLBean();
				bean.setORDER_L_ID(idArray.getJSONObject(i).getString("ORDER_L_ID"));
				bean.setIS_CLOSE(operate);
				list.add(bean);
			}
			map.put("dbType", PlatformUtil.getDbType());
			map.put("list", list);
			dao.updateOrerLStatusByLId(map);//更新行信息
			jo.put("flag", "0");//正常情况
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title:deleteOrderHInfo
	 * @Description:删除采购订单主表信息 
	 * 参数格式:
	 * @param  java.lang.String(主信息ID)
	 * @return 
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject deleteOrderHInfo(String ids)  throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(ids);//存储ID数组的json
			List<String> orderList = new ArrayList<String>();
			for (int i = 0; i < idArray.length(); i++) {
				orderList.add(idArray.getJSONObject(i).getString("ORDER_H_ID"));
			}
			dao.deleteOrderLInfo(orderList);//删除行信息
			dao.deleteOrderHInfo(orderList);//删除主信息
			for (int j = 0; j < orderList.size(); j++) {
				PoPurApLBean bean = new PoPurApLBean();
				bean.setPO_ORDER_H_ID(orderList.get(j));
				bean.setPO_ORDER_L_ID("");
				dao.editPurInfoByOrder(bean);//修改采购申请行信息
			}
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title:addOrderHHisInfo
	 * @Description:新增采购订单历史表信息
	 * 参数格式:
	 * @param  java.lang.String(主信息) java.lang.String(行信息) java.lang.String(行号)
	 * @return 
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject addOrderHHisInfo(String formStr, String gridStr) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONObject formJo = new JSONObject(formStr);//主信息json
			JSONArray gridArray = new JSONArray(gridStr);//行信息json组
			//JSONArray lineArray = new JSONArray(lineNums);//行号数组
			String orderHId = formJo.getString("ORDER_H_ID");//采购订单主ID
			String orderHHisId = formJo.getString("ORDER_H_HIS_ID");//采购订单历史主ID
			String orderHCode = formJo.getString("ORDER_H_CODE");//采购订单编号
			String orderHName = formJo.getString("ORDER_H_NAME");//采购订单说明
			String orderHType = formJo.getString("ORDER_H_TYPE");//采购订单类型
			String poDocCatCode = formJo.getString("PO_DOC_CAT_CODE");//采购单据类型
			String contractId = formJo.getString("AP_CONTRACT_ID");//采购合同ID
			String vendorId = formJo.getString("VENDOR_ID");//供应商ID
			String purchaseDeptId = formJo.getString("PURCHASE_DEPT_ID");//采购部门ID
			String deptId = formJo.getString("DEPARTMENT_ID");//成本中心ID
			String projectId = formJo.getString("PROJECT_ID");//项目ID
			String purchasePerson = formJo.getString("PURCHASE_PERSON");//采购人
			String version = formJo.getString("VERSION");//版本
			String orgId = formJo.getString("ORG_ID");//组织ID
			String ledgerId = formJo.getString("LEDGER_ID");//账簿ID
			String isClose = formJo.getString("IS_CLOSE");//是否关闭
			String amount = formJo.getString("AMOUNT");//不含税金额
			String invAmount = formJo.getString("INV_AMOUNT");//含税金额
			String insCode = formJo.getString("INS_CODE");//实例编码
			String auditStatus = formJo.getString("AUDIT_STATUS");//工作流状态
			String auditStatusDesc = formJo.getString("AUDIT_STATUS_DESC");//工作流状态描述
			String sysAuditStatus = formJo.getString("SYS_AUDIT_STATUS");//业务状态
			String sysAuditStatusDesc = formJo.getString("SYS_AUDIT_STATUS_DESC");//业务状态描述
			String documentDate = formJo.getString("DOCUMENT_DATE");//单据日期
			PoOrderHHisBean hisBean = new PoOrderHHisBean();
			hisBean.setORDER_H_HIS_ID(orderHHisId);
			hisBean.setORDER_H_ID(orderHId);
			hisBean.setORDER_H_CODE(orderHCode);
			hisBean.setORDER_H_NAME(orderHName);
			hisBean.setORDER_H_TYPE(orderHType);
			hisBean.setPO_DOC_CAT_CODE(poDocCatCode);
			hisBean.setCONTRACT_ID(contractId);
			hisBean.setVENDOR_ID(vendorId);
			hisBean.setPURCHASE_DEPT_ID(purchaseDeptId);
			hisBean.setDEPT_ID(deptId);
			hisBean.setPROJECT_ID(projectId);
			hisBean.setPURCHASE_PERSON(purchasePerson);
			hisBean.setVERSION(Integer.valueOf(version));
			hisBean.setORG_ID(orgId);
			hisBean.setLEDGER_ID(ledgerId);
			hisBean.setIS_CLOSE(isClose);
			hisBean.setAMOUNT(Double.valueOf(amount));
			hisBean.setINV_AMOUNT(Double.valueOf(invAmount));
			hisBean.setINS_CODE(insCode);
			hisBean.setAUDIT_STATUS(auditStatus);
			hisBean.setAUDIT_STATUS_DESC(auditStatusDesc);
			hisBean.setSYS_AUDIT_STATUS(sysAuditStatus);
			hisBean.setSYS_AUDIT_STATUS_DESC(sysAuditStatusDesc);
			hisBean.setDOCUMENT_DATE(sdf.parse(documentDate));
			hisBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
			hisBean.setCREATED_BY(CurrentSessionVar.getUserId());
			hisBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			hisBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			dao.addOrderHHisInfo(hisBean);
				for (int i = 0; i < gridArray.length(); i++) {
					PoOrderLHisBean lHisBean = new PoOrderLHisBean();
					JSONObject gridJo = gridArray.getJSONObject(i);
					lHisBean.setORDER_H_HIS_ID(orderHHisId);
					lHisBean.setORDER_L_HIS_ID(gridJo.getString("ORDER_L_HIS_ID"));
					lHisBean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
					lHisBean.setORDER_H_ID(orderHId);
					//lHisBean.setORDER_L_NUM(lineArray.getInt(i));
					lHisBean.setORDER_L_NUM(Integer.valueOf(gridJo.getString("ORDER_L_NUM")));
					lHisBean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
					lHisBean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
					lHisBean.setDEPT_ID(deptId);
					lHisBean.setPROJECT_ID(projectId);
					lHisBean.setDIM_ID(gridJo.getString("DIM_ID"));
					lHisBean.setQTY(Double.valueOf(gridJo.getString("QTY")));
					lHisBean.setTAX(Double.valueOf(gridJo.getString("TAX")));
					lHisBean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
					lHisBean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
					lHisBean.setIS_CLOSE(gridJo.getString("IS_CLOSE"));
					lHisBean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
					lHisBean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
					lHisBean.setORG_ID(orgId);
					lHisBean.setLEDGER_ID(ledgerId);
					lHisBean.setDESCRIPTION(gridJo.getString("DESCRIPTION"));
					lHisBean.setVERSION(Integer.valueOf(version));
					lHisBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					lHisBean.setCREATED_BY(CurrentSessionVar.getUserId());
					lHisBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					lHisBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addOrderLHisInfo(lHisBean);
				}
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title:editOrderHHisInfo
	 * @Description:修改采购订单历史表信息
	 * 参数格式:
	 * @param  java.lang.String(主信息) java.lang.String(行信息) java.lang.String(行号) java.lang.String(删除的行)
	 * @return 
	 * @throws 
	 */
	public JSONObject editOrderHHisInfo(String formStr, String gridStr,String deleteIds) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONObject formJo = new JSONObject(formStr);//主信息json
			JSONArray gridArray = new JSONArray(gridStr);//行信息json组
			JSONArray idArray = new JSONArray(deleteIds);//删除的行
			String orderHId = formJo.getString("ORDER_H_ID");//采购订单主ID
			String orderHHisId = formJo.getString("ORDER_H_HIS_ID");//采购订单历史主ID
			String orderHCode = formJo.getString("ORDER_H_CODE");//采购订单编号
			String orderHName = formJo.getString("ORDER_H_NAME");//采购订单说明
			String orderHType = formJo.getString("ORDER_H_TYPE");//采购订单类型
			//String poDocCatCode = formJo.getString("PO_DOC_CAT_CODE");//采购单据类型
			String contractId = formJo.getString("AP_CONTRACT_ID");//采购合同ID
			String vendorId = formJo.getString("VENDOR_ID");//供应商ID
			String purchaseDeptId = formJo.getString("PURCHASE_DEPT_ID");//采购部门ID
			String deptId = formJo.getString("DEPARTMENT_ID");//成本中心ID
			String projectId = formJo.getString("PROJECT_ID");//项目ID
			String purchasePerson = formJo.getString("PURCHASE_PERSON");//采购人
			String version = formJo.getString("VERSION");//版本
			String orgId = formJo.getString("ORG_ID");//组织ID
			String ledgerId = formJo.getString("LEDGER_ID");//账簿ID
			//String isClose = formJo.getString("IS_CLOSE");//是否关闭
			String amount = formJo.getString("AMOUNT");//不含税金额
			String invAmount = formJo.getString("INV_AMOUNT");//含税金额
			String insCode = formJo.getString("INS_CODE");//实例编码
			//String auditStatus = formJo.getString("AUDIT_STATUS");//工作流状态
			//String auditStatusDesc = formJo.getString("AUDIT_STATUS_DESC");//工作流状态描述
			String sysAuditStatus = formJo.getString("SYS_AUDIT_STATUS");//业务状态
			String sysAuditStatusDesc = formJo.getString("SYS_AUDIT_STATUS_DESC");//业务状态描述
			String documentDate = formJo.getString("DOCUMENT_DATE");//单据日期
			
			PoOrderHHisBean hisbean = new PoOrderHHisBean();
			hisbean.setORDER_H_HIS_ID(orderHHisId);
			hisbean.setORDER_H_ID(orderHId);
			hisbean.setORDER_H_CODE(orderHCode);
			hisbean.setORDER_H_NAME(orderHName);
			hisbean.setORDER_H_TYPE(orderHType);
			hisbean.setCONTRACT_ID(contractId);
			hisbean.setVENDOR_ID(vendorId);
			hisbean.setPURCHASE_DEPT_ID(purchaseDeptId);
			hisbean.setDEPT_ID(deptId);
			hisbean.setPROJECT_ID(projectId);
			hisbean.setPURCHASE_PERSON(purchasePerson);
			hisbean.setVERSION(Integer.valueOf(version));
			hisbean.setORG_ID(orgId);
			hisbean.setLEDGER_ID(ledgerId);
			hisbean.setAMOUNT(Double.valueOf(amount));
			hisbean.setINV_AMOUNT(Double.valueOf(invAmount));
			hisbean.setINS_CODE(insCode);
			hisbean.setSYS_AUDIT_STATUS(sysAuditStatus);
			hisbean.setSYS_AUDIT_STATUS_DESC(sysAuditStatusDesc);
			hisbean.setDOCUMENT_DATE(sdf.parse(documentDate));
			hisbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			hisbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			dao.editOrderHHisInfo(hisbean);//修改订单历史主信息
				List<String> delOrderList = new ArrayList<String>();//删除订单行信息的List
				//List<String> delPurList = new ArrayList<String>();//删除申请行信息的List
				for (int i = 0; i < idArray.length(); i++) {
					delOrderList.add(idArray.getJSONObject(i).getString("ORDER_L_HIS_ID"));
				}
				if(delOrderList.size()>0){//delList的长度不为0
					dao.deleteOrderLHisInfo(delOrderList);//删除订单历史行信息
				}
				for (int j = 0; j < gridArray.length(); j++) {
					JSONObject gridJo = gridArray.getJSONObject(j);
					if(dao.ifExistLHis(gridJo.getString("ORDER_L_HIS_ID"))==null){//新增行信息
						PoOrderLHisBean lbean = new PoOrderLHisBean();
						lbean.setORDER_H_HIS_ID(orderHHisId);
						lbean.setORDER_L_HIS_ID(gridJo.getString("ORDER_L_HIS_ID"));
						lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
						lbean.setORDER_H_ID(orderHId);
						//lbean.setORDER_L_NUM(lineArray.getInt(j));
						lbean.setORDER_L_NUM(Integer.valueOf(gridJo.getString("ORDER_L_NUM")));
						lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
						lbean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
						lbean.setDEPT_ID(deptId);
						lbean.setPROJECT_ID(projectId);
						lbean.setDIM_ID(gridJo.getString("DIM_ID"));
						lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
						lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
						lbean.setIS_CLOSE(gridJo.getString("IS_CLOSE"));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
						lbean.setORG_ID(orgId);
						lbean.setLEDGER_ID(ledgerId);
						lbean.setDESCRIPTION(gridJo.getString("DESCRIPTION"));
						lbean.setVERSION(Integer.valueOf(version));
						lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setCREATED_BY(CurrentSessionVar.getUserId());
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.addOrderLHisInfo(lbean);//新增行信息
						
					}else{//修改行信息
						PoOrderLHisBean lbean = new PoOrderLHisBean();
						lbean.setORDER_L_HIS_ID(gridJo.getString("ORDER_L_HIS_ID"));
						lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
						//lbean.setORDER_L_NUM(lineArray.getInt(j));
						lbean.setORDER_L_NUM(Integer.valueOf(gridJo.getString("ORDER_L_NUM")));
						lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
						lbean.setDEPT_ID(deptId);
						lbean.setPROJECT_ID(projectId);
						lbean.setDIM_ID(gridJo.getString("DIM_ID"));
						lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
						lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
						lbean.setIS_CLOSE(gridJo.getString("IS_CLOSE"));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
						lbean.setORG_ID(orgId);
						lbean.setLEDGER_ID(ledgerId);
						lbean.setDESCRIPTION(gridJo.getString("DESCRIPTION"));
						lbean.setVERSION(Integer.valueOf(version));
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.editOrderLHisInfo(lbean);;
					}
				}
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	
}
