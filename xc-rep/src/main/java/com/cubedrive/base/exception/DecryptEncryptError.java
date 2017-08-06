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
public class DecryptEncryptError extends CubeDriveError {

    public DecryptEncryptError(Throwable e){
        super("decrypt or encrypt error",e);
    }
}
