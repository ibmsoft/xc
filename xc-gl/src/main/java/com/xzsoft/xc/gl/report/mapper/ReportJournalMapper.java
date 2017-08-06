package com.xzsoft.xc.gl.report.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.gl.report.modal.JournalLedgerReport;

/**
 *@author tangxl
 *@describe 序时账Mapper
 */
public interface ReportJournalMapper {
	/**
	 * 
	 * @methodName  getJournalLedgerData
	 * @author      tangxl
	 * @date        2016年9月23日
	 * @describe    TODO
	 * @param 		map
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<JournalLedgerReport> getJournalLedgerData(HashMap<String, String> map) throws Exception;
	/**
	 * 
	 * @methodName  getJournalLedgerTitle
	 * @author      tangxl
	 * @date        2016年9月23日
	 * @describe    获取时序账的标题
	 * @param 		map
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public List<HashMap<String, String>> getJournalLedgerTitle(HashMap<String, String> map) throws Exception;
	
}
