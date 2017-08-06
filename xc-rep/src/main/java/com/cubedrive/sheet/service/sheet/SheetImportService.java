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
package com.cubedrive.sheet.service.sheet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentConfigMapper;
import com.cubedrive.sheet.SheetTableSequence;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.cubedrive.sheet.persistence.sheet.SheetStyleMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;
import com.cubedrive.sheet.service.sheet._import.CSVImportEnvironment;
import com.cubedrive.sheet.service.sheet._import.DefaultXSSFReadEventListener;
import com.cubedrive.sheet.service.sheet._import.SheetImportEnvironment;
import com.cubedrive.sheet.service.sheet._import.csv.CSVImportHandler;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventListener;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventWorkbook;

@Service
public class SheetImportService {
    
    @Autowired
    private SheetTabMapper sheetTabMapper;
    
    @Autowired
    private SheetCellMapper sheetCellMapper;
    
    @Autowired
    private SheetTabElementMapper sheetTabElementMapper;
    
    @Autowired
    private DocumentConfigMapper documentConfigMapper;
    
    @Autowired
    private SheetStyleMapper sheetStyleMapper;
    
    @Autowired
    private TransactionTemplate transactionTemplate;

    public void importXlsx(DocumentFile documentFile, File xlsxFile, Locale userLocale) throws Exception{
        SheetImportEnvironment.SheetImportEnvironmentBuilder builder = new SheetImportEnvironment.SheetImportEnvironmentBuilder(
                documentFile, xlsxFile, userLocale);
        builder.sheetCellMapper(sheetCellMapper).sheetTabMapper(sheetTabMapper)
            .sheetTabElementMapper(sheetTabElementMapper).sheetStyleMapper(sheetStyleMapper).documentConfigMapper(documentConfigMapper);
        builder.cellTable(SheetTableSequence.instance().sheetCellTable());
        SheetImportEnvironment environment = builder.build();
        XSSFReadEventListener xssfReadEventListener = new DefaultXSSFReadEventListener(environment);
        XSSFReadEventWorkbook xssfReadEventWorkbook = null;
        try(InputStream in = new BufferedInputStream(new FileInputStream(xlsxFile))){
            xssfReadEventWorkbook = new XSSFReadEventWorkbook(in,xssfReadEventListener);
            xssfReadEventWorkbook.readDocument();
        }catch(IOException ex){
            throw ex;
        }finally{
            if(xssfReadEventWorkbook != null){
                xssfReadEventWorkbook.close();
            }
        }
    }
    
    public void importCsv(DocumentFile documentFile, File csvFile) throws Exception{
        CSVImportEnvironment.CSVImportEnvironmentBuilder builder = new CSVImportEnvironment.CSVImportEnvironmentBuilder(
                documentFile, csvFile);
        builder.sheetCellMapper(sheetCellMapper).sheetTabMapper(sheetTabMapper);
        builder.cellTable(SheetTableSequence.instance().sheetCellTable());
        builder.transactionTemplate(transactionTemplate);
        CSVImportEnvironment environment = builder.build();
        CSVImportHandler csvImportHandler = new CSVImportHandler(environment);
        csvImportHandler.handleCSV();
    }

}
