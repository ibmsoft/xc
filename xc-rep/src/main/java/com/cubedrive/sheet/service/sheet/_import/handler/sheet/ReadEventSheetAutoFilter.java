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
package com.cubedrive.sheet.service.sheet._import.handler.sheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilterColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilters;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;

public class ReadEventSheetAutoFilter {
	
	
	private CellRangeAddress _cellRangeAddress;
	
	private Map<Integer, FilterColumn> _filterColumns = new HashMap<>();
	
	private ReadEventSheetAutoFilter(){
		
	}
	
	public Map<String,Object> getAutoFilter(CellReference cellRef){
		if(_cellRangeAddress.isInRange(cellRef.getRow(), cellRef.getCol())){
			if(_cellRangeAddress.getFirstRow() == cellRef.getRow()){
				int colId = cellRef.getCol() - _cellRangeAddress.getFirstColumn();
				FilterColumn filterColumn =  _filterColumns.get(colId);
				List<String> filterValues = filterColumn != null ? filterColumn._filterValues : Collections.<String>emptyList();
				return createAutoFilterSetting(filterValues);
			}
		}
		return null;
	}
	
	
	private Map<String,Object> createAutoFilterSetting(List<String> filterValues){
		Map<String,Object> autoFilterMap = new HashMap<>();
		autoFilterMap.put("trigger", "ss-filter ss-trigger");
		List<Object> filter = new ArrayList<>();
		filter.add("");
		filter.add(1);
		filter.add(0);
		filter.add(_cellRangeAddress.getLastRow() - _cellRangeAddress.getFirstRow());
		filter.add(0);
		autoFilterMap.put("filter", JsonUtil.toJson(filter));
		Map<String,Object> filterCdt = new HashMap<>();
		if(filterValues.isEmpty()){
			filterCdt.put("type", "no-match");
		}else{
			filterCdt.put("type", "match");
		}
		filterCdt.put("values", filterValues);
		autoFilterMap.put("filterCdt", JsonUtil.toJson(filterCdt));
		return autoFilterMap;
	}
	
	private static class FilterColumn {
		
		private final int _colId;
		
		private final List<String> _filterValues = new ArrayList<>() ;
		
		private FilterColumn(int colId, List<CTFilter> ctFilters){
			this._colId = colId;
			for(CTFilter ctFilter:ctFilters){
				_filterValues.add(ctFilter.getVal());
			}
		}
		
	}
	
	public static ReadEventSheetAutoFilter build(SheetImportContext sheetImportContext){
		ReadEventSheetAutoFilter readEventSheetAutoFilter = null;
		CTWorksheet worksheet = sheetImportContext.sheet().worksheet();
		if(worksheet.isSetAutoFilter()){
			readEventSheetAutoFilter = new ReadEventSheetAutoFilter();
			CTAutoFilter ctAutoFilter = worksheet.getAutoFilter();
			readEventSheetAutoFilter._cellRangeAddress = CellRangeAddress.valueOf(ctAutoFilter.getRef());
			List<CTFilterColumn> ctFilterColumnList = ctAutoFilter.getFilterColumnList();
			if(ctFilterColumnList != null && !ctFilterColumnList.isEmpty()){
				for(CTFilterColumn ctFilterColumn: ctFilterColumnList){
					if(ctFilterColumn.isSetFilters() && !ctFilterColumn.isSetCustomFilters()){
						CTFilters ctFilters = ctFilterColumn.getFilters();
						FilterColumn filterColumn = new FilterColumn((int)ctFilterColumn.getColId(),ctFilters.getFilterList());
						readEventSheetAutoFilter._filterColumns.put(filterColumn._colId, filterColumn);
					}
				}
			}
		}
		return readEventSheetAutoFilter;
		
	}
	
	

}
