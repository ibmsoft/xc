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
package com.cubedrive.sheet.web.sheet;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.utils.DecryptEncryptUtil;
import com.cubedrive.base.utils.DecryptEncryptUtil.DesEncrypter;
import com.cubedrive.sheet.domain.sheet.SheetUserAttachment;
import com.cubedrive.sheet.service.sheet.SheetUserAttachmentService;
import com.cubedrive.sheet.web.BaseSheetController;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/sheetAttach")
public class SheetAttachController extends BaseSheetController {
    
    @Autowired
    private SheetUserAttachmentService userAttachmentService;
    
    @RequestMapping(value = "uploadFile", method=RequestMethod.POST)
    public void uploadFile(@RequestParam MultipartFile filePath,
            @RequestParam(required=false) String documentId,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        DesEncrypter attachmentDesEncrypter = DecryptEncryptUtil.getDesEncrypterForAttachment();
        
        User loginUser = getLoginUser();
        Integer documentId_int = null;
        if(documentId != null){
            DesEncrypter fileDesEncrypter = DecryptEncryptUtil.getDesEncrypterForFile(loginUser.getUsername());
            documentId_int = Integer.parseInt(fileDesEncrypter.doDecryptUrl(documentId));
        }
        
        SheetUserAttachment userAttachment = userAttachmentService.create(filePath, loginUser.getId(), documentId_int);
            
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", "true");
        results.put("attachId", attachmentDesEncrypter.doEncryptUrl(userAttachment.getId().toString()));
        results.put("fileName", userAttachment.getFileName());
        results.put("mimeType", userAttachment.getMimeType());
        results.put("info", messageSource.getMessage("general.changesSaved", null, getUserLocale()));
        
        outputJson(results, request, response);
    }
    
    @RequestMapping(value = "downloadFile")
    public void downloadFile(@RequestParam String attachId,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        DesEncrypter attachmentDesEncrypter = DecryptEncryptUtil.getDesEncrypterForAttachment();
        Integer attachId_int = Integer.parseInt(attachmentDesEncrypter.doDecryptUrl(attachId));
        SheetUserAttachment userAttachment = userAttachmentService.getById(attachId_int);
        if(userAttachment != null){
            File attachmentFile = new File(userAttachment.getFilePath());
            outputBinary(userAttachment.getFileName(), new FileInputStream(attachmentFile), userAttachment.getMimeType(),request,response);
        }
    }
    
    @RequestMapping(value = "deleteFile", method=RequestMethod.POST)
    public void deleteFile(@RequestParam String attachId,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        DesEncrypter attachmentDesEncrypter = DecryptEncryptUtil.getDesEncrypterForAttachment();
        Integer attachId_int = Integer.parseInt(attachmentDesEncrypter.doDecryptUrl(attachId));
        userAttachmentService.deleteById(attachId_int);
        Map<String, Object> results = Maps.newHashMap();
        results.put("success", "true");
        results.put("info", messageSource.getMessage("general.changesSaved", null, getUserLocale()));
        outputJson(results, request, response);
    }

}
