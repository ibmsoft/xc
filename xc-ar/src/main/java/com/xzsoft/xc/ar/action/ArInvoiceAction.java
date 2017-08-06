package com.xzsoft.xc.ar.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.ar.service.ArInvoiceBaseService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;
/**
 * 
  * @ClassName ArInvoiceAction
  * @Description 应收模块销售发票处理信息
  * @author 任建建
  * @date 2016年7月7日 上午8:58:40
 */
@Controller("/arInvoiceAction.do")
public class ArInvoiceAction {
	public static final Logger log = Logger.getLogger(ArInvoiceAction.class);
	
	@Resource
	ArInvoiceBaseService arInvoiceBaseService;
	/**
	 * 
	  * @Title getArAttCatCode 方法名
	  * @Description 得到应收模块的附件分类编码
	  * @param request
	  * @param response
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=getArAttCatCode")
	public void getArAttCatCode(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			String ledgerId = request.getParameter("LEDGER_ID");//账簿ID
			String arDocCatCode = request.getParameter("AR_DOC_CAT_CODE");//销售发票编码
			
			String attCatCode = arInvoiceBaseService.getArAttCatCode(arDocCatCode,ledgerId,language);
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
	  * @Title saveOrUpdateArInvoice 方法名
	  * @Description 销售发票保存和更新方法
	  * @param request
	  * @param response
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=saveOrUpdateArInvoice",method=RequestMethod.POST)
	public void saveOrUpdateArInvoice(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//得到前台传过来的参数
			String mainTabInfoJson = request.getParameter("mainTabInfo");//销售发票主信息
			String rowTabInfoJson = request.getParameter("rowTabInfo");//销售发票行信息
			String deleteDtlInfo = request.getParameter("deleteDtl");//销售发票行信息
			
			jo = arInvoiceBaseService.saveOrUpdateInvoice(mainTabInfoJson,rowTabInfoJson,deleteDtlInfo,language);
			jo.put("flag","0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_AR_INVOICE_SAVE_FAILED", null)+e.getMessage());//销售发票保存失败：
		}
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title deleteArInvoice 方法名
	  * @Description 删除销售发票
	  * @param request
	  * @param response
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=deleteArInvoice",method=RequestMethod.POST)
	public void deleteArInvoice(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//得到前台传过来的参数
			String invoiceId = request.getParameter("invoiceId");//销售发票ID
			
			arInvoiceBaseService.deleteInvoice(invoiceId,language);
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_AR_INVOICE_DELETR_SUCCESS", null));//销售发票删除成功！
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
	  * @Title printArInvoice 方法名
	  * @Description 对销售发票进行开票处理
	  * @param request
	  * @param response
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=printArInvoice",method=RequestMethod.POST)
	public void printArInvoice(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//得到前台传过来的参数
			String invoiceId = request.getParameter("invoiceId");//销售发票ID
			//操作类型
			String opType = request.getParameter("opType");//销售发票ID
			
			arInvoiceBaseService.printArInvoice(invoiceId,opType,language);
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_AR_INVOICE_PRINT_SUCCESS", null));//销售发票开票成功！
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
	  * @Title getArInvoiceProcAndForms 方法名
	  * @Description 得到销售发票模块中的流程相关信息
	  * @param request
	  * @param response 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=getArInvoiceProcAndForms",method=RequestMethod.POST)
	public void getArInvoiceProcAndForms(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			String ledgerId = request.getParameter("LEDGER_ID");//账簿ID
			String arDocCatCode = request.getParameter("AR_DOC_CAT_CODE");//销售发票编码
			
			jo = arInvoiceBaseService.getInvoiceProcAndForms(arDocCatCode,ledgerId,language);
			jo.put("flag","0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
}
