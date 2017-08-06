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
public class MoreInfoError extends CubeDriveError {

    private final String info;

    public MoreInfoError(String info) {
        super();
        this.info = info;
    }

    public MoreInfoError(String info, Throwable ex) {
        super(ex);
        this.info = info;
    }

    public String getInfo() {
        return info;
    }


}
