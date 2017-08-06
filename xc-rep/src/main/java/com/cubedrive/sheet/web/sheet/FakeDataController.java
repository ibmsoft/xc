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
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/fakeData")
public class FakeDataController extends BaseSheetController {
	
    /**
     * This is for example to have a list of dropdown item ...
     * 
     * @param query
     * @param request
     * @param response
     */
    @RequestMapping(value = "dropdownList")
    public void dropdownList(@RequestParam(required = false) String query,
               HttpServletRequest request, HttpServletResponse response) {
        
        Map<String, Object> results = Maps.newHashMap();
        List<Object> listBatchObjs = new ArrayList<Object>();
    
        Map<String, Object> obj1 = Maps.newHashMap();
        if(null != query) query = query.toLowerCase();
    
        String hrDept = "HR Dept";
        obj1.put("value", hrDept);
        obj1.put("id", 1);
        if(null == query || hrDept.toLowerCase().indexOf(query)>=0 ) listBatchObjs.add(obj1);
        
        String softDept = "Software Dept";
        obj1 = Maps.newHashMap();
        obj1.put("value", softDept);
        obj1.put("id", 2);
        if(null == query || softDept.toLowerCase().indexOf(query)>=0 ) listBatchObjs.add(obj1);
        
        String qaDept = "QA Dept";
        obj1 = Maps.newHashMap();
        obj1.put("value", qaDept);
        obj1.put("id", 3);
        if(null == query || qaDept.toLowerCase().indexOf(query)>=0 ) listBatchObjs.add(obj1);
        
        String saleDept = "Sale Dept";
        obj1 = Maps.newHashMap();
        obj1.put("value", saleDept);
        obj1.put("id", 4);
        if(null == query || saleDept.toLowerCase().indexOf(query)>=0 ) listBatchObjs.add(obj1);
    
        results.put("success", "true");
        results.put("results", listBatchObjs);
        
        outputJson(results, request, response);
    }
	
}
