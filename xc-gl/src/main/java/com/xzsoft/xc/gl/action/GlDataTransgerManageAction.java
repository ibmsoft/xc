package com.xzsoft.xc.gl.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.gl.modal.AccountTemplate;
import com.xzsoft.xc.gl.service.GlDataTransgerManageService;
import com.xzsoft.xc.gl.util.AccountTemplateUtil;
import com.xzsoft.xc.gl.util.GlBaseDataExportUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: GlDataTransgerManageAction 
 * @Description: 总账基础数据导入导出管理类
 * @note:<p>执行基础数据导入前，例如：到日客户数据，需先建立该表的数据导入导出模板，数据模板依赖系统数据建模，需先建立被导入表的数据库模型！</p>
 * @author tangxl
 * @date 2016年5月5日 上午09:11:15 
 *
 */
@Controller
@RequestMapping("/glDataTransgerManageAction.do")
public class GlDataTransgerManageAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(GlDataTransgerManageAction.class.getName()) ;
	//树形导入模板
	@SuppressWarnings("serial")
	private final static HashMap<String, Integer> modelMap = new HashMap<String, Integer>(){
		//具有树形结构的模板，需要上级编码的模板UP_CODE,科目表中包含了UP_ACC_CODE
		{   put("XC_GL_ACCOUNTS", 1);
			put("XC_GL_CASH_ITEMS", 1);
			put("XC_PM_PROJECTS", 1);
			put("XC_GL_LD_CUST_ASS_SEGVALS", 1);
		    put("XC_EX_ITEMS", 1);
		    put("XC_BG_ITEMS", 1);
		}
	};
	@SuppressWarnings("serial")
	private final static HashMap<String, String> nameMap = new HashMap<String, String>();
	public GlDataTransgerManageAction(){
		
	}
	@Resource
	private GlDataTransgerManageService glDataTransgerManageService;
	/**
	 * 
	 * @title:  createDataTransferModel
	 * @desc:   根据模板配置数据生成导入导出模板
	 * <p>
	 *   若导出模板为:科目、现金流项目、项目以及辅助段模板时，额外添加 上级编码【UP_CODE】
	 * </p>
	 * @author  tangxl
	 * @date    2016年5月5日
	 * @param   request
	 * @param   response
	 */
	@RequestMapping(params="method=createDataTransferModel")
	public void createDataTransferModel(HttpServletRequest request, HttpServletResponse response){
		//模板配置信息编码
		String modelCode = request.getParameter("modelCode");
		String modelName = "";
		HSSFWorkbook workbook = null;
		ServletOutputStream outputStream = null;
		String lang = "";
		try {
			 lang = XCUtil.getLang();//语言值
			List<AccountTemplate> accountList = glDataTransgerManageService.getTemplateByCode(modelCode);
			if(accountList !=null && accountList.size()>0){
				modelName = accountList.get(0).getTemplateDesc();
				if(StringUtils.isEmpty(modelName) || StringUtils.isBlank(modelName))
					modelName = accountList.get(0).getTemplateName();
				workbook = AccountTemplateUtil.createTemplateByModel(accountList, modelName,modelCode.toUpperCase());
				response.setContentType("application/vnd.ms-excel");  
				try {
					modelName = java.net.URLDecoder.decode(modelName, "utf-8");
				} catch (UnsupportedEncodingException e) {
					modelName = "ImportModel";
				}
				String fileHeader = "";
				String userAgent = request.getHeader("User-Agent").toUpperCase();
			    if (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("EDG") > 0 || (userAgent.indexOf("GECKO")>0 && userAgent.indexOf("RV:11")>0)) {		 	
			    	fileHeader = "attachment;filename=" + URLEncoder.encode(modelName + ".xls", "UTF-8");  		   
			    } else { 
			     	fileHeader = "attachment;filename=" + new String(modelName.getBytes("utf-8"),"iso-8859-1") + ".xls";
			    }  
				response.setHeader("Content-disposition", fileHeader);
				outputStream = response.getOutputStream();
			}else{
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_MODAL_NO_EXIST", null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("GlDataTransgerManageAction-->createDataTransferModel-->导出模板异常，原因："+e.getMessage());
		}finally{
			if(workbook !=null && outputStream !=null){
				try {
					workbook.write(outputStream);
					workbook.close();
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error("GlDataTransgerManageAction-->createDataTransferModel-->导出模板异常，原因："+e.getMessage());
				}

			}
		}
	}
	/**
	 * @title:         importAccountData
	 * @description:   导入总账的基础数据
	 * @author         tangxl
	 * @date           2016年5月6日
	 * @param          request
	 * @param          response
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(params="method=importAccountData")
	public void importAccountData(HttpServletRequest request, HttpServletResponse response){
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		String resultJson = "";
		switch (lang) {
		case XConstants.ZH:
			resultJson = "{\"success\":true,\"flag\":\"1\",\"msg\":\"导入成功！\"}";
			break;
		case XConstants.EN:
			resultJson = "{\"success\":true,\"flag\":\"1\",\"msg\":\"Successfully import\"}";
			break;
		default:
			break;
		}
		//模板编码
		String templateCode = request.getParameter("templateCode");
		//账簿ID，导入项目、自定义段时的传递参数
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		if("XC_PM_PROJECTS".equalsIgnoreCase(templateCode) || "XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode))
			paramsMap.put("LEDGER_ID", request.getParameter("ledgerId"));
		if("XC_GL_ACCOUNTS".equalsIgnoreCase(templateCode)){
		    paramsMap.put("ACC_HRCY_ID", request.getParameter("accHrcyId"));
		}
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode))
			paramsMap.put("SEG_CODE", request.getParameter("segCode"));
		if("XC_EX_ITEMS".equalsIgnoreCase(templateCode))
			paramsMap.put("EX_HRCY_ID", request.getParameter("exHrcyID"));
		if("XC_BG_ITEMS".equalsIgnoreCase(templateCode))
			paramsMap.put("BG_HRCY_ID", request.getParameter("bgHrcyID"));
		//本次导入会话ID
		String sessionId = request.getSession().getId();
		try {
			//===1、获取上传文件转化成HSSFWorkbook对象,只处理.xls结尾的文档
			HSSFWorkbook uploadBook = AccountTemplateUtil.parseIoStreamToExcel(request);
			//===2、根据模板编码，获取当前要导入的模板列信息
			List<AccountTemplate> accountList = glDataTransgerManageService.getTemplateByCode(templateCode);
			if(accountList == null || accountList.size()<1){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_MODAL_INFO_NO", null));
			}
			//===3、校验模板信息与上传的Excel模板是否一致,返回模板定义的列及其类型
			HashMap<String, String> map = AccountTemplateUtil.checkTemplateChanges(uploadBook, accountList,templateCode.toUpperCase());
			int cashItemOrder = 0;
			if("XC_GL_CASH_ITEMS".equalsIgnoreCase(templateCode)){
				try {
					cashItemOrder = glDataTransgerManageService.getMaxCashOrder();
				} catch (Exception e) {
					cashItemOrder = 0;
				}
			}
			//===4、根据模板编码，解析上传的.xls文件, 转换成List<HashMap>结果集
			List<String> colNameList = new ArrayList<String>();
			List<HashMap> dataList = AccountTemplateUtil.convertExcelToList(uploadBook,map,sessionId,colNameList,templateCode.toUpperCase(),paramsMap,cashItemOrder);
			//===5、插入数据到临时表中,并执行校验<分开事物执行，数据量较大时，事物开启时间不够>
			try {
				glDataTransgerManageService.insertDataIntoTmp(sessionId,dataList, templateCode.toUpperCase(),colNameList,map);
			//===6.1、基本数据校验
				String returnJson = glDataTransgerManageService.checkImportValid(sessionId, templateCode,paramsMap);
				if(returnJson !=null){
					resultJson = "{\"success\":false,\"flag\":\"2\",\"msg\":\""+sessionId+"\"}";
				}else{
				    //===6.2、数据插入到临时表后，树形基础数据导入，处理Excel数据的上下级关系
					//===计算已导入数据的层级以及是否为子节点信息==============
					if(modelMap.containsKey(templateCode.toUpperCase())){
						//=====6.2.1 更新导入数据的层级、上级编码信息
						List<HashMap> importList = glDataTransgerManageService.updateDataLevel(sessionId,templateCode.toUpperCase(),paramsMap);
						if(importList==null || importList.size()==0)
							throw new Exception(XipUtil.getMessage(lang, "XC_GL_IMPORT_UPDATE", null));
						//List<HashMap> importList = glDataTransgerManageService.getCurrentImportData(sessionId,templateCode.toUpperCase());
					}
					//===6.3、基本校验通过，非树形结构数据直接从临时表插入到正式表
					glDataTransgerManageService.insertTmpDataToFormal(sessionId, templateCode.toUpperCase(),paramsMap);
					switch (lang) {
					case XConstants.ZH:
						resultJson = "{\"success\":true,\"flag\":\"1\",\"msg\":\"数据导入成功!\"}";
						break;
					case XConstants.EN:
						resultJson = "{\"success\":true,\"flag\":\"1\",\"msg\":\"Data import is successful\"}";
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_INSERT_EXCEPTION", null)+e.getMessage());
			}
		} catch (Exception e) {
			resultJson = "{\"success\":false,\"flag\":\"0\",\"msg\":\""+e.getMessage()+"\"}";
			log.error("GlDataTransgerManageAction-->importAccountData-->导入总账基础数据异常，原因："+e.getMessage());
			e.printStackTrace();
		}finally{
			PlatformUtil.outPrint(response, resultJson);
		}
	}
	/**
	 * 
	 * @title:   exportBaseDataByModel
	 * @describe:导出模板配置数据
	 * <p>
	 *  科目信息、账簿辅助段信息需要按条件导出
	 * </p>
	 * @author  tangxl
	 * @date    2016年5月25日
	 * @param   request
	 * @param   response
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(params="method=exportBaseDataByModel")
	public void exportBaseDataByModel(HttpServletRequest request,HttpServletResponse response){	
		String templateCode = request.getParameter("templateCode");
		String lang = "";
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		ServletOutputStream outputStream = null;
		HSSFWorkbook workbook = null;
		paramsMap.put("MODEL_CODE", templateCode);
		if("XC_PM_PROJECTS".equalsIgnoreCase(templateCode))
			paramsMap.put("LEDGER_ID", request.getParameter("ledgerId"));
		if("XC_GL_ACCOUNTS".equalsIgnoreCase(templateCode)){
			String accCategory = request.getParameter("accCategoryCode");
			if(accCategory == null)
				accCategory = "";
			String accGroupId = request.getParameter("accGroupId");
			if(accGroupId == null)
				accGroupId = "";
		    paramsMap.put("ACC_CATEGORY_CODE",accCategory);
		    paramsMap.put("ACC_HRCY_ID", request.getParameter("accHrcyId"));
		    paramsMap.put("ACC_GROUP_ID", accGroupId);
		}
		if("XC_GL_LD_CUST_ASS_SEGVALS".equalsIgnoreCase(templateCode)){
			paramsMap.put("SEG_CODE", request.getParameter("segCode"));
			paramsMap.put("LEDGER_ID", request.getParameter("ledgerId"));
		   }
		if("XC_EX_ITEMS".equalsIgnoreCase(templateCode))
			paramsMap.put("EX_HRCY_ID", request.getParameter("exHrcyID"));
		if("XC_BG_ITEMS".equalsIgnoreCase(templateCode))
			paramsMap.put("BG_HRCY_ID", request.getParameter("bgHrcyID"));
		List<String> codeNameList = new ArrayList<String>();
		List<String> codeOrderList = new ArrayList<String>();
		HashMap<String, String> typeMap = new HashMap<String, String>();
		//获得要导出的数据
		try {
			lang = XCUtil.getLang();//语言值
			//具有树形结构的模板，需要上级编码的模板UP_CODE,科目表中包含了UP_ACC_CODE
			if("zh".equalsIgnoreCase(lang)){
				nameMap.put("XC_GL_ACCOUNTS", "会计科目信息表");
				nameMap.put("XC_GL_CASH_ITEMS", "现金流量项目信息表");
				nameMap.put("XC_PM_PROJECTS", "项目信息表");
				nameMap.put("XC_GL_LD_CUST_ASS_SEGVALS", "账簿自定义辅助段值表");
				nameMap.put("XC_AP_VENDORS", "供应商信息表");
				nameMap.put("XC_GL_ACC_GROUP", "科目分组信息表");
				nameMap.put("XC_AR_CUSTOMERS", "客户信息表");
				nameMap.put("XC_GL_DIMENSIONS", "计量单位信息表");
				nameMap.put("XC_GL_PRODUCTS", "产品信息表");
				nameMap.put("XC_EX_ITEMS", "费用项目信息表");
				nameMap.put("XC_BG_ITEMS", "预算项目信息表");
			}else if("en".equalsIgnoreCase(lang)){
				nameMap.put("XC_GL_ACCOUNTS", "Account information table");
				nameMap.put("XC_GL_CASH_ITEMS", "Project cash flow information table");
				nameMap.put("XC_PM_PROJECTS", "Project information table");
				nameMap.put("XC_GL_LD_CUST_ASS_SEGVALS", "Books of the custom auxiliary value table");
				nameMap.put("XC_AP_VENDORS", "Supplier information table");
				nameMap.put("XC_GL_ACC_GROUP", "Subject group information table");
				nameMap.put("XC_AR_CUSTOMERS", "The customer information table");
				nameMap.put("XC_GL_DIMENSIONS", "The measuring unit information table");
				nameMap.put("XC_GL_PRODUCTS", "Product information table");
				nameMap.put("XC_EX_ITEMS", "Project cost information table");
				nameMap.put("XC_BG_ITEMS", "Budget project information table");
			}
			List<HashMap> dataList = glDataTransgerManageService.getExportData(paramsMap,codeNameList,typeMap,codeOrderList);
			workbook = GlBaseDataExportUtil.exportGlBaseData(dataList, codeNameList,codeOrderList, typeMap, nameMap.get(templateCode));
		//导出模板数据
			response.setContentType("application/vnd.ms-excel");  
			String fileHeader = "";
			String userAgent = request.getHeader("User-Agent").toUpperCase();
		    if (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("EDG") > 0 || (userAgent.indexOf("GECKO")>0 && userAgent.indexOf("RV:11")>0)) {		 	
		    	fileHeader = "attachment;filename=" + URLEncoder.encode(nameMap.get(templateCode) + ".xls", "UTF-8");  		   
		    } else { 
		     	fileHeader = "attachment;filename=" + new String(nameMap.get(templateCode).getBytes("utf-8"),"iso-8859-1") + ".xls";
		    }  
			response.setHeader("Content-disposition", fileHeader);
			outputStream = response.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("GlDataTransgerManageAction-->exportBaseDataByModel-->导出数据异常，原因"+e.getMessage());
		}finally{
			if(workbook !=null && outputStream !=null){
				try {
					workbook.write(outputStream);
					workbook.close();
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error("GlDataTransgerManageAction-->exportBaseDataByModel-->导出数据异常，原因"+e.getMessage());
				}

			}
		
			
		}
		
	} 
}
