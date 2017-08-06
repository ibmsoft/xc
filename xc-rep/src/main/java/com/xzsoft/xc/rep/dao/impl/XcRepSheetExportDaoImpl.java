/**
 * 
 */
package com.xzsoft.xc.rep.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.rep.dao.XcRepSheetExportDao;
import com.xzsoft.xc.rep.mapper.XCReportModuleMapper;
import com.xzsoft.xc.rep.modal.ReportSheetBean;
@Repository("xcRepSheetExportDao")
public class XcRepSheetExportDaoImpl implements XcRepSheetExportDao {
	@Resource
	private XCReportModuleMapper xCReportModuleMapper;
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年9月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.xcRepSheetExportDao#exportSheetExcelZip(java.util.HashMap)
	 */
	@Override
	public Object exportSheetExcelZip(HashMap<String, String> paramsMap)
			throws Exception {
		
		return null;
	}

	/* (non-Javadoc)
	 * @name     getSheetListByIds
	 * @author   tangxl
	 * @date     2016年9月21日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.rep.dao.XcRepSheetExportDao#getSheetListByIds(java.lang.String)
	 */
	@Override
	public List<ReportSheetBean> getSheetListByIds(String sheetIds)
			throws Exception {
		List<String> sheetIdList = new ArrayList<String>();
		Collections.addAll(sheetIdList, sheetIds.split(","));
		return xCReportModuleMapper.getSheetById(sheetIdList);
	}

}
