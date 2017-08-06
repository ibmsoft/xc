package com.xzsoft.xc.gl.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.gl.service.LedgerService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * @ClassName: LedgerAction 
 * @Description: 账簿信息管理
 * @author linp
 * @date 2016年5月13日 下午2:53:14 
 *
 */
@Controller
@RequestMapping("/ledgerAction.do")
public class LedgerAction {
	
	@Resource
	private LedgerService ledgerService ;
	
	/**
	 * @Title: grant 
	 * @Description: 账簿授权管理
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=grant")
	public void grant(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		try {
			// 所选账簿信息  [{'LEDGER_ID':''},{'LEDGER_ID':''},...]
			String ldJsonArray = request.getParameter("ldArray") ;
			
			// 所选用户角色功能信息   [{'USER_ID':'','ROLE_ID':''},{'USER_ID':'','ROLE_ID':''},...]
			String urJsonArray = request.getParameter("urArray") ;
			
			// 授权处理
			ledgerService.grantOrRevoke2UserRole(ldJsonArray, urJsonArray, "grant");
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(lang, "XC_GL_GRANT_SUCCESS", null)) ;
			
		} catch (Exception e) {
			try {
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_GRANT_FAIL", null)+e.getMessage()) ;
				
			} catch (JSONException e1) {
			}
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: revoke 
	 * @Description: 取消账簿授权
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=revoke")
	public void revoke(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		try {
			// 所选账簿信息  [{'LEDGER_ID':''},{'LEDGER_ID':''},...]
			String ldJsonArray = request.getParameter("ldArray") ;
			
			// 所选用户角色功能信息   [{'USER_ID':'','ROLE_ID':''},{'USER_ID':'','ROLE_ID':''},...]
			String urJsonArray = request.getParameter("urArray") ;
			
			// 取消授权
			ledgerService.grantOrRevoke2UserRole(ldJsonArray, urJsonArray, "revoke");
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(lang, "XC_GL_CANCEL_GRANT_SUCCESS", null)) ;
			
		} catch (Exception e) {
			try {
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_CANCEL_GRANT_FAIL", null)+e.getMessage()) ;
				
			} catch (JSONException e1) {
			}
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: batchDelSec 
	 * @Description: 批量删除权限信息
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=batchDelSec")
	public void batchDelSec(HttpServletRequest request, HttpServletResponse response) {
	JSONObject jo = new JSONObject() ;
	HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
	String lang = sessionVars.get(SessionVariable.language);
		try {
			// 所选账簿信息  [{'LD_SEC_ID':''},{'LD_SEC_ID':''},...]
			String secJsonArray = request.getParameter("secArray") ;
			
			// 批量删除授权
			ledgerService.batchDelSec(secJsonArray);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(lang, "XC_GL_DELETE_GRANT_SUCCESS", null)) ;
			
		} catch (Exception e) {
			try {
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(lang, "XC_GL_DELETE_GRANT_FAIL", null)+e.getMessage()) ;
				
			} catch (JSONException e1) {
			}
		}
		
		WFUtil.outPrint(response,jo.toString());
	}

}
