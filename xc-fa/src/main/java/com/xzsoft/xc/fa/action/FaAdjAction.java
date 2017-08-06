package com.xzsoft.xc.fa.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.fa.modal.FaAdj;
import com.xzsoft.xc.fa.service.FaAdjService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;


@Controller
@RequestMapping("/faAdjAction.do")
public class FaAdjAction {

	private final Log log = LogFactory.getLog(getClass());

	@Resource
	private FaAdjService faAdjService;
	
	/**
	 * saveFaAdj:(保存资产调整)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=saveFaAdj")
	public void saveFaAdj(@RequestBody FaAdj faAdj, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	String language = sessionVars.get(SessionVariable.language);
    	
    	HashMap<String, Object> extraParams = new HashMap<String, Object>();
    	extraParams.put("userId", userId);
    	extraParams.put("language", language);
    	
    	String returnJson = "{flag:0, msg:''}";
    	
		try {
			faAdjService.saveFaAdj(faAdj, extraParams);
		} catch (Exception e) {
			log.error(new Exception().getStackTrace()[0], e);
			if(e.getMessage()!=null){
				returnJson = "{flag:1, msg:\""+e.getMessage()+"\"}";
			}else{
				returnJson = "{flag:1, msg:\"\"}";
			}
		}
		
		PlatformUtil.outPrint(response, JSONUtil.Encode(returnJson));
	}
	
	/**
	 * summitFaAdj:(提交资产调整)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=summitFaAdj")
	public void summitFaAdj(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	String language = sessionVars.get(SessionVariable.language);
    	
    	HashMap<String, Object> extraParams = new HashMap<String, Object>();
    	extraParams.put("userId", userId);
    	extraParams.put("language", language);
    	
    	String adjHid = request.getParameter("adjHid");
    	
    	String returnJson = "{flag:0, msg:''}";
    	
		try {
			faAdjService.submitFaAdj(adjHid, extraParams);
		} catch (Exception e) {
			log.error(new Exception().getStackTrace()[0], e);
			if(e.getMessage()!=null){
				returnJson = "{flag:1, msg:\""+e.getMessage()+"\"}";
			}else{
				returnJson = "{flag:1, msg:\"\"}";
			}
		}
		
		PlatformUtil.outPrint(response, JSONUtil.Encode(returnJson));
	}
	
	/**
	 * revokeFaAdj:(撤销资产调整)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=revokeFaAdj")
	public void revokeFaAdj(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	String language = sessionVars.get(SessionVariable.language);
    	String jtType = request.getParameter("jtType");
    	
    	HashMap<String, Object> extraParams = new HashMap<String, Object>();
    	extraParams.put("userId", userId);
    	extraParams.put("language", language);
    	extraParams.put("jtType", jtType);
    	
    	String adjHid = request.getParameter("adjHid");
    	
    	String returnJson = "{flag:0, msg:''}";
    	
		try {
			faAdjService.revokeFaAdj(adjHid, extraParams);
		} catch (Exception e) {
			log.error(new Exception().getStackTrace()[0], e);
			if(e.getMessage()!=null){
				returnJson = "{flag:1, msg:\""+e.getMessage()+"\"}";
			}else{
				returnJson = "{flag:1, msg:\"\"}";
			}
		}
		
		PlatformUtil.outPrint(response, JSONUtil.Encode(returnJson));
	}
}
