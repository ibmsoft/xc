package com.xzsoft.xc.gl.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.gl.service.PeriodBalanceManageService;
import com.xzsoft.xc.gl.service.XCGLSumBalanceService;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: PeriodBalanceManageAction 
 * @Description: 账簿余额管理类(账簿分期初以及期中打开两种)
 * @author tangxl
 * @date 2016年1月20日 上午09:11:15 
 *
 */
@Controller
@RequestMapping("/periodBalanceManageAction.do")
public class PeriodBalanceManageAction {
	@Resource
	PeriodBalanceManageService periodBalanceManageService;
	@Resource
	private XCGLSumBalanceService xcglSumBalanceService;
	// 日志记录器
	private static Logger log = Logger.getLogger(PeriodBalanceManageAction.class.getName()) ;
	
	/**
	 * @Title: saveGlPeriodBalances 
	 * @Description: TODO(导出期初余额模板)
	 * @note:导出期初余额模板 
	 * @param  request
	 * @param  response     
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	@RequestMapping(params="method=downloadBalanceModel")
	public void downloadBalanceModel(HttpServletRequest request, HttpServletResponse response){
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		String modelType = request.getParameter("modelType");
		HttpSession session = request.getSession();
		String url = File.separator + "XCloud" + File.separator + "GL"+ File.separator + "Balances" + File.separator
				+ "balancesModel" + File.separator + modelType + ".xlsx";
		String excelUrl = session.getServletContext().getRealPath(url);
		OutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			response.setContentType("application/vnd.ms-excel");  
			String modelName = "";
			try {
				switch (lang) {
				case XConstants.ZH:
					modelName = java.net.URLDecoder.decode("账簿余额模板", "utf-8");
					break;
				case XConstants.EN:
					modelName = java.net.URLDecoder.decode("Balance the books of the template", "utf-8");
					break;
				default:
					break;
				}
			} catch (UnsupportedEncodingException e) {
				modelName = "yearBalanceModel";
			}
			//============================下载模板，区分请求浏览器=========================
			String fileHeader = "";
			String userAgent = request.getHeader("User-Agent").toUpperCase();
		    if (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("EDG") > 0 || (userAgent.indexOf("GECKO")>0 && userAgent.indexOf("RV:11")>0)) {		 	
		    	fileHeader = "attachment;filename=" + URLEncoder.encode(modelName + ".xlsx", "UTF-8");  		   
		    } else { 
		     	fileHeader = "attachment;filename=" + new String(modelName.getBytes("utf-8"),"iso-8859-1") + ".xlsx";
		    }  
			response.setHeader("Content-disposition", fileHeader);
			File excelFile = new File(excelUrl);
			inputStream = new FileInputStream(excelFile);
			outputStream = response.getOutputStream();
			byte[] buffer = new byte[1024];
			int c =0;
			while((c=inputStream.read(buffer))>0){
				outputStream.write(buffer, 0,c);
			}
		} catch (Exception e) {
			log.error("PeriodBalanceManageAction类--->downloadBalanceModel方法--->导出余额模板失败，失败原因"+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				if(inputStream!=null){
					inputStream.close();
				}
				if(outputStream !=null){
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				log.error("PeriodBalanceManageAction类--->downloadBalanceModel方法--->关闭io流失败");
				e.printStackTrace();
			}
		}
	}
	/**
	 *@Title: downloadCashModel
	 *@Description: TODO(导出现金期初余额模板)
	 *@param 2016年3月8日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	@RequestMapping(params="method=downloadCashModel")
	public void downloadCashModel(HttpServletRequest request,HttpServletResponse response){
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		String modelType = request.getParameter("modelType");
		HttpSession session = request.getSession();
		String url = File.separator + "XCloud" + File.separator + "GL"+ File.separator + "Cash" + File.separator
				+ "cashModel" + File.separator + modelType + ".xlsx";
		String excelUrl = session.getServletContext().getRealPath(url);
		OutputStream outputStream = null;
		InputStream  inputStream  = null;
		try{
			response.setContentType("application/vnd.ms-excel");  
			String modelName = "";
			try {
				switch (lang) {
				case XConstants.ZH:
					modelName = java.net.URLDecoder.decode("现金流量项模板", "utf-8");
					break;
				case XConstants.EN:
					modelName = java.net.URLDecoder.decode("The cash flow template", "utf-8");
					break;
				default:
					break;
				}
			} catch (UnsupportedEncodingException e) {
				modelName = "cashModel";
			}
			//============================下载模板，区分请求浏览器=========================
			String fileHeader = "";	
			String userAgent = request.getHeader("User-Agent").toUpperCase();
			if (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("EDG") > 0 || (userAgent.indexOf("GECKO")>0 && userAgent.indexOf("RV:11")>0)) { 				
			    	fileHeader = "attachment;filename=" + URLEncoder.encode(modelName + ".xlsx", "UTF-8");  		    
			    	
			    } else {  
			
			    	fileHeader = "attachment;filename=" + new String(modelName.getBytes("utf-8"),"iso-8859-1") + ".xlsx";
			   
			    }  
				response.setHeader("Content-disposition", fileHeader);
				File excelFile = new File(excelUrl);
				inputStream = new FileInputStream(excelFile);
				outputStream = response.getOutputStream();
				byte[] buffer = new byte[1024];
				int c =0;
				while((c=inputStream.read(buffer))>0){
					outputStream.write(buffer, 0,c);
				}
		}catch(Exception e){
			log.error("PeriodBalanceManageAction类--->downloadCashModel方法--->导出余额模板失败，失败原因"+e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				if(inputStream!=null){
					inputStream.close();
				}
				if(outputStream !=null){
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException e) {
				log.error("PeriodBalanceManageAction类--->downloadCashModel方法--->关闭io流失败");
				e.printStackTrace();
			}
		}
		
	}
	

	
	/**
	 * 
	 * @Title: importBalances 
	 * @Description: TODO(将余额导入到账簿余额临时表中) 
	 * @param request
	 * @param response 
	 * @return void    返回类型 
	 * @auther tangxl
	 * @throws
	 */
	@RequestMapping(params="method=importBalances")
	public void importBalances(HttpServletRequest request, HttpServletResponse response){
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		//账簿id
		String ledgerId = request.getParameter("ledgerId");
		boolean isDuplicate =  false;
		String sessionId = CurrentSessionVar.getSession().getId();
		//账簿期间
		String periodCode = request.getParameter("periodCode");
		//余额期间类型 INIT-->年初期间，MID-->年中期间
		String balanceType = request.getParameter("balanceType");
		String returnJson = "";
		switch (lang) {
		case XConstants.ZH:
			returnJson = "{\"success\":true,\"msg\":\"数据导入成功！\"}";
			break;
		case XConstants.EN:
			returnJson = "{\"success\":true,\"msg\":\"Successfully import data\"}";
			break;
		default:
			break;
		}
		try {
			if(StringUtils.isBlank(ledgerId) || StringUtils.isEmpty(ledgerId)){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_NO_EXIST", null));
			}
			InputStream inputStream = null;
			List<FileItem> fileList = new ArrayList<FileItem>();
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
			fileList = servletFileUpload.parseRequest(request);
			if(fileList == null || fileList.size()<0){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_FILE_UPLOAD_FAIL", null));
			}else{
				FileItem item = fileList.get(0);
				String fileName = item.getName();
				String excelType = "2003";
				if(fileName.toLowerCase().endsWith(".xlsx")){
					excelType = "2007";
				}
				//文件转换成输入流
				inputStream = item.getInputStream();
				//=================1、导入前，先删除沉淀的冗余数据======
				periodBalanceManageService.deletePeriodBalances(ledgerId,sessionId);
				//=================2、插入Excel数据到导入表中并执行校验，返回校验结果=======
				String importJson = periodBalanceManageService.saveGlPeriodBalances(inputStream, ledgerId, balanceType, periodCode, excelType);
				if(importJson !=null && !"".equals(importJson)){
					returnJson = "{\"success\":false,\"msg\":\""+importJson+"\",\"tag\":0}";
				}else{
					//=============3、获取科目组合ID============
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("sessionId", sessionId);
					map.put("isValid", "Y");
					map.put("ledgerId", ledgerId);
					periodBalanceManageService.fillExcelDataCCID(map);
					//=============4、插入记录到tmp临时表中==========
					try {
						periodBalanceManageService.insertExcelDataToTmp(map);
					} catch (org.springframework.dao.DuplicateKeyException e) {
						e.printStackTrace();
						isDuplicate = true;
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_DATA_REPEAT", null));
					}
					//=============5、插入临时表中成功，删除本次导入的数据======
					periodBalanceManageService.deletePeriodBalances(ledgerId,sessionId);
				}
			}
		} catch (Exception e) {
			log.error("PeriodBalanceManageAction类--->importBalances方法--->导入账簿期初余额失败，失败原因"+e.getMessage());
			if(isDuplicate){
				switch (lang) {
				case XConstants.ZH:
					returnJson = "{\"success\":false,\"msg\":\"导入账簿期初余额失败，"+e.getMessage()+"\",\"tag\":2,\"sessionId\":\""+sessionId+"\"}";
					break;
				case XConstants.EN:
					returnJson = "{\"success\":false,\"msg\":\"Import the books of the beginning balance of failure,"+e.getMessage()+"\",\"tag\":2,\"sessionId\":\""+sessionId+"\"}";
					break;
				default:
					break;
				}
			}else{
				switch (lang) {
				case XConstants.ZH:
					returnJson = "{\"success\":false,\"msg\":\"导入账簿期初余额失败，"+e.getMessage()+"\",\"tag\":1}";
					break;
				case XConstants.EN:
					returnJson = "{\"success\":false,\"msg\":\"Import the books of the beginning balance of failure,"+e.getMessage()+"\",\"tag\":1}";
					break;
				default:
					break;
				}
			}
			e.printStackTrace();
		}finally{
			PlatformUtil.outPrint(response, returnJson);
		}
	}
	/**
	 *@Title: importCash
	 *@Description: TODO(将余额导入到现金流量临时表中)
	 *@param 2016年3月9日  设定文件
	 *@return void 返回类型
	 *@author lizhh
	 *@throws
	 */
	@RequestMapping(params="method=importCash")
	public void importCash(HttpServletRequest request,HttpServletResponse response){
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		String ledgerId = request.getParameter("ledgerId");
		String sessionId = request.getSession().getId();
		String returnJson = "";
		switch (lang) {
		case XConstants.ZH:
			returnJson = "{\"success\":true,\"msg\":\"数据导入成功！\"}";
			break;
		case XConstants.EN:
			returnJson = "{\"success\":true,\"msg\":\"Successfully import data\"}";
			break;
		default:
			break;
		}
		
		try{
			InputStream inputStream = null;
			List<FileItem> fileList = new ArrayList<FileItem>();
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
			fileList = servletFileUpload.parseRequest(request);
			if(fileList == null || fileList.size()<0){
				switch (lang) {
				case XConstants.ZH:
					returnJson="{\"success\":false,\"msg\":\"文件上传失败！\",\"tag\":0}";
					break;
				case XConstants.EN:
					returnJson="{\"success\":false,\"msg\":\"File upload failed\",\"tag\":0}";
					break;
				default:
					break;
				}
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_FILE_UPLOAD_FAIL", null));
			}
			else{
				FileItem item = fileList.get(0);
				String fileName = item.getName();
				String excelType = "2003";
				if(fileName.toLowerCase().endsWith(".xlsx")){
					excelType = "2007";
				}
				//文件转换成输入流
				inputStream = item.getInputStream();
				//=================1、导入前，先删除沉淀的冗余数据======			
     		periodBalanceManageService.deleteCashTmp(ledgerId,sessionId);
     		   //===================2、保存Excel数据到xc_gl_cash_imp表中，并校验导入数据的合法性===
     		   //===================importJson 不为空，说明本次导入校验不通过===============
     		String importJson=periodBalanceManageService.saveCashTmp(inputStream,ledgerId,excelType,sessionId);
     		if(importJson!=null&&!"".equals(importJson)){
     			returnJson="{\"success\":false,\"msg\":\""+sessionId+"\",\"tag\":1}";
     			throw new Exception(XipUtil.getMessage(lang, "XC_GL_DATA_ILLEGAL", null));
     		}else{
    			//=================3、更新xc_gl_cash_imp表中的 现金流项目ID CA_ID 和 会计科目ID ACC_ID,并导入数据到xc_gl_cash_tmp表中===
     			HashMap<String, String> map = new HashMap<String, String>();
     			map.put("ledgerId", ledgerId);
     			map.put("sessionId", sessionId);
     			map.put("dbType", PlatformUtil.getDbType());
     			//==========捕获重复记录异常，证明存在重复导入记录==================
    		     try {
					periodBalanceManageService.fillCashItemId(map);
				} catch (org.springframework.dao.DuplicateKeyException  e){
					returnJson="{\"success\":false,\"msg\":\""+sessionId+"\",\"tag\":2}";
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_DATA_IMPORT_REPEAT", null));
				}
     		} 		
		  }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			PlatformUtil.outPrint(response, returnJson);
		}
	}
	/**
	 * 
	 * @title:         checkTheBalance
	 * @description:   检查余额是否平衡
	 * @author         tangxl
	 * @date           2016年4月22日
	 * @param          request
	 * @param          response
	 */
	@RequestMapping(params="method=checkTheBalance")
	public void checkTheBalance(HttpServletRequest request, HttpServletResponse response){
		HashMap<String, String> sessionVars = (HashMap<String, String>) request.getSession().getAttribute(SessionVariable.XZ_SESSION_VARS);
		String lang = sessionVars.get(SessionVariable.language);
		String returnJson = "";
		String ledgerId = request.getParameter("ledgerId");
		String periodCode = request.getParameter("periodCode");
		String isBuild = request.getParameter("isBuild");
		HashMap<String, String> map = new HashMap<String, String>();
		if(isBuild !=null && "Y".equalsIgnoreCase(isBuild))
			map.put("isBuild", isBuild);
		map.put("ledgerId", ledgerId);
		map.put("periodCode", periodCode);
		if(periodCode.endsWith("-12")){
			map.put("isInitial", "Y");
		}else{
			map.put("isInitial", "N");
		}
		try {
			//检查余额是否平衡
			returnJson = periodBalanceManageService.checkTheBalance(map);
		} catch (Exception e) {
			if(map.containsKey("isBuild")){
				switch (lang) {
				case XConstants.ZH:
					returnJson =  "{\"flag\":\"0\",\"msg\":\"余额平衡检查失败！\"}";
					break;
				case XConstants.EN:
					returnJson =  "{\"flag\":\"0\",\"msg\":\"Balance check failed\"}";
					break;
				default:
					break;
				}
				
			}else{
				String metaData = "{'fields':[{'name':'CATOGERY_NAME','type':'string'},{'name':'CATOGERY_STATUS','type':'string'},{'name':'CATOGERY_AMOUNT','type':'float'}]}";
				returnJson =  "{\"false\":true,\"metaData\":"+metaData+",\"rows\":[],\"total\":0}";
			}
			e.printStackTrace();
		}finally{
			PlatformUtil.outPrint(response, returnJson);
		}
		
	}
}
