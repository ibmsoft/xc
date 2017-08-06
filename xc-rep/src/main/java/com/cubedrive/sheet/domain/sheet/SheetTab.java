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

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cubedrive.base.domain.BaseDomain;
import com.cubedrive.base.domain.document.DocumentFile;

public class SheetTab extends BaseDomain {

	private static final long serialVersionUID = -1061507017182935906L;
	/*
	 *Enterprise Sheet 原始字段 
	 */
	private Integer id;
	@JsonIgnore
	private DocumentFile documentFile;

	private String name;
	
	private Integer tabOrder;

	private String extraInfo;
    
    private String color;

	private Boolean active;
	
	private String cellTable;
	/*
	 * 云ERP报表用到的字段
	 */
	@JsonIgnore
	private String tabId;
	@JsonIgnore
	private String ledgerId;
	@JsonIgnore
	private String dbType;
	@JsonIgnore
	private String orgId;
	@JsonIgnore
	private String operateType;
	@JsonIgnore
	private String orgName;
	@JsonIgnore
	private String periodName;
	@JsonIgnore
	private String periodId;
	/*
	 *报表3.0定义的字段
	 */
	@JsonIgnore
	private String cnyId;
	@JsonIgnore
	private String modalsheetId;
	@JsonIgnore
	private String suitId;
	@JsonIgnore
	private String modaltypeId;
	@JsonIgnore
	private Integer MODALFORMAT_ID;
	@JsonIgnore
	private String titleMaxRow;
	@JsonIgnore
	private String isFj;
	/**
	 * @return the dbType
	 */
	public String getDbType() {
		return dbType;
	}
	/**
	 * @param dbType the dbType to set
	 */
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getPeriodId() {
		return periodId;
	}
	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	public String getCnyId() {
		return cnyId;
	}
	public void setCnyId(String cnyId) {
		this.cnyId = cnyId;
	}
	public String getModalsheetId() {
		return modalsheetId;
	}
	public void setModalsheetId(String modalsheetId) {
		this.modalsheetId = modalsheetId;
	}
	public String getSuitId() {
		return suitId;
	}
	public void setSuitId(String suitId) {
		this.suitId = suitId;
	}
	public String getModaltypeId() {
		return modaltypeId;
	}
	public void setModaltypeId(String modaltypeId) {
		this.modaltypeId = modaltypeId;
	}
	public Integer getMODALFORMAT_ID() {
		return MODALFORMAT_ID;
	}
	public void setMODALFORMAT_ID(Integer mODALFORMAT_ID) {
		MODALFORMAT_ID = mODALFORMAT_ID;
	}
	public String getTitleMaxRow() {
		return titleMaxRow;
	}
	public void setTitleMaxRow(String titleMaxRow) {
		this.titleMaxRow = titleMaxRow;
	}
	public String getIsFj() {
		return isFj;
	}
	public void setIsFj(String isFj) {
		this.isFj = isFj;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getPeriodName() {
		return periodName;
	}
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DocumentFile getDocumentFile() {
		return documentFile;
	}

	public void setDocumentFile(DocumentFile documentFile) {
		this.documentFile = documentFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getTabOrder() {
		return tabOrder;
	}

	public void setTabOrder(Integer tabOrder) {
		this.tabOrder = tabOrder;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCellTable() {
		return cellTable;
	}

	public void setCellTable(String cellTable) {
		this.cellTable = cellTable;
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
		SheetTab other = (SheetTab) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	/**
	 * @return the ledgerId
	 */
	public String getLedgerId() {
		return ledgerId;
	}
	/**
	 * @param ledgerId the ledgerId to set
	 */
	public void setLedgerId(String ledgerId) {
		this.ledgerId = ledgerId;
	}
	/**
	 * @return the tabId
	 */
	public String getTabId() {
		return tabId;
	}
	/**
	 * @param tabId the tabId to set
	 */
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	
}
