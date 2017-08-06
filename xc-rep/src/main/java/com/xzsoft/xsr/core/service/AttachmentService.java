package com.xzsoft.xsr.core.service;

import org.apache.commons.fileupload.FileItem;

import com.xzsoft.xsr.core.modal.Attachment;

public interface AttachmentService {

	/**
	 * 上传报表附件
	 * @param item
	 * @param params
	 * @param userId
	 * @param fileName
	 * @return
	 */
	public String saveAttachment(FileItem item, String[] params, String userId,
			String fileName);

	public Attachment getAttachmentById(String attachId);

}
