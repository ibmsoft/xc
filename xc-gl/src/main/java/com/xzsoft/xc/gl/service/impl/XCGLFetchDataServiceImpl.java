package com.xzsoft.xc.gl.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.gl.dao.XCGLFetchDataDAO;
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
import com.xzsoft.xc.gl.service.XCGLFetchDataService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;
import com.xzsoft.xip.framework.util.JSONUtil;
import com.xzsoft.xip.framework.util.JsonDateValueProcessor;
import com.xzsoft.xip.platform.util.XipUtil;
@Service("xCGLFetchDataService")
public class XCGLFetchDataServiceImpl implements XCGLFetchDataService {
	@Resource
	private XCGLFetchDataDAO xCGLFetchDataDAO;
	@Override
	public HashMap<String, String> fecthLedgerData(String lastUpdateDate, String userName,
			String userPwd,String fetchType) throws Exception {
		String returnJsonStr = "[]";
		String synchronizeDate = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean setMaxDate = false;
		Date date = null;
		if(StringUtils.isBlank(lastUpdateDate) || StringUtils.isEmpty(lastUpdateDate)){
			//throw new Exception("同步日期不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQBNWK", null));
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(lastUpdateDate);
				synchronizeDate = sdf.format(new Date());
			} catch (ParseException e) {
				//throw new Exception("同步日期格式不正确！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQGSBZQ", null));
			}
			List<Ledger> ledgerList = xCGLFetchDataDAO.fecthLedgerData(date, userName, userPwd);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			if(ledgerList!=null && ledgerList.size()>0){
				if("reportFecth".equalsIgnoreCase(fetchType)){
					JSONArray jsonArray = new JSONArray();
					for(Ledger tmp:ledgerList){
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("ledgerCode", tmp.getLedgerCode());
						jsonObject.put("ledgerName", tmp.getLedgerName());
						jsonObject.put("orgCode", tmp.getOrgCode());
						jsonObject.put("startPeriodCode", tmp.getStartPeriodCode());
						jsonObject.put("cnyCode", tmp.getCnyCode());
						jsonObject.put("accHrcyId", tmp.getAccHrcyId());
						if(tmp.getMaxUpdateDate() != null && !setMaxDate){
							synchronizeDate = sdf.format(tmp.getMaxUpdateDate());
							setMaxDate = true;
						}
						jsonArray.add(jsonObject);
					}
					returnJsonStr = jsonArray.toString();
				}else{
					returnJsonStr  = JSONUtil.beanListToJson(ledgerList, jsonConfig);
				}
			}
		}
		map.put("synchronizeDate", synchronizeDate);
		map.put("data", returnJsonStr);
		return map;
	}
	
	@Override
	public HashMap<String, String> fecthDimensionsData(String dimType, String lastUpdateDate,
			String ledgerCode, String userName, String userPwd, String fetchType)
			throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		String returnJsonStr = "";
		String maxUpdateDate = "";
		boolean setMaxDate = false;
		Date date = null;
		if(StringUtils.isBlank(lastUpdateDate) || StringUtils.isEmpty(lastUpdateDate)){
			//throw new Exception("同步日期不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQBNWK", null));
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(lastUpdateDate);
				maxUpdateDate = sdf.format(new Date());
			} catch (ParseException e) {
				//throw new Exception("同步日期格式不正确！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQGSBZQ", null));
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			/*--------------------根据dimType类型，同步对应的辅助段信息--------------------------------*/
			//===========XC_GL_ACCOUNTS  同步总账科目
			//===========XC_AP_VENDORS   供应商
			//===========XC_AR_CUSTOMERS 客户
			//===========XIP_PUB_EMPS    个人往来
			//===========XIP_PUB_ORGS    内部往来
			//===========XC_GL_PRODUCTS  产品
			//===========XIP_PUB_DEPTS   成本中心
			//===========XC_PM_PROJECTS  项目
			//===========XC_GL_CUSTOM1         自定义段1
			//===========XC_GL_CUSTOM2         自定义段2
			//===========XC_GL_CUSTOM3         自定义段3
			//===========XC_GL_CUSTOM4         自定义段4
			if("XC_GL_ACCOUNTS".equalsIgnoreCase(dimType)){
				returnJsonStr = "[]";
//				List<Account> accountList = xCGLFetchDataDAO.fecthAccountData(date, ledgerCode, userName, userPwd);
//				if(accountList!=null && accountList.size()>0){
//					if("reportFecth".equalsIgnoreCase(fetchType)){
//						JSONArray jsonArray = new JSONArray();
//						for(Account tmp:accountList){
//							JSONObject jsonObject = new JSONObject();
//							jsonObject.put("code", tmp.getAccCode());
//							jsonObject.put("name", tmp.getAccName());
//							jsonObject.put("upCode", tmp.getUpAccCode());
//							jsonObject.put("ledgerCode", "-1");
//							if(tmp.getMaxUpdateDate() != null && !setMaxDate){
//								maxUpdateDate = sdf.format(tmp.getMaxUpdateDate());
//								setMaxDate = true;
//							}
//							jsonArray.add(jsonObject);
//						}
//						returnJsonStr = jsonArray.toString();
//					}else{
//						returnJsonStr  = JSONUtil.beanListToJson(accountList, jsonConfig);
//					}
//				}else{
//					returnJsonStr = "[]";
//				}
			}else if("XC_AP_VENDORS".equalsIgnoreCase(dimType)){
				List<Vendor> vendorList = xCGLFetchDataDAO.fetchVendorData(date, ledgerCode, userName, userPwd);
				if(vendorList != null && vendorList.size()>0){
					if("reportFecth".equalsIgnoreCase(fetchType)){
						JSONArray jsonArray = new JSONArray();
						for(Vendor tmp:vendorList){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", tmp.getVendorCode());
							jsonObject.put("name", tmp.getVendorName());
							jsonObject.put("upCode", "-1");
							jsonObject.put("ledgerCode", "-1");
							if(tmp.getSynchronizeDate() != null && !setMaxDate){
								maxUpdateDate = sdf.format(tmp.getSynchronizeDate());
								setMaxDate = true;
							}
							jsonArray.add(jsonObject);
						}
						returnJsonStr = jsonArray.toString();
					}else{
						returnJsonStr  = JSONUtil.beanListToJson(vendorList, jsonConfig);
					}
				}else{
					returnJsonStr = "[]";
				}
			}else if("XC_AR_CUSTOMERS".equalsIgnoreCase(dimType)){
				List<Customer> customerList = xCGLFetchDataDAO.fetchCustomerData(date, ledgerCode, userName, userPwd);
				if(customerList != null && customerList.size()>0){
					if("reportFecth".equalsIgnoreCase(fetchType)){
						JSONArray jsonArray = new JSONArray();
						for(Customer tmp:customerList){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", tmp.getCustomerCode());
							jsonObject.put("name", tmp.getCustomerName());
							jsonObject.put("upCode", "-1");
							jsonObject.put("ledgerCode", "-1");
							if(tmp.getSynchronizeDate() != null && !setMaxDate){
								maxUpdateDate = sdf.format(tmp.getSynchronizeDate());
								setMaxDate = true;
							}
							jsonArray.add(jsonObject);
						}
						returnJsonStr = jsonArray.toString();
					}else{
						returnJsonStr  = JSONUtil.beanListToJson(customerList, jsonConfig);
					}
				}else{
					returnJsonStr = "[]";
				}
			}else if("XIP_PUB_EMPS".equalsIgnoreCase(dimType)){
				List<Employee> empList = xCGLFetchDataDAO.fetchEmpData(date, ledgerCode, userName, userPwd);
				if(empList != null && empList.size()>0){
					if("reportFecth".equalsIgnoreCase(fetchType)){
						JSONArray jsonArray = new JSONArray();
						for(Employee tmp:empList){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", tmp.getEmpCode());
							jsonObject.put("name", tmp.getEmpName());
							jsonObject.put("upCode", "-1");
							jsonObject.put("ledgerCode", "-1");
							if(tmp.getSynchronizeDate() != null && !setMaxDate){
								maxUpdateDate = sdf.format(tmp.getSynchronizeDate());
								setMaxDate = true;
							}
							jsonArray.add(jsonObject);
						}
						returnJsonStr = jsonArray.toString();
					}else{
						returnJsonStr  = JSONUtil.beanListToJson(empList, jsonConfig);
					}
				}else{
					returnJsonStr = "[]";
				}
			}else if("XIP_PUB_ORGS".equalsIgnoreCase(dimType)){
				List<Organization> orgList = xCGLFetchDataDAO.fetchOrgData(date, ledgerCode, userName, userPwd);
				if(orgList != null && orgList.size()>0){
					if("reportFecth".equalsIgnoreCase(fetchType)){
						JSONArray jsonArray = new JSONArray();
						for(Organization tmp:orgList){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", tmp.getOrgCode());
							jsonObject.put("name", tmp.getOrgName());
							jsonObject.put("upCode",tmp.getUpOrgCode());
							jsonObject.put("ledgerCode","-1");
							if(tmp.getSynchronizeDate() != null && !setMaxDate){
								maxUpdateDate = sdf.format(tmp.getSynchronizeDate());
								setMaxDate = true;
							}
							jsonArray.add(jsonObject);
						}
						returnJsonStr = jsonArray.toString();
					}else{
						returnJsonStr  = JSONUtil.beanListToJson(orgList, jsonConfig);
					}
				}else{
					returnJsonStr = "[]";
				}
			}else if("XC_GL_PRODUCTS".equalsIgnoreCase(dimType)){
				List<Product> productList = xCGLFetchDataDAO.fetchProductData(date, ledgerCode, userName, userPwd);
				if(productList != null && productList.size()>0){
					if("reportFecth".equalsIgnoreCase(fetchType)){
						JSONArray jsonArray = new JSONArray();
						for(Product tmp:productList){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", tmp.getProductCode());
							jsonObject.put("name", tmp.getProductName());
							jsonObject.put("upCode", "-1");
							jsonObject.put("ledgerCode", "-1");
							if(tmp.getSynchronizeDate() != null && !setMaxDate){
								maxUpdateDate = sdf.format(tmp.getSynchronizeDate());
								setMaxDate = true;
							}
							jsonArray.add(jsonObject);
						}
						returnJsonStr = jsonArray.toString();
					}else{
						returnJsonStr  = JSONUtil.beanListToJson(productList, jsonConfig);
					}
				}else{
					returnJsonStr = "[]";
				}
			}else if("XIP_PUB_DEPTS".equalsIgnoreCase(dimType)){
				List<Department> deptList = xCGLFetchDataDAO.fetchDepartmentData(date, ledgerCode, userName, userPwd);
				if(deptList != null && deptList.size()>0){
					if("reportFecth".equalsIgnoreCase(fetchType)){
						JSONArray jsonArray = new JSONArray();
						for(Department tmp:deptList){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", tmp.getDeptCode());
							jsonObject.put("name", tmp.getDeptName());
							jsonObject.put("upCode",tmp.getUpDeptCode());
							jsonObject.put("ledgerCode",tmp.getLedgerCode());
							if(tmp.getSynchronizeDate() != null && !setMaxDate){
								maxUpdateDate = sdf.format(tmp.getSynchronizeDate());
								setMaxDate = true;
							}
							jsonArray.add(jsonObject);
						}
						returnJsonStr = jsonArray.toString();
					}else{
						returnJsonStr  = JSONUtil.beanListToJson(deptList, jsonConfig);
					}
				}else{
					returnJsonStr = "[]";
				}
			}else if("XC_PM_PROJECTS".equalsIgnoreCase(dimType)){
				List<Project> projectList = xCGLFetchDataDAO.fetchProjectData(date, ledgerCode, userName, userPwd);
				if(projectList != null && projectList.size()>0){
					if("reportFecth".equalsIgnoreCase(fetchType)){
						JSONArray jsonArray = new JSONArray();
						for(Project tmp:projectList){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", tmp.getProjectCode());
							jsonObject.put("name", tmp.getProjectName());
							jsonObject.put("upCode",tmp.getUpProjectCode());
							jsonObject.put("ledgerCode",tmp.getLedgerCode());
							if(tmp.getSynchronizeDate() != null && !setMaxDate){
								maxUpdateDate = sdf.format(tmp.getSynchronizeDate());
								setMaxDate = true;
							}
							jsonArray.add(jsonObject);
						}
						returnJsonStr = jsonArray.toString();
					}else{
						returnJsonStr  = JSONUtil.beanListToJson(projectList, jsonConfig);
					}
				}else{
					returnJsonStr = "[]";
				}				
			}else if("XC_GL_CUSTOM1".equalsIgnoreCase(dimType) || "XC_GL_CUSTOM2".equalsIgnoreCase(dimType)
					|| "XC_GL_CUSTOM3".equalsIgnoreCase(dimType) || "XC_GL_CUSTOM4".equalsIgnoreCase(dimType)){
				List<Custom> customList = xCGLFetchDataDAO.fetchCustomData(dimType.toUpperCase(),date, ledgerCode, userName, userPwd);
				if(customList != null && customList.size()>0){
					if("reportFecth".equalsIgnoreCase(fetchType)){
						JSONArray jsonArray = new JSONArray();
						for(Custom tmp:customList){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("code", tmp.getSegvalCode());
							jsonObject.put("name", tmp.getSegvalName());
							jsonObject.put("upCode",tmp.getUpSegvalCode());
							jsonObject.put("ledgerCode",tmp.getLedgerCode());
							if(tmp.getSynchronizeDate() != null && !setMaxDate){
								maxUpdateDate = sdf.format(tmp.getSynchronizeDate());
								setMaxDate = true;
							}
							jsonArray.add(jsonObject);
						}
						returnJsonStr = jsonArray.toString();
					}else{
						returnJsonStr  = JSONUtil.beanListToJson(customList, jsonConfig);
					}
				}else{
					returnJsonStr = "[]";
				}
			}
		}
		map.put("synchronizeDate", maxUpdateDate);
		map.put("data", returnJsonStr);
		return map;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: fecthAccountData</p> 
	 * <p>Description: 科目信息同步处理 </p> 
	 * @param lastUpdateDate
	 * @param hrcyCode
	 * @param userName
	 * @param userPwd
	 * @param fetchType
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.service.XCGLFetchDataService#fecthAccountData(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public HashMap<String, String> fecthAccountData(String lastUpdateDate, String hrcyCode, String userName, String userPwd,String fetchType) throws Exception {
		HashMap<String, String> map = new HashMap<String, String>();
		String returnJsonStr = "";
		String maxUpdateDate = "";
		boolean setMaxDate = false;
		Date date = null;
		if(StringUtils.isBlank(lastUpdateDate) || StringUtils.isEmpty(lastUpdateDate)){
			//throw new Exception("同步日期不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQBNWK", null));
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(lastUpdateDate);
				maxUpdateDate = sdf.format(new Date());
			} catch (ParseException e) {
				//throw new Exception("同步日期格式不正确！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQGSBZQ", null));
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			
			List<Account> accountList = xCGLFetchDataDAO.fecthAccountData(date, hrcyCode, userName, userPwd);
			if(accountList!=null && accountList.size()>0){
				if("reportFecth".equalsIgnoreCase(fetchType)){
					JSONArray jsonArray = new JSONArray();
					for(Account tmp:accountList){
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("code", tmp.getAccCode());
						jsonObject.put("name", tmp.getAccName());
						jsonObject.put("upCode", tmp.getUpAccCode());
						jsonObject.put("ledgerCode", "-1");
						if(tmp.getMaxUpdateDate() != null && !setMaxDate){
							maxUpdateDate = sdf.format(tmp.getMaxUpdateDate());
							setMaxDate = true;
						}
						jsonArray.add(jsonObject);
					}
					returnJsonStr = jsonArray.toString();
				}else{
					returnJsonStr  = JSONUtil.beanListToJson(accountList, jsonConfig);
				}
			}else{
				returnJsonStr = "[]";
			}
		}
		map.put("synchronizeDate", maxUpdateDate);
		map.put("data", returnJsonStr);
		return map;
	}
	
	@Override
	public HashMap<String, String> fecthBalancesData(String lastUpdateDate,
			String ledgerCode, String userName, String userPwd, String fetchType)
			throws Exception {
		String returnJsonStr = "[]";
		String synchronizeDate = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean setMaxDate = false;
		Date date = null;
		if(StringUtils.isBlank(lastUpdateDate) || StringUtils.isEmpty(lastUpdateDate)){
			//throw new Exception("同步日期不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQBNWK", null));
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(lastUpdateDate);
				synchronizeDate = sdf.format(new Date());
			} catch (ParseException e) {
				//throw new Exception("同步日期格式不正确！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQGSBZQ", null));
			}
			List<Balances> balancesList = xCGLFetchDataDAO.fetchBalanceData(ledgerCode,date, userName, userPwd);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			if(balancesList!=null && balancesList.size()>0){
				if("reportFecth".equalsIgnoreCase(fetchType)){
					jsonConfig.setExcludes(new String[]{"CREATION_DATE","CREATED_BY","LAST_UPDATE_DATE","LAST_UPDATED_BY","synchronizeDate"});
					JSONArray jsonArray = new JSONArray();
					for(Balances tmp:balancesList){
						JSONObject jsonObject = new JSONObject();
						if(tmp.getSynchronizeDate()!= null && !setMaxDate){
							synchronizeDate = sdf.format(tmp.getSynchronizeDate());
							setMaxDate = true;
						}
						jsonObject = JSONObject.fromObject(tmp, jsonConfig);
						jsonArray.add(jsonObject);
					}
					returnJsonStr = jsonArray.toString();
				}else{
					returnJsonStr  = JSONUtil.beanListToJson(balancesList, jsonConfig);
				}
			}
		}
		map.put("synchronizeDate", synchronizeDate);
		map.put("data", returnJsonStr);
		return map;
	}
	@Override
	public HashMap<String, String> fecthCcidData(String lastUpdateDate,
			String ledgerCode, String userName, String userPwd, String fetchType)
			throws Exception {
		String returnJsonStr = "[]";
		String synchronizeDate = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean setMaxDate = false;
		Date date = null;
		if(StringUtils.isBlank(lastUpdateDate) || StringUtils.isEmpty(lastUpdateDate)){
			//throw new Exception("同步日期不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQBNWK", null));
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(lastUpdateDate);
				synchronizeDate = sdf.format(new Date());
			} catch (ParseException e) {
				//throw new Exception("同步日期格式不正确！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQGSBZQ", null));
			}
			List<CCID> ccidList = xCGLFetchDataDAO.fecthCcidData(ledgerCode, date, userName, userPwd);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			if(ccidList!=null && ccidList.size()>0){
				if("reportFecth".equalsIgnoreCase(fetchType)){
					JSONArray jsonArray = new JSONArray();
					for(CCID tmp:ccidList){
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("id", tmp.getCcid());
						jsonObject.put("code", tmp.getCcidCode());
						jsonObject.put("name", tmp.getCcidName());
						jsonObject.put("isLeaf", tmp.getIsLeaf());

						if(tmp.getSynchronizeDate() != null && !setMaxDate){
							synchronizeDate = sdf.format(tmp.getSynchronizeDate());
							setMaxDate = true;
						}
						jsonArray.add(jsonObject);
					}
					returnJsonStr = jsonArray.toString();
				}else{
					returnJsonStr  = JSONUtil.beanListToJson(ccidList, jsonConfig);
				}
			}
		}
		map.put("synchronizeDate", synchronizeDate);
		map.put("data", returnJsonStr);
		return map;
	}
	@Override
	public HashMap<String, String> fecthVoucherData(String lastUpdateDate,
			String ledgerCode, String userName, String userPwd, String fetchType)
			throws Exception {
		String returnJsonStr = "[]";
		String synchronizeDate = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean setMaxDate = false;
		Date date = null;
		if(StringUtils.isBlank(lastUpdateDate) || StringUtils.isEmpty(lastUpdateDate)){
			//throw new Exception("同步日期不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQBNWK", null));
		}else if(StringUtils.isBlank(ledgerCode) || StringUtils.isEmpty(ledgerCode)){
			//throw new Exception("同步账簿编码不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBZBBMBNWK", null));
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(lastUpdateDate);
				synchronizeDate = sdf.format(new Date());
			} catch (ParseException e) {
				//throw new Exception("同步日期格式不正确！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQGSBZQ", null));
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			List<VHead> headList = xCGLFetchDataDAO.fetchVoucherHeaderData(ledgerCode, date, userName, userPwd);
			if (headList != null && headList.size() > 0) {
				List<VLine> lineList = xCGLFetchDataDAO.fetchVoucherLinesData(ledgerCode, date, userName, userPwd);
				jsonConfig.setExcludes(new String[] { "synchronizeDate","lines","addLines","updLines","delLines","accList","accIdList"});
				JSONArray jsonArray = new JSONArray();
				for (VHead tmp : headList) {
					JSONObject jsonObject = new JSONObject();
					if (tmp.getSynchronizeDate() != null && !setMaxDate) {
						synchronizeDate = sdf.format(tmp.getSynchronizeDate());
						setMaxDate = true;
					}
					jsonObject = JSONObject.fromObject(tmp, jsonConfig);
					//======================================================
					//---------------添加凭证明细行------------------------------
					//======================================================
					if(lineList!=null && lineList.size()>0){
						JSONArray lineArray = new JSONArray();
						for (VLine line : lineList) {
							if(line.getHeadId().equalsIgnoreCase(tmp.getHeadId())){
								JSONObject subJsonObj = new JSONObject();
								subJsonObj = JSONObject.fromObject(line, jsonConfig);
								lineArray.add(subJsonObj);
							}
						}
						jsonObject.put("lines", lineArray.toString());
					}else{
						jsonObject.put("lines", "[]");
					}
					jsonArray.add(jsonObject);
				}
				returnJsonStr = jsonArray.toString();
			}
		}
		map.put("synchronizeDate", synchronizeDate);
		map.put("data", returnJsonStr);
		return map;
	}
	@Override
	public HashMap<String, String> fecthCashSumData(String lastUpdateDate,
			String ledgerCode, String userName, String userPwd, String fetchType)
			throws Exception {
		String returnJsonStr = "[]";
		String synchronizeDate = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean setMaxDate = false;
		Date date = null;
		if(StringUtils.isBlank(lastUpdateDate) || StringUtils.isEmpty(lastUpdateDate)){
			//throw new Exception("同步日期不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQBNWK", null));
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(lastUpdateDate);
				synchronizeDate = sdf.format(new Date());
			} catch (ParseException e) {
				//throw new Exception("同步日期格式不正确！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQGSBZQ", null));
			}
			List<CashSum> cashSumList = xCGLFetchDataDAO.fetchCashSumData(ledgerCode,date, userName, userPwd);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			if(cashSumList!=null && cashSumList.size()>0){
				if("reportFecth".equalsIgnoreCase(fetchType)){
					jsonConfig.setExcludes(new String[]{"CREATION_DATE","CREATED_BY","LAST_UPDATE_DATE","LAST_UPDATED_BY","synchronizeDate"});
					JSONArray jsonArray = new JSONArray();
					for(CashSum tmp:cashSumList){
						JSONObject jsonObject = new JSONObject();
						if(tmp.getSynchronizeDate()!= null && !setMaxDate){
							synchronizeDate = sdf.format(tmp.getSynchronizeDate());
							setMaxDate = true;
						}
						jsonObject = JSONObject.fromObject(tmp, jsonConfig);
						jsonArray.add(jsonObject);
					}
					returnJsonStr = jsonArray.toString();
				}else{
					returnJsonStr  = JSONUtil.beanListToJson(cashSumList, jsonConfig);
				}
			}
		}
		map.put("synchronizeDate", synchronizeDate);
		map.put("data", returnJsonStr);
		return map;
	}
	@Override
	public HashMap<String, String> fetchCashItemDate(String lastUpdateDate,
			String userName, String userPwd, String fetchType) throws Exception {
		String returnJsonStr = "[]";
		String synchronizeDate = "";
		HashMap<String, String> map = new HashMap<String, String>();
		boolean setMaxDate = false;
		Date date = null;
		if(StringUtils.isBlank(lastUpdateDate) || StringUtils.isEmpty(lastUpdateDate)){
			//throw new Exception("同步日期不能为空！");
			throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQBNWK", null));
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				date = sdf.parse(lastUpdateDate);
				synchronizeDate = sdf.format(new Date());
			} catch (ParseException e) {
				//throw new Exception("同步日期格式不正确！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_GL_TBRQGSBZQ", null));
			}
			List<CashItem> cashItemList = xCGLFetchDataDAO.fetchCashItemData(date, userName, userPwd);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setIgnoreDefaultExcludes(false);
			jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd HH:mm:ss"));
			if(cashItemList!=null && cashItemList.size()>0){
				if("reportFecth".equalsIgnoreCase(fetchType)){
					jsonConfig.setExcludes(new String[]{"CREATION_DATE","LAST_UPDATE_DATE","synchronizeDate"});
					JSONArray jsonArray = new JSONArray();
					for(CashItem tmp:cashItemList){
						JSONObject jsonObject = new JSONObject();
						if(tmp.getSynchronizeDate()!= null && !setMaxDate){
							synchronizeDate = sdf.format(tmp.getSynchronizeDate());
							setMaxDate = true;
						}
						jsonObject = JSONObject.fromObject(tmp, jsonConfig);
						jsonArray.add(jsonObject);
					}
					returnJsonStr = jsonArray.toString();
				}else{
					returnJsonStr  = JSONUtil.beanListToJson(cashItemList, jsonConfig);
				}
			}
		}
		map.put("synchronizeDate", synchronizeDate);
		map.put("data", returnJsonStr);
		return map;
	}

}
