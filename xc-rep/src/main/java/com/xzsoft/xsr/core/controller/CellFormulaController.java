package com.xzsoft.xsr.core.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.service.CellFormulaService;
import com.xzsoft.xsr.core.util.JsonUtil;

@Controller
@RequestMapping("/cellFormulaController.do")
public class CellFormulaController {
	private static Logger log = Logger.getLogger(CellFormulaController.class.getName());

	@Autowired
	CellFormulaService cellFormulaService;

	/**
	 * 保存采集公式
	 */
	@RequestMapping(params = "method=saveCellFormat", method = RequestMethod.POST)
	public void saveCellFormat(
			@RequestParam(required = true) String entity_id,
			@RequestParam(required = true) String sheetJson,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String json = "{flag:0, msg:'保存采集公式成功!'}";
		try {
			HashMap<String, String> sessionVars = (HashMap<String, String>) session
					.getAttribute(SessionVariable.XZ_SESSION_VARS);
			String userId = sessionVars.get(SessionVariable.userId);
			String suitId=(String)session.getAttribute("suitId");
			cellFormulaService.saveCellFormat(entity_id, sheetJson, suitId,userId,PlatformUtil.getDbType());
		} catch (Exception e) {
			json = "{flag:1, msg:'保存采集公式出错," + e.getMessage() + "'}";
			log.error("保存采集公式出错",e);
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
	 * 加载数据单元格
	 */
	@RequestMapping(params = "method=loadValue", method = RequestMethod.POST)
	public void loadValue( String msFormatId,String entity_id,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> results = Maps.newHashMap();
		HttpSession session = request.getSession();
		String suitId=(String)session.getAttribute("suitId");
		String json = "";
		try {
			List<CellData> list=cellFormulaService.loadValue(msFormatId,entity_id,suitId,PlatformUtil.getDbType());
			results.put("cells", list); 
		} catch (Exception e) {
			json = "{flag:1, msg:'加载数据单元格出错," + e.getMessage() + "'}";
			log.error("加载数据单元格出错",e);
		}
		// 输出JSON
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
	 * 翻译公式
	 */
	@RequestMapping(params = "method=translate", method = RequestMethod.POST)
	public void translate(
			@RequestParam(required = true) String formulaText,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId=(String)session.getAttribute("suitId");
		String json = "{flag:0, msg:'";
		String result="";
		try {
			result=cellFormulaService.translate(formulaText,suitId,PlatformUtil.getDbType());
			json=json+result+"'}";
		} catch (Exception e) {
			json = "{flag:1, msg:'公式翻译出错," + e.getMessage() + "'}";
			log.error("公式翻译出错",e);
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
	 * 试运算
	 */
	@RequestMapping(params = "method=cellTest", method = RequestMethod.POST)
	public void cellTest(String formulaText,String period_id,String entity_id,String ledgerId,String cnyCode,String cnyId,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId=(String)session.getAttribute("suitId");
		String json = "{flag:0, msg:'";
		String result="";
		try {
			result=cellFormulaService.cellTest(formulaText, period_id, entity_id,suitId,cnyId,ledgerId,cnyCode);
			json=json+result+"'}";
		} catch (Exception e) {
			json = "{flag:1, msg:'试运算出错," + e.getMessage() + "'}";
			log.error("试运算出错",e);
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
	 * 通过行列ID获取行列指标name
	 */
	@RequestMapping(params = "method=getRCName", method = RequestMethod.POST)
	public void getRCName(String RCId,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId=(String)session.getAttribute("suitId");
		String json = "{flag:0, msg:'";
		String result="";
		try {
			result=cellFormulaService.getRCName(RCId, suitId,PlatformUtil.getDbType());
			json=json+result+"'}";
		} catch (Exception e) {
			json = "{flag:1, msg:'获取行列指标名称出错," + e.getMessage() + "'}";
			log.error("获取行列指标名称出错",e);
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
	
	
}
