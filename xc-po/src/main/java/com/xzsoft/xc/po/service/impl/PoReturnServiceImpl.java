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
import com.xzsoft.xc.po.dao.PoReturnDao;
import com.xzsoft.xc.po.modal.PoReturnHBean;
import com.xzsoft.xc.po.modal.PoReturnLBean;
import com.xzsoft.xc.po.service.PoReturnService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/** 
 * @ClassName: PoReturnServiceImpl
 * @Description: 退货单Service实现类 
 * @author weicw
 * @date 2016年12月28日 上午11:17:04 
 * 
 */
@Service("poReturnService")
public class PoReturnServiceImpl implements PoReturnService {
	@Resource
	PoReturnDao dao;
	//日志记录
	Logger log = Logger.getLogger(PoReturnServiceImpl.class.getName());
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	RuleService ruleService;
	/**
	 * @Title:saveReturnData
	 * @Description:保存退货单
	 * 参数格式:
	 * @param  java.lang.String(主信息) java.lang.String(行信息) java.lang.String(删除的主ID) java.lang.String(行号)
	 * @return 
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject saveReturnData(String formStr,String gridStr,String deleteIds,String lineNums) throws Exception {
		JSONObject jo= new JSONObject();
		try {
			JSONObject formJo = new JSONObject(formStr);//主信息
			JSONArray gridArray = new JSONArray(gridStr);//行信息
			JSONArray delArray = new JSONArray(deleteIds);//删除的行信息ID
			JSONArray  lineArray = new JSONArray(lineNums);//行号
			String returnHId = formJo.getString("RETURN_H_ID");//主信息ID
			String returnHCode = formJo.getString("RETURN_H_CODE");//主信息编码
			String returnHName = formJo.getString("RETURN_H_NAME");//主信息说明
			String docCatCode = formJo.getString("PO_DOC_CAT_CODE");//单据类型
			//String entryHId = formJo.getString("ENTRY_H_ID");//入库单id
			String orderHId = formJo.getString("ORDER_H_ID");//采购订单id
			String contractId = formJo.getString("AP_CONTRACT_ID");//合同id
			String vendorId = formJo.getString("VENDOR_ID");//供应商id
			String deptId = formJo.getString("DEPT_ID");//部门id
			//String arrivalDate = formJo.getString("ARRIVAL_DATE");//到货时间
			String documentDate = formJo.getString("DOCUMENT_DATE");//单据日期
			String purchasePerson = formJo.getString("PURCHASE_PERSON");//业务员
			String warehouseId = formJo.getString("WAREHOUSE_ID");//仓库
			//String amount = formJo.getString("AMOUNT");//金额
			//String invAmount = formJo.getString("INV_AMOUNT");//含税金额
			String orgId = formJo.getString("ORG_ID");//组织
			String ledgerId = formJo.getString("LEDGER_ID");//账簿
			String insCode = formJo.getString("INS_CODE");//实例编码
			String auditStatus = formJo.getString("AUDIT_STATUS");//工作流状态
			String auditStatusDesc = formJo.getString("AUDIT_STATUS_DESC");//工作流状态描述
			String sysAuditStatus = formJo.getString("SYS_AUDIT_STATUS");//业务状态
			String sysAuditStatusDesc = formJo.getString("SYS_AUDIT_STATUS_DESC");//业务状态描述
			if(dao.ifExistH(returnHId)==null){//新增
				PoReturnHBean hbean = new PoReturnHBean();
				hbean.setRETURN_H_ID(returnHId);
				String ruleCode = "PO_RETURN";
				String year = sdf.format(CurrentUserUtil.getCurrentDate()).substring(0,4);	//年
				String month = sdf.format(CurrentUserUtil.getCurrentDate()).substring(5,7);	//月
				//得到单据编码
				returnHCode = ruleService.getNextSerialNum(ruleCode, ledgerId, "", year, month, CurrentSessionVar.getUserId());
				if(returnHCode!=null||returnHCode!=""){
					hbean.setRETURN_H_CODE(returnHCode);
				}
				hbean.setRETURN_H_NAME(returnHName);
				hbean.setPO_DOC_CAT_CODE(docCatCode);
				//hbean.setENTRY_H_ID(entryHId);
				hbean.setORDER_H_ID(orderHId);
				hbean.setCONTRACT_ID(contractId);
				hbean.setVENDOR_ID(vendorId);
				hbean.setDEPT_ID(deptId);
				//hbean.setARRIVAL_DATE(sdfs.parse(arrivalDate));
				hbean.setDOCUMENT_DATE(sdf.parse(documentDate));
				hbean.setPURCHASE_PERSON(purchasePerson);
				hbean.setWAREHOUSE_ID(warehouseId);
				//hbean.setAMOUNT(Double.valueOf(amount));
				//hbean.setINV_AMOUNT(Double.valueOf(invAmount));
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
				dao.addReturnHData(hbean);//新增主信息
				for (int i = 0; i < gridArray.length(); i++) {
					JSONObject gridJo = gridArray.getJSONObject(i);
					if(dao.ifExistL(gridJo.getString("RETURN_L_ID"))==null){//新增行信息
						PoReturnLBean lbean = new PoReturnLBean();
						lbean.setRETURN_L_ID(gridJo.getString("RETURN_L_ID"));
						lbean.setRETURN_H_ID(returnHId);
						lbean.setRETURN_L_NUM(lineArray.getInt(i));
						lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
						lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
						lbean.setENTRY_L_ID(gridJo.getString("ENTRY_L_ID"));
						lbean.setDIM_ID(gridJo.getString("DIM_ID"));
						//lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
						//lbean.setARRIVAL_QTY(Double.valueOf(gridJo.getString("ARRIVAL_QTY")));
						lbean.setRETURN_QTY(Double.valueOf(gridJo.getString("RETURN_QTY")));
						lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
						lbean.setORG_ID(gridJo.getString("ORG_ID"));
						lbean.setLEDGER_ID(gridJo.getString("LEDGER_ID"));
						lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setCREATED_BY(CurrentSessionVar.getUserId());
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.addReturnLData(lbean);//新增行信息
					}
					
				}
			}else{//修改
				PoReturnHBean hbean = new PoReturnHBean();
				hbean.setRETURN_H_ID(returnHId);
				hbean.setRETURN_H_NAME(returnHName);
				//hbean.setENTRY_H_ID(entryHId);
				hbean.setORDER_H_ID(orderHId);
				hbean.setCONTRACT_ID(contractId);
				hbean.setVENDOR_ID(vendorId);
				hbean.setDEPT_ID(deptId);
				//hbean.setARRIVAL_DATE(sdfs.parse(arrivalDate));
				hbean.setDOCUMENT_DATE(sdf.parse(documentDate));
				hbean.setPURCHASE_PERSON(purchasePerson);
				hbean.setWAREHOUSE_ID(warehouseId);
				//hbean.setAMOUNT(Double.valueOf(amount));
				//hbean.setINV_AMOUNT(Double.valueOf(invAmount));
				hbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				hbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.editReturnHData(hbean);//修改主信息
				List<String> delList = new ArrayList<String>();//删除的行ID
				for (int j = 0; j < delArray.length(); j++) {
					delList.add(delArray.getJSONObject(j).getString("id"));
				}
				if(delList.size()!=0){
					dao.delReturnLData(delList);//删除行信息
				}
				for (int i = 0; i < gridArray.length(); i++) {
					JSONObject gridJo = gridArray.getJSONObject(i);
					if(dao.ifExistL(gridJo.getString("RETURN_L_ID"))==null){//新增行信息
						PoReturnLBean lbean = new PoReturnLBean();
						lbean.setRETURN_L_ID(gridJo.getString("RETURN_L_ID"));
						lbean.setRETURN_H_ID(returnHId);
						lbean.setRETURN_L_NUM(lineArray.getInt(i));
						lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
						lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
						lbean.setENTRY_L_ID(gridJo.getString("ENTRY_L_ID"));
						lbean.setDIM_ID(gridJo.getString("DIM_ID"));
						//lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
						//lbean.setARRIVAL_QTY(Double.valueOf(gridJo.getString("ARRIVAL_QTY")));
						lbean.setRETURN_QTY(Double.valueOf(gridJo.getString("RETURN_QTY")));
						lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
						lbean.setORG_ID(gridJo.getString("ORG_ID"));
						lbean.setLEDGER_ID(gridJo.getString("LEDGER_ID"));
						lbean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setCREATED_BY(CurrentSessionVar.getUserId());
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.addReturnLData(lbean);//新增行信息
					}else{//修改行信息
						PoReturnLBean lbean = new PoReturnLBean();
						lbean.setRETURN_L_ID(gridJo.getString("RETURN_L_ID"));
						lbean.setRETURN_H_ID(returnHId);
						lbean.setRETURN_L_NUM(lineArray.getInt(i));
						lbean.setMATERIAL_ID(gridJo.getString("MATERIAL_ID"));
						lbean.setORDER_L_ID(gridJo.getString("ORDER_L_ID"));
						lbean.setENTRY_L_ID(gridJo.getString("ENTRY_L_ID"));
						//lbean.setQTY(Double.valueOf(gridJo.getString("QTY")));
						//lbean.setARRIVAL_QTY(Double.valueOf(gridJo.getString("ARRIVAL_QTY")));
						lbean.setRETURN_QTY(Double.valueOf(gridJo.getString("RETURN_QTY")));
						lbean.setTAX(Double.valueOf(gridJo.getString("TAX")));
						lbean.setPRICE(Double.valueOf(gridJo.getString("PRICE")));
						lbean.setINV_PRICE(Double.valueOf(gridJo.getString("INV_PRICE")));
						lbean.setAMOUNT(Double.valueOf(gridJo.getString("AMOUNT")));
						lbean.setINV_AMOUNT(Double.valueOf(gridJo.getString("INV_AMOUNT")));
						lbean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						lbean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						dao.editReturnLData(lbean);//修改行信息
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
	/**
	 * @Title:delReturnData
	 * @Description:删除退货单
	 * 参数格式:
	 * @param  java.lang.String(主信息ID) 
	 * @return 
	 * @throws 
	 */
	@Override 
	public JSONObject delReturnData(String ids) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray delArray = new JSONArray(ids);
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < delArray.length(); i++) {
				list.add(delArray.getString(i));
			}
			dao.delReturnLDataByHId(list);//删除行信息
			dao.delReturnHData(list);//删除主信息
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
