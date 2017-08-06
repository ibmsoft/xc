/**
 * 
 */
package com.xzsoft.xc.rep.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.rep.modal.CellFormula;
import com.xzsoft.xc.rep.modal.CellPreFormula;
import com.xzsoft.xc.rep.modal.DataDTO;

/**
 * @className XCRepFormulaManageDao
 * @describe  报表公式处理取数接口
 * @author    tangxl
 * @date      2016-08-04
 */
public interface XCRepFormulaManageDao {
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    判断报表单元格公式是否存在
	 * @param 		rowitemId
	 * @param 		colitemId
	 * @param 		ledgerId
	 * @param 		accHrcyId
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public String checkFormulaIsExist(String rowitemId, String colitemId,String ledgerId,String accHrcyId) throws Exception;
	/**
	 * 
	 * @methodName  getPeriodByCode
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    获取期间信息
	 * @param       accHrcyId
	 * @param       code
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public DataDTO getPeriodByCode(String accHrcyId,String code)throws Exception;
	/**
	 * 
	 * @methodName  getEntityByCode
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    获取期间信息
	 * @param       accHrcyId
	 * @param       entityCode
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public DataDTO getEntityByCode(String accHrcyId,String entityCode)throws Exception;
	
	/**
	 * @Title: getLedgerByCode 
	 * @Description: 根据编码获取账簿信息
	 * @param ledgerCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public DataDTO getLedgerByCode(String ledgerCode)throws Exception;
	
	/**
	 * 
	 * @methodName  getRowByCode
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    获取行指标信息
	 * @param 		accHrcyId
	 * @param 		code
	 * @param 		dbType
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public DataDTO getRowByCode(String accHrcyId,String code,String dbType)throws Exception;
	/**
	 * 
	 * @methodName  getColByCode
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    获取列指标信息
	 * @param 		suitId
	 * @param 		code
	 * @param 		dbType
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public DataDTO getColByCode(String accHrcyId,String code,String dbType)throws Exception;
	/**
	 * 
	 * @methodName  getCnyByCode
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    获取币种信息
	 * @param 		accHrcyId
	 * @param 		code
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public DataDTO getCnyByCode(String accHrcyId,String code)throws Exception;
	/**
	 * 
	 * @methodName  saveCellFormula
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    保存报表公式信息
	 * @param 		updateList
	 * @param 		insertList
	 * @param 		delList
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void saveCellFormula(List<CellFormula> updateList,List<CellFormula> insertList,List<HashMap<String, String>> delList) throws Exception;
	/**
	 * 
	 * @methodName  savePreCellFormula
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    保存公式的预处理信息
	 * @param preFormulaList
	 * @param updateList
	 * @param delList
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void savePreCellFormula(List<CellPreFormula> preFormulaList) throws Exception;
	
	/**
	 * @Title: delPreCellFormula 
	 * @Description:  删除公式的预处理信息
	 * @param updateList
	 * @param delList
	 * @throws Exception    设定文件
	 */
	public void delPreCellFormula(List<CellFormula> updateList,List<HashMap<String, String>> delList) throws Exception;

	/**
	 * 
	 * @methodName  judgetFormulaType
	 * @author      tangxl
	 * @date        2016年8月11日
	 * @describe    判断函数类型
	 * @param 		params
	 * @param 		judgeType
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public int judgetFormulaType(String params,String judgeType) throws Exception;
	/**
	 * 
	 * @methodName  queryTranslateName
	 * @author      tangxl
	 * @date        2016年10月18日
	 * @describe    TODO
	 * @param 		querySql
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public HashMap<String, String> queryTranslateName(String querySql)throws Exception;
}
