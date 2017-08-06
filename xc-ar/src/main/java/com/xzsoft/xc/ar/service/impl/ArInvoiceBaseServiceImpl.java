package com.xzsoft.xc.ar.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import com.xzsoft.xc.ar.dao.ArInvoiceBaseDao;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.modal.ArCustomerBean;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;
import com.xzsoft.xc.ar.modal.ArInvoiceLBean;
import com.xzsoft.xc.ar.service.ArInvoiceBaseService;
import com.xzsoft.xc.ar.util.XCARConstants;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName ApInvoiceBaseServiceImpl
  * @Description 应付模块发票基础信息处理实现类
  * @author 任建建
  * @date 2016年6月14日 下午2:20:29
 */
@Service("arInvoiceBaseService")
public class ArInvoiceBaseServiceImpl implements ArInvoiceBaseService {
	public static final Logger log = Logger.getLogger(ArInvoiceBaseServiceImpl.class);
	@Resource
	ArInvoiceBaseDao arInvoiceBaseDao;
	@Resource
	RuleService ruleService;
	@Resource
	private XCARCommonDao xcarCommonDao;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * 
	  * <p>Title getArAttCatCode</p>
	  * <p>Description 得到应付模块的附件分类编码</p>
	  * @param arDocCatCode
	  * @param ledgerId
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArInvoiceBaseService#getArAttCatCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getArAttCatCode(String arDocCatCode, String ledgerId,String language) throws Exception {
		String attCatCode = "";
		try {
			//查询账簿级销售发票中的流程相关信息
			HashMap<String,String> apAttCatCode = arInvoiceBaseDao.getInvoiceLdProcessInfo(arDocCatCode,ledgerId);
			String apCatName = apAttCatCode.get("AR_CAT_NAME");
			attCatCode = apAttCatCode.get("ATT_CAT_CODE");
			if(attCatCode == null || "".equals(attCatCode)){
				throw new Exception(apCatName + XipUtil.getMessage(language, "XC_AP_ENCLOSURE_EXCEPTION", null));//未设置附件分类，请在【应付单据类型设置】功能处设置！
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return attCatCode;
	}
	/*
	 * 
	  * <p>Title saveOrUpdateInvoice</p>
	  * <p>Description 销售发票保存和更新方法</p>
	  * @param mainTabInfoJson
	  * @param rowTabInfoJson
	  * @param deleteDtlInfo
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArInvoiceBaseService#saveOrUpdateInvoice(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveOrUpdateInvoice(String mainTabInfoJson,String rowTabInfoJson,String deleteDtlInfo,String language) throws Exception {
		JSONObject json = new JSONObject();
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			if(mainTabInfoJson == null || "".equals(mainTabInfoJson))
				throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE_IS_NOT_NULL", null));//销售发票信息不能为空！
			//销售发票主信息
			ArInvoiceHBean arInvoiceH = new ArInvoiceHBean();
			JSONObject invoiceHJson = new JSONObject(mainTabInfoJson);

			String AR_INV_H_ID = invoiceHJson.getString("AR_INV_H_ID");
			String AR_DOC_CAT_CODE = invoiceHJson.getString("AR_DOC_CAT_CODE");
			String L_AR_INV_H_ID = invoiceHJson.getString("L_AR_INV_H_ID");
			String LEDGER_ID = invoiceHJson.getString("LEDGER_ID");
			String BIZ_DATE = invoiceHJson.getString("BIZ_DATE");
			String CUSTOMER_ID = invoiceHJson.getString("CUSTOMER_ID");
			String PROJECT_ID = invoiceHJson.getString("PROJECT_ID");
			String DEPT_ID = invoiceHJson.getString("DEPT_ID");
			String AR_CONTRACT_ID = invoiceHJson.getString("AR_CONTRACT_ID");
			String AMOUNT = invoiceHJson.getString("AMOUNT");
			String TAX_AMT = invoiceHJson.getString("TAX_AMT");
			String TAX_RATE = invoiceHJson.getString("TAX_RATE");
			String INV_AMOUNT = invoiceHJson.getString("INV_AMOUNT");
			String CANCEL_AMT = invoiceHJson.getString("CANCEL_AMT");
			String INV_NO = invoiceHJson.getString("INV_NO");
			String DESCRIPTION = invoiceHJson.getString("DESCRIPTION");

			//专用发票中客户纳税识别号必须存在，如果没有选择客户时给出提示：该客户没有纳税识别号，不能录如增值税专用发票
			if(XCARConstants.ZZS_HZ.equals(AR_DOC_CAT_CODE) || XCARConstants.ZZS.equals(AR_DOC_CAT_CODE)){
				ArCustomerBean arCustomer = xcarCommonDao.getArCustomer(CUSTOMER_ID);
				if(arCustomer.getTAX_NO() == null || "".equals(arCustomer.getTAX_NO())){
					throw new Exception("客户："+arCustomer.getCUSTOMER_NAME()+"没有纳税识别号，不能录入增值税专用发票，请重新选择！");
					//throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE_IS_NOT_NULL", null));//没有纳税识别号，不能录入增值税专用发票，请重新选择！
				}
			}
			arInvoiceH.setAR_INV_H_ID(AR_INV_H_ID);
			arInvoiceH.setAR_DOC_CAT_CODE(AR_DOC_CAT_CODE);
			arInvoiceH.setL_AR_INV_H_ID(L_AR_INV_H_ID);
			arInvoiceH.setLEDGER_ID(LEDGER_ID);
			arInvoiceH.setBIZ_DATE(sdf.parse(BIZ_DATE));
			arInvoiceH.setCUSTOMER_ID(CUSTOMER_ID);
			arInvoiceH.setPROJECT_ID(PROJECT_ID);
			arInvoiceH.setDEPT_ID(DEPT_ID);
			arInvoiceH.setAR_CONTRACT_ID(AR_CONTRACT_ID);
			
			if(INV_AMOUNT != null && !"".equals(INV_AMOUNT))
				arInvoiceH.setINV_AMOUNT(Double.parseDouble(INV_AMOUNT));
			if(AMOUNT != null && !"".equals(AMOUNT))
				arInvoiceH.setAMOUNT(Double.parseDouble(AMOUNT));
			if(TAX_RATE != null && !"".equals(TAX_RATE))
				arInvoiceH.setTAX_RATE(Double.parseDouble(TAX_RATE));
			if(TAX_AMT != null && !"".equals(TAX_AMT))
				arInvoiceH.setTAX_AMT(Double.parseDouble(TAX_AMT));
			if(CANCEL_AMT != null && !"".equals(CANCEL_AMT))
				arInvoiceH.setCANCEL_AMT(Double.parseDouble(CANCEL_AMT));
			arInvoiceH.setINV_NO(INV_NO);
			arInvoiceH.setDESCRIPTION(DESCRIPTION);
			arInvoiceH.setCREATED_BY(userId);
			arInvoiceH.setCREATION_DATE(createDate);
			arInvoiceH.setLAST_UPDATE_DATE(createDate);
			arInvoiceH.setLAST_UPDATED_BY(userId);
			
			//销售发票行信息
			JSONArray invoiceLJson = new JSONArray(rowTabInfoJson);
			ArInvoiceLBean arInvoiceL = null;
			//销售发票删除行
			JSONArray deleteDtlJson = new JSONArray(deleteDtlInfo);
			List<ArInvoiceLBean> addInvoiceLs = new ArrayList<ArInvoiceLBean>();
			List<ArInvoiceLBean> updateInvoiceLs = new ArrayList<ArInvoiceLBean>();
			List<String> deleteInvoices = new ArrayList<String>();
			if(deleteDtlJson != null && deleteDtlJson.length() > 0){
				for(int i = 0;i < deleteDtlJson.length();i++){
					JSONObject delJson = deleteDtlJson.getJSONObject(i);
					String delDtlId = delJson.getString("AR_INV_L_ID");
					deleteInvoices.add(delDtlId);
				}
				arInvoiceH.setDeleteInvoiceLs(deleteInvoices);;
			}
			if(invoiceLJson != null && invoiceLJson.length() > 0){
				for(int i = 0;i < invoiceLJson.length();i++){
					JSONObject lJson = invoiceLJson.getJSONObject(i);
					arInvoiceL = new ArInvoiceLBean();
					
					arInvoiceL.setAR_INV_H_ID(AR_INV_H_ID);
					arInvoiceL.setAR_SALE_TYPE_ID(lJson.getString("AR_SALE_TYPE_ID"));
					arInvoiceL.setL_AR_INV_H_ID(lJson.getString("L_AR_INV_H_ID"));
					arInvoiceL.setL_AR_INV_L_ID(lJson.getString("L_AR_INV_L_ID"));
					arInvoiceL.setPRODUCT_ID(lJson.getString("PRODUCT_ID"));
					arInvoiceL.setMODEL(lJson.getString("MODEL"));
					
					if(lJson.getString("PRICE") != null && !"".equals(lJson.getString("PRICE")))
						arInvoiceL.setPRICE(Double.parseDouble(lJson.getString("PRICE")));
					if(lJson.getString("INV_PRICE") != null && !"".equals(lJson.getString("INV_PRICE")))
						arInvoiceL.setINV_PRICE(Double.parseDouble(lJson.getString("INV_PRICE")));
					if(lJson.getString("AMOUNT") != null && !"".equals(lJson.getString("AMOUNT")))
						arInvoiceL.setAMOUNT(Double.parseDouble(lJson.getString("AMOUNT")));
					if(lJson.getString("INV_AMOUNT") != null && !"".equals(lJson.getString("INV_AMOUNT")))
						arInvoiceL.setINV_AMOUNT(Double.parseDouble(lJson.getString("INV_AMOUNT")));
					if(lJson.getString("TAX_AMT") != null && !"".equals(lJson.getString("TAX_AMT")))
						arInvoiceL.setTAX_AMT(Double.parseDouble(lJson.getString("TAX_AMT")));
					if(lJson.getString("TAX") != null && !"".equals(lJson.getString("TAX")))
						arInvoiceL.setTAX(Double.parseDouble(lJson.getString("TAX")));
					
					arInvoiceL.setQTY(lJson.getInt("QTY"));
					arInvoiceL.setDIM_CODE(lJson.getString("DIM_CODE"));
					arInvoiceL.setDESCRIPTION(lJson.getString("DESCRIPTION"));
					arInvoiceL.setCREATED_BY(userId);
					arInvoiceL.setCREATION_DATE(createDate);
					arInvoiceL.setLAST_UPDATE_DATE(createDate);
					arInvoiceL.setLAST_UPDATED_BY(userId);

					String AR_INV_L_ID = lJson.getString("AR_INV_L_ID");
					if(AR_INV_L_ID != null && !"".equals(AR_INV_L_ID)){//更新处理
						arInvoiceL.setAR_INV_L_ID(AR_INV_L_ID);
						updateInvoiceLs.add(arInvoiceL);
					}
					else{//新增处理
						arInvoiceL.setAR_INV_L_ID(java.util.UUID.randomUUID().toString());
						addInvoiceLs.add(arInvoiceL);
					}
				}
				arInvoiceH.setAddInvoiceL(addInvoiceLs);
				arInvoiceH.setUpdateInvoiceL(updateInvoiceLs);
			}
			else{
				throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE_DTL_IS_NOT_NULL", null));//销售发票明细不能为空，请添加销售发票明细！
			}
			
			//通过销售发票ID得到销售发票信息
			ArInvoiceHBean arInvoiceHBean = xcarCommonDao.getArInvoiceH(AR_INV_H_ID);
			//根据数据库中是否存在当前操作的销售发票，来判断执行数据保存或更新处理
			if(arInvoiceHBean != null && !"".equals(arInvoiceHBean)){
				arInvoiceBaseDao.updateInvoice(arInvoiceH);
				
				json.put("arDocCode",arInvoiceHBean.getAR_INV_H_CODE());
				json.put("flag","0");
				json.put("msg",XipUtil.getMessage(language, "XC_AR_INVOICE_SAVE_SUCCESS", null));//销售发票保存成功！
				
			}else{
				//增值税普通发票红字，增值税专用发票红字
				if((XCARConstants.ZZS_HZ.equals(AR_DOC_CAT_CODE) || XCARConstants.ZZSPT_HZ.equals(AR_DOC_CAT_CODE)) && (L_AR_INV_H_ID != null && !"".equals(L_AR_INV_H_ID))){
					//红字发票需要判断应收单的未收金额是否还够冲销当前红色应收单。如果不够需要提示，如果够冲销，需要在冲销完后修改蓝色应收单的未收金额和未申请金额。
					String judReturn = xcarCommonDao.judgmentInvoiceAmount(L_AR_INV_H_ID,Double.parseDouble(INV_AMOUNT));
					if(judReturn != null && !"".equals(judReturn)){
						throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_SAVE_AMOUNT_FAILED", null));//红冲金额超过未收金额，不能进行冲销！
					}
				}
				String year = BIZ_DATE.substring(0,4);								//年
				String month = BIZ_DATE.substring(5,7);								//月
				//得到单据编码
				String arDocCode = ruleService.getNextSerialNum("AR_INV", LEDGER_ID, "", year, month, userId);
				arInvoiceH.setAR_INV_H_CODE(arDocCode);
				arInvoiceBaseDao.saveInvoice(arInvoiceH);
				
				json.put("arDocCode",arDocCode);
				json.put("flag","0");
				json.put("msg",XipUtil.getMessage(language, "XC_AR_INVOICE_SAVE_SUCCESS", null));//销售发票保存成功！
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}
	/*
	 * 
	  * <p>Title deleteInvoice</p>
	  * <p>Description 删除销售发票</p>
	  * @param jsonArray
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArInvoiceBaseService#deleteInvoice(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteInvoice(String jsonArray,String language) throws Exception {
		try {
			if(jsonArray == null || "".equals(jsonArray)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数不能为空
			}
			else{
				JSONArray ja = new JSONArray(jsonArray);
				if(ja != null && ja.length() > 0){
					List<String> invoices = new ArrayList<String>();
					for(int i = 0;i < ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						String invoice = jo.getString("AR_INV_H_ID");
						
						//通过销售发票ID得到销售发票信息
						ArInvoiceHBean apInvoiceHBean = xcarCommonDao.getArInvoiceH(invoice);
						
						String sysAuditStatus = apInvoiceHBean.getSYS_AUDIT_STATUS();//业务状态
						String apInvHCode = apInvoiceHBean.getAR_INV_H_CODE();//销售发票的编号

						//判断销售发票的流程状态
						if(XCARConstants.STATUS_C.equals(sysAuditStatus)){//审批中
							throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_01", null));//"销售发票" + apInvHCode + "正在审批中，不能删除！"
						}
						if(XCARConstants.STATUS_E.equals(sysAuditStatus)){//审批完成
							throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_02", null));//"销售发票" + apInvHCode + "审批流程已经完成，不能删除！"
						}
						if(XCARConstants.STATUS_F.equals(sysAuditStatus)){//已复核
							throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_03", null));//"销售发票" + apInvHCode + "已复核，不能删除！"
						}
						//通过销售发票ID判断销售发票是否被核销预付过
						int invoicePreCount = arInvoiceBaseDao.getInvoicePreCount(invoice);
						if(invoicePreCount > 0){
							throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AR_INVOICE_DELETE_04", null));//"销售发票" + apInvHCode + "已经核销预付过，不能删除！"
						}
						//通过销售发票ID判断当前销售发票是否是另一个红字发票的蓝字发票
						int lInvoiceCount = arInvoiceBaseDao.getLInvoiceCount(invoice);
						if(lInvoiceCount > 0){
							throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_05", null));//"销售发票" + apInvHCode + "是另一个发票的蓝字发票，不能删除！"
						}
						//通过销售发票ID判断当前销售发票是否被应收单引用
						int invGlhCount = arInvoiceBaseDao.getInvGlHCount(invoice);
						if(invGlhCount > 0){
							throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AR_INVOICE_DELETE_06", null));//"销售发票" + apInvHCode + "已经被应收单引用，不能删除！"
						}
						invoices.add(invoice);
					}
					//执行删除销售发票处理
					if(invoices.size() > 0 && invoices != null){
						arInvoiceBaseDao.deleteInvoices(invoices);
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
	  * <p>Title printArInvoice</p>
	  * <p>Description 对销售发票进行开票（作废）处理</p>
	  * @param jsonArray
	  * @param opType
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArInvoiceBaseService#printArInvoice(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void printArInvoice(String jsonArray,String opType,String language) throws Exception {
		try {
			if(jsonArray == null || "".equals(jsonArray)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数不能为空
			}
			else{
				JSONArray ja = new JSONArray(jsonArray);
				List<ArInvoiceHBean> arInvoiceHList = new ArrayList<ArInvoiceHBean>();
				if(ja != null && ja.length() > 0){
					for(int i = 0;i < ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						String invoice = jo.getString("AR_INV_H_ID");
						//通过销售发票ID得到销售发票信息
						ArInvoiceHBean arInvoiceHBean = xcarCommonDao.getArInvoiceH(invoice);
						ArInvoiceHBean arInvoiceH = new ArInvoiceHBean();
						arInvoiceH.setAR_INV_H_ID(invoice);
						arInvoiceH.setLAST_UPDATED_BY(CurrentUserUtil.getCurrentUserId());
						arInvoiceH.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						if("0".equals(opType)){//未开票进行开票处理
							arInvoiceH.setPRINT_USER_ID(CurrentUserUtil.getCurrentUserId());
							arInvoiceH.setPRINT_DATE(CurrentUserUtil.getCurrentDate());
							arInvoiceH.setPRINT_STATUS("1");
						}
						else if("1".equals(opType)){//已经开票进行反处理
							arInvoiceH.setPRINT_USER_ID(null);
							arInvoiceH.setPRINT_DATE(null);
							arInvoiceH.setPRINT_STATUS("0");
						}
						else if("2".equals(opType)){//对销售发票进行作废处理
							if("2".equals(arInvoiceHBean.getPRINT_STATUS()))
								throw new Exception(XipUtil.getMessage(language, "XC_AR_INVOICE", null) + arInvoiceHBean.getAR_INV_H_CODE() + XipUtil.getMessage(language, "XC_AR_INVOICE_VOID_01", null));//"销售发票" + arInvoiceHBean.getAR_INV_H_CODE() + "已经作废了，不能再次作废！"
							arInvoiceH.setPRINT_STATUS("2");
							arInvoiceH.setPRINT_USER_ID(arInvoiceHBean.getPRINT_USER_ID());
							arInvoiceH.setPRINT_DATE(arInvoiceHBean.getPRINT_DATE());
						}
						arInvoiceHList.add(arInvoiceH);
					}
					//执行对销售发票开票处理
					arInvoiceBaseDao.printArInvoice(arInvoiceHList);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title getInvoiceProcAndForms</p>
	  * <p>Description 得到销售发票模块中的流程相关信息</p>
	  * @param apDocCatCode
	  * @param ledgerId
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArInvoiceBaseService#getInvoiceProcAndForms(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject getInvoiceProcAndForms(String apDocCatCode,String ledgerId,String language) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			//查询账簿级销售发票中的流程相关信息
			HashMap<String,String> apLdProMap = arInvoiceBaseDao.getInvoiceLdProcessInfo(apDocCatCode,ledgerId);
			
			String processId = apLdProMap.get("PROCESS_ID");
			String processCode = apLdProMap.get("PROCESS_CODE");
			String ruleCode = apLdProMap.get("RULE_CODE");
			String attCatCode = apLdProMap.get("ATT_CAT_CODE");
			
			jo.put("PROCESS_ID",processId);
			jo.put("PROCESS_CODE",processCode);
			jo.put("RULE_CODE",ruleCode);
			jo.put("ATT_CAT_CODE",attCatCode);
			
			//查询全局销售发票中的流程相关信息
			HashMap<String,String> apProMap = arInvoiceBaseDao.getInvoiceProcessInfo(apDocCatCode);
			
			if(processId == null || "".equals(processId)){
				String gbProcId = apProMap.get("PROCESS_ID");
				String gbProcCode = apProMap.get("PROCESS_CODE");
				if(gbProcId == null || "".equals(gbProcId)){
					//throw new Exception("单据类型：" + apProMap.get("AR_CAT_NAME") + "未设置审批流程，请在单据类型设置功能处设置！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT", null)+apProMap.get("AR_CAT_NAME")+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PROCESS_IS_NOT_NULL", null));
				}
				else{
					jo.put("PROCESS_ID",gbProcId);
					jo.put("PROCESS_CODE",gbProcCode);
				}
			}
			jo.put("AR_CAT_CODE",apProMap.get("AR_CAT_CODE"));		//销售发票类型编码
			jo.put("AR_CAT_NAME",apProMap.get("AR_CAT_NAME"));		//销售发票类型名称
			jo.put("PC_W_FORM_ID",apProMap.get("PC_W_FORM_ID"));	//电脑端填报表单ID
			jo.put("PC_W_FORM_URL",apProMap.get("PC_W_FORM_URL")); 	//电脑端填报表单地址
			jo.put("PC_A_FORM_ID",apProMap.get("PC_A_FORM_ID"));  	//电脑端审批表单ID
			jo.put("PC_A_FORM_URL",apProMap.get("PC_A_FORM_URL"));	//电脑端审批表单地址
			jo.put("PC_P_FORM_ID",apProMap.get("PC_P_FORM_ID"));	//电脑端打印表单ID
			jo.put("PC_P_FORM_URL",apProMap.get("PC_P_FORM_URL"));	//电脑端打印表单地址
			jo.put("M_W_FORM_ID",apProMap.get("M_W_FORM_ID"));		//移动端填报表单ID
			jo.put("M_W_FORM_URL",apProMap.get("M_W_FORM_URL"));	//移动端填报表单地址
			jo.put("M_A_FORM_ID",apProMap.get("M_A_FORM_ID"));		//移动端审批表单ID
			jo.put("M_A_FORM_URL",apProMap.get("M_A_FORM_URL"));	//移动端审批表单地址
			jo.put("ORG_ID",apLdProMap.get("ORG_ID"));				//组织ID
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
}