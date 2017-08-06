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
package com.cubedrive.sheet.service.sheet._import.handler.sheet;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.util.CellReference;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTPane;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSelection;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetView;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;

public class ReadEventSheetView {
    
    private final Map<String,Object> _setting = new HashMap<>();
    
    private ReadEventSheetView(){
        
    }
    
    public Map<String,Object> getSetting(){
        return _setting;
    }
    
    public static ReadEventSheetView build(SheetImportContext sheetImportContext){
        ReadEventSheetView readEventSheetView = new ReadEventSheetView();
        XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
        CTWorksheet worksheet = readEventSheet.worksheet();
        if(worksheet.isSetSheetViews()){
            if(worksheet.getSheetViews().getSheetViewList().size()>0){
                CTSheetView sheetView = worksheet.getSheetViews().getSheetViewList().get(0);
                if(sheetView.isSetShowGridLines() && !sheetView.getShowGridLines()){
                    readEventSheetView._setting.put("noGridLine", true);
                }
                if(!sheetView.isSetPane()){
                	if( !sheetView.getSelectionList().isEmpty()){
                    CTSelection selection = sheetView.getSelectionList().get(0);
	                    if(selection.isSetActiveCell()){
		                    String activeCell = selection.getActiveCell();
		                    CellReference activeCellReference =new CellReference(activeCell);
		                    Map<String,Object> focusCell=new HashMap<>();
		                    focusCell.put("col", activeCellReference.getCol() + 1);
		                    focusCell.put("row", activeCellReference.getRow() + 1);
		                    readEventSheetView._setting.put("focusCell", focusCell);
	                    }
                	}
                }else{
                	CTPane ctPane = sheetView.getPane();
                	String state = ctPane.getState().toString();
                	String sheetViewTopLeftCell = "A1";
                	if(sheetView.isSetTopLeftCell()){
                		sheetViewTopLeftCell = sheetView.getTopLeftCell();
                	}
                	CellReference sheetViewTopLeftCellRef =new CellReference(sheetViewTopLeftCell);
//                	CellReference paneTopLeftCellRef = null;
//                	if(ctPane.isSetTopLeftCell()){
//                		paneTopLeftCellRef = new CellReference(ctPane.getTopLeftCell());
//                	}
                	int xSplit = ctPane.isSetXSplit() ? (int)ctPane.getXSplit() : 0;
                	int ySplit = ctPane.isSetYSplit() ? (int)ctPane.getYSplit() : 0;
                	if(xSplit > 510){
                		xSplit = (xSplit-510)/1080;
                	}
                	
                	if(ySplit > 270){
                		ySplit = (ySplit-270)/270;
                	}
                	
                	
            		Map<String,Object> paneSetting = getPaneSetting(sheetViewTopLeftCellRef,xSplit,ySplit);
                	if("frozen".equals(state)){
                		readEventSheetView._setting.put("freezePos",paneSetting);
                	}else if("split".equals(state)){
                		readEventSheetView._setting.put("splitPos",paneSetting);
                	}
                	
                }
            }
    
        }
        return readEventSheetView;
    }
    
    private static Map<String,Object> getPaneSetting(CellReference sheetViewTopLeftCellRef, int xSplit, int ySplit){
    	Map<String,Object> paneSetting = new HashMap<>();
    	int startRow = sheetViewTopLeftCellRef.getRow()+1;
    	int startCol = sheetViewTopLeftCellRef.getCol()+1;
    	int row = startRow + ySplit;
    	int col = startCol + xSplit;
    	paneSetting.put("startRow", startRow);
    	paneSetting.put("startCol", startCol);
    	paneSetting.put("row", row);
    	paneSetting.put("col", col);
    	paneSetting.put("reset", true);
    	return paneSetting;
    }
    
  
    
}
