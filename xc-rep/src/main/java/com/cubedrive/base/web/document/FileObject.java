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
package com.cubedrive.base.web.document;

import java.util.Map;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.utils.DateTimeUtil;
import com.cubedrive.base.utils.JsonObjectArray;
import com.google.common.collect.Maps;

public class FileObject {
	private String id;
	private String name;
	private String exname;
	private boolean star;
	private String updateDate;

	public FileObject() {
		super();
	}
	
	public FileObject(DocumentFile file) {
		name = file.getName();
		exname = file.getExname();
		star = file.getStar().booleanValue();
		updateDate = DateTimeUtil.formatAsYYYYMMddHHmmss(file.getUpdateDate());
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExname() {
		return exname;
	}

	public void setExname(String exname) {
		this.exname = exname;
	}

	public boolean isStar() {
		return star;
	}

	public void setStar(boolean star) {
		this.star = star;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public static Map<String, Object> createMetaData() {
		Map<String, Object> metaData = Maps.newHashMap();
		metaData.put("totalProperty", "totalCount");
		metaData.put("root", "results");
		metaData.put("id", "id");
		
		JsonObjectArray fields = new JsonObjectArray();
		fields.put("name", "id");
		fields.put("name", "name");
		fields.put("name", "exname");
		fields.put("name", "star");
		fields.put("name", "updateDate");
		fields.put("name", "createDate");
		metaData.put("fields", fields);
		return metaData;
	}
}
