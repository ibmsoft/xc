package com.xzsoft.xsr.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.xzsoft.xsr.core.mapper.AttachmentMapper;
import com.xzsoft.xsr.core.modal.Attachment;
import com.xzsoft.xsr.core.service.AttachmentService;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	@Autowired
	private AttachmentMapper attachmentMapper;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String saveAttachment(FileItem item, String[] params, String userId,
			String fileName) {
		String msg = "";
		try {
			InputStream is = item.getInputStream();
			byte[] fileData = null;
			fileData = FileCopyUtils.copyToByteArray(is);
			Timestamp time = new Timestamp(new Date().getTime());
			String ATTACH_ID = UUID.randomUUID().toString();
			String ATTACH_FILETYPE = fileName.substring(fileName.lastIndexOf(".")+1);//附件类型
			HashMap parametermap = new HashMap();
			parametermap.put("ATTACH_ID", ATTACH_ID);
			parametermap.put("ATTACH_NAME", fileName);
			parametermap.put("ATTACH_STREAM", fileData);
			parametermap.put("ATTACH_FILETYPE", ATTACH_FILETYPE);
			parametermap.put("ATTACH_STATUS", 1);
			parametermap.put("SUIT_ID", params[0]);
			parametermap.put("ENTITY_ID", params[1]);
			parametermap.put("PERIOD_ID", params[2]);
			parametermap.put("CURRENCY_ID", params[3]);
			parametermap.put("MODALTYPE_ID", params[4]);
			parametermap.put("MODALSHEET_ID", params[5]);
			parametermap.put("ROWITEM_ID", params[6]);
			parametermap.put("COLITEM_ID", params[7]);
			parametermap.put("LAST_UPDATED_BY", userId);
			parametermap.put("CREATED_BY", userId);
			parametermap.put("LAST_UPDATE_DATE", time);
			parametermap.put("CREATION_DATE", time);
			
//			attachment.setATTACH_NAME(fileName);
//			attachment.setATTACH_NUM("ATT"+ATTACH_ID.substring(0,10));
//			//attachment.setATTACH_STREAM(Blob(fileData));
//			attachment.setATTACH_FILETYPE(ATTACH_FILETYPE);
//			attachment.setATTACH_STATUS("1");
//			attachment.setSUIT_ID(params[0]);
//			attachment.setENTITY_ID(params[1]);
//			attachment.setPERIOD_ID(params[2]);
//			attachment.setCURRENCY_ID(params[3]);
//			attachment.setMODALTYPE_ID(params[4]);
//			attachment.setMODALSHEET_ID(params[5]);
//			attachment.setROWITEM_ID(params[6]);
//			attachment.setCOLITEM_ID(params[7]);
//			attachment.setLAST_UPDATED_BY(userId);
//			attachment.setCREATED_BY(userId);
//			attachment.setLAST_UPDATE_DATE(time);
//			attachment.setCREATION_DATE(time);
			attachmentMapper.saveAttachment(parametermap);
			
			msg = "附件上传成功!";
		} catch (IOException e) {
			msg = e.getMessage();
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public Attachment getAttachmentById(String attachId) {
		
		return attachmentMapper.getAttachmentById(attachId);
	}
	
	

}
