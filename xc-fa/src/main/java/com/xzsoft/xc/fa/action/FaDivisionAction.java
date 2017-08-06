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

import com.xzsoft.xc.fa.modal.FaDivision;
import com.xzsoft.xc.fa.service.FaDivisionService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;


@Controller
@RequestMapping("/faDivisionAction.do")
public class FaDivisionAction {
	private final Log log = LogFactory.getLog(getClass());
	@Resource
	private FaDivisionService faDivisionService;
	/**
	 * saveFaDivision:(保存资产拆分)
	 *
	 * @param request
	 * @param response
	 * @author zhaoxin
	 */
	@RequestMapping(params = "method=saveFaDivision")
	public void saveFaDivision(@RequestBody FaDivision faDivision  , HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	String language = sessionVars.get(SessionVariable.language);
    	
    	HashMap<String, Object> extraParams = new HashMap<String, Object>();
    	extraParams.put("userId", userId);
    	extraParams.put("language", language);
    	
    	String returnJson = "{flag:0, msg:''}";		
    	try {
    		faDivisionService.saveFaDivision(faDivision, extraParams);
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
	 * deleteFaDivision:(删除资产拆分，资产事务，还原被拆分资产)
	 *
	 * @param request
	 * @param response
	 * @author zhaoxin
	 */
	@RequestMapping(params = "method=deleteFaDivision")
	public void deleteFaDivision(@RequestBody FaDivision faDivision  , HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	String language = sessionVars.get(SessionVariable.language);
    	
    	HashMap<String, Object> extraParams = new HashMap<String, Object>();
    	extraParams.put("userId", userId);
    	extraParams.put("language", language);
    	
    	String returnJson = "{flag:0, msg:''}";		
    	try {
    		faDivisionService.deleteFaDivision(faDivision, extraParams);
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
