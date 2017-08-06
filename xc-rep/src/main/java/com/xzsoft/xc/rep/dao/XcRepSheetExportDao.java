
package com.xzsoft.xc.rep.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.rep.modal.ReportSheetBean;


public interface XcRepSheetExportDao {
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
	public Object exportSheetExcelZip(HashMap<String, String> paramsMap) throws Exception;
	/**
	 * 
	 * @methodName  getSheetListByIds
	 * @author      tangxl
	 * @date        2016年9月21日
	 * @describe    根据sheetId获取报表
	 * @param 		sheetIds
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<ReportSheetBean> getSheetListByIds(String sheetIds) throws Exception;
}
