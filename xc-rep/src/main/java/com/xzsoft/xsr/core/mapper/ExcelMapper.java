package com.xzsoft.xsr.core.mapper;

import java.util.List;

import com.xzsoft.xsr.core.modal.CellValue;
import com.xzsoft.xsr.core.modal.Colitem;

public interface ExcelMapper {

	/**
	 * 获取远光数据
	 * @param msFormatId
	 * @return
	 * @throws Exception
	 */
	public List<CellValue> expYgRepData(String suitId,String entityCode,String periodId,String currencyId,String mdlTypeCode,String mdlSheetCode) throws Exception;
	/**
	 * 获取浮动行模版物理列与DATA列的对应
	 */
	public List<Colitem> getFjColitemList(String suitId,String modalsheetCode,String modaltypeCode) throws Exception;
	/**
	 * 查询固定行的值相关信息
	 */
	public List<CellValue> getCellValues(String suitId,String entityCode,String periodCode,String currencyCode)throws Exception;
	/**
	 * 获取当前登录人的姓名
	 */
	public String getUserName(String userId)throws Exception;
}
