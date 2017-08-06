package com.cubedrive.sheet.web.sheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.service.common.SeqService;
import com.cubedrive.base.service.document.DocumentConfigService;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DateTimeUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.base.utils.Triple;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetCellObj;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabObj;
import com.cubedrive.sheet.domain.sheet.SheetTabSpan;
import com.cubedrive.sheet.service.sheet.SheetCellService;
import com.cubedrive.sheet.service.sheet.SheetCellService.SheetCellRangeCondition;
import com.cubedrive.sheet.service.sheet.SheetDbTableService;
import com.cubedrive.sheet.service.sheet.SheetDbTableService.TableColumnInfo;
import com.cubedrive.sheet.service.sheet.SheetDbTableService.TableInfo;
import com.cubedrive.sheet.service.sheet.SheetService;
import com.cubedrive.sheet.service.sheet.SheetTabElementService;
import com.cubedrive.sheet.service.sheet.SheetTabService;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SheetOperationSupport extends BaseSheetController {
    
    @Autowired
    protected DocumentFileService documentFileService;

    @Autowired
    protected SheetService sheetService;

    @Autowired
    protected SheetTabService sheetTabService;

    @Autowired
    protected SheetCellService sheetCellService;

    @Autowired
    protected SheetTabElementService sheetTabElementService;
    
    @Autowired
    protected SheetDbTableService sheetDbTableService;
    
    @Autowired
    protected SeqService seqService;
    
    @Autowired
    protected DocumentConfigService documentConfigService;
    
    protected Function<SheetCell,SheetCellObj> cell2CellObjFunc = new Cell2CellObjFunction();
    
    protected Function<SheetCell,Object[]> cell2ArrayFunc = new Cell2ArrayFunction();
    

    
    private static class Cell2CellObjFunction implements Function<SheetCell,SheetCellObj>{
        @Override
        public SheetCellObj apply(SheetCell cell) {
            return new SheetCellObj(cell);
        }
    }
    
    private static class Cell2ArrayFunction implements Function<SheetCell, Object[]>{
        @Override
        public Object[] apply(SheetCell cell) {
            Object[] result;
            String payload = cell.getRawData() != null ? cell.getRawData() : cell.getContent();
            if(cell.getCal()!= null && cell.getCal())
                result = new Object[]{cell.getTabId(),cell.getX(), cell.getY(), payload, 1};
            else{
                result = new Object[]{cell.getTabId(),cell.getX(), cell.getY(), payload};
            }
            return result;
        }
        
    }
    
    protected Map<String,Object> doLoadCellOnDemand(String fileId, Integer sheetId, Integer startCellId, Integer size,  Boolean skipCal, boolean withRawData, Function transformFunc) throws Exception{
        User loginUser = this.getLoginUser();

        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
        Integer documentId = new Integer(fileDesEncrypter.doDecryptUrl(fileId));
        List<SheetCell> cells;
        DocumentFile document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));
        Integer nextStartCellId = 0;
        if(BaseAppConstants.SHEET_DB_TABLE_EXNAME.equals(document.getExname())){
            Triple<Integer, List<SheetCell>, TableInfo> triple= sheetDbTableService.loadCells(document,sheetId, startCellId, size);
            cells = triple.getT2();
            if(triple.getT1().equals(size)){
                nextStartCellId = startCellId + size ;
            }
        }else{
            SheetTab tab = sheetTabService.load(sheetId);
            if(withRawData)
                cells = sheetCellService.loadTabCellOfRawDataOnDemand(tab,startCellId, size,documentId, skipCal);
            else
                cells = sheetCellService.loadTabCellOnDemand(tab, startCellId, size, documentId, skipCal);
            if (cells.size() == size)
                nextStartCellId = cells.get(cells.size() - 1).getId();
        }
        // ok, we need check next start cellId
        
        List<Object> transformedCells = Lists.transform(cells, transformFunc);

        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", transformedCells.size());
        results.put("results", transformedCells);
        results.put("startCellId", nextStartCellId);
        return results;
    }
    
    protected Map<String,Object> doLoadDocCellOnDemand(String fileId, Integer sheetId, Integer startCellId, Integer size, Function transformFunc) throws Exception{
        User loginUser = this.getLoginUser();

        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
        Integer documentId = new Integer(fileDesEncrypter.doDecryptUrl(fileId));
        List<SheetCell> cells;
        DocumentFile document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));
        Integer nextStartCellId = 0;
        if(BaseAppConstants.SHEET_DB_TABLE_EXNAME.equals(document.getExname())){
            Triple<Integer, List<SheetCell>, TableInfo> triple= sheetDbTableService.loadCells(document,sheetId, startCellId, size);
            cells = triple.getT2();
            if(triple.getT1().equals(size)){
                nextStartCellId = startCellId + size ;
            }
        }else{
            SheetTab tab = sheetTabService.load(sheetId);           
            cells = sheetCellService.loadDocCellOnDemand(tab, startCellId, size, documentId);
            if (cells.size() == size)
                nextStartCellId = cells.get(cells.size() - 1).getId();
        }
        // ok, we need check next start cellId
        
        List<Object> transformedCells = Lists.transform(cells, transformFunc);

        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", transformedCells.size());
        results.put("results", transformedCells);
        results.put("startCellId", nextStartCellId);
        return results;
    }
    
    protected Map<String,Object> doLoadCornerCalCellOnDemand(String fileId, Integer sheetId, Integer startCellId, Integer size, Boolean skipCal, Function transformFunc) throws Exception{
        User loginUser = this.getLoginUser();
        
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
        Integer documentId = new Integer(fileDesEncrypter.doDecryptUrl(fileId));
        List<SheetCell> cells;
        DocumentFile document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));
        Integer nextStartCellId = 0;
        if(BaseAppConstants.SHEET_DB_TABLE_EXNAME.equals(document.getExname())){
            Triple<Integer, List<SheetCell>, TableInfo> triple= sheetDbTableService.loadCells(document,sheetId, startCellId, size);
            cells = triple.getT2();
            if(triple.getT1().equals(size)){
                nextStartCellId = startCellId + size ;
            }
        }else{
            SheetTab tab = sheetTabService.load(sheetId);
            cells = sheetCellService.loadTabCornerCalCellOnDemand(tab, startCellId, size, documentId, skipCal);
            if (cells.size() == size)
                nextStartCellId = cells.get(cells.size() - 1).getId();
        }
        // ok, we need check next start cellId
        
        List<Object> transformedCells = Lists.transform(cells, transformFunc);
        
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", transformedCells.size());
        results.put("results", transformedCells);
        results.put("startCellId", nextStartCellId);
        return results;
    }

    
    protected Map<String,Object> doLoadSheet(Integer tabId, Boolean notActiveTabFlag, Integer size, boolean loadNonDataCell, boolean withRawData, Function transformFunc){
        User loginUser = this.getLoginUser();
        SheetTab tab = sheetTabService.load(tabId);
        Integer nextStartCellId = null;
        List<SheetCell> cells; 
        if(withRawData)
            cells = sheetCellService.loadTabCellOfRawDataOnDemand(tab, 0, size, null, true);
        else
            cells = sheetCellService.loadTabCellOnDemand(tab, 0,size, null, true);
        
        List<SheetCell> tabNonDataCellOfStyle = new ArrayList<>() ;
        
        if(loadNonDataCell)
            tabNonDataCellOfStyle = sheetCellService.loadTabNonDataCellOfStyle(tab.getId(), tab.getCellTable());
        
        if (cells.size() >= size){
            nextStartCellId = cells.get(cells.size() - 1).getId();
        }
        
        List<SheetCell> tempCells = new ArrayList<SheetCell>(cells.size() + tabNonDataCellOfStyle.size());
        tempCells.addAll(cells);
        cells.clear();
        tempCells.addAll(tabNonDataCellOfStyle);
        tabNonDataCellOfStyle.clear();
        cells = tempCells;
        
        // ok, now we need load data ...
        Map<String, Object> messages = Maps.newHashMap();
        int floatingTotal = (int)sheetTabElementService.getCountByTabElement(tabId);
        messages.put("floatingTotal", floatingTotal);
        // set this tab as active ...
        if(false == notActiveTabFlag){
            sheetTabService.activeTab(tab);
        }
        List<Object> transformedCells = Lists.transform(cells, transformFunc);
        Integer totalSize = sheetCellService.getTabCellCount(tab);
        
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", totalSize);
        results.put("results", transformedCells);
        results.put("startCellId", nextStartCellId);
        results.put("message", messages);
        return results;
    }
    
    protected Map<String,Object> doLoadSheetRowCol(Integer tabId, Boolean notActiveTabFlag, Integer size, Function transformFunc){
        User loginUser = this.getLoginUser();
        SheetTab tab = sheetTabService.load(tabId);
        Integer nextStartCellId = null;
        List<SheetCell> cells;

        cells = sheetCellService.loadTabCornerCalCellOnDemand(tab, 0,size, null, true);
        
        if (cells.size() >= size){
            nextStartCellId = cells.get(cells.size() - 1).getId();
        }
        
        
        // ok, now we need load data ...
        Map<String, Object> messages = Maps.newHashMap();
        int floatingTotal = (int)sheetTabElementService.getCountByTabElement(tabId);
        messages.put("floatingTotal", floatingTotal);
        // set this tab as active ...
        if(false == notActiveTabFlag){
            sheetTabService.activeTab(tab);
        }
        List<Object> transformedCells = Lists.transform(cells, transformFunc);
        Integer totalSize = sheetCellService.getTabCellCount(tab);
        
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", totalSize);
        results.put("results", transformedCells);
        results.put("startCellId", nextStartCellId);
        results.put("message", messages);
        return results;
    }
    
    protected Map<String,Object> doLoadSheetInfo(String fileId, Integer startCellId, Integer size, Function transformFunc) throws Exception{
        User loginUser = this.getLoginUser();
        
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
        
        // ok, we need load cells json based on file id ...
        Integer documentId = new Integer(fileDesEncrypter.doDecryptUrl(fileId));
        DocumentFile document = documentFileService.getById(documentId);
        if (document == null) {
            document = createDefaultDocumentFile(loginUser);
            documentFileService.createDocumentFile(document);
            sheetService.createDefaultSS(document);
        }
        
        List<SheetCell> cells;
        Integer nextStartCellId = 0;
        Triple<Integer, Map<Integer, SheetTab>, Map<Integer, SheetTabSpan>> activeTabIdAndTabAndSpan = sheetTabService.getTabsWithSpan(document.getId());
        
        Integer activeTabId = activeTabIdAndTabAndSpan.getT1();
        SheetTab activeTab = activeTabIdAndTabAndSpan.getT2().get(activeTabId);
        Collection<SheetTabObj> tabs;
        if(BaseAppConstants.SHEET_DB_TABLE_EXNAME.equals(document.getExname())){
            Triple<Integer, List<SheetCell>, TableInfo> triple= sheetDbTableService.loadCells(document,activeTabId, startCellId, size);
            if(triple.getT1().equals(size)){
                nextStartCellId = startCellId + size ;
            }
            
            List<TableColumnInfo> tableColumns = triple.getT3().getTableColumns();
            final Integer sheetId = activeTabId;
            List<SheetCell> columnCellObjs = Lists.transform(tableColumns, new Function<TableColumnInfo, SheetCell>(){
                @Override
                public SheetCell apply(TableColumnInfo tableColumnInfo) {
                    return tableColumnInfo.toSheetCellOfRawData(sheetId);
                }
                
            });
            cells = new ArrayList<>(columnCellObjs.size() + triple.getT2().size());
            cells.addAll(columnCellObjs);
            cells.addAll(triple.getT2());
            tabs = Lists.newArrayList(sheetDbTableService.getSheetTabObj(sheetId,  triple.getT3()));
        }else{
            
            // ok, this method will return cells of size + cal is true in other
            // cells ...
            // also, we need return startCellId ...
            tabs = mergeTabWithSpan(activeTabIdAndTabAndSpan.getT2(), activeTabIdAndTabAndSpan.getT3());
            cells = sheetCellService.getCornerCalCellsWithSize(activeTab, activeTabIdAndTabAndSpan.getT2().values(), startCellId, size);
            
            if (cells.size() >= size){
                nextStartCellId = cells.get(cells.size() - 1).getId();
            }
            
        }
        
        List<Object> transformedCells = Lists.transform(cells, transformFunc);
        
        // ok, this is for user information ...
        Map<String, Object> userInfo = Maps.newHashMap();
        userInfo.put("email", loginUser.getEmail());
        userInfo.put("username", loginUser.getUsername());
        userInfo.put("userRealName", loginUser.getFirstname() + " " + loginUser.getLastname());
        
        Integer totalSize = activeTabIdAndTabAndSpan.getT3().get(activeTabId).getTotalCellNum();
        
        // ok, now we need load data ...
        Map<String, Object> messages = Maps.newHashMap();
        messages.put("fileName", document.getName());
        messages.put("fileStared", document.getStar());
        messages.put("exname", document.getExname());
        
        messages.put("floatingTotal", sheetTabElementService.getCountByTabElement(activeTabId));
        messages.put("sheets", tabs);
        messages.put("user", userInfo);
        messages.put("fileConfig", documentConfigService.find(document.getId()));
        messages.put("startCellId", nextStartCellId);
        messages.put("uuid",seqService.getUUID("document" + documentId));        
        
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", totalSize);
        results.put("results", transformedCells);
        results.put("message", messages);
        results.put("updateDate", DateTimeUtil.formatAsYYYYMMddHHmmss(document.getUpdateDate()));
        return results;
    }
    
    
    protected Map<String,Object> doLoadSheetInfo2(String fileId, Integer startCellId, Integer size, int throttle,  Function transformFunc) throws Exception{
        User loginUser = this.getLoginUser();
        
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
        
        // ok, we need load cells json based on file id ...
        Integer documentId = new Integer(fileDesEncrypter.doDecryptUrl(fileId));
        DocumentFile document = documentFileService.getById(documentId);
        
        if (document == null) {
            document = createDefaultDocumentFile(loginUser);
            documentFileService.createDocumentFile(document);
            sheetService.createDefaultSS(document);
        }
        
        List<SheetCell> cells;
        Integer nextStartCellId = 0;
        int totalCellCount = 0;
        Triple<Integer, Map<Integer, SheetTab>, Map<Integer, SheetTabSpan>> activeTabIdAndTabAndSpan = sheetTabService.getTabsWithSpan(document.getId());
        final Integer activeTabId = activeTabIdAndTabAndSpan.getT1();
        SheetTab activeTab = activeTabIdAndTabAndSpan.getT2().get(activeTabId);
        for(SheetTabSpan tabSpan:activeTabIdAndTabAndSpan.getT3().values()){
            totalCellCount += tabSpan.getTotalCellNum();
        }
        
        int activedCellCount = activeTabIdAndTabAndSpan.getT3().get(activeTabId).getTotalCellNum();
        List<SheetTabObj> tabObjs = mergeTabWithSpan(activeTabIdAndTabAndSpan.getT2(), activeTabIdAndTabAndSpan.getT3());
        SheetTabObj activedTabObj = Collections2.filter(tabObjs, new Predicate<SheetTabObj>(){
            @Override
            public boolean apply(SheetTabObj tabObj) {
                return activeTabId.equals(tabObj.getId());
            }
            
        }).iterator().next();
        String loadWay = "";
        if( totalCellCount < throttle){
            loadWay = "doc";
            cells = sheetCellService.loadAllCellsByDocument(activeTabIdAndTabAndSpan.getT2().values(), startCellId, size);
            for(SheetTabObj tabObj:tabObjs){
                tabObj.setAllCellDataLoaded(true);
            }
        }else if(activedCellCount < throttle){
            loadWay = "tab";
            List<Integer> otherTabIds = sheetCellService.getOtherTabIds(documentId, activeTabId);
            cells = sheetCellService.loadAllCellsByTabAndCalCells(activeTab, startCellId, size, otherTabIds);
            activedTabObj.setAllCellDataLoaded(true);
        }else{
            loadWay = "cor_cal";
            cells = sheetCellService.getCornerCalCellsWithSize(activeTab,activeTabIdAndTabAndSpan.getT2().values(), startCellId, size);
        }
       
        
        if (cells.size() >= size){
            nextStartCellId = cells.get(cells.size() - 1).getId();
        }
        
        List<Object> transformedCells = Lists.transform(cells, transformFunc);
        
        // ok, this is for user information ...
        Map<String, Object> userInfo = Maps.newHashMap();
        userInfo.put("email", loginUser.getEmail());
        userInfo.put("username", loginUser.getUsername());
        userInfo.put("userRealName", loginUser.getFirstname() + " " + loginUser.getLastname());
        
        Integer totalSize = activedTabObj.getTotalCellNum();
        
        // ok, now we need load data ...
        Map<String, Object> messages = Maps.newHashMap();
        messages.put("fileName", document.getName());
        messages.put("fileStared", document.getStar());
        messages.put("exname", document.getExname());
        messages.put("loadWay", loadWay);
        
        messages.put("floatingTotal", sheetTabElementService.getCountByTabElement(activedTabObj.getId()));
        messages.put("sheets", tabObjs);
        messages.put("user", userInfo);
        messages.put("fileConfig", documentConfigService.find(document.getId()));
        messages.put("startCellId", nextStartCellId);
        messages.put("uuid",seqService.getUUID("document" + documentId));        
        
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", totalSize);
        results.put("results", transformedCells);
        results.put("message", messages);
        results.put("updateDate", DateTimeUtil.formatAsYYYYMMddHHmmss(document.getUpdateDate()));
        return results;
        
    }

    protected Map<String,Object> doLoadActivedSheetOfFile(String fileId,Integer startCellId, Integer size, boolean loadNonDataCell, boolean withUuid, boolean withRawData,Function transformFunc) throws Exception{
        User loginUser = this.getLoginUser();

        DesEncrypter fileDesEncrypter = DecryptEncryptUtil
                .getDesEncrypterForFile(loginUser.getUsername());

        // ok, we need load cells json based on file id ...
        Integer documentId = new Integer(fileDesEncrypter.doDecryptUrl(fileId));
        DocumentFile document = documentFileService.getById(documentId);
        SheetTab activedTab = null;
        if (document == null) {
            document = createDefaultDocumentFile(loginUser);
            documentFileService.createDocumentFile(document);
            sheetService.createDefaultSS(document);
        }
        
        List<SheetCell> cells;
        Integer nextStartCellId = 0;
        
        int totalCellCount = 0;
        Triple<Integer, Map<Integer, SheetTab>, Map<Integer, SheetTabSpan>> activeTabIdAndTabAndSpan = sheetTabService.getTabsWithSpan(document.getId());
        final Integer activeTabId = activeTabIdAndTabAndSpan.getT1();
        SheetTab activeTab = activeTabIdAndTabAndSpan.getT2().get(activeTabId);
        for(SheetTabSpan tabSpan:activeTabIdAndTabAndSpan.getT3().values()){
            totalCellCount += tabSpan.getTotalCellNum();
        }
        
        List<SheetTabObj> tabs;
        
        if(BaseAppConstants.SHEET_DB_TABLE_EXNAME.equals(document.getExname())){
            Triple<Integer, List<SheetCell>, TableInfo> triple= sheetDbTableService.loadCells(document,activeTabId, startCellId, size);
            if(triple.getT1().equals(size)){
                nextStartCellId = startCellId + size ;
            }
            
            List<TableColumnInfo> tableColumns = triple.getT3().getTableColumns();
            final Integer sheetId = activeTabId;
            List<SheetCell> columnCellObjs = Lists.transform(tableColumns, new Function<TableColumnInfo, SheetCell>(){
                @Override
                public SheetCell apply(TableColumnInfo tableColumnInfo) {
                    return tableColumnInfo.toSheetCellOfRawData(sheetId);
                }
                
            });
            cells = new ArrayList<>(columnCellObjs.size() + triple.getT2().size());
            cells.addAll(columnCellObjs);
            cells.addAll(triple.getT2());
            tabs = Lists.newArrayList(sheetDbTableService.getSheetTabObj(sheetId,  triple.getT3()));
        }else{
    
            // ok, this method will return cells of size + cal is true in other
            // cells ...
            // also, we need return startCellId ...
            cells = sheetCellService.getActiveCellsWithSize(activeTab, activeTabIdAndTabAndSpan.getT2().values(), startCellId, size, withRawData);
            tabs = mergeTabWithSpan(activeTabIdAndTabAndSpan.getT2(), activeTabIdAndTabAndSpan.getT3());
            List<SheetCell> tabNonDataCellOfStyle = new ArrayList<>();
            if(loadNonDataCell)
                tabNonDataCellOfStyle = sheetCellService.loadTabNonDataCellOfStyle(activeTabId, activeTab.getCellTable());
            
            if (cells.size() >= size){
                nextStartCellId = cells.get(cells.size() - 1).getId();
            }
            
            List<SheetCell> tempCells = new ArrayList<SheetCell>(cells.size() + tabNonDataCellOfStyle.size());
            tempCells.addAll(cells);
            cells.clear();
            tempCells.addAll(tabNonDataCellOfStyle);
            tabNonDataCellOfStyle.clear();
            cells = tempCells;
        }
        
        List<Object> transformedCells = Lists.transform(cells, transformFunc);
        
        // ok, this is for user information ...
        Map<String, Object> userInfo = Maps.newHashMap();
        userInfo.put("email", loginUser.getEmail());
        userInfo.put("username", loginUser.getUsername());
        userInfo.put("userRealName", loginUser.getFirstname() + " " + loginUser.getLastname());

        Integer totalSize = activeTabIdAndTabAndSpan.getT3().get(activeTabId).getTotalCellNum();;

        // ok, now we need load data ...
        Map<String, Object> messages = Maps.newHashMap();
        messages.put("fileName", document.getName());
        messages.put("fileStared", document.getStar());
        messages.put("exname", document.getExname());
        
        messages.put("floatingTotal", sheetTabElementService.getCountByTabElement(activeTabId));
        messages.put("sheets", tabs);
        messages.put("user", userInfo);
        messages.put("fileConfig", documentConfigService.find(document.getId()));
        messages.put("startCellId", nextStartCellId);
        if(withUuid){
            messages.put("uuid",seqService.getUUID("document" + documentId));
        }
        
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", totalSize);
        results.put("results", transformedCells);
        results.put("message", messages);
        results.put("updateDate", DateTimeUtil.formatAsYYYYMMddHHmmss(document.getUpdateDate()));
        return results;
    }
    
    protected Map<String,Object> doLoadSheetsOfFile( String fileId, Integer startCellId,Integer size, boolean withRawData,Function transformFunc){
        User loginUser = this.getLoginUser();
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
        
        // ok, we need load cells json based on file id ...
        DocumentFile document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));
        if (document == null) {
            document = createDefaultDocumentFile(loginUser);
            documentFileService.createDocumentFile(document);
            sheetService.createDefaultSS(document);
        }
        
        Triple<Integer, Map<Integer, SheetTab>, Map<Integer, SheetTabSpan>> activeTabIdAndTabAndSpan = sheetTabService.getTabsWithSpan(document.getId());
        
        List<SheetTabObj> tabs = mergeTabWithSpan(activeTabIdAndTabAndSpan.getT2(), activeTabIdAndTabAndSpan.getT3());
        List<SheetCell> leftTopCells;
        List<SheetCell> cells;
        if(withRawData){
            leftTopCells = sheetCellService.getLeftTopCellsOfRawDataOfActiveTab(document.getId());
            cells= sheetCellService.getCalCellsOfRawData(document.getId(), startCellId, size);
        } else {
            leftTopCells = sheetCellService.getLeftTopCellsOfActiveTab(document.getId());
            cells= sheetCellService.getCalCells(document.getId(), startCellId, size);
        }
        
        Integer nextStartCellId = 0;
        if (cells.size() >= size){
            nextStartCellId = cells.get(cells.size() - 1).getId();
        }
        
        List<SheetCell> tempCells = new ArrayList<>(leftTopCells.size() + cells.size());
        tempCells.addAll(leftTopCells);
        leftTopCells.clear();
        tempCells.addAll(cells);
        cells.clear();
        cells = tempCells;
        
        List<Object> transformedCells = Lists.transform(cells, transformFunc);
        // ok, now we need load data ...
        Map<String, Object> messages = Maps.newHashMap();
        messages.put("fileName", document.getName());
        messages.put("fileStared", document.getStar());
        messages.put("exname", document.getExname());
        messages.put("floatings",sheetTabElementService.getActiveTabElements(document.getId()));
        messages.put("sheets", tabs);
        messages.put("fileConfig", documentConfigService.find(document.getId()));
        messages.put("startCellId", nextStartCellId);        
        
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("results", transformedCells);

        results.put("message", messages);
        results.put("updateDate",DateTimeUtil.formatAsYYYYMMddHHmmss(document.getUpdateDate()));
        return results;
    }
    
    
    protected Map<String,Object> doLoadRange(String range, Integer nextCellId, Integer limit, boolean withRawData, Function transformFunc){
        
        Map<String, Object> results = Maps.newHashMap();
        List<List<Integer>> rangeParams = JsonUtil.fromJson(range,new TypeReference<List<List<Integer>>>(){});
        Integer lastCellId = null;
        List<Object> transformedCells = Collections.emptyList();
        if(rangeParams.size() > 0) {
            List<SheetCellRangeCondition> rangeConditions = Lists.transform(rangeParams, new Function<List<Integer>,SheetCellRangeCondition>(){
    
                @Override
                public SheetCellRangeCondition apply(List<Integer> input) {
                    Integer tabId = input.get(0);
                    Integer startRow =  input.get(1);
                    Integer startCol = input.get(2);
                    Integer endRow =  input.get(3);
                    Integer endCol =input.get(4);
                    return new SheetCellRangeCondition(tabId, startRow, startCol, endRow, endCol);
                }
                
            });
            List<SheetCell> cells;
            if(withRawData)
                cells = sheetCellService.getCellOfRawDataByRanges(rangeConditions, nextCellId, limit);
            else
                cells = sheetCellService.getCellByRanges(rangeConditions, nextCellId, limit);
            
            if(cells.size()>0){
                transformedCells = Lists.transform(cells, transformFunc);
                if(cells.size() == limit){
                    lastCellId= cells.get(cells.size() -1).getId();
                }
            }
        }
        
        results.put("results", transformedCells);
        results.put("nextCellId", lastCellId);
        results.put("success", true);
        return results;
    }
    
    
    
    protected DocumentFile createDefaultDocumentFile(User author) {
        DocumentFile file = new DocumentFile(author);
        file.setCreateDate(new Date());
        file.setUpdateDate(new Date());
        file.setName("Untitled SpreadSheet");
        file.setStar(false);
        file.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
        return file;
    }
    
    private List<SheetTabObj> mergeTabWithSpan(Map<Integer,SheetTab> tabMap, Map<Integer, SheetTabSpan> tabSpanMap){
        List<SheetTabObj> tabObjList = new ArrayList<>(tabMap.size());
        List<SheetTab> tabList = new ArrayList<>(tabMap.values());
        Collections.sort(tabList, new Comparator<SheetTab>(){
            @Override
            public int compare(SheetTab o1, SheetTab o2) {
                int order =  o1.getTabOrder() - o2.getTabOrder() ;
                return order < 0 ? -1 : order==0 ? 0: 1;
            }
            
        });
        for(SheetTab sheetTab:tabList){
            SheetTabObj tabObj = new SheetTabObj(sheetTab);
            SheetTabSpan sheetTabSpan =tabSpanMap.get(sheetTab.getId());
            tabObj.setMaxRow(sheetTabSpan.getMaxRow());
            tabObj.setMaxCol(sheetTabSpan.getMaxCol());
            tabObj.setTotalCellNum(sheetTabSpan.getTotalCellNum());
            tabObjList.add(tabObj);
        }
        
        return tabObjList;
    }

}
