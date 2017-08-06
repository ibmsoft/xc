package com.xzsoft.xsr.core.service;

import java.io.File;

public interface DictService {

	/**
	 * 字典导出
	 * @param filepath
	 * @param dictName
	 * @throws Exception
	 */
	void dictExp(String filepath, String dictName) throws Exception;

	/**
	 * 字典导入
	 * @param file
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	String dictImpt(File folder, String userId) throws Exception;

}
