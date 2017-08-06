package com.xzsoft.xc.ap.action;

import javax.annotation.Resource;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.ap.service.ApVendorService;
import com.xzsoft.xip.wf.common.util.WFUtil;


/** 
 * @ClassName: ApVendorAction 
 * @Description: 供应商分配Action
 * @author weicw 
 * @date 2016年11月24日 上午10:58:01 
 * 
 * 
 */
@Controller
@RequestMapping("/apVendorAction.do")
public class ApVendorAction {
	@Resource
	ApVendorService service;
	/**
	 * @Title:addLedgerVendor
	 * @Description:新增组织下的供应商
	 * 参数格式:
	 * @param  
	 * @return 
	 * @throws 
	 */
	@RequestMapping(params="method=addLedgerVendor")
	public void addLedgerVendor(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String str = request.getParameter("str");
			String orgId = request.getParameter("orgId");
			String ledgerId = request.getParameter("ledgerId");
			jo = service.addLedgerVendor(str,orgId,ledgerId);
		} catch (Exception e) {
			
		}
		WFUtil.outPrint(response, jo.toString());
	}
	@RequestMapping(params="method=deleteLedgerVendor")
	public void deleteLedgerVendor(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject();
		try {
			String ledgerId = request.getParameter("ledgerId");
			String orgId = request.getParameter("orgId");
			String vendorIds = request.getParameter("vendorIds");
			jo = service.deleteLedgerVendor(ledgerId,orgId,vendorIds);
		} catch (Exception e) {
			
		}
		WFUtil.outPrint(response, jo.toString());
	}
	
}

