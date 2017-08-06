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
package com.cubedrive.sheet.service.sheet._import;

import java.io.File;
import java.util.Locale;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentConfigMapper;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.cubedrive.sheet.persistence.sheet.SheetStyleMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;

public class SheetImportEnvironment {

    private DocumentFile documentFile;

    private File xlsxFile;
    
    private Locale locale;

    private SheetTabMapper _sheetTabMapper;
    
    private SheetCellMapper _sheetCellMapper;

    private SheetTabElementMapper _sheetTabElementMapper;
    
    private DocumentConfigMapper _documentConfigMapper;
    
    private SheetStyleMapper _sheetStyleMapper;

    private String _cellTable;
    
    
    private SheetImportEnvironment() {
    }

    public DocumentFile documentFile() {
        return this.documentFile;
    }

    public File xlsxFile() {
        return this.xlsxFile;
    }

    public SheetTabMapper sheetTabMapper() {
        return this._sheetTabMapper;
    }

    public SheetCellMapper sheetCellMapper() {
        return this._sheetCellMapper;
    }

    public SheetTabElementMapper sheetTabElementMapper() {
        return this._sheetTabElementMapper;
    }

    public DocumentConfigMapper documentConfigMapper(){
        return this._documentConfigMapper;
    }
    
    public SheetStyleMapper sheetStyleMapper(){
        return this._sheetStyleMapper;
    }
    
    public String cellTable(){
        return this._cellTable;
    }
    
    public Locale locale(){
        return this.locale;
    }
    
    public static class SheetImportEnvironmentBuilder {

        private SheetImportEnvironment environment;

        public SheetImportEnvironmentBuilder(DocumentFile documentFile, File xlsxFile, Locale locale) {
            environment = new SheetImportEnvironment();
            environment.documentFile = documentFile;
            environment.xlsxFile = xlsxFile;
            environment.locale = locale;
        }

        public SheetImportEnvironmentBuilder sheetTabMapper(SheetTabMapper sheetTabMapper) {
            environment._sheetTabMapper = sheetTabMapper;
            return this;
        }

        public SheetImportEnvironmentBuilder sheetCellMapper(SheetCellMapper sheetCellMapper) {
            environment._sheetCellMapper = sheetCellMapper;
            return this;
        }

        public SheetImportEnvironmentBuilder sheetTabElementMapper(SheetTabElementMapper sheetTabElementMapper) {
            environment._sheetTabElementMapper = sheetTabElementMapper;
            return this;
        }
        
        public SheetImportEnvironmentBuilder documentConfigMapper(DocumentConfigMapper documentConfigMapper){
            environment._documentConfigMapper = documentConfigMapper;
            return this;
        }
        
        public SheetImportEnvironmentBuilder sheetStyleMapper(SheetStyleMapper sheetStyleMapper){
            environment._sheetStyleMapper = sheetStyleMapper;
            return this;
        }
        
        public SheetImportEnvironmentBuilder cellTable(String cellTable){
            environment._cellTable = cellTable;
            return this;
        }

        public SheetImportEnvironment build() {
            return environment;
        }

    }

}
