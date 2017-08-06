package com.cubedrive.sheet.service.sheet._import.ctx;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.sheet.domain.sheet.SheetStyle;
import com.cubedrive.sheet.service.sheet._import.SheetImportEnvironment;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class WorkbookStylesTable {
    
    private final SheetImportEnvironment _sheetImportEnvironment;
    
    private final Map<Integer, WorkbookStyleEntry> _styles = new HashMap<>();
    
    WorkbookStylesTable(SheetImportEnvironment sheetImportEnvironment){
        this._sheetImportEnvironment = sheetImportEnvironment;
    }
    
    public int getOrCreateStyle(Map<String,Object> styleSettings){
        WorkbookStyleEntry entry = new WorkbookStyleEntry(styleSettings);
        Integer hashId = entry.getHashId();
        if(!_styles.containsKey(hashId)){
            _styles.put(hashId, entry);
        }
        return hashId;
    }
    
    
    public void flushAll(){
        if(!_styles.isEmpty()){
            final Integer documentId = _sheetImportEnvironment.documentFile().getId();
            Collection<SheetStyle> sheetStyles = Collections2.transform(_styles.values(), new Function<WorkbookStyleEntry,SheetStyle>(){

                @Override
                public SheetStyle apply(WorkbookStyleEntry entry) {
                    String settingsJson = JsonUtil.toJson(entry.getSettings());
                    SheetStyle sheetStyle = new SheetStyle(documentId, entry.getHashId(), settingsJson);
                    return sheetStyle;
                }
                
            });
            _sheetImportEnvironment.sheetStyleMapper().batchInsert(sheetStyles);
        }
    }
    
    private static class WorkbookStyleEntry{
        
        private Integer _hashId;
        
        private final Map<String,Object> _settings = new TreeMap<>();
        
        
        public WorkbookStyleEntry(Map<String,Object> styleSettings){
            _settings.putAll(styleSettings);
        }
        
        public Integer getHashId(){
            if(_hashId == null){
               _hashId = Math.abs(_settings.hashCode());
            }
            return _hashId;
        }
        
        public Map<String,Object> getSettings(){
            return _settings;
        }
        
        public int hashCode(){
            return _hashId;
        }
        
        public boolean equals(Object o){
            if(o == this);
            if(!(o instanceof WorkbookStyleEntry))return false;
            WorkbookStyleEntry other = (WorkbookStyleEntry)o;
            Integer hashId = getHashId();
            Integer otherHashId = other.getHashId();
            return Objects.equals(hashId, otherHashId);
        }
        
        
    }

}
