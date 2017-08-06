package com.xzsoft.xc.rep.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.webbuilder.utils.WebUtil;
import com.xzsoft.xc.rep.service.XcRepSheetExportService;
import com.xzsoft.xc.rep.util.FileUtil;
import com.xzsoft.xc.rep.util.XcRepFilepath;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xip.platform.util.PlatformUtil;
@Controller
@RequestMapping("/xcRepSheetExportAction.do")
public class XcRepSheetExportAction {
	private static Logger log = Logger.getLogger(XcRepSheetExportAction.class.getName());
	@Resource
	XcRepSheetExportService xcRepSheetExportService;
	/**
	 * 
	 * @methodName  exportSheetExcel
	 * @author      tangxl
	 * @date        2016年9月21日
	 * @describe    
	 *<document>
	 *<title>报表导出</title>
	 *<p>按账簿分类：Excel文件名为【账簿名称】，页签名为模板名称(资产负债表、利润表、所有者权益表)</p>
	 *<p>按模板分类：Excel文件名为【模板名称】，页签名为各个账簿名称</p>  
	 *</document>
	 * @param       sheetIdList         要导出报表的id串
	 * @param       exportType          导出类型：按账簿分类-LEDGER;按模板分类-SHEET
	 * @param       request
	 * @param       response
	 * @param       session
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params = "method=exportSheetExcel", method = RequestMethod.POST)
	public void exportSheetExcel(
			@RequestParam(required = true) String sheetIds,
			@RequestParam(required = true) String exportType,
			HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		//创建压缩文件的目录
		String randomFolder = UUID.randomUUID().toString();
		String fileDir = session.getServletContext().getRealPath(XcRepFilepath.REPORT_PATH).concat(File.separator).concat(randomFolder);
		File file = new File(fileDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		JSONObject json = null;;
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		try {
			paramsMap.put("sheetIds", sheetIds);
			paramsMap.put("exportType", exportType);
			paramsMap.put("filePath", fileDir);
			paramsMap.put("locale", request.getLocale());
			json = (JSONObject) xcRepSheetExportService.exportSheetExcelZip(paramsMap);
			json.put("delFolder", fileDir);
		} catch (Exception e) {
			json.put("flag", "1");
			if(XConstants.ZH.equals(language)){
				log.error("报表导出excel文件出错", e);
				json.put("msg","导出报表excel文件失败："+e.getMessage());
			}else{
				log.error("Fail to export report into office excel document", e);
				json.put("msg","Fail to export report into office excel document,cause:"+e.getMessage());
			}

		}finally{
			PlatformUtil.outPrint(response, json.toString());
		}
	}
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年9月22日
	 * @describe    TODO
	 * @param 		fileName
	 * @param 		filePath
	 * @param		request
	 * @param 		response
	 * @param 		session
	 * @变动说明       
	 * @version     1.0
	 */
	@RequestMapping(params = "method=exportSheetZip", method = RequestMethod.POST)
	public void exportSheetZip(@RequestParam(required = true) String fileName, @RequestParam(required = true) String filePath,String delFolderPath,
			HttpServletRequest request, HttpServletResponse response) {
		InputStream is = null;
		ServletOutputStream os = null;
		String language = XConstants.ZH;
		try {
			language = XCUtil.getLang();
		} catch (Exception x) {
			language = XConstants.ZH;
		}
		try {
			delFolderPath = java.net.URLDecoder.decode(delFolderPath,"UTF-8");
			filePath = java.net.URLDecoder.decode(filePath,"UTF-8");
			fileName = java.net.URLDecoder.decode(fileName,"UTF-8");
			File file = new File(filePath);
			is = new FileInputStream(file);
			response.setHeader("content-type", "application/force-download");
			response.setHeader("content-disposition", "attachment;" + WebUtil.encodeFilename(request, fileName+".zip"));
			os = response.getOutputStream();
			byte[] data = new byte[10 * 1024];
			int len;
			while ((len = is.read(data)) != -1) {
				os.write(data, 0, len);
			}
		} catch (Exception ex) {
			if(XConstants.ZH.equals(language)){
				log.error("模板导出-下载ZIP文件出错!", ex);
			}else{
				log.error("Error occur when download zip file,cause:", ex);
			}
		} finally {
			// 关闭输入流和输出流
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					if(XConstants.ZH.equals(language)){
						log.error("关闭输入流和输出流出错!", e);
					}else{
						log.error("Error occur when close io stream,cause:", e);
					}
				}
			}
			// 删除文件夹
			if (delFolderPath != null) {
				File file = new File(delFolderPath);
				FileUtil.deleteFolder(file);
			}
		}
	}
}
