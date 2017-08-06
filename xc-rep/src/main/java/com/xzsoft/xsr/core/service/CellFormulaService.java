package com.xzsoft.xsr.core.service;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xsr.core.modal.CellData;

public interface CellFormulaService {
	
	public String saveCellFormat(String entity_id, String sheetJson, String suitId,String userId,String dbType) throws Exception;
	
	public List<CellData> loadValue(String msFormatId,String entity_id,String suitId,String dbType) throws Exception;
	
	public String translate(String formulaText,String suitId,String dbType) throws Exception;
	
	public String translateRep(String formula, String suit_id,String dbType) throws Exception;
	
	public String cellTest(String formulaText,String period_id,String entity_id,String suitId,String cnyId,String ledgerId,String cnyCode) throws Exception;
	
	public String getArithmetic(String formulaText,String period_id,String entity_id,String suitId,String cnyId,String ledgerId,String cnyCode) throws Exception;
	
	public String getRCName(String RCId,String suitId,String dbType)throws Exception;
	
	/**
	 * @Title: getRowAndColumnItemName 
	 * @Description: 获取报表的行列指标名称
	 * @param map
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getRowAndColumnItemName(HashMap<String,String> map) throws Exception ;



}
