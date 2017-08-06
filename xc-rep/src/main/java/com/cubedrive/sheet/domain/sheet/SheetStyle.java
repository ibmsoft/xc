package com.cubedrive.sheet.domain.sheet;

import java.util.Objects;

import com.cubedrive.base.domain.BaseDomain;

public class SheetStyle  extends BaseDomain {
    
    private static final long serialVersionUID = 5667994026050086746L;

    private Integer documentId;
    
    private Integer hashId;
    
    private String style;

    public SheetStyle(Integer documentId, Integer hashId, String style) {
        super();
        this.documentId = documentId;
        this.hashId = hashId;
        this.style = style;
    }
    
    public SheetStyle() {
        super();
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getHashId() {
        return hashId;
    }

    public void setHashId(Integer hashId) {
        this.hashId = hashId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
    
    public boolean equals(Object o){
        if(o == this)return true;
        if(!(o instanceof SheetStyle))return false;
        SheetStyle other = (SheetStyle)o;
        return Objects.equals(documentId, other.documentId) && Objects.equals(hashId, other.hashId);
    }
    
    public int hashCode(){
        return Objects.hash(documentId, hashId);
    }
    

}
