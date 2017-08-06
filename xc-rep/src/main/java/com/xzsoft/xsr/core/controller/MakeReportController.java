package com.xzsoft.xsr.core.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;
import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.service.FjService;
import com.xzsoft.xsr.core.service.MakeReportService;
import com.xzsoft.xsr.core.util.JsonUtil;
import com.xzsoft.xsr.core.webservice.dc.XCReceive;

/**
 * 报表数据采集
 * 
 * @project 报表3.0
 * @version v1.0
 * @author songyh
 * @Date 2016-01-22
 * @desc 报表生成 页页相关的controller类
 */
@Controller
@RequestMapping("/makeReportController.do")
public class MakeReportController {
	// 日志记录器
	private static Logger log = LoggerFactory.getLogger(MakeReportController.class) ;
	
	@Autowired
	MakeReportService makeReportService;
	@Autowired
	FjService fjService;

	/**
	 * 执行报表数据采集或数据运算
	 */
	@RequestMapping(params = "method=collectData", method = RequestMethod.POST)
	public void collectData(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String json = "";
		String entityId = request.getParameter("entityId");
		String periodId = request.getParameter("periodId");
		String currencyId = request.getParameter("currencyId");
		String mdlShtIdList = request.getParameter("mdlShtIdList"); // 模板id列表
		String fetchFlag = request.getParameter("fetchFlag"); // 是否执行数据ETL
		String fjFlag = request.getParameter("fjFlag"); // 是否采集浮动行数据
		String fType = request.getParameter("fType"); // 运算类型：REP－报表运算；APP－数据采集
		
		String ledgerId = request.getParameter("ledgerId") ;
		String cnyCode = request.getParameter("currencyCode") ;
		
		try {
			String userId = CurrentSessionVar.getUserId() ;
			makeReportService.collectData(suitId, entityId, periodId, currencyId, mdlShtIdList, fetchFlag, fjFlag,
					fType, userId,ledgerId,cnyCode);
			json = "{flag:0}";
		} catch (Exception e) {
			json = "{flag:1, msg:'数据采集出错," + e.getMessage() + "'}";
			log.error("数据采集出错", e);
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 报表锁定或解锁
	 */
	@RequestMapping(params = "method=lockReport", method = RequestMethod.POST)
	public void lockReport(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String json = "";
		String entityId = request.getParameter("entityId");
		String periodId = request.getParameter("periodId");
		String currencyId = request.getParameter("currencyId");
		String mdlShtIdList = request.getParameter("mdlShtIdList"); // 模板id列表
		String lockType = request.getParameter("lockType"); // A-解锁；E－锁定
		try {
			String userId = CurrentSessionVar.getUserId() ;
			makeReportService.lockReport(userId, suitId, entityId, periodId, currencyId, mdlShtIdList, lockType);
		} catch (Exception e) {
			json = "{flag:1, msg:'报表锁定/解锁出错," + e.getMessage() + "'}";
			log.error("报表锁定/解锁出错", e);
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * @Title: clearData 
	 * @Description: 清空报表数据
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params = "method=clearData", method = RequestMethod.POST)
	public void clearData(HttpServletRequest request, HttpServletResponse response) {
		JSONObject jo = new JSONObject() ;

		try {
			HttpSession session = request.getSession();
			String suitId = (String) session.getAttribute("suitId");
			
			String entityId = request.getParameter("entityId");
			String periodId = request.getParameter("periodId");
			String currencyId = request.getParameter("currencyId");
			String mdlShtIdList = request.getParameter("mdlShtIdList"); // 模板id列表
			
			String userId = CurrentSessionVar.getUserId() ;
			makeReportService.clearData(userId, suitId, entityId, periodId, currencyId, mdlShtIdList);
			
			jo.put("flag", "0") ;
			jo.put("msg", "数据清除成功") ;
			
		} catch (Exception e) {
			log.error("数据清除出错", e);
			
			jo.put("flag", "1") ;
			jo.put("msg", "数据清除失败："+e.getMessage()) ;			
		}
		
		WFUtil.outPrint(response, jo.toString());
	}

	/**
	 * @Title: loadDataValue 
	 * @Description: 获取报表数据
	 * @param msFormatId
	 * @param ledgerId
	 * @param entityId
	 * @param periodId
	 * @param cnyId
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params = "method=loadDataValue", method = RequestMethod.POST)
	public void loadDataValue(String msFormatId, String ledgerId, String entityId, String periodId, String cnyId,
			HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> results = Maps.newHashMap();
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		// 标记：qry-查询，其他-填报
		String mark = request.getParameter("mark");
		try {
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("suitId", suitId);
			map.put("msformatId", msFormatId);
			map.put("ledgerId", ledgerId) ;
			map.put("entityId", entityId);
			map.put("periodId", periodId);
			map.put("cnyId", cnyId);
			map.put("dbType", PlatformUtil.getDbType());
			map.put("mark", mark);
			
			List<CellData> list = makeReportService.loadDataValue(map);
			
			results.put("cells", list);
			
		} catch (Exception e) {
			log.error("加载数据单元格出错", e);
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
			log.error("传输json串出错", e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * 判断是否为汇总单位
	 * 
	 * @param entityId
	 *            公司ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=isHrchyEntity", method = RequestMethod.POST)
	public void isHrchyEntity(String entityId, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String json = "";
		try {
			boolean leafFlag = makeReportService.isHrchyEntity(suitId, entityId);
			json = "{flag:'0',leafFlag:'" + leafFlag + "'}";
		} catch (Exception e) {
			json = "{flag:'1',msg:'" + e.getMessage() + "'}";
			log.error("判断是否为汇总单位出错:" + e.getMessage());
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 获取汇总单位下的所有直接单位
	 * 
	 * @param entityId
	 *            汇总单位ID
	 * @param hrchyId
	 *            公司层次ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getHrchySon", method = RequestMethod.POST)
	public void getHrchySon(String entityId, String hrchyId, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String json = "";
		try {
			String entity_ids = makeReportService.getHrchySon(suitId, entityId, hrchyId);
			json = "{flag:'0',entity_ids:'" + entity_ids + "'}";
		} catch (Exception e) {
			json = "{flag:'1',msg:'" + e.getMessage() + "'}";
			log.error("获取汇总单位下的所有直接单位出错:" + e.getMessage());
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 获取层次中所有的汇总节点
	 * 
	 * @param entityId
	 *            汇总单位ID
	 * @param hrchyId
	 *            公司层次ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getHrchySonNode", method = RequestMethod.POST)
	public void getHrchySonNode(String entityId, String hrchyId, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String json = "";
		try {
			String entity_ids = makeReportService.getHrchySonNode(suitId, entityId, hrchyId, "");
			json = "{flag:'0',entity_ids:'" + entity_ids + "'}";
		} catch (Exception e) {
			json = "{flag:'1',msg:'" + e.getMessage() + "'}";
			log.error("获取层次中所有的汇总节点出错:" + e.getMessage());
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 节点检查
	 * 
	 * @param periodId
	 * @param cnyId
	 * @param hrchyId
	 * @param entityId
	 * @param mdltypeId
	 * @param mdlshtIdlist
	 *            所有模板
	 * @param corpIdList
	 *            汇总单位下的所有直接单位
	 * @param checkEveryHrchy
	 *            节点检查是否为层层检查
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=hrchyCheck", method = RequestMethod.POST)
	public void hrchyCheck(String periodId, String cnyId, String hrchyId, String entityId, String mdltypeId,
			String mdlshtIdlist, String corpIdList, String checkEveryHrchy, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String userId = CurrentSessionVar.getUserId() ;
		String[] mdlshtlist = mdlshtIdlist.split(","); // 模板列表
		String json = "";
		String checkFlag = "true";
		String flag = "true";
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("suitId", suitId);
			map.put("userId", userId);
			map.put("hrchyId", hrchyId);
			map.put("entityId", entityId);
			map.put("periodId", periodId);
			map.put("mdltypeId", mdltypeId);
			map.put("cnyId", cnyId);
			map.put("dbType", PlatformUtil.getDbType());

			if (mdlshtlist.length > 0) {
				for (int i = 0; i < mdlshtlist.length; i++) {
					map.put("mdlshtId", mdlshtlist[i]);
					if ("Y".equals(checkEveryHrchy)) {// 层层检查
						flag = makeReportService.hrchyCheckAll(map);
					} else {
						flag = makeReportService.hrchyCheck(map);
					}
					if (flag.equals("false")) {
						checkFlag = "false";
					}
				} // 循环模板结束

				if ("true".equals(checkFlag)) {
					json = "{msg:'节点检查成功',flag:'0'}";
				} else {
					json = "{msg:'',flag:'1', checkTime:'" + new Date() + "'}";
				}
			}

		} catch (Exception e) {
			json = "{flag:'2',msg:'" + e.getMessage() + "'}";
			log.error("节点检查出错:" + e.getMessage());
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 数据校验：:浮动表主行数与明细行合计数
	 * 
	 * @param periodId
	 * @param cnyId
	 * @param hrchyId
	 * @param entityId
	 * @param mdlshtIdlist
	 *            所有模板
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=verifyFj", method = RequestMethod.POST)
	public void verifyFj(String periodId, String cnyId, String entityId, String mdltypeId, String mdlshtIdlist,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String userId = CurrentSessionVar.getUserId() ;
		String[] mdlshtlist = mdlshtIdlist.split(","); // 模板列表
		String json = "";
		String flag = "true";
		String checkFlag = "true";
		int flagInt = 0;
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("suitId", suitId);
			map.put("userId", userId);
			map.put("entityId", entityId);
			map.put("periodId", periodId);
			map.put("cnyId", cnyId);
			map.put("mdltypeId", mdltypeId);

			if (mdlshtlist.length > 0) {
				// 在数据校验前先删除相应结果数据
				makeReportService.delVerifyFjResult(userId);
				for (int i = 0; i < mdlshtlist.length; i++) {
					map.put("mdlshtId", mdlshtlist[i]);
					// 数据校验
					flag = makeReportService.verifyFj(map);
					if (flag.equals("false")) {
						checkFlag = "false";
						flagInt = 1;
					} else if (flag.contains("出错")) {
						checkFlag = "数据校验出错：" + flag;
						flagInt = 2;
						break;
					} else {
						flagInt = 0;
					}
				} // 循环模板结束

				if ("true".equals(checkFlag)) {
					json = "{msg:'数据校验成功',flag:'" + flagInt + "'}";
				} else {
					json = "{msg:'" + checkFlag + "',flag:'" + flagInt + "', checkTime:'" + new Date() + "'}";
				}
			}

		} catch (Exception e) {
			json = "{flag:'2',msg:'" + e.getMessage() + "'}";
			log.error("数据校验出错:" + e.getMessage());
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 报表固定行数据保存--liuyang/20160222
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=saveRepData", method = RequestMethod.POST)
	public void saveRepData(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String ledgerId = request.getParameter("ledgerId") ;
		String entity_id = request.getParameter("entityId");
		String sheetjsonArray = request.getParameter("sheetjsonArray");
		String periodId = request.getParameter("periodId");
		String cnyId = request.getParameter("cnyId");
		String modaltypeId = request.getParameter("modaltype_id");
		String modalsheetIdArray = request.getParameter("modalsheetIdArray");
		
		String json = "{flag:0, msg:'保存数据成功!'}";
		
		try {
			String userId = CurrentSessionVar.getUserId() ;
			String suitId = (String) session.getAttribute("suitId");
			List<Object> mst = JsonUtil.parseArray(modalsheetIdArray);
			List<Object> stj = JsonUtil.parseArray(sheetjsonArray);
			for (int i = 0; i < mst.size(); i++) {
				Map<String, Object> mstMap = (Map<String, Object>) mst.get(i);
				String modalsheetId = (String) mstMap.get("modalsheetId");
				String msFormatId = (String) mstMap.get("msFormatId");
				String sheetJson = "";
				for (int i2 = 0; i2 < stj.size(); i2++) {
					Map<String, Object> stjMap = (Map<String, Object>) stj.get(i2);
					System.out.println(stjMap.get("msFormatId"));
					if (((String) stjMap.get("msFormatId")).equals(msFormatId)) {
						sheetJson = JsonUtil.toJson(stjMap.get("reportData"));
					}
				}
				makeReportService.saveRepData(modaltypeId, modalsheetId, periodId, cnyId, ledgerId, entity_id, sheetJson, userId, suitId);
			}

		} catch (Exception e) {
			json = "{flag:1, msg:'报表数据保存出错," + e.getMessage() + "'}";
			log.error("报表数据保存出错", e);
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 查询模版浮动行信息--liuyang/20160225
	 */
	@RequestMapping(params = "method=getFjRowitemInfo", method = RequestMethod.POST)
	public void getFjRowitemInfo(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String modalsheetId = request.getParameter("modalsheetId");
		String modaltypeId = request.getParameter("modaltypeId");
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try {
			result = makeReportService.getFjRowitemInfo(suitId, modaltypeId, modalsheetId);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		Writer writer = null;
		try {
			writer = response.getWriter();
			JsonUtil.toJson(result, writer);
			writer.flush();
		} catch (Exception e) {
			log.error("传输json串出错", e);
		} finally {
			IOUtils.closeQuietly(writer);
		}

	}

	/**
	 * 数据校验：:判断报表是否已生成
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=isSheet", method = RequestMethod.POST)
	public void isSheet(String periodId, String cnyId, String entityId, String mdlshtId, String mdltypeId,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String flag = "false";
		String json = null;
		Map<String,String> map = new HashMap<String,String>();
		map.put("suitId", suitId);
		map.put("mdlshtId", mdlshtId);
		map.put("mdltypeId", mdltypeId);
		map.put("entityId", entityId);
		map.put("periodId", periodId);
		map.put("cnyId", cnyId);
		try {
			flag = makeReportService.isSheet(map);
			if ("true".equals(flag)) {
				json = "{flag:'0',msg:'报表已生成'}";
			} else {
				json = "{msg:'',flag:'1', msg:'当前汇总单位在所选期间下未生成报表,不能按进行分析查询'}";
			}

		} catch (Exception e) {
			json = "{flag:'1',msg:'" + e.getMessage() + "'}";
			log.error("查询报表是否已生成出错:" + e.getMessage());
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 获取层次中的所有子孙节点
	 * 
	 * @param entityId
	 *            汇总单位ID
	 * @param hrchyId
	 *            公司层次ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=getHrchyAllSon", method = RequestMethod.POST)
	public void getHrchyAllSon(String entityId, String hrchyId, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		String json = "";
		try {
			String entity_ids = makeReportService.getHrchyAllSon(suitId, entityId, hrchyId, "");
			json = "{flag:'0',entity_ids:'" + entity_ids + "'}";
		} catch (Exception e) {
			json = "{flag:'1',msg:'" + e.getMessage() + "'}";
			log.error("获取层次中所有子孙节点出错:" + e.getMessage());
		}
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 报表打开时向临时表中插入浮动行明细数据---liuyang/20160304
	 */
	@RequestMapping(params = "method=instTempFjData", method = RequestMethod.POST)
	public void instTempFjData(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String modalsheetId = request.getParameter("modalsheetId");
		String modaltypeId = request.getParameter("modaltypeId");
		String entityId = request.getParameter("entityId");
		String periodId = request.getParameter("periodId");
		String currencyId = request.getParameter("currencyId");

		try {
			String userId = CurrentSessionVar.getUserId() ;
			String suitId = (String) session.getAttribute("suitId");
			if (userId != null && !"".equals(userId)) {
				fjService.instFjDataToTemp(userId, suitId, modalsheetId, modaltypeId, entityId, periodId, currencyId);
			}
		} catch (Exception e) {
			log.error("向临时表插入数据出错", e);
		}

	}

	/**
	 * 数据抽取
	 */
	@RequestMapping(params = "method=extractData", method = RequestMethod.POST)
	public void extractData(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String entityId = request.getParameter("entityId");
		String dcId = (String) session.getAttribute("defaultDcId");
		String suitId = (String) session.getAttribute("suitId");
		String json = "";
		ApplicationContext context = AppContext.getApplicationContext();
		XCReceive xCReceive = (XCReceive) context.getBean("xCReceive");
		try {
			xCReceive.extractData(dcId, entityId, suitId);
			json = "{flag:'0',msg:'数据抽取成功'}";
		} catch (Exception e) {
			json = "{flag:'1',msg:'" + e.getMessage() + "'}";
			log.error("数据抽取失败", e);
		}
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

}
