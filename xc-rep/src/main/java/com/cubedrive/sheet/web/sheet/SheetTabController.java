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
package com.cubedrive.sheet.web.sheet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabObj;
import com.cubedrive.sheet.service.sheet.SheetService;
import com.cubedrive.sheet.service.sheet.SheetTabElementService;
import com.cubedrive.sheet.service.sheet.SheetTabService;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/sheetTab")
public class SheetTabController extends BaseSheetController {

	@Autowired
	private UserService userService;

	@Autowired
	private DocumentFileService documentFileService;

	@Autowired
	private SheetService sheetService;

	@Autowired
	private SheetTabService sheetTabService;
	
	@Autowired
	private SheetTabElementService sheetTabElementService;

	/**
	 * This is the action to create a new sheet (tab) under a document
	 * 
	 * @param fileId
	 * @param pos
	 * @param name
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "create")
	public void create(@RequestParam(required = true) String fileId,
			@RequestParam(required = true) Integer position,
			@RequestParam(required = true) String name,
			@RequestParam(required = false) String color,
			HttpServletRequest request, HttpServletResponse response) {

		User loginUser = this.getLoginUser();

		// ok, we need load cells json based on file id ...
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		DocumentFile document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));

		int newTabOrder = sheetTabService.adjustSpreadTabOrder(document,position);
		SheetTab newTab = sheetTabService.insert(document, name, newTabOrder,true, color);

		// update document updateDate
		documentFileService.updateDoc2Now(document);

		Map<String, Object> results = Maps.newHashMap();
		results.put("id", newTab.getId());
		results.put("success", true);
		results.put("info", "Changes Saved");

		outputJson(results, request, response);

	}

    @RequestMapping(value = "update")
    public void update(@RequestParam(required = true) String sheetId,
            @RequestParam(required = false) Integer position,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color,
            HttpServletRequest request, HttpServletResponse response) {
        
    	User loginUser = this.getLoginUser();
        
        // get tab
        SheetTab tab = sheetTabService.load(new Integer(sheetId));
        
        // get all tabs under this document ... and check whether it is duplicate name
        List<SheetTabObj> tabs = sheetTabService.find(tab.getDocumentFile().getId());
        boolean isDuplicatedName = false;
        if(null != name){
            for (SheetTabObj tempTab : tabs) {
                if (tempTab.getName().equalsIgnoreCase(name) && tempTab.getId().intValue() != tab.getId().intValue()) {
                    isDuplicatedName = true;
                    break;
                }
            }
        }
        
        Map<String, Object> results = Maps.newHashMap();
        if (isDuplicatedName) {
            results.put("success", false);
            results.put("info", "A sheet with the name \"" + name + "\" already exists. Please enter another name.");
        } else {
            if(null != name){
                sheetTabService.updateSheetTabName(new Integer(sheetId), name);
            }
            if(null != position){
                // this is the method to reset order
                sheetTabService.resetOrder(tab, tab.getTabOrder(), position);
            }
            if(null != color){
                sheetTabService.updateSheetTabColor(new Integer(sheetId), color);
            }
            documentFileService.updateDoc2Now(tab.getDocumentFile());
            results.put("success", true);
            results.put("info", "Changes Saved");
        }
        
        outputJson(results, request, response);
        
    }
    
    @RequestMapping(value = "updateExtraInfo", method=RequestMethod.POST)
    public void updateExtraInfo(@RequestParam(required = true) Integer sheetId,
            @RequestParam(required = true) String extraInfo,
            HttpServletRequest request, HttpServletResponse response) {
        
        sheetTabService.updateSheetTabExtraInfo(sheetId, extraInfo);
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("info", "Changes Saved");
        outputJson(results, request, response);
    }
    

	@RequestMapping(value = "renameSheet")
	public void renameSheet(@RequestParam(required = true) String sheetId,
			@RequestParam(required = true) String name,
			HttpServletRequest request, HttpServletResponse response) {

		User loginUser = this.getLoginUser();

		// we need take care of unique name in this file ...
		SheetTab tab = sheetTabService.load(new Integer(sheetId));
		
		// get all tabs under this document ... and check whether it is duplicate name 
		List<SheetTabObj> tabs = sheetTabService.find(tab.getDocumentFile().getId());
		boolean isDuplicatedName = false;
		for (SheetTabObj tempTab : tabs) {
			if (tempTab.getName().equalsIgnoreCase(name) && tempTab.getId().intValue() != tab.getId().intValue()) {
				isDuplicatedName = true;
				break;
			}
		}
		
		Map<String, Object> results = Maps.newHashMap();
		if (isDuplicatedName) {
			results.put("success", false);
			results.put("info", "A sheet with the name \"" + name + "\" already exists. Please enter another name.");
		} else {
			sheetTabService.updateSheetTabName(new Integer(sheetId), name);
			documentFileService.updateDoc2Now(tab.getDocumentFile());
			results.put("success", true);
			results.put("info", "Changes Saved");
		}

		outputJson(results, request, response);
	}
    
    @RequestMapping(value = "changeSheetColor")
    public void changeSheetColor(@RequestParam(required = true) String sheetId,
        @RequestParam(required = true) String color,
        HttpServletRequest request, HttpServletResponse response) {
        
    	User loginUser = this.getLoginUser();
        
        // we need take care of unique name in this file ...
        SheetTab tab = sheetTabService.load(new Integer(sheetId));
        
		Map<String, Object> results = Maps.newHashMap();        
        sheetTabService.updateSheetTabColor(new Integer(sheetId), color);
        documentFileService.updateDoc2Now(tab.getDocumentFile());
        results.put("success", true);
        results.put("info", "Changes Saved");
        
        outputJson(results, request, response);
    }

	@RequestMapping(value = "deleteSheet")
	public void deleteSheet(@RequestParam(required = true) String sheetId,
			HttpServletRequest request, HttpServletResponse response) {

		User loginUser = this.getLoginUser();

		// get tab
		SheetTab tab = sheetTabService.load(new Integer(sheetId));

		// check to see whether this is the only tab, if yes, not allowed to
		// delete ...
		List<SheetTabObj> tabs = sheetTabService.find(tab.getDocumentFile().getId());

		Map<String, Object> results = Maps.newHashMap();
		if (tabs.size() > 1) {
			// update document updateDate
			documentFileService.updateDoc2Now(tab.getDocumentFile());
			SheetTab firstTab = sheetTabService.findFirstTab(tab.getDocumentFile().getId());
			sheetTabService.activeTab(firstTab);

			sheetTabService.deleteTab(tab.getId(), tab.getCellTable());

			results.put("success", true);
			results.put("info", "Changes Saved");
		} else {
			results.put("success", false);
			results.put("info", "At lease one sheet need to be existing.");
		}

		outputJson(results, request, response);
	}

	@RequestMapping(value = "changeSheetOrder")
	public void changeSheetOrder(@RequestParam(required = true) String sheetId,
			@RequestParam(required = true) Integer prePos,
			@RequestParam(required = true) Integer curPos,
			HttpServletRequest request, HttpServletResponse response) {

		User loginUser = this.getLoginUser();

		// get tab
		SheetTab tab = sheetTabService.load(new Integer(sheetId));

		// this is the method to reset order
		sheetTabService.resetOrder(tab, prePos, curPos);

		// update document updateDate
		documentFileService.updateDoc2Now(tab.getDocumentFile());

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);
		results.put("info", "Changes Saved");

		outputJson(results, request, response);
	}

	/**
	 * this is for copy and paste function UI need pass the following
	 * parameters: 1: tabId, 2: tabname, 3: position Tabname is paste tab name,
	 * postion is which place this new tab will be in.
	 * http://localhost:8080/ROOT
	 * /spreadsheetTab/copyPaste?tabId=4gI4Kbj-Fmk_&name=sheet500&position=3
	 * 
	 * @param tabId
	 * @param name
	 * @param position
	 * @return
	 */
	@RequestMapping(value = "copySheet")
	public void copySheet(@RequestParam(required = true) String oldSheetId,
			@RequestParam(required = true) String newSheetName,
			@RequestParam(required = true) Integer pos,
			HttpServletRequest request, HttpServletResponse response) {

		User loginUser = this.getLoginUser();

		int _tabId = Integer.parseInt(oldSheetId);
		SheetTab tab = sheetTabService.load(_tabId);
		
		// check to see whether name is duplicated ...
		// get all tabs under this document ... and check whether it is duplicate name 
		List<SheetTabObj> tabs = sheetTabService.find(tab.getDocumentFile().getId());
		boolean isDuplicatedName = false;
		for (SheetTabObj tempTab : tabs) {
			if (tempTab.getName().equalsIgnoreCase(newSheetName)) {
				isDuplicatedName = true;
				break;
			}
		}
		
		Map<String, Object> results = Maps.newHashMap();
		if (isDuplicatedName) {
			results.put("success", false);
			results.put("info", "A sheet with the name \"" + newSheetName + "\" already exists. Please enter another name.");
			outputJson(results, request, response);		
			return;
		} 

		// create new tab
		int newTabOrder = sheetTabService.getNewTabOrder(tab.getDocumentFile(), pos);
		SheetTab targetTab = sheetTabService.insert(tab.getDocumentFile(), newSheetName, newTabOrder, false, tab.getColor());

		// ok, make copy cells information
		sheetTabService.copyTab(tab, targetTab,tab.getCellTable(), targetTab.getCellTable());

		// update document updateDate
		documentFileService.updateDoc2Now(targetTab.getDocumentFile());
		
		results.put("success", true);
		results.put("info", "Changes Saved");
		results.put("id", targetTab.getId().toString());
		outputJson(results, request, response);
	}
}
