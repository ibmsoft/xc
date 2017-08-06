package com.xzsoft.xsr.core.webservice.internal;



import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.xzsoft.xsr.core.service.ReportReceiveService;

/**
 * 接收报表系统内部webservice接口数据
 * 
 * @author ZhouSuRong 2016-4-6
 */
@Controller
@RequestMapping("/internalWSController.do")
public class InternalDataReceive {
	public Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ReportReceiveService reportReceiveService;
	@Autowired
	private InternalDataService internalDataService;
	/**
	 * 报表数据接收
	 */
	@RequestMapping(params = "method=recevieReport", method = RequestMethod.POST)
	public void recevieReport(HttpServletRequest request, HttpServletResponse response) {
		String json = "";
		HashMap<String, String> params = new HashMap<String, String>();
		String modalTypeId = request.getParameter("modalTypeId");
		String periodId = request.getParameter("periodId");
		String cnyId = request.getParameter("cnyId");
		String corpModalList = request.getParameter("cmList");
		String userId = (String) request.getSession().getAttribute("userId");
		String suitId = (String) request.getSession().getAttribute("suitId");
		params.put("modalTypeId", modalTypeId);
		params.put("periodId", periodId);
		params.put("cnyId", cnyId);
		params.put("userId", userId);
		params.put("suitId", suitId);
		params.put("corpModalList", corpModalList);
		try {
			json = reportReceiveService.recevieSelectedReport(params);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	 * 数据导入
	 * @throws Exception 
	 */
	@RequestMapping(params = "method=dataImp", method = RequestMethod.POST)
	public void dataImp(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//InternalDataService internalDataService=new InternalDataServiceImpl();
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List fileList = upload.parseRequest(request);
        Iterator it = fileList.iterator();
        while (it.hasNext()) {
            FileItem item = (FileItem) it.next();
            if (!item.isFormField()) {
                // 判断该表单项是否是普通类型
                String fileName = item.getName();
                String fileType = "";
                if (fileName == null || "".equals(fileName.trim())) {
                    continue;
                }
                // 扩展名格式：
                int pos = fileName.lastIndexOf(".");
                if (pos >= 0) {
                    fileType = fileName.substring(pos);
                }
                JSONArray tableList = null;
                try {
                	InputStream in = item.getInputStream();
                    byte[] bytes = new byte[in.available()];
                    in.read(bytes);
                    String jsonStr = new String(bytes, "utf-8");
                    tableList = JSONArray.fromObject(jsonStr);
                    for(int i=0;i<tableList.size();i++){
                    	String json = tableList.getJSONObject(i).toString();
                    	internalDataService.sendData(json,"","");
                    }
                } catch (IOException e) {
                    log.error(e);
                }
            }
        }
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
