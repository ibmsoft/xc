package com.xzsoft.xc.gl.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.gl.common.XCGLCommonService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.CCID;
import com.xzsoft.xc.gl.modal.CashItem;
import com.xzsoft.xc.gl.modal.Custom;
import com.xzsoft.xc.gl.modal.Customer;
import com.xzsoft.xc.gl.modal.Employee;
import com.xzsoft.xc.gl.modal.Organization;
import com.xzsoft.xc.gl.modal.Product;
import com.xzsoft.xc.gl.modal.Vendor;
import com.xzsoft.xc.gl.modal.VoucherCategory;
import com.xzsoft.xc.gl.util.XCGLConstants;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * 
 * @ClassName: XCGLCommonServiceImpl 
 * @Description: 云ERP平台之总账系统通用服务接口
 * @author linp
 * @date 2015年12月9日 下午2:34:03 
 *
 */
@Service("xcglCommonService")
public class XCGLCommonServiceImpl implements XCGLCommonService {
	// 日志记录器
	private static Logger log = Logger.getLogger(XCGLCommonServiceImpl.class.getName());
	
	@Resource
	private RedisDao redisDaoImpl ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;

	/**
	 * @Title: getValueJson 
	 * @Description: 将科目及辅助核算段信息转换为JSON字符串
	 * @param type
	 * @param id
	 * @param code
	 * @param name
	 * @param upId
	 * @return
	 */
	private String getValueJson(String type, String id,String code,String name, String upId) {
		JSONObject jo = new JSONObject();
		
		try {
			
			if("tree".equals(type)){	// 树形结构
				jo.put("id", id) ;
				jo.put("code", code) ;
				jo.put("name", name) ;
				jo.put("upId", upId) ;
				
			}else if("list".equals(type)){  // 列表结构
				jo.put("id", id) ;
				jo.put("code", code) ;
				jo.put("name", name) ;
				
			}else{
				jo.put("id", id) ;
				jo.put("code", code) ;
				jo.put("name", name) ;
			}
			
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		
		return jo.toString() ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncVendors2Redis</p> 
	 * <p>Description: 将供应商同步到Redis数据库 </p> 
	 * @param segCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncVendors2Redis(java.lang.String)
	 */
	public synchronized JSONObject syncVendors2Redis(String segCode)throws Exception {
		
		log.info("供应商信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		try {
			// 查询供应商信息
			List<Vendor> vendors = xcglCommonDAO.getAllVendors() ;
			
			String key = XCRedisKeys.getGlobalSegKey(segCode) ;
			
			// 将供应商信息同步到Redis数据库
			if(vendors != null && vendors.size() >0){
				// 清除供应商缓存信息
				redisDaoImpl.del(key);
				
				// 缓存供应商
				for(Vendor vendor : vendors){
					String value = this.getValueJson("list",vendor.getVendorId(), vendor.getVendorCode(), vendor.getVendorName(),null) ;
					redisDaoImpl.hashPut(key, vendor.getVendorId() , value);
					redisDaoImpl.hashPut(key, vendor.getVendorCode() , value);
				}
			}else{
				// 清除供应商缓存信息
				redisDaoImpl.del(key);
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将供应商信息同步到Redis数据库!") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJGYSXXTBDRSJK", null)) ;
			log.info("供应商信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("供应商信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncCustomers2Redis</p> 
	 * <p>Description: 将客户同步到Redis数据库 </p> 
	 * @param segCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncCustomers2Redis(java.lang.String)
	 */
	public synchronized JSONObject syncCustomers2Redis(String segCode)throws Exception {
		
		log.info("客户信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		try {
			// 查询供应商信息
			List<Customer> customers = xcglCommonDAO.getAllCustomers() ;
			
			String key = XCRedisKeys.getGlobalSegKey(segCode) ;
			
			// 将客户信息同步到Redis数据库
			if(customers != null && customers.size() >0){
				// 清除客户缓存
				redisDaoImpl.del(key);
				
				// 缓存客户信息
				for(Customer customer : customers){
					String value = this.getValueJson("list", customer.getCustomerId(), customer.getCustomerCode(), customer.getCustomerName(),null) ;
					redisDaoImpl.hashPut(key, customer.getCustomerId(), value);
					redisDaoImpl.hashPut(key, customer.getCustomerCode(), value);
				}
			}else{
				// 清除客户缓存
				redisDaoImpl.del(key);
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将客户信息同步到Redis数据库!") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJKHXXTBDRSJK", null)) ;
			log.info("客户信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("客户信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncOrgs2Redis</p> 
	 * <p>Description: 将内部往来同步到Redis数据库 </p> 
	 * @param segCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncOrgs2Redis(java.lang.String)
	 */
	public synchronized JSONObject syncOrgs2Redis(String segCode)throws Exception {
		
		log.info("内部往来信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());

		JSONObject jo = new JSONObject();
		
		try {
			// 查询内部往来信息
			List<Organization> orgs = xcglCommonDAO.getAllOrgs() ;
			
			String key = XCRedisKeys.getGlobalSegKey(segCode) ;
			
			// 将内部往来信息同步到Redis数据库
			if(orgs != null && orgs.size() >0){
				// 清除内部往来缓存
				redisDaoImpl.del(key);
				
				// 缓存内部往来信息
				for(Organization org : orgs){
					String value = this.getValueJson("tree", org.getOrgId(), org.getOrgCode(), org.getOrgName(),org.getUpOrgId()) ;
					redisDaoImpl.hashPut(key, org.getOrgId(), value);
					redisDaoImpl.hashPut(key, org.getOrgCode(), value);
				}
			}else{
				// 清除内部往来缓存
				redisDaoImpl.del(key);
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将内部往来信息同步到Redis数据库!") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJNBWLXXTBDRSJK", null)) ;
			log.info("内部往来信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("内部往来信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());

			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncEmps2Redis</p> 
	 * <p>Description: 将个人往来同步到Redis数据库 </p> 
	 * @param segCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncEmps2Redis(java.lang.String)
	 */
	public synchronized JSONObject syncEmps2Redis(String segCode)throws Exception {
		
		log.info("个人往来信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		
		try {
			// 查询个人往来信息
			List<Employee> emps = xcglCommonDAO.getAllEmps() ;
			
			String key = XCRedisKeys.getGlobalSegKey(segCode) ;

			
			// 将个人往来信息同步到Redis数据库
			if(emps != null && emps.size() >0){
				// 批量清除个人往来缓存信息
				redisDaoImpl.del(key);
				
				// 批量缓存个人往来
				for(Employee emp : emps){
					String value = this.getValueJson("list", emp.getEmpId(), emp.getEmpCode(), emp.getEmpName(),null) ;
					redisDaoImpl.hashPut(key, emp.getEmpId(), value);
					redisDaoImpl.hashPut(key, emp.getEmpCode(), value);
				}
			}else{
				// 批量清除个人往来缓存信息
				redisDaoImpl.del(key);
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将个人往来信息同步到Redis数据库!") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJGRWLXXTBDRSJK", null)) ;
			log.info("个人往来信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("个人往来信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncProducts2Redis</p> 
	 * <p>Description: 将产品同步到Redis数据库 </p> 
	 * @param segCode
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncProducts2Redis(java.lang.String)
	 */
	public synchronized JSONObject syncProducts2Redis(String segCode)throws Exception {
		
		log.info("产品信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		
		try {
			// 查询产品信息
			List<Product> products = xcglCommonDAO.getAllProducts() ;
			
			String key = XCRedisKeys.getGlobalSegKey(segCode) ;
			
			// 将产品信息同步到Redis数据库
			if(products != null && products.size() >0){
				// 清除产品缓存信息
				redisDaoImpl.del(key);
				
				// 缓存产品
				for(Product prod : products){
					String value = this.getValueJson("list", prod.getProductId(), prod.getProductCode(), prod.getProductName(),null) ;
					redisDaoImpl.hashPut(key, prod.getProductId(), value);
					redisDaoImpl.hashPut(key, prod.getProductCode(), value);
				}
				
			}else{
				// 清除产品缓存
				redisDaoImpl.del(key);
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将产品信息同步到Redis数据库!") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJCPXXTBDRSJK", null)) ;
			log.info("产品信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("产品信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncProjects2Redis</p> 
	 * <p>Description: 将项目同步到Redis数据库或从Redis数据库清除 </p> 
	 * @param ledgerId
	 * @param segCode
	 * @param opType
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncProjects2Redis(java.lang.String, java.lang.String)
	 */
	public synchronized JSONObject syncProjects2Redis(String ledgerId, String segCode, String opType)throws Exception {
		
		log.info("项目信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());

		JSONObject jo = new JSONObject();
		
		try {
			// 键值 -> 分账簿同步处理
			String key = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
			
			if(XCGLConstants.REDIS_OP_ADD.equalsIgnoreCase(opType)){ // 执行新增操作
				// 查询产品信息
				List<Project> projects = xcglCommonDAO.getAllProjectsByLedger(ledgerId) ;
				
				// 清除账簿下的项目缓存信息
				redisDaoImpl.del(key);
				
				// 缓存账簿下的项目信息
				if(projects != null && projects.size() >0){
					for(Project prj : projects){
						String value = this.getValueJson("tree", prj.getProjectId(), prj.getProjectCode(), prj.getProjectName(),prj.getUpPorjectId()) ;
						redisDaoImpl.hashPut(key, prj.getProjectId(), value);
						redisDaoImpl.hashPut(key, prj.getProjectCode(), value);
					}
					
				}else{
					// 清除账簿下的项目缓存信息
					redisDaoImpl.del(key);
				}
				
			}else if(XCGLConstants.REDIS_OP_CLEAR.equalsIgnoreCase(opType)){ // 执行清除操作
				// 从Redis数据库清除产品信息
				redisDaoImpl.del(key);
				
			}else{
				// 扩展使用
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将项目信息同步到Redis数据库!") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJXMXXTBDSJK", null)) ;
			log.info("项目信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("项目信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncDepts2Redis</p> 
	 * <p>Description: 将成本中心同步到Redis数据库  </p> 
	 * @param ledgerId
	 * @param segCode
	 * @param opType
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncDepts2Redis(java.lang.String, java.lang.String)
	 */
	public synchronized JSONObject syncDepts2Redis(String ledgerId, String segCode, String opType)throws Exception {
		
		log.info("成本中心信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());

		JSONObject jo = new JSONObject();
		
		try {
			// 键值
			String key = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
			
			if(XCGLConstants.REDIS_OP_ADD.equalsIgnoreCase(opType)){ // 执行新增操作
				// 查询成本中心信息
				List<Department> depts = xcglCommonDAO.getAllDepts(ledgerId) ;
				
				// 清除成本中心缓存
				redisDaoImpl.del(key);
				
				// 缓存清除成本中心
				if(depts != null && depts.size() >0){
					for(Department dept : depts){
						String value = this.getValueJson("tree", dept.getDeptId(), dept.getDeptCode(), dept.getDeptName(),dept.getUpDeptId()) ;
						redisDaoImpl.hashPut(key, dept.getDeptId(), value);
						redisDaoImpl.hashPut(key, dept.getDeptCode(), value);
					}
					
				}else{
					// 清除成本中心缓存
					redisDaoImpl.del(key);
				}
				
			}else if(XCGLConstants.REDIS_OP_CLEAR.equalsIgnoreCase(opType)){ // 执行清除操作
				// 从Redis数据库清除成本中心信息
				redisDaoImpl.del(key);
				
			}else{
				// 扩展使用
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将成本中心信息同步到Redis数据库!") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJCBZXXXTBDRSJK", null)) ;
			log.info("成本中心信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("成本中心信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncCustoms2Redis</p> 
	 * <p>Description: 将自定义段同步到Redis数据库 </p> 
	 * @param ledgerId
	 * @param segCode
	 * @param opType
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncCustoms2Redis(java.lang.String, java.lang.String)
	 */
	public synchronized JSONObject syncCustoms2Redis(String ledgerId, String segCode, String opType)throws Exception {
		
		log.info("自定义段信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		
		try {
			// 键值
			String key = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
			if(XCGLConstants.REDIS_OP_ADD.equalsIgnoreCase(opType)){ // 执行新增操作
				// 查询自定义段信息
				List<Custom> customs = xcglCommonDAO.getAllCustomsByLedger(ledgerId,segCode) ;
				
				// 清除自定义段信息缓存
				redisDaoImpl.del(key);
				
				// 将自定义段信息缓存到Redis数据库
				if(customs != null && customs.size() >0){
					for(Custom cust : customs){
						String value = this.getValueJson("tree",cust.getSegvalId(), cust.getSegvalCode(), cust.getSegvalName(),cust.getUpSegvalId()) ;
						redisDaoImpl.hashPut(key, cust.getSegvalId(), value);
						redisDaoImpl.hashPut(key, cust.getSegvalCode(), value);
					}
				}else{
					// 清除自定义段信息缓存
					redisDaoImpl.del(key);
				}
				
			}else if(XCGLConstants.REDIS_OP_CLEAR.equalsIgnoreCase(opType)){ // 执行清除操作
				// 从Redis数据库清除自定义段信息
				redisDaoImpl.del(key);
				
			}else{
				// 扩展使用
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将自定义段【"+segCode+"】信息同步到Redis数据库!") ;
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("param1", segCode);
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJZDYD_XXTBDRSJK", params)) ;
			log.info("自定义段信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("自定义段信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncAccHrcy2Redis</p> 
	 * <p>Description: 将会计科目体系同步到Redis数据库 </p> 
	 * @param hrcyId
	 * @param opType
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncAccHrcy2Redis(java.lang.String, java.lang.String, java.lang.String)
	 */
	public synchronized JSONObject syncAccHrcy2Redis(String hrcyId, String opType)throws Exception {
		
		log.info("会计科目体系信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());

		JSONObject jo = new JSONObject();
		
		try {
			// 键值
			String key = XCRedisKeys.getAccKey(hrcyId) ;
			
			if(XCGLConstants.REDIS_OP_ADD.equalsIgnoreCase(opType)){ // 执行新增操作
				// 查询科目信息
				List<Account> accounts = xcglCommonDAO.getAllAccountsByHrcy(hrcyId) ;
				
				// 批量清除某个会计科目体系信息
				redisDaoImpl.del(key);
				
				// 将科目信息同步到Redis数据库
				if(accounts != null && accounts.size() >0){
					for(Account acc : accounts){
						String value = this.getValueJson("tree",acc.getAccId(), acc.getAccCode(), acc.getAccName(), acc.getUpAccId()) ;
						
						// 判断科目是否为银行或现金科目
						JSONObject valJo = new JSONObject(value) ;
						if("Y".equals(acc.getIsBankAcc()) || "Y".equals(acc.getIsCashAcc())){
							valJo.put("isCash", "Y") ;
						}else{
							valJo.put("isCash", "N") ;
						}
						valJo.put("isLeaf", acc.getIsLeaf()) ;
						valJo.put("direction", acc.getBalanceDirection()) ;
						
						value = valJo.toString() ;
						redisDaoImpl.hashPut(key, acc.getAccId(), value);
						redisDaoImpl.hashPut(key, acc.getAccCode(), value);
					}
					
				}else{
					// 批量清除某个会计科目体系信息
					redisDaoImpl.del(key);
				}
				
			}else if(XCGLConstants.REDIS_OP_CLEAR.equalsIgnoreCase(opType)){ // 执行清除操作
				// 从Redis数据库清除科目信息
				redisDaoImpl.del(key);
				
			}else{
				// 扩展使用
			}
			
			// 提示信息
			jo.put("flag", "0") ;
			//jo.put("msg", "成功将科目信息同步到Redis数据库!") ;
			jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJKNXXTBDRSJK", null)) ;
			log.info("会计科目体系信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("会计科目体系信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncAccount2Redis</p> 
	 * <p>Description: 科目增量同步</p> 
	 * @param account
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncAccount2Redis(com.xzsoft.xc.gl.modal.Account)
	 */
	@Override
	public synchronized void syncAccount2Redis(HashMap<String,Account> accounts, String accId)throws Exception {
		try {
			// 科目信息
			Account acc = accounts.get(accId) ;
			
			// 键值
			String key = XCRedisKeys.getAccKey(acc.getAccHrcyId()) ;
			
			String value = this.getValueJson("tree",acc.getAccId(), acc.getAccCode(), acc.getAccName(), acc.getUpAccId()) ;
			
			// 判断科目是否为银行或现金科目
			JSONObject valJo = new JSONObject(value) ;
			if("Y".equals(acc.getIsBankAcc()) || "Y".equals(acc.getIsCashAcc())){
				valJo.put("isCash", "Y") ;
			}else{
				valJo.put("isCash", "N") ;
			}
			valJo.put("isLeaf", acc.getIsLeaf()) ;
			
			value = valJo.toString() ;
			redisDaoImpl.hashPut(key, acc.getAccId(), value);
			redisDaoImpl.hashPut(key, acc.getAccCode(), value);
			
			// 递归同步上级科目
			String upAccId = acc.getUpAccId() ;
			if(!"-1".equals(upAccId)){
				syncAccount2Redis(accounts, upAccId) ;
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncCCID2Redis</p> 
	 * <p>Description:将CCID信息同步到Redis数据库 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncCCID2Redis(java.util.List)
	 */
	public synchronized JSONObject syncCCID2Redis(String ledgerId)throws Exception {
		
		log.info("CCID信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		
		try {
			// 查询账簿信息
			List<Ledger> ledgers = new ArrayList<Ledger>() ;
			if("ALL".equals(ledgerId)){ // 不分账簿同步
				String userId = CurrentSessionVar.getUserId() ;
				ledgers = xcglCommonDAO.getSecAllLedgers(userId) ;
				
			}else{ // 分账簿同步处理
				Ledger ledger = xcglCommonDAO.getLedger(ledgerId) ;
				ledgers.add(ledger) ;
			}
			
			if(ledgers != null && ledgers.size()>0){
				for(Ledger ledger : ledgers){
					// 账簿ID
					String ldId = ledger.getLedgerId() ;
					// 键值
					String ccidKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CCID, ldId) ;
					// 查询CCID信息
					List<CCID> ccids = xcglCommonDAO.getCCID4Ledger(ldId) ;
					
					if(ccids != null && ccids.size() >0){
						// 执行缓冲清除处理
						redisDaoImpl.del(ccidKey);
						
						// 执行数据同步处理
						for(CCID ccid : ccids){
							String valueJson = this.getValueJson("list", ccid.getCcid(), ccid.getCcidCode(), ccid.getCcidName(),null) ;
							redisDaoImpl.hashPut(ccidKey, ccid.getCcidCode(), valueJson);
							redisDaoImpl.hashPut(ccidKey, ccid.getCcid(), valueJson);
						}
						
						// 提示信息
						jo.put("flag", "0") ;
						//jo.put("msg", "成功将CCID信息同步到Redis数据库!") ;
						jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJKMZHXXTBDSJK", null)) ;
					}else{
						// 执行缓冲清除处理
						redisDaoImpl.del(ccidKey);
					}
				}
			}
			
			log.info("CCID信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("CCID信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncCashItem2Redis</p> 
	 * <p>Description: 将现金流量表项同步到Redis数据库 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncCashItem2Redis()
	 */
	public synchronized JSONObject syncCashItem2Redis()throws Exception {
		
		log.info("现金流量项同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		
		try {
			// 键值
			String cashKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_CASH_ITEMS) ;
			
			// 查询现金流量项目信息
			List<CashItem> items = xcglCommonDAO.getCashItems() ;
			if(items != null && items.size() >0){
				// 批量清除现金流量项目
				redisDaoImpl.del(cashKey);
				
				// 批量缓存现金流量项目
				for(CashItem item : items){
					String valueJson = this.getValueJson("tree", item.getCaId(), item.getCaCode(), item.getCaName(), item.getUpId()) ;
					
					// 增加流入流出方向
					JSONObject valJo = new JSONObject(valueJson) ;
					valJo.put("direction", item.getCaDirection()) ;
					
					valueJson = valJo.toString() ;
					// 按照现金流量项目ID
					redisDaoImpl.hashPut(cashKey, item.getCaId(), valueJson);
					// 按现金流量项目编码
					redisDaoImpl.hashPut(cashKey, item.getCaCode(), valueJson);
				}
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "成功将现金流量项同步到Redis数据库!") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJXJLLXTBDRSJK", null)) ;
			}else{
				// 批量清除现金流量项目
				redisDaoImpl.del(cashKey);
			}
			
			log.info("现金流量项同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("现金流量项同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncLedgers</p> 
	 * <p>Description: 将账簿信息缓存到Redis数据库 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncLedgers()
	 */
	public synchronized JSONObject syncLedgers(String ledgerIdOrCode,String cat)throws Exception {
		log.info("账簿信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		
		try {
			// 键值
			String ldKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_LEDGERS) ;
			
			// 查询账簿信息
			List<Ledger> ledgers = new ArrayList<Ledger>() ;
			if("ALL".equals(ledgerIdOrCode)){
				// 当前用户下所有的账簿信息
				String userId = CurrentSessionVar.getUserId() ;
				ledgers = xcglCommonDAO.getSecAllLedgers(userId) ;
				
			}else{
				// 同步账簿ID或CODE指定的单个账簿信息
				Ledger ledger = null ;
				if("ById".equals(cat)){ 
					// 通过ID查询
					ledger = xcglCommonDAO.getLedger(ledgerIdOrCode) ;
					
				}else if("ByCode".equals(cat)){ 
					// 通过CODE查询
					ledger = xcglCommonDAO.getLedgerByCode(ledgerIdOrCode) ;
				}

				ledgers.add(ledger) ;
			}
			
			if(ledgers != null && ledgers.size() >0){
				
				// 批量缓存账簿信息
				for(Ledger ledger : ledgers){
					String valueJson = this.getValueJson("list", ledger.getLedgerId(), ledger.getLedgerCode(), ledger.getLedgerName(), null) ;
					
					JSONObject valJo = new JSONObject(valueJson) ;
					valJo.put("hrcyId", ledger.getAccHrcyId()) ;			// 科目体系
					valJo.put("orgId", ledger.getAccHrcyId()) ;				// 组织
					
					valueJson = valJo.toString() ;
					
					redisDaoImpl.hashPut(ldKey, ledger.getLedgerId(), valueJson);
					redisDaoImpl.hashPut(ldKey, ledger.getLedgerCode(), valueJson);
				}
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "成功将账簿信息同步到Redis数据库!") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJZBXXTBDRSJK", null)) ;
			}
			
			log.info("账簿信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("账簿信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: syncVCategory</p> 
	 * <p>Description: 将凭证分类信息缓存到Redis数据库 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.XCGLCommonService#syncVCategory()
	 */
	public synchronized JSONObject syncVCategory()throws Exception {
		log.info("凭证分类信息同步到Redis数据库开始; beginDate="+XCDateUtil.getCurrentDateBy24());
		
		JSONObject jo = new JSONObject();
		
		try {
			// 键值
			String catKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_V_CATEGORY) ;

			
			// 查询凭证分类信息
			List<VoucherCategory> items = xcglCommonDAO.getAllVCategory() ;
			if(items != null && items.size() >0){
				// 清除凭证分类信息
				redisDaoImpl.del(catKey);
				
				// 循环缓存数据
				for(VoucherCategory item : items){
					String valueJson = this.getValueJson("list", item.getCategoryId(), item.getCategoryCode(), item.getCategoryName(), null) ;
					
					redisDaoImpl.hashPut(catKey, item.getCategoryId(), valueJson);
					redisDaoImpl.hashPut(catKey, item.getCategoryCode(), valueJson);
				}
				
				// 提示信息
				jo.put("flag", "0") ;
				//jo.put("msg", "成功将凭证分类信息同步到Redis数据库!") ;
				jo.put("msg", XipUtil.getMessage(XCUtil.getLang(), "XC_GL_CGJPZFLXXTBDRSJK", null)) ;
			}else{
				// 清除凭证分类信息
				redisDaoImpl.del(catKey);
			}
			
			log.info("凭证分类信息同步到Redis数据库成功; endDate="+XCDateUtil.getCurrentDateBy24());
			
		} catch (Exception e) {
			
			log.info("凭证分类信息同步到Redis数据库失败; endDate="+XCDateUtil.getCurrentDateBy24());
			
			log.error(e.getMessage());
			jo.put("flag", "1") ;
			jo.put("msg", e.getMessage()) ;
			throw e ;
		}
		
		return jo;
	}
	
}
