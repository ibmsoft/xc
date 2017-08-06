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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.base.domain.document.DocumentConfig;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentConfigMapper;
import com.cubedrive.base.persistence.document.DocumentFileMapper;
import com.cubedrive.sheet.SheetTableSequence;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;
import com.cubedrive.sheet.persistence.sheet.SheetTabMapper;
import com.google.common.collect.Lists;

@Service
public class SheetService {

	@Autowired
	private DocumentFileMapper documentFileMapper;

	@Autowired
	private SheetTabMapper sheetTabMapper;
	
	@Autowired
	private SheetCellMapper sheetCellMapper;
	
	@Autowired
	private SheetTabElementMapper sheetTabElementMapper;
	
	@Autowired
	private DocumentConfigMapper documentConfigMapper;

	// ok, this will create one SS file now
	// Default 3 tab will be created. And documentFile need to be passed
	public List<SheetTab> createDefaultSS(DocumentFile documentFile) {
		String cellTable = SheetTableSequence.instance().sheetCellTable();
		SheetTab tab1 = generateSpreadSheetTab(documentFile, "Sheet1", 1, true, cellTable);
		SheetTab tab2 = generateSpreadSheetTab(documentFile, "Sheet2", 2, false, cellTable);
	    SheetTab tab3 = generateSpreadSheetTab(documentFile, "Sheet3", 3, false, cellTable);

		sheetTabMapper.insertSheetTab(tab1);
		sheetTabMapper.insertSheetTab(tab2);
		sheetTabMapper.insertSheetTab(tab3);

		return Lists.newArrayList(tab1, tab2, tab3);
	}
	
	public List<SheetTab> createSSWith1Tab(DocumentFile documentFile) {
		String cellTable = SheetTableSequence.instance().sheetCellTable();
		SheetTab tab1 = generateSpreadSheetTab(documentFile, "Sheet1", 1, true, cellTable);
		sheetTabMapper.insertSheetTab(tab1);
		return Lists.newArrayList(tab1);
	}
	
	/**
	 * ok, this method will copy from sheet to sheet 
	 * ToFile is empty at this case ...
	 * 
	 * @param fromFile
	 * @param toFile
	 */
	public void copyFile(DocumentFile fromFile, DocumentFile toFile) {
		List<SheetTab> tabs = sheetTabMapper.findTabsByDocumentFile(fromFile.getId());
		
		// this is for new cell table ...
		String _cellTableName = SheetTableSequence.instance().sheetCellTable();
		
		// this is a map ...
		Map<String, String> tabMap = new HashMap<String, String>();
		for(SheetTab sourceTab : tabs) {
			SheetTab targetTab = new SheetTab();
			targetTab.setDocumentFile(toFile);
			targetTab.setName(sourceTab.getName());
			targetTab.setTabOrder(sourceTab.getTabOrder());
			targetTab.setActive(sourceTab.getActive());
			targetTab.setColor(sourceTab.getColor());
			targetTab.setCellTable(_cellTableName);
			sheetTabMapper.insertSheetTab(targetTab);
			
			// this is same as - sheetTabService.copyTab(tab, targetTab, targetTab.getCellTable());
			sheetCellMapper.copySheetCellFromSheet(sourceTab.getId(), targetTab.getId(), sourceTab.getCellTable(), _cellTableName);
			
			// this will copy items from sheet_element table ...
			List<SheetTabElement> elements = sheetTabElementMapper.findElementsByTab(sourceTab.getId());
			for(SheetTabElement element : elements) {
				element.setTab(targetTab);
				String oldItemString = "\"span\":[" + sourceTab.getId().toString() + ",";
				String newItemString = "\"span\":[" + targetTab.getId().toString() + ",";
				String json = element.getContent().replace(oldItemString, newItemString);
				
				oldItemString = "span:[" + sourceTab.getId().toString() + ",";
				newItemString = "span:[" + targetTab.getId().toString() + ",";
				json = json.replace(oldItemString, newItemString);
				
				// check whether includes sheetId ...
				oldItemString = "sheetId:" + sourceTab.getId().toString();
				newItemString = "sheetId:" + targetTab.getId().toString();
				json = json.replace(oldItemString, newItemString);
				
				oldItemString = "\"sheetId\":" + sourceTab.getId().toString();
				newItemString = "\"sheetId\":" + targetTab.getId().toString();
				json = json.replace(oldItemString, newItemString);
				
				element.setContent(json);
				sheetTabElementMapper.insert(element);
			}
			
			tabMap.put(sourceTab.getId().toString(), targetTab.getId().toString());
		}
		
		// ok, we need check whether cell field include cross reference...: span:[tabId,
		this.updateCellCrossSheetRef(tabMap, _cellTableName);
		
		// ok, need copy file config too - which save name manager ...
        List<DocumentConfig> allConfigs = documentConfigMapper.find(fromFile.getId());		
		for(DocumentConfig config : allConfigs) {
			// ok, need replace tabId ... for this case:
			// "span":[566,27,9,30,9] TO "span":[tabId,....
			String json = config.getJson();			
			for (String key : tabMap.keySet()) {
				// check whether it is match ...
				String oldItemString = "\"span\":[" + key + ",";
				String newItemString = "\"span\":[" + tabMap.get(key) + ",";
				json = json.replace(oldItemString, newItemString);
				
				oldItemString = "span:[" + key + ",";
				newItemString = "span:[" + tabMap.get(key) + ",";
				json = json.replace(oldItemString, newItemString);
				
				// check whether includes sheetId ...
				oldItemString = "sheetId:" + key;
				newItemString = "sheetId:" + tabMap.get(key);
				json = json.replace(oldItemString, newItemString);
				
				oldItemString = "\"sheetId\":" + key;
				newItemString = "\"sheetId\":" + tabMap.get(key);
				json = json.replace(oldItemString, newItemString);
			}
			config.setJson(json);
			config.setDocumentFile(toFile);
			documentConfigMapper.insert(config);
		}
	}
	
	/**
	 * This function only called after copy tab.
	 * this function will check cell whether include cross sheet reference,
	 * if yes, replace it.
	 * 
	 * @param sourceTabId
	 * @param targetTabId
	 */
	public void updateCellCrossSheetRef(Map<String, String> tabIdMap, String _cellTableName) {
		
		Map<String, String> spanMaps = new HashMap<String, String>();
		for (String key : tabIdMap.keySet()) {
			// check whether it is match ...
			String oldItemString = "\\\"span\\\":[" + key + ",";
			String newItemString = "\\\"span\\\":[" + tabIdMap.get(key) + ",";
			spanMaps.put(oldItemString, newItemString);
		}
		
		for (String fromTabId : tabIdMap.keySet()) {		
			String toTabId = tabIdMap.get(fromTabId);
			List<SheetCell> calCells = sheetCellMapper.findCalCellsByTab(new Integer(toTabId), _cellTableName);	
			for (SheetCell cell : calCells) {			
				String content = cell.getContent();
				// loop each ...
				for (String oldStr : spanMaps.keySet()) {
					if (content.contains(oldStr)) {
						content = content.replace(oldStr, spanMaps.get(oldStr));
						sheetCellMapper.updateContentOnly(cell.getId(), content, _cellTableName);
					}
				}
			}
		}
	}
	
	/**
	 * This is the function to delete all tabs information under this file ...
	 * 
	 * @param documentFile
	 */
	public void deleteAllTabs(DocumentFile documentFile) {
		List<SheetTab> tabs = sheetTabMapper.findTabsByDocumentFile(documentFile.getId());
		
		String cellTableName = null;
		for(SheetTab sourceTab : tabs) {
			sheetTabElementMapper.deleteByTab(sourceTab.getId(), null);
			cellTableName = sourceTab.getCellTable();
		}
		
		// delete all tabs ...
		if (cellTableName != null)
		    sheetTabMapper.deleteTabsByDocumentFile(documentFile.getId(), cellTableName);	
	}
	
	// this is private function 
	private SheetTab generateSpreadSheetTab(DocumentFile documentFile,
			String tabName, Integer tabOrder, Boolean isActive, String cellTable) {

		SheetTab ssTab = new SheetTab();
		ssTab.setDocumentFile(documentFile);
		ssTab.setName(tabName);
		ssTab.setTabOrder(tabOrder);
		ssTab.setActive(isActive);
		ssTab.setCellTable(cellTable);
		return ssTab;
	}

}
