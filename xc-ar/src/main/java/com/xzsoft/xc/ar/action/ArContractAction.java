package com.xzsoft.xc.ar.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.ar.service.ArContractBaseService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.wf.common.util.WFUtil;



/** 
 * @className ArContractAction
 * @Description 合同管理Action
 * @author 韦才文
 * @version 创建时间：2016年7月7日 下午4:27:51 
 */
@Controller
@RequestMapping("/arContractAction.do")
public class ArContractAction {
	@Resource
	ArContractBaseService service;
	/**
	 * 
	  * @Title saveArContractInfo 方法名
	  * @Description 新增、修改、变更合同
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=saveArContractInfo",method=RequestMethod.POST)
	public void saveArContractInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			//得到前台传过来的参数
			String arContractArray = request.getParameter("json");//销售合同信息
			String opType = request.getParameter("opType");//销售合同信息
			jo = service.saveArContractInfo(arContractArray,opType);
			//提示信息
			jo.put("flag","0");
		} catch (Exception e) {
			jo.put("msg",e.getMessage());
			jo.put("flag","1");
		}
		//提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	/**
	 * 
	  * @Title delContractInfo 方法名
	  * @Description 删除合同
	  * @param request
	  * @param response
	  * @throws Exception
	  * @return void 返回类型
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params="method=delContractInfo")
	public void delContractInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String idsArray =  request.getParameter("ids");//获取前台传的id字符串
			jo =  service.delContractInfo(idsArray,language);
			//提示信息
			jo.put("flag","0");
		} catch (Exception e) {
			jo.put("msg",e.getMessage());
			jo.put("flag","1");
		}
		WFUtil.outPrint(response, jo.toString());
	}
}
