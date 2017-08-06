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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.sheet.domain.sheet.CellTouchObj;
import com.cubedrive.sheet.domain.sheet.SheetTabElementObj;
import com.cubedrive.sheet.domain.sheet.SheetTabObj;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/touch")
public class TouchController extends BaseSheetController {


	@Autowired
	private DocumentFileService documentFileService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(@RequestParam(required = false) String editFileId,
			@RequestParam(required = false) String fileId,
			Model model) {
		
		// check to see whether user existing ... if not. force login
		model.addAttribute("lang", userService.currentUserLang());
		return "touch/main";
	}
	
	@RequestMapping(value = "load")
	public void load(
			@RequestParam(defaultValue = "0") Integer start,
			@RequestParam(defaultValue = "0") Integer sheetId,
			@RequestParam(defaultValue = "10") Integer limit,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		Map<String, Object> results = Maps.newHashMap();
		
		// this is fist time to load cell information ...
		if (sheetId == 0 && start == 0) { 		
			List<SheetTabObj> tabs = new ArrayList<SheetTabObj>();
			tabs.add(new SheetTabObj("First Tab", 10, true));
			tabs.add(new SheetTabObj("Second Tab", 20, false));
			
			List<SheetTabElementObj> floatings = new ArrayList<SheetTabElementObj>();
			floatings.add(new SheetTabElementObj(10, "[2,2,2,3]"));
			
			// ok, now we need load data ...
			Map<String, Object> messages = Maps.newHashMap();
			messages.put("sheets", tabs);
			messages.put("floatings", floatings);
			messages.put("fileName", "TEST");
			
			// add cells 
			List<CellTouchObj> cellObjs = new ArrayList<CellTouchObj>();
			cellObjs.add(new CellTouchObj(2,2,10, createCellJson("data", "Merged cells", null, null)));
			cellObjs.add(new CellTouchObj(3,2,10, createCellJson("data", "Bold", "fw", "bold")));
			cellObjs.add(new CellTouchObj(4,2,10, createCellJson("data", "Text Color", "color", "red")));
			cellObjs.add(new CellTouchObj(5,2,10, createCellJson("data", "BGC", "bgc", "cyan")));
			cellObjs.add(new CellTouchObj(6,2,10, createCellJson("data", "Size", "fz", "18")));
			cellObjs.add(new CellTouchObj(0,1,10, createCellJson("width", "250", null, null)));
			
			cellObjs.add(new CellTouchObj(8,2,10, createCellJson("data", "1", null, null)));
			cellObjs.add(new CellTouchObj(9,2,10, createCellJson("data", "2", null, null)));
			cellObjs.add(new CellTouchObj(10,2,10, createCellJson("data", "3", null, null)));
//			cellObjs.add(new CellTouchObj(11,2,10, createCellJson("data", "=sum(1,2)", "cal", "true")));

			results.put("success", true);
			results.put("message", messages);
			results.put("results", cellObjs);
			
			outputJson(results, request, response);
			return;
		} 
		
		// if query for tab 2 ...
		if (sheetId == 20 && start == 0) {
			List<CellTouchObj> cellObjs = new ArrayList<CellTouchObj>();
			cellObjs.add(new CellTouchObj(2,2,20, createCellJson("data", "Italic", "fs", "italic")));
			cellObjs.add(new CellTouchObj(3,2,20, createCellJson("data", "Underline", "u", "underline")));
			cellObjs.add(new CellTouchObj(4,2,20, createCellJson("data", "Strike", "s", "line-through")));
			cellObjs.add(new CellTouchObj(5,2,20, createCellJson("data", "Courier New", "ff", "Courier New")));
			results.put("results", cellObjs);		
		}
		
		results.put("success", true);
		outputJson(results, request, response);
	} 
	
	// private create cell object
	private Map<String, Object> createCellJson(String format1, String data1, String format2, String data2) {
		
		Map<String, Object> dataObj = Maps.newHashMap();
		dataObj.put(format1, data1);
		
		if (format2 != null) {
			if (format2.equalsIgnoreCase("cal")) {
				dataObj.put(format2, true);
				dataObj.put("arg", "sum(1,2)");
			}
			else dataObj.put(format2, data2);
		}
		
		return dataObj;
	}

}
