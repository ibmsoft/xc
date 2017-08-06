package com.xzsoft.xc.rep.action;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.xzsoft.xc.rep.service.EnterpriseSheetService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * @ClassName: EnterpriseSheetAction 
 * @Description: 报表模板格式处理Action类
 * @author linp
 * @date 2016年9月1日 上午9:21:35 
 *
 */
@Controller
@RequestMapping("/esAction.do")
public class EnterpriseSheetAction  extends BaseAction {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EnterpriseSheetAction.class) ;
	
	@Autowired
	private EnterpriseSheetService esService ;
	
	/**
	 * @Title: findTemplate 
	 * @Description: 查询模板基础信息
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=findTemplate")
	public void findTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> result = Maps.newHashMap() ;
		try {
			// 科目体系ID
			String accHrcyId = request.getParameter("accHrcyId") ;
			
			// 执行查询
			result = esService.findAllTemplates(accHrcyId) ;
			
			if(result == null || result.size() ==0){
				result.put("flag", "2") ;
				result.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_QRY_001", null)) ;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		// 输出结果集
		this.out(response, result);
	}
	
	/**
	 * @Title: findTemplateById 
	 * @Description: 按模板ID查询模板信息 
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=findTemplateById")
	public void findTemplateById(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> result = Maps.newHashMap() ;
		try {
			// 科目体系ID
			String tabId = request.getParameter("tabId") ;
			
			// 执行查询
			result = esService.findAllTemplateById(tabId) ;
			
			if(result == null || result.size() ==0){
				result.put("flag", "2") ;
				result.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_QRY_002", null)) ;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		// 输出结果集
		this.out(response, result);
	}
	
	/**
	 * @Title: createTemplate 
	 * @Description: 新增模板基础信息
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=createTemplate")
	public void createTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject retJo = new JSONObject() ;
		
		try {
			// 参数 ： {'accHrcyId' : '','tabCode' : '','tabName' : ''}
			String paramJson = request.getParameter("reqParams") ;
			
			// 执行新增处理
			retJo = esService.createTemplate(paramJson) ;
			
			// 加载成功标志
			retJo.put("flag", "0") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_SAVE_001", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			Map<String, String> tokens = new HashMap<String,String>() ;
			tokens.put("info", e.getMessage()) ;
			
			retJo.put("flag", "1") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_SAVE_002", tokens)) ;
		} 
		
		// 输出返回信息
		this.outPrint(response, retJo.toString());
	}
	
	/**
	 * @Title: deleteTemplate 
	 * @Description: 删除报表模板信息
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=deleteTemplate")
	public void deleteTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject retJo = new JSONObject() ;
		
		try {
			// 参数 ： {'accHrcyId' : '','sheetId' : ''}
			String reqParams = request.getParameter("reqParams") ;
			
			JSONObject reqJo = JSONObject.fromObject(reqParams) ;
			String hrcyId = reqJo.getString("accHrcyId") ;
			String sheetId = reqJo.getString("sheetId") ;
			
			// 执行删除
			esService.deleteTemplate(hrcyId, Integer.parseInt(sheetId));
			
			// 加载成功标志
			retJo.put("flag", "0") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_DEL_001", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			Map<String, String> tokens = new HashMap<String,String>() ;
			tokens.put("info", e.getMessage()) ;
			
			retJo.put("flag", "1") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_DEL_002", tokens)) ;
		}
		
		// 输出返回信息
		this.outPrint(response, retJo.toString());
	}
	
	/**
	 * 
	 * @Title: deleteTabById 
	 * @Description: 按报表ID删除模板格式
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=deleteTabById")
	public void deleteTabById(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject retJo = new JSONObject() ;
		
		try {
			// 参数 ： 
			String tabId = request.getParameter("tabId") ;
			
			// 执行删除
			esService.deleteTemplate(tabId);
			
			// 加载成功标志
			retJo.put("flag", "0") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_DEL_001", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			retJo.put("flag", "1") ;
			
			Map<String, String> tokens = new HashMap<String,String>() ;
			tokens.put("info", e.getMessage()) ;
			
			retJo.put("flag", "1") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_DEL_002", tokens)) ;
		}
		
		// 输出返回信息
		this.outPrint(response, retJo.toString());
	}
	
	
	/**
	 * @Title: saveTemplate 
	 * @Description: 保存报表模板信息 
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=saveTemplate")
	public void saveTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject retJo = new JSONObject() ;
		
		try {
			// 参数
			String accHrcyId = request.getParameter("accHrcyId") ;
			String sheetId = request.getParameter("sheetId") ;
			String dataJson = request.getParameter("dataJson") ;
			
			// 执行保存处理
			esService.saveTemplate(accHrcyId, Integer.parseInt(sheetId), dataJson);
			
			// 加载成功标志
			retJo.put("flag", "0") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_SAVE_001", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			retJo.put("flag", "1") ;
			
			Map<String, String> tokens = new HashMap<String,String>() ;
			tokens.put("info", e.getMessage()) ;
			
			retJo.put("flag", "1") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_SAVE_002", tokens)) ;
		}
		
		// 输出返回信息
		this.outPrint(response, retJo.toString());
	}
	
	/**
	 * @Title: uploadTemplate 
	 * @Description: 上传单张模板
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=impTemplate")
	public void uploadTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject retJo = new JSONObject() ;
		
		try {
			// 参数信息
			
			// 加载成功标志
			retJo.put("flag", "0") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			retJo.put("flag", "1") ;
			switch (XCUtil.getLang()) {
			case XConstants.ZH:
				retJo.put("msg", "加载失败："+e.getMessage()) ;
				break;
			case XConstants.EN:
				retJo.put("msg", "Fail to load data:"+e.getMessage()) ;
				break;
			default:
				retJo.put("msg", "加载失败："+e.getMessage()) ;
				break;
			}
			
		}
		
		// 输出返回信息
		this.outPrint(response, retJo.toString());
	}
	
	/**
	 * @Title: saveRowItems 
	 * @Description: 保存行指标信息
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=saveRowItems")
	public void saveRowItems(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> result = Maps.newHashMap() ;
		
		try {
		    String accHrcyId = request.getParameter("accHrcyId") ;
		    String tabOrder = request.getParameter("sheetId") ;
		    String tabId = request.getParameter("tabId") ;
		    String rowArray = request.getParameter("rowArray") ;
		    
		    // 执行保存
		    List<Map<String,Object>> cells = esService.saveSaveOrUpdateRowItems(accHrcyId,tabId, Integer.parseInt(tabOrder), rowArray) ;
			
			// 加载成功标志
		    result.put("flag", "0") ;
		    result.put("cells", cells) ;
		    result.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_SAVE_001", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			Map<String, String> tokens = new HashMap<String,String>() ;
			tokens.put("info", e.getMessage()) ;
			
			result.put("flag", "1") ;
			result.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_SAVE_002", tokens)) ;
		}
		
		// 输出返回信息
		this.out(response, result);
	}
	
	/**
	 * @Title: saveColItems 
	 * @Description: 保存列指标信息
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=saveColItems")
	public void saveColItems(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String, Object> result = Maps.newHashMap() ;
		
		try {
		    String accHrcyId = request.getParameter("accHrcyId") ;
			String tabOrder = request.getParameter("sheetId") ;
			String tabId = request.getParameter("tabId") ;
			String colArray = request.getParameter("colArray") ;
			
			// 执行保存
		    List<Map<String,Object>> cells = esService.saveSaveOrUpdateColItems(accHrcyId, tabId, Integer.parseInt(tabOrder), colArray) ;
			
			// 加载成功标志
		    result.put("flag", "0") ;
		    result.put("cells", cells) ;
		    result.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_SAVE_001", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			result.put("flag", "1") ;
			
			Map<String, String> tokens = new HashMap<String,String>() ;
			tokens.put("info", e.getMessage()) ;
			
			result.put("flag", "1") ;
			result.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_SAVE_002", tokens)) ;
		}
		
		// 输出返回信息
		this.out(response, result);
	}
	
	/**
	 * @Title: copyTemplates 
	 * @Description: 模板复制 
	 * <p>
	 * 	参数信息：
	 * 	 {'srcHrcyId'   : '','targetHrcyId': '','tabArray'    : [{'TAB_ID':''},...]}
	 * </p>
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=copyTemplates")
	public void copyTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject retJo = new JSONObject() ;
		
		try {
			// 参数 
			String paramJson = request.getParameter("reqParams") ;
			
			// 执行复制处理
			esService.copyTemplates(paramJson);
			
			// 加载成功标志
			retJo.put("flag", "0") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_COPY_001", null)) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			Map<String, String> tokens = new HashMap<String,String>() ;
			tokens.put("info", e.getMessage()) ;
			
			retJo.put("flag", "1") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_COPY_002", tokens)) ;
		} 
		
		// 输出返回信息
		this.outPrint(response, retJo.toString());
	}
	
	/**
	 * @Title: expTemplates 
	 * @Description: 导出报表模板
	 * @param request
	 * @param response
	 * @throws Exception    设定文件
	 */
	@RequestMapping(params ="method=expTemplates")
	public void expTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String tabsJson = null ;
		
		try {
			// 导出参数 
			String accHrcyId = request.getParameter("accHrcyId") ;
			String tabArray = request.getParameter("tabArray") ;
			
			// 生成数据
			tabsJson = esService.expTemplates(accHrcyId, tabArray);
			if(tabsJson == null){
				switch(XCUtil.getLang()){
				case XConstants.ZH :
					tabsJson = "空数据>>根据条件未找到合法数据，条件{accHrcyId:"+accHrcyId+",tabArray:"+tabArray+"}" ;
					break;
				case XConstants.EN :
					tabsJson = "empty file>>Not exists valid data; exported conditions >> {accHrcyId:"+accHrcyId+",tabArray:"+tabArray+"}" ;
					break ;
				default:
					break ;
				}
			}
		} catch (Exception e) {
			switch(XCUtil.getLang()){
			case XConstants.ZH :
				tabsJson = "导出失败>>" + e.getMessage() ;
				break;
			case XConstants.EN :
				tabsJson = "exported failure >>" + e.getMessage() ;
				break ;
			default:
				break ;
			}
		}
		
		// 执行下载
		this.downloadTextFile(response, tabsJson, "tabs.json");
	}
	
	/**
	 * 
	 * @Title: impTemplates 
	 * @Description: 模板接收
	 * @param request
	 * @param response    设定文件
	 * @throws Exception 
	 */
	@RequestMapping(params ="method=impTemplates")
	public void impTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject retJo = new JSONObject() ;
		
		try {
			// 科目体系ID
			String accHrcyId = request.getParameter("accHrcyId") ;
			
			// 获取文件内容
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
			List<FileItem> fileList = new ArrayList<FileItem>();
			fileList = servletFileUpload.parseRequest(request);
			if(fileList != null && fileList.size()>0){
				FileItem item = fileList.get(0);
				InputStream is = item.getInputStream() ;
				// 读取文件
				BufferedReader reader = new BufferedReader(new InputStreamReader(is)) ;
				StringBuilder sb = new StringBuilder();     
				String line = null;    
				while ((line = reader.readLine()) != null) {   
					sb.append(line);     
				}  
				is.close();   
				
				// 执行接收
				esService.impTemplates(sb.toString(),accHrcyId);
				
				// 提示信息
				retJo.put("success", true) ;
				retJo.put("flag", "0") ;
				retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_IMP_001", null)) ;
				
			}else{
				String msg = null ;
				switch(XCUtil.getLang()){
				case XConstants.ZH :
					msg = "文件上传失败" ;
					break ;
				case XConstants.EN :
					msg = "file upload failure" ;
					break;
				default:
					break ;
				}
				throw new Exception(msg);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			Map<String,String> tokens = new HashMap<String,String>() ;
			tokens.put("info", e.getMessage()) ;
			
			retJo.put("success", false) ;
			retJo.put("flag", "1") ;
			retJo.put("msg", XipUtil.getMessage(request, "XC_REP_TPL_IMP_002", tokens)) ;
		}
		
		// 输出提示信息
		WFUtil.outPrint(response, retJo.toString());
	}
	
}
