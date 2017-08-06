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

import com.cubedrive.base.domain.BaseDomain;
import com.cubedrive.base.domain.document.DocumentFile;

public class SheetDropdown extends BaseDomain {
	
	private static final long serialVersionUID = -2512858314186445811L;

	private Integer id;

	private DocumentFile documentFile;

	private String name;

	private String content;

    public SheetDropdown() {
    }

    public SheetDropdown(DocumentFile documentFile, String name, String content) {
        this.documentFile=documentFile;
        this.name = name;
        this.content = content;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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
		SheetDropdown other = (SheetDropdown) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
