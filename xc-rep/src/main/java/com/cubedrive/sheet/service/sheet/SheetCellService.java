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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentFileMapper;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;

@Service
public class SheetCellService {

    private final static Logger LOG = LoggerFactory.getLogger(SheetCellService.class);

    @Autowired
    private SheetCellMapper sheetCellMapper;

    @Autowired
    private SheetTabMapper sheetTabMapper;

    @Autowired
    private SheetTabElementMapper sheetTabElementMapper;

    @Autowired
    private DocumentFileMapper documentFileMapper;

    /**
     * this is action to insert new rows in the sheet
     * @param tabId
     * @param row
     * @param rowSpan
     * @param cellTable
     */
    public void insertRows(Integer tabId, Integer row, Integer rowSpan, String cellTable) {
        sheetCellMapper.insertRows(tabId, row, rowSpan, cellTable);

        // ok, we need check merge cells by row
        //this.checkMergedCellsByRow(tabId, row, rowSpan);
    }

    /**
     * this is the action to insert new column in the sheet
     * @param tabId
     * @param col
     * @param colSpan
     * @param cellTable
     */
    public void insertCols(Integer tabId, Integer col, Integer colSpan, String cellTable) {
        sheetCellMapper.insertColumns(tabId, col, colSpan, cellTable);

        // ok, we need check merge cell information -- such as: ftype:meg, json:[5,5,9,9] at this case we need change data ...
        //this.checkMergedCellsByCol(tabId, col, colSpan);
    }

    public SheetCell getCellByTabAndXY(Integer tabId, Integer x, Integer y, String cellTable) {
        SheetCell cell = sheetCellMapper.getCellByTabAndXY(tabId, x, y, cellTable);
        return cell;
    }

    public List<SheetCell> getCellByColRange(Integer tabId, Integer startCol, Integer endCol, String cellTable) {
        List<SheetCell> cells = sheetCellMapper.getCellByColRange(tabId, startCol, endCol, cellTable);
        return cells;
    }

    public List<SheetCell> getCellByRowRange(Integer tabId, Integer startRow, Integer endRow, String cellTable) {
        List<SheetCell> cells = sheetCellMapper.getCellByRowRange(tabId, startRow, endRow, cellTable);
        return cells;
    }

    public List<SheetCell> getCellByCellRange(Integer tabId, Integer startRow, Integer endRow, Integer startCol, Integer endCol, String cellTable) {
        List<SheetCell> cells = sheetCellMapper.getCellByRange(tabId, startRow, endRow, startCol, endCol, cellTable);
        return cells;
    }

    public List<SheetCell> getCellsBySearch(DocumentFile file, String query) {
        List<SheetTab> tabs = sheetTabMapper.findTabsByDocumentFile(file.getId());
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(new Integer(0));
        for (SheetTab tab : tabs) {
            ids.add(tab.getId());
        }
        List<SheetCell> cells = sheetCellMapper.getCellsBySearch(ids, query, tabs.get(0).getCellTable());
        return cells;
    }

    public List<SheetCell> getCellsBySearch2(DocumentFile file, String query, Integer offset, Integer limit) {
        List<SheetTab> tabs = sheetTabMapper.findTabsByDocumentFile(file.getId());
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(new Integer(0));
        for (SheetTab tab : tabs) {
            ids.add(tab.getId());
        }
        List<SheetCell> cells = sheetCellMapper.getCellsBySearch2(ids, query, tabs.get(0).getCellTable(), offset, limit);
        return cells;
    }



    public List<SheetCell> getCellsByRow(Integer tabId, Integer x, String cellTable) {
        List<SheetCell> cells = sheetCellMapper.getCellsByRow(tabId, x, cellTable);
        return cells;
    }

    public List<SheetCell> getCellsByCol(Integer tabId, Integer y, String cellTable) {
        List<SheetCell> cells = sheetCellMapper.getCellsByCol(tabId, y, cellTable);
        return cells;
    }

    public List<SheetCell> getActiveAndCalCells(Integer documentId) {
        // first we need get active tab id and cell table name
        SheetTab tab = sheetTabMapper.getActiveTab(documentId);
        if (tab == null) { // special case, if it not any active, set first one as active
            tab = sheetTabMapper.findTabsByDocumentFile(documentId).get(0);
            sheetTabMapper.activeSheetTab(documentId, tab.getId());
        }

        List<SheetCell> cells = sheetCellMapper.findCellsByTab(tab.getId(), tab.getCellTable());
        // need add cal is true for not active tab ..
        // this is for first time load ...
        List<Integer> unactiveTabIds = getAllUnactiveTabIds(documentId);
        List<SheetCell> calIsTrueCells = sheetCellMapper.findDeactiveCalCells(unactiveTabIds, tab.getCellTable());

        List<SheetCell> tempCells = new ArrayList<>(cells.size() + calIsTrueCells.size());
        tempCells.addAll(cells);
        cells.clear();
        tempCells.addAll(calIsTrueCells);
        calIsTrueCells.clear();
        return tempCells;
    }


    public Integer getTabCellCount(SheetTab tab) {
        return sheetCellMapper.getTabCellCount(tab.getId(), tab.getCellTable());
    }


    public List<SheetCell> getActiveCellsWithSize(SheetTab activeTab, Collection<SheetTab> tabs, Integer startCellId, Integer size, boolean cellRawData) {
        // first we need get active tab id and cell table name
       
        List<SheetCell> cells;
       
        List<Integer> unactiveTabIds = new ArrayList<>(tabs.size()-1);
        for(SheetTab tab:tabs){
            if(tab.getActive() == null || !tab.getActive()){
                unactiveTabIds.add(tab.getId());
            }
        }
        if(!cellRawData){
            cells = sheetCellMapper.loadCellsOnDemand(startCellId, activeTab.getId(), size, activeTab.getCellTable(), unactiveTabIds);
        }else{
            cells = sheetCellMapper.loadCellsOfRawDataOnDemand(startCellId, activeTab.getId(), size, activeTab.getCellTable(), unactiveTabIds);
        }
        return cells;
    }

    public List<SheetCell> getCornerCalCellsWithSize(SheetTab activeTab, Collection<SheetTab> tabs, Integer startCellId, Integer size) {
        List<Integer> tabIds  = new ArrayList<>();
        for(SheetTab tab:tabs){
            tabIds.add(tab.getId());
        }
        List<SheetCell> cells = sheetCellMapper.loadCornerCalCellsOnDemand(startCellId, activeTab.getId(), size, activeTab.getCellTable(), tabIds);
        return cells;
    }

 

    public List<SheetCell> loadTabNonDataCellOfStyle(Integer tabId, String cellTable){
        return sheetCellMapper.loadNonDataCellOfStyle(tabId, cellTable);
    }

    public List<SheetCell> getLeftTopCells(Integer documentId, Integer tabId){

        SheetTab tab = sheetTabMapper.getActiveTab(documentId);
        if (tab == null) { // special case, if it not any active, set first one as active
            tab = sheetTabMapper.findTabsByDocumentFile(documentId).get(0);
            sheetTabMapper.activeSheetTab(documentId, tab.getId());
        }
        // need add cal is true for not active tab ..
        // this is for first time load ...
        List<SheetCell> cells = sheetCellMapper.getLeftTopCells(tabId, tab.getCellTable());
        return cells;

    }

    public List<SheetCell> getLeftTopCellsOfActiveTab(Integer documentId){

        SheetTab tab = sheetTabMapper.getActiveTab(documentId);
        if (tab == null) { // special case, if it not any active, set first one as active
            tab = sheetTabMapper.findTabsByDocumentFile(documentId).get(0);
            sheetTabMapper.activeSheetTab(documentId, tab.getId());
        }
        // need add cal is true for not active tab ..
        // this is for first time load ...
        List<SheetCell> cells = sheetCellMapper.getLeftTopCells(tab.getId(), tab.getCellTable());
        return cells;

    }

    public List<SheetCell> getLeftTopCellsOfRawDataOfActiveTab(Integer documentId){

        SheetTab tab = sheetTabMapper.getActiveTab(documentId);
        if (tab == null) { // special case, if it not any active, set first one as active
            tab = sheetTabMapper.findTabsByDocumentFile(documentId).get(0);
            sheetTabMapper.activeSheetTab(documentId, tab.getId());
        }
        // need add cal is true for not active tab ..
        // this is for first time load ...
        List<SheetCell> cells = sheetCellMapper.getLeftTopCellsOfRawData(tab.getId(), tab.getCellTable());
        return cells;
    }

    public List<SheetCell> getCalCells(Integer documentId, Integer startCellId, Integer size){

        SheetTab tab = sheetTabMapper.getActiveTab(documentId);
        if (tab == null) { // special case, if it not any active, set first one as active
            tab = sheetTabMapper.findTabsByDocumentFile(documentId).get(0);
            sheetTabMapper.activeSheetTab(documentId, tab.getId());
        }
        // need add cal is true for not active tab ..
        // this is for first time load ...
        List<Integer>  allTabIds = getAllTabIds(documentId);
        List<SheetCell> cells;
        if(! allTabIds.isEmpty())
            cells = sheetCellMapper.getCalCells(allTabIds, startCellId, size, tab.getCellTable());
        else
            cells = Collections.emptyList();
        return cells;

    }

    public List<SheetCell> getCalCellsOfRawData(Integer documentId, Integer startCellId, Integer size){

        SheetTab tab = sheetTabMapper.getActiveTab(documentId);
        if (tab == null) { // special case, if it not any active, set first one as active
            tab = sheetTabMapper.findTabsByDocumentFile(documentId).get(0);
            sheetTabMapper.activeSheetTab(documentId, tab.getId());
        }
        // need add cal is true for not active tab ..
        // this is for first time load ...
        List<Integer>  allTabIds = getAllTabIds(documentId);
        List<SheetCell> cells ;
        if(! allTabIds.isEmpty())
            cells = sheetCellMapper.getCalCellsOfRawData(allTabIds, startCellId, size, tab.getCellTable());
        else
            cells = Collections.emptyList();
        return cells;
    }

    public List<SheetCell> getDeactiveCalCells(Integer documentId) {
        // first we need get active tab id and cell table name
        SheetTab tab = sheetTabMapper.getActiveTab(documentId);
        if (tab == null) { // special case, if it not any active, set first one as active
            tab = sheetTabMapper.findTabsByDocumentFile(documentId).get(0);
            sheetTabMapper.activeSheetTab(documentId, tab.getId());
        }
        // need add cal is true for not active tab ..
        // this is for first time load ...
        List<Integer> unactiveTabIds = getAllUnactiveTabIds(documentId);
        List<SheetCell> calIsTrueCells = sheetCellMapper.findDeactiveCalCells(unactiveTabIds, tab.getCellTable());

        return calIsTrueCells;
    }

    /**
     * this function will load more cell objs for tab ...
     * @param tab
     * @param startCellId
     * @param size
     * @param fileId
     * @param skipCal
     * @return
     */
    public List<SheetCell> loadTabCellOnDemand(SheetTab tab, Integer startCellId, Integer size, Integer fileId, Boolean skipCal) {

        List<SheetCell> cells;
        if(skipCal){
            cells = sheetCellMapper.loadNotCalCellsOnDemand(startCellId, tab.getId(), size, tab.getCellTable());
        } else {
            Integer tabId = tab.getId();
            List<Integer> otherTabIds = this.getOtherTabIds(fileId, tabId);
            if(! otherTabIds.isEmpty())
                cells = sheetCellMapper.loadCellsOnDemand(startCellId, tabId, size, tab.getCellTable(), otherTabIds);
            else
                cells = Collections.emptyList();
        }
        return cells;
    }
    
    public List<SheetCell> loadDocCellOnDemand(SheetTab tab, Integer startCellId, Integer size, Integer fileId) {
        List<SheetCell> cells;        
        List<Integer> tabIds = this.getAllTabIds(fileId);
        if(! tabIds.isEmpty())
            cells = sheetCellMapper.loadDocCellsOnDemand(startCellId, size, tab.getCellTable(), tabIds);
        else
            cells = Collections.emptyList();
        return cells;
    }

    public List<SheetCell> loadTabCornerCalCellOnDemand(SheetTab tab, Integer startCellId, Integer size, Integer fileId, Boolean skipCal) {

        List<SheetCell> cells;
        Integer tabId = tab.getId();
        if(skipCal){
            cells = sheetCellMapper.loadCornerCellsOnDemand(startCellId, tabId, size, tab.getCellTable());
        }else{
            List<Integer> tabIds = this.getAllTabIds(fileId);
            if(! tabIds.isEmpty())
                cells = sheetCellMapper.loadCornerCalCellsOnDemand(startCellId, tabId, size, tab.getCellTable(), tabIds);
            else
                cells = Collections.emptyList();
        }
        return cells;
    }



    public List<SheetCell> loadTabCellOfRawDataOnDemand(SheetTab tab, Integer startCellId, Integer size, Integer fileId, Boolean skipCal) {
        List<SheetCell> cells;
        if(skipCal){
            cells = sheetCellMapper.loadNotCalCellsOfRawDataOnDemand(startCellId, tab.getId(), size, tab.getCellTable());
        } else {
            Integer tabId = tab.getId();
            List<Integer> otherTabIds = getOtherTabIds(fileId, tabId);
            if(! otherTabIds.isEmpty())
                cells = sheetCellMapper.loadCellsOfRawDataOnDemand(startCellId, tabId, size, tab.getCellTable(), otherTabIds);
            else
                cells = Collections.emptyList();
        }
        return cells;
    }



    public List<SheetCell> getTabCells(SheetTab tab) {
        List<SheetCell> cells = sheetCellMapper.findCellsByTab(tab.getId(), tab.getCellTable());
        return cells;
    }

    /**
     * this is the batch process to insert cell ...
     *
     * @param listBatchObjs
     * @param sheetTab
     */
    public void insertBatchSetCells(List<SheetCell> listBatchObjs, SheetTab tab) {
        int size=listBatchObjs.size() ;
        if (size > 0) {
            if(size <= 100){
                sheetCellMapper.batchInsertSheetCell3(listBatchObjs, tab.getCellTable());
            } else{
                int i = 0;
                for(; i<size ;){
                    if(i+100 <= size){
                        sheetCellMapper.batchInsertSheetCell3(listBatchObjs.subList(i, i+100), tab.getCellTable());
                        i+=100;
                    }else{
                        sheetCellMapper.batchInsertSheetCell3(listBatchObjs.subList(i, size), tab.getCellTable());
                        break;
                    }
                }
            }
        }
    }

    // this is just for one item ...
    public void setCell(Map<String,Object> jsonObj, SheetTab sheetTab) {
        int x = new Integer(jsonObj.get("row").toString());
        int y = new Integer(jsonObj.get("col").toString());
        String cellTable = sheetTab.getCellTable();
        int tabId = sheetTab.getId();
        String content = (String)jsonObj.get("json");
        String data = (String)jsonObj.get("data");
        boolean cal = (boolean)jsonObj.get("cal");

        // get cell first
        SheetCell cell = sheetCellMapper.getCellByTabAndXY(tabId, x, y, cellTable);

        if (cell != null) { // update or delete
            if (this.checkContentIsNull(content)) {
                sheetCellMapper.deleteById(cell.getId(), cellTable);
            } else {
                sheetCellMapper.updateCellContent(cell.getId(), content, data, cal, cellTable);
            }
        } else if (this.checkContentIsNull(content) == false) { // create new cell now ...
            cell = new SheetCell(tabId, x, y, content, data, cal);
            sheetCellMapper.insertSheetCell(tabId, cell, cellTable);
        }
    }

    // this will delete the cells start from the rows
    public void deleteRows(int sheetTab, int startRow, int endRow,
                           String cellTable) {
        if (sheetTab > 0) {
            sheetCellMapper.deleteRowStart2End(sheetTab, startRow, endRow, cellTable);
            sheetCellMapper.updateRowsWithSpan(sheetTab, endRow - startRow + 1, endRow, cellTable);

            // ok we also need look merged cells case ...
            // ok, we need check merge cell information -- such as: ftype:meg, json:[5,5,9,9] at this case we need change data ...
            //this.deceaseMergedCellsByRow(sheetTab, startRow, endRow); // included
        }
    }

    // this will delete the cells start from the rows
    public void deleteCols(int tabId, int startCol, int endCol,
                           String cellTable) {
        int totalSpan = endCol - startCol + 1;

        if (tabId > 0) {
            sheetCellMapper.deleteColStart2End(tabId, startCol, endCol, cellTable);
            sheetCellMapper.updateColsWithSpan(tabId, totalSpan, endCol, cellTable);

            // ok we also need look merged cells case ...
            // ok, we need check merge cell information -- such as: ftype:meg, json:[5,5,9,9] at this case we need change data ...
            //this.deceaseMergedCellsByCol(tabId, startCol, endCol); // included
        }
    }

    public List<Integer> getAllUnactiveTabIds(Integer documentId) {
        List<SheetTab> tabs = sheetTabMapper.findTabsByDocumentFile(documentId);

        List<Integer> ids = new ArrayList<Integer>();
        ids.add(new Integer(0));
        for (SheetTab tab : tabs) {
            if (!tab.getActive()) ids.add(tab.getId());
        }

        return ids;
    }

    public List<Integer> getOtherTabIds(Integer documentId, Integer tabId) {
        return sheetTabMapper.getOtherTabIdsByDocument(documentId, tabId);
    }

    public List<Integer> getAllTabIds(Integer documentId) {
        return sheetTabMapper.getAllTabIdsByDocument(documentId);
    }

    public void insertCells(Integer tabId, Integer row, Integer rowSpan,
                            Integer col, Integer colSpan, String moveDirection, String cellTable) {

        int maxrow = row+rowSpan-1;
        int maxcol = col+colSpan-1;

        if (moveDirection.equalsIgnoreCase("down")) {
			/*
	         * move the cells after minrow "rowSpan" step forward in the row direction
	         */
            sheetCellMapper.updateRowsAddSpanYY(tabId, rowSpan, row, col, maxcol, cellTable);
        } else {
			/*
	         * move the cells after mincol "colSpan" step forward in the col direction
	         */
            sheetCellMapper.updateColsAddSpanXX(tabId, colSpan, row, maxrow, col, cellTable);
        }
    }

    /*
     * for removeCell action, need change the row/col index of all cells after the removed span
     */
    public void deleteCells(Integer tabId, Integer minrow, Integer maxrow,
                            Integer mincol, Integer maxcol, String moveDirection, String cellTable) {
        // delete all cells ...
        sheetCellMapper.deleteCellsXXYY(tabId, minrow, maxrow, mincol, maxcol, cellTable);

        int rowSpan = maxrow-minrow+1;
        int colSpan = maxcol-mincol+1;

        if (moveDirection.equalsIgnoreCase("up")) {
	    	/*
	         * move the cells after maxrow "rowSpan" step forward in the row direction
	         */
            sheetCellMapper.updateRowsWithSpanYY(tabId, rowSpan, maxrow, mincol, maxcol, cellTable);
        } else {
        	/*
	         * move the cells after maxcol "colSpan" step forward in the col direction
	         */
            sheetCellMapper.updateColsWithSpanXX(tabId, colSpan, minrow, maxrow, maxcol, cellTable);
        }
    }

    public void sortSpan(Map<String,Object> jsonObj) {

        String sortOn = (String)jsonObj.get("sortOn");
        List<Integer> span = (List<Integer>)jsonObj.get("span");
        boolean expand = (boolean)jsonObj.get("expand");
        boolean allString = (boolean)jsonObj.get("allString");
        Map<String, Integer> diff = (Map<String, Integer>)jsonObj.get("diff");

        Integer tabId = span.get(0);
        SheetTab tab = sheetTabMapper.getById(tabId);

        // current we only handle col case ...
        if (sortOn.equalsIgnoreCase("col")) {
            Integer minCol = span.get(2);
            Integer maxCol = span.get(4);

    		/*
    		 * need change the cell with row index equal 'value' to 'key'
    		 */
            for (Map.Entry<String, Integer> entry : diff.entrySet()) {

                Integer key = new Integer(entry.getKey());
                Integer value = entry.getValue();

    			/*
				 * we will set "val" row to "-key" row first, then change it back after all diff processed
				 */
                if (expand) {
                    sheetCellMapper.updateRowDiff(tab.getId(), key, value, tab.getCellTable());
                } else {
                    sheetCellMapper.updateCellsDiff(tab.getId(), key, value, minCol, maxCol, tab.getCellTable());
                }
            }

            sheetCellMapper.upDownCell(tab.getId(), tab.getCellTable());
        }
    }


    public void moveRows(Map<String,Object> jsonObj, SheetTab tab) {

        Integer minCol = (Integer)jsonObj.get("mincol");
        Integer maxCol = (Integer)jsonObj.get("maxcol");
        boolean keyAsSource = (boolean)jsonObj.get("keyAsSource");
        Map<String, Integer> diff = (Map<String, Integer>)jsonObj.get("diff");

		/*
		 * need change the cell with row index equal 'value' to 'key'
		 */
        for (Map.Entry<String, Integer> entry : diff.entrySet()) {

            Integer key = new Integer(entry.getKey());
            Integer value = entry.getValue();

			/*
			 * we will set "val" row to "-key" row first, then change it back after all diff processed
			 */
            if (keyAsSource) {
                Integer temp = key;
                key = value;
                value = temp;
            }

            if (0 == minCol && 0 == maxCol) {
                sheetCellMapper.updateRowDiff(tab.getId(), key, value, tab.getCellTable());
            } else {
                sheetCellMapper.updateCellsDiff(tab.getId(), key, value, minCol, maxCol, tab.getCellTable());
            }
        }

        sheetCellMapper.upDownCell(tab.getId(), tab.getCellTable());
    }

    // get maxX from tab ...
    public Integer getMaxX(SheetTab tab) {
        return sheetCellMapper.getMaxX(tab.getId(), tab.getCellTable());
    }


    // this is the method to check content is null or not ...
    // content == null || content == JSONObject.NULL || content == ""
    // Return TRUE
    //     	{"tabId":"PbaksKJ41qI_","x":1,"y":11,"action":"createUpdate","content":"{}"}
    private boolean checkContentIsNull(String content) {
        boolean result = false;
        if (content == null || content.length() == 0
                || content.equals("{}") || content.equalsIgnoreCase("{\"data\":\"\"}")) {
            result = true;
        }

        return result;
    }

    // this action will update merged cells in column direction ...
    private void checkMergedCellsByCol(Integer tabId, int col, int colSpan) {
        List<SheetTabElement> elements = sheetTabElementMapper.findElementsByTab(tabId);
        for(SheetTabElement element : elements) {
            String type = element.getEtype();
            String content = element.getContent();
            if (type.equalsIgnoreCase("meg")) { // check whether it is merge cells
                List<Object> xyXY = JsonUtil.parseArray((String)content);
                if (xyXY.size() == 4) {
                    int minY = (int)xyXY.get(1);
                    int maxY = (int)xyXY.get(3);

                    if (minY > col) {
                        xyXY.set(1, minY+colSpan);
                        xyXY.set(3, maxY+colSpan);
                        sheetTabElementMapper.updateContent(element.getId(), JsonUtil.toJson(xyXY));
                    } else  if (minY <= col && maxY >= col) {
                        sheetTabElementMapper.delete(element.getId());
                    }
                }
            }
        }
    }

    // this action will update merged cells in column direction ...
    private void checkMergedCellsByRow(Integer tabId, int row, int rowSpan) {
        // ok, we need check merge cell information -- such as: ftype:meg, json:[5,5,9,9] at this case we need change data ...
        List<SheetTabElement> elements = sheetTabElementMapper.findElementsByTab(tabId);
        for(SheetTabElement element : elements) {
            String type = element.getEtype();
            String content = element.getContent();

            if (type.equalsIgnoreCase("meg")) { // check whether it is merge cells
                List<Object> xyXY = JsonUtil.parseArray((String)content);

                if (xyXY.size() == 4) {
                    int minX = (int)xyXY.get(0);
                    int maxX = (int)xyXY.get(2);

                    // ok, if there is  - update it. otherwise, do nothing, UI will insert a new one
                    if (minX > row) {
                        xyXY.set(0, minX+rowSpan);
                        xyXY.set(2, maxX+rowSpan);
                        sheetTabElementMapper.updateContent(element.getId(), JsonUtil.toJson(xyXY));
                    } else if (minX <= row && maxX >= row) {
                        sheetTabElementMapper.delete(element.getId());
                    }
                }
            }
        }
    }

    // this action will update merged cells in column direction ...
    private void deceaseMergedCellsByRow(Integer tabId, int startRow, int endRow) {
        List<SheetTabElement> elements = sheetTabElementMapper.findElementsByTab(tabId);
        for(SheetTabElement element : elements) {
            String type = element.getEtype();
            String content = element.getContent();

            if (type.equalsIgnoreCase("meg")) { // check whether it is merge cells
                List<Object> xyXY = JsonUtil.parseArray((String)content);

                if (xyXY.size() == 4) {
                    int minX = (int)xyXY.get(0);
                    int maxX = (int)xyXY.get(2);

                    // this is the rows count to be deleted ...
                    int deletedRowCount = endRow - startRow + 1;

                    // case 1: minX is under endRow
                    if (endRow <= minX) {
                        xyXY.set(0, minX-deletedRowCount);
                        xyXY.set(2, maxX-deletedRowCount);
                        sheetTabElementMapper.updateContent(element.getId(), JsonUtil.toJson(xyXY));
                    } else if ( (startRow <= minX && endRow > minX)
                            || (startRow > minX && startRow <= maxX)) { // delete it if it is between merged
                        sheetTabElementMapper.delete(element.getId());
                    }

                    /**
                     // case 2: start delete Row <= minX
                     else if (startRow <= minX && endRow > minX) {
                     if (endRow >= maxX) sheetTabElementMapper.delete(element.getId());
                     else { // this is in the middle
                     xyXY.set(0, startRow);
                     xyXY.set(2, maxX - deletedRowCount);
                     needUpdate = true;
                     }
                     }
                     // case 3: start delete Row > minX
                     else if (startRow > minX && startRow <= maxX) {
                     if (endRow >= maxX) xyXY.set(2, startRow - 1); // need reduce one
                     else xyXY.set(2, maxX - deletedRowCount);
                     needUpdate = true;
                     }
                     **/
                }

            }
        }
    }

    // this action will update merged cells in column direction ...
    private void deceaseMergedCellsByCol(Integer tabId, int startCol, int endCol) {
        List<SheetTabElement> elements = sheetTabElementMapper.findElementsByTab(tabId);
        for(SheetTabElement element : elements) {
            String type = element.getEtype();
            String content = element.getContent();

            if (type.equalsIgnoreCase("meg")) { // check whether it is merge cells
                List<Object> xyXY = JsonUtil.parseArray((String)content);

                if (xyXY.size() == 4) {
                    int minY = (int)xyXY.get(1);
                    int maxY = (int)xyXY.get(3);

                    // this is the rows count to be deleted ...
                    int deletedColCount = endCol - startCol + 1;

                    // case 1: minY is under endCol
                    if (endCol <= minY) {
                        xyXY.set(0, minY-deletedColCount);
                        xyXY.set(2, maxY-deletedColCount);
                        sheetTabElementMapper.updateContent(element.getId(), JsonUtil.toJson(xyXY));
                    } else if ( (startCol <= minY && endCol > minY)
                            || (startCol > minY && startCol <= maxY)) { // delete it if it is between merged
                        sheetTabElementMapper.delete(element.getId());
                    }

                }

            }
        }
    }

    public List<SheetCell> getCellByRanges(List<SheetCellRangeCondition> ranges, Integer nextCellId, Integer limit){
        SheetTab sheetTab = sheetTabMapper.getById(ranges.get(0).getTabId());
        return sheetCellMapper.getCellByRanges(ranges, nextCellId, limit, sheetTab.getCellTable());
    }

    public List<SheetCell> getCellOfRawDataByRanges(List<SheetCellRangeCondition> ranges, Integer nextCellId, Integer limit){
        SheetTab sheetTab = sheetTabMapper.getById(ranges.get(0).getTabId());
        return sheetCellMapper.getCellOfRawDataByRanges(ranges, nextCellId, limit, sheetTab.getCellTable());
    }

    public List<SheetCell> getCellOfStyleByRange(SheetCellRangeCondition range,Integer nextCellId, Integer limit){
        SheetTab sheetTab = sheetTabMapper.getById(range.getTabId());
        return sheetCellMapper.getCellOfStyleByRange(range, nextCellId, limit, sheetTab.getCellTable());
    }
    
    
    public List<SheetCell> listCellByTabId(Integer tabId, String cellTable, Integer offset, Integer limit){
        return sheetCellMapper.findCellsByTabOnExport(tabId, cellTable, offset, limit);
    }

    public List<SheetCell> loadAllCellsByDocument(Collection<SheetTab> tabs, Integer startCellId, Integer size){
        List<SheetCell> cells ;
        if (!tabs.isEmpty()) {
            List<Integer> tabIds = new ArrayList<>();
            for(SheetTab tab:tabs){
                tabIds.add(tab.getId());
            }
            String cellTable = tabs.iterator().next().getCellTable();
            cells = sheetCellMapper.loadAllCellsByDocument(startCellId, size, cellTable, tabIds);
        }else{
           cells = Collections.emptyList();
        }
        return cells;
    }
    
    public List<SheetCell> loadAllCellsByTabAndCalCells(SheetTab sheetTab, Integer startCellId, Integer size, List<Integer> otherTabIds){    	
        List<SheetCell> cells = sheetCellMapper.loadAllCellsByTabAndCalCells(startCellId, size,  sheetTab.getCellTable(), sheetTab.getId(), otherTabIds);
        return cells;
    }
    
    public static class SheetCellRangeCondition{
        private final int tabId;
        private final int startRow;
        private final int startCol;
        private final int endRow;
        private final int endCol;

        public SheetCellRangeCondition(int tabId, int startRow, int startCol, int endRow, int endCol) {
            super();
            this.tabId = tabId;
            this.startRow = startRow;
            this.startCol = startCol;
            this.endRow = endRow;
            this.endCol = endCol;
        }

        public int getTabId() {
            return tabId;
        }

        public int getStartRow() {
            return startRow;
        }

        public int getStartCol() {
            return startCol;
        }

        public int getEndRow() {
            return endRow;
        }

        public int getEndCol() {
            return endCol;
        }




    }

}
