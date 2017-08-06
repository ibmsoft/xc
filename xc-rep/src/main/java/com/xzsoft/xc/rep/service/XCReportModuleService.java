package com.xzsoft.xc.rep.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @className XCReportModuleService
 * @describe  报表采集公式设置
 * @author    tangxl
 * @date      2016-09-02
 */
public interface XCReportModuleService {
	/**
	 * 
	 * @methodName  loadReportModule
	 * @author      tangxl
	 * @date        2016年9月2日
	 * @describe    加载公式设置模板
	 * @param       map
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public Map<String, Object> loadReportModule(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  translateFormula
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    公式翻译
	 * @param       formulaText
	 * @param       accHrcyId
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public String translateFormula(String formulaText,String accHrcyId,String ledgerId) throws Exception;
	/**
	 * 
	 * @methodName  formulaTest
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    公式测试
	 * @param       map
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public String formulaTest(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  loadSheet
	 * @author      tangxl
	 * @date        2016年9月14日
	 * @describe    加载报表
	 * @param		sheetIds
	 * @param       querySheetId 
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public Map<String, Object> loadSheet(String sheetIds,String querySheetId) throws Exception;
	
}
