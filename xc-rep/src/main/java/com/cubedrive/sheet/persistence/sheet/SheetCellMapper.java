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

import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.service.sheet.SheetCellService.SheetCellRangeCondition;

@Repository
public interface SheetCellMapper {

	SheetCell getCellById(@Param(value = "cellId") Integer cellId,
						  @Param(value = "cellTable") String cellTable);

	SheetCell getCellByTabAndXY(@Param(value = "tabId") Integer tabId,
								@Param(value = "x") Integer x,
								@Param(value = "y") Integer y,
								@Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellByColRange(@Param(value = "tabId") Integer tabId,
									  @Param(value = "startCol") Integer startCol,
									  @Param(value = "endCol") Integer endCol,
									  @Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellByRowRange(@Param(value = "tabId") Integer tabId,
									  @Param(value = "startRow") Integer startRow,
									  @Param(value = "endRow") Integer endRow,
									  @Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellByRange(@Param(value = "tabId") Integer tabId,
								   @Param(value = "startRow") Integer startRow,
								   @Param(value = "endRow") Integer endRow,
								   @Param(value = "startCol") Integer startCol,
								   @Param(value = "endCol") Integer endCol,
								   @Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellByRanges(@Param(value="ranges") List<SheetCellRangeCondition> ranges,
									@Param(value="cellId") Integer cellId,
									@Param(value="limit") Integer limit,
									@Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellOfRawDataByRanges(@Param(value="ranges") List<SheetCellRangeCondition> ranges,
											 @Param(value="cellId") Integer cellId,
											 @Param(value="limit") Integer limit,
											 @Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellOfStyleByRange(@Param(value="range") SheetCellRangeCondition range,
										  @Param(value="cellId") Integer cellId,
										  @Param(value="limit") Integer limit,
										  @Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellsBySearch(@Param(value = "tabIds") List<Integer> tabIds,
									 @Param(value = "query") String query,
									 @Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellsBySearch2(@Param(value = "tabIds") List<Integer> tabIds,
									  @Param(value = "query") String query,
									  @Param(value = "cellTable") String cellTable,
									  @Param(value="offset") Integer offset,
									  @Param(value = "limit") Integer limit);

	List<SheetCell> getCellsByRow(@Param(value = "tabId") Integer tabId,
								  @Param(value = "x") Integer x,
								  @Param(value = "cellTable") String cellTable);

	List<SheetCell> getCellsByCol(@Param(value = "tabId") Integer tabId,
								  @Param(value = "y") Integer y,
								  @Param(value = "cellTable") String cellTable);

	List<SheetCell> findCellsByTab(@Param(value = "tabId") Integer tabId,
								   @Param(value = "cellTable") String cellTable);

	List<SheetCell> findCalCellsByTab(@Param(value = "tabId") Integer tabId,
									  @Param(value = "cellTable") String cellTable);

	List<SheetCell> findCellsByTabOnExport(@Param(value = "tabId") Integer tabId,
										   @Param(value = "cellTable") String cellTable,
										   @Param(value="offset") Integer offset,
										   @Param(value = "limit") Integer limit);

	/**
	 * This is the action to get cells by row ...
	 *
	 * @param tabId
	 * @param cellTable
	 * @param offset
	 * @param loadOnceRows
	 * @return
	 */
	List<SheetCell> findCellsByRowsOnExport(@Param(value = "tabId") Integer tabId,
											@Param(value = "cellTable") String cellTable,
											@Param(value = "startRow") Integer startRow,
											@Param(value = "endRow") Integer endRow);

	List<SheetCell> findCellsByTabOrderXY(
			@Param(value = "tabId") Integer tabId,
			@Param(value = "cellTable") String cellTable);

	int countCell(@Param(value = "tabId") Integer tabId,
				  @Param(value = "x") Integer x,
				  @Param(value = "y") Integer y,
				  @Param(value = "cellTable") String cellTable);

	// this one is mainly for import part - with general tabId ...
	void batchInsertSheetCell(
			@Param(value = "tabId") Integer tabId,
			@Param(value = "cells") List<SheetCell> cells,
			@Param(value = "cellTable") String cellTable);

	// this one is based on the tab inside cells ...
	void batchInsertSheetCell2(
			@Param(value = "cells") List<SheetCell> cells,
			@Param(value = "cellTable") String cellTable);

	void batchInsertSheetCell3(
			@Param(value = "cells") List<SheetCell> cells,
			@Param(value = "cellTable") String cellTable);

	void batchDeleteSheetCell(
			@Param(value = "cells") List<SheetCell> cells,
			@Param(value = "cellTable") String cellTable);

	Integer insertSheetCell(@Param(value = "tabId") Integer tabId,
							@Param(value = "cell") SheetCell cell,
							@Param(value = "cellTable") String cellTable);

	void updateSheetCell(@Param(value = "cell") SheetCell cell,
						 @Param(value = "cellTable") String cellTable);

	void updateCellContent(@Param(value = "cellId") Integer cellId,
						   @Param(value = "content") String content,
						   @Param(value = "data") String data,
						   @Param(value = "cal") Boolean cal,
						   @Param(value = "cellTable") String cellTable);

	void updateContentOnly(@Param(value = "cellId") Integer cellId,
						   @Param(value = "content") String content,
						   @Param(value = "cellTable") String cellTable);

	void deleteById(@Param(value = "cellId") Integer cellId,
					@Param(value = "cellTable") String cellTable);

	void deleteByXY(@Param(value = "tabId") Integer tabId,
					@Param(value = "x") Integer x,
					@Param(value = "y") Integer y,
					@Param(value = "cellTable") String cellTable);

	void deleteCellsBySheetTab(@Param(value = "tabId") Integer tabId,
							   @Param(value = "cellTable") String cellTable);

	void insertRows(@Param(value = "tabId") Integer tabId,
					@Param(value = "rowNum") Integer rowNum,
					@Param(value = "rowSpan") Integer rowSpan,
					@Param(value = "cellTable") String cellTable);

	void insertColumns(@Param(value = "tabId") Integer tabId,
					   @Param(value = "columnNum") Integer columnNum,
					   @Param(value = "colSpan") Integer colSpan,
					   @Param(value = "cellTable") String cellTable);

	void deleteCellsXXYY(@Param(value = "tabId") Integer tabId,
						 @Param(value = "startX") Integer startX,
						 @Param(value = "endX") Integer endX,
						 @Param(value = "startY") Integer startY,
						 @Param(value = "endY") Integer endY,
						 @Param(value = "cellTable") String cellTable);

	void deleteRowStart2End(@Param(value = "tabId") Integer tabId,
							@Param(value = "startX") Integer startX,
							@Param(value = "endX") Integer endX,
							@Param(value = "cellTable") String cellTable);

	void updateRowsWithSpan(@Param(value = "tabId") Integer tabId,
							@Param(value = "totalSpan") Integer totalSpan,
							@Param(value = "endX") Integer endX,
							@Param(value = "cellTable") String cellTable);

	void deleteColStart2End(@Param(value = "tabId") Integer tabId,
							@Param(value = "startY") Integer startY,
							@Param(value = "endY") Integer endY,
							@Param(value = "cellTable") String cellTable);

	void updateColsWithSpan(@Param(value = "tabId") Integer tabId,
							@Param(value = "totalSpan") Integer totalSpan,
							@Param(value = "endY") Integer endY,
							@Param(value = "cellTable") String cellTable);

	void updateRowsWithSpanYY(@Param(value = "tabId") Integer tabId,
							  @Param(value = "rowSpan") Integer rowSpan,
							  @Param(value = "maxrow") Integer maxrow,
							  @Param(value = "mincol") Integer mincol,
							  @Param(value = "maxcol") Integer maxcol,
							  @Param(value = "cellTable") String cellTable);

	void updateColsWithSpanXX(@Param(value = "tabId") Integer tabId,
							  @Param(value = "colSpan") Integer colSpan,
							  @Param(value = "minrow") Integer minrow,
							  @Param(value = "maxrow") Integer maxrow,
							  @Param(value = "maxcol") Integer maxcol,
							  @Param(value = "cellTable") String cellTable);

	void updateRowsAddSpanYY(@Param(value = "tabId") Integer tabId,
							 @Param(value = "rowSpan") Integer rowSpan,
							 @Param(value = "minrow") Integer minrow,
							 @Param(value = "mincol") Integer mincol,
							 @Param(value = "maxcol") Integer maxcol,
							 @Param(value = "cellTable") String cellTable);

	void updateColsAddSpanXX(@Param(value = "tabId") Integer tabId,
							 @Param(value = "colSpan") Integer colSpan,
							 @Param(value = "minrow") Integer minrow,
							 @Param(value = "maxrow") Integer maxrow,
							 @Param(value = "mincol") Integer mincol,
							 @Param(value = "cellTable") String cellTable);

	void copySheetCellFromSheet(
			@Param(value = "sourceSpreadTabId") Integer sourceSpreadTabId,
			@Param("destSpreadTabId") Integer destSpreadTabId,
			@Param("sourceCellTable") String sourceCellTable, @Param("destCellTable") String destCellTable);

	void copySheetCellFromSheetInDiffTable(
			@Param(value = "sourceSpreadTabId") Integer sourceSpreadTabId,
			@Param("destSpreadTabId") Integer destSpreadTabId,
			@Param("sourceCellTable") String sourceCellTable,
			@Param("destCellTable") String destCellTable);

	void updateCellRows(@Param(value = "tabId") Integer tabId,
						@Param(value = "cellTable") String cellTable,
						@Param(value = "orgX") Integer orgX,
						@Param(value = "newX") Integer newX);

	void updateCellRowsMore(@Param(value = "tabId") Integer tabId,
							@Param(value = "cellTable") String cellTable,
							@Param(value = "orgX") Integer orgX,
							@Param(value = "newX") Integer newX,
							@Param(value = "mincol") Integer mincol,
							@Param(value = "maxcol") Integer maxcol);

	List<SheetCell> findFirstRowByTab(@Param(value="tabId")Integer tabId,@Param(value = "cellTable") String cellTable);

	List<SheetCell> findCellIdsByTab(@Param(value = "tabId") Integer tabId,
									 @Param(value = "cellTable") String cellTable,
									 @Param(value = "orgX") Integer orgX);

	List<SheetCell> findCellIdsByTabMore(@Param(value = "tabId") Integer tabId,
										 @Param(value = "cellTable") String cellTable,
										 @Param(value = "orgX") Integer orgX,
										 @Param(value = "mincol") Integer mincol,
										 @Param(value = "maxcol") Integer maxcol);

	void batchUpdateXByIds(
			@Param(value = "cellIds") List<SheetCell> cellIds,
			@Param(value = "cellTable") String cellTable,
			@Param(value = "updatedX") Integer updatedX);

	int countOnExport(@Param(value = "tabId") Integer tabId,
					  @Param(value = "cellTable") String cellTable);

	Integer getMaxY(@Param(value = "tabId") Integer tabId,
					@Param(value = "cellTable") String cellTable);

	Integer getMaxX(@Param(value = "tabId") Integer tabId,
					@Param(value = "cellTable") String cellTable);

	Long countByTabId(@Param(value="tabId")Integer tabId,
					  @Param(value="cellTable")String cellTable);

	/**
	 * This function will load all cell in this tab (tabId)
	 * and call calculate is true in other tabs
	 * @param startCellId
	 * @param tabId
	 * @param size
	 * @param cellTable
	 * @param tabIds
	 * @return
	 */
	List<SheetCell> loadCellsOnDemand(
			@Param(value="startCellId")Integer startCellId,
			@Param(value="tabId")Integer tabId,
			@Param(value="size")Integer size,
			@Param(value="cellTable")String cellTable,
			@Param(value = "tabIds") List<Integer> tabIds);
	
	List<SheetCell> loadDocCellsOnDemand(
			@Param(value="startCellId")Integer startCellId,			
			@Param(value="size")Integer size,
			@Param(value="cellTable")String cellTable,
			@Param(value = "tabIds") List<Integer> tabIds);

	List<SheetCell> loadCornerCellsOnDemand(
			@Param(value="startCellId")Integer startCellId,
			@Param(value="tabId")Integer tabId,
			@Param(value="size")Integer size,
			@Param(value="cellTable")String cellTable);

	List<SheetCell> loadCornerCalCellsOnDemand(
			@Param(value="startCellId")Integer startCellId,
			@Param(value="tabId")Integer tabId,
			@Param(value="size")Integer size,
			@Param(value="cellTable")String cellTable,
			@Param(value = "tabIds") List<Integer> tabIds);

	List<SheetCell> loadCellsOfRawDataOnDemand(
			@Param(value="startCellId")Integer startCellId,
			@Param(value="tabId")Integer tabId,
			@Param(value="size")Integer size,
			@Param(value="cellTable")String cellTable,
			@Param(value = "tabIds") List<Integer> tabIds);



	List<SheetCell> loadNotCalCellsOnDemand(
			@Param(value="startCellId")Integer startCellId,
			@Param(value="tabId")Integer tabId,
			@Param(value="size")Integer size,
			@Param(value="cellTable")String cellTable);

	List<SheetCell> loadNotCalCellsOfRawDataOnDemand(
			@Param(value="startCellId")Integer startCellId,
			@Param(value="tabId")Integer tabId,
			@Param(value="size")Integer size,
			@Param(value="cellTable")String cellTable);



	// this will get all cal is true cell in not active sheet ...
	List<SheetCell> findDeactiveCalCells(
			@Param(value = "tabIds") List<Integer> tabIds,
			@Param(value = "cellTable") String cellTable);

	List<SheetCell> getLeftTopCells(
			@Param(value = "tabId") Integer tabId,
			@Param(value = "cellTable") String cellTable);

	List<SheetCell> getLeftTopCellsOfRawData(
			@Param(value = "tabId") Integer tabId,
			@Param(value = "cellTable") String cellTable);

	List<SheetCell> getCalCells(
			@Param(value = "tabIds") List<Integer> tabIds,
			@Param(value="startCellId")Integer startCellId,
			@Param(value="size")Integer size,
			@Param(value = "cellTable") String cellTable);

	List<SheetCell> getCalCellsOfRawData(
			@Param(value = "tabIds") List<Integer> tabIds,
			@Param(value="startCellId")Integer startCellId,
			@Param(value="size")Integer size,
			@Param(value = "cellTable") String cellTable);



	// the following 3 method are for sort span ...
	void updateRowDiff(@Param(value = "tabId") Integer tabId,
					   @Param(value = "key") Integer key,
					   @Param(value = "val") Integer val,
					   @Param(value = "cellTable") String cellTable);

	void updateCellsDiff(@Param(value = "tabId") Integer tabId,
						 @Param(value = "key") Integer key,
						 @Param(value = "val") Integer val,
						 @Param(value = "mincol") Integer mincol,
						 @Param(value = "maxcol") Integer maxcol,
						 @Param(value = "cellTable") String cellTable);

	void upDownCell(@Param(value = "tabId") Integer tabId,
					@Param(value = "cellTable") String cellTable);

	Integer getTabCellCount(@Param(value = "tabId") Integer tabId,
			@Param(value = "cellTable") String cellTable);

	List<SheetCell> loadNonDataCellOfStyle(@Param(value = "tabId") Integer tabId,@Param(value = "cellTable") String cellTable);
	
	List<SheetCell> loadAllCellsByDocument(@Param(value="startCellId")Integer startCellId,
            @Param(value="size")Integer size,
            @Param(value="cellTable")String cellTable,
            @Param(value = "tabIds") List<Integer> tabIds);
	
	List<SheetCell> loadAllCellsByTab(@Param(value="startCellId")Integer startCellId,
            @Param(value="size")Integer size,
            @Param(value="cellTable")String cellTable,
            @Param(value="tabId")Integer tabId);
	
	List<SheetCell> loadAllCellsByTabAndCalCells(@Param(value="startCellId")Integer startCellId,
            @Param(value="size")Integer size,
            @Param(value="cellTable")String cellTable,
            @Param(value="tabId")Integer tabId,
            @Param(value = "tabIds") List<Integer> tabIds);
	
}
