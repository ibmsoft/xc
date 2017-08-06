package com.xzsoft.xc.po.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.po.service.PoReturnService;
import com.xzsoft.xip.wf.common.util.WFUtil;

/** 
 * @ClassName: PoReturnAction
 * @Description: 退货单Action
 * @author weicw
 * @date 2016年12月28日 上午11:18:59 
 * 
 */
@Controller
@RequestMapping("/poReturnAction.do")
public class PoReturnAction {
	@Resource
	 PoReturnService service;
	/**
	 * @Title:saveReturnData
	 * @Description:保存退货单
	 * 参数格式:
	 * @param  
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=saveReturnData")
	public void saveReturnData(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String formStr = request.getParameter("formStr");
			String gridStr = request.getParameter("gridStr");
			String deleteIds = request.getParameter("deleteIds");
			String lineNums = request.getParameter("lineNums");
			jo = service.saveReturnData(formStr, gridStr, deleteIds, lineNums);
		} catch (Exception e) {
			
		}
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title:delReturnData
	 * @Description:删除退货单
	 * 参数格式:
	 * @param  java.lang.String(主信息ID) 
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=delReturnData")
	public void delReturnData(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");
			jo = service.delReturnData(ids);
		} catch (Exception e) {
			
		}
		WFUtil.outPrint(response, jo.toString());
	}
}
