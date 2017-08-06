package com.xzsoft.xsr.core.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.webbuilder.common.Main;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xsr.core.init.XSRFilepath;
import com.xzsoft.xsr.core.modal.ReceiveBean;
import com.xzsoft.xsr.core.service.BatOpService;
import com.xzsoft.xsr.core.util.FileUtil;

@Controller
@RequestMapping("/batOpController.do")
public class BatOpController {

	private  Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private BatOpService batOpService;
	
	/**
	 * 批量导出-将报表导出成远光报表格式 多单位，多模板在同一个文件中
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */								
	@RequestMapping(params = "method=exportYgReportWithMoreCorp", method = RequestMethod.POST)
	public void exportYgReportWithMoreCorp(
	HttpServletRequest request,HttpServletResponse response) {
		HttpSession session = request.getSession();
		String suitId=(String) session.getAttribute("suitId");
		// 获取参数
		String entityCodeList=request.getParameter("corp");//公司编码列表
		String entityNameList=request.getParameter("corpName");//公司名称列表
		String periodCode=request.getParameter("period");//期间编码
		String currencyCode=request.getParameter("currency");//币种编码
		String mdlTypeCode=request.getParameter("mdltype");//模板集编码
		String mdlSheetList=request.getParameter("mdllist");//模板编码列表
		String sheetNamelist=request.getParameter("sheetNamelist");//模板编码列表
		String reportNum=request.getParameter("reportNumber");//报表个数
		String mdlshtWithEntity = request.getParameter("mdlsheetWithEntity");
		// 取得Session变量中的userName
		// 1、得到平台的Session变量
		HashMap<String, String> sessionVars = (HashMap<String, String>) session.getAttribute(SessionVariable.XZ_SESSION_VARS);
		
		String userId = sessionVars.get(SessionVariable.userId);
		//采用 用户角色授权
		String roleId = sessionVars.get(SessionVariable.roleId);
		// 返回信息
		String json = "";
		String xlsName="";
		String xlsFullPath = "";
		String zipFile[]=new String [1];
		FileUtil fo=new FileUtil();
		String uuid = UUID.randomUUID().toString();
		//得到下发文件的名称
		xlsName=fo.getXlsName(periodCode, uuid);
		// 根据操作类型,创建文件路径
		String xlsPath = XSRFilepath.ygDataPath;
		String xlsFullName=xlsPath+File.separator+xlsName;
		xlsFullPath =  session.getServletContext().getRealPath("XSRFile/YGReport").replace(File.separator+File.separator, File.separator);
		xlsFullName =  session.getServletContext().getRealPath(xlsFullName).replace(File.separator+File.separator, File.separator);
		//以客户机IP地址为名称，创建模板下发目录（服务器）
		fo.newFolder(xlsFullPath);
		try {
			//执行远光数据的导出
			 batOpService.exportDataWithMoreCorp(suitId, xlsFullName, entityCodeList, entityNameList, periodCode, currencyCode, mdlSheetList,sheetNamelist, mdlTypeCode, reportNum, userId, mdlshtWithEntity);
			    	//远光格式文件zip打包
				    zipFile[0]=xlsFullName.replace(".ini",".zip").replace(".INI", ".zip");
	
				    //需要压缩的文件列表
				    List<String> file = new ArrayList<String>();
				    file.add(xlsFullName);
				    file.add(xlsFullName.replace("JI", "JS").replace(".INI", ".DAT").replace(".ini", ".DAT"));
				    
				    fo.zipFile(zipFile[0], file);
				    
//				    //删除文件信息(ini和dat文件)  导出失败之后--垃圾文件删除不了，在异常里面有处理
//				    fo.delFile(xlsFullName);
//				    fo.delFile(xlsFullName.replace("JI", "JS").replace(".INI", ".DAT"));
				    // 9.返回下载参数信息
				    int t = xlsName.lastIndexOf(".");
					xlsName = xlsName.substring(0,t);
				    json = "{'filepath':'"+zipFile[0].replace("\\", "/")+"','name':'"+xlsName+"'}";
				   
				   
		} catch (Exception e) {
			 json = "{'filepath':'"+zipFile[0].replace("\\", "/")+"','name':'"+xlsName+"',msg':'导出文件出错!'}";
		}finally{
			try {
				fo.delFile(xlsFullName);
				fo.delFile(xlsFullName.replace("JI", "JS").replace(".INI", ".DAT"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 // 设置编码格式
	    response.setContentType("text/xml;charset=UTF-8");
	 // 输出JSON串
		try {
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
	}
	
}
	/**
	 * 批量导入-报表接收上传
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */	
	@RequestMapping(params = "method=batImportSheets",method=RequestMethod.POST)
	public void batImportSheets(HttpServletRequest request,HttpServletResponse response){
		HttpSession session=request.getSession();
		String sessionId=session.getId();
		System.out.println(sessionId);
		session.setAttribute("sessionId", sessionId);
		String modaltypeId = request.getParameter("modaltypeId");
		String modaltypeCode = request.getParameter("modaltypeCode");
		String filename = request.getParameter("filename");
		String isSelectAll = request.getParameter("isSelectAll");
		String suitId=(String) session.getAttribute("suitId");
		HashMap<String, String> sessionVars = (HashMap<String, String>) session
				.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		List<FileItem> fileList;
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String tempDir = "XSRFile/YGReport";
		File folder = new File(Main.path, tempDir);
		folder.mkdirs();
		String xlsName=tempDir+File.separator+filename;
		String xlsFullName=session.getServletContext().getRealPath(xlsName).replace(File.separator+File.separator, File.separator);
		FileUtil  fileUtil=new FileUtil();
		String type="YGReport";
		String delFilePath = xlsFullName;;
		String json = "{flag:0,msg:'上传文件成功',success:true}";
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
						if (!filename.equals("")) {  
				            FileOutputStream fos = new FileOutputStream(xlsFullName);  
				            byte[] buffer = new byte[8192]; // 每次读8K字节  
				            int count = 0; 
				            InputStream is=item.getInputStream();
				            // 开始读取上传文件的字节，并将其输出到服务端的上传文件输出流中  
				            while ((count = is.read(buffer)) > 0) {  
				                fos.write(buffer, 0, count); // 向服务端文件写入字节流  
				            }  
				            fos.close(); // 关闭FileOutputStream对象  
				            is.close(); // InputStream对象  
				        } 
						// 上传zip文件到新建上传路径
						String zipFile[] = new String[1];
						zipFile[0] = xlsFullName;

						fileUtil.unZip(zipFile);
					}
				}
			}
		} catch (Exception e) {
			json = "{flag:1, msg:'上传文件解压出错: " + e.getMessage() + "'}";
			e.printStackTrace();
			return;
		}
		
		//封装参数
		ReceiveBean bean=new ReceiveBean();
		bean.setMODALTYPE_ID(modaltypeId);
		bean.setSESSIONID(sessionId);
		bean.setUSER_ID(userId);
		bean.setSUIT_ID(suitId);

		// 将文件后缀统一转换成小写
		fileUtil.fileSuffixesToLowerCase(xlsFullName);

		// 将通过zip文件得到ini远光包头文件
		xlsFullName = xlsFullName.replace(".zip", ".ini").replace(".ini",".INI");
		try {
			//数据解析，并插入数据信息
			String isExistData=batOpService.parseAndAddImpData(sessionId, userId, suitId, type, filename, modaltypeId, modaltypeCode, bean, xlsFullName);
			if(isExistData.equals("0")){
				json="{flag:1,msg:'该报表无数据，请重新导入'}";
				// 提示信息
				response.setCharacterEncoding("utf-8");
				try {
					response.getWriter().print(json);
					response.getWriter().close();
				} catch (IOException e) {
					log.error("传输json串出错", e);
				}
				return ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			json = "{flag:1, msg:'数据解析插入数据出错: " + e.getMessage() + "'}";
			return;
		}
		//开始校验
		//校验公司
		try {
			batOpService.updateTempEntityData(bean, sessionId);
		} catch (Exception e) {
			e.printStackTrace();
			json = "{flag:1, msg:'【单位校验】： " + e.getMessage() + "'}";
			return;
		}
		//校验期间
		try {
			batOpService.updateTempPeriodData(bean, sessionId, suitId);
		} catch (Exception e) {
			e.printStackTrace();
			json = "{flag:1, msg:'【期间校验】： " + e.getMessage() + "'}";
			return;
		}
		//币种校验
		try {
			batOpService.updateTempCurrencyData(suitId, sessionId);
		} catch (Exception e1) {
			e1.printStackTrace();
			json = "{flag:1, msg:'【期间校验】： " + e1.getMessage() + "'}";
			return;
		}
		//模板校验
		try {
			batOpService.updateTempModalData(suitId, sessionId, modaltypeId);
		} catch (Exception e1) {
			e1.printStackTrace();
			json = "{flag:1, msg:'【模板校验】： " + e1.getMessage() + "'}";
			return;
		}
		//行指标校验
		try {
			batOpService.updateTempRowitemData(suitId, sessionId, modaltypeId);
		} catch (Exception e1) {
			e1.printStackTrace();
			json = "{flag:1, msg:'【行指标校验】： " + e1.getMessage() + "'}";
			return;
		}
		//列指标校验
		try {
			batOpService.updateTempColitemData(suitId, sessionId, modaltypeId);
		} catch (Exception e1) {
			e1.printStackTrace();
			json = "{flag:1, msg:'【列指标校验】： " + e1.getMessage() + "'}";
			return;
		}
		//校验行列指标是否合法
		try {
			batOpService.checkTempRowAndCol(suitId, sessionId, modaltypeId);
		} catch (Exception e1) {
			e1.printStackTrace();
			json = "{flag:1, msg:'【行列引用校验】： " + e1.getMessage() + "'}";
			return;
		}finally{
			try {
				fileUtil.delFile(delFilePath);
				fileUtil.delFile(delFilePath.replace("zip", "INI"));
				fileUtil.delFile(delFilePath.replace("zip", "DAT").replace("JI", "JS"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		json = "{flag:0,msg:'解析成功',filename:'" + filename + "',sessionId:'"+sessionId+"',success:true}";
		// 提示信息
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().print(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}
	/**
	 * 接收选中的报表
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "method=impReportData", method = RequestMethod.POST)
	public void impReportData(HttpServletRequest request,HttpServletResponse response){
		String json = "";
		HttpSession session = request.getSession();
		String suitId=(String) session.getAttribute("suitId");
		String cmStr = request.getParameter("cmStr");
		String sessionId = request.getParameter("sessionId") ;
		String modaltypeId = request.getParameter("modaltypeId") ;
		String periodId = request.getParameter("periodId") ;
		String currencyId = request.getParameter("currencyId") ;
		
		HashMap<String, String> sessionVars = (HashMap<String, String>) session.getAttribute(SessionVariable.XZ_SESSION_VARS);
		String userId = sessionVars.get(SessionVariable.userId);
		String roleId = sessionVars.get(SessionVariable.roleId);
		List<HashMap<String, String>> entityAndModalMapList=new ArrayList<HashMap<String,String>>();
		try {
			String flag=batOpService.isLock(sessionId, suitId, cmStr);
			if("".equals(flag)){
				String[] strArray = cmStr.split("&");
				if(strArray.length > 0){
					for(int i=0;i<strArray.length;i++){
						HashMap<String, String> entityAndModalMap=new HashMap<String, String>();
						String[] s = strArray[i].split(":");
						String entityId = s[0];
						String modalsheetId = s[1];
						batOpService.receiveData(suitId, sessionId, entityId, periodId, currencyId, modaltypeId, modalsheetId, userId);
					}
				}
			}
			json="{flag:0,msg:'报表全部接收成功！'}";
		} catch (Exception e) {
			e.printStackTrace();
			json="{flag:1,msg:'报表全部接收失败！"+e.getMessage()+"'}";
		}
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().print(json);
			response.getWriter().close();
		} catch (IOException e) {
			log.error("传输json串出错", e);
		}
	}
	
}
