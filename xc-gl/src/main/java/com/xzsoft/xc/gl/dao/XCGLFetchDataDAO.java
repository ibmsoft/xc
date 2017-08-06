package com.xzsoft.xc.gl.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;
/**
 * 
 *@desc  总账同步取数接口
 *@author tangxl
 *@date 2016年3月16日
 *
 */
public interface XCGLFetchDataDAO {
	/**
	 * @desc    获取账簿信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   lastUpdateDate
	 * @param   userName
	 * @param   userPwd
	 * @return  List<Ledger>
	 * @throws  Exception
	 */
	public List<Ledger> fecthLedgerData(Date lastUpdateDate,String userName,String userPwd) throws Exception;
	/**
	 * 
	 * @desc  获取科目信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param lastUpdateDate
	 * @param hrcyCode
	 * @param userName
	 * @param userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Account> fecthAccountData(Date lastUpdateDate,String hrcyCode, String userName, String userPwd) throws Exception;
	/**
	 * 
	 * @desc   获取供应商信息
	 * @author tangxl
	 * @date   2016年3月17日
	 * @param  lastUpdateDate
	 * @param  ledgerCode
	 * @param  userName
	 * @param  userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Vendor> fetchVendorData(Date lastUpdateDate,String ledgerCode, String userName, String userPwd) throws Exception;
	/**
	 * 
	 * @desc    获取客户信息
	 * @author  tangxl
	 * @date    2016年3月17日
	 * @param   lastUpdateDate
	 * @param   ledgerCode
	 * @param   userName
	 * @param   userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Customer> fetchCustomerData(Date lastUpdateDate,String ledgerCode, String userName, String userPwd) throws Exception;
	/**
	 * 
	 * @desc    获取科目自定义段信息
	 * @author  tangxl
	 * @date    2016年3月17日
	 * @param   lastUpdateDate
	 * @param   segmentCode  科目段分别为 "XC_GL_CUSTOM1"、"XC_GL_CUSTOM2"、"XC_GL_CUSTOM3"、"XC_GL_CUSTOM4"
	 * @param   ledgerCode
	 * @param   userName
	 * @param   userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Custom> fetchCustomData(String segmentCode,Date lastUpdateDate,String ledgerCode, String userName, String userPwd) throws Exception;
	/**
	 * 
	 * @desc    获取内部往来<组织>信息
	 * @author  tangxl
	 * @date    2016年3月17日
	 * @param   lastUpdateDate
	 * @param   ledgerCode
	 * @param   userName
	 * @param   userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Organization> fetchOrgData(Date lastUpdateDate,String ledgerCode, String userName, String userPwd) throws Exception;
	/**
	 * 
	 * @desc    获取个人往来<员工>信息
	 * @author  tangxl
	 * @date    2016年3月17日
	 * @param   lastUpdateDate
	 * @param   ledgerCode
	 * @param   userName
	 * @param   userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Employee> fetchEmpData(Date lastUpdateDate,String ledgerCode, String userName, String userPwd) throws Exception;
	/**
	 * 
	 * @desc  fetchProductData 获取产品信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param lastUpdateDate
	 * @param ledgerCode
	 * @param userName
	 * @param userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Product> fetchProductData(Date lastUpdateDate,String ledgerCode, String userName, String userPwd) throws Exception;
	/**
	 * 
	 * @desc  fetchDepartmentData 获取部门信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param lastUpdateDate
	 * @param ledgerCode
	 * @param userName
	 * @param userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Department> fetchDepartmentData(Date lastUpdateDate,String ledgerCode, String userName, String userPwd) throws Exception;
	/**
	 * 
	 * @desc  fetchProjectData 获取项目信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param lastUpdateDate
	 * @param ledgerCode
	 * @param userName
	 * @param userPwd
	 * @return
	 * @throws Exception
	 */
	public List<Project> fetchProjectData(Date lastUpdateDate,String ledgerCode, String userName, String userPwd) throws Exception;
	/**
	 * @desc    fecthBalanceData 获取余额信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   lastUpdateDate
	 * @param   ledgerCode
	 * @param   userName
	 * @param   userPwd
	 * @return  List<Balances>
	 * @throws  Exception
	 */
	public List<Balances> fetchBalanceData(String ledgerCode,Date lastUpdateDate,String userName,String userPwd) throws Exception;
	/**
	 * @desc    fecthCcidData 获取科目组合信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   ledgerCode
	 * @param   lastUpdateDate
	 * @param   userName
	 * @param   userPwd
	 * @return  List<CCID>
	 * @throws  Exception
	 */
	public List<CCID> fecthCcidData(String ledgerCode,Date lastUpdateDate,String userName,String userPwd) throws Exception;	
	/**
	 * @desc    fecthVoucherHeaderData 获取凭证头信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   ledgerCode
	 * @param   lastUpdateDate
	 * @param   userName
	 * @param   userPwd
	 * @return  List<VHead>
	 * @throws  Exception
	 */
	public List<VHead> fetchVoucherHeaderData(String ledgerCode,Date lastUpdateDate,String userName,String userPwd) throws Exception;
	/**
	 * @desc    fecthVoucherLinesData 获取凭证行信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   ledgerCode
	 * @param   lastUpdateDate
	 * @param   userName
	 * @param   userPwd
	 * @return  List<VLine>
	 * @throws  Exception
	 */
	public List<VLine> fetchVoucherLinesData(String ledgerCode,Date lastUpdateDate,String userName,String userPwd) throws Exception;
	/**
	 * @desc    fetchCashSumData 获取现金流统计信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   ledgerCode
	 * @param   lastUpdateDate
	 * @param   userName
	 * @param   userPwd
	 * @return  List<VLine>
	 * @throws  Exception
	 */
	public List<CashSum> fetchCashSumData(String ledgerCode,Date lastUpdateDate,String userName,String userPwd) throws Exception;
	/**
	 * @desc    获取现金流项目信息
	 * @author  tangxl
	 * @date    2016年3月16日
	 * @param   lastUpdateDate
	 * @param   userName
	 * @param   userPwd
	 * @return  List<CashItem>
	 * @throws  Exception
	 */
	public List<CashItem> fetchCashItemData(Date lastUpdateDate,String userName,String userPwd) throws Exception;
	/**
	 *  
	 * @methodName getBillQuota
	 * @methodDescribe 获取云ERP内置流程审批节点的阈值
	 * @date 2016年5月23日
	 * @author tangxl
	 * @param ledgerId
	 * @param billType
	 * @param actCode
	 * @return
	 * @throws Exception
	 */
    public HashMap<String,Object> getBillQuota(String ledgerId, String billType,String actCode) throws Exception;
	/**
     * 
     * @methodName  方法名
     * @author      tangxl
     * @date        2016年7月26日
     * @describe    获取预算单审批表单
     * @param 		bgCatCode 预算单类型 BG01-费用预算；BG02-成本预算；BG03-专项预算；BG04-资金预算 @see XCBGConstants
     * @param 		formType 1-电脑端审批表单；2-电脑端填报表单；3-移动端审批表单；4-移动端填报表单
     * @return
     * @变动说明       
     * @version     1.0
     */
    public String getBgBillAuditPage(String bgCatCode,String formType) throws Exception;
}

