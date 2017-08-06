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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.util.CellReference;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfvo;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColorScale;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDataBar;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTIconSet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STConditionalFormattingOperator;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STTimePeriod;

import com.cubedrive.sheet.service.sheet._export.SpreadsheetExportHelper;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;

class ConditionalFormattingRules {
    
    private static final Map<String,ConditionalFormattingRule> type_rules =  new HashMap<>();
    
    private static final DataBarRule dataBarRule = new DataBarRule();
    
    private static final ColorScaleRule colorScaleRule = new ColorScaleRule();
    
    private static final IconSetRule iconSetRule = new IconSetRule();
    
    static{
        type_rules.put("greater", new GreaterThanRule());
        type_rules.put("less", new LessThanRule());
        type_rules.put("between", new BetweenRule());
        type_rules.put("equal", new EqualRule());
        type_rules.put("include", new ContainsTextRule());
        type_rules.put("date", new TimePeriodRule());
        type_rules.put("repeat", new RepeatRule());
        type_rules.put("max", new TopRule());
        type_rules.put("top", new TopPercentRule());
        type_rules.put("min", new BottomRule());
        type_rules.put("bottom", new BottomPercentRule());
        type_rules.put("average", new AverageRule());
    }
    
    static ConditionalFormattingRule getRule(Map<String,Object> conditionalFormattingInfo){
    	ConditionalFormattingRule rule = null;
    	String name = (String)conditionalFormattingInfo.get("name");
    	if("boolstyle".equals(name)){
    		Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
    		String type = (String)optionMap.get("type");
    		rule = type_rules.get(type);
    	}else if("colorbar".equals(name)){
    		rule = dataBarRule;
    	}else if("colorgrad".equals(name)){
    		rule = colorScaleRule;
    	}else if("iconset".equals(name)){
    		rule = iconSetRule;
    	}
    	return rule;
    }
    
    
    static interface ConditionalFormattingRule{
        
        public void applyRule(Map<String,Object> conditionalFormattingInfo,XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule);
        
    }
    
    static abstract class AbstractConditionalFormattingRule implements ConditionalFormattingRule{
        
    	@SuppressWarnings("unchecked")
        protected Integer getDxfId(Map<String, Object> conditionalFormattingInfo,
        		XSSFWorksheetExportContext xssfWorksheetExportContext){
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
            Map<String,Object> styleMap = (Map<String,Object>) optionMap.get("style");
            Map<String,Object> dxfMap = new SpreadsheetExportHelper.ExtHashMap<>();
            if(styleMap.containsKey("cbgc"))
                dxfMap.put("cbgc", styleMap.get("cbgc"));
            if(styleMap.containsKey("ccolor"))
                dxfMap.put("ccolor", styleMap.get("ccolor"));
            
            if(dxfMap.isEmpty())
                return null;
            else
                return xssfWorksheetExportContext.parentContext().getOrCreateDxf(dxfMap);
        }
        
    	protected String getStartRef(Map<String, Object> conditionalFormattingInfo){
    		List<Map<String,Object>> rngList = (List<Map<String,Object>>)conditionalFormattingInfo.get("rng");
			List<Integer> firstSpan =(List<Integer>)rngList.get(0).get("span");
			String startRef = CellReference.convertNumToColString(firstSpan.get(2)-1)+firstSpan.get(1);
			return startRef;
    	}
        
        public void applyRule(Map<String,Object> conditionalFormattingInfo,
        		XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule){
        	Integer dxfId = getDxfId(conditionalFormattingInfo,xssfWorksheetExportContext);
            if(dxfId != null){
                cfRule.setDxfId(dxfId);
            }
            doApplyRule(conditionalFormattingInfo,xssfWorksheetExportContext,cfRule);
        }
        
        protected abstract void doApplyRule(Map<String,Object> conditionalFormattingInfo,
        		XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule);
    }
    
    static class GreaterThanRule extends AbstractConditionalFormattingRule{

        @Override
        @SuppressWarnings("unchecked")
        protected void doApplyRule(Map<String, Object> conditionalFormattingInfo, 
                XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule) {
        	Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
            String base =(String) optionMap.get("base");
            cfRule.setOperator(STConditionalFormattingOperator.GREATER_THAN);
            cfRule.setType(STCfType.CELL_IS);
            cfRule.addNewFormula().setStringValue(base);
        }
    }
    
    static class LessThanRule extends AbstractConditionalFormattingRule{

        @Override
        @SuppressWarnings("unchecked")
        protected void doApplyRule(Map<String, Object> conditionalFormattingInfo, 
                XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule) {
        	Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
            String base = (String)optionMap.get("base");
            cfRule.setOperator(STConditionalFormattingOperator.LESS_THAN);
            cfRule.setType(STCfType.CELL_IS);
            cfRule.addNewFormula().setStringValue(base);
        }
        
        
    }
    
    static class BetweenRule extends AbstractConditionalFormattingRule {

		@Override
		@SuppressWarnings("unchecked")
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			Map<String,String> betweenMap = (Map<String,String>)optionMap.get("base");
			String min = betweenMap.get("min");
			String max = betweenMap.get("max");
			cfRule.setOperator(STConditionalFormattingOperator.BETWEEN);
			cfRule.setType(STCfType.CELL_IS);
			cfRule.addNewFormula().setStringValue(min);
			cfRule.addNewFormula().setStringValue(max);
		}
    }
    
    static class EqualRule extends AbstractConditionalFormattingRule {

		@Override
		@SuppressWarnings("unchecked")
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo, 
				XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			String base = (String)optionMap.get("base");
			cfRule.setOperator(STConditionalFormattingOperator.EQUAL);
			cfRule.setType(STCfType.CELL_IS);
			cfRule.addNewFormula().setStringValue(base);
		}
    }
    
   static class ContainsTextRule extends AbstractConditionalFormattingRule{

    	private static final String contains_text_formula = "NOT(ISERROR(SEARCH(\"%s\",%s)))";
    	
		@Override
		@SuppressWarnings("unchecked")
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext,	CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			String base = (String)optionMap.get("base");
			String startRef = getStartRef(conditionalFormattingInfo);
			cfRule.setOperator(STConditionalFormattingOperator.CONTAINS_TEXT);
			cfRule.setType(STCfType.CONTAINS_TEXT);
			cfRule.setText(base);
			cfRule.addNewFormula().setStringValue(String.format(contains_text_formula, base,startRef));
		}
    	
    }
    
    static class TimePeriodRule extends AbstractConditionalFormattingRule{
    	
    	private static final String yesterday_formula = "FLOOR(%s,1)=TODAY()-1";
    	private static final String today_formula = "FLOOR(%s,1)=TODAY()";
    	private static final String tomorrow_formula = "FLOOR(%s,1)=TODAY()+1";
    	private static final String last_7_days_formula = "AND(TODAY()-FLOOR(%s,1)<=6,FLOOR(%s,1)<=TODAY())";
    	private static final String last_week_formula = "AND(TODAY()-ROUNDDOWN(%s,0)>=(WEEKDAY(TODAY())),TODAY()-ROUNDDOWN(%s,0)<(WEEKDAY(TODAY())+7))";
    	private static final String this_week_formula = "AND(TODAY()-ROUNDDOWN(%s,0)<=WEEKDAY(TODAY())-1,ROUNDDOWN(%s,0)-TODAY()<=7-WEEKDAY(TODAY()))";
    	private static final String next_week_formula = "AND(ROUNDDOWN(%s,0)-TODAY()>(7-WEEKDAY(TODAY())),ROUNDDOWN(%s,0)-TODAY()<(15-WEEKDAY(TODAY())))";
    	private static final String last_month_formula = "AND(MONTH(%s)=MONTH(TODAY())-1,OR(YEAR(%s)=YEAR(TODAY()),AND(MONTH(%s)=1,YEAR(%s)=YEAR(TODAY())-1)))";
    	private static final String this_month_formula = "AND(MONTH(%s)=MONTH(TODAY()),YEAR(%s)=YEAR(TODAY()))";
    	private static final String next_month_formula = "AND(MONTH(%s)=MONTH(TODAY())+1,OR(YEAR(%s)=YEAR(TODAY()),AND(MONTH(%s)=12,YEAR(%s)=YEAR(TODAY())+1)))";
    	
		@Override
		@SuppressWarnings("unchecked")
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			int base = ((Integer)optionMap.get("base")).intValue();
			String startRef = getStartRef(conditionalFormattingInfo);
			
			cfRule.setType(STCfType.TIME_PERIOD);
			STTimePeriod.Enum timePeriodEnum = null;
			String formulaText = null;
			switch(base){
				case 0:
					timePeriodEnum = STTimePeriod.YESTERDAY;
					formulaText = String.format(yesterday_formula, startRef);
					break;
				case 1:
					timePeriodEnum = STTimePeriod.TODAY;
					formulaText = String.format(today_formula, startRef);
					break;
				case 2:
					timePeriodEnum = STTimePeriod.TOMORROW;
					formulaText = String.format(tomorrow_formula, startRef);
					break;
				case 3:
					timePeriodEnum = STTimePeriod.LAST_7_DAYS;
					formulaText = String.format(last_7_days_formula, startRef,startRef);
					break;
				case 4:
					timePeriodEnum = STTimePeriod.LAST_WEEK;
					formulaText = String.format(last_week_formula, startRef, startRef);
					break;
				case 5:
					timePeriodEnum = STTimePeriod.THIS_WEEK;
					formulaText = String.format(this_week_formula, startRef, startRef);
					break;
				case 6:
					timePeriodEnum = STTimePeriod.NEXT_WEEK;
					formulaText = String.format(next_week_formula, startRef, startRef);
					break;
				case 7:
					timePeriodEnum = STTimePeriod.LAST_MONTH;
					formulaText = String.format(last_month_formula, startRef, startRef, startRef, startRef);
					break;
				case 8:
					timePeriodEnum = STTimePeriod.THIS_MONTH;
					formulaText = String.format(this_month_formula, startRef, startRef);
					break;
				case 9:
					timePeriodEnum = STTimePeriod.NEXT_MONTH;
					formulaText = String.format(next_month_formula, startRef, startRef, startRef, startRef);
					break;
			}
			if(timePeriodEnum != null && formulaText != null){
				cfRule.setTimePeriod(timePeriodEnum);
				cfRule.addNewFormula().setStringValue(formulaText);
			}
			
		}
    	
    }
    
    static class RepeatRule extends AbstractConditionalFormattingRule{

		@SuppressWarnings("unchecked")
		@Override
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo, 
				XSSFWorksheetExportContext xssfWorksheetExportContext,CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			int base = ((Integer)optionMap.get("base")).intValue();
			if(0 == base){ //repeat
				cfRule.setType(STCfType.DUPLICATE_VALUES);
			}else if(1==base){
				cfRule.setType(STCfType.UNIQUE_VALUES);
			}
			
		}
    	
    }
    
    static class TopRule extends AbstractConditionalFormattingRule{

		@SuppressWarnings("unchecked")
		@Override
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			Integer base = (Integer)optionMap.get("base");
			cfRule.setType(STCfType.TOP_10);
			cfRule.setRank(base);
		}
    	
    }
    
    static class TopPercentRule extends AbstractConditionalFormattingRule{

		@SuppressWarnings("unchecked")
		@Override
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			Integer base = (Integer)optionMap.get("base");
			cfRule.setType(STCfType.TOP_10);
			cfRule.setPercent(true);
			cfRule.setRank(base);
		}
    }
    
    static class BottomRule extends AbstractConditionalFormattingRule{
    	
    	@SuppressWarnings("unchecked")
		@Override
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext,CTCfRule cfRule) {
    		Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			Integer base = (Integer)optionMap.get("base");
			cfRule.setType(STCfType.TOP_10);
			cfRule.setBottom(true);
			cfRule.setRank(base);
		}
    	
    }
    
    static class BottomPercentRule extends AbstractConditionalFormattingRule{

		@SuppressWarnings("unchecked")
		@Override
		protected void doApplyRule(
				Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext,
				CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			Integer base = (Integer)optionMap.get("base");
			cfRule.setType(STCfType.TOP_10);
			cfRule.setBottom(true);
			cfRule.setPercent(true);
			cfRule.setRank(base);
		}
    	
    }
    
    static class AverageRule extends AbstractConditionalFormattingRule{

		@SuppressWarnings("unchecked")
		@Override
		protected void doApplyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext,CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			int base = ((Integer)optionMap.get("base")).intValue();
			cfRule.setType(STCfType.ABOVE_AVERAGE);
			if(1==base){
				cfRule.setAboveAverage(false);
			}
		}
    	
    }
    
    static class DataBarRule implements ConditionalFormattingRule{

		@SuppressWarnings("unchecked")
		@Override
		public void applyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext,	CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			cfRule.setType(STCfType.DATA_BAR);
			CTDataBar ctDataBar = cfRule.addNewDataBar();
			CTCfvo ctcfvo_min = ctDataBar.addNewCfvo();
			String mintype = (String)optionMap.get("mintype");
			if(mintype != null && !"min".equals(mintype)){
				ctcfvo_min.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.Enum.forString(mintype));
				String min = (String)optionMap.get("min");
				ctcfvo_min.setVal(min);
				
			}else{
				ctcfvo_min.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.MIN);
				ctcfvo_min.setVal("0");
			}
			
			CTCfvo ctcfvo_max = ctDataBar.addNewCfvo();
			String maxtype = (String)optionMap.get("maxtype");
			if(maxtype != null && !"max".equals(maxtype)){
				ctcfvo_max.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.Enum.forString(maxtype));
				String max = (String)optionMap.get("max");
				ctcfvo_max.setVal(max);
			}else{
				ctcfvo_max.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.MAX);
				ctcfvo_max.setVal("0");
			}
			String color = (String)optionMap.get("pos");
			CTColor ctColor = ctDataBar.addNewColor();
			byte[] rgb = SpreadsheetExportHelper.colorAsBytes(color);
			if(rgb != null){
				SpreadsheetExportHelper.setCTColorByRgb(ctColor, rgb);
			}
			
		}
    	
    }
    
    static class ColorScaleRule implements ConditionalFormattingRule{

		@SuppressWarnings("unchecked")
		@Override
		public void applyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext, CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			cfRule.setType(STCfType.COLOR_SCALE);
			CTColorScale ctColorScale = cfRule.addNewColorScale();
			CTCfvo ctcfvo_min = ctColorScale.addNewCfvo();
			ctcfvo_min.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.MIN);
			ctcfvo_min.setVal("0");
			CTCfvo ctcfvo_max = ctColorScale.addNewCfvo();
			ctcfvo_max.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.MAX);
			ctcfvo_max.setVal("0");
			List<Integer> startColor = (List<Integer>)optionMap.get("start"); 
			ctColorScale.addNewColor().setRgb(SpreadsheetExportHelper.colorAsBytes(startColor.toArray(new Integer[0])));
			List<Integer> stopColor = (List<Integer>)optionMap.get("stop");
			if(stopColor !=null)
			    ctColorScale.addNewColor().setRgb(SpreadsheetExportHelper.colorAsBytes(stopColor.toArray(new Integer[0])));
			List<Integer> endColor = (List<Integer>)optionMap.get("end");
			if(endColor != null)
				ctColorScale.addNewColor().setRgb(SpreadsheetExportHelper.colorAsBytes(endColor.toArray(new Integer[0])));
		}
    	
    }
    
    static class IconSetRule implements ConditionalFormattingRule{

		@Override
		public void applyRule(Map<String, Object> conditionalFormattingInfo,
				XSSFWorksheetExportContext xssfWorksheetExportContext,	CTCfRule cfRule) {
			Map<String,Object> optionMap = (Map<String,Object>)conditionalFormattingInfo.get("opt");
			Map<String,Object> iconSetMap = (Map<String,Object>)optionMap.get("is");
			Integer set, level;
			Boolean rv,only;
			if(iconSetMap != null){
				set = (Integer)iconSetMap.get("set");
				level = (Integer)iconSetMap.get("level");
			}else{
				set = (Integer)optionMap.get("set");
				level =(Integer)optionMap.get("level");
			}
			STIconSetType.Enum iconSetType = getIconSetTypeBy(set,level);
			
			if(iconSetType != null){
				cfRule.setType(STCfType.ICON_SET);
				CTIconSet ctIconSet = cfRule.addNewIconSet();
				ctIconSet.setIconSet(iconSetType);
				rv = (Boolean)optionMap.get("rv");
				if(rv != null && rv){
					ctIconSet.setReverse(true);
				}
				only = (Boolean)optionMap.get("only");
				if(only != null && only){
					ctIconSet.setShowValue(false);
				}
				List<Map<String,Object>> ths = (List<Map<String,Object>>) optionMap.get("ths");
				if(ths != null){
					setCfvoList(ctIconSet, ths);
				}else{
					setDefaultCfvoList(ctIconSet,level);
				}
			}
		}
		
		private STIconSetType.Enum getIconSetTypeBy(int set, int level){
			STIconSetType.Enum type = null;
			if(level ==3){
				switch(set){
					case 0: type = STIconSetType.X_3_ARROWS; break;
					case 1: type = STIconSetType.X_3_SYMBOLS_2; break;
					case 2: type = STIconSetType.X_3_SYMBOLS; break;
					case 5: type = STIconSetType.X_3_FLAGS; break;
					case 6: type = STIconSetType.X_3_ARROWS_GRAY; break;
//					case 7: type = STIconSetType.X_4_RATING; break;
					case 9: type =	STIconSetType.X_3_TRAFFIC_LIGHTS_2; break;
				}
			}else if(level ==4){
				switch(set){
					case 3: type = STIconSetType.X_4_RED_TO_BLACK; break;
					case 4: type = STIconSetType.X_4_TRAFFIC_LIGHTS; break;
				}
			}
			
			if(type == null)
				throw new IllegalStateException("Can't finld icon set, set:"+set+", level:"+level);
			return type;
		}
		
		private void setDefaultCfvoList(CTIconSet ctIconSet, int level){
			
			if(level == 3){
				CTCfvo ctCfvo1 = ctIconSet.addNewCfvo();
				ctCfvo1.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.PERCENT);
				ctCfvo1.setVal("0");
				
				CTCfvo ctCfvo2 = ctIconSet.addNewCfvo();
				ctCfvo2.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.PERCENT);
				ctCfvo2.setVal("33");
				
				CTCfvo ctCfvo3 = ctIconSet.addNewCfvo();
				ctCfvo3.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.PERCENT);
				ctCfvo3.setVal("67");
			}else if(level == 4){
				CTCfvo ctCfvo1 = ctIconSet.addNewCfvo();
				ctCfvo1.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.PERCENT);
				ctCfvo1.setVal("0");
				
				CTCfvo ctCfvo2 = ctIconSet.addNewCfvo();
				ctCfvo2.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.PERCENT);
				ctCfvo2.setVal("25");
				
				CTCfvo ctCfvo3 = ctIconSet.addNewCfvo();
				ctCfvo3.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.PERCENT);
				ctCfvo3.setVal("50");
				
				CTCfvo ctCfvo4 = ctIconSet.addNewCfvo();
				ctCfvo4.setType(org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.PERCENT);
				ctCfvo4.setVal("75");
			}
			
		}
		
		private void setCfvoList(CTIconSet ctIconSet, List<Map<String,Object>>thsList){
			int level = thsList.size() + 1;
			Map<String,Object> ths_0 = thsList.get(0);
			Map<String,Object> ths_1 = thsList.get(1);
			
			String firstUnit = (String)ths_0.get("u");
			
			CTCfvo ctCfvo1 = ctIconSet.addNewCfvo();
			ctCfvo1.setVal("0");
			ctCfvo1.setType(getSTCfvoTypeBy(firstUnit));
			
			CTCfvo ctCfvo2 = ctIconSet.addNewCfvo();
			ctCfvo2.setVal((String)ths_0.get("v"));
			ctCfvo2.setType(getSTCfvoTypeBy(firstUnit));
			if("<".equals(ths_0.get("c")))
				ctCfvo2.setGte(false);
			
			CTCfvo ctCfvo3 = ctIconSet.addNewCfvo();
			ctCfvo3.setVal((String)ths_1.get("v"));
			ctCfvo3.setType(getSTCfvoTypeBy((String)ths_1.get("u")));
			if("<".equals(ths_1.get("c")))
				ctCfvo3.setGte(false);
			
			if(level == 4){
				Map<String,Object> ths_2 = thsList.get(2);
				CTCfvo ctCfvo4 = ctIconSet.addNewCfvo();
				ctCfvo4.setVal((String)ths_2.get("v"));
				ctCfvo4.setType(getSTCfvoTypeBy((String)ths_2.get("u")));
				if("<".equals(ths_2.get("c")))
					ctCfvo4.setGte(false);
				
			}
		}
		
		private static org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.Enum getSTCfvoTypeBy(String unit){
			if("number".equals(unit)){
				return org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.NUM;
			}else{
				return org.openxmlformats.schemas.spreadsheetml.x2006.main.STCfvoType.PERCENT;
			}
		}
		
    }
    
    
   
    
    
    
    

}
