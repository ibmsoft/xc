package com.xzsoft.xc.ar.service.impl;

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

import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.action.ArInvGlBaseAction;
import com.xzsoft.xc.ar.dao.ArInvGlBaseDao;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArInvGlLBean;
import com.xzsoft.xc.ar.modal.ArPayHBean;
import com.xzsoft.xc.ar.modal.ArPayLBean;
import com.xzsoft.xc.ar.service.ArInvGlBaseService;
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
@Service("arInvGlHService")
public class ArInvGlBaseServiceImpl implements ArInvGlBaseService {
	@Resource
	ArInvGlBaseDao dao;
	@Resource
	XCARCommonDao xcARCommondao;
	@Resource
	RuleService ruleService;
	@Resource
	ApArTransDao apArTransDao;
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//日志记录器
	private static Logger log = Logger.getLogger(ArInvGlBaseServiceImpl.class.getName());
	/**
	 * @Title:glCount
	 * @Description: 应收期初总账取数
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject glCount(String invGlH) throws Exception{
		JSONObject jo = new JSONObject();
		String lang = XCUtil.getLang();//语言值
		try {
			JSONArray array = new JSONArray(invGlH);
			for(int i=0;i<array.length();i++){
				JSONObject json = array.getJSONObject(i);
				String ledgerId = json.getString("LEDGER_ID");//账簿ID
				String periodCode = json.getString("PERIOD_CODE");//会计期
				String glDate = periodCode + "-01";//业务日期取会计期的前一天
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(glDate));
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				String accId = json.getString("ACC_ID");//应收科目
				String ccId = json.getString("CCID");//应收科目组合
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
				String docCatCode = "YSD";//单据类型
				String customerId = map.get("CUSTOMER_ID").toString();//客户ID
				String projectId = map.get("PROJECT_ID").toString();//项目ID
				String deptId = map.get("DEPT_ID").toString();//部门ID
				Float amount = Float.valueOf(map.get("AMOUNT").toString());//金额
				Float invAmount = Float.valueOf(map.get("INV_AMOUNT").toString());//含税金额
				Float cancelAmt = Float.valueOf(map.get("CANCEL_AMT").toString());//核销金额
				Float noPayAmt = Float.valueOf(map.get("NO_PAY_AMT").toString());//未支付金额
				Float adjAmt = Float.valueOf(map.get("ADJ_AMT").toString());//调整金额
				String init = "0";
				String source = map.get("SOURCE").toString();//来源
				String desription = XipUtil.getMessage(lang, "XC_AP_INV_DESCRIPTION", null);
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("AR_CCID_DEBT",ccId);
				String id = dao.ifGlCount(map1);
				List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
				List<ArInvTransBean> editTransList = new ArrayList<ArInvTransBean>();
				ArInvTransBean transBean = new ArInvTransBean();
				if(id==null||"".equals(id)){//如果不存在同科目组合
					ArInvGlHBean invGlHBean = new ArInvGlHBean();
					invGlHBean.setAR_INV_GL_H_ID(invGlHId);
					invGlHBean.setAR_DOC_CAT_CODE(docCatCode);
					invGlHBean.setLEDGER_ID(ledgerId);
					invGlHBean.setGL_DATE(sdf.parse(sdf.format(calendar.getTime())));
					invGlHBean.setCUSTOMER_ID(customerId);
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
					invGlHBean.setAR_ACC_ID_DEBT(accId);
					invGlHBean.setAR_CCID_DEBT(ccId);
					invGlHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					invGlHBean.setCREATED_BY(CurrentSessionVar.getUserId());
					invGlHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					invGlHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addInvGlHInfo(invGlHBean);//调用dao层的方法
					//添加应收单行表信息
					String invGlLId = java.util.UUID.randomUUID().toString();
					String arSaleTypeId ="";//销售类型ID
					if(map.get("AR_SALE_TYPE_ID")==null){
						arSaleTypeId = null;
					}else{
						arSaleTypeId = map.get("AR_SALE_TYPE_ID").toString();
					}
					
					ArInvGlLBean invGlLBean = new ArInvGlLBean();
					invGlLBean.setAR_INV_GL_L_ID(invGlLId);
					invGlLBean.setAR_INV_GL_H_ID(invGlHId);
					invGlLBean.setAR_SALE_TYPE_ID(arSaleTypeId);
					invGlLBean.setACC_ID(accId);
					invGlLBean.setCCID(ccId);
					invGlLBean.setAMOUNT(amount);
					invGlLBean.setDESCRIPTION(desription);
					invGlLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					invGlLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					invGlLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					invGlLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addInvGlLInfo(invGlLBean);//dao层的方法
					transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
					transBean.setSOURCE_ID(invGlHId);
					transBean.setSOURCE_DTL_ID(invGlLId);
					transBean.setAR_INV_GL_H_ID(invGlHId);
					transBean.setGL_DATE(sdf.parse(glDate));
					transBean.setSOURCE_TAB("XC_AR_INV_GL_H");
					transBean.setAR_DOC_CAT_CODE("YSD");
					transBean.setAR_DOC_CODE("");
					transBean.setDESCRIPTION(desription);
					transBean.setDR_AMT(amount);
					transBean.setCR_AMT(0);
					transBean.setTRANS_STATUS("0");
					transBean.setCCID(ccId);
					transBean.setCREATED_BY(CurrentSessionVar.getUserId());
					transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					transList.add(transBean);
				}else{//修改
					ArInvGlHBean resultBean = dao.getInvGlHInfo(id);
					ArInvGlHBean bean = new ArInvGlHBean();
					bean.setAMOUNT(amount+resultBean.getAMOUNT());
					bean.setCANCEL_AMT(0);
					bean.setNO_PAY_AMT(amount+resultBean.getAMOUNT());
					bean.setPAID_AMT(0);
					bean.setLAST_UPDATE_DATE( CurrentUserUtil.getCurrentDate());
					bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					bean.setAR_INV_GL_H_ID(id);
					dao.editInvGlHInfo(bean);//更新主表
					ArInvGlLBean lBean = new ArInvGlLBean();
					lBean.setAMOUNT(amount+resultBean.getAMOUNT());
					lBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					lBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					lBean.setAR_INV_GL_H_ID(id);
					dao.updateGlCountInvL(lBean);//行表
					transBean.setSOURCE_ID(invGlHId);
					transBean.setGL_DATE(sdf.parse(glDate));
					transBean.setSOURCE_TAB("XC_AR_INV_GL_H");
					transBean.setAR_DOC_CAT_CODE("YSD");
					transBean.setAR_DOC_CODE("");
					transBean.setDESCRIPTION(desription);
					transBean.setDR_AMT(amount);
					transBean.setCR_AMT(0);
					transBean.setTRANS_STATUS("0");
					transBean.setCCID(ccId);
					transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					editTransList.add(transBean);
				}
				apArTransDao.insertArTrans(transList);//插入交易明细
				apArTransDao.updateArTrans(editTransList);
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
	 * @Title:split
	 * @Description: 应收期初拆分
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject split(String jsonStr) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONObject json = new JSONObject(jsonStr);
			String invGlHId = json.getString("AR_INV_GL_H_ID");//应收单主表ID
			String projectId = json.getString("PROJECT_ID");//项目ID
			String deptId = json.getString("DEPT_ID");//部门I
			String contractId = json.getString("AR_CONTRACT_ID");//合同ID
			String amount = json.getString("AMOUNT");//金额
			String descrption = json.getString("DESCRIPTION");//摘要
			String glDate = json.getString("GL_DATE");//业务日期
			String invGlHCode = json.getString("AR_INV_GL_H_CODE");//应收单编号
			ArInvGlHBean invGlHBean = new ArInvGlHBean();
			invGlHBean.setAR_INV_GL_H_ID(invGlHId);
			invGlHBean.setPROJECT_ID(projectId);
			invGlHBean.setGL_DATE(sdf.parse(glDate));
			invGlHBean.setDEPT_ID(deptId);
			invGlHBean.setAR_CONTRACT_ID(contractId);
			invGlHBean.setAMOUNT(Double.valueOf(amount));
			invGlHBean.setINIT("1");
			invGlHBean.setDESCRIPTION(descrption);
			invGlHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			invGlHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			ArInvGlHBean msg = dao.getInvGlHInfo(invGlHId);
			List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
			List<ArInvTransBean> editTransList = new ArrayList<ArInvTransBean>();
			ArInvTransBean transBean = new ArInvTransBean();
			if(msg==null){//新增
				
				String invHId = json.getString("AR_INV_H_ID");//发票ID
				String docCatCode = json.getString("AR_DOC_CAT_CODE");//单据类型
				String ledgerId = json.getString("LEDGER_ID");//账簿ID
				String customerId = json.getString("CUSTOMER_ID");//客户ID
				String accId = json.getString("AR_ACC_ID_DEBT");//科目ID
				String ccid = json.getString("AR_CCID_DEBT");//科目组合ID
				invGlHBean.setAR_INV_GL_H_CODE(invGlHCode);
				invGlHBean.setAR_INV_H_ID(invHId);
				invGlHBean.setAR_DOC_CAT_CODE(docCatCode);
				invGlHBean.setLEDGER_ID(ledgerId);
				invGlHBean.setCUSTOMER_ID(customerId);
				invGlHBean.setADJ_AMT(0);
				invGlHBean.setAR_ACC_ID_DEBT(accId);
				invGlHBean.setAR_CCID_DEBT(ccid);
				invGlHBean.setTAX_AMT(0);
				invGlHBean.setTAX_RATE(0);
				invGlHBean.setINV_AMOUNT(Float.valueOf(amount));
				invGlHBean.setCANCEL_AMT(0);
				invGlHBean.setPAID_AMT(0);
				invGlHBean.setNO_PAY_AMT(Float.valueOf(amount));
				invGlHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				invGlHBean.setCREATED_BY(CurrentSessionVar.getUserId());
				String LEDGER_ID = invGlHBean.getLEDGER_ID();
				String AR_DOC_CAT_CODE = invGlHBean.getAR_DOC_CAT_CODE();
				Date createDate = invGlHBean.getCREATION_DATE();
				String userId = invGlHBean.getCREATED_BY();
				//得到编码规则
				/*String ruleCode = xcARCommondao.getArCode(LEDGER_ID,AR_DOC_CAT_CODE);
				if(ruleCode == null || "".equals(ruleCode)){
					throw new Exception("该账簿级单据类型没有启用编码规则！");
				}*/
				String ruleCode = "AR_INV_GL";
				String year = sdf.format(createDate).substring(0,4);	//年
				String month = sdf.format(createDate).substring(5,7);	//月
				//得到单据编码
				String arInvGlHCode = ruleService.getNextSerialNum(ruleCode, LEDGER_ID, "", year, month, userId);
				if(arInvGlHCode!=null||arInvGlHCode!=""){
					invGlHBean.setAR_INV_GL_H_CODE(arInvGlHCode);
				}
				dao.addInvGlHInfo(invGlHBean);
				ArInvGlLBean invGlLBean = new ArInvGlLBean();
				String invGlLId =  java.util.UUID.randomUUID().toString();
				invGlLBean.setAR_INV_GL_L_ID(invGlLId);
				invGlLBean.setAR_INV_GL_H_ID(invGlHId);
				invGlLBean.setACC_ID(accId);
				invGlLBean.setCCID(ccid);
				invGlLBean.setAMOUNT(Double.valueOf(amount));
				invGlLBean.setDESCRIPTION(descrption);
				invGlLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				invGlLBean.setCREATED_BY(CurrentSessionVar.getUserId());
				invGlLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				invGlLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.addInvGlLInfo(invGlLBean);
				transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
				transBean.setSOURCE_ID(invGlHId);
				transBean.setSOURCE_DTL_ID(invGlLId);
				transBean.setAR_INV_GL_H_ID(invGlHId);
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setSOURCE_TAB("XC_AR_INV_GL_H");
				transBean.setAR_DOC_CAT_CODE("YSD");
				transBean.setAR_DOC_CODE(arInvGlHCode);
				transBean.setDESCRIPTION(descrption);
				transBean.setDR_AMT(Double.valueOf(amount));
				transBean.setCR_AMT(0);
				transBean.setTRANS_STATUS("0");
				transBean.setCCID(ccid);
				transBean.setCREATED_BY(CurrentSessionVar.getUserId());
				transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				transList.add(transBean);
				jo.put("code", arInvGlHCode);
			}else{//修改
				invGlHBean.setCANCEL_AMT(0);
				invGlHBean.setNO_PAY_AMT(Double.valueOf(amount));
				invGlHBean.setPAID_AMT(0);
				dao.editInvGlHInfo(invGlHBean);//更新应收单主表信息
				ArInvGlLBean lBean = new ArInvGlLBean();
				lBean.setAMOUNT(Double.valueOf(amount));
				lBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				lBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				lBean.setAR_INV_GL_H_ID(invGlHId);
				dao.updateGlCountInvL(lBean);//行表
				transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
				transBean.setSOURCE_ID(invGlHId);
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setSOURCE_TAB("XC_AR_INV_GL_H");
				transBean.setAR_DOC_CAT_CODE("YSD");
				transBean.setAR_DOC_CODE(invGlHCode);
				transBean.setDESCRIPTION(descrption);
				transBean.setDR_AMT(Double.valueOf(amount));
				transBean.setCR_AMT(0);
				transBean.setTRANS_STATUS("0");
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				transList.add(transBean);
				jo.put("code", invGlHCode);
			}
			apArTransDao.insertArTrans(transList);//插入交易明细
			apArTransDao.updateArTrans(editTransList);//更新交易明细
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
	 * @Title:deleteInfo
	 * @Description: 应收期初删除数据
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteInfo(String ids,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(ids);
			List<String> idList = new ArrayList<String>(); 
			List<String> hIds = new ArrayList<String>();
			List<ArInvTransBean> delTransList = new ArrayList<ArInvTransBean>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				idList.add(ob.getString("id"));
				hIds.add(ob.getString("id"));
				List<HashMap<String, String>> resultList = dao.getInvHId(ob.getString("id"));
				for (int j = 0; j < resultList.size(); j++) {
					hIds.add(resultList.get(j).get("AR_INV_GL_H_ID"));
				}
			}
			for(int m=0;m<hIds.size();m++){
				ArInvTransBean transBean = new ArInvTransBean();
				transBean.setSOURCE_ID(hIds.get(m));
				transBean.setAR_DOC_CAT_CODE("YSD");
				delTransList.add(transBean);
			}
			if(!"1".equals(dao.checkInvIfUse(hIds))){
				dao.delInvGlLInfo(hIds);
				dao.delInvGlHInfo(idList);
				dao.delInvGlHInfoByInvHId(idList);
				apArTransDao.deleteArTrans(delTransList);
				jo.put("flag", "0");
			}else{
				jo.put("flag", "1");
				jo.put("msg", XipUtil.getMessage(lang, "XC_AR_QC_INV_DELETE ", null));
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
	 * @Title:delInvGlHInfo
	 * @Description: 应收期初单独删除明细数据
	 * 参数格式:
	 * @param  java.lang.String
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
			List<ArInvTransBean> delTransList = new ArrayList<ArInvTransBean>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				idList.add(ob.getString("id"));
				ArInvTransBean transBean = new ArInvTransBean();
				transBean.setSOURCE_ID(ob.getString("id"));
				transBean.setAR_DOC_CAT_CODE("YSD");
				delTransList.add(transBean);
			}
			dao.delInvGlLInfo(idList);//删除行表信息
			dao.delInvGlHInfo(idList);//删除主表信息
			apArTransDao.deleteArTrans(delTransList);//删除交易明细
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
		}
		return jo;
	}
	/**
	 * @Title:glCountYuS
	 * @Description: 预收期初总账取数
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject glCountYuS(String payH) throws Exception {
		JSONObject jo = new JSONObject();
		String lang = XCUtil.getLang();//语言值
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
				//添加预收单主表信息
				String payHId = java.util.UUID.randomUUID().toString();//预收单主ID
				String docCatCode = "SKD_YUSK";//单据类型
				String customerId = map.get("CUSTOMER_ID").toString();//客户ID
				String projectId = map.get("PROJECT_ID").toString();//项目ID
				String deptId = map.get("DEPT_ID").toString();//部门ID
				Double amount = Double.valueOf(map.get("AMOUNT").toString());//金额
				String init = "0";
				String source = map.get("SOURCE").toString();//来源
				String desription = XipUtil.getMessage(lang, "XC_AP_INV_DESCRIPTION", null);//摘要
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("SALE_ACC_ID", accId);
				String id = dao.ifGlCountYuS(map1);
				List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
				List<ArInvTransBean> editTransList = new ArrayList<ArInvTransBean>();
				ArInvTransBean transBean = new ArInvTransBean();
				if(id==null||"".equals(id)){//如果不存在同科目组合，添加数据
					ArPayHBean payHBean = new ArPayHBean();
					payHBean.setAR_PAY_H_ID(payHId);
					payHBean.setAR_DOC_CAT_CODE(docCatCode);
					payHBean.setLEDGER_ID(ledgerId);
					payHBean.setGL_DATE(sdf.parse(sdf.format(calendar.getTime())));
					payHBean.setCUSTOMER_ID(customerId);
					payHBean.setPROJECT_ID(projectId);
					payHBean.setDEPT_ID(deptId);
					payHBean.setAMOUNT(amount);
					payHBean.setINIT(init);
					payHBean.setSOURCE(source);
					payHBean.setDESCRIPTION(desription);
					payHBean.setSALE_ACC_ID(accId);
					payHBean.setSALE_CCID(ccId);
					payHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					payHBean.setCREATED_BY(CurrentSessionVar.getUserId());
					payHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					payHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addPayHInfo(payHBean);//调用dao层的方法
					//添加预收单行表信息
					String payLId = java.util.UUID.randomUUID().toString();//预收单行表主ID
					String arSaleTypeId ="";//销售类型ID
					if(map.get("AR_SALE_TYPE_ID")==null){
						arSaleTypeId = null;
					}else{
						arSaleTypeId = map.get("AR_SALE_TYPE_ID").toString();
					}
					
					ArPayLBean payLBean = new ArPayLBean();
					payLBean.setAR_PAY_L_ID(payLId);
					payLBean.setAR_PAY_H_ID(payHId);
					payLBean.setAR_SALE_TYPE_ID(arSaleTypeId);
					payLBean.setACC_ID(accId);
					payLBean.setCCID(ccId);
					payLBean.setAMOUNT(amount);
					payLBean.setCANCEL_AMT(Double.valueOf(0));
					payLBean.setNO_CANCEL_AMT(amount);
					payLBean.setDESCRIPTION(desription);
					payLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					payLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					payLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					payLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addPayLInfo(payLBean);//dao层的方法
					transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
					transBean.setSOURCE_ID(payHId);
					transBean.setSOURCE_DTL_ID(payLId);
					transBean.setGL_DATE(sdf.parse(glDate));
					transBean.setSOURCE_TAB("XC_AR_PAY_H");
					transBean.setAR_DOC_CAT_CODE("SKD_YUFK");
					transBean.setAR_DOC_CODE("");
					transBean.setDESCRIPTION(desription);
					transBean.setDR_AMT(Double.valueOf(amount));
					transBean.setCR_AMT(0);
					transBean.setTRANS_STATUS("0");
					transBean.setCCID(ccId);
					transBean.setCREATED_BY(CurrentSessionVar.getUserId());
					transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					transList.add(transBean);
				}else{//修改
					ArPayHBean resultBean = dao.getPayHInfo(id);
					ArPayHBean bean = new ArPayHBean();
					bean.setAMOUNT(amount+resultBean.getAMOUNT());
					bean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					bean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					bean.setAR_PAY_H_ID(id);
					dao.editPayHInfo(bean);//更新主表
					ArPayLBean lBean = new ArPayLBean();
					lBean.setAMOUNT(amount+resultBean.getAMOUNT());
					lBean.setNO_CANCEL_AMT(amount+resultBean.getAMOUNT());
					lBean.setCANCEL_AMT(Double.valueOf(0));
					lBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					lBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					lBean.setAR_PAY_H_ID(id);
					dao.updateGlCountPayL(lBean);//更新行表
					transBean.setSOURCE_ID(payHId);
					transBean.setGL_DATE(sdf.parse(glDate));
					transBean.setSOURCE_TAB("XC_AR_PAY_H");
					transBean.setAR_DOC_CAT_CODE("SKD_YUFK");
					transBean.setAR_DOC_CODE("");
					transBean.setDESCRIPTION(desription);
					transBean.setDR_AMT(Double.valueOf(amount));
					transBean.setCR_AMT(0);
					transBean.setTRANS_STATUS("0");
					transBean.setCCID(ccId);
					transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					editTransList.add(transBean);
				}
				apArTransDao.insertArTrans(transList);//插入交易明细
				apArTransDao.updateArTrans(editTransList);//更新交易明细
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
	 * @Title:splitYuS
	 * @Description: 预收期初拆分
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject splitYuS(String jsonStr) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONObject json = new JSONObject(jsonStr);
			String payHId = json.getString("AR_PAY_H_ID");//预收单主表主ID
			String projectId = json.getString("PROJECT_ID");//项目ID
			String deptId = json.getString("DEPT_ID");//部门ID
			String contractId = json.getString("AR_CONTRACT_ID");//合同ID
			String amount = json.getString("AMOUNT");//金额
			String descrption = json.getString("DESCRIPTION");//摘要
			String glDate = json.getString("GL_DATE");//业务日期
			String payHCode = json.getString("AR_PAY_H_CODE");//单据编号
			ArPayHBean payHBean = new ArPayHBean();
			payHBean.setAR_PAY_H_ID(payHId);
			payHBean.setPROJECT_ID(projectId);
			payHBean.setGL_DATE(sdf.parse(glDate));
			payHBean.setDEPT_ID(deptId);
			payHBean.setAR_CONTRACT_ID(contractId);
			payHBean.setAMOUNT(Double.valueOf(amount));
			payHBean.setINIT("1");
			payHBean.setDESCRIPTION(descrption);
			payHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			payHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
			List<ArInvTransBean> editTransList = new ArrayList<ArInvTransBean>();
			ArInvTransBean transBean = new ArInvTransBean();
			ArPayHBean msg = dao.getPayHInfo(payHId);
			if(msg==null){//新增
				
				String payCustomerHId = json.getString("AR_PAY_CUSTOMER_H_ID");//客户期初（预收）
				String docCatCode = json.getString("AR_DOC_CAT_CODE");//单据类型编码
				String ledgerId = json.getString("LEDGER_ID");//账簿ID
				String customerId = json.getString("CUSTOMER_ID");//客户ID
				String accId = json.getString("PAY_ACC_ID");//科目ID
				String ccid = json.getString("SALE_CCID");
				payHBean.setAR_PAY_H_CODE(payHCode);
				payHBean.setAR_PAY_CUSTOMER_H_ID(payCustomerHId);
				payHBean.setAR_DOC_CAT_CODE(docCatCode);
				payHBean.setLEDGER_ID(ledgerId);
				payHBean.setCUSTOMER_ID(customerId);
				payHBean.setSALE_ACC_ID(accId);
				payHBean.setSALE_CCID(ccid);
				payHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				payHBean.setCREATED_BY(CurrentSessionVar.getUserId());
				String LEDGER_ID = payHBean.getLEDGER_ID();
				String AR_DOC_CAT_CODE = payHBean.getAR_DOC_CAT_CODE();
				Date createDate = payHBean.getCREATION_DATE();
				String userId = payHBean.getCREATED_BY();//制单人ID
				//得到编码规则
				/*String ruleCode = xcARCommondao.getArCode(LEDGER_ID,AR_DOC_CAT_CODE);
				if(ruleCode == null || "".equals(ruleCode)){
					throw new Exception("该账簿级单据类型没有启用编码规则！");
				}*/
				String ruleCode = "AR_PAY";
				String year = sdf.format(createDate).substring(0,4);	//年
				String month = sdf.format(createDate).substring(5,7);	//月
				//得到单据编码
				String arPayHCode = ruleService.getNextSerialNum(ruleCode, LEDGER_ID, "", year, month, userId);
				if(arPayHCode!=null||arPayHCode!=""){
					payHBean.setAR_PAY_H_CODE(arPayHCode);
				}
				dao.addPayHInfo(payHBean);
				//添加预收单行表信息
				String payLId = java.util.UUID.randomUUID().toString();//预收单行表主ID
				ArPayLBean payLBean = new ArPayLBean();
				payLBean.setAR_PAY_L_ID(payLId);
				payLBean.setAR_PAY_H_ID(payHId);
				payLBean.setACC_ID(accId);
				payLBean.setCCID(ccid);
				payLBean.setAMOUNT(Double.valueOf(amount));
				payLBean.setCANCEL_AMT(Double.valueOf(0));
				payLBean.setNO_CANCEL_AMT(Double.valueOf(amount));
				payLBean.setDESCRIPTION(descrption);
				payLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				payLBean.setCREATED_BY(CurrentSessionVar.getUserId());
				payLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				payLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.addPayLInfo(payLBean);//dao层的方法
				transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
				transBean.setSOURCE_ID(payHId);
				transBean.setSOURCE_DTL_ID(payLId);
				transBean.setAR_PAY_H_ID(payHId);
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setSOURCE_TAB("XC_AR_PAY_H");
				transBean.setAR_DOC_CAT_CODE("SKD_YUFK");
				transBean.setAR_DOC_CODE(arPayHCode);
				transBean.setDESCRIPTION(descrption);
				transBean.setDR_AMT(Double.valueOf(amount));
				transBean.setCR_AMT(0);
				transBean.setTRANS_STATUS("0");
				transBean.setCCID(ccid);
				transBean.setCREATED_BY(CurrentSessionVar.getUserId());
				transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				transList.add(transBean);
				jo.put("code", arPayHCode);
			}else{//修改
				dao.editPayHInfo(payHBean);//更新主表
				ArPayLBean lBean = new ArPayLBean();
				lBean.setAMOUNT(Double.valueOf(amount));
				lBean.setNO_CANCEL_AMT(Double.valueOf(amount));
				lBean.setCANCEL_AMT(Double.valueOf(0));
				lBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				lBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				lBean.setAR_PAY_H_ID(payHId);
				dao.updateGlCountPayL(lBean);//更新行表
				transBean.setSOURCE_ID(payHId);
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setSOURCE_TAB("XC_AR_PAY_H");
				transBean.setAR_DOC_CAT_CODE("SKD_YUFK");
				transBean.setAR_DOC_CODE(payHCode);
				transBean.setDESCRIPTION(descrption);
				transBean.setDR_AMT(Double.valueOf(amount));
				transBean.setCR_AMT(0);
				transBean.setTRANS_STATUS("0");
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				editTransList.add(transBean);
				jo.put("code", payHCode);
			}
			apArTransDao.insertArTrans(transList);//插入交易明细
			apArTransDao.updateArTrans(editTransList);//更新交易明细
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
	 * @Title:deleteInfoYuS
	 * @Description: 预收期初删除数据
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteInfoYuS(String ids,String lang) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(ids);
			List<String> idList = new ArrayList<String>(); 
			List<String> hIds = new ArrayList<String>();
			List<ArInvTransBean> delTransList = new ArrayList<ArInvTransBean>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				idList.add(ob.getString("id"));
				hIds.add(ob.getString("id"));
				List<HashMap<String, String>> resultList = dao.getPayHId(ob.getString("id"));
				
				
				for (int j = 0; j < resultList.size(); j++) {
					hIds.add(resultList.get(j).get("AR_PAY_H_ID"));
					
				}
			}
			for(int m=0;m<hIds.size();m++){
				ArInvTransBean transBean = new ArInvTransBean();
				transBean.setSOURCE_ID(hIds.get(m));
				transBean.setAR_DOC_CAT_CODE("SDK_YUSK");
				delTransList.add(transBean);
				
			}
			if(!"1".equals(dao.checkPayIfUse(hIds))){
				dao.delPayLInfo(hIds);//删除行表信息
				dao.delPayHInfo(idList);//删除主表信息
				dao.delPayHInfoByPayHId(idList);
				apArTransDao.deleteArTrans(delTransList);//删除交易明细
				jo.put("flag", "0");
			}else{
				jo.put("flag", "1");
				jo.put("msg", XipUtil.getMessage(lang, "XC_AR_QC_PAY_DELETE ", null));
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
	 * @Title:delPayHInfo
	 * @Description: 预收期初单独删除明细数据
	 * 参数格式:
	 * @param  java.lang.String
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
			List<ArInvTransBean> delTransList = new ArrayList<ArInvTransBean>();
			for (int i = 0; i < array.length(); i++) {
				JSONObject ob = array.getJSONObject(i);
				idList.add(ob.getString("id"));
				ArInvTransBean transBean = new ArInvTransBean();
				transBean.setSOURCE_ID(ob.getString("id"));
				transBean.setAR_DOC_CAT_CODE("SKD_YSK");
				delTransList.add(transBean);
			}
			dao.delPayLInfo(idList);//删除行表
			dao.delPayHInfo(idList);//删除主表
			apArTransDao.deleteArTrans(delTransList);//删除交易明细
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
		}
		return jo;
	}
	/**
	 * @Title balanceInv
	 * @Description: 应收记账 
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
		}
		return jo;
	}
	/**
	 * @Title cancelBalanceInv
	 * @Description: 应收取消记账 
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
				jo.put("msg", XipUtil.getMessage(lang, "XC_AR_QC_CANCELBALANCE_INV ", null));
			}
			
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
		}
		return jo;
	}
	/**
	 * @Title balanceInv
	 * @Description: 预收记账 
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
		}
		return jo;
	}
	/**
	 * @Title cancelBalanceInv
	 * @Description: 预收取消记账 
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
				jo.put("msg", XipUtil.getMessage(lang, "XC_AR_QC_CANCELBALANCE_PAY ", null));
			}
			
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
		}
		return jo;
	}


}
