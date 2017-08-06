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
package com.cubedrive.base.persistence;

import java.util.Collections;
import java.util.List;

public class SQLQueryResult<T> {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final static SQLQueryResult EMPTY_RESULT = new SQLQueryResult(0,
			Collections.EMPTY_LIST);

	public final long totalRecords;

	public final List<T> data;

	public SQLQueryResult(long totalRecords, List<T> data) {
		super();
		this.totalRecords = totalRecords;
		this.data = data;
	}

}
