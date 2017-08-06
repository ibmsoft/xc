package com.xzsoft.xsr.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xzsoft.xsr.core.mapper.LoggerManagerMapper;
import com.xzsoft.xsr.core.service.LoggerManage;
@Service("loggerManage")
public class LoggerManageImpl implements LoggerManage {

	@Resource
	private LoggerManagerMapper logManMapper;
	/**
	 * 记录日志信息
	 * @throws Exception
	 */
	@Override
	public void recordLog(String userId,String sessionId,String suitId, String modaltypeId,List<String> modalsheetIds, String entityId, String periodId,
			String cnyId, String logtype, String message, String resultFlag,String serverId)throws Exception {
		
		List<Map<String, String>>mstIds=logManMapper.queryMst(suitId, modaltypeId, entityId, periodId, cnyId);
		List<String>mstTemp=new ArrayList<String>();
		for (Map<String, String> map : mstIds) {
			mstTemp.add(map.get("MODALSHEET_ID"));
		}
		for(int i=0;i<modalsheetIds.size();i++){
			String modalsheetId=modalsheetIds.get(i);
			if(mstTemp.indexOf(modalsheetId)==-1){
				modalsheetIds.remove(i);
			}
		}
		List<Map<String, String>> logInfo=new ArrayList<Map<String,String>>();
		String date=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
		for(int i=0;i<modalsheetIds.size();i++){
			Map<String,String> tempMap=new HashMap<String, String>();
			String modalsheetId=modalsheetIds.get(i);
			tempMap.put("JKLOG_ID",UUID.randomUUID().toString());
			tempMap.put("ENTITY_ID",entityId);
			tempMap.put("PERIOD_ID",periodId);
			tempMap.put("CURRENCY_ID",cnyId);
			tempMap.put("LOG_TYPE",logtype);
			tempMap.put("SUIT_ID",suitId);
			tempMap.put("MODALTYPE_ID",modaltypeId);
			tempMap.put("MODALSHEET_ID",modalsheetId);
			tempMap.put("MESSAGE",message);
			tempMap.put("SESSIONID",sessionId);
			tempMap.put("RESULT_FLAG",resultFlag);
			tempMap.put("UI_SERVER_ID", serverId);
			tempMap.put("CREATION_DATE",date);
			tempMap.put("CREATED_BY",userId);
			tempMap.put("LAST_UPDATE_DATE",date);
			tempMap.put("LAST_UPDATED_BY",userId);
			logInfo.add(tempMap);
		}
		logManMapper.recordLog(logInfo);
	}
	
	
	
	
}
