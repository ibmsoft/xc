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

import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArInvGlLBean;
import com.xzsoft.xc.ar.service.ArInvGlBaseService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/** 
 * @className ArInvGlBaseAction
 * @Description 应收单Action
 * @author 韦才文
 * @version 创建时间：2016年7月9日 下午1:02:37 
 */
@Controller
@RequestMapping("/arInvGlBaseAction.do")
public class ArInvGlBaseAction {
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	ArInvGlBaseService service;
	/*
	 * 总账取数
	 */
	@RequestMapping(params="method=glCount")
	public void glCount(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String invGlH = request.getParameter("invGlH");//前台传过来的参数
		jo = service.glCount(invGlH);
		WFUtil.outPrint(response, jo.toString());
	}
	/*
	 * 拆分
	 */
	@RequestMapping(params="method=split")
	public void split(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String jsonStr = request.getParameter("json");//获取前台转过来的参数
		JSONObject jo = new JSONObject();
		jo = service.split(jsonStr);
		WFUtil.outPrint(response, jo.toString());
		
	}
	/*
	 * 删除
	 */
	@RequestMapping(params="method=deleteInfo")
	public void deleteInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		String ids = request.getParameter("arry1");
		jo = service.deleteInfo(ids,language);
		WFUtil.outPrint(response, jo.toString());
	}
	/*
	 * 单独删除明细
	 */
	@RequestMapping(params="method=delInvGlHInfo")
	public void delInvGlHInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String ids = request.getParameter("arry1");
		jo = service.delInvGlHInfo(ids);
		WFUtil.outPrint(response, jo.toString());
	}
	/*
	 * 总账取数（预收）
	 */
	@RequestMapping(params="method=glCountYuS")
	public void glCountYuS(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String payH = request.getParameter("payH");//前台传过来的参数
		jo = service.glCountYuS(payH);
		WFUtil.outPrint(response, jo.toString());
	}
	/*
	 * 拆分
	 */
	@RequestMapping(params="method=splitYuS")
	public void splitYuS(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String jsonStr = request.getParameter("json");//获取前台转过来的参数
		JSONObject jo = new JSONObject();
		jo = service.splitYuS(jsonStr);
		WFUtil.outPrint(response, jo.toString());
		
	}
	/*
	 * 删除
	 */
	@RequestMapping(params="method=deleteInfoYuS")
	public void deleteInfoYuS(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		String ids = request.getParameter("arry1");
		jo = service.deleteInfoYuS(ids,language);
		WFUtil.outPrint(response, jo.toString());
	}
	/*
	 * 单独删除明细
	 */
	@RequestMapping(params="method=delPayHInfo")
	public void delPayHInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String ids = request.getParameter("arry1");
		jo = service.delPayHInfo(ids);
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title balanceInv
	 * @Description: 应收记账 
	 * 参数格式:
	 * @param 
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=balanceInv")
	public void balanceInv(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String ids = request.getParameter("ids");
		jo = service.balanceInv(ids);
		WFUtil.outPrint(response, jo.toString());
		
	}
	/**
	 * @Title cancelBalanceInv
	 * @Description: 应收取消记账 
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
		String ids = request.getParameter("ids");
		jo = service.cancelBalanceInv(ids,language);
		WFUtil.outPrint(response, jo.toString());
	
	}
	/**
	 * @Title balancePay
	 * @Description: 预收记账 
	 * 参数格式:
	 * @param 
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=balancePay")
	public void balancePay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		String ids = request.getParameter("ids");
		jo = service.balancePay(ids);
		WFUtil.outPrint(response, jo.toString());
		
	}
	/**
	 * @Title cancelBalancePay
	 * @Description: 预收取消记账 
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
		String ids = request.getParameter("ids");
		jo = service.cancelBalancePay(ids,language);
		WFUtil.outPrint(response, jo.toString());
	
	}
}
