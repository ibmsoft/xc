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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentConfigMapper;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;

public class SheetExportEnvironment {

    private DocumentFile _documentFile;

//    private File _xlsxFile;

    private Locale _locale;

    private SheetTabMapper _sheetTabMapper;

    private SheetCellMapper _sheetCellMapper;

    private SheetTabElementMapper _sheetTabElementMapper;

    private DocumentConfigMapper _documentConfigMapper;
    
    private List<SheetTab> _sheetTabs = new ArrayList<>();

    private SheetExportEnvironment() {

    }

    public DocumentFile documentFile() {
        return _documentFile;
    }

//    public File xlsxFile() {
//        return _xlsxFile;
//    }

    public Locale locale() {
        return _locale;
    }

    public SheetTabMapper sheetTabMapper() {
        return _sheetTabMapper;
    }

    public SheetCellMapper sheetCellMapper() {
        return _sheetCellMapper;
    }

    public SheetTabElementMapper sheetTabElementMapper() {
        return _sheetTabElementMapper;
    }

    public DocumentConfigMapper documentConfigMapper() {
        return _documentConfigMapper;
    }
    
    public void addSheetTab(SheetTab sheetTab){
        _sheetTabs.add(sheetTab);
    }
    
    public String getSheetName(Integer sheetId){
        for(SheetTab _sheetTab:_sheetTabs){
            if(_sheetTab.getId().equals(sheetId)){
                return _sheetTab.getName();
            }
        }
        return null;
    }
    
    public Integer getTabOrder(Integer sheetId){
    	for(SheetTab _sheetTab:_sheetTabs){
            if(_sheetTab.getId().equals(sheetId)){
                return _sheetTab.getTabOrder();
            }
        }
        return null;
    }

    public static class SheetExportEnvironmentBuilder {

        private SheetExportEnvironment environment;

        public SheetExportEnvironmentBuilder(DocumentFile documentFile,Locale locale) {
            environment = new SheetExportEnvironment();
            environment._documentFile = documentFile;
//            environment._xlsxFile = xlsxFile;
            environment._locale = locale;
        }

        public SheetExportEnvironmentBuilder sheetTabMapper(SheetTabMapper sheetTabMapper) {
            environment._sheetTabMapper = sheetTabMapper;
            return this;
        }

        public SheetExportEnvironmentBuilder sheetCellMapper(SheetCellMapper sheetCellMapper) {
            environment._sheetCellMapper = sheetCellMapper;
            return this;
        }

        public SheetExportEnvironmentBuilder sheetTabElementMapper(SheetTabElementMapper sheetTabElementMapper) {
            environment._sheetTabElementMapper = sheetTabElementMapper;
            return this;
        }

        public SheetExportEnvironmentBuilder documentConfigMapper(DocumentConfigMapper documentConfigMapper) {
            environment._documentConfigMapper = documentConfigMapper;
            return this;
        }

        public SheetExportEnvironment build() {
            return environment;
        }

    }

}
