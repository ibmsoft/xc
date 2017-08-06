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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cubedrive.base.BaseAppConfiguration;
import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.account.UserSetting;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.exception.MoreInfoError;
import com.cubedrive.base.persistence.SQLQueryResult;
import com.cubedrive.base.plugin.DocumentConvertPlugins;
import com.cubedrive.base.utils.BaseFileUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.base.utils.MimeTypes;
import com.cubedrive.base.utils.MyBatisOgnlUtil;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetCellObj;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElementObj;
import com.cubedrive.sheet.domain.sheet.SheetTabSpan;
import com.cubedrive.sheet.service.sheet.SheetCellService.SheetCellRangeCondition;
import com.cubedrive.sheet.service.sheet.SheetDropdownService;
import com.cubedrive.sheet.service.sheet.SheetExportService;
import com.cubedrive.sheet.service.sheet.SheetImportService;
import com.cubedrive.sheet.utils.SheetFileUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@Controller
@RequestMapping(value = "/sheet")
public class SheetController extends SheetOperationSupport {

	@Autowired
    private SheetDropdownService sheetDropdownService;
	
	@Autowired
	private SheetImportService sheetImportService;

    @Autowired
    private SheetExportService sheetExportService;

	/**
	 * Retrieve couple of parameters and pass them to main page
	 * to load spreadsheet.
	 * 
	 * If user does not pass the editFileId, a new sheet document 
	 * will be generated with 3 default tab.
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
		String lang = BaseAppConstants.USER_SETTING_LANG_EN;
		String theme = BaseAppConstants.USER_SETTING_THEME_BLUE;
		UserSetting userSetting = loginUser.getSetting();
		if (userSetting != null) {
			lang = userSetting.getLang().name();
			theme = userSetting.getTheme().name();
		}

		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

		String _editFileId = null;
		DocumentFile document = null;
		if (editFileId != null) {
			_editFileId = editFileId;
			document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(_editFileId)));
			
		} else { // ok, if editFileId is empty, we will create a new one ...
			document = createDefaultDocumentFile(loginUser);
			documentFileService.createDocumentFile(document);
			sheetService.createDefaultSS(document);

			_editFileId = fileDesEncrypter.doEncryptUrl(document.getId()
					.toString());
		}
		
		model.addAttribute("editFileId", _editFileId);
		model.addAttribute("lang", lang);

		return "sheet/main";
	}
	
	
    @RequestMapping(value = "createSheet", method=RequestMethod.POST)
	public void createSheet(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

		User loginUser = this.getLoginUser();

        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

		// ok, we need load cells json based on file id ...
		DocumentFile document = createDefaultDocumentFile(loginUser);
		documentFileService.createDocumentFile(document);
		sheetService.createDefaultSS(document);

        Map<String, Object> results = new HashMap<>();
		results.put("success", true);
		results.put("id", fileDesEncrypter.doEncryptUrl(document.getId().toString()));

		outputJson(results, request, response);
	}
    
    @RequestMapping(value="dbTableAsSheet")
    public void dbTableAsSheet(@RequestParam String tableName, HttpServletRequest request, HttpServletResponse response)throws Exception{
        User loginUser = this.getLoginUser();
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
        SQLQueryResult<DocumentFile> documents =  documentFileService.getListDocuments(null, null, null, null, tableName, null, Lists.newArrayList(BaseAppConstants.SHEET_DB_TABLE_EXNAME));
        DocumentFile documentFile;
        if(documents.totalRecords == 0){
            documentFile = sheetDbTableService.createDocumentByDbTable(tableName, loginUser);
            if(documentFile == null){
                throw new RuntimeException("No such database table:"+tableName);
            }
        }else{
            documentFile = documents.data.get(0);
        }
        
        Map<String, Object> results = new HashMap<>();
        results.put("fileId", fileDesEncrypter.doEncryptUrl(documentFile.getId().toString()));
        results.put("success", true);
        outputJson(results, request, response);
    }
	

	/**
	 * this is the first action which need to be loaded during open 
	 * sheet file. 
	 * 
	 * @param fileId
	 * @param startCellId
	 * @param size - used to define how many cell need to be returned
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "loadActivedSheetOfFile")
	public void loadActivedSheetOfFile(@RequestParam String fileId,
			@RequestParam(defaultValue = "0") Integer startCellId,@RequestParam Integer size, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {

	    Map<String,Object> results = doLoadActivedSheetOfFile(fileId, startCellId, size, false, false, false, cell2CellObjFunc);
		outputJson(results, request, response);
	}
	
	@RequestMapping(value = "loadActivedSheetOfFile2")
    public void loadActivedSheetOfFile2(@RequestParam String fileId,
            @RequestParam(defaultValue = "0") Integer startCellId, @RequestParam Integer size, 
            HttpServletRequest request, HttpServletResponse response) throws Exception {
	    Map<String,Object> results = doLoadActivedSheetOfFile(fileId, startCellId, size, true, true, true, cell2ArrayFunc);
        outputJson(results, request, response);
    }
	
	@RequestMapping(value = "loadActivedSheetOfFile3")
    public void loadActivedSheetOfFile3(@RequestParam String fileId,
            @RequestParam(defaultValue = "0") Integer startCellId,
            @RequestParam Integer size, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Map<String,Object> results = doLoadActivedSheetOfFile(fileId, startCellId, size, true, true, false, cell2ArrayFunc);
        outputJson(results, request, response);
    }
    
    /**
     * just load the sheets and the 0,0 cell of all sheets
     * @param fileId
     * @param request
     * @param response
     */
    @RequestMapping(value = "loadSheetsOfFile")
    public void loadSheetsOfFile(@RequestParam String fileId,
        @RequestParam(defaultValue = "0") Integer startCellId, @RequestParam Integer size, 
        HttpServletRequest request,HttpServletResponse response) {

        Map<String, Object> results = doLoadSheetsOfFile(fileId, startCellId, size, false, cell2CellObjFunc);
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "loadSheetsOfFile2")
    public void loadSheetsOfFile2(@RequestParam String fileId,
        @RequestParam(defaultValue = "0") Integer startCellId, @RequestParam Integer size, 
        HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> results = doLoadSheetsOfFile(fileId, startCellId, size, true, cell2ArrayFunc);
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "loadSheetsOfFile3")
    public void loadSheetsOfFile3(@RequestParam String fileId,
        @RequestParam(defaultValue = "0") Integer startCellId, @RequestParam Integer size, 
        HttpServletRequest request,HttpServletResponse response) {
        Map<String, Object> results = doLoadSheetsOfFile(fileId, startCellId, size, false, cell2ArrayFunc);
        outputJson(results, request, response);
    }

	/**
     * just load the sheets and the 0,0 cell of all sheets
     * @param fileId
     * @param request
     * @param response
     */
    @RequestMapping(value = "loadSheetInfo")
    public void loadSheetInfo(
        @RequestParam String fileId,
        @RequestParam(defaultValue = "0") Integer startCellId, @RequestParam Integer size,
        HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        Map<String,Object> results = doLoadSheetInfo(fileId, startCellId, size, cell2ArrayFunc);
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "loadSheetInfo2")
    public void loadSheetInfo2(
        @RequestParam String fileId,
        @RequestParam(defaultValue = "0") Integer startCellId, 
        @RequestParam Integer size,
        @RequestParam Integer throttleOfBigFile,
        HttpServletRequest request,
        HttpServletResponse response) throws Exception {

        Map<String,Object> results = doLoadSheetInfo2(fileId, startCellId, size, throttleOfBigFile, cell2ArrayFunc);
        outputJson(results, request, response);
    }

	/**
	 * this is the active to load a list of cells based on size
	 * 
	 * @param sheetId
	 * @param startCellId
	 * @param size
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "loadCellOnDemand")
	public void loadCellOnDemand(@RequestParam String fileId,
			@RequestParam Integer sheetId,
			@RequestParam(defaultValue = "0") Integer startCellId,
			@RequestParam Integer size,
			@RequestParam(defaultValue = "false") Boolean skipCal,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	     
	    Map<String,Object> results = doLoadCellOnDemand(fileId, sheetId, startCellId, size, skipCal, false, cell2CellObjFunc); 
		outputJson(results, request, response);
	}
	
	@RequestMapping(value = "loadCellOnDemand2")
    public void loadCellOnDemand2(@RequestParam String fileId,
            @RequestParam Integer sheetId,
            @RequestParam(defaultValue = "0") Integer startCellId,
            @RequestParam Integer size,
            @RequestParam(defaultValue = "false") Boolean skipCal,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

	    Map<String,Object> results = doLoadCellOnDemand(fileId, sheetId, startCellId, size, skipCal, true, cell2ArrayFunc); 
        outputJson(results, request, response);
    }
	
	@RequestMapping(value = "loadCellOnDemand3")
    public void loadCellOnDemand3(@RequestParam String fileId,
            @RequestParam Integer sheetId,
            @RequestParam(defaultValue = "0") Integer startCellId,
            @RequestParam Integer size,
            @RequestParam(defaultValue = "false") Boolean skipCal,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String,Object> results = doLoadCellOnDemand(fileId, sheetId, startCellId, size, skipCal, false, cell2ArrayFunc); 
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "loadCellOnDemand4")
    public void loadCellOnDemand4(@RequestParam String fileId,
            @RequestParam Integer sheetId,
            @RequestParam(defaultValue = "0") Integer startCellId,
            @RequestParam Integer size,
            @RequestParam(defaultValue = "false") Boolean skipCal,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Map<String,Object> results = doLoadCornerCalCellOnDemand(fileId, sheetId, startCellId, size, skipCal, cell2ArrayFunc);
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "loadCellOnDemand5")
    public void loadCellOnDemand5(@RequestParam String fileId,
            @RequestParam Integer sheetId,
            @RequestParam(defaultValue = "0") Integer startCellId,
            @RequestParam Integer size,
            @RequestParam(defaultValue = "false") Boolean skipCal,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Map<String,Object> results = doLoadDocCellOnDemand(fileId, sheetId, startCellId, size, cell2ArrayFunc);
        outputJson(results, request, response);
    }
	
	@RequestMapping(value = "loadElementOnDemand")
	public void loadElementOnDemand(@RequestParam String fileId,
			@RequestParam String sheetId,
			@RequestParam(defaultValue = "0") Integer startElementId,
			@RequestParam Integer size,
			HttpServletRequest request, HttpServletResponse response) {
		
		Integer tabId = Integer.parseInt(sheetId);
		List<SheetTabElementObj> tabElementObjs = sheetTabElementService.loadTabElementOnDemand(tabId, startElementId, size);
		Integer nextStartElementId = 0;
		if (tabElementObjs.size() == size)
			nextStartElementId = tabElementObjs.get(tabElementObjs.size() - 1).getId();

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);
		results.put("total", tabElementObjs.size());
		results.put("results", tabElementObjs);
		results.put("startElementId", nextStartElementId);
		outputJson(results, request, response);
		
	}
    
    /**
     * this is the active to load a list of cal cells based on size
     *
     * @param fileId
     * @param startCellId
     * @param size
     * @param request
     * @param response
     */
    @RequestMapping(value = "loadCalCellOnDemand")
    public void loadCalCellOnDemand(@RequestParam String fileId,
        @RequestParam(defaultValue = "0") Integer startCellId,
        @RequestParam Integer size,
        HttpServletRequest request, HttpServletResponse response) {

        User loginUser = this.getLoginUser();
        
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil
        .getDesEncrypterForFile(loginUser.getUsername());
        Integer documentId = new Integer(fileDesEncrypter.doDecryptUrl(fileId));
        
        List<SheetCell> cells = sheetCellService.getCalCells(documentId, startCellId, size);
        List<SheetCellObj> cellObjs = Lists.transform(cells, cell2CellObjFunc);
        
        // ok, we need check next start cellId
        Integer nextStartCellId = 0;
        if (cells.size() == size)
            nextStartCellId = cells.get(cells.size() - 1).getId();
        
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", cells.size());
        results.put("results", cellObjs);
        results.put("startCellId", nextStartCellId);
        outputJson(results, request, response);
    }

	/**
	 * this is action to load sheet data when I click sheet tab. This will
	 * return sheet information
	 * 
	 * @param sheetId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "loadSheet")
	public void loadSheet(@RequestParam(value="sheetId", required = true) Integer tabId,
            @RequestParam(defaultValue = "false") Boolean notActiveTabFlag,
			@RequestParam Integer size, HttpServletRequest request,
			HttpServletResponse response) {
	    
	    Map<String,Object> results = doLoadSheet(tabId, notActiveTabFlag, size, false, false, cell2CellObjFunc);
		outputJson(results, request, response);
	}
	
	@RequestMapping(value = "loadSheet2")
    public void loadSheet2(@RequestParam(value="sheetId", required = true) Integer tabId,
            @RequestParam(defaultValue = "false") Boolean notActiveTabFlag,
            @RequestParam Integer size, HttpServletRequest request,
            HttpServletResponse response) {

	    Map<String,Object> results = doLoadSheet(tabId, notActiveTabFlag, size, true, true, cell2ArrayFunc);
        outputJson(results, request, response);
    }
	
	@RequestMapping(value = "loadSheet3")
    public void loadSheet3(@RequestParam(value="sheetId", required = true) Integer tabId,
            @RequestParam(defaultValue = "false") Boolean notActiveTabFlag,
            @RequestParam Integer size, HttpServletRequest request,
            HttpServletResponse response) {

        Map<String,Object> results = doLoadSheet(tabId, notActiveTabFlag, size, true, false, cell2ArrayFunc);
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "loadSheet4")
    public void loadSheet4(@RequestParam(value="sheetId", required = true) Integer tabId,
            @RequestParam(defaultValue = "false") Boolean notActiveTabFlag,
            @RequestParam Integer size, HttpServletRequest request,
            HttpServletResponse response) {
        
        Map<String,Object> results = doLoadSheetRowCol(tabId, notActiveTabFlag, size, cell2ArrayFunc);
        outputJson(results, request, response);
    }

    @RequestMapping(value = "loadSheet5")
    public void loadSheet5(@RequestParam(value="sheetId", required = true) Integer tabId,
            @RequestParam(defaultValue = "false") Boolean notActiveTabFlag,
            @RequestParam Integer throttleOfBigFile,
            @RequestParam Integer size, HttpServletRequest request,
            HttpServletResponse response) {
        
    	SheetTab tab = sheetTabService.load(tabId);
    	SheetTabSpan tabSpan = sheetTabService.getTabSpan(tab);
    	
    	Integer totalCellNum = tabSpan.getTotalCellNum();
    	Map<String,Object> results;
    	if(totalCellNum <= throttleOfBigFile){
    		results = doLoadSheet(tabId, notActiveTabFlag, size, true, false, cell2ArrayFunc);
    		results.put("loadWay", "tab");
    	}else{
    		results = doLoadSheetRowCol(tabId, notActiveTabFlag, size, cell2ArrayFunc);
    		results.put("loadWay", "rowcol");
    	}    	
        outputJson(results, request, response);
    }
	/**
	 * load a list of cells information based on pass parameters
	 * 
	 * @param fileId
	 * @param cells
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "loadCells")
	public void loadCells(@RequestParam(value="cells",required = true) String cellParams,
			HttpServletRequest request, HttpServletResponse response) {
		// ok, at this moment we will hard code userId as 1 TODO ...
		List<SheetCell> cells = new LinkedList<>();
		
		List<Object> items = JsonUtil.parseArray(cellParams);
		for (int i = 0; i < items.size(); i++) {
			List<Object> temp = (List<Object>) items.get(i);
			Integer tabId = (Integer) temp.get(0);
			Integer row = (Integer) temp.get(1);
			Integer col = (Integer) temp.get(2);

			SheetTab tab = sheetTabService.load(new Integer(tabId));
			
			if (row.intValue()  == 0) {
				cells.addAll(sheetCellService.getCellsByCol(tabId, col, tab.getCellTable()));
			} else if (col.intValue()  == 0) {
				cells.addAll(sheetCellService.getCellsByRow(tabId, row, tab.getCellTable()));
			} else {
				SheetCell cell = sheetCellService.getCellByTabAndXY(tab.getId(), row, col, tab.getCellTable());
                if(null != cell){
                    cells.add(cell);
                }
			}
		}
		
		List<SheetCellObj> cellObjs = Lists.transform(cells, cell2CellObjFunc);
		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);
		results.put("total", cellObjs.size());
		results.put("results", cellObjs);

		outputJson(results, request, response);
	}
    
    /**
     * load a range of cells information based on pass parameters
     *
     * @param fileId
     * @param range
     * @param request
     * @param response
     */
    @RequestMapping(value = "loadRange")
    public void loadRange(@RequestParam String range,
                          HttpServletRequest request, HttpServletResponse response) {
        // ok, at this moment we will hard code userId as 1 TODO ...
        List<SheetCell> cells = new LinkedList<>();
        
        List<Object> items = JsonUtil.parseArray(range);
        for (int i = 0; i < items.size(); i++) {
            List<Object> temp = (List<Object>) items.get(i);
            
            Integer tabId = (Integer) temp.get(0);
            Integer startRow = (Integer) temp.get(1);
            Integer startCol = (Integer) temp.get(2);
            Integer endRow = (Integer) temp.get(3);
            Integer endCol = (Integer) temp.get(4);
            
            SheetTab tab = sheetTabService.load(new Integer(tabId));
            if(endRow.intValue() ==0 && endCol.intValue() ==0){
				cells.addAll(sheetCellService.getTabCells(tab));
			} else if (endRow.intValue() == 0) {
                cells.addAll(sheetCellService.getCellByColRange(tabId, startCol, endCol, tab.getCellTable()));
            } else if (endCol.intValue() == 0) {
                cells.addAll(sheetCellService.getCellByRowRange(tabId, startRow, endRow, tab.getCellTable()));
            } else {
                cells.addAll(sheetCellService.getCellByCellRange(tabId, startRow, endRow, startCol, endCol, tab.getCellTable()));
            }
        }
        
        List<SheetCellObj> cellObjs = Lists.transform(cells, cell2CellObjFunc);
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("total", cellObjs.size());
        results.put("results", cellObjs);
        
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "loadRange2")
    public void loadRange2(@RequestParam String range, 
            @RequestParam(defaultValue="0")Integer nextCellId,@RequestParam Integer limit, 
            HttpServletRequest request, HttpServletResponse response){
        
        Map<String,Object> results = doLoadRange(range, nextCellId, limit, true, cell2ArrayFunc);
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "loadRange3")
    public void loadRange3(@RequestParam String range, 
            @RequestParam(defaultValue="0")Integer nextCellId,@RequestParam Integer limit, 
            HttpServletRequest request, HttpServletResponse response){
        
        Map<String,Object> results = doLoadRange(range, nextCellId, limit, false, cell2ArrayFunc);
        outputJson(results, request, response);
    }
    
    
    @RequestMapping(value = "loadRangeStyle")
    public void loadRangeStyle(@RequestParam String range, 
            @RequestParam(defaultValue="0")Integer nextCellId,
            @RequestParam Integer limit, 
            HttpServletRequest request, HttpServletResponse response){
        
        Map<String, Object> results = Maps.newHashMap();
        List<Integer> rangeParams = JsonUtil.fromJson(range,new TypeReference<List<Integer>>(){});
        SheetCellRangeCondition rangeCondition = new SheetCellRangeCondition(rangeParams.get(0),rangeParams.get(1),rangeParams.get(2),rangeParams.get(3),rangeParams.get(4));
        Integer lastCellId = null;
        List<SheetCell> cells = sheetCellService.getCellOfStyleByRange(rangeCondition, nextCellId, limit);
        List<Object[]> cellQuadPlexs = Collections.emptyList();
        if(cells.size()>0){
            cellQuadPlexs = Lists.transform(cells, new Function<SheetCell, Object[]>(){
                @Override
                public Object[] apply(SheetCell cell) {
                    return new Object[]{cell.getTabId(), cell.getX(), cell.getY(), cell.getContent()};
                }
            
            });
            if(cells.size() == limit){
                lastCellId= cells.get(cells.size() -1).getId();
            }
        }
        results.put("results", cellQuadPlexs);
        results.put("nextCellId", lastCellId);
        results.put("success", true);
        outputJson(results, request, response);
    }
    
	
	/**
	 * This function handle UI save as action. File -> Save as
	 * 
	 * If id is empty then we need create a new file and copy everything from the old file; 
	 * if id is not empty, then means it overwrite an existed file, we will remove the old file 
	 * content first and then copy all the content from the old file.
	 * 
	 * @param oldFileId
	 * @param id
	 * @param name
	 * @param exname
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "saveFileAs")
	public void saveFileAs(@RequestParam String oldFileId,
			@RequestParam(required = false) String id,
			@RequestParam String name,
			@RequestParam String exname,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO
		// Replace this with existing user id
		User loginUser = this.getLoginUser();
		
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		DocumentFile currentFile = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(oldFileId)));
		
		DocumentFile saveAsFile = null;
		if (id != null && id.length()>3) {
			saveAsFile = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(id)));
		}
		
		// if saveAsFile is null
		if (saveAsFile == null) {
			saveAsFile = new DocumentFile(loginUser);
			saveAsFile.setName(name);
			saveAsFile.setStar(currentFile.getStar());
			saveAsFile.setUpdateDate(new Date());
			saveAsFile.setCreateDate(new Date());
			if (exname.equalsIgnoreCase(BaseAppConstants.SHEET_FILE_EXNAME) ||
					exname.equalsIgnoreCase(BaseAppConstants.SHEET_TEMPLATE_EXNAME))
			    saveAsFile.setExname(exname);
			else 
				saveAsFile.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
			documentFileService.createDocumentFile(saveAsFile);
			sheetService.copyFile(currentFile, saveAsFile);
		} else {
			saveAsFile.setName(name);
			saveAsFile.setStar(currentFile.getStar());
			saveAsFile.setUpdateDate(new Date());
			documentFileService.updateDocumentFile(saveAsFile);
			
			sheetService.deleteAllTabs(saveAsFile);
			documentConfigService.clean(saveAsFile);
			sheetDropdownService.deleteByDocumentId(saveAsFile.getId());
			
			sheetService.copyFile(currentFile, saveAsFile);
		}
		
		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);
		results.put("fileId", fileDesEncrypter.doEncryptUrl(saveAsFile.getId().toString()));

		outputJson(results, request, response);
	}

	/**
	 * This is the action to create a file from template
	 * 
	 * @param tplFileId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "copyFromTpl")
	public void copyFromTpl(@RequestParam String tplFileId,
			HttpServletRequest request, HttpServletResponse response) {
		// ok, at this moment we will hard code userId as 1 TODO ...
		User loginUser = this.getLoginUser();

		DesEncrypter fileDesEncrypter = DecryptEncryptUtil
				.getDesEncrypterForFile(loginUser.getUsername());
		DocumentFile tpl = documentFileService.getById(new Integer(
				fileDesEncrypter.doDecryptUrl(tplFileId)));

		DocumentFile documentFile = new DocumentFile(loginUser);
		documentFile.setName(tpl.getName());
		documentFile.setStar(false);
		documentFile.setUpdateDate(new Date());
		documentFile.setCreateDate(new Date());
		documentFile.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
		documentFileService.createDocumentFile(documentFile);

		// copy file from and to ...
		sheetService.copyFile(tpl, documentFile);

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);
		results.put("fileId",
				fileDesEncrypter.doEncryptUrl(documentFile.getId().toString()));

		outputJson(results, request, response);
	}

	/**
	 * This is the action to find the cells content like query 
	 * 
	 * @param tplFileId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "findCells")
	public void findCells(@RequestParam String fileId,
			@RequestParam String query,
			HttpServletRequest request, HttpServletResponse response) {
		// ok, at this moment we will hard code userId as 1 TODO ...
		User loginUser = this.getLoginUser();
		
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		DocumentFile currentFile = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));
		
		Map<String, Object> results = new HashMap<>();
		if (query.trim().length() < 2) {
			results.put("success", false);
			results.put("info", "Please enter at least 2 characters to process search");
			outputJson(results, request, response);
			return;
		}
		
		// this is the query by - add % % ...
        String _queryName = MyBatisOgnlUtil.matchAnywhere(query);      
        List<SheetCell> cells = sheetCellService.getCellsBySearch(currentFile, _queryName);
        List<SheetCellObj> cellObjs = Lists.transform(cells, cell2CellObjFunc);
        
        results.put("success", true);
        results.put("total", cells.size());
        results.put("results", cellObjs);
        outputJson(results, request, response);
	}
	
	
	@RequestMapping(value = "findCells2")
    public void findCells2(@RequestParam String fileId,
            @RequestParam String query,
            @RequestParam(defaultValue="0") Integer start,
            @RequestParam(defaultValue="10") Integer limit,
            HttpServletRequest request, HttpServletResponse response) {
        // ok, at this moment we will hard code userId as 1 TODO ...
        User loginUser = this.getLoginUser();
        
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
        DocumentFile currentFile = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));
        
        Map<String, Object> results = new HashMap<>();
        if (query.trim().length() < 2) {
            results.put("success", false);
            results.put("info", "Please enter at least 2 characters to process search");
            outputJson(results, request, response);
            return;
        }
        
        Integer lastCellId = null;
        // this is the query by - add % % ...
        String _queryName = MyBatisOgnlUtil.matchAnywhere(query);      
        List<SheetCell> cells = sheetCellService.getCellsBySearch2(currentFile, _queryName, start, limit);
        
        if(cells.size() == limit){
            lastCellId= cells.get(cells.size() -1).getId();
        }
        List<Object[]> cellObjs = Lists.transform(cells, cell2ArrayFunc);
        
        results.put("success", true);
        results.put("total", cells.size());
        results.put("results", cellObjs);
        results.put("nextCellId", lastCellId);
        outputJson(results, request, response);
    }
	/**
	 * This is action to process import and create a new file
	 * based on imported file.
	 * 
	 * @param filePath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	public void uploadFile(@RequestParam MultipartFile filePath,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> results = new HashMap<>();
		User loginUser = this.getLoginUser();

		String userIdString = String.valueOf(loginUser.getId());
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil
				.getDesEncrypterForFile(loginUser.getUsername());
		File excelFile = SheetFileUtil.placeUploadExcel(userIdString, filePath);
		String extension = FilenameUtils.getExtension(excelFile.getName())
				.toLowerCase();

		if (!extension.equals("csv") && !extension.equals("xlsx")) {
			results.put("success", false);
			results.put("info",
					"Currently only .CSV and .XLSX file is allowed to import.");
			outputJson(results, request, response);
			return;
		}

		try {
			// get imported file first ...
			DocumentFile documentFile = new DocumentFile(loginUser);
			documentFile.setName(excelFile.getName());
			documentFile.setStar(false);
			documentFile.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
			documentFile.setCreateDate(new Date());
			documentFile.setUpdateDate(new Date());

			documentFileService.createDocumentFile(documentFile);

			if ("csv".equals(extension)) {
				sheetImportService.importCsv(documentFile, excelFile);
			} else {
				sheetImportService.importXlsx(documentFile, excelFile,
						request.getLocale());
			}
			String encryptedId = fileDesEncrypter.doEncryptUrl(documentFile
					.getId().toString());

			results.put("fileId", encryptedId);
			results.put("status", "ok");
			results.put("success", true);

		} catch (Exception ex) {
			ex.printStackTrace();
			results.put("success", false);
			results.put("info", ex.getMessage());
		}

		outputJson(results, request, response);
	}

	/**
	 * This is for export function 
	 * 
	 * @param documentId
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value = "export", method = RequestMethod.GET)
	public void exportFile(@RequestParam String documentId,
	            @RequestParam(defaultValue="xlsx") String fileType,
	        HttpServletRequest request, HttpServletResponse response) throws Exception {
	    
	    User loginUser = this.getLoginUser();
	    DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
	    
	    DocumentFile documentFile = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(documentId)));
	        File xlsxFile = sheetExportService.exportSpreadsheet(documentFile,  request.getLocale());
	        String exportFilename =FilenameUtils.getBaseName(documentFile.getName()) + "." + fileType;
	        
	        File exportFile;
	        if(!"xlsx".equals(fileType)){
	            exportFile = File.createTempFile(exportFilename, null, BaseAppConfiguration.instance().getTempDir());
	            DocumentConvertPlugins.instance().getPlugin().convertFile(xlsxFile, exportFile, fileType);
	            
	        }else{
	            exportFile = xlsxFile;
	    }
	        String contentType = MimeTypes.getMimeType(fileType);
	    
	        try(InputStream in = new FileInputStream(exportFile)){
	            super.outputBinary(exportFilename, in, contentType, request, response);
	    }
	}

    @RequestMapping(value = "uploadImage", method=RequestMethod.POST)
	public void uploadImage(@RequestParam MultipartFile imagepath,
			@RequestParam String documentId,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		User loginUser = this.getLoginUser();
		String fileName;
		if (imagepath != null) {
            fileName = FilenameUtils.getName(imagepath.getOriginalFilename());
            String extension = FilenameUtils.getExtension(fileName);

            if (!BaseAppConstants.ACCEPT_IMAGE_TYPE.contains(extension.toLowerCase())) {
                throw new MoreInfoError("Only image file (.png, .jpg, .gif, .bmp) is allowed to upload");
            }
        } else {
            throw new MoreInfoError("Please select one of images to upload");
        }
		
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		DocumentFile documentFile = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(documentId)));		
			
		File pictureDir = SheetFileUtil.getUserExcelPictureDirByDocument(
				String.valueOf(documentFile.getAuthor().getId()), String.valueOf(documentFile.getId()));
		
		// clean file ...
		fileName = BaseFileUtil.cleanFileName(fileName);	
		File pictureImageFile = new File(pictureDir, fileName);
		FileUtils.copyInputStreamToFile(imagepath.getInputStream(), pictureImageFile);
		
		String httpFilePath = BaseFileUtil.getPathUnderUserFileServer(pictureImageFile);
			
		Map<String, Object> results = Maps.newHashMap();
		results.put("success", "true");
		results.put("httpFilePath", httpFilePath);
		results.put("info", messageSource.getMessage("general.changesSaved", null, getUserLocale()));
		
		outputJson(results, request, response);
	}
    
    @RequestMapping(value = "delete", method = RequestMethod.GET)
	public void delete(@RequestParam String documentId,
	        HttpServletRequest request, HttpServletResponse response) throws Exception {
	    
	    User loginUser = this.getLoginUser();
	    DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());    
	    DocumentFile documentFile = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(documentId)));
	    
	    sheetService.deleteAllTabs(documentFile);
	    
	    // TODO ..... delete documentFile now ....
	    
	    Map<String, Object> results = Maps.newHashMap();
		results.put("success", "true");
		results.put("info", messageSource.getMessage("general.changesSaved", null, getUserLocale()));	
		outputJson(results, request, response);
    }
    


	
}
