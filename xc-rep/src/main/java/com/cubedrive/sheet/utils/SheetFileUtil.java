package com.cubedrive.sheet.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cubedrive.base.BaseAppConstants;
import com.cubedrive.base.utils.BaseFileUtil;
import com.cubedrive.sheet.SheetAppConstants;

public class SheetFileUtil {

	public static File getUserExcelDir(String userId) {
		File excelDir = new File(BaseFileUtil.getUserHomeDir(userId), SheetAppConstants.USER_EXCEL_DIR);
		if (!excelDir.exists())
			excelDir.mkdir();
		return excelDir;
	}
	
	public static File getUserAttachDir(String userId) {
		File attachDir = new File(BaseFileUtil.getUserHomeDir(userId), SheetAppConstants.SHEET_ATTACHMENT_DIR);
		if (!attachDir.exists())
			attachDir.mkdir();
		return attachDir;
	}
	
	public static File placeUploadExcel(String userId, MultipartFile excelFile)
			throws IOException {
		File excelUploadDir = getUserExcelUploadDir(userId);
		String filename = FilenameUtils.getName(excelFile.getOriginalFilename());
		File _file = new File(excelUploadDir, filename);
		FileUtils.copyInputStreamToFile(excelFile.getInputStream(), _file);
		return _file;
	}
	
	public static File placeUserFileAttachment(MultipartFile uploadFile, Integer userId, Integer documentId)
			throws IOException {
		
		File userAttachmentDir =  getUserAttachDir(userId.toString());
	    File saveDir;
	    if(documentId != null){
	        saveDir = new File(userAttachmentDir, documentId.toString());
	        if(!saveDir.exists()){
	            saveDir.mkdir();
	        }
	    }else{
	        saveDir = userAttachmentDir;
	    }
	    String filename =  System.nanoTime() +"." + FilenameUtils.getExtension(uploadFile.getOriginalFilename());
        File _file = new File(saveDir, filename);   
        FileUtils.copyInputStreamToFile(uploadFile.getInputStream(), _file);
        return _file;
	}

	public static File getUserExcelPictureDir(String userId) {
		File excelPictureDir = new File(getUserExcelDir(userId), "pictures");
		if (!excelPictureDir.exists())
			excelPictureDir.mkdir();
		return excelPictureDir;
	}

	public static File getUserExcelPictureDirByDocument(String userId,
			String documentId) {
		File documentPictureDir = new File(getUserExcelPictureDir(userId),
				documentId);
		if (!documentPictureDir.exists())
			documentPictureDir.mkdir();
		return documentPictureDir;
	}

	public static File getUserExcelUploadDir(String userId) {
		File excelUploadDir = new File(getUserExcelDir(userId), "upload");
		if (!excelUploadDir.exists())
			excelUploadDir.mkdir();
		return excelUploadDir;
	}

	public static File getUploadExcel(String userId, String filename) {
		File excelUploadDir = getUserExcelUploadDir(userId);
		File _file = new File(excelUploadDir, filename);
		return _file;
	}
}
