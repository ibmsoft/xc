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

import javax.xml.namespace.QName;

import org.apache.poi.ss.util.CellReference;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtensionList;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;

public class SparklineGroupProcessor extends WorksheetProcessor{
	
	private static final String x14_namespace = "http://schemas.microsoft.com/office/spreadsheetml/2009/9/main";
	private static final String xm_namesapce= "http://schemas.microsoft.com/office/excel/2006/main";
	
	private final List<SparklineGroupInfo> _sparklineGroupInfoList = new ArrayList<>();
	
	
	private SparklineGroupProcessor(XSSFWorksheetExportContext xssfWorksheetExportContext){
		super(xssfWorksheetExportContext);
		for(Map<String,Object> miniChartMap:xssfWorksheetExportContext.getMiniCharts()){
			String sheetName = xssfWorksheetExportContext.sheet().getSheetName();
			_sparklineGroupInfoList.add(new SparklineGroupInfo(sheetName,miniChartMap));
		}
	}
	
	@Override
	public void process() {
		CTWorksheet ctWorksheet = _xssfWorksheetExportContext.sheet().getXSSFSheet().getCTWorksheet();
		if(!ctWorksheet.isSetExtLst()){
			ctWorksheet.addNewExtLst();
		}
		CTExtensionList ctExtLst = ctWorksheet.getExtLst();
		CTExtension ctExtension = ctExtLst.addNewExt();
		
		XmlCursor cursor = ctExtension.newCursor();
		cursor.toNextToken();
		cursor.insertNamespace("x14", x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"uri"), "{05C60535-1F16-4fd2-B633-F4F36F0B64E0}");
		
		cursor.beginElement("sparklineGroups", x14_namespace);
		cursor.insertNamespace("xm", xm_namesapce);
		
		for(SparklineGroupInfo _sparklineGroupInfo:_sparklineGroupInfoList){
			addSparklineGroup(sparklineTypeByMinichart(_sparklineGroupInfo.getType()),_sparklineGroupInfo.getF(),_sparklineGroupInfo.getSqref(),cursor);
		}
		cursor.toEndToken();
		cursor.dispose();
		
		
	}
	
	private String sparklineTypeByMinichart(String miniChartType){
		String sparklineType = null;
		switch(miniChartType){
			case "gainloss": sparklineType = "stacked";break;
			case "column":sparklineType = "column";break;
			case "line":sparklineType = null;break;
		}
		return sparklineType;
	}
	
	private void addSparklineGroup(String type, String f, String sqref,XmlCursor cursor){
		cursor.beginElement("sparklineGroup",x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"displayEmptyCellsAs"), "gap");
		if(type != null){
			cursor.insertAttributeWithValue(new QName(null,"type"), type);
			if("stacked".equals(type)){
				cursor.insertAttributeWithValue(new QName(null,"negative"), "1");
			}
		}
		
		cursor.beginElement("colorSeries",x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"theme"), "4");
		cursor.insertAttributeWithValue(new QName(null,"tint"), "-0.499984740745262");
		cursor.toNextToken();
		
		cursor.beginElement("colorNegative", x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"theme"), "5");
		cursor.toNextToken();
		
		cursor.beginElement("colorAxis", x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"rgb"), "FF000000");
		cursor.toNextToken();
		
		cursor.beginElement("colorMarkers", x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"theme"), "4");
		cursor.insertAttributeWithValue(new QName(null,  "tint"), "499984740745262");
		cursor.toNextToken();
		
		cursor.beginElement("colorFirst", x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"theme"), "4");
		cursor.insertAttributeWithValue(new QName(null,"tint"), "39997558519241921");
		cursor.toNextToken();
		
		cursor.beginElement("colorLast", x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"theme"), "4");
		cursor.insertAttributeWithValue(new QName(null,"tint"), "39997558519241921");
		cursor.toNextToken();
		
		cursor.beginElement("colorHigh",x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"theme"), "4");
		cursor.toNextToken();
		
		cursor.beginElement("colorLow",x14_namespace);
		cursor.insertAttributeWithValue(new QName(null,"theme"), "4");
		cursor.toNextToken();
		
		cursor.beginElement("sparklines",x14_namespace);
		cursor.beginElement("sparkline", x14_namespace);
		cursor.insertElementWithText("f", xm_namesapce, f);
		cursor.insertElementWithText("sqref",xm_namesapce,sqref);
		cursor.toNextToken();
		cursor.toNextToken();
		cursor.toNextToken();
	}
	
	
	
	private static class SparklineGroupInfo {
		
		private final String _sheetName;
		private final Map<String,Object> _miniChartMap;
		
		private SparklineGroupInfo(String sheetName, Map<String,Object> miniChartMap){
			_sheetName = sheetName;
			_miniChartMap = miniChartMap;
		}
		
		String getType(){
			String type =(String)((Map<String,Object>)_miniChartMap.get("opt")).get("type");
			return type;
		}
		
		String getF(){
			List<Integer> span = (List<Integer>)((Map<String,Object>)((Map<String,Object>)_miniChartMap.get("opt")).get("base")).get("span");
			return formatSpan(span);
		}
		
		String getSqref(){
			List<Integer> span = (List<Integer>)((Map<String,Object>)((List<Map<String,Object>>)_miniChartMap.get("rng")).get(0)).get("span");
			return CellReference.convertNumToColString(span.get(2)-1) + span.get(1);
		}
		
		private String formatSpan(List<Integer> span){
			StringBuilder buf = new StringBuilder();
			buf.append(_sheetName);
			buf.append("!");
			buf.append(CellReference.convertNumToColString(span.get(2)-1));
			buf.append(span.get(1));
			buf.append(":");
			buf.append(CellReference.convertNumToColString(span.get(4)-1));
			buf.append(span.get(3));
			return buf.toString();
		}
	}

	public static SparklineGroupProcessor build(XSSFWorksheetExportContext xssfWorksheetExportContext){
		SparklineGroupProcessor processor = new SparklineGroupProcessor(xssfWorksheetExportContext);
		return processor;
	}
	
	

}
