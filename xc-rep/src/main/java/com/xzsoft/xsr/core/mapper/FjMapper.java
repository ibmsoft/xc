package com.xzsoft.xsr.core.mapper;

import java.util.List;
import java.util.Map;

import com.xzsoft.xsr.core.modal.Colitem;
import com.xzsoft.xsr.core.modal.ModalSheetElement;
import com.xzsoft.xsr.core.modal.ModalSheetFormat;

public interface FjMapper {
	/**
	 * 获取浮动行模板格式
	 * @param msFormatId
	 * @throws Exception
	 */
	public List<ModalSheetFormat> getFjModalsheetFormatCellList(String msFormatId,String titleMaxRow,String rowItemCode) throws Exception; 
	/**
	 * 通过模板id获取模板元素
	 * @param msFormatId
	 * @throws Exception
	 */
	public List<ModalSheetElement> getFjModalsheetElementList(String msFormatId) throws Exception; 

	/**
	 * 获取浮动行模版物理列与DATA列的对应
	 */
	public List<Colitem> getFjColitemList(String suitId,String modalsheetId,String modaltypeId) throws Exception;
	
	/**
	 * 获取浮动行模版物理列与DATA列的对应（只获取列指标类型为3的）
	 */
	public List<Colitem> getFjColitemListOnly3(String suitId,String modalsheetId,String modaltypeId) throws Exception;
	
	/**
	 * 删除浮动行公式设置数据
	 */
	public int delFjCalFormula(String suitId,String entityId,String modalsheetId,String rowitemId) throws Exception;
	
	/**
	 * 删除浮动行明细数据
	 */
	public int delFjData(String suitId,String entityId,String modalsheetId,String rowitemId,String periodId,String currencyId) throws Exception;
	
	/**
	 * 删除浮动行明细数据（临时表）
	 */
	public int delFjDataTemp(String suitId,String entityId,String modalsheetId,String rowitemId,String periodId,String currencyId,String userId) throws Exception;
	/**
	 * 删除浮动行所有明细数据
	 */
	public int delFjDataAll(String suitId,String entityId,String modalsheetId,String periodId,String currencyId) throws Exception;
	/**
	 * 删除临时表数据（多模版下的）
	 */
	public int delFjDataTempAll(Map<String, Object> param) throws Exception;
	/**
	 * 删除临时表数据（一个模版）
	 */
	public int delFjDataTempSingle(String suitId,String entityId,String modalsheetId,String periodId,String currencyId,String userId) throws Exception;
	/**
	 * 判断当前模版有无浮动行
	 */
	public int isExistFjItem(String suitId,String modalsheetId,String modaltypeId) throws Exception; 
	/**
	 * 判断当前模版浮动行数据是否已插入临时表
	 */
	public int isExistTempFjData(String userId,String suitId,String modalsheetId,String entityId,String periodId,String currencyId ) throws Exception; 
	/**
	 *判断某浮动行下的明细数据条数 
	 */
	public int countFjData(String suitId,String entityId,String periodId,String currencyId,String rowitemId,String modalsheetId) throws Exception;
	/**
	 *判断模版下所有浮动行的明细数据条数 
	 */
	public int countFjDataAll(String suitId,String entityId,String periodId,String currencyId,String modalsheetId) throws Exception;
	
	/**
	 *公示设置中，某浮动行下的记录数 
	 */
	public int countFjFormula(String suitId,String entityId,String rowitemId,String modalsheetId) throws Exception;
	/**
	 *判断浮动行临时表中，某浮动行下的明细数据条数 
	 */
	public int countFjTempData(String suitId,String entityId,String periodId,String currencyId,String rowitemId,String modalsheetId,String userId) throws Exception;
	
	/**
	 * 通过列编码获取列id
	 */
	public List<Map<String, String>> getColitemIdByCode(Map<String, String> param) throws Exception;
	
	
}
