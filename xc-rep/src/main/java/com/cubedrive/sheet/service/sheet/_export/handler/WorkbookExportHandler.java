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

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.type.TypeReference;

import com.cubedrive.base.domain.document.DocumentConfig;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorkbookExportContext;

public class WorkbookExportHandler {

	private static final WorkbookExportHandler _instance = new WorkbookExportHandler();

	private WorkbookExportHandler() {

	}

	public void startWorkbook(XSSFWorkbookExportContext workbookExportContext) {
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
		SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(xssfWorkbook, 20, true);
		workbookExportContext.setWorkbook(xssfWorkbook, sxssfWorkbook);
	}
	
	public void addName(DocumentConfig documentConfig,XSSFWorkbookExportContext workbookExportContext){
	    List<Map<String,Object>> definedNameSettings = JsonUtil.fromJson(documentConfig.getJson(), new TypeReference<List<Map<String,Object>>>(){});
	    for(Map<String,Object> definedNameSetting : definedNameSettings){
		    String cal = (String)definedNameSetting.get("cal");
		    Integer scope =(Integer)definedNameSetting.get("scope");
    	    XSSFWorkbook xssfWorkbook = workbookExportContext.xssfWorkbook();
    	    XSSFName name = xssfWorkbook.createName();
    	    if(scope != null){
    	    	Integer tabOrder = workbookExportContext.sheetExportEnvironment().getTabOrder(scope);
    	    	if(tabOrder != null)
    	    		name.setSheetIndex(tabOrder - 1);
    	    }
    	    name.setNameName(documentConfig.getName());
    	    name.setRefersToFormula(cal);
	    }
	}
	

	public void endWorkbook(XSSFWorkbookExportContext workbookExportContext){
	    
	}
	
	public static WorkbookExportHandler instance(){
		return _instance;
	}
	
}
