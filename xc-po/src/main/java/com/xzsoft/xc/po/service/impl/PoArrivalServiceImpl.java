package com.xzsoft.xc.po.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.xzsoft.xc.po.dao.PoArrivalDao;
import com.xzsoft.xc.po.modal.PoArrivalHBean;
import com.xzsoft.xc.po.modal.PoArrivalLBean;
import com.xzsoft.xc.po.service.PoArrivalService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/** 
 * @ClassName: PoArrivalServiceImpl
 * @Description: 到货单Service层接口实现类
 * @author weicw
 * @date 2016年12月22日 下午1:57:53 
 * 
 */
@Service("poArrivalService")
public class PoArrivalServiceImpl implements PoArrivalService {
	@Resource
	PoArrivalDao dao;
	@Resource
	RuleService ruleService;
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//日期格式化
	SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//日志记录
	Logger log = Logger.getLogger(PoArrivalServiceImpl.class.getName());
	/**
	 * 
	  * @Title saveArrivalInfo 方法名
	  * @Description 保存到货单
	  * @param java.lang.String(主信息) java.lang.String(行信息) java.lang.String(删除的行ID) java.lang.String(行号)
	  * @return org.codehaus.jettison.json.JSONObject
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject saveArrivalInfo(String formStr, String gridStr,
			String deleteIds, String lineNums) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONObject formJo = new JSONObject(formStr);//主信息
			JSONArray gridArray = new JSONArray(gridStr);//行信息
			JSONArray deleteArray = new JSONArray(deleteIds);//删除的行ID
			JSONArray lineArray = new JSONArray(lineNums);//行号
			String arrivalHId = formJo.getString("ARRIVAL_H_ID");//到货单主ID
			String arrivalHCode = formJo.getString("ARRIVAL_H_CODE");//到货单编号
			String arrivalHName = formJo.getString("ARRIVAL_H_NAME");//到货单说明
			String docCatCode = formJo.getString("PO_DOC_CAT_CODE");//单据类型
			String orderHId = formJo.getString("ORDER_H_ID");//采购订单id
			String vendorId = formJo.getString("VENDOR_ID");//到货供应商id
			String deptId = formJo.getString("DEPT_ID");//成本中心id
			String projectId = formJo.getString("PROJECT_ID");//项目id
			String contractId = formJo.getString("CONTRACT_ID");//合同id
			String arrivalDate = formJo.getString("ARRIVAL_DATE");//到货时间
			String documentDate = formJo.getString("DOCUMENT_DATE");//单据日期
			String purchasePerson = formJo.getString("PURCHASE_PERSON");//业务员
			String warehouseId = formJo.getString("WAREHOUSE_ID");//仓库id
			String amount = formJo.getString("AMOUNT");//不含税金额
			String invAmount = formJo.getString("INV_AMOUNT");//含税金额
			String orgId = formJo.getString("ORG_ID");//组织id
			String ledgerId = formJo.getString("LEDGER_ID");//账簿id
			String insCode = formJo.getString("INS_CODE");//实例编码
			String auditStatus = formJo.getString("AUDIT_STATUS");//审批状态
			String auditStatusDesc = formJo.getString("AUDIT_STATUS_DESC");//审批状态描述
			String sysAuditStatus = formJo.getString("SYS_AUDIT_STATUS");//业务状态
			String sysAuditStatusDesc = formJo.getString("SYS_AUDIT_STATUS_DESC");//业务状态描述
			if(dao.ifExistH(arrivalHId)==null){//新增
				PoArrivalHBean hbean = new PoArrivalHBean();
				hbean.setARRIVAL_H_ID(arrivalHId);
				String ruleCode = "PO_ARRIVAL";
				String year = sdf.format(CurrentUserUtil.getCurrentDate()).substring(0,4);	//年
				String month = sdf.format(CurrentUserUtil.getCurrentDate()).substring(5,7);	//月
				//得到单据编码
				arrivalHCode = ruleService.getNextSerialNum(ruleCode, ledgerId, "", year, month, CurrentSessionVar.getUserId());
				if(arrivalHCode!=null||arrivalHCode!=""){
					hbean.setARRIVAL_H_CODE(arrivalHCode);
				}
				hbean.setARRIVAL_H_NAME(arrivalHName);
				hbean.setPO_DOC_CAT_CODE(docCatCode);
				hbean.setORDER_H_ID(orderHId);
				hbean.setVENDOR_ID(vendorId);
				hbean.setDEPT_ID(deptId);
				hbean.setPROJECT_ID(projectId);
				hbean.setCONTRACT_ID(contractId);
				hbean.setARRIVAL_DATE(sdfs.parse(arrivalDate));
				hbean.setDOCUMENT_DATE(sdf.parse(documentDate));
				hbean.setPURCHASE_PERSON(purchasePerson);
				hbean.setWAREHOUSE_ID(warehouseId);
				hbean.setAMOUNT(Double.valueOf(amount));
				hbean.setINV_AMOUNT(Double.valueOf(invAmount));
				hbean.setORG_ID(orgId);
				hbean.setLEDGER_ID(ledgerId);
				hbean.setINS_CODE(insCode);
				hbean.setAUDIT_STATUS(auditStatus);
				hbean.setAUDIT_STATUS_DESC(auditStatusDesc);
				hbean.setSYS_AUDIT_STATUS(sysAuditStatus);
				hbean.setSYS_AUDIT_STATUS_DESC(sysAuditStatusDesc);
				hbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				hbean.setCREATED_BY(CurrentSessionVar.getUserId());
				hbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				hbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.addPoArrivalHInfo(hbean);//新增主信息
				for (int i = 0; i < gridArray.length(); i++) {
					JSONObject gridJo = gridArray.getJSONObject(i);
					PoArrivalLBean lbean = new PoArrivalLBean();
					lbean.setARRIVAL_L_ID(gridJo.getString("ARRIVAL_L_ID"));
					lbean.setARRIVAL_H_ID(arrivalHId);
					lbean.setARRIVAL_L_NUM(lineArray.getInt(i));
					lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
					lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
					lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
					lbean.setARRIVAL_QTY(Double.valueOf(gridJo.getString("ARRIVAL_QTY")));
					lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
					lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
					lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
					lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
					lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
					lbean.setORG_ID(orgId);
					lbean.setLEDGER_ID(ledgerId);
					lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					lbean.setCREATED_BY(CurrentSessionVar.getUserId());
					lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addPoArrivalLInfo(lbean);
				}
			}else{//修改
				PoArrivalHBean hbean = new PoArrivalHBean();
				hbean.setARRIVAL_H_ID(arrivalHId);
				hbean.setARRIVAL_H_NAME(arrivalHName);
				hbean.setORDER_H_ID(orderHId);
				hbean.setVENDOR_ID(vendorId);
				hbean.setDEPT_ID(deptId);
				hbean.setPROJECT_ID(projectId);
				hbean.setCONTRACT_ID(contractId);
				hbean.setARRIVAL_DATE(sdfs.parse(arrivalDate));
				hbean.setDOCUMENT_DATE(sdf.parse(documentDate));
				hbean.setPURCHASE_PERSON(purchasePerson);
				hbean.setWAREHOUSE_ID(warehouseId);
				hbean.setAMOUNT(Double.valueOf(amount));
				hbean.setINV_AMOUNT(Double.valueOf(invAmount));
				hbean.setORG_ID(orgId);
				hbean.setLEDGER_ID(ledgerId);
				hbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				hbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.editPoArrivalHInfo(hbean);//修改主信息
				
				List<String> delList = new ArrayList<String>();//删除的行id List
				for (int i = 0; i < deleteArray.length(); i++) {
					JSONObject delJo = deleteArray.getJSONObject(i);
					delList.add(delJo.getString("id"));
				}
				if(delList.size()!=0){
					dao.deletePoArrivalLInfo(delList);
				}
				for (int i = 0; i < gridArray.length(); i++) {
					JSONObject gridJo = gridArray.getJSONObject(i);
					if(dao.ifExistL(gridJo.getString("ARRIVAL_L_ID"))==null){//新增行
						PoArrivalLBean lbean = new PoArrivalLBean();
						lbean.setARRIVAL_L_ID(gridJo.getString("ARRIVAL_L_ID"));
						lbean.setARRIVAL_H_ID(arrivalHId);
						lbean.setARRIVAL_L_NUM(lineArray.getInt(i));
						lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
						lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
						lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
						lbean.setARRIVAL_QTY(Double.valueOf(gridJo.getString("ARRIVAL_QTY")));
						lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
						lbean.setORG_ID(orgId);
						lbean.setLEDGER_ID(ledgerId);
						lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setCREATED_BY(CurrentSessionVar.getUserId());
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.addPoArrivalLInfo(lbean);
					}else{//修改行
						PoArrivalLBean lbean = new PoArrivalLBean();
						lbean.setARRIVAL_L_ID(gridJo.getString("ARRIVAL_L_ID"));
						lbean.setARRIVAL_L_NUM(lineArray.getInt(i));
						lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
						lbean.setARRIVAL_QTY(Double.valueOf(gridJo.getString("ARRIVAL_QTY")));
						lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
						lbean.setORG_ID(orgId);
						lbean.setLEDGER_ID(ledgerId);
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.editPoArrivalLInfo(lbean);
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
	 * 
	  * @Title deleteArrivalInfo 方法名
	  * @Description 删除到货单
	  * @param java.lang.String(删除的主ID)
	  * @return org.codehaus.jettison.json.JSONObject
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject deleteArrivalInfo(String deleteIds) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(deleteIds);
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < idArray.length(); i++) {
				JSONObject idJo = idArray.getJSONObject(i);
				idList.add(idJo.getString("id"));
			}
			if(idList.size()!=0){
				dao.deletePoArrivalLInfoByHId(idList);//删除行信息
				dao.deletePoArrivalHInfo(idList);//删除主信息	
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
