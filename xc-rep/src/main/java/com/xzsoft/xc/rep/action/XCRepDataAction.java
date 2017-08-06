package com.xzsoft.xc.rep.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.xzsoft.xc.rep.modal.CalFormulaBean;
import com.xzsoft.xc.rep.modal.PreFormulaBean;
import com.xzsoft.xc.rep.modal.RepSheetBean;
import com.xzsoft.xc.rep.service.AbstractRepDataHandlerService;
import com.xzsoft.xc.rep.service.XCRepDataService;
import com.xzsoft.xc.rep.util.XCRepFormulaUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;

/**
 * @ClassName: XCRepDataAction 
 * @Description: 报表数据处理控制类
 * @author linp
 * @date 2016年8月3日 上午9:32:23 
 *
 */
@Controller
@RequestMapping("/xcRepDataAction.do") 
public class XCRepDataAction extends BaseAction {
	// 日志记录器
	private static final Logger log = LoggerFactory.getLogger(XCRepDataAction.class) ;
	@Autowired
	private XCRepDataService xcRepDataService ;
	@Autowired
	private AbstractRepDataHandlerService abstractRepDataHandlerService ;
	
	
	
	/**
	 * @Title: queryReportData 
	 * @Description: 填报编制 >> 查询报表指标值信息
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=queryReports")
	public void queryReports(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = Maps.newHashMap() ;
		try {
			// 请求参数格式：{'accHrcyId':'','ledgerId':'','periodCode':'','orgId':'','cnyCode':'','sheetTabId':''}
			String reqParams = request.getParameter("reqParams") ;
			
			// 执行数据查询
			result = xcRepDataService.getReportResult(reqParams) ;
			
			// 标志信息
			result.put("flag", "0") ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			
			result.put("flag", "1") ;
			result.put("msg", e.getMessage()) ;
		}
		
		// 输出结果集
		this.out(response, result);
	}
	
	
	/**
	 * @Title: gatherRepData 
	 * @Description: 报表数据采集
	 * <p>
	 * 	根据报表设置的取数公式计算报表指标数据
	 *  {
	 *  	'ledgerId'   : '',
	 *  	'accHrcyId'  : '',
	 *  	'periodCode' : '',
	 *  	'orgId'      : '',
	 *  	'cnyCode'    : '',
	 *  	'tabOrder'   : '',
	 *  	'opMode'     : 'single/batch'
	 *  	'data' : [{'tabId':''},...]
	 *  }
	 * </p>
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=gatherRepData")
	public void gatherRepData(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		try {
			// 参数信息
			String params = request.getParameter("reqParams") ;
			
			// 执行数据采集处理
			xcRepDataService.gatherRepData(params);
			// 提示信息
			jo.put("flag", "0") ;
			if(XConstants.ZH.equals(language)){
				jo.put("msg", "采集成功!") ;
			}else{
				jo.put("msg", "success!") ;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			if(XConstants.ZH.equals(language)){
				jo.put("msg", "采集失败:"+e.getMessage()) ;
			}else{
				jo.put("msg", "Fail to collect data,cause:"+e.getMessage()) ;
			}
			
		}
		
		this.outPrint(response,jo.toString());
	}
	
	/**
	 * 
	 * @Title: saveOrUpdateReport 
	 * @Description: 执行报表数据保存处理
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=saveOrUpdateReport")
	public void saveOrUpdateReport(HttpServletRequest request, HttpServletResponse response) {
		JSONObject retJo = new JSONObject() ;
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		try {
			// 参数信息
			String paramJson = request.getParameter("extraParams") ;
			String dataJson = request.getParameter("dataJson") ;
			
			// 执行保存或更新处理
			xcRepDataService.saveOrUpdateRepData(paramJson, dataJson);
			
			// 提示信息
			retJo.put("flag", "0") ;
			if(XConstants.ZH.equals(language)){
				retJo.put("msg", "保存成功!") ;
			}else{
				retJo.put("msg", "success!") ;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			
			retJo.put("flag", "1") ;
			if(XConstants.ZH.equals(language)){
				retJo.put("msg", "保存失败>>"+e.getMessage()) ;
			}else{
				retJo.put("msg", "Error,cause:"+e.getMessage()) ;
			}
		}
		
		this.outPrint(response, retJo.toString());
		
	}
	
	/**
	 * @Title: lockOrUnlockReports 
	 * @Description: 锁定/解锁报表
	 *  <p>
	 *  此功能将报表设置为锁定状态，对锁定之后的报表所做的数据修改操作将不会生效。
	 *  参数:
	 *  单表锁定参数格式：
	 *  	{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'single','opType':'lock'} ,
	 *  多表锁定参数格式：
	 *  	{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'batch','opType':'unlock','data':[{'sheetId':''},{'sheetId':''},...]}
	 *  </p>
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=lockOrUnlockReports")
	public void lockOrUnlockReports(HttpServletRequest request, HttpServletResponse response){
		JSONObject jo = new JSONObject() ;
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		try {
			// 参数 
			String params = request.getParameter("reqParams") ;
			// 执行锁定
			xcRepDataService.lockOrUnlockReports(params);
			// 提示信息
			jo.put("flag", "0") ;
			if(XConstants.ZH.equals(language)){
				jo.put("msg", "报表锁定成功！") ;
			}else{
				jo.put("msg", "success!") ;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			if(XConstants.ZH.equals(language)){
				jo.put("msg", "锁定失败:"+e.getMessage()) ;
			}else{
				jo.put("msg", "Fail to lock report,cause:"+e.getMessage()) ;
			}
		}
		this.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: cleanReports 
	 * @Description: 清除报表指标数据
	 * <p>
	 *  功能说明：删除已经生成的报表的指标数据，包固定行和浮动行指标数据
	 *  参数说明:
	 *  	单表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'single'} ,
	 *  	多表锁定参数格式：
	 *  		{'ledgerId':'','periodCode':'','accHrcyId':'','tabOrder':0,'opMode':'batch','data':[{'sheetId':''},{'sheetId':''},...]}
	 * </p>
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params ="method=cleanReports")
	public void cleanReports(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = new JSONObject() ;
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		try {
			// 获取参数
			String reqParams = request.getParameter("reqParams") ;
			
			// 清空报表
			xcRepDataService.cleanReports(reqParams);
			
			// 提示信息
			jo.put("flag", "0") ;
			if(XConstants.ZH.equals(language)){
				jo.put("msg", "清空成功!") ;
			}else{
				jo.put("msg", "success!") ;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			if(XConstants.ZH.equals(language)){
				jo.put("msg", "清空失败: "+e.getMessage()) ;
			}else{
				jo.put("msg", "Error,cause:"+e.getMessage()) ;
			}
			
		}
		
		this.outPrint(response,jo.toString());
	}
	
	/**
	 * @Title: testFormulaCal 
	 * @Description: 公式试运算
	 * <p>
	 * 参数信息：
	 * {
	 *	  'formula': '',
	 *	  'ledgerId'   : '',
	 *	  'accHrcyId'  : '',
	 *	  'orgId'      : '',
	 *	  'cnyCode'    : '',
	 *	  'periodCode' : ''
	 *	}
	 *</p>
	 * @param request
	 * @param response    设定文件
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params ="method=testFormulaCal")
	public void testFormulaCal(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = new JSONObject() ;
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		String exceptionMsg = "参数不合法";
		if(XConstants.EN.equals(language)){
			exceptionMsg = "Invalid parameters!";
		}
		try {
			// 参数
			String params = request.getParameter("reqParams") ;
			if(params == null || "".equals(params)) throw new Exception(exceptionMsg);
			JSONObject paramJo = JSONObject.fromObject(params) ;
			
			// 参数转换
			CalFormulaBean formula = (CalFormulaBean) JSONObject.toBean(paramJo, CalFormulaBean.class) ;
			RepSheetBean sheetBean = (RepSheetBean) JSONObject.toBean(paramJo, RepSheetBean.class) ;
			
			// 预处理公式
			String[] arr = new String[0] ;
			
			//指标取数公式
			String repFormulas[] = XCRepFormulaUtil.getRepFormula(formula.getFormula()+"", arr) ;
			List<PreFormulaBean> preFormulaList = new ArrayList<PreFormulaBean>();
			for(String str : repFormulas){
				JSONObject object = JSONObject.fromObject(XCRepFormulaUtil.getAllCodes(str));
				
				PreFormulaBean cp = new PreFormulaBean();
				cp.setFormulaCat("CAL");
				cp.setFormulaStr(str);
				cp.setFormulaJson(object.toString());
				cp.setFormulaType("REP");
				preFormulaList.add(cp);
			}
			
			//自定义取数公式
			String expFormulas[] = XCRepFormulaUtil.getExpFormula(formula.getFormula()+"", arr) ;
			for(String exp : expFormulas){
				Map<String,String> paramMap = ((Map<String, String>) XCRepFormulaUtil.getAllParam(exp).get("paramMap"));
				JSONObject object = JSONObject.fromObject(paramMap);
				
				PreFormulaBean cp = new PreFormulaBean();
				cp.setFormulaCat("CAL");
				cp.setFormulaStr(exp);
				cp.setFormulaJson(object.toString());
				cp.setFormulaType("EXP");
				preFormulaList.add(cp);
			}			
			// 公式预处理信息
			formula.setPreFormulaBeans(preFormulaList);
			
			// 执行试运算
			String equation = abstractRepDataHandlerService.getArithmeticByFormula(formula, sheetBean) ;
			String result = abstractRepDataHandlerService.evalFormula(equation) ;
			
			result = equation.concat(" = ").concat(result) ;
			
			// 提示信息
			jo.put("flag", "0") ;
			jo.put("result", result) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			if(XConstants.EN.equals(language)){
				jo.put("msg", "Fail to execute formula,cause:"+e.getMessage()) ;
			}else{
				jo.put("msg", "试运算失败: "+e.getMessage()) ;
			}
		}
		this.outPrint(response,jo.toString());
	}
	
}
