package com.xzsoft.xc.po.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.po.service.PoCommonService;
import com.xzsoft.xip.platform.util.PlatformUtil;

/** 
 * @ClassName: PoCommonAction
 * @Description: 采购管理公共Action层 
 * @author weicw
 * @date 2016年12月12日 上午9:54:26 
 * 
 */
@Controller
@RequestMapping("/poCommonAction.do")
public class PoCommonAction {
	@Resource
	PoCommonService service;
	/**
	 * @Title:getProcAndFormsByCat
	 * @Description:获取账簿费用单据信息
	 * 参数格式:
	 * @param  
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=getProcAndFormsByCat")
	public void getProcAndFormsByCat(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String ledgerId = request.getParameter("ledgerId");//账簿ID
			String docCat = request.getParameter("docCat");//单据类型
			jo = service.getProcAndFormsByCat(ledgerId, docCat);
		} catch (Exception e) {
			
		}
		PlatformUtil.outPrint(response, jo.toString());
	}
}
