package com.xzsoft.xc.rep.action;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cubedrive.base.utils.JsonUtil;
import com.google.common.collect.Maps;
import com.xzsoft.xc.rep.modal.ReportCellFormula;
import com.xzsoft.xc.rep.modal.ReportTabBean;
import com.xzsoft.xc.rep.modal.ReportTabCellBean;
import com.xzsoft.xc.rep.modal.ReportTabElementBean;
import com.xzsoft.xc.rep.service.XCRepFormulaManageService;
import com.xzsoft.xc.rep.service.XCReportModuleService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/**
 * @ClassName: XCRepCellManageAction 
 * @Description: 报表模板公式设置处理类
 * @author tangxl
 * @date 2016年8月3日 上午9:32:23 
 *
 */
@Controller
@RequestMapping("/xCRepFormulaManageAction.do") 
public class XCRepFormulaManageAction extends BaseAction {
	// 日志记录器
	private static final Logger log = LoggerFactory.getLogger(XCRepFormulaManageAction.class) ;
	@Resource
	private XCRepFormulaManageService xCRepFormulaManageService;
	@Resource
	private XCReportModuleService xCReportModuleService;
	/**
	 * 
	 * @methodName  saveCellFormula
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    报表数据采集公式保存
	 * <p>保存公共级别报表时，orgId和ledgerId为 -1</p>
	 * @param 		request
	 * @param 		response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params ="method=saveCellFormula")
	public void saveCellFormula(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		
		// 参数信息
		String orgId = request.getParameter("orgId");
		String accHrcyId = request.getParameter("accHrcyId");
		String sheetJson = request.getParameter("sheetJson");
		String ledgerId = request.getParameter("ledgerId");
		String formulaCat = "ledger";
		String language = XConstants.ZH;
		try {
			 language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		if("-1".equals(ledgerId)){	// 公共级公式
			formulaCat = "common";
		}
		
		String userId =CurrentSessionVar.getUserId();
		String dbType = PlatformUtil.getDbType();
		
		try {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("entityId", orgId);
				map.put("sheetJson", sheetJson);
				map.put("ledgerId", ledgerId);
				map.put("formulaCat", formulaCat);
				map.put("userId", userId);
				map.put("accHrcyId", accHrcyId);
				map.put("dbType", dbType);
				
				xCRepFormulaManageService.saveCellFormula(map);
				// 提示信息
				jo.put("flag", "0") ;
				if(XConstants.EN.equalsIgnoreCase(language)){
					jo.put("msg", "Save formulas success!") ;
				}else{
					jo.put("msg", "报表采集公式保存成功！") ;
				}
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			if(XConstants.EN.equalsIgnoreCase(language)){
				jo.put("msg", "Error occuring when saving formula,cause:") ;
			}else{
				jo.put("msg", "报表采集公式保存出错，原因："+e.getMessage()) ;
			}
		}finally{
			WFUtil.outPrint(response,jo.toString());
		}
	}
	/**
	 * 
	 * @methodName  loadReportModule
	 * @author      tangxl
	 * @date        2016年9月2日
	 * @describe    采集公式设置 -- 加载要设置公式的模板
	 * <document>
	 *   <title>根据【账簿】+【科目体系】加载模板</title>
	 *   <p>
	 *    存在多张报表时，是加载第一张报表的内容和格式，其他报表仅加载标题和序号
	 *    ledgerId：账簿id，公共级和账簿级报表的区别
	 *    accHrcyId：科目体系id,
	 *    tabOrder：模板序号（自然数，若为-1，表示查所有的模板）
	 *   </p>
	 * </document>
	 * @param request
	 * @param response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params="method=loadReportModule")
	public void loadReportModule(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> results = null;
		String language = XConstants.ZH;
		try {
			 language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		try {
			// 参数
			String ledgerId = request.getParameter("ledgerId");
			String accHrcyId = request.getParameter("accHrcyId");
			String tabOrder = request.getParameter("tabOrder");
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ledgerId", ledgerId);
			map.put("accHrcyId", accHrcyId);
			map.put("tabOrder", tabOrder);
			
			// 加载
			results = xCReportModuleService.loadReportModule(map);
			
		} catch (Exception e) {
			results = Maps.newHashMap();
			if(XConstants.EN.equalsIgnoreCase(language)){
				results.put("fileName", "Formulas Setting");
			}else{
				results.put("fileName", "取数公式设置");
			}
			results.put("sheets", new ArrayList<ReportTabBean>());
			results.put("cells", new ArrayList<ReportTabCellBean>());
			results.put("floatings", new ArrayList<ReportTabElementBean>());
			results.put("formulas", new ArrayList<ReportCellFormula>());
			if(XConstants.EN.equalsIgnoreCase(language)){
				log.error("Fail to load module>> "+e.getMessage());
			}else{
				log.error("加载报表模板失败>> "+e.getMessage());
			}
		}
		
		this.out(response, results);
	}
	/**
	 * 
	 * @methodName  translateFormula
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    公式翻译
	 * <document>公式翻译：跟原始公式翻译基本一致</document>
	 * @param       request
	 * @param       response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params="method=translateFormula")
	public void translateFormula(HttpServletRequest request,HttpServletResponse response){
		String formulaText = request.getParameter("formulaText");
		String accHrcyId = request.getParameter("accHrcyId");
		String ledgerId = request.getParameter("ledgerId");
		JSONObject returnObject = new JSONObject();
		String language = XConstants.ZH;
		try {
			 language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		try {
			String resultStr = xCReportModuleService.translateFormula(formulaText,accHrcyId,ledgerId);
			returnObject.put("flag", "0");
			returnObject.put("msg", resultStr);
		} catch (Exception e) {
			returnObject.put("flag", "1");
			if(XConstants.EN.equalsIgnoreCase(language)){
				returnObject.put("msg", "Fail to translate formulas,cause:"+e.getMessage());
			}else{
				returnObject.put("msg", "公式翻译异常："+e.getMessage());
			}
			
		}finally{
			PlatformUtil.outPrint(response, returnObject.toString());
		}
	}
	/**
	 * 
	 * @methodName  formulaTest
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    测试公式
	 * @param 		request
	 * @param 		response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params="method=formulaTest")
	public void formulaTest(HttpServletRequest request,HttpServletResponse response){
		JSONObject returnObject = new JSONObject();
		String formulaText = request.getParameter("formulaText");
		String ledgerId = request.getParameter("ledgerId");
		String accHrcyId = request.getParameter("accHrcyId");
		String orgId = request.getParameter("orgId");
		String cnyCode = request.getParameter("cnyCode");
		String periodCode = request.getParameter("periodCode");
		String language = XConstants.ZH;
		try {
			 language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("formulaText", formulaText);
		map.put("ledgerId", ledgerId);
		map.put("accHrcyId", accHrcyId);
		map.put("orgId", orgId);
		map.put("cnyCode", cnyCode);
		map.put("periodCode", periodCode);
		try {
			String resultStr = xCReportModuleService.formulaTest(map);
			returnObject.put("flag", "0");
			returnObject.put("msg", resultStr);
		} catch (Exception e) {
			returnObject.put("flag", "1");
			if(XConstants.EN.equalsIgnoreCase(language)){
				returnObject.put("msg", "Fail to execute formulas,cause:"+e.getMessage());
			}else{
			    returnObject.put("msg", "公式试运算失败："+e.getMessage());
			}
			
		}finally{
			PlatformUtil.outPrint(response, returnObject.toString());
		}
	}
	/**
	 * 
	 * @methodName  loadSheet
	 * @author      tangxl
	 * @date        2016年9月14日
	 * @describe    报表查询
	 * @param 		request
	 * @param 		response
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params="method=loadSheet")
	public void loadSheet(HttpServletRequest request,HttpServletResponse response){
		String sheetIds = request.getParameter("sheetIds");
		String querySheetId = request.getParameter("querySheetId");
		Writer writer = null;
		Map<String, Object> results = null;
		try {
			results = xCReportModuleService.loadSheet(sheetIds,querySheetId);
		} catch (Exception e) {
			results = Maps.newHashMap();
			results.put("fileName", "sheet");
			results.put("sheets", new ArrayList<ReportTabBean>());
			results.put("cells", new ArrayList<ReportTabCellBean>());
			results.put("floatings", new ArrayList<ReportTabElementBean>());
			results.put("formulas", new ArrayList<ReportCellFormula>());
			//sheetReflect  -- 记录报表id和Enterprise Sheet对应序号的json
			results.put("sheetReflect", "");
			e.printStackTrace();
			log.error("XCRepFormulaManageAction-->loadReportModule：fail to load report module,cause："+e.getMessage());
		}finally{
			response.setContentType("text/html;charset=UTF-8");
			response.setHeader("Cache-Control", "no-store, no-cache");
			response.setHeader("Pragma", "no-cache");
			try {
				writer = response.getWriter();
				JsonUtil.toJson(results,writer);
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(writer != null)
				IOUtils.closeQuietly(writer);
		}
	}
}
