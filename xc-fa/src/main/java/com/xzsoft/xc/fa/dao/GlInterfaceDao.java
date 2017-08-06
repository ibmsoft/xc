package com.xzsoft.xc.fa.dao;

import java.util.Date;
import java.util.HashMap;


public interface GlInterfaceDao {
	
	/**
	 * 
	 * insertGlInterfaceHead:(新增会计平台头)
	 *
	 * @param interfaceHeadJson
	 * @author q p
	 */
	public void insertGlInterfaceHead(HashMap interfaceHeadJson);

	/**
	 * 
	 * updateGlInterface:(更新会计平台)
	 *
	 * @param iHeadId
	 * @param glInterfaceIds
	 * @author q p
	 */
	public void updateGlInterface(String iHeadId, String glInterfaceIds, String lastUpdateBy, Date lastUpdateDate);
	
	/**
	 * 
	 * updateGlInterface:(更新会计平台)
	 *
	 * @param iHeadId
	 * @param lastUpdateBy
	 * @param lastUpdateDate
	 * @author q p
	 */
	public void updateGlInterface(String iHeadId, String lastUpdateBy, Date lastUpdateDate);
	
	/**
	 * 
	 * deleteGlInterfaceHead:(删除会计平台头)
	 *
	 * @param iHeadId
	 * @author q p
	 */
	public void deleteGlInterfaceHead(String iHeadId);
}
