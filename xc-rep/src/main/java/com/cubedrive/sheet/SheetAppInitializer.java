package com.cubedrive.sheet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cubedrive.base.BaseAppConfiguration;

public class SheetAppInitializer implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		SheetAppConfiguration.instance().init(BaseAppConfiguration.instance());
		ApplicationContext springApplicationContext= WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		DataSource dataSource = springApplicationContext.getBean("dataSource", DataSource.class);
		SheetTableSequence.instance().init(dataSource);
	}
	

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		SheetTableSequence.instance().destroy();
		
	}
	
	
	
	

	

}
