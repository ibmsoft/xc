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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.account.UserSetting;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.service.document.DocumentConfigService;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.service.sheet.SheetCellService;
import com.cubedrive.sheet.service.sheet.SheetImportService;
import com.cubedrive.sheet.service.sheet.SheetService;
import com.cubedrive.sheet.service.sheet.SheetTabElementService;
import com.cubedrive.sheet.service.sheet.SheetTabService;
import com.cubedrive.sheet.web.BaseSheetController;

@Controller
@RequestMapping(value = "/sheetapi")
public class SheetAPIController extends BaseSheetController {
	
	private final static Logger LOG = LoggerFactory.getLogger(SheetAPIController.class);
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private DocumentFileService documentFileService;
	
	@Autowired
	private SheetService sheetService;
	
	@Autowired
	private SheetTabService sheetTabService;
	
	@Autowired
	private SheetCellService sheetCellService;
	
	@Autowired
	private SheetTabElementService sheetTabElementService;
	
	@Autowired
	private DocumentConfigService documentConfigService;

    @Autowired
	private SheetImportService sheetImportService;
    
    
	/**
	 * This is for API example ...
	 * 
	 * @param editFileId
	 * @param model
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@RequestParam(required = false) String editFileId,
    		Model model) throws Exception {	
		
    	User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		
		String lang = BaseAppConstants.USER_SETTING_LANG_EN;
		String theme = BaseAppConstants.USER_SETTING_THEME_BLUE;
		UserSetting userSetting = loginUser.getSetting();
		if (userSetting != null) {
			lang = userSetting.getLang().name();
			theme = userSetting.getTheme().name();
		}
		
		String _editFileId = null;
		DocumentFile document = null;
		if (editFileId != null) {
			_editFileId = editFileId;
			document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(_editFileId)));
		} 
		
		model.addAttribute("editFileId", _editFileId);
		model.addAttribute("lang", lang);
		
        return "sheetapi/main";
    }
    
    /**
	 * this is the action to save the input json data and create a new file now
	 * We need replace the existing sheetId with the latest id ...
	 * 
	 * @param fileId
	 * @param request
	 * @param response
     * @throws Exception 
	 */
	@RequestMapping(value = "saveJsonFile")
	public void saveJsonFile(
			@RequestParam(required = true) String json,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

		// parse the json string and create a file now ...
        Map<String, Object> jsonObj = JsonUtil.getJsonObj(json);
        DocumentFile documentFile = null;
        
        // if oldFileId exist, this means we will replace the existing file ...OK
        // need delete it first and replace it with the data in the Json ...
        if (jsonObj.get("oldFileId") != null) {
            String encryptedId = jsonObj.get("oldFileId").toString();
            documentFile = documentFileService.getById(Integer.parseInt(fileDesEncrypter.doDecryptUrl(encryptedId)));
            sheetService.deleteAllTabs(documentFile);
            documentFileService.updateDoc2Now(documentFile);
        } else {
            documentFile = this.createDocumentFile(loginUser, 
            		(String) jsonObj.get("name"),
            		jsonObj.get("stared"),  
            		jsonObj.get("exname"));
        }

        // parse a list of tab now
        Map<String, String> sheetIdMap = new HashMap<String, String>();
        Map<String, SheetTab> sheetIdTabMap = new HashMap<String, SheetTab>();
        
        List<Object> sheets = (List<Object>)jsonObj.get("sheets");
        for (int i = 0; i < sheets.size(); i++) {
        	Map<String, Object> sheetTab = (Map<String, Object>) sheets.get(i);
            String tabName = sheetTab.get("name").toString();
            
            // check whether it is active
            boolean isActive = false;
            if (sheetTab.get("actived") != null && (boolean)sheetTab.get("actived")) isActive = true;
            
            // check for color ...
            String color = null;
            if (sheetTab.get("color") != null) color = sheetTab.get("color").toString();

            // Create spreadsheet tab now
            SheetTab thisTab = sheetTabService.insert(documentFile, tabName, i+1, isActive, color);
            sheetIdMap.put(sheetTab.get("id").toString(), thisTab.getId().toString());
            sheetIdTabMap.put(sheetTab.get("id").toString(), thisTab);
        }
        
        // ok, parse a list of cells now 
        // TODO, maybe we need better let UI call updateBatchCells, this will reduce backend work
        // And also improve performance
        Integer preTabId = null;
        List<SheetCell> listBatchObjs = new ArrayList<SheetCell>();
        List<Object> cells = (List<Object>)jsonObj.get("cells");
        for (int i = 0; i < cells.size(); i++) {
        	Map<String, Object> cellMaps = (Map<String, Object>) cells.get(i);
            String origSheetId = cellMaps.get("i").toString(); // this is for sheetId ...
            SheetTab tab = sheetIdTabMap.get(origSheetId);
            
            // some data is not valid ...
            if (tab == null) continue;
            
			// ok, we need do some check before process more ...
			if (listBatchObjs.size() > 300 || (preTabId != null && (!preTabId.equals(tab.getId())))) {
				sheetCellService.insertBatchSetCells(listBatchObjs, tab);
				listBatchObjs.clear();
			}
			
			listBatchObjs.add(this.getCell(cellMaps, tab));			
			preTabId = tab.getId();
        }
        
        if (listBatchObjs.size() > 0) {
        	SheetTab tab = sheetTabService.load(preTabId);
        	sheetCellService.insertBatchSetCells(listBatchObjs, tab);
        }
        
        // add config floating ...NEED change tabID / sheetId too ...
        List<Object> floatings = (List<Object>)jsonObj.get("floatings");
        for (int i = 0; i < floatings.size(); i++) {
        	Map<String, Object> floatMaps = (Map<String, Object>) floatings.get(i);
        	String origSheetId = floatMaps.get("sheet").toString();
            SheetTab tab = sheetIdTabMap.get(origSheetId);
        	sheetTabElementService.create(floatMaps, tab, origSheetId);
        }   
        
        // add file config
        List<Object> fileConfigs = (List<Object>)jsonObj.get("fileConfig");
        for (int i = 0; i < fileConfigs.size(); i++) {
        	Map<String,Object> configObj = (Map<String, Object>) fileConfigs.get(i);
			documentConfigService.create(configObj, documentFile);	
        }
        
        Map<String, Object> results = new HashMap<>();
        results.put("fileId", fileDesEncrypter.doEncryptUrl(documentFile.getId().toString()));
		results.put("sheetIdMap", sheetIdMap);
		results.put("success", true);

		outputJson(results, request, response);
	}
	
	private DocumentFile createDocumentFile(User author, String name,
			Object stared, Object exname) throws Exception {
		DocumentFile file = new DocumentFile(author);
		file.setCreateDate(new Date());
		file.setUpdateDate(new Date());
		file.setName(name);
		
		if (stared != null && (boolean)stared) file.setStar(true);
		
		if (exname != null && exname.toString().equalsIgnoreCase(BaseAppConstants.SHEET_TEMPLATE_EXNAME)) 
			file.setExname(BaseAppConstants.SHEET_TEMPLATE_EXNAME);
		else 
			file.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
		
		documentFileService.createDocumentFile(file	);
		
		return file;
	}
	
	private SheetCell getCell(Map<String,Object> jsonObj, SheetTab tab) {
    	int x = new Integer(jsonObj.get("x").toString());
    	int y = new Integer(jsonObj.get("y").toString());
    	String cellTable = tab.getCellTable();
    	int tabId = tab.getId();
    	String content = (String)jsonObj.get("j");
    	boolean cal = false;
    	if (jsonObj.get("c") != null) cal = (boolean)jsonObj.get("c");
    	
    	SheetCell cell = new SheetCell(tabId, x, y, content, null, cal);
    	
    	return cell;
    }

}
