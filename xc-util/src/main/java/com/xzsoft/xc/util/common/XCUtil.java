package com.xzsoft.xc.util.common;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;

import com.xzsoft.xip.framework.common.SessionVariable;
import com.xzsoft.xip.framework.common.XipTenant;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.framework.util.XipSwitch;
import com.xzsoft.xip.platform.dao.UserDao;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.session.CustomSessionContext;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: XCUtil 
 * @Description: 云ERP公共方法
 * @author linp
 * @date 2016年5月9日 下午7:26:01 
 *
 */
public class XCUtil {

	/**
	 * @Title: getXipTenant 
	 * @Description: 获取租户信息的对象
	 * @return
	 * @throws Exception    设定文件
	 */
	public static XipTenant getXipTenant() throws Exception {
		XipTenant xt = XipSwitch.getCurrTenant() ;
		return xt ;
	}
	
	/**
	 * @Title: getTenantId 
	 * @Description: 获取租户ID
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String getTenantId() throws Exception {
		String tenanId = "xip" ;
		XipTenant xt = getXipTenant() ;
		if(xt != null){
			tenanId = xt.getTenantId() ;
		}
		return tenanId ;
	}
	
	/**
	 * @Title: setSessionUserId 
	 * @Description: 初始化用户登录信息
	 * @param loginName
	 * @param password
	 * @throws Exception    设定文件
	 */
	@SuppressWarnings("unchecked")
	public static void setSessionUserId(String loginName,String password) throws Exception {
    	// 用户ID
    	String userId = CurrentSessionVar.getUserId() ;
    	
    	// 如果用户未登录，则设置模拟登陆
    	if(userId == null || "".equals(userId)){
            HttpSession session = CustomSessionContext.getCurrentSession();
            HashMap<String,Object> sh = (HashMap<String,Object>) session.getAttribute(SessionVariable.XZ_SESSION_VARS);

            if (sh == null) {
                sh = new HashMap<String,Object>();
                session.setAttribute(SessionVariable.XZ_SESSION_VARS,sh);
            }
            ApplicationContext context = AppContext.getApplicationContext();
            UserDao userDao = (UserDao) context.getBean("userDaoImpl");
            userId = userDao.getUserIdByName(loginName);
            if ((userId == null) || ("".equals(userId))) {
                throw new Exception("<result><flag>1</flag><msg>传入的用户名在平台中不存在，请确认。</msg></result>");
            }
            sh.put(SessionVariable.userId,userId);
            // 员工姓名
            String empName = userDao.getUserEmpNameById(userId);
            sh.put(SessionVariable.empName,empName);
    	}
    }
	
	/**
	 * @Title: getLang 
	 * @Description: 当前指定语言环境
	 * @return
	 * @throws Exception    设定文件
	 */
	@SuppressWarnings("unchecked")
	public static String getLang() throws Exception {
		String language = "zh" ;
		// session中获取
		HttpSession session = CurrentSessionVar.getSession() ;
		HashMap<String, Object> hashmap = (HashMap<String, Object>) session.getAttribute(SessionVariable.XZ_SESSION_VARS);
		if(hashmap != null && hashmap.size() >0){
			language = (String) hashmap.get(SessionVariable.language) ;
			// 如果session不存在，则从参数中获取
			if(language == null || "".equals(language)){
				language = PlatformUtil.getParamVal("XIP_DEFAULT_LANG", "P", "P");
				// 如果参数未设置，则使用默认值中文"zh"
				if(language == null || "".equals(language)){
					language = "zh" ;
				}
			}
		}
		return language ;
	}
}
