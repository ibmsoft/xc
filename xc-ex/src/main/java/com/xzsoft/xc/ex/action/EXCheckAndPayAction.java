package com.xzsoft.xc.ex.action;

import java.util.ArrayList;
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

import com.xzsoft.xc.ex.modal.CheckAndPayDocDTO;
import com.xzsoft.xc.ex.service.EXCheckAndPayService;
import com.xzsoft.xc.ex.util.XCEXConstants;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCJSONUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * @ClassName: EXCheckAndPayAction 
 * @Description: 单据复核和单据支付控制类
 * @author linp
 * @date 2016年3月27日 下午10:29:50 
 *
 */
@Controller
@RequestMapping("/exCheckAndPayAction.do") 
public class EXCheckAndPayAction {
	// 日志记录器
	private static final Logger log = Logger.getLogger(EXCheckAndPayAction.class.getName()) ;
	
	@Resource
	private EXCheckAndPayService exCheckAndPayService ;

	/**
	 * @Title: checkEXDoc 
	 * @Description: 报销单复核处理
	 * <p>
	 * 	报销单复核处理 ： 系统支持一次对一张单据的复核处理(单据包括：借款单、还款单、费用报销单和差旅报销单)
	 *  参数格式：
	 *  {	
	 *  	"ledgerId"	:	"",		// 账簿ID
	 *  	"accDate"	:	"",		// 会计日期
	 *  	"payMethod"	:	"",		// 支付方式：1-立即支付,2-挂账支付
	 *  	"payType"	:	"",		// 付款银行
	 *  	"bankId"	:	"",		// 现金流量项目ID
	 *  	"cashId"	:	"",		// 现金流量项目ID
	 *      "exCatCode" :   "",		// 单据类型
	 *  	"exDocId"	:	"",		// 待复核单据
	 *  	"cancelAmt"	:	"",		// 核销金额
	 *  	"payAmt"	:	""		// 本次支付金额
	 *  }
	 * 
	 * </p>
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params = "method=checkEXDoc",method=RequestMethod.POST)
	public void checkEXDoc(HttpServletRequest request,HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		
		try {
			// 参数信息
			String jsonStr = request.getParameter("params") ;
			
			// 参数转换(将JSON串转换为JavaBean)
			CheckAndPayDocDTO dto = XCJSONUtil.jsonToBean(jsonStr, CheckAndPayDocDTO.class) ;
				
			// 对立即支付报销单生成付款单
			dto.setOpType(XCEXConstants.PAY_MODE_CHECK); 	
			
			// 执行复核处理
			jo = exCheckAndPayService.checkEXDoc(dto);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_FIN_SUCCESS", null)) ;//单据复核成功
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", e.getMessage()) ;
				
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString()); 
	}
	
	/**
	 * @Title: uncheckEXDoc 
	 * @Description: 报销单取消复核处理
	 * <p>
	 * 	 报销单取消复核处理, 系统一次支持多张报销单据的取消复核处理
	 *  传入参数格式为: [{"EX_DOC_ID":"","EX_DOC_ID":""...}]
	 * </p>
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=uncheckEXDoc",method=RequestMethod.POST)
	public void uncheckEXDoc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject() ;
		
		try {
			// 传入已复核的多张单据
			String jsonStr = request.getParameter("params") ;
			JSONArray ja = new JSONArray(jsonStr) ;
			
			if(ja != null){
				exCheckAndPayService.uncheckEXDoc(ja);
			}
			
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CANCEL_FIN_SUCCESS", null)) ;//报销单取消复核成功！
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CANCEL_FIN_FAILED", null)+e.getMessage()) ;//取消复核失败：
				
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString()); 
	}
	
	/**
	 * @Title: cancelDoc 
	 * @Description: 取消单据
	 * 参数格式: {'EX_DOC_ID':'','INS_CODE':'','V_HEAD_ID':''}
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=cancelDoc",method=RequestMethod.POST)
	public void cancelDoc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject() ;
		
		try {
			// 参数信息
			String docId = request.getParameter("docId") ;
			String cancelReason = request.getParameter("cancelReason") ;
			
			// 执行单据取消
			exCheckAndPayService.cancelDoc(docId,cancelReason);
			
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CANCEL_SUCCESS", null)) ;//取消成功！
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CANCEL_FAILED", null)+e.getMessage()) ;//取消失败：
				
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString()); 
	}
	
	/**
	 * @Title: createPayDoc 
	 * @Description: 新建付款单
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=createPayDoc",method=RequestMethod.POST)
	public void createPayDoc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject() ;
		
		try {
			// 定义参数传递类
			CheckAndPayDocDTO dto = new CheckAndPayDocDTO() ;
			dto.setOpType(XCEXConstants.PAY_MODE_PAYMENT); 	// 对挂账支付报销单创建付款单
			
			// 1.表单信息
			String infoJson = request.getParameter("infoJson");
			JSONObject infoJo = new JSONObject(infoJson);
			String ledgerId = infoJo.getString("LEDGER_ID");				// 账簿ID
			String exPayDesc = infoJo.getString("EX_PAY_DESC");				// 付款说明
			String depositBankId = infoJo.getString("DEPOSIT_BANK_ID");		// 付款账号
			String payDate = infoJo.getString("EX_PAY_DATE");				// 支付日期
			String payAmt =infoJo.getString("EX_PAY_T_AMT");				// 付款总额
			String payType = infoJo.getString("PAY_TYPE");					// 付款方式
			String cashId = infoJo.getString("CA_ID");						// 现金流量项目
			
			// 将参数封装到对象
			dto.setLedgerId(ledgerId);
			dto.setExPayDesc(exPayDesc);
			dto.setBankId(depositBankId);
			dto.setAccDate(XCDateUtil.str2Date(payDate));
			dto.setPayAmt(Float.parseFloat(payAmt));
			dto.setPayType(payType);
			dto.setCashId(cashId);
			dto.setExCatCode(XCEXConstants.EX_DOC_EX06);	// 单据类型
			

			// 2.本次付款的报销单
			List<String> exDocIds = new ArrayList<String>() ;		// 定义存储报销单ID列表
			String infoArray = request.getParameter("infoArray");
			JSONArray docArray = new JSONArray(infoArray);
			for(int i=0;i<docArray.length();i++){
				JSONObject docJo = docArray.getJSONObject(i);
				String docId = docJo.getString("EX_DOC_ID") ;
				exDocIds.add(docId) ;
			}
			dto.setExDocIds(exDocIds);
			
			// 3.生成付款单
			jo = exCheckAndPayService.createPayDoc(dto);		
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_SAVE_SUCCESS", null)) ;//生成付款单成功！
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_SAVE_FAILED", null)+e.getMessage()) ;//生成付款单失败：
				
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString()); 
	}
	
	/**
	 * @Title: editPayDoc 
	 * @Description: 修改付款单
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=editPayDoc",method=RequestMethod.POST)
	public void editPayDoc(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 定义参数传递类
			CheckAndPayDocDTO dto = new CheckAndPayDocDTO() ;
			dto.setOpType(XCEXConstants.PAY_MODE_PAYMENT); 	// 挂账支付报销单创建付款单
			
			// 1.表单信息
			String infoJson = request.getParameter("infoJson");
			JSONObject infoJo = new JSONObject(infoJson);
			String exPayId = infoJo.getString("EX_PAY_ID");					// 支付单ID
			String exPayCode = infoJo.getString("EX_PAY_CODE");				// 付款单编码
			String vStatus = infoJo.getString("V_STATUS");					// 凭证状态
			String payStatus = infoJo.getString("PAY_STATUS");				// 付款状态
			String headId = infoJo.getString("V_HEAD_ID") ;					// 凭证头ID
			
			// 支付单ID不能为空
			if(exPayId == null || "".equals(exPayId)){
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_EDIT_EXCEPTION_01", null));//支付单ID不能为空！
			}
			// 凭证已审核
			if("3".equals(vStatus)){
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_EDIT_EXCEPTION_02", null));//支付单对应支付凭证已审核不能修改！
			}
			if("Y".equals(payStatus)){
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_EDIT_EXCEPTION_03", null));//单据已支付不能修改！
			}
			if(headId == null || "".equals(headId)){
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_EDIT_EXCEPTION_04", null));//修改付款单时,参数凭证头ID不能为空！
			}
			
			String ledgerId = infoJo.getString("LEDGER_ID");				// 账簿ID
			String exPayDesc = infoJo.getString("EX_PAY_DESC");				// 付款说明
			String depositBankId = infoJo.getString("DEPOSIT_BANK_ID");		// 付款账号
			String payDate = infoJo.getString("EX_PAY_DATE");				// 支付日期
			String payAmt = infoJo.getString("EX_PAY_T_AMT");				// 付款总额
			String payType = infoJo.getString("PAY_TYPE");					// 付款方式
			String cashId = infoJo.getString("CA_ID");						// 现金流量项目
							
			// 将参数封装到对象
			dto.setPayId(exPayId);
			dto.setPayCode(exPayCode);
			dto.setLedgerId(ledgerId);
			dto.setHeadId(headId);
			dto.setExPayDesc(exPayDesc);
			dto.setBankId(depositBankId);
			dto.setAccDate(XCDateUtil.str2Date(payDate));
			dto.setPayAmt(Float.parseFloat(payAmt));
			dto.setPayType(payType);
			dto.setCashId(cashId);
			dto.setExCatCode(XCEXConstants.EX_DOC_EX06);	// 单据类型
			
			
			// 2.本次付款的报销单
			List<String> exDocIds = new ArrayList<String>() ;		// 定义存储报销单ID列表
			HashMap<String,Object> amtMap = new HashMap<String,Object>() ;
			
			String infoArray = request.getParameter("infoArray");
			JSONArray docArray = new JSONArray(infoArray);
			for(int i=0;i<docArray.length();i++){
				JSONObject docJo = docArray.getJSONObject(i);
				String docId = docJo.getString("EX_DOC_ID") ;
				String docPayAmt = docJo.getString("TOPIC_PAY_AMT") ;
				
				exDocIds.add(docId) ;
				amtMap.put(docId, docPayAmt) ;		
			}
			dto.setExDocIds(exDocIds);
			dto.setAmtMap(amtMap);
			
			// 3.修改付款单
			jo = exCheckAndPayService.editPayDoc(dto);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_EDIT_SUCCESS", null)) ;//修改付款单成功
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_EDIT_FAILED", null)+e.getMessage()) ;//修改付款单失败：
				
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString()); 
	}
	
	/**
	 * @Title: uncheckPayDoc 
	 * @Description: 取消付款单审核处理
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=uncheckPayDoc",method=RequestMethod.POST)
	public void uncheckPayDoc(HttpServletRequest request,HttpServletResponse response) throws Exception{
		JSONObject jo = new JSONObject() ;
		
		try {
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CANCEL_PAY_SUCCESS", null)) ;//取消付款成功！
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "0") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CANCEL_PAY_FAILED", null)+e.getMessage()) ;//取消付款失败：
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString()); 
	}
	
	/**
	 * @Title: saveVoucher 
	 * @Description: 保存凭证
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=saveVoucher",method=RequestMethod.POST)
	public void saveVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 表单信息
			String headJson = request.getParameter("infoJson") ;
			
			// 凭证分录行信息
			String lineArray = request.getParameter("infoArray") ;

			// 保存凭证信息
			exCheckAndPayService.saveVoucher(headJson, lineArray);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_VOUCHER_SAVE_SUCCESS", null)) ;//凭证保存成功！
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "0") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_VOUCHER_SAVE_FAILED", null)+e.getMessage()) ;//凭证保存失败：
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString()); 
	}
	
	/**
	 * @Title: submitVoucher 
	 * @Description: 提交凭证
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=submitVoucher",method=RequestMethod.POST)
	public void submitVoucher(HttpServletRequest request,HttpServletResponse response) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			// 凭证头信息
			String headJson = request.getParameter("infoJson") ;
			// 凭证分录行信息
			String lineArray = request.getParameter("infoArray") ;
			// 辅助参数信息
			String paramJson = request.getParameter("paramJson") ;
			
			// 凭证提交
			exCheckAndPayService.submitVoucher(headJson, lineArray, paramJson);
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_VOUCHER_SUBMIT_SUCCESS", null)) ;//凭证提交成功
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "0") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_EX_VOUCHER_SUBMIT_FAILED", null)+e.getMessage()) ;//凭证提交失败：
				
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString()); 
	}
}
