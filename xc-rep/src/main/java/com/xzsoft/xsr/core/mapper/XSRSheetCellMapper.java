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
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.persistence.sheet.SheetCellMapper;
import com.xzsoft.xsr.core.modal.CellData;

@Repository
public interface XSRSheetCellMapper extends SheetCellMapper {

	List<SheetCell> findCellsByTabOnExport(@Param(value = "tabId") Integer tabId,
										   @Param(value = "cellTable") String cellTable,
										   @Param(value="offset") Integer offset,
										   @Param(value = "limit") Integer limit);

	// this one is mainly for import part - with general tabId ...
	void batchInsertSheetCell(
			@Param(value = "tabId") Integer tabId,
			@Param(value = "cells") List<SheetCell> cells,
			@Param(value = "cellTable") String cellTable);

	// this one is based on the tab inside cells ...
	void batchInsertSheetCell2(
			@Param(value = "cells") List<SheetCell> cells,
			@Param(value = "cellTable") String cellTable);
	
	/**
	 * 通过模板id获取模板格式
	 * @param msFormatId
	 * @throws Exception
	 */
	public List<SheetCell> getModalsheetFormatCellList(String msFormatId) throws Exception;
	/**
	 * 查询报表数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<CellData> loadDataValue(
			@Param(value = "suitId") String suitId,
			@Param(value = "msformatId") String msformatId,
			@Param(value = "entityId") String entityId,
			@Param(value = "periodId") String periodId,
			@Param(value = "cnyId") String cnyId,
			@Param(value = "dbType") String dbType,
			@Param(value = "ledgerId") String ledgerId) throws Exception;
	
	public List<Map<String, Object>> getFjRowitemInfo(String suitId,String modaltypeId,String modalsheetId)throws Exception;
}
