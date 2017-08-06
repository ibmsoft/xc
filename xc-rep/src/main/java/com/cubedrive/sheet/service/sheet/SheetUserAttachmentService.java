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
package com.cubedrive.sheet.service.sheet;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cubedrive.base.AppContext;
import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.event.DocumentDeleteEvent;
import com.cubedrive.base.event.UserDeleteEvent;
import com.cubedrive.base.utils.BaseFileUtil;
import com.cubedrive.base.utils.MimeTypes;
import com.cubedrive.sheet.SheetAppConstants;
import com.cubedrive.sheet.domain.sheet.SheetUserAttachment;
import com.cubedrive.sheet.persistence.sheet.SheetUserAttachmentMapper;
import com.cubedrive.sheet.utils.SheetFileUtil;
import com.google.common.eventbus.Subscribe;


@Service
public class SheetUserAttachmentService {
    
    @Autowired
    private SheetUserAttachmentMapper userAttachmentMapper;
    
    
    @PostConstruct
    public void init(){
        AppContext.instance().eventBus().register(this);
    }
    
    public SheetUserAttachment create(MultipartFile uploadFile, Integer userId, Integer documentId) throws IOException{
    	
         File attachmentFile = SheetFileUtil.placeUserFileAttachment(uploadFile, userId, documentId);
         
         SheetUserAttachment userAttachment = new SheetUserAttachment();
         userAttachment.setUserId(userId);
         userAttachment.setDocumentId(documentId);
         userAttachment.setFileName(FilenameUtils.getName(uploadFile.getOriginalFilename()));
         userAttachment.setFilePath(attachmentFile.getAbsolutePath());
         userAttachment.setMimeType(MimeTypes.getMimeType(FilenameUtils.getExtension(uploadFile.getOriginalFilename())));
         userAttachment.setCreateTime(new Date());
         userAttachmentMapper.insert(userAttachment);
         return userAttachment; 
    }
    
    public SheetUserAttachment getById(Integer attachmentId){
        return userAttachmentMapper.getById(attachmentId);
    }
    
    public void deleteById(Integer attachmentId){
        SheetUserAttachment userAttachment = userAttachmentMapper.getById(attachmentId);
        if(userAttachment != null){
            FileUtils.deleteQuietly(new File(userAttachment.getFilePath()));
            userAttachmentMapper.deleteById(attachmentId);
        }
    }
    
    
    @Subscribe
    public void onDocumentDelete(DocumentDeleteEvent event){
        Integer documentId = event.getDocumentFile().getId();
        File userAttachmentDir = BaseFileUtil.getUserSubDir(event.getDocumentFile().getAuthor().getId().toString(), SheetAppConstants.SHEET_ATTACHMENT_DIR);
        File documentAttachmentDir = new File(userAttachmentDir, documentId.toString());
        FileUtils.deleteQuietly(documentAttachmentDir);
    }
    
    @Subscribe
    public void onUserDelete(UserDeleteEvent event){
        File userAttachmentDir = BaseFileUtil.getUserSubDir(event.getUser().getId().toString(), SheetAppConstants.SHEET_ATTACHMENT_DIR);
        FileUtils.deleteQuietly(userAttachmentDir);
    }
    
}
