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

import com.cubedrive.sheet.domain.sheet.SheetCustom;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class SheetCustomObj {
	
	private Integer id;

	private String category;

	private String text;

    public SheetCustomObj() {
    }

    public SheetCustomObj(SheetCustom sheetCustom) {
    	this.id = sheetCustom.getId();
        this.category = sheetCustom.getCategory();
        this.text = sheetCustom.getContent();
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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
		obj2.put("name", "category");
		fields.add(obj2);
		
		Map<String, String> obj3 = Maps.newHashMapWithExpectedSize(1);
		obj3.put("name", "text");
		fields.add(obj3);

		metaData.put("fields", fields);
		return metaData;
	}

}
