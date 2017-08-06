package com.xzsoft.xc.gl.dao;

import java.util.HashMap;
import java.util.List;

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

/**
 * @ClassName: XCGLCommonDAO 
 * @Description: 云ERP平台之总账系统通用数据层接口
 * @author linp
 * @date 2015年12月29日 下午6:09:46 
 *
 */
public interface XCGLCommonDAO {
	
	/**
	 * @Title: getAllLedgers 
	 * @Description: 查询所有的账簿信息
	 * @return
	 * @throws Exception
	 */
	public List<Ledger> getAllLedgers()throws Exception ;
	
	/**
	 * @Title: getSecAllLedgers 
	 * @Description: 查询用户权限下的账簿信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Ledger> getSecAllLedgers(String userId)throws Exception ;
	
	/**
	 * @Title: getLedger 
	 * @Description: 按账簿ID查询单个账簿信息
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Ledger getLedger(String ledgerId)throws Exception ;
	
	/**
	 * @Title: getLedgerByCode 
	 * @Description: 按账簿编码查询单个账簿信息
	 * @param ledgerCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public Ledger getLedgerByCode(String ledgerCode) throws Exception ;
	
	/**
	 * @Title: getLedgerByOrg 
	 * @Description: 按组织查询账簿信息，目前账簿与组织为一一对应关系
	 * @param orgId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Ledger getLedgerByOrg(String orgId) throws Exception ;
	
	/**
	 * @Title: getLedgerSemgments 
	 * @Description: 查询账簿下已启用的辅助核算信息
	 * @param ledgerId
	 * @return
	 * @throws Exception
	 */
	public List<LedgerSegment> getLedgerSemgments(String ledgerId)throws Exception ;
	
	/**
	 * @Title: getSegEnabled4Ledger 
	 * @Description: 获取账簿的辅助核算段启用情况
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<HashMap<String,String>> getSegEnabled4Ledger(String ledgerId) throws Exception ;
	
	/**
	 * @Title: getAllAccHrcys 
	 * @Description: 查询所有会计科目体系信息
	 * @return
	 * @throws Exception
	 */
	public List<AccHrcy> getAllAccHrcys()throws Exception ;
	
	/**
	 * @Title: getAccHrcyById 
	 * @Description: 根据科目体系ID查询科目体系信息
	 * @param accHrcyId
	 * @return
	 * @throws Exception    设定文件
	 */
	public AccHrcy getAccHrcyById(String accHrcyId) throws Exception ;
	
	/**
	 * @Title: getAccHrcyByCode 
	 * @Description: 根据科目体系编码查询科目体系信息
	 * @param accHrcyCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public AccHrcy getAccHrcyByCode(String accHrcyCode) throws Exception ;
	
	/**
	 * @Title: getAllVendors 
	 * @Description: 查询所有供应商信息
	 * @return List<Vendor>    返回类型
	 */
	public List<Vendor> getAllVendors()throws Exception ;
	
	/**
	 * @Title: getVendorById 
	 * @Description: 查询单个供应商信息
	 * @return
	 * @throws Exception
	 */
	public Vendor getVendorById(String vendorId)throws Exception ;
	
	/**
	 * @Title: getAllCustomers 
	 * @Description: 查询所有客户信息
	 * @return
	 * @throws Exception
	 */
	public List<Customer> getAllCustomers()throws Exception ;
	
	/**
	 * @Title: getCustomerById 
	 * @Description: 查询客户信息
	 * @return
	 * @throws Exception
	 */
	public Customer getCustomerById(String customerId)throws Exception ;
	
	/**
	 * @Title: getAllOrgs 
	 * @Description: 查询内部往来信息
	 * @return
	 * @throws Exception
	 */
	public List<Organization> getAllOrgs()throws Exception ;
	
	/**
	 * @Title: getOrgById 
	 * @Description: 按组织ID查询组织信息
	 * @param orgId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Organization getOrgById(String orgId)throws Exception ;
	
	/**
	 * @Title: getOrgByLedgerId 
	 * @Description: 按账簿ID查询组织信息 
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Organization getOrgByLedgerId(String ledgerId)throws Exception ;
	
	/**
	 * @Title: getAllEmps 
	 * @Description: 查询个人往来信息
	 * @return
	 * @throws Exception
	 */
	public List<Employee> getAllEmps()throws Exception ;
	
	/**
	 * @Title: getAllProducts 
	 * @Description: 查询产品信息
	 * @return
	 * @throws Exception
	 */
	public List<Product> getAllProducts()throws Exception ;
	
	/**
	 * @Title: getAllProjectsByLedger 
	 * @Description: 查询账簿下的项目信息
	 * @param ledgerId
	 * @return
	 * @throws Exception
	 */
	public List<Project> getAllProjectsByLedger(String ledgerId)throws Exception ;
	
	/**
	 * @Title: getAllDepts 
	 * @Description: 查询账簿下的成本中心信息
	 * @param ledgerId
	 * @return
	 * @throws Exception
	 */
	public List<Department> getAllDepts(String ledgerId)throws Exception ;
	
	/**
	 * @Title: getAllCustomsByLedger 
	 * @Description: 查询账簿下的自定义段值信息
	 * @param ledgerId
	 * @param segCode
	 * @return
	 * @throws Exception
	 */
	public List<Custom> getAllCustomsByLedger(String ledgerId,String segCode)throws Exception ;
	
	/**
	 * @Title: getAllAccountsByHrcy 
	 * @Description: 查询会计科目体系下的所有科目信息
	 * @param hrcyId
	 * @return
	 * @throws Exception
	 */
	public List<Account> getAllAccountsByHrcy(String hrcyId)throws Exception ;
	
	/**
	 * @Title: getAllAccountsByLedger 
	 * @Description: 查询账簿对应会计科目体系下的所有科目信息
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,Account> getAllAccountsByLedger(String ledgerId)throws Exception ;
	
	/**
	 * @Title: getCCID4Ledger 
	 * @Description: 查询所有已经存在的CCID信息
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<CCID> getCCID4Ledger(String ledgerId)throws Exception ;
	
	/**
	 * @Title: getCCIDById 
	 * @Description: 查询CCID信息
	 * @param id
	 * @return
	 * @throws Exception    设定文件
	 */
	public CCID getCCIDById(String id)throws Exception ;
	
	/**
	 * @Title: getCCIDByCode 
	 * @Description:  查询CCID信息
	 * @param code
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public CCID getCCIDByCode(String code,String ledgerId)throws Exception ;
	
	/**
	 * @Title: getCashItems 
	 * @Description: 获取现金流量项目信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<CashItem> getCashItems()throws Exception ;
	
	/**
	 * @Title: getCashItemById 
	 * @Description: 查询单个现金流量项目信息
	 * @param caId
	 * @return
	 * @throws Exception    设定文件
	 */
	public CashItem getCashItemById(String caId)throws Exception ;
	
	/**
	 * @Title: getAllVCategory 
	 * @Description: 查询凭证分类信息
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<VoucherCategory> getAllVCategory()throws Exception ;
	
	/**
	 * @Title: getCategory 
	 * @Description: 按凭证分类ID查询凭证分类信息
	 * @param catId
	 * @return
	 * @throws Exception    设定文件
	 */
	public VoucherCategory getCategory(String catId) throws Exception ;
	
	/**
	 * @Title: getCategoryByCode 
	 * @Description: 按凭证分类编码查询凭证分类信息
	 * @param catCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public VoucherCategory getCategoryByCode(String catCode) throws Exception ;
	
	/**
	 * @Title: getLedgerAllAccounts 
	 * @Description: 查询账簿下所有的科目信息
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Account> getLedgerAllAccounts(String ledgerId) throws Exception ;
	
	/**
	 * @Title: getLedgerAccounts 
	 * @Description: 查询账簿下指定的科目信息
	 * @param ledgerId
	 * @param accIds
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Account> getLedgerAccounts(String ledgerId,List<String> accIds) throws Exception ;
	
	/**
	 * @Title: getAccSeg 
	 * @Description: 查询科目启用的辅助段信息
	 * @param ledgerId
	 * @param accId
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<String> getAccSeg(String ledgerId,String accId) throws Exception ;
	
	/**
	 * @Title: getVSource 
	 * @Description: 查询凭证来源
	 * @param srcCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public VSource getVSource(String srcCode) throws Exception ;
	
	/**
	 * @Title: getEmployee 
	 * @Description: 根据用户ID查询员工信息
	 * @param userId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Employee getEmployeeByUserId(String userId) throws Exception ;
	
	/**
	 * @Title: getAccountById 
	 * @Description: 查询单个科目信息
	 * @param accId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Account getAccountById(String accId) throws Exception ;
	
	/**
	 * @Title: getLdAcountById 
	 * @Description: 判断该科目是否分配到账簿下
	 * @param accId
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Account getLdAcountById(String accId,String ledgerId) throws Exception ;
	
	/**
	 * @Title: getLdAccProperties 
	 * @Description: 查询账簿科目 属性信息
	 * @param ledgerId
	 * @param accIds
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<Account> getLdAccProperties(String ledgerId, List<String> accIds) throws Exception ;
	
	/**
	 * @Title: getProjectById 
	 * @Description: 查询项目信息
	 * @param prjId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Project getProjectById(String prjId) throws Exception ;
	
	/**
	 * @Title: getDeptById 
	 * @Description: 查询成本中心信息
	 * @param deptId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Department getDeptById(String deptId) throws Exception ;
	
	/**
	 * @Title: getCurrentPeriod 
	 * @Description: 根据当前日期查询账簿会计期
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Period getCurrentPeriod(String ledgerId) throws Exception ;
	
	/**
	 * @Title: getMaxOpenPeriod 
	 * @Description: 查询账簿打开的最大会计日期
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public Period getMaxOpenPeriod(String ledgerId) throws Exception ;
}
