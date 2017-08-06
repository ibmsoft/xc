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
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBar;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDxf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFont;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet;

import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.handler.sheet.ConditionalFormattingIcons.ConditionalFormattingIcon;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;
import com.google.common.collect.Lists;

public class ReadEventSheetConditionalFormatting {
	
	private SheetImportContext _sheetImportContext;
	
	private final List<Map<String,Object>> _cdtObjectList = new ArrayList<>();
	
	
	private ReadEventSheetConditionalFormatting(SheetImportContext sheetImportContext){
		_sheetImportContext = sheetImportContext;
	}
	
	public  List<Map<String,Object>> getCdtObjectList(){
	    return _cdtObjectList;
	}
	
	private void handleSheetConditionalFormattings(){
		XSSFReadEventSheet sheet = _sheetImportContext.sheet();
		List<CTConditionalFormatting> ctConditonalFormattingList = sheet.worksheet().getConditionalFormattingList();
		for(CTConditionalFormatting ctConditionalFormating:ctConditonalFormattingList){
		    List<Map<String,Object>> cdtObjects = doHandleConditionalFormatting(ctConditionalFormating);
		    _cdtObjectList.addAll(cdtObjects);
		}
	}
	
	@SuppressWarnings("unchecked")
    private List<Map<String,Object>> doHandleConditionalFormatting(CTConditionalFormatting ctConditionalFormating){
		List<String> sqref = ctConditionalFormating.getSqref();
		String[] refArray = sqref.get(0).split(" ");
		List<List<Map<String,Object>>> ranges = new ArrayList<>();
		for(String ref:refArray){
		    List<Map<String,Object>> rng = new ArrayList<>();
		    rng.add(getRange(ref));
		    ranges.add(rng);
		}
		List<CTCfRule> cfRuleList = ctConditionalFormating.getCfRuleList();
		List<Map<String,Object>>  cdtSettings = new ArrayList<>();
		for(CTCfRule ctCfRule:cfRuleList){
		    Map<String,Object> cdtSetting = getCdtSetting(ctCfRule);
		    if(cdtSetting != null)
		        cdtSettings.add(cdtSetting);
		}
		List<Map<String,Object>> cdtObjects = new ArrayList<>();
		for(Map<String,Object> cdtSetting:cdtSettings){
		    for(List<Map<String,Object>> rng:ranges){
		        Map<String,Object> cdtObject = new HashMap<>(cdtSetting);
		        cdtObject.put("rng", rng);
		        cdtObjects.add(cdtObject);
		    }
		}
		return cdtObjects;
	}
	
	private Map<String,Object> getRange(String ref){
		CellRangeAddress cellRangeAddress = CellRangeAddress.valueOf(ref);
		int sheetId = _sheetImportContext.getTabId();
		int startRowIndex =  cellRangeAddress.getFirstRow() + 1;
		int startColumnIndex = cellRangeAddress.getFirstColumn() + 1;
		int endRowIndex = cellRangeAddress.getLastRow() + 1;
		int endColumnIndex = cellRangeAddress.getLastColumn() + 1;
		Map<String,Object> rng = new HashMap<>();
		rng.put("span", Lists.newArrayList(sheetId,startRowIndex,startColumnIndex,endRowIndex,endColumnIndex));
		rng.put("type", 1);
		return rng;
	}
	
	private Map<String,Object> getCdtSetting(CTCfRule ctCfRule){
	    String name = null;
	    Map<String,Object> optMap = null;
	    String type = ctCfRule.getType().toString();
	    
	    switch(type){
    	    case "cellIs":{
    	        optMap = cellIsOpt(ctCfRule);
    	        if(optMap != null)
    	            name = "boolstyle";
    	        break;
    	    }
    	    case "containsText":{
    	        if("containsText".equals(ctCfRule.getOperator().toString())){
        	        optMap = new HashMap<>();
        	        optMap.put("type", "include");
        	        optMap.put("base", ctCfRule.getText());
        	        name = "boolstyle";
    	        }
    	        break;
    	    }
    	    case "timePeriod":{
    	        optMap = timePeriodOpt(ctCfRule);
    	        if(optMap != null)
    	            name = "boolstyle";
    	        break;
    	    }
    	    case "duplicateValues":{
    	        optMap = new HashMap<>();
    	        optMap.put("type", "repeat");
    	        optMap.put("base", 0);
    	        name = "boolstyle";
    	        break;
    	    }
    	    case "uniqueValues":{
    	        optMap = new HashMap<>();
    	        optMap.put("type", "repeat");
    	        optMap.put("base", 1);
    	        name = "boolstyle";
    	        break;
    	    }
    	    case "top10":{
    	        optMap = top10Opt(ctCfRule);
    	        if(optMap != null)
    	            name = "boolstyle";
    	        break;
    	    }
    	    case "aboveAverage":{
    	        optMap = new HashMap<>();
    	        optMap.put("type", "average");
    	        optMap.put("base", ctCfRule.isSetAboveAverage() && !ctCfRule.getAboveAverage() ? 1 : 0);
    	        name = "boolstyle";
    	        break;
    	    }
    	    case "dataBar":{
    	        if(ctCfRule.isSetDataBar()){
    	            name = "colorbar";
    	            CTDataBar ctDataBar = ctCfRule.getDataBar();
    	            CTColor dataBarColor = ctDataBar.getColor();
        	        XSSFColor xssfColor = new XSSFColor(dataBarColor);
        	        if(dataBarColor.isSetTheme()){
        	        	_sheetImportContext.parentContext().workbook().theme().inheritFromThemeAsRequired(xssfColor);
        	        }
        	        byte[] rgb = xssfColor.getRGB();
        	        if(rgb != null){
        	        	optMap = new HashMap<>();
	        	        optMap.put("pos",SpreadsheetImportHelper.colorBytesToRgbFormat(rgb, true));
	        	        optMap.put("neg", SpreadsheetImportHelper.colorBytesToRgbFormat(rgb, false));
        	        }
        	        if(ctDataBar.sizeOfCfvoArray() == 2 ){
        	        	CTCfvo ctCfvo_min= ctDataBar.getCfvoArray(0);
        	        	CTCfvo ctCfvo_max = ctDataBar.getCfvoArray(1);
        	        	if(ctCfvo_min.isSetVal()){
        	        		String min = ctCfvo_min.getVal();
        	        		optMap.put("mintype", ctCfvo_min.getType().toString());
        	        		optMap.put("min", min);
        	        	}
        	        	if(ctCfvo_max.isSetVal()){
        	        		String max = ctCfvo_max.getVal();
        	        		optMap.put("maxtype", ctCfvo_max.getType().toString());
        	        		optMap.put("max", max);
        	        		
        	        	}
        	        }
        	        
    	        }
    	        break;
    	    }
    	    case "colorScale":{
    	        if(ctCfRule.isSetColorScale()){
    	            name = "colorgrad";
        	        optMap = new HashMap<>();
        	        int colorListSize = ctCfRule.getColorScale().getColorList().size();
        	        if(colorListSize > 0){
        	        	CTColor ctColor0 = ctCfRule.getColorScale().getColorArray(0);
        	        	XSSFColor xssfColor0 =new XSSFColor(ctColor0);
        	        	if(ctColor0.isSetTheme()){
        	        		_sheetImportContext.parentContext().workbook().theme().inheritFromThemeAsRequired(xssfColor0);
        	        	}
	        	        byte[] startColorBytes = xssfColor0.getRGB();
	        	        optMap.put("start", SpreadsheetImportHelper.colorBytesToInts(startColorBytes));
        	        }
        	        if(colorListSize > 1){
        	        	CTColor ctColor1 = ctCfRule.getColorScale().getColorArray(1);
        	        	XSSFColor xssfColor1 = new XSSFColor(ctColor1);
        	        	if(ctColor1.isSetTheme()){
        	        		_sheetImportContext.parentContext().workbook().theme().inheritFromThemeAsRequired(xssfColor1);
        	        	}
	        	        byte[] stopColorBytes = xssfColor1.getRGB();
	        	        optMap.put("stop", SpreadsheetImportHelper.colorBytesToInts(stopColorBytes));
        	        }
        	        if(colorListSize >2){
        	        	CTColor ctColor2 = ctCfRule.getColorScale().getColorArray(2);
        	        	XSSFColor xssfColor2 = new XSSFColor(ctColor2);
        	        	if(ctColor2.isSetTheme()){
        	        		_sheetImportContext.parentContext().workbook().theme().inheritFromThemeAsRequired(xssfColor2);
        	        	}
	        	        byte[] endColorBytes = xssfColor2.getRGB();
	        	        optMap.put("end", SpreadsheetImportHelper.colorBytesToInts(endColorBytes));
        	        }
    	        }
    	        break;
    	    }
    	    case "iconSet":{
    	    	optMap = iconSetOpt(ctCfRule);
    	    	if(optMap != null)
    	            name = "iconset";
    	        break;
    	    }
    	    
    	    
    	    
	    }
	    if(name != null && optMap != null){
	        if("boolstyle".equals(name)){
    	        StylesTable styles = _sheetImportContext.parentContext().workbook().stylesSource();
    	        CTDxf ctDxf = styles.getDxfAt((int)ctCfRule.getDxfId());
    	        if(ctDxf != null){
    	            Map<String,Object> stylesMap = new HashMap<>();
        	        if(ctDxf.isSetFont()){
        	            CTFont ctFont = ctDxf.getFont();
        	            if(!ctFont.getColorList().isEmpty()){
        	            	CTColor fontCTColor = ctFont.getColorList().get(0);
        	                XSSFColor fontColor = new XSSFColor(fontCTColor);
        	                if(fontCTColor.isSetTheme()){
        	                	_sheetImportContext.parentContext().workbook().theme().inheritFromThemeAsRequired(fontColor);
        	                }
        	                String rgb = SpreadsheetImportHelper.colorBytesToRgbFormat(fontColor.getRGB(), true);
        	                if(rgb == null){
        	                    rgb = SpreadsheetImportHelper.cdt_default_ccolor;
        	                }
        	                stylesMap.put("ccolor", rgb);
        	            }
        	        }
        	        if(ctDxf.isSetFill()){
        	            if(ctDxf.getFill().isSetPatternFill() 
        	                    && (ctDxf.getFill().getPatternFill().isSetBgColor() || ctDxf.getFill().getPatternFill().isSetFgColor())){
        	                CTColor bgColor = null;
        	                if(ctDxf.getFill().getPatternFill().isSetBgColor()){
        	                    bgColor = ctDxf.getFill().getPatternFill().getBgColor();
        	                }
        	                if(bgColor == null && ctDxf.getFill().getPatternFill().isSetFgColor()){
        	                    bgColor = ctDxf.getFill().getPatternFill().getFgColor();
        	                }
        	                if(bgColor != null){
        	                	XSSFColor bgXssfColor = new XSSFColor(bgColor);
        	                	if(bgColor.isSetTheme()){
        	                		_sheetImportContext.parentContext().workbook().theme().inheritFromThemeAsRequired(bgXssfColor);
        	                	}
        	                    String rgb = SpreadsheetImportHelper.colorBytesToRgbFormat(bgXssfColor.getRGB(), true);
        	                    if(rgb == null){
        	                        rgb = SpreadsheetImportHelper.cdt_default_cbgc;
        	                    }
        	                    stylesMap.put("cbgc", rgb);
        	                }
        	            }
        	        }
        	        if(!stylesMap.isEmpty()){
        	            optMap.put("style", stylesMap);
        	        }
    	        }
	        }
	        Map<String,Object> cdtMap = new HashMap<>();
	        cdtMap.put("name", name);
	        cdtMap.put("opt", optMap);
	        return cdtMap;
	    }
	    return null;
	}
	
	private Map<String,Object> cellIsOpt(CTCfRule ctCfRule){
	    String type = null;
	    Object base = null;
	    String operator =  ctCfRule.getOperator().toString();
	    switch(operator){
    	    case "greaterThan":{
    	        type = "greater";
    	        base = ctCfRule.getFormulaArray(0);
    	        break;
    	    }
    	    case "lessThan":{
    	        type = "less";
    	        base = ctCfRule.getFormulaArray(0);
    	        break;
    	    }
    	    case "between":{
    	        type = "between";
    	        Map<String,String> betweenMap = new HashMap<>();
    	        betweenMap.put("min", ctCfRule.getFormulaArray(0));
    	        betweenMap.put("max", ctCfRule.getFormulaArray(1));
    	        base = betweenMap;
    	        break;
    	    }
    	    case "equal":{
    	        type = "equal";
    	        base = ctCfRule.getFormulaArray(0);
    	        break;
    	    }
    	    default:
    	        break;
	    }
	    if(type != null && base != null){
	        Map<String,Object> optMap = new HashMap<>();
	        optMap.put("type", type);
	        optMap.put("base", base);
	        return optMap;
	    }
	    return null;
	}
	
	private Map<String,Object> timePeriodOpt(CTCfRule ctCfRule){
	    if(ctCfRule.isSetTimePeriod()){
	        Map<String,Object> optMap = new HashMap<>();
	        int base = -1;
	        String timePeriod = ctCfRule.getTimePeriod().toString();
	        switch(timePeriod){
	            case "yesterday":base = 0;break;
	            case "today": base =1; break;
	            case "tomorrow": base = 2; break;
	            case "last7Days": base = 3;break;
	            case "lastWeek": base = 4;break;
	            case "thisWeek": base = 5;break;
	            case "nextWeek": base = 6;break;
	            case "lastMonth": base =7;break;
	            case "thisMonth": base =8;break;
	            case "nextMonth": base =9;break;
	        }
	        
	        if(base !=-1){
	            optMap.put("type", "date");
	            optMap.put("base", base);
	            return optMap;
	        }
	        
	    }
	    return null;
	    
	    
	}
	
	private Map<String,Object> top10Opt(CTCfRule ctCfRule){
	    if(ctCfRule.isSetRank()){
	        Map<String,Object> optMap = new HashMap<>();
	        optMap.put("base", ctCfRule.getRank());
	        String type;
	        if(ctCfRule.isSetPercent()){
	            if(ctCfRule.isSetBottom())
	                type = "bottom";
	            else
	                type = "top";
	        }else{
	            if(ctCfRule.isSetBottom())
	                type = "min";
	            else
	                type = "max";
	        }
	        optMap.put("type", type);
	        return optMap;
	    }
	    return null;
	}
	
	private Map<String,Object> iconSetOpt(CTCfRule ctCfRule){
		Map<String,Object> optMap = null;
		CTIconSet  ctIconSet= ctCfRule.getIconSet();
		String iconSetName = ctIconSet.getIconSet().toString();
		ConditionalFormattingIcon cfIcon = ConditionalFormattingIcons.getIconByName(iconSetName);
		if(cfIcon != ConditionalFormattingIcons.dummy_icon){
			boolean rv = ctIconSet.isSetReverse() && ctIconSet.getReverse();
			boolean only = (ctIconSet.isSetShowValue()) && !ctIconSet.getShowValue();
			List<Map<String,Object>> ths = new ArrayList<>();
			for(int i=1; i<cfIcon.getLevel(); i++){
				CTCfvo ctCfvo = ctIconSet.getCfvoArray(i);
				Map<String,Object> thsMap = new HashMap<>();
				thsMap.put("v", ctCfvo.getVal());
				thsMap.put("c", (ctCfvo.isSetGte()) && !ctCfvo.getGte() ? "<" :"<=");
				thsMap.put("u", "percent".equals(ctCfvo.getType().toString()) ? "percent" : "number");
				ths.add(thsMap);
			}
			int cfIconLevel = cfIcon.getLevel();
			int[] icons = new int[cfIconLevel];
			for(int i=0; i<cfIconLevel; i++){
				icons[i] = i;
			}
			
			Map<String,Object> iconSetMap = new HashMap<>();
			iconSetMap.put("set", cfIcon.getSet());
			iconSetMap.put("level", cfIconLevel);
			iconSetMap.put("icons",icons);
			optMap = new HashMap<>();
			optMap.put("is", iconSetMap);
			optMap.put("rv", rv);
			optMap.put("only", only);
			optMap.put("ths", ths);
			
		}
		return optMap;
	}
	
	
	
	
	

	public static ReadEventSheetConditionalFormatting build(SheetImportContext sheetImportContext){
		ReadEventSheetConditionalFormatting conditionalFormatting = new ReadEventSheetConditionalFormatting(sheetImportContext);
		conditionalFormatting.handleSheetConditionalFormattings();
		return conditionalFormatting;
		
	}
}
