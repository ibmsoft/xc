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
package com.cubedrive.sheet.persistence.sheet;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.sheet.domain.sheet.SheetDropdown;

@Repository
public interface SheetDropdownMapper {

	SheetDropdown getById(@Param(value = "id") Integer id);
	
	SheetDropdown getByNameAndFileId(@Param(value = "name") String name,
			@Param(value = "fileId") Integer fileId);
	
	void insert(@Param(value = "sheetDropdown") SheetDropdown sheetDropdown);
	
	void update(@Param(value = "sheetDropdown") SheetDropdown sheetDropdown);
	
	List<SheetDropdown> findByDocId(@Param(value = "documentId") Integer documentId);
	
	void deleteById(@Param(value = "id") Integer id);

	void deleteByDocumentId(@Param(value = "documentId") Integer documentId);
}
