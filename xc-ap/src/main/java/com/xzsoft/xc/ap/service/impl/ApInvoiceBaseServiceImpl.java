package com.xzsoft.xc.ap.service.impl;

import java.math.BigDecimal;
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

import com.xzsoft.xc.ap.dao.ApCheckUtilDao;
import com.xzsoft.xc.ap.dao.ApInvoiceBaseDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.modal.ApInvoiceHBean;
import com.xzsoft.xc.ap.modal.ApInvoiceLBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.ap.service.ApInvoiceBaseService;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
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
@Service("apInvoiceBaseService")
public class ApInvoiceBaseServiceImpl implements ApInvoiceBaseService {
	public static final Logger log = Logger.getLogger(ApInvoiceBaseServiceImpl.class);
	@Resource
	ApInvoiceBaseDao apInvoiceBaseDao;
	@Resource
	RuleService ruleService;
	@Resource
	private XCAPCommonDao xcapCommonDao;
	@Resource
	private ApCheckUtilDao apCheckUtilDao;
	@Resource
	private VoucherHandlerService voucherHandlerService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * 
	  * <p>Title getApAttCatCode</p>
	  * <p>Description 得到应付模块的附件分类编码</p>
	  * @param apDocCatCode
	  * @param ledgerId
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvoiceBaseService#getApAttCatCode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getApAttCatCode(String apDocCatCode, String ledgerId,String language) throws Exception {
		String attCatCode = "";
		try {
			//查询账簿级采购发票中的流程相关信息
			HashMap<String,String> apAttCatCode = apInvoiceBaseDao.getInvoiceLdProcessInfo(apDocCatCode,ledgerId);
			String apCatName = apAttCatCode.get("AP_CAT_NAME");
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
	  * <p>Description 采购发票保存和更新方法</p>
	  * @param mainTabInfoJson
	  * @param rowTabInfoJson
	  * @param deleteDtlInfo
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvoiceBaseService#saveOrUpdateInvoice(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveOrUpdateInvoice(String mainTabInfoJson,String rowTabInfoJson,String deleteDtlInfo,String language) throws Exception {
		JSONObject json = new JSONObject();
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			if(mainTabInfoJson == null || "".equals(mainTabInfoJson))
				throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE_IS_NOT_NULL", null));//采购发票信息不能为空！
			//采购发票主信息
			ApInvoiceHBean apInvoiceH = new ApInvoiceHBean();
			JSONObject invoiceHJson = new JSONObject(mainTabInfoJson);

			String AP_INV_H_ID = invoiceHJson.getString("AP_INV_H_ID");
			String AP_DOC_CAT_CODE = invoiceHJson.getString("AP_DOC_CAT_CODE");
			String L_AP_INV_H_ID = invoiceHJson.getString("L_AP_INV_H_ID");
			String LEDGER_ID = invoiceHJson.getString("LEDGER_ID");
			String BIZ_DATE = invoiceHJson.getString("BIZ_DATE");
			String VENDOR_ID = invoiceHJson.getString("VENDOR_ID");
			String PROJECT_ID = invoiceHJson.getString("PROJECT_ID");
			String DEPT_ID = invoiceHJson.getString("DEPT_ID");
			String AP_CONTRACT_ID = invoiceHJson.getString("AP_CONTRACT_ID");
			String TAX_AMT = invoiceHJson.getString("TAX_AMT");
			String TAX_RATE = invoiceHJson.getString("TAX_RATE");
			String CANCEL_AMT = invoiceHJson.getString("CANCEL_AMT");
			String INV_NO = invoiceHJson.getString("INV_NO");
			String DESCRIPTION = invoiceHJson.getString("DESCRIPTION");
			String SOURCE = invoiceHJson.getString("SOURCE");
			
			double srcAmt = 0;

			apInvoiceH.setAP_INV_H_ID(AP_INV_H_ID);
			apInvoiceH.setAP_DOC_CAT_CODE(AP_DOC_CAT_CODE);
			apInvoiceH.setL_AP_INV_H_ID(L_AP_INV_H_ID);
			apInvoiceH.setLEDGER_ID(LEDGER_ID);
			apInvoiceH.setBIZ_DATE(sdf.parse(BIZ_DATE));
			apInvoiceH.setVENDOR_ID(VENDOR_ID);
			apInvoiceH.setPROJECT_ID(PROJECT_ID);
			apInvoiceH.setDEPT_ID(DEPT_ID);
			apInvoiceH.setAP_CONTRACT_ID(AP_CONTRACT_ID);
			if(TAX_RATE != null && !"".equals(TAX_RATE))
				apInvoiceH.setTAX_RATE(Double.parseDouble(TAX_RATE));
			if(CANCEL_AMT != null && !"".equals(CANCEL_AMT))
				apInvoiceH.setCANCEL_AMT(Double.parseDouble(CANCEL_AMT));
			apInvoiceH.setINV_NO(INV_NO);
			apInvoiceH.setDESCRIPTION(DESCRIPTION);
			apInvoiceH.setCREATED_BY(userId);
			apInvoiceH.setCREATION_DATE(createDate);
			apInvoiceH.setLAST_UPDATE_DATE(createDate);
			apInvoiceH.setLAST_UPDATED_BY(userId);
			apInvoiceH.setSOURCE(SOURCE);
			
			//采购发票行信息
			JSONArray invoiceLJson = new JSONArray(rowTabInfoJson);
			//采购发票删除行
			JSONArray deleteDtlJson = new JSONArray(deleteDtlInfo);
			List<ApInvoiceLBean> apInvoiceLs = new ArrayList<ApInvoiceLBean>();
			List<ApInvoiceLBean> apUpdateInvoiceLs = new ArrayList<ApInvoiceLBean>();
			List<String> apDeleteInvoices = new ArrayList<String>();
			if(deleteDtlJson != null && deleteDtlJson.length() > 0){
				for(int i = 0;i < deleteDtlJson.length();i++){
					JSONObject delJson = deleteDtlJson.getJSONObject(i);
					String delDtlId = delJson.getString("AP_INV_L_ID");
					apDeleteInvoices.add(delDtlId);
				}
				apInvoiceH.setApInvoiceLs(apDeleteInvoices);
			}
			if(invoiceLJson != null && invoiceLJson.length() > 0){
				for(int i = 0;i < invoiceLJson.length();i++){
					JSONObject lJson = invoiceLJson.getJSONObject(i);
					ApInvoiceLBean apInvoiceL = new ApInvoiceLBean();
					
					apInvoiceL.setAP_INV_H_ID(AP_INV_H_ID);
					apInvoiceL.setAP_PUR_TYPE_ID(lJson.getString("AP_PUR_TYPE_ID"));
					apInvoiceL.setBG_ITEM_ID(lJson.getString("BG_ITEM_ID"));
					apInvoiceL.setORDER_H_ID(lJson.getString("ORDER_H_ID"));
					apInvoiceL.setORDER_L_ID(lJson.getString("ORDER_L_ID"));
					apInvoiceL.setMATERIAL_ID(lJson.getString("MATERIAL_ID"));
					apInvoiceL.setAMOUNT(lJson.getDouble("AMOUNT"));
					apInvoiceL.setQTY(lJson.getDouble("QTY"));
					apInvoiceL.setDIM_CODE(lJson.getString("DIM_CODE"));
					apInvoiceL.setDESCRIPTION(lJson.getString("DESCRIPTION"));
					apInvoiceL.setCREATED_BY(userId);
					apInvoiceL.setCREATION_DATE(createDate);
					apInvoiceL.setLAST_UPDATE_DATE(createDate);
					apInvoiceL.setLAST_UPDATED_BY(userId);

					BigDecimal rowAmt = new BigDecimal(lJson.getDouble("AMOUNT"));
					BigDecimal rowAmts = rowAmt.setScale(2,BigDecimal.ROUND_HALF_UP);
					srcAmt = srcAmt + rowAmts.doubleValue();
					String AP_INV_L_ID = lJson.getString("AP_INV_L_ID");
					if(AP_INV_L_ID != null && !"".equals(AP_INV_L_ID)){//更新处理
						apInvoiceL.setAP_INV_L_ID(AP_INV_L_ID);
						apUpdateInvoiceLs.add(apInvoiceL);
					}
					else{//新增处理
						apInvoiceL.setAP_INV_L_ID(java.util.UUID.randomUUID().toString());
						apInvoiceLs.add(apInvoiceL);
					}
				}
				apInvoiceH.setApInvoiceL(apInvoiceLs);
				apInvoiceH.setUpdateInvoiceL(apUpdateInvoiceLs);
			}
			else{
				throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE_DTL_IS_NOT_NULL", null));//采购发票明细不能为空，请添加采购发票明细！
			}
			if(TAX_AMT == null || "".equals(TAX_AMT)){
				apInvoiceH.setAMOUNT(srcAmt);
				apInvoiceH.setINV_AMOUNT(srcAmt);
			}
			else{
				apInvoiceH.setTAX_AMT(Double.parseDouble(TAX_AMT));
				BigDecimal taxAmt = new BigDecimal(Double.parseDouble(TAX_AMT));
				BigDecimal taxAmts = taxAmt.setScale(2,BigDecimal.ROUND_HALF_UP);
				srcAmt = srcAmt + taxAmts.doubleValue();
				apInvoiceH.setAMOUNT(srcAmt);
				apInvoiceH.setINV_AMOUNT(srcAmt);
			}

			//通过采购发票ID得到采购发票信息
			ApDocumentHBean apInvoiceHBean = xcapCommonDao.getApInvoiceH(AP_INV_H_ID);
			//红字发票需要判断应付单的未付金额是否还够冲销当前红色发票。如果不够需要提示，如果够冲销，需要在冲销完后修改应付单的未付金额和未申请金额。
			if(XCAPConstants.CGFP_H.equals(AP_DOC_CAT_CODE) && (L_AP_INV_H_ID != null && !"".equals(L_AP_INV_H_ID))){//普通发票红字或者增值税专用发票红字
				String returnStr = xcapCommonDao.judgmentInvoiceAmount(L_AP_INV_H_ID,srcAmt - ((apInvoiceHBean == null) ? 0 : apInvoiceHBean.getINV_AMOUNT()));
				if(returnStr != null && !"".equals(returnStr)){
					throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_SAVE_AMOUNT_FAILED", null));//红冲金额超过未付金额，不能进行冲销！
				}
			}
			//根据数据库中是否存在当前操作的采购发票，来判断执行数据保存或更新处理
			if(apInvoiceHBean != null && !"".equals(apInvoiceHBean)){
				apInvoiceBaseDao.updateInvoice(apInvoiceH);
				
				json.put("apDocCode",apInvoiceHBean.getAP_INV_H_CODE());
				json.put("srcAmt",srcAmt);
				json.put("flag","0");
				json.put("msg",XipUtil.getMessage(language, "XC_AP_INVOICE_SAVE_SUCCESS", null));//采购发票保存成功！
				
			}else{
				String year = BIZ_DATE.substring(0,4);								//年
				String month = BIZ_DATE.substring(5,7);								//月
				//得到单据编码
				String apDocCode = ruleService.getNextSerialNum("AP_INV", LEDGER_ID, "", year, month, userId);
				apInvoiceH.setAP_INV_H_CODE(apDocCode);
				apInvoiceBaseDao.saveInvoice(apInvoiceH);
				
				json.put("apDocCode",apDocCode);
				json.put("srcAmt",srcAmt);
				json.put("flag","0");
				json.put("msg",XipUtil.getMessage(language, "XC_AP_INVOICE_SAVE_SUCCESS", null));//采购发票保存成功！
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
	  * <p>Description 删除采购发票</p>
	  * @param jsonArray
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvoiceBaseService#deleteInvoice(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteInvoice(String jsonArray,String language) throws Exception {
		try {
			if(jsonArray == null || "".equals(jsonArray)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
			}
			else{
				JSONArray ja = new JSONArray(jsonArray);
				if(ja != null && ja.length() > 0){
					List<String> invoices = new ArrayList<String>();
					//更新采购订单行的list
					List<ApVoucherHandlerBean> updateOrderLLists = new ArrayList<ApVoucherHandlerBean>();
					for(int i = 0;i < ja.length();i++){
						JSONObject jo = ja.getJSONObject(i);
						String invoice = jo.getString("AP_INV_H_ID");
						
						//通过采购发票ID得到采购发票信息
						ApDocumentHBean apInvoiceHBean = xcapCommonDao.getApInvoiceH(invoice);
						
						String sysAuditStatus = apInvoiceHBean.getSYS_AUDIT_STATUS();//业务状态
						String apInvHCode = apInvoiceHBean.getAP_INV_H_CODE();//采购发票的编号

						//判断采购发票的流程状态
						if(XCAPConstants.STATUS_C.equals(sysAuditStatus)){//审批中
							throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_01", null));//"采购发票" + apInvHCode + "正在审批中，不能删除！"
						}
						if(XCAPConstants.STATUS_E.equals(sysAuditStatus)){//审批完成
							throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_02", null));//"采购发票" + apInvHCode + "审批流程已经完成，不能删除！"
						}
						if(XCAPConstants.STATUS_F.equals(sysAuditStatus)){//已复核
							throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_03", null));//"采购发票" + apInvHCode + "已复核，不能删除！"
						}
						//通过采购发票ID判断采购发票是否被核销预付过
						int invoicePreCount = apInvoiceBaseDao.getInvoicePreCount(invoice);
						if(invoicePreCount > 0){
							throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_04", null));//"采购发票" + apInvHCode + "已经核销预付过，不能删除！"
						}
						//通过采购发票ID判断当前采购发票是否是另一个红字发票的蓝字发票
						int lInvoiceCount = apInvoiceBaseDao.getLInvoiceCount(invoice);
						if(lInvoiceCount > 0){
							throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_05", null));//"采购发票" + apInvHCode + "是另一个发票的蓝字发票，不能删除！"
						}
						//通过采购发票ID判断当前采购发票是否被应付单引用
						int invGlhCount = apInvoiceBaseDao.getInvGlHCount(invoice);
						if(invGlhCount > 0){
							throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE", null) + apInvHCode + XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_06", null));//"采购发票" + apInvHCode + "已经被应付单引用，不能删除！"
						}
						invoices.add(invoice);
						//回写采购订单行里面的已开票金额
						if(XCAPConstants.CGFP_CGDD.equals(apInvoiceHBean.getAP_DOC_CAT_CODE()) || XCAPConstants.CGFP_CGDD_H.equals(apInvoiceHBean.getAP_DOC_CAT_CODE())){
							//根据采购发票行ID得到采购发票行信息
							for (ApDocumentLBean apDocumentL : xcapCommonDao.getApInvoiceL(invoice)) {
								ApVoucherHandlerBean xcApCommonBean = new ApVoucherHandlerBean();
								xcApCommonBean.setPriKey(apDocumentL.getORDER_L_ID());
								xcApCommonBean.setAMOUNT(apDocumentL.getAMOUNT() * -1);
								updateOrderLLists.add(xcApCommonBean);
							}
						}
					}
					//更新采购订单行表金额
					if(updateOrderLLists != null && updateOrderLLists.size() > 0)
						xcapCommonDao.updatePoOrderLAmtInfo(updateOrderLLists);
					//执行删除采购发票处理
					if(invoices.size() > 0 && invoices != null)
						apInvoiceBaseDao.deleteInvoices(invoices);
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
	  * <p>Description 得到采购发票模块中的流程相关信息</p>
	  * @param apDocCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvoiceBaseService#getInvoiceProcAndForms(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject getInvoiceProcAndForms(String apDocCatCode,String ledgerId) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			//查询账簿级采购发票中的流程相关信息
			HashMap<String,String> apLdProMap = apInvoiceBaseDao.getInvoiceLdProcessInfo(apDocCatCode,ledgerId);
			
			String processId = apLdProMap.get("PROCESS_ID");
			String processCode = apLdProMap.get("PROCESS_CODE");
			String ruleCode = apLdProMap.get("RULE_CODE");
			String attCatCode = apLdProMap.get("ATT_CAT_CODE");
			
			jo.put("PROCESS_ID",processId);
			jo.put("PROCESS_CODE",processCode);
			jo.put("RULE_CODE",ruleCode);
			jo.put("ATT_CAT_CODE",attCatCode);
			
			//查询全局采购发票中的流程相关信息
			HashMap<String,String> apProMap = apInvoiceBaseDao.getInvoiceProcessInfo(apDocCatCode);
			
			if(processId == null || "".equals(processId)){
				String gbProcId = apProMap.get("PROCESS_ID");
				String gbProcCode = apProMap.get("PROCESS_CODE");
				if(gbProcId == null || "".equals(gbProcId)){
					//throw new Exception("单据类型：" + apProMap.get("AP_CAT_NAME") + "未设置审批流程，请在单据类型设置功能处设置");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT", null)+apProMap.get("AP_CAT_NAME")+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PROCESS_IS_NOT_NULL", null));
				}
				else{
					jo.put("PROCESS_ID",gbProcId);
					jo.put("PROCESS_CODE",gbProcCode);
				}
			}
			jo.put("AP_CAT_CODE",apProMap.get("AP_CAT_CODE"));		//采购发票类型编码
			jo.put("AP_CAT_NAME",apProMap.get("AP_CAT_NAME"));		//采购发票类型名称
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
	/*
	 * 
	  * <p>Title signOrCancelApInvoice</p>
	  * <p>Description 对采购发票签收或者取消签收处理</p>
	  * @param jsonArray
	  * @param opType
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvoiceBaseService#signOrCancelApInvoice(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void signOrCancelApInvoice(String jsonArray,String opType,String language) throws Exception {
		try {
			if(jsonArray == null || "".equals(jsonArray)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
			}
			else{
				JSONArray ja = new JSONArray(jsonArray);
				List<ApInvoiceHBean> apInvoiceHList = new ArrayList<ApInvoiceHBean>();
				if(ja != null && ja.length() > 0){
					for(int i = 0;i < ja.length();i++){
						ApInvoiceHBean apInvoiceH = new ApInvoiceHBean();
						JSONObject jo = ja.getJSONObject(i);
						String invoice = jo.getString("AP_INV_H_ID");
						//通过采购发票ID得到采购发票信息
						ApDocumentHBean apInvoiceHBean = xcapCommonDao.getApInvoiceH(invoice);
						//opType操作类型
						apInvoiceH.setAP_INV_H_ID(invoice);
						apInvoiceH.setLAST_UPDATED_BY(CurrentUserUtil.getCurrentUserId());
						apInvoiceH.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						if("1".equals(opType)){//未签收进行签收处理
							apInvoiceH.setSIGN_USER_ID(CurrentUserUtil.getCurrentUserId());
							apInvoiceH.setSIGN_DATE(CurrentUserUtil.getCurrentDate());
							apInvoiceH.setSIGN_STATUS("2");
						}
						else if("2".equals(opType)){//已经签收进行取消签收处理
							if(apInvoiceHBean.getFIN_USER_ID() != null && !"".equals(apInvoiceHBean.getFIN_USER_ID()))
								throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE", null) + apInvoiceHBean.getAP_INV_H_CODE() + XipUtil.getMessage(language, "XC_AP_INVOICE_SIGN_02", null));//"采购发票" + apInvoiceHBean.getAP_INV_H_CODE() + "已经复核了，不能取消签收！"
							apInvoiceH.setSIGN_USER_ID(null);
							apInvoiceH.setSIGN_DATE(null);
							apInvoiceH.setSIGN_STATUS("1");
						}
						else if("3".equals(opType)){//对采购发票进行拒签处理
							if("3".equals(apInvoiceHBean.getSIGN_STATUS()))
								throw new Exception(XipUtil.getMessage(language, "XC_AP_INVOICE", null) + apInvoiceHBean.getAP_INV_H_CODE() + XipUtil.getMessage(language, "XC_AP_INVOICE_SIGN_01", null));//"采购发票" + apInvoiceHBean.getAP_INV_H_CODE() + "已经拒签了，不能再次拒签！"
							//判断采购发票的流程状态
							/*if(XCAPConstants.STATUS_C.equals(apInvoiceHBean.getSYS_AUDIT_STATUS()))//审批中
								throw new Exception("采购发票" + apInvoiceHBean.getAP_INV_H_CODE()  + "正在审批中，不能拒签！");
							if(XCAPConstants.STATUS_E.equals(apInvoiceHBean.getSYS_AUDIT_STATUS()))//审批完成
								throw new Exception("采购发票" + apInvoiceHBean.getAP_INV_H_CODE()  + "审批流程已经完成，不能拒签！");
							if(XCAPConstants.STATUS_F.equals(apInvoiceHBean.getSYS_AUDIT_STATUS()))//已复核
								throw new Exception("采购发票" + apInvoiceHBean.getAP_INV_H_CODE()  + "已复核，不能拒签！");*/
							apInvoiceH.setSIGN_STATUS("3");
						}
						apInvoiceHList.add(apInvoiceH);
					}
					if(apInvoiceHList.size() > 0 && apInvoiceHList != null)
						//执行对采购发票签收处理
						apInvoiceBaseDao.signOrCancelApInvoice(apInvoiceHList);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}