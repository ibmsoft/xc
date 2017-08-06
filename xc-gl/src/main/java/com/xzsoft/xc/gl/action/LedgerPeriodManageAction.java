package com.xzsoft.xc.gl.action;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.gl.service.LedgerPeriodManageService;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: LedgerPeriodManageAction 
 * @Description: 账簿期间追加处理管理类(处理追加期间后的余额信息,追加当前会计期的下一自然会计期，同时也只能追加当前会计期以前的调整会计期)
 * @note:(追加会计期前，必须保证账簿的下一会计期处于启用状态)
 * @author tangxl
 * @date 2016年1月20日 上午09:11:15 
 *
 */
@Controller
@RequestMapping("/ledgerPeriodManageAction.do")
public class LedgerPeriodManageAction {
	@Resource
	LedgerPeriodManageService ledgerPeriodManageService;
	// 日志记录器
	private static Logger log = Logger.getLogger(LedgerPeriodManageAction.class.getName()) ;
	/**
	 * 
	 * @title:  appendLedgerPeriod
	 * @desc:   追加账簿期间
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   request
	 * @param   response
	 */
	@RequestMapping(params="method=appendLedgerPeriod")
	public void appendLedgerPeriod(HttpServletRequest request, HttpServletResponse response){
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		String ledgerId = request.getParameter("ledgerId");
		//账簿的开始会计期
		String startPeriod = request.getParameter("startPeriod");
		String returnJson ="";
		try {
				ledgerPeriodManageService.appendledgerPeriod(ledgerId,"",startPeriod);
				switch (lang) {
				case XConstants.ZH:
					returnJson = "{\"flag\":\"1\",\"msg\":\"追加会计期间成功！！\"}";
					break;
				case XConstants.EN:
					returnJson = "{\"flag\":\"1\",\"msg\":\"Successfully append accounting period！！\"}";
					break;
				default:
					break;
				}
				
		} catch (Exception e) {
			returnJson = "{\"flag\":\"0\",\"msg\":\""+e.getMessage()+"\"}";
			log.error("LedgerPeriodManageAction类--->appendLedgerPeriod方法--->追加账簿期间出错，失败原因"+e.getMessage());
			e.printStackTrace();
		}finally{
			PlatformUtil.outPrint(response, returnJson);
		}
	}
	/**
	 * 
	 * @title:  appendAdjustPeriod
	 * @desc:   追加账簿调整会计期间
	 * @author  tangxl
	 * @date    2016年3月20日
	 * @param   request
	 * @param   response
	 */
	@RequestMapping(params="method=appendAdjustPeriod")
	public void appendAdjustPeriod(HttpServletRequest request, HttpServletResponse response){
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		String ledgerId = request.getParameter("ledgerId");
		//账簿的开始会计期
		String startPeriod = request.getParameter("startPeriod");
		//要追加的调整会计期
		String adjustPeriod = request.getParameter("adjustPeriod");
		String returnJson ="";
		try {
				ledgerPeriodManageService.appendAdjustPeriod(ledgerId,adjustPeriod,startPeriod);
				switch (lang) {
				case XConstants.ZH:
					returnJson = "{\"flag\":\"1\",\"msg\":\"追加会计期间成功！！\"}";
					break;
				case XConstants.EN:
					returnJson = "{\"flag\":\"1\",\"msg\":\"Successfully append accounting period！！\"}";
					break;
				default:
					break;
				}
		} catch (Exception e) {
			returnJson = "{\"flag\":\"0\",\"msg\":\""+e.getMessage()+"\"}";
			log.error("LedgerPeriodManageAction类--->appendAdjustPeriod方法--->追加账簿期间出错，失败原因"+e.getMessage());
			e.printStackTrace();
		}finally{
			PlatformUtil.outPrint(response, returnJson);
		}
	}
	/**
	 * 
	 * @methodName  convertModelToVoucher
	 * @author      tangxl
	 * @date        2016年7月19日
	 * @describe    总账期末结转
	 * <document>
	 *    <title>期末结转</title>
	 *    <p>1、根据结转模板信息获取科目已过账的余额数据，若科目的余额已为0，不再为当前科目生成凭证明细信息</p>
	 *    <p>2、根据要结转的会计期，获取数据，调用凭证生成接口生成记账凭证</p>
	 *    <p>3、科目结转的本质是将某几个科目的余额转到一个科目上，生成的凭证在提交后会重新汇总科目余额，从而实现余额转移的目的</p>
	 * </document>
	 * @param       request
	 * @param       response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params="method=convertModelToVoucher")
	public void convertModelToVoucher(HttpServletRequest request, HttpServletResponse response){
		String jzTplId = request.getParameter("jzTplId");
		String periodCode = request.getParameter("periodCode");
		String voucherDate = request.getParameter("voucherDate");
		JSONObject object = new JSONObject();
		try {
			object = ledgerPeriodManageService.carryoverBalances(jzTplId,periodCode,voucherDate);
		} catch (Exception e) {
			object.put("flag", "1");
			object.put("msg", e.getMessage());
		}finally{
			PlatformUtil.outPrint(response, object.toString());
		}
	}
}
