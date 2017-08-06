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

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabIdOrderInfo;
import com.cubedrive.sheet.domain.sheet.SheetTabSpan;
import com.cubedrive.sheet.persistence.resultmap.SheetTabWithRowSize;

@Repository
public interface SheetTabMapper {

	SheetTab getFirstSheetTabByDocumentFile(
			@Param(value = "documentId") Integer documentId);

	SheetTab getLastSheetTabByDocumentFile(
			@Param(value = "documentId") Integer documentId);

	SheetTab getById(@Param(value = "tabId") Integer tabId);
    
	List<SheetTabSpan> getTabSpans(@Param(value = "tabIds") Collection<Integer> tabIds, @Param(value = "cellTable") String cellTable);
	
	SheetTabSpan getTabSpan(@Param(value = "tabId") Integer tabId, @Param(value = "cellTable") String cellTable);
	
	SheetTab getActiveTab(@Param(value = "documentId") Integer documentId);

	List<SheetTab> findTabsByDocumentFile(
			@Param(value = "documentId") Integer documentId);

	// return cell max row size
	SheetTabWithRowSize getSheetTabWithRowSizeById(
			@Param(value = "tabId") Integer tabId,
			@Param(value = "cellTable") String cellTable);

	List<SheetTab> findTabsWithRowSizeByDocumentFile(
			@Param(value = "documentId") Integer documentId,
			@Param(value = "cellTable") String cellTable);

	Integer insertSheetTab(SheetTab tab);

	void updateSheetTab(SheetTab tab);
	
	void updateTabOrder(
			@Param(value = "tabId") Integer tabId,
			@Param(value = "tabOrder") Integer tabOrder);

	void deleteById(@Param(value = "tabId") Integer tabId,@Param(value = "cellTable") String cellTable);

	void deleteTabsByDocumentFile(
			@Param(value = "documentId") Integer documentId,
			@Param(value = "cellTable") String cellTable);

	void activeSheetTab(@Param(value = "documentId") Integer documentId,
			@Param(value = "tabId") Integer tabId);

	void copySheetTabFromDocumentId(
			@Param(value = "sourceDocumentId") Integer sourceDocumentId,
			@Param(value = "destDocumentId") Integer destDocumentId);

	List<Integer> findSheetTabIdByDocument(
			@Param(value = "documentId") Integer documentId);

	List<SheetTabIdOrderInfo> findSheetTabIdOrderInfoByDocument(
			@Param(value = "documentId") Integer documentId);

	void shiftSheetTab(
			@Param(value = "documentId") Integer documentId,
			@Param(value = "shiftTabs") List<SheetTabIdOrderInfo> shiftTabs);

	void addTabExtraInfo(
			@Param(value = "tabId") Integer tabId,
			@Param(value = "extraInfo") String extraInfo);
	
	int countTabByDocument(@Param(value = "documentId") Integer documentId);
	
	int getSize(@Param(value = "allTabs") List<SheetTab> allTabs,
			@Param(value = "cellTable") String cellTable);
	
	void updatePosAfter(@Param(value = "documentId") Integer documentId, 
			@Param(value = "prePos") Integer prePos,
			@Param(value = "curPos") Integer curPos);
	
	void updatePosBefore(@Param(value = "documentId") Integer documentId, 
			@Param(value = "prePos") Integer prePos,
			@Param(value = "curPos") Integer curPos);
	
	SheetTab getTabByDocumentAndIndex(@Param(value = "documentId") Integer documentId,@Param(value="index") Integer index);
	
	List<Integer> getAllTabIdsByDocument(Integer documentId);
	
	List<Integer> getOtherTabIdsByDocument(@Param(value = "documentId") Integer documentId, @Param(value = "excludeTabId") Integer excludeTabId);
	

}
