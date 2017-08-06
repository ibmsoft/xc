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
public class CubeDriveError extends RuntimeException {

    public CubeDriveError(){
        super();
    }

    public CubeDriveError(String message){
        super(message);
    }

    public CubeDriveError(String message,Throwable ex){
        super(message,ex);
    }

    public CubeDriveError(Throwable ex){
        super(ex);
    }
}
