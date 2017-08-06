package com.xzsoft.xc.rep.action;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.xzsoft.xsr.core.util.JsonUtil;


/**
 * @ClassName: BaseAction 
 * @Description: 基础处理类
 * @author linp
 * @date 2016年8月12日 下午4:44:14 
 *
 */
public class BaseAction {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BaseAction.class) ;

	/**
	 * @Title: out 
	 * @Description: 输出响应信息
	 * @param response
	 * @param result
	 * @throws Exception    设定文件
	 */
	public void out(HttpServletResponse response, Map<String, Object> result)  {
		
		String contentType = "text/html;charset=UTF-8";
		response.setContentType(contentType);
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setHeader("Pragma", "no-cache");
		
		Writer writer = null;
		try {
			writer = response.getWriter();
			JsonUtil.toJson(result, writer);
			writer.flush();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
	
	/**
	 * @Title: outPrint 
	 * @Description: 输出返回信息
	 * @param response
	 * @param rData    设定文件
	 */
	public void outPrint(HttpServletResponse response, String rData) {
		rData = rData.replaceAll("\\n", "");
		rData = rData.replaceAll("\\r", "");
		response.setContentType("text/html;charset=utf-8");

		try {
			response.getWriter().write(rData);
			response.getWriter().close();
		} catch (IOException arg2) {
			arg2.printStackTrace();
		}
	}
	
	/**
	 * @Title: downloadTextFile 
	 * @Description: 下载文本文件
	 * @param response
	 * @param textData
	 * @param fileName    设定文件
	 */
	public void downloadTextFile(HttpServletResponse response, String textData, String fileName) {
		ServletOutputStream os = null ;
		
		try {
			response.setContentType("text/html;charset=UTF-8");
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment; filename="+fileName);
			
			os = response.getOutputStream();
			os.write(textData.getBytes());
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			try {
				if(os != null) os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
