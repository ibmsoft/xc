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
package com.xzsoft.xc.rep.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.cubedrive.sheet.domain.sheet.SheetTabElement;
import com.cubedrive.sheet.persistence.sheet.SheetTabElementMapper;

@Repository
public interface XCSheetTabElementMapper extends SheetTabElementMapper{	

	void insert(@Param(value = "element") SheetTabElement element);
	
	void batchInsert(List<SheetTabElement> elements);

	public List<SheetTabElement> findElementsByTabAndType(@Param(value = "tabId") Integer tabId,@Param(value="type") String type);
	
	List<SheetTabElement> getElementsByName(@Param(value = "tabId") Integer tabId,@Param(value="type") String type, @Param(value="name") String name);

	List<SheetTabElement> findElementsByTab(@Param(value = "tabId") String tabId);
	
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
