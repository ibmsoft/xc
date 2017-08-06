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
package com.cubedrive.base.domain.document;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cubedrive.base.domain.BaseDomain;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.xzsoft.xc.rep.service.XcSheetExpCommonService;
import com.xzsoft.xsr.core.service.ImpAndExpExcelService;

public class DocumentFile extends BaseDomain {
	
	private static final long serialVersionUID = -7477780712584171952L;
	/*
	 *Enterprise Sheet 原始字段 
	 */
	private Integer id;

	@JsonIgnore
	private User author;

	private String name;

	private String description;
	
	private String exname;
	
	private Boolean star = false;
	
	private String parentId="0";

	private Date createDate;

	private Date updateDate;
	@JsonIgnore
	private List<SheetTab> sheetTabList;
	/*
	 * 云ERP报表用到的字段
	 */
	private XcSheetExpCommonService xcSheetExpCommonService;
	/*
	 *报表3.0定义的字段
	 */
	@JsonIgnore
	private Integer MODALFORMAT_ID;
	@JsonIgnore
	private Timestamp CREATION_DATE;
	@JsonIgnore
	private String CREATED_BY;
	@JsonIgnore
	private ImpAndExpExcelService impAndExpExcelService;
	
	public Integer getMODALFORMAT_ID() {
		return MODALFORMAT_ID;
	}
	public void setMODALFORMAT_ID(Integer mODALFORMAT_ID) {
		MODALFORMAT_ID = mODALFORMAT_ID;
	}
	public List<SheetTab> getSheetTabList() {
		return sheetTabList;
	}
	public void setSheetTabList(List<SheetTab> sheetTabList) {
		this.sheetTabList = sheetTabList;
	}
	public Timestamp getCREATION_DATE() {
		return CREATION_DATE;
	}
	public void setCREATION_DATE(Timestamp cREATION_DATE) {
		CREATION_DATE = cREATION_DATE;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	public ImpAndExpExcelService getImpAndExpExcelService() {
		return impAndExpExcelService;
	}
	public void setImpAndExpExcelService(ImpAndExpExcelService impAndExpExcelService) {
		this.impAndExpExcelService = impAndExpExcelService;
	}
	/**
	 * @return the xcSheetExpCommonService
	 */
	public XcSheetExpCommonService getXcSheetExpCommonService() {
		return xcSheetExpCommonService;
	}
	/**
	 * @param xcSheetExpCommonService the xcSheetExpCommonService to set
	 */
	public void setXcSheetExpCommonService(
			XcSheetExpCommonService xcSheetExpCommonService) {
		this.xcSheetExpCommonService = xcSheetExpCommonService;
	}
	public DocumentFile(User author){
        this.author=author;
    }
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getStar() {
		return star;
	}

	public void setStar(Boolean star) {
		this.star = star;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getExname() {
		return exname;
	}

	public void setExname(String exname) {
		this.exname = exname;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// this is initial ....
    public DocumentFile() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentFile other = (DocumentFile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
