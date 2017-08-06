package com.xzsoft.xsr.core.service;

import java.util.List;

public interface ImpAndExpModalService {

	public List<String> exportModal(String[] params) throws Exception;
	
	public String getImpModalsheetList(String filePath) throws Exception;
	
	public void receiveModal(String[] params) throws Exception;
}
