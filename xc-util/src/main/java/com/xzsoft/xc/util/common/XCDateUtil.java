package com.xzsoft.xc.util.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName: XCDateUtil 
 * @Description: 云ＥＲＰ日期处理类 
 * @author linp
 * @date 2016年8月17日 下午5:17:19 
 *
 */
public class XCDateUtil {
	
	// 日期：4位年2位月2位日格式
	public static final String Y_M_D = "yyyy-MM-dd" ;
	// 时间：按12小时制
	public static final String Y_M_D_12 = "yyyy-MM-dd hh:mm:ss" ;
	// 时间：按24小时制
	public static final String Y_M_D_24 = "yyyy-MM-dd HH:mm:ss" ;
	
	// 日期格式器
	public static final SimpleDateFormat ymd = new SimpleDateFormat(Y_M_D) ;
	// 时间格式器(12小时制)
	public static final SimpleDateFormat ymd12 = new SimpleDateFormat(Y_M_D_12) ;
	// 时间格式器(24小时制)
	public static final SimpleDateFormat ymd24 = new SimpleDateFormat(Y_M_D_24) ;

	// 将日期转换为字符串(年月日格式)
	public static String date2Str(Date date){
		return ymd.format(date) ;
	}
	// 将日期转换为字符串(12小时制格式)
	public static String date2StrBy12(Date date){
		return ymd12.format(date) ;
	}
	// 将日期转换为字符串(24小时制格式)
	public static String date2StrBy24(Date date){
		return ymd24.format(date) ;
	}
	// 将日期格式字符串转换为年月日格式
	public static Date str2Date(String dateStr) throws Exception {
		Date date = null ;
		try {
			date = ymd.parse(dateStr) ;
		} catch (Exception e) {
			throw e ;
		}
		return date ;
	}
	// 将日期格式字符串转换为12小时制日期
	public static Date str2DateBy12(String dateStr) throws Exception {
		Date date = null ;
		try {
			date = ymd12.parse(dateStr) ;
		} catch (Exception e) {
			throw e ;
		}
		return date ;
	}
	// 将日期格式字符串转换为24小时制日期
	public static Date str2DateBy24(String dateStr) throws Exception {
		Date date = null ;
		try {
			date = ymd24.parse(dateStr) ;
		} catch (Exception e) {
			throw e ;
		}
		return date ;
	}
	
	// 获取当前时间(24小时制)
	public static String getCurrentDateBy24() throws Exception {
		return ymd24.format(new Date()) ; 
	}
	
}
