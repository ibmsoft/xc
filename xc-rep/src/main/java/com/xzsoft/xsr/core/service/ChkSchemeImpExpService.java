package com.xzsoft.xsr.core.service;

import java.io.File;

/**
 * @接口名 : ChkSchemeImpExpService
 * @author: liuwl
 * @date: 2016年1月5日
 */
public interface ChkSchemeImpExpService {

	/**
	 * 稽核方案导出
	 * 
	 * @param filepath
	 * @param suitId
	 * @param schemeList
	 * @param request
	 */
	public void ChkSchemeExp(String filepath, String chk_scheme_id) throws Exception;

	/**
	 * 稽核方案导入
	 * 
	 * @param file
	 * @param suitId
	 */
	public String ChkSchemeImp(File file, String suitId) throws Exception;
}
