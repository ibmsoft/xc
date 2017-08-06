package com.xzsoft.xsr.core.controller;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.plugin.DocumentConvertPlugins;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.xzsoft.xsr.core.service.ImpAndExpExcelService;
import com.google.common.collect.Maps;
import com.wb.util.ZipUtil;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xsr.core.init.XSRFilepath;
import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.Rowitem;
import com.xzsoft.xsr.core.service.ImpAndExpModalService;
import com.xzsoft.xsr.core.service.ModalsheetService;
import com.xzsoft.xsr.core.util.JsonUtil;
import com.xzsoft.xsr.core.util.FileUtil;

@Controller
@RequestMapping("/modalsheetController.do")
public class ModalsheetController {
	private static Logger log = Logger.getLogger(ModalsheetController.class.getName());
	@Autowired
	private ModalsheetService modalsheetService;
	@Autowired
	private ImpAndExpModalService impAndExpModalService;
	@Autowired
	private ImpAndExpExcelService impAndExpExcelService;

	/**
	 * 判断模板格式是否存在
	 */
	@RequestMapping(params = "method=isModaLFormatExist", method = RequestMethod.POST)
	public void isModaLFormatExist(HttpServletRequest request, HttpServletResponse response) {

		String json = "{flag:0, msg:'false'}";
		try {
			String msFormatId = request.getParameter("msFormatId") ;
			int formatCount = modalsheetService.isModaLFormatExist(msFormatId);
			if (formatCount > 0) {
				json = "{flag:0, msg:'true'}";
			}
		} catch (Exception e) {
			json = "{flag:1, msg:\"查询模板格式是否存在出错:" + e.getMessage() + "\"}";
			log.error("查询模板格式是否存在出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 保存模板格式
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=saveModalsheetFormat", method = RequestMethod.POST)
	public void saveModalsheetFormat(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String json = "{flag:0, msg:'保存模板格式成功!'}";
		try {
			HashMap<String, String> sessionVars = (HashMap<String, String>) session.getAttribute(SessionVariable.XZ_SESSION_VARS);
			String userId = sessionVars.get(SessionVariable.userId);
			String suitId = (String) session.getAttribute("suitId");
			
//			@RequestParam(required = true) String modalsheetId,
//			@RequestParam(required = true) String titleMaxRow,
//			@RequestParam(required = true) String msFormatId,
//			@RequestParam(required = true) String sheetJson,
//			@RequestParam(required = true) String colnoResult,
//			@RequestParam(required = true) String modaltypeId,
//			@RequestParam(required = true) String rowitemWithX,
			
			String sheetJson = request.getParameter("sheetJson") ;
			String modalsheetId = request.getParameter("modalsheetId") ;
			String msFormatId = request.getParameter("msFormatId") ;
			String titleMaxRow = request.getParameter("titleMaxRow") ;
			String colnoResult = request.getParameter("colnoResult") ;
			String modaltypeId = request.getParameter("modaltypeId") ;
			String rowitemWithX = request.getParameter("rowitemWithX") ;
			
			
			modalsheetService.saveModalsheetFormat(modalsheetId, titleMaxRow,
					msFormatId, sheetJson, userId, colnoResult, suitId, modaltypeId, rowitemWithX);
		} catch (Exception e) {
			json = "{flag:1, msg:\"保存模板格式出错:" + e.getMessage() + "\"}";
			log.error("保存模板格式出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
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
	 * @Title: loadModalsheetFormat 
	 * @Description: 加载模板格式
	 * @param msFormatId
	 * @param modalsheetId
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params = "method=loadModalsheetFormat", method = RequestMethod.POST)
	public void loadModalsheetFormat(
			@RequestParam(required = true) String msFormatId,
			@RequestParam(required = true) String modalsheetId,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> results = Maps.newHashMap();
		try {
			results = modalsheetService.loadModalsheetFormat(msFormatId, modalsheetId);
		} catch (Exception e) {
			log.error("加载模板格式出错", e);
		}
		
		response.setContentType("text/html;charset=UTF-8");
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
	 * @Title: deleteModalsheet 
	 * @Description: 删除模板
	 * @param modalsheetId
	 * @param msFormatId
	 * @param request
	 * @param response    设定文件
	 */
	@RequestMapping(params = "method=deleteModalsheet", method = RequestMethod.POST)
	public void deleteModalsheet(
			@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String msFormatId,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String json = "{flag:0, msg:'删除模板成功!'}";
		try {
			String suitId = (String) session.getAttribute("suitId");
			String returnStr = modalsheetService.deleteModalsheet(modalsheetId, msFormatId, suitId);
			if(null != returnStr) {
				json = "{flag:2, msg:'" + returnStr + "'}";
			}
		} catch (Exception e) {
			json = "{flag:1, msg:\"删除模板出错:" + e.getMessage() + "\"}";
			log.error("删除模板出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
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
	 * 通过行指标编码获取行指标
	 */
	@RequestMapping(params = "method=getOneRowitem", method = RequestMethod.POST)
	public void getOneRowitem(
			@RequestParam(required = true) String rowitemcode,
			@RequestParam(required = true) String rowno,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String suitId = (String) session.getAttribute("suitId");
		Rowitem rowitem = null;
		String json = null;
		try {
			rowitem = modalsheetService.getOneRowitem(suitId, rowitemcode);
			if (null != rowitem) {
				json = "{flag:'0',msg:'success',itemname:'"
						+ rowitem.getROWITEM_NAME() + "',isfjitem:'"
						+ rowitem.getISFJITEM() + "',rowno:'" + rowno
						+ "',direction:'" + rowitem.getDIRECTION()
						+ "',itemid:'" + rowitem.getROWITEM_ID() + "',upcode:'"
						+ rowitem.getUPCODE() + "'}";
			} else {
				json = "{flag:'1',msg:'notdata'}";
			}
		} catch (Exception e) {
			json = "{flag:1, msg:\"查询行指标出错:" + e.getMessage() + "\"}";
			log.error("查询行指标出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 保存行指标
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=updateRowitem", method = RequestMethod.POST)
	public void updateRowitem(@RequestParam(required = true) String json,
			@RequestParam(required = true) String modaltypeId,
			@RequestParam(required = true) String modaltypeCode,
			@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String modalsheetCode,
			@RequestParam(required = true) String modalsheetName,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String suitId = (String) session.getAttribute("suitId");
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		String rspJson = "";
		String data = "";
		String[] params = new String[7];
		params[0] = suitId;
		params[1] = userId;
		params[2] = modaltypeId;
		params[3] = modaltypeCode;
		params[4] = modalsheetId;
		params[5] = modalsheetCode;
		params[6] = modalsheetName;
		try {
			List<Rowitem> list = getObjectListFromJsonString(json,
					Rowitem.class);

			Set<Integer> set = new HashSet<Integer>();
			for (int i = 0; i < list.size(); i++) {
				Rowitem rowitem = list.get(i);
				set.add(rowitem.getROWNO());
			}
			if (list.size() != set.size()) {
				rspJson = "{flag:'1',msg:'行次不能重复'}";
				return;
			}
			if (list.size() > 0) {
				data = modalsheetService.updateRowitem(params, list);
				rspJson = "{flag:0, msg:'保存行指标成功', data:" + data + "}";
			} else {
				rspJson = "{flag:1, msg:'保存行指标出错,无数据'}";
			}
		} catch (Exception e) {
			rspJson = "{flag:1, msg:\"保存行指标出错:" + e.getMessage() + "\"}";
			log.error("保存行指标出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(rspJson);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 通过列指标编码获取列指标
	 */
	@RequestMapping(params = "method=getOneColitem", method = RequestMethod.POST)
	public void getOneColitem(
			@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String colitemcode,
			@RequestParam(required = true) String rowno,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String suitId = (String) session.getAttribute("suitId");
		Colitem colitem = null;
		String json = null;
		try {
			colitem = modalsheetService.getOneColitem(suitId, modalsheetId,
					colitemcode);
			if (null != colitem) {
				json = "{flag:'0',msg:'success',itemname:'"
						+ colitem.getCOLITEM_NAME() + "',rowno:'" + rowno
						+ "',datatype:'" + colitem.getDATATYPE()
						+ "',colitem_id:'" + colitem.getCOLITEM_ID()
						+ "',upcode:'" + colitem.getUPCODE() + "'}";
			} else {
				json = "{flag:'1',msg:'notdata'}";
			}

		} catch (Exception e) {
			json = "{flag:1, msg:\"查询列指标出错:" + e.getMessage() + "\"}";
			log.error("查询列指标出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 保存列指标
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=updateColitem", method = RequestMethod.POST)
	public void updateColitem(@RequestParam(required = true) String json,
			@RequestParam(required = true) String modaltypeId,
			@RequestParam(required = true) String modaltypeCode,
			@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String modalsheetCode,
			@RequestParam(required = true) String modalsheetName,
			@RequestParam(required = true) String isFjModal,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String suitId = (String) session.getAttribute("suitId");
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		String rspJson = "";
		String data = "";
		String[] params = new String[8];
		params[0] = suitId;
		params[1] = userId;
		params[2] = modaltypeId;
		params[3] = modaltypeCode;
		params[4] = modalsheetId;
		params[5] = modalsheetCode;
		params[6] = modalsheetName;
		params[7] = isFjModal;
		try {
			List<Colitem> list = getObjectListFromJsonString(json,
					Colitem.class);

			if (list.size() > 0) {
				data = modalsheetService.updateColitem(params, list);
				rspJson = "{flag:0, msg:'保存列指标成功', data:" + data + "}";
			} else {
				rspJson = "{flag:1, msg:'保存列指标出错,无数据'}";
			}
		} catch (Exception e) {
			rspJson = "{flag:1, msg:\"保存列指标出错:" + e.getMessage() + "\"}";
			log.error("保存列指标出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(rspJson);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 获取其他栏次的行指标
	 */
	@RequestMapping(params = "method=getOtherRowitemList", method = RequestMethod.POST)
	public void getOtherRowitemList(
			@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String lanNo,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String data = "";
		String rspJson = "";
		try {
			data = modalsheetService.getOtherRowitemList(modalsheetId, lanNo);
			rspJson = "{flag:0, msg:'查询其他行指标成功', data:" + data + "}";
		} catch (Exception e) {
			rspJson = "{flag:1, msg:\"查询其他行指标出错:" + e.getMessage() + "\"}";
			log.error("查询其他行指标出错", e);
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
	 * 复制模板格式
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=copyModalsheet", method = RequestMethod.POST)
	public void copyModalsheet(
			@RequestParam(required = true) String srcMsAndFIdList,
			@RequestParam(required = true) String srcModaltypeId,
			@RequestParam(required = true) String modaltypeId,
			@RequestParam(required = true) String modaltypeCode,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String suitId = (String) session.getAttribute("suitId");
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		String[] params = new String[7];
		params[0] = suitId;
		params[1] = userId;
		params[2] = srcModaltypeId;
		params[3] = modaltypeId;
		params[4] = modaltypeCode;
		String json = "{flag:0, msg:'复制模板格式成功!'}";
		String isModalExist = "";
		try {
			String[] modalsheetList = srcMsAndFIdList.split(":");
			for(String modalsheetId : modalsheetList) {
				String[] modalsheetArray = modalsheetId.split("&");
				params[5] = modalsheetArray[0];
				params[6] = modalsheetArray[1];
				String returnStr = modalsheetService.copyModalsheet(params);
				if(null != returnStr) {
					if("".equals(isModalExist)) {
						isModalExist = returnStr;
					} else {
						isModalExist = isModalExist + "、" + returnStr;
					}
				}
			}
			if(!"".equals(isModalExist)) {
				json = "{flag:2, msg:'" + isModalExist + " 模板已存在!'}";
			}
		} catch (Exception e) {
			json = "{flag:1, msg:\"复制模板格式出错," + e.getMessage() + "\"}";
			log.error("复制模板格式出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
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
	 * 导出模板
	 */
	@RequestMapping(params = "method=exportModal", method = RequestMethod.POST)
	public void exportModal(@RequestParam(required = true) String modaltypeId,
			@RequestParam(required = true) String modalsheetCodeNameList,
			@RequestParam(required = true) String modalsheetIdList,
			@RequestParam(required = true) String isExpCalFormula,
			@RequestParam(required = true) String isOnlyExpRepFormula,
			@RequestParam(required = true) String isExpChkFormula,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String suitId = (String) session.getAttribute("suitId");
		// 创建导出文件的存放目录
		String fileDir = session.getServletContext().getRealPath(XSRFilepath.modalImpExpPath);
		File file = new File(fileDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 使用uuid生成压缩文件名称
		String zipfileName = UUID.randomUUID().toString();
		// 生成压缩文件路径全名
		String zipfilePathAndName = fileDir + File.separator + zipfileName
				+ ".zip";
		String json = "";
		List<String> fileList = null;
		try {
			// get方式得到的中文参数是乱码，需要转码
			// modalsheetCodeNameList = new
			// String(modalsheetCodeNameList.getBytes("iso8859-1"),"utf-8");
			String[] params = new String[8];
			params[0] = suitId;
			params[1] = modaltypeId;
			params[2] = modalsheetCodeNameList;
			params[3] = modalsheetIdList;
			params[4] = fileDir;
			params[5] = isExpCalFormula;
			params[6] = isOnlyExpRepFormula;
			params[7] = isExpChkFormula;
			// 调用service方法, 得到生成的文件名称列表
			fileList = impAndExpModalService.exportModal(params);
			// 将生成的文件压缩
			FileUtil.zipFile(zipfilePathAndName, fileList);
			json = "{flag:0, msg:'导出模板成功', fileName:'" + zipfileName
					+ "', filePathAndName:'" + zipfilePathAndName + "'}";
		} catch (Exception e) {
			json = "{flag:1, msg:\"导出模板出错:" + e.getMessage() + "\"}";
			log.error("导出模板出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		} finally {
			// 删除生成的xml文件
			for (String delFile : fileList) {
				try {
					FileUtil.delFile(delFile);
				} catch (Exception e) {
					log.error("导出模板-删除文件出错：", e);
				}
			}
		}
	}

	/**
	 * 模板接收-解压zip文件
	 * 
	 * @param request
	 * @param response
	 * @param session
	 */
	@RequestMapping(params = "method=importModal", method = RequestMethod.POST)
	public void importModal(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		String json = "{flag:0, msg:'上传文件解压成功'}";
		// 创建文件解压目录
		String zipFileDir = session.getServletContext().getRealPath(XSRFilepath.modalImpExpPath)
				+ File.separator + session.getId();
		// 文件解压目录
		File zipFile = new File(zipFileDir);
		if (!zipFile.exists()) {
			zipFile.mkdirs();
		}
		List<FileItem> fileList = null;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
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
						att_file_type = attName.substring(attName
								.lastIndexOf("."));
					}
					if (".zip".equals(att_file_type.toLowerCase())) {
						ZipUtil.unzip(item.getInputStream(), zipFile);
					}
				}
			}
		} catch (Exception e) {
			json = "{flag:1, msg:\"上传文件解压出错: " + e.getMessage() + "\"}";
			log.error("模板接收-上传模板解压出错：", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		// 输出JSON
		response.setContentType("textml;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}

	/**
	 * 模板接收-解析modaltype.xml文件，获取到接收文件中的所有模板信息-编辑为json串
	 * 展示在页面gridpanel中
	 */
	@RequestMapping(params = "method=getImpModalsheetList", method = RequestMethod.POST)
	public void getImpModalsheetList(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		// modaltype.xml文件的存放目录
		String zipFileDir = session.getServletContext().getRealPath(XSRFilepath.modalImpExpPath)
				+ File.separator + session.getId();

		String filePath = zipFileDir + File.separator + "modaltype.xml";
		String rspJson = "";
		try {
			// 文件解压目录
			File zipFile = new File(zipFileDir);
			if (!zipFile.exists()) {
				throw new RuntimeException("文件解压目录不存在！");
			}
			rspJson = impAndExpModalService.getImpModalsheetList(filePath);
		} catch (Exception e) {
			log.error("获取到接收文件中的所有模板信息出错", e);
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
	 * 接收模板
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "method=receiveModal", method = RequestMethod.POST)
	public void receiveModal(
			@RequestParam(required = true) String modalsheetCodeList,
			@RequestParam(required = true) String isImpCalFormula,
			@RequestParam(required = true) String isOnlyImpRepFormula,
			@RequestParam(required = true) String isImpChkFormula,
			@RequestParam(required = true) String isDeleteFormual,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String suitId = (String) session.getAttribute("suitId");
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		String rspJson = "";
		// 模板文件存放的目录
		String fileDir = session.getServletContext().getRealPath(XSRFilepath.modalImpExpPath)
				+ File.separator + session.getId();
		String[] params = new String[8];
		params[0] = suitId;
		params[1] = userId;
		params[2] = fileDir;
		params[3] = modalsheetCodeList;
		params[4] = isImpCalFormula;
		params[5] = isOnlyImpRepFormula;
		params[6] = isImpChkFormula;
		params[7] = isDeleteFormual;
		try {
			// 判断目录是否存在
			File zipFile = new File(fileDir);
			if (!zipFile.exists()) {
				throw new RuntimeException("文件解压目录不存在！");
			}
			impAndExpModalService.receiveModal(params);
			rspJson = "{flag:0, msg:'模板接收成功'}";
		} catch (Exception e) {
			rspJson = "{flag:1, msg:\"模板接收出错:" + e.getMessage() + "\"}";
			log.error("模板接收出错", e);
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
	 * 通过行列指标编码，模板id获取行列指标id
	 */
	@RequestMapping(params = "method=getRowAndColitemId", method = RequestMethod.POST)
	public void getRowAndColitemId(
			@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String rowitemCode,
			@RequestParam(required = true) String colitemCode,
			@RequestParam(required = true) String row,
			@RequestParam(required = true) String col,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		String suitId = (String) session.getAttribute("suitId");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("suitId", suitId);
		params.put("modalsheetId", modalsheetId);
		params.put("rowitemCode", rowitemCode);
		params.put("colitemCode", colitemCode);
		String rcId = "";
		String rspJson = "";
		try {
			rcId = modalsheetService.getRowAndColitemId(params);
			rspJson = "{flag:0, msg:'查询行列指标ID成功', row:'" + row + "', col:'"
					+ col + "', data:'" + rcId + "'}";
		} catch (Exception e) {
			rspJson = "{flag:1, msg:\"查询行列指标ID出错," + e.getMessage() + "\"}";
			log.error("查询行列指标ID出错", e);
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
	 * getObjectListFromJsonString:(将Json字符串转换成List集合)
	 *
	 * @param jsonString
	 *            Json字符串
	 * @param beanClass
	 *            bean类型
	 * @return 集合
	 * @author XZTeam
	 * @version Ver 1.0
	 * @since Ver 1.0
	 */
	public static List getObjectListFromJsonString(String jsonString, Class beanClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;
		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, beanClass);
			list.add(pojoValue);
		}
		return list;
	}

	/**
	 * 保存指标精度或汇总属性 2016-01-06
	 * 
	 * @author liuwl
	 */
	@RequestMapping(params = "method=saveItemProperty", method = RequestMethod.POST)
	public void saveItemProperty(
			@RequestParam(required = true) String msFormatId,
			@RequestParam(required = true) String reportData,
			@RequestParam(required = true) String modaltypeId,
			@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String pro_type,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		System.out.println(reportData);
		String json = "";
		if (pro_type.equals("PRECISION")) {
			json = "{flag:0, msg:'保存指标精度成功!'}";
		} else {
			json = "{flag:0, msg:'保存汇总属性成功!'}";
		}
		try {
			modalsheetService.saveItemProperty(msFormatId, reportData,
					modaltypeId, modalsheetId, pro_type, suitId);
		} catch (Exception e) {

			json = "{flag:1, msg:\"保存指标精度或汇总属性," + e.getMessage() + "\"}";
			log.error("保存指标精度或汇总属性出错", e);
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
	 * 加载指标精度或汇总属性 2016-01-06
	 * 
	 * @author liuwl
	 */
	@RequestMapping(params = "method=loadItemProperty", method = RequestMethod.POST)
	public void loadItemProperty(
			@RequestParam(required = true) String msFormatId,
			@RequestParam(required = true) String pro_type,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId = (String) session.getAttribute("suitId");
		Map<String, Object> results = Maps.newHashMap();
		try {
			List<CellData> cellList = modalsheetService.loadItemProperty(
					msFormatId, pro_type, suitId);
			results.put("cells", cellList);

		} catch (Exception e) {
			log.error("加载指标精度或汇总属性出错", e);
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
			log.error("传输json串出错", e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 *模板格式设置-模板上传
	 */
	@RequestMapping(params = "method=uploadFile", method = RequestMethod.POST)
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Timestamp time = new Timestamp(new Date().getTime());
		String userId = CurrentSessionVar.getUserId() ;
		String json = "{flag:0, msg:'模板上传成功!'}";
		/**
		 * 参考ES代码，通过上传的文件在路径下生成excel文件
		 */
		String msFormatId = request.getParameter("msFormatId");

		String filename = null;
		String excelFilepathAndName = null;
		File excelFile = null;
		List<FileItem> fileList = null;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			fileList = upload.parseRequest(request);			
			if(fileList.size() == 1) {
				FileItem item = fileList.get(0); 
				if (!item.isFormField()) {
					// 判断该表单项是否是普通类型
					filename = item.getName();
					excelFilepathAndName = request.getSession().getServletContext().getRealPath(XSRFilepath.excelModalImpExpPath)
							+ File.separator + filename;
					excelFile = new File(excelFilepathAndName);
					FileUtils.copyInputStreamToFile(item.getInputStream(), excelFile);
				}
			}
			/**
			 * 创建ES的DocumentFile类，ES导入excel程序代码需要DocumentFile类型参数。实际在报表3.0中用来传递参数。
			 */
			DocumentFile documentFile= new DocumentFile();
			documentFile.setName(excelFile.getName());
			documentFile.setStar(false);
			documentFile.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
			documentFile.setCreateDate(new Date());
			documentFile.setUpdateDate(new Date());
			
			documentFile.setMODALFORMAT_ID(Integer.parseInt(msFormatId));
			documentFile.setCREATION_DATE(time);
			documentFile.setCREATED_BY(userId);

			impAndExpExcelService.importXlsx(documentFile, excelFile,request.getLocale());
			
		} catch (Exception e) {
			json = "{flag:1, msg:\"模板上传出错:" + e.toString() + "\"}";
			log.error("模板上传出错", e);
		} finally {
			//删除文件
			try {
				FileUtil.delFile(excelFilepathAndName);
			} catch (Exception e) {
				log.error("模板上传-删除文件出错：", e);
			}
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
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
	 * 模板格式设置-模板下载
	 */
	@RequestMapping(params = "method=exportFile", method = RequestMethod.POST)
	public void exportFile(@RequestParam(required = true) String modalsheetId,
			@RequestParam(required = true) String modalsheetName,
			@RequestParam(required = true) String msFormatId,
			HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		String json = "";
		String fileType = "xlsx";
		String excelFilepathAndName = session.getServletContext().getRealPath(XSRFilepath.excelModalImpExpPath)
				+ File.separator + modalsheetName + ".xlsx";
		DocumentFile documentFile = new DocumentFile();
		List<SheetTab> sheetTabList = new ArrayList<>();
		SheetTab st = new SheetTab();
		st.setModalsheetId(modalsheetId);
		st.setId(Integer.parseInt(msFormatId));
		st.setName(modalsheetName);
		st.setActive(true);
		st.setTabOrder(1);
		st.setOperateType("ExpModal");
		sheetTabList.add(st);
		documentFile.setSheetTabList(sheetTabList);
		documentFile.setName(excelFilepathAndName);
		try {
			String fileDir = session.getServletContext().getRealPath(XSRFilepath.excelModalImpExpPath);
			File file = new File(fileDir);
			if (!file.exists()) {
				file.mkdirs();
			}
			
			File xlsxFile = impAndExpExcelService.exportSpreadsheet(documentFile, request.getLocale());

			File exportFile;
			if (!"xlsx".equals(fileType)) {
				exportFile = new File(excelFilepathAndName);
				DocumentConvertPlugins.instance().getPlugin().convertFile(xlsxFile, exportFile, fileType);
			} else {
				exportFile = xlsxFile;
			}
			json = "{flag:'0', msg:'下载模板成功', fileName:'" + modalsheetName
					+ "', filePathAndName:'" + excelFilepathAndName + "'}";
			} catch(Exception e) {
			json = "{flag:1, msg:\"下载模板出错:" + e.getMessage() + "\"}";
			log.error("下载模板出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
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
	 * 报表导出-导出excel压缩文件
	 * @modifyAuthor tangxl
	 * @modifiedDate 2016-08-17
	 * @modify 修改报表导出方式
	 * @param sheetWithOrgIdlist  -- 添加ledgerId&ledgerName参数
	 * <document>
	 *   <title>按账簿分组批量导出报表</title>
	 *   <p>修改原始按组织分组导出报表的方式，增加账簿条件</p>
	 * </document>
	 * 
	 */
	@RequestMapping(params = "method=exportExcelFileWithData", method = RequestMethod.POST)
	public void exportExcelFileWithData(@RequestParam(required = true) String modaltypeId,
			@RequestParam(required = true) String periodId,
			@RequestParam(required = true) String periodName,
			@RequestParam(required = true) String cnyId,
			@RequestParam(required = true) String sheetWithOrgIdlist,
			@RequestParam(required = true) String expType,
			HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws Exception {
		
		// 创建导出文件的存放目录
		String fileDir = session.getServletContext().getRealPath(XSRFilepath.repExcelDataPath);
		File file = new File(fileDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 使用uuid生成压缩文件名称
		String zipfileName = UUID.randomUUID().toString();
		// 生成压缩文件路径全名
		String zipfilePathAndName = fileDir + File.separator + zipfileName + ".zip";
		List<String> fileList = new ArrayList<>(); 
		
		String suitId = (String) session.getAttribute("suitId");
		String json = "";
		//模板id&模板名称&模板格式id&公司id&公司名称&是否浮动&maxRow&ledgerId
		/*-----------修改sheetWithOrgIdArray为存放ledgerId的的数组-------*/
		String[] sheetWithOrgIdArray = sheetWithOrgIdlist.split(","); 
		Map<String, List<Map<String, String>>> sheetWithOrgMap = new HashMap<>();
		Map<String, String> fileNameMap = new HashMap<>();
		//按照账簿分类时，打包的文件名为【模板名称】，打包文件中的excel文件名字分别为【账簿名称】
		//按照模板分类时，打包的文件名为【账簿名称】，打包文件中的excel文件名字分别为【模板名称】
		if("LEDGER".equals(expType)) {
			for(String s : sheetWithOrgIdArray) {
				String[] p = s.split("&");
				String ledgerId = p[7];
				Map<String, String> m = new HashMap<>();
				m.put("modalsheetId", p[0]);
				m.put("tabName", p[1]); //页签为【模板名称】
				m.put("formatId", p[2]);
				m.put("orgId", p[3]);
				m.put("isFj", p[5]);
				m.put("maxRow", p[6]);
				m.put("orgName", p[4]);
				m.put("ledgerId", p[7]);
				m.put("ledgerName", p[8]);
				if(sheetWithOrgMap.containsKey(ledgerId)) {
					sheetWithOrgMap.get(ledgerId).add(m);
				} else {
					List<Map<String, String>> l = new ArrayList<>();
					l.add(m);
					sheetWithOrgMap.put(ledgerId, l);
					fileNameMap.put(ledgerId, p[8]); //按照账簿分类时，文件名为账簿名称，页签为模板名称
				}
			}
		} else {
			for(String s : sheetWithOrgIdArray) {
				String[] p = s.split("&");
				String modalsheetId = p[0];
				Map<String, String> m = new HashMap<>();
				m.put("modalsheetId", p[0]);
				m.put("formatId", p[2]);
				m.put("orgId", p[3]);
				m.put("tabName", p[8]);//页签为【账簿名称】
				m.put("isFj", p[5]);
				m.put("maxRow", p[6]);
				m.put("orgName", p[4]);
				m.put("ledgerId", p[7]);
				m.put("ledgerName", p[8]);
				if(sheetWithOrgMap.containsKey(modalsheetId)) {
					sheetWithOrgMap.get(modalsheetId).add(m);
				} else {
					List<Map<String, String>> l = new ArrayList<>();
					l.add(m);
					sheetWithOrgMap.put(modalsheetId, l);
					fileNameMap.put(modalsheetId, p[1]); //按照模板分类时，文件名为模板，页签为账簿名称
				}
			}
		}
		
		try {
			Set<String> sheetWithOrgMapSet = sheetWithOrgMap.keySet();
			for(String key : sheetWithOrgMapSet) {
				List<Map<String, String>> paramList = sheetWithOrgMap.get(key);
				
				String excelFilepathAndName = session.getServletContext().getRealPath(XSRFilepath.repExcelDataPath)
						+ File.separator + fileNameMap.get(key) + ".xlsx";
				
				DocumentFile documentFile = new DocumentFile();
				List<SheetTab> sheetTabList = new ArrayList<>();
				
				int count = 1;
				for(Map<String, String> paraMap : paramList) {
					SheetTab st = new SheetTab();
					st.setModalsheetId(paraMap.get("modalsheetId"));
					//st.setId(count);
					st.setId(Integer.parseInt(paraMap.get("formatId"))); //页签id设置为formatId
					st.setMODALFORMAT_ID(Integer.parseInt(paraMap.get("formatId")));
					st.setName(paraMap.get("tabName"));
					st.setTabOrder(count);
					st.setOrgId(paraMap.get("orgId"));
					st.setPeriodId(periodId);
					st.setCnyId(cnyId);
					st.setSuitId(suitId);
					st.setModaltypeId(modaltypeId);
					st.setTitleMaxRow(paraMap.get("maxRow"));
					st.setOperateType("N".equals(paraMap.get("isFj"))? "ExpFixData": "ExpFloatData");
					st.setActive((count == 1)? true: false);
					st.setOrgName(paraMap.get("orgName"));
					st.setPeriodName(periodName);
					//设置账簿ID
					st.setLedgerId(paraMap.get("ledgerId"));
					sheetTabList.add(st);
					count++;
				}

				documentFile.setSheetTabList(sheetTabList);
				documentFile.setName(excelFilepathAndName);
				//生成Excel文档
				impAndExpExcelService.exportSpreadsheet(documentFile, request.getLocale());
				fileList.add(excelFilepathAndName);
			}

			// 将生成的文件压缩
			FileUtil.zipFile(zipfilePathAndName, fileList);
			json = "{flag:0, msg:'报表导出excel文件成功', fileName:'" + zipfileName
					+ "', filePathAndName:'" + zipfilePathAndName + "'}";
		} catch(Exception e) {
			json = "{flag:1, msg:\"报表导出excel文件出错:" + e.getMessage() + "\"}";
			log.error("报表导出excel文件出错", e);
		}
		json = json.replaceAll("\\n", "").replaceAll("\\r", "").replace("\\", "/");
		// 输出JSON
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}
}
