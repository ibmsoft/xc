package com.xzsoft.xc.po.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.po.service.PoArrivalService;
import com.xzsoft.xip.wf.common.util.WFUtil;

/** 
 * @ClassName: PoArrivalAction
 * @Description:到货单Action
 * @author weicw
 * @date 2016年12月22日 下午2:41:29 
 * 
 */
@Controller
@RequestMapping("/poArrivalAction.do")
public class PoArrivalAction {
	@Resource
	PoArrivalService service;
	/**
	 * 
	  * @Title saveArrivalInfo 方法名
	  * @Description 保存到货单
	  * @param 
	  * @return 
	 */
	@RequestMapping(params="method=saveArrivalInfo")
	public void saveArrivalInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String formStr = request.getParameter("formStr");
			String gridStr = request.getParameter("gridStr");
			String deleteIds = request.getParameter("deleteIds");
			String lineNums = request.getParameter("lineNums");
			jo = service.saveArrivalInfo(formStr, gridStr, deleteIds, lineNums);
		} catch (Exception e) {
	
		}
		WFUtil.outPrint(response, jo.toString());
	}
	/**
	 * 
	  * @Title deleteArrivalInfo 方法名
	  * @Description 删除到货单
	  * @param 
	  * @return 
	 */
	@RequestMapping(params="method=deleteArrivalInfo")
	public void deleteArrivalInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String deleteIds = request.getParameter("deleteIds");
			jo = service.deleteArrivalInfo(deleteIds);
		} catch (Exception e) {
			// TODO: handle exception
		}
		WFUtil.outPrint(response, jo.toString());	
	}
}
