package com.xzsoft.xsr.core.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;





import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xsr.core.init.XSRFilepath;
import com.xzsoft.xsr.core.service.ExcelSevice;
import com.xzsoft.xsr.core.util.FileUtil;

@Controller
@RequestMapping("/excelController.do")
public class ExcelController {
	private  Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private ExcelSevice excelService;
	/**
	 * 将报表导出成远光报表格式 多单位，多模板在同一个文件中
	 * added by zhousr 2012-11-14
	 * @param mapping
	 * @param form
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
		String json="";
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
			 String msg=excelService.exportDataWithMoreCorp(suitId, xlsFullName, entityCodeList, entityNameList, periodCode, currencyCode, mdlSheetList,sheetNamelist, mdlTypeCode, reportNum, userId, mdlshtWithEntity);
			 if(!"".equals(msg)){
//			    	if(msg.indexOf(",") != -1){
//			    		json = "{msg:'模板 《"+msg.substring(0,msg.length()-1)+"》 列指标类型或者数据有问题，请检查'}";
//			    	}else{
//			    		json = "{msg:'"+msg+"'}";
//			    	}
				 json = "{msg:'"+msg+"'}";
			    }else{
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
				   
			    }
		} catch (Exception e) {
			// TODO: handle exception
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
}