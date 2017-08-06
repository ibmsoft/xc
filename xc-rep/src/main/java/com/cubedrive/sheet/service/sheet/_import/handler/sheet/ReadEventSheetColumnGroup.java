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

import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCol;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCols;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventSheet;

public class ReadEventSheetColumnGroup {
	
	
	private List<ColumnGroup> _groups = new ArrayList<>();
	
	private ColumnGroup _current ;
	
	private ReadEventSheetColumnGroup(){
	}
	
	private void registerColumn(CTCol ctCol){
		boolean _isSetOutlineLevel = ctCol.isSetOutlineLevel();
		if(_isSetOutlineLevel){
			if(_current == null){
				newColumnGroup();
			}
			
			boolean hidden = ctCol.isSetHidden() ? ctCol.getHidden():false;
			ColumnGroupUnit unit = new ColumnGroupUnit((int)ctCol.getMin(),(int)ctCol.getMax(),ctCol.getOutlineLevel(), hidden);
			_current._units.add(unit);
			if(unit._level > _current._maxLevel){
				_current._maxLevel = unit._level;
			}
		}else{
			if(_current != null){
				_current = null;
			}
		}
	}
	
	private void newColumnGroup(){
		ColumnGroup group = new ColumnGroup();
		_groups.add(group);
		_current = group;
	}
	
	public List<Map<String,Object>> getGroups(){
		List<Map<String,Object>> result = new ArrayList<>();
		for(ColumnGroup _group: _groups){
			if(!_group._units.isEmpty()){
				List<ColumnGroupUnit> units = _group.toGroup();
				for(ColumnGroupUnit unit:units){
					result.add(unit.toMap(_group._maxLevel));
				}
			}
		}
		return result;
	}
	
	
	private static class ColumnGroup{
		
		private int _maxLevel =0;
		
		private List<ColumnGroupUnit> _units = new ArrayList<>();
		
		private ColumnGroup(){}
		
		
		private List<ColumnGroupUnit> toGroup(){
			List<ColumnGroupUnit> units = new ArrayList<>(_units);
			Collections.sort(units);
			int unitSize = units.size();
			for(int i=0; i<unitSize-1; i++){
				ColumnGroupUnit unit = units.get(i);
				for(int j=i+1; j<unitSize; j++){
					ColumnGroupUnit parent = units.get(j);
					if(parent.isAdjacent(unit)){
						parent.expand(unit);
					}
				}
			}
			return units;
		}
		
		
	}
	
	private static class ColumnGroupUnit implements Comparable<ColumnGroupUnit> {
		private int _min;
		private int _max;
		private int _level;
		private boolean _collapsed;
		
		private ColumnGroupUnit(int min, int max, int level, boolean collapsed){
			_min = min;
			_max = max;
			_level = level;
			_collapsed = collapsed;
		}
		
		private boolean isAdjacent(ColumnGroupUnit other){
			boolean adjacent = false;
			if(other._max < this._max && other._max +1 == this._min){
				adjacent = true;
			}else if(other._max > this._max && this._max +1 == other._min){
				adjacent = true;
			}
			return adjacent;
		}
		
		private void expand(ColumnGroupUnit other){
			if(this._level + 1 == other._level){
				if(other._max < this._max && other._max +1 == this._min){
					this._min = other._min;
				}else if(other._max > this._max && this._max +1 == other._min){
					this._max = other._max;
				}
			}
		}
		
		private Map<String,Object> toMap(int maxLevel){
			Map<String,Object> map = new HashMap<>();
			map.put("level", (maxLevel -_level)+1);
			List<Integer> span = new ArrayList<>();
			span.add(_min);
			span.add(_max);
			map.put("span", span);
			if(_collapsed){
				map.put("collapsed", _min);
			}
			return map;
		}

		@Override
		public int compareTo(ColumnGroupUnit other) {
			int result = this._level - other._level;
			return result > 0 ? -1 : result ==0 ? 0 : 1;
		}
		
		
	}

	
	public static ReadEventSheetColumnGroup build(SheetImportContext sheetImportContext){
		ReadEventSheetColumnGroup readEventSheetColumnGroup = new ReadEventSheetColumnGroup();
		XSSFReadEventSheet readEventSheet = sheetImportContext.sheet();
        CTWorksheet worksheet = readEventSheet.worksheet();
        if(!worksheet.getColsList().isEmpty()){
        	CTCols ctCols = worksheet.getColsList().get(0);
        	for (CTCol ctCol : ctCols.getColList()) {
        		readEventSheetColumnGroup.registerColumn(ctCol);
        	}
        }
        return readEventSheetColumnGroup;
	}
}
