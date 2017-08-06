/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
package com.cubedrive.sheet.service.sheet._import;

import java.io.File;

import org.springframework.transaction.support.TransactionTemplate;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;

public class CSVImportEnvironment {
    
    private DocumentFile _documentFile;
    
    private File _csvFile;
    
    private SheetTabMapper _sheetTabMapper;
    
    private SheetCellMapper _sheetCellMapper;
    
    private String _cellTable;
    
    private TransactionTemplate _transactionTemplate;
    
    private CSVImportEnvironment(){
        
    }
    
    public DocumentFile documentFile(){
        return _documentFile;
    }
    
    public File csvFile(){
        return _csvFile;
    }
    
    public SheetTabMapper sheetTabMapper(){
        return _sheetTabMapper;
    }
    
    public SheetCellMapper sheetCellMapper(){
        return _sheetCellMapper;
    }
    
    public String cellTable(){
        return _cellTable;
    }
    
    public TransactionTemplate transactionTemplate(){
        return _transactionTemplate;
    }
    
    public static class CSVImportEnvironmentBuilder{
        
        private CSVImportEnvironment csvImportEnvironment = new CSVImportEnvironment();
        
        public CSVImportEnvironmentBuilder(DocumentFile documentFile,File csvFile){
            csvImportEnvironment._documentFile = documentFile;
            csvImportEnvironment._csvFile = csvFile;
        }
        
        public CSVImportEnvironmentBuilder sheetTabMapper(SheetTabMapper sheetTabMapper){
            csvImportEnvironment._sheetTabMapper = sheetTabMapper;
            return this;
        }
        
        public CSVImportEnvironmentBuilder sheetCellMapper(SheetCellMapper sheetCellMapper){
            csvImportEnvironment._sheetCellMapper = sheetCellMapper;
            return this;
        }
        
        public CSVImportEnvironmentBuilder cellTable(String cellTable){
            csvImportEnvironment._cellTable = cellTable;
            return this;
        }
        
        public CSVImportEnvironmentBuilder transactionTemplate(TransactionTemplate transactionTemplate){
            csvImportEnvironment._transactionTemplate = transactionTemplate;
            return this;
        }
        
        public CSVImportEnvironment build(){
            return csvImportEnvironment;
        }
    }
    
    

}
