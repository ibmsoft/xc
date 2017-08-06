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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.utils.Triple;
import com.cubedrive.sheet.SheetTableSequence;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.domain.sheet.SheetTabIdOrderInfo;
import com.cubedrive.sheet.domain.sheet.SheetTabObj;
import com.cubedrive.sheet.domain.sheet.SheetTabSpan;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;
import com.google.common.collect.Sets;

@Service
public class SheetTabService {
	
	@Autowired
	private SheetTabMapper sheetTabMapper;
	
	@Autowired
	private SheetCellMapper sheetCellMapper;
	
	@Autowired
	private SheetTabElementMapper sheetTabElementMapper;
	
	public SheetTab findFirstTab(Integer documentId) {
		return sheetTabMapper.getFirstSheetTabByDocumentFile(documentId);
	}
	
	public List<SheetTabObj> find(Integer documentId) {	
		List<SheetTab> tabs = sheetTabMapper.findTabsByDocumentFile(documentId);
		
		List<SheetTabObj> results = new ArrayList<SheetTabObj>();
		for (int i=0; i< tabs.size(); i++) {
			results.add(new SheetTabObj(tabs.get(i)));
		}
		
		return results;
	}
	
	public List<SheetTab> findTabsByDocument(Integer documentId){
	    return sheetTabMapper.findTabsByDocumentFile(documentId);
	}
	
	public SheetTabSpan getTabSpan(SheetTab tab){
	    return sheetTabMapper.getTabSpan(tab.getId(), tab.getCellTable());
	}
    
    public Triple<Integer, Map<Integer, SheetTab>,  Map<Integer, SheetTabSpan>> getTabsWithSpan(Integer documentId) {
        List<SheetTab> tabs = sheetTabMapper.findTabsByDocumentFile(documentId);
        if(tabs.size() > 0){
            boolean firstTabActive = true;
            Integer activeTabId = tabs.get(0).getId();
            
            Map<Integer,SheetTab> tabMap = new HashMap<>();
            for(SheetTab tab:tabs){
                tabMap.put(tab.getId(), tab);
                if(tab.getActive() != null && tab.getActive()){
                    activeTabId = tab.getId();
                    firstTabActive = false;
                }
            }
            
            List<SheetTabSpan> tabSpans = sheetTabMapper.getTabSpans(tabMap.keySet(), tabs.get(0).getCellTable());
            Map<Integer, SheetTabSpan> tabSpanMap = new HashMap<>();
            if(tabSpans.isEmpty()){
                for(Integer tabId:tabMap.keySet()){
                    tabSpanMap.put(tabId, new SheetTabSpan(tabId, 0, 0, 0));
                }
            }else{
                for(SheetTabSpan tabSpan:tabSpans){
                    tabSpanMap.put(tabSpan.getTabId(), tabSpan);
                }
                
                for(Integer tabId: Sets.difference(tabMap.keySet(), tabSpanMap.keySet())){
                    tabSpanMap.put(tabId, new SheetTabSpan(tabId, 0, 0, 0));
                }
                
            }
            
            if(firstTabActive){
                sheetTabMapper.activeSheetTab(documentId, activeTabId);
            }
            return new Triple<>(activeTabId, tabMap, tabSpanMap);
        }else{
            return new Triple<>(null, null, null);
        }
       
    }
	
	public SheetTab load(Integer tabId) {
		return sheetTabMapper.getById(tabId);
	}

	/**
	 * This is the method to delete tab ...
	 * @param tabId
	 */
	public void deleteTab(Integer tabId, String cellTable) {
		sheetTabMapper.deleteById(tabId, cellTable);
		sheetTabElementMapper.deleteByTab(tabId, null);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public SheetTab updateSheetTabName(Integer tabId, String tabName) {
		Date now= new Date();
		SheetTab _tab = sheetTabMapper.getById(tabId);
		
		_tab.setName(tabName);
		sheetTabMapper.updateSheetTab(_tab);
		
		return _tab;
	}
	
    @Transactional(rollbackFor = Exception.class)
    public SheetTab updateSheetTabColor(Integer tabId, String color) {
        SheetTab _tab = sheetTabMapper.getById(tabId);
        
        _tab.setColor(color);
        sheetTabMapper.updateSheetTab(_tab);
        
        return _tab;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public SheetTab updateSheetTabExtraInfo(Integer tabId, String extraInfo){
        SheetTab _tab = sheetTabMapper.getById(tabId);
        
        _tab.setExtraInfo(extraInfo);
        sheetTabMapper.updateSheetTab(_tab);
        
        return _tab;
    }
	
	public void activeTab(SheetTab tab) {
	    if (tab != null && tab.getId() != null)  sheetTabMapper.activeSheetTab(tab.getDocumentFile().getId(), tab.getId());
	}
	
	public SheetTab insert(DocumentFile documentFile,
			String tabName, Integer tabOrder, Boolean isActive, String color) {
		
		SheetTab firstTab = sheetTabMapper.getFirstSheetTabByDocumentFile(documentFile.getId());
		String cellTab = null;
		if (firstTab != null) {
			cellTab = firstTab.getCellTable();
		} else {
			cellTab = SheetTableSequence.instance().sheetCellTable();
		}

		SheetTab tab = this.generateSheetTab(documentFile, tabName,
				tabOrder, isActive, cellTab, color);
		
		sheetTabMapper.insertSheetTab(tab);
		if (isActive)
			sheetTabMapper.activeSheetTab(documentFile.getId(), tab.getId());
		
		return tab;
	}
	
	/**
	 * This will copy source tab to target tab ...
	 * @param sourceTab
	 * @param targetTab
	 * @param destCellTable
	 */
	public void copyTab(SheetTab sourceTab, SheetTab targetTab, String sourceCellTable, String destCellTable) {
		sheetCellMapper.copySheetCellFromSheet(sourceTab.getId(), targetTab.getId(), sourceCellTable, destCellTable);
		
		// ok this is a special case for mini chart copy ..
		// {"name":"minichart","rng":[{"span":[1,6,5,6,5],"type":1}],"opt":{"base":{"span":[1,3,3,5,3],"type":1},"type":"column","pc":"rgb(0,0,128)","nc":"rgb(0,0,128)"},"id":"ext-gen2045-20140929130315361"}
		// We also need change tabId which included inside span - first paramter 
		// first we need load elements
		List<SheetTabElement> elements = sheetTabElementMapper.findElementsByTab(sourceTab.getId());
		for(SheetTabElement element : elements) {
			element.setTab(targetTab);
			String oldItemString = "\"span\":[" + sourceTab.getId().toString() + ",";
			String newItemString = "\"span\":[" + targetTab.getId().toString() + ",";
			String json = element.getContent().replace(oldItemString, newItemString);
			element.setContent(json);
			sheetTabElementMapper.insert(element);
		}
	}
	
	/**
	 * this is the function to adjust the order of tab
	 * @param documentFile
	 * @param position
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int adjustSpreadTabOrder(DocumentFile documentFile, Integer position) {
		int newTabOrder = position;
		List<SheetTabIdOrderInfo> tabIdOrderInfoList = sheetTabMapper
				.findSheetTabIdOrderInfoByDocument(documentFile.getId());
		
		int shiftPos = -1;
		if (tabIdOrderInfoList != null && !tabIdOrderInfoList.isEmpty()) {
			for (int i = 0; i < tabIdOrderInfoList.size(); i++) {
				SheetTabIdOrderInfo _info = tabIdOrderInfoList.get(i);
				int _tabOrder = _info.getTabOrder();
				
				if (i >= position) {
					shiftPos = i;
					break;
				} else {
					newTabOrder = _tabOrder + 1;
				}
			}
			
			if (shiftPos != -1) {
				List<SheetTabIdOrderInfo> shiftList = tabIdOrderInfoList.subList(shiftPos, tabIdOrderInfoList.size());
				sheetTabMapper.shiftSheetTab(documentFile.getId(), shiftList);
			}

		} else {
			newTabOrder = 1;
		}

		return newTabOrder;
	}
	
	/**
	 * This is the method to reset order for the tab ..
	 * @param tab
	 * @param position
	 */
	public void resetOrder(SheetTab tab, Integer prePos, Integer curPos) {	
		
		List<SheetTabIdOrderInfo> tabIdOrderInfoList = sheetTabMapper
				.findSheetTabIdOrderInfoByDocument(tab.getDocumentFile().getId());
		
		List<SheetTabIdOrderInfo> shiftList = new ArrayList<SheetTabIdOrderInfo>();
		int _tabNewOrder = -1;
		for (int i = 0; i < tabIdOrderInfoList.size(); i++) {
			SheetTabIdOrderInfo _info = tabIdOrderInfoList.get(i);
			if (i == curPos) {			
				if (curPos > prePos) {
					_tabNewOrder = _info.getTabOrder() + 1;	
				} else {	
					_tabNewOrder = _info.getTabOrder();
					shiftList.add(_info);
				}
			}
			
			if (i > curPos && _info.getTabId().intValue() != tab.getId().intValue()) {
				shiftList.add(_info);
			}			
		}
		
		if (_tabNewOrder > 0) sheetTabMapper.updateTabOrder(tab.getId(), _tabNewOrder);
		if (shiftList.size() > 0) sheetTabMapper.shiftSheetTab(tab.getDocumentFile().getId(), shiftList);
	}
	
	/**
	 * This function will return a new order tab id for this position
	 * @param documentId
	 * @param position
	 * @return
	 */
	public int getNewTabOrder(DocumentFile document, Integer position) {
		int newTabOrder = 1;
		List<SheetTab> spreadsheetTabs = sheetTabMapper.findTabsByDocumentFile(document.getId());
		if (spreadsheetTabs == null) return newTabOrder;
        
        // ok, if this is last one
        int total = spreadsheetTabs.size();
        if (position >= total) {
            newTabOrder = spreadsheetTabs.get(total-1).getTabOrder() + 1;
        } else {
        	for (int i=position; i<spreadsheetTabs.size(); i++) {
            	SheetTab it = spreadsheetTabs.get(i);
            	int tempOrder = it.getTabOrder();
            	if (i == position)  newTabOrder = tempOrder + 1;
                if (i > position) {
                    sheetTabMapper.updateTabOrder(it.getId(), tempOrder + 1);
                }
            }
        }
		
		return newTabOrder;
	}
	
	public SheetTab getTabByDocumentAndIndex(Integer documentId, Integer index){
	    return sheetTabMapper.getTabByDocumentAndIndex(documentId, index);
	}

	// ============================================
	private SheetTab generateSheetTab(DocumentFile documentFile,
			String tabName, Integer tabOrder, Boolean isActive, 
			String cellTable, String color) {

		SheetTab ssTab = new SheetTab();
		ssTab.setDocumentFile(documentFile);
		ssTab.setName(tabName);
		ssTab.setTabOrder(tabOrder);
		ssTab.setActive(isActive);
		ssTab.setCellTable(cellTable);
		ssTab.setColor(color);
		return ssTab;
	}
	
}
