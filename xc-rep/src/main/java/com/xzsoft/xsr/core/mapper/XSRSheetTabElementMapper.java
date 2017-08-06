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
package com.xzsoft.xsr.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;

@Repository
public interface XSRSheetTabElementMapper extends SheetTabElementMapper{	

	void insert(@Param(value = "element") SheetTabElement element);
	
	void batchInsert(List<SheetTabElement> elements);
	
	List<SheetTabElement> findElementsByTabAndType(@Param(value = "tabId") Integer tabId,@Param(value="type") String type);
	
	List<SheetTabElement> getElementsByName(@Param(value = "tabId") Integer tabId,@Param(value="type") String type, @Param(value="name") String name);

	List<SheetTabElement> findElementsByTab(@Param(value = "tabId") Integer tabId);
	
	public int getFjModalDataCount(
			@Param(value = "orgId") String orgId, 
			@Param(value = "periodId") String periodId, 
			@Param(value = "cnyId") String cnyId, 
			@Param(value = "modalsheetId") String modalsheetId) throws Exception;
}
