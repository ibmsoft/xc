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

import com.xzsoft.xc.fa.modal.FaClean;
import com.xzsoft.xc.fa.service.FaCleanService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;

@Controller
@RequestMapping("/faCleanAction.do")
public class FaCleanAction {
	private final Log log = LogFactory.getLog(getClass());
	@Resource
	private FaCleanService faCleanService;
	/**
	 * saveFaDivision:(资产清理)
	 *
	 * @param request
	 * @param response
	 * @author zhaoxin
	 */
	@RequestMapping(params = "method=cleanFaAddition")
	public void cleanFaAddition(@RequestBody FaClean faClean  , HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	String language = sessionVars.get(SessionVariable.language);
    	
    	HashMap<String, Object> extraParams = new HashMap<String, Object>();
    	extraParams.put("userId", userId);
    	extraParams.put("language", language);
    	
    	String returnJson = "{flag:0, msg:''}";		
    	try {
    		faCleanService.cleanFaAddition(faClean, extraParams);
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
