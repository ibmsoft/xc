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
package com.cubedrive.sheet.service.sheet._import.xssf;

public interface XSSFReadEventListener {

    void onStartWorkbook(XSSFReadEventWorkbook workbook);

    void onEndWorkbook(XSSFReadEventWorkbook workbook);

    void onStartSheet(XSSFReadEventSheet sheet);

    void onEndSheet(XSSFReadEventSheet sheet);

    void onStartRow(XSSFReadEventRow row);

    void onEndRow(XSSFReadEventRow row);

    void onStartCell(XSSFReadEventCell cell);

    void onEndCell(XSSFReadEventCell cell);

}
