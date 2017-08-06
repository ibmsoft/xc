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

import java.io.OutputStream;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.cubedrive.base.exception.CubeDriveError;
import com.cubedrive.base.exception.MoreInfoAndCodeError;
import com.cubedrive.base.exception.MoreInfoError;
import com.cubedrive.base.exception.NoAuthenticatedOperationError;
import com.cubedrive.base.utils.JsonUtil;
import com.cubedrive.base.utils.WebUtil;
import com.google.common.collect.Maps;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	public GlobalExceptionHandler(){
	}
	
	@Autowired
	protected MessageSource messageSource;
	
 	@ExceptionHandler
    protected void handleMoreInfoAndCodeError(MoreInfoAndCodeError error, HttpServletRequest request, HttpServletResponse response) {
 		LOG.error("", error);
        Map<String, String> results = Maps.newHashMap();
        results.put("success", "false");
        results.put("info", error.getInfo());
        results.put("code", error.getCode());
        WebUtil.outputJson(results, request, response);
    }

    @ExceptionHandler
    protected void handleNoAuthenticatedOperationError(NoAuthenticatedOperationError error, HttpServletRequest request, HttpServletResponse response) {
    	LOG.error("", error);
        Map<String, String> results = Maps.newHashMap();
        results.put("success", "false");
        results.put("info", messageSource.getMessage("myProfile.pwNotMatch", null, WebUtil.getRequestLocale()));
        WebUtil.outputJson(results, request, response);
    }

    @ExceptionHandler
    protected void handleMoreInfoError(MoreInfoError error, HttpServletRequest request, HttpServletResponse response) {
    	LOG.error("", error);
        Map<String, String> results = Maps.newHashMap();
        results.put("success", "false");
        results.put("info", error.getInfo());
        WebUtil.outputJson(results, request, response);
    }

    
    
    @ExceptionHandler
    protected void handleCubeDriveErrorError(CubeDriveError error, HttpServletRequest request, HttpServletResponse response) {
    	LOG.error("", error);
        Map<String, String> results = Maps.newHashMap();
        results.put("success", "false");
        results.put("info", messageSource.getMessage("general.internalError", null,  WebUtil.getRequestLocale()));
        WebUtil.outputJson(results, request, response);
    }
    

	@ExceptionHandler
	public void handleUncaughtException(Exception ex,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		LOG.error("", ex);
		Locale locale = RequestContextUtils.getLocale(request);
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setHeader("Pragma", "no-cache");
		Map<String, String> results = Maps.newHashMap();
		results.put("success", "false");
		results.put("info", messageSource.getMessage("general.internalError",null, locale));
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			JsonUtil.toJson(results, out);
			out.flush();
		}finally {
			IOUtils.closeQuietly(out);
		}
	}

}
