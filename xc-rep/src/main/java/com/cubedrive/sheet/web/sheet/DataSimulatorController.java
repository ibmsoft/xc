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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/dataSimulator")
public class DataSimulatorController extends BaseSheetController {
	
	@RequestMapping(value = "listDropdown")
    public void listDropdown(HttpServletRequest request, HttpServletResponse response) {

		List<SheetDropdownObj> dropDownObjs = new ArrayList<SheetDropdownObj>();
		
		// enter some simulate data
		SheetDropdownObj obj1 = new SheetDropdownObj();
		obj1.setId("1");
		obj1.setName("Test");
		obj1.setData("This is Test");
		dropDownObjs.add(obj1);
		
		Map<String, Object> results = Maps.newHashMap();
		results.put("totalCount", dropDownObjs.size());
		results.put("results", dropDownObjs);
		results.put("metaData", SheetDropdownObj.createMetaData());
		outputJson(results, request, response);
    }

}
