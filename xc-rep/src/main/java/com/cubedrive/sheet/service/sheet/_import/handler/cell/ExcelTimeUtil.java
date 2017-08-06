package com.cubedrive.sheet.service.sheet._import.handler.cell;

import java.util.HashMap;
import java.util.Map;

public class ExcelTimeUtil {
	
	private static final Map<String,String> _timeFormats = new HashMap<>();
	
	static{
		_timeFormats.put("[$-F400]h:mm:ss\\ AM/PM", "g:i:s A");
		_timeFormats.put("h:mm;@", "H:i");
		_timeFormats.put("[$-409]h:mm\\ AM/PM;@", "g:i A");
		_timeFormats.put("h:mm:ss;@", "H:i:s");
		_timeFormats.put("[$-409]h:mm:ss\\ AM/PM;@", "g:i:s A");
		_timeFormats.put("h\"时\"mm\"分\";@", "H时i分");
		_timeFormats.put("h\"时\"mm\"分\"ss\"秒\";@", "H时i分s秒");
		_timeFormats.put("上午/下午h\"时\"mm\"分\";@", " Ag时i分");
		_timeFormats.put("上午/下午h\"时\"mm\"分\"ss\"秒\";@", "Ag时i分s秒 ");
		_timeFormats.put("[DBNum1][$-804]h\"时\"mm\"分\";@", "H时i分s");
		_timeFormats.put("[DBNum1][$-804]上午/下午h\"时\"mm\"分\";@", "Ag时i分");
	}
	
	public static String getFormatByExcelTime(String formatString){
		String format = _timeFormats.get(formatString);
		return format;
	}

}
