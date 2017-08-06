package com.xzsoft.xsr.core.controller;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xsr.core.init.SessionVariableXsr;

@Controller
@RequestMapping("/SessionListController.do")
public class SessionListController {
	private static Logger log = Logger.getLogger(SessionListController.class.getName());
	/**
	 * 获取Session里面的参数信息 add by songyh 2016-01-06
	 * @param mapping
	 * @param actionForm
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "method=getSessionParams", method = RequestMethod.GET)
	public void getSessionParams(
			//@RequestParam(required = true) String mode,
			HttpServletRequest request, HttpServletResponse response){  
		StringBuffer sb = new StringBuffer();
		sb.append("<table border='1' cellpadding='0' cellspacing='0' width='50%' bordercolor='#00A06B'><tr><th>类别</th><th>键</th><th>值</th></tr>");
		//平台会话变量
		Field[] filedArray =SessionVariable.class.getFields();
		for (int i = 0; i < filedArray.length; i++) {
			String fieldKey = filedArray[i].getName();
			String fieldValue = (String)request.getSession().getAttribute(fieldKey);
			if(!"".equals(fieldValue) && fieldValue !=null){
				sb.append("<tr><td height='28'> &nbsp;&nbsp;&nbsp;").append("平台").append("</td><td>").append(fieldKey).append("</td><td>").append(fieldValue).append("</td></tr>");
			}
		}
		//报表侧会话变量 
		Field[] filedArrayXsr =SessionVariableXsr.class.getFields();
		for (int i = 0; i < filedArrayXsr.length; i++) {
			String fieldKey = filedArrayXsr[i].getName();
			String fieldValue = (String)request.getSession().getAttribute(fieldKey);
			if(!"".equals(fieldValue) && fieldValue !=null){
				sb.append("<tr><td height='28'> &nbsp;&nbsp;&nbsp;").append("报表").append("</td><td>").append(fieldKey).append("</td><td>").append(fieldValue).append("</td></tr>");
			}
		}
		sb.append("</table>");
		//this.outPrint(response, sb.toString());
		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(sb.toString());
			response.getWriter().close();
		} catch (IOException e) {
			log.error("获取报表系统会话变量失败！",e);
		}
	}
}
