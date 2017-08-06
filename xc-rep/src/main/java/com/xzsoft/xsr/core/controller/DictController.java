package com.xzsoft.xsr.core.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
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
import com.webbuilder.utils.WebUtil;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xsr.core.service.DictService;

@Controller
@RequestMapping("/dictController.do")
public class DictController {
	private static Logger log = Logger.getLogger(CheckFormulaController.class.getName());
	@Autowired
	private DictService dictExpService;
	
	/**
	 * 字典导出
	 * @param dictName
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=dictExp",method = RequestMethod.POST)
	public void dictExp(@RequestParam(required = true) String dictName,HttpServletRequest request,
			HttpServletResponse response){
		
		// 建立临时文件目录
		String tempDir = "XSRFile/dict";
		File file = new File(Main.path, tempDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		// 建立XML文件
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String filepath = file + File.separator + "Dict".concat(format.format(new Date())).concat(".xml");
		// 将内容写入XML
		InputStream is = null;
		ServletOutputStream os = null;

		try {
			dictExpService.dictExp(filepath,dictName);
			is = new FileInputStream(new File(filepath));
			response.setHeader("content-type", "application/force-download");
			response.setHeader("content-disposition", "attachment;" + WebUtil.encodeFilename(request, dictName+".xml"));
			os = response.getOutputStream();
			byte[] data = new byte[10 * 1024];
			int len;
			while ((len = is.read(data)) != -1) {
				os.write(data, 0, len);
			}

		} catch (Exception ex) {
			log.error("下载xml文件出错!", ex);
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
				File f = new File(filepath);
				if (f.exists()) {
					f.delete();
				}
			}
		}
		
	}
	
	/**
	 * 字典导入
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=dictImpt",method = RequestMethod.POST)
	public void dictImpt(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession();
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		
		String tempDir = "XSRFile/dict";
		File folder = new File(Main.path, tempDir);
		folder.mkdirs();
		List<FileItem> fileList;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String json = "";
		try {
			fileList = upload.parseRequest(request);
			Iterator<FileItem> it = fileList.iterator();
			 while (it.hasNext()) {
		            FileItem item = (FileItem) it.next();  //获取FileItem对象
		            if (!item.isFormField()) {// 判断是否为文件域
		                if (item.getName() != null && !item.getName().equals("")) {// 判断是否选择了文件
		                    String fileName=item.getName();     //获取文件名
		                    File tempFile = new File(fileName);// 构造临时对象
		                    File file = new File(folder,tempFile.getName());   // 获取根目录对应的真实物理路径
		                    InputStream is=item.getInputStream();
		                    int buffer=1024;     //定义缓冲区的大小
		                    int length=0;
		                    byte[] b=new byte[buffer];
		                    FileOutputStream fos=new FileOutputStream(file);
		                    while((length=is.read(b))!=-1){
		                         fos.write(b,0,length);                      //向文件输出流写读取的数据
		                    }
		                    fos.close();
		                        Thread.sleep(1000);     //线程休眠1秒
		                }
		            }
		        }
		} catch (FileUploadException e) {
			log.error(e);
		} catch (Exception e) {
			log.error(e);
		}
		File fs[] = folder.listFiles();
		for (File f : fs) {
			try {
				json = dictExpService.dictImpt(f,userId);
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
}
