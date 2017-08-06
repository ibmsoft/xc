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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetViews;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFCellExportContext;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.handler.CellStyleHandler;

public class ConfigCellProcessor extends CellProcessor {
	
	private CTSheetView ctSheetView;
	
	private ConfigCellProcessor(XSSFCellExportContext xssfCellExportContext){
		super(xssfCellExportContext);
	}
	
	public void process(){
	    setSheetView();
	}
	
	private void setSheetView(){
		XSSFWorksheetExportContext worksheetExportContext = _xssfCellExportContext.parentContext();
		if(worksheetExportContext.sheetTab().getActive()){
			ctSheetView.setTabSelected(true);
		}else{
		    ctSheetView.setTabSelected(false);
		}
		Map<String,Object> contentMap = _xssfCellExportContext.contentMap();
		setSheetStyle();
	    String configJson = (String)contentMap.get("config");
	    if(configJson != null){
	        Map<String,Object> configMap = JsonUtil.getJsonObj(configJson);
	        Boolean noGridLine = (Boolean)configMap.get("noGridLine");
	        if(noGridLine != null && noGridLine){
	        	ctSheetView.setShowGridLines(!noGridLine);
	        }
	        boolean containsFreezePos = configMap.containsKey("freezePos");
	        boolean containsSplitPos = configMap.containsKey("splitPos");
	        if(!containsFreezePos && !containsSplitPos)
	        	setSheetViewWithoutPane(configMap);
	        else
	        	setSheetViewWithPane(configMap, containsFreezePos ? true : false);
	    }
	}
	
	
	private void setDefaultColumnWidthAndRowHeight(){
		Map<String,Object> contentMap = _xssfCellExportContext.contentMap();
        Integer dw = (Integer)contentMap.get("dw");
        if(dw ==null || dw ==0){
            dw = SpreadsheetExportHelper.default_column_width;
        }
        Integer dh = (Integer)contentMap.get("dh");
        if(dh == null || dh == 0){
            dh = SpreadsheetExportHelper.default_row_height;
        }
        _xssfCellExportContext.parentContext().setDefaultColumnWidthAndRowHeight(dw, dh);
	}
	
	private void setSheetStyle(){
    	XSSFCellStyle cellStyle = CellStyleHandler.instance().getOrCreateCellStyle(_xssfCellExportContext);
    	if(cellStyle != null){
    		XSSFSheet xssfSheet = _xssfCellExportContext.parentContext().sheet().getXSSFSheet();
    		if(xssfSheet.getCTWorksheet().sizeOfColsArray() > 0){
    			CTCols ctCols=  xssfSheet.getCTWorksheet().getColsArray(0);
    			CTCol ctCol = ctCols.addNewCol();
            	ctCol.setMin(_xssfCellExportContext.parentContext().getNextCol());
            	ctCol.setMax(16384);
            	ctCol.setStyle(cellStyle.getIndex());
    		}
    	}
    }
	
	@SuppressWarnings("unchecked")
	private void setSheetViewWithoutPane(Map<String,Object> configMap){
	    Map<String,Object> selection = (Map<String,Object>)configMap.get("selection");
        CTSelection ctSelection = ctSheetView.addNewSelection();
        List<String> sqrefList = new ArrayList<>();
        String startCellRef;
        String sqref;
        if(selection!=null && !selection.isEmpty()){
            Map<String,Integer> startPos =  (Map<String,Integer>)selection.get("startPos");
            Map<String,Integer> endPos = (Map<String,Integer>)selection.get("endPos");
            int startRow = startPos.get("row");
            int endRow = endPos.get("row");
            
            startCellRef =  CellReference.convertNumToColString((Integer)startPos.get("col")-1)+ (startRow == 0 ? 1 :startRow);
            String endCellRef = CellReference.convertNumToColString((Integer)endPos.get("col")-1)+ (endRow == 0 ? 1048576 : endRow);
            if(!startCellRef.equals(endCellRef)){
                sqref = startCellRef+":"+endCellRef;
            }else{
                sqref = startCellRef;
            }
        }else{
        	sqref = startCellRef = "A1";
        }
        sqrefList.add(sqref);
        ctSelection.setActiveCell(startCellRef);
        ctSelection.setSqref(sqrefList);
	}
	
	
	private void setSheetViewWithPane(Map<String,Object> configMap, boolean isFrozen){
		XSSFWorksheetExportContext xssfWorksheetExportContext = _xssfCellExportContext.parentContext();
		
		Map<String,Object> rangeInfoMap = (Map<String,Object>)configMap.get("rangeInfo");
		Map<String,Object> cregionMap = (Map<String,Object>) rangeInfoMap.get("cregion");
		Map<String,Object> lregionMap = (Map<String,Object>) rangeInfoMap.get("lregion");
		Map<String,Object> ltregionMap = (Map<String,Object>) rangeInfoMap.get("ltregion");
		Map<String,Object> tregionMap = (Map<String,Object>) rangeInfoMap.get("tregion");
		
		boolean lregionVisible  = (Boolean)lregionMap.get("visible");
		boolean ltregionVisible = (Boolean)ltregionMap.get("visible");
		boolean tregionVisible = (Boolean)tregionMap.get("visible");
		
		CTPane ctPane = ctSheetView.addNewPane();
		
		if(!lregionVisible && !ltregionVisible){
			Map<String,Object> tregionRange = (Map<String,Object>)tregionMap.get("range");
			Map<String,Object> cregionRange = (Map<String,Object>)cregionMap.get("range");
			String sheetViewTopLeftCell =  CellReference.convertNumToColString(((Integer)tregionRange.get("colStart"))-1) + (Integer)tregionRange.get("rowStart");
			ctSheetView.setTopLeftCell(sheetViewTopLeftCell);
			ctSheetView.setWorkbookViewId(0);
			
			
			ctPane.setActivePane( org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.BOTTOM_LEFT);
			Map<String,Object> ulefter = (Map<String,Object>)rangeInfoMap.get("ulefter");
			Map<String,Object> ulefterRange =(Map<String,Object>) ulefter.get("range");
			int yOffset = (Integer)ulefterRange.get("rowEnd") - (Integer)ulefterRange.get("rowStart") +1;
			if(isFrozen){
				ctPane.setState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState.FROZEN);
				ctPane.setYSplit(yOffset);
			}else{
				int rowHeighPxStart = xssfWorksheetExportContext.getRowHeightCumulateUntil((Integer)tregionRange.get("rowStart"));
				int rowHeighPxEnd = xssfWorksheetExportContext.getRowHeightCumulateUntil((Integer)tregionRange.get("rowStart") + yOffset);
				ctPane.setYSplit(270 + 15 * (rowHeighPxEnd-rowHeighPxStart));
			
			}
			ctPane.setTopLeftCell( "A" +  ((Integer)cregionRange.get("rowStart")));
			
			CTSelection ctSelection1 = ctSheetView.addNewSelection();
			List<String> sqref1 = new ArrayList<>();
			String activeCell1 = CellReference.convertNumToColString(((Integer)tregionRange.get("colStart"))-1) + (Integer)tregionRange.get("rowStart");
			sqref1.add(activeCell1);
			ctSelection1.setSqref(sqref1);
			ctSelection1.setActiveCell(activeCell1);
			
			CTSelection ctSelection2 = ctSheetView.addNewSelection();
			String activeCell2 = CellReference.convertNumToColString((Integer)cregionRange.get("colStart")-1) + (Integer)cregionRange.get("rowStart");
			List<String> sqref2 = new ArrayList<>();
			sqref2.add(activeCell2);
			ctSelection2.setSqref(sqref2);
			ctSelection2.setActiveCell(activeCell2);
			
		}else if(!tregionVisible && !ltregionVisible){
			Map<String,Object> lregionRange = (Map<String,Object>)lregionMap.get("range");
			Map<String,Object> cregionRange = (Map<String,Object>)cregionMap.get("range");
			
			String sheetViewTopLeftCell =  CellReference.convertNumToColString(((Integer)lregionRange.get("colStart"))-1) + (Integer)lregionRange.get("rowStart");
			ctSheetView.setTopLeftCell(sheetViewTopLeftCell);
			ctSheetView.setWorkbookViewId(0);
			
			ctPane.setActivePane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.TOP_RIGHT);
			
			Map<String,Object> lheader = (Map<String,Object>)rangeInfoMap.get("lheader");
			Map<String,Object> lheaderRange = (Map<String,Object>)lheader.get("range");
			int xOffset = (Integer)lheaderRange.get("colEnd") - (Integer)lheaderRange.get("colStart") +1;
			
			if(isFrozen){
				ctPane.setState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState.FROZEN);
				ctPane.setXSplit(xOffset);
			}else{
				int columnWidthPxStart = xssfWorksheetExportContext.getColumnWidthCumulateUntil(((Integer)lregionRange.get("colStart")));
				int columnWidthPxEnd = xssfWorksheetExportContext.getColumnWidthCumulateUntil(((Integer)lregionRange.get("colStart")+xOffset));
				ctPane.setXSplit(510 + 15*(columnWidthPxEnd- columnWidthPxStart));
			}
			ctPane.setTopLeftCell(CellReference.convertNumToColString((Integer)cregionRange.get("colStart")-1)+ "1");
			
			
//			if(!isFrozen ||  (isFrozen && xOffset>1)){
				CTSelection ctSelection1 = ctSheetView.addNewSelection();
				String activeCell1 = CellReference.convertNumToColString(((Integer)lregionRange.get("colStart"))) + (Integer)lregionRange.get("rowStart");
				List<String> sqref1 = new ArrayList<>();
				sqref1.add(activeCell1);
				ctSelection1.setSqref(sqref1);
				ctSelection1.setActiveCell(activeCell1);
				
				CTSelection ctSelection2 = ctSheetView.addNewSelection();
				String activeCell2 =CellReference.convertNumToColString((Integer)cregionRange.get("colStart")-1) + (Integer)cregionRange.get("rowStart"); 
				List<String> sqref2 = new ArrayList<>();
				sqref2.add(activeCell2);
				ctSelection2.setSqref(sqref2);
				ctSelection2.setActiveCell(activeCell2);
//			}
			
			
		}else if(tregionVisible && ltregionVisible && lregionVisible){
			Map<String,Object> tregionRange = (Map<String,Object>)tregionMap.get("range");
			Map<String,Object> lregionRange = (Map<String,Object>)lregionMap.get("range");
			Map<String,Object> cregionRange = (Map<String,Object>)cregionMap.get("range");
			Map<String,Object> ltregionRange = (Map<String,Object>)ltregionMap.get("range");
			
			String sheetViewTopLeftCell =  CellReference.convertNumToColString(((Integer)ltregionRange.get("colStart"))-1) + (Integer)ltregionRange.get("rowStart");
			ctSheetView.setTopLeftCell(sheetViewTopLeftCell);
			ctSheetView.setWorkbookViewId(0);
			
			ctPane.setActivePane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.BOTTOM_RIGHT);
			
			Map<String,Object> ulefter = (Map<String,Object>)rangeInfoMap.get("ulefter");
			Map<String,Object> ulefterRange =(Map<String,Object>) ulefter.get("range");
			int yOffset = (Integer)ulefterRange.get("rowEnd") - (Integer)ulefterRange.get("rowStart") +1;
			
			Map<String,Object> lheader = (Map<String,Object>)rangeInfoMap.get("lheader");
			Map<String,Object> lheaderRange = (Map<String,Object>)lheader.get("range");
			int xOffset = (Integer)lheaderRange.get("colEnd") - (Integer)lheaderRange.get("colStart") +1;
			
			if(isFrozen){
				ctPane.setState(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPaneState.FROZEN);
				ctPane.setXSplit(xOffset);
				ctPane.setYSplit(yOffset);
			}else{
				int rowHeighPxStart = xssfWorksheetExportContext.getRowHeightCumulateUntil((Integer)tregionRange.get("rowStart"));
				int rowHeighPxEnd = xssfWorksheetExportContext.getRowHeightCumulateUntil((Integer)tregionRange.get("rowStart") + yOffset);
				ctPane.setYSplit(270 + 15 * (rowHeighPxEnd-rowHeighPxStart));
				
				int columnWidthPxStart = xssfWorksheetExportContext.getColumnWidthCumulateUntil(((Integer)lregionRange.get("colStart")));
				int columnWidthPxEnd = xssfWorksheetExportContext.getColumnWidthCumulateUntil(((Integer)lregionRange.get("colStart")+xOffset));
				ctPane.setXSplit(510 + 15*(columnWidthPxEnd- columnWidthPxStart));
			}
			ctPane.setTopLeftCell(CellReference.convertNumToColString((Integer)cregionRange.get("colStart")-1)+ (Integer)cregionRange.get("rowStart"));
			
		
			
			CTSelection ctSelection1 = ctSheetView.addNewSelection();
			String activeCell1 = CellReference.convertNumToColString(((Integer)ltregionRange.get("colStart"))-1) + (Integer)ltregionRange.get("rowStart");
			List<String> sqref1 = new ArrayList<>();
			sqref1.add(activeCell1);
			ctSelection1.setPane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.TOP_LEFT);
			ctSelection1.setSqref(sqref1);
			ctSelection1.setActiveCell(activeCell1);
			
			CTSelection ctSelection2 = ctSheetView.addNewSelection();
			String activeCell2 =CellReference.convertNumToColString((Integer)tregionRange.get("colStart")-1) + (Integer)tregionRange.get("rowStart"); 
			List<String> sqref2 = new ArrayList<>();
			sqref2.add(activeCell2);
			ctSelection2.setPane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.TOP_RIGHT);
			ctSelection2.setSqref(sqref2);
			ctSelection2.setActiveCell(activeCell2);
			
			CTSelection ctSelection3 = ctSheetView.addNewSelection();
			String activeCell3 =CellReference.convertNumToColString((Integer)lregionRange.get("colStart")-1) + (Integer)lregionRange.get("rowStart"); 
			List<String> sqref3 = new ArrayList<>();
			sqref3.add(activeCell3);
			ctSelection3.setPane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.BOTTOM_LEFT);
			ctSelection3.setSqref(sqref3);
			ctSelection3.setActiveCell(activeCell3);
			
			CTSelection ctSelection4 = ctSheetView.addNewSelection();
			String activeCell4 = CellReference.convertNumToColString((Integer)cregionRange.get("colStart")-1) + (Integer)cregionRange.get("rowStart"); 
			List<String> sqref4 = new ArrayList<>();
			sqref4.add(activeCell4);
			ctSelection4.setPane(org.openxmlformats.schemas.spreadsheetml.x2006.main.STPane.BOTTOM_RIGHT);
			ctSelection4.setSqref(sqref4);
			ctSelection4.setActiveCell(activeCell4);
			
		}
		
	}
	
	public static ConfigCellProcessor build(XSSFCellExportContext xssfCellExportContext){
		ConfigCellProcessor configCellProcessor = new ConfigCellProcessor(xssfCellExportContext);
		XSSFSheet sheet = xssfCellExportContext.parentContext().sheet().getXSSFSheet();
		CTWorksheet ctWorksheet = sheet.getCTWorksheet();
		CTSheetViews ctSheetViews;
		if(ctWorksheet.isSetSheetViews()){
		    ctSheetViews = ctWorksheet.getSheetViews();
		}else{
		    ctSheetViews = ctWorksheet.addNewSheetViews();
		}
		CTSheetView ctSheetView;
		if(! ctSheetViews.getSheetViewList().isEmpty()){
		    ctSheetView = ctSheetViews.getSheetViewList().get(0);
		}else{
		    ctSheetView = ctSheetViews.addNewSheetView();
		}
		if("ed".equals(xssfCellExportContext.contentMap().get("dsd"))){
			xssfCellExportContext.parentContext().lockAll();
		}
		
	    configCellProcessor.ctSheetView = ctSheetView;
	    return configCellProcessor;
	}

}
