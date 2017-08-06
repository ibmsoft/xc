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
package com.cubedrive.base.persistence.document;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.base.domain.document.DocumentConfig;

@Repository
public interface DocumentConfigMapper {

	public void insert(
			@Param(value = "documentConfig") DocumentConfig documentConfig);

	public void batchInsert(List<DocumentConfig> documentConfigList);

	public void updateByConfig(@Param(value = "documentConfig") DocumentConfig documentConfig);
	
	public void update(@Param(value = "documentId") Integer documentId,
			@Param(value = "name") String name,
			@Param(value = "cType") String cType,
			@Param(value = "json") String json);
	
	public void delete(@Param(value = "documentId") Integer documentId,
			@Param(value = "name") String name,
			@Param(value = "cType") String cType);
	
	public void clean(@Param(value = "documentId") Integer documentId);

	public DocumentConfig getById(@Param(value = "configId") Integer configId);
	
	public List<DocumentConfig> find (
			@Param(value = "documentId") Integer documentId
	);
	
}
