package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;

import com.xzsoft.xsr.core.modal.Attachment;


public interface AttachmentMapper {

	@SuppressWarnings("rawtypes")
	public void saveAttachment(HashMap params);

	public Attachment getAttachmentById(String attachId);


}
