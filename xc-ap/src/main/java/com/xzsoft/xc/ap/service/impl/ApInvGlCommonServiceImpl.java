package com.xzsoft.xc.ap.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.xzsoft.xc.ap.dao.ApInvGlCommonDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.ap.modal.ApInvGlLBean;
import com.xzsoft.xc.ap.modal.ApPayHBean;
import com.xzsoft.xc.ap.modal.ApPayLBean;
import com.xzsoft.xc.ap.service.ApInvGlCommonService;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/** 
 * @className
 * @Description 
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午12:54:04 
 */
@Service("apInvGlHService")
public class ApInvGlCommonServiceImpl implements ApInvGlCommonService {
	@Resource
	ApInvGlCommonDao dao;
	@Resource
	XCAPCommonDao xcAPCommondao;
	@Resource
	RuleService ruleService;
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//日志记录器
	private static Logger log = Logger.getLogger(ApInvGlCommonServiceImpl.class.getName());
	/**
	 * @Title glCount
	 * @Description: 应付期初总账取数
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject glCount(String invGlH) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String lang = XCUtil.getLang();//语言值
			JSONArray array = new JSONArray(invGlH);
			for(int i=0;i<array.length();i++){
				JSONObject json = array.getJSONObject(i);
				String ledgerId = json.getString("LEDGER_ID");//账簿ID
				String periodCode = json.getString("PERIOD_CODE");//会计期
				String glDate = periodCode + "-01";//业务日期取会计期的前一天
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(glDate));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				String accId = json.getString("ACC_ID");//应付科目
				String ccId = json.getString("CCID");//应付科目组合
				String currencyCode = json.getString("CURRENCY_CODE");//币种
				HashMap<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("LEDGER_ID", ledgerId);
				paramMap.put("PERIOD_CODE", periodCode);
				paramMap.put("ACC_ID", accId);
				paramMap.put("CCID", ccId);
				paramMap.put("CURRENCY_CODE", currencyCode);
				HashMap<String, Object> map  = dao.getQcDatas(paramMap);
				//添加应收单主表信息
				String invGlHId = java.util.UUID.randomUUID().toString();//应收单主表
				String docCatCode = "YFD";//单据类型
				String customerId = map.get("VENDOR_ID").toString();//供应商ID
				String projectId = map.get("PROJECT_ID").toString();//项目ID
				String deptId = map.get("DEPT_ID").toString();//部门ID
				Double amount = Double.valueOf(map.get("AMOUNT").toString());//金额
				Double invAmount = Double.valueOf(map.get("INV_AMOUNT").toString());//含税金额
				Double cancelAmt = Double.valueOf(map.get("CANCEL_AMT").toString());//核销金额
				Double noPayAmt = Double.valueOf(map.get("NO_PAY_AMT").toString());//未支付金额
				Double adjAmt = Double.valueOf(map.get("ADJ_AMT").toString());//调整金额
				String init = "0";
				String source = map.get("SOURCE").toString();//来源
				String desription = XipUtil.getMessage(lang, "XC_AP_INV_DESCRIPTION", null);
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("AP_CCID_DEBT",ccId);
				String id = dao.ifGlCount(map1);
				if(id==null||"".equals(id)){//如果不存在同科目的期初，添加数据
					ApInvGlHBean invGlHBean = new ApInvGlHBean();
					invGlHBean.setAP_INV_GL_H_ID(invGlHId);
					invGlHBean.setAP_DOC_CAT_CODE(docCatCode);
					invGlHBean.setLEDGER_ID(ledgerId);
					invGlHBean.setGL_DATE(sdf.parse(sdf.format(calendar.getTime())));
					invGlHBean.setVENDOR_ID(customerId);
					invGlHBean.setPROJECT_ID(projectId);
					invGlHBean.setDEPT_ID(deptId);
					invGlHBean.setAMOUNT(amount);
					invGlHBean.setINV_AMOUNT(invAmount);
					invGlHBean.setCANCEL_AMT(cancelAmt);
					invGlHBean.setNO_PAY_AMT(noPayAmt);
					invGlHBean.setADJ_AMT(adjAmt);
					invGlHBean.setINIT(init);
					invGlHBean.setSOURCE(source);
					invGlHBean.setDESCRIPTION(desription);
					invGlHBean.setAP_ACC_ID_DEBT(accId);
					invGlHBean.setAP_CCID_DEBT(ccId);
					invGlHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					invGlHBean.setCREATED_BY(CurrentSessionVar.getUserId());
					invGlHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					invGlHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addInvGlHInfo(invGlHBean);//调用dao层的方法
					//添加应收单行表信息
					String invGlLId = java.util.UUID.randomUUID().toString();
					String apPurTypeId ="";//销售类型ID
					if(map.get("AP_PUR_TYPE_ID")==null){
						apPurTypeId = null;
					}else{
						apPurTypeId = map.get("AP_PUR_TYPE_ID").toString();
					}
					
					ApInvGlLBean invGlLBean = new ApInvGlLBean();
					invGlLBean.setAP_INV_GL_L_ID(invGlLId);
					invGlLBean.setAP_INV_GL_H_ID(invGlHId);
					invGlLBean.setAP_PUR_TYPE_ID(apPurTypeId);
					invGlLBean.setACC_ID(accId);
					invGlLBean.setCCID(ccId);
					invGlLBean.setAMOUNT(amount);
					invGlLBean.setDESCRIPTION(desription);
					invGlLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					invGlLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					invGlLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					invGlLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addInvGlLInfo(invGlLBean);//dao层的方法
					
				}else{
					ApInvGlHBean resultBean = dao.getInvGlHInfo(id);//之前存在的数据
					ApInvGlHBean bean = new ApInvGlHBean();
					bean.setPROJECT_ID(projectId);
					bean.setDEPT_ID(deptId);
					bean.setAMOUNT(amount+resultBean.getAMOUNT());
					bean.setNO_PAY_AMT(amount+resultBean.getAMOUNT());
					bean.setPAID_AMT(0);
					bean.setCANCEL_AMT(0);
					bean.setDESCRIPTION(desription);
					bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					bean.setAP_INV_GL_H_ID(id);
					dao.editInvGlHInfo(bean);//更新主表
					
					ApInvGlLBean lBean = new ApInvGlLBean();
					lBean.setAMOUNT(amount+resultBean.getAMOUNT());
					lBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					lBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					lBean.setAP_INV_GL_H_ID(id);
					dao.updateGlCountInvL(lBean);//更新行表
				}
				jo.put("flag", "0");	
			}
		} catch (Exception e) {
			jo.put("flag", "1");	
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title split
	 * @Description: 应付期初拆分
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject split(String jsonStr) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONObject json = new JSONObject(jsonStr);
			String invGlHId = json.getString("AP_INV_GL_H_ID");//应付单主表ID
			String projectId = json.getString("PROJECT_ID");//项目ID
			String deptId = json.getString("DEPT_ID");//部门I
			String contractId = json.getString("AP_CONTRACT_ID");//合同ID
			String amount = json.getString("AMOUNT");//金额
			String descrption = json.getString("DESCRIPTION");//摘要
			String glDate = json.getString("GL_DATE");//业务日期
			String invGlHCode = json.getString("AP_INV_GL_H_CODE");//应付单编号
			ApInvGlHBean invGlHBean = new ApInvGlHBean();
			invGlHBean.setAP_INV_GL_H_ID(invGlHId);
			invGlHBean.setPROJECT_ID(projectId);
			invGlHBean.setGL_DATE(sdf.parse(glDate));
			invGlHBean.setDEPT_ID(deptId);
			invGlHBean.setAP_CONTRACT_ID(contractId);
			invGlHBean.setAMOUNT(Double.valueOf(amount));
			invGlHBean.setINIT("1");
			invGlHBean.setDESCRIPTION(descrption);
			invGlHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			invGlHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			ApInvGlHBean resultBean = dao.getInvGlHInfo(invGlHId);
			if(resultBean==null){//新增
				
				String invHId = json.getString("AP_INV_H_ID");//发票ID
				String docCatCode = json.getString("AP_DOC_CAT_CODE");//单据类型
				String ledgerId = json.getString("LEDGER_ID");//账簿ID
				String vendorId = json.getString("VENDOR_ID");//供应商ID
				String accId = json.getString("AP_ACC_ID_DEBT");//科目ID
				String ccid = json.getString("AP_CCID_DEBT");//科目组合ID
				invGlHBean.setAP_INV_GL_H_CODE(invGlHCode);
				invGlHBean.setAP_INV_H_ID(invHId);
				invGlHBean.setAP_DOC_CAT_CODE(docCatCode);
				invGlHBean.setLEDGER_ID(ledgerId);
				invGlHBean.setVENDOR_ID(vendorId);
				invGlHBean.setADJ_AMT(0);
				invGlHBean.setAP_ACC_ID_DEBT(accId);
				invGlHBean.setAP_ACC_ID_TAX(ccid);
				invGlHBean.setTAX_AMT(0);
				invGlHBean.setTAX_RATE(0);
				invGlHBean.setINV_AMOUNT(Double.valueOf(amount));
				invGlHBean.setCANCEL_AMT(0);
				invGlHBean.setPAID_AMT(0);
				invGlHBean.setNO_PAY_AMT(Double.valueOf(amount));
				invGlHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				invGlHBean.setCREATED_BY(CurrentSessionVar.getUserId());
				String LEDGER_ID = invGlHBean.getLEDGER_ID();
				String AR_DOC_CAT_CODE = invGlHBean.getAP_DOC_CAT_CODE();
				Date createDate = invGlHBean.getCREATION_DATE();
				String userId = invGlHBean.getCREATED_BY();
				//得到编码规则
				/*String ruleCode = xcAPCommondao.getApCode(LEDGER_ID,AR_DOC_CAT_CODE);
				if(ruleCode == null || "".equals(ruleCode)){
					throw new Exception("该账簿级单据类型没有启用编码规则！");
				}*/
				String ruleCode = "AP_INV_GL";
				String year = sdf.format(createDate).substring(0,4);	//年
				String month = sdf.format(createDate).substring(5,7);	//月
				//得到单据编码
				String apInvGlHCode = ruleService.getNextSerialNum(ruleCode, LEDGER_ID, "", year, month, userId);
				if(apInvGlHCode!=null||apInvGlHCode!=""){
					invGlHBean.setAP_INV_GL_H_CODE(apInvGlHCode);
				}
				dao.addInvGlHInfo(invGlHBean);
				ApInvGlLBean invGlLBean = new ApInvGlLBean();
				invGlLBean.setAP_INV_GL_L_ID(java.util.UUID.randomUUID().toString());
				invGlLBean.setAP_INV_GL_H_ID(invGlHId);
				invGlLBean.setACC_ID(accId);
				invGlLBean.setCCID(ccid);
				invGlLBean.setAMOUNT(Double.valueOf(amount));
				invGlLBean.setDESCRIPTION(descrption);
				invGlLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				invGlLBean.setCREATED_BY(CurrentSessionVar.getUserId());
				invGlLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				invGlLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.addInvGlLInfo(invGlLBean);//dao层的方法
				ApInvTransBean transBean = new ApInvTransBean();
				transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
				transBean.setSOURCE_ID(invGlHId);
				transBean.setSOURCE_DTL_ID(invGlLBean.getAP_INV_GL_L_ID());
				transBean.setAP_INV_GL_H_ID(invGlHId);
				transBean.setAP_CONTRACT_ID(contractId);
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setSOURCE_TAB("XC_AP_INV_GL_H");
				transBean.setAP_DOC_CAT_CODE("YFD");
				transBean.setVENDOR_ID(vendorId);
				transBean.setDR_AMT(Double.valueOf(0));
				transBean.setCR_AMT(Double.valueOf(amount));
				transBean.setCCID(ccid);
				transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setCREATED_BY(CurrentSessionVar.getUserId());
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				transBean.setAP_DOC_CODE(apInvGlHCode);
				transBean.setDESCRIPTION(descrption);
				dao.addInvTrans(transBean);
				jo.put("code", apInvGlHCode);
			}else{//修改
				invGlHBean.setPAID_AMT(0);
				invGlHBean.setNO_PAY_AMT(Double.valueOf(amount));
				invGlHBean.setCANCEL_AMT(0);
				dao.editInvGlHInfo(invGlHBean);//更新主表
				ApInvGlLBean lBean = new ApInvGlLBean();
				lBean.setAMOUNT(Double.valueOf(amount));
				lBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				lBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				lBean.setAP_INV_GL_H_ID(invGlHId);
				dao.updateGlCountInvL(lBean);//更新行表
				ApInvTransBean transBean = new ApInvTransBean();
				transBean.setSOURCE_ID(invGlHId);
				transBean.setAP_CONTRACT_ID(contractId);
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setDR_AMT(Double.valueOf(0));
				transBean.setCR_AMT(Double.valueOf(amount));
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.updateInvtrans(transBean);
				jo.put("code", invGlHCode);
			}
			jo.put("flag","0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
		
	}
	/**
	 * @Title deleteInfo
	 * @Description: 删除应付单
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject deleteInfo(String ids,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(ids);
			List<String> idList = new ArrayList<String>(); //主表ID
			List<String> hids = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				idList.add(ob.getString("id"));
				List<HashMap<String, String>>  reusltList = dao.getInvHId(ob.getString("id"));
				hids.add(ob.getString("id"));
				for(int j=0;j<reusltList.size();j++){
					hids.add(reusltList.get(j).get("AP_INV_GL_H_ID"));
				}
			}
			if(!"1".equals(dao.checkInvIfUse(hids))){
				dao.delInvGlLInfo(hids);//先删行信息，再删主信息
				dao.delInvGlHInfo(idList);
				dao.delInvGlHInfoByInvHId(idList);
				dao.deleteInvTrans(hids);
				jo.put("flag", "0");	
			}else{
				jo.put("flag", "1");
				jo.put("msg", XipUtil.getMessage(lang, "XC_AP_QC_INV_DELETE", null));
			}
			
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title delInvGlHInfo
	 * @Description: 单独删除明细
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject delInvGlHInfo(String ids) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(ids);
			List<String> idList = new ArrayList<String>(); 
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				idList.add(ob.getString("id"));
			}
			dao.delInvGlLInfo(idList);
			dao.delInvGlHInfo(idList);
			dao.deleteInvTrans(idList);
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
	 * @Title glCountYuF
	 * @Description: 预付期初总账取数
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject glCountYuF(String payH) throws Exception {
		JSONObject jo = new JSONObject();
		String lang = XCUtil.getLang();
		try {
			JSONArray array = new JSONArray(payH);
			for(int i=0;i<array.length();i++){
				JSONObject json = array.getJSONObject(i);
				String ledgerId = json.getString("LEDGER_ID");//账簿ID
				String periodCode = json.getString("PERIOD_CODE");//会计期
				String glDate = periodCode + "-01";//业务日期取会计期的前一天
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(glDate));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				String accId = json.getString("ACC_ID");//科目ID
				String ccId = json.getString("CCID");//科目组合ID
				String currencyCode = json.getString("CURRENCY_CODE");//币种
				HashMap<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("LEDGER_ID", ledgerId);
				paramMap.put("PERIOD_CODE", periodCode);
				paramMap.put("ACC_ID", accId);
				paramMap.put("CCID", ccId);
				paramMap.put("CURRENCY_CODE", currencyCode);
				HashMap<String, Object> map  = dao.getQcDatas(paramMap);
				//添加预付单主表信息
				String payHId = java.util.UUID.randomUUID().toString();//预付单主ID
				String docCatCode = "FKD_YUFK";//单据类型
				String vendorId = map.get("VENDOR_ID").toString();//供应商ID
				String projectId = map.get("PROJECT_ID").toString();//项目ID
				String deptId = map.get("DEPT_ID").toString();//部门ID
				Double amount = Double.valueOf(map.get("AMOUNT").toString());//金额
				String init = "0";
				String source = map.get("SOURCE").toString();//来源
				String desription = XipUtil.getMessage(lang, "XC_AP_INV_DESCRIPTION", null);//摘要
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("PAY_CCID", ccId);
				String id = dao.ifGlCountYuF(map1);
				if(id==null||"".equals(id)){//如果不存在同科目的期初，添加数据
					ApPayHBean payHBean = new ApPayHBean();
					payHBean.setAP_PAY_H_ID(payHId);
					payHBean.setAP_DOC_CAT_CODE(docCatCode);
					payHBean.setLEDGER_ID(ledgerId);
					payHBean.setGL_DATE(sdf.parse(sdf.format(calendar.getTime())));
					payHBean.setVENDOR_ID(vendorId);
					payHBean.setPROJECT_ID(projectId);
					payHBean.setDEPT_ID(deptId);
					payHBean.setAMOUNT(amount);
					payHBean.setINIT(init);
					payHBean.setSOURCE(source);
					payHBean.setDESCRIPTION(desription);
					payHBean.setPAY_ACC_ID(accId);
					payHBean.setPAY_CCID(ccId);
					payHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					payHBean.setCREATED_BY(CurrentSessionVar.getUserId());
					payHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					payHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addPayHInfo(payHBean);//调用dao层的方法
					//添加预收单行表信息
					String payLId = java.util.UUID.randomUUID().toString();//预收单行表主ID
					String apPurTypeId ="";//销售类型ID
					if(map.get("AP_PUR_TYPE_ID")==null){
						apPurTypeId = null;
					}else{
						apPurTypeId = map.get("AP_PUR_TYPE_ID").toString();
					}
					
					ApPayLBean payLBean = new ApPayLBean();
					payLBean.setAP_PAY_L_ID(payLId);
					payLBean.setAP_PAY_H_ID(payHId);
					payLBean.setAP_PUR_TYPE_ID(apPurTypeId);
					payLBean.setACC_ID(accId);
					payLBean.setCCID(ccId);
					payLBean.setAMOUNT(amount);
					payLBean.setDESCRIPTION(desription);
					payLBean.setCANCEL_AMT(Double.valueOf(0));
					payLBean.setNO_CANCEL_AMT(Double.valueOf(amount));
					payLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					payLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					payLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					payLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addPayLInfo(payLBean);//dao层的方法*
					
				}else{//修改
					ApPayHBean resultBean = dao.getPayHInfo(id);
					ApPayHBean bean = new ApPayHBean();
					bean.setAMOUNT(amount+resultBean.getAMOUNT());
					bean.setAP_PAY_H_ID(id);
					bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.editPayHInfo(bean);;//更新主表
					ApPayLBean lBean = new ApPayLBean();
					lBean.setAMOUNT(amount+resultBean.getAMOUNT());
					lBean.setCANCEL_AMT(0);
					lBean.setNO_CANCEL_AMT(amount+resultBean.getAMOUNT());
					lBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					lBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					lBean.setAP_PAY_H_ID(id);
					dao.updateGlCountPayL(lBean);//更新-行表
				}
				jo.put("flag", '0');	
			}
		} catch (Exception e) {
			jo.put("flag", '1');	
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	
	/**
	 * @Title splitYuF
	 * @Description: 预付期初拆分
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject splitYuF(String jsonStr) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONObject json = new JSONObject(jsonStr);
			String payHId = json.getString("AP_PAY_H_ID");//预收单主表主ID
			String projectId = json.getString("PROJECT_ID");//项目ID
			String deptId = json.getString("DEPT_ID");//部门ID
			String contractId = json.getString("AP_CONTRACT_ID");//合同ID
			String amount = json.getString("AMOUNT");//金额
			String descrption = json.getString("DESCRIPTION");//摘要
			String glDate = json.getString("GL_DATE");//业务日期
			String payHCode = json.getString("AP_PAY_H_CODE");//单据编号
			ApPayHBean payHBean = new ApPayHBean();
			payHBean.setAP_PAY_H_ID(payHId);
			payHBean.setPROJECT_ID(projectId);
			payHBean.setGL_DATE(sdf.parse(glDate));
			payHBean.setDEPT_ID(deptId);
			payHBean.setAP_CONTRACT_ID(contractId);
			payHBean.setAMOUNT(Double.valueOf(amount));
			payHBean.setINIT("1");
			payHBean.setDESCRIPTION(descrption);
			payHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			payHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			ApPayHBean msg = dao.getPayHInfo(payHId);
			if(msg==null){//新增
				String payVendorHId = json.getString("AP_PAY_VENDOR_H_ID");//客户期初（预付）
				String docCatCode = json.getString("AP_DOC_CAT_CODE");//单据类型编码
				String ledgerId = json.getString("LEDGER_ID");//账簿ID
				String vendorId = json.getString("VENDOR_ID");//供应商ID
				String accId = json.getString("PAY_ACC_ID");//科目ID
				String ccId = json.getString("PAY_CCID");//科目组合ID
				payHBean.setAP_PAY_H_CODE(payHCode);
				payHBean.setAP_PAY_VENDOR_H_ID(payVendorHId);
				payHBean.setAP_DOC_CAT_CODE(docCatCode);
				payHBean.setLEDGER_ID(ledgerId);
				payHBean.setVENDOR_ID(vendorId);
				payHBean.setPAY_ACC_ID(accId);
				payHBean.setPAY_CCID(ccId);
				payHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				payHBean.setCREATED_BY(CurrentSessionVar.getUserId());
				String LEDGER_ID = payHBean.getLEDGER_ID();
				String AP_DOC_CAT_CODE = payHBean.getAP_DOC_CAT_CODE();
				Date createDate = payHBean.getCREATION_DATE();
				String userId = payHBean.getCREATED_BY();//制单人ID
				//得到编码规则
				/*String ruleCode = xcAPCommondao.getApCode(LEDGER_ID,AP_DOC_CAT_CODE);
				if(ruleCode == null || "".equals(ruleCode)){
					throw new Exception("该账簿级单据类型没有启用编码规则！");
				}*/
				String ruleCode = "AP_PAY";
				String year = sdf.format(createDate).substring(0,4);	//年
				String month = sdf.format(createDate).substring(5,7);	//月
				//得到单据编码
				String apPayHCode = ruleService.getNextSerialNum(ruleCode, LEDGER_ID, "", year, month, userId);
				if(apPayHCode!=null||apPayHCode!=""){
					payHBean.setAP_PAY_H_CODE(apPayHCode);
				}
				dao.addPayHInfo(payHBean);
				//添加预收单行表信息
				String payLId = java.util.UUID.randomUUID().toString();//预收单行表主ID
				ApPayLBean payLBean = new ApPayLBean();
				payLBean.setAP_PAY_L_ID(payLId);
				payLBean.setAP_PAY_H_ID(payHId);
				payLBean.setACC_ID(accId);
				payLBean.setCCID(ccId);
				payLBean.setAMOUNT(Double.valueOf(amount));
				payLBean.setDESCRIPTION(descrption);
				payLBean.setCANCEL_AMT(Double.valueOf(0));
				payLBean.setNO_CANCEL_AMT(Double.valueOf(amount));
				payLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				payLBean.setCREATED_BY(CurrentSessionVar.getUserId());
				payLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				payLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.addPayLInfo(payLBean);//dao层的方法*
				ApInvTransBean transBean = new ApInvTransBean();
				transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
				transBean.setSOURCE_ID(payHId);
				transBean.setSOURCE_DTL_ID(payLBean.getAP_PAY_L_ID());
				transBean.setAP_PAY_H_ID(payHId);
				transBean.setAP_CONTRACT_ID(contractId);
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setSOURCE_TAB("XC_AP_PAY_H");
				transBean.setAP_DOC_CAT_CODE("FKD_YUFK");
				transBean.setVENDOR_ID(vendorId);
				transBean.setDR_AMT(Double.valueOf(amount));
				transBean.setCR_AMT(Double.valueOf(0));
				transBean.setCCID(ccId);
				transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setCREATED_BY(CurrentSessionVar.getUserId());
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				transBean.setAP_DOC_CODE(payHCode);
				transBean.setDESCRIPTION(descrption);
				dao.addInvTrans(transBean);
				jo.put("code", apPayHCode);
			}else{//修改
				dao.editPayHInfo(payHBean);//更新主表
				ApPayLBean lBean = new ApPayLBean();
				lBean.setAMOUNT(Double.valueOf(amount));
				lBean.setCANCEL_AMT(0);
				lBean.setNO_CANCEL_AMT(Double.valueOf(amount));
				lBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				lBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				lBean.setAP_PAY_H_ID(payHId);
				dao.updateGlCountPayL(lBean);//更新-行表
				ApInvTransBean transBean = new ApInvTransBean();
				transBean.setSOURCE_ID(payHId);
				transBean.setAP_CONTRACT_ID(contractId);
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setDR_AMT(Double.valueOf(amount));
				transBean.setCR_AMT(Double.valueOf(0));
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.updateInvtrans(transBean);
				jo.put("code", payHCode);
			}
			jo.put("flag","0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	
	/**
	 * @Title deleteInfoYuF
	 * @Description: 预付期初删除信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject deleteInfoYuF(String ids,String lang) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(ids);
			List<String> idList = new ArrayList<String>(); 
			List<String> hids = new ArrayList<String>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				idList.add(ob.getString("id"));
				hids.add(ob.getString("id"));
				List<HashMap<String, String>> resultList = dao.getPayHId(ob.getString("id"));
				for (int j = 0; j < resultList.size(); j++) {
					hids.add(resultList.get(j).get("AP_PAY_H_ID"));
				}
			}
			if(!"1".equals(dao.checkPayIfUse(hids))){
				dao.delPayLInfo(hids);//删除行信息
				dao.delPayHInfo(idList);//删除主信息
				dao.delPayHInfoByPayHId(idList);
				dao.deleteInvTrans(hids);
				jo.put("flag", "0");
			}else{
				jo.put("flag", "1");
				jo.put("msg", XipUtil.getMessage(lang, "XC_AP_QC_PAY_DELETE", null));
			}
			
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	
	/**
	 * @Title delPayHInfo
	 * @Description: 预付期初单独删除明细信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject delPayHInfo(String ids) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(ids);
			List<String> idList = new ArrayList<String>(); 
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				idList.add(ob.getString("id"));
			}
			dao.delPayLInfo(idList);
			dao.delPayHInfo(idList);
			dao.deleteInvTrans(idList);
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
	 * @Title balanceInv
	 * @Description: 应付记账 
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject balanceInv(String ids) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(ids);
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < idArray.length(); i++) {
				String id = idArray.getJSONObject(i).getString("id");
				idList.add(id);
			}
			dao.balanceInv(idList);
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
	 * @Title cancelBalanceInv
	 * @Description: 应付取消记账 
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject cancelBalanceInv(String ids,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(ids);
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < idArray.length(); i++) {
				String id = idArray.getJSONObject(i).getString("id");
				idList.add(id);
			}
			if(!"1".equals(dao.checkInvIfUse(idList))){
				dao.cancelBalanceInv(idList);
				jo.put("flag", "0");
			}else{
				jo.put("flag", "1");
				jo.put("msg", XipUtil.getMessage(lang, "XC_AP_QC_CANCELBALANCE_INV", null));
			}
			
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title balanceInv
	 * @Description: 预付记账 
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject balancePay(String ids) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(ids);
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < idArray.length(); i++) {
				String id = idArray.getJSONObject(i).getString("id");
				idList.add(id);
			}
			dao.balancePay(idList);
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
	 * @Title cancelBalanceInv
	 * @Description: 预付取消记账 
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject cancelBalancePay(String ids,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(ids);
			List<String> idList = new ArrayList<String>();
			for (int i = 0; i < idArray.length(); i++) {
				String id = idArray.getJSONObject(i).getString("id");
				idList.add(id);
			}
			if(!"1".equals(dao.checkPayIfUse(idList))){
				dao.cancelBalancePay(idList);
				jo.put("flag", "0");
			}else{
				jo.put("flag", "1");
				jo.put("msg", XipUtil.getMessage(lang, "XC_AP_QC_CANCELBALANCE_PAY", null));
			}
			
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}

}
