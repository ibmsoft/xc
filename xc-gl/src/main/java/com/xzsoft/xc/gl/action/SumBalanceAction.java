package com.xzsoft.xc.gl.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.gl.service.XCGLSumBalanceService;
import com.xzsoft.xc.gl.util.XCGLWebBuilderUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * @ClassName: SumBalanceAction 
 * @Description: 余额汇总处理
 * @author linp
 * @date 2016年1月7日 上午10:45:15 
 *
 */
@Controller
@RequestMapping("/sumBalanceAction.do")
public class SumBalanceAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(SumBalanceAction.class.getName()) ;
	@Resource
	private XCGLSumBalanceService xcglSumBalanceService ;
	// 建账标志: 0-空闲,1-建账/取消建账正在执行
	private int createAccountFlag = 0 ;
	
	/**
	 * @Title: sumBalances 
	 * @Description: 总账余额汇总处理
	 * @param request
	 * @param response
	 */
	@RequestMapping(params ="method=sumBalances")
	public void sumBalances(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		
		try {
			// 凭证头ID信息 ： JSON串格式[{'V_HEAD_ID':'1'},{'V_HEAD_ID':'2'},...]
			String vouchers  = request.getParameter("headArray") ;
			JSONArray ja = new JSONArray(vouchers) ;
			
			// 凭证过账与凭证提交处理: Y-凭证过账处理 , N-凭证提交处理
			String isAccount = request.getParameter("isAccount") ;
			
			// 是否统计现金流量项目表: ALL-汇总余额和统计现金流量项目, CA-只统计现金流量项目, BA-只汇总总账余额
			String isSumCash = request.getParameter("isSumCash") ;
			
			// 是否取消提交
			String isCanceled = request.getParameter("isCanceled") ;
			
			// 当前用户ID 
			String userId = CurrentSessionVar.getUserId() ;
			
			// 执行余额汇总
			if(ja.length() > 0){
				jo = xcglSumBalanceService.sumGLBalances(ja, isAccount, isSumCash, userId, isCanceled) ;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", e.getMessage()) ;
			} catch (JSONException e1) {
				log.error(e.getMessage());
			}
		}
		
		// 提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: modifyCreateAccountFlag 
	 * @Description: 判断账簿是否正在执行建账/取消建账操作
	 * @param isCanceled
	 * @throws Exception    设定文件
	 */
	private synchronized void modifyCreateAccountFlag(String isCanceled)throws Exception{
		String lang = XCUtil.getLang();
		String msg = "";
		switch (lang) {
		case XConstants.ZH:
			msg = "Y".equals(isCanceled) ? "Books already canceled the subscription" :"Books already build the subscription" ;
			break;
		case XConstants.EN:
			msg = "Y".equals(isCanceled) ? "Books already canceled the subscription" :"Books already build the subscription" ;
			break;
		default:
			break;
		}
		if(this.createAccountFlag == 1) throw new Exception(msg) ;
	}

	/**
	 * @Title: createOrCancelAccount 
	 * @Description: 账簿建账或取消处理
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params ="method=createOrCancelAccount")
	public void createOrCancelAccount(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject() ;
		String lang = XCUtil.getLang();
		try {
			// 账簿ID
			String ledgerId  = request.getParameter("ledgerId") ;
			// 建账或取消建账 :Y-取消建账,N-建账
			String isCanceled = request.getParameter("isCanceled") ;
			// 当前用户ID 
			String userId = CurrentSessionVar.getUserId() ;
			
			// 判断账簿是否正在执行建账或取消建账操作
			try {
				this.modifyCreateAccountFlag(isCanceled);
			} catch (Exception e1) {
				throw e1;
			}
			
			// 判断当前账簿是否已建账 :1-已建账,0-未建账
			int isCreateAccount = XCGLWebBuilderUtil.isCreateAccount(ledgerId,"BA") ;
			
			if("N".equals(isCanceled) && isCreateAccount == 1){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_BUILD", null)) ;
			}
			
			if("Y".equals(isCanceled) && isCreateAccount == 0){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_CANCEL_BUILD", null)) ;
			}
			
			// 定义参数
			HashMap<String,String> map = new  HashMap<String,String>() ;
			map.put("ledgerId", ledgerId) ;
			map.put("userId", userId) ;
			map.put("isCanceled", isCanceled) ;
			
			// 执行建账或取消建账
			jo = xcglSumBalanceService.sumInitBalances(map) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			jo.put("flag", "1");
			jo.put("msg", XipUtil.getMessage(lang, "XC_GL_BUILD_FAIL", null)+e.getMessage()) ;
		}
		
		// 提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: createCash 
	 * @Description: 期初现金流量项目建账与取消建账
	 * @param request
	 * @param response    设定文件
	 * @throws JSONException 
	 */
	@RequestMapping(params ="method=createOrCancelCash")
	public void createOrCancelCash(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject() ;
		String lang = XCUtil.getLang();
		try {
			// 账簿ID
			String ledgerId  = request.getParameter("ledgerId") ;
			
			// 建账或取消建账 :Y-取消建账,N-建账
			String isCanceled = request.getParameter("isCanceled") ;
			
			// 判断账簿是否正在执行建账或取消建账操作
			try {
				this.modifyCreateAccountFlag(isCanceled);
			} catch (Exception e1) {
				throw e1;
			}
			
			// 判断当前账簿是否已建账 :1-已建账,0-未建账
			int isCreateAccount = XCGLWebBuilderUtil.isCreateAccount(ledgerId,"CA") ;
			
			if("N".equals(isCanceled) && isCreateAccount == 1){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_BUILD", null)) ;
			}
			
			if("Y".equals(isCanceled) && isCreateAccount == 0){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_CANCEL_BUILD", null)) ;
			}
			
			// 执行统计
			jo = xcglSumBalanceService.sumInitCash(ledgerId,isCanceled) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
		}
		
		// 提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: sumGLBal 
	 * @Description: 仅汇总总账余额表
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=sumGLBal")
	public void sumGLBal(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		try {
			// 凭证头ID信息 ： JSON串格式[{'V_HEAD_ID':'1'},{'V_HEAD_ID':'2'},...]
			String vouchers  = request.getParameter("headArray") ;
			JSONArray ja = new JSONArray(vouchers) ;
			
			// 科目体系ID
			String hrcyId = request.getParameter("hrcyId") ;
			
			// 凭证过账与凭证保存处理: Y-凭证过账处理 , N-凭证保存处理
			String isAccount = request.getParameter("isAccount") ;
			
			// 是否统计现金流量项目表: ALL-汇总余额和统计现金流量项目, CA-只统计现金流量项目, BA-只汇总总账余额
			String isSumCash = request.getParameter("isSumCash") ;
			
			// 是否取消提交
			String isCanceled = request.getParameter("isCanceled") ;
			
			// 当前用户ID 
			String userId = CurrentSessionVar.getUserId() ;
			
			// 执行余额汇总
			if(ja.length() > 0){
				xcglSumBalanceService.sumGLBal(ja, hrcyId, isAccount, isSumCash, userId, isCanceled);
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(lang, "XC_GL_TOTAL_SUCCESS", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", e.getMessage()) ;
			} catch (JSONException e1) {
				log.error(e.getMessage());
			}
		}
		
		// 提示信息
		WFUtil.outPrint(response,jo.toString());
	}
}
