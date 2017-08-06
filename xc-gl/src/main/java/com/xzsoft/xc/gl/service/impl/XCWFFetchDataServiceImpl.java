/**
 * 
 */
package com.xzsoft.xc.gl.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xzsoft.xc.gl.dao.XCGLFetchDataDAO;
import com.xzsoft.xc.gl.service.XCWFFetchDataService;

/**
 * @fileName    XCWFFetchDataServiceImpl
 * @description 云ERP获取内置流程的阈值
 * @author      tangxl
 *
 */
@Service("xCWFFetchDataService")
public class XCWFFetchDataServiceImpl implements XCWFFetchDataService {
	@Resource
	XCGLFetchDataDAO xCGLFetchDataDAO;
	/* (non-Javadoc)
	 * @see com.xzsoft.xc.gl.service.XCWFFetchDataService#fecthLedgerData(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String,Object> getBillQuota(String ledgerId, String billType,String actCode) throws Exception {
		return xCGLFetchDataDAO.getBillQuota(ledgerId, billType, actCode);
	}
	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月26日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.gl.service.XCWFFetchDataService#getBgBillAuditPage(java.lang.String, java.lang.String)
	 */
	@Override
	public String getBgBillAuditPage(String bgCatCode, String formType)
			throws Exception {
		return xCGLFetchDataDAO.getBgBillAuditPage(bgCatCode, formType);
	}

}
