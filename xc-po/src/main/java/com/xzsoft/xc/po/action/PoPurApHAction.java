package com.xzsoft.xc.po.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.po.service.PoPurApHService;
import com.xzsoft.xip.platform.util.PlatformUtil;

/** 
 * @ClassName: PoPurApHAction
 * @Description: 采购申请Action
 * @author weicw
 * @date 2016年12月5日 下午2:25:38 
 * 
 */
@Controller
@RequestMapping("/poPurApHAction.do")
public class PoPurApHAction {
	@Resource
	PoPurApHService service;
	/**
	 * @Title:saveInfo
	 * @Description:保存采购申请
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=saveInfo")
	public void saveInfo(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String formStr = request.getParameter("formStr");
			String gridStr = request.getParameter("gridStr");
			String deleteIds = request.getParameter("deleteIds");
			jo = service.saveInfo(formStr, gridStr, deleteIds);
		} catch (Exception e) {
			try {
				jo.put("flag", "1");
				jo.put("msg", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		PlatformUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title:deleteInfo
	 * @Description:删除采购申请
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=deleteInfo")
	public void deleteInfo(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");
			jo = service.deleteInfo(ids);
		} catch (Exception e) {
			
		}
		PlatformUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title:editPurIsClose
	 * @Description:关闭或打开采购申请单 
	 * 参数格式:
	 * @param  java.util.HashMap
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=editPurIsClose")
	public void editPurIsClose(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");//id数组
			String operate = request.getParameter("IS_CLOSE");//是否关闭状态
			jo = service.editPurIsClose(ids, operate);
		} catch (Exception e) {
			
		}
		PlatformUtil.outPrint(response, jo.toString());
	}
}
