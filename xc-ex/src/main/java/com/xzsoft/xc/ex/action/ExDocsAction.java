package com.xzsoft.xc.ex.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.ex.service.EXDocBaseService;
import com.xzsoft.xc.ex.service.EXVoucherService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
 * @ClassName: ExDocsAction
 * @Description: 费用单据处理类
 * @author weicw
 * @date 2015年3月10日 下午6:10:37
 *
 */
@Controller
@RequestMapping("/exDocsAction.do")
public class ExDocsAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(ExDocsAction.class.getName());
	@Resource
	private RuleService ruleService;
	@Resource
	private EXVoucherService exVoucherService;
	@Resource
	private EXDocBaseService exDocBaseService ;
	
	/**
	 * @Title: getNextSerialNum
	 * @Description: 费用单据编号处理
	 * @param @param request
	 * @param @param response 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@RequestMapping(params = "method=getExDocCode")
	public void getExDocCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;

		try {
			// 获取参数信息
			String ruleCode = request.getParameter("ruleCode");
			String ledgerId = request.getParameter("ledgerId");
			String categoryId = request.getParameter("categoryId");
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String userId = request.getParameter("userId");
			
			// 取得单据编号
			String exDocCode = ruleService.getNextSerialNum(ruleCode, ledgerId, categoryId, year, month, userId);
			
			jo.put("flag", "0") ;
			jo.put("msg", exDocCode) ;
		} catch (Exception e) {
			log.error(e.getMessage());

			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
		}
		
		// 输出响应信息
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: getProcAndFormsByCat 
	 * @Description: 根据费用单据类型获取审批流程和审批表单信息
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params = "method=getProcAndFormsByCat")
	public void getProcAndFormsByCat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 预算校验
			String isChkBg = request.getParameter("isChkBg") ;
			String exDocId = request.getParameter("exDocId") ;
			JSONObject resultJo = exDocBaseService.checkBudget(isChkBg, exDocId);
			
			// -1 校验不通过或者校验异常；0 校验通过；1校验不通过，但预算不是绝对控制，给出警告提示        
			String flag = resultJo.getString("flag") ; 
			
			if("0".equals(flag)){ 
				// 预算校验通过
				String ledgerId = request.getParameter("ledgerId") ;
				String docCat = request.getParameter("docCat") ;
				jo = exDocBaseService.getProcAndFormsByCat(ledgerId, docCat) ;
				
				jo.put("phase", "QRY") ;
				
			}else if("1".equals(flag)){ 
				// 预算校验未通过但属预警控制
				String ledgerId = request.getParameter("ledgerId") ;
				String docCat = request.getParameter("docCat") ;
				jo = exDocBaseService.getProcAndFormsByCat(ledgerId, docCat) ;
				
				jo.put("phase", "BGCHK") ;
				jo.put("chkFlag", flag) ;
				jo.put("msg", resultJo.get("msg"));
				
			}else{ 
				// 预算校验未通过但属绝对控制
				jo.put("phase", "BGCHK") ;
				jo.put("chkFlag", flag) ;
				jo.put("msg", resultJo.get("msg"));
			}
			
			jo.put("flag", "0") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: getDocsAndUrlsByLedger 
	 * @Description: 获取账簿启用的单据类型及填报地址信息
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params = "method=getDocsAndUrlsByLedger")
	public void getDocsAndUrlsByLedger(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			String ledgerId = request.getParameter("ledgerId") ;
			
			// 执行查询
			JSONArray ja = exDocBaseService.getDocsAndUrlsByLedger(ledgerId) ;
			
			jo.put("flag", "0") ;
			jo.put("docArray", ja.toString()) ; // 包含单据类型
			
		} catch (Exception e) {
			log.error(e.getMessage());

			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: getDocPrintUrlByCat 
	 * @Description: 获取单据类型指定的打印表单信息
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params = "method=getDocPrintUrlByCat")
	public void getDocPrintUrlByCat(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 获取单据类型参数
			String docCat = request.getParameter("docCat") ;
			// 执行查询
			jo = exDocBaseService.getDocPrintUrlByCat(docCat) ;
			// 返回值
			jo.put("flag", "0") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());

			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: getAttCatCode 
	 * @Description: 获取附件分类码
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params = "method=getAttCatCode")
	public void getAttCatCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 单据类型及账簿ID
			String ledgerId = request.getParameter("ledgerId") ;
			String docCat = request.getParameter("docCat") ;
			
			// 执行查询
			String attCat = exDocBaseService.getAttCatCode(ledgerId, docCat) ;
			
			jo.put("flag", "0") ;
			jo.put("attCat", attCat) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());

			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: saveOrUpdateDoc 
	 * @Description: 费用报销单据保存或更新
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params = "method=saveOrUpdateDoc")
	public void saveOrUpdateDoc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 单据主表信息
			String masterTabJsonStr = request.getParameter("infoJson") ;
			// 单据明细表信息
			String detailTabJsonArray = request.getParameter("lineArray") ;
			
			// 执行保存
			jo = exDocBaseService.saveOrupdateDoc(masterTabJsonStr, detailTabJsonArray) ;
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "单据保存成功") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_SAVE_SUCCESS_01", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			//jo.put("msg", "单据保存失败: " + e.getMessage()) ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_SAVE_FAILED_01", null) + e.getMessage()) ;
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: delDoc 
	 * @Description: 删除费用单据信息
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params = "method=delDoc")
	public void delDoc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 删除参数
			String jsonArray = request.getParameter("params") ; 
			
			if(jsonArray != null && !"".equals(jsonArray)){
				// 执行删除
				exDocBaseService.delDoc(jsonArray);
				
			}else{
				//throw new Exception("删除参数不合法,正确格式为[{'EX_DOC_ID',''},{'EX_DOC_ID',''},...]") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_delDoc_EXCEPTION_01", null)) ;
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "单据删除成功") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_delDoc_EXCEPTION_02", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());

			jo.put("flag", "1") ;
			//jo.put("msg", "单据删除失败： " + e.getMessage()) ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_delDoc_EXCEPTION_03", null) + e.getMessage()) ;
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: checkBudgetAndUpdateDoc 
	 * @Description: 校验预算并更新单据信息
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params = "method=checkBudgetAndUpdateDoc")
	public void checkBudgetAndUpdateDoc(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 单据主表信息
			String masterTabJsonStr = request.getParameter("infoJson") ;
			// 单据明细表信息
			String detailTabJsonArray = request.getParameter("lineArray") ;
			// 账簿是否需要执行预算校验
			String isChkBg = request.getParameter("isChkBg") ;
			// 是否忽略预算警告
			String ingoreBgWarning = request.getParameter("ingoreBgWarning") ;
			
			// 执行单据更新
			exDocBaseService.checkBudgetAndUpdateDoc(masterTabJsonStr, detailTabJsonArray, isChkBg,ingoreBgWarning);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_SAVE_SUCCESS_01", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			jo.put("flag", "1") ;
			HttpSession session = request.getSession() ;
			if("1".equals(session.getAttribute("BG_CHK_FALG_".concat(session.getId())))){ // 预算警告
				jo.put("bgFlag", "1") ;
				jo.put("msg", e.getMessage()) ;
				
			}else if("-1".equals(session.getAttribute("BG_CHK_FALG_".concat(session.getId())))){ // 预算失败
				jo.put("bgFlag", "-1") ;
				jo.put("msg", e.getMessage()) ;
				
			}else{ // 其他
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_SAVE_FAILED_01", null) + e.getMessage()) ;
			}
		}
		
		WFUtil.outPrint(response,jo.toString());
	}
	
}
