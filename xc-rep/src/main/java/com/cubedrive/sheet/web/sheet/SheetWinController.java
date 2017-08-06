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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.service.document.DocumentConfigService;
import com.cubedrive.base.service.document.DocumentFileService;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.sheet.service.sheet.SheetCellService;
import com.cubedrive.sheet.service.sheet.SheetImportService;
import com.cubedrive.sheet.service.sheet.SheetService;
import com.cubedrive.sheet.service.sheet.SheetTabElementService;
import com.cubedrive.sheet.service.sheet.SheetTabService;
import com.cubedrive.sheet.web.BaseSheetController;

@Controller
@RequestMapping(value = "/sheetwin")
public class SheetWinController extends BaseSheetController {
	
	private final static Logger LOG = LoggerFactory.getLogger(SheetWinController.class);
	
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

    @Autowired
	private SheetImportService sheetImportService;
    
    
	/**
	 * This is for API example ...
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
		DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
		
		String _editFileId = null;
		DocumentFile document = null;
		if (editFileId != null) {
			_editFileId = editFileId;
			document = documentFileService.getById(new Integer(fileDesEncrypter.doDecryptUrl(_editFileId)));
		} 
		
		model.addAttribute("editFileId", _editFileId);
		model.addAttribute("lang", "en");
		
        return "sheetwin/main";
    }
    
}
