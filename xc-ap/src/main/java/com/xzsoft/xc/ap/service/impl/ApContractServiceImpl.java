package com.xzsoft.xc.ap.service.impl;

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

import com.xzsoft.xc.ap.dao.ApContractDao;
import com.xzsoft.xc.ap.modal.ApContractBean;
import com.xzsoft.xc.ap.modal.ApContractHisBean;
import com.xzsoft.xc.ap.service.ApContractService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/** 
 * @ClassName: ApContractServiceImpl 
 * @Description: TODO
 * @author weicw 
 * @date 2016年8月19日 下午3:24:50 
 * 
 * 
 */
@Service("apContractService")
public class ApContractServiceImpl implements ApContractService {
	@Resource
	ApContractDao dao;
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//日志记录
	Logger log = Logger.getLogger(ApContractServiceImpl.class);
	/**
	 * @Title:saveContarct
	 * @Description:保存合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveContarct(String info,String lang) throws Exception{
		JSONObject joMsg = new JSONObject();
		try {
			JSONObject jo = new JSONObject(info);
			String contractHisId = jo.getString("AP_CONTRACT_HIS_ID");
			String contractId = jo.getString("AP_CONTRACT_ID");
			String contractCode = jo.getString("AP_CONTRACT_CODE");
			String contractName = jo.getString("AP_CONTRACT_NAME");
			String contractType = jo.getString("AP_CONTRACT_TYPE");
			String ledgerId = jo.getString("LEDGER_ID");
			String startDate = jo.getString("START_DATE");
			String endDate = jo.getString("END_DATE");
			String vendorId = jo.getString("VENDOR_ID");
			String amount = jo.getString("AMOUNT");
			String deptId = jo.getString("DEPT_ID");
			String projectId = jo.getString("PROJECT_ID");
			String user = jo.getString("EMP_ID");
			String isClose = jo.getString("IS_CLOSE");
			String description = jo.getString("DESCRIPTION");
			String contractClassify = jo.getString("CONTRACT_CLASSIFY");
			String taxAmt = jo.getString("TAX_AMT");
			String invAmount = jo.getString("INV_AMOUNT");
			String orgId = jo.getString("ORG_ID");
			String version = jo.getString("VERSION");
			String auditStatus = jo.getString("AUDIT_STATUS");
			String auditStatusDesc = jo.getString("AUDIT_STATUS_DESC");
			String sysAuditStatus = jo.getString("SYS_AUDIT_STATUS");
			String sysAuditStatusDesc = jo.getString("SYS_AUDIT_STATUS_DESC");
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("LEDGER_ID", ledgerId);
			map.put("AP_CONTRACT_CODE", contractCode);
			map.put("AP_CONTRACT_NAME", contractName);
			map.put("AP_CONTRACT_ID", contractId);
			if("0".equals(dao.checkExistAdd(map))){//判断同账簿下是否有同编码或同合同名称的合同 
				ApContractBean bean = new ApContractBean();
				bean.setAP_CONTRACT_ID(contractId);
				bean.setAP_CONTRACT_CODE(contractCode);
				bean.setAP_CONTRACT_NAME(contractName);
				bean.setAP_CONTRACT_TYPE(contractType);
				bean.setLEDGER_ID(ledgerId);
				bean.setSTART_DATE(sdf.parse(startDate));
				bean.setEND_DATE(sdf.parse(endDate));
				bean.setVENDOR_ID(vendorId);
				bean.setAMOUNT(Double.parseDouble(amount));
				bean.setDEPT_ID(deptId);
				bean.setPROJECT_ID(projectId);
				bean.setCONTRACT_USER(user);
				bean.setIS_CLOSE(isClose);
				bean.setDESCRIPTION(description);
				bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				bean.setCONTRACT_CLASSIFY(contractClassify);
				bean.setTAX_AMT(Double.valueOf(taxAmt));
				bean.setINV_AMOUNT(Double.valueOf(invAmount));
				bean.setORG_ID(orgId);
				bean.setVERSION(Integer.valueOf(version));
				bean.setAUDIT_STATUS(auditStatus);
				bean.setAUDIT_STATUS_DESC(auditStatusDesc);
				bean.setSYS_AUDIT_STATUS(sysAuditStatus);
				bean.setSYS_AUDIT_STATUS_DESC(sysAuditStatusDesc);
				if(dao.ifExist(contractId)==null){//新增合同
					bean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					bean.setCREATED_BY(CurrentSessionVar.getUserId());
					dao.addContractInfo(bean);	
				}else if("E".equalsIgnoreCase(dao.ifExist(contractId))){//变更
					ApContractHisBean rbean  = dao.getContractHisInfo(contractHisId);
					if(rbean==null){//新增变更
						if(!"1".equalsIgnoreCase(dao.ifChange(contractId))){
							ApContractBean resultBean = dao.getContractInfo(contractId);
							ApContractHisBean hisBean = new ApContractHisBean();
							hisBean.setAP_CONTRACT_HIS_ID(java.util.UUID.randomUUID().toString());
							hisBean.setAP_CONTRACT_ID(contractId);
							hisBean.setAP_CONTRACT_CODE(resultBean.getAP_CONTRACT_CODE());
							hisBean.setAP_CONTRACT_NAME(resultBean.getAP_CONTRACT_NAME());
							hisBean.setAP_CONTRACT_TYPE(resultBean.getAP_CONTRACT_TYPE());
							hisBean.setLEDGER_ID(resultBean.getLEDGER_ID());
							hisBean.setSTART_DATE(resultBean.getSTART_DATE());
							hisBean.setEND_DATE(resultBean.getEND_DATE());
							hisBean.setVENDOR_ID(resultBean.getVENDOR_ID());
							hisBean.setAMOUNT(resultBean.getAMOUNT());
							hisBean.setDEPT_ID(resultBean.getDEPT_ID());
							hisBean.setPROJECT_ID(resultBean.getPROJECT_ID());
							hisBean.setCONTRACT_USER(resultBean.getCONTRACT_USER());
							hisBean.setIS_CLOSE(resultBean.getIS_CLOSE());
							hisBean.setDESCRIPTION(resultBean.getDESCRIPTION());
							hisBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
							hisBean.setCREATED_BY(CurrentSessionVar.getUserId());
							hisBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
							hisBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
							hisBean.setCONTRACT_CLASSIFY(resultBean.getCONTRACT_CLASSIFY());
							hisBean.setTAX_AMT(resultBean.getTAX_AMT());
							hisBean.setINV_AMOUNT(resultBean.getINV_AMOUNT());
							hisBean.setORG_ID(resultBean.getORG_ID());
							hisBean.setVERSION(0);
							hisBean.setAUDIT_STATUS("E");
							hisBean.setAUDIT_STATUS_DESC(XipUtil.getMessage(lang, "XC_AP_CONTRACT_AUDIT_SUCCESS", null));
							hisBean.setSYS_AUDIT_STATUS("E");
							hisBean.setSYS_AUDIT_STATUS_DESC(XipUtil.getMessage(lang, "XC_AP_CONTRACT_AUDIT_SUCCESS", null));
							dao.addContractHis(hisBean);
						}
						ApContractHisBean hisBean = new ApContractHisBean();
						hisBean.setAP_CONTRACT_HIS_ID(contractHisId);
						hisBean.setAP_CONTRACT_ID(contractId);
						hisBean.setAP_CONTRACT_CODE(contractCode);
						hisBean.setAP_CONTRACT_NAME(contractName);
						hisBean.setAP_CONTRACT_TYPE(contractType);
						hisBean.setLEDGER_ID(ledgerId);
						hisBean.setSTART_DATE(sdf.parse(startDate));
						hisBean.setEND_DATE(sdf.parse(endDate));
						hisBean.setVENDOR_ID(vendorId);
						hisBean.setAMOUNT(Double.parseDouble(amount));
						hisBean.setDEPT_ID(deptId);
						hisBean.setPROJECT_ID(projectId);
						hisBean.setCONTRACT_USER(user);
						hisBean.setIS_CLOSE(isClose);
						hisBean.setDESCRIPTION(description);
						hisBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						hisBean.setCREATED_BY(CurrentSessionVar.getUserId());
						hisBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						hisBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						hisBean.setCONTRACT_CLASSIFY(contractClassify);
						hisBean.setTAX_AMT(Double.valueOf(taxAmt));
						hisBean.setINV_AMOUNT(Double.valueOf(invAmount));
						hisBean.setORG_ID(orgId);
						hisBean.setVERSION(Integer.valueOf(version));
						hisBean.setAUDIT_STATUS(auditStatus);
						hisBean.setAUDIT_STATUS_DESC(auditStatusDesc);
						hisBean.setSYS_AUDIT_STATUS(sysAuditStatus);
						hisBean.setSYS_AUDIT_STATUS_DESC(sysAuditStatusDesc);
						dao.addContractHis(hisBean);
						
					}else{//修改变更
						ApContractHisBean editBean = new ApContractHisBean();
						editBean.setAP_CONTRACT_HIS_ID(contractHisId);
						editBean.setAP_CONTRACT_ID(contractId);
						editBean.setAP_CONTRACT_CODE(contractCode);
						editBean.setAP_CONTRACT_NAME(contractName);
						editBean.setAP_CONTRACT_TYPE(contractType);
						editBean.setLEDGER_ID(ledgerId);
						editBean.setSTART_DATE(sdf.parse(startDate));
						editBean.setEND_DATE(sdf.parse(endDate));
						editBean.setVENDOR_ID(vendorId);
						editBean.setAMOUNT(Double.parseDouble(amount));
						editBean.setDEPT_ID(deptId);
						editBean.setPROJECT_ID(projectId);
						editBean.setCONTRACT_USER(user);
						editBean.setIS_CLOSE(isClose);
						editBean.setDESCRIPTION(description);
						editBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						editBean.setCREATED_BY(CurrentSessionVar.getUserId());
						editBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						editBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						editBean.setCONTRACT_CLASSIFY(contractClassify);
						editBean.setTAX_AMT(Double.valueOf(taxAmt));
						editBean.setINV_AMOUNT(Double.valueOf(invAmount));
						editBean.setORG_ID(orgId);
						editBean.setVERSION(Integer.valueOf(version));
						dao.editContractHisInfo(editBean);//修改合同历史表
					}
					
				}else if("A".equalsIgnoreCase(dao.ifExist(contractId))){//修改合同
					dao.editContractInfo(bean);
				}
				joMsg.put("flag","0");
			}else{
				joMsg.put("flag", "1");
				joMsg.put("msg", XipUtil.getMessage(lang, "XC_AP_CONTRACT_SAVE_CODE_ERROR", null));
			}
		} catch (Exception e) {
			joMsg.put("flag", "1");
			joMsg.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return joMsg;
	}
	/**
	 * @Title:delContractInfo
	 * @Description:删除合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	public JSONObject delContractInfo(String idArray,String lang) throws Exception{
		JSONObject joMsg = new JSONObject();
		try {
			JSONArray joArray = new JSONArray(idArray);
			List<String> ids = new ArrayList<String>();
			String m = "";
			for (int i = 0; i < joArray.length(); i++) {
				JSONObject ob = joArray.getJSONObject(i);
				ids.add(ob.getString("id"));
				if(Integer.valueOf(dao.checkExistDel(ob.getString("id")))>0){
					m = "no";
				}
			}
			if(!"no".equals(m)){
				dao.delContractInfo(ids);
				joMsg.put("flag", "0");
			}else{
				joMsg.put("flag", "1");
				joMsg.put("msg", XipUtil.getMessage(lang, "XC_AP_CONTRACT_DELETE_ISUSE_ERROR", null));
			}
		} catch (Exception e) {
			joMsg.put("flag", "1");
			joMsg.put("msg", e.getMessage());
			log.error(e.getMessage());
		}
		return joMsg;
		
	}
	/**
	 * @Title:closeContract
	 * @Description:关闭合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	public JSONObject closeContract(String idArray,String lang) throws Exception{
		JSONObject joMsg = new JSONObject();
		try {
			JSONArray joArray = new JSONArray(idArray);
			List<String> ids = new ArrayList<String>();
			String m = "";
			for (int i = 0; i < joArray.length(); i++) {
				JSONObject ob = joArray.getJSONObject(i);
				ids.add(ob.getString("id"));
				if(Integer.valueOf(dao.checkExistDel(ob.getString("id")))>0){
					m = "no";
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ids", ids);
			map.put("LAST_UPDATE_DATE", CurrentUserUtil.getCurrentDate());
			map.put("LAST_UPDATED_BY", CurrentSessionVar.getUserId());
			if(!"no".equals(m)){
				dao.closeContract(map);
				joMsg.put("flag", "0");
			}else{
				joMsg.put("flag", "1");
				joMsg.put("msg", XipUtil.getMessage(lang, "XC_AP_CONTRACT_CLOSE_ISUSE_ERROR", null));
			}
		} catch (Exception e) {
			joMsg.put("flag", "1");
			joMsg.put("msg", e.getMessage());
			log.error(e.getMessage());
		}
		return joMsg;

	}
	/**
	 * @Title:openContract
	 * @Description:打开合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	public JSONObject openContract(String idArray,String lang) throws Exception{
		JSONObject joMsg = new JSONObject();
		try {
			JSONArray joArray = new JSONArray(idArray);
			List<String> ids = new ArrayList<String>();
			String m = "";
			for (int i = 0; i < joArray.length(); i++) {
				JSONObject ob = joArray.getJSONObject(i);
				ids.add(ob.getString("id"));
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ids", ids);
			map.put("LAST_UPDATE_DATE", CurrentUserUtil.getCurrentDate());
			map.put("LAST_UPDATED_BY", CurrentSessionVar.getUserId());
			dao.openContract(map);
			joMsg.put("flag", "0");
		} catch (Exception e) {
			joMsg.put("flag", "1");
			joMsg.put("msg", e.getMessage());
			log.error(e.getMessage());
		}
		return joMsg;

	}
}

