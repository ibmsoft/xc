package com.xzsoft.xc.po.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.po.service.PoContractTypeService;
import com.xzsoft.xip.platform.util.PlatformUtil;

/** 
 * @ClassName: PoContractTypeAction 
 * @Description: 合同类型表Action
 * @author weicw 
 * @date 2016年11月29日 下午5:25:58 
 * 
 * 
 */
@Controller
@RequestMapping("/poContractTypeAction.do")
public class PoContractTypeAction {
	@Resource
	PoContractTypeService service;
	/**
	 * @Title:saveContractType
	 * @Description: 保存合同类型
	 * 参数格式:
	 * @param java.lang.String
	 * @return
	 * @throws 
	 */
	@RequestMapping(params="method=saveContractType")
	public void saveContractType(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String recordsStr = request.getParameter("records");
			String deleteStr = request.getParameter("deleteRecords");
			jo = service.saveContractType(recordsStr, deleteStr);
		} catch (Exception e) {
			
		}
		PlatformUtil.outPrint(response, jo.toString());
	}
}

