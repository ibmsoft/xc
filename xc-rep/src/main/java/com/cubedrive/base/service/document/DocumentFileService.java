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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cubedrive.base.AppContext;
import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.base.event.DocumentDeleteEvent;
import com.cubedrive.base.persistence.SQLQueryResult;
import com.cubedrive.base.persistence.document.DocumentFileMapper;
import com.cubedrive.base.utils.MyBatisOgnlUtil;

@Service
public class DocumentFileService {

	@Autowired
	private DocumentFileMapper documentFileMapper;

	public DocumentFile getById(Integer id) {
		return documentFileMapper.getById(id);
	}

	/**
	 * need create document list based on parameters. Similiar with hibernate
	 * search criteria
	 * 
	 * shareMeDocFileIds - this will be in OR - you can put null
	 * 
	 * @param start
	 *            - 0, 10, etc
	 * @param limit
	 *            - total size in one page
	 * @param sort
	 *            - sort by
	 * @param query
	 *            - string like
	 * @param exname
	 *            - myExcel, myPpt, myDoc
	 * @param authorId
	 *            - user
	 * @return
	 */
	public SQLQueryResult<DocumentFile> getListDocuments(Integer start,
			Integer limit, String sort, String sortBy, String queryForName,
			Integer authorId, List<String> exnameList) {

		String queryBy = null;
		if (queryForName != null && queryForName.trim().length() > 0) {
			queryBy = "document_name";
		}

		// initial value ...
		SQLQueryResult<DocumentFile> queryResult = SQLQueryResult.EMPTY_RESULT;
		int _count = documentFileMapper.countDocument(authorId, exnameList, queryForName,
				queryBy);

		// check sort ... set default one
		if (sort != null && sort.equalsIgnoreCase("name")) sort = "document_name";
		else sort = "update_date";
		if (sortBy == null) sortBy = "DESC";

		if (_count != 0) {		
			List<DocumentFile> _data = documentFileMapper
					.findDocuments(authorId, exnameList, start, limit, queryForName,
							queryBy, sort, sortBy, null);
			queryResult = new SQLQueryResult<DocumentFile>(_count, _data);
		}

		return queryResult;
	}
	
	/**
	 * this will find folder and file with the related type ...
	 * 
	 * @param user
	 * @param folderId
	 * @param shareMeDocFileIds
	 * @param fileType
	 * @return
	 */
	public List<DocumentFile> findFileFolders(User user, String folderId, List<String> exnameList, 
			String query, String queryBy) {
		
		if (query != null) query = MyBatisOgnlUtil.matchAnywhere(query);

		String sort = "update_date";
		String sortBy = "DESC";
		
		List<DocumentFile> _data = documentFileMapper
				.findDocuments(user.getId(), exnameList, 0, 500,
						query, queryBy, sort, sortBy, folderId);
		return _data;
	}

	/**
	 * This is the method to create document based on file and user
	 * 
	 * @param documentFile
	 * @param authorId
	 */
	public void createDocumentFile(DocumentFile documentFile) {
		documentFileMapper.insertDocumentFile(documentFile);
	}

	public void updateDocumentFile(DocumentFile documentFile) {
		documentFileMapper.updateDocumentFile(documentFile);
	}

	/**
	 * Just quick update doc to now date
	 * 
	 * @param documentFile
	 */
	public void updateDoc2Now(DocumentFile documentFile) {
		documentFileMapper.updateDocDate(documentFile.getId(), new Date());
	}

	public void deleteDocumentFile(DocumentFile documentFile){
	    documentFileMapper.deleteById(documentFile.getId());
	    AppContext.instance().eventBus().post(new DocumentDeleteEvent(documentFile));
	}
	
}
