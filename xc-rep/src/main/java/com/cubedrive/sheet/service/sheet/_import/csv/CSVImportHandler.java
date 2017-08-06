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
package com.cubedrive.sheet.service.sheet._import.csv;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.service.sheet._import.CSVImportEnvironment;

public class CSVImportHandler {
    
    private final CSVImportEnvironment _environment;
    
    private SheetTab _sheetTab;
    
    private int rowIndex = 1;
    
    public CSVImportHandler(CSVImportEnvironment csvImportEnvironment){
        _environment = csvImportEnvironment;
    }
    
    private void createSheetTab(){
        _sheetTab = new SheetTab();
        _sheetTab.setDocumentFile(_environment.documentFile());
        _sheetTab.setName("Sheet1");
        _sheetTab.setTabOrder(1);
        _sheetTab.setActive(true);
        _sheetTab.setCellTable(_environment.cellTable());
        _environment.sheetTabMapper().insertSheetTab(_sheetTab);
    }
    
    @SuppressWarnings("unchecked")
    private void handleRow(String line) throws Exception{
        int colIndex = 1;
        CSVParser csvParser = new CSVParser(line);
        List<String> datas = csvParser.getAllFieldsInArrayList();
        List<SheetCell> cells = new ArrayList<>(datas.size());
        for(String data:datas){
            if(data != null && data.length() !=0){
                SheetCell cell = handleCell(data, colIndex);
                cells.add(cell);
            }
            colIndex ++;
        }
        if(! cells.isEmpty()){
            _environment.sheetCellMapper().batchInsertSheetCell2(cells, _environment.cellTable());
        }
    }
    
    private SheetCell handleCell(String data, int colIndex){
        Map<String,Object> contentMap = new HashMap<>();
        contentMap.put("data", data);
        
        SheetCell sheetCell = new SheetCell();
        sheetCell.setX(rowIndex);
        sheetCell.setY(colIndex);
        sheetCell.setTabId(_sheetTab.getId());
        sheetCell.setContent(JsonUtil.toJson(contentMap));
        sheetCell.setCal(false);
        sheetCell.setRawData(data);
        return sheetCell;
    }
    
    public void handleCSV() {
        _environment.transactionTemplate().execute(new TransactionCallback<Void>(){
            @Override
            public Void doInTransaction(TransactionStatus status) {
                try {
                    doHandleCSV();
                    return null;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            
        });
    }
    
    private void doHandleCSV()throws Exception{
        createSheetTab();
        try(BufferedReader in = new BufferedReader(new InputStreamReader
            (new FileInputStream(_environment.csvFile()),Charset.forName("UTF-8")))){
        
            for(String line=in.readLine(); line != null; line=in.readLine()){
                if(line.length() != 0){
                    handleRow(line);
                }
                rowIndex ++;
            }
        }
    }

}
