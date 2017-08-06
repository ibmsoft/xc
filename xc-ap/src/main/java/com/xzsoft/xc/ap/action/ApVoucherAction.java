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

import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.ap.service.ApVoucherService;
import com.xzsoft.xc.util.common.XCJSONUtil;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
  * @ClassName ApVoucherAction
  * @Description 应付模块Action层处理
  * @author 任建建
  * @date 2016年4月5日 下午7:07:45
 */
@Controller
@RequestMapping("/apVoucherAction.do")
public class ApVoucherAction {
	private static final Logger log = Logger.getLogger(ApVoucherAction.class);
	
	@Resource
	private ApVoucherService apVoucherService;
	/**
	 * 
	  * @Title createVoucher
	  * @Description 生成凭证
	  * 参数格式：
	  * {
	  * 	"ledgerId" : ""		//账簿ID
	  * 	"apId" : ""			//待处理的ID
	  * 	"accDate" : ""		//凭证日期
	  * 	"apCatCode" : ""	//单据类型
	  * }
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=createVoucher",method=RequestMethod.POST)
	public void createVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//传过来的参数
			String params = request.getParameter("params");
			ApVoucherHandlerBean avhb = XCJSONUtil.jsonToBean(params,ApVoucherHandlerBean.class);//json串转化为实体类
			//生成凭证的方法
			String returnJo = apVoucherService.newVoucher(avhb,language);
			//返回参数
			jo = new JSONObject(returnJo);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("headId","");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_SAVE_FAILED", null)+e.getMessage());//凭证保存失败：
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title checkVoucher
	  * @Description 凭证审核
	  * 参数格式：
	  * {
	  * 	"apId" : ""			//待处理的ID
	  * 	"apCatCode" : ""	//单据类型
	  * 	"hedaId" : ""		//凭证头ID
	  * }
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=checkVoucher",method=RequestMethod.POST)
	public void checkVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//待审核的单据信息（包括单据ID、单据大类编码、凭证头ID）
			String apInfo = request.getParameter("apInfo");
			JSONArray ja = new JSONArray(apInfo);
			//凭证审核
			apVoucherService.checkVoucher(ja,language);
			
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
	  * @Title cancelCheckVoucher
	  * @Description 取消凭证审核
	  * 参数格式：
	  * {
	  * 	"apId" : ""			//待处理的ID
	  * 	"apCatCode" : ""	//单据类型
	  * 	"hedaId" : ""		//凭证头ID
	  * }
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=cancelCheckVoucher",method=RequestMethod.POST)
	public void cancelCheckVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//待取消审核的单据信息（包括单据ID、单据大类编码、凭证头ID）
			String apInfo = request.getParameter("apInfo");
			JSONArray ja = new JSONArray(apInfo);
			//取消凭证审核
			apVoucherService.cancelCheckVoucher(ja,language);
			
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
	  * @Title signVoucher
	  * @Description 签字
	  * 参数格式：
	  * {
	  * 	"apId" : ""			//待处理的ID
	  * 	"apCatCode" : ""	//单据类型
	  * 	"hedaId" : ""		//凭证头ID
	  * }
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=signVoucher",method=RequestMethod.POST)
	public void signVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	    	language = sessionVars.get(SessionVariable.language);
			//待签字的单据信息（包括单据ID、单据大类编码、凭证头ID）
			String apInfo = request.getParameter("apInfo");
			JSONArray ja = new JSONArray(apInfo);
			//取消凭证审核
			apVoucherService.signVoucher(ja,language);
			
			//提示信息
			jo.put("flag","0");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_SIGN_SUCCESS", null));//凭证签字成功！
			
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_SIGN_FAILED", null)+e.getMessage());//凭证签字失败：
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}/**
	 * 
	  * @Title cancelSignVoucher
	  * @Description 取消签字
	  * @param request
	  * @param response	设定文件
	  * @return void	返回类型
	 */
	@RequestMapping(params="method=cancelSignVoucher",method=RequestMethod.POST)
	public void cancelSignVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String language = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
			language = sessionVars.get(SessionVariable.language);
			//凭证头信息
			String apInfo = request.getParameter("apInfo");
			JSONArray ja = new JSONArray(apInfo);
			//取消凭证审核
			apVoucherService.cancelSignVoucher(ja,language);
			
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
}