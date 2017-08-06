package com.xzsoft.xsr.core.mapper;

import java.util.HashMap;
import java.util.List;

public interface ReceiveNBDataMapper {
	/**
	 * 根据Id查询code
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getRepCodeById(HashMap params) throws Exception;
	/**
	 * 判断临时表报表是否存在
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public int IsHaveTempDate(HashMap params) throws Exception;
	/**
	 * 查询报表状态
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String getSheetStatus(HashMap params) throws Exception;
	/**
	 * 插入报表状态表
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertSheets(HashMap params) throws Exception;
	/**
	 * 修改报表状态表
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void updateSheets(HashMap params) throws Exception;
	/**
	 * 删除固定行数据
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteCellValue(HashMap params) throws Exception;
	/**
	 * 删除浮动行数据
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteFJCellValue(HashMap params) throws Exception;
	/**
	 * 插入固定行数据
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertCellValue(HashMap params) throws Exception;
	/**
	 * 插入浮动行数据
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertFJCellValue(HashMap params) throws Exception;
	/**
	 * 删除已经接收的临时表数据
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteTempDate(HashMap params) throws Exception;
	/**
	 * 删除已经接收的fj临时表数据
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void deleteFJTempDate(HashMap params) throws Exception;
	/**
	 * 插入错误日志
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public void insertImpLog(HashMap params) throws Exception;
}
