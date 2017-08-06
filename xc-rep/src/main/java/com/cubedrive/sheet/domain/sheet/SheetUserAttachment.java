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
package com.cubedrive.sheet.domain.sheet;

import java.util.Date;
import java.util.Objects;

import com.cubedrive.base.domain.BaseDomain;

public class SheetUserAttachment extends BaseDomain {
    
    private static final long serialVersionUID = 4208091011644509977L;

    private Integer id;

    private Integer userId;

    private Integer documentId;

    private String fileName;

    private String filePath;

    private String mimeType;

    private Date createTime;

    public SheetUserAttachment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean equals(Object o){
        if(o == this)return true;
        if(!(o instanceof SheetUserAttachment))return false;
        SheetUserAttachment other = (SheetUserAttachment)o;
        return Objects.equals(id, other.id);
    }
    
    public int hashCode(){
        return Objects.hash(id);
    }
    
    

}
