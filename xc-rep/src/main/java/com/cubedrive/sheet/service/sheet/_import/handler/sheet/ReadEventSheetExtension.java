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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTExtension;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.sheet.service.sheet._import.XmlBeansHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;

public class ReadEventSheetExtension {
	
	public final static String EXTENSION_MINICHART = "minichart";
	
	private static final List<CTExtensionProcessor> extensionProcessors;
	
	static{
		extensionProcessors = new ArrayList<>();
		extensionProcessors.add(new SparklineProcessor());
		
	}
	
	private final List<ExtensionObject> _extensionObjectList = new ArrayList<>();
	
	private ReadEventSheetExtension(){
		
	}
	
	public List<ExtensionObject> getExtensionList(){
		return _extensionObjectList;
	}
	
	private static List<ExtensionObject> processExtension(CTExtension ctExtension,SheetImportContext sheetImportContext){
		for(CTExtensionProcessor ctExtensionProcessor:extensionProcessors){
			if(ctExtensionProcessor.accept(ctExtension,sheetImportContext)){
				return ctExtensionProcessor.process(ctExtension,sheetImportContext);
			}
		}
		return Collections.emptyList();
		
	}

	public static class ExtensionObject{
		private final String _name;
		private final Map<String,Object> _extensionSetting;
		
		private ExtensionObject(String name,Map<String,Object> extensionSetting){
			_name = name;
			_extensionSetting = extensionSetting;
		}
		
		public String getName(){
			return _name;
		}
		
		public Map<String,Object> getExtensionSetting(){
			return _extensionSetting;
		}
		
	}
	
	
	private abstract static interface CTExtensionProcessor {
		
	    boolean accept(CTExtension ctExtension,SheetImportContext sheetImportContext);
		
		List<ExtensionObject> process(CTExtension ctExtension,SheetImportContext sheetImportContext);
	}
	
	private static class SparklineProcessor implements CTExtensionProcessor{
		

		@Override
		public boolean accept(CTExtension ctExtension,SheetImportContext sheetImportContext) {
			XmlObject sparklineGroup = XmlBeansHelper.getElement(ctExtension, XmlBeansHelper.office14_spreadsheet_namespace, "$this//x14:sparklineGroup");
			if(sparklineGroup != null)
				return true;
			else
				return false;
		}

		@Override
		public List<ExtensionObject> process(CTExtension ctExtension,SheetImportContext sheetImportContext) {
	        List<XmlObject> sparklines = XmlBeansHelper.getXPathElements(ctExtension, XmlBeansHelper.office14_spreadsheet_namespace, "$this//x14:sparklineGroup");
	        List<ExtensionObject> miniChartList = new ArrayList<>();
	        for(XmlObject sparkline:sparklines){
	            String chartType = XmlBeansHelper.getAttributeValue(sparkline,"","type");
	            String cellRangeAddress = XmlBeansHelper.getElementValue(sparkline, XmlBeansHelper.office11_excel_namespace, "$this//xm:f");
	            String cellRef =  XmlBeansHelper.getElementValue(sparkline, XmlBeansHelper.office11_excel_namespace, "$this//xm:sqref");
	            if(chartType == null)
	            	chartType = "line";
	            Map<String,Object> miniChart = toMiniChart(sheetImportContext,chartType,cellRangeAddress,cellRef);
	            
	            miniChartList.add(new ExtensionObject(ReadEventSheetExtension.EXTENSION_MINICHART,miniChart));
	        }
	        
	        return miniChartList;
			
		}
		
		private Map<String,Object> toMiniChart(SheetImportContext sheetImportContext, String chartType,String cellRangeAddress, String cellRef){
			int sheetTabId = sheetImportContext.getTabId();
			Map<String,Object> miniChartSetting= new HashMap<>();
			miniChartSetting.put("name", "minichart");
			Map<String,Object> rng = new HashMap<>();
			CellReference cellRefObject = new CellReference(cellRef);
			rng.put("span", new int[]{sheetTabId,cellRefObject.getRow()+1,cellRefObject.getCol()+1, cellRefObject.getRow()+1,cellRefObject.getCol()+1});
			rng.put("type", 1);
			List<Map<String,Object>> rngList = new ArrayList<>();
			rngList.add(rng);
			miniChartSetting.put("rng", rngList);
			Map<String,Object> opt = new HashMap<>();
			Map<String,Object> base = new HashMap<>();
			CellRangeAddress cellRangeAddressObject = CellRangeAddress.valueOf(cellRangeAddress);
			base.put("span", new int[]{sheetTabId,cellRangeAddressObject.getFirstRow()+1,cellRangeAddressObject.getFirstColumn()+1,cellRangeAddressObject.getLastRow()+1,cellRangeAddressObject.getLastColumn()+1});
			base.put("type", 1);
			opt.put("base", base);
			switch(chartType){
				case "stacked":
					opt.put("type", "gainloss");
					opt.put("pc", "rgb(248,105,107)");
					opt.put("nc", "rgb(90,190,123)");
					break;
				case "column":
					opt.put("type", "column");
					opt.put("pc", "rgb(0,0,128)");
					opt.put("nc", "rgb(0,0,128)");
					break;
				default:
					opt.put("type", "line");
					opt.put("sc", "rgb(0,0,128)");
					break;
			}
			miniChartSetting.put("opt", opt);
			return miniChartSetting;
		}
		
	}
	
	
	public static ReadEventSheetExtension build(SheetImportContext sheetImportContext){
		ReadEventSheetExtension readEventSheetExtList = new ReadEventSheetExtension();
		CTWorksheet ctWorksheet = sheetImportContext.sheet().worksheet();
		if(ctWorksheet.isSetExtLst()){
			List<CTExtension> ctExtensionList= ctWorksheet.getExtLst().getExtList();
			for(CTExtension ctExtension:ctExtensionList){
				List<ExtensionObject> resultList= processExtension(ctExtension,sheetImportContext);
				if(!resultList.isEmpty()){
					readEventSheetExtList._extensionObjectList.addAll(resultList);
				}
			}
		}
		
		return readEventSheetExtList;
	}

}
