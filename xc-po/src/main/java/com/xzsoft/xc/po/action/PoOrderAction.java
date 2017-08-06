package com.xzsoft.xc.po.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.po.service.PoOrderService;
import com.xzsoft.xip.wf.common.util.WFUtil;

/** 
 * @ClassName: PoOrderAction
 * @Description: 采购订单Action
 * @author weicw
 * @date 2016年12月15日 下午4:06:00 
 * 
 */
@Controller
@RequestMapping("/poOrderAction.do")
public class PoOrderAction {
	@Resource
	PoOrderService service;
	/**
	 * @Title:saveOrder
	 * @Description:保存采购订单  
	 * 参数格式:
	 * @param  
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=saveOrder")
	public void saveOrder(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String formStr = request.getParameter("formStr");//主信息
			String gridStr = request.getParameter("gridStr");//行信息
			String deleteIds = request.getParameter("deleteIds");//删除的行
			jo = service.saveOrder(formStr, gridStr, deleteIds);//,lineNums
		} catch (Exception e) {
			
			try {
				jo.put("flag", "1");
				jo.put("msg", e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			
			
		}
		WFUtil.outPrint(response, jo.toString());
	}
	
	/**
	 * @Title:updateOrerHStatus
	 * @Description:更改采购订单主信息是否关闭的状态 
	 * 参数格式:
	 * @param  
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=updateOrerHStatus")
	public void updateOrerHStatus(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");//主信息ID
			String operate = request.getParameter("operate");//是否关闭
			jo = service.updateOrerHStatus(ids, operate);
		} catch (Exception e) {

			
		}
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title:updateOrerLStatusByLId
	 * @Description:更改采购订单行信息是否关闭的状态 (根据行信息ID)
	 * 参数格式:
	 * @param  
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=updateOrerLStatusByLId")
	public void updateOrerLStatusByLId(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");//主信息ID
			String operate = request.getParameter("operate");//是否关闭
			jo = service.updateOrerLStatusByLId(ids, operate);
		} catch (Exception e) {

			
		}
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * @Title:deleteOrderHInfo
	 * @Description:删除采购订单主表信息 
	 * 参数格式:
	 * @param  java.lang.String(主信息ID)
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=deleteOrderHInfo")
	public void deleteOrderHInfo(HttpServletRequest request,HttpServletResponse response)  throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");//主信息ID
			jo = service.deleteOrderHInfo(ids);
		} catch (Exception e) {

			
		}
		WFUtil.outPrint(response, jo.toString());
	}
}
