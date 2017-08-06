package com.xzsoft.xc.gl.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xc.gl.common.AbstractVoucherHandler;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.service.GLVoucherHandlerService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.gl.util.XCGLConstants;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * 
 * @ClassName: VoucherHandlerAction 
 * @Description: 记账凭证处理控制类
 * @author linp
 * @date 2015年12月18日 下午6:10:37 
 *
 */
@Controller
@RequestMapping("/voucherHandlerAction.do")
public class VoucherHandlerAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(VoucherHandlerAction.class.getName()) ;
	@Resource
	private VoucherHandlerService voucherHandlerService ;
	@Resource
	private RuleService ruleService ;
	@Resource
	private  AbstractVoucherHandler abstractVoucherHandler ;
	@Resource
	private GLVoucherHandlerService glVoucherHandlerService ;
	
	/**
	 * 
	 * @Title: handlerCCID 
	 * @Description: 凭证分录科目组合ID处理
	 * @param @param request
	 * @param @param response    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	@RequestMapping(params ="method=handlerCCID", method=RequestMethod.POST)
	public void handlerCCID(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		
		try {
			// 获取参数信息
			String ledgerId = request.getParameter("ledgerId") ;
			String accId = request.getParameter("accId") ;
			String segments = request.getParameter("segments") ;
			
			// 辅助核算段信息
			HashMap<String,String> segMap =  new HashMap<String,String>() ;
			
			// 将字符串转换为JSON数组
			JSONArray ja = new JSONArray(segments);
			if(ja.length() >0){
				for(int i=0; i<ja.length(); i++){
					String source = ja.getString(i) ;
					JSONObject obj = new JSONObject(source) ;
					segMap.put(obj.getString("seg_code").toUpperCase(), obj.getString("seg_val_id")) ;
				}
			}
			
			// 获取凭证处理服务类
			VoucherHandlerService service = (VoucherHandlerService) AppContext.getApplicationContext().getBean("voucherHandlerService") ;
			
			// 处理凭证分录CCID
			jo = service.handlerCCID(ledgerId, accId, segMap) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			// 提示信息
			try {
				jo.put("flag", "1") ;
				jo.put("ccid", "") ;
				jo.put("ccid_name", "") ;
				jo.put("msg", e.getMessage()) ;
			} catch (JSONException e1) {
				log.error(e.getMessage());
			}
		}
		
		// 提示信息
		WFUtil.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: getVoucherById 
	 * @Description: 查询凭证头信息
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=getVoucherById")
	public void getVoucherById(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = "{}" ;
		
		try {
			// 凭证头ID
			String headId = request.getParameter("headId") ;
			
			// 执行凭证查询
			VHead bean = voucherHandlerService.getVHead(headId) ;

			// 凭证创建日期
			String accDate = XCDateUtil.date2StrBy24(bean.getCreationDate()) ;
			
			// 封装返回参数
			JSONObject jo = new JSONObject() ;

			jo.put("ledger", bean.getLedgerId()) ;
			jo.put("accountDate", accDate) ;
			jo.put("ldPeriod", bean.getPeriodCode()) ;
			jo.put("v_category", bean.getCategoryId()) ;								// 凭证分类
			jo.put("v_serial_no", bean.getSerialNum()==null?"":bean.getSerialNum()) ; 	// 凭证号
			jo.put("v_summary", bean.getSummary()==null?"":bean.getSummary()) ; 		// 凭证摘要
			jo.put("v_attchment_num", bean.getAttchTotal()) ;							// 附件张数
			jo.put("v_head_id", bean.getHeadId()) ;										// 凭证头ID
			jo.put("v_src_code", bean.getSrcCode()==null?"":bean.getSrcCode()) ;		// 凭证来源
			jo.put("v_write_off", bean.getIsWriteOff()==null?"":bean.getIsWriteOff()) ;	// 冲销标志
			jo.put("v_write_off_num", bean.getWriteOffNum()==null?"":bean.getWriteOffNum()) ;		// 冲销凭证号
			jo.put("v_total_dr", bean.getTotalDR()) ;
			jo.put("v_total_cr", bean.getTotalCR()) ;
			jo.put("v_is_signed", bean.getIsSigned()) ;	// 是否需要签字
			jo.put("v_status", bean.getVStatus()) ;	// 凭证状态
			jo.put("v_template_type", bean.getTemplateType()) ;	// 凭证模板类型
			jo.put("v_created_name", bean.getCreatedName()) ;
			jo.put("v_verifier", bean.getVerifier()==null?"":bean.getVerifier()) ;
			jo.put("v_signatory", bean.getSignatory()==null?"":bean.getSignatory()) ;
			jo.put("v_bookkeeper", bean.getBookkeeper()==null?"":bean.getBookkeeper()) ;
			
			jsonStr = jo.toString() ;
			
		} catch (Exception e) {
			jsonStr = "{}" ;
		}
		
		// 提示信息
		WFUtil.outPrint(response,jsonStr);
		
	}
	
	/**
	 * @Title: getVoucherSerialNum 
	 * @Description: 取得凭证编号
	 * @param request
	 * @param response
	 */
	@RequestMapping(params ="method=getVoucherSerialNum")
	public void getVoucherSerialNum(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		
		try {
			// 获取参数
			String ledgerId =request.getParameter("ledgerId") ;
			String categoryId = request.getParameter("categoryId") ;
			String periodCode = request.getParameter("periodCode") ;
			String userId = CurrentSessionVar.getUserId() ;
			
			// 凭证编号规则分类码
			String ruleCode = XCGLConstants.RUEL_TYPE_PZ ;
			
			// 拆分期间
			String year = periodCode.substring(0, 4) ;
			String month = periodCode.substring(5, 7) ;
			
			// 计算凭证流水号
			String serialNum = ruleService.getNextSerialNum(ruleCode,ledgerId,categoryId,year,month,userId) ;
			
			//提示信息
			jo.put("flag", "0") ;
			jo.put("serialNum", serialNum) ;
			jo.put("msg", "") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", e.getMessage()) ;
				jo.put("serialNum", "") ;
			} catch (JSONException e1) {
				log.error(e.getMessage());
			}
		} 
		
		// 提示信息
		WFUtil.outPrint(response,jo.toString());
	}

	/**
	 * @Title: saveVoucher 
	 * @Description: 保存凭证信息(包括新建和更新操作)
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=saveVoucher")
	public void saveVoucher(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		
		try {
			// 凭证头信息
			String headJson = request.getParameter("infoJson") ;
			VHead head = voucherHandlerService.headJson2Bean(headJson) ;
			head.setSumFlag("N");
			
			// 凭证分录行信息
			String lineArray = request.getParameter("infoArray") ;
			List<VLine> lines = voucherHandlerService.lineArray2Bean(lineArray) ;
			head.setLines(lines);
			
			// 扩展参数
			String paramJson = request.getParameter("paramJson") ;
			JSONObject paramJo = new JSONObject(paramJson) ;
			// 操作类型（add-新增，copy-复制，write_off-冲销，modify-修改）
			String optype = paramJo.getString("optype") ;
			head.setOptype(optype);
			
			// 执行凭证校验
			HashMap<String,Object> map = new HashMap<String,Object>() ;
			map.put("optype", optype) ;
			map.put("bean", head) ;
			abstractVoucherHandler.checkSingleVoucher(map);
			
			// 执行凭证创建或更新处理
			String headId = head.getHeadId() ;
			if(headId != null && !"".equals(headId)){
				// 更新凭证
				voucherHandlerService.doUpdateVoucher(head);
				
			}else{ 
				// 凭证头ID
				head.setHeadId(UUID.randomUUID().toString()); 
				// 创建凭证
				voucherHandlerService.doNewVoucher(head);
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("vheadId", head.getHeadId()) ;
			jo.put("msg", "保存成功") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", e.getMessage()) ;
			} catch (JSONException e1) {
				log.error(e.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	
	/**
	 * @Title: submitVoucher 
	 * @Description: 提交单张凭证信息(包括新建、更新和提交处理)
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=submitVoucher")
	public void submitVoucher(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		
		try {
			// 凭证头信息
			String headJson = request.getParameter("infoJson") ;
			VHead head = voucherHandlerService.headJson2Bean(headJson) ;
			head.setSumFlag("N");
			
			// 凭证分录行信息
			String lineArray = request.getParameter("infoArray") ;
			List<VLine> lines = voucherHandlerService.lineArray2Bean(lineArray) ;
			head.setLines(lines);
			
			// 扩展参数
			String paramJson = request.getParameter("paramJson") ;
			JSONObject paramJo = new JSONObject(paramJson) ;
			// 操作类型（add-新增，copy-复制，write_off-冲销，modify-修改）
			String optype = paramJo.getString("optype") ;
			head.setOptype(optype);
			
			// 执行凭证校验
			HashMap<String,Object> map = new HashMap<String,Object>() ;
			map.put("optype", optype) ;
			map.put("bean", head) ;
			abstractVoucherHandler.checkSingleVoucher(map);
			
			// 执行凭证提交、新建及提交处理
			String headId = head.getHeadId() ;
			if(headId != null && !"".equals(headId)){
				// 更新并提交凭证
				glVoucherHandlerService.doUpdateAndSubmitVoucher(head);
				
			}else{ 
				// 凭证头ID
				head.setHeadId(UUID.randomUUID().toString()); 
				// 新建并提交凭证
				glVoucherHandlerService.doNewAndSubmitVoucher(head);
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("vheadId", head.getHeadId()) ;
			jo.put("vSerialNo", head.getSerialNum()) ;
			jo.put("msg", "提交成功") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				jo.put("flag", "1") ;
				jo.put("msg", e.getMessage()) ;
			} catch (JSONException e1) {
				log.error(e.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jo.toString());
	}
	
	/**
	 * @Title: submitVouchers 
	 * @Description: 批量提交凭证
	 * <p>
	 * 	参数格式：
	 * 		[{'V_HEAD_ID':''},{'V_HEAD_ID':''},...]
	 * </p>
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=submitVouchers")
	public void submitVouchers(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID信息
			String jsonArray = request.getParameter("headArray") ;
			
			// 提交凭证处理
			jsonStr = glVoucherHandlerService.doSubmitGLVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1");
				jo.put("msg", "提交失败:"+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: cancelSubmitVouchers 
	 * @Description: 批量取消提交处理
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=cancelSubmitVouchers")
	public void cancelSubmitVouchers(HttpServletRequest request, HttpServletResponse response) {
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 执行撤回提交处理
			jsonStr = glVoucherHandlerService.doCancelSubmitGLVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1");
				jo.put("msg", "撤回提交失败:"+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: delVouchers 
	 * @Description: 批量删除凭证处理
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=delVouchers")
	public void delVouchers(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 执行撤回提交处理
			jsonStr = voucherHandlerService.doDelVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1");
				jo.put("msg", "删除失败:"+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: checkVouchers 
	 * @Description: 凭证审核
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=checkVouchers")
	public void checkVouchers(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 执行凭证审核
			jsonStr = voucherHandlerService.doCheckVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("flag", "1");
				jo.put("msg", "凭证审核失败:"+e.getMessage()) ;
				jsonStr = jo.toString();
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: uncheckVouchers 
	 * @Description: 取消审核
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=uncheckVouchers")
	public void uncheckVouchers(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 执行取消审核
			jsonStr = voucherHandlerService.doUncheckVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject();
				jo.put("flag", "1");
				jo.put("msg", "取消审核失败:"+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: rejectVouchers 
	 * @Description: 驳回凭证
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=rejectVouchers")
	public void rejectVouchers(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 凭证驳回
			jsonStr = voucherHandlerService.doRejectVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject();
				jo.put("flag", "1");
				jo.put("msg", "驳回失败:"+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: signVouchers 
	 * @Description: 出纳签字
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=signVouchers")
	public void signVouchers(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 凭证签字处理
			jsonStr = voucherHandlerService.doSignVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject();
				jo.put("flag", "1");
				jo.put("msg", "签字失败:"+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: unsignVouchers 
	 * @Description: 取消签字
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=unsignVouchers")
	public void unsignVouchers(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 取消出纳签字
			jsonStr = voucherHandlerService.doUnsignVouchers(jsonArray) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject();
				jo.put("flag", "1");
				jo.put("msg", "取消签字失败:"+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: account 
	 * @Description: 执行记账处理
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=account")
	public void account(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 记账处理
			jsonStr = voucherHandlerService.doAccount(jsonArray) ;
						
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject();
				jo.put("flag", "1");
				jo.put("msg", "记账失败:"+e.getMessage()) ;	
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: cancelAccount 
	 * @Description: 凭证取消记账处理
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=cancelAccount")
	public void cancelAccount(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = null ;
		
		try {
			// 凭证头ID的JSON数组
			String jsonArray = request.getParameter("headArray") ;
			
			// 取消记账处理
			jsonStr = voucherHandlerService.doCancelAccount(jsonArray) ;
						
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				JSONObject jo = new JSONObject();
				jo.put("flag", "1");
				jo.put("msg", "取消记账失败:"+e.getMessage()) ;
				jsonStr = jo.toString() ;
			} catch (Exception e1) {
				log.error(e1.getMessage());
			}
		}
		
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: impVoucher 
	 * @Description: 凭证导入
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=impVoucher")
	public void impVoucher(HttpServletRequest request, HttpServletResponse response) {
		String jsonStr = null ;
		
		try {
			// 获取账簿ID
			String ledgerId = request.getParameter("ledgerId") ;
			
			// 取得excel工作簿
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
			List<FileItem> fileList = new ArrayList<FileItem>();
			fileList = servletFileUpload.parseRequest(request);
			if(fileList != null && fileList.size()>0){
				FileItem item = fileList.get(0);
				HSSFWorkbook workbook = new HSSFWorkbook(item.getInputStream());
				String sessionId = request.getSession().getId() ;
				
				// 导入凭证
				jsonStr = glVoucherHandlerService.doImportVoucher(ledgerId, sessionId, workbook);
				
				// 返回信息
				JSONObject rJo = new JSONObject(jsonStr) ;
				rJo.put("success", true) ;
				jsonStr = rJo.toString() ;
				
			}else{
				throw new Exception("文件上传失败!");
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			try {
				JSONObject jo = new JSONObject() ;
				jo.put("success", false) ;
				jo.put("flag", "1") ;
				jo.put("msg", "导入失败："+e.getMessage()) ;
				jsonStr = jo.toString();
			} catch (JSONException e1) {
				log.error(e1.getMessage());
			}
		}
		
		// 输出提示信息
		WFUtil.outPrint(response, jsonStr);
	}
	
	/**
	 * @Title: expVoucher 
	 * @Description: 凭证导出
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=expVoucher")
	public void expVoucher(HttpServletRequest request, HttpServletResponse response) {
		
		ServletOutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		
		try {
			// 凭证头ID
			String headId = request.getParameter("headId") ;
			
			// 组合Excel文件
			String filePath = request.getSession().getServletContext().getRealPath("XCloud/GL/voucher/voucher.xls") ;
			
			workbook = glVoucherHandlerService.doExportVoucher(headId, filePath) ;
			
			// 处理响应头
			response.setContentType("application/vnd.ms-excel");  
			String fileHeader = "";
			String userAgent = request.getHeader("User-Agent").toUpperCase();
		    if (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("EDG") > 0 || (userAgent.indexOf("GECKO")>0 && userAgent.indexOf("RV:11")>0)) {		 	
		    	fileHeader = "attachment;filename=" + URLEncoder.encode("总账凭证.xls", "UTF-8");  		   
		    } else { 
		     	fileHeader = "attachment;filename=" + new String("总账凭证".getBytes("utf-8"),"iso-8859-1") + ".xls";
		    }  
			response.setHeader("Content-disposition", fileHeader);
			
			// 输出文件流
			outputStream = response.getOutputStream();
			workbook.write(outputStream);
			outputStream.flush();
			
		} catch (Exception e) {
			log.error("凭证导出失败:"+e.getMessage());
			
		} finally {
			// 关闭流
			try {
				if(workbook !=null) workbook.close();
				if(outputStream !=null) outputStream.close();
				
			} catch (IOException ie) {
				log.error(ie.getMessage());
			}
		}
	}
}
