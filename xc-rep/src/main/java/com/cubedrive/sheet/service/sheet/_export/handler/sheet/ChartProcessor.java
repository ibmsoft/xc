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
package com.cubedrive.sheet.service.sheet._export.handler.sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChartSpace;

import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;
import com.cubedrive.sheet.service.sheet._export.xssf.XSSFWriteEventChart;

public class ChartProcessor extends WorksheetProcessor{
	
	private final ChartInfo _chartInfo;
	
	private final Map<String,Object> _chartMap;

	private ChartProcessor(Map<String,Object> chartMap, XSSFWorksheetExportContext xssfWorksheetExportContext) {
		super(xssfWorksheetExportContext);
		_chartInfo = new ChartInfo(xssfWorksheetExportContext.sheet().getSheetName(),chartMap);
		_chartMap = chartMap;
	}

	@Override
	public void process() {
	    Drawing drawing = _xssfWorksheetExportContext.sheet().createDrawingPatriarch();
	    XSSFWriteEventChart chart = SpreadsheetExportHelper.createChart(_xssfWorksheetExportContext, (XSSFDrawing)drawing, _chartMap);
	    CTChartSpace ctChartSpace = chart.getCTChartSpace();
	    ctChartSpace.addNewDate1904().setVal(false);
	    ctChartSpace.addNewLang().setVal("zh-CN");
	    ctChartSpace.addNewRoundedCorners().setVal(true);
	    try {
            Charts.buildChart(_chartInfo, chart.getCTChart());
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
	}
	
	static class ChartInfo {
	    
		private final String _sheetName;
		private final Map<String,Object> _chartMap;
		
		
		private ChartInfo(String sheetName,Map<String,Object> chartMap){
			_sheetName = sheetName;
			_chartMap = chartMap;
		}
		
		public String sheetName(){
			return _sheetName;
		}
		
		public String seriesPosition(){
			return (String)_chartMap.get("seriesPosition");
		}
		
		public String legendPosition(){
			return (String)_chartMap.get("legendPosition");
		}
		
		public String chartType(){
			return (String)_chartMap.get("chartType");
		}
		
		public String xTitle(){
			return (String)_chartMap.get("xTitle");
		}
		
		public String yTitle(){
			return (String)_chartMap.get("yTitle");
		}
		
		@SuppressWarnings("unchecked")
		public List<CellRangeAddress> series(){
			Map<String,Object> source = (Map<String,Object>)_chartMap.get("source");
			List<List<Object>> objectsList = (List<List<Object>>)source.get("series");
			List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();
			for(List<Object> objects:objectsList){
				Integer firstRow = (Integer)objects.get(1);
				Integer firstCol = (Integer)objects.get(2);
				Integer lastRow = (Integer)objects.get(3);
				Integer lastCol = (Integer)objects.get(4);
				CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow-1,lastRow-1,firstCol-1,lastCol-1);
				cellRangeAddressList.add(cellRangeAddress);
			}
			return cellRangeAddressList;
		}
		
		@SuppressWarnings("unchecked")
		public List<CellReference> categories(){
		    List<CellReference> cellReferenceList = new SpreadsheetExportHelper.OverIndexNullValueList<>();
			Map<String,Object> source = (Map<String,Object>)_chartMap.get("source");
			if(source.containsKey("categories")){
    			List<List<Object>> objectsList = (List<List<Object>>)source.get("categories");
    			for(List<Object> objects: objectsList){
    				Integer row = (Integer)objects.get(1);
    				Integer col = (Integer)objects.get(2);
    				CellReference cellReference = new CellReference(row-1,col-1,true,true);
    				cellReferenceList.add(cellReference);
    			}
			}
			return cellReferenceList;
		}
		
		@SuppressWarnings("unchecked")
		public CellRangeAddress labels(){
			Map<String,Object> source = (Map<String,Object>)_chartMap.get("source");
			if(source.containsKey("labels")){
    			List<List<Object>> objectsList = (List<List<Object>>)source.get("labels");
    			Integer firstRow = (Integer)objectsList.get(0).get(1);
    			Integer firstCol = (Integer)objectsList.get(0).get(2);
    			Integer lastRow = (Integer)objectsList.get(objectsList.size()-1).get(1);
    			Integer lastCol = (Integer)objectsList.get(objectsList.size()-1).get(2);
    			return new CellRangeAddress(firstRow-1,lastRow-1,firstCol-1,lastCol-1);
			}else{
			    return null;
			}
		}
		
		@SuppressWarnings("unchecked")
		public List<String> cachedCategories(){
			Map<String,Object> source = (Map<String,Object>)_chartMap.get("source");
			List<Map<String,Object>> objectMapList = (List<Map<String,Object>>)source.get("cacheFields");
			List<String> categoryValues = new ArrayList<>();
			for(Map<String,Object> objectMap:objectMapList){
				categoryValues.add((String)objectMap.get("name"));
			}
			return categoryValues;
		}
		
		public Integer x(){
			return (Integer)_chartMap.get("x");
		}
		
		public Integer y(){
			return (Integer)_chartMap.get("y");
		}
		
		public Integer width(){
			return (Integer)_chartMap.get("width");
		}
		
		public Integer height(){
			return (Integer)_chartMap.get("height");
		}
	}
	
	
	public static ChartProcessor build(Map<String,Object> chartMap,XSSFWorksheetExportContext xssfWorksheetExportContext){
	    ChartProcessor chartProcessor = new ChartProcessor(chartMap,xssfWorksheetExportContext);
	    return chartProcessor;
	}

}
