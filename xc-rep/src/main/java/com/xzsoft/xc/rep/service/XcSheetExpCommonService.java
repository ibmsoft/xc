package com.xzsoft.xc.rep.service;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.cubedrive.base.domain.document.DocumentFile;
import com.cubedrive.sheet.domain.sheet.SheetCell;
import com.cubedrive.sheet.domain.sheet.SheetTab;
import com.cubedrive.sheet.domain.sheet.SheetTabElement;

public interface XcSheetExpCommonService {
	/**
	 * 
	 * @methodName  exportSheetByCondition
	 * @author      tangxl
	 * @date        2016年9月21日
	 * @describe    按条件导出报表excel
	 * @param 		documentFile
	 * @param 		userLocale
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public File exportSheetByCondition(DocumentFile documentFile, Locale userLocale) throws Exception;
	/**
	 * 
	 * @methodName  getFixedCells
	 * @author      tangxl
	 * @date        2016年9月21日
	 * @describe    去报表的固定单元格
	 * @param 		sheetTab
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<SheetCell> getFixedCells(SheetTab sheetTab);
	/**
	 * 
	 * @methodName  findElementsByTabId
	 * @author      tangxl
	 * @date        2016年9月21日
	 * @describe    TODO
	 * @param 		tabId
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<SheetTabElement> findElementsByTabId(String tabId);
}
