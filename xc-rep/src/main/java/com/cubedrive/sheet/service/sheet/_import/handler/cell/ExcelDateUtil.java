package com.cubedrive.sheet.service.sheet._import.handler.cell;

import java.util.HashMap;
import java.util.Map;


public class ExcelDateUtil {
	
	private static final Map<String,String> _dateFormats = new HashMap<>();
	private static final Map<Integer,String> _numFmts = new HashMap<>();
	
	static{
		_dateFormats.put("[$-F800]dddd\\,\\ mmmm\\ dd\\,\\ yyyy", "Y年m月d日"); //2015年5月14日
		_dateFormats.put("[DBNum1][$-804]yyyy\"年\"m\"月\"d\"日\";@", "Y年m月d日"); //二○一五年五月十四日
		_dateFormats.put("[DBNum1][$-804]yyyy\"年\"m\"月\";@", "Y年m月");//二○一五年五月
		_dateFormats.put("[DBNum1][$-804]m\"月\"d\"日\";@", "m月d日"); //五月十四日
		_dateFormats.put("yyyy\"年\"m\"月\"d\"日\";@", "Y年m月d日");//2015年5月14日
		_dateFormats.put("yyyy\"年\"m\"月\";@", "Y年m月");//2015年5月
		_dateFormats.put("m\"月\"d\"日\";@", "m月d日");//5月14日
		_dateFormats.put("[$-804]aaaa;@", "l"); //星期四
		_dateFormats.put("[$-804]aaa;@", "l");//四
		_dateFormats.put("yyyy/m/d;@","Y/m/d");//2015/5/14
		_dateFormats.put("[$-409]yyyy/m/d\\ h:mm\\ AM/PM;@","Y/m/d g:i A");//2015/5/14 12:00 AM
		_dateFormats.put("yyyy/m/d\\ h:mm;@", "Y/m/d H:i");//2015/5/14 0:00
		_dateFormats.put("yy/m/d;@", "y/m/d");//15/5/14
		_dateFormats.put("m/d;@","m/d");//5/14
		_dateFormats.put("m/d/yy;@","m/d/y"); //5/14/15
		_dateFormats.put("mm/dd/yy;@", "m/d/y");//05/14/15
		_dateFormats.put("[$-409]d\\-mmm;@", "d-M");//14-May
		_dateFormats.put("[$-409]d\\-mmm\\-yy;@", "d-M-y");//14-May-15
		_dateFormats.put("[$-409]dd\\-mmm\\-yy;@", "d-M-y");//14-May-15
		_dateFormats.put("[$-409]mmm\\-yy;@", "M-y");//May-15
		_dateFormats.put("[$-409]mmmm\\-yy;@", "F-y");//May-15
		_dateFormats.put("[$-409]mmmmm;@","M");//M
		_dateFormats.put("[$-409]mmmmm\\-yy;@", "M-y");//M-15
		
		_numFmts.put(58,  "m月d日");//5月14日
		
	}
	
	
	
	public static String getFormatByExcelDate(String formatString) {
		return _dateFormats.get(formatString);
	}
	
	public static String getFormatByNumFmt(int numFmtId){
		return _numFmts.get(numFmtId);
	}

}
