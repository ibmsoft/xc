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
package com.cubedrive.sheet.service.sheet._export.handler.cell;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.common.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cubedrive.base.utils.DateTimeUtil;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper.DataType;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.handler.CellStyleHandler;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;


public class DataCellProcessor extends CellProcessor {

    private DataCellProcessor(XSSFCellExportContext xssfCellExportContext) {
        super(xssfCellExportContext);
    }

    @Override
    public void process() {
        registerTable();
    	setCellStyle();
    	boolean ignored = isContentIgnored();
    	if(! ignored){
	    	setCellData();
	    	setCellHyperlink();
	    	setCellComment();
    	}

    }
    
    private boolean isContentIgnored(){
        XSSFWorksheetExportContext xssfWorksheetExportContext = _xssfCellExportContext.parentContext();
        boolean ignore = false;
        if(xssfWorksheetExportContext.getMergeProcessor() != null){
            ignore = xssfWorksheetExportContext.getMergeProcessor().isIgnorableCell(_xssfCellExportContext.sheetCell());
        }
        return ignore;
    }
    

	private void setCellStyle(){
    	SXSSFCell sxssfCell = _xssfCellExportContext.getExcelCell();
    	XSSFCellStyle cellStyle = CellStyleHandler.instance().getOrCreateCellStyle(_xssfCellExportContext);
    	if(cellStyle != null){
    		sxssfCell.setCellStyle(cellStyle);
    	}else if(_xssfCellExportContext.parentContext().isSheetNeedProtection()){
    		sxssfCell.setCellStyle(_xssfCellExportContext.parentContext().parentContext().getNonProtectionStyle());
    	}
    	
    }
    
    private void setCellData(){
    	SXSSFCell sxssfCell = _xssfCellExportContext.getExcelCell();
    	DataType dataType = _xssfCellExportContext.getDataType();
        String data = _xssfCellExportContext.getData();
        Map<String,Object> contentMap = _xssfCellExportContext.contentMap();
        String tableColumn =(String) _xssfCellExportContext.getVariable("tableColumn");
        if(tableColumn != null){
            dataType = DataType.string;
            data = tableColumn;
        }
        try{
	    	switch(dataType){
	    		case formula:
	    		    sxssfCell.setCellType(Cell.CELL_TYPE_FORMULA);
	    		   
	    			Object fv = _xssfCellExportContext.getFormulaValue();
	    			if(fv != null){
	    				if(fv instanceof Number){
	    					sxssfCell.setCellValue(((Number)fv).doubleValue());
	    				}else{
	    					String fvString = fv.toString();
	    					Double fvDouble = Doubles.tryParse(fvString);
	    					if(fvDouble != null){  // if value is numeric, we cached
	    						sxssfCell.setCellValue(fvDouble);
	    					}else{   // else we miss it.It helps we import.
	    						sxssfCell.setCellValue("");
	    					}
	    				}
	    			}
	    			sxssfCell.setCellFormula(SpreadsheetExportHelper.filterFormulaIllegalCharacter(data.substring(1)));
	    			Integer afrow = (Integer)contentMap.get("afrow");
	    			Integer aerow = (Integer)contentMap.get("aerow");
	    			Integer afcol = (Integer)contentMap.get("afcol");
	    			Integer aecol = (Integer)contentMap.get("aecol");
	    			if(afrow != null &&  afcol != null ){
	    				Map<String,String> attributes = new HashMap<>();
	    				attributes.put("ca", "1");
	    				if(aerow != null && aecol != null){
		    				int rowIndex= _xssfCellExportContext.sheetCell().getX();
		    				int columnIndex = _xssfCellExportContext.sheetCell().getY();
		    				attributes.put("aca", "1");
		    				attributes.put("ref", CellReference.convertNumToColString(columnIndex-1)+rowIndex+":"+CellReference.convertNumToColString(columnIndex+aecol-1)+(rowIndex+aerow));
		    				attributes.put("t", "array");
	    				}else{
	    					sxssfCell.setCellFormula("");
	    				}
	    				sxssfCell.getFormulaValue().setAttributes(attributes);
	    			}
	    			break;
	    		case bool:
	    		    sxssfCell.setCellType(Cell.CELL_TYPE_BOOLEAN);
	    			sxssfCell.setCellValue(Boolean.valueOf(data.toLowerCase()));
	    			break;
	    		case string:
	    		    sxssfCell.setCellType(Cell.CELL_TYPE_STRING);
	    			sxssfCell.setCellValue(data);
	    			break;
	    		case numeric:
	    		    sxssfCell.setCellType(Cell.CELL_TYPE_NUMERIC);
	    			sxssfCell.setCellValue(Double.parseDouble(data));
	    			break;
	    		case date:
	    			Date javaDate = DateTimeUtil.parseAsYYYYMMdd(data);
	    			sxssfCell.setCellValue(javaDate);
	    			break;
	    		case time:
	    			double timeValue = convertTimeAsDouble(data);
	    	        sxssfCell.setCellValue(timeValue);
	    	        break;
	    		case datetime:
	    		    Date javaDatetime = DateTimeUtil.parseAsYYYYMMddHHmmss(data);
	    		    sxssfCell.setCellValue(javaDatetime);
                    break;
	    		case error:
	    		    sxssfCell.setCellType(Cell.CELL_TYPE_ERROR);
				    sxssfCell.setCellValue(data);
				    break;
	    		case blank:
	    			sxssfCell.setCellType(Cell.CELL_TYPE_BLANK);
	    			break;
	    	}
        }catch(Exception ex){
        	sxssfCell.setCellType(Cell.CELL_TYPE_STRING);
 			sxssfCell.setCellValue(_xssfCellExportContext.sheetCell().getRawData());
        }
    }
    
    private void setCellHyperlink(){
    	Map<String,Object> contentMap = _xssfCellExportContext.contentMap();
    	if(contentMap.containsKey("link")){	
    		String linkUrl = (String)contentMap.get("link");
    		SXSSFCell excelCell = _xssfCellExportContext.getExcelCell();
            XSSFWorkbook xssfWorkbook = _xssfCellExportContext.parentContext().parentContext().xssfWorkbook();
            CreationHelper creationHelper = xssfWorkbook.getCreationHelper();
            int linkType = SpreadsheetExportHelper.checkURLType(linkUrl);
            if(linkType != -1){
            	XSSFHyperlink link = (XSSFHyperlink)creationHelper.createHyperlink(linkType);
            	if(linkType == Hyperlink.LINK_DOCUMENT){
            		link.setLocation(linkUrl);
            	}else{
            		link.setAddress(linkUrl);
            	}
	            excelCell.setHyperlink(link);
	            excelCell.setCellStyle(_xssfCellExportContext.parentContext().parentContext().getHyperlinkStyle());
            }
    	}
    	
    }
    
    private void setCellComment(){
    	String commentString = (String)_xssfCellExportContext.contentMap().get("comment");
    	if(! Strings.isNullOrEmpty(commentString)){
    		SXSSFCell excelCell = _xssfCellExportContext.getExcelCell();
        	XSSFSheet xssfSheet = _xssfCellExportContext.parentContext().sheet().getXSSFSheet();
        	XSSFWorkbook xssfWorkbook = _xssfCellExportContext.parentContext().parentContext().xssfWorkbook();
            CreationHelper creationHelper = xssfWorkbook.getCreationHelper();
            XSSFDrawing drawing = xssfSheet.createDrawingPatriarch();
            ClientAnchor anchor = creationHelper.createClientAnchor();
            XSSFComment comment = drawing.createCellComment(anchor);
            RichTextString str = creationHelper.createRichTextString(commentString);
            comment.setString(str);
            excelCell.setCellComment(comment);
            comment.setColumn(_xssfCellExportContext.sheetCell().getY() - 1);
            comment.setRow(_xssfCellExportContext.sheetCell().getX() -1);
    	}
    			
    }
    
    private void registerTable() {
		if(_xssfCellExportContext.contentMap().containsKey("tpl")){
			String tableColumn =  _xssfCellExportContext.parentContext().
		        registerTable(_xssfCellExportContext.sheetCell(),_xssfCellExportContext.contentMap());
			if(tableColumn != null){
			    _xssfCellExportContext.putVariable("tableColumn", tableColumn);
			}
		}
	}
    
    
    private double convertTimeAsDouble(String timeString){
//    	String timeValue;
//		boolean isPM = false;
//		String lowCaseVal = timeString.toLowerCase().trim();
//        if (lowCaseVal.endsWith("pm") || lowCaseVal.endsWith("am")) {
//        	timeValue = timeString.substring(0, lowCaseVal.length()-2).trim();
//        	if (lowCaseVal.endsWith("pm")) isPM = true;
//        }else{
//        	timeValue = lowCaseVal;
//        }
        
        Double val = DateUtil.convertTime(timeString);
        return val;
    }


    public static DataCellProcessor build(XSSFCellExportContext xssfCellExportContext) {
        DataCellProcessor dataCellProcessor = new DataCellProcessor(xssfCellExportContext);
        return dataCellProcessor;
    }


}
