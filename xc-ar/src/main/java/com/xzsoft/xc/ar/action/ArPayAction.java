/**
 * @Title:ArPayAction.java
 * @package:com.xzsoft.xc.ar.action
 * @Description:TODO
 * @date:2016年7月7日下午2:49:30
 * @version V1.0
 */
package com.xzsoft.xc.ar.action;


import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.ar.service.ArPayBaseService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * @ClassName: ArPayAction
 * @Description: TODO
 * @author:zhenghy
 * @date 2016年7月7日 下午2:49:30
 */
@Controller("/arPayAction.do")
public class ArPayAction {
	
	public static final Logger log = Logger.getLogger(ArPayAction.class);
	@Resource
	ArPayBaseService arPayBaseService;
	
	/**
	 * @Title: ArPayAction
	 * @Description: 删除收款单
	 * @param @param request
	 * @param @param response
	 * @param @throws Exception 
	 * @return 
	 * @throws
	 */
	@RequestMapping(params="method=deleteArPay")
	public void deleteArPay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
			String language = sessionVars.get(SessionVariable.language);//多语言
			//获取参数
			String deleteRecs = request.getParameter("deleteRecs");
			//跳转到service层
			jo = arPayBaseService.deleteArPay(deleteRecs,language);
			
			jo.put("flag", "0");
			jo.put("msg", "删除成功！");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
	
	/**
	 * @Title: saveArPay
	 * @Description: 保存收款单
	 * @param request
	 * @param response
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	@RequestMapping(params="method=saveArPay")
	public void saveArPay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
			String language = sessionVars.get(SessionVariable.language);//多语言
			//获取参数
			String opType = request.getParameter("opType");
			String arPayH = request.getParameter("arPayHJson");
			String newRecs = request.getParameter("newRecs");
			String updateRecs = request.getParameter("updateRecs");
			String deleteRecs = request.getParameter("deleteRecs");
			//跳转到service层
			jo =  arPayBaseService.saveArPay(opType, arPayH, newRecs, updateRecs, deleteRecs,language);
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
	
}
