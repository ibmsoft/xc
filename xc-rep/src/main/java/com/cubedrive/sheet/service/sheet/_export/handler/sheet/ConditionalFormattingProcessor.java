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

import org.apache.poi.hssf.util.CellReference;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCfRule;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTConditionalFormatting;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;

public class ConditionalFormattingProcessor extends WorksheetProcessor{
    
    private final Map<String,Object> _conditionalFormattingInfo;
    
    private ConditionalFormattingProcessor(Map<String,Object> conditionalFormattingInfo,XSSFWorksheetExportContext xssfWorksheetExportContext){
        super(xssfWorksheetExportContext);
        _conditionalFormattingInfo = conditionalFormattingInfo;
    }
    

    @SuppressWarnings("unchecked")
	@Override
    public void process() {
    	ConditionalFormattingRules.ConditionalFormattingRule conditionalFormattingRule = 
			ConditionalFormattingRules.getRule(_conditionalFormattingInfo);
    	if(conditionalFormattingRule != null){
    		String sqref;
    		List<Map<String,Object>> rngList = (List<Map<String,Object>>)_conditionalFormattingInfo.get("rng");
			List<Integer> firstSpan =(List<Integer>)rngList.get(0).get("span");
			String startRef = CellReference.convertNumToColString(firstSpan.get(2)-1)+firstSpan.get(1);
			if(firstSpan.size()>3){
				String endRef = CellReference.convertNumToColString(firstSpan.get(4)-1)+firstSpan.get(3);
				sqref = startRef+":"+endRef;
			}else{
				sqref = startRef;
			}
			CTWorksheet ctWorksheet = _xssfWorksheetExportContext.sheet().getXSSFSheet().getCTWorksheet();
			CTConditionalFormatting ctConditionalFormatting =ctWorksheet.addNewConditionalFormatting();
			try{
			List<String> sqrefList = new ArrayList<>();
			sqrefList.add(sqref);
			ctConditionalFormatting.setSqref(sqrefList);
			CTCfRule cfRule = ctConditionalFormatting.addNewCfRule();
			cfRule.setPriority(1);
			conditionalFormattingRule.applyRule(_conditionalFormattingInfo, _xssfWorksheetExportContext, cfRule);
			}catch(Exception ex){
				ctWorksheet.removeConditionalFormatting(ctWorksheet.sizeOfConditionalFormattingArray()-1);
			}
    	}
    	
        
    }
    
    public static ConditionalFormattingProcessor build(Map<String,Object> conditionalFormattingInfo,XSSFWorksheetExportContext xssfWorksheetExportContext){
    	ConditionalFormattingProcessor conditionalFormattingProcessor = 
    			new ConditionalFormattingProcessor(conditionalFormattingInfo,xssfWorksheetExportContext);
    	return conditionalFormattingProcessor;
    }
    
    

}
