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

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentFileMapper;
import com.cubedrive.sheet.domain.sheet.SheetDropdown;
import com.cubedrive.sheet.persistence.sheet.SheetDropdownMapper;
import com.cubedrive.sheet.web.sheet.SheetDropdownObj;

@Service
public class SheetDropdownService {

	@Autowired
	private SheetDropdownMapper sheetDropdownMapper;

	@Autowired
	private DocumentFileMapper documentFileMapper;

	// check to see whether insert item existing ...
	public void insert(SheetDropdown sheetDropdown) {
		SheetDropdown existingDrop = this.getByNameAndFileId(sheetDropdown.getName(), 
				sheetDropdown.getDocumentFile().getId());
		if (existingDrop != null) {
			existingDrop.setContent(sheetDropdown.getContent());
			sheetDropdownMapper.update(existingDrop);
		} else {
			sheetDropdownMapper.insert(sheetDropdown);
		}
	}

	/**
	 * this will list a bunch of drop down list ...
	 * 
	 * @param fileId
	 * @return
	 */
	public List<SheetDropdownObj> findByDocId(Integer fileId) {
		List<SheetDropdown> items = sheetDropdownMapper.findByDocId(fileId);

		List<SheetDropdownObj> objs = new ArrayList<SheetDropdownObj>();
		for (int i = 0; i < items.size(); i++) {
			SheetDropdown item = items.get(i);
			SheetDropdownObj obj = new SheetDropdownObj(item.getId().toString(), item.getName(), item.getContent());
			objs.add(obj);
		}

		return objs;
	}

	// copy from a file to another file ...
	public void copy(Integer fromDocId, Integer toDocId) {
		// first delete toDoc ...
		this.deleteByDocumentId(toDocId);

		DocumentFile toDoc = documentFileMapper.getById(toDocId);

		List<SheetDropdown> items = sheetDropdownMapper.findByDocId(fromDocId);
		for (int i = 0; i < items.size(); i++) {
			SheetDropdown item = items.get(i);
			item.setDocumentFile(toDoc);
			sheetDropdownMapper.insert(item);
		}
	}

	public SheetDropdown getById(Integer id) {
		return sheetDropdownMapper.getById(id);
	}
	
	public void delete(SheetDropdown obj) {
		sheetDropdownMapper.deleteById(obj.getId());
	}

	public SheetDropdown getByNameAndFileId(String name, Integer fileId) {
		return sheetDropdownMapper.getByNameAndFileId(name, fileId);
	}

	public void deleteByDocumentId(Integer documentId) {
		sheetDropdownMapper.deleteByDocumentId(documentId);
	}
}