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
package com.cubedrive.sheet.service.sheet._import.handler.formula;

import org.apache.poi.ss.util.CellReference;

import com.cubedrive.sheet.service.sheet._import.ctx.CellImportContext;
import com.cubedrive.sheet.service.sheet._import.handler.formula.ParseNode.PtgState;

public class SharedFormulaRenderer {

    public static String renderSharedFormula(String formulaString, CellImportContext cellContext, int rowRelValue, int colRelValue) {
        ParseNode rootNode = FormulaParser.parse(formulaString, cellContext);
        PtgState[] rpnStates = ParseNode.toRPN(rootNode);
        for (PtgState ptgState : rpnStates) {
            Ptg ptg = ptgState.getPtg();
            if (ptg instanceof RefPtg) {
                RefPtg refPtg = (RefPtg) ptg;
                CellReference cellRef = new CellReference(refPtg.getRef());
                String newRef = createRef(cellRef.getCol() , colRelValue,
                        cellRef.getRow(), rowRelValue,
                        cellRef.isColAbsolute(), cellRef.isRowAbsolute());
                RefPtg newRefPtg = RefPtg.newInstance(refPtg.getSheetName(), newRef);
                ptgState.setPtg(newRefPtg);
            } else if (ptg instanceof AreaPtg) {
                AreaPtg areaPtg = (AreaPtg) ptg;
                if (!isRow(areaPtg.getFirstRef()) && !isColumn(areaPtg.getFirstRef())
                        && !isRow(areaPtg.getLastRef()) && !isColumn(areaPtg.getLastRef())) {
                    CellReference firstCellReference = new CellReference(areaPtg.getFirstRef());
                    CellReference lastCellReference = new CellReference(areaPtg.getLastRef());
                    String newFirstRef = createRef(firstCellReference.getCol() , colRelValue,
                            firstCellReference.getRow() , rowRelValue ,
                            firstCellReference.isColAbsolute(), firstCellReference.isRowAbsolute());
                    String newLastRef = createRef(lastCellReference.getCol(),colRelValue,
                            lastCellReference.getRow(),rowRelValue,
                            lastCellReference.isColAbsolute(), lastCellReference.isRowAbsolute());
                    AreaPtg newAreaPtg = AreaPtg.newInstance(areaPtg.getFirstSheetName(),
                            newFirstRef, areaPtg.getLastSheetName(), newLastRef);
                    ptgState.setPtg(newAreaPtg);
                }
            }
        }
        return FormulaRenderer.render(rpnStates);
    }

    private static String createRef(int colIndex, int colRelValue, int rowIndex, int rowRelValue, boolean colAbs, boolean rowAbs) {
    	StringBuilder sb = new StringBuilder();
    	int newColIndex = colIndex ;
    	if(colAbs){
    		sb.append('$');
    	}else{
    		newColIndex += colRelValue;
    	}
        sb.append(CellReference.convertNumToColString(newColIndex));
        
        int newRowIndex = rowIndex + 1;
        if (rowAbs) {
            sb.append('$');
        }else{
        	newRowIndex += rowRelValue;
        }
        sb.append(newRowIndex);
        return sb.toString();
    }

    private static boolean isRow(String ref) {
        try {
            Integer.parseInt(ref);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static boolean isColumn(String ref) {
        String upperRef = ref.toUpperCase();
        for (char c : upperRef.toCharArray()) {
            if (c >= 48 && c <= 57) {
                return false;
            }
        }
        return true;
    }

}
