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
package com.cubedrive.sheet.service.sheet._export.handler.sheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.helpers.ColumnHelper;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;

public class ColumnGroupProcessor extends WorksheetProcessor{
	
	private final List<Map<String,Object>> _colGroups;
	
	private final CTWorksheet _ctWorksheet;
	
	private final ColumnHelper _columnHelper;
	
	private ColumnGroupProcessor(List<Map<String,Object>> colGroups, XSSFWorksheetExportContext xssfWorksheetExportContext){
		super(xssfWorksheetExportContext);
		_colGroups = colGroups;
		_ctWorksheet = _xssfWorksheetExportContext.sheet().getXSSFSheet().getCTWorksheet();
		_columnHelper = new ColumnHelper(_ctWorksheet);
	}
	
	@Override
	public void process() {
		if(_colGroups != null && _colGroups.size()>0){
			List<ColumnGroupUnit> units = new ArrayList<>();
			for(Map<String,Object> _colGroup: _colGroups){
				int level = (Integer)_colGroup.get("level");
				List<Integer> span = (List<Integer>)_colGroup.get("span");
				int min = span.get(0);
				int max = span.get(1);
				units.add(new ColumnGroupUnit(min,max,level));
			}
			Collections.sort(units);
			CTCols ctCols= _ctWorksheet.getColsList().get(0);
			for(ColumnGroupUnit unit:units){
				groupColumn(unit._min, unit._max, ctCols);
			}
			_ctWorksheet.setColsArray(0,ctCols);
			setSheetFormatPrOutlineLevelCol(ctCols);
		}
		
	}
	
	private void groupColumn(int fromColumn, int toColumn, CTCols ctCols) {
		
        CTCol ctCol=CTCol.Factory.newInstance();
        CTCol fixCol_before = _columnHelper.getColumn1Based(toColumn, false);
        if (fixCol_before != null) {
            fixCol_before = (CTCol)fixCol_before.copy();
        }

        ctCol.setMin(fromColumn);
        ctCol.setMax(toColumn);
        _columnHelper.addCleanColIntoCols(ctCols, ctCol);

        CTCol fixCol_after = _columnHelper.getColumn1Based(toColumn, false);
        if (fixCol_before != null && fixCol_after != null) {
            _columnHelper.setColumnAttributes(fixCol_before, fixCol_after);
        }

        for(int index=fromColumn;index<=toColumn;index++){
            CTCol col=_columnHelper.getColumn1Based(index, false);
            short outlineLevel=col.getOutlineLevel();
            col.setOutlineLevel((short)(outlineLevel+1));
            index=(int)col.getMax();
        }
        
    }
	
	private void setSheetFormatPrOutlineLevelCol(CTCols ctCols){
        short outlineLevel = 0;
        for (CTCol col : ctCols.getColList()) {
            outlineLevel = col.getOutlineLevel() > outlineLevel ? col.getOutlineLevel() : outlineLevel;
        }
        CTSheetFormatPr ctSheetFormatPr = _ctWorksheet.isSetSheetFormatPr() ? _ctWorksheet.getSheetFormatPr():_ctWorksheet.addNewSheetFormatPr();
        ctSheetFormatPr.setOutlineLevelCol(outlineLevel);
	}
	
	private static class ColumnGroupUnit implements Comparable<ColumnGroupUnit>{
		
		private int _min;
		private int _max;
		private int _level;
		
		private ColumnGroupUnit(int min, int max, int level){
			_min = min;
			_max = max;
			_level = level;
		}

		@Override
		public int compareTo(ColumnGroupUnit other) {
			int result = _max - other._max;
			if(result == 0){
				result = _min < other._min ? 1:-1;
			}
			return  result > 0 ? 1 : result == 0 ? 0 :-1;
		}
		
	}
	
	public static ColumnGroupProcessor build(List<Map<String,Object>> colGroups,XSSFWorksheetExportContext xssfWorksheetExportContext){
		ColumnGroupProcessor columnGroupProcessor = new ColumnGroupProcessor(colGroups,xssfWorksheetExportContext);
		return columnGroupProcessor;
	}

}
