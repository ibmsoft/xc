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
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.domain.sheet.SheetTabElementObj;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;

@Service
public class SheetTabElementService {
	
	@Autowired
	private SheetTabElementMapper sheetTabElementMapper;
	
	public List<SheetTabElementObj> getActiveTabElements(Integer documentId) {
		List<SheetTabElement> elements = sheetTabElementMapper.findActiveTabElements(documentId);
		
		List<SheetTabElementObj> results = new ArrayList<SheetTabElementObj>();
		for (SheetTabElement element : elements) {
			results.add(new SheetTabElementObj(element));
		}
		
		return results;
	}

	public List<SheetTabElementObj> getElements(Integer tabId) {
		List<SheetTabElement> elements =  sheetTabElementMapper.findElementsByTab(tabId);
		
		List<SheetTabElementObj> results = new ArrayList<SheetTabElementObj>();
		for (SheetTabElement element : elements) {
			results.add(new SheetTabElementObj(element));
		}
		
		return results;
	}
	
	public SheetTabElement create(Map<String,Object> jsonObj, SheetTab sheetTab) {
		String name = jsonObj.get("name").toString();
		String eType = jsonObj.get("ftype").toString();
		String json = jsonObj.get("json").toString();

		SheetTabElement sheetTabElement = new SheetTabElement(name, eType, json);
		sheetTabElement.setTab(sheetTab);
		sheetTabElementMapper.insert(sheetTabElement);
		
		return sheetTabElement;
	}
	
	public SheetTabElement create(Map<String,Object> jsonObj, SheetTab sheetTab, String originalTabId) {
		String name = jsonObj.get("name").toString();
		String eType = jsonObj.get("ftype").toString();
		String json = jsonObj.get("json").toString();
		
		String oldItemString = "\"span\":[" + originalTabId + ",";
		String newItemString = "\"span\":[" + sheetTab.getId().toString() + ",";
		json = json.replace(oldItemString, newItemString);
		
		oldItemString = "span:[" + originalTabId + ",";
		newItemString = "span:[" + sheetTab.getId().toString() + ",";
		json = json.replace(oldItemString, newItemString);
		
		// change sheetId if any ... for example, for image / widget ...
		oldItemString = "\"sheetId\":" + originalTabId;
		newItemString = "\"sheetId\":" + sheetTab.getId().toString();
		json = json.replace(oldItemString, newItemString);	

		SheetTabElement sheetTabElement = new SheetTabElement(name, eType, json);
		sheetTabElement.setTab(sheetTab);
		sheetTabElementMapper.insert(sheetTabElement);
		
		return sheetTabElement;
	}
	
	public void update(Map<String,Object> jsonObj, SheetTab sheetTab) {
		String name = jsonObj.get("name").toString();
		String eType = jsonObj.get("ftype").toString();
		String json = jsonObj.get("json").toString();

		sheetTabElementMapper.update(sheetTab.getId(), name, json);
	}
	
	public void createUpdate(Map<String,Object> jsonObj, SheetTab sheetTab) {		
		String name = jsonObj.get("name").toString();
		String eType = jsonObj.get("ftype").toString();
		String json = jsonObj.get("json").toString();
		Integer tabId = sheetTab.getId();
		List<SheetTabElement> elements =  sheetTabElementMapper.getElementsByName(tabId, eType, name);
		if(0 < elements.size()){
			sheetTabElementMapper.update(tabId, name, json);
		}else{
			SheetTabElement sheetTabElement = new SheetTabElement(name, eType, json);
			sheetTabElement.setTab(sheetTab);
			sheetTabElementMapper.insert(sheetTabElement);
		}
	}
	
	public void remove(Map<String,Object> jsonObj, SheetTab sheetTab) {
		String name = jsonObj.get("name").toString();
		sheetTabElementMapper.deleteByTab(sheetTab.getId(), name);
	}
	
	public long getCountByTabElement(Integer tabId){
		return sheetTabElementMapper.countElementByTab(tabId);
	}
	
	public List<SheetTabElementObj> loadTabElementOnDemand(Integer tabId, Integer startElementId, Integer size){
		List<SheetTabElement> elements = sheetTabElementMapper.loadElementsOnDemand(startElementId, size, tabId);
		List<SheetTabElementObj> elementObjs = new ArrayList<>(elements.size());
		for(SheetTabElement element:elements){
			elementObjs.add(new SheetTabElementObj(element));
		}
		return elementObjs;
	}
	
}
