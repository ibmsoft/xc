package com.xzsoft.xc.ar.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ar.action.ArContractAction;
import com.xzsoft.xc.ar.dao.ArContractBaseDao;
import com.xzsoft.xc.ar.modal.ArContractBean;
import com.xzsoft.xc.ar.service.ArContractBaseService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/** 
 * @className
 * @Description 
 * @author 韦才文
 * @version 创建时间：2016年7月7日 下午4:24:06 
 */
@Service("arContractBaseService")
public class ArContractBaseServiceImpl implements ArContractBaseService {
	@Resource
	ArContractBaseDao dao;
	//日志记录器
	private static Logger log = Logger.getLogger(ArContractAction.class.getName());
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * 
	  * <p>Title saveArContractInfo</p>
	  * <p>Description 合同信息处理</p>
	  * @param info
	  * @param opType
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArContractBaseService#saveArContractInfo(java.lang.String, java.lang.String)
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject saveArContractInfo(String info, String opType) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONObject json = new JSONObject(info);
			String arContractId = json.getString("AR_CONTRACT_ID");
			String arContractHisId = json.getString("AR_CONTRACT_HIS_ID");
			String arContractCode = json.getString("AR_CONTRACT_CODE");
			String arContractName = json.getString("AR_CONTRACT_NAME");
			String arContractType = json.getString("AR_CONTRACT_TYPE");
			String ledgerId = json.getString("LEDGER_ID");
			String amount = json.getString("AMOUNT");
			String customerId = json.getString("CUSTOMER_ID");
			String deptId = json.getString("DEPT_ID");
			String projectId = json.getString("PROJECT_ID");
			String contractUser = json.getString("CONTRACT_USER");
			String description = json.getString("DESCRIPTION");
			String startDate = json.getString("START_DATE");
			String endDate = json.getString("END_DATE");
			String orgId = json.getString("ORG_ID");
			String taxAmt = json.getString("TAX_AMT");
			String version = json.getString("VERSION");
			String invAmount = json.getString("INV_AMOUNT");
			String arContractTypeId = json.getString("AR_CONTRACT_TYPE_ID");
			
			ArContractBean bean =  new ArContractBean();
			bean.setAR_CONTRACT_ID(arContractId);
			bean.setAR_CONTRACT_CODE(arContractCode);
			bean.setAR_CONTRACT_NAME(arContractName);
			bean.setAR_CONTRACT_TYPE(arContractType);
			bean.setLEDGER_ID(ledgerId);
			bean.setAMOUNT(Double.parseDouble(amount));
			bean.setTAX_AMT(Double.parseDouble(taxAmt));
			bean.setINV_AMOUNT(Double.parseDouble(invAmount));
			bean.setCUSTOMER_ID(customerId);
			bean.setDEPT_ID(deptId);
			bean.setPROJECT_ID(projectId);
			bean.setCONTRACT_USER(contractUser);
			bean.setDESCRIPTION(description);
			bean.setSTART_DATE(sdf.parse(startDate));
			bean.setEND_DATE(sdf.parse(endDate));
			bean.setCREATED_BY(CurrentSessionVar.getUserId());
			bean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
			bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			bean.setAR_CONTRACT_TYPE_ID(arContractTypeId);
			bean.setORG_ID(orgId);
			bean.setTAX_AMT(Double.parseDouble(taxAmt));
			bean.setINV_AMOUNT(Double.parseDouble(invAmount));
			bean.setAUDIT_STATUS("A");
			bean.setAUDIT_STATUS_DESC("起草");
			bean.setSYS_AUDIT_STATUS("A");
			bean.setSYS_AUDIT_STATUS_DESC("草稿");
			bean.setVERSION(Integer.parseInt(version));
			//新增合同处理
			if("add".equals(opType))
				dao.addContractInfo(bean);//调用service层的方法
			//修改合同处理
			if("edit".equals(opType)){
				bean.setSYS_AUDIT_STATUS(json.getString("SYS_AUDIT_STATUS"));
				bean.setSYS_AUDIT_STATUS_DESC(json.getString("SYS_AUDIT_STATUS_DESC"));
				dao.editContractInfo(bean);//调用service层的方法
			}
			//合同变更处理
			if("change".equals(opType)){
				bean.setAR_CONTRACT_HIS_ID(arContractHisId);
				//查找当前合同是否变更对应的信息
				ArContractBean arContractBean = dao.getArContractHisByHisId(arContractHisId);
				if(arContractBean != null){
					bean.setVERSION(arContractBean.getVERSION());
					dao.updateArContractHis(bean);//调用service层更新合同历史表的方法
				}
				else{
					List<ArContractBean> arContractList = dao.getArContractHis(arContractId);
					int hisVersion = 1;
					if(arContractList.size() > 0 && arContractList != null){
						hisVersion = arContractList.get(0).getVERSION();
						hisVersion = hisVersion + 1;
					}
					if(hisVersion == 1){
						ArContractBean oldContract = dao.getArContractById(arContractId);
						oldContract.setAR_CONTRACT_HIS_ID(UUID.randomUUID().toString());
						oldContract.setVERSION(0);
						dao.insertArContractHis(oldContract);//调用service层新增合同历史表的方法
					}
					bean.setVERSION(hisVersion);
					dao.insertArContractHis(bean);//调用service层新增合同历史表的方法
				}
			}
			jo.put("flag", "0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			throw e;
		}
		return jo;
	}
	/*
	 * 
	  * <p>Title delContractInfo</p>
	  * <p>Description  删除合同</p>
	  * @param idsArray
	  * @param lang
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArContractBaseService#delContractInfo(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject delContractInfo(String idsArray,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			
			List<String> ids = new ArrayList<String>();
			JSONArray array = new JSONArray(idsArray);
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				ids.add(ob.getString("id"));
			}
			if(dao.checExistDel(ids)==null){
				dao.delContractInfo(ids);//调用service的方法
				jo.put("flag", "0");
			}else if("1".endsWith(dao.checExistDel(ids))){
				jo.put("flag", "1");
				jo.put("msg", XipUtil.getMessage(lang, "XC_AR_CONTRACT_DELETE", null));
			}
		
			
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			
		}
		return jo;
	}
}
