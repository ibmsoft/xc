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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.type.TypeReference;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTHeaderFooter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageMargins;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPageSetup;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext.ColumnSetting;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext.TableInfo;
import com.cubedrive.sheet.service.sheet._export.handler.sheet.ChartProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.sheet.ColumnGroupProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.sheet.ConditionalFormattingProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.sheet.DataValidationProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.sheet.SparklineGroupProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.sheet.TableProcessor;
import com.google.common.base.Strings;

public class WorksheetExportHandler {

    private static final WorksheetExportHandler _instance = new WorksheetExportHandler();

    private WorksheetExportHandler() {

    }

    public void startWorksheet(XSSFWorksheetExportContext worksheetExportContext) {
        SXSSFSheet sheet = createSXSSFSheet(worksheetExportContext);
        worksheetExportContext.setSheet(sheet);
        worksheetExportContext.sheetExportEnvironment().addSheetTab(worksheetExportContext.sheetTab());
    }


    private SXSSFSheet createSXSSFSheet(XSSFWorksheetExportContext worksheetExportContext) {
        SXSSFWorkbook sxssfWorkbook = worksheetExportContext.parentContext().sxssfWorkbook();
        SheetTab sheetTab = worksheetExportContext.sheetTab();
        SXSSFSheet sxssfSheet = (SXSSFSheet) sxssfWorkbook.createSheet(SpreadsheetExportHelper.toJavaString(sheetTab.getName()));
        if (sheetTab.getActive()) {
            sxssfWorkbook.setActiveSheet(worksheetExportContext.index());
        }
        if(sheetTab.getColor() != null){
        	CTWorksheet ctWorksheet = sxssfSheet.getXSSFSheet().getCTWorksheet();
        	if(! ctWorksheet.isSetSheetPr()){
        		CTColor tabCTColor = ctWorksheet.addNewSheetPr().addNewTabColor();
        		tabCTColor.setRgb(SpreadsheetExportHelper.colorAsBytes(sheetTab.getColor()));
        	}
        }
        return sxssfSheet;

    }
    
    
    public void postProcessIterateSheetCell(XSSFWorksheetExportContext worksheetExportContext){
        setSheetDefaultColumnWidthAndRowHeight(worksheetExportContext);
        setWorksheetCols(worksheetExportContext);
    }
    
    public void addSheetElement(SheetTabElement sheetTabElement,XSSFWorksheetExportContext worksheetExportContext){
        String eType = sheetTabElement.getEtype();
        String content = sheetTabElement.getContent();
        if("floor".equals(eType)){
            Map<String,Object> contentMap = JsonUtil.getJsonObj(content);
            String floorType = (String)contentMap.get("floorType");
            if("picture".equals(floorType)){
                addPicture(worksheetExportContext,contentMap);
            }else if("chart".equals(floorType)){
                addChart(worksheetExportContext,contentMap);
            }
        }else if("cdt".equals(eType)){
        	Map<String,Object> cdtMap = JsonUtil.getJsonObj(content);
        	String cdtName = (String)cdtMap.get("name");
        	if("minichart".equals(cdtName)){
        		worksheetExportContext.addMiniChart(cdtMap);
        	}else if("vd".equals(cdtName)){
        		addDataValidation(worksheetExportContext,cdtMap);
        	}else{
        		addConditionalFormatting(worksheetExportContext, cdtMap);
        	}
        }else if("colgroup".equals(eType)){
        	List<Map<String,Object>> colGroups = JsonUtil.fromJson(content, new TypeReference<List<Map<String,Object>>>	(){});
        	addColumnGroups(worksheetExportContext,colGroups);
        }
    }
    
    public void postProcessIterateSheetTabElement(XSSFWorksheetExportContext worksheetExportContext){
        
    }
    
    public void endWorksheet(XSSFWorksheetExportContext xssfWorksheetExportContext){
        addMergeCells(xssfWorksheetExportContext);
        addTables(xssfWorksheetExportContext);
        lockSheet(xssfWorksheetExportContext);
        addRowGroups(xssfWorksheetExportContext);
        addSparklineGroups(xssfWorksheetExportContext);
        if(xssfWorksheetExportContext.getConfigCellProcessor() != null){
        	xssfWorksheetExportContext.getConfigCellProcessor().process();
        }
        addPrintSetup(xssfWorksheetExportContext);
    }
    
    private void addMergeCells(XSSFWorksheetExportContext xssfWorksheetExportContext){
        if(xssfWorksheetExportContext.getMergeProcessor() != null){
            xssfWorksheetExportContext.getMergeProcessor().process();
        }
    }
    
    private void addTables(XSSFWorksheetExportContext xssfWorksheetExportContext) {
        Integer tableIndex = (Integer)xssfWorksheetExportContext.parentContext().getVariable("table_index");
        if(tableIndex == null){
            tableIndex = 1;
        }
        int workbookTableIndex = tableIndex ;
        int sheetTableIndex = 1;
    	for(TableInfo tableInfo:xssfWorksheetExportContext.tables()){
    		TableProcessor tableProcessor = TableProcessor.build(tableInfo, workbookTableIndex,sheetTableIndex,xssfWorksheetExportContext);
    		tableProcessor.process();
    		workbookTableIndex ++;
    		sheetTableIndex ++;
    	}
    	xssfWorksheetExportContext.parentContext().putVariable("table_index", workbookTableIndex);
	}
    
    private void lockSheet(XSSFWorksheetExportContext xssfWorksheetExportContext){
    	if(xssfWorksheetExportContext.isSheetNeedProtection()){
    		XSSFSheet sheet = xssfWorksheetExportContext.sheet().getXSSFSheet();
		  	sheet.enableLocking();
	        sheet.lockObjects(true);
	        sheet.lockScenarios(true);
	        sheet.lockSelectLockedCells(true);
    	}
    }
    
    private void addRowGroups(XSSFWorksheetExportContext xssfWorksheetExportContext){
        if(xssfWorksheetExportContext.getRowGroupProcessor() != null){
            xssfWorksheetExportContext.getRowGroupProcessor().process();
        }
    }
    
    private void addSparklineGroups(XSSFWorksheetExportContext xssfWorksheetExportContext){
    	if(!xssfWorksheetExportContext.getMiniCharts().isEmpty()){
    		SparklineGroupProcessor processor = SparklineGroupProcessor.build(xssfWorksheetExportContext);
    		processor.process();
    	}
    }

	
    
    private void addPicture(XSSFWorksheetExportContext worksheetExportContext,Map<String,Object> contentMap){
        String url = (String)contentMap.get("url");
        XSSFClientAnchor clientAnchor =  SpreadsheetExportHelper.createClientAnchor(worksheetExportContext,contentMap);
        String extension = FilenameUtils.getExtension(url);
        int pictureType = SpreadsheetExportHelper.getPictureType(extension);
        XSSFSheet xssfSheet = worksheetExportContext.sheet().getXSSFSheet();
        XSSFWorkbook xssfWorkbook = worksheetExportContext.parentContext().xssfWorkbook();
        try(InputStream in = SpreadsheetExportHelper.getInputStreamByUrl(url)){
            int pictureIndex =xssfWorkbook.addPicture(in, pictureType);
            Drawing drawing = xssfSheet.createDrawingPatriarch();
            drawing.createPicture(clientAnchor, pictureIndex);
        } catch (IOException e) {
            //eat it
        }
    }
    
    private void addChart(XSSFWorksheetExportContext worksheetExportContext,Map<String,Object> contentMap){
        ChartProcessor chartProcessor = ChartProcessor.build(contentMap, worksheetExportContext);
        chartProcessor.process();
    }
    
    private void addConditionalFormatting(XSSFWorksheetExportContext worksheetExportContext,Map<String,Object> conditionalFormattingInfo){
    	ConditionalFormattingProcessor conditionalFormattingProcessor = ConditionalFormattingProcessor.build(conditionalFormattingInfo, worksheetExportContext);
    	conditionalFormattingProcessor.process();
    }
    
    private void addDataValidation(XSSFWorksheetExportContext worksheetExportContext,Map<String,Object> dataValidationInfo){
    	DataValidationProcessor dataValidationProcessor = DataValidationProcessor.build(dataValidationInfo, worksheetExportContext);
    	dataValidationProcessor.process();
    }
    
    private void addColumnGroups(XSSFWorksheetExportContext worksheetExportContext,List<Map<String,Object>> columnGroups){
    	ColumnGroupProcessor columnGroupProcessor = ColumnGroupProcessor.build(columnGroups, worksheetExportContext);
    	columnGroupProcessor.process();
    }
    
    private void addPrintSetup(XSSFWorksheetExportContext xssfWorksheetExportContext){
        if(!Strings.isNullOrEmpty(xssfWorksheetExportContext.sheetTab().getExtraInfo())){
            XSSFSheet sheet = xssfWorksheetExportContext.sheet().getXSSFSheet();
            Map<String,Object> extraInfo = JsonUtil.getJsonObj(xssfWorksheetExportContext.sheetTab().getExtraInfo());
            Map<String,Object> printSetting = (Map<String,Object>)extraInfo.get("printSetting");
            if(printSetting != null && !printSetting.isEmpty()){
            
                Map<String,Number> pageMarginsSettings = (Map<String,Number>)printSetting.get("pageMargins");
                if(pageMarginsSettings != null){
                    CTPageMargins ctPageMargin;
                    if(!sheet.getCTWorksheet().isSetPageMargins()){
                        ctPageMargin = sheet.getCTWorksheet().addNewPageMargins();
                    }else{
                        ctPageMargin = sheet.getCTWorksheet().getPageMargins();
                    }
                    
                    Number bottom = pageMarginsSettings.get("bottom");
                    if(bottom != null){
                        ctPageMargin.setBottom(new Double(bottom.toString()));
                    }
                    Number footer = pageMarginsSettings.get("footer");
                    if(footer != null){
                        ctPageMargin.setFooter(new Double(footer.toString()));
                    }
                    Number header = pageMarginsSettings.get("header");
                    if(header != null){
                        ctPageMargin.setHeader(new Double(header.toString()));
                    }
                    Number left = pageMarginsSettings.get("left");
                    if(left != null){
                        ctPageMargin.setHeader(new Double(left.toString()));
                    }
                    Number right = pageMarginsSettings.get("right");
                    if(right != null){
                        ctPageMargin.setRight(new Double(right.toString()));
                    }
                    Number top = pageMarginsSettings.get("top");
                    if(top != null){
                        ctPageMargin.setTop(new Double(top.toString()));
                    }
                }
                Map<String,Object> pageSetupSettings = ( Map<String,Object>)printSetting.get("pageSetup");
                if(pageSetupSettings != null){
                    CTPageSetup ctPageSetup;
                    if(!sheet.getCTWorksheet().isSetPageSetup()){
                        ctPageSetup = sheet.getCTWorksheet().addNewPageSetup();
                    }else{
                        ctPageSetup = sheet.getCTWorksheet().getPageSetup();
                    }
                    String orientation = (String)pageSetupSettings.get("orientation");
                    if(orientation != null){
                        ctPageSetup.setOrientation(org.openxmlformats.schemas.spreadsheetml.x2006.main.STOrientation.Enum.forString(orientation));
                    }
                    String pageOrder = (String)pageSetupSettings.get("pageOrder");
                    if(pageOrder != null){
                        ctPageSetup.setPageOrder(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPageOrder.Enum.forString(pageOrder));
                    }
                    Integer paperSize = (Integer)pageSetupSettings.get("paperSize");
                    if(paperSize != null){
                        ctPageSetup.setPaperSize(paperSize);
                    }
                    Integer fitToHeight =(Integer)pageSetupSettings.get("fitToHeight");
                    if(fitToHeight != null){
                        ctPageSetup.setFitToHeight(fitToHeight);
                    }
                    Integer fitToWidth = (Integer)pageSetupSettings.get("fitToWidth");
                    if(fitToWidth != null){
                        ctPageSetup.setFitToWidth(fitToWidth);
                    }
                    Integer scale = (Integer)pageSetupSettings.get("scale");
                    if(scale != null){
                        ctPageSetup.setScale(scale);
                    }
                    Boolean useFirstPageNumber = (Boolean)pageSetupSettings.get("useFirstPageNumber");
                    if(useFirstPageNumber != null){
                        ctPageSetup.setUseFirstPageNumber(useFirstPageNumber);
                    }
                    Boolean usePrinterDefaults = (Boolean)pageSetupSettings.get("usePrinterDefaults");
                    if(usePrinterDefaults != null){
                        ctPageSetup.setUsePrinterDefaults(usePrinterDefaults);
                    }
                    Boolean blackAndWhite = (Boolean)pageSetupSettings.get("blackAndWhite");
                    if(blackAndWhite != null){
                        ctPageSetup.setBlackAndWhite(blackAndWhite);
                    }
                    String cellComments =(String)pageSetupSettings.get("cellComments");
                    if(cellComments != null){
                        ctPageSetup.setCellComments(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCellComments.Enum.forString(cellComments));
                    }
                }
                
                Map<String,Object> headerFooterSettings = (Map<String,Object>)printSetting.get("headerFooter");
                if(headerFooterSettings != null){
                    CTHeaderFooter ctHeaderFooter;
                    if(!sheet.getCTWorksheet().isSetHeaderFooter()){
                        ctHeaderFooter = sheet.getCTWorksheet().addNewHeaderFooter();
                    }else{
                        ctHeaderFooter = sheet.getCTWorksheet().getHeaderFooter();
                    }
                    
                    String firstHeader = (String)headerFooterSettings.get("firstHeader");
                    if(firstHeader != null){
                        ctHeaderFooter.setFirstHeader(firstHeader);
                    }
                    String firstFooter = (String)headerFooterSettings.get("firstFooter");
                    if(firstFooter != null){
                        ctHeaderFooter.setFirstFooter(firstFooter);
                    }
                    String oddHeader = (String)headerFooterSettings.get("oddHeader");
                    if(oddHeader != null){
                        ctHeaderFooter.setOddHeader(oddHeader);
                    }
                    String oddFooter =(String)headerFooterSettings.get("oddFooter");
                    if(oddFooter != null){
                        ctHeaderFooter.setOddFooter(oddFooter);
                    }
                    String evenHeader = (String)headerFooterSettings.get("evenHeader");
                    if(evenHeader != null){
                        ctHeaderFooter.setEvenHeader(evenHeader);
                    }
                    String evenFooter = (String)headerFooterSettings.get("evenFooter");
                    if(evenFooter != null){
                        ctHeaderFooter.setEvenFooter(evenFooter);
                    }
                    Boolean scaleWithDoc = (Boolean)headerFooterSettings.get("scaleWithDoc");
                    if(scaleWithDoc != null){
                        ctHeaderFooter.setScaleWithDoc(scaleWithDoc);
                    }
                    Boolean differentFirst = (Boolean)headerFooterSettings.get("differentFirst");
                    if(differentFirst != null){
                        ctHeaderFooter.setDifferentFirst(differentFirst);
                    }
                    Boolean differentOddEven = (Boolean)headerFooterSettings.get("differentOddEven");
                    if(differentOddEven != null){
                        ctHeaderFooter.setDifferentOddEven(differentOddEven);
                    }
                    Boolean alignWithMargins = (Boolean)headerFooterSettings.get("alignWithMargins");
                    if(alignWithMargins != null){
                        ctHeaderFooter.setAlignWithMargins(alignWithMargins);
                    }
                    
                }
            }
        }
        
    }
    
    
    private void setWorksheetCols(XSSFWorksheetExportContext worksheetExportContext){
    	XSSFSheet xssfSheet = worksheetExportContext.sheet().getXSSFSheet();
		List<ColumnSetting> columnSettingList= worksheetExportContext.getSortedColumnSettings();
    	boolean locked = worksheetExportContext.isSheetNeedProtection();
    	short nonProtectionStyleIndex = worksheetExportContext.parentContext().getNonProtectionStyle().getIndex();
    	int lastMin = 1;
    	CTCols ctCols=  xssfSheet.getCTWorksheet().getColsList().get(0);
        if(! columnSettingList.isEmpty()){
            for(ColumnSetting columnSetting:columnSettingList){
            	int min = columnSetting.getMin();
            	int max = columnSetting.getMax();
            	
            	if(locked &&  min-lastMin!=0){
            		CTCol ctCol = ctCols.addNewCol();
                    ctCol.setMin(lastMin);
                    ctCol.setMax(min-1);
                    ctCol.setStyle(nonProtectionStyleIndex);
            	}
            	
                CTCol ctCol = ctCols.addNewCol();
                ctCol.setMin(min);
                ctCol.setMax(max);
                if(columnSetting.getWidth()!=0){
                    ctCol.setCustomWidth(true);
                    ctCol.setWidth(columnSetting.getWidth()/8.0); //change px to excel unit
                }
                if(columnSetting.isHidden()){
                    ctCol.setHidden(true);
                }
                if(columnSetting.getStyleId() !=null){
                	ctCol.setStyle(columnSetting.getStyleId());
                }else if(locked){
                	ctCol.setStyle(nonProtectionStyleIndex);
                }
                lastMin = max+1;
            }
            
        }
        worksheetExportContext.setNextCol(lastMin);
//        if(locked){
//        	CTCol ctCol = ctCols.addNewCol();
//            ctCol.setMin(lastMin);
//            ctCol.setMax(16384);
//            ctCol.setStyle(nonProtectionStyleIndex);
//        	
//        }
    }
    
    private void setSheetDefaultColumnWidthAndRowHeight(XSSFWorksheetExportContext worksheetExportContext){
        int defaultColumnWidth = worksheetExportContext.getDefaultColumnWidth();
        if(defaultColumnWidth == 0){
            defaultColumnWidth = SpreadsheetExportHelper.default_column_width;
        }
        int defaultRowHeight = worksheetExportContext.getDefaultRowHeight();
        if(defaultRowHeight == 0){
            defaultRowHeight = SpreadsheetExportHelper.default_row_height;
        }
        worksheetExportContext.setDefaultColumnWidthAndRowHeight(defaultColumnWidth, defaultRowHeight);
        XSSFSheet sheet = worksheetExportContext.sheet().getXSSFSheet();
        CTWorksheet ctWorksheet = sheet.getCTWorksheet();
        CTSheetFormatPr ctSheetFormatPr;
        if(ctWorksheet.isSetSheetFormatPr()){
            ctSheetFormatPr = ctWorksheet.getSheetFormatPr();
        }else{
            ctSheetFormatPr= ctWorksheet.addNewSheetFormatPr();
        }
        ctSheetFormatPr.setBaseColWidth(8);
        ctSheetFormatPr.setDefaultColWidth(defaultColumnWidth/8.0);
        ctSheetFormatPr.setDefaultRowHeight(defaultRowHeight * 3.0 /4.0);
    }
    

    public static WorksheetExportHandler instance() {
        return _instance;
    }
}
