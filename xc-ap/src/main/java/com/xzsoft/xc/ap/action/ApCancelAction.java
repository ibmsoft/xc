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

import com.xzsoft.xc.ap.service.ApCancelService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
 * @ClassName: ApCancelAction 
 * @Description: 核销单相关操作
 * @author zhenghy
 * @date 2016年8月19日 下午3:58:35
 */
@Controller("/apCancelAction.do")
public class ApCancelAction {
	public static final Logger log = Logger.getLogger(ApCancelAction.class);
	
	@Resource
	ApCancelService apCancelService;
	/**
	 * @Title: saveCancel
	 * @Description: 保存核销单
	 * @param request
	 * @param response
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	@RequestMapping(params ="method=saveCancel",method=RequestMethod.POST)
	public void saveCancel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
			String language = sessionVars.get(SessionVariable.language);//多语言
			String cancelH = request.getParameter("cancelH");
			String newRecs = request.getParameter("newRecs");
			String updateRecs = request.getParameter("updateRecs");
			String deleteRecs = request.getParameter("deleteRecs");
			String opType = request.getParameter("opType");
			jo = apCancelService.saveCancel(cancelH, newRecs, updateRecs, deleteRecs, opType,language);
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
			
		}
		WFUtil.outPrint(response, jo.toString());
	}
	
	/**
	 * @Title: deleteCancel
	 * @Description: 删除核销单
	 * @param request
	 * @param response
	 * @throws Exception  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	@RequestMapping(params ="method=deleteCancel",method=RequestMethod.POST)
	public void deleteCancel(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
			String language = sessionVars.get(SessionVariable.language);//多语言
			String deleteRecs = request.getParameter("deleteRecs");
			jo = apCancelService.deleteCancel(deleteRecs,language);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
			log.error(e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	}
}
