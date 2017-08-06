package com.xzsoft.xc.rep.dao;

import java.util.HashMap;
import java.util.Map;

import com.xzsoft.xc.rep.modal.CellFormula;

/**
 * @className XCReportModuleDao
 * @describe  报表采集公式设置
 * @author    tangxl
 * @date      2016-09-02
 */
public interface XCReportModuleDao {
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
	 * @Title: getTabFormulas
	 * @Description: 查询单表取数公式信息 
	 * @param accHrcyId
	 * @param ledgerId
	 * @param tabId
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,CellFormula> getTabFormulas(String accHrcyId,String ledgerId,String tabId) throws Exception ;
	
	/**
	 * @methodName  getRepValue
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    指标间公式解析
	 * @param 		map
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public String getRepValue(Map<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  getCellFunction
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    自定义函数解析
	 * @param 		funcCode
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public HashMap<String, String> getCellFunction(String funcCode) throws Exception;
	/**
	 * 
	 * @methodName  excuteProcedure
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    执行存储过程
	 * @param       map
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public String excuteProcedure(Map<String,String> map) throws Exception;
	/**
	 * 
	 * @methodName  loadSheet
	 * @author      tangxl
	 * @date        2016年9月14日
	 * @describe    加载报表
	 * @param		sheetIds<所有的报表>
	 * @param       querySheetId<要展示的报表>
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public Map<String, Object> loadSheet(String sheetIds,String querySheetId) throws Exception;
}
