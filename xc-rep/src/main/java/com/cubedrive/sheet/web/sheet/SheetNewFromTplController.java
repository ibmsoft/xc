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

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.service.document.DocumentConfigService;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.base.web.BaseController;
import com.cubedrive.sheet.service.sheet.SheetCellService;
import com.cubedrive.sheet.service.sheet.SheetService;
import com.cubedrive.sheet.service.sheet.SheetTabElementService;
import com.cubedrive.sheet.service.sheet.SheetTabService;

@Controller
@RequestMapping(value = "/sheetNewFromTpl")
public class SheetNewFromTplController extends BaseController {
	
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
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@RequestParam(required = false) String tplFileId,
    		Model model) throws Exception {	

		// ok, at this moment we will hard code userId as 1 TODO ...
		User loginUser = this.getLoginUser();	
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		
		try {
			DocumentFile tpl = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(tplFileId)));	
			DocumentFile documentFile = new DocumentFile(loginUser);
	        documentFile.setName(tpl.getName());
	        documentFile.setStar(false);
	        documentFile.setUpdateDate(new Date());
	        documentFile.setCreateDate(new Date());
	        documentFile.setExname(BaseAppConstants.SHEET_FILE_EXNAME);
	        documentFileService.createDocumentFile(documentFile);
	        sheetService.copyFile(tpl, documentFile);
	        String _fileId = fileDesEncrypter.doEncryptUrl(documentFile.getId().toString());      
	        return "redirect:sheet?editFileId=" + _fileId;
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:sheet";
		}
	}

}
