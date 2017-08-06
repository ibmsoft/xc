package com.xzsoft.xc.gl.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class XCGLThreadPool {

	private static ThreadPoolExecutor pool ;
	
	/**
	 * @Title: initExecutors 
	 * @Description: 初始化线程数
	 * @return
	 * @throws Exception
	 */
	public static ThreadPoolExecutor initExecutors() throws Exception {
		// 创建并初始化线程池
		pool = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
	    
		pool.prestartAllCoreThreads() ;
	    
	    return pool ;
	}
	
	/**
	 * @Title: addTask 
	 * @Description: 添加任务
	 * @throws Exception
	 */
	public static void addTask(Runnable command) throws Exception {
		if(pool != null){
			pool.execute(command);
		}
	}
}
