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
package com.cubedrive.sheet.domain.sheet;

public class SheetTabSpan {
	
    private Integer tabId;
	private Integer maxRow;
	private Integer maxCol;
	private Integer totalCellNum;
	
    public SheetTabSpan() {
        super();
    }

    public SheetTabSpan(Integer tabId, Integer maxRow, Integer maxCol, Integer totalCellNum) {
        super();
        this.tabId = tabId;
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        this.totalCellNum = totalCellNum;
    }

    public Integer getTabId() {
        return tabId;
    }

    public void setTabId(Integer tabId) {
        this.tabId = tabId;
    }

    public int getMaxRow() {
        return maxRow == null ? 0: maxRow.intValue();
    }
    
    public void setMaxRow(Integer maxRow) {
        this.maxRow = maxRow;
    }
    
    public int getMaxCol() {
        return maxCol == null ? 0 : maxCol.intValue();
    }
    
    public void setMaxCol(Integer maxCol) {
        this.maxCol = maxCol;
    }
    
    public int getTotalCellNum() {
        return totalCellNum == null ? 0: totalCellNum.intValue();
    }
    
    public void setTotalCellNum(Integer totalCellNum) {
        this.totalCellNum = totalCellNum;
    }

}
