package com.xzsoft.xc.fa.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.fa.service.impl.LedgerCatSettingServiceImpl;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * 
 * ClassName:LedgerCatSettingAction
 * Function: 资产分类账簿设置
 *
 * @author   q p
 * @Date	 2016	2016年7月12日		下午4:35:55
 *
 */

@Controller
@RequestMapping("/ledgerCatSettingAction.do")
public class LedgerCatSettingAction {

	private final Log log = LogFactory.getLog(getClass());

	@Resource
	private LedgerCatSettingServiceImpl ledgerCatSettingServiceImpl;
	
	/**
	 * syncLedgerCatSetting:(同步资产分类)
	 *
	 * @param request
	 * @param response
	 * @author q p
	 */
	@RequestMapping(params = "method=syncLedgerCatSetting")
	public void syncLedgerCatSetting(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		
		String ledgerId = request.getParameter("ledgerId");
		
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
    	String userId = sessionVars.get(SessionVariable.userId);
		
    	String returnJson = "{flag:0, msg:''}";
    	
		try {
			ledgerCatSettingServiceImpl.syncLedgerCatSetting(userId, ledgerId);
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
