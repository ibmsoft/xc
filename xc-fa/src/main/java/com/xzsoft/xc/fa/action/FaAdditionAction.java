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

import com.xzsoft.xc.fa.modal.FaAddition;
import com.xzsoft.xc.fa.service.FaAdditionService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;


@Controller
@RequestMapping("/faAdditionAction.do")
public class FaAdditionAction {

	private final Log log = LogFactory.getLog(getClass());

	@Resource
	private FaAdditionService faAdditionService;
	
	/**
	 * saveFaAddition:(保存资产)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=saveFaAddition")
	public void saveFaAddition(@RequestBody FaAddition faAddition, HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
    	String language = sessionVars.get(SessionVariable.language);
    	
    	HashMap<String, Object> extraParams = new HashMap<String, Object>();
    	extraParams.put("userId", userId);
    	extraParams.put("language", language);
    	
    	String returnJson = "";
    	
		try {
			FaAddition returnFa = faAdditionService.saveFaAddition(faAddition, extraParams);
			
			returnJson = "{flag:0, msg:'', assetId:'"+returnFa.getAssetId()+"', opType:'"+returnFa.getOperate()+"'}";
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
