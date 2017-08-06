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

import com.cubedrive.sheet.service.sheet._import.ctx.SheetImportContext;
import com.cubedrive.sheet.service.sheet._import.xssf.XSSFReadEventRow;

public class ReadEventSheetRowGroup {
	
	private SheetImportContext _sheetImportContext;
	
	private List<RowGroup> _groups = new ArrayList<>();
	
	private RowGroup _current;
	
	private ReadEventSheetRowGroup(){
		
	}
	
	public void register(XSSFReadEventRow row){
		Integer rowOutlineLevel = row.outlineLevel();
		if(rowOutlineLevel != null){
			if(_current == null){
				newRowGroup();
			}
			
			if(_current._maxLevel < rowOutlineLevel){
				_current._maxLevel = rowOutlineLevel;
			}
			
			int rowIndex = row.rowIndex();
			List<RowGroupUnit> levelUnit = _current._levelUnits.get(rowOutlineLevel);
			if(levelUnit == null){
				levelUnit = new ArrayList<RowGroupUnit>();
				_current._levelUnits.put(rowOutlineLevel, levelUnit);
			}
			RowGroupUnit unit = null;
			for(RowGroupUnit _unit:levelUnit){
				if(_unit.isContinuous(rowIndex)){
					unit = _unit;
					break;
				}
			}
			if(unit ==null){
				unit = new RowGroupUnit(rowIndex,rowOutlineLevel,row.hidden());
				levelUnit.add(unit);
			}else{
				unit.enlarge(rowIndex);
			}
			
		}else{
			if(_current != null){
				_current = null;
			}
		}
		
	}
	
	private void newRowGroup(){
		RowGroup group = new RowGroup();
		_groups.add(group);
		_current = group;
	}
	
	public List<Map<String,Object>> getGroups(){
		List<Map<String,Object>> result = new ArrayList<>();
		for(RowGroup _group: _groups){
			if(!_group._levelUnits.isEmpty()){
				List<RowGroupUnit> units = _group.toGroup();
				for(RowGroupUnit unit: units){
					result.add(unit.toMap(_group._maxLevel));
				}
			}
		}
		return result;
	} 
	
	private static class RowGroup {
		private int _maxLevel = 0;
		private Map<Integer,List<RowGroupUnit>> _levelUnits = new HashMap<>();
		
		private RowGroup(){}
		
		private List<RowGroupUnit> toGroup(){
			List<RowGroupUnit> units = new ArrayList<>();
			for(List<RowGroupUnit> levelUnit:_levelUnits.values()){
				units.addAll(levelUnit);
			}
			
			Collections.sort(units);
			int unitSize = units.size();
			for(int i=0; i<unitSize-1; i++){
				RowGroupUnit unit = units.get(i);
				for(int j=i+1; j<unitSize; j++){
					RowGroupUnit parent = units.get(j);
					if(parent.isAdjacent(unit)){
						parent.expand(unit);
					}
				}
			}
			return units;
		}
	}
	
	
	private static class RowGroupUnit implements Comparable<RowGroupUnit>{
		private int _min;
		private int _max ;
		private int _level = -1;
		private boolean _collapsed;
		
		private RowGroupUnit(int min, int level, boolean collapsed){
			this._max = this._min =min;
			this._level = level;
			this._collapsed = collapsed;
		}
		
		private boolean isContinuous(int rowIndex){
			if(_max+1 == rowIndex){
				return true;
			}
			return false;
		}
		
		private void enlarge(int rowIndex){
			_max = rowIndex;
		}
		
		private boolean isAdjacent(RowGroupUnit other){
			boolean adjacent = false;
			if(other._max < this._max && other._max +1 == this._min){
				adjacent = true;
			}else if(other._max > this._max && this._max +1 == other._min){
				adjacent = true;
			}
			return adjacent;
		}
		
		private void expand(RowGroupUnit other){
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
			int start = _min-1;
			span.add(start);
			span.add(_max);
			map.put("span", span);
			if(_collapsed){
				map.put("collapsed", start);
			}
			return map;
		}

		@Override
		public int compareTo(RowGroupUnit other) {
			int result = this._level - other._level;
			return result > 0 ? -1 : result ==0 ? 0 : 1;
		}
	}
	
	public static ReadEventSheetRowGroup build(SheetImportContext sheetImportContext){
		ReadEventSheetRowGroup readEventSheetRowGroup = new ReadEventSheetRowGroup();
		readEventSheetRowGroup._sheetImportContext = sheetImportContext;
		return readEventSheetRowGroup;
	}

}
