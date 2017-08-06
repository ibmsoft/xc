package com.xzsoft.xc.fa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * ClassName:XcFaUtil
 * Function: 资产工具类
 *
 * @author   GuoXiuFeng
 * @version  Ver 1.0
 * @since    Ver 1.0
 * @Date	 2016	2016年8月26日		上午9:13:05
 *
 */
public class XcFaUtil {
	private static Logger log = Logger.getLogger(XcFaUtil.class);

	public static void main(String[] args) {
		//System.out.println(calcEndPeriod("2012-01-06".substring(0, 7), "", 12));
System.out.println(getMonths("2016-12","2016-12"));
	}

	/**
	 * getMonths:(计算两个期间之间的月份数)
	 *
	 * @param startPeriod 开始期间
	 * @param endPeriod 结束期间
	 * @return 月份数
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @since   Ver 1.0
	*/
	public static int getMonths(String startPeriod, String endPeriod) {
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(sdf.parse(startPeriod.concat("-01")));
			c2.setTime(sdf.parse(endPeriod.concat("-01")));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) * 12
				+ c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH) + 1;
	}

	/**
	 * calcEndPeriod:(根据开始期间和使用年限以及分摊规则计算停用期间)
	 *
	 * @param startPeriod 开始期间
	 * @param rule 分摊规则
	 * @param limit 使用期限单位是月
	 * @return 返回停用的期间
	 * @author GuoXiuFeng
	 * @version Ver 1.0
	 * @throws ParseException 日期格式解析异常
	 * @since   Ver 1.0
	*/
	public static String calcEndPeriod(String startPeriod, String rule,int limit) throws ParseException {
		String endPeriod = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar startCalendar = Calendar.getInstance();
		Date beginDate = sdf.parse(startPeriod.concat("-01"));
		startCalendar.setTime(beginDate);
		if ("DYXZBJT".equals(rule)) {
			startCalendar.add(Calendar.MONTH, limit);
		} else {
			startCalendar.add(Calendar.MONTH, limit - 1);
		}
		endPeriod = sdf.format(startCalendar.getTime()).substring(0, 7);
		return endPeriod;
	}
	public static double getYearSum(double deprYears){
		double sum = 0;
		int index =0;
		do{
			sum  = sum+(deprYears - index);
			index++;
		}while((deprYears - index)>0);
		return sum;
	}
}
