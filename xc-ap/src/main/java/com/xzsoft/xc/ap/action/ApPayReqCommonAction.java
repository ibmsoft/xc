package com.xzsoft.xc.ap.action;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;









import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.ap.dao.ApPayReqCommonDao;
import com.xzsoft.xc.ap.modal.ApPayReqLBean;
import com.xzsoft.xc.ap.service.ApPayReqCommonService;
import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
  * @ClassName: ApPayReqCommonAction
  * @Description: 操作付款申请Action
  * @author 韦才文
  * @date 2016年6月14日 上午16:15:15
 */
@Controller
@RequestMapping("/apPayReqCommonAction.do")
public class ApPayReqCommonAction {
	//日志记录器
	 private static Logger log = Logger.getLogger(ApPayReqCommonAction.class.getName());
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	ApPayReqCommonService apPayReqService;
	@Resource
	UpdateInvGlAmountService updateInvGlAmountService;
	@Resource
	ApArTransDao apArTransDao;
	
	
	/*
	 * 付款申请单保存操作
	 */
	@RequestMapping(params="method=savePay")
	public void savePay(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String reqHMsg = request.getParameter("regHMsg");//前台传过来的付款单主表信息
			String reqLMsg = request.getParameter("regLMsg");//前台传过来的付款单行表信息
			String deleteDtl = request.getParameter("deleteDtl");
		    jo = apPayReqService.savePay(reqHMsg, reqLMsg,deleteDtl);
		} catch (Exception e) {
			
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	/*
	 * 付款申请单删除操作
	 */
	@RequestMapping(params="method=delPayReq")
	public void delPayReq(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String idArray = request.getParameter("payReqHId");
			jo  = apPayReqService.delPayReq(idArray);
		} catch (Exception e) {
			
		}
		
		WFUtil.outPrint(response, jo.toString());
		
	}
	/**
	 * 
	  * @Title: getInvoiceProcAndForms 方法名
	  * @Description：得到付款申请单模块中的流程相关信息
	  * @param：@param request
	  * @param：@param response 设定文件
	  * @return void 返回类型
	 */
	@RequestMapping(params="method=getProcAndFormsByCat",method=RequestMethod.POST)
	public void getProcAndFormsByCat(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			String ledgerId = request.getParameter("LEDGER_ID");//账簿ID
			String apDocCatCode = request.getParameter("AP_DOC_CAT_CODE");//采购发票编码
			
			jo = apPayReqService.getProcAndFormsByCat(ledgerId,apDocCatCode);
			jo.put("flag","0");
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		WFUtil.outPrint(response,jo.toString());
	}
	@RequestMapping(params="method=closeDoc")
	public void closeDoc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String language = sessionVars.get(SessionVariable.language);
		JSONObject jo = new JSONObject();
		try {
			String ids = request.getParameter("ids");
			jo = apPayReqService.closeDoc(ids,language);
		} catch (Exception e) {
			jo.put("flag","1");
			jo.put("msg",e.getMessage());
		}
		
		PlatformUtil.outPrint(response,jo.toString());
	}
}
