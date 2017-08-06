package com.xzsoft.xsr.core.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.xzsoft.xsr.core.modal.CellData;
/**
 * 报表数据采集
 * 
 * @project 报表3.0
 * @version v1.0
 * @author songyh
 * @Date 2016-01-22
 * @desc 报表生成 页页相关的Service接口
 */
public interface MakeReportService {
	/**
	 * 数据采集或报表运算
	 */
	public String collectData(
							String suitId,
							String entityId,
							String periodId,
							String currencyId,
							String mdlShtIdList,  //模板id列表
							String fetchFlag, //是否执行数据ETL
							String fjFlag,    //是否采集浮动行数据
							String fType ,   //运算类型：REP－报表运算；APP－数据采集
							String userId,
							String ledgerId,
							String cnyCode
							) throws Exception;
	
	/**
	 * 报表锁定或解锁
	 */
	public String lockReport(String userId,
							String suitId,
							String entityId,
							String periodId,
							String currencyId,
							String mdlShtIdList,  //模板id列表
							String lockType    //A-解锁；E－锁定
							) throws Exception;	
	/**
	 * 清空选定报表数据
	 */
	public String clearData(String userId,
							String suitId,
							String entityId,
							String periodId,
							String currencyId,
							String mdlShtIdList  //模板id列表
							) throws Exception;


	public List<CellData> loadDataValue(Map<String,String> map) throws Exception;
	
	public boolean isHrchyEntity(String suitId,String entityId) throws Exception;
	
	public String getHrchySon(String suitId,String entityId,String hrchyId) throws Exception;
	
	public String getHrchySonNode(String suitId,String entityId,String hrchyId,String entity_ids) throws Exception;
	
	public String hrchyCheck(Map<String,String> map) throws Exception;
	public String hrchyCheckAll(Map<String,String> map) throws Exception;

	public String verifyFj(Map<String,String> map) throws Exception;
	public void delVerifyFjResult(String userId) throws Exception;

	public String isSheet(Map<String,String> map) throws Exception;
	/**
	 * 报表固定行数据保存--liuyang/20160222
	 */
	public String saveRepData(String modaltypeId,String modalsheetId,String periodId,String cnyId,String ledgerId, String entityId,String sheetJson,String userId,String suitId) throws Exception;

	
	
	/**
	 * 查询模版下的浮动行--liuyang/20160225
	 */
	public List<Map<String, String>> getFjRowitemInfo(String suitId,String modaltypeId,String modalsheetId) throws Exception;

	public String getHrchyAllSon(String suitId,String entityId,String hrchyId,String entity_ids) throws Exception;
	
	/**
	 * 报表生成或更新--liuyang/20160229
	 */
	public void operRep(String suitId,String modalsheetId,String entityId,String periodId,String currencyId,String userId) throws Exception;
	/**
	 * 报表数据上报（主表）--liuyang/20160425
	 */
	public List<Map<String, Object>> dataUpload(Map<String, String> param)throws Exception;
	
}
