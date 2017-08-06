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

import com.cubedrive.sheet.domain.sheet.SheetCustom;

@Repository
public interface SheetCustomMapper {

	SheetCustom getById(@Param(value = "id") Integer id);
	
	void insert(@Param(value = "sheetCustom") SheetCustom sheetCustom);
	
	void update(@Param(value = "sheetCustom") SheetCustom sheetCustom);
	
	List<SheetCustom> findByUserId(@Param(value = "userId") Integer userId);
	
	SheetCustom findByNameAndUser(@Param(value = "sheetCustom") SheetCustom sheetCustom);
	
	void deleteById(@Param(value = "id") Integer id);

	void deleteByUserId(@Param(value = "userId") Integer userId);
}
