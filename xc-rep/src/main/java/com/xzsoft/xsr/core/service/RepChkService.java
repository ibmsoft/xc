package com.xzsoft.xsr.core.service;

public interface RepChkService {

	public String[] verifyRepDataUseDB(String[] params) throws Exception;
	
	public String[] verifyRepDataUseRedis(String[] params) throws Exception;
}
