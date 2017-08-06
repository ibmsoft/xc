package com.xzsoft.xsr.core.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xsr.core.modal.CheckFormula;
import com.xzsoft.xsr.core.modal.Chk_Scheme;


public interface CheckFormulaMapper {
	/**
	 * 通过模板id和表套id，获取模板对应的稽核公式
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<CheckFormula> getCheckFormulaList(HashMap<String, String> params) throws Exception;
	/**
	 * 删除稽核方案明细中一张模板涉及的公式
	 * @param params
	 * @throws Exception
	 */
	public void deleteChkSchemeDtl(HashMap<String, String> params) throws Exception;
	/**
	 * 删除一张模板上的稽核公式
	 * @param params
	 * @throws Exception
	 */
	public void deleteCheckFormula(HashMap<String, String> params) throws Exception;
	/**
	 * 批量插入稽核公式
	 * @param formulas
	 * @throws Exception
	 */
	public void insertBatchCheckFormat(@Param(value="formulas") List<CheckFormula> formulas) throws Exception;
	/**
	 * 根据稽核方案id查询稽核方案
	 * 
	 * @param chk_scheme_id
	 * @throws Exception
	 */
	public Chk_Scheme selectChkSchemeById(String chk_scheme_id) throws Exception;

	/**
	 * 根据稽核方案id查询稽核公式
	 * 
	 * @param chk_scheme_id
	 * @throws Exception
	 */
	public ArrayList<CheckFormula> selectChkFormulaByScheme(String chk_scheme_id) throws Exception;

	/**
	 * 根据行指标id查询行指标code
	 * 
	 * @param Rowitem_id
	 * @throws Exception
	 */
	public String selectRowitem_Code(String Rowitem_id) throws Exception;

	/**
	 * 根据列指标id查询列指标code
	 * 
	 * @param Colitem_id
	 * @throws Exception
	 */
	public String selectColitem_Code(String Colitem_id) throws Exception;

	/**
	 * 根据行指标code查询行指标id
	 * 
	 * @param Rowitem_code
	 * @throws Exception
	 */
	public String selectRowitem_Id(String Rowitem_code, String SUIT_ID) throws Exception;

	/**
	 * 根据列指标code查询列指标id
	 * 
	 * @param Colitem_code
	 * @throws Exception
	 */
	public String selectColitem_Id(String Colitem_code, String SUIT_ID) throws Exception;

	/**
	 * 判断稽核方案是否存在
	 * 
	 * @param CHK_SCHEME_CODE,SUIT_ID
	 * @return
	 * @throws Exception
	 */
	public int isChkSchemeExist(String CHK_SCHEME_CODE, String SUIT_ID) throws Exception;

	/**
	 * 判断稽核公式的行指标在系统中是否存在
	 * 
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public int isRowItemExit(String ROWITEM_ID, String SUIT_ID) throws Exception;

	/**
	 * 判断稽核公式的列指标在系统中是否存在
	 * 
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public int isColItemExit(String COLITEM_ID, String SUIT_ID) throws Exception;

	/**
	 * 判断稽核公式在系统中是否存在
	 * 
	 * @param CHK_FORMULA_ID
	 * @return
	 * @throws Exception
	 */
	public String selectFormulaId(HashMap<String, String> params) throws Exception;

	/**
	 * 导入稽核方案
	 * 
	 * @param formatCells
	 * @throws Exception
	 */
	public void insertChkScheme(@Param(value = "chk_scheme") Chk_Scheme chk_scheme) throws Exception;

	/**
	 * 导入稽核公式
	 * 
	 * @param formatCells
	 * @throws Exception
	 */
	public void insertChkFormula(HashMap<String, String> params) throws Exception;

	/**
	 * 导入方案引用公式
	 * 
	 * @param formatCells
	 * @throws Exception
	 */
	public void insertChkSchemeDtl(String SCHEME_ID, String FORMULA_ID) throws Exception;
	/**
	 * 通过表套Id,行指标id,列指标id,稽核公式右边 查询稽核公式id
	 * 从而判断稽核公式是否存在
	 * @param suitId
	 * @param rowitemId
	 * @param colitemId
	 * @param fRight
	 * @return
	 * @throws Exception
	 */
	public String getChkFormulaId(@Param(value = "suitId") String suitId,
			@Param(value = "rowitemId") String rowitemId,
			@Param(value = "colitemId") String colitemId,
			@Param(value = "fRight") String fRight) throws Exception;
	/**
	 * 批量更新稽核公式
	 * @param formulas
	 * @throws Exception
	 */
	public void updateBatchChkFormula(@Param(value="formulas") List<CheckFormula> formulas) throws Exception;
}

