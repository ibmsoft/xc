/**
 * 
 */
package com.xzsoft.xc.rep.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.rep.modal.CellFormula;
import com.xzsoft.xc.rep.modal.ReportCellFormula;
import com.xzsoft.xc.rep.modal.ReportSheetBean;
import com.xzsoft.xc.rep.modal.ReportTabBean;
import com.xzsoft.xc.rep.modal.ReportTabCellBean;
import com.xzsoft.xc.rep.modal.ReportTabElementBean;

/**
 * @className XCReportModuleMapper
 * @describe  报表采集公式Mapper接口
 * @author    tangxl
 * @date      2016-09-02
 */
public interface XCReportModuleMapper {
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年9月2日
	 * @describe    根据科目体系获取报表模板
	 * @param       accHrcyId
	 * @param       tabOrder
	 * @return
	 * @变动说明      <li>tabOrder 暂时废除，不起作用  </li>
	 * @version     1.0
	 */
	public List<ReportTabBean> getTabByAccHrcyId(@Param(value="accHrcyId")String accHrcyId,@Param(value="tabOrder")int tabOrder);
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年9月2日
	 * @describe    根据模板id获取模板列
	 * @param       tabId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<ReportTabCellBean> getCellByTabId(@Param(value="tabId")String tabId,@Param(value="tabNo")int tabNo);
	/**
	 * 
	 * @methodName  getElementByTabId
	 * @author      tangxl
	 * @date        2016年9月2日
	 * @describe    根据模板id获取模板列格式
	 * @param       tabId
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<ReportTabElementBean> getElementByTabId(@Param(value="tabId")String tabId,@Param(value="tabNo")int tabNo);
	/**
	 * 
	 * @methodName  getFormulaByTabId
	 * @author      tangxl
	 * @date        2016年9月2日
	 * @describe    获取单元格的公式
	 * @param map
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<ReportCellFormula> getFormulaByTabId(HashMap<String, String> map);
	/**
	 * 
	 * @methodName  getRepValue
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    指标间取数公式解析
	 * @param 		map
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public String getRepValue(Map<String, String> map);
	/**
	 * 
	 * @methodName  getCellFunction
	 * @author      tangxl
	 * @date        2016年9月6日
	 * @describe    自定义函数解析
	 * @param 		funcCode<唯一索引 xc_rep_func>
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public HashMap<String, String> getCellFunction(@Param(value="funcCode")String funcCode) throws Exception;
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
	 * @methodName  getSheetById
	 * @author      tangxl
	 * @date        2016年9月14日
	 * @describe    获取已生成的报表
	 * @param 		sheetIdList
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<ReportSheetBean> getSheetById(List<String> sheetIdList);
	/**
	 * 
	 * @methodName  getCellValByTabId
	 * @author      tangxl
	 * @date        2016年9月14日
	 * @describe    获取单元格的内容
	 * @param map
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<ReportCellFormula> getCellValByTabId(HashMap<String, String> map);
	
	/**
	 * @Title: getCellFormulas 
	 * @Description: 查询报表取数公式信息
	 * @param map
	 * @return    设定文件
	 */
	public List<CellFormula> getCellFormulas(HashMap<String,?> map) ;
	
}
