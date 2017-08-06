package com.xzsoft.xsr.core.webservice.internal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cxf.endpoint.Client;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.mapper.MakeReportMapper;
import com.xzsoft.xsr.core.service.FjService;
import com.xzsoft.xsr.core.service.LoggerManage;
import com.xzsoft.xsr.core.service.MakeReportService;
import com.xzsoft.xsr.core.util.JsonUtil;
import com.xzsoft.xsr.core.webservice.WebServiceUtil;
/**
 * 报表系统内部的webservice数据上传
 * @author liuyang
 * @date 2016-04-27
 */
@Controller
@RequestMapping("/internalDataUpload.do")
public class InternalDataUpload {
	public Logger log = Logger.getLogger(this.getClass());
	
	@Resource
	private MakeReportMapper makeReportMapper;
	@Resource
	private FjService fjservice;
	@Resource
	private MakeReportService makeReportService;
	@Resource
	private LoggerManage logmanage;
	
	@RequestMapping(params = "method=repDataUpload" ,method = RequestMethod.POST)
	public void repDataUpload(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String dbType=PlatformUtil.getDbType();
		HttpSession session = request.getSession();
		String sessionId=session.getId();
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		String userName = sessionVars.get(SessionVariable.userName);
		String suitId=(String)session.getAttribute("suitId");
		String suitCode=(String)session.getAttribute("suitCode");
		String serverId=request.getParameter("serveId");
//		String address=request.getParameter("address");
		String address="http://localhost:8120/xsr/services/internalDataService?wsdl";
		String modalsheets=request.getParameter("modalsheets");
		String modaltypeId=request.getParameter("modaltypeId");
		String modaltypeCode=request.getParameter("modaltypeCode");
		String entitys=request.getParameter("entitys");
		String periodId=request.getParameter("periodId");
		String cnyId=request.getParameter("cnyId");
		//创建客户端
		Client client=WebServiceUtil.getWebServiceClient(address);
		HashMap params = new HashMap();
		 params.put("PERIOD_ID", periodId);
		 params.put("CURRENCY_ID", cnyId);
		//根据ID获取各维度的code值
		String periodCode=null;
		String currencyCode=null;
		List<HashMap> repCodesMap=makeReportMapper.getRepCodes(params);
		if (repCodesMap.size()>0) {
			periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
			currencyCode=repCodesMap.get(0).get("CURRENCY_CODE").toString();
		}
		entitys=entitys.replace("[\"","").replace("\"]","").replace("\",\"",",");
		String[] entityArry=entitys.split(",");
		Map<String, Object> repData=null;
		String json = "{\"flag\":\"0\", \"msg\":\"数据上报成功!\"}";
		//循环公司进行数据上报
	  try{	
		for(int i=0;i<entityArry.length;i++){
			repData=new HashMap<String, Object>();
			String entity=entityArry[i];
			String entityId=entity.split(":")[0];
			String entityCode=entity.split(":")[1];
			repData.put("suitCode",suitCode);
			repData.put("modaltypeCode",modaltypeCode);
			repData.put("entityCode",entityCode);
			repData.put("periodCode",periodCode);
			repData.put("currencyCode",currencyCode);
			repData.put("userName",userName);
			//查询当前公司下所有模版的数据
			List<Map<String, Object>>modalsData=new ArrayList<Map<String,Object>>();
			List<String> mstIds=new ArrayList<String>();
			modalsheets=modalsheets.replace("[\"","").replace("\"]","").replace("\",\"",",");
			String[] modalArry=modalsheets.split(","); 
			for(int j=0;j<modalArry.length;j++){
				Map<String, Object>modalData=new HashMap<String, Object>();
				String modalsheet=modalArry[j];
				String modalsheetId=modalsheet.split(":")[0];
				String modalsheetCode=modalsheet.split(":")[1];
				String isFjM=modalsheet.split(":")[2];
				mstIds.add(modalsheetId);
				//获取模版主行数据
				Map<String, String> map1=new HashMap<String, String>();
				map1.put("suitId",suitId);
				map1.put("entityId",entityId);
				map1.put("periodId",periodId);
				map1.put("cnyId",cnyId);
				map1.put("modaltypeId",modaltypeId);
				map1.put("modalsheetId",modalsheetId);
				map1.put("dbType", dbType);
				List<Map<String, Object>>fixData=makeReportService.dataUpload(map1);
				//如果模版为浮动行模版，则获取模版浮动行数据
				List<Map<String, String>> floatData=new ArrayList<Map<String,String>>();
				if("Y".equals(isFjM)){
					floatData=fjservice.fjDataUpload(suitId, modalsheetId, modaltypeId, entityId, periodId, cnyId);
				}
				modalData.put("modalsheetCode",modalsheetCode);
				modalData.put("fixData",fixData);
				modalData.put("floatData",floatData);
				modalsData.add(modalData);
			}
			repData.put("modalsData", modalsData);

			//调用webservice接口,上报数据(以公司为最小单位进行)
			//flag为0，成功；1失败，从res中获取flag获取
			try{
				Object[] res=client.invoke("sendData",JsonUtil.toJson(repData),"admin","xzsoft");
				Map<String, Object>mesJson=JsonUtil.getJsonObj((String) res[0]);
				if("0".equals(mesJson.get("flag").toString())){
					Map<String, Object> map2=new HashMap<String, Object>();
					map2.put("suitId",suitId);
					map2.put("entityId",entityId);
					map2.put("periodId",periodId);
					map2.put("cnyId",cnyId);
					map2.put("modaltypeId",modaltypeId);
					map2.put("mstIds",mstIds);
					map2.put("dbType", dbType);
					map2.put("date",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
					map2.put("user", userId);
					makeReportMapper.upRepInfo(map2);                          //更新报表上报状态
					logmanage.recordLog(userId, sessionId, suitId, modaltypeId, mstIds, entityId, periodId, cnyId,
							"0",mesJson.get("msg").toString(), "T",serverId);           //插入日志
				}else{
					logmanage.recordLog(userId, sessionId, suitId, modaltypeId, mstIds, entityId, periodId, cnyId,
							"0",mesJson.get("msg").toString(), "F",serverId);           //插入日志
				}
			}catch(Exception e){
				logmanage.recordLog(userId, sessionId, suitId, modaltypeId, mstIds, entityId, periodId, cnyId,
						"0",e.getMessage(), "F",serverId);           //插入日志
				break;
			}
		}
		
	  }catch(Exception e){
		  log.error(e.getMessage());
		  json="{\"flag\":\"1\", \"msg\":\""+e.getMessage()+"\"}";
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
	 * 报表数据导出为json文件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=repDataExp" ,method = RequestMethod.POST)
	public void repDataExp(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String dbType=PlatformUtil.getDbType();
		HttpSession session = request.getSession();
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		String userName = sessionVars.get(SessionVariable.userName);
		String suitId=(String)session.getAttribute("suitId");
		String suitCode=(String)session.getAttribute("suitCode");
		String modalsheets=request.getParameter("modalsheets");
		String modaltypeId=request.getParameter("modaltypeId");
		String modaltypeCode=request.getParameter("modaltypeCode");
		String entitys=request.getParameter("entitys");
		String periodId=request.getParameter("periodId");
		String cnyId=request.getParameter("cnyId");
		
		HashMap params = new HashMap();
		 params.put("PERIOD_ID", periodId);
		 params.put("CURRENCY_ID", cnyId);
		//根据ID获取各维度的code值
		String periodCode=null;
		String currencyCode=null;
		List<HashMap> repCodesMap=makeReportMapper.getRepCodes(params);
		if (repCodesMap.size()>0) {
			periodCode = repCodesMap.get(0).get("PERIOD_CODE").toString();
			currencyCode=repCodesMap.get(0).get("CURRENCY_CODE").toString();
		}
		entitys=entitys.replace("[\"","").replace("\"]","").replace("\",\"",",");
		String[] entityArry=entitys.split(",");
		List<Map<String, Object>> repDatas=new ArrayList<Map<String,Object>>();
		Map<String, Object> repData=null;
	try{	
		for(int i=0;i<entityArry.length;i++){
			repData=new HashMap<String, Object>();
			String entity=entityArry[i];
			String entityId=entity.split(":")[0];
			String entityCode=entity.split(":")[1];
			repData.put("suitCode",suitCode);
			repData.put("modaltypeCode",modaltypeCode);
			repData.put("entityCode",entityCode);
			repData.put("periodCode",periodCode);
			repData.put("currencyCode",currencyCode);
			repData.put("userName",userName);
			//查询当前公司下所有模版的数据
			List<Map<String, Object>>modalsData=new ArrayList<Map<String,Object>>();
			List<String> mstIds=new ArrayList<String>();
			modalsheets=modalsheets.replace("[\"","").replace("\"]","").replace("\",\"",",");
			String[] modalArry=modalsheets.split(","); 
			for(int j=0;j<modalArry.length;j++){
				Map<String, Object>modalData=new HashMap<String, Object>();
				String modalsheet=modalArry[j];
				String modalsheetId=modalsheet.split(":")[0];
				String modalsheetCode=modalsheet.split(":")[1];
				String isFjM=modalsheet.split(":")[2];
				mstIds.add(modalsheetId);
				//获取模版主行数据
				Map<String, String> map1=new HashMap<String, String>();
				map1.put("suitId",suitId);
				map1.put("entityId",entityId);
				map1.put("periodId",periodId);
				map1.put("cnyId",cnyId);
				map1.put("modaltypeId",modaltypeId);
				map1.put("modalsheetId",modalsheetId);
				map1.put("dbType", dbType);
				List<Map<String, Object>>fixData=makeReportService.dataUpload(map1);
				//如果模版为浮动行模版，则获取模版浮动行数据
				List<Map<String, String>> floatData=new ArrayList<Map<String,String>>();
				if("Y".equals(isFjM)){
					floatData=fjservice.fjDataUpload(suitId, modalsheetId, modaltypeId, entityId, periodId, cnyId);
				}
				modalData.put("modalsheetCode",modalsheetCode);
				modalData.put("fixData",fixData);
				modalData.put("floatData",floatData);
				modalsData.add(modalData);
			}
			repData.put("modalsData", modalsData);
			repDatas.add(repData);
		}
		String jsonStr=JsonUtil.toJson(repDatas);
		byte[] jsonByte=jsonStr.getBytes("utf-8");
	    DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = "XSREXP".concat(format.format(new Date())).concat(".json");
        fileName = URLEncoder.encode(fileName, "utf-8");
        response.setContentType("application/x-msdownload;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ServletOutputStream out = response.getOutputStream();
        out.write(jsonByte);
        out.flush();
        out.close();
	 } catch (SQLException e) {
         log.error(e);
     } catch (UnsupportedEncodingException e) {
         log.error(e);
     } catch (IOException e) {
         log.error(e);
     } 
		
	
	}
	
	
	
}
