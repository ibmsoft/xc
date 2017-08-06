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
package com.cubedrive.base.service.document;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.base.domain.document.DocumentConfig;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.persistence.document.DocumentConfigMapper;
import com.cubedrive.base.persistence.document.DocumentFileMapper;

@Service
public class DocumentConfigService {
	
	@Autowired
	private DocumentFileMapper documentFileMapper;
	
	@Autowired
	private DocumentConfigMapper documentConfigMapper;
	
	public void create(DocumentConfig documentConfig) {
		documentConfigMapper.insert(documentConfig);
	}

	public List<DocumentConfig> find(Integer documentId) {
		return documentConfigMapper.find(documentId);
	}
	
	public void copy(DocumentFile fromDoc, DocumentFile toDoc) {
		List<DocumentConfig> allConfigs = documentConfigMapper.find(fromDoc.getId());
		
		for(DocumentConfig config : allConfigs) {
			config.setDocumentFile(toDoc);
			documentConfigMapper.insert(config);
		}
	}
	
	public DocumentConfig create(Map<String,Object> jsonObj, DocumentFile document) {
		String name = jsonObj.get("name").toString();
		String cType = jsonObj.get("ctype").toString();
		String json = jsonObj.get("json").toString();

		DocumentConfig config = new DocumentConfig(name, cType, json);
		config.setDocumentFile(document);
		documentConfigMapper.insert(config);
		
		return config;
	}
	
	public void update(Map<String,Object> jsonObj, DocumentFile document) {
		String name = jsonObj.get("name").toString();
		String cType = jsonObj.get("ctype").toString();
		String json = jsonObj.get("json").toString();

		documentConfigMapper.update(document.getId(), name, cType, json);
	}
	
	public void remove(Map<String,Object> jsonObj, DocumentFile document) {
		String name = jsonObj.get("name").toString();
		String cType = jsonObj.get("ctype").toString();
		documentConfigMapper.delete(document.getId(), name, cType);
	}
	
	public void clean(DocumentFile document) {
		documentConfigMapper.clean(document.getId());
	}
}
