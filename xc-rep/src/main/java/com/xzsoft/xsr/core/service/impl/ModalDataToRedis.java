package com.xzsoft.xsr.core.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xzsoft.xip.framework.util.SpringDBHelp;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xsr.core.mapper.RepChkMapper;
import com.xzsoft.xsr.core.modal.CheckFormulaCellData;
import com.xzsoft.xsr.core.service.impl.repCheck.OperateRedisData;

/**
 * 模板数据保存到redis中
 * @author ZhouSuRong
 *
 */
@Repository
public class ModalDataToRedis {
	private static Logger log = Logger.getLogger(ModalDataToRedis.class.getName());
	@Autowired
	private OperateRedisData redisOp; 
	@Autowired
	private RepChkMapper chkMapper;
	
	/**
	 * 将报表数据保存到redis
	 * 本方法：保存一张报表上的固定和浮动行数据到redis中
	 * @param suitId
	 * @param entityId
	 * @param periodId
	 * @param cnyId
	 * @param modalsheetId
	 * @return true/false
	 */
	public boolean modaltoRedis(String suitId, String entityId, String periodId, String cnyId, String modalsheetId){
		try{
			//根据参数确定此报表，拼接成key
			StringBuffer modalKey = new StringBuffer();
			modalKey.append("&").append(entityId)
			        .append("&").append(periodId)
			        .append("&").append(cnyId)
			        .append("&").append(modalsheetId);
			//获取上次保存时的状态
			if(redisOp.hasKey(modalKey.toString())){
				List<String> oldList = redisOp.getList(modalKey.toString());   
				//删除该模板数据上次保存的所有redis
				if(oldList!=null){
					for(int j=0;j<oldList.size();j++){
						redisOp.del(oldList.get(j));
					}
				}
			}
			
			List<String> modalDataKeyList = new ArrayList<String>();
			List<String> floatDataKey = new ArrayList<String>();
			List<String> fixedDataKey = new ArrayList<String>();
			//将浮动行数据放进redis中
			floatDataKey = getFloatDataToRedis(suitId,entityId,periodId,cnyId,modalsheetId);
			//将固定行数据放进redis中
			fixedDataKey = getFixedDataToRedis(suitId,entityId,periodId,cnyId,modalsheetId);
			
			modalDataKeyList.addAll(floatDataKey);
			modalDataKeyList.addAll(fixedDataKey);
			
			//删除上次的状态
			redisOp.del(modalKey.toString());
			//将本次保存的状态放进redis中
			redisOp.putAllList(modalKey.toString(), modalDataKeyList);
			
			deleteRedisStatus(suitId,entityId,periodId,cnyId,modalsheetId);
			
			return true;
		}catch(Exception e){
			//抛异常前，先将未成功同步redis的数据插入到状态表：xsr_rep_redis_status中
			try {
				insertRedisStatus(suitId,entityId,periodId,cnyId,modalsheetId);
			} catch (Exception e1) {
				log.error("插入XSR_REP_REDIS_STATUS失败！报表坐标：suit_id="+suitId+" 公司ID="+entityId+" 期间ID="
			              +periodId+ " 币种ID=" +cnyId+" 模板ID："+modalsheetId, e1);
			}
			log.error("数据保存到redis失败!报表坐标：suit_id="+suitId+" 公司ID="+entityId+" 期间ID="
			              +periodId+ " 币种ID=" +cnyId+" 模板ID："+modalsheetId, e);
			return false;
		}
	}
	/**
	 * 查询报表浮动行数据，将数据转换为键值对，保存到redis
	 * @param suitId
	 * @param entityId
	 * @param periodId
	 * @param cnyId
	 * @param modalsheetId
	 * @return
	 */
	public List<String> getFloatDataToRedis(String suitId,String entityId,String periodId,String cnyId,String modalsheetId) {
	   	StringBuilder fjDataKey = new StringBuilder();
    	HashMap<String, String> fjCellDataMap = new HashMap<>();
    	List<String> fjCellDataKey = new ArrayList<>();
    	Connection con = null;
    	Statement st = null;
    	ResultSet rs = null;
		String value="0";
		try{
			List<HashMap<String, String>> fjColSet = chkMapper.getFjdataColSet(modalsheetId);
			HashMap<String, String> fjColSetMap = new HashMap<>();
			StringBuilder fjsql = new StringBuilder();
			for(HashMap<String, String> mp : fjColSet) {
				fjColSetMap.put(mp.get("DATA_COL"), mp.get("COLITEM_CODE"));
				fjsql.append(",").append(mp.get("DATA_COL"));
			}
			StringBuilder sql = new StringBuilder();
	    	sql.append(" select t.ENTITY_CODE,t.PERIOD_CODE,t.CURRENCY_CODE,t.ROWITEM_CODE,t.ROWNUMDTL ").append(fjsql)
	    	   .append(" from xsr_rep_fj_cellvalue t ")
	    	   .append(" where t.SUIT_ID = '").append(suitId)
	    	   .append("' and t.ENTITY_ID = '").append(entityId)
	    	   .append("' and t.PERIOD_ID = '").append(periodId)
	    	   .append("' and t.CURRENCY_ID = '").append(cnyId)
	    	   .append("' and t.MODALSHEET_ID = '").append(modalsheetId).append("'");
			
    		con = SpringDBHelp.getConnection();
    		st = con.createStatement();
    		rs = st.executeQuery(sql.toString());
    		Set<String> dataSet = fjColSetMap.keySet();
    		StringBuilder publicKey = new StringBuilder();
    		while(rs.next()) {
    			for(String dataKey : dataSet) {
    				//设置key：表套id&期间code&公司code&币种code&行指标code&列指标code&浮动行明细号
    				if("".equals(publicKey.toString())) {
    					publicKey.append(suitId).append(rs.getString("ENTITY_CODE"))
    					         .append(rs.getString("PERIOD_CODE")).append(rs.getString("CURRENCY_CODE"));
    				}
					fjDataKey.setLength(0);
					fjDataKey.append(publicKey).append(rs.getString("ROWITEM_CODE")).append("&")
					         .append(fjColSetMap.get(dataKey)).append("&")
					         .append(rs.getString("ROWNUMDTL"));
					value = rs.getString(dataKey);
					if(null != value) {
						fjCellDataMap.put(fjDataKey.toString(), rs.getString(dataKey));
						fjCellDataKey.add(fjDataKey.toString());
					}
				}
    		}
    		Set<String> fjCellDataSet = fjCellDataMap.keySet();
    		for(String key : fjCellDataSet) {
    			redisOp.put(key, fjCellDataMap.get(key));
    		}
		}catch(Exception e){
			log.error("redis数据保存-查询浮动行数据出错", e);
    		throw new RuntimeException("redis数据保存-查询浮动行数据出错: " + e.getMessage());
		}finally{
    		try {
    			rs.close();
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		SpringDBHelp.releaseConnection(con);
		}
		return fjCellDataKey;
	}
	/**
	 * 查询报表固定行数据，将数据转换为键值对，保存到redis
	 * @param suitId
	 * @param entityId
	 * @param periodId
	 * @param cnyId
	 * @param modalsheetId
	 * @return
	 */
	public List<String> getFixedDataToRedis(String suitId,String entityId,String periodId,String cnyId,String modalsheetId) {
    	HashMap<String, String> param = new HashMap<>();
    	param.put("entityId", entityId);
    	param.put("periodId", periodId);
    	param.put("cnyId", cnyId);
    	param.put("suitId", suitId);
    	param.put("modalsheetId", modalsheetId);
    	List<String> fixedDataKeyList = new ArrayList<>();
		try{
			//查询该张报表的固定行数据
	    	List<CheckFormulaCellData> cellDataList = chkMapper.getFixedCellDataList(param);
	    	//报表的数据map,value为cellv或celltextv字段值
	    	HashMap<String, String> cellDataMap = new HashMap<>();
	    	StringBuilder key = new StringBuilder();
	    	StringBuilder fixedDataKey = new StringBuilder();
	    	//将该张报表的固定行数据存放到map中
	    	for(CheckFormulaCellData data : cellDataList) {
	    		fixedDataKey.setLength(0);
	    		//设置key：表套id&期间code&公司code&币种code&行指标code&列指标code
	    		if("".equals(key.toString())) {
	    			key.append(suitId).append(data.getENTITY_CODE())
	    			   .append(data.getPERIOD_CODE()).append(data.getCURRENCY_CODE());
	    		}
	    		fixedDataKey.append(key).append(data.getROWITEM_CODE()).append("&").append(data.getCOLITEM_CODE());
	    		String value = null;
	    		switch(data.getDATATYPE()) {
	    			case "1":
	    				value = data.getCELLTEXTV();
	    				break;
	    			case "3":
	    				value = data.getCELLV();
	    				break;
	    			case "5":
	    				value = data.getCELLTEXTV();
	    				break;
	    		}
	    		if(null != value) {
	    			cellDataMap.put(fixedDataKey.toString(), value);	
	    			fixedDataKeyList.add(fixedDataKey.toString());
	    		}
	    	}
    		Set<String> cellDataSet = cellDataMap.keySet();
    		for(String k : cellDataSet) {
    			redisOp.put(k, cellDataMap.get(k));
    		}
		} catch(Exception e){
			log.error("redis数据保存-查询固定行数据出错", e);
    		throw new RuntimeException("redis数据保存-查询固定行数据出错: " + e.getMessage());
		}
		return fixedDataKeyList;
	}
	/**
	 * 删除redis状态表
	 * @param suitId
	 * @param entityId
	 * @param periodId
	 * @param cnyId
	 * @param modalsheetId
	 */
	public void deleteRedisStatus(String suitId,String entityId,String periodId,String cnyId,String modalsheetId) {
		try{
			chkMapper.deleteRedisStatus(suitId, entityId, periodId, cnyId, modalsheetId);
		}catch(Exception e){
			log.error("redis数据保存-删除redis状态表出错", e);
    		throw new RuntimeException("redis数据保存-删除redis状态表出错: " + e.getMessage());
		}
	}
	/**
	 * 新增redis状态表
	 * @param suitId
	 * @param entityId
	 * @param periodId
	 * @param cnyId
	 * @param modalsheetId
	 */
	public void insertRedisStatus(String suitId,String entityId,String periodId,String cnyId,String modalsheetId) {
		try{
			deleteRedisStatus(suitId, entityId, periodId, cnyId, modalsheetId);
			chkMapper.insertRedisStatus(suitId, entityId, periodId, cnyId, modalsheetId,PlatformUtil.getDbType());
		}catch(Exception e){
			log.error("redis数据保存-新增redis状态表出错", e);
    		throw new RuntimeException("redis数据保存-新增redis状态表出错: " + e.getMessage());
		}
	}
}
