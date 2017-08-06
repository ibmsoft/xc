package com.xzsoft.xsr.core.mapper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
/**
 * 抽取总账系统相关操作
 * 
 * @project 报表3.0
 * @version v1.0
 * @author liuwl
 * @Date 2016-03-18
 */
public interface XCBAReceiveMapper {
	/**
	 * 查询一个公司的公司账簿
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public String getEntityBook(String suitId,String entityId) throws Exception;
	/**
	 * 查询所有公司id与公司账簿
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getEntityBooks(String suitId) throws Exception;
	/**
	 * 查询时间戳表中该公司最新跟新时间点
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getDateValue(@Param("dcId")String dcId,@Param("orgId")String orgId,@Param("mod")String mod) throws Exception;
	/**
	 * 删除ba表中已用总账数据（GL_BA）,现金流量（GL_CASH）
	 * @param
	 * @return
	 * @throws Exception
	 */
	public void delete_XCGL(String dcid,String org_id,@Param(value="mod")String mod,String lastUpdateDate,String LEDGER_ID) throws Exception;
	/**
	 * 向ba表中插入总账数据（GL_BA）,现金流量（GL_CASH）
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void insert_XCGL(@Param(value="dcId")String dcId,@Param(value="orgId")String org_id,@Param(value="mod")String mod,@Param(value="suitId")String suitId,@Param(value="list")List<Map<String, String>> list,@Param(value="synchronizedDate")Timestamp synchronizedDate) throws Exception;
	
	/**
	 * 插入抽数时间戳表
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void insertDateValue(String DC_ID,String entity_id,String mod,Timestamp DATE_VALUE) throws Exception;
	/**
	 *  抽数完成修改该公司该模块最新跟新时间点
	 * @param 
	 * @return
	 * @throws Exception
	 */
	public void updateDateValue(Timestamp DATE_VALUE,String ETL_KEY_ID) throws Exception;
}
