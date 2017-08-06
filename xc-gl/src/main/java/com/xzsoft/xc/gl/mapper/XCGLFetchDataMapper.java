package com.xzsoft.xc.gl.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

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
 *@desc   总账取数接口 
 *@author tangxl
 *@date   2016年3月16日
 *
 */
public interface XCGLFetchDataMapper {
	/**
	 *@desc    fecthLedgerData 获取账簿信息
	 *@author  tangxl
	 *@date    2016年3月16日
	 *@param   map
	 *@return  List<Ledger>
	 */
	public List<Ledger> fecthLedgerData(HashMap<String, Object> map);
	/**
	 * 
	 * @desc   fecthAccountData 获取总账科目信息
	 * @author tangxl
	 * @date   2016年3月17日
	 * @param  map
	 * @return
	 */
	public List<Account> fecthAccountData(HashMap<String, Object> map);
	/**
	 * 
	 * @desc    fetchVendorData 获取供应商信息
	 * @author  tangxl
	 * @date    2016年3月17日
	 * @param   map
	 * @return
	 */
	public List<Vendor> fetchVendorData(HashMap<String, Object> map);
	/**
	 * 
	 * @desc    fetchCustomerData 获取客户信息
	 * @author  tangxl
	 * @date    2016年3月17日
	 * @param   map
	 * @return
	 */
	public List<Customer> fetchCustomerData(HashMap<String, Object> map);
	/**
	 * 
	 * @desc    fetchCustomerData 获取科目自定义段信息
	 * @author  tangxl
	 * @date    2016年3月17日
	 * @param   map
	 * @return
	 */
	public List<Custom> fetchCustomData(HashMap<String, Object> map);
	/**
	 * 
	 * @desc    fetchOrganizationData 获取组织信息
	 * @author  tangxl
	 * @date    2016年3月17日
	 * @param   map
	 * @return
	 */
	public List<Organization> fetchOrganizationData(HashMap<String, Object> map);
	/**
	 *  
	 * @desc  fetchEmpData 获取员工信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param map
	 * @return
	 */
	public List<Employee> fetchEmpData(HashMap<String, Object> map);
	/**
	 *  
	 * @desc  fetchProductData 获取产品信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param map
	 * @return
	 */
	public List<Product> fetchProductData(HashMap<String, Object> map);
	/**
	 *  
	 * @desc  fetchDepartmentData 获取成本中心信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param map
	 * @return
	 */
	public List<Department> fetchDepartmentData(HashMap<String, Object> map);
	/**
	 *  
	 * @desc  fetchProjectData 获取项目信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param map
	 * @return
	 */
	public List<Project> fetchProjectData(HashMap<String, Object> map);
	/**
	 *  
	 * @desc  fetchBalancesData 获取余额信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param map
	 * @return
	 */
	public List<Balances> fetchBalancesData(HashMap<String, Object> map);
	/**
	 *  
	 * @desc  fetchCcidData 获取科目组合信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param map
	 * @return
	 */
	public List<CCID> fetchCcidData(HashMap<String, Object> map);
	/**
	 * 
	 * @desc  fetchVoucherHeads 获取凭证头信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param map
	 * @return
	 */
	public List<VHead> fetchVoucherHeads(HashMap<String, Object> map);
	/**
	 * 
	 * @desc  fetchVoucherLines 获取凭证行信息
	 * @author  tangxl
	 * @date 2016年3月17日
	 * @param map
	 * @return
	 */
	public List<VLine> fetchVoucherLines(HashMap<String, Object> map);
	/**
	 * 
	 * @desc  fetchCashSumData 获取现金流信息
	 * @author  tangxl
	 * @date 2016年3月18日
	 * @param map
	 * @return
	 */
	public List<CashSum> fetchCashSumData(HashMap<String, Object> map);
	/**
	 * 
	 * @desc  fetchCashSumData 获取现金流信息
	 * @author  tangxl
	 * @date 2016年3月18日
	 * @param map
	 * @return
	 */
	public List<CashItem> fetchCashItemData(HashMap<String, Object> map);
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
    public HashMap<String,Object> getBillQuota(@Param(value="ledgerId")String ledgerId,@Param(value="billType")String billType,@Param(value="actCode")String actCode) throws Exception;
    /**
     * 
     * @methodName  getBgBillAuditPage
     * @author      tangxl
     * @date        2016年7月26日
     * @describe    获取预算审批表单
     * @param       sql
     * @return
     * @变动说明       
     * @version     1.0
     */
    public String getBgBillAuditPage(@Param(value="sql")String sql);    
}
