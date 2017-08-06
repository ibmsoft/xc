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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetDropdown;
import com.cubedrive.sheet.service.sheet.SheetDropdownService;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/sheetDropdown")
public class SheetDropdownController extends BaseSheetController {


    @Autowired
    private SheetDropdownService sheetDropdownService;

    @Autowired
    private DocumentFileService documentFileService;
    
    /**
     * This will get a list of dropdown for this document ...
     * 
     * http://localhost:8080/sheet/sheetDropdown/list?documentId=wZ*xzpVsico_
     * 
     * @param documentId
     * @param request
     * @param response
     */
    @RequestMapping(value = "list")
    public void list(@RequestParam(required = true) String documentId,
                       HttpServletRequest request, HttpServletResponse response) {
    	User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		
		DocumentFile documentFile = documentFileService.getById(Integer.parseInt(fileDesEncrypter.doDecryptUrl(documentId)));
		List<SheetDropdownObj> dropDownObjs = sheetDropdownService.findByDocId(documentFile.getId());
		
		Map<String, Object> results = Maps.newHashMap();
		results.put("totalCount", dropDownObjs.size());
		results.put("results", dropDownObjs);
		results.put("metaData", SheetDropdownObj.createMetaData());
		outputJson(results, request, response);
    }
    
    /**
     * http://localhost:8080/sheet/sheetDropdown/create?documentId=wZ*xzpVsico_&name=months&data=[%22Male%22,%22Female%22]
     * 
     * @param data
     * @param name
     * @param documentId
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "createUpdate")
	public void createUpdate(@RequestParam(required = true) String data,
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String documentId,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		
		DocumentFile documentFile = documentFileService.getById(Integer.parseInt(fileDesEncrypter.doDecryptUrl(documentId)));
		
		SheetDropdown dropDown = new SheetDropdown();
		dropDown.setDocumentFile(documentFile);
		dropDown.setName(name);
		dropDown.setContent(data);
		
		sheetDropdownService.insert(dropDown);

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);

		outputJson(results, request, response);
	}
    
    @RequestMapping(value = "load")
	public void load(
			@RequestParam(required = true) String id,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		
		Map<String, Object> results = Maps.newHashMap();
		try {
		    SheetDropdown dropdown = sheetDropdownService.getById(new Integer(id));
		    results.put("success", true);
			results.put("object", new SheetDropdownObj(dropdown));
		} catch (Exception e) {
			results.put("success", false);
		}

		outputJson(results, request, response);
    }
    
    @RequestMapping(value = "delete")
	public void delete(
			@RequestParam(required = true) String id,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		
		SheetDropdown dropdown = sheetDropdownService.getById(new Integer(id));
		sheetDropdownService.delete(dropdown);
		
		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);

		outputJson(results, request, response);
    }
    
    /**
     * Example:
     * 
     *     http://localhost:8080/cubedrive/sheetDropdown/insert?documentId=g8sI3hfiSg8_&dropJsons={'id':"gender_store",'data':["Male","Female"]}
     * 
     *    UI need pass, a JSON string include bunch of information
     *    documentId:sDkK0Y2SEOU_
     *    dropJsons:[{"id":"gender_store","data":["Male","Female"]},{"id":"country_store","data":["USA","Germany","Canada"]},{"id":"age_store","data":["0-20","20-40",">40"]}]
     *    
     *    http://localhost:8080/sheet/sheetDropdown/insertListItem?documentId=wZ*xzpVsico_&dropJsons=[{%22id%22:%22gender_store%22,%22data%22:[%22Male%22,%22Female%22]},{%22id%22:%22country_store%22,%22data%22:[%22USA%22,%22Germany%22,%22Canada%22]},{%22id%22:%22age_store%22,%22data%22:[%220-20%22,%2220-40%22,%22%3E40%22]}]
     *    
     *    params : {
     *          dropJsons: dropJsons,
     *          documentId: documentId
     *    }
     *    
     * @param documentId
     * @param dropJsons
     * @param request
     * @param response
     */
    @RequestMapping(value = "insertListItem")
    public void insertListItem(@RequestParam(required = true) String documentId,
    		           @RequestParam(required = true) String dropJsons,
                       HttpServletRequest request, HttpServletResponse response) {

    	User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

        DocumentFile documentFile = documentFileService.getById(Integer.parseInt(fileDesEncrypter.doDecryptUrl(documentId)));
        
        List<Object> items = JsonUtil.parseArray(dropJsons);
        for (int i=0; i<items.size(); i++) {
        	Map<String,Object> jsonObj = (Map<String,Object>)items.get(i);
        	String name = (String) jsonObj.get("id");
			String content = JsonUtil.toJson(jsonObj.get("data"));   			
			SheetDropdown sheetDropdown = new SheetDropdown(documentFile, name, content);
			sheetDropdownService.insert(sheetDropdown); 
        }

        Map<String, Object> results = Maps.newHashMap();
        results.put("success", "true");
        results.put("info", "Changes Saved");
        outputJson(results, request, response);
    }
    
    /**
     * This is for example only ...
     * 
     * @param query
     * @param request
     * @param response
     */
    @RequestMapping(value = "remoteExample")
    public void remoteExample(@RequestParam(required = false) String query,
                       HttpServletRequest request, HttpServletResponse response) {
        
        Map<String, Object> results = Maps.newHashMap();
        List<Object> listBatchObjs = new ArrayList<Object>();
    
        Map<String, Object> obj1 = Maps.newHashMap();
    
        if(null != query){
            query = query.toLowerCase();
        }
    
        String apple = "apple";
        obj1.put("value", apple);
        if(null == query){
            listBatchObjs.add(obj1);
        }else if(0 == apple.indexOf(query)){
            listBatchObjs.add(obj1);
        }
        
        String pear = "pear";
        obj1 = Maps.newHashMap();
        obj1.put("value", pear);
    
        if(null == query){
            listBatchObjs.add(obj1);
        }else if(0 == pear.indexOf(query)){
            listBatchObjs.add(obj1);
        }
        
        String orange = "orange";
        obj1 = Maps.newHashMap();
        obj1.put("value", orange);
        if(null == query){
            listBatchObjs.add(obj1);
        }else if(0 == orange.indexOf(query)){
            listBatchObjs.add(obj1);
        }
        
        String mango = "mango";
        obj1 = Maps.newHashMap();
        obj1.put("value", mango);
        if(null == query){
            listBatchObjs.add(obj1);
        }else if(0 == mango.indexOf(query)){
            listBatchObjs.add(obj1);
        }
    
        results.put("success", "true");
        results.put("results", listBatchObjs);
        
        outputJson(results, request, response);
    }
    
    /**
     * This is for example only ...
     * 
     * @param query
     * @param request
     * @param response
     */
    @RequestMapping(value = "remoteExampleJsonp")
    public void remoteExampleJsonp(@RequestParam(required = false) String query,
    		@RequestParam(required = false) String callback,
                       HttpServletRequest request, HttpServletResponse response) {
        
        Map<String, Object> results = Maps.newHashMap();
        List<Object> listBatchObjs = new ArrayList<Object>();
    
        Map<String, Object> obj1 = Maps.newHashMap();
    
        if(null != query){
            query = query.toLowerCase();
        }
    
        String apple = "apple";
        obj1.put("value", apple);
        if(null == query){
            listBatchObjs.add(obj1);
        }else if(0 == apple.indexOf(query)){
            listBatchObjs.add(obj1);
        }
        
        String pear = "pear";
        obj1 = Maps.newHashMap();
        obj1.put("value", pear);
    
        if(null == query){
            listBatchObjs.add(obj1);
        }else if(0 == pear.indexOf(query)){
            listBatchObjs.add(obj1);
        }
        
        String orange = "orange";
        obj1 = Maps.newHashMap();
        obj1.put("value", orange);
        if(null == query){
            listBatchObjs.add(obj1);
        }else if(0 == orange.indexOf(query)){
            listBatchObjs.add(obj1);
        }
        
        String mango = "mango";
        obj1 = Maps.newHashMap();
        obj1.put("value", mango);
        if(null == query){
            listBatchObjs.add(obj1);
        }else if(0 == mango.indexOf(query)){
            listBatchObjs.add(obj1);
        }
    
        results.put("success", "true");
        results.put("results", listBatchObjs);
        
        outputJSJsonP(callback, results, request, response);
    }


}