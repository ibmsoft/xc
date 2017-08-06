package com.xzsoft.xsr.core.service;

import java.util.List;

/**
 * @desc: 记录报表数据同步的日志信息
 * @author liuyang
 */
public interface LoggerManage {
	
	/**
	 * 记录日志信息
	 * @throws Exception
	 */
	public void recordLog(String userId,String sessionId,String suitId,String modaltypeId,List<String> modalsheetIds,String entityId,String periodId,String cnyId,
			String logtype,String message,String resultFlag,String serverId) throws Exception;
}
