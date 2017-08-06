package com.xzsoft.xc.gl.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.mapper.XCGLCommonMapper;
import com.xzsoft.xc.gl.modal.AccHrcy;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.CCID;
import com.xzsoft.xc.gl.modal.CashItem;
import com.xzsoft.xc.gl.modal.Custom;
import com.xzsoft.xc.gl.modal.Customer;
import com.xzsoft.xc.gl.modal.Employee;
import com.xzsoft.xc.gl.modal.LedgerSegment;
import com.xzsoft.xc.gl.modal.Organization;
import com.xzsoft.xc.gl.modal.Period;
import com.xzsoft.xc.gl.modal.Product;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.gl.modal.Vendor;
import com.xzsoft.xc.gl.modal.VoucherCategory;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * @ClassName: XCGLCommonDAOImpl 
 * @Description: 云ERP平台之总账系统通用数据层接口实现类
 * @author linp
 * @date 2015年12月29日 下午6:10:17 
 *
 */
@Repository("xcglCommonDAO")
public class XCGLCommonDAOImpl implements XCGLCommonDAO {
	//日志记录器
	private static Logger log = Logger.getLogger(XCGLCommonDAOImpl.class.getName());
	
	@Resource
	private XCGLCommonMapper xcglCommonMapper ;

	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllLedgers</p> 
	 * <p>Description: 查询所有的账簿信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllLedgers()
	 */
	@Override
	public List<Ledger> getAllLedgers()throws Exception {
		List<Ledger> ledgers = new ArrayList<Ledger>() ;
		try {
			// 查询所有的账簿信息
			ledgers = xcglCommonMapper.getAllLedgers() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return ledgers;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getSecAllLedgers</p> 
	 * <p>Description: 查询用户权限下的账簿信息 </p> 
	 * @param userId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getSecAllLedgers(java.lang.String)
	 */
	@Override
	public List<Ledger> getSecAllLedgers(String userId)throws Exception {
		List<Ledger> ledgers = new ArrayList<Ledger>() ;
		try {
			// 查询用户权限下的账簿信息
			ledgers = xcglCommonMapper.getSecAllLedgers(userId) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return ledgers;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedger</p> 
	 * <p>Description: 查询单个账簿信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getLedger(java.lang.String)
	 */
	public Ledger getLedger(String ledgerId)throws Exception {
		return xcglCommonMapper.getLedger(ledgerId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerByCode</p> 
	 * <p>Description: 按账簿编码查询单个账簿信息 </p> 
	 * @param ledgerCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getLedgerByCode(java.lang.String)
	 */
	@Override
	public Ledger getLedgerByCode(String ledgerCode) throws Exception {
		return xcglCommonMapper.getLedgerByCode(ledgerCode) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerByOrg</p> 
	 * <p>Description: 按组织查询账簿信息 </p> 
	 * @param orgId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getLedgerByOrg(java.lang.String)
	 */
	@Override
	public Ledger getLedgerByOrg(String orgId) throws Exception {
		return xcglCommonMapper.getLedgerByOrg(orgId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerSemgments</p> 
	 * <p>Description: 查询账簿下已启用的辅助核算信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getLedgerSemgments(java.lang.String)
	 */
	public List<LedgerSegment> getLedgerSemgments(String ledgerId)throws Exception {
		List<LedgerSegment> segments = new ArrayList<LedgerSegment>() ;
		try {
			// 查询账簿下已启用的辅助核算信息 
			HashMap<String,String> map = new HashMap<String,String>() ;
			map.put("ledgerId", ledgerId) ;
			map.put("userId", CurrentSessionVar.getUserId()) ;
			
			segments = xcglCommonMapper.getLedgerSemgments(map) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return segments;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getSegEnabled4Ledger</p> 
	 * <p>Description: 获取账簿的辅助核算段启用情况 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getSegEnabled4Ledger(java.lang.String)
	 */
	public List<HashMap<String,String>> getSegEnabled4Ledger(String ledgerId) throws Exception {
		return xcglCommonMapper.getSegEnabled(ledgerId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllAccHrcys</p> 
	 * <p>Description: 查询所有会计科目体系信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllAccHrcys()
	 */
	@Override
	public List<AccHrcy> getAllAccHrcys()throws Exception {
		List<AccHrcy> hrcys = new ArrayList<AccHrcy>() ;
		try {
			// 查询所有会计科目体系信息
			hrcys = xcglCommonMapper.getAllAccHrcys() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return hrcys;
	}
	
	/**
	 * @Title: getAccHrcyById 
	 * @Description: 根据科目体系ID查询科目体系信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public AccHrcy getAccHrcyById(String accHrcyId) throws Exception {
		return xcglCommonMapper.getAccHrcyById(accHrcyId) ;
	}
	
	/**
	 * @Title: getAccHrcyByCode 
	 * @Description: 根据科目体系编码查询科目体系信息
	 * @param accHrcyCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public AccHrcy getAccHrcyByCode(String accHrcyCode) throws Exception {
		return xcglCommonMapper.getAccHrcyByCode(accHrcyCode) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllVendors</p> 
	 * <p>Description: 查询所有供应商信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#syncVendors2Redis()
	 */
	@Override
	public List<Vendor> getAllVendors()throws Exception {
		List<Vendor> vendors = new ArrayList<Vendor>() ;
		try {
			// 当前数据库类型
			String dbType = PlatformUtil.getDbType() ;
			// 查询供应商信息
			vendors = xcglCommonMapper.getAllVendors(dbType.toLowerCase()) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return vendors;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getVendorById</p> 
	 * <p>Description: 查询单个供应商信息</p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#syncVendors2Redis()
	 */
	@Override
	public Vendor getVendorById(String vendorId)throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		map.put("vendorId", vendorId) ;
		
		return xcglCommonMapper.getVendorById(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllCustomers</p> 
	 * <p>Description: 查询所有客户信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllCustomers()
	 */
	@Override
	public List<Customer> getAllCustomers()throws Exception {
		List<Customer> customers = new ArrayList<Customer>() ;
		try {
			// 当前数据库类型
			String dbType = PlatformUtil.getDbType() ;
			// 查询客户信息
			customers = xcglCommonMapper.getAllCustomers(dbType.toLowerCase()) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		return customers;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCustomerById</p> 
	 * <p>Description: 查询单个客户信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllCustomers()
	 */
	@Override
	public Customer getCustomerById(String customerId)throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		map.put("customerId", customerId) ;
		
		return xcglCommonMapper.getCustomerById(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllOrgs</p> 
	 * <p>Description: 查询内部往来信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllOrgs()
	 */
	@Override
	public List<Organization> getAllOrgs()throws Exception {
		List<Organization> orgs = new ArrayList<Organization>() ;
		try {
			// 查询内部往来信息
			orgs = xcglCommonMapper.getAllOrgs() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return orgs;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getOrgById</p> 
	 * <p>Description: 按组织ID查询组织信息 </p> 
	 * @param orgId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getOrgById(java.lang.String)
	 */
	@Override
	public Organization getOrgById(String orgId)throws Exception {
		return xcglCommonMapper.getOrgById(orgId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getOrgByLedgerId</p> 
	 * <p>Description: 按账簿ID查询组织信息   </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getOrgByLedgerId(java.lang.String)
	 */
	@Override
	public Organization getOrgByLedgerId(String ledgerId)throws Exception {
		return xcglCommonMapper.getOrgByLedgerId(ledgerId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllEmps</p> 
	 * <p>Description: 查询个人往来信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllEmps()
	 */
	@Override
	public List<Employee> getAllEmps()throws Exception {
		List<Employee> emps = new ArrayList<Employee>() ;
		try {
			// 查询个人往来信息
			emps = xcglCommonMapper.getAllEmps() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return emps;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllProducts</p> 
	 * <p>Description: 查询产品信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllProducts()
	 */
	@Override
	public List<Product> getAllProducts()throws Exception {
		List<Product> products = new ArrayList<Product>() ;
		try {
			// 当前数据库类型
			String dbType = PlatformUtil.getDbType() ;
			// 查询产品信息 
			products = xcglCommonMapper.getAllProducts(dbType.toLowerCase()) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return products;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllProjectsByLedger</p> 
	 * <p>Description: 查询账簿下的项目信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllProjectsByLedger(java.lang.String)
	 */
	@Override
	public List<Project> getAllProjectsByLedger(String ledgerId)throws Exception {
		List<Project> projects = new ArrayList<Project>() ;
		try {
			// 查询账簿下的项目信息
			projects = xcglCommonMapper.getAllProjectsByLedger(ledgerId) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return projects;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllDepts</p> 
	 * <p>Description: 查询账簿下的成本中心信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllDepts(java.lang.String)
	 */
	@Override
	public List<Department> getAllDepts(String ledgerId)throws Exception {
		List<Department> depts = new ArrayList<Department>() ;
		try {
			// 查询账簿下的项目信息
			depts = xcglCommonMapper.getAllDeptsByLedger(ledgerId) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return depts;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllCustomsByLedger</p> 
	 * <p>Description: 查询账簿下的自定义段值信息  </p> 
	 * @param ledgerId
	 * @param segCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllCustomsByLedger(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Custom> getAllCustomsByLedger(String ledgerId,String segCode)throws Exception {
		List<Custom> customs = new ArrayList<Custom>() ;
		try {
			// 当前数据库类型
			String dbType = PlatformUtil.getDbType() ;
			
			// 查询参数
			HashMap<String,String> map = new HashMap<String,String>() ;
			map.put("dbType", dbType) ;
			map.put("ledgerId", ledgerId) ;
			map.put("segCode", segCode) ;
			
			// 查询账簿下的自定义段值信息 
			customs = xcglCommonMapper.getAllCustomsByLedger(map) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return customs;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllAccountsByHrcy</p> 
	 * <p>Description: 查询会计科目体系下的所有科目信息 </p> 
	 * @param hrcyId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllAccountsByHrcy(java.lang.String)
	 */
	@Override
	public List<Account> getAllAccountsByHrcy(String hrcyId)throws Exception {
		List<Account> accounts = new ArrayList<Account>() ;
		try {
			// 当前数据库类型
			String dbType = PlatformUtil.getDbType() ;
			
			// 查询参数
			HashMap<String,String> map = new HashMap<String,String>() ;
			map.put("dbType", dbType) ;
			map.put("hrcyId", hrcyId) ;
			
			// 查询会计科目体系下的所有科目信息
			accounts = xcglCommonMapper.getAllAccountsByHrcy(map) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return accounts;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllAccountsByLedger</p> 
	 * <p>Description: </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllAccountsByLedger(java.lang.String)
	 */
	@Override
	public HashMap<String,Account> getAllAccountsByLedger(String ledgerId)throws Exception {
		HashMap<String,Account> map = new HashMap<String,Account>() ;
		try {
			// 当前数据库类型
			String dbType = PlatformUtil.getDbType() ;
			
			// 查询参数
			HashMap<String,String> paramMap = new HashMap<String,String>() ;
			paramMap.put("dbType", dbType) ;
			paramMap.put("ledgerId", ledgerId) ;
			
			// 查询会计科目体系下的所有科目信息
			List<Account> accounts = xcglCommonMapper.getAllAccountsByLedger(paramMap) ;
			if(accounts != null && accounts.size()>0){
				for(Account account : accounts){
					map.put(account.getAccId(), account) ;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return map;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCCID4Ledger</p> 
	 * <p>Description: 查询所有已经存在的CCID信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAllCCID(java.lang.String)
	 */
	@Override
	public List<CCID> getCCID4Ledger(String ledgerId)throws Exception {
		List<CCID> ccids = new ArrayList<CCID>() ;
		try {
			// 查询CCID信息
			ccids = xcglCommonMapper.getCCID4Ledger(ledgerId) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return ccids ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCCIDById</p> 
	 * <p>Description: 查询CCID信息 </p> 
	 * @param id
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getCCIDById(java.lang.String)
	 */
	@Override
	public CCID getCCIDById(String id)throws Exception {
		CCID ccid = null ;
		try {
			// 查询CCID信息
			ccid = xcglCommonMapper.getCCIDById(id) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return ccid ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCCIDByCode</p> 
	 * <p>Description: 查询CCID信息 </p> 
	 * @param code
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getCCIDByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public CCID getCCIDByCode(String code,String ledgerId)throws Exception {
		CCID ccid = null ;
		try {
			HashMap<String,String> map = new HashMap<String,String>() ;
			map.put("ledgerId", ledgerId) ;
			map.put("code", code) ;
			
			// 查询CCID信息
			ccid = xcglCommonMapper.getCCIDByCode(map) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return ccid ;
	}
	
	/**
	 * @Title: getCashItems 
	 * @Description: 获取现金流量项目信息
	 * @return
	 * @throws Exception    设定文件
	 */
	@Override
	public List<CashItem> getCashItems()throws Exception {
		List<CashItem> items = new ArrayList<CashItem>() ;
		try {
			// 查询CashItems信息
			items = xcglCommonMapper.getCashItems() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return items ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCashItemById</p> 
	 * <p>Description: 查询单个现金流量项目信息 </p> 
	 * @param caId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getCashItemById(java.lang.String)
	 */
	@Override
	public CashItem getCashItemById(String caId)throws Exception {
		return xcglCommonMapper.getCashItem(caId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAllVCategory</p> 
	 * <p>Description: 查询凭证分类信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getVCategory()
	 */
	@Override
	public List<VoucherCategory> getAllVCategory()throws Exception {
		List<VoucherCategory> items = new ArrayList<VoucherCategory>() ;
		try {
			// 查询凭证分类信息
			items = xcglCommonMapper.getAllVCategory() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return items ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCategory</p> 
	 * <p>Description: 查询凭证分类信息 </p> 
	 * @param catId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getCategory(java.lang.String)
	 */
	@Override
	public VoucherCategory getCategory(String catId) throws Exception {
		return xcglCommonMapper.getCategory(catId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCategoryByCode</p> 
	 * <p>Description: 按凭证分类编码查询凭证分类信息 </p> 
	 * @param catCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getCategoryByCode(java.lang.String)
	 */
	public VoucherCategory getCategoryByCode(String catCode) throws Exception {
		return xcglCommonMapper.getCategoryByCode(catCode) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerAllAccounts</p> 
	 * <p>Description: 查询账簿下所有的科目信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getLedgerAccounts(java.lang.String)
	 */
	@Override
	public List<Account> getLedgerAllAccounts(String ledgerId) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("ledgerId", ledgerId) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		
		return xcglCommonMapper.getLedgerAllAccounts(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLedgerAccounts</p> 
	 * <p>Description: 查询账簿下指定的科目信息 </p> 
	 * @param ledgerId
	 * @param accIds
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getLedgerAccounts(java.lang.String, java.util.List)
	 */
	@Override
	public List<Account> getLedgerAccounts(String ledgerId,List<String> accIds) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("ledgerId", ledgerId) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		map.put("list", accIds) ;
		
		return xcglCommonMapper.getLedgerAccounts(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAccSeg</p> 
	 * <p>Description: 查询科目启用的辅助段信息 </p> 
	 * @param ledgerId
	 * @param accId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAccSeg(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getAccSeg(String ledgerId,String accId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("ledgerId", ledgerId) ;
		map.put("accId", accId) ;
		
		return xcglCommonMapper.getAccSeg(map) ;
	}
	

	/*
	 * (非 Javadoc) 
	 * <p>Title: getVSource</p> 
	 * <p>Description: 查询凭证来源 </p> 
	 * @param srcCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getVSource(java.lang.String)
	 */
	@Override
	public VSource getVSource(String srcCode) throws Exception {
		return xcglCommonMapper.getVSource(srcCode) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getEmployeeByUserId</p> 
	 * <p>Description: 根据用户ID查询员工信息 </p> 
	 * @param userId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getEmployeeByUserId(java.lang.String)
	 */
	@Override
	public Employee getEmployeeByUserId(String userId) throws Exception {
		return xcglCommonMapper.getEmployeeByUserId(userId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAccountById</p> 
	 * <p>Description: 查询单个科目信息 </p> 
	 * @param accId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getAccountById(java.lang.String)
	 */
	@Override
	public Account getAccountById(String accId) throws Exception {
		return xcglCommonMapper.getAccount(accId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLdAcountById</p> 
	 * <p>Description: 判断该科目是否分配到账簿 </p> 
	 * @param accId
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getLdAcountById(java.lang.String, java.lang.String)
	 */
	@Override
	public Account getLdAcountById(String accId,String ledgerId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("accId", accId) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		
		return xcglCommonMapper.getLdAccount(map) ;
	}
	/*
	 * (非 Javadoc) 
	 * <p>Title: getLdAccProperties</p> 
	 * <p>Description: 查询账簿科目 属性信息 </p> 
	 * @param ledgerId
	 * @param accIds
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getLdAccProperties(java.lang.String, java.util.List)
	 */
	@Override
	public List<Account> getLdAccProperties(String ledgerId, List<String> accIds) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("list", accIds) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		
		return xcglCommonMapper.getLdAccProperties(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getProjectById</p> 
	 * <p>Description: 查询项目信息 </p> 
	 * @param prjId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getProjectById(java.lang.String)
	 */
	public Project getProjectById(String prjId) throws Exception {
		return xcglCommonMapper.getProject(prjId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getDeptById</p> 
	 * <p>Description: 查询成本中心信息 </p> 
	 * @param deptId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getDeptById(java.lang.String)
	 */
	public Department getDeptById(String deptId) throws Exception {
		return xcglCommonMapper.getDept(deptId) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getCurrentPeriod</p> 
	 * <p>Description: 根据当前日期查询账簿会计期 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getCurrentPeriod(java.lang.String)
	 */
	public Period getCurrentPeriod(String ledgerId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		
		return xcglCommonMapper.getCurrentPeriod(map) ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getMaxOpenPeriod</p> 
	 * <p>Description: 查询账簿打开的最大会计日期 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.XCGLCommonDAO#getMaxOpenPeriod(java.lang.String)
	 */
	public Period getMaxOpenPeriod(String ledgerId) throws Exception {
		HashMap<String,String> map = new HashMap<String,String>() ;
		map.put("ledgerId", ledgerId) ;
		map.put("dbType", PlatformUtil.getDbType()) ;
		
		return xcglCommonMapper.getMaxOpenPeriod(map) ;
	}
}
