package com.cubedrive.base.exception;

import java.io.File;

@SuppressWarnings("serial")
public class DocumentConvertError  extends CubeDriveError{
	
	public DocumentConvertError(String message){
		super(message);
	}
	
	public DocumentConvertError(String message, Throwable ex){
		super(message, ex);
	}
	
	
	public DocumentConvertError(File sourceFile, File destFile, String fileType){
		this("Document convert error,source file:"+ sourceFile.getName()+", dest file:"+ destFile.getName()+", file type"+fileType);
	}
	
	public DocumentConvertError(File sourceFile, File destFile, String fileType, Throwable ex){
		this("Document convert error,source file:"+ sourceFile.getName()+", dest file:"+ destFile.getName()+", file type"+fileType, ex);
	}

}
