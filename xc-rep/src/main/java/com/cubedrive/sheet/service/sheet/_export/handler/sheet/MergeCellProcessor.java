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
package com.cubedrive.sheet.service.sheet._export.handler.sheet;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;

public class MergeCellProcessor extends WorksheetProcessor{
    
    private final List<CellRangeAddress> _mergeRegions;
    
    private MergeCellProcessor(List<CellRangeAddress> mergeRegions, XSSFWorksheetExportContext xssfWorksheetExportContext){
        super(xssfWorksheetExportContext);
        _mergeRegions = mergeRegions;
    }
    
    public boolean isIgnorableCell(SheetCell sheetCell){
        boolean ignore = false;
        int rowIndex = sheetCell.getX() -1;
        int columnIndex = sheetCell.getY() -1;
        for(CellRangeAddress _mergeRegion:_mergeRegions){
            if(_mergeRegion.getFirstRow() == rowIndex && _mergeRegion.getFirstColumn() == columnIndex)
                break;
            
            if(_mergeRegion.isInRange(rowIndex, columnIndex)){
                ignore = true;
                break;
            }
        }
        return ignore;
    }

    @Override
    public void process() {
        XSSFSheet xssfSheet = _xssfWorksheetExportContext.sheet().getXSSFSheet();
        for(CellRangeAddress _mergeRegion:_mergeRegions){
            xssfSheet.addMergedRegion(_mergeRegion);
        }
    }

    public static MergeCellProcessor build(List<List<Integer>> mergeCellConfigList,XSSFWorksheetExportContext xssfWorksheetExportContext){
        List<CellRangeAddress> mergeCells = new ArrayList<>();
        for(List<Integer> mergeCellConfig:mergeCellConfigList){
            String startCell = CellReference.convertNumToColString(mergeCellConfig.get(1) -1)+mergeCellConfig.get(0);
            String endCell = "";
            if(mergeCellConfig.size()>2){
                endCell = ":"+CellReference.convertNumToColString(mergeCellConfig.get(3) -1)+mergeCellConfig.get(2);
            }
            CellRangeAddress mergeRegion = CellRangeAddress.valueOf(startCell+endCell);
            mergeCells.add(mergeRegion);
        }
        MergeCellProcessor mergeCellProcessor = new MergeCellProcessor(mergeCells,xssfWorksheetExportContext);
        return mergeCellProcessor;
    }
}
