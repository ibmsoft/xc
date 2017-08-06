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
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.cubedrive.base.utils.BaseFileUtil;
import com.cubedrive.base.utils.MimeTypes;

@SuppressWarnings("serial")
public class UserFileServlet extends HttpServlet {

	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String userFilePath = req.getPathInfo();
		File userFile = BaseFileUtil.getFileBaseUserFilePath(userFilePath);
		String contentType = MimeTypes.getMimeType(FilenameUtils.getExtension(userFile.getName()));
		OutputStream output = resp.getOutputStream();
		try {
			resp.setContentType(contentType);
			FileUtils.copyFile(userFile, output);
		} finally {
			IOUtils.closeQuietly(output);
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
