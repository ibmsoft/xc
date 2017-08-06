package com.xzsoft.xc.fa.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.fa.service.FaDepreciationService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.PlatformUtil;

@Controller
@RequestMapping("/faDepreciationAction.do")
public class FaDepreciationAction {
	private final Log log = LogFactory.getLog(getClass());
	@Resource
	private FaDepreciationService faDepreciationService;
	/**
	 * saveFaDepreciation:(资产计提折旧)
	 *
	 * @param request
	 * @param response
	 * @author zhaoxin
	 */
	@RequestMapping(params = "method=calcDepr")
	public void calcDepr(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	String language = sessionVars.get(SessionVariable.language);
    	String ledgerId = request.getParameter("ledgerId");
    	String periodCode = request.getParameter("periodCode");
    	JSONObject json = faDepreciationService.calFaDepr(ledgerId, periodCode, language, userId);
		PlatformUtil.outPrint(response, json.toString());
	} 
	
}
