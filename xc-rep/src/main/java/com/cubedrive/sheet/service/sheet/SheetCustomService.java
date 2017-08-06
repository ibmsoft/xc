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
package com.cubedrive.sheet.service.sheet;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.persistence.document.DocumentFileMapper;
import com.cubedrive.sheet.domain.sheet.SheetCustom;
import com.cubedrive.sheet.persistence.sheet.SheetCustomMapper;
import com.cubedrive.sheet.web.sheet.SheetCustomObj;

@Service
public class SheetCustomService {

	@Autowired
	private SheetCustomMapper sheetCustomMapper;

	@Autowired
	private DocumentFileMapper documentFileMapper;

	// check to see whether insert item existing ...
	public void insert(SheetCustom sheetCustom) {
		// first check whether this custom name existing ...
		SheetCustom item = sheetCustomMapper.findByNameAndUser(sheetCustom);
		if (item == null) sheetCustomMapper.insert(sheetCustom);
	}

	/**
	 * this will list a bunch of drop down list ...
	 * 
	 * @param fileId
	 * @return
	 */
	public List<SheetCustomObj> findByUserId(User user) {
		List<SheetCustom> items = sheetCustomMapper.findByUserId(user.getId());
		
		// FIRST check to see empty or not ... if yes, add 2 default ...
		if (items == null || items.size() == 0) {
			SheetCustom default0 = new SheetCustom(user, "number", "0.00%");
			sheetCustomMapper.insert(default0);
			
			SheetCustom default00 = new SheetCustom(user, "number", "0.0e+00");
			sheetCustomMapper.insert(default00);
			
			SheetCustom default01 = new SheetCustom(user, "number", "# ??/??");
			sheetCustomMapper.insert(default01);
			
			SheetCustom default1 = new SheetCustom(user, "number", "#,##0.00;[Red]-#,##0.00");
			sheetCustomMapper.insert(default1);
			
			SheetCustom default2 = new SheetCustom(user, "number", "[Blue]#,##0.00;[Red]-#,##0.00;[Red]zero");
			sheetCustomMapper.insert(default2);
			
			SheetCustom default3 = new SheetCustom(user, "number", "$#,##0.00;[Red]-$#,##0.00;[Red]ZERO");
			sheetCustomMapper.insert(default3);
			
			items = sheetCustomMapper.findByUserId(user.getId());
		}
		
		List<SheetCustomObj> results = new ArrayList<SheetCustomObj>();
		for (SheetCustom item : items) {
			SheetCustomObj obj = new SheetCustomObj(item);
			results.add(obj);
		}
		return results;
	}

	public SheetCustom getById(Integer id) {
		return sheetCustomMapper.getById(id);
	}
	
	public void delete(SheetCustom obj) {
		sheetCustomMapper.deleteById(obj.getId());
	}

	public void deleteByUserId(Integer userId) {
		sheetCustomMapper.deleteByUserId(userId);
	}
}