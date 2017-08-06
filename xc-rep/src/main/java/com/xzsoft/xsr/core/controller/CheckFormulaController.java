package com.xzsoft.xsr.core.controller;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.webbuilder.common.Main;
import com.webbuilder.utils.ZipUtil;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xsr.core.init.XSRFilepath;
import com.xzsoft.xsr.core.init.XSRInitParameters;
import com.xzsoft.xsr.core.service.ChkSchemeImpExpService;
import com.xzsoft.xsr.core.service.RepChkService;

@Controller
@RequestMapping("/chkController.do")
public class CheckFormulaController {
	private static Logger log = Logger.getLogger(CheckFormulaController.class.getName());
	@Autowired
	private ChkSchemeImpExpService chkSchemeImpExpService;
	@Autowired
	private RepChkService repChkService;

	/**
	 * 稽核方案导出
	 * 
	 * @param chk_scheme_ids
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=ChkSchemeExp", method = RequestMethod.POST)
	public void ChkSchemeExp(@RequestParam(required = true) String chk_scheme_id, HttpServletRequest request,
			HttpServletResponse response) {
		String json = "";

		// 建立临时文件目录
		String tempDir = "XSRFile/chkscheme";
		File file = new File(Main.path, tempDir);
		File zipfile = new File(Main.path, "XSRFile/ChkScheme.zip");
		if (!file.exists()) {
			file.mkdirs();
		}

		// 建立XML文件
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String filepath = file + File.separator + "ChkScheme".concat(format.format(new Date())).concat(".xml");
		// 将内容写入XML并压缩ZIP
		File fs[] = null;
		try {
			chkSchemeImpExpService.ChkSchemeExp(filepath, chk_scheme_id);
			fs = file.listFiles();
			ZipUtil.zip(fs, zipfile);
			json = "{flag:0, msg:'导出成功!','name':'ChkScheme','filepath':'"
					+ (Main.path + "/XSRFile/ChkScheme.zip").replace("\\", "/") + "'}";
			for (File f : fs)
				f.delete();
		} catch (IOException e) {
			json = "{flag:1, msg:'导出失败!'}";
			log.error(e);
		} catch (Exception e) {
			json = "{flag:1, msg:'导出失败!'}";
			log.error(e);
		}
		String rData = (JSONUtil.Encode(json)).replaceAll("\\n", "");
		rData = (JSONUtil.Encode(json)).replaceAll("\\r", "");
		response.setContentType("text/html;charset=utf-8");
		try {
			// 输出JSON串
			response.getWriter().write(rData);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 稽核方案导入
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=ChkSchemeImp", method = RequestMethod.POST)
	public void ChkSchemeImp(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String tempDir = "XSRFile/chkscheme";
		File folder = new File(Main.path, tempDir);
		folder.mkdirs();
		List<FileItem> fileList;
		String suitId = (String) session.getAttribute("suitId");
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String json = "";
		try {
			fileList = upload.parseRequest(request);
			Iterator<FileItem> it = fileList.iterator();
			while (it.hasNext()) {
				FileItem item = it.next();
				if (!item.isFormField()) {
					// 判断该表单项是否是普通类型
					String attName = item.getName();
					String att_file_type = "";
					if (attName == null || attName.trim().equals("")) {
						continue;
					}
					// 扩展名格式：
					if (attName.lastIndexOf(".") >= 0) {
						att_file_type = attName.substring(attName.lastIndexOf("."));
					}
					if (".zip".equals(att_file_type.toLowerCase())) {
						ZipUtil.unzip(item.getInputStream(), folder);
					}
				}
			}
		} catch (FileUploadException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		}
		File fs[] = folder.listFiles();
		for (File f : fs) {
			try {
				json = chkSchemeImpExpService.ChkSchemeImp(f, suitId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			f.delete();
		}
		String rData = (JSONUtil.Encode(json)).replaceAll("\\n", "");
		rData = (JSONUtil.Encode(json)).replaceAll("\\r", "");
		response.setContentType("text/html;charset=utf-8");
		try {
			// 输出JSON串
			response.getWriter().write(rData);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}
	/**
	 * 报表稽核-常规稽核
	 */
	@RequestMapping(params = "method=verifyRepDataUseDB", method = RequestMethod.POST)
	public void verifyRepDataUseDB(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String rspJson = "";
		//参数
		String suitId = (String) session.getAttribute("suitId");
		String periodId = (String) request.getParameter("periodId"); // 期间
		String cnyId = (String) request.getParameter("cnyId"); // 币种
		String modaltypeId = request.getParameter("modaltypeId"); // 模板集
		String verifyCaseID = request.getParameter("verifyCaseID"); // 稽核方案
		if("".equals(verifyCaseID)) {
			verifyCaseID = "ALL";
		}
		String isCheckFjRow = request.getParameter("isCheckFjRow"); // 是否稽核浮动行
		String entityWithSheetsArrays = request.getParameter("entityWithSheetsArray");
        String fjModalsheetList = request.getParameter("fjModalsheetList");
        String chkFreeMarkerFilePath = session.getServletContext().getRealPath(XSRFilepath.chkFreemarkerPath);
		String chkResultFileDir = session.getServletContext().getRealPath(XSRFilepath.checkResultPath);
		File file = new File(chkResultFileDir);
		//生成稽核结果的存放路径
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			String[] params = new String[9];
			params[0] = suitId;
			params[1] = periodId;
			params[2] = cnyId;
			params[3] = modaltypeId; 
			params[4] = verifyCaseID;
			params[5] = isCheckFjRow;
			params[6] = entityWithSheetsArrays;
			params[7] = fjModalsheetList;
			params[8] = chkFreeMarkerFilePath;
			String[] returnArray = repChkService.verifyRepDataUseDB(params);
			rspJson = returnArray[0];
			String path = returnArray[1];
			session.removeAttribute("CheckResultFilePath");
			if(null != path && !"".equals(path)) {
				session.setAttribute("CheckResultFilePath", path);
			}
		} catch (Exception e) {
			rspJson = "{flag:1, msg:'报表稽核出现程序异常:" + e.getMessage() + "'}";
			log.error("报表稽核出现程序异常", e);
		}
		rspJson = rspJson.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(rspJson);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}
	
	/**
	 * 报表稽核--使用redis稽核
	 */
	@RequestMapping(params = "method=verifyRepDataUseRedis", method = RequestMethod.POST)
	public void verifyRepDataUseRedis(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String rspJson = "";
		String suitId = (String) session.getAttribute("suitId");
		if(XSRInitParameters.parameterMap.containsKey(suitId)) {
			Map<String, Map<String, String>> paramTypeMap = XSRInitParameters.parameterMap.get(suitId);
			if(paramTypeMap.containsKey("REDIS_CHECK")) {
				Map<String, String> paramMap = paramTypeMap.get("REDIS_CHECK");
				if(paramMap.containsKey("ENABLE_FLAG") && "Y".equals(paramMap.get("ENABLE_FLAG"))) {
					String listCount = (null == paramMap.get("p_formula_count"))? "2000": paramMap.get("p_formula_count");
					//参数
					String periodId = (String) request.getParameter("periodId"); // 期间
					String cnyId = (String) request.getParameter("cnyId"); // 币种
					String modaltypeId = request.getParameter("modaltypeId"); // 模板集
					String verifyCaseID = request.getParameter("verifyCaseID"); // 稽核方案
					if("".equals(verifyCaseID)) {
						verifyCaseID = "ALL";
					}
					String isCheckFjRow = request.getParameter("isCheckFjRow"); // 是否稽核浮动行
					String entityWithSheetsArrays = request.getParameter("entityWithSheetsArray");
			        String fjModalsheetList = request.getParameter("fjModalsheetList");
			        String chkFreeMarkerFilePath = session.getServletContext().getRealPath(XSRFilepath.chkFreemarkerPath);
					String chkResultFileDir = session.getServletContext().getRealPath(XSRFilepath.checkResultPath);
					File file = new File(chkResultFileDir);
					//生成稽核结果的存放路径
					if (!file.exists()) {
						file.mkdirs();
					}
					try {
						String[] params = new String[10];
						params[0] = suitId;
						params[1] = periodId;
						params[2] = cnyId;
						params[3] = modaltypeId; 
						params[4] = verifyCaseID;
						params[5] = isCheckFjRow;
						params[6] = entityWithSheetsArrays;
						params[7] = fjModalsheetList;
						params[8] = chkFreeMarkerFilePath;
						params[9] = listCount;
						String[] returnArray = repChkService.verifyRepDataUseRedis(params);
						rspJson = returnArray[0];
						String path = returnArray[1];
						session.removeAttribute("CheckResultFilePath");
						if(null != path && !"".equals(path)) {
							session.setAttribute("CheckResultFilePath", path);
						}
					} catch (Exception e) {
						rspJson = "{flag:1, msg:'报表稽核出现程序异常:" + e.getMessage() + "'}";
						log.error("报表稽核出现程序异常", e);
					}
				} else {
					rspJson = "{flag:1, msg:'系统未启用redis稽核!'}";
				}
			} else {
				rspJson = "{flag:1, msg:'系统未启用redis稽核!'}";
			}
		} else {
			rspJson = "{flag:1, msg:'系统未启用redis稽核!'}";
		}
		rspJson = rspJson.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(rspJson);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}
	
}
