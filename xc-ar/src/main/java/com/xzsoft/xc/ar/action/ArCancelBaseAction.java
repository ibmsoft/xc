package com.xzsoft.xc.ar.action;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.modal.ArCancelHBean;
import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;
import com.xzsoft.xc.ar.service.ArCancelBaseService;
import com.xzsoft.xc.ar.service.ArDocsAndVoucherService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;



/** 
 * @className ArCancelBaseAction
 * @Description 核销单Action
 * @author 韦才文
 * @version 创建时间：2016年7月13日 上午10:02:49 
 */
@Controller
@RequestMapping("/arCancelBaseAction.do")
public class ArCancelBaseAction {

@Resource
ArCancelBaseService service;

/*
 * 保存核销单
 */
@RequestMapping(params="method=saveCancel")
public void saveCancel(HttpServletRequest request,HttpServletResponse response) throws Exception{
	HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
	try {
		String str = request.getParameter("cancelJson");//获取前台传过来的数据
		String deleteDtl = request.getParameter("deleteDtl");
		String gridStr = request.getParameter("grid");
		jo = service.saveCancel(str, deleteDtl, gridStr,language);
	} catch (Exception e) {
		jo.put("flag", "1");
		jo.put("msg", e.getMessage());
	}
	
	WFUtil.outPrint(response, jo.toString());
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
	
	PlatformUtil.outPrint(response, jo.toString());
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
	
	PlatformUtil.outPrint(response, jo.toString());
}
/*
 * 删除核销单
 */
@RequestMapping(params="method=deleteCancel")
public void deleteCancel(HttpServletRequest request,HttpServletResponse response) throws Exception{
	HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String rows = request.getParameter("rows");
			String arInfo = request.getParameter("arInfo");
			jo = service.deleteCancel(rows, arInfo,language);
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
}
