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
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.domain.account.User;
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
import com.cubedrive.sheet.service.sheet.SheetService;
import com.cubedrive.sheet.service.sheet.SheetTabElementService;
import com.cubedrive.sheet.service.sheet.SheetTabService;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/sheetCell")
public class SheetCellController extends BaseSheetController {

	private static final Logger LOG =LoggerFactory.getLogger(SheetCellController.class);
	
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

	@Autowired
	private SheetCellService sheetCellService;

	@Autowired
	private DocumentConfigService documentConfigService;

	/**
	 * UI will pass a list of action to process ...
	 * 
	 * @param actions
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "updateBatchCells")
	public void updateBatchCells(@RequestParam(required = true) String actions,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		User loginUser = this.getLoginUser();

		List<Object> items = JsonUtil.parseArray(actions);

		SheetTab tab = null;
		List<SheetCell> listBatchObjs = new ArrayList<SheetCell>();

		// map tablD and really id
		Map<String, SheetTab> tabIdMaps = Maps.newHashMap();
		int tabId = 0;

		for (int j = 0, size = items.size(); j < size; j++) {
			Map<String, Object> jsonObj = (Map<String, Object>) items.get(j);

			// first get the tabId ...
			SheetTab thisTab = null;
			if (jsonObj.get("sheetId") != null) {
				if (tabIdMaps.containsKey(jsonObj.get("sheetId"))) {
					thisTab = tabIdMaps.get((String) jsonObj.get("sheetId"));
				} else {
					Integer newtabId = new Integer(jsonObj.get("sheetId").toString());
					thisTab = sheetTabService.load(newtabId);
					if (thisTab != null) tabIdMaps.put(jsonObj.get("sheetId").toString(), thisTab);
				}

				// if this is from different tab, submit existing list and clean
				if (tab != null && thisTab != null
						&& !thisTab.getId().equals(tab.getId())) {
					if (listBatchObjs != null && listBatchObjs.size() > 0) {
						sheetCellService.insertBatchSetCells(listBatchObjs, tab);
						listBatchObjs.clear();
					}
				}
				tab = thisTab;
			}

			// ok, start to call service to process action - first we need check
			// by multiple cells ...
			String action = (String) jsonObj.get("action");
			if (action.equalsIgnoreCase("setCell")) { // this is a special case, need batch insert
				if (thisTab != null)
					listBatchObjs.add(this.getCell(jsonObj, thisTab));
				else {
					LOG.error("Pass data error - not tabId, please investigate: " + jsonObj.toString());
				}
			}else {
				// first submit the list object ...
				if (listBatchObjs.size() > 0) {
					sheetCellService.insertBatchSetCells(listBatchObjs, tab);
					listBatchObjs.clear();
				}

				// deal currently action
				this.handleCell(jsonObj, tab, loginUser);
			}

			// ok, we need do some check before process more ...
			if (listBatchObjs.size() > 300) {
				sheetCellService.insertBatchSetCells(listBatchObjs, tab);
				listBatchObjs.clear();
			}
		}

		// insert last one ...
		if (listBatchObjs.size() > 0)
			sheetCellService.insertBatchSetCells(listBatchObjs, tab);

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);
		results.put("info", messageSource.getMessage("general.changesSaved",
				null, getUserLocale()));
		outputJson(results, request, response);
	}

	// ////////////////////////////////// private function
	// ===========================
	// this is only for one cell ...
	// ///////////////////////////////////////////////////////////////////////////////
	private void handleCell(Map<String, Object> jsonObj, SheetTab sheetTab,
			User user) {

		DesEncrypter fileDesEncrypter = DecryptEncryptUtil
				.getDesEncrypterForFile(user.getUsername());
		String action = (String) jsonObj.get("action");

		if (action.equalsIgnoreCase("setActivedSheet")) {
			Integer tabId = new Integer(jsonObj.get("activedSheetId")
					.toString());
			SheetTab tab = sheetTabService.load(tabId);
			sheetTabService.activeTab(tab);

		} else if (action.equalsIgnoreCase("removeRow")) {
			Integer minRow = new Integer(jsonObj.get("minrow").toString());
			Integer maxrow = new Integer(jsonObj.get("maxrow").toString());
			sheetCellService.deleteRows(sheetTab.getId(), minRow, maxrow,
					sheetTab.getCellTable());

		} else if (action.equalsIgnoreCase("removeColumn")) {
			Integer mincol = new Integer(jsonObj.get("mincol").toString());
			Integer maxcol = new Integer(jsonObj.get("maxcol").toString());
			sheetCellService.deleteCols(sheetTab.getId(), mincol, maxcol,
					sheetTab.getCellTable());

		} else if (action.equalsIgnoreCase("removeCell")) {
			Integer minRow = new Integer(jsonObj.get("minrow").toString());
			Integer maxrow = new Integer(jsonObj.get("maxrow").toString());
			Integer mincol = new Integer(jsonObj.get("mincol").toString());
			Integer maxcol = new Integer(jsonObj.get("maxcol").toString());
			String moveDirection = jsonObj.get("moveDir").toString();

			sheetCellService.deleteCells(sheetTab.getId(), minRow, maxrow,
					mincol, maxcol, moveDirection, sheetTab.getCellTable());

		} else if (action.equalsIgnoreCase("insertRow")) {
			Integer row = new Integer(jsonObj.get("row").toString());
			Integer rowSpan = new Integer(jsonObj.get("rowSpan").toString());
			sheetCellService.insertRows(sheetTab.getId(), row, rowSpan,
					sheetTab.getCellTable());

		} else if (action.equalsIgnoreCase("insertColumn")) {
			Integer col = new Integer(jsonObj.get("col").toString());
			Integer colSpan = new Integer(jsonObj.get("colSpan").toString());
			sheetCellService.insertCols(sheetTab.getId(), col, colSpan,
					sheetTab.getCellTable());

		} else if (action.equalsIgnoreCase("insertCell")) {
			Integer row = new Integer(jsonObj.get("row").toString());
			Integer rowSpan = new Integer(jsonObj.get("rowSpan").toString());
			Integer col = new Integer(jsonObj.get("col").toString());
			Integer colSpan = new Integer(jsonObj.get("colSpan").toString());
			String moveDirection = jsonObj.get("moveDir").toString();
			sheetCellService.insertCells(sheetTab.getId(), row, rowSpan, col,
					colSpan, moveDirection, sheetTab.getCellTable());

		} else if (action.equalsIgnoreCase("sortSpan")) {
			// sort span ....
			sheetCellService.sortSpan(jsonObj);

		} else if (action.equalsIgnoreCase("moveRows")) {
			sheetCellService.moveRows(jsonObj, sheetTab);

		} else if (action.equalsIgnoreCase("createFloatingItem")) {
			sheetTabElementService.create(jsonObj, sheetTab);

		} else if (action.equalsIgnoreCase("updateFloatingItem")) {
			sheetTabElementService.update(jsonObj, sheetTab);

		} else if (action.equalsIgnoreCase("removeFloatingItem")) {
			sheetTabElementService.remove(jsonObj, sheetTab);

		} else if (action.equalsIgnoreCase("updateHiddens")) {
			sheetTabElementService.createUpdate(jsonObj, sheetTab);

		} else if (action.equalsIgnoreCase("createFileConfig")) {
			String fileIdStr = fileDesEncrypter.doDecryptUrl(jsonObj.get(
					"fileId").toString());
			Integer fileId = new Integer(fileIdStr);
			DocumentFile file = documentFileService.getById(fileId);
			documentConfigService.create(jsonObj, file);

		} else if (action.equalsIgnoreCase("updateFileConfig")) {
			String fileIdStr = fileDesEncrypter.doDecryptUrl(jsonObj.get(
					"fileId").toString());
			Integer fileId = new Integer(fileIdStr);
			DocumentFile file = documentFileService.getById(fileId);
			documentConfigService.update(jsonObj, file);

		} else if (action.equalsIgnoreCase("removeFileConfig")) {
			String fileIdStr = fileDesEncrypter.doDecryptUrl(jsonObj.get(
					"fileId").toString());
			Integer fileId = new Integer(fileIdStr);
			DocumentFile file = documentFileService.getById(fileId);
			documentConfigService.remove(jsonObj, file);

		}
	}

	// ==============================================================
	private SheetCell getCell(Map<String, Object> jsonObj, SheetTab tab) {
		int x = new Integer(jsonObj.get("row").toString());
		int y = new Integer(jsonObj.get("col").toString());
		String cellTable = tab.getCellTable();
		int tabId = tab.getId();
		String content = (String) jsonObj.get("json");
		String data = String.valueOf(jsonObj.get("data"));
		boolean cal = (boolean) jsonObj.get("cal");

		SheetCell cell = new SheetCell(tabId, x, y, content, data, cal);

		return cell;
	}

}
