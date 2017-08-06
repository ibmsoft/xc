/**
 * Enterprise Spreadsheet Solutions
 * Copyright(c) FeyaSoft Inc. All right reserved.
 * info@enterpriseSheet.com
 * http://www.enterpriseSheet.com
 * 
 * Licensed under the EnterpriseSheet Commercial License.
 * http://enterprisesheet.com/license.jsp
 * 
 * You need to have a valid license key to access this file.
 */
package com.cubedrive.sheet.persistence.sheet;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.sheet.domain.sheet.SheetTabElement;

@Repository
public interface SheetTabElementMapper {	

	void insert(@Param(value = "element") SheetTabElement element);
	
	void batchInsert(List<SheetTabElement> elements);
	
	void update(@Param(value = "tabId") Integer tabId,
			@Param(value = "name") String name,
			@Param(value = "json") String json);
	
	void updateContent(@Param(value = "id") Integer id,
			@Param(value = "json") String json);
	
	void deleteByTab(@Param(value = "tabId") Integer tabId,
			@Param(value = "name") String name);
	
	void delete(@Param(value = "elementId") Integer elementId);
	
	SheetTabElement getElementById(@Param(value = "elementId") Integer elementId);
	
	List<SheetTabElement> findElementsByTabAndType(@Param(value = "tabId") Integer tabId,@Param(value="type") String type);
	
	List<SheetTabElement> getElementsByName(@Param(value = "tabId") Integer tabId,@Param(value="type") String type, @Param(value="name") String name);

	List<SheetTabElement> findElementsByTab(@Param(value = "tabId") Integer tabId);
	
	List<SheetTabElement> findActiveTabElements(@Param(value = "documentId") Integer documentId);
	
	void copy(@Param(value = "fromTabId") Integer fromTabId, @Param(value = "toTabId") Integer toTabId);
	
	long countElementByTab(@Param(value = "tabId") Integer tabId);
	
	List<SheetTabElement> loadElementsOnDemand(@Param(value="startElementId")Integer startElementId,@Param(value="size")Integer size,@Param(value = "tabId") Integer tabId);
	/**
	 * 
	 * @methodName  方法名
	 * @author      tangxl
	 * @date        2016年9月22日
	 * @describe    根据tabId和eType获取单元格的格式
	 * @param       map
	 * @return
	 * @变动说明       
	 * @version     1.0
	 */
	public List<SheetTabElement> findTabElementById(HashMap<String, String> map);

}
