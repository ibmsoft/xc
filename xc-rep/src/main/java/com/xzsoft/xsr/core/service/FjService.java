package com.xzsoft.xsr.core.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


public interface FjService {
	
	/**
	 * 获取数据源字段
	 * @param sql
	 */
	public String getFields(String sql) throws Exception ;
	
	/**
	 * 加载浮动行模版格式
	 * @param msFormatId
	 * @param modalsheetName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loadFjModalsheetFormat(String userId,String suitId,String modalsheetId,String modaltypeId,String rowItemId,String entity_id,String msFormatId, String modalsheetName,String titleMaxRow,String rowItemCode,String mark,String periodId,String currencyId) throws Exception;
	/**
	 * 报表查询时，主从表合并加载
	 * @param msFormatId
	 * @param modalsheetName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> loadRepFormat(String userId,String suitId,String modalsheetId,String modaltypeId,String entity_id,String msFormatId, String modalsheetName,String titleMaxRow,String periodId,String currencyId) throws Exception;
	
	/**
	 * 浮动行公式设置数据提交
	 */
	public String saveCellFormula(String modaltypeId,String modalsheetId,String entity_id,String titleMaxRow,String rowItemId,String sheetJson,String startCol,String suitId,String userId) throws Exception;
	/**
	 * 报表浮动行数据提交
	 */
	public String saveFjCellData(String modaltypeId,String modalsheetId,String entity_id,String titleMaxRow,String rowItemId,String sheetJson,String startCol,String suitId,String userId,String periodId,String currencyId,String rowItemCode) throws Exception;
	/**
	 * 报表浮动行数据提交（临时表）
	 */
	public String saveFjCellDataToTemp(String modaltypeId,String modalsheetId,String entity_id,String titleMaxRow,String rowItemId,String sheetJson,String startCol,String suitId,String userId,String periodId,String currencyId,String rowItemCode) throws Exception;
	
	/**
	 * 页面sheetJson转化为{"x":"","y":"","data":"","D":""}数据形式
	 */
	public List<Map<String, String>> sheetJsonToData(String modaltypeId,String sheetJson,String suitId,String modalsheetId,String titleMaxRow,String mark) throws Exception;
	
	/**
	 * 查询浮动行公式设置数据
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> selFjCalFormula(String suitId,String modalsheetId, String modaltypeId, String rowItemId,String entity_id) throws Exception;
	
	/**
	 * 查询报表浮动行数据(临时表)
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> selRepFjData(String userId,String suitId,String modalsheetId, String modaltypeId, String rowItemId,String entity_id,String periodId,String currencyId) throws Exception;
	
	/**
	 * 浮动行数据采集
	 * @return
	 * @throws Exception
	 */
	public String collectFjData(Connection conn,String suitId,String modalsheetId, String modaltypeId, String rowItemId,String entity_id,String periodId,String currencyId,
			String rowItemCode,String entity_code,String periodCode,String currencyCode,String userId,String ledgerId,String cnyCode) throws Exception;
	
	/**
	 * 汇总浮动行明细数据（临时表）
	 */
	public List<Map<String, String>> sumFjDataTemp(String userId,String suitId,String modalsheetId, String modaltypeId, String rowItemId,String entityId,String periodId,String currencyId) throws Exception;
	/**
	 *打开报表，同时将浮动行明细数据插入临时表 
	 */
	public String instFjDataToTemp(String userId,String suitId,String modalsheetId, String modaltypeId,String entityId,String periodId,String currencyId) throws Exception;
	/**
	 *查询报表下所有浮动行数据
	 */
	public List<Map<String, String>> selRepFjDataAll(String suitId,String modalsheetId,String modaltypeId,String entityId,String periodId,String currencyId)throws Exception;
	
	/**
	 * 查询报表浮动行的所有数据（临时表）
	 */
	public List<Map<String, String>> selRepFjDataTempAll(String userId,String suitId,String modalsheetId,String modaltypeId,
			String entity_id,String periodId,String currencyId)throws Exception;
	
	/**
	 * 主表数据保存时，将临时表数据同步到浮动行值正式表
	 */
	public String instTempToFjData(String userId, String suitId,String modalsheetId, String modaltypeId, String entityId,
			String periodId, String currencyId) throws Exception;
	/**
	 * 通过列编码获取列id
	 */
	public Map<String, String> getColitemIdByCode(Map<String, String> param) throws Exception;
	/**
	 * 删除临时表数据（多模版）
	 */
	public String delFjDataTempAll(Map<String, Object> param) throws Exception;
	/**
	 * 查询报表浮动行数据
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> selFjData(String suitId, String modalsheetId,String modaltypeId, String rowItemId, String entity_id,
			String periodId, String currencyId) throws Exception;
	/**
	 * 查询报表浮动行数据
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> repQryFjCellData(String suitId, String modalsheetId,String modaltypeId, String rowItemId, String entity_id,
			String msFormatId, String modalsheetName, String titleMaxRow,String rowItemCode, String periodId, String currencyId)throws Exception;
	
	/**
	 * 报表数据上报（浮动行）
	 */
	public List<Map<String, String>> fjDataUpload(String suitId,String modalsheetId,String modaltypeId,String entityId,String periodId,String currencyId)throws Exception;
}
