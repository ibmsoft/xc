package com.xzsoft.xsr.core.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
/**
 *
 * ClassName:XSRUtil 兴竹集中式报表系统 Function: 系统常用的公共变量
 *
 * @author 		XZTeam
 * @version 	Ver 1.0
 * @since 		Ver 1.0
 * @Date 		2015-12-14 上午08:28:32
 *
 */
public class XSRUtil {
	private static final Logger log = Logger.getLogger(XSRUtil.class.getName());

	/**
	 * getOS:(获取当前操作系统版本信息)
	 *
	 * @return			系统的版本信息
	 * @author 			XZTeam
	 * @version 		Ver 1.0
	 * @since 			Ver 1.0
	 */
	public static String getOS() {
		return System.getProperty("os.name").toLowerCase();
	}

	/**
	 * getSysEncoding:(获取当前应用服务器操作系统的字符集信息)
	 *
	 * @return 			字符集名称(Iso-8859-1,UTF-8,GBK)
	 * @author 			XZTeam
	 * @version 		Ver 1.0
	 * @since 			Ver 1.0
	 */
	public static String getSysEncoding() {
		String encoding = System.getProperty("file.encoding");
		// 判断字符串是否存在
		if (encoding.toUpperCase().contains("ISO")) {
			encoding = "iso-8859-1";
		} else if (encoding.toUpperCase().contains("UTF-8")) {
			encoding = "UTF-8";
		} else if (encoding.toUpperCase().contains("GB")) {
			encoding = "GBK";
		} else {
			// 扩展使用
		}
		return encoding;
	}

	/**
	 * encodingTrans:(将字符串进行解码和转码处理)
	 *
	 * @param oldStr			原字符串
	 * @param decode_encoding	解码格式
	 * @param trans_encoding	转码格式
	 * @return 					处理完毕的字符串
	 * @throws Exception
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since 					Ver 1.0
	 */
	public static String encodingTrans(String oldStr, String decode_encoding,
			String trans_encoding) throws Exception {
		String newStr = "";
		try {
			newStr = new String(oldStr.getBytes(decode_encoding),
					trans_encoding);
		} catch (Exception e) {
			log.error("字符转码处理出错！");
			throw e;
		}
		return newStr;
	}

	/**
	 * gbkToSysEncoding:(将GBK编码的字符串转换成符合系统的编码格式)
	 *
	 * @param oldStr			原字符串
	 * @return 					转换完毕的字符串
	 * @throws Exception
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since 					Ver 1.0
	 */
	public static String gbkToSysEncoding(String oldStr) throws Exception {
		String newStr = "";
		try {
			if ("GBK".equals(getSysEncoding())
					|| "UTF-8".equals(getSysEncoding())) {
				// 不做转码处理
				newStr = oldStr;
			} else if ("iso-8859-1".equals(getSysEncoding())) {
				newStr = new String(oldStr.getBytes("GBK"), getSysEncoding());
			} else {
				// 扩展使用
				newStr = oldStr;
			}
		} catch (Exception e) {
			log.error("字符转码处理出错！");
			throw e;
		}
		return newStr;
	}

	/**
	 * encodingTrans:(在已知解码格式的情况下，将字符串进行转码处理)
	 *
	 * @param oldStr			原字符处
	 * @param trans_encoding	转码格式
	 * @return					转码完毕的字符串
	 * @throws Exception
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since 					Ver 1.0
	 */
	public static String encodingTrans(String oldStr, String trans_encoding)
			throws Exception {
		String newStr = "";
		try {
			newStr = new String(oldStr.getBytes(), trans_encoding);
		} catch (Exception e) {
			log.error("已知解码格式时,字符转码处理出错！");
			throw e;
		}
		return newStr;
	}

	/**
	 * getOffsetMonth	:		(计算偏移月份期间)
	 *
	 * @param currentMonth		格式 ： '2012-01'
	 * @param n					如果n为正数,则向前偏移n个月份, 如果n为负数则向后偏移n个月份
	 * @return					偏移后的期间
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static String getOffsetMonth(String currentMonth, int n){
		String endStr = "";
		int endY = 0;
		int endM = 0;
		int startYear = Integer.parseInt(currentMonth.substring(0, 4));
	    int startMonth = 0;
	    if (currentMonth.substring(5, 6).equals("0")){
	      startMonth = Integer.parseInt(currentMonth.substring(6));
	    }else{
	      startMonth = Integer.parseInt(currentMonth.substring(5, 7));
	    }
	    endY = startYear - n / 12;
	    if(n < startMonth ){
	    	endM = startMonth - n;
	    	if(endM > 12){
	    		endY = startYear + (endM - ((endM-12) % 12)) /12  ;
	    		endM = endM % 12;
	    	}
	    }else if(n % 12 < startMonth){
	    	endM = startMonth - n % 12;
	    }else {
	    	endY = endY - 1;
	    	endM = startMonth - n % 12 + 12;
	    }

	    if(endM <10){
	    	endStr = ""+endY+"-"+"0"+endM;
	    }else{
	    	endStr = ""+endY+"-"+""+endM;
	    }
	  return endStr;
	}

	/**
	 * getIntervalDay:(根据所给的起始,终止时间来计算间隔天数)
	 *
	 * @param startDate			开始日期 , 格式 ： "2012-01-01"
	 * @param endDate			结束日期 , 格式 ： "2012-12-01"
	 * @return					间隔天数数
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	 public static int getIntervalDay(java.sql.Date startDate,java.sql.Date endDate) {
	    long startdate = startDate.getTime();
	    long enddate = endDate.getTime();
	    long interval = enddate - startdate;
	    int intervalday = (int)((long) interval / (1000 * 60 * 60 * 24));
	    return intervalday;
	 }

	/**
	 * getIntervalMonth:(根据所给的起始,终止时间来计算间隔月数)
	 *
	 * @param startDate			开始日期 , 格式 ： "2012-01"
	 * @param endDate			结束日期 , 格式 ： "2012-12"
	 * @return					间隔月数
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static int getIntervalMonth(String startDate, String endDate) {
		int startYear = Integer.parseInt(startDate.substring(0, 4));
		int startMonth = 0;
		if (startDate.substring(5, 6).equals("0")){
		  startMonth = Integer.parseInt(startDate.substring(6));
		}else{
		  startMonth = Integer.parseInt(startDate.substring(5, 7));
		}
		int endYear = Integer.parseInt(endDate.substring(0, 4));
		int endMonth = 0;
		if (endDate.substring(5, 6).equals("0"))
		  endMonth = Integer.parseInt(endDate.substring(6));
		endMonth = Integer.parseInt(endDate.substring(5, 7));
		int intervalMonth =
			(endYear * 12 + endMonth) - (startYear * 12 + startMonth);
		return intervalMonth;
	}

	/**
	 * getYearMonthDay:(获取当前的年、月、日)
	 *
	 * @return					当期的日期
	 * @author 					XZTeam
	 * @version	 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static String getYearMonthDay() {
		Calendar cl = Calendar.getInstance();
		String year = String.valueOf(cl.get(Calendar.YEAR));
		String month = String.valueOf(cl.get(Calendar.MONTH) + 1);
		String day = String.valueOf(cl.get(Calendar.DATE));
		month = month.length() > 1 ? month : "0" + month;
		day = day.length() > 1 ? day : "0" + day;
		return year + month + day;
	}

	/**
	 * getNextDate:(获取给定日期的下一天)
	 *
	 * @param date  			给定的日期
	 * @return 					获得的该日期的下一天
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static java.sql.Date getNextDate(java.sql.Date date) {
		if (date == null)
			return null;
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_MONTH, 1);
		Date next = ca.getTime();
		return XSRUtil.utilDate2SqlDate(next);
	}

	/**
	 * getLastDate:(获取给定日期的前一天)
	 *
	 * @param date 				给定的日期
	 * @return					获得的给定日期的前一天
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static java.sql.Date getLastDate(java.sql.Date date) {
		if (date == null)
			return null;
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.DAY_OF_MONTH, -1);
		Date next = ca.getTime();
		return XSRUtil.utilDate2SqlDate(next);
	}

	/**
	 * utilDate2SqlDate:(将java.util.date类型的时间转换成java.sql.date类型的时间)
	 *
	 * @param date				原java.util.Date类型的时间
	 * @return					转换完毕的java.sql.date类型的时间
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static java.sql.Date utilDate2SqlDate(java.util.Date date) {
		java.sql.Date result = null;
		if (null != date) {
			long time = date.getTime();
			result = new java.sql.Date(time);
		}
		return result;

	}

	/**
	 * getPreMonth:(获取输入"YYYYMM前n个月的YYYYMM")
	 *
	 * @param dateStr			输入的日期
	 * @param n					日期的前几个月
	 * @return					转换完毕的日期
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static String getPreMonth(String dateStr, int n) {

		String endStr = "";
		int year = Integer.parseInt(dateStr.substring(0, 4));
		int month = 0;
		String sMonth = "";

		if (Integer.parseInt(dateStr.substring(6, 8))<25 && dateStr.substring(4, 6).equals("01")) {
			year = year-1;
			month = 12;
		} else if(Integer.parseInt(dateStr.substring(6, 8))>=25){
			month = Integer.parseInt(dateStr.substring(4, 6));
		}else{
			month = Integer.parseInt(dateStr.substring(4, 6))-1;
		}
		if(month<10){
			sMonth = "0"+month;
		}else{
			sMonth = month+"";
		}

		endStr = year+sMonth+"";
		return endStr;
	}

	
	/**
	 * getSysDate:(得到当前日期(java.sql.Date类型), 注意：返回的时间只到秒)
	 *
	 * @return					当前日期
	 * @author 					XZTeam
	 * @version					Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static java.sql.Date getSysDate() {
		Calendar oneCalendar = Calendar.getInstance();
		int year = oneCalendar.get(Calendar.YEAR);
		int month = oneCalendar.get(Calendar.MONTH);
		int day = oneCalendar.get(Calendar.DATE);
		int hours = oneCalendar.get(Calendar.HOUR_OF_DAY);
		int minute = oneCalendar.get(Calendar.MINUTE);
		int second = oneCalendar.get(Calendar.SECOND);
		int weekday = oneCalendar.get(Calendar.DAY_OF_WEEK);
		oneCalendar.clear();
		oneCalendar.set(year, month, day, hours, minute, second);
		oneCalendar.set(Calendar.DAY_OF_WEEK, weekday);
		return new java.sql.Date(oneCalendar.getTime().getTime());
	}

	/**
	 * getPeriodCode:(得到当前月份前一个月的期间编码)
	 * @return					当前的日期
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static String getPeriodCode() {
		String s = XSRUtil.getPreMonth(XSRUtil.getYearMonthDay(), 1);
		String year = s.substring(0, 4); // 年份
		String month = s.substring(4, 6); // 月份
		String pcode = year + "-" + month; // 期间编码
		return pcode;
	}

	/**
	 * replaceStr:(替换Java去除字符串中的空格、回车、换行符、制表符)
	 *
	 * @param oldStr			原字符串
	 * @return					转换完毕的字符串
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static String replaceStr(String oldStr) {
		Pattern p = Pattern.compile("\\s+|\t|\r|\n");
		Matcher m = p.matcher(oldStr);
		return m.replaceAll("");
	}

	/**
	 * replaceTabEnter:(替换Java去除字符串中的回车、换行符、制表符 \t:制表符 \n:换行符 \r:回车符)
	 *
	 * @param oldStr			原字符串
	 * @return 					替换完毕的字符串
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static String replaceTabEnter(String oldStr) {
		Pattern p = Pattern.compile("\t|\r|\n");
		Matcher m = p.matcher(oldStr);
		return m.replaceAll("");
	}

	/**
	 * isNumber:(判断传入的字符串是否为自然数)
	 *
	 * @param oldStr			传入字符串
	 * @return					"Y" 或者 "N"
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static String isNumber(String oldStr) {
		if (oldStr != null && oldStr.trim().length() > 0) {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher isNum = pattern.matcher(oldStr.trim());
			if (isNum.matches()) {
				return "Y";
			} else {
				return "N";
			}
		} else {
			return "N";
		}
	}

	/**
	 * isFloat:(判断传入的字符串是否为浮点数)
	 *
	 * @param oldStr			传入的字符串
	 * @return					"Y" 或者 "N"
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static String isFloat(String oldStr) {
		if (oldStr != null && oldStr.trim().length() > 0) {
			Pattern p = Pattern.compile("(-|[0-9])?[0-9]*(\\.?)[0-9]+");
			Matcher m = p.matcher(oldStr);
			boolean b = m.matches();
			if (b) {
				return "Y";
			} else {
				return "N";
			}
		} else {
			return "N";
		}
	}

	/**
	 * getMinValue:(如果list中全是数值时,则可判断该数值序列中最小值)
	 *
	 * @param list				传入的集合
	 * @return					最小值
	 * @throws 					Exception
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static int getMinValue(List list) throws Exception {
		int min_value = 0;
		Integer temp = 0;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				temp = (Integer) list.get(i);
				temp = temp.intValue();
				if (min_value == 0) {
					min_value = temp;
				}
				if (min_value != 0 && min_value > temp) {
					min_value = temp;
				}
			}
		}
		return min_value;
	}

	/**
	 * getMaxValue:(如果list中全是数值时,则可判断该数值序列中最大值)
	 *
	 * @param list				传入的集合
	 * @return					最大值
	 * @throws 					Exception
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static int getMaxValue(List list) throws Exception {
		int max_value = 0;
		Integer temp = 0;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				temp = (Integer) list.get(i);
				temp = temp.intValue();
				if (max_value == 0) {
					max_value = temp;
				}
				if (max_value != 0 && max_value < temp) {
					max_value = temp;
				}
			}
		}
		return max_value;
	}

	/**
	 * delMutilModalId:(去除list中的重复项目)
	 *
	 * @param list				list集合
	 * @return					去除完毕的list集合
	 * @author 					XZTeam
	 * @version 				Ver 1.0
	 * @since   				Ver 1.0
	 */
	public static ArrayList delMutilModalId(ArrayList list) {
		for (int i = 0; i < list.size(); i++) {
			String temp = (String) list.get(i);
			for (int j = i + 1; j < list.size(); j++) {
				if (temp.equals(list.get(j)) || temp == "") {
					list.remove(temp);
					j--;
				}
			}
		}
		return list;
	}
	//同时匹配 \s 以及各种其他的空白字符（包括全角空格等）
	public static String replaceBlank(String str)
	  {
       Pattern p = Pattern.compile("[\\s\\p{Zs}]");
	   Matcher m = p.matcher(str);
	   String after = m.replaceAll("");
	   return after;
	}

	/* public static void main(String[] args){
	 }*/
}
