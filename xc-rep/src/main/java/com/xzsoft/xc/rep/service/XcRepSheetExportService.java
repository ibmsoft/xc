package com.xzsoft.xc.rep.service;

import java.util.HashMap;

public interface XcRepSheetExportService {
	/**
	 * 
	 * @methodName  exportSheetExcelZip
	 * @author      tangxl
	 * @date        2016年9月21日
	 * @describe    打包导出Excel报表
	 * @param 		paramsMap
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public Object exportSheetExcelZip(HashMap<String, Object> paramsMap) throws Exception;
}
