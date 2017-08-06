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
package com.cubedrive.sheet.service.sheet._export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.cubedrive.base.domain.document.DocumentConfig;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;


public class XSSFWorkbookExportlet {

    private final SheetExportEnvironment _environment;
    
    private XSSFWriteEventListener _writeEventListener;

    public XSSFWorkbookExportlet(SheetExportEnvironment environment,XSSFWriteEventListener writeEventListener) {
        _environment = environment;
        _writeEventListener = writeEventListener;
    }
    
    public void export(OutputStream os) throws IOException{
    	//创建xssfworkbook工作簿
    	_writeEventListener.onStartWorkbook();
    	//在工作簿中插入页签<页签的命名规则为：按账簿分类和按模板分类>
    	iterSheetTab();
    	//这里与Excel文档相关的配置不存在，该方法并没有真正执行
    	iterDocumentConfig();
    	//excel生成完毕的后续工作，并没有什么后续动作，因为该方法也无对应的方法体。
    	_writeEventListener.onEndWorkbook();
	    _writeEventListener.writeOutputStream(os);
    }
    
    private void iterSheetTab(){
		List<SheetTab> tabs = _environment.documentFile().getSheetTabList();
        for(SheetTab tab:tabs){
            createWorksheet(tab);
        }
    }
    
    private void iterDocumentConfig(){
		/**
    	 * zhousr 2016-2-20 start 报表中没有documentConfig对应的数据
		 */
        //List<DocumentConfig> documentConfigList = _environment.documentConfigMapper().find(documentId);
        List<DocumentConfig> documentConfigList = new ArrayList<>();
		/**
    	 * zhousr 2016-2-20 end
		 */

        for(DocumentConfig documentConfig:documentConfigList){
            _writeEventListener.onWorkbookElement(documentConfig);
        }
    }
    
    private void createWorksheet(SheetTab sheetTab){
    	if("ExpFixData".equals(sheetTab.getOperateType())) {
    		/**
    		 * 报表导出-导出固定行报表的数据
    		 */
    		_writeEventListener.onStartSheet(sheetTab);
    		List<SheetCell> sheetCellList = _environment.documentFile().getXcSheetExpCommonService().getFixedCells(sheetTab);
    		for(SheetCell sheetCell : sheetCellList) {
    			_writeEventListener.onSheetCell(sheetCell);
    		}
            _writeEventListener.afterIterateSheetCell();
            List<SheetTabElement> tabElements =  _environment.documentFile().getXcSheetExpCommonService().findElementsByTabId(sheetTab.getTabId());
            for(SheetTabElement tabElement:tabElements){
                _writeEventListener.onSheetTabElement(tabElement);
            }
            _writeEventListener.afterIterateSheetTabElement();
            _writeEventListener.onEndSheet();
    	}else if("ExpModal".equals(sheetTab.getOperateType())){
    		_writeEventListener.onStartSheet(sheetTab);
            SheetCellIterator sheetCellIterator = new SheetCellIterator(sheetTab,_environment.sheetCellMapper(),50);
            for(;sheetCellIterator.hasNext();){
                SheetCell sheetCell = sheetCellIterator.next();
                _writeEventListener.onSheetCell(sheetCell);
            }
            _writeEventListener.afterIterateSheetCell();
            List<SheetTabElement> tabElements = _environment.sheetTabElementMapper().findElementsByTab(sheetTab.getId());
            for(SheetTabElement tabElement:tabElements){
                _writeEventListener.onSheetTabElement(tabElement);
            }
            _writeEventListener.afterIterateSheetTabElement();
            _writeEventListener.onEndSheet();
    	}
    }
}
