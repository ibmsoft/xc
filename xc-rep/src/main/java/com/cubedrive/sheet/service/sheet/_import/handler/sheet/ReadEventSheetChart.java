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
package com.cubedrive.sheet.service.sheet._import.handler.sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFGraphicFrame;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTAreaSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTBarSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTLineSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPieSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTPlotArea;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTRadarSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTStrVal;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;

public class ReadEventSheetChart {
    
    private static final List<ChartInfoProcessor> _chartInfoProcessors = new ArrayList<>();
    
    static {
        _chartInfoProcessors.add(new BarChartProcessor());
        _chartInfoProcessors.add(new LineChartProcessor());
        _chartInfoProcessors.add(new AreaChartProcessor());
        _chartInfoProcessors.add(new PieChartProcessor());
        _chartInfoProcessors.add(new RadarChartProcessor());
        _chartInfoProcessors.add(new ScatterChartProcessor());
    }
    
    private final List<Map<String,Object>> _chartSettings = new ArrayList<>(); 

    private ReadEventSheetChart(){
        
    }
    
    public List<Map<String,Object>> getChartSettings(){
    	return _chartSettings;
    }
    
    private static Map<String,Object> handleChart(XSSFChart xssfChart, XSSFGraphicFrame graphicFrame,SheetImportContext sheetImportContext){
        Map<String,Object> chartInfoMap = null;
        CTChart ctChart = xssfChart.getCTChart();
    	
    	for(ChartInfoProcessor _chartInfoProcessor:_chartInfoProcessors){
    	    if(_chartInfoProcessor.isProcessable(ctChart)){
    	       chartInfoMap = _chartInfoProcessor.getChartInfo(ctChart);
    	       break;
    	    }
    	}
    	if(chartInfoMap != null){
    		XSSFClientAnchor anchor = graphicFrame.getAnchor();
    		Map<String,Object> locationMap = calculateLocation(anchor);
    		chartInfoMap.putAll(locationMap);
    	}
    	return chartInfoMap;
    }
    
    private static Map<String,Object> calculateLocation(XSSFClientAnchor anchor){
    	Map<String,Object> locationMap = new HashMap<>();
    	locationMap.put("sr", anchor.getRow1());
    	locationMap.put("sc", anchor.getCol1());
    	locationMap.put("er", anchor.getRow2());
    	locationMap.put("ec", anchor.getCol2());
    	locationMap.put("sx", (int)(anchor.getDx1()/9525));
    	locationMap.put("sy", (int)(anchor.getDy1()/9525));
    	locationMap.put("ex", (int)(anchor.getDx2()/9525));
    	locationMap.put("ey", (int)(anchor.getDy2()/9525));
    	return locationMap;
    }
    
    
    private static abstract class ChartInfoProcessor {
    	
    	abstract boolean isProcessable(CTChart ctChart);
    	
    	abstract String getChartType(CTPlotArea ctPlotArea);
    	
    	abstract void setSourceFields(CTPlotArea ctPlotArea,List<List<Object>> series,List<List<Object>> categories, List<List<Object>> labels,List<Map<String,Object>> cacheFields);
    	
    	private final Map<String,Object> getChartInfo(CTChart ctChart){
    		CTPlotArea ctPlotArea = ctChart.getPlotArea();
    		Map<String,Object> chartInfoMap = new HashMap<>();
    		String chartType = getChartType(ctPlotArea);
    		chartInfoMap.put("chartType", chartType);
    		
    		List<List<Object>> series = new ArrayList<>();
			List<List<Object>> categories = new ArrayList<>();
			List<List<Object>> labels = new ArrayList<>();
			List<Map<String,Object>> cacheFields = new ArrayList<>();
			Map<String,Object> firstCacheField = new HashMap<>();
			firstCacheField.put("name", "category");
			cacheFields.add(firstCacheField);
			
			setSourceFields(ctPlotArea,series,categories,labels,cacheFields);
			if(series.isEmpty()){
                return null;
            }
			
			Map<String,Object> sourceMap = new HashMap<>();
    		chartInfoMap.put("source", sourceMap);
    		
		    sourceMap.put("series", series);
    		
			if(!categories.isEmpty()){
				sourceMap.put("categories", categories);
			}
			if(!labels.isEmpty()){
				sourceMap.put("labels", labels);
			}
			if(cacheFields.size()>1){
				sourceMap.put("cacheFields", cacheFields);
			}
			sourceMap.put("usAbs", true);
			
			chartInfoMap.put("seriesPosition", getSeriesPosition(series.get(0)));
			chartInfoMap.put("legendPosition", getLegend(ctChart));
			
			String xTitle = getXTitle(ctPlotArea);
			if(xTitle != null && xTitle.length()>0){
				chartInfoMap.put("xTitle", xTitle);
			}
			
			String yTitle = getYTitle(ctPlotArea);
			if(yTitle != null && yTitle.length()>0){
				chartInfoMap.put("yTitle", yTitle);
			}
			chartInfoMap.put("floorType", "chart");
			
			return chartInfoMap;
    		
    	}
    	
    	protected List<Object> parseF(String f){
			CellRangeAddress cellRangeAddress = CellRangeAddress.valueOf(f);
			List<Object> serie = new ArrayList<>();
			serie.add("");
			serie.add(cellRangeAddress.getFirstRow() + 1);
			serie.add(cellRangeAddress.getFirstColumn() + 1);
			serie.add(cellRangeAddress.getLastRow() + 1);
			serie.add(cellRangeAddress.getLastColumn() + 1);
			return serie;
    	}
    	
    	
    	
    	protected List<List<Object>> parseLabel(String f){
    		CellRangeAddress cellRangeAddress = CellRangeAddress.valueOf(f);
    		List<List<Object>> valueList = new ArrayList<>();
    		if(cellRangeAddress.getFirstRow() == cellRangeAddress.getLastRow()){
    			for(int i=0; i<= cellRangeAddress.getLastColumn()- cellRangeAddress.getFirstColumn();i++){
    				List<Object> val = new ArrayList<>();
    				val.add("");
    				val.add(cellRangeAddress.getFirstRow() + 1);
    				val.add(cellRangeAddress.getFirstColumn() + 1 +i);
    				val.add(cellRangeAddress.getLastRow() + 1);
    				val.add(cellRangeAddress.getLastColumn() + 1 + i);
    				valueList.add(val);
    			}
    		}else{
    			for(int i=0; i<= cellRangeAddress.getLastRow()- cellRangeAddress.getFirstRow();i++){
    				List<Object> val = new ArrayList<>();
    				val.add("");
    				val.add(cellRangeAddress.getFirstRow() + 1 + i);
    				val.add(cellRangeAddress.getFirstColumn() + 1);
    				val.add(cellRangeAddress.getLastRow() + 1 + i);
    				val.add(cellRangeAddress.getLastColumn() + 1 );
    				valueList.add(val);
    			}
    		}
			
			return valueList;
    	}
    	
    	
    	
    	private String getSeriesPosition(List<Object> serie){
    		if(serie.get(1).equals(serie.get(3))){
    			return "row";
    		}else{
    			return "col";
    		}
    	}
    	
    	private String getLegend(CTChart ctChart){
    		String legend = "right";
    		if(ctChart.isSetLegend() && ctChart.getLegend().isSetLegendPos() ){
    			String pos = ctChart.getLegend().getLegendPos().getVal().toString();
    			switch(pos){
    				case "T": legend = "top"; break;
    				case "B": legend = "bottom"; break;
    				case "L": legend = "left"; break;
    				case "R": 
    				default:	
    					legend = "right"; break;
    			}
    		}
    		return legend;
    	}
    	
    	protected String getCatTitle(CTPlotArea ctPlotArea){
    		if(! ctPlotArea.getCatAxList().isEmpty()){
    			CTCatAx ctCatAx=ctPlotArea.getCatAxList().get(0);
    			if(ctCatAx.isSetTitle()){
    				CTTitle ctTitle = ctCatAx.getTitle();
    				StringBuffer text = new StringBuffer();
    				XmlObject[] t = ctTitle
    					.selectPath("declare namespace a='"+"http://schemas.openxmlformats.org/drawingml/2006/main"+"' .//a:t");
    				for (int m = 0; m < t.length; m++) {
    					NodeList kids = t[m].getDomNode().getChildNodes();
    					for (int n = 0; n < kids.getLength(); n++) {
    						if (kids.item(n) instanceof Text) {
    							text.append(kids.item(n).getNodeValue());
    						}
    					}
    				}
    				return text.toString();
    			}
    		}
    		return null;
    	}
    	
    	protected String getValTitle(CTPlotArea ctPlotArea){
    		if(! ctPlotArea.getValAxList().isEmpty()){
    			CTValAx ctValAx = ctPlotArea.getValAxList().get(0);
    			if(ctValAx.isSetTitle()){
    				CTTitle ctTitle = ctValAx.getTitle();
    				StringBuffer text = new StringBuffer();
    				XmlObject[] t = ctTitle
    					.selectPath("declare namespace a='"+"http://schemas.openxmlformats.org/drawingml/2006/main"+"' .//a:t");
    				for (int m = 0; m < t.length; m++) {
    					NodeList kids = t[m].getDomNode().getChildNodes();
    					for (int n = 0; n < kids.getLength(); n++) {
    						if (kids.item(n) instanceof Text) {
    							text.append(kids.item(n).getNodeValue());
    						}
    					}
    				}
    				return text.toString();
    			}
    		}
    		return null;
    	}
    	
		protected String getXTitle(CTPlotArea ctPlotArea) {
			return getCatTitle(ctPlotArea);
		}

		protected String getYTitle(CTPlotArea ctPlotArea) {
			return getValTitle(ctPlotArea);
		}
    	
    }
    
    private static class BarChartProcessor extends ChartInfoProcessor{

		@Override
		boolean isProcessable(CTChart ctChart) {
			CTPlotArea ctPlotArea = ctChart.getPlotArea();
			if(ctPlotArea.getBarChartList() != null && !ctPlotArea.getBarChartList().isEmpty()){
				return true;
			}else{
				return false;
			}
		}

		@Override
		String getChartType(CTPlotArea ctPlotArea) {
			CTBarChart ctBarChart = ctPlotArea.getBarChartList().get(0);
			if("col".equals(ctBarChart.getBarDir().getVal().toString())){
				return "column";
			}else{
				return "bar";
			}
		}


		@Override
		void setSourceFields(CTPlotArea ctPlotArea,List<List<Object>> series,List<List<Object>> categories, List<List<Object>> labels,List<Map<String,Object>> cacheFields){
			CTBarChart ctBarChart = ctPlotArea.getBarChartList().get(0);
			String labelRef = null;
			for(CTBarSer ctBarSer: ctBarChart.getSerList()){
			    if(ctBarSer.getVal().isSetNumRef() &&  ctBarSer.getVal().getNumRef().getF()!=null){
    				String valRef = ctBarSer.getVal().getNumRef().getF();
    				List<Object> serie = parseF(valRef);
    				series.add(serie);
    				if(ctBarSer.isSetTx() && ctBarSer.getTx().isSetStrRef()){
    					String categoryRef= ctBarSer.getTx().getStrRef().getF();
    					List<Object> category = parseF(categoryRef);
    					categories.add(category);
    					if(ctBarSer.getTx().getStrRef().isSetStrCache()){
    						List<CTStrVal>  ptList = ctBarSer.getTx().getStrRef().getStrCache().getPtList();
    						if(!ptList.isEmpty()){
    							String field = ptList.get(0).getV();
    							Map<String,Object> fieldMap = new HashMap<>();
    							fieldMap.put("name", field);
    							cacheFields.add(fieldMap);
    						}
    					}
    				}
    				if(labelRef ==null && ctBarSer.isSetCat() && ctBarSer.getCat().isSetStrRef()){
    					labelRef = ctBarSer.getCat().getStrRef().getF();
    				}
			    }
				
			}
			if(labelRef != null){
				labels.addAll(parseLabel(labelRef));
			}
			
		}

		@Override
		protected String getXTitle(CTPlotArea ctPlotArea) {
			CTBarChart ctBarChart = ctPlotArea.getBarChartList().get(0);
			if("col".equals(ctBarChart.getBarDir().getVal().toString())){
				return getCatTitle(ctPlotArea);
			}else{
				return getValTitle(ctPlotArea);
			}
		}

		@Override
		protected String getYTitle(CTPlotArea ctPlotArea) {
			CTBarChart ctBarChart = ctPlotArea.getBarChartList().get(0);
			if("col".equals(ctBarChart.getBarDir().getVal().toString())){
				return getValTitle(ctPlotArea);
			}else{
				return getCatTitle(ctPlotArea);
			}
		}
    	
    }
    
    
    private static class LineChartProcessor extends ChartInfoProcessor{

        @Override
        boolean isProcessable(CTChart ctChart) {
            CTPlotArea ctPlotArea = ctChart.getPlotArea();
            if(ctPlotArea.getLineChartList() != null && !ctPlotArea.getLineChartList().isEmpty()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        String getChartType(CTPlotArea ctPlotArea) {
            return "line";
        }

        @Override
        void setSourceFields(CTPlotArea ctPlotArea, List<List<Object>> series,
                List<List<Object>> categories, List<List<Object>> labels,
                List<Map<String, Object>> cacheFields) {
            CTLineChart ctLineChart = ctPlotArea.getLineChartList().get(0);
            String labelRef = null;
            for(CTLineSer ctLineSer: ctLineChart.getSerList()){
                if(ctLineSer.getVal().isSetNumRef() &&  ctLineSer.getVal().getNumRef().getF()!=null){
                    String valRef = ctLineSer.getVal().getNumRef().getF();
                    List<Object> serie = parseF(valRef);
                    series.add(serie);
                    if(ctLineSer.isSetTx() && ctLineSer.getTx().isSetStrRef()){
                        String categoryRef= ctLineSer.getTx().getStrRef().getF();
                        List<Object> category = parseF(categoryRef);
                        categories.add(category);
                        if(ctLineSer.getTx().getStrRef().isSetStrCache()){
                            List<CTStrVal>  ptList = ctLineSer.getTx().getStrRef().getStrCache().getPtList();
                            if(!ptList.isEmpty()){
                                String field = ptList.get(0).getV();
                                Map<String,Object> fieldMap = new HashMap<>();
                                fieldMap.put("name", field);
                                cacheFields.add(fieldMap);
                            }
                        }
                    }
                    if(labelRef == null && ctLineSer.isSetCat() && ctLineSer.getCat().isSetStrRef()){
                        labelRef = ctLineSer.getCat().getStrRef().getF();
                    }
                }
                
            }
            if(labelRef != null){
				labels.addAll(parseLabel(labelRef));
			}
        }
        
    }
    
    private static class AreaChartProcessor extends ChartInfoProcessor{

        @Override
        boolean isProcessable(CTChart ctChart) {
            CTPlotArea ctPlotArea = ctChart.getPlotArea();
            if(ctPlotArea.getAreaChartList() != null && !ctPlotArea.getAreaChartList().isEmpty()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        String getChartType(CTPlotArea ctPlotArea) {
            return "area";
        }

        @Override
        void setSourceFields(CTPlotArea ctPlotArea, List<List<Object>> series,
                List<List<Object>> categories, List<List<Object>> labels,
                List<Map<String, Object>> cacheFields) {
            
            CTAreaChart ctAreaChart = ctPlotArea.getAreaChartList().get(0);
            String labelRef = null;
            for(CTAreaSer ctAreaSer: ctAreaChart.getSerList()){
                if(ctAreaSer.getVal().isSetNumRef() &&  ctAreaSer.getVal().getNumRef().getF()!=null){
                    String valRef = ctAreaSer.getVal().getNumRef().getF();
                    List<Object> serie = parseF(valRef);
                    series.add(serie);
                    if(ctAreaSer.isSetTx() && ctAreaSer.getTx().isSetStrRef()){
                        String categoryRef= ctAreaSer.getTx().getStrRef().getF();
                        List<Object> category = parseF(categoryRef);
                        categories.add(category);
                        if(ctAreaSer.getTx().getStrRef().isSetStrCache()){
                            List<CTStrVal>  ptList = ctAreaSer.getTx().getStrRef().getStrCache().getPtList();
                            if(!ptList.isEmpty()){
                                String field = ptList.get(0).getV();
                                Map<String,Object> fieldMap = new HashMap<>();
                                fieldMap.put("name", field);
                                cacheFields.add(fieldMap);
                            }
                        }
                    }
                    if(labelRef == null && ctAreaSer.isSetCat() && ctAreaSer.getCat().isSetStrRef()){
                        labelRef = ctAreaSer.getCat().getStrRef().getF();
                    }
                }
                
            }
            if(labelRef != null){
				labels.addAll(parseLabel(labelRef));
			}
            
        }
        
    }
    
    private static class ScatterChartProcessor extends ChartInfoProcessor{

        @Override
        boolean isProcessable(CTChart ctChart) {
            CTPlotArea ctPlotArea = ctChart.getPlotArea();
            if(ctPlotArea.getScatterChartList() != null && !ctPlotArea.getScatterChartList().isEmpty()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        String getChartType(CTPlotArea ctPlotArea) {
            return "scatter";
        }

        @Override
        void setSourceFields(CTPlotArea ctPlotArea, List<List<Object>> series,
                List<List<Object>> categories, List<List<Object>> labels,
                List<Map<String, Object>> cacheFields) {
            CTScatterChart ctScatterChart = ctPlotArea.getScatterChartList().get(0);
            String labelRef = null;
            for(CTScatterSer ctScatterSer: ctScatterChart.getSerList()){
                if(ctScatterSer.getYVal().isSetNumRef() &&  ctScatterSer.getYVal().getNumRef().getF()!=null){
                    String valRef = ctScatterSer.getYVal().getNumRef().getF();
                    List<Object> serie = parseF(valRef);
                    series.add(serie);
                    if(ctScatterSer.isSetTx() && ctScatterSer.getTx().isSetStrRef()){
                        String categoryRef= ctScatterSer.getTx().getStrRef().getF();
                        List<Object> category = parseF(categoryRef);
                        categories.add(category);
                        if(ctScatterSer.getTx().getStrRef().isSetStrCache()){
                            List<CTStrVal>  ptList = ctScatterSer.getTx().getStrRef().getStrCache().getPtList();
                            if(!ptList.isEmpty()){
                                String field = ptList.get(0).getV();
                                Map<String,Object> fieldMap = new HashMap<>();
                                fieldMap.put("name", field);
                                cacheFields.add(fieldMap);
                            }
                        }
                    }
                    if(labelRef == null && ctScatterSer.isSetXVal() && ctScatterSer.getXVal().isSetStrRef()){
                        labelRef =  ctScatterSer.getXVal().getStrRef().getF();
                    }
                }
            }
            if(labelRef != null){
				labels.addAll(parseLabel(labelRef));
			}
        }
            
    }
        
    
    private static class PieChartProcessor extends ChartInfoProcessor{
        
        @Override
        boolean isProcessable(CTChart ctChart) {
            CTPlotArea ctPlotArea = ctChart.getPlotArea();
            if(ctPlotArea.getPieChartList() != null && !ctPlotArea.getPieChartList().isEmpty()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        String getChartType(CTPlotArea ctPlotArea) {
            return "pie";
        }

        @Override
        void setSourceFields(CTPlotArea ctPlotArea, List<List<Object>> series,
                List<List<Object>> categories, List<List<Object>> labels,
                List<Map<String, Object>> cacheFields) {
            
            CTPieChart ctPieChart = ctPlotArea.getPieChartList().get(0);
            String labelRef = null;
            for(CTPieSer ctPieSer: ctPieChart.getSerList()){
                if(ctPieSer.getVal().isSetNumRef() &&  ctPieSer.getVal().getNumRef().getF()!=null){
                    String valRef = ctPieSer.getVal().getNumRef().getF();
                    List<Object> serie = parseF(valRef);
                    series.add(serie);
                    if(ctPieSer.isSetTx() && ctPieSer.getTx().isSetStrRef()){
                        String categoryRef= ctPieSer.getTx().getStrRef().getF();
                        List<Object> category = parseF(categoryRef);
                        categories.add(category);
                        if(ctPieSer.getTx().getStrRef().isSetStrCache()){
                            List<CTStrVal>  ptList = ctPieSer.getTx().getStrRef().getStrCache().getPtList();
                            if(!ptList.isEmpty()){
                                String field = ptList.get(0).getV();
                                Map<String,Object> fieldMap = new HashMap<>();
                                fieldMap.put("name", field);
                                cacheFields.add(fieldMap);
                            }
                        }
                    }
                    if(labelRef == null && ctPieSer.isSetCat() && ctPieSer.getCat().isSetStrRef()){
                        labelRef = ctPieSer.getCat().getStrRef().getF();
                    }
                }
            }
            if(labelRef != null){
				labels.addAll(parseLabel(labelRef));
			}
        }
        
    }
    
    private static class RadarChartProcessor extends ChartInfoProcessor{
        @Override
        boolean isProcessable(CTChart ctChart) {
            CTPlotArea ctPlotArea = ctChart.getPlotArea();
            if(ctPlotArea.getRadarChartList() != null && !ctPlotArea.getRadarChartList().isEmpty()){
                return true;
            }else{
                return false;
            }
        }

        @Override
        String getChartType(CTPlotArea ctPlotArea) {
            return "radar";
        }

        @Override
        void setSourceFields(CTPlotArea ctPlotArea, List<List<Object>> series,
                List<List<Object>> categories, List<List<Object>> labels,
                List<Map<String, Object>> cacheFields) {
            
            CTRadarChart ctRadarChart = ctPlotArea.getRadarChartList().get(0);
            String labelRef = null;
            for(CTRadarSer ctRadarSer: ctRadarChart.getSerList()){
                if(ctRadarSer.getVal().isSetNumRef() &&  ctRadarSer.getVal().getNumRef().getF()!=null){
                    String valRef = ctRadarSer.getVal().getNumRef().getF();
                    List<Object> serie = parseF(valRef);
                    series.add(serie);
                    if(ctRadarSer.isSetTx() && ctRadarSer.getTx().isSetStrRef()){
                        String categoryRef= ctRadarSer.getTx().getStrRef().getF();
                        List<Object> category = parseF(categoryRef);
                        categories.add(category);
                        if(ctRadarSer.getTx().getStrRef().isSetStrCache()){
                            List<CTStrVal>  ptList = ctRadarSer.getTx().getStrRef().getStrCache().getPtList();
                            if(!ptList.isEmpty()){
                                String field = ptList.get(0).getV();
                                Map<String,Object> fieldMap = new HashMap<>();
                                fieldMap.put("name", field);
                                cacheFields.add(fieldMap);
                            }
                        }
                    }
                    if(labelRef== null && ctRadarSer.isSetCat() && ctRadarSer.getCat().isSetStrRef()){
                        labelRef = ctRadarSer.getCat().getStrRef().getF();
                    }
                }
                
            }
            if(labelRef != null){
				labels.addAll(parseLabel(labelRef));
			}
        }
        
    }
    
    private static List<XSSFGraphicFrame> extractGraphicFrame(XSSFDrawing drawing){
    	 List<XSSFShape> shapes = drawing.getShapes();
    	 List<XSSFGraphicFrame> graphicFrames = new ArrayList<>();
    	 for(XSSFShape shape:shapes){
    		 if(shape instanceof XSSFGraphicFrame){
    			 graphicFrames.add((XSSFGraphicFrame)shape);
    		 }
    	 }
    	 return graphicFrames;
    }
    
    public static ReadEventSheetChart build(SheetImportContext sheetImportContext){
    	ReadEventSheetChart readEventSheetChart = new ReadEventSheetChart();
        XSSFReadEventSheet sheet = sheetImportContext.sheet();
        XSSFDrawing drawing = sheet.drawing();
        if(drawing != null){
            List<XSSFChart> xssfCharts = drawing.getCharts();
            List<XSSFGraphicFrame> graphicFrames = extractGraphicFrame(drawing);
            for(int i=0;i<xssfCharts.size();i++){
            	XSSFChart xssfChart = xssfCharts.get(i);
            	XSSFGraphicFrame graphicFrame = graphicFrames.get(i);
            	Map<String,Object> chartSetting = handleChart(xssfChart,graphicFrame,sheetImportContext);
            	if(chartSetting != null)
            	    readEventSheetChart._chartSettings.add(chartSetting);
            }
        }
        return readEventSheetChart;
    }
}
