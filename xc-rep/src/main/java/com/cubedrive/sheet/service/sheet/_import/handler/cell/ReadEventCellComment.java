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

import org.apache.poi.xssf.model.CommentsTable;
import org.apache.poi.xssf.usermodel.XSSFComment;

import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;

public class ReadEventCellComment {
    
    private static final ReadEventCellComment emptyCellComment = new ReadEventCellComment();
    
    private final Map<String,Object> _comment = new HashMap<>();
    
    private ReadEventCellComment(){
        
    }
    
    public Map<String,Object> getComment(){
        return _comment;
    }

    public static ReadEventCellComment build(CellImportContext cellImportContext){
        SheetImportContext sheetImportContext = cellImportContext.parentContext().parentContext();
        CommentsTable commentTable = sheetImportContext.sheet().sheetComments();
        if(commentTable != null){
            ReadEventCellComment readEventCellComment = new ReadEventCellComment();
            XSSFComment xssfComment =commentTable.findCellComment(cellImportContext.cellReference().formatAsString());
            if(xssfComment !=null){
                String text = xssfComment.getString().getString();
    //            text = text.replaceAll("(\r\n|\n)", "<br />");
                readEventCellComment._comment.put("comment", text);
            }
            return readEventCellComment;
        }
        return emptyCellComment;
        
        
    }
    
}
