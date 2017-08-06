package com.xzsoft.xc.po.service.impl;

import java.text.SimpleDateFormat;
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

import com.xzsoft.xc.po.dao.PoPurApHDao;
import com.xzsoft.xc.po.modal.PoPurApHBean;
import com.xzsoft.xc.po.modal.PoPurApLBean;
import com.xzsoft.xc.po.service.PoPurApHService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

import java.util.ArrayList;

/** 
 * @ClassName: PoPurApHServiceImpl
 * @Description: 采购申请Service实现类
 * @author weicw
 * @date 2016年12月5日 上午11:05:08 
 * 
 */
@Service("poPurApHService")
public class PoPurApHServiceImpl implements PoPurApHService{
	@Resource
	PoPurApHDao dao;
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	RuleService ruleService;
	//记录日志
	Logger log = Logger.getLogger(PoPurApHServiceImpl.class.getName());
	/**
	 * @Title:saveInfo
	 * @Description:保存采购申请
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject saveInfo(String formStr,String gridStr,String deleteIds) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONObject formJo = new JSONObject(formStr);
			JSONArray gridArray = new JSONArray(gridStr);
			String purApHId = formJo.getString("PUR_AP_H_ID");//采购申请ID
			String orgId = formJo.getString("ORG_ID");//组织ID
			String ledgerId = formJo.getString("LEDGER_ID");//账簿ID
			String purApHCode = formJo.getString("PUR_AP_H_CODE");//采购申请编号
			String purApHName = formJo.getString("PUR_AP_H_NAME");//采购申请说明
			String projectId = formJo.getString("PROJECT_ID");//项目ID
			String deptId = formJo.getString("DEPT_ID");//部门ID
			String auditStatus = formJo.getString("AUDIT_STATUS");//工作流状态
			String auditStatusDesc = formJo.getString("AUDIT_STATUS_DESC");//工作流状态说明
			String sysAuditStatus = formJo.getString("SYS_AUDIT_STATUS");//业务状态
			String sysAuditStatusDesc = formJo.getString("SYS_AUDIT_STATUS_DESC");//业务状态说明
			String documentDate = formJo.getString("DOCUMENT_DATE");//单据日期
			String amount = formJo.getString("AMOUNT");//金额
			String isClose = formJo.getString("IS_CLOSE");//是否关闭
			String poDocCatCode = formJo.getString("PO_DOC_CAT_CODE");//采购单据类型
			PoPurApHBean bean = new PoPurApHBean();
			if(dao.ifExistH(purApHId)==null){//新增采购申请
				String ruleCode = "PO_PUR_AP";
				String year = sdf.format(CurrentUserUtil.getCurrentDate()).substring(0,4);	//年
				String month = sdf.format(CurrentUserUtil.getCurrentDate()).substring(5,7);	//月
				//得到单据编码
				purApHCode = ruleService.getNextSerialNum(ruleCode, ledgerId, "", year, month, CurrentSessionVar.getUserId());
				if(purApHCode!=null||purApHCode!=""){
					bean.setPUR_AP_H_CODE(purApHCode);
				}
				bean.setPUR_AP_H_ID(purApHId);
				bean.setPUR_AP_H_NAME(purApHName);
				bean.setAMOUNT(Double.valueOf(amount));
				bean.setIS_CLOSE(isClose);
				bean.setORG_ID(orgId);
				bean.setLEDGER_ID(ledgerId);
				bean.setPROJECT_ID(projectId);
				bean.setDEPT_ID(deptId);
				bean.setAUDIT_STATUS(auditStatus);
				bean.setAUDIT_STATUS_DESC(auditStatusDesc);
				bean.setSYS_AUDIT_STATUS(sysAuditStatus);
				bean.setSYS_AUDIT_STATUS_DESC(sysAuditStatusDesc);
				bean.setDOCUMENT_DATE(sdf.parse(documentDate));
				bean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				bean.setCREATED_BY(CurrentSessionVar.getUserId());
				bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				bean.setPO_DOC_CAT_CODE(poDocCatCode);
				dao.addPurHInfo(bean);
				for (int i = 0; i < gridArray.length(); i++) {
					JSONObject gridJo = gridArray.getJSONObject(i);
					PoPurApLBean lbean = new PoPurApLBean();
					lbean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
					lbean.setPUR_AP_H_ID(purApHId);
					lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
					lbean.setDIM_ID(gridJo.getString("DIM_ID"));
					lbean.setQTY(Integer.parseInt(gridJo.getString("QTY")));
					lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
					lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
					lbean.setREQUIRED_DATE(sdf.parse(gridJo.getString("REQUIRED_DATE")));
					lbean.setPURCHASE_REASON(gridJo.getString("PURCHASE_REASON"));
					lbean.setORG_ID(orgId);
					lbean.setLEDGER_ID(ledgerId);
					lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					lbean.setCREATED_BY(CurrentSessionVar.getUserId());
					lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addPurLInfo(lbean);//新增行信息
				}
			}else{//修改采购申请
				//修改主信息
				bean.setPUR_AP_H_ID(purApHId);
				bean.setPUR_AP_H_CODE(purApHCode);
				bean.setPUR_AP_H_NAME(purApHName);
				bean.setAMOUNT(Double.valueOf(amount));
				bean.setORG_ID(orgId);
				bean.setLEDGER_ID(ledgerId);
				bean.setPROJECT_ID(projectId);
				bean.setDEPT_ID(deptId);
				bean.setDOCUMENT_DATE(sdf.parse(documentDate));
				bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.editPurHInfo(bean);
				/*
				 * 修改行信息
				 * */
				JSONArray deleteArray = new JSONArray(deleteIds);
				List<String> ids = new ArrayList<String>();
				//删除行信息
				for (int i = 0; i < deleteArray.length(); i++) {
					String id = deleteArray.getJSONObject(i).getString("id");
					ids.add(id);
				}
				if(ids.size()>0){
					dao.deletePurLInfo(ids);
				}
				for (int i = 0; i < gridArray.length(); i++){
					JSONObject gridJo = gridArray.getJSONObject(i);
					String purApLId = gridJo.getString("PUR_AP_L_ID");
					if(dao.ifExistL(purApLId)==null){//新增行信息
						PoPurApLBean lbean = new PoPurApLBean();
						lbean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
						lbean.setPUR_AP_H_ID(purApHId);
						lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
						lbean.setDIM_ID(gridJo.getString("DIM_ID"));
						lbean.setQTY(Integer.parseInt(gridJo.getString("QTY")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setREQUIRED_DATE(sdf.parse(gridJo.getString("REQUIRED_DATE")));
						lbean.setPURCHASE_REASON(gridJo.getString("PURCHASE_REASON"));
						lbean.setORG_ID(orgId);
						lbean.setLEDGER_ID(ledgerId);
						lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setCREATED_BY(CurrentSessionVar.getUserId());
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.addPurLInfo(lbean);//新增行信息
					}else{//修改行信息
						PoPurApLBean lbean = new PoPurApLBean();
						lbean.setPUR_AP_L_ID(gridJo.getString("PUR_AP_L_ID"));
						lbean.setQTY(Integer.parseInt(gridJo.getString("QTY")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setREQUIRED_DATE(sdf.parse(gridJo.getString("REQUIRED_DATE")));
						lbean.setPURCHASE_REASON(gridJo.getString("PURCHASE_REASON"));
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.editPurLInfo(lbean);
					}
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
	/**
	 * @Title:deleteInfo
	 * @Description:删除采购申请
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteInfo(String ids) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray  idArray = new JSONArray(ids); 
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < idArray.length(); i++) {
				String id = idArray.getJSONObject(i).getString("id");
				list.add(id);
			}
			if(list.size()>0){
				dao.deletePurLInfoByHId(list);
				dao.deletePurHInfo(list);
			}
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "0");
			jo.put("msg",e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title:editPurIsClose
	 * @Description:关闭或打开采购申请单 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@Override
	public JSONObject editPurIsClose(String ids,String operate) throws Exception{
		JSONObject jo = new JSONObject();
		List<PoPurApHBean> list = new ArrayList<PoPurApHBean>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			JSONArray idArray = new JSONArray(ids);
			for(int i=0;i<idArray.length();i++){
				JSONObject idJson = idArray.getJSONObject(i);
				PoPurApHBean bean = new PoPurApHBean();
				bean.setIS_CLOSE(operate);
				bean.setPUR_AP_H_ID(idJson.getString("PUR_AP_H_ID"));
				list.add(bean);
			}
			map.put("list", list);
			map.put("dbType", PlatformUtil.getDbType());//数据库类型
			dao.editPurIsClose(map);
			jo.put("flag", "0");//正常情况
		} catch (Exception e) {//异常情况
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
		}
		return jo;
	}
}
