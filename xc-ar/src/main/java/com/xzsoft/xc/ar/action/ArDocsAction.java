package com.xzsoft.xc.ar.action;

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

import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.service.ArAuditService;
import com.xzsoft.xc.ar.service.ArDocsAndVoucherService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;
/**
 * 
  * @ClassName ArDocsAction
  * @Description 应收模块单据处理信息
  * @author 任建建
  * @date 2016年7月6日 上午11:38:44
 */
@Controller("/arDocsAction.do")
public class ArDocsAction {
	public static final Logger log = Logger.getLogger(ArDocsAction.class);
	@Resource
	private RuleService ruleService;
	@Resource
	private XCARCommonDao xcarCommonDao;
	@Resource
	private ArDocsAndVoucherService arDocsAndVoucherService;
	@Resource
	private ArAuditService arAuditService;
	/**
	 * 
	  * @Title getArDocCode 方法名
	  * @Description 得到应收模块的单据编码
	  * @param request
	  * @param response
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params ="method=getArDocCode",method=RequestMethod.POST)
	public void getArDocCode(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			//获取前台传过来的参数信息
			String ledgerId = request.getParameter("ledgerId");				//账簿ID
			String arDocCatCode = request.getParameter("arDocCatCode");		//单据类型编码
			String date = request.getParameter("date");						//业务日期
			String userId = request.getParameter("userId");					//当前登录人
			
			String year = date.substring(0,4);								//年
			String month = date.substring(5,7);								//月
			
			//得到编码规则
			String ruleCode = null;
			if("ZZS".equals(arDocCatCode) || "ZZS-HZ".equals(arDocCatCode) || "ZZSPT".equals(arDocCatCode) || "ZZSPT-HZ".equals(arDocCatCode))
				ruleCode = "AR_INV";
			if("YSD".equals(arDocCatCode) || "YSD-HZ".equals(arDocCatCode))
				ruleCode = "AR_INV_GL";
			if("SKD_JSK".equals(arDocCatCode) || "SKD_JSK-H".equals(arDocCatCode) || "SKD_QTSK".equals(arDocCatCode) || "SKD_QTSK-H".equals(arDocCatCode) || "SKD_YUSK".equals(arDocCatCode) || "SKD_YUSK-H".equals(arDocCatCode))
				ruleCode = "AR_PAY";
			if("YSHLDC".equals(arDocCatCode) || "YSHYF".equals(arDocCatCode) || "YSHYUS".equals(arDocCatCode) || "YUSHLDC".equals(arDocCatCode))
				ruleCode = "AR_CANCEL";
			//得到单据编码
			String arDocCode = ruleService.getNextSerialNum(ruleCode, ledgerId, "", year, month, userId);
			//提示信息
			jo.put("flag","0");
			jo.put("arDocCode",arDocCode);
			jo.put("msg","");
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
			jo.put("arDocCode","");
			log.error(e.getMessage());
		} 
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title createDocsAndVoucher 方法名
	  * @Description 应收模块生成单据和凭证的方法
	  * arDocClass：1、YSD 应收单；2、SKD 收款单；3、HXD 核销单；4、TZD 调整单
	  * mainTabInfoJson：单据主信息
	  * rowTabInfoJson：单据行信息
	  * deleteDtlInfo：是否删除行信息
	  * @param request
	  * @param response
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=createDocsAndVoucher",method=RequestMethod.POST)
	public void createDocsAndVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//得到前台传过来的参数
			String mainTabInfoJson = request.getParameter("mainTabInfo");//单据主信息
			String rowTabInfoJson = request.getParameter("rowTabInfo");//单据行信息
			String deleteDtlInfo = request.getParameter("deleteDtl");//行信息删除
			jo = arDocsAndVoucherService.createDocsAndVoucher(mainTabInfoJson, rowTabInfoJson, deleteDtlInfo,language);
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_SAVE_FAILED", null)+e.getMessage());//凭证保存失败：
		}
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title checkArVoucher
	  * @Description 凭证审核
	  * 参数格式：
	  * {
	  * 	"arId" : ""			//待处理的ID
	  * 	"arCatCode" : ""	//单据类型
	  * }
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=checkArVoucher",method=RequestMethod.POST)
	public void checkArVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//待审核的单据信息（包括单据ID、单据大类编码、凭证头ID）
			String arInfo = request.getParameter("arInfo");
			JSONArray ja = new JSONArray(arInfo);
			//凭证审核
			arDocsAndVoucherService.checkArVoucher(ja,language);
			
			//提示信息
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_CHECK_SUCCESS", null));//凭证审核成功！
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_CHECK_FAILED", null)+e.getMessage());//凭证审核失败：
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title cancelCheckArVoucher
	  * @Description 取消凭证审核
	  * 参数格式：
	  * {
	  * 	"arId" : ""			//待处理的ID
	  * 	"arCatCode" : ""	//单据类型
	  * }
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=cancelCheckArVoucher",method=RequestMethod.POST)
	public void cancelCheckArVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//待取消审核的单据信息（包括单据ID、单据大类编码、凭证头ID）
			String arInfo = request.getParameter("arInfo");
			JSONArray ja = new JSONArray(arInfo);
			//取消凭证审核
			arDocsAndVoucherService.cancelCheckArVoucher(ja,language);
			
			//提示信息
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_CANCEL_CHECK_SUCCESS", null));//凭证取消审核成功！
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_CANCEL_CHECK_FAILED", null)+e.getMessage());//凭证取消审核失败：
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title signArVoucher
	  * @Description 签字
	  * 参数格式：
	  * {
	  * 	"arId" : ""			//待处理的ID
	  * 	"arCatCode" : ""	//单据类型
	  * }
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=signArVoucher",method=RequestMethod.POST)
	public void signArVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//待签字的单据信息（包括单据ID、单据大类编码、凭证头ID）
			String arInfo = request.getParameter("arInfo");
			JSONArray ja = new JSONArray(arInfo);
			//取消凭证审核
			arDocsAndVoucherService.signArVoucher(ja,language);
			
			//提示信息
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_SIGN_SUCCESS", null));//凭证签字成功！
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_SIGN_FAILED", null)+e.getMessage());//凭证签字失败：
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title cancelSignArVoucher
	  * @Description 取消签字
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=cancelSignArVoucher",method=RequestMethod.POST)
	public void cancelSignArVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//凭证头信息
			String arInfo = request.getParameter("arInfo");
			JSONArray ja = new JSONArray(arInfo);
			//取消凭证审核
			arDocsAndVoucherService.cancelSignArVoucher(ja,language);
			
			//提示信息
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_CANCEL_SIGN_SUCCESS", null));//凭证取消签字成功！
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_CANCEL_SIGN_FAILED", null)+e.getMessage());//凭证取消签字失败：
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title cancelFinArInvGl 方法名
	  * @Description 对应收单进行取消复核处理
	  * @param request
	  * @param response 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=cancelFinArInvGl",method=RequestMethod.POST)
	public void cancelFinArInvGl(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//凭证头信息
			String arInfo = request.getParameter("arInfo");
			JSONArray ja = new JSONArray(arInfo);
			//取消凭证审核
			arDocsAndVoucherService.cancelFinArInvGl(ja,language);
			
			//提示信息
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_SUCCESS", null));//单据取消复核成功！
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_FAILED", null)+e.getMessage());//取消复核失败：
			log.error(e.getMessage());
				
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title deleteArDoc
	  * @Description 删除单据（包括验证是否有凭证（删除凭证），是否存在明细（存在明细不能删除），凭证审核是否通过（审核通过也不能删除），交易明细表也要删除）
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=deleteArDoc",method=RequestMethod.POST)
	public void deleteArDoc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			String arInfo = request.getParameter("arInfo");
			JSONArray ja = new JSONArray(arInfo);
			arDocsAndVoucherService.deleteArDoc(ja,language);
			
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
	  * @Title revokeArCompleteProcess 方法名
	  * @Description 流程审批通过之后再次撤回
	  * @param request
	  * @param response
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=revokeArCompleteProcess",method=RequestMethod.POST)
	public void revokeArCompleteProcess(HttpServletRequest request,HttpServletResponse response) throws Exception{
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
			arAuditService.revokeCompleteProcess(docId,docCat,cancelReason,language);
			
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_PROCESS_RETURN_SUCCESS", null));//流程退回成功！
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_AP_PROCESS_RETURN_FAILED", null)+e.getMessage());//流程退回失败：
		}
		WFUtil.outPrint(response,jo.toString()); 
	}
}