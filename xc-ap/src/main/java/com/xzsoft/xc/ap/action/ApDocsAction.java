package com.xzsoft.xc.ap.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.service.ApAuditService;
import com.xzsoft.xc.ap.service.ApInvGlBaseService;
import com.xzsoft.xc.ap.service.ApInvoiceBaseService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;
/**
 * 
  * @ClassName ApDocsAction
  * @Description 应付模块单据处理信息
  * @author 任建建
  * @date 2016年6月15日 上午9:17:16
 */
@Controller("/apDocsAction.do")
public class ApDocsAction {
	public static final Logger log = Logger.getLogger(ApDocsAction.class);
	@Resource
	private RuleService ruleService;
	@Resource
	private XCAPCommonDao xcapCommonDao;
	@Resource
	private ApInvoiceBaseService apInvoiceBaseService;
	@Resource
	private ApInvGlBaseService apInvGlBaseService;
	@Resource
	private ApAuditService apAuditService;
	/**
	 * 
	  * @Title getApDocCode 方法名
	  * @Description 得到应付的单据编码
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params ="method=getApDocCode",method=RequestMethod.POST)
	public void getApDocCode(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			//获取前台传过来的参数信息
			String ledgerId = request.getParameter("ledgerId");				//账簿ID
			String apDocCatCode = request.getParameter("apDocCatCode");		//单据类型编码
			String date = request.getParameter("date");						//业务日期
			String userId = request.getParameter("userId");					//当前登录人
			
			String year = date.substring(0,4);								//年
			String month = date.substring(5,7);								//月
			
			//得到编码规则
			String ruleCode = null;
			if("PTFP".equals(apDocCatCode) || "ZZSFP".equals(apDocCatCode) || "PTFP-H".equals(apDocCatCode) || "ZZSFP-H".equals(apDocCatCode))
				ruleCode = "AP_INV";
			if("CGJS".equals(apDocCatCode) || "QTFK".equals(apDocCatCode) || "YUFK".equals(apDocCatCode))
				ruleCode = "AP_PAY_REQ";
			if("YFHYS".equals(apDocCatCode) || "YFHLDC".equals(apDocCatCode) || "YFHYUF".equals(apDocCatCode) || "YUFHLDC".equals(apDocCatCode))
				ruleCode = "AP_CANCEL";
			if("YFD".equals(apDocCatCode) || "YFD-H".equals(apDocCatCode))
				ruleCode = "AP_INV_GL";
			if("FKD_CGJS".equals(apDocCatCode) || "FKD_QTFK".equals(apDocCatCode) || "FKD_YUFK".equals(apDocCatCode) || "FKD_CGJS-H".equals(apDocCatCode) || "FKD_QTFK-H".equals(apDocCatCode) || "FKD_YUFK-H".equals(apDocCatCode))
				ruleCode = "AP_PAY";
			//得到单据编码
			String apDocCode = ruleService.getNextSerialNum(ruleCode,ledgerId,"",year,month,userId);
			//提示信息
			jo.put("flag","0");
			jo.put("apDocCode",apDocCode);
			jo.put("msg","");
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
			jo.put("apDocCode","");
			log.error(e.getMessage());
		} 
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title getApAttCatCode 方法名
	  * @Description 得到应付模块的附件分类编码
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=getApAttCatCode")
	public void getApAttCatCode(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			String ledgerId = request.getParameter("LEDGER_ID");//账簿ID
			String apDocCatCode = request.getParameter("AP_DOC_CAT_CODE");//采购发票编码
			
			String attCatCode = apInvoiceBaseService.getApAttCatCode(apDocCatCode,ledgerId,language);
			jo.put("flag","0");
			jo.put("attCatCode",attCatCode);
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title saveOrUpdateInvoice 方法名
	  * @Description 采购发票保存和更新方法
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=saveOrUpdateInvoice",method=RequestMethod.POST)
	public void saveOrUpdateInvoice(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//得到前台传过来的参数
			String mainTabInfoJson = request.getParameter("mainTabInfo");//采购发票主信息
			String rowTabInfoJson = request.getParameter("rowTabInfo");//采购发票行信息
			String deleteDtlInfo = request.getParameter("deleteDtl");//采购发票行信息
			
			jo = apInvoiceBaseService.saveOrUpdateInvoice(mainTabInfoJson,rowTabInfoJson,deleteDtlInfo,language);
			jo.put("flag","0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INVOICE_SAVE_FAILED", null)+e.getMessage());//采购发票保存失败：
		}
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title deleteInvoice 方法名
	  * @Description 删除采购发票
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=deleteInvoice",method=RequestMethod.POST)
	public void deleteInvoice(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//得到前台传过来的参数
			String invoiceId = request.getParameter("invoiceId");//采购发票ID
			
			apInvoiceBaseService.deleteInvoice(invoiceId,language);
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INVOICE_DELETE_SUCCESS", null));//采购发票删除成功！
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}

	/**
	 * 
	  * @Title signOrCancelApInvoice 方法名
	  * @Description 对采购发票签收或者取消签收处理
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=signOrCancelApInvoice",method=RequestMethod.POST)
	public void signOrCancelApInvoice(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//得到前台传过来的参数
			String invoiceId = request.getParameter("invoiceId");//采购发票ID
			//操作类型
			String opType = request.getParameter("opType");//操作类型：签收/取消签收
			
			apInvoiceBaseService.signOrCancelApInvoice(invoiceId,opType,language);
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INVOICE_SIGN_SUCCESS", null));//采购发票签收成功！
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title getInvoiceProcAndForms 方法名
	  * @Description 得到采购发票模块中的流程相关信息
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=getInvoiceProcAndForms",method=RequestMethod.POST)
	public void getInvoiceProcAndForms(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ledgerId = request.getParameter("LEDGER_ID");//账簿ID
			String apDocCatCode = request.getParameter("AP_DOC_CAT_CODE");//采购发票编码
			
			jo = apInvoiceBaseService.getInvoiceProcAndForms(apDocCatCode,ledgerId);
			jo.put("flag","0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title revokeCompleteProcess 方法名
	  * @Description 流程审批通过之后再次撤回
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=revokeCompleteProcess",method=RequestMethod.POST)
	public void revokeCompleteProcess(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//参数信息
			String docId = request.getParameter("docId");
			String docCat = request.getParameter("docCat");
			String cancelReason = request.getParameter("cancelReason");
			//执行单据取消
			apAuditService.revokeCompleteProcess(docId,docCat,cancelReason,language);
			
			jo.put("flag","0");
			jo.put("msg","");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_PROCESS_RETURN_SUCCESS", null));//流程退回成功！
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_PROCESS_RETURN_FAILED", null)+e.getMessage());//流程退回失败：
		}
		WFUtil.outPrint(response,jo.toString()); 
	}

	/**
	 * 
	  * @Title saveApInvGlAndVoucher 方法名
	  * @Description 应付单保存和更新方法
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=saveApInvGlAndVoucher",method=RequestMethod.POST)
	public void saveApInvGlAndVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//得到前台传过来的参数
			String mainTabInfoJson = request.getParameter("mainTabInfo");//应付单主信息
			String rowTabInfoJson = request.getParameter("rowTabInfo");//应付单行信息
			String deleteDtlInfo = request.getParameter("deleteDtl");//是否删除应付单行信息
			
			jo = apInvGlBaseService.saveApInvGlAndVoucher(mainTabInfoJson,rowTabInfoJson,deleteDtlInfo,language);
			jo.put("flag","0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INV_GL_SAVE_FAILED", null)+e.getMessage());//应付单保存失败：
		}
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title deleteApInvGl 方法名
	  * @Description 删除应付单
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=deleteApInvGl",method=RequestMethod.POST)
	public void deleteApInvGl(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			String apInfo = request.getParameter("apInfo");
			JSONArray ja = new JSONArray(apInfo);
			//删除
			apInvGlBaseService.deleteApDoc(ja,language);
			
			//提示信息
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_SUCCESS", null));//单据删除成功！
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_FAILED", null)+e.getMessage());//单据删除失败：
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title cancelFinApInvGl 方法名
	  * @Description 应付单取消复核处理
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=cancelFinApInvGl",method=RequestMethod.POST)
	public void cancelFinApInvGl(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			String apInfo = request.getParameter("apInfo");
			JSONArray ja = new JSONArray(apInfo);
			//删除
			apInvGlBaseService.cancelFinApInvGl(ja,language);
			
			//提示信息
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_SUCCESS", null));//单据取消复核成功！
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_FAILED", null)+e.getMessage());//单据取消复核失败：
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title checkBudget 方法名
	  * @Description 应付模块检查预算是否足够（采购发票、应付单、付款单-其他付款）
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=checkBudget",method=RequestMethod.POST)
	public void checkBudget(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//预算校验
//			String isChkBg = request.getParameter("isChkBg");//预算检查：Y-是，N-否
//			String apDocId = request.getParameter("apDocId");//应付模块ID（包括采购发票ID、应付单ID、付款单ID）
//			String apDocCat = request.getParameter("apDocCat");//类型（采购发票：CGFP、应付单：YFD、付款单：FKD）
			String checkArray = request.getParameter("checkArray");
			JSONArray ja = new JSONArray(checkArray);
			JSONObject resultJo = apInvGlBaseService.checkBudget(ja,language);
			
			//-1 校验不通过或者校验异常；0 校验通过；1校验不通过，但预算不是绝对控制，给出警告提示        
			String flag = resultJo.getString("flag"); 
			
			if("0".equals(flag)){ 
				jo.put("phase", "QRY");
			}
			else if("1".equals(flag)){ 
				jo.put("phase", "BGCHK");
				jo.put("chkFlag", flag);
				jo.put("msg", resultJo.get("msg"));
				
			}
			else{ 
				//预算校验未通过但属绝对控制
				jo.put("phase", "BGCHK");
				jo.put("chkFlag", flag);
				jo.put("msg", resultJo.get("msg"));
			}
			jo.put("flag", "0");
			
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
}