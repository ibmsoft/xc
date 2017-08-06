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

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.base.domain.document.DocumentFile;

@Repository
public interface DocumentFileMapper {

	public void insertDocumentFile(
			@Param(value = "document") DocumentFile documentFile);

	public void updateDocumentFile(@Param(value = "document") DocumentFile documentFile);

	public DocumentFile getById(@Param(value = "documentId") Integer documentFileId);
	
	void updateDocDate(@Param(value = "documentId") Integer documentId,
			@Param(value = "updateDate") Date updateDate);

	Integer countDocument(
			@Param(value = "authorId") Integer authorId,
			@Param(value = "exnames") List<String> exnames,
			@Param(value = "query") String query,
			@Param(value = "queryBy") String queryBy);
	
	List<DocumentFile> findDocuments(
			@Param(value = "authorId") Integer authorId,
			@Param(value = "exnames") List<String> exnames,
			@Param(value = "start") Integer start,
			@Param(value = "limit") Integer limit,
			@Param(value = "query") String query,
			@Param(value = "queryBy") String queryBy,
			@Param(value = "sort") String sort,
			@Param(value = "sortBy") String sortBy,
			@Param(value = "folderId") String folderId
	);
	
	int deleteById(Integer documentId);
}
