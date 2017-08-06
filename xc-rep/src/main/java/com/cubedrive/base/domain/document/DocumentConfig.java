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

import org.codehaus.jackson.annotate.JsonIgnore;

import com.cubedrive.base.domain.BaseDomain;

public class DocumentConfig extends BaseDomain {
	
	private static final long serialVersionUID = -7477780722574171952L;

	private Integer id;
	
	@JsonIgnore
	private DocumentFile documentFile;

	private String name;
	
	private String json;

	private String ctype;
	
	public DocumentConfig() {
		
	}
	
	public DocumentConfig(String name, String ctype, String content) {
		this.name = name;
		this.ctype = ctype;
		this.json = content;
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

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
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
		DocumentConfig other = (DocumentConfig) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
