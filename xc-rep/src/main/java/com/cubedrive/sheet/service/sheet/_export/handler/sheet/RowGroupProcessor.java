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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.xssf.streaming.SXSSFRow;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTSheetFormatPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorksheet;

import com.cubedrive.sheet.service.sheet._export.ctx.XSSFWorksheetExportContext;

public class RowGroupProcessor extends WorksheetProcessor{
    
    private static Method setOutlineLevelMethod;
    static{
        try {
            setOutlineLevelMethod = SXSSFRow.class.getDeclaredMethod("setOutlineLevel", int.class);
            setOutlineLevelMethod.setAccessible(true);
        }  catch (Exception e) {
            setOutlineLevelMethod = null;
        }
    }
    
    private final List<Map<String,Object>> _rowGroups;
    
    private int _maxLevel = -1;
    
    private int _maxRowIndex = -1;
    
    private List<RowGroupUnit> _units;
    
    private RowGroupProcessor(List<Map<String,Object>> rowGroups, XSSFWorksheetExportContext xssfWorksheetExportContext){
        super(xssfWorksheetExportContext);
        _rowGroups = rowGroups;
    }
    
    private void init(){
        if(_rowGroups != null && _rowGroups.size() >0){
            int maxLevel = 0;
            int maxRowIndex = 0;
            List<RowGroupUnit> units = new ArrayList<>(_rowGroups.size());
            for(Map<String,Object> _rowGroup: _rowGroups){
                int level = (Integer)_rowGroup.get("level");
                List<Integer> span = (List<Integer>)_rowGroup.get("span");
                int min = span.get(0);
                int max = span.get(1);
                Object collapsed = _rowGroup.get("collapsed");
                boolean hidden = collapsed instanceof Integer ? true: false;
                if(level > maxLevel){
                    maxLevel = level;
                }
                if(max > maxRowIndex){
                    maxRowIndex = max;
                }
                RowGroupUnit unit = new RowGroupUnit(min,max,level,hidden);
                units.add(unit);
                
            }
            Collections.sort(units);
            for(int i= units.size()-1; i>=0; i--){
                RowGroupUnit unit = units.get(i);
                if(unit._marked)break;
                for(int j=i-1; j>=0; j--){
                    RowGroupUnit childUnit = units.get(j);
                    if(unit.contains(childUnit)){
                        childUnit._level = unit._level - childUnit._level + 1;
                        childUnit._marked = true;
                    }
                }
                unit._level = 1;
                unit._marked = true;
            }
            
            _maxLevel = maxLevel;
            _maxRowIndex = maxRowIndex;
            
            
            _units = units;
        }
    }
    
    
    @Override
    public void process() {
        if(_maxLevel != -1){
            CTWorksheet ctWorksheet = _xssfWorksheetExportContext.sheet().getXSSFSheet().getCTWorksheet();
            CTSheetFormatPr ctSheetFormatPr = ctWorksheet.isSetSheetFormatPr() ? ctWorksheet.getSheetFormatPr():ctWorksheet.addNewSheetFormatPr();
            ctSheetFormatPr.setOutlineLevelRow((short)_maxLevel);
            int lastRowIndex = 1;
            if(_xssfWorksheetExportContext.getLastSheetCell()!= null){
                lastRowIndex = _xssfWorksheetExportContext.getLastSheetCell().getX()+1;
            }
            int maxRowIndex = _maxRowIndex;
            if(maxRowIndex > lastRowIndex){
                for(int i = lastRowIndex; i<=maxRowIndex; i++){
                    SXSSFRow row = (SXSSFRow)_xssfWorksheetExportContext.sheet().createRow(i-1);
                    setRowOutlineLevel(row, i);
                }
            }
            
        }
    }
    
    public void setRowOutlineLevel(SXSSFRow row, int rowIndex){
        RowGroupUnit result = null;
        if(_units != null && !_units.isEmpty()){
            for(Iterator<RowGroupUnit> it = _units.iterator();it.hasNext();){
                RowGroupUnit unit = it.next();
                if(unit._min > rowIndex){
                    continue;
                }else if(rowIndex >= unit._min && rowIndex <= unit._max){
//                    outlineLevel = new RowOutlineLevel(unit._level, unit._hidden);
                    result = unit;
                    break;
                }else if(rowIndex > unit._max){
                    it.remove();
                }
            }
        }
        if(result != null && setOutlineLevelMethod!=null){
           try {
               setOutlineLevelMethod.invoke(row, result._level);
           } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
           }
           if(result._hidden){
               row.setHidden(true);
           }
        }
        
    }
    
    
    
    private static class RowGroupUnit implements Comparable<RowGroupUnit>{
        private int _min;
        private int _max;
        private int _level;
        private boolean _hidden;
        private boolean _marked = false;
        
        private RowGroupUnit(int min, int max, int level, boolean hidden){
            _min = min;
            _max = max;
            _level = level;
            _hidden = hidden;
        }

        @Override
        public int compareTo(RowGroupUnit other) {
            int result = _max - other._max;
            if(result == 0){
                result = _min < other._min ? 1:-1;
            }
            return result>0 ? 1:result==0 ? 0:-1;
        }
        
        
        public boolean contains(RowGroupUnit other){
            if(_min <= other._min && _max >= other._max){
                return true;
            }
            return false;
        }
        
        public boolean equals(Object o){
            if(o == this)return true;
            if(!(o instanceof RowGroupUnit))return false;
            RowGroupUnit other = (RowGroupUnit)o;
            return Objects.equals(_min, other._min) && Objects.equals(_level, other._level);
        }
        
        public int hashCode(){
            return Objects.hash(_min,_level);
        }
        
    }
 
    public static RowGroupProcessor build(List<Map<String,Object>> rowGroups,XSSFWorksheetExportContext xssfWorksheetExportContext){
        RowGroupProcessor rowGroupProcessor = new RowGroupProcessor(rowGroups,xssfWorksheetExportContext);
        rowGroupProcessor.init();
        return rowGroupProcessor;
    }

}
