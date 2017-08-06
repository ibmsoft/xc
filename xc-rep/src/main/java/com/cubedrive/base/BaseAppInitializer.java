/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.base;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AgeFileFilter;

public class BaseAppInitializer implements ServletContextListener{
	
	private ScheduledExecutorService sweepTempFilesExecutor;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		AppContext.instance().init(sce.getServletContext());
		BaseAppConfiguration.instance().init();
		sweepTempFilesExecutor = Executors.newSingleThreadScheduledExecutor();
		sweepTempFilesExecutor.scheduleAtFixedRate(new SweepFilesTask(), 0, 30, TimeUnit.MINUTES);
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if(sweepTempFilesExecutor != null){
			sweepTempFilesExecutor.shutdownNow();
		}
		
	}
	
	
	private class SweepFilesTask implements Runnable {

		public void run() {
			File tempDir = BaseAppConfiguration.instance().getTempDir();
			Date oldestAllowedFileDate	 = new Date(System.currentTimeMillis() - 3 * 60 * 60 *1000L);
			Iterator<File> filesToDelete = FileUtils.iterateFiles(tempDir, new AgeFileFilter(oldestAllowedFileDate), null); 
	        //if deleting subdirs, replace null above with TrueFileFilter.INSTANCE
	        while (filesToDelete.hasNext()) { 
	            FileUtils.deleteQuietly(filesToDelete.next()); 
	        }  
		}
	}
	

}
