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
package com.cubedrive.sheet.service.sheet._import.handler;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBookView;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTDefinedName;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;

import com.cubedrive.base.domain.document.DocumentConfig;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.utils.DoSomethingUtil;
import com.cubedrive.base.utils.DoSomethingUtil.OnException;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventWorkbook;

public class WorkbookImportHandler {
    
    private static final WorkbookImportHandler _instance = new WorkbookImportHandler();
    
    private WorkbookImportHandler(){
        
    }

    public void startWorkbook(final WorkbookImportContext workbookImportContext) {
        DoSomethingUtil.doSomething(
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    saveSheetTab(workbookImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.raise;
                }
            },
            
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    loadWorkbookOptions(workbookImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.raise;
                }
            }, 
            
            new DoSomethingUtil.DoSomething(){
                public void doIt() {
                    saveDefinedName(workbookImportContext);
                }
                public OnException onException() {
                    return DoSomethingUtil.OnException.ignore;
                }
            }
         );
    }


    public void endWorkbook(WorkbookImportContext workbookImportContext) {

    }
    


    private void saveSheetTab(WorkbookImportContext workbookImportContext){
        XSSFReadEventWorkbook readEventWorkbook = workbookImportContext.workbook();
        CTWorkbook workbook = readEventWorkbook.workbook();
        int activeSheetId = -1;
        CTBookView bookView = workbook.getBookViews().getWorkbookViewArray(0);
        if(bookView.isSetActiveTab()){
            activeSheetId =  (int)bookView.getActiveTab();
        }
        DocumentFile documentFile = workbookImportContext.sheetImportEnvironment().documentFile();
        String cellTable = workbookImportContext.sheetImportEnvironment().cellTable();
        
        for(int i=0;i<workbook.getSheets().getSheetList().size();i++){
            CTSheet ctSheet = workbook.getSheets().getSheetArray(i);
            int tabOrder = i+1;
            int sheetId = (int)ctSheet.getSheetId();
            if(activeSheetId == -1){
                activeSheetId = (int)ctSheet.getSheetId();
            }
            SheetTab sheetTab = new SheetTab();
            sheetTab.setName(SpreadsheetImportHelper.toJavascriptCompatibleString(ctSheet.getName()));
            sheetTab.setDocumentFile(documentFile);
            sheetTab.setTabOrder(tabOrder);
            sheetTab.setCellTable(cellTable);
            sheetTab.setActive(activeSheetId == sheetId);
            /**
             * start
             * 2016-2-19 zhousr [注释ES原代码]注释掉ES向数据库中插入页签方法的调用，报表3.0没有页签表
             * 2016-2-19 zhousr [添加报表3.0新代码]给页签id赋值，ES中是在插入页签后通过数据库返回的页签id
             */
            //workbookImportContext.sheetImportEnvironment().sheetTabMapper().insertSheetTab(sheetTab);
            sheetTab.setId(documentFile.getMODALFORMAT_ID());
            /**
             * end
             * 2016-2-19 zhousr 
             */
            workbookImportContext.addSheetTab(sheetId, sheetTab);
        }
       
    }
    
    private void loadWorkbookOptions(WorkbookImportContext workbookImportContext){
    	XSSFReadEventWorkbook readEventWorkbook = workbookImportContext.workbook();
        CTWorkbook workbook = readEventWorkbook.workbook();
        if(workbook.isSetWorkbookPr()){
        	boolean date1904 = workbook.getWorkbookPr().isSetDate1904() && workbook.getWorkbookPr().getDate1904();
        	workbookImportContext.setDate1904(date1904);
        }
    }
    
    private void saveDefinedName(WorkbookImportContext workbookImportContext){
        XSSFReadEventWorkbook readEventWorkbook = workbookImportContext.workbook();
        CTWorkbook workbook = readEventWorkbook.workbook();
        DocumentFile documentFile = workbookImportContext.sheetImportEnvironment().documentFile();
                
        if(workbook.isSetDefinedNames()){
            Map<String,DefinedNameObject> definedNameObjects = new HashMap<>();
            for(CTDefinedName ctDefinedName: workbook.getDefinedNames().getDefinedNameList()){
                String name = ctDefinedName.getName();
                if(name.startsWith("_xlnm.")){
                	name = name.substring(6);
                }
                String cal =  ctDefinedName.getStringValue();;
                String cType = "named_func";
                int localSheetId = ctDefinedName.isSetLocalSheetId() ? (int)ctDefinedName.getLocalSheetId(): -1;
            	Map<String,Object> definedNameSetting = new HashMap<>();
            	definedNameSetting.put("cal", cal);
            	if(localSheetId !=-1){
            		SheetTab sheetTab = workbookImportContext.getSheetTabByOrder(localSheetId);
            		definedNameSetting.put("scope", sheetTab.getId());
            	}
                String key = name+cType;
                DefinedNameObject definedNameObject = definedNameObjects.get(key);
                if(definedNameObject == null){
                	definedNameObject = new DefinedNameObject(name, cType);
                	definedNameObjects.put(key, definedNameObject);
                }
                definedNameObject.values.add(definedNameSetting);
                workbookImportContext.addDefinedName(name);
            }
            
            if(definedNameObjects.size()>0){
            	List<DocumentConfig> definedNameList = new ArrayList<>();
            	for(DefinedNameObject definedNameObject:definedNameObjects.values()){
            		DocumentConfig definedName = new DocumentConfig();
	                definedName.setDocumentFile(documentFile);
	                definedName.setName(definedNameObject.name);
	                definedName.setJson(JsonUtil.toJson(definedNameObject.values));
	                definedName.setCtype(definedNameObject.cType);
	                
	                definedNameList.add(definedName);
            	}
                workbookImportContext.sheetImportEnvironment().documentConfigMapper().batchInsert(definedNameList);
            }
        }
    }
    
    public static WorkbookImportHandler instance(){
        return _instance;
    }
    
    
    private static class DefinedNameObject {
    	
    	private final String name;
    	private final String cType;
    	private final List<Map<String,Object>> values = new ArrayList<>();;
    	
    	private DefinedNameObject(String name,String cType){
    		this.name = name;
    		this.cType = cType;
    	}
    	
    	public boolean equals(Object o){
    		if(o == this)return true;
    		if(!(o instanceof DefinedNameObject))return false;
    		DefinedNameObject other = (DefinedNameObject)o;
    		return Objects.equals(name, other.name) && Objects.equals(cType, other.cType);
    	}
    	
    	public int hashCode(){
    		return Objects.hash(name,cType);
    	}
    }

}
