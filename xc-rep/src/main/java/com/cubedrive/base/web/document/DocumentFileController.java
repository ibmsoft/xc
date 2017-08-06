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
package com.cubedrive.base.web.document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.SQLQueryResult;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DateTimeUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.base.utils.MyBatisOgnlUtil;
import com.cubedrive.base.web.BaseController;
import com.cubedrive.sheet.service.sheet.SheetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/document")
public class DocumentFileController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private DocumentFileService documentFileService;

	@Autowired
	private SheetService sheetService;
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
		User loginUser = this.getLoginUser();
        return "documentFile/main";
    }

	/**
	 * Action to create a new file
	 * 
	 * @param exname
	 * @param name
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "createFile")
	public void createFile(@RequestParam(required = true) String exname,
			@RequestParam(required = true) String initName,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil
				.getDesEncrypterForFile(loginUser.getUsername());

		DocumentFile documentFile = new DocumentFile(loginUser);
		documentFile.setName(initName);
		documentFile.setUpdateDate(new Date());
		documentFile.setCreateDate(new Date());		
		if (exname.equalsIgnoreCase(BaseAppConstants.SHEET_FILE_EXNAME)
				|| exname.equalsIgnoreCase(BaseAppConstants.SHEET_TEMPLATE_EXNAME)) {
		documentFile.setExname(exname);
		}
		documentFileService.createDocumentFile(documentFile);

		// ok, if it is sheet type ... we need create default 3 tab for this
		if (exname.equalsIgnoreCase(BaseAppConstants.SHEET_FILE_EXNAME)
				|| exname.equalsIgnoreCase(BaseAppConstants.SHEET_TEMPLATE_EXNAME)) {
			sheetService.createDefaultSS(documentFile);
		}

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);
		results.put("fileId", fileDesEncrypter.doEncryptUrl(documentFile.getId().toString()));

		outputJson(results, request, response);
	}

	@RequestMapping(value = "list")
	public void list(
			@RequestParam(required = false) Integer start,
			@RequestParam(required = false) Integer limit,
			@RequestParam(required = false) String sort,
			@RequestParam(required = false) String dir,
			@RequestParam(required = false) Boolean onlyTpl,
			@RequestParam(required = false, value = "query") String query,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

		// this is the query by - add % % ...
		String _queryName = MyBatisOgnlUtil.matchAnywhere(query);
		String queryBy = null;
		if (query != null && query.length() > 0) queryBy = "document_name";	
		
		List<String> exnameList = new ArrayList<String>();
		if (onlyTpl == null || onlyTpl == false) exnameList.add(BaseAppConstants.SHEET_FILE_EXNAME);
		exnameList.add(BaseAppConstants.SHEET_TEMPLATE_EXNAME);
		exnameList.add(BaseAppConstants.FOLDER_EXNAME);

		// get a list of result based on params.
		SQLQueryResult<DocumentFile> queryResult = documentFileService
				.getListDocuments(start, limit, sort, dir, _queryName, loginUser.getId(), exnameList);

		List<FileObject> fileObjs = this.getFileObjList(queryResult, fileDesEncrypter);

		Map<String, Object> results = Maps.newHashMap();
		results.put("totalCount", queryResult.totalRecords);
		results.put("results", fileObjs);
		results.put("metaData", FileObject.createMetaData());
		outputJson(results, request, response);
	}

	@RequestMapping(value = "changeFileName")
	public void changeFileName(@RequestParam(required = true) String id,
			@RequestParam(required = true) String name,
			HttpServletRequest request, HttpServletResponse response) {

		// ok, at this moment we will hard code userId as 1 TODO ...
		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

		DocumentFile document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(id)));

		document.setName(name);
		document.setUpdateDate(new Date());
		documentFileService.updateDocumentFile(document);

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);

		outputJson(results, request, response);
	}

	@RequestMapping(value = "changeFileStared")
	public void changeFileStared(@RequestParam(required = true) String fileId,
			@RequestParam(required = true) boolean fileStared,
			HttpServletRequest request, HttpServletResponse response) {

		// ok, at this moment we will hard code userId as 1 TODO ...
		User loginUser = this.getLoginUser();
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

		DocumentFile document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));

		document.setStar(fileStared);
		document.setUpdateDate(new Date());
		documentFileService.updateDocumentFile(document);

		Map<String, Object> results = Maps.newHashMap();
		results.put("success", true);

		outputJson(results, request, response);
	}

	@RequestMapping(value = "deleteFile", method=RequestMethod.POST)
	public void deleteFile(@RequestParam(required = true) String fileId,  HttpServletRequest request, HttpServletResponse response) {
	    
	    User loginUser = this.getLoginUser();
        DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

        DocumentFile document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(fileId)));
        documentFileService.deleteDocumentFile(document);
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", true);
        results.put("info", messageSource.getMessage("general.changesSaved", null, getUserLocale()));
        outputJson(results, request, response);
        
	}

	/**
	 * This is the function return a list of fileObjs ...
	 * 
	 * @param queryResult
	 * @param fileDesEncrypter
	 * @param loginUser
	 * @param queryForName
	 * @return
	 */
	private List<FileObject> getFileObjList(
			SQLQueryResult<DocumentFile> queryResult,
			DesEncrypter fileDesEncrypter) throws Exception {

		List<FileObject> fileObjs = Lists
				.newArrayListWithExpectedSize(queryResult.data.size());
		// loop
		for (int i = 0; i < queryResult.data.size(); i++) {
			DocumentFile file = queryResult.data.get(i);
			FileObject fileObj = new FileObject(file);
			fileObj.setId(fileDesEncrypter.doEncryptUrl(file.getId().toString()));			
			fileObj.setUpdateDate(DateTimeUtil.formatAsYYYYMMddHHmmss(file.getUpdateDate()));
			fileObjs.add(fileObj);
		}

		return fileObjs;
	}

}
