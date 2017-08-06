package com.xzsoft.xc.apar.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.CashItem;
import com.xzsoft.xc.gl.modal.Customer;
import com.xzsoft.xc.gl.modal.Employee;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.gl.modal.Vendor;
import com.xzsoft.xc.gl.modal.VoucherCategory;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.modal.Department;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.modal.Project;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName ApOrArVoucherDocCat
  * @Description 组合凭证需要的信息
  * @author RenJianJian
  * @date 2016年9月29日 下午4:41:40
 */
@Service("apOrArVoucherDocCat")
public class ApOrArVoucherDocCat{
	//日志记录器
	private static final Logger log = Logger.getLogger(ApOrArVoucherDocCat.class);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
	@Resource
	private RedisDao redisDaoImpl;
	@Resource
	private XCGLCommonDAO xcglCommonDAO;

	/**
	 * 
	  * @Title getCaCode 方法名
	  * @Description 取得现金流量项目编码
	  * @param caId
	  * @param isCash
	  * @param accCode
	  * @param language
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getCaCode(String caId,String isCash,String accCode,String language) throws Exception{
		String caCode = null;
		if("Y".equals(isCash)){
			if(caId == null || "".equals(caId)){
				//throw new Exception("凭证分录科目【"+accCode+"】为现金或银行科目,请选择现金流量项目");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getCaCode_02", null)+"【"+accCode+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getCaCode_03", null));
			}
			String cashKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_CASH_ITEMS);
			Object valJson = redisDaoImpl.hashGet(cashKey,caId);
			if(valJson == null){
				//从数据库查询处理 
				CashItem item = xcglCommonDAO.getCashItemById(caId);
				if(item == null || "".equals(item)){
					//throw new Exception("现金流量没有启用，请重新选择！");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getCaCode_01", null)+"【"+caId+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getDeptCode_02", null));
				}
				caCode = item.getCaCode();
			}else{
				//从缓存中处理 
				JSONObject valJo = new JSONObject(String.valueOf(valJson));
				caCode = valJo.getString("code");
			}
		}
		return caCode;
	}
	/**
	 * 
	  * @Title getAccMap 方法名
	  * @Description 获取科目信息
	  * @param hrcyId
	  * @param accId
	  * @return
	  * @throws Exception
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getAccMap(String hrcyId,String accId) throws Exception{
		HashMap<String,String> accMap = new HashMap<String,String>();
		
		String isCash = null;
		String accCode = null;
		String accKey =  XCRedisKeys.getAccKey(hrcyId);
		Object accJson = redisDaoImpl.hashGet(accKey,accId);
		if(accJson == null){
			//从数据库查询处理 
			Account acc = xcglCommonDAO.getAccountById(accId);
			if(acc == null){
				//throw new Exception("科目("+accId+")未分配到当前账簿或已失效");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getAccMap_01", null)+"【"+accId+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getAccMap_02", null));
			}
			if("Y".equals(acc.getIsBankAcc()) || "Y".equals(acc.getIsCashAcc())){
				isCash = "Y";
			}
			if("N".equals(acc.getIsLeaf())){
				throw new Exception("科目("+acc.getAccCode()+")不是末级科目，请重新选择！");
			}
			accCode = acc.getAccCode();
		}else{
			//从缓存中处理 
			JSONObject accJo = new JSONObject(String.valueOf(accJson));
			isCash = accJo.getString("isCash");
			accCode = accJo.getString("code");
		}
		accMap.put("accCode",accCode);
		accMap.put("isCash",isCash);
		return accMap;
	}
	//-------------应付
	/**
	 * 
	  * @Title getVendorCode 方法名
	  * @Description 处理供应商信息（应付模块）
	  * @param apDocumentBean
	  * @param ledger
	  * @param language
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getVendorCode(ApDocumentHBean apDocumentBean,Ledger ledger,String language) throws Exception {
		String vendorCode = "00";
		if(apDocumentBean.getVENDOR_ID() !=null && !"".equals(apDocumentBean.getVENDOR_ID())){
			
			String venKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_AP_VENDORS) ;
			
			Object venJson = redisDaoImpl.hashGet(venKey,apDocumentBean.getVENDOR_ID());
			if(venJson == null){
				//从数据库查询处理 
				Vendor vendor = xcglCommonDAO.getVendorById(apDocumentBean.getVENDOR_ID());
				if(vendor == null || "".equals(vendor)){
					throw new Exception(XipUtil.getMessage(language, "XC_AP_VENDOR_IS_NULL", null));//供应商没有启用或者已经过期，请重新选择！
				}
				vendorCode = vendor.getVendorCode();
			}else{
				//从缓存中处理 
				JSONObject vendorJo = new JSONObject(String.valueOf(venJson));
				vendorCode = vendorJo.getString("code");
			}
		}
		return vendorCode;
	}
	/**
	 * 
	  * @Title getEmpCode 方法名
	  * @Description 处理个人往来（应付模块）
	  * @param apDocumentBean
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getEmpCode(ApDocumentHBean apDocumentBean) throws Exception {
		String empCode = null;
		Employee emp = xcglCommonDAO.getEmployeeByUserId(apDocumentBean.getCREATED_BY());
		if (emp == null) {
			empCode = "00";
		}else{
			empCode = emp.getEmpCode(); 
		}
		return empCode;
	}
	/**
	 * 
	  * @Title getPrjCode 方法名
	  * @Description 处理项目（应付模块）
	  * @param apDocumentBean
	  * @param ledger
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getPrjCode(ApDocumentHBean apDocumentBean,Ledger ledger) throws Exception {
		String prjCode = "00";
		if(apDocumentBean.getPROJECT_ID() !=null && !"".equals(apDocumentBean.getPROJECT_ID())){
			
			String prjKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_PM_PROJECTS, ledger.getLedgerId()) ;
			Object prjJson = redisDaoImpl.hashGet(prjKey,apDocumentBean.getPROJECT_ID());
			if(prjJson == null){
				//从数据库查询处理 
				Project prj = xcglCommonDAO.getProjectById(apDocumentBean.getPROJECT_ID());
				if(prj != null && !"".equals(prj)){
					prjCode = prj.getProjectCode();
					//throw new Exception("项目没有启用或者已经过期，请重新选择！");
				}
			}else{
				//从缓存中处理 
				JSONObject prjJo = new JSONObject(String.valueOf(prjJson));
				prjCode = prjJo.getString("code");
			}
		}
		return prjCode;
	}
	/**
	 * 
	  * @Title getDeptCode 方法名
	  * @Description 处理成本中心（应付模块）
	  * @param apDocumentBean
	  * @param ledger
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getDeptCode(ApDocumentHBean apDocumentBean,Ledger ledger) throws Exception {
		String deptCode = "00";
		if(apDocumentBean.getDEPT_ID() !=null && !"".equals(apDocumentBean.getDEPT_ID())){
			
			String deptKey = XCRedisKeys.getLedgerSegKey(XConstants.XIP_PUB_DETPS, ledger.getLedgerId()) ;
			
			Object deptJson = redisDaoImpl.hashGet(deptKey,apDocumentBean.getDEPT_ID());
			if(deptJson == null){
				//从数据库查询处理 
				Department dept = xcglCommonDAO.getDeptById(apDocumentBean.getDEPT_ID());
				if(dept != null && !"".equals(dept)){
					deptCode = dept.getDeptCode();
					//throw new Exception("部门没有启用，请重新选择！");
				}
			}else{
				//从缓存中处理 
				JSONObject deptJo = new JSONObject(String.valueOf(deptJson));
				deptCode = deptJo.getString("code");
			}
		}
		return deptCode;
	}
	//-------------应收
	/**
	 * 
	  * @Title getCustomerCode 方法名
	  * @Description 处理客户信息（应收模块）
	  * @param arDocumentBean
	  * @param ledger
	  * @param language
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getCustomerCode(ArDocumentHBean arDocumentBean,Ledger ledger,String language) throws Exception {
		String customerCode = "00";
		if(arDocumentBean.getCUSTOMER_ID() !=null && !"".equals(arDocumentBean.getCUSTOMER_ID())){
			
			String cusKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_AR_CUSTOMERS) ;
			
			Object cusJson = redisDaoImpl.hashGet(cusKey,arDocumentBean.getCUSTOMER_ID());
			if(cusJson == null){
				//从数据库查询处理 
				Customer customer = xcglCommonDAO.getCustomerById(arDocumentBean.getCUSTOMER_ID());
				if(customer == null || "".equals(customer)){
					throw new Exception(XipUtil.getMessage(language, "XC_AR_CUSTOMER_IS_NULL", null));//供应商没有启用或者已经过期，请重新选择！
				}
				customerCode = customer.getCustomerCode();
			}else{
				//从缓存中处理 
				JSONObject customerJo = new JSONObject(String.valueOf(cusJson));
				customerCode = customerJo.getString("code");
			}
		}
		return customerCode;
	}
	/**
	 * 
	  * @Title getEmpCode 方法名
	  * @Description 处理个人往来（应收模块）
	  * @param arDocumentBean
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getEmpCode(ArDocumentHBean arDocumentBean) throws Exception {
		String empCode = null;
		Employee emp = xcglCommonDAO.getEmployeeByUserId(arDocumentBean.getCREATED_BY());
		if (emp == null) {
			empCode = "00";
		}else{
			empCode = emp.getEmpCode(); 
		}
		return empCode;
	}
	/**
	 * 
	  * @Title getDeptCode 方法名
	  * @Description 处理成本中心（应收模块）
	  * @param arDocumentBean
	  * @param ledger
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getDeptCode(ArDocumentHBean arDocumentBean,Ledger ledger) throws Exception {
		String deptCode = "00";
		if(arDocumentBean.getDEPT_ID() !=null && !"".equals(arDocumentBean.getDEPT_ID())){
			
			String deptKey = XCRedisKeys.getLedgerSegKey(XConstants.XIP_PUB_DETPS, ledger.getLedgerId()) ;
			
			Object deptJson = redisDaoImpl.hashGet(deptKey,arDocumentBean.getDEPT_ID());
			if(deptJson == null){
				//从数据库查询处理 
				Department dept = xcglCommonDAO.getDeptById(arDocumentBean.getDEPT_ID());
				if(dept != null && !"".equals(dept)){
					deptCode = dept.getDeptCode();
					//throw new Exception("部门没有启用，请重新选择！");
				}
			}else{
				//从缓存中处理 
				JSONObject deptJo = new JSONObject(String.valueOf(deptJson));
				deptCode = deptJo.getString("code");
			}
		}
		return deptCode;
	}
	/**
	 * 
	  * @Title getPrjCode 方法名
	  * @Description 处理项目（应收模块）
	  * @param arDocumentBean
	  * @param ledger
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getPrjCode(ArDocumentHBean arDocumentBean,Ledger ledger) throws Exception {
		String prjCode = "00";
		if(arDocumentBean.getPROJECT_ID() !=null && !"".equals(arDocumentBean.getPROJECT_ID())){
			
			String prjKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_PM_PROJECTS, ledger.getLedgerId()) ;
			Object prjJson = redisDaoImpl.hashGet(prjKey,arDocumentBean.getPROJECT_ID());
			if(prjJson == null){
				//从数据库查询处理 
				Project prj = xcglCommonDAO.getProjectById(arDocumentBean.getPROJECT_ID());
				if(prj != null && !"".equals(prj)){
					prjCode = prj.getProjectCode();
					//throw new Exception("项目没有启用或者已经过期，请重新选择！");
				}
			}else{
				//从缓存中处理 
				JSONObject prjJo = new JSONObject(String.valueOf(prjJson));
				prjCode = prjJo.getString("code");
			}
		}
		return prjCode;
	}
	
	//组合凭证
	/**
	 * 
	  * @Title mergeVLines 方法名
	  * @Description 合并凭证分录信息
	  * @param head
	  * @param dLines
	  * @param cLines
	  * @return
	  * @throws Exception
	  * @return VHead 返回类型
	 */
	public VHead mergeVLines(VHead head,List<VLine> dLines,List<VLine> cLines) throws Exception{
		try{
			//组合凭证信息并设置凭证分录序号
			List<VLine> lines = new ArrayList<VLine>();
			int i = 1;
			//借方分录
			for(VLine line : dLines){
				line.setOrderBy(i);
				i++;
			}
			lines.addAll(dLines);
			//贷方分录
			for(VLine line : cLines){
				line.setOrderBy(i);
				i++;
			}
			lines.addAll(cLines);
			//封装凭证信息
			head.setLines(lines);
			
		} catch (Exception e){
			throw e;
		}
		
		return head;
	}
	
	/**
	 * 
	  * @Title createVHead 方法名
	  * @Description 生成应付模块凭证头信息
	  * @param apDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @return VHead 返回类型
	 */
	public VHead createVHead(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception{
		VHead head = new VHead();
		try{
			JSONObject paramJo = new JSONObject(paramJson);
			String accDate = paramJo.getString("accDate");		//会计日期
			int attchTotal = paramJo.getInt("attchTotal");		//附件张数
			head.setLedgerCode(ledger.getLedgerCode());			//账簿编码
			head.setPeriodCode(accDate.substring(0,7));			//会计期
			//设置凭证来源编码
			head.setSrcCode(vs.getSrcCode());
			//设置凭证来源ID
			head.setSrcId(paramJo.getString("sourceId"));
			//设置凭证分类
			if(vs.getvCat() == null || "".equals(vs.getvCat())){
				//throw new Exception("编码为【"+vs.getSrcCode()+"】的凭证来源未设置凭证分类,请确认！");//编码为【"+vs.getSrcCode()+"】的凭证来源未设置凭证分类,请确认！
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_01", null)+"【"+vs.getSrcCode()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_02", null));
			}
			else{
				String catKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_V_CATEGORY);
				Object catJson = redisDaoImpl.hashGet(catKey,vs.getvCat());
				if(catJson == null){
					//从数据库中查询
					VoucherCategory cat = xcglCommonDAO.getCategory(vs.getvCat());
					head.setCategoryCode(cat.getCategoryCode());	
				}
				else{
					//从缓存中处理
					JSONObject jo = new JSONObject(String.valueOf(catJson));
					head.setCategoryCode(jo.getString("code"));	
				}
			}
			head.setSummary(apDocumentBean.getDESCRIPTION());	//摘要
			head.setAttchTotal(attchTotal);						//附件数
			head.setSerialNum(null);							//凭证号
			head.setTemplateType("1");							//凭证模板
			head.setVStatus("1");								//凭证状态
			head.setCreatedBy(CurrentUserUtil.getCurrentUserId());
			head.setCreationDate(sdf.parse(accDate));
			head.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
			head.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
			
		} catch (Exception e){
			log.error(e.getMessage());
			throw e;
		}
		return head;
	}
	
	/**
	 * 
	  * @Title createVHead 方法名
	  * @Description 生成应收模块凭证头信息
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @return
	  * @throws Exception
	  * @return VHead 返回类型
	 */
	public VHead createVHead(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson) throws Exception{
		VHead head = new VHead();
		try{
			JSONObject paramJo = new JSONObject(paramJson);
			String accDate = paramJo.getString("accDate");		//会计日期
			int attchTotal = paramJo.getInt("attchTotal");		//附件张数
			head.setLedgerCode(ledger.getLedgerCode());			//账簿编码
			head.setPeriodCode(accDate.substring(0,7));			//会计期
			//设置凭证来源编码
			head.setSrcCode(vs.getSrcCode());
			//设置凭证来源ID
			head.setSrcId(paramJo.getString("sourceId"));
			//设置凭证分类
			if(vs.getvCat() == null || "".equals(vs.getvCat())){
				//throw new Exception("编码为【"+vs.getSrcCode()+"】的凭证来源未设置凭证分类,请确认！");
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_01", null)+"【"+vs.getSrcCode()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_02", null));
			}
			else{
				String catKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_V_CATEGORY);
				Object catJson = redisDaoImpl.hashGet(catKey,vs.getvCat());
				if(catJson == null){
					//从数据库中查询
					VoucherCategory cat = xcglCommonDAO.getCategory(vs.getvCat());
					head.setCategoryCode(cat.getCategoryCode());	
				}
				else{
					//从缓存中处理
					JSONObject jo = new JSONObject(String.valueOf(catJson));
					head.setCategoryCode(jo.getString("code"));	
				}
			}
			head.setSummary(arDocumentBean.getDESCRIPTION());	//摘要
			head.setAttchTotal(attchTotal);						//附件数
			head.setSerialNum(null);							//凭证号
			head.setTemplateType("1");							//凭证模板
			head.setVStatus("1");								//凭证状态
			head.setCreatedBy(CurrentUserUtil.getCurrentUserId());
			head.setCreationDate(sdf.parse(accDate));
			head.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
			head.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
			
		} catch (Exception e){
			log.error(e.getMessage());
			throw e;
		}
		return head;
	}
}