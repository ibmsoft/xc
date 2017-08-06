package com.xzsoft.xsr.core.service.impl.repCheck;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.log4j.Logger;

/**
 * 自定义的一个UncaughtExceptionHandler
 */
public class CheckExceptionHandler implements UncaughtExceptionHandler {

	private static Logger log = Logger.getLogger(CheckExceptionHandler.class.getName());
	
	public CheckExceptionHandler() {}
	
	/**
	 * 这里可以做任何针对异常的处理,比如记录日志等等
	 */
	public void uncaughtException(Thread a, Throwable e) {
		log.error(e);
		CheckThread b = (CheckThread) a;
	    b.flag = true;
	    b.resultMsg = e.getMessage();
	}
}
