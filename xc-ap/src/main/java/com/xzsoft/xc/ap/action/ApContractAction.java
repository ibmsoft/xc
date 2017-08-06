package com.xzsoft.xc.ap.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.ap.service.ApContractService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/** 
 * @ClassName: ApContractAction 
 * @Description: TODO
 * @author weicw 
 * @date 2016年8月19日 下午4:19:12 
 * 
 * 
 */
@Controller
@RequestMapping("/apContractAction.do")
public class ApContractAction {
	@Resource
	ApContractService service;
	/*
	 * 保存合同
	 */
	@RequestMapping(params="method=saveContarct")
	public void saveContarct(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		String info = request.getParameter("info");
		JSONObject jo  = new JSONObject();
		try {
			 jo = service.saveContarct(info,language);
		} catch (Exception e) {
			
		}
		
		PlatformUtil.outPrint(response, jo.toString());
	}
	/*
	 * 删除合同
	 */
	@RequestMapping(params="method=delContractInfo")
	public void delContractInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		String ids = request.getParameter("idArray");
		JSONObject jo = service.delContractInfo(ids,language);
		PlatformUtil.outPrint(response, jo.toString());
	}
	/*
	 * 关闭合同
	 */
	@RequestMapping(params="method=closeContract")
	public void closeContract(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		String ids = request.getParameter("idArray");
		JSONObject jo = service.closeContract(ids,language);
		PlatformUtil.outPrint(response, jo.toString());
	}
	/*
	 * 打开合同
	 */
	@RequestMapping(params="method=openContract")
	public void openContract(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		String ids = request.getParameter("idArray");
		JSONObject jo = service.openContract(ids,language);
		PlatformUtil.outPrint(response, jo.toString());
	}
}

