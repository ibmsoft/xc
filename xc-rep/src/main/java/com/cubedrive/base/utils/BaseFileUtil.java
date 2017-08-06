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
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cubedrive.base.BaseAppConfiguration;
import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.domain.document.DocumentFile;

public class BaseFileUtil {
	
	public static File getUserSubDir(String userId, String type) {
		File editorDir = new File(getUserHomeDir(userId), type);
		if (!editorDir.exists())
			editorDir.mkdir();
		return editorDir;
	}
	
	public static File getUserHomeDir(String userId) {
		File userDir = new File(BaseAppConfiguration.instance().getHomeDir(), userId);
		if (!userDir.exists())
			userDir.mkdir();
		return userDir;
	}
	
	public static String getPathUnderUserFileServer(File file) {
		String path = file.getAbsolutePath().replace('\\', '/');
		return BaseAppConfiguration.instance().getUserFileServer() 
				+ path.substring(BaseAppConfiguration.instance().getHomeDir().getAbsolutePath().length());
	}
	
	public static String getUserImageFullPath(String imagePath) {
		if (imagePath == null) {
			imagePath = "images/people.png";
		} else if (!imagePath.equals("images/people.png")) {
			imagePath = BaseFileUtil.getPathUnderUserFileServer(imagePath);
		}
		return imagePath;
	}
	
	public static File getPhotoDir() {
		File photolDir = new File(BaseAppConfiguration.instance().getHomeDir(), "photo");
		if (!photolDir.exists())
			photolDir.mkdir();
		return photolDir;
	}
	
	public static File getUserAttachmentDir(){
	    File attachmentDir = new File(BaseAppConfiguration.instance().getHomeDir(), "attachment");
        if (!attachmentDir.exists())
            attachmentDir.mkdir();
        return attachmentDir;
	}
	
	//==============================================================
	public static String getPathUnderUserFileServer(String path) {
		return BaseAppConfiguration.instance().getUserFileServer() + path.replace('\\', '/');
	}

	public static File getFileBaseUserFilePath(String path) {
		return new File(BaseAppConfiguration.instance().getHomeDir(), path);
	}
	


	public static File createTempDir(String dirname) {
		File tempDir = new File(BaseAppConfiguration.instance().getTempDir(), dirname);
		if (!tempDir.exists())
			tempDir.mkdir();
		return tempDir;
	}
	
	public static File placeUploadFile(DocumentFile documentFile, MultipartFile uploadFile)
			throws IOException {
		File userUploadDir = BaseFileUtil.getUserSubDir(documentFile.getAuthor().getId().toString(), BaseAppConstants.USER_UPLOAD_DIR);	
		File _folder = new File(userUploadDir, documentFile.getId().toString());
		if (!_folder.exists()) _folder.mkdir();
		
		String filename = FilenameUtils.getName(uploadFile.getOriginalFilename());
		filename = BaseFileUtil.cleanFileName(filename);	
		File _file = new File(_folder, filename);	
		FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), _file);
		return _file;
	}
	
	public static File placeUploadFile(DocumentFile documentFile, File origFile)
			throws IOException {
		File userUploadDir = BaseFileUtil.getUserSubDir(documentFile.getAuthor().getId().toString(), BaseAppConstants.USER_UPLOAD_DIR);	
		File _folder = new File(userUploadDir, documentFile.getId().toString());
		if (!_folder.exists()) _folder.mkdir();
		
		String filename = FilenameUtils.getName(origFile.getName());
		filename = BaseFileUtil.cleanFileName(filename);	
		File _file = new File(_folder, filename);	
		FileUtils.copyFile(origFile, _file);
		return _file;
	}
	
	
//	public static File getUploadFile(DocumentFile documentFile)
//			throws IOException {
//		File userUploadDir = BaseFileUtil.getUserSubDir(documentFile.getAuthor().getId().toString(), BaseAppConstants.USER_UPLOAD_DIR);	
//		File _folder = new File(userUploadDir, documentFile.getId().toString());
//		
//		File _file = null;
//		if (_folder.exists()) {
//			if (documentFile.getSavedLoc() != null) _file = new File(_folder, documentFile.getSavedLoc());
//			if (_file == null || !_file.exists()) _file = new File(_folder, documentFile.getName());
//		}
//		
//		return _file;
//	}

	public static String cleanFileName(String badFileName) {
		final int[] illegalChars = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,32, 34, 42, 47, 58, 60, 62, 63, 92, 124};
		StringBuilder cleanName = new StringBuilder();
		for (int i = 0; i < badFileName.length(); i++) {
			int c = (int)badFileName.charAt(i);
			if (Arrays.binarySearch(illegalChars, c) < 0) {
				cleanName.append((char)c);
			}
		}
		return cleanName.toString();
	} 
	
	

}
