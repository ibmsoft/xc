package com.xzsoft.xsr.core.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xzsoft.xsr.core.modal.DcAccount;
import com.xzsoft.xsr.core.modal.DcCaItem;
import com.xzsoft.xsr.core.modal.DcDimCus;
import com.xzsoft.xsr.core.modal.DcDimSet;

public interface XCDimMapper {

	public List<DcDimSet> getDimSetByDcid(@Param(value = "dcid") String dcid) throws Exception;

	public DcAccount getDcAccountByCode(@Param(value = "dcid") String dcid,
			                            @Param(value = "code") String code,
			                            @Param(value = "ledgerCode") String ledgerCode) throws Exception;
	public DcDimCus getDcDimCusByCode(@Param(value = "dcid") String dcid,
                                      @Param(value = "code") String code, 
                                      @Param(value = "tableName") String tableName,
                                      @Param(value = "ledgerCode") String ledgerCode) throws Exception;
	public Timestamp getDimDataValue(@Param(value = "dcid") String dcid,
				                     @Param(value = "entityId") String entityId, 
				                     @Param(value = "keyType") String keyType,
				                     @Param(value = "keyMod") String keyMod) throws Exception;
	public List<String> getKMHrcy(@Param(value = "suitId") String suitId) throws Exception;
	
	public DcCaItem getDcCaItemByCode(@Param(value = "dcid") String dcid,
                                      @Param(value = "code") String code) throws Exception;
	/**
	 * 通过数据中心ID，数据源类型， 数据源名称返回数据源信息
	 * @param dcid
	 * @param srcType
	 * @param srcName
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getDcEtlSrc(@Param(value = "dcid") String dcid,
                                          @Param(value = "srcType") String srcType,
                                          @Param(value = "srcName") String srcName) throws Exception;
}
