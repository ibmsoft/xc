package com.xzsoft.xsr.core.modal;

public class Attachment {
	private String ATTACH_ID;      //附件ID
	private String ATTACH_NAME;    //'附件名称',
	private String ATTACH_NUM;     //'附件编号：  规则ATT+10位长度的附件ID',
	private byte[] ATTACH_STREAM;
	private String ATTACH_FILETYPE;//'附件类型',
	private String ATTACH_STATUS;  //'附件状态：0-保存 ，1-提交 2、已上久其',
	public String getATTACH_ID() {
		return ATTACH_ID;
	}
	public void setATTACH_ID(String aTTACH_ID) {
		ATTACH_ID = aTTACH_ID;
	}
	public String getATTACH_NAME() {
		return ATTACH_NAME;
	}
	public void setATTACH_NAME(String aTTACH_NAME) {
		ATTACH_NAME = aTTACH_NAME;
	}
	public String getATTACH_NUM() {
		return ATTACH_NUM;
	}
	public void setATTACH_NUM(String aTTACH_NUM) {
		ATTACH_NUM = aTTACH_NUM;
	}
	public byte[] getATTACH_STREAM() {
		return ATTACH_STREAM;
	}
	public void setATTACH_STREAM(byte[] aTTACH_STREAM) {
		ATTACH_STREAM = aTTACH_STREAM;
	}
	public String getATTACH_FILETYPE() {
		return ATTACH_FILETYPE;
	}
	public void setATTACH_FILETYPE(String aTTACH_FILETYPE) {
		ATTACH_FILETYPE = aTTACH_FILETYPE;
	}
	public String getATTACH_STATUS() {
		return ATTACH_STATUS;
	}
	public void setATTACH_STATUS(String aTTACH_STATUS) {
		ATTACH_STATUS = aTTACH_STATUS;
	}
	

}
