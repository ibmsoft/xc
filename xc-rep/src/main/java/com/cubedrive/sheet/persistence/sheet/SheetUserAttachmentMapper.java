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

import com.cubedrive.sheet.domain.sheet.SheetUserAttachment;

@Repository
public interface SheetUserAttachmentMapper {
    
    int insert(SheetUserAttachment attachment);
    
    int updateDocument(@Param(value="attachmentId")Integer attachmentId,@Param(value="documentId") Integer documentId);
    
    SheetUserAttachment getById(Integer attachmentId);
    
    List<SheetUserAttachment> findByUser(Integer userId);
    
    List<SheetUserAttachment> findByDocument(Integer documentId);
    
    int deleteById(Integer attachmentId);
    
    int deleteByUser(Integer userId);
    
    int deleteByDocument(Integer documentId);

}
