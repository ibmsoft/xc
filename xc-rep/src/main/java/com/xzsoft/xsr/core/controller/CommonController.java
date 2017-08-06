package com.xzsoft.xsr.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.webbuilder.utils.WebUtil;
import com.xzsoft.xsr.core.init.XSRFilepath;

@Controller
@RequestMapping("/CommonController.do")
public class CommonController {
	public Logger log = Logger.getLogger(this.getClass());

	/**
	 * zip文件导出
	 */
	@RequestMapping(params = "method=zipExp", method = RequestMethod.POST)
	public void zipExp(@RequestParam(required = true) String name, @RequestParam(required = true) String filepath,
			HttpServletRequest request, HttpServletResponse response) {
		log.info("===#== : 开始下载报表数据文件");
		InputStream is = null;
		ServletOutputStream os = null;
		try {
			is = new FileInputStream(new File(filepath));
			response.setHeader("content-type", "application/force-download");
			response.setHeader("content-disposition", "attachment;" + WebUtil.encodeFilename(request, name+".zip"));
			os = response.getOutputStream();
			byte[] data = new byte[10 * 1024];
			int len;
			while ((len = is.read(data)) != -1) {
				os.write(data, 0, len);
			}

		} catch (Exception ex) {
			log.error("下载ZIP文件出错!", ex);
			ex.printStackTrace();
		} finally {
			// 关闭输入流和输出流
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("关闭输入流和输出流出错!", e);
				}
			}
			// 删除临时文件
			if (filepath != null) {
				File file = new File(filepath);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}
	/**
	 * 模板导出zip文件
	 * @param fileName  被下载的文件名称, 不带文件的扩展名.zip
	 * @param filePathAndName  被下载的文件绝对路径和名称，如：C:/XSRFile/123.zip
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=zipExpModal", method = RequestMethod.POST)
	public void zipExpModal(@RequestParam(required = true) String fileName, @RequestParam(required = true) String filePathAndName,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		InputStream is = null;
		ServletOutputStream os = null;
		try {
			//String fileDir =  session.getServletContext().getRealPath(XSRFilepath.modalImpExpPath);
			//String zipfilePathAndNameTEST = fileDir + File.separator + fileName + ".zip";
			//File file2 = new File(zipfilePathAndNameTEST);
			//is = new FileInputStream(file2);
			filePathAndName = java.net.URLDecoder.decode(filePathAndName,"UTF-8");
			fileName = java.net.URLDecoder.decode(fileName,"UTF-8");
			File file = new File(filePathAndName);
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
			log.error("模板导出-下载ZIP文件出错!", ex);
		} finally {
			// 关闭输入流和输出流
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("关闭输入流和输出流出错!", e);
				}
			}
			// 删除临时文件
			if (filePathAndName != null) {
				File file = new File(filePathAndName);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}
	/**
	 * 下载模板格式文件
	 * @param fileName  被下载的文件名称, 不带文件的扩展名.zip
	 * @param filePathAndName  被下载的文件绝对路径和名称，如：C:/XSRFile/123.zip
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=excelExpModal", method = RequestMethod.POST)
	public void excelExpModal(@RequestParam(required = true) String fileName, @RequestParam(required = true) String filePathAndName,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		InputStream is = null;
		ServletOutputStream os = null;
		try {
			//fileName = new String(fileName.getBytes("iso8859-1"),"utf-8");
			//filePathAndName = new String(filePathAndName.getBytes("iso8859-1"),"utf-8");
			File file = new File(filePathAndName);
			is = new FileInputStream(file);
			response.setHeader("content-type", "application/force-download");
			response.setHeader("content-disposition", "attachment;" + WebUtil.encodeFilename(request, fileName+".xlsx"));
			os = response.getOutputStream();
			byte[] data = new byte[10 * 1024];
			int len;
			while ((len = is.read(data)) != -1) {
				os.write(data, 0, len);
			}
		} catch (Exception ex) {
			log.error("下载模板-下载excel文件出错!", ex);
		} finally {
			// 关闭输入流和输出流
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("关闭输入流和输出流出错!", e);
				}
			}
			// 删除临时文件
			if (filePathAndName != null) {
				File file = new File(filePathAndName);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}
	/**
	 * 报表批量导出文件
	 * @param fileName  被下载的文件名称, 不带文件的扩展名.zip
	 * @param filePathAndName  被下载的文件绝对路径和名称，如：C:/XSRFile/123.zip
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=batExpSheets")
	public void batExpSheets(@RequestParam(required = true) String fileName, @RequestParam(required = true) String filePathAndName,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		InputStream is = null;
		ServletOutputStream os = null;
		try {
			//String fileDir =  session.getServletContext().getRealPath(XSRFilepath.modalImpExpPath);
			//String zipfilePathAndNameTEST = fileDir + File.separator + fileName + ".zip";
			//File file2 = new File(zipfilePathAndNameTEST);
			//is = new FileInputStream(file2);
			filePathAndName = java.net.URLDecoder.decode(filePathAndName,"UTF-8");
			fileName = java.net.URLDecoder.decode(fileName,"UTF-8");
			File file = new File(filePathAndName);
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
			log.error("模板导出-下载ZIP文件出错!", ex);
		} finally {
			// 关闭输入流和输出流
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("关闭输入流和输出流出错!", e);
				}
			}
			// 删除临时文件
			if (filePathAndName != null) {
				File file = new File(filePathAndName);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}
	/**
	 * 导出word版稽核结果
	 * 在稽核完成后，如果存在稽核结果，将稽核结果的word文件路径存在到session中
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=downloadDocFile", method = RequestMethod.POST)
	public void downloadDocFile(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		InputStream is = null;
		ServletOutputStream os = null;
		String filePathAndName = null;
		try {
			filePathAndName = (String)session.getAttribute("CheckResultFilePath");
			if(null != filePathAndName && !"".equals(filePathAndName)) {
				String fileName = filePathAndName.substring(filePathAndName.lastIndexOf(File.separator)+1);
				File file = new File(filePathAndName);
				is = new FileInputStream(file);
				response.setHeader("content-type", "application/force-download");
				response.setHeader("content-disposition", "attachment;" + WebUtil.encodeFilename(request, fileName));
				os = response.getOutputStream();
				byte[] data = new byte[10 * 1024];
				int len;
				while ((len = is.read(data)) != -1) {
					os.write(data, 0, len);
				}
			} else {
				log.error("导出word版稽核结果-出错!-session中没有获取到CheckResultFilePath稽核结果路径！");
			}
		} catch (Exception ex) {
			log.error("导出word版稽核结果-下载doc文件出错!", ex);
		} finally {
			// 关闭输入流和输出流
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("关闭输入流和输出流出错!", e);
				}
			}
			// 删除临时文件
			if (filePathAndName != null) {
				File file = new File(filePathAndName);
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}
}
