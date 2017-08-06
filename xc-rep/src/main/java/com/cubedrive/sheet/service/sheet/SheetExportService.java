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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.base.BaseAppConfiguration;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentConfigMapper;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;
import com.cubedrive.sheet.service.sheet._export.DefaultXSSFWriteEventListener;
import com.cubedrive.sheet.service.sheet._export.SheetExportEnvironment;
import com.cubedrive.sheet.service.sheet._export.XSSFWorkbookExportlet;

@Service
public class SheetExportService {

    @Autowired
    private SheetTabMapper sheetTabMapper;

    @Autowired
    private SheetCellMapper sheetCellMapper;

    @Autowired
    private SheetTabElementMapper sheetTabElementMapper;

    @Autowired
    private DocumentConfigMapper documentConfigMapper;

    public File exportSpreadsheet(DocumentFile documentFile, Locale userLocale) throws IOException {
        SheetExportEnvironment.SheetExportEnvironmentBuilder builder =
                new SheetExportEnvironment.SheetExportEnvironmentBuilder(documentFile, userLocale);
        builder.sheetTabMapper(sheetTabMapper).sheetCellMapper(sheetCellMapper).
                sheetTabElementMapper(sheetTabElementMapper).documentConfigMapper(documentConfigMapper);
        SheetExportEnvironment environment = builder.build();
        DefaultXSSFWriteEventListener writeEventListener = new DefaultXSSFWriteEventListener(environment);
        XSSFWorkbookExportlet exportlet = new XSSFWorkbookExportlet(environment,writeEventListener);
        File outputFile = createOutputFile();
        try(BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(outputFile))){
            exportlet.export(os);
        }
        return outputFile;
    }
    
    private File createOutputFile() throws IOException{
      return File.createTempFile("sheet", ".xlsx", BaseAppConfiguration.instance().getTempDir());
    }
}
