package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

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
 * @ClassName: XCGLCommonMapper 
 * @Description: 云ERP平台之总账系统通用数据层Mapper
 * @author linp
 * @date 2015年12月29日 下午6:15:39 
 *
 */
public interface XCGLCommonMapper {

	/**
	 * @Title: getAllLedgers 
	 * @Description: 查询所有的账簿信息
	 * @return
	 */
	public List<Ledger> getAllLedgers() ;
	
	/**
	 * @Title: getSecAllLedgers 
	 * @Description: 查询用户权限下的账簿信息
	 * @return    设定文件
	 */
	public List<Ledger> getSecAllLedgers(String userId) ;
	
	/**
	 * @Title: getLedger 
	 * @Description: 获取账簿信息
	 * @param ledgerId
	 * @return
	 */
	public Ledger getLedger(@Param(value="ledgerId")String ledgerId) ;
	
	/**
	 * @Title: getLedgerByCode 
	 * @Description: 获取账簿信息
	 * @param ledgerCode
	 * @return    设定文件
	 */
	public Ledger getLedgerByCode(@Param(value="ledgerCode")String ledgerCode) ;
	
	/**
	 * @Title: getLedgerByOrg 
	 * @Description: 按组织ID查询账簿
	 * @param orgId
	 * @return    设定文件
	 */
	public Ledger getLedgerByOrg(@Param(value="orgId")String orgId) ;
	
	/**
	 * @Title: getLedgerSemgments 
	 * @Description: 查询账簿下已启用的辅助核算信息 
	 * @param map
	 * @return    设定文件
	 */
	public List<LedgerSegment> getLedgerSemgments(HashMap<String,String> map) ;
	
	/**
	 * @Title: getSegEnabled 
	 * @Description: 获取账簿辅助核算段启用情况
	 * @param ledgerId
	 * @return List<HashMap<String,String>>    返回类型
	 */
	public List<HashMap<String,String>> getSegEnabled(@Param(value="ledgerId")String ledgerId) ;
	
	/**
	 * @Title: getAllAccHrcys 
	 * @Description: 查询所有会计科目体系信息
	 * @return
	 */
	public List<AccHrcy> getAllAccHrcys() ;
	
	/**
	 * @Title: getAccHrcyById 
	 * @Description: 按科目体系ID查询科目体系信息
	 * @param accHrcyId
	 * @return    设定文件
	 */
	public AccHrcy getAccHrcyById(String accHrcyId) ;
	
	/**
	 * @Title: getAccHrcyByCode 
	 * @Description: 按科目体系编码查询科目体系信息
	 * @param accHrcyCode
	 * @return    设定文件
	 */
	public AccHrcy getAccHrcyByCode(String accHrcyCode) ;
	
	/**
	 * @Title: getAllVendors 
	 * @Description: 查询供应商信息
	 * @param dbType
	 * @return
	 */
	public List<Vendor> getAllVendors(@Param(value="dbType")String dbType)  ;
	
	/**
	 * @Title: getVendorById 
	 * @Description: 查询单个供应商信息
	 * @param map
	 * @return
	 */
	public Vendor getVendorById(HashMap<String,String> map) ;
	
	/**
	 * @Title: getAllCustomers 
	 * @Description: 查询客户信息
	 * @param dbType
	 * @return
	 */
	public List<Customer> getAllCustomers(@Param(value="dbType")String dbType)  ;
	
	/**
	 * @Title: getCustomerById 
	 * @Description: 查询单个客户信息
	 * @param map
	 * @return
	 */
	public Customer getCustomerById(HashMap<String,String> map) ;
	
	/**
	 * @Title: getAllProducts 
	 * @Description: 查询产品信息
	 * @param dbType
	 * @return
	 */
	public List<Product> getAllProducts(@Param(value="dbType")String dbType)  ;
	
	/**
	 * @Title: getAllOrgs 
	 * @Description: 查询内部往来信息
	 * @return List<Organization>    返回类型
	 */
	public List<Organization> getAllOrgs()  ;
	
	/**
	 * @Title: getOrgById 
	 * @Description: 按组织ID查询组织信息
	 * @param orgId
	 * @return    设定文件
	 */
	public Organization getOrgById(String orgId)  ;
	
	/**
	 * @Title: getOrgByLedgerId 
	 * @Description: 按账簿ID查询组织信息 
	 * @param ledgerId
	 * @return    设定文件
	 */
	public Organization getOrgByLedgerId(String ledgerId)  ;

	/**
	 * @Title: getAllEmps 
	 * @Description: 查询个人往来信息
	 * @return List<Employee>    返回类型
	 */
	public List<Employee> getAllEmps()  ;
	
	/**
	 * @Title: getAllDepts 
	 * @Description: 查询账簿下的成本中心信息
	 * @return List<Department>    返回类型
	 */
	public List<Department> getAllDeptsByLedger(@Param(value="ledgerId")String ledgerId)  ;
	
	/**
	 * @Title: getAllProjectsByLedger 
	 * @Description: 查询账簿下的项目信息
	 * @return List<Project>    返回类型
	 */
	public List<Project> getAllProjectsByLedger(@Param(value="ledgerId")String ledgerId) ;
	
	/**
	 * @Title: getAllCustomsByLedger 
	 * @Description: 查询账簿下的自定义段值信息
	 * @param map
	 * @return
	 */
	public List<Custom> getAllCustomsByLedger(HashMap<String,String> map) ;
	
	/**
	 * @Title: getAllAccountsByHrcy 
	 * @Description: 查询会计科目体系下的所有科目信息
	 * @param map
	 * @return
	 */
	public List<Account> getAllAccountsByHrcy(HashMap<String,String> map) ;
	
	/**
	 * @Title: getAllAccountsByLedger 
	 * @Description: 查询账簿会计科目体系下的所有科目信息
	 * @param map
	 * @return    设定文件
	 */
	public List<Account> getAllAccountsByLedger(HashMap<String,String> map) ;
	
	/**
	 * @Title: getCCID4Ledger 
	 * @Description:  查询所有已经存在的CCID信息
	 * @param ledgerId
	 * @return    设定文件
	 */
	public List<CCID> getCCID4Ledger(@Param(value="ledgerId")String ledgerId) ;
	
	/**
	 * @Title: getCCIDById 
	 * @Description: 查询CCID信息
	 * @param id
	 * @return    设定文件
	 */
	public CCID getCCIDById(@Param(value="id")String id) ;
	
	/**
	 * @Title: getCCIDByCode 
	 * @Description: 查询CCID信息
	 * @param map
	 * @return    设定文件
	 */
	public CCID getCCIDByCode(HashMap<String,String> map) ;
	
	/**
	 * @Title: getCashItems 
	 * @Description: 查询所有现金流量项目信息
	 * @return    设定文件
	 */
	public List<CashItem> getCashItems() ;
	
	/**
	 * @Title: getVCategory 
	 * @Description: 查询凭证分类信息
	 * @param categoryId
	 * @return
	 */
	public VoucherCategory getVCategory(@Param(value="categoryId")String categoryId) ;
	
	/**
	 * @Title: getAllVCategory 
	 * @Description: 查询全部的凭证分类信息
	 * @return    设定文件
	 */
	public List<VoucherCategory> getAllVCategory() ; 
	
	/**
	 * @Title: getCategory 
	 * @Description: 查询凭证分类信息 
	 * @param catId
	 * @return    设定文件
	 */
	public VoucherCategory getCategory(@Param(value="catId")String catId) ; 
	
	/**
	 * @Title: getCategoryByCode 
	 * @Description: 按凭证分类编码查询凭证分类信息  
	 * @param catCode
	 * @return    设定文件
	 */
	public VoucherCategory getCategoryByCode(@Param(value="catCode")String catCode) ; 
	
	/**
	 * @Title: getCurrentPeriod 
	 * @Description: 根据当前日期查询账簿会计期
	 * @param map
	 * @return    设定文件
	 */
	public Period getCurrentPeriod(HashMap<String,String> map) ;
	
	/**
	 * @Title: getMaxOpenPeriod 
	 * @Description: 查询账簿打开的最大会计日期
	 * @param map
	 * @return    设定文件
	 */
	public Period getMaxOpenPeriod(HashMap<String,String> map) ; 
	
	/**
	 * @Title: getPeriodByCode 
	 * @Description: 查询某个账簿下的会计期信息
	 * @param map
	 * @return    设定文件
	 */
	public Period getPeriod(HashMap<String,String> map) ;
	
	/**
	 * @Title: getPeriodByAccDate 
	 * @Description: 判断会计期是否在当前会计期的日期范围之内
	 * @param map
	 * @return    设定文件
	 */
	public Period getPeriodByAccDate(HashMap<String,Object> map) ;
	
	/**
	 * @Title: getAllPeriods 
	 * @Description: 查询某个账簿下的所有会计期信息
	 * @param ledgerId
	 * @return    设定文件
	 */
	public List<Period> getAllPeriods(@Param(value="ledgerId")String ledgerId) ;
	
	/**
	 * @Title: getLedgerAllAccounts 
	 * @Description: 查询账簿所有的科目信息
	 * @param map
	 * @return    设定文件
	 */
	public List<Account> getLedgerAllAccounts(HashMap<String, Object> map) ; 
	
	/**
	 * @Title: getLedgerAccounts 
	 * @Description: 查询账簿下指定的科目信息
	 * @param map
	 * @return    设定文件
	 */
	public List<Account> getLedgerAccounts(HashMap<String, Object> map) ; 
	
	/**
	 * @Title: getAccSeg 
	 * @Description: 查询科目启用的辅助段信息
	 * @param map
	 * @return    设定文件
	 */
	public List<String> getAccSeg(HashMap<String,String> map) ;
 
	/**
	 * @Title: getVSource 
	 * @Description: 查询凭证来源 
	 * @param srcCode
	 * @return    设定文件
	 */
	public VSource getVSource(@Param(value="srcCode")String srcCode) ;
	
	/**
	 * @Title: getEmployeeByUserId 
	 * @Description: 根据用户ID查询员工信息
	 * @param userId
	 * @return    设定文件
	 */
	public Employee getEmployeeByUserId(@Param(value="userId")String userId) ;
	
	/**
	 * @Title: getAccount 
	 * @Description: 查询单个科目信息 
	 * @param accId
	 * @return    设定文件
	 */
	public Account getAccount(@Param(value="accId")String accId) ;
	
	/**
	 * @Title: getLdAccount 
	 * @Description: 判断该科目是否分配到账簿
	 * @param map
	 * @return    设定文件
	 */
	public Account getLdAccount(HashMap<String,String> map) ;
	
	/**
	 * @Title: getLdAccProperties 
	 * @Description: 查询账簿科目属性信息
	 * @param map
	 * @return    设定文件
	 */
	public List<Account> getLdAccProperties(HashMap<String,Object> map) ;
	
	/**
	 * @Title: getCashItem 
	 * @Description: 查询单个现金流量项目信息 
	 * @param caId
	 * @return    设定文件
	 */
	public CashItem getCashItem(@Param(value="caId")String caId) ;
	
	/**
	 * @Title: getProject 
	 * @Description: 查询项目信息
	 * @param prjId
	 * @return    设定文件
	 */
	public Project getProject(@Param(value="prjId")String prjId) ;
	
	/**
	 * @Title: getDept 
	 * @Description: 查询成本中心信息 
	 * @param deptId
	 * @return    设定文件
	 */
	public Department getDept(@Param(value="deptId")String deptId) ;
	
}
