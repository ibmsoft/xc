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
package com.cubedrive.sheet.persistence.resultmap;

import com.cubedrive.sheet.domain.sheet.SheetTab;

public class SheetTabWithRowSize extends SheetTab{
	
	private static final long serialVersionUID = -5921912454436724008L;
	
	private Integer rowSize;

	public Integer getRowSize() {
		return rowSize;
	}

	public void setRowSize(Integer rowSize) {
		this.rowSize = rowSize;
	}

}
