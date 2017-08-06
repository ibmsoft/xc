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
package com.cubedrive.sheet.service.sheet._import.handler.cell;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import com.cubedrive.base.utils.DateTimeUtil;
import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;
import com.cubedrive.sheet.utils.MoneyUtil;

public class NumericCellValueProcessor {
	private static final ValueHandler[] valueHandlers;
	private static final DatetimeValueHandler datetimeValueHandler = new DatetimeValueHandler();
	private static final TimeValueHandler timeValueHandler = new TimeValueHandler();
	private static final DateValueHandler dateValueHandler = new DateValueHandler();
	private static final CurrencyValueHandler currencyValueHandler = new CurrencyValueHandler();
	private static final PercentValueHandler percentValueHandler = new PercentValueHandler();
	private static final FractionValueHandler fractionValueHandler = new FractionValueHandler();
	private static final DecimalValueHandler decimalValueHandler = new DecimalValueHandler();
	private static final CommaValueHandler commaValueHandler = new CommaValueHandler();
	private static final RegularValueHandler regularValueHandler = new RegularValueHandler();
	private static final TextValueHandler textValueHandler = new TextValueHandler();
	private static final DoubleValueHandler doubleValueHandler = new DoubleValueHandler();
	
	static {
		valueHandlers = new ValueHandler[]{
		    		datetimeValueHandler,
		    		timeValueHandler,
		    		dateValueHandler,
		    		currencyValueHandler,
		    		percentValueHandler,
		    		fractionValueHandler,
		    		decimalValueHandler,
		    		commaValueHandler,
		    		regularValueHandler,
		    		textValueHandler,
		    		doubleValueHandler
//		            new FallbackValueHandler()
		};
	}

   
    
    
    private static class MySimpleDateFormat {
        private final SimpleDateFormat _dateFormat;
        private final String _dfm;

        private MySimpleDateFormat(SimpleDateFormat dateFormat, String dfm) {
            _dateFormat = dateFormat;
            _dfm = dfm;
        }
    }

    private static final Map<Locale, MySimpleDateFormat[]> localeDateFormats = new HashMap<>();

    private static MySimpleDateFormat[] createByLocale(Locale locale) {
        MySimpleDateFormat[] dateFormats = new MySimpleDateFormat[]{
                new MySimpleDateFormat(new SimpleDateFormat("yyyy-MM-dd", locale), "Y-m-d"),
                new MySimpleDateFormat(new SimpleDateFormat("dd-MM-yyyy", locale), "d-m-Y"),
                new MySimpleDateFormat(new SimpleDateFormat("MMMMM dd, yyyy", locale), "F d, Y"),
                new MySimpleDateFormat(new SimpleDateFormat("MMM dd,yyyy", locale), "M d, Y"),
                new MySimpleDateFormat(new SimpleDateFormat("MM/dd/yyyy", locale), "m/d/Y"),
                new MySimpleDateFormat(new SimpleDateFormat("yy-MM-dd", locale), "y-m-d"),
                new MySimpleDateFormat(new SimpleDateFormat("MM/dd/yy", locale), "m/d/y"),
                new MySimpleDateFormat(new SimpleDateFormat("dd-MM-yy", locale), "d-m-y"),
                new MySimpleDateFormat(new SimpleDateFormat("dd-MMM-yy", locale), "d-M-y"),
                new MySimpleDateFormat(new SimpleDateFormat("EEEEE, MMM dd, yyyy", locale), "l, M d, Y"),
        };
        return dateFormats;
    }

    private static MySimpleDateFormat[] getByLocale(Locale locale) {
        MySimpleDateFormat[] dateFormats = localeDateFormats.get(locale);
        if (dateFormats == null) {
            dateFormats = createByLocale(locale);
            localeDateFormats.put(locale, dateFormats);
        }
        return dateFormats;
    }

    private final Number _cellValue;

    private final CellImportContext _cellImportContext;

    private Locale _locale;

    private int _dataFormatIdx;

    private String _dataFormatString;

    private String _formatValue;

    private String _cellValueString;
    
    private boolean _date1904;


    private NumericCellValueProcessor(Number cellValue, CellImportContext cellImportContext) {
        _cellValue = cellValue;
        _cellImportContext = cellImportContext;

    }
    
    private static ValueHandler valueHandlerByName(String name){
    	ValueHandler valueHandler;
    	switch(name){
	    	case "datetime": valueHandler = datetimeValueHandler;break;
	    	case "time": valueHandler = timeValueHandler;break;
	    	case "date": valueHandler = dateValueHandler;break;
	    	case "currency": valueHandler = currencyValueHandler;break;
	    	case "percent": valueHandler = percentValueHandler; break;
	    	case "fraction": valueHandler= fractionValueHandler;break;
	    	case "decimal": valueHandler = decimalValueHandler;break;
	    	case "comma":valueHandler = commaValueHandler;break;
	    	case "regular":valueHandler = regularValueHandler;break;
	    	case "text": valueHandler = textValueHandler;break;
	    	case "double":valueHandler = doubleValueHandler;break;
	    	default:
	    		 throw new IllegalStateException("No ValueHandler for the name:"+name);
    	}
    	return valueHandler;
    }

    public Map<String, Object> getCellValue() {
    	WorkbookImportContext workbookImportContext = _cellImportContext.workbookImportContext();
    	String handlerName = workbookImportContext.getCacheNumericValueHandler(_dataFormatIdx);
    	Map<String, Object> result;
    	if(handlerName !=null){
    		ValueHandler valueHandler = valueHandlerByName(handlerName);
    		result = valueHandler.handle(this);
    		return result;
    	}else{
	        for (ValueHandler valueHandler : valueHandlers) {
	            try{
	                result = valueHandler.handle(this);
	                if (result != null){
	                	workbookImportContext.cachedNumericValueHandler(_dataFormatIdx, valueHandler.getName());
	                    return result;
	                }
	            }catch(Exception ex){
	                ex.printStackTrace();
	                continue;
	            }
	        }
	    	
	        throw new IllegalStateException(
	                String.format("No ValueHandler for this numeric cell(%s),formatValue=%s,dataFormatString:%s", 
	                _cellImportContext.cellReference().formatAsString(),_formatValue , _dataFormatString));
    	}

    }


    private static interface ValueHandler {
        Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat);
        String getName();
    }

    private static class RegularValueHandler implements ValueHandler {
        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
            if("General".equals(numericCellValueFormat._dataFormatString)){
                Map<String, Object> result = new HashMap<>();
                result.put("data", numericCellValueFormat._formatValue);
                result.put("fm", "regular");
                result.put("dfm", null);
                return result;
            }
            return null;
        }
        
        public String getName(){
        	return "regular";
        }
    }
    
    
    private static class TextValueHandler implements ValueHandler{
        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
            if(numericCellValueFormat._dataFormatString.startsWith("@")){
                Map<String, Object> result = new HashMap<>();
                result.put("data", numericCellValueFormat._formatValue);
                result.put("fm", "text");
                result.put("dfm", null);
                return result;
            }
            
            return null;
        }
        
        public String getName(){
        	return "text";
        }
    }

    private static class DoubleValueHandler implements ValueHandler {

        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
            try {
//                Double doubleFormatValue = Double.parseDouble(numericCellValueFormat._formatValue);
//                Double doubleCellValue = numericCellValueFormat._cellValue.doubleValue();
//                if (doubleCellValue - doubleFormatValue == 0) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("data", String.valueOf(numericCellValueFormat._cellValue.doubleValue()));
                    result.put("fm", "number");
                    result.put("dfm", null);
                    return result;
//                }
            } catch (NumberFormatException ex) {

            }
            return null;
        }
        
        public String getName(){
        	return "double";
        }
    }

    private static class CommaValueHandler implements ValueHandler {

        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
            if (numericCellValueFormat._formatValue.indexOf(',') != -1) {
                String uncommaFormatValue = numericCellValueFormat._formatValue.replaceAll(",", "");
                try {
                    Double doubleFormatValue = Double.parseDouble(uncommaFormatValue);
                    Double doubleCellValue = numericCellValueFormat._cellValue.doubleValue();
                    if (doubleCellValue - doubleFormatValue == 0) {
                        Map<String, Object> result = new HashMap<>();
                        result.put("data", numericCellValueFormat._cellValueString);
                        result.put("fm", "comma");
                        return result;
                    }
                } catch (NumberFormatException ex) {

                }
            }
            return null;
        }
        
        public String getName(){
        	return "comma";
        }
    }

    private static class DateValueHandler implements ValueHandler {

        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
        	String dfm = null; 
        	if(numericCellValueFormat._dataFormatString != null){
        		dfm = ExcelDateUtil.getFormatByExcelDate(numericCellValueFormat._dataFormatString);
	            if (dfm == null && DateUtil.isADateFormat(numericCellValueFormat._dataFormatIdx, numericCellValueFormat._dataFormatString)) {
	                MySimpleDateFormat[] dateFormats = getByLocale(numericCellValueFormat._locale);
	                for (MySimpleDateFormat dateFormat : dateFormats) {
	                    try {
	                        dateFormat._dateFormat.parse(numericCellValueFormat._formatValue);
	                        dfm = dateFormat._dfm;
	                        break;
	                    } catch (ParseException e) {
	                        continue;
	                    }
	                }
	                if(dfm == null){
	                	dfm = "Y-m-d";
	                }
	            }
        	}else{
        		dfm = ExcelDateUtil.getFormatByNumFmt(numericCellValueFormat._dataFormatIdx);
        	}
            
            if(dfm != null){
            	 Date javaDate = DateUtil.getJavaDate(numericCellValueFormat._cellValue.doubleValue(),numericCellValueFormat._date1904);
                 String data = DateTimeUtil.formatAsPattern(javaDate, "yyyy-MM-dd");
                 Map<String, Object> result = new HashMap<>();
                 result.put("data", data);
                 result.put("fm", "date");
                 result.put("dfm", dfm);
                 result.put("rawData", numericCellValueFormat._formatValue);
                 return result;
            }else{
            	return null;
            }
        }
        
        public String getName(){
        	return "date";
        }
    }


    private static class TimeValueHandler implements ValueHandler {
        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
        	String dfm = ExcelTimeUtil.getFormatByExcelTime(numericCellValueFormat._dataFormatString);
        	if (dfm == null && numericCellValueFormat._dataFormatString.indexOf("h") != -1 &&
                    numericCellValueFormat._dataFormatString.indexOf("mm") != -1) {
                boolean containSecond =  numericCellValueFormat._formatValue.contains("ss");
                boolean containAM = numericCellValueFormat._dataFormatString.contains("AM/PM");
                if(!containSecond){
                    if(containAM){
                        dfm = "g:i A";
                    }else{
                        dfm = "H:i";
                    }
                }else{
                    if(containAM){
                        dfm = "g:i:s A";
                    }else{
                        dfm = "H:i:s";
                    }
                }
                
            }
        	
        	if(dfm != null){
        		Calendar c = DateUtil.getJavaCalendar(numericCellValueFormat._cellValue.doubleValue(),numericCellValueFormat._date1904);
                String timeValue = c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
                
                Map<String, Object> result = new HashMap<>();
                result.put("data", timeValue);
                result.put("fm", "time");
                result.put("dfm", dfm);
                result.put("rawData", timeValue);
                return result;
        	}else{
        		 return null;
        	}
        }
        
        public String getName(){
        	return "time";
        }
    }
    
    
    private static class DatetimeValueHandler implements ValueHandler{
        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
            if (numericCellValueFormat._dataFormatString.indexOf("y") != -1 &&
                    numericCellValueFormat._dataFormatString.indexOf("h") != -1) {
                int lastSpacePos = numericCellValueFormat._formatValue.lastIndexOf(" ");
                String datePart =  numericCellValueFormat._formatValue.substring(0,lastSpacePos);
               
                MySimpleDateFormat[] dateFormats = getByLocale(numericCellValueFormat._locale);
                String dfm =null;
                String dateData = null;
                for (MySimpleDateFormat dateFormat : dateFormats) {
                    try {
                        Date date = dateFormat._dateFormat.parse(datePart);
                        dateData = dateFormats[0]._dateFormat.format(date);
                        dfm =  dateFormat._dfm;
                        break;
                    } catch (ParseException e) {
                        continue;
                    }
                }
                String tfm = null;
                String timeData = null;
                if(dfm!=null && dateData!=null){
                    String timePart =  numericCellValueFormat._formatValue.substring(lastSpacePos+1);
                    int leftColonPos =  timePart.indexOf(":");
                    int rightColonPos = timePart.lastIndexOf(":");
                    boolean containSecond = false;
                    boolean containAM = numericCellValueFormat._dataFormatString.contains("AM/PM");
                    
                    if(leftColonPos == rightColonPos){
                        timeData = timePart.substring(0,leftColonPos+3);
                    }else{
                        timeData = timePart.substring(0,rightColonPos+3);
                        containSecond = true;
                    }
                    if(!containSecond){
                        if(containAM){
                            tfm = "g:i A";
                        }else{
                            tfm = "H:i";
                        }
                    }else{
                        if(containAM){
                            tfm = "g:i:s A";
                        }else{
                            tfm = "H:i:s";
                        }
                    }
                
                }
                if(dfm != null && tfm !=null){
                    String dtfm = dfm +" "+tfm;
                    Map<String, Object> result = new HashMap<>();
                    result.put("fm", "datetime");
                    result.put("value", dateData +" "+timeData);
                    result.put("dfm", dtfm);
                    result.put("tfm", tfm);
                    result.put("dtfm", dtfm);
                    return result;
                }
            }
            return null;
        }
        
        public String getName(){
        	return "datetime";
        }
    }


    private static class PercentValueHandler implements ValueHandler {
        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
            if (numericCellValueFormat._formatValue.indexOf("%") != -1 &&
                    numericCellValueFormat._dataFormatString.indexOf("%") != -1) {
                BigDecimal d = new BigDecimal(numericCellValueFormat._formatValue.trim().replace("%", ""))
                        .divide(BigDecimal.valueOf(100));
                Map<String, Object> result = new HashMap<>();
                result.put("data", d.toPlainString());
                result.put("fm", "percent");
                return result;
            }
            return null;
        }

        public String getName(){
        	return "percent";
        }
    }


    private static class CurrencyValueHandler implements ValueHandler {
        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
        	
        	// Read result from: _dataFormatString
        	String dataFmString = numericCellValueFormat._dataFormatString.trim();
        	String symbol = MoneyUtil.getSymbol(dataFmString,numericCellValueFormat._formatValue);
        	     	
            // can be something like this: 18,000.00 kr, 18,000.00 �
            //mif (symbol == null) symbol = trimedFormatValue.substring(0, 1);
            // REST can be wrong too ...
        	     	
            if(symbol != null){
                String rest = MoneyUtil.getRestWithoutSymbol(numericCellValueFormat._formatValue);
                int dotPos = rest.lastIndexOf(".");
                int scale = 0;
                if(dotPos > 0){
                    scale = rest.length() - (dotPos+1);
                }
                
                String fm = null;
                if (symbol != null) {
                	fm = "money|" + symbol + "|%d|none";
                }
                
                if (fm != null) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("data", numericCellValueFormat._cellValueString);
                    result.put("fm", String.format(fm,scale));
                    result.put("dfm", null);
                    return result;
                }
            }
            return null;
        }
        
        public String getName(){
        	return "currency";
        }
    }

    private static class DecimalValueHandler implements ValueHandler {
        
        private static boolean isDecimalFormat(String dataFormatString){
            for(int i=0; i<dataFormatString.length();i++){
                char c = dataFormatString.charAt(i);
                if(c == '0' || c == '.' || c=='E' || c=='+' || c=='#' || c==',')
                    continue;
                else
                    return false;
            }
            return true;
        }
        
        private static String getDecimalFormat(String dataFormatString){
            StringBuilder buf = new StringBuilder();
            int firstZeroPos = dataFormatString.indexOf("0");
            if(firstZeroPos==0){
                for(int i=0; i<dataFormatString.length();i++){
                    char c = dataFormatString.charAt(i);
                    if(c == '0')
                        buf.append(c);
                    else if(c =='.')
                        buf.append(c);
                    else
                        break;
                }
            }
            return buf.toString();
        }
        
        
        @Override
        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
            if(isDecimalFormat(numericCellValueFormat._dataFormatString)){
                Map<String, Object> result = new HashMap<>();
                result.put("data", new BigDecimal(numericCellValueFormat._cellValueString).toPlainString());
                int sciencePos = numericCellValueFormat._dataFormatString.indexOf("E");
                if(sciencePos>1){
                    result.put("fm", "science");
                }else if(numericCellValueFormat._formatValue.indexOf(",")!=-1){
                    result.put("fm", "comma");
                }
                String decimalFormat = getDecimalFormat(numericCellValueFormat._dataFormatString);
                int dotPos = decimalFormat.indexOf(".");
                if(dotPos > 0){
                    result.put("dpd", decimalFormat.length()- (dotPos+1));
                }else{
                    result.put("dpd", 0);
                }
                return result;
            }
            return null;
        }
        
        public String getName(){
        	return "decimal";
        }
    }
    
    private static class FractionValueHandler implements ValueHandler {

        public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
            if(numericCellValueFormat._formatValue.indexOf("/")!=-1 && 
                    numericCellValueFormat._dataFormatString.contains("?/?")){
                Map<String, Object> result = new HashMap<>();
                result.put("data", numericCellValueFormat._cellValueString);
                result.put("fm", "fraction");
                result.put("dfm", null);
                return result;
            }
            
            return null;
        }
        
        public String getName(){
        	return "fraction";
        }
        
    }
    
//    private static class FallbackValueHandler implements ValueHandler {
//
//    	 @Override
//         public Map<String, Object> handle(NumericCellValueProcessor numericCellValueFormat) {
//             Map<String, Object> result = new HashMap<>();
//             result.put("data", numericCellValueFormat._formatValue);
//             return result;
//         }
//    	
//    }

    public static NumericCellValueProcessor build(Number cellValue, CellImportContext cellImportContext) {
        XSSFCellStyle xssfCellStyle = cellImportContext.getCellStyle();
        boolean date1904 = cellImportContext.parentContext().parentContext().parentContext().isDate1904();
        short dataFormatIdx = xssfCellStyle.getDataFormat();
        String dataFormatString = xssfCellStyle.getDataFormatString();
        String formatValue;
        try{
        	formatValue = cellImportContext.getDataFormatter().formatRawCellContents(cellValue.doubleValue(), dataFormatIdx, dataFormatString,date1904);
        }catch(Exception ex){
        	formatValue = null;
        }
        NumericCellValueProcessor valueProcessor = new NumericCellValueProcessor(cellValue, cellImportContext);
        valueProcessor._dataFormatIdx = dataFormatIdx;
        valueProcessor._dataFormatString = dataFormatString;
        valueProcessor._locale = cellImportContext.getLocale();
        valueProcessor._formatValue = formatValue;
        valueProcessor._cellValueString = cellValue.toString();
        valueProcessor._date1904 = date1904;
        return valueProcessor;
    }


}
