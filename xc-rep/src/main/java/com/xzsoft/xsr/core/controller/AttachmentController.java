package com.xzsoft.xsr.core.controller;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xsr.core.modal.Attachment;
import com.xzsoft.xsr.core.service.AttachmentService;




@Controller
@RequestMapping("/attachmentController.do")
public class AttachmentController {
	private static Logger log = Logger.getLogger(AttachmentController.class.getName());
	
	@Autowired
	private AttachmentService attachmentService;
	/**
	 * 附件导入
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=uploadAttachment", method = RequestMethod.POST)
	public void uploadAttachment(HttpServletRequest request, HttpServletResponse response){
		String json = null;
		String msg = null;

		String paramList = request.getParameter("paramList");
		String params[] = null;
		if(paramList != null || !"".equals(paramList)) {
			params = paramList.split(",");
		}
		
		HttpSession session = request.getSession();
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		
		List<FileItem> fileList;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			fileList = upload.parseRequest(request);
			Iterator<FileItem> it = fileList.iterator();
			while (it.hasNext()) {
				FileItem item = it.next();
				if (!item.isFormField()) {
					// 判断该表单项是否是普通类型
					String fileName = item.getName();
					long fileSize = item.getSize(); //文件大小
					if(fileSize>16*1024*1024){
						msg="文件大小超过16M,请重新上传！";
					}else{
						msg = attachmentService.saveAttachment(item, params, userId, fileName);
					}
					json = "{success:true,flag:0,msg:'"+msg+"'}";
				}
			}
		} catch (Exception e) {
			String msgCause = e.getCause().getClass().getName();
			if(msgCause.equals("com.mysql.jdbc.PacketTooBigException")){
				msg="您上传的文件超过数据库设置的最大值，请重新上传!";
			}else{
				msg = e.getMessage();
			}
			json = "{success:false,flag:1,msg:'"+msg+"'}";
			e.printStackTrace();
		}
		// 提示信息
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 附件导出
	 * @param attachId
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=downloadAttachment", method = RequestMethod.POST)
	public void downloadAttachment(String attachId,HttpServletResponse response) throws Exception{
		Attachment attachment = attachmentService.getAttachmentById(attachId);
	    byte[] data = attachment.getATTACH_STREAM();  
	    String fileName = attachment.getATTACH_NAME();  
	    response.reset();  
	    response.setHeader("Content-Disposition", "attachment; filename="+ 
	    		new String((fileName).getBytes("GBK"), "iso8859-1"));
	    response.addHeader("Content-Length", "" + data.length);  
	    response.setContentType("application/octet-stream;charset=UTF-8");  
	    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
	    outputStream.write(data);  
	    outputStream.flush();  
	    outputStream.close();  
	}
}
