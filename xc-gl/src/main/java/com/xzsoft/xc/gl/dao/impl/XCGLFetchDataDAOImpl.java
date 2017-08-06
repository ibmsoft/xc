package com.xzsoft.xc.gl.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.XCGLFetchDataDAO;
import com.xzsoft.xc.gl.mapper.XCGLFetchDataMapper;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.Balances;
import com.xzsoft.xc.gl.modal.CCID;
import com.xzsoft.xc.gl.modal.CashItem;
import com.xzsoft.xc.gl.modal.CashSum;
import com.xzsoft.xc.gl.modal.Custom;
import com.xzsoft.xc.gl.modal.Customer;
import com.xzsoft.xc.gl.modal.Employee;
import com.xzsoft.xc.gl.modal.Organization;
import com.xzsoft.xc.gl.modal.Product;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.Vendor;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;
@Repository("xCGLFetchDataDAO") 
public class XCGLFetchDataDAOImpl implements XCGLFetchDataDAO {
	@Resource
	XCGLFetchDataMapper xCGLFetchDataMapper;

	@Override
	public List<Ledger> fecthLedgerData(Date lastUpdateDate, String userName,
			String userPwd) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fecthLedgerData(map);
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: fecthAccountData</p> 
	 * <p>Description: 获取科目信息 </p> 
	 * @param lastUpdateDate
	 * @param hrcyCode
	 * @param userName
	 * @param userPwd
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLFetchDataDAO#fecthAccountData(java.util.Date, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Account> fecthAccountData(Date lastUpdateDate, String hrcyCode, String userName, String userPwd) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("hrcyCode", hrcyCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fecthAccountData(map);
	}
	
	@Override
	public List<Vendor> fetchVendorData(Date lastUpdateDate, String ledgerCode,
			String userName, String userPwd) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchVendorData(map);
	}

	@Override
	public List<Customer> fetchCustomerData(Date lastUpdateDate,
			String ledgerCode, String userName, String userPwd)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchCustomerData(map);
	}

	@Override
	public List<Custom> fetchCustomData(String segmentCode,
			Date lastUpdateDate, String ledgerCode, String userName,
			String userPwd) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		map.put("segmentCode", segmentCode);
		return xCGLFetchDataMapper.fetchCustomData(map);
	}

	@Override
	public List<Organization> fetchOrgData(Date lastUpdateDate,
			String ledgerCode, String userName, String userPwd)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchOrganizationData(map);
	}

	@Override
	public List<Employee> fetchEmpData(Date lastUpdateDate, String ledgerCode,
			String userName, String userPwd) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchEmpData(map);
	}

	@Override
	public List<Product> fetchProductData(Date lastUpdateDate,
			String ledgerCode, String userName, String userPwd)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchProductData(map);
	}

	@Override
	public List<Department> fetchDepartmentData(Date lastUpdateDate,
			String ledgerCode, String userName, String userPwd)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchDepartmentData(map);
	}

	@Override
	public List<Project> fetchProjectData(Date lastUpdateDate,
			String ledgerCode, String userName, String userPwd)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchProjectData(map);
	}

	@Override
	public List<Balances> fetchBalanceData(String ledgerCode,
			Date lastUpdateDate, String userName, String userPwd)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchBalancesData(map);
	}

	@Override
	public List<CCID> fecthCcidData(String ledgerCode, Date lastUpdateDate,
			String userName, String userPwd) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchCcidData(map);
	}

	@Override
	public List<VHead> fetchVoucherHeaderData(String ledgerCode,
			Date lastUpdateDate, String userName, String userPwd)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchVoucherHeads(map);
	}

	@Override
	public List<VLine> fetchVoucherLinesData(String ledgerCode,
			Date lastUpdateDate, String userName, String userPwd)
			throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchVoucherLines(map);
	}

	@Override
	public List<CashSum> fetchCashSumData(String ledgerCode, Date lastUpdateDate,
			String userName, String userPwd) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("ledgerCode", ledgerCode);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchCashSumData(map);
	}

	@Override
	public List<CashItem> fetchCashItemData(Date lastUpdateDate,
			String userName, String userPwd) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("dbType", PlatformUtil.getDbType());
		map.put("fetchDate", lastUpdateDate);
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		return xCGLFetchDataMapper.fetchCashItemData(map);
	}

	/* (non-Javadoc)
	 * @see com.xzsoft.xc.gl.dao.XCGLFetchDataDAO#getBillQuota(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String,Object> getBillQuota(String ledgerId, String billType, String actCode)
			throws Exception {
		return xCGLFetchDataMapper.getBillQuota(ledgerId,billType,actCode);
	}

	/* (non-Javadoc)
	 * @name     方法名
	 * @author   tangxl
	 * @date     2016年7月26日
	 * @注释                   TODO
	 * @version  1.0
	 * @see com.xzsoft.xc.gl.dao.XCGLFetchDataDAO#getBgBillAuditPage(java.lang.String, java.lang.String)
	 */
	@Override
	public String getBgBillAuditPage(String bgCatCode, String formType)
			throws Exception {
	    //bgCatCode 预算单类型 BG01-费用预算；BG02-成本预算；BG03-专项预算；BG04-资金预算 @see XCBGConstants
	    //formType 1-电脑端审批表单；2-电脑端填报表单；3-移动端审批表单；4-移动端填报表单
		StringBuffer buffer = new StringBuffer();
		if("1".equalsIgnoreCase(formType)){
			buffer.append("SELECT p.FORM_URL FROM xc_bg_cat t, xip_wf_process_forms p ");
			buffer.append("WHERE t.BG_CAT_CODE = '")
			       .append(bgCatCode)
			        .append("' AND t.PC_A_FORM_ID = p.FORM_ID AND p.ENABLE_FLAG = 'Y'");
		}else if ("2".equals(formType)) {
			buffer.append("SELECT p.FORM_URL FROM xc_bg_cat t, xip_wf_process_forms p ");
			buffer.append("WHERE t.BG_CAT_CODE = '")
			       .append(bgCatCode)
			        .append("' AND t.PC_W_FORM_ID = p.FORM_ID AND p.ENABLE_FLAG = 'Y'");
		}else if ("3".equals(formType)) {
			buffer.append("SELECT p.FORM_URL FROM xc_bg_cat t, xip_wf_process_forms p ");
			buffer.append("WHERE t.BG_CAT_CODE = '")
			       .append(bgCatCode)
			        .append("' AND t.M_A_FORM_ID = p.FORM_ID AND p.ENABLE_FLAG = 'Y'");
		}else if ("4".equals(formType)) {
			buffer.append("SELECT p.FORM_URL FROM xc_bg_cat t, xip_wf_process_forms p ");
			buffer.append("WHERE t.BG_CAT_CODE = '")
			       .append(bgCatCode)
			        .append("' AND t.M_W_FORM_ID = p.FORM_ID AND p.ENABLE_FLAG = 'Y'");
		}
		String formUrl = xCGLFetchDataMapper.getBgBillAuditPage(buffer.toString());
		if(formUrl == null || "".equals(formUrl))
			//throw new Exception("流程审批表单不存在或未启用！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_LCSPBDBCZHWQY", null));
		return formUrl;
	}
	
}
