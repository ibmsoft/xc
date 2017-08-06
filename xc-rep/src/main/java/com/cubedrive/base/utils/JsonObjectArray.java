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
package com.cubedrive.base.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cubedrive.base.utils.JsonObjectArray.JsonObjectArraySerializer;

@JsonSerialize(using=JsonObjectArraySerializer.class)
public class JsonObjectArray {
	
	private final List<JsonObjectArrayItem> _items = new ArrayList<>();
	
	public void put(String objectKey, Object objectValue){
		assert(objectKey != null);
		_items.add(new JsonObjectArrayItem(objectKey, objectValue));
	}
	
	public void add(Object objectValue){
		_items.add(new JsonObjectArrayItem(null, objectValue));
	}
	
	private static class JsonObjectArrayItem{
		private final String key;
		private final Object value;
		
		public JsonObjectArrayItem(String key, Object value) {
			super();
			this.key = key;
			this.value = value;
		}
		
	}
	
	
	public static class JsonObjectArraySerializer extends JsonSerializer<JsonObjectArray> {

		@Override
		public void serialize(JsonObjectArray jsonObjectArray, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,JsonProcessingException {
			jgen.writeStartArray();
			for(JsonObjectArrayItem _item:jsonObjectArray._items){
				if(_item.key != null){
					jgen.writeStartObject();
					jgen.writeFieldName(_item.key);
					jgen.writeObject(_item.value);
					jgen.writeEndObject();
				}else{
					jgen.writeObject(_item.value);
				}
				
			}
			jgen.writeEndArray();
		}
		
		public Class<JsonObjectArray> handledType(){
			return JsonObjectArray.class;
		}
		
	}
	
	public static void main(String[]args){
		JsonObjectArray jsonObjectArray = new JsonObjectArray();
		jsonObjectArray.put("a", "1");
		jsonObjectArray.put("b", "2");
		jsonObjectArray.put("d", "4");
		
		Map<String,Object> nestJsonObjectArray = new HashMap<>();
		nestJsonObjectArray.put("name", "zzy");
		nestJsonObjectArray.put("age", 12);
		jsonObjectArray.add(nestJsonObjectArray);
		System.out.println(JsonUtil.toJson(jsonObjectArray));
		
	}
	

}
