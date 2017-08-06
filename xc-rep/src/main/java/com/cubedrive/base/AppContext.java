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
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import com.cubedrive.base.domain.account.UserSetting.Lang;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class AppContext {
	
	private static final AppContext _appContext = new AppContext();
	
	private ServletContext _servletContext ;

	private EventBus _eventBus; 
	
	private ThreadPoolExecutor _threadPoolExecutor;
	
	private static String _spreadsheetCellTablePrefix;

	private static int _spreadsheetCellTableSize;

	private static File _homeDir;

	private static File _tempDir;

	private static String _userFileServer;

	private static String _mainUrl;

	private static String _defaultLang;
	
	
	private AppContext(){
	}
	
	void init(ServletContext servletContext){
		_eventBus = new EventBus();
		_servletContext = servletContext;
		setupThreadPoolExecutor();
		doInit();
	}
	
	void destroy(){
		shutdownThreadPoolExecutor();
	}
	
	private static void doInit() {

		// this is for server
		String sheet_home_dir = "/data/sheet_data_dir";
		String sheet_tmp_dir = "/data/sheet_data_dir/sheet_tempDir";
		
		// this is for local
		// String sheet_home_dir = "C:/sheet_dir";
		// String sheet_tmp_dir = "C:/sheet_tempDir";
		
		_homeDir = new File(sheet_home_dir);
		if (!_homeDir.exists()) _homeDir.mkdirs();
		_tempDir = new File(sheet_tmp_dir);
		if (! _tempDir.exists()) _tempDir.mkdirs();

		// a new cell table will be auto generated if its size > _spreadsheetCellTableSize
		// This is used to improve performance
		_spreadsheetCellTablePrefix = "sheet_cell_";  // do not change this 
		_spreadsheetCellTableSize = 1000000;

		// define default language: Lang.en.name() OR Lang.zh_CN.name()
		_defaultLang = Lang.en.name();
		
		// change this to match your server name, for example: www.enterpriseSheet.com/sheet
	    _mainUrl= "http://www.enterprisesheet.com/sheet";
		_userFileServer ="http://www.enterprisesheet.com/sheet/userFile";
//		_mainUrl= "http://localhost:8080/sheet";
//		_userFileServer ="http://localhost:8080/sheet/userFile";
	}
	
	private void setupThreadPoolExecutor(){
		ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("cubedrive-pool-%d")
			.setPriority(Thread.NORM_PRIORITY).build();
		
		int processorCount = Runtime.getRuntime().availableProcessors();
		_threadPoolExecutor = new ThreadPoolExecutor(processorCount,
				processorCount * 2, 5L, TimeUnit.MINUTES,
				new SynchronousQueue<Runnable>(), threadFactory,
				new CallerRunsPolicy());
	}
	
	
	private void shutdownThreadPoolExecutor(){
		if(_threadPoolExecutor != null){
			_threadPoolExecutor.shutdown();
			try{
				if(!_threadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)){
					_threadPoolExecutor.shutdownNow();
					if(! _threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS)){
						System.err.println("CubeDrive thread pool did not terminated");
					}
				}
			}catch(InterruptedException ie){
				_threadPoolExecutor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	
	
	public EventBus eventBus() {
		return _eventBus;
	}

	public ServletContext getServletContext() {
		return _servletContext;
	}
	
	public ThreadPoolExecutor getThreadPoolExecutor(){
		return _threadPoolExecutor;
	}
	
	public static String getSpreadsheetCellTablePrefix() {
		return _spreadsheetCellTablePrefix;
	}

	public static int getSpreadsheetCellTableSize() {
		return _spreadsheetCellTableSize;
	}

	public static File getHomeDir() {
		return _homeDir;
	}

	public static String getUserFileServer() {
		return _userFileServer;
	}

	public static String getMainUrl() {
		return _mainUrl;
	}

	public static String getDefaultLang() {
		return _defaultLang;
	}

	public static File getTempDir() {
		return _tempDir;
	}

	
	public static AppContext instance(){
		return _appContext;
	}
	
	
	

}
