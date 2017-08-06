package com.xzsoft.xsr.core.mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author liuyang
 */
public interface LoggerManagerMapper {
	
	public void recordLog(List<Map<String, String>> logInfo) throws Exception;
	/**
	 * 查询公司生成报表用到的所有模版
	 */
	public List<Map<String, String>> queryMst(String suitId, String modaltypeId,String entityId, String periodId,
			String cnyId) throws Exception;
	
	
	
}
