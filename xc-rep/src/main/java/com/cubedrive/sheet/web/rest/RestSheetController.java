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
package com.cubedrive.sheet.web.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.exception.MoreInfoAndCodeError;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.base.utils.MimeTypes;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.service.sheet.SheetCellService;
import com.cubedrive.sheet.service.sheet.SheetExportService;
import com.cubedrive.sheet.service.sheet.SheetImportService;
import com.cubedrive.sheet.service.sheet.SheetService;
import com.cubedrive.sheet.service.sheet.SheetTabService;
import com.cubedrive.sheet.utils.SheetFileUtil;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Controller
@RequestMapping(value = "/restSheet/")
public class RestSheetController extends BaseSheetController {

    private static final Logger LOG = LoggerFactory .getLogger(RestSheetController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentFileService documentFileService;

    @Autowired
    private SheetService sheetService;
    
    @Autowired
	private SheetImportService sheetImportService;

    @Autowired
    private SheetExportService sheetExportService;
    
    @Autowired
    private SheetTabService sheetTabService;
    
    @Autowired
    private SheetCellService sheetCellService;
    
    /**
     * This is the method to import a file through REST web service.
     * You can call this method from your server side code.
     *
     * URL: http://localhost:8080/sheet/restSheet/initialSheet
     * Method: POST
     * Request body: fileName (option)
     *               upload csv, xlsx file (file name should be end with .csv, .xlsx) - name: uploadXlsFile
     *               folderId (option)
     *               
     * Return: JSON string
     *         [success: 'true', info: 'change saved', code: '200', fileId: 'uniqueId']
     *         For Code: 200 - ok; 401 - unauthorized; 404 - not found; 406 - Not Acceptable; 500 - internal error
     *         
	 * @param fileName - option. If you do not pass this, name will be uploaded file name
	 * @param uploadXlsFile
	 * @param folderId - Option. If you pass this folder id. This file will be created under this folder.
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "initialSheet", method = RequestMethod.POST)
	public void initialSheet(
		//	@RequestParam(required = true) String username,
		//	@RequestParam(required = true) String password,
			@RequestParam(required = false) String fileName,
			@RequestParam MultipartFile uploadXlsFile,
			@RequestParam(required = false) String folderId,
            HttpServletRequest request,	HttpServletResponse response) throws Exception {
		
		try {
			DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile();
			String parentId = "0";
			if (folderId != null && !folderId.equals("0")) parentId = fileDesEncrypter.doDecryptUrl(folderId);
			
			User author = this.getLoginUser(); //userService.getUserByName(username);
			File importExcelFile = SheetFileUtil.placeUploadExcel(author.getId().toString(), uploadXlsFile);
			String extension = FilenameUtils.getExtension(importExcelFile.getName()).toLowerCase();
			
			if (null == fileName || fileName.trim().length() == 0) {
				fileName = importExcelFile.getName();
			}
			
			DocumentFile documentFile = new DocumentFile(author);
			documentFile.setParentId(parentId);
			documentFile.setName(fileName);
			documentFile.setStar(false);
			documentFile.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
			documentFile.setCreateDate(new Date());
			documentFile.setUpdateDate(new Date());

			documentFileService.createDocumentFile(documentFile	);

			if ("csv".equals(extension)) {
				sheetImportService.importCsv(documentFile, importExcelFile);
			} else if ("xlsx".equals(extension)) {
				sheetImportService.importXlsx(documentFile, importExcelFile, request.getLocale());
			} else {
				throw new MoreInfoAndCodeError("Please choose .csv or .xlsx file to process import.","406");
			}
			String encryptedId = fileDesEncrypter.doEncryptUrl(documentFile.getId().toString());
		
			outputText("{\"success\":true, \"code\": 200, \"info\":\"Change Saved\", \"filename\":\"" + fileName + ",\"fileId\":\"" + encryptedId + "\"}",request,response);
		} catch (Exception e) {
			LOG.error("initialSheet failed",e);
            throw new MoreInfoAndCodeError("Internal Error","500");
		} 

	}
    
    
    /**
     * This method will throw a MS xlsx file - export MS Excel XLSX file. User need provide the required file id.
     * <p/>
     * URL: http://localhost:8080/sheet/restSheet/exportXlsx?fileId=F0uoaw5*gnI_
     * <p/>
     * Method: POST
     * Request body: username, password, fileId
     *
     * @param fileId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportXlsx")
    public void exportXlsx(@RequestParam(required = true) String fileId,
                         //  @RequestParam(required = false) String username,
                         //  @RequestParam(required = false) String password,
                           HttpServletRequest request, HttpServletResponse response) {

        if (null == fileId) {
            throw new MoreInfoAndCodeError("FileId is the required fields.", "406");
        }

        // now we will return a list of file
       //  User author = userService.getUserByName(username);

        try {
            DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile();
            String documentId = fileDesEncrypter.doDecryptUrl(fileId);
            DocumentFile documentFile = documentFileService.getById(Integer.valueOf(documentId));

            // check whether it is doc file
            if (!documentFile.getExname().equalsIgnoreCase(BaseAppConstants.SHEET_FILE_EXNAME)) {
                throw new MoreInfoAndCodeError("Only XLSX file is allowed to process.", "406");
            }
            
            File excelFile = sheetExportService.exportSpreadsheet(documentFile, Locale.US);
    	    String exportFilename = documentFile.getName();
    	    if(! exportFilename.endsWith(".xlsx")){
    	        exportFilename = exportFilename + ".xlsx";
    	    }

            try(InputStream in = new FileInputStream(excelFile)){
    	        super.outputBinary(exportFilename, in, MimeTypes.MIME_APPLICATION_VND_MSEXCEL, request, response);
    	    }
            
        } catch (Exception e) {
            LOG.error("Export SpreadSheet failed.", e);
            throw new MoreInfoAndCodeError("Internal Error", "500");
        }

    }
    
    /**
     * http://localhost:8080/sheet/restSheet/loadData/doc/Ydv6oOezu30_/sheet/1
     * 
     * @param fileId
     * @param sheetIndex
     * @param offset
     * @param limit
     * @param request
     * @param response
     */
    @RequestMapping(value="loadData/doc/{fileId}/sheet/{sheetIndex}", method=RequestMethod.GET)
    public void loadData(@PathVariable String fileId, 
            @PathVariable Integer sheetIndex,
            @RequestParam(defaultValue="0") Integer offset,
            @RequestParam(defaultValue="3000") Integer limit,
            HttpServletRequest request, HttpServletResponse response){
        
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile();
        Integer documentId = Integer.parseInt(fileDesEncrypter.doDecryptUrl(fileId));
        SheetTab sheetTab = sheetTabService.getTabByDocumentAndIndex(documentId, sheetIndex);        
        
        List<Object[]> cells;
        Integer nextOffset;
        
        if(sheetTab != null){
            List<SheetCell> sheetCells = sheetCellService.listCellByTabId(sheetTab.getId(), sheetTab.getCellTable(), offset, limit);
            cells = Lists.transform(sheetCells, new Function<SheetCell, Object[]>(){

                @Override
                public Object[] apply(SheetCell cell) {
                    Map<String,Object> jsonMap = JsonUtil.getJsonObj(cell.getContent());
                    Object customData = jsonMap.get("customData");
                    Object cellValue = customData != null ? customData : jsonMap.get("data");
                    return new Object[]{cell.getX(), cell.getY(), cellValue};
                }            
            });
            
            if(sheetCells.isEmpty()){
                nextOffset = offset;
            }else{
                nextOffset = offset + sheetCells.size();
            }
        }else{
            cells = Collections.emptyList();
            nextOffset = 0;
        }
        
        Map<String,Object> results = new HashMap<>();
        results.put("success", true);
        results.put("offset", nextOffset);
        results.put("cells", cells);
        outputJson(results, request, response);
    }

}
