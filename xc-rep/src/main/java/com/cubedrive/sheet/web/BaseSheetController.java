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
package com.cubedrive.sheet.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cubedrive.base.web.BaseController;
import com.cubedrive.sheet.exception.SpreadSheetImportError;
import com.google.common.collect.Maps;

public class BaseSheetController extends BaseController {

	@ExceptionHandler
	protected void handleNotSpreadSheetException(SpreadSheetImportError error,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> results = Maps.newHashMap();
		results.put("success", "false");
		results.put("info", messageSource.getMessage("spreadsheet.onlyFormat", null, getUserLocale()));
		outputJson(results, request, response);
	}

}
