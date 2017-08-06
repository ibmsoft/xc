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

import org.apache.poi.ss.util.CellRangeAddress;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRElt;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTRst;

import com.cubedrive.sheet.service.sheet._import.SpreadsheetImportHelper;
import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.ctx.WorkbookImportContext;
import com.cubedrive.sheet.service.sheet._import.handler.formula.SharedFormulaRenderer;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventCell;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventFormula;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventUtil;

public class ReadEventCellValue {

    private Object _cellValue;

    private ReadEventCellValue() {
    }

    public Object getCellValue() {
        return _cellValue;
    }


    private static Object doGetCellValue(int cellType, CellImportContext cellImportContext) {
        switch (cellType) {
            case CellImportContext.CELL_TYPE_NUMERIC:
                return numericCellValue(cellImportContext);
            case CellImportContext.CELL_TYPE_STRING:
                return stringCellValue(cellImportContext);
            case CellImportContext.CELL_TYPE_FORMULA:
                return "="+formulaCellValue(cellImportContext);
            case CellImportContext.CELL_TYPE_BOOLEAN:
                return booleanCellValue(cellImportContext);
            case CellImportContext.CELL_TYPE_ERROR:
                return errorCellValue(cellImportContext);
            case CellImportContext.CELL_TYPE_BLANK:
                return null;
            default:
                throw new IllegalStateException("Illegal cell type: " + cellType);
        }

    }

    private static String booleanCellValue(CellImportContext cellImportContext) {
        XSSFReadEventCell cell = cellImportContext.cell();
        return "1".equals(cell.val()) ? "TRUE":"FALSE";
    }

    private static String errorCellValue(CellImportContext cellImportContext) {
        XSSFReadEventCell cell = cellImportContext.cell();
        return cell.val();
    }


    private static Number numericCellValue(CellImportContext cellImportContext) {
        XSSFReadEventCell cell = cellImportContext.cell();
        if (cell.val() != null) {
            try {
                int intVal = Integer.parseInt(cell.val());
                return intVal;
            } catch (NumberFormatException ex) {
                double doubleVal = Double.parseDouble(cell.val());
                return doubleVal;
            }
        } else {
            return null;
        }
    }

    private static String stringCellValue(CellImportContext cellImportContext) {
        XSSFReadEventCell cell = cellImportContext.cell();
        String text = null;
        if ("inlineStr".equals(cell.dataType())) {
            if (cell.richString() != null) {
                text = cell.richString().getStringValue();
            } else if (cell.val() != null) {
                text = cell.val();
            }
        } else if ("str".equals(cell.dataType())) {
            if (cell.val() != null)
                text = cell.val();
        } else if ("s".equals(cell.dataType())) {
            if (cell.val() != null) {
                int idx = Integer.parseInt(cell.val());
                WorkbookImportContext context = cellImportContext.parentContext().parentContext().parentContext();
                CTRst st = context.workbook().sharedStringSource().getEntryAt(idx);
                if (st.sizeOfRArray() == 0) {
                    text = st.getT();
                } else {
                    StringBuilder buf = new StringBuilder();
                    for (CTRElt r : st.getRList()) {
                        buf.append(r.getT());
                    }
                    text = buf.toString();
                }
            }
        }
        if (text != null) {
            return XSSFReadEventUtil.utfDecode(text);
        } else {
            return null;
        }
    }

    private static String formulaCellValue(CellImportContext cellImportContext) {
        XSSFReadEventCell cell = cellImportContext.cell();
        SheetImportContext sheetContext = cellImportContext.parentContext().parentContext();
        CellRangeAddress cellRangeAddress = sheetContext.getArrayFormulaRangeAddress(cellImportContext.cellReference());
        XSSFReadEventFormula formula = cell.formula();
        String formulaText;
        if (cellRangeAddress != null) {
            XSSFReadEventFormula arrayFormula = sheetContext.getArrayFormula(cellRangeAddress);
            formulaText = arrayFormula.val();
            Map<String,Object> arrayFormulaSetting = new HashMap<>();
            if(cellRangeAddress.getFirstRow() == cellImportContext.cellReference().getRow() &&
            		cellRangeAddress.getFirstColumn() == cellImportContext.cellReference().getCol()){
            	arrayFormulaSetting.put("afrow", 0);
            	arrayFormulaSetting.put("afcol", 0);
            	arrayFormulaSetting.put("aerow", cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow());
            	arrayFormulaSetting.put("aecol", cellRangeAddress.getLastColumn() - cellRangeAddress.getFirstColumn());
            }else{
            	arrayFormulaSetting.put("afrow", cellRangeAddress.getFirstRow()-cellImportContext.cellReference().getRow());
            	arrayFormulaSetting.put("afcol", cellRangeAddress.getFirstColumn()-cellImportContext.cellReference().getCol());
            }
            cellImportContext.putVariable("arrayFormulaSetting", arrayFormulaSetting);
            
        } else if ("shared".equals(formula.formulaType())) {
            formulaText = convertSharedFormula(formula.sharedGroupIndex(), cellImportContext);
        } else {
            formulaText = formula.val();
        }
        if(formulaText != null && formulaText.startsWith("_xlfn.")){
            formulaText = formulaText.substring(6);
        }
                
        formulaText = SpreadsheetImportHelper.toJavascriptCompatibleStringFormula(formulaText);
        
        return formulaText;
    }


    private static String convertSharedFormula(Integer si, CellImportContext cellImportContext) {
        SheetImportContext sheetContext = cellImportContext.parentContext().parentContext();
        XSSFReadEventFormula sharedFormula = sheetContext.getSharedFormula(si);
        if (sharedFormula == null)
            throw new IllegalStateException(
                    "Master cell of a shared formula with sid=" + si + " was not found");

        String formula = sharedFormula.val();
        CellRangeAddress ref = CellRangeAddress.valueOf(sharedFormula.reference());
        String result = SharedFormulaRenderer.renderSharedFormula(formula, cellImportContext,
                cellImportContext.cellReference().getRow() - ref.getFirstRow(),
                cellImportContext.cellReference().getCol() - ref.getFirstColumn());
        return result;
    }


   


    public static ReadEventCellValue build(CellImportContext cellImportContext) {
        ReadEventCellValue readEventCellValue = new ReadEventCellValue();
        int cellType = cellImportContext.getCellType();
        Object cellValue = doGetCellValue(cellType, cellImportContext);
        readEventCellValue._cellValue = cellValue;
        return readEventCellValue;


    }


}
