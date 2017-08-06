package com.xzsoft.xc.fa.mapper;

import java.util.Date;
import java.util.HashMap;

import org.apache.ibatis.annotations.Param;


public interface GlInterfaceMapper {

	/**
	 * 
	 * insertGlInterfaceHead:(新增会计平台头)
	 *
	 * @param interfaceHeadJson
	 * @author q p
	 */
	public void insertGlInterfaceHead(HashMap interfaceHeadJson);
	
	/**
	 * updateGlInterface:(更新会计平台)
	 *
	 * @param headId
	 * @param glInterfaceIds
	 * @author q p
	 */
	public void updateGlInterface(@Param("iHeadId")String iHeadId, @Param("glInterfaceIds")String glInterfaceIds, @Param("lastUpdateBy")String lastUpdateBy, @Param("lastUpdateDate")Date lastUpdateDate);

	/**
	 * 
	 * updateGlInterface:(更新会计平台)
	 *
	 * @param iHeadId
	 * @param lastUpdateBy
	 * @param lastUpdateDate
	 * @author q p
	 */
	public void updateGlInterfaceByIhead(@Param("iHeadId")String iHeadId,  @Param("lastUpdateBy")String lastUpdateBy, @Param("lastUpdateDate")Date lastUpdateDate);
	
	/**
	 * 
	 * deleteGlInterfaceHead:(删除会计平台头)
	 *
	 * @param iHeadId
	 * @author q p
	 */
	public void deleteGlInterfaceHead(String iHeadId);
}
