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
package com.cubedrive.sheet.service.sheet._export.ctx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.codehaus.jackson.type.TypeReference;

import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.service.sheet._export.SheetExportEnvironment;
import com.cubedrive.sheet.service.sheet._export.handler.cell.ConfigCellProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.sheet.MergeCellProcessor;
import com.cubedrive.sheet.service.sheet._export.handler.sheet.RowGroupProcessor;
import com.google.common.base.Strings;

public class XSSFWorksheetExportContext extends SSExportContext {

    private final XSSFWorkbookExportContext _workbookExportContext;

    private final SheetTab _sheetTab;

    private final int _index;

    private SXSSFSheet _sheet;
    
    private final Map<Integer,ColumnSetting> _cols = new HashMap<>();
    
    private final Map<Integer,Map<String,Object>> _colStyleSettings = new HashMap<>();
    
    private List<ColumnSetting> _sortedColumnSettingList ;
    
    private final Map<Integer,Integer> _rows = new HashMap<>();
    
    private SXSSFRow _currentRow;
    
    private SheetCell _lastSheetCell;
    
    private final Map<String,TableInfo> _tables = new HashMap<>();
    
    private boolean _sheetProtection = false;
    
    private boolean _lockedAll = false;
    
    private final Set<Integer> _lockedRows = new HashSet<>();
    
    private final Set<Integer> _lockedColumns = new HashSet<>();
    
    private final Set<Integer> _noLockedRows = new HashSet<>();
    
    private final Set<Integer> _noLockedColumns = new HashSet<>();
    
    private RowGroupProcessor _rowGroupProcessor = null;
    
    private MergeCellProcessor _mergeCellProcessor = null;
    
    private final List<Map<String,Object>> _miniCharts = new ArrayList<>();
    
    private ConfigCellProcessor _configCellProcessor;
    
    private int _nextCol = 1;
    
    private RowSetting _rowSetting;
    
    private Map<String,Object> _sheetStyleSetting = Collections.emptyMap() ;

    private HiddenInfo _rowHiddens = null;

    private HiddenInfo _colHiddens = null;

    public XSSFWorksheetExportContext(
            SheetExportEnvironment sheetExportEnvironment,
            XSSFWorkbookExportContext workbookExportContext, SheetTab sheetTab,
            int index) {
        super(sheetExportEnvironment);
        _workbookExportContext = workbookExportContext;
        _sheetTab = sheetTab;
        _index = index;
    }
    
    public void init(){
    	loadRowGroupElements();
    	loadMergeCells();
        loadRowHidden();
    }
    
    private void loadRowGroupElements(){
    	 List<SheetTabElement> rowGroupElements = _sheetExportEnvironment
                 .sheetTabElementMapper().findElementsByTabAndType(_sheetTab.getId(), "rowgroup");
         if(! rowGroupElements.isEmpty()){
             List<Map<String,Object>> rowGroups = JsonUtil.fromJson(rowGroupElements.get(0).getContent(),
                     new TypeReference<List<Map<String,Object>>>(){});
             if(rowGroups != null && !rowGroups.isEmpty()){
                 _rowGroupProcessor = RowGroupProcessor.build(rowGroups, this);
             }
         }
    }
    
    private void loadMergeCells(){
    	List<SheetTabElement> mergeCellElements = null;
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("tabId",_sheetTab.getTabId());
    	map.put("eType", "meg");
    	//findTabElementById  -- 云ERP导出时添加的方法
        mergeCellElements = _sheetExportEnvironment.sheetTabElementMapper().findTabElementById(map);
        List<List<Integer>> mergeCellConfigList = new ArrayList<>();
        for(SheetTabElement mergeCellElement:mergeCellElements){
            List<Integer> mergeCellConfig = JsonUtil.parseArray(mergeCellElement.getContent(), Integer.class);
            mergeCellConfigList.add(mergeCellConfig);
        }
        if(!mergeCellConfigList.isEmpty()){
            _mergeCellProcessor = MergeCellProcessor.build(mergeCellConfigList, this);
        }
        
    }

    private void loadRowHidden(){
        List<SheetTabElement> rowHiddens = _sheetExportEnvironment
                .sheetTabElementMapper().getElementsByName(_sheetTab.getId(), "row", "rowHiddens");
        if(!rowHiddens.isEmpty()){
            String rangesJson = rowHiddens.get(0).getContent();
            List<int[]> ranges = JsonUtil.fromJson(rangesJson, new TypeReference<List<int[]>>(){});
            if(!ranges.isEmpty()){
                _rowHiddens = new HiddenInfo(ranges);
            }
        }

        List<SheetTabElement> colHiddens = _sheetExportEnvironment
                .sheetTabElementMapper().getElementsByName(_sheetTab.getId(), "col", "colHiddens");
        if(!colHiddens.isEmpty()){
            String rangesJson = colHiddens.get(0).getContent();
            List<int[]> ranges = JsonUtil.fromJson(rangesJson, new TypeReference<List<int[]>>(){});
            if(!ranges.isEmpty()){
                _colHiddens= new HiddenInfo(ranges);
            }
        }
    }

    public SheetTab sheetTab() {
        return _sheetTab;
    }

    public int index() {
        return _index;
    }

    public void setSheet(SXSSFSheet sheet) {
        _sheet = sheet;
    }

    public SXSSFSheet sheet() {
        return _sheet;
    }
    
    public Map<Integer,ColumnSetting> cols(){
        return _cols;
    }
    
    public SXSSFRow getCurrentRow(){
        return _currentRow;
    }
    
    public void setCurrentRow(SXSSFRow row){
        _currentRow = row; 
    }
    
    public SheetCell getLastSheetCell(){
        return _lastSheetCell;
    }

    public void setLastSheetCell(SheetCell sheetCell){
        _lastSheetCell = sheetCell;
    }
    
    public RowGroupProcessor getRowGroupProcessor(){
        return _rowGroupProcessor;
    }
    
    public MergeCellProcessor getMergeProcessor(){
        return _mergeCellProcessor;
    }
    
    public void setConfigCellProcessor(ConfigCellProcessor configCellProcessor){
    	_configCellProcessor = configCellProcessor;
    }
    
    public ConfigCellProcessor getConfigCellProcessor(){
    	return _configCellProcessor;
    }
    
    public int getRowHeight(Integer rowIndex){
        Integer rowHeight = _rows.get(rowIndex);
        if(rowHeight == null){
            rowHeight = getDefaultRowHeight();
        }
        return rowHeight;
    }
    
    public void setRowHeight(int rowIndex,Integer rowHeight){
        _rows.put(rowIndex, rowHeight);
    }
    
    public int getColumnWidth(Integer columnIndex){
        ColumnSetting columnSetting = _cols.get(columnIndex);
        if(columnSetting == null){
            for(ColumnSetting _columnSetting:_sortedColumnSettingList){
                if(_columnSetting.min > columnIndex)
                    break;
                if(_columnSetting.min <= columnIndex && columnIndex <=  _columnSetting.max){
                    columnSetting = _columnSetting;
                    break;
                }
            }
        }
        if(columnSetting != null){
            if(columnSetting.isHidden())
                return 0;
            else
                return columnSetting.getWidth();
        }else{
            return getDefaultColumnWidth();
        }
    }
    
    public int getColumnWidthCumulateUntil(int untilColumn){
    	int i = 1;
    	int columnWidthCumulate = 0;
    	int columnWidth;
    	while(i < untilColumn){
    		columnWidth = getColumnWidth(i);
    		columnWidthCumulate += columnWidth;
    		i++;
    	};
    	return columnWidthCumulate;
    	
    }
    
    public int[] getColumnOffset(int columnPosPx){
        int i =1;
        int currentPx =0;
        int columnIndex = 0;
        int columnWidth;
        do{
             columnWidth = getColumnWidth(i);
             if(columnWidth ==0){
                 columnIndex ++;
             }else{
                 if((currentPx +columnWidth) > columnPosPx)
                     break;
                 else
                     columnIndex ++;
             }
             currentPx += columnWidth;
             i++;
        }while(true);
        int rest = columnPosPx - currentPx ;
        return new int[]{columnIndex,rest};
    }
    
    public int getRowHeightCumulateUntil(int untilRow){
    	int i = 1;
    	int rowHeightCumulate = 0;
    	int rowHeight;
    	while(i < untilRow){
    		rowHeight = getRowHeight(i);
    		rowHeightCumulate += rowHeight;
    		i++;
    	};
    	return rowHeightCumulate;
    	
    }
    
    public int[] getRowOffset(int rowPosPx){
        int i =1;
        int currentPx =0;
        int rowIndex = 0;
        int rowHeight;
        do{
            rowHeight = getRowHeight(i);
             if(rowHeight ==0){
                 rowIndex ++;
             }else{
                 if((currentPx + rowHeight) > rowPosPx)
                     break;
                 else
                     rowIndex ++;
             }
             currentPx += rowHeight;
             i++;
        }while(true);
        int rest = rowPosPx - currentPx ;
        return new int[]{rowIndex,rest};
    }
    
    public int getDefaultColumnWidth(){
        Integer defaultColumnWidht = (Integer)getVariable("defaultColumnWidth");
        if(defaultColumnWidht == null)
            return 0;
        return defaultColumnWidht.intValue();
    }
    
    public int getDefaultRowHeight(){
        Integer defaultRowHeight = (Integer)getVariable("defaultRowHeight");
        if(defaultRowHeight == null)
            return 0;
        return defaultRowHeight.intValue();
    }
    
    public void setDefaultColumnWidthAndRowHeight(int defaultColumnWidth,int defaultRowHeight){
        putVariable("defaultColumnWidth",defaultColumnWidth);
        putVariable("defaultRowHeight",defaultRowHeight);
    }
    
    public Map<String,Object> getColumnStyleSetting(int columnIndex){
    	return _colStyleSettings.get(columnIndex);
    }
    
    public void putColumnStyleSetting(int columnIndex, Map<String,Object> columnStyleSetting){
    	_colStyleSettings.put(columnIndex, columnStyleSetting);
    }
    
    public void lockAll(){
    	this._sheetProtection = true;
    	this._lockedAll = true;
    }
    
    public boolean isLockedAll(){
    	return _lockedAll;
    }
    
    public void lockCell(SheetCell sheetCell){
    	if(!_sheetProtection)
    		_sheetProtection = true;
    	
    	if(sheetCell.getX()==0 && sheetCell.getY()!=0){
    		_lockedColumns.add(sheetCell.getY());
    	}else if(sheetCell.getX()!=0 && sheetCell.getY()==0){
    		_lockedRows.add(sheetCell.getX());
    	}
    }
    
    public boolean isRowLocked(int rowIndex){
    	if(_sheetProtection)
    		return _lockedRows.contains(rowIndex);
    	return false;
    }
    
    public boolean isColumnLocked(int columnIndex){
    	if(_sheetProtection)
    		return _lockedColumns.contains(columnIndex);
    	return false;
    }
    
    public void addNoLockedRow(int rowIndex){
    	_noLockedRows.add(rowIndex);
    }
    
    public boolean isRowNoLocked(int rowIndex){
    	return _noLockedRows.contains(rowIndex);
    }
    
    public void addNoLockedColumn(int columnIndex){
    	_noLockedColumns.add(columnIndex);
    }
    
    public boolean isColumnNoLocked(int columnIndex){
    	return _noLockedColumns.contains(columnIndex);
    }
    
    
    public boolean isSheetNeedProtection(){
    	return _sheetProtection;
    }
    
    public List<ColumnSetting> getSortedColumnSettings(){
        if(_sortedColumnSettingList == null){
            List<ColumnSetting> columnSettingList = new ArrayList<>(_cols.values());
            Collections.sort(columnSettingList);
            _sortedColumnSettingList = columnSettingList;
        }
        return _sortedColumnSettingList;
    }
    
    public String registerTable(SheetCell sheetCell, Map<String,Object> contentMap){
        Map<String,Object> tpl = JsonUtil.getJsonObj((String)contentMap.get("tpl"));
    	String id = (String)tpl.get("id");
    	TableInfo tableInfo = _tables.get(id);
    	if(tableInfo == null){
    		tableInfo = new TableInfo(tpl);
    		_tables.put(id, tableInfo);
    	}
    	List<Integer> span = tableInfo.getSpan();
    	int startRowIndex = span.get(1);
    	int startColIndex = span.get(2);
    	
    	if(sheetCell.getX() == startRowIndex){
    	    String plain = (String)tpl.get("plain");
    	    String headerData;
    	    int headerIndex = sheetCell.getY()- startColIndex;
    	    if(Strings.isNullOrEmpty(plain)){
    	        headerData = Strings.isNullOrEmpty(sheetCell.getRawData())  ? "Column"+(headerIndex+1) : sheetCell.getRawData();
    	    }else{
    	        headerData = plain;
    	    }
    	    tableInfo._headers[headerIndex] = headerData;
    	    return headerData;
    	}
    	return null;
    	
    }
    
    public Collection<TableInfo> tables(){
    	return _tables.values();
    }
    
    public void addMiniChart(Map<String,Object> miniChartMap){
    	_miniCharts.add(miniChartMap);
    }
    
    public List<Map<String,Object>> getMiniCharts(){
    	return _miniCharts;
    }
    
    @SuppressWarnings("unchecked")
    public XSSFWorkbookExportContext parentContext() {
        return _workbookExportContext;
    }
    
    public int getNextCol() {
  		return _nextCol;
  	}

  	public void setNextCol(int nextCol) {
  		this._nextCol = nextCol;
  	}
  	
  	public Map<String,Object> getRowStyleSetting(int rowIndex){
  		if(_rowSetting != null && _rowSetting.rowIndex == rowIndex )
  			return _rowSetting.styleSetting;
  		else
  			return null;
  	}
  	
  	public void putRowStyleSetting(int rowIndex,Map<String,Object> rowStyleSetting){
  		_rowSetting = new RowSetting(rowIndex, rowStyleSetting);
  	}
  	
  	public Map<String,Object> getSheetStyleSetting(){
  		return _sheetStyleSetting;
  	}
  	
  	public void setSheetStyleSetting(Map<String,Object> sheetStyleSetting){
  		_sheetStyleSetting = sheetStyleSetting;
  	}

    public boolean isRowHidden(int rowIndex){
        if(_rowHiddens == null) return false;
        return _rowHiddens.isHidden(rowIndex);
    }

    public boolean isColHidden(int colIndex){
        if(_colHiddens == null) return false;
        return _colHiddens.isHidden(colIndex);
    }

    
    public static class ColumnSetting implements Comparable<ColumnSetting> {
        private int min;
        private int max;
        private int width;
        private Short styleId;
        private boolean hidden=false;

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }
        
        public boolean isHidden() {
            return hidden;
        }

        public void setHidden(boolean hidden) {
            this.hidden = hidden;
        }

        public Short getStyleId() {
			return styleId;
		}

		public void setStyleId(Short styleId) {
			this.styleId = styleId;
		}

		@Override
        public int compareTo(ColumnSetting other) {
            if (min < other.min)
                return -1;
            if (min == other.min)
                return 0;
            else
                return 1;
        }
        
        public boolean equals(Object o){
            if(o == this )return true;
            if(!(o instanceof ColumnSetting))return false;
            ColumnSetting other = (ColumnSetting)o;
            return Objects.equals(width, other.width) && Objects.equals(hidden, other.hidden) && Objects.equals(styleId, other.styleId);
        }
        
        public int hashCode(){
            return Objects.hash(width,hidden,styleId);
        }

    }
    
    private static class RowSetting{
    	private final int rowIndex;
    	private final Map<String,Object> styleSetting;
    	
		public RowSetting(int rowIndex, Map<String, Object> styleSetting) {
			super();
			this.rowIndex = rowIndex;
			this.styleSetting = styleSetting;
		}
    	
    }
    
    public static class TableInfo {
        
    	private final Map<String,Object> _tbl;
    	
    	private final String[] _headers;
    	
    	public TableInfo(Map<String,Object>  tbl){
    		_tbl = tbl;
    		List<Integer> span = (List<Integer>) tbl.get("span");
    		_headers = new String[span.get(4)-span.get(2)+1];
    		
    	}
    	
    	public String getId(){
    		return (String)_tbl.get("id");
    	}
    	
    	public List<Integer> getSpan(){
    		return (List<Integer>) _tbl.get("span");
    	}
    	
    	public String[] getHeaders(){
    	    return _headers;
    	}
    	
    	public boolean equals(Object o){
    		if(o == this)return true;
    		if(!(o instanceof TableInfo))return false;
    		TableInfo other = (TableInfo)o;
    		return Objects.equals(getId(), other.getId());
    	}
    	
    	public int hashCode(){
    		return Objects.hash(getId());
    	}
    }

    public static class HiddenInfo{
        private final List<int[]> _ranges;

        private HiddenInfo(List<int[]> ranges){
            _ranges = ranges;
        }

        private boolean isHidden(int index){
            boolean hidden = false;
            for(int[] _range:_ranges){
                if(_range[0] <= index && _range[1] >= index){
                    hidden = true;
                    break;
                }
            }
            return hidden;
        }
    }


}
