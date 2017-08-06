/**
 * @Title:ApPayBaseAction.java
 * @package:com.xzsoft.xc.ap.action
 * @Description:TODO
 * @date:2016年8月5日下午5:50:38
 * @version V1.0
 */
package com.xzsoft.xc.ap.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.ap.service.ApPayBaseService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * @ClassName: ApPayBaseAction
 * @Description: 应付模块付款单相关操作
 * @author:zhenghy
 * @date: 2016年8月5日 下午5:50:38
 */
@Controller("/apPayBaseAction.do")
public class ApPayBaseAction {
	public static final Logger log = Logger.getLogger(ApPayBaseAction.class);
	
	@Resource
	ApPayBaseService apPayBaseService;
	
	/**
	 * @Title: saveApPay
	 * @Description: 保存付款单
	 * @param request
	 * @param response
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	@RequestMapping(params ="method=saveApPay",method=RequestMethod.POST)
	public void saveApPay(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
			String language = sessionVars.get(SessionVariable.language);//多语言
			String apPayH = request.getParameter("apPayH");
			String newPayLs = request.getParameter("newRecs");
			String updatePayLs = request.getParameter("updateRecs");
			String deletePayLs = request.getParameter("deleteRecs");
			String opType = request.getParameter("opType");
			jo = apPayBaseService.saveApPay(apPayH, newPayLs, updatePayLs, deletePayLs, opType,language);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
	
	/**
	 * @Title: deleteApPay
	 * @Description: 删除付款单
	 * @param request
	 * @param response
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	@RequestMapping(params ="method=deleteApPay",method=RequestMethod.POST)
	public void deleteApPay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
			String language = sessionVars.get(SessionVariable.language);//多语言
			String deleteRecs = request.getParameter("deleteRecs");
			//多语言未完成
			jo = apPayBaseService.deleteApPay(deleteRecs,language);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
}
