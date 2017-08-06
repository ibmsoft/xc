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
package com.cubedrive.base.exception;


@SuppressWarnings("serial")
public class NoAuthenticatedOperationError extends CubeDriveError {


	public NoAuthenticatedOperationError(String message) {
		super(message);
	}

}
