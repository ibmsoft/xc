package com.xzsoft.xc.ap.action;

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

import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.ap.modal.ApInvGlLBean;
import com.xzsoft.xc.ap.service.ApInvGlCommonService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/** 
 * @className ApInvGlCommonAction
 * @Description 应付单Action
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午1:02:37 
 */
@Controller
@RequestMapping("/apInvGlCommonAction.do")
public class ApInvGlCommonAction {
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	ApInvGlCommonService service;
	/**
	 * @Title glCount
	 * @Description: 应付期初总账取数
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@RequestMapping(params="method=glCount")
	public void glCount(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String invGlH = request.getParameter("invGlH");//前台传过来的参数
			jo = service.glCount(invGlH);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
	
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title split
	 * @Description: 应付期初拆分
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@RequestMapping(params="method=split")
	public void split(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String jsonStr = request.getParameter("json");//获取前台转过来的参数
			
			jo = service.split(jsonStr);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
		
	}
	/**
	 * @Title deleteInfo
	 * @Description: 删除应付单
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@RequestMapping(params="method=deleteInfo")
	public void deleteInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("arry1");
			jo = service.deleteInfo(ids,language);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title delInvGlHInfo
	 * @Description: 单独删除明细
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@RequestMapping(params="method=delInvGlHInfo")
	public void delInvGlHInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("arry1");
			jo = service.delInvGlHInfo(ids);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title glCountYuF
	 * @Description: 预付期初总账取数
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@RequestMapping(params="method=glCountYuF")
	public void glCountYuF(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String payH = request.getParameter("payH");//前台传过来的参数
			jo = service.glCountYuF(payH);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title splitYuF
	 * @Description: 预付期初拆分
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@RequestMapping(params="method=splitYuF")
	public void splitYuS(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String jsonStr = request.getParameter("json");//获取前台转过来的参数
			jo = service.splitYuF(jsonStr);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		
		WFUtil.outPrint(response, jo.toString());
		
	}
	/**
	 * @Title deleteInfoYuF
	 * @Description: 预付期初删除信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@RequestMapping(params="method=deleteInfoYuF")
	public void deleteInfoYuF(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("arry1");
			jo = service.deleteInfoYuF(ids,language);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title delPayHInfo
	 * @Description: 预付期初单独删除明细信息
	 * 参数格式:
	 * @param java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@RequestMapping(params="method=delPayHInfo")
	public void delPayHInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("arry1");
			jo = service.delPayHInfo(ids);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title balanceInv
	 * @Description: 应付记账 
	 * 参数格式:
	 * @param 
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=balanceInv")
	public void balanceInv(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");
			jo = service.balanceInv(ids);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
		
	}
	/**
	 * @Title cancelBalanceInv
	 * @Description: 应付取消记账 
	 * 参数格式:
	 * @param 
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=cancelBalanceInv")
	public void cancelBalanceInv(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");
			jo = service.cancelBalanceInv(ids,language);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	
	}
	/**
	 * @Title balancePay
	 * @Description: 预付记账 
	 * 参数格式:
	 * @param 
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=balancePay")
	public void balancePay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");
			jo = service.balancePay(ids);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
		
	}
	/**
	 * @Title cancelBalancePay
	 * @Description: 预付取消记账 
	 * 参数格式:
	 * @param 
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=cancelBalancePay")
	public void cancelBalancePay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");
			jo = service.cancelBalancePay(ids,language);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response, jo.toString());
	
	}
}
