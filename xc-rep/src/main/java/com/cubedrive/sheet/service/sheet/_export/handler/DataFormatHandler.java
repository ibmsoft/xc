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
package com.cubedrive.sheet.service.sheet._export.handler;

import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper.DataType;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;
import com.cubedrive.sheet.service.sheet._export.dataformat.LocaleDate;
import com.cubedrive.sheet.service.sheet._export.dataformat.LocaleMoney;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;

public class DataFormatHandler {
    
    public static final short NO_FMT = -255;
    
    private static final DataFormatHandler _instance = new DataFormatHandler();
    
    
    private DataFormatHandler(){
        
    }

    public short getOrCreateDataFormat(XSSFCellExportContext xssfCellExportContext){
        short fmt = NO_FMT;
        String dataFormat = getDataFormat(xssfCellExportContext);
        if(dataFormat != null){
            fmt = doGetOrCreateDataFormat(xssfCellExportContext,dataFormat);
        }
        return fmt;
    }
    
    private String getDataFormat(XSSFCellExportContext xssfCellExportContext){
    	SheetCell sheetCell = xssfCellExportContext.sheetCell();
    	String data = null;
    	if(sheetCell.getCal()){
			data = sheetCell.getRawData();
    	}
    	
    	if(Strings.isNullOrEmpty(data)){
    	    Object dataObject = xssfCellExportContext.contentMap().get("data");
        	if(dataObject != null){
        	    data = dataObject.toString();
        	}
    	}
    	xssfCellExportContext.setData(SpreadsheetExportHelper.toJavaString(data));
    	
        DataType dataType = DataType.string;
        if(!Strings.isNullOrEmpty(data)){
        	if("#NULL!".equals(data) || "#DIV/0!".equals(data) 
        			|| "#VALUE!".equals(data) || "#REF!".equals(data)
        			|| "#NAME?".equals(data) || "#NUM!".equals(data)
        			|| "#N/A".equals(data)){
        		xssfCellExportContext.setDataType(DataType.error);
        		return null;
        	}else{
                if(!data.startsWith("0") && (Ints.tryParse(data) != null || Doubles.tryParse(data) != null)){
                    dataType= DataType.numeric;
                }
        	}
        }else{
        	xssfCellExportContext.setDataType(DataType.blank);
        	return null;
        }
        
        String fm = (String)xssfCellExportContext.contentMap().get("fm");
        if(!Strings.isNullOrEmpty(fm) && fm.startsWith("money")){
            fm = "money";
        }
        String dfm = (String)xssfCellExportContext.contentMap().get("dfm");
        String dataFormat = null;
        if(fm != null){
            switch(fm){
            	case "bool":
            		dataType = DataType.bool;
            		break;
                case "text": 
                    dataFormat = "@";
                    break;
                case "date": 
                    dataFormat = dateDataFormat(xssfCellExportContext);
                    dataType = DataType.date;
                    break;
                case "time":
                    dataFormat = timeDataFormat(xssfCellExportContext);
                    dataType = DataType.time;
                    break;
                case "datetime":
                    dataFormat = datetimeDataFormat(xssfCellExportContext);
                    dataType = DataType.datetime;
                    break;
                case "money":
                    dataFormat = moneyDataFormat(xssfCellExportContext);
                    dataType = DataType.numeric;
                    break;
                case "percent":
                    dataFormat = "0.00%";
                    dataType = DataType.numeric;
                    break;
                case "fraction":
                    dataFormat = "# ?/?";
                    dataType = DataType.numeric;
                    break;
                case "science":
                    dataFormat = "0.00E+00";
                    dataType = DataType.numeric;
                    break;
                case "comma":
                    dataFormat = "#,##0.00";
                    dataType = DataType.numeric;
                    break;
                case "regular":
                    dataFormat = "General";
                    break;
                case "number":
                	if("# ?/?".equals(dfm)){
            		    dataFormat = "# ?/?";
                	}else{
                		dataFormat = "0.00";
                    }
                    dataType = DataType.numeric;
                    break;
                	
            }
        }
        
        if(sheetCell.getCal()){
        	 dataType= DataType.formula;
        }
        xssfCellExportContext.setDataType(dataType);
        return dataFormat;
        
    }
    
    private String dateDataFormat(XSSFCellExportContext xssfCellExportContext){
        String dateFormat = (String) xssfCellExportContext.contentMap().get("dfm");
        String locale = xssfCellExportContext.sheetExportEnvironment().locale().toString();
        String dataFormatString = null;
        try{
            dataFormatString = LocaleDate.getInstance().getExcelDateFormat(locale, dateFormat);
        }catch(Exception ex){
            
        }
        if(dataFormatString == null){
//            dataFormatString = "yyyy\\\\-mm\\\\-dd;@";
            dataFormatString ="m/d/yyyy;@";
        }
        return dataFormatString;
    }
    
    private String timeDataFormat(XSSFCellExportContext xssfCellExportContext){
        String timeValue = xssfCellExportContext.sheetCell().getRawData();
        String dfm =  (String) xssfCellExportContext.contentMap().get("dfm");
        String format="h:mm;@";
        int firstColonPos = timeValue.indexOf(":"); 
        int secondColonPos = timeValue.indexOf(":", firstColonPos+1);
        if(firstColonPos != -1 && secondColonPos !=-1){
            format = "[h]:mm:ss;@";
        }
        if(firstColonPos!=-1 && dfm.endsWith("A")){
        	if(secondColonPos !=-1){
                format = "[$-409]h:mm:ss\\ AM/PM;@";
            }else{
                format ="[$-409]h:mm\\ AM/PM;@";
            }
        }
        return format;
    }
    
    
    private String datetimeDataFormat(XSSFCellExportContext xssfCellExportContext){
        String dateFormat = (String) xssfCellExportContext.contentMap().get("dfm");
        String locale = xssfCellExportContext.sheetExportEnvironment().locale().toString();
        String dataFormatString = null;
        try{
            dataFormatString = LocaleDate.getInstance().getExcelDatetimeFormat(locale, dateFormat);
        }catch(Exception ex){
            
        }
        if(dataFormatString == null){
            dataFormatString ="yyyy-mm-dd h:mm:ss;@";
        }
        return dataFormatString;
    }
    
    private String moneyDataFormat(XSSFCellExportContext xssfCellExportContext){
        String fm = (String)xssfCellExportContext.contentMap().get("fm");
        int firstSplitPos = fm.indexOf('|');
        int secondSplitPos = fm.indexOf('|', firstSplitPos+1);
        int thirdSplitPos = fm.indexOf('|',secondSplitPos+1); 
        String symbol = fm.substring(firstSplitPos+1, secondSplitPos);
        Integer scale = 0;
        boolean negative = false;
        String scaleString = fm.substring(secondSplitPos+1, thirdSplitPos);
        if(! Strings.isNullOrEmpty(scaleString)){
            scale = Ints.tryParse(scaleString);
        }
        String negativeString = fm.substring(thirdSplitPos+1);
        if(!Strings.isNullOrEmpty(negativeString) && negativeString.startsWith("negative")){
            negative = true;
        }
        String dataFormat = LocaleMoney.getCurrencyDataFormat(symbol,scale == null ? 0:scale.intValue(), negative);
        return dataFormat;
    }
    
    
    private Short doGetOrCreateDataFormat(XSSFCellExportContext cellContext, final String dataFormatString) {
        Map<String,Short> dataFormats = cellContext.parentContext().parentContext().dataFormats();
        Short dataFormatIndex =dataFormats.get(dataFormatString);
        if (dataFormatIndex == null) {
            XSSFWorkbook xssfWorkbook = cellContext.parentContext().parentContext().xssfWorkbook();
            XSSFDataFormat fmt = xssfWorkbook.createDataFormat();
            dataFormatIndex = fmt.getFormat(dataFormatString);
            dataFormats.put(dataFormatString, dataFormatIndex);
        }
        return dataFormatIndex;
    }
    
    
    public static DataFormatHandler instance(){
        return _instance;
    }
    
}
