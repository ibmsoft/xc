package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xsr.core.modal.CellData;
import com.xzsoft.xsr.core.modal.CellFormula;
import com.xzsoft.xsr.core.modal.DataDTO;

public interface FjCellFormulaMapper {
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
	public List<CellData> loadValue(String msFormatId,String entity_id,String suitId) throws Exception;
	
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
	public DataDTO getRowByCode(String suit_id,String code)throws Exception;
	/**
	 * 查询列指标信息
	 * @param ls
	 * @return
	 */
	public DataDTO getColByCode(String suit_id,String code)throws Exception;
	/**
	 * 查询行指标name
	 * @param ls
	 * @return
	 */
	public String getRowNameById(String suit_id,String id)throws Exception;
	/**
	 * 查询列指标name
	 * @param ls
	 * @return
	 */
	public String getColNameById(String suit_id,String id)throws Exception;
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
	public List<CellFormula> getRepFormulaList(HashMap<String, String> params) throws Exception;
	/**
	 * 通过模板ID查询出该模板的固定公式，单位级公式优先，供报表生成/数据采集调用， 如果fType=‘true’,只查询REP类型公式
	 * by songyh 2016-01-27
	 * @param modalsheetId
	 * @param suitId
	 * @param isOnlyImpRepFormula
	 * @return
	 * @throws Exception
	 */
	public List<CellFormula> getCalFormulaList(HashMap<String, String> params) throws Exception;
	/**
	 * 通过模板id，删除该模板上的公共级采集公式
	 * @param modalsheetId
	 * @param suitId
	 * @throws Exception
	 */
	public void deleteCalFormula(HashMap<String, String> params) throws Exception;	
}
