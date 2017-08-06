package com.xzsoft.xc.ar.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.ar.service.ArInvGlAdjService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;
 
/** 
 * 
 * @author 张龙
 * @version 2016年7月20日
 */
@Controller
@RequestMapping("/arInvGlAdjAction.do")
public class ArInvGlAdjAction {
	private static Logger log = Logger.getLogger(ArInvGlAdjAction.class.getName());
	@Resource
	ArInvGlAdjService service;
	/*
	 * 保存调整单
	 */
	@RequestMapping(params="method=saveInvAdj")
	public void saveInvAdj(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String params = request.getParameter("params");
			jo = service.saveInvAdj(params,language);//调用service的方法
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());	
		}
		
		PlatformUtil.outPrint(response,jo.toString());	
	}
	/*
	 * 删除调整单
	 */
	@RequestMapping(params="method=deleteAdj")
	public void deleteAdj(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String rows = request.getParameter("rows");
			String arInfo = request.getParameter("arInfo");
			jo = service.deleteAdj(rows, arInfo,language);
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());	
		}
		
		PlatformUtil.outPrint(response, jo.toString());
	}
	/*
	 * 凭证审核
	 */
	@RequestMapping(params="method=checkArVoucher")
	public void checkArVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String arInfo = request.getParameter("arInfo");
			jo  = service.checkArVoucher(arInfo,language);
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());	
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	/*
	 * 取消凭证审核
	 */
	@RequestMapping(params="method=cancelCheckArVoucher")
	public void cancelCheckArVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String arInfo = request.getParameter("arInfo");
			jo = service.cancelCheckArVoucher(arInfo,language);
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());	
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
}


