package com.xzsoft.xc.gl.action;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xzsoft.xc.gl.servlets.XCGLInitServlet;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.modal.User;
import com.xzsoft.xip.platform.service.UserService;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @fileName CustomizationLoginAction
 * @describe 客户化登录
 * @author   tangxl
 *
 */
@Controller
@RequestMapping("/customizedLoginAction.do")
public class CustomizationLoginAction {
	// 日志记录器
	private static Logger log = Logger.getLogger(CustomizationLoginAction.class.getName()) ;
	
	@Resource
    UserService userServiceImpl;
	/**
	 * 
	 *@methodName       customLoginCheck
	 *@methodDescribe   客户化登录校验<使用当前用户个人密码或者超级管理员admin密码都能登录>主登录方法会传递用户名和密码两个参数过来
	 *@date 2016年5月26日
	 *@author tangxl
	 *@param request
	 *@param response
	 *@return
	 *@throws Exception
	 */
	@RequestMapping(params="method=customLoginCheck")
	public void customLoginCheck(HttpServletRequest request, HttpServletResponse response) throws Exception{
		JSONObject json = new JSONObject();
		String userName = request.getParameter("loginName");
		String password = request.getParameter("password");
		String lang = XCUtil.getLang();//多语言变量
		try {
			User adminUser = userServiceImpl.getUserByName("ADMIN");
			User user = userServiceImpl.getUserByName(userName);
			/*校验密码的非法性
			 *1、登录用户名不存在
			 *2、管理员账户不存在：2.1 当前用户密码不正确。2.2 当前用户为非系统用户，并且用户未生效或者已失效
			 *3、管理员和当前用户账户都存在：3.1 密码既不为管理员账户密码也不是当前用户密码 3.2 同2.2 
			 */
			
			if(user == null){
				json.put("flag", "1");
				json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_NULL", null));
				return ;
			}
			
			if(adminUser == null){
				//管理员用户不存在
				if(user.getPassword() == null || !password.equals(user.getPassword())){
					json.put("flag", "1");
					json.put("msg", XipUtil.getMessage(lang, "XC_GL_PASSWORD_ERROR", null));
				}else{
					if ("U".equals(user.getUserType())) {
						long nowTime = new Date().getTime();
						if(user.getStartDate() != null && nowTime<user.getStartDate().getTime()){
							json.put("flag", "1");
							json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_NO_EFFECT", null));
						}else if(user.getEndDate() !=null && nowTime>user.getEndDate().getTime()){
							json.put("flag", "1");
							json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_NO_EFFECT", null));
						}else{
							json.put("flag", "0");
							json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_LOGIN", null));
						}
					} else {
						json.put("flag", "0");
						json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_LOGIN", null));
					}
				}
				return ;
			}
			if(user != null && adminUser != null){
				if (user.getPassword() == null && adminUser.getPassword() == null) {
					//空密码<没必要判断，系统中的用户名和密码是不能为空的>
					json.put("flag", "1");
					json.put("msg", XipUtil.getMessage(lang, "XC_GL_PASSWORD_ERROR", null));
				}else if(user.getPassword() == null && !password.equals(adminUser.getPassword())){
					json.put("flag", "1");
					json.put("msg", XipUtil.getMessage(lang, "XC_GL_PASSWORD_ERROR", null));
				}else if(adminUser.getPassword() == null && !password.equals(user.getPassword())){
					json.put("flag", "1");
					json.put("msg", XipUtil.getMessage(lang, "XC_GL_PASSWORD_ERROR", null));
				} else if (!password.equals(user.getPassword()) && !password.equals(adminUser.getPassword())) {
					json.put("flag", "1");
					json.put("msg", XipUtil.getMessage(lang, "XC_GL_PASSWORD_ERROR", null));
				} else {
					if ("U".equals(user.getUserType())) {
						long nowTime = new Date().getTime();
						if(user.getStartDate() != null && nowTime<user.getStartDate().getTime()){
							json.put("flag", "1");
							json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_NO_EFFECT", null));
						}else if(user.getEndDate() !=null && nowTime>user.getEndDate().getTime()){
							json.put("flag", "1");
							json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_NO_EFFECT", null));
						}else{
							json.put("flag", "0");
							json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_LOGIN", null));
						}
					} else {
						json.put("flag", "0");
						json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_LOGIN", null));
					}
				}
			}
		} catch (Exception e) {
			json.put("flag", "1");
			json.put("msg", XipUtil.getMessage(lang, "XC_GL_USER_LOGIN_EXCEPTION", null));
		}finally{
			// 如果登录成功，则加载云ERP缓存数据
			this.doCache(); 
			
			PlatformUtil.outPrint(response, json.toString());
		}
	}
	
	/**
	 * @Title: doCache 
	 * @Description: 加载缓存数据
	 */
	private void doCache() {
		try {
			// 启动线程执行初始化处理
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						// 判断Redis是否开启
						String lang = XCUtil.getLang();//多语言变量
						boolean isUsedRedis = Boolean.parseBoolean(PlatformUtil.getXipConfigVal("xip.useRedis")) ;
						if(!isUsedRedis){
							throw new Exception(XipUtil.getMessage(lang, "XC_GL_REDIS_CLOSE", null)) ;
							
						}else{
							// 判断Redis数据库是否配置正确或是否已经开启服务
							if(PlatformUtil.getRedisTemplate() == null){
								throw new Exception(XipUtil.getMessage(lang, "XC_GL_REDIS_CONNECT_ERROR", null)) ;
							
							}else{
								log.info(XipUtil.getMessage(lang, "XC_GL_DATA_INIT", null));
								
								// 执行数据缓存处理
								XCGLInitServlet initServlet = new XCGLInitServlet() ;
								initServlet.doCache2Redis(null, "ALL");
								
								log.info(XipUtil.getMessage(lang, "XC_GL_DATA_FINISH", null));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}).start() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
