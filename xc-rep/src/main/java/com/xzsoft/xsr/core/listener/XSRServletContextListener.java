package com.xzsoft.xsr.core.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.xzsoft.xsr.core.init.XSRInitParameters;

/**
 * 报表系统监听器
 * @author ZhouSuRong
 * 2016-4-29
 */
public class XSRServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//初始化系统需要的参数
		XSRInitParameters.initParameters();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
