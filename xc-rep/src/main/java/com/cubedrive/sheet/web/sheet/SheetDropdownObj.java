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

import java.util.List;
import java.util.Map;

import com.cubedrive.sheet.domain.sheet.SheetDropdown;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SheetDropdownObj {
	
	private String id;
	private String name;
	private String data;
	
	public SheetDropdownObj() {
	}
	
	public SheetDropdownObj(String id, String name, String data) {
		this.id = id;
		this.name = name;
		this.data = data;
	}
	
	public SheetDropdownObj(SheetDropdown obj) {
		this.id = obj.getId().toString();
		this.name = obj.getName();
		this.data = obj.getContent();
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public static Map<String, Object> createMetaData() {
		Map<String, Object> metaData = Maps.newHashMapWithExpectedSize(4);
		metaData.put("totalProperty", "totalCount");
		metaData.put("root", "results");
		metaData.put("id", "id");
		
		List<Map<String, String>> fields = Lists.newArrayListWithExpectedSize(16);
		Map<String, String> obj1 = Maps.newHashMapWithExpectedSize(1);
		obj1.put("name", "id");
		fields.add(obj1);
		
		Map<String, String> obj2 = Maps.newHashMapWithExpectedSize(1);
		obj2.put("name", "name");
		fields.add(obj2);
		
		Map<String, String> obj3 = Maps.newHashMapWithExpectedSize(1);
		obj3.put("name", "data");
		fields.add(obj3);

		metaData.put("fields", fields);
		return metaData;
	}
	
}