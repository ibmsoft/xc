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
package com.cubedrive.base.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.account.UserSetting.Lang;
import com.google.common.base.Strings;

public class WebUtil {

    public enum WebRequestType{
        ajax,no_ajax
    }

    public static WebRequestType requestType(HttpServletRequest httpServletRequest){
        String header = httpServletRequest.getHeader("X-Requested-With");
        WebRequestType type = "XMLHttpRequest".equalsIgnoreCase(header) ? WebRequestType.ajax : WebRequestType.no_ajax;
        return type;
    }

    public static boolean isIE(HttpServletRequest request) {
        return request.getHeader("USER-AGENT").toLowerCase().indexOf("msie") > 0 ? true : false;
    }
    
    
    /**
     * this will return locale based on lang ...
     * @param lang
     * @return
     */
    public static Locale getLocale(Lang lang) {
    	Locale langLocale = new Locale("en", "US");
    	if (lang.name().equalsIgnoreCase("zh_CN")) langLocale = new Locale("zh","CN");
    	
    	return langLocale;
    }
    
    public static Locale getRequestLocale() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        Locale locale = RequestContextUtils.getLocale(request);
        return locale;
    }
    
    public static void outputJson(Object object, HttpServletRequest request, HttpServletResponse response) {
        String contentType = "text/html;charset=UTF-8";
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");
        Writer writer = null;
        try {
            writer = response.getWriter();
            JsonUtil.toJson(object, writer);
            writer.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static void outputJsonFile(File jsonFile, HttpServletRequest request, HttpServletResponse response) {
        String contentType = "text/html;charset=UTF-8";
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");
        InputStream in = null;
        OutputStream out = null;
        try {
        	in = new FileInputStream(jsonFile);
        	out = response.getOutputStream();
        	IOUtils.copy(in, out);
        	out.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    public static void outputText(Object object, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            String text = object instanceof String ? (String) object : object.toString();
            IOUtils.write(text, out, BaseAppConstants.utf8);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
    
    public static void outputJSJsonP(String callback, Object object, HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/javascript");
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setHeader("Pragma", "no-cache");
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			String text = callback + "(" + JsonUtil.toJson(object) + ")";
			IOUtils.write(text, out, BaseAppConstants.utf8);
			out.flush();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

    public static void outputBinary(String originalFilename, InputStream in, String contentType, HttpServletRequest request, HttpServletResponse response) throws IOException {
    	String agent = request.getHeader("USER-AGENT");
    	String filename;
    	if (-1 != agent.indexOf("MSIE") || -1 != agent.indexOf("Trident")) {
    		filename = URLEncoder.encode(originalFilename, "UTF-8");
		} else {
		    filename = new String(originalFilename.getBytes("UTF-8"), "ISO-8859-1");
		}
    	
    	if (!Strings.isNullOrEmpty(filename)) {
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
        } else {
            response.setHeader("Content-Disposition", "inline");
        }
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

}
