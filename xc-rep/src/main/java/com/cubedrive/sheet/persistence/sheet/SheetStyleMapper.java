package com.cubedrive.sheet.persistence.sheet;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.sheet.domain.sheet.SheetStyle;

@Repository
public interface SheetStyleMapper {
    
    int insert(SheetStyle sheetStyle);
    
    int batchInsert(@Param(value="sheetStyles")Collection<SheetStyle> sheetStyles);
    
    List<SheetStyle> findByDocumentId(Integer documentId);
    
    int deleteByDocumentId(Integer documentId);

}
