package com.xzsoft.xc.rep.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xc.rep.modal.DataDTO;


/**
 * @ClassName: XCRepFormulaMapper 
 * @Description: 报表公式设置Mapper
 * @author linp
 * @date 2016年8月4日 上午10:58:34 
 *
 */
public interface XCRepFormulaMapper {
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    判断报表单元格公式是否存在
	 * @param 		rowitemId
	 * @param 		colitemId
	 * @param 		entityId
	 * @param 		suitId
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public String checkFormulaIsExist(@Param(value="rowitemId")String rowitemId, @Param(value="colitemId")String colitemId,@Param(value="ledgerId")String ledgerId,@Param(value="accHrcyId")String accHrcyId) throws Exception;
	/**
	 * 
	 * @methodName  getPeriodByCode
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    获取期间信息
	 * @param       suitId
	 * @param       code
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public DataDTO getPeriodByCode(@Param(value="accHrcyId")String accHrcyId,@Param(value="code")String code)throws Exception;
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
	public DataDTO getEntityByCode(@Param(value="accHrcyId")String accHrcyId,@Param(value="entityCode")String entityCode)throws Exception;
	
	/**
	 * @Title: getLedgerByCode 
	 * @Description: 账簿信息
	 * @param ledgerCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public DataDTO getLedgerByCode(@Param(value="ledgerCode")String ledgerCode)throws Exception;
	
	/**
	 * 
	 * @methodName  getRowByCode
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    获取行指标信息
	 * @param 		suitId
	 * @param 		code
	 * @param 		dbType
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public DataDTO getRowByCode(@Param(value="accHrcyId")String accHrcyId,@Param(value="code")String code,@Param(value="dbType")String dbType)throws Exception;
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
	public DataDTO getColByCode(@Param(value="accHrcyId")String accHrcyId,@Param(value="code")String code,@Param(value="dbType")String dbType)throws Exception;
	/**
	 * 
	 * @methodName  getCnyByCode
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    获取币种信息
	 * @param 		suitId
	 * @param 		code
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public DataDTO getCnyByCode(@Param(value="accHrcyId")String accHrcyId,@Param(value="code")String code)throws Exception;
	/**
	 * 
	 * @methodName  insertCellFormula
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    新增公式
	 * @param       map
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertCellFormula(HashMap<String, Object> map) throws Exception;
	/**
	 * 
	 * @methodName  updateCellFormula
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    修改公式
	 * @param       map
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void updateCellFormula(HashMap<String, Object> map) throws Exception;
	/**
	 * 
	 * @methodName  deleteCellFormula
	 * @author      tangxl
	 * @date        2016年8月4日
	 * @describe    清除公式
	 * @param       map
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public void deleteCellFormula(HashMap<String, Object> map) throws Exception;
	/**
	 * 
	 * @methodName  insertPreFormula
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    预处理公式插入
	 * @param map
	 * @变动说明       
	 * @version     1.0
	 */
	public void insertPreFormula(HashMap<String, Object> map);
	/**
	 * 
	 * @methodName  delPreFormulaByBean
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    预处理公式删除
	 * @param       map
	 * @变动说明       
	 * @version     1.0
	 */
	public void delPreFormulaByBean(HashMap<String, Object> map);
	/**
	 * 
	 * @methodName  delPreFormulaByMap
	 * @author      tangxl
	 * @date        2016年8月5日
	 * @describe    预处理公式删除
	 * @param       map
	 * @变动说明       
	 * @version     1.0
	 */
	public void delPreFormulaByMap(HashMap<String, Object> map);
	/**
	 * 
	 * @methodName  judgetFormulaType
	 * @author      tangxl
	 * @date        2016年8月11日
	 * @describe    判断函数类型
	 * @param params
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public int judgetFormulaType(@Param(value="params")String params,@Param(value="judgeType")String judgeType) throws Exception;
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
	public HashMap<String, String> queryTranslateName(@Param(value="querySql")String querySql);
}
