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
package com.cubedrive.base.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.cubedrive.base.domain.account.User;
import com.cubedrive.base.service.account.UserService;
import com.cubedrive.base.utils.WebUtil;

public class BaseController {

	@Autowired
	protected MessageSource messageSource;

	@Autowired
	protected UserService userService;

	// get user
	protected User getLoginUser() {
		return userService.getCurrentUser();
	}

	public Locale getUserLocale() {
		return WebUtil.getRequestLocale();
	}

	// this is get lang file
	protected String getLang() {
		return "en";
	}

	protected void outputJson(Object object, HttpServletRequest request,
			HttpServletResponse response) {
		WebUtil.outputJson(object, request, response);
	}

	public void outputJsonFile(File jsonFile, HttpServletRequest request,
			HttpServletResponse response) {
		WebUtil.outputJsonFile(jsonFile, request, response);
	}

	public void outputText(Object object, HttpServletRequest request,
			HttpServletResponse response) {
		WebUtil.outputText(object, request, response);
	}
	
	public void outputJSJsonP(String callback, Object object, HttpServletRequest request,
			HttpServletResponse response) {
		WebUtil.outputJSJsonP(callback, object, request, response);
	}

	public void outputBinary(String originalFilename, InputStream in,
			String contentType, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		WebUtil.outputBinary(originalFilename, in, contentType, request, response);
	}

	

}
