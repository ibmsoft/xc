package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xsr.core.modal.CellFormula;

public interface SumReportMapper {
	/**
	 * 判断报表是否存在
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int isReportExist(HashMap params) throws Exception;

	/**
	 * 查询报表状态
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String queryReportStatus(HashMap params) throws Exception;

	/**
	 * 修改报表信息
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateReportD(HashMap params) throws Exception;

	/**
	 * 查询公司名称
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public List<String> queryEntityName(String corpIdList) throws Exception;

	/**
	 * 查询公司级别公式
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<CellFormula> queryCorpFormula(HashMap params);

	/**
	 * 删除汇总指标值
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteSumItemValue(HashMap params);

	/**
	 * 插入汇总指标值
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertSumCellValue(HashMap params);

	/**
	 * 插入汇总报表
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertSumSheet(HashMap params);

	/**
	 * 删除旧浮动行数据
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteFJCellValue(HashMap params);

	/**
	 * 插入新浮动行数据
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertFJCellValue(HashMap params);
	/**
	 * 通过模版id查询模版名称
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	public String getModalName(String modalSheetId)throws Exception;
}
