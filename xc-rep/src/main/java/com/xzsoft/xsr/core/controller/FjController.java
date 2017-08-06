package com.xzsoft.xsr.core.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.service.FjService;
import com.xzsoft.xsr.core.util.JsonUtil;

@Controller
@RequestMapping("/fjController.do")
public class FjController {
	private static Logger log = Logger.getLogger(FjController.class.getName());
	
	@Resource
	private FjService fjservice;
	
	
	/**
	 * 获取数据源字段
	 */
	@RequestMapping(params = "method=getFields")
	public void getFields(HttpServletRequest request, HttpServletResponse response){
		String metaData = "{'fields':[{'name':'COLUMN_NAME','type':'string'},{'name':'COLUMN_TYPE','type':'string'}]}";
		String srctype=request.getParameter("srctype");
		String datasrc=request.getParameter("datasrc");
//		System.out.println(srctype);
//		System.out.println(datasrc);
		String sql="";
		if(srctype.equals("V")){
			sql="SELECT * FROM "+datasrc;
		}else if(srctype.equals("S")){
			sql=datasrc;
		}
		
		try {
			String json=fjservice.getFields(sql);
			PlatformUtil.outPrint(response, json); 
		} catch (Exception e) {
			log.error("获取数据源字段失败", e);
			String json  = JSONUtil.parseStoreJsonForExt4(0, "false", metaData, "");
	        PlatformUtil.outPrint(response, JSONUtil.Encode(json));
			
		}
		
	}
	
	/**
	 * 加载浮动行模版格式
	 */
	@RequestMapping(params = "method=loadFjModalsheetFormat")
	public void loadFjModalsheetFormat(
			@RequestParam(required = true) String msFormatId,
			@RequestParam(required = true) String modalsheetName,
			@RequestParam(required = true) String titleMaxRow,
			@RequestParam(required = true) String rowItemCode,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId=(String)session.getAttribute("suitId");
		String modalsheetId=request.getParameter("modalsheetId");
		String modaltypeId=request.getParameter("modaltypeId");
		String rowItemId=request.getParameter("rowItemId");
		String entityId=request.getParameter("entityId");
		String mark=request.getParameter("mark");
		String periodId=request.getParameter("periodId");
		String currencyId=request.getParameter("currencyId");
		Map<String, Object> results = Maps.newHashMap();
		try {
			String userId = CurrentSessionVar.getUserId() ;
			results = fjservice.loadFjModalsheetFormat(userId,suitId, modalsheetId, modaltypeId, rowItemId, entityId,msFormatId, modalsheetName,titleMaxRow,rowItemCode,mark,periodId,currencyId);
		} catch (Exception e) {
			log.error("加载模板格式出错",e);
		}
		String contentType = "text/html;charset=UTF-8";
		response.setContentType(contentType);
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setHeader("Pragma", "no-cache");
		Writer writer = null;
		try {
			writer = response.getWriter();
			JsonUtil.toJson(results, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("传输json串出错",e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	/**
	 * 报表查询时，主从表合并加载
	 */
	@RequestMapping(params = "method=loadRepFormat" ,method = RequestMethod.POST)
	public void loadRepFormat(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String suitId=(String)session.getAttribute("suitId");
		String msFormatId=request.getParameter("msFormatId");
		String modalsheetName=request.getParameter("modalsheetName");
		String titleMaxRow=request.getParameter("titleMaxRow");
//		String rowItemCode=request.getParameter("rowItemCode");
		String modalsheetId=request.getParameter("modalsheetId");
		String modaltypeId=request.getParameter("modaltypeId");
//		String rowItemId=request.getParameter("rowItemId");
		String ledgerId = request.getParameter("ledgerId") ;
		String entityId=request.getParameter("entityId");
		String periodId=request.getParameter("periodId");
		String currencyId=request.getParameter("currencyId");
		Map<String, Object> results = Maps.newHashMap();
		try {
			String userId = CurrentSessionVar.getUserId() ;
			results = fjservice.loadRepFormat(userId, suitId, modalsheetId, modaltypeId, entityId, msFormatId, modalsheetName, titleMaxRow, periodId, currencyId);
		} catch (Exception e) {
			log.error("加载报表模板格式出错",e);
		}
		String contentType = "text/html;charset=UTF-8";
		response.setContentType(contentType);
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setHeader("Pragma", "no-cache");
		Writer writer = null;
		try {
			writer = response.getWriter();
			JsonUtil.toJson(results, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("传输json串出错",e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	/**
	 * 浮动行公式设置数据提交
	 */
	@RequestMapping(params = "method=saveCellFormula", method = RequestMethod.POST)
	public void saveCellFormula(
			@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String entityId,
			@RequestParam(required = true) String titleMaxRow,
			@RequestParam(required = true) String rowItemId,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String modaltypeId=request.getParameter("modaltypeId");
		String startCol=request.getParameter("startCol");
		String sheetJson=request.getParameter("sheetJson");
		String mark=request.getParameter("mark");
		String periodId=request.getParameter("periodId");
		String currencyId=request.getParameter("currencyId");
		String rowItemCode=request.getParameter("rowItemCode");
		String json = "{flag:0, msg:'数据提交成功!'}";
		try {
			String userId = CurrentSessionVar.getUserId() ;
			String suitId=(String)session.getAttribute("suitId");
			if("FjData".equals(mark)){
				if(userId==null || "".equals(userId)){
					json = "{flag:2, msg:'session失效，无法进行数据保存!'}";
				}else{
					fjservice.saveFjCellDataToTemp(modaltypeId, modalsheetId, entityId, titleMaxRow, rowItemId, sheetJson, startCol, suitId, userId,periodId,currencyId,rowItemCode);
					List<Map<String, String>> sumresult=fjservice.sumFjDataTemp(userId, suitId, modalsheetId, modaltypeId, rowItemId, entityId, periodId, currencyId);
					json="{flag:0, msg:'数据提交成功!',result:"+JsonUtil.toJson(sumresult)+"}";
				}
			}else{
				fjservice.saveCellFormula(modaltypeId,modalsheetId,entityId,titleMaxRow,rowItemId,sheetJson,startCol,suitId,userId);
			}
		} catch (Exception e) {
			json = "{flag:1, msg:'数据提交出错," + e.getMessage() + "'}";
			log.error("浮动行数据提交出错",e);
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错",e);
		}
	}
	/**
	 * 报表浮动行数据提交
	 */
	@RequestMapping(params = "method=saveFjCellData", method = RequestMethod.POST)
	public void saveFjCellData(HttpServletRequest request, HttpServletResponse response){
		String modalsheetId=request.getParameter("modalsheetId");
		String entityId=request.getParameter("entityId");
		String titleMaxRow=request.getParameter("titleMaxRow");
		String rowItemId=request.getParameter("rowItemId");
		saveCellFormula(modalsheetId, entityId, titleMaxRow, rowItemId, request, response);
	}
	/**
	 * 报表浮动行数据加载
	 */
	@RequestMapping(params = "method=loadFjCellData", method = RequestMethod.POST)
	public void loadFjCellData(HttpServletRequest request, HttpServletResponse response){
		String msFormatId=request.getParameter("msFormatId");
		String modalsheetName=request.getParameter("modalsheetName");
		String titleMaxRow=request.getParameter("titleMaxRow");
		String rowItemCode=request.getParameter("rowItemCode");
		loadFjModalsheetFormat(msFormatId, modalsheetName, titleMaxRow, rowItemCode, request, response);
	}
	/**
	 * 查询报表浮动行数据
	 */
	@RequestMapping(params = "method=repQryFjCellData", method = RequestMethod.POST)
	public void repQryFjCellData(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String suitId=(String)session.getAttribute("suitId");
		String msFormatId=request.getParameter("msFormatId");
		String modalsheetName=request.getParameter("modalsheetName");
		String titleMaxRow=request.getParameter("titleMaxRow");
		String rowItemCode=request.getParameter("rowItemCode");
		String modalsheetId=request.getParameter("modalsheetId");
		String modaltypeId=request.getParameter("modaltypeId");
		String rowItemId=request.getParameter("rowItemId");
		String entityId=request.getParameter("entityId");
		String periodId=request.getParameter("periodId");
		String currencyId=request.getParameter("currencyId");
		Map<String, Object> results = Maps.newHashMap();
		try {
			results = fjservice.repQryFjCellData(suitId, modalsheetId, modaltypeId, rowItemId, entityId,msFormatId, modalsheetName,titleMaxRow,rowItemCode,periodId,currencyId);
		} catch (Exception e) {
			log.error("加载模板格式出错",e);
		}
		String contentType = "text/html;charset=UTF-8";
		response.setContentType(contentType);
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setHeader("Pragma", "no-cache");
		Writer writer = null;
		try {
			writer = response.getWriter();
			JsonUtil.toJson(results, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("传输json串出错",e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	/**
	 * 删除临时表数据(多模版)
	 */
	@RequestMapping(params = "method=delFjDataTempAll", method = RequestMethod.POST)
	public void delFjDataTempAll(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		String userId = CurrentSessionVar.getUserId() ;
		String suitId=(String)session.getAttribute("suitId");
		if(userId==null || "".equals(userId)){
			System.out.println("session失效-----");
			return;
		}
		String entityId=request.getParameter("entityId");
		String periodId=request.getParameter("periodId");
		String currencyId=request.getParameter("currencyId");
		String modalsheetIds=request.getParameter("modalsheetIds");
		List<String> mstIds=new ArrayList<String>();
		String[] ids=modalsheetIds.split(",");
		for(int i=0;i<ids.length;i++){
			String id=ids[i];
			if(id!=null&&!"".equals(id)){
				mstIds.add(id);
			}
		}
		Map<String, Object> param=new HashMap<String, Object>();
		param.put("suitId",suitId );
		param.put("userId",userId );
		param.put("entityId",entityId );
		param.put("periodId", periodId);
		param.put("currencyId",currencyId );
		param.put("mstIds",mstIds );
		try {
			fjservice.delFjDataTempAll(param);
			System.out.println("临时表数据删除成功-----");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 通过列指标编码获取列id
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=getColitemIdByCode", method = RequestMethod.POST)
	public void getColitemIdByCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		String suitId=(String)session.getAttribute("suitId");
		String colitemCode=request.getParameter("colitemCode");
		Map<String, String>param=new HashMap<String, String>();
		param.put("colitemCode", colitemCode);
		param.put("suitId", suitId);
		Map<String, String>result=fjservice.getColitemIdByCode(param);
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		Writer writer = null;
		try {
			writer = response.getWriter();
			JsonUtil.toJson(result, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("传输json串出错",e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
		
	}
	
	

}
