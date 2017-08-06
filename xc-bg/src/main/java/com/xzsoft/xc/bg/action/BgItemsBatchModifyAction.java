package com.xzsoft.xc.bg.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.bg.modal.BgItemDTO;
import com.xzsoft.xc.bg.service.BgItemsBatchService;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.bg.util.XCBGItemsBatchModifyUtil;
import com.xzsoft.xip.platform.util.PlatformUtil;


/**
 * @ClassName: BgItemsBatchModifyAction 
 * @Description: 预算项目/费用项目批量修改
 * @author tangxl
 * @date 2016年7月25日 上午15:11:15 
 *
 */
@Controller
@RequestMapping("/bgItemsBatchModifyAction.do")
public class BgItemsBatchModifyAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(BgItemsBatchModifyAction.class.getName()) ;
	@Resource
	BgItemsBatchService bgItemsBatchService;
	/**
	 * 
	 * @methodName  exportItemsForExcel
	 * @author      tangxl
	 * @date        2016年7月25日
	 * @describe    预算/费用项目导出
	 * @param       request
	 * @param       response
	 * @变动说明       
	 * @version     1.0
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params="method=exportItemsForExcel")
	public void exportItemsForExcel(HttpServletRequest request, HttpServletResponse response){
		//模板配置信息编码
		String hasDataExport = request.getParameter("hasDataExport");
		String exportModel = request.getParameter("exportModel");
		String exportName = "预算项目";
		String[] titleArray = null;
		//费用项目设置
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(exportModel)){
			titleArray = new String[]{"费用项目名称","会计科目","预算项目","是否启用<Y-启用,N-禁用>"};
			exportName = "费用项目";
		}else{
			titleArray = new String[]{"预算项目名称","控制维度<1-预算项目，2-预算项目+成本中心>","控制方式<1-不控制，2-预警控制，3-绝对控制>","是否启用<Y-启用，N-禁用>"};
		}
		HSSFWorkbook workbook = null;
		ServletOutputStream outputStream = null;
		try {
			if("Y".equalsIgnoreCase(hasDataExport)){
				//自定义勾选数据导出
				String jsonData = request.getParameter("jsonData");
				JSONArray jsonArray = JSONArray.fromObject(jsonData);
				List<BgItemDTO> dataList = JSONArray.toList(jsonArray, new BgItemDTO(), new JsonConfig());
				workbook = XCBGItemsBatchModifyUtil.getItemsExcel(titleArray, dataList, exportName,exportModel);
			}else if("N".equalsIgnoreCase(hasDataExport)){
				//Excel模板，无数据导出
				workbook = XCBGItemsBatchModifyUtil.getItemsExcel(titleArray, null, exportName,exportModel);
			}else{
				//按条件导出
				String ledgerId = request.getParameter("ledgerId");
				String bgItemName = request.getParameter("bgItemName");
				List<BgItemDTO> dataList = bgItemsBatchService.getBgItemsByLedger(ledgerId, bgItemName,exportModel);
				workbook = XCBGItemsBatchModifyUtil.getItemsExcel(titleArray, dataList, exportName,exportModel);
			}
			response.setContentType("application/vnd.ms-excel");
			exportName = java.net.URLDecoder.decode(exportName, "utf-8");
			String fileHeader = "";
			String userAgent = request.getHeader("User-Agent").toUpperCase();
		    if (userAgent.indexOf("MSIE") > 0 || userAgent.indexOf("EDG") > 0 || (userAgent.indexOf("GECKO")>0 && userAgent.indexOf("RV:11")>0)) {		 	
		    	fileHeader = "attachment;filename=" + URLEncoder.encode(exportName + ".xls", "UTF-8");  		   
		    } else { 
		     	fileHeader = "attachment;filename=" + new String(exportName.getBytes("utf-8"),"iso-8859-1") + ".xls";
		    }  
			response.setHeader("Content-disposition", fileHeader);
			outputStream = response.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(workbook !=null && outputStream !=null){
				try {
					workbook.write(outputStream);
					workbook.close();
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error("导出【"+exportName+"】板异常，原因："+e.getMessage());
				}

			}
		
		}
	}
	/**
	 * @title:         importAccountData
	 * @description:   导入修改预算/费用项目数据
	 * @author         tangxl
	 * @date           2016年5月6日
	 * @param          request
	 * @param          response
	 */
	@RequestMapping(params="method=importModifiedBgItems")
	public void importModifiedBgItems(HttpServletRequest request, HttpServletResponse response){
		String resultJson = "{\"success\":true,\"flag\":\"1\",\"msg\":\"导入成功！\"}";
		String importType = request.getParameter("importType");
		String ledgerId = request.getParameter("ledgerId");
		//1、解析Excel
		try {
			String sessionId = request.getSession().getId();
			HSSFWorkbook uploadBook = XCBGItemsBatchModifyUtil.parseIoStreamToExcel(request);
			List<HashMap<String, String>> feeItemList = XCBGItemsBatchModifyUtil.parseExcelToItemList(uploadBook,importType,sessionId,ledgerId);
			bgItemsBatchService.deleteTmpImport(ledgerId,sessionId,importType);
			resultJson = bgItemsBatchService.updateImportItems(ledgerId, feeItemList,importType);
		} catch (Exception e) {
			resultJson = "{\"success\":false,\"flag\":\"0\",\"msg\":\""+e.getMessage()+"\"}";
			log.error("BgItemsBatchModifyAction-->importModifedBgItems-->批量修改费用/预算项目数据异常，原因："+e.getMessage());
			e.printStackTrace();
		}finally{
			PlatformUtil.outPrint(response, resultJson);
		}
	}
}