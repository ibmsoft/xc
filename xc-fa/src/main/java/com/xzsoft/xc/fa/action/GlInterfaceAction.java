package com.xzsoft.xc.fa.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.fa.service.GlInterfaceService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;


@Controller
@RequestMapping("/glInterfaceAction.do")
public class GlInterfaceAction {

	private final Log log = LogFactory.getLog(getClass());

	@Resource
	private GlInterfaceService glInterfaceService;
	
	/**
	 * 
	 * saveGlInterface:(保存会计平台)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=saveGlInterface")
	public void saveGlInterface(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	
    	String glInterface = request.getParameter("glInterface");
    	String glInterfaceIds = request.getParameter("glInterfaceIds");
    	String glInterfaceHead = request.getParameter("glInterfaceHead");
    	
    	String returnJson = "{flag:0, msg:''}";
    	
		try {
			glInterfaceService.saveInterface(glInterface, glInterfaceIds, glInterfaceHead, userId);
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
	 * 
	 * submitGlInterface:(提交)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=submitGlInterface")
	public void submitGlInterface(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
    	
    	String vHeadId = request.getParameter("vHeadId");
    	String iHeadId = request.getParameter("iHeadId");
    	
    	String returnJson = "{flag:0, msg:''}";
    	
		try {
			String json = glInterfaceService.submitGlInterface(vHeadId, iHeadId);
			
			if(!"".equals(json)){
				returnJson = json;
			}
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
	 * 
	 * draftGlInterface:(撤回)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=draftGlInterface")
	public void draftGlInterface(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
    	
    	String vHeadId = request.getParameter("vHeadId");
    	String iHeadId = request.getParameter("iHeadId");
    	
    	String returnJson = "{flag:0, msg:''}";
    	
		try {
			String json = glInterfaceService.draftGlInterface(vHeadId, iHeadId);
			
			if(!"".equals(json)){
				returnJson = json;
			}
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
	 * 
	 * deleteGlInterface:(删除)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=deleteGlInterface")
	public void deleteGlInterface(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	
    	String vHeadId = request.getParameter("vHeadId");
    	String iHeadId = request.getParameter("iHeadId");
    	
    	String returnJson = "{flag:0, msg:''}";
    	
		try {
			String json = glInterfaceService.deleteGlInterface(vHeadId, iHeadId, userId);
			
			if(!"".equals(json)){
				returnJson = json;
			}
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
