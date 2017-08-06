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
package com.cubedrive.sheet.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MoneyUtil {
	
    private static final Map<String,String> _moneyFmMap= excelMoneyFmMap();
    
    private static final String[] _moneyArray = new String[]{"£","Kr","\u20b9","\u20ac","\u00a5","$"};
    
	private static Map<String, String> excelMoneyFmMap() {
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("[$$-409]", "$");  // USD
		maps.put("\"$\"", "$");  // USD
		maps.put("[$¥-804]", "\u00a5");  // RMB
		maps.put("[$¥-411]", "\u00a5");  // JPY
		maps.put("[$\u20AC-", "\u20ac");  // EUR - can be [$€-1] and [$€-2]
		maps.put("[$£-809]", "£");  // England pound
		maps.put("[$\u20b9-4009]", "\u20b9");  // Rupee
		maps.put("[$$-1009]", "C$");  // Canadian Dollar
		maps.put("[$kr-41D]", "Kr");  // Swede - SEK
		maps.put("[$$-C09]", "A$");  // AUD
		maps.put("[$₩-412]", "₩");  // Korean
		maps.put("[$$-1409]", "$");  // New Zealand
		maps.put("[$zł-415]", "zł");  // Poland
		maps.put("[$$-2C0A]", "$");  // Argentina
		maps.put("[$₽-419]", "₽");  // Russian
		maps.put("[$₺-41F]", "₺");  // Turkey 
		maps.put("[$Rp-421]", "Rp");  // Indonesia 
		maps.put("[$R-1C09]", "R");  // South Africa 
		
		return maps;
	}
	

	/**
	 * USD: _-"$"* #,##0.00_-;\-"$"* #,##0.00_-;_-"$"* "-"??_-;_-@_-   
	 * RMB: _ [$¥-804]* #,##0.00_ ;_ [$¥-804]* \-#,##0.00_ ;_ [$¥-804]* "-"??_ ;_ @_   
	 * GBP: _-[$£-809]* #,##0.00_-;\-[$£-809]* #,##0.00_-;_-[$£-809]* "-"??_-;_-@_- 
	 * EUR: _-[$€-2]\ * #,##0.00_-;\-[$€-2]\ * #,##0.00_-;_-[$€-2]\ * "-"??_-;_-@_-   OR    $\u20AC-2
	 * CAD: -[$$-1009]* #,##0.00_-;\-[$$-1009]* #,##0.00_-;_-[$$-1009]* "-"??_-;_-@_-
	 * RUP: [$₹-4009]\ #,##0.00
	 * SEK: #,##0.00\ [$kr-41D]
	 * AUD: _-[$$-C09]* #,##0.00_-;\-[$$-C09]* #,##0.00_-;_-[$$-C09]* "-"??_-;_-@_-
	 * Korean: _-[$₩-412]* #,##0.00_-;\-[$₩-412]* #,##0.00_-;_-[$₩-412]* "-"??_-;_-@_-
	 * New Zealand: _-[$$-1409]* #,##0.00_-;\-[$$-1409]* #,##0.00_-;_-[$$-1409]* "-"??_-;_-@_-
	 * Poland: _-* #,##0.00\ [$zł-415]_-;\-* #,##0.00\ [$zł-415]_-;_-* "-"??\ [$zł-415]_-;_-@_-
	 * Argentina: _ [$$-2C0A]\ * #,##0.00_ ;_ [$$-2C0A]\ * \-#,##0.00_ ;_ [$$-2C0A]\ * "-"??_ ;_ @_
	 * Russian: _-* #,##0.00\ [$₽-419]_-;\-* #,##0.00\ [$₽-419]_-;_-* "-"??\ [$₽-419]_-;_-@_-
	 * Turkey: #,##0.00\ [$₺-41F]
	 * Indonesia: [$Rp-421]#,##0.00
	 * South Africa: _ [$R-1C09]\ * #,##0.00_ ;_ [$R-1C09]\ * \-#,##0.00_ ;_ [$R-1C09]\ * "-"??_ ;_ @_
	 * 
	 * @param dataFm
	 * @return
	 */
	public static String getSymbol(String dataFm,String dataValue) {
		Map<String, String> maps = _moneyFmMap;
		Set<String> keys = maps.keySet();
		String symbol = null;
		for(String key : keys) {
			if (dataFm.contains(key)) {
			    symbol= maps.get(key);
			    break;
			}
		}
		if(symbol == null){
		    for(String _money:_moneyArray){
		        if(dataValue.indexOf(_money)!=-1){
		            symbol = _money;
		            break;
			}
		}
		}
		return symbol;
	}

	public static String getRestWithoutSymbol(String dataFormatValue){
	    StringBuilder buf = new StringBuilder();
	    for(int i=0; i<dataFormatValue.length(); i++){
	        char c = dataFormatValue.charAt(i);
	        if(c == 46) // .
	            buf.append(c);
	        else if (c >=48 && c<= 57) // 0-9
	            buf.append(c);
	        else if (c == 45) // -
	            buf.append(c);
	    }
	    return buf.toString();
	}
}
