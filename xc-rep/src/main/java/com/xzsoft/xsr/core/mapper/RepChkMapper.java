package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xsr.core.modal.CheckFormula;
import com.xzsoft.xsr.core.modal.CheckFormulaCellData;

public interface RepChkMapper {
	/**
	 * 获取模板固定行的稽核公式
	 * @param modalsheetId
	 * @param verifyCaseID
	 * @return
	 * @throws Exception
	 */
	public List<CheckFormula> getFixedFormulaList(HashMap<String, String> params) throws Exception;
	/**
	 * 获取报表固定行的数据
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<CheckFormulaCellData> getFixedCellDataList(HashMap<String, String> params) throws Exception;
	/**
	 * 通过公司，期间，币种，行指标，列指标code,表套id查询出一个固定行数据
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public CheckFormulaCellData getFixedCellDataByCode(HashMap<String, String> params) throws Exception;
	/**
	 * 通过模板id，获取该模板的所有指标（行指标编码&列指标编码）
	 * @param modalsheetId
	 * @return
	 * @throws Exception
	 */
	public List<String> getModalItemList(
			@Param(value = "modalsheetId") String modalsheetId,
			@Param(value = "dbType") String dbType) throws Exception;
	/**
	 * 根据行指标，列指标编码，模板集id获取稽核公式右边的中文描述
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getCheckFormulaZHDesc(HashMap<String, String> params) throws Exception;
	/**
	 * 获取模板浮动行的稽核公式
	 * @param modalsheetId
	 * @param verifyCaseID
	 * @return
	 * @throws Exception
	 */
	public List<CheckFormula> getFjFormulaList(HashMap<String, String> params) throws Exception;
	/**
	 * 获取浮动行data列与列指标的映射关系
	 */
	public List<HashMap<String, String>> getFjdataColSet(String modalsheetId) throws Exception;
	/**
	 * 获取浮动行数据
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> getFjCellDataList(HashMap<String, String> params) throws Exception;
	/**
	 * 获取浮动行的最大行数
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<CheckFormulaCellData> getFjRowNum(HashMap<String, String> params) throws Exception;
	/**
	 * 删除redis状态表
	 * @param suitId
	 * @param entityId
	 * @param periodId
	 * @param cnyId
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void deleteRedisStatus(@Param(value = "suitId") String suitId,
			@Param(value = "entityId") String entityId,
			@Param(value = "periodId") String periodId,
			@Param(value = "cnyId") String cnyId,
			@Param(value = "modalsheetId") String modalsheetId) throws Exception;
	/**
	 * 新增redis状态表
	 * @param suitId
	 * @param entityId
	 * @param periodId
	 * @param cnyId
	 * @param modalsheetId
	 * @throws Exception
	 */
	public void insertRedisStatus(@Param(value = "suitId") String suitId,
			@Param(value = "entityId") String entityId,
			@Param(value = "periodId") String periodId,
			@Param(value = "cnyId") String cnyId,
			@Param(value = "modalsheetId") String modalsheetId,
			@Param(value = "dbType") String dbType) throws Exception;
}
