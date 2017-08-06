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
package com.cubedrive.sheet.service.sheet._import.handler.cell;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFHyperlink;

import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;

public class ReadEventCellHyperlink {
    
    private final Map<String, Object> _hyperlink = new HashMap<>();
    
    private ReadEventCellHyperlink(){
        
    }
    
    
    public Map<String,Object> getHyperlink(){
        return _hyperlink;
    }
    
    public static ReadEventCellHyperlink build(CellImportContext cellImportContext){
        ReadEventCellHyperlink readEventCellHyperlink = new ReadEventCellHyperlink();
        SheetImportContext sheetImportContext =cellImportContext.parentContext().parentContext();
        XSSFHyperlink hyperlink = sheetImportContext.getHyperlink(cellImportContext.cellReference().formatAsString());
        if(hyperlink != null){
            readEventCellHyperlink._hyperlink.put("fm", "link");
            readEventCellHyperlink._hyperlink.put("link", hyperlink.getAddress());
        }
        return readEventCellHyperlink;
    }
    

}
