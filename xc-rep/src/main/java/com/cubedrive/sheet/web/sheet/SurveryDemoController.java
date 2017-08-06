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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.service.document.DocumentConfigService;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetCellObj;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.service.sheet.SheetCellService;
import com.cubedrive.sheet.service.sheet.SheetService;
import com.cubedrive.sheet.service.sheet.SheetTabElementService;
import com.cubedrive.sheet.service.sheet.SheetTabService;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/surveyDemo")
public class SurveryDemoController extends BaseSheetController {
	
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
	
	@RequestMapping(value = "injectData")
	public void injectData(
			@RequestParam(required = true) Object jsonData,
			HttpServletRequest request, HttpServletResponse response) {

		User loginUser = this.getLoginUser();

		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());

		// ok, we need load cells json based on file id ...
		DocumentFile document = null;
		List<String> exnameList = new ArrayList<String>();
		exnameList.add(BaseAppConstants.SHEET_FILE_EXNAME);
		List<DocumentFile> documents = documentFileService.findFileFolders(loginUser, null, exnameList, "EnterpriseSheet Survey Result", "document_name");
		if (documents == null || documents.size() == 0) {
			document = createSurveyDocumentFile(loginUser);
			documentFileService.createDocumentFile(document);
			sheetService.createDefaultSS(document);
			
			// insert title item
			List<SheetCell> titleObjs = new ArrayList<SheetCell>();
			SheetTab tab = sheetTabService.findFirstTab(document.getId());
			SheetCell cell01 = new SheetCell(tab.getId(), 0, 1, "{width: 160}");
			titleObjs.add(cell01);
			SheetCell cell02 = new SheetCell(tab.getId(), 0, 2, "{width: 150}");
			titleObjs.add(cell02);
			SheetCell cell03 = new SheetCell(tab.getId(), 0, 3, "{width: 150}");
			titleObjs.add(cell03);
			SheetCell cell08 = new SheetCell(tab.getId(), 0, 8, "{width: 250}");
			titleObjs.add(cell08);
			SheetCell cell09 = new SheetCell(tab.getId(), 0, 9, "{width: 150}");
			titleObjs.add(cell09);
			SheetCell cell1 = new SheetCell(tab.getId(), 1, 1, "{data: \"First lean EnterpriseSheet\", fw: \"bold\"}");
			titleObjs.add(cell1);
			SheetCell cell2 = new SheetCell(tab.getId(), 1, 2, "{data: \"Title\", fw: \"bold\"}");
			titleObjs.add(cell2);
			SheetCell cell3 = new SheetCell(tab.getId(), 1, 3, "{data: \"Organization\", fw: \"bold\"}");
			titleObjs.add(cell3);
			SheetCell cell4 = new SheetCell(tab.getId(), 1, 4, "{data: \"Priced fairly\", fw: \"bold\"}");
			titleObjs.add(cell4);
			SheetCell cell5 = new SheetCell(tab.getId(), 1, 5, "{data: \"High quality\", fw: \"bold\"}");
			titleObjs.add(cell5);
			SheetCell cell6 = new SheetCell(tab.getId(), 1, 6, "{data: \"Recommend\", fw: \"bold\"}");
			titleObjs.add(cell6);
			SheetCell cell7 = new SheetCell(tab.getId(), 1, 7, "{data: \"Custom service\", fw: \"bold\"}");
			titleObjs.add(cell7);
			SheetCell cell8 = new SheetCell(tab.getId(), 1, 8, "{data: \"Comments\", fw: \"bold\"}");
			titleObjs.add(cell8);
			SheetCell cell9 = new SheetCell(tab.getId(), 1, 9, "{data: \"Submit Date\", fw: \"bold\"}");
			titleObjs.add(cell9);
			sheetCellService.insertBatchSetCells(titleObjs, tab);
		} else 
			document = documents.get(0);

		// get json data and inject into file ...
		Map<String, Object> items = JsonUtil.getJsonObj((String) jsonData);
		
		// inject those data into document ... get first tab ...
		SheetTab tab = sheetTabService.findFirstTab(document.getId());
		Integer x = sheetCellService.getMaxX(tab);
		
		List<SheetCell> listBatchObjs = new ArrayList<SheetCell>();
		Integer y = 1;
		for (Object value : items.values()) {
			String content = "{data:\"" + value + "\"}";
			SheetCell cell = new SheetCell(tab.getId(), x+1, y, content);
			listBatchObjs.add(cell);
			y++;
		}
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm"); 
		SheetCell cell2 = new SheetCell(tab.getId(), x+1, y, "{data:\"" + dt.format(new Date()) + "\"}");
		listBatchObjs.add(cell2);
		
		sheetCellService.insertBatchSetCells(listBatchObjs, tab);
		
		Map<String, Object> results = Maps.newHashMap();
		results.put("documentId", fileDesEncrypter.doEncrypt(document.getId().toString()));
		results.put("success", true);

		outputJson(results, request, response);
	}
	
	// this is for test purpose
	@RequestMapping(value = "getCells")
	public void getCells (
			HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> results = Maps.newHashMap();
		
		List<SheetCellObj> cellObjs = new ArrayList<SheetCellObj>();
		
		for (int i = 1; i <= 20; i++) {
	        for(int j = 1; j <= 50; j++){
	        	int data = i * j;
	        	SheetCellObj obj = new SheetCellObj(i, j, 2, "{data: \"" + data  + " ok\"}");
	        	cellObjs.add(obj);	
	        }
        }
		
		results.put("success", true);
		results.put("cellObjs", cellObjs);
		outputJson(results, request, response);
	}
	
	// private method
	private DocumentFile createSurveyDocumentFile(User author) {
		DocumentFile file = new DocumentFile(author);
		file.setCreateDate(new Date());
		file.setUpdateDate(new Date());
		file.setName("EnterpriseSheet Survey Result");
		file.setStar(true);
		file.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
		return file;
	}

}
