package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.CellFormula;
import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.DataDTO;
import com.xzsoft.xsr.core.modal.Rowitem;

public interface CellFormulaMapper {
	/**
	 * 批量插入采集公式
	 * @param formatCells
	 * @throws Exception
	 */
	public void insertBatchCellFormat(@Param(value="formatCells") List<CellFormula> formatCells) throws Exception;
	
	/**
	 * 根据行列指标删除采集公式
	 * @param rowitemId
	 * @param colitemId
	 * @throws Exception
	 */
	public void deleteByRC(String rowitemId,String colitemId,String entity,String suit_id)throws Exception;

	/**
	 * 根据行列指标查找采集公式是否存在
	 * @param rowitemId
	 * @param colitemId
	 * @throws Exception
	 */
	public int isExistByRC(String rowitemId,String colitemId,String entity,String suit_id)throws Exception;
	/**
	 * 根据行列指标查公式ID
	 * @param rowitemId
	 * @param colitemId
	 * @throws Exception
	 */
	public String selectIdByRC(String rowitemId,String colitemId,String entity,String suit_id)throws Exception;
	
	/**
	 * 根据行列指标更新公式
	 * @param rowitemId
	 * @param colitemId
	 * @throws Exception
	 */
	public void updateByID(CellFormula cellFormula)throws Exception;
	/**
	 * 根据MSFORMAT_ID加载数据单元格
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public List<CellData> loadValue(String msFormatId,String entity_id,String suitId,@Param("dbType")String dbType) throws Exception;
	
	/**
	 * 查询字典表中的期间信息
	 * @return
	 * @throws Exception
	 */
	public List<DataDTO> getDictPeriod() throws Exception;

	/**
	 * 查询期间信息
	 * @param ls
	 * @return
	 */
	public DataDTO getPeriodByCode(String suit_id,String code)throws Exception;
	/**
	 * 查询公司信息
	 * @param ls
	 * @return
	 */
	public DataDTO getEntityByCode(String suit_id,String code)throws Exception;
	
	/**
	 * 查询行指标信息
	 * @param ls
	 * @return
	 */
	public DataDTO getRowByCode(String suit_id,String code,@Param("dbType")String dbType)throws Exception;
	/**
	 * 查询列指标信息
	 * @param ls
	 * @return
	 */
	public DataDTO getColByCode(String suit_id,String code,@Param("dbType")String dbType)throws Exception;
	/**
	 * 查询行指标name
	 * @param ls
	 * @return
	 */
	public String getRowNameById(String suit_id,String id,@Param("dbType")String dbType)throws Exception;
	/**
	 * 查询列指标name
	 * @param ls
	 * @return
	 */
	public String getColNameById(String suit_id,String id,@Param("dbType")String dbType)throws Exception;
	/**
	 * 查询币种信息
	 * @param ls
	 * @return
	 */
	public DataDTO getCnyByCode(String suit_id,String code)throws Exception;
	
	/**
	 * 查询指标公式值
	 * @param ls
	 * @return
	 */
	public String getRepValue(Map map)throws Exception;
	/**
	 * 查询FUN_DB_SUB
	 * @param ls
	 * @return
	 */
	public String getFUN_DB_SUB(String FUN_CODE)throws Exception;
	/**
	 * 调用测试存储过程
	 * @param ls
	 * @return
	 */
	public String testByProc(Map map)throws Exception;
	/**
	 * 通过模板ID查询出该模板的公共级公式, 如果isOnlyImpRepFormula=‘true’,只导出REP类型公式
	 * @param modalsheetId
	 * @param suitId
	 * @param isOnlyImpRepFormula
	 * @return
	 * @throws Exception
	 */
	public List<CellFormula> getCalFormulaList(HashMap<String, String> params) throws Exception;
	/**
	 * 通过模板ID查询出该模板的固定公式，单位级公式优先，供报表生成/数据采集调用， 如果fType=‘true’,只查询REP类型公式
	 * by songyh 2016-01-27
	 * @param modalsheetId
	 * @param suitId
	 * @param isOnlyImpRepFormula
	 * @return
	 * @throws Exception
	 */
	public List<CellFormula> getRepFormulaList(HashMap<String, String> params) throws Exception;
	/**
	 * 通过模板id，删除该模板上的公共级采集公式
	 * @param modalsheetId
	 * @param suitId
	 * @throws Exception
	 */
	public void deleteCalFormula(HashMap<String, String> params) throws Exception;
	/**
	 * 通过表套id,行指标id,列指标id,公司id
	 * 查询系统中是否存在某条采集公式
	 * 当查询公共级公式时，公司id为-1
	 * @param suitId
	 * @param rowitemId
	 * @param colitemId
	 * @param entityId
	 * @return
	 * @throws Exception
	 */
	public String getCalFormulaId(@Param(value = "suitId") String suitId,
			@Param(value = "rowitemId") String rowitemId,
			@Param(value = "colitemId") String colitemId,
			@Param(value = "entityId") String entityId) throws Exception;
	/**
	 * 批量更新采集公式
	 * @param cellFormulas
	 * @throws Exception
	 */
	public void updateBatchCalFormula(@Param(value="cellFormulas") List<CellFormula> cellFormulas) throws Exception;
	
	public String getPeriodCodeById(String periodId) throws Exception;
	public String getDimCodeCloumn(String key,String DC_Id) throws Exception;
	public String getDcVal(HashMap params) throws Exception;
	public HashMap getFunInfo(String FUN_CODE)throws Exception;
	public String getDcCashVal(HashMap params) throws Exception;

	public String getDcIdByOrg(@Param(value = "suitId") String suitId,@Param(value = "entityId") String entity_id) throws Exception;

	public String getDefaultDcId(String suitId);
	
	/**
	 * @Title: getRowItemById 
	 * @Description: 查询行指标信息
	 * @param rowItemId
	 * @return    设定文件
	 */
	public Rowitem getRowItemById(String rowItemId)  ;
	
	/**
	 * @Title: getColitemById 
	 * @Description: 查询列指标信息
	 * @param colItemId
	 * @return    设定文件
	 */
	public Colitem getColitemById(String colItemId) ;
}
