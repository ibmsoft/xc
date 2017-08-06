package com.xzsoft.xc.gl.common.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import net.sf.json.JsonConfig;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Comment;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.gl.common.AbstractVoucherHandler;
import com.xzsoft.xc.gl.common.XCGLCommonService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.mapper.VoucherMapper;
import com.xzsoft.xc.gl.mapper.XCGLCommonMapper;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.CCID;
import com.xzsoft.xc.gl.modal.Employee;
import com.xzsoft.xc.gl.modal.Period;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VHeadDTO;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VLineDTO;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.gl.service.XCGLSumBalanceService;
import com.xzsoft.xc.gl.util.XCGLConstants;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCJSONUtil;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.framework.util.JsonDateValueProcessor;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.PlatformUtil;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: VoucherHandlerCommonService 
 * @Description: 凭证处理公共方法
 * @author linp
 * @date 2016年3月2日 上午10:10:04 
 *
 */
@Service("voucherHandlerCommonService")
public class VoucherHandlerCommonService extends AbstractVoucherHandler {
	// 日志记录器
	private static Logger log = Logger.getLogger(AbstractVoucherHandler.class.getName()) ;
	// 数值正则表达式
	Pattern pattern = Pattern.compile("(\\-?)[0-9]*(\\.?)[0-9]*");
	
	@Resource
	private VoucherMapper voucherMapper ;
	@Resource
	private XCGLCommonMapper xcglCommonMapper ;
	@Resource
	private RedisDao redisDaoImpl ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	@Resource
	private XCGLSumBalanceService xcglSumBalanceService ;
	@Resource
	private XCGLCommonService xcglCommonService ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getFullCode</p> 
	 * <p>Description: 获取科目组合的全编码信息 
	 * 
	 * 	全编码组合顺序为:
	 * 	        科目.供应商.客户.产品.内部往来.个人往来.成本中心.项目.自定义1.自定义2.自定义3.自定义4
	 * 	   ACC_CODE.VENDOR_CODE.CUSTOMER_CODE.PRODUCT_CODE.ORG_CODE.EMP_CODE.DEPT_CODE.PROJECT_CODE.CUSTOM1_CODE.CUSTOM2_CODE.CUSTOM3_CODE.CUSTOM4_CODE
	 * 
	 * @param ledgerId
	 * @param accId
	 * @param segments
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#getFullCode(java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public String getFullCode(String ledgerId, String accId, HashMap<String,String> segments)throws Exception {
		StringBuffer fullCode = new StringBuffer() ;
		String lang = XCUtil.getLang();//语言值
		try {
			// 段名称常量
			String account = XConstants.XC_GL_ACCOUNTS ;	//科目
			String vendor = XConstants.XC_AP_VENDORS ;
			String customer = XConstants.XC_AR_CUSTOMERS ;
			String product = XConstants.XC_GL_PRODUCTS ;
			String org = XConstants.XIP_PUB_ORGS ;	// 内部往来
			String emp = XConstants.XIP_PUB_EMPS ;	// 个人往来
			String dept = XConstants.XIP_PUB_DETPS ;	 // 成本中心
			String project = XConstants.XC_PM_PROJECTS ;
			String custom1 = XConstants.XC_GL_CUSTOM1 ;
			String custom2 = XConstants.XC_GL_CUSTOM2 ;
			String custom3 = XConstants.XC_GL_CUSTOM3 ;
			String custom4 = XConstants.XC_GL_CUSTOM4 ;
			
			
			// 科目编码
			String hrcyId = xcglCommonDAO.getLedger(ledgerId).getAccHrcyId() ; // 科目体系ID
			String accKey = XCRedisKeys.getAccKey(hrcyId) ;
			String accCode = this.getSegCodeVal(accKey, accId, account)  ;
			
			// 供应商编码
			String vendorId = segments.get(vendor) ;		
			String vendorKey = XCRedisKeys.getGlobalSegKey(vendor);
			String vendorCode = ((vendorId==null || "".equals(vendorId) || "00".equals(vendorId)) ? "00" : this.getSegCodeVal(vendorKey, vendorId, vendor)) ;
			
			// 客户编码
			String customerId = segments.get(customer);		
			String customerKey = XCRedisKeys.getGlobalSegKey(customer) ;
			String customerCode = ((customerId==null || "".equals(customerId) || "00".equals(customerId)) ? "00" : this.getSegCodeVal(customerKey, customerId, customer)) ;
			
			// 产品编码
			String productId = segments.get(product);		
			String productKey = XCRedisKeys.getGlobalSegKey(product) ;
			String productCode = ((productId==null || "".equals(productId) || "00".equals(productId)) ? "00" : this.getSegCodeVal(productKey, productId, product)) ;
			
			// 内部往来编码
			String orgId = segments.get(org);	
			String orgKey = XCRedisKeys.getGlobalSegKey(org) ;
			String orgCode = ((orgId==null || "".equals(orgId) || "00".equals(orgId)) ? "00" : this.getSegCodeVal(orgKey, orgId, org)) ;
			
			// 个人往来编码
			String empId = segments.get(emp);		
			String empKey = XCRedisKeys.getGlobalSegKey(emp) ;
			String empCode = ((empId==null || "".equals(empId) || "00".equals(empId)) ? "00" : this.getSegCodeVal(empKey, empId, emp)) ;
			
			// 成本中心编码
			String deptId = segments.get(dept);			
			String deptKey = XCRedisKeys.getLedgerSegKey(dept, ledgerId) ;
			String deptCode = ((deptId==null || "".equals(deptId) || "00".equals(deptId)) ? "00" : this.getSegCodeVal(deptKey, deptId, dept)) ;
	
			// 项目编码
			String projectId = segments.get(project);		
			String prjKey = XCRedisKeys.getLedgerSegKey(project, ledgerId) ;
			String projectCode = ((projectId==null || "".equals(projectId) || "00".equals(projectId)) ? "00" : this.getSegCodeVal(prjKey, projectId, project)) ;
			
			// 自定义段1编码
			String custom1Id = segments.get(custom1);		
			String cust1Key = XCRedisKeys.getLedgerSegKey(custom1, ledgerId) ;
			String custom1Code = ((custom1Id==null || "".equals(custom1Id) || "00".equals(custom1Id)) ? "00" : this.getSegCodeVal(cust1Key, custom1Id, custom1)) ;
			
			// 自定义段2编码
			String custom2Id = segments.get(custom2);		
			String cust2Key = XCRedisKeys.getLedgerSegKey(custom2, ledgerId) ;
			String custom2Code = ((custom2Id==null || "".equals(custom2Id) || "00".equals(custom2Id)) ? "00" : this.getSegCodeVal(cust2Key, custom2Id, custom2)) ;
			
			// 自定义段3编码
			String custom3Id = segments.get(custom3);		
			String cust3Key = XCRedisKeys.getLedgerSegKey(custom3, ledgerId) ;
			String custom3Code = ((custom3Id==null || "".equals(custom3Id) || "00".equals(custom3Id)) ? "00" : this.getSegCodeVal(cust3Key, custom3Id, custom3)) ;
			
			// 自定义段4编码
			String custom4Id = segments.get(custom4);		
			String cust4Key = XCRedisKeys.getLedgerSegKey(custom4, ledgerId) ;
			String custom4Code = ((custom4Id==null || "".equals(custom4Id) || "00".equals(custom4Id)) ? "00" : this.getSegCodeVal(cust4Key, custom4Id, custom4)) ;
			
			// 生成科目组合全编码信息
			fullCode.append(accCode).append("/")
			.append(vendorCode).append("/")
			.append(customerCode).append("/")
			.append(productCode).append("/")
			.append(orgCode).append("/")
			.append(empCode).append("/")
			.append(deptCode).append("/")
			.append(projectCode).append("/")
			.append(custom1Code).append("/")
			.append(custom2Code).append("/")
			.append(custom3Code).append("/")
			.append(custom4Code) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_CCID_CODE_FAIL", null)+e.getMessage()) ;
		}
		
		return fullCode.toString();
		
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: getCCID</p> 
	 * <p>Description: 取得科目组合ID, 如果科目组合ID不存在则创建并写入Redis缓存 </p> 
	 * @param fullCode
	 * @param ledgerId
	 * @param accId
	 * @param segments
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.dao.VoucherHandlerDAO#getCCID(java.lang.String, java.lang.String, java.lang.String, java.util.HashMap)
	 */
	@Override
	public JSONObject getCCID(String fullCode,String ledgerId, String accId, HashMap<String, String> segments) throws Exception {
		JSONObject jo = new JSONObject() ;
		List<CCID> ccids = new ArrayList<CCID>() ;
		String lang = XCUtil.getLang();//语言值
		try {
			// 当前对象
			AbstractVoucherHandler avh = (AbstractVoucherHandler) AppContext.getApplicationContext().getBean("voucherHandlerCommonService") ;
			
			// 如果科目组合全编码为空
			if(fullCode == null){
				// 获取科目体系ID
				String hrcyId = xcglCommonDAO.getLedger(ledgerId).getAccHrcyId() ;  
				
				// 判断会计科目是否同步到Redis缓存
				String accKey = XCRedisKeys.getAccKey(hrcyId) ;
				
				Object accJson = redisDaoImpl.hashGet(accKey, accId) ;
				
				if(accJson == null){ // 刷新Redis的科目缓存信息
					
					xcglCommonService.syncAccHrcy2Redis(hrcyId, "add") ;
					
					// 获取缓存信息，判断科目是否存在
					accJson = redisDaoImpl.hashGet(accKey, accId) ;
					if(accJson == null){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCOUNT_NO_EXIST", null)+accId) ;
						
					}else{
						// 判断科目是否为末级科目
						JSONObject accJo = new JSONObject(String.valueOf(accJson)) ;
						if(!"Y".equals(accJo.get("isLeaf"))){
							throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+accJo.get("code")+XipUtil.getMessage(lang, "XC_GL_NO_END_ACCOUNT", null)) ;
						}
					}
				}
				
				// 生成CCID
				avh.newCCID(ledgerId, hrcyId, accId, segments, "Y", jo, ccids) ;
				
				
			}else{
				// 获取CCID的key值
				String ccidKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CCID, ledgerId) ;
				
				// 取得CCID的JSON串信息
				Object ccidJson = redisDaoImpl.hashGet(ccidKey, fullCode) ;
				
				// 处理CCID信息
				if(ccidJson == null){ // ccid不存在则刷新缓存
					
					// 将数据库CCID加载到Redis缓冲
					xcglCommonService.syncCCID2Redis(ledgerId) ;
					
					// 从缓存中获取CCID信息
					ccidJson = redisDaoImpl.hashGet(ccidKey, fullCode) ;
					
					if(ccidJson == null){ 
						// 刷新缓存后ccid仍不存在，则创建ccid
						String hrcyId = xcglCommonDAO.getLedger(ledgerId).getAccHrcyId() ;  
						avh.newCCID(ledgerId, hrcyId, accId, segments, "Y", jo, ccids) ;
						
					}else{ // ccid已生成
						jo = new JSONObject(String.valueOf(ccidJson)) ;
					}
					
				}else{
					//转换成JSON对象
					jo = new JSONObject(String.valueOf(ccidJson)) ;
				}
			}
			
			// 将新创建的科目组合信息写入redis缓存
			if(!ccids.isEmpty()){
				String ccidKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CCID, ledgerId)  ;
				for(CCID ccid : ccids){
					// CCID缓存JSON串信息
					JSONObject obj = new JSONObject() ;
					obj.put("id", ccid.getCcid()) ;
					obj.put("code", ccid.getCcidCode()) ;
					obj.put("name", ccid.getCcidName()) ;
					
					redisDaoImpl.hashPut(ccidKey, ccid.getCcidCode(), obj.toString());
					redisDaoImpl.hashPut(ccidKey, ccid.getCcid(), obj.toString());
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: getSegCodeVal 
	 * @Description: 取得辅助核算段的编码值信息
	 * @param key
	 * @param hashKey
	 * @return
	 * @throws Exception
	 */
	private String getSegCodeVal(String segKey, String hashKey, String seg)throws Exception {
		String segCode = null ;
		String lang = XCUtil.getLang();//语言值
		try {
			// 获取缓存数据
			Object valJson = redisDaoImpl.hashGet(segKey, hashKey) ; 
			
			// 如果redis中不存在段值, 则执行数据缓存加载处理
			if(valJson == null){
				
				if(XConstants.XC_GL_ACCOUNTS.equals(seg)){ // 科目
					String hrcyId = segKey.substring(segKey.lastIndexOf(".")+1, segKey.length()) ;
					xcglCommonService.syncAccHrcy2Redis(hrcyId, XCGLConstants.REDIS_OP_ADD) ;
					
				}else if(XConstants.XC_AP_VENDORS.equals(seg)){ // 供应商
					xcglCommonService.syncVendors2Redis(seg) ;
					
				}else if(XConstants.XC_AR_CUSTOMERS.equals(seg)){ // 客户
					xcglCommonService.syncCustomers2Redis(seg) ;
					
				}else if(XConstants.XC_GL_PRODUCTS.equals(seg)){ // 产品
					xcglCommonService.syncProducts2Redis(seg) ;
					
				}else if(XConstants.XIP_PUB_ORGS.equals(seg)){ // 内部往来
					xcglCommonService.syncOrgs2Redis(seg) ;
					
				}else if(XConstants.XIP_PUB_EMPS.equals(seg)){ // 个人往来
					xcglCommonService.syncEmps2Redis(seg) ;
					
				}else if(XConstants.XIP_PUB_DETPS.equals(seg)){ // 成本中心
					String ledgerId = segKey.substring(segKey.lastIndexOf(".")+1, segKey.length()) ;
					xcglCommonService.syncDepts2Redis(ledgerId, seg, XCGLConstants.REDIS_OP_ADD) ;
					
				}else if(XConstants.XC_PM_PROJECTS.equals(seg)){ // PA项目
					String ledgerId = segKey.substring(segKey.lastIndexOf(".")+1, segKey.length()) ;
					xcglCommonService.syncProjects2Redis(ledgerId, seg, XCGLConstants.REDIS_OP_ADD) ;
					
				}else if(XConstants.XC_GL_CUSTOM1.equals(seg)
							|| XConstants.XC_GL_CUSTOM2.equals(seg)
							|| XConstants.XC_GL_CUSTOM3.equals(seg)
							|| XConstants.XC_GL_CUSTOM4.equals(seg)){ // 自定义维度[1~4]
					String ledgerId = segKey.substring(segKey.lastIndexOf(".")+1, segKey.length()) ;
					xcglCommonService.syncCustoms2Redis(ledgerId, seg, XCGLConstants.REDIS_OP_ADD) ;
				}
				
				// 再次获取缓存数据
				valJson = redisDaoImpl.hashGet(segKey, hashKey) ;
			}
			
			// 判断传入的辅助值是否合法
			if(valJson == null){
				String message = "";
				switch(seg){
				case XConstants.XC_AP_VENDORS :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_AP_VENDOR", null)) ;
					break ;
				case XConstants.XC_AR_CUSTOMERS :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_AR_CUSTOMER", null)) ;
					break ;
				case XConstants.XC_GL_PRODUCTS :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_PRODUCTS", null)) ;
					break ;
				case XConstants.XIP_PUB_ORGS :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_PUB_ORGS", null)) ;
					break ;
				case XConstants.XIP_PUB_EMPS :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_PUB_EMPS", null)) ;
					break ;
				case XConstants.XIP_PUB_DETPS :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_PUB_DEPTS", null)) ;
					break ;
				case XConstants.XC_PM_PROJECTS :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_PM_PROJECTS", null)) ;
					break ;
				case XConstants.XC_GL_CUSTOM1 :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_CUSTOMER1", null)) ;
					break ;
				case XConstants.XC_GL_CUSTOM2 :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_CUSTOMER2", null)) ;
					break ;
				case XConstants.XC_GL_CUSTOM3 :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_CUSTOMER3", null)) ;
					break ;
				case XConstants.XC_GL_CUSTOM4 :
					message = message.concat(XipUtil.getMessage(lang, "XC_GL_CUSTOMER4", null)) ;
					break ;
				default :
					break ;
				}
				
				throw new Exception(message+XipUtil.getMessage(lang, "XC_GL_VAL_NAME", null)+hashKey+XipUtil.getMessage(lang, "XC_GL_WRONGFUL_IN_LEDGER", null)) ;
			}
			
			JSONObject jo = new JSONObject(String.valueOf(valJson)) ;
			segCode = jo.getString("code") ;
			
		} catch (Exception e) {
			throw e ;
		}
		return segCode ;
	}
	
	/**
	 * @Title: newCCID 
	 * @Description: 按科目层级生成CCID
	 * @param ledgerId	账簿ID
	 * @param hrcyId	科目体系ID
	 * @param accId		科目ID
	 * @param segments	科目辅助段
	 * @param isLeaf	是否末级
	 * @param jo		存储CCID返回值{"id":"","code":"","name":""}
	 * @param ccids		存储本次CCID信息
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public void newCCID(String ledgerId, String hrcyId, String accId, 
					HashMap<String,String> segments, String isLeaf, JSONObject jo,List<CCID> ccids) throws Exception {
		String lang = XCUtil.getLang();//语言值
		try {
			if(ledgerId == null || accId == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CREAT_CCID", null)) ;
			
			// 生成空CCID对象
			CCID ccid = this.generateEmptyCCID() ;
			
			// 设置科目信息
			String accKey =  XCRedisKeys.getAccKey(hrcyId) ;
			Object accJson = redisDaoImpl.hashGet(accKey, accId) ;
			
			// 科目信息
			JSONObject accJo = new JSONObject(String.valueOf(accJson)) ;
			
			// 判断是否为末级科目
			if("Y".equals(isLeaf) && !"Y".equals(accJo.getString("isLeaf"))){
				String code = accJo.getString("code") ;
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME ", null)+code+XipUtil.getMessage(lang, "XC_GL_NO_END_ACCOUNT ", null)) ;
			}
			
			ccid.setAccId(accId);
			ccid.setAccCode(accJo.getString("code"));
			ccid.setAccName(accJo.getString("name"));
			
			// 处理辅助核算段信息
			ccid = this.handlerSegments(ledgerId, ccid, segments) ;
			
			// 处理CCID编码
			StringBuffer fullCode = new StringBuffer() ;
			fullCode.append(ccid.getAccCode()).append("/")
				.append(ccid.getVendorCode()).append("/")
				.append(ccid.getCustomerCode()).append("/")
				.append(ccid.getProductCode()).append("/")
				.append(ccid.getOrgCode()).append("/")
				.append(ccid.getEmpCode()).append("/")
				.append(ccid.getDeptCode()).append("/")
				.append(ccid.getProjectCode()).append("/")
				.append(ccid.getCustom1Code()).append("/")
				.append(ccid.getCustom2Code()).append("/")
				.append(ccid.getCustom3Code()).append("/")
				.append(ccid.getCustom4Code()) ;
			ccid.setCcidCode(fullCode.toString());
			
			// 设置ccid
			ccid.setCcid(java.util.UUID.randomUUID().toString());
			
			// 设置账簿ID
			ccid.setLedgerId(ledgerId);
			
			// 处理CCID名称
			StringBuffer fullName = new StringBuffer() ;
			fullName.append(ccid.getAccCode()).append(ccid.getAccName());
			if(!"00".equals(ccid.getVendorCode())){
				fullName.append("/").append(ccid.getVendorCode()).append(ccid.getVendorName());
			}
			if(!"00".equals(ccid.getCustomerCode())){
				fullName.append("/").append(ccid.getCustomerCode()).append(ccid.getCustomerName());
			}
			if(!"00".equals(ccid.getProductCode())){
				fullName.append("/").append(ccid.getProductCode()).append(ccid.getProductName());
			}
			if(!"00".equals(ccid.getOrgCode())){
				fullName.append("/").append(ccid.getOrgCode()).append(ccid.getOrgName());
			}
			if(!"00".equals(ccid.getEmpCode())){
				fullName.append("/").append(ccid.getEmpCode()).append(ccid.getEmpName());
			}
			if(!"00".equals(ccid.getDeptCode())){
				fullName.append("/").append(ccid.getDeptCode()).append(ccid.getDeptName());
			}
			if(!"00".equals(ccid.getProjectCode())){
				fullName.append("/").append(ccid.getProjectCode()).append(ccid.getProjectName());
			}
			if(!"00".equals(ccid.getCustom1Code())){
				fullName.append("/").append(ccid.getCustom1Code()).append(ccid.getCustom1Name());
			}
			if(!"00".equals(ccid.getCustom2Code())){
				fullName.append("/").append(ccid.getCustom2Code()).append(ccid.getCustom2Name());
			}
			if(!"00".equals(ccid.getCustom3Code())){
				fullName.append("/").append(ccid.getCustom3Code()).append(ccid.getCustom3Name());
			}
			if(!"00".equals(ccid.getCustom4Code())){
				fullName.append("/").append(ccid.getCustom4Code()).append(ccid.getCustom4Name());
			}
			
			String ccidName = fullName.toString() ;
			ccid.setCcidName(ccidName);
			
			// 处理时间信息
			ccid.setCreatedBy(CurrentSessionVar.getUserId());
			ccid.setCreationDate(CurrentUserUtil.getCurrentDate());
			
			// 设置末级
			ccid.setIsLeaf(isLeaf);
			
			// 保存科目组合信息
			voucherMapper.createCCID(ccid);
			
			ccids.add(ccid) ;
			
			// 返回末级节点的CCID信息
			if("Y".equals(isLeaf)){ 
				jo.put("id", ccid.getCcid()) ;
				jo.put("code", ccid.getCcidCode()) ;
				jo.put("name", ccid.getCcidName()) ;
			}
			
			// 生成科目父级科目的CCID信息
			String upAccId = accJo.getString("upId") ; // 父级科目ID
			if(!"-1".equals(upAccId)){ // 如果存在父级科目
				// 父级科目信息
				JSONObject parentAccJo = new JSONObject(String.valueOf(redisDaoImpl.hashGet(accKey, upAccId))) ;
				
				// 父级科目组合全编码
				StringBuffer parentFullCode = new StringBuffer() ;
				parentFullCode.append(parentAccJo.getString("code")).append("/")
					.append(ccid.getVendorCode()).append("/")
					.append(ccid.getCustomerCode()).append("/")
					.append(ccid.getProductCode()).append("/")
					.append(ccid.getOrgCode()).append("/")
					.append(ccid.getEmpCode()).append("/")
					.append(ccid.getDeptCode()).append("/")
					.append(ccid.getProjectCode()).append("/")
					.append(ccid.getCustom1Code()).append("/")
					.append(ccid.getCustom2Code()).append("/")
					.append(ccid.getCustom3Code()).append("/")
					.append(ccid.getCustom4Code());
				// ccidkey
				String ccidKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CCID, ledgerId)  ;
				
				// 取得父级科目CCID
				Object parentCCID = redisDaoImpl.hashGet(ccidKey, parentFullCode.toString()) ;
				
				// 如果父级未生成科目组合ID
				if(parentCCID == null){
					// 递归生成父级CCID
					this.newCCID(ledgerId, hrcyId, upAccId, segments, "N", jo, ccids);
				}
			}			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}
	
	/**
	 * @Title: generateEmptyCCID 
	 * @Description: 生成空CCID对象
	 * @return
	 * @throws Exception
	 */
	private CCID generateEmptyCCID() throws Exception {
		CCID ccid = new CCID();
		
		ccid.setVendorId("00");
		ccid.setVendorCode("00");
		ccid.setVendorName("00");
		
		ccid.setCustomerId("00");
		ccid.setCustomerCode("00");
		ccid.setCustomerName("00");
		
		ccid.setProductId("00");
		ccid.setProductCode("00");
		ccid.setProductName("00");
		
		ccid.setOrgId("00");
		ccid.setOrgCode("00");
		ccid.setOrgName("00");
		
		ccid.setEmpId("00");
		ccid.setEmpCode("00");
		ccid.setEmpName("00");
		
		ccid.setDeptId("00");
		ccid.setDeptCode("00");
		ccid.setDeptName("00");
	
		ccid.setProjectId("00");
		ccid.setProjectCode("00");
		ccid.setProjectName("00");
		
		ccid.setCustom1Id("00");
		ccid.setCustom1Code("00");
		ccid.setCustom1Name("00");
		
		ccid.setCustom2Id("00");
		ccid.setCustom2Code("00");
		ccid.setCustom2Name("00");
		
		ccid.setCustom3Id("00");
		ccid.setCustom3Code("00");
		ccid.setCustom3Name("00");
		
		ccid.setCustom4Id("00");
		ccid.setCustom4Code("00");
		ccid.setCustom4Name("00");
		
		return ccid ;
	}
	
	/**
	 * @Title: handlerSegments 
	 * @Description: 处理辅助核算段信息
	 * @param ledgerId
	 * @param ccid
	 * @param segments
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private CCID handlerSegments(String ledgerId, CCID ccid, HashMap<String,String> segments) throws Exception {
		String lang = XCUtil.getLang();//语言值
		if(segments != null && segments.size()>0){
			Set set = segments.keySet() ;
			Iterator it = set.iterator();
			while(it.hasNext()){
				String segCode = (String) it.next() ; 		// 核算段编码
				String segValId = segments.get(segCode) ; 	// 核算段值ID
				
				// 排除空值的辅助段
				if(segValId == null || "".equals(segValId) || "00".equals(segValId)) continue ;
				
				if(segCode.equals(XConstants.XC_AP_VENDORS)){ // 供应商
					String segKey = XCRedisKeys.getGlobalSegKey(segCode) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncVendors2Redis(segCode) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_VENDOR_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setVendorId(segValId);
					ccid.setVendorCode(segJo.getString("code"));
					ccid.setVendorName(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XC_AR_CUSTOMERS)){ // 客户
					String segKey = XCRedisKeys.getGlobalSegKey(segCode) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncCustomers2Redis(segCode) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CUSTOMER_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setCustomerId(segValId);
					ccid.setCustomerCode(segJo.getString("code"));
					ccid.setCustomerName(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XC_GL_PRODUCTS)){ // 产品
					String segKey = XCRedisKeys.getGlobalSegKey(segCode) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncProducts2Redis(segCode) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_PRODUCT_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setProductId(segValId);
					ccid.setProductCode(segJo.getString("code"));
					ccid.setProductName(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XIP_PUB_ORGS)){ // 内部往来
					String segKey = XCRedisKeys.getGlobalSegKey(segCode) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncOrgs2Redis(segCode);
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_INTET_CONNECT_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setOrgId(segValId);
					ccid.setOrgCode(segJo.getString("code"));
					ccid.setOrgName(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XIP_PUB_EMPS)){ // 个人往来
					String segKey = XCRedisKeys.getGlobalSegKey(segCode) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncEmps2Redis(segCode) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_PERSONAL_CONNECT_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setEmpId(segValId);
					ccid.setEmpCode(segJo.getString("code"));
					ccid.setEmpName(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XIP_PUB_DETPS)){ // 成本中心
					String segKey = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncDepts2Redis(ledgerId, segCode, XCGLConstants.REDIS_OP_ADD) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_DEPT_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setDeptId(segValId);
					ccid.setDeptCode(segJo.getString("code"));
					ccid.setDeptName(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XC_PM_PROJECTS)){ // 项目
					String segKey = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncProjects2Redis(ledgerId, segCode, XCGLConstants.REDIS_OP_ADD) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_PROJECT_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setProjectId(segValId);
					ccid.setProjectCode(segJo.getString("code"));
					ccid.setProjectName(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XC_GL_CUSTOM1)){ // 自定义维度1
					String segKey = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncCustoms2Redis(ledgerId, segCode, XCGLConstants.REDIS_OP_ADD) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION1_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setCustom1Id(segValId);
					ccid.setCustom1Code(segJo.getString("code"));
					ccid.setCustom1Name(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XC_GL_CUSTOM2)){ // 自定义维度2
					String segKey = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncCustoms2Redis(ledgerId, segCode, XCGLConstants.REDIS_OP_ADD) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION2_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setCustom2Id(segValId);
					ccid.setCustom2Code(segJo.getString("code"));
					ccid.setCustom2Name(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XC_GL_CUSTOM3)){ // 自定义维度3
					String segKey = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncCustoms2Redis(ledgerId, segCode, XCGLConstants.REDIS_OP_ADD) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION3_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setCustom3Id(segValId);
					ccid.setCustom3Code(segJo.getString("code"));
					ccid.setCustom3Name(segJo.getString("name"));
					
				}else if(segCode.equals(XConstants.XC_GL_CUSTOM4)){ // 自定义维度4
					String segKey = XCRedisKeys.getLedgerSegKey(segCode, ledgerId) ;
					Object valJo = redisDaoImpl.hashGet(segKey, segValId) ;
					
					// 刷新缓存
					if(valJo == null){ 
						xcglCommonService.syncCustoms2Redis(ledgerId, segCode, XCGLConstants.REDIS_OP_ADD) ;
						valJo = redisDaoImpl.hashGet(segKey, segValId) ;
						if(valJo == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION4_NO_EXIST", null)+segValId) ;
					}
					
					JSONObject segJo = new JSONObject(String.valueOf(valJo)) ;
					ccid.setCustom4Id(segValId);
					ccid.setCustom4Code(segJo.getString("code"));
					ccid.setCustom4Name(segJo.getString("name"));
					
				}else{
					
				}
			}
		}
		
		return ccid ;
	}
	
	/**
	 * @Title: jsonArray2List 
	 * @Description: 将包含凭证头ID信息的JSON数组转换为List对象
	 * @param ja
	 * @return
	 * @throws Exception    设定文件
	 */
	public List<String> jsonArray2List(String jsonArray)throws Exception {
		List<String> list = new ArrayList<String>() ;
		String lang = XCUtil.getLang();//语言值
		try {
			// 将JSON字符串转换成JSON数组
			JSONArray ja = null ;
			try {
				ja = new JSONArray(jsonArray);
				
			} catch (Exception e) {
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_JSONARRAY_NO_ERROR", null)+"[{\"V_HEAD_ID\":1},{\"V_HEAD_ID\":2}…]") ;
			}
			
			// 执行转换
			if(ja != null && ja.length() > 0){
				for(int i=0; i<ja.length(); i++){
					String jsonStr = ja.getString(i) ;
					JSONObject jo =  new JSONObject(jsonStr) ;
					// 凭证头ID
					String headId = jo.getString("V_HEAD_ID") ;
					
					list.add(headId) ;
				}
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return list ;
	} 

	/*
	 * (非 Javadoc) 
	 * <p>Title: jsonStr2headBean</p> 
	 * <p>Description: 将单张凭证信息转换成JavaBean </p> 
	 * @param paramJson
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.AbstractVoucherHandler#json2Bean(java.lang.String)
	 */
	public VHead jsonStr2headBean(String paramJson) throws Exception {
		VHead bean = new VHead() ;
		String lang = XCUtil.getLang();//语言值
		// 参数转换
		JSONObject jo = null ;
		try {
			jo = new JSONObject(paramJson) ;
		} catch (Exception e) {
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_JSON_WRONGFUL", null)) ;
		}
		
		// 将JSON字符串转换成JavaBean
		if(jo != null){
			// 组合凭证头信息
			VHeadDTO headDto = XCJSONUtil.jsonToBean(paramJson, VHeadDTO.class) ;
			BeanUtils.copyProperties(headDto, bean);
			
			// 组合凭证分录行信息
			String lineJson = jo.getString("lines") ;
			JSONArray ja = new JSONArray(lineJson) ;
			if(ja != null && ja.length() >0){
				List<VLine> vLines = new ArrayList<VLine>() ;
				for(int i=0; i<ja.length(); i++){
					String lineJsonStr = ja.getString(i);
					
					VLineDTO lineDto = XCJSONUtil.jsonToBean(lineJsonStr, VLineDTO.class) ;
					
					VLine line = new VLine() ;
					BeanUtils.copyProperties(lineDto, line);
					
					vLines.add(line) ;
				}
				bean.setLines(vLines);
			}
		}
		
		return bean ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: bean2Json</p> 
	 * <p>Description: 将单张凭证信息（JavaBean）转换成JSON串  </p> 
	 * @param bean
	 * @param cfg
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.AbstractVoucherHandler#bean2Json(com.xzsoft.xc.gl.modal.VHead, net.sf.json.JsonConfig)
	 */
	public String headBean2JsonStr(VHead bean,JsonConfig cfg) throws Exception {
		String jsonStr = null ;
		try {
			if(bean != null) {
				if(cfg != null){ 
					// 将JavaBean转换成JSON串
					jsonStr = XCJSONUtil.beanToJson(bean, cfg) ;
					
				}else{
					// 默认配置项目
					JsonConfig jcfg = new JsonConfig() ;
					
					// 定义排除项
					String[] excludes = new String[]{"accList","accIdList","addLines","updLines","delLines"} ;
					jcfg.setExcludes(excludes);   
					jcfg.setIgnoreDefaultExcludes(false); 
					
					// 日期格式
					jcfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor("yyyy-MM-dd"));
					
					// 将JavaBean转换成JSON串
					jsonStr = XCJSONUtil.beanToJson(bean, jcfg) ;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return jsonStr ;
	}
	
	/**
	 * @Title: sumGLBalances 
	 * @Description: 执行余额汇总处理
	 * @param list   			// 凭证头ID列表
	 * @param isAccount  		// 当前操作是否为过账操作 ;  取值说明:Y-过账汇总,N-提交、撤回提交和驳回汇总
	 * @param isSumCash			// 是否统计现金流量项 ; 取值说明:ALL-汇总科目余额和统计现金流量项, CA-仅统计现金流量项, BA-仅汇总科目余额
	 * @param userId			// 当前用户ID
	 * @param isCanceled		// 凭证汇总标志; 取值说明:Y-已汇总,N-未汇总
	 * @throws Exception    设定文件
	 */
	public void sumGLBalances(List<String> list, String isAccount, String isSumCash, String userId, String isCanceled) throws Exception {
		try {
			if(list != null && list.size()>0){
				JSONArray ja = new JSONArray() ;
				for(int i=0; i<list.size(); i++){
					String headId = list.get(i);
					
					JSONObject jo = new JSONObject() ;
					jo.put("V_HEAD_ID", headId) ;
					
					ja.put(jo) ;
				}
				
				// 执行汇总处理
				xcglSumBalanceService.sumGLBalances(ja, isAccount, isSumCash, userId, isCanceled) ;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: checkParams</p> 
	 * <p>Description: 验证参数的合法性</p> 
	 * @param jsonArray
	 * @param checkType
	 * <p> 
	 * 	checkType分类说明:
	 * 		save    - 凭证保存
	 *  	update  - 凭证修改
	 *  	writeOff- 凭证冲销
	 * 		submit  - 凭证提交
	 *  	cancel  - 撤回提交
	 * 		delete  - 凭证删除
	 *  	reject  - 凭证驳回
	 *  	check   - 凭证审核
	 *  	uncheck - 取消审核
	 *  	sign    - 出纳签字
	 *  	unsign  - 取消签字
	 *  	account - 凭证过账
	 * </p>
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.AbstractVoucherHandler#checkParams(java.util.List, java.lang.String)
	 */
	public HashMap<String,Object> checkParams(String jsonArray, String checkType) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		
		try {
			if("save".equals(checkType)){
				// 新增凭证
				jo = this.chkSaveOrUpdateV(jsonArray,checkType) ;
				
			}else if ("update".equals(checkType)){	
				// 更新凭证
				jo = this.chkSaveOrUpdateV(jsonArray,checkType) ;
				
			}else if ("write_off".equals(checkType)){	
				// 冲销凭证
				jo = this.chkSaveOrUpdateV(jsonArray,checkType) ;
				
			}else if ("submit".equals(checkType)){
				// 提交凭证
				jo = this.chkNotSubmitV(jsonArray) ;
				
			}else if ("cancel".equals(checkType)){
				// 撤回提交
				jo = this.chkSubmitedAndNotVerifiedV(jsonArray) ;
				
			}else if("delete".equals(checkType)){ 
				// 删除凭证
				jo = this.chkNotSubmitV(jsonArray) ;
				
			}else if ("reject".equals(checkType)){
				// 驳回凭证
				jo = this.chkSubmitedAndNotVerifiedV(jsonArray) ;
			
			}else if ("check".equals(checkType)){
				// 审核凭证
				jo = this.checkV(jsonArray) ;
				
			}else if ("uncheck".equals(checkType)){
				// 取消审核
				jo = this.uncheckV(jsonArray) ;
				
			}else if ("sign".equals(checkType)){
				// 出纳签字
				jo = this.signV(jsonArray); 
				
			}else if ("unsign".equals(checkType)){
				// 取消签字
				jo = this.unsignV(jsonArray) ;
				
			}else if ("account".equals(checkType)){
				// 凭证过账
				jo = this.accountV(jsonArray) ;
				
			}else if ("unaccount".equals(checkType)){
				// 取消过账
				jo = this.unaccountV(jsonArray) ;
				
			}else{
				// 扩展使用
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: chkSaveOrUpdateV 
	 * @Description: 凭证保存、修改和冲销校验
	 * @param jsonArray
	 * @param chktype
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> chkSaveOrUpdateV(String jsonArray,String chktype) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		
		try {
			// 凭证信息(包括凭证头和凭证分录行信息)
			JSONArray ja = new JSONArray(jsonArray) ;
			String jsonStr = ja.getString(0) ;
			JSONObject vJson = new JSONObject(jsonStr) ;
			
			// 将凭证信息转换为JavaBean
			VHead head = this.jsonStr2headBean(vJson.toString()) ;
			
			// 执行凭证校验
			head = this.chkVoucher(head, chktype) ;
			
			// 返回信息
			jo.put("head", head) ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: chkVoucher 
	 * @Description: 校验凭证信息
	 * @param head
	 * @param chktype
	 * @return
	 * @throws Exception    设定文件
	 */
	private VHead chkVoucher(VHead head,String chktype) throws Exception {
		String lang = XCUtil.getLang();//语言值
		try {
			// 新增或冲销凭证时,不需要传入凭证ID
			if("save".equals(chktype) || "write_off".equals(chktype)){
				if(head.getHeadId() != null && !"".equals(head.getHeadId())){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_ADD_VOUCHER_ERROR", null)) ;
				}
			}
			
			// 账簿不能为空
			String ledgerCode = head.getLedgerCode() ;
			if(ledgerCode == null || "".equals(ledgerCode)){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_LEDGER_NO_NULL", null)) ;
				
			}else{
				String ldKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_LEDGERS) ;
				Object ldJson = redisDaoImpl.hashGet(ldKey, ledgerCode) ;
				
				// 刷新缓存
				if(ldJson == null){ 
					xcglCommonService.syncLedgers(ledgerCode, "ByCode") ;
					ldJson = redisDaoImpl.hashGet(ldKey, ledgerCode) ;
					if(ldJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_LEDGER", null)+"【"+ledgerCode+"】"+XipUtil.getMessage(lang, "XC_GL_NO_EXIST", null)) ;
				}
				
				// 从缓存中查询
				JSONObject ldJo = new JSONObject(String.valueOf(ldJson)) ;
				head.setLedgerId(ldJo.getString("id"));
				head.setHrcyId(ldJo.getString("hrcyId"));
			}
			
			// 会计日期对应的账簿会计期不能为空且为开启状态
			String periodCode = head.getPeriodCode() ;
			if(periodCode != null && !"".equals(periodCode)){
				// 判断会计期是否存在并且开启 : 1-打开、2-关闭、3-永久关闭
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("ledgerId", head.getLedgerId()) ;
				map.put("periodCode", periodCode) ;
				Period period = xcglCommonMapper.getPeriod(map) ;
				if(period == null){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_NO_OPEN", null)+"【"+periodCode+"】"+XipUtil.getMessage(lang, "XC_GL_PERIOD_NAME", null)) ;
				}else{
					if(!"1".equals(period.getPeriodStatus())){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_LEDGER_IS", null)+"【"+periodCode+"】"+XipUtil.getMessage(lang, "XC_GL_PERIOD_CLOSE", null)) ;
					}
				}
			}else{
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_PERIOD", null)) ;
			}			
			
			// 会计日期不能为空
			Date accountDate = head.getCreationDate() ;
			if(accountDate == null || "".equals(accountDate)){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_PERIOD_DATE", null)) ;
			}else{
				// 判断会计日期是否在会计期的日期范围内
				HashMap<String,Object> pmap = new HashMap<String,Object>() ;
				pmap.put("dbType", PlatformUtil.getDbType()) ;
				pmap.put("pcode", head.getPeriodCode()) ;
				pmap.put("accDate", head.getCreationDate()) ;
				
				Period period = xcglCommonMapper.getPeriodByAccDate(pmap) ;
				if(period == null){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_PERIOD_STRING", null)+XCDateUtil.date2Str(accountDate)+XipUtil.getMessage(lang, "XC_GL_PERIOD_P", null)+periodCode) ;
				}
			}
			
			// 制单人不能为空
			String createdBy = head.getCreatedBy() ;
			if(createdBy == null || "".equals(createdBy)){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_CREATBY_NO_NULL", null)) ;
			}
			
			// 凭证分类不能为空
			String catCode = head.getCategoryCode() ;
			if(catCode == null || "".equals(catCode)){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_CLASS_NO_NULL", null)) ;
			}else{
				String catKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_V_CATEGORY) ;
				Object catJson = redisDaoImpl.hashGet(catKey, catCode) ;
				
				// 刷新缓存
				if(catJson == null){
					xcglCommonService.syncVCategory() ;
					catJson = redisDaoImpl.hashGet(catKey, catCode) ;
					if(catJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_CLASS", null)+"【"+catCode+"】"+XipUtil.getMessage(lang, "XC_GL_NO_EXIST", null)) ;
				}
				
				JSONObject ldJo = new JSONObject(String.valueOf(catJson)) ;
				head.setCategoryId(ldJo.getString("id"));
			}
			
			// 凭证模板类型不能为空
			String templateType = head.getTemplateType() ;
			if(templateType == null || "".equals(templateType)){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_MODAL_NO_NULL", null)) ;
			}
			
			// 一个凭证不能只存在一条凭证分录信息
			List<VLine> lines = head.getLines() ;
			if(lines == null || lines.size()<2){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_LOWER_TWO", null)) ;
			}
			
			// 如果操作为更新凭证, 判断凭证是否在系统中存在且未提交
			if("update".equals(chktype)){
				String headId = head.getHeadId() ;
				if(headId == null || "".equals(headId)){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_UPDATE_VOUCHER_NO_NULL", null)) ;
				}else{
					VHead updHead = voucherMapper.getVHead(headId) ;
					if(updHead == null){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_NAME", null)+"【"+headId+"】"+XipUtil.getMessage(lang, "XC_GL_SYSTEM_NO_EXIST", null)) ;
					}else{
						String updHeadStatu = updHead.getVStatus() ;
						if("2".equals(updHeadStatu)){
							throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_NO_UPDATE", null)) ;
						}
					}
				}
			}
			
			// 如果操作为冲销凭证, 判断被冲销的凭证是否存在、且已过账并未被冲销
			if("write_off".equals(chktype)){
				String writeOffNum = head.getWriteOffNum() ;
				if(writeOffNum == null || "".equals(writeOffNum)){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_WRITE_OFF_NO_NULL", null)) ;
				}else{
					VHead writeOffHead = voucherMapper.getVHead(writeOffNum) ;
					if(writeOffHead == null){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_WRITE_OFF_VOUCHER", null)+"【"+writeOffNum+"】"+XipUtil.getMessage(lang, "XC_GL_SYSTEM_NO_EXIST ", null)) ;
					}else{
						// 记账人
						String bookkeeperId = writeOffHead.getBookkeeperId() ;
						// 是否冲销标志
						String isWriteOff = writeOffHead.getIsWriteOff() ;
						if(bookkeeperId == null || "".equals(bookkeeperId)){ // 凭证未过账
							throw new Exception(XipUtil.getMessage(lang, "XC_GL_WRITE_OFF_VOUCHER", null)+"【"+writeOffNum+"】"+XipUtil.getMessage(lang, "XC_GL_NO_POSTING", null)) ;
						}else{ // 凭证已过账
							if("Y".equals(isWriteOff)){
								throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_NAME", null)+"【"+writeOffNum+"】"+XipUtil.getMessage(lang, "XC_GL_ARLEADY_WRITE_OFF", null)) ;
							}
						}
					}
				}
			}
			
			// 处理CCID信息
			this.chkCCID(head);
			
			// 校验凭证模板类型是否合法
			List<String> accIdList = head.getAccIdList() ;
			List<Account> accounts = xcglCommonDAO.getLdAccProperties(head.getLedgerId(), accIdList) ;
			if(accounts != null && accounts.size()>0){
				String isAmount = "N";
				String isForeignCny = "N" ;
				for(Account acc : accounts){
					// 是否为属性核算
					if("Y".equals(acc.getIsAmount())){
						isAmount = "Y" ;
					}
					// 是否为外币计量
					if("Y".equals(isForeignCny)){
						isForeignCny = "Y" ;
					}
				}
				if("Y".equals(isForeignCny) && "Y".equals(isAmount)){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_ERROR", null)) ;
				}else{
					// 金额式
					if("N".equals(isForeignCny) && "N".equals(isAmount)){ 
						head.setTemplateType("1");
					}
					// 数量金额式
					if("N".equals(isForeignCny) && "Y".equals(isAmount)){
						head.setTemplateType("2"); 
					}
					// 外币金额式
					if("Y".equals(isForeignCny) && "N".equals(isAmount)){
						head.setTemplateType("3"); 
					}
				}
			}
			
			// 处理凭证分录行信息 (判断是否需要出纳签字、分录行ID、科目计量单位和币种)
			this.chkEntries(head, chktype);
			
			// 判断凭证借贷是否平衡
			this.chkBalances(head) ;
			
			// 处理凭证头ID和制单人
			if("save".equals(chktype) || "write_off".equals(chktype)){
				// 凭证头ID
				head.setHeadId(UUID.randomUUID().toString());
				// 制单人
				Employee emp = xcglCommonDAO.getEmployeeByUserId(createdBy) ;
				head.setCreatedName(emp.getEmpName());
			}
			// 最后修改日期信息
			head.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
			
			// 凭证是否汇总标志:Y-已汇总,N-未汇总
			head.setSumFlag("N");
			
			// 凭证状态：1-未提交,2-已提交
			head.setVStatus("1");
			
		} catch (Exception e) {
			throw e ;
		}
		
		return head ;
	}
	
	/**
	 * @Title: chkCCID 
	 * @Description: 处理凭证分录的CCID信息
	 * @param head
	 * @param chktype
	 * @throws Exception    设定文件
	 */
	private void chkCCID(VHead head) throws Exception {
		String lang = XCUtil.getLang();//语言值
		// 凭证分录行信息
		List<VLine> lines = head.getLines() ;
		if(lines != null && lines.size()>0){
			// 科目ID列表
			List<String> accIdList = new ArrayList<String>() ;
			// 分录行序号
			List<Integer> lineNum = new ArrayList<Integer>() ;
			
			// 循环凭证分录并处理CCID
			for(VLine line :lines){
				// 操作人,操作日期
				line.setCreatedBy(head.getCreatedBy());
				line.setCreationDate(head.getLastUpdateDate());
				line.setLastUpdateDate(head.getLastUpdateDate());
				
				// 分录行行号
				int orderby = line.getOrderBy() ;
				if(orderby == 0){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_ENTRY_NUMBER_NO_NULL", null)) ;
				}
				if(lineNum.contains(orderby)){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_ENTRY_NUMBER",null)+"【"+orderby+"】"+XipUtil.getMessage(lang, "XC_GL_ALREADY_REPEAT", null)) ;
				}
				lineNum.add(orderby) ;
				
				// 1.验证科目组合信息的合法性
				String ccid = line.getCcid() ;
				if(ccid != null && !"".equals(ccid)){
					// 如果CCID存在则取CCID为凭证分录的科目组合ID
					String ccidKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CCID, head.getLedgerId()) ;
					Object ccidJson = redisDaoImpl.hashGet(ccidKey, ccid) ;
					
					// 刷新缓存
					if(ccidJson == null){
						xcglCommonService.syncCCID2Redis(head.getLedgerId()) ;
						ccidJson = redisDaoImpl.hashGet(ccidKey, ccid) ;
						if(ccidJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_ENTRY_CCID", null)+"【"+ccid+"】"+XipUtil.getMessage(lang, "XC_GL_SYSTEM_NO_EXIST", null)) ;
					}
					
					JSONObject jo = new JSONObject(String.valueOf(ccidJson)) ;
					// 科目组合编码
					String ccidCode = jo.getString("code") ;
					// 科目编码
					String accCode = ccidCode.substring(0, ccidCode.indexOf("/")) ;
					// 科目串信息
					String accKey =  XCRedisKeys.getAccKey(head.getHrcyId()) ;
					Object accJson = redisDaoImpl.hashGet(accKey, accCode) ;
					
					// 刷新科目缓存
					if(accJson == null){
						xcglCommonService.syncAccHrcy2Redis(head.getHrcyId(), XCGLConstants.REDIS_OP_ADD) ;
						accJson = redisDaoImpl.hashGet(accKey, accCode) ;
						if(accJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME ", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_NO_EXIST ", null)) ;
					}
					
					// 判断科目是否为末级科目
					JSONObject accJo = new JSONObject(String.valueOf(accJson)) ;
					if(!"Y".equals(accJo.getString("isLeaf"))){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_CCID_NAME", null)+"【"+ccid+"】"+XipUtil.getMessage(lang, "XC_GL_NO_FINAL", null)) ;
						
					}else{
						// 记录末级科目ID
						String accId = String.valueOf(accJo.getString("id")) ;
						if(!accIdList.contains(accId)){
							accIdList.add(accId) ;
						}
						line.setAccId(accId);
					}
					
				}else{
					
					//  如果不存在科目组合则根据传入的科目编码和科目辅助段生成科目组合信息
					String accCode = line.getAccCode(); 			// 科目编码
					if(accCode == null || "".equals(accCode) || "00".equals(accCode)){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_ENTRY_ACCOUNT_CODE_NO_NULL", null)) ;
						
					}else{
						// 判断科目是否存在
						String accKey =  XCRedisKeys.getAccKey(head.getHrcyId()) ;
						Object accJson = redisDaoImpl.hashGet(accKey, accCode) ;
						
						// 刷新科目缓存
						if(accJson == null || "".equals(accJson)){
							xcglCommonService.syncAccHrcy2Redis(head.getHrcyId(), XCGLConstants.REDIS_OP_ADD) ;
							accJson = redisDaoImpl.hashGet(accKey, accCode) ;
							if(accJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_NO_EXIST", null)) ;
						}
						
						// 科目是否为末级科目
						JSONObject accJo = new JSONObject(String.valueOf(accJson)) ;
						String isLeaf = accJo.getString("isLeaf") ;
						if(!"Y".equals(isLeaf)){
							throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_NO_FINAL ", null)) ;
							
						}else{
							// 记录末级科目ID
							String accId = String.valueOf(accJo.getString("id")) ;
							if(!accIdList.contains(accId)){
								accIdList.add(accId) ;
							}
							line.setAccId(accId);
							
							// 查询科目启用的辅助核算段信息
							List<String> accSegList = xcglCommonDAO.getAccSeg(head.getLedgerId(), accId) ;
							
							// 处理科目辅助段
							HashMap<String, String> segments = new HashMap<String, String>() ;
							
							// 供应商编码
							String vendorCode ="00" ;
							if(accSegList.contains(XConstants.XC_AP_VENDORS)){
								vendorCode = line.getVendorCode() ;	
								// 如果科目启用了供应商段
								if(vendorCode != null && !"".equals(vendorCode) && !"00".equals(vendorCode)){
									String vendorKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_AP_VENDORS) ;
									Object vendorJson = redisDaoImpl.hashGet(vendorKey, vendorCode) ;
									
									// 刷新缓存
									if(vendorJson == null || "".equals(vendorJson)){
										xcglCommonService.syncVendors2Redis(XConstants.XC_AP_VENDORS) ;
										vendorJson = redisDaoImpl.hashGet(vendorKey, vendorCode) ;
										if(vendorJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+vendorCode+"】"+XipUtil.getMessage(lang, "XC_GL_VENDOR_VALUE_NO_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(vendorJson)) ;
									segments.put(XConstants.XC_AP_VENDORS, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_ENABLE_VENDOR", null)) ;
								}
							}

							
							// 客户编码
							String customerCode ="00" ;
							if(accSegList.contains(XConstants.XC_AR_CUSTOMERS)){
								customerCode = line.getCustomerCode() ;	
								if(customerCode != null && !"".equals(customerCode) && !"00".equals(customerCode)){
									String customerKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_AR_CUSTOMERS) ;
									Object customerJson = redisDaoImpl.hashGet(customerKey, customerCode) ;
									
									// 刷新缓存
									if(customerJson == null || "".equals(customerJson)){
										xcglCommonService.syncCustomers2Redis(XConstants.XC_AR_CUSTOMERS) ;
										customerJson = redisDaoImpl.hashGet(customerKey, customerCode) ;
										if(customerJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+customerCode+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_VALUE", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(customerJson)) ;
									segments.put(XConstants.XC_AR_CUSTOMERS, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_VALUE_NO_NULL", null)) ;
								}
							}
		
							// 产品编码
							String prodCode = "00" ;
							if(accSegList.contains(XConstants.XC_GL_PRODUCTS)){
								prodCode = line.getProdCode() ;	
								if(prodCode != null && !"".equals(prodCode) && !"00".equals(prodCode)){
									String prodKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_PRODUCTS) ;
									Object prodJson = redisDaoImpl.hashGet(prodKey, prodCode) ;
									
									// 刷新缓存
									if(prodJson == null || "".equals(prodJson)){
										xcglCommonService.syncProducts2Redis(XConstants.XC_GL_PRODUCTS) ;
										prodJson = redisDaoImpl.hashGet(prodKey, prodCode) ;
										if(prodJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+prodCode+"】"+XipUtil.getMessage(lang, "XC_GL_PRODUCT_VALUE_NO_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(prodJson)) ;
									segments.put(XConstants.XC_GL_PRODUCTS, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_PRODUCT_VALUE_NO_NULL", null)) ;
								}
							}
	
							// 内部往来编码
							String orgCode ="00" ;
							if(accSegList.contains(XConstants.XIP_PUB_ORGS)){
								orgCode = line.getOrgCode() ; 
								if(orgCode != null && !"".equals(orgCode) && !"00".equals(orgCode)){
									String orgKey = XCRedisKeys.getGlobalSegKey(XConstants.XIP_PUB_ORGS) ;
									Object orgJson = redisDaoImpl.hashGet(orgKey, orgCode) ;
									
									// 刷新缓存
									if(orgJson == null || "".equals(orgJson)){
										xcglCommonService.syncOrgs2Redis(XConstants.XIP_PUB_ORGS);
										orgJson = redisDaoImpl.hashGet(orgKey, orgCode) ;
										if(orgJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+orgCode+"】"+XipUtil.getMessage(lang, "XC_GL_INTERNAL_CON_VALUE_NO_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(orgJson)) ;
									segments.put(XConstants.XIP_PUB_ORGS, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_INTERANL_CON_VALUE_NO_NULL", null)) ;
								}
							}
		
							// 个人往来编码	
							String empCode ="00" ;	
							if(accSegList.contains(XConstants.XIP_PUB_EMPS)){
								 empCode = line.getEmpCode() ;	
								if(empCode != null && !"".equals(empCode) && !"00".equals(empCode)){
									String empKey = XCRedisKeys.getGlobalSegKey(XConstants.XIP_PUB_EMPS) ;
									Object empJson = redisDaoImpl.hashGet(empKey, empCode) ;
									
									// 刷新缓存
									if(empJson == null || "".equals(empJson)){
										xcglCommonService.syncEmps2Redis(XConstants.XIP_PUB_EMPS) ;
										empJson = redisDaoImpl.hashGet(empKey, empCode) ;
										if(empJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS ", null)+"【"+empCode+"】"+XipUtil.getMessage(lang, "XC_GL_PERSONAL_VALUE_NO_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(empJson)) ;
									segments.put(XConstants.XIP_PUB_EMPS, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME",null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_PERSONAL_VALUE_NO_NULL", null)) ;
								}
							}
		
							// 成本中心编码
							String deptCode ="00" ;
							if(accSegList.contains(XConstants.XIP_PUB_DETPS)){
								deptCode = line.getDeptCode() ;		
								if(deptCode != null && !"".equals(deptCode) && !"00".equals(deptCode)){
									String deptKey = XCRedisKeys.getLedgerSegKey(XConstants.XIP_PUB_DETPS, head.getLedgerId()) ;
									Object deptJson = redisDaoImpl.hashGet(deptKey, deptCode) ;
									
									// 刷新缓存
									if(deptJson == null || "".equals(deptJson)){
										xcglCommonService.syncDepts2Redis(head.getLedgerId(), XConstants.XIP_PUB_DETPS, XCGLConstants.REDIS_OP_ADD) ;
										deptJson = redisDaoImpl.hashGet(deptKey, deptCode) ;
										if(deptJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+deptCode+"】"+XipUtil.getMessage(lang, "XC_GL_DEPT_VALUE_NO_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(deptJson)) ;
									segments.put(XConstants.XIP_PUB_DETPS, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_DEPT_VALUE_NO_NULL", null)) ;
								}
							}
			
							// 项目编码
							String prjCode ="00" ;
							if(accSegList.contains(XConstants.XC_PM_PROJECTS)){
								prjCode = line.getPrjCode() ;		
								if(prjCode != null && !"".equals(prjCode)){
									String prjKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_PM_PROJECTS, head.getLedgerId()) ;
									Object prjJson = redisDaoImpl.hashGet(prjKey, prjCode) ;
									
									// 刷新缓存
									if(prjJson == null || "".equals(prjJson)){
										xcglCommonService.syncProjects2Redis(head.getLedgerId(), XConstants.XC_PM_PROJECTS, XCGLConstants.REDIS_OP_ADD);
										prjJson = redisDaoImpl.hashGet(prjKey, prjCode) ;
										if(prjJson == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+prjCode+"】"+XipUtil.getMessage(lang, "XC_GL_PROJECT_VALUE_NO_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(prjJson)) ;
									segments.put(XConstants.XC_PM_PROJECTS, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_PROJECT_VALUE_NO_NULL", null)) ;
								}
							}
			
							// 自定义1编码
							String cust1Code ="00" ;
							if(accSegList.contains(XConstants.XC_GL_CUSTOM1)){
								cust1Code = line.getCust1Code() ;	
								if(cust1Code != null && !"".equals(cust1Code) && !"00".equals(cust1Code)){
									String cust1Key = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CUSTOM1, head.getLedgerId()) ;
									Object cust1Json = redisDaoImpl.hashGet(cust1Key, cust1Code) ;
									
									// 刷新缓存
									if(cust1Json == null || "".equals(cust1Json)){
										xcglCommonService.syncCustoms2Redis(head.getLedgerId(), XConstants.XC_GL_CUSTOM1, XCGLConstants.REDIS_OP_ADD) ;
										cust1Json = redisDaoImpl.hashGet(cust1Key, cust1Code) ;
										if(cust1Json == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+cust1Code+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION1_NOT_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(cust1Json)) ;
									segments.put(XConstants.XC_GL_CUSTOM1, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION1_NOT_NULL", null)) ;
								}
							}
			
							// 自定义2编码
							String cust2Code ="00" ;
							if(accSegList.contains(XConstants.XC_GL_CUSTOM2)){
								cust2Code = line.getCust2Code()  ;		
								if(cust2Code != null && !"".equals(cust2Code) && !"00".equals(cust2Code)){
									String cust2Key = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CUSTOM2, head.getLedgerId()) ;
									Object cust2Json = redisDaoImpl.hashGet(cust2Key, cust2Code) ;
									
									// 刷新缓存
									if(cust2Json == null || "".equals(cust2Json)){
										xcglCommonService.syncCustoms2Redis(head.getLedgerId(), XConstants.XC_GL_CUSTOM2, XCGLConstants.REDIS_OP_ADD) ;
										cust2Json = redisDaoImpl.hashGet(cust2Key, cust2Code) ;
										if(cust2Json == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+cust2Code+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION2_NOT_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(cust2Json)) ;
									segments.put(XConstants.XC_GL_CUSTOM2, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION2_NOT_NULL", null)) ;
								}
							}
	
							// 自定义3编码
							String cust3Code ="00" ;	
							if(accSegList.contains(XConstants.XC_GL_CUSTOM3)){
								cust3Code = line.getCust3Code()  ;	
								if(cust3Code != null && !"".equals(cust3Code) && !"00".equals(cust3Code)){
									String cust3Key = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CUSTOM3, head.getLedgerId()) ;
									Object cust3Json = redisDaoImpl.hashGet(cust3Key, cust3Code) ;
									
									// 刷新缓存
									if(cust3Json == null || "".equals(cust3Json)){
										xcglCommonService.syncCustoms2Redis(head.getLedgerId(), XConstants.XC_GL_CUSTOM3, XCGLConstants.REDIS_OP_ADD) ;
										cust3Json = redisDaoImpl.hashGet(cust3Key, cust3Code) ;
										if(cust3Json == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+cust3Code+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION3_NOT_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(cust3Json)) ;
									segments.put(XConstants.XC_GL_CUSTOM3, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION3_NOT_NULL", null)) ;
								}
							}
	
							// 自定义4编码	
							String cust4Code ="00" ;
							if(accSegList.contains(XConstants.XC_GL_CUSTOM4)){
								cust4Code = line.getCust4Code()  ;		
								if(cust4Code != null && !"".equals(cust4Code) && !"00".equals(cust4Code)){
									String cust4Key = XCRedisKeys.getLedgerSegKey(XConstants.XC_GL_CUSTOM4, head.getLedgerId()) ;
									Object cust4Json = redisDaoImpl.hashGet(cust4Key, cust4Code) ;
									
									// 刷新Redis缓存
									if(cust4Json == null || "".equals(cust4Json)){
										xcglCommonService.syncCustoms2Redis(head.getLedgerId(), XConstants.XC_GL_CUSTOM4, XCGLConstants.REDIS_OP_ADD) ;
										cust4Json = redisDaoImpl.hashGet(cust4Key, cust4Code) ;
										if(cust4Json == null) throw new Exception(XipUtil.getMessage(lang, "XC_GL_CODE_IS", null)+"【"+cust4Code+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION4_NOT_EXIST", null)) ;
									}
									
									JSONObject obj = new JSONObject(String.valueOf(cust4Json)) ;
									segments.put(XConstants.XC_GL_CUSTOM4, obj.getString("id")) ;
									
								}else{
									throw new Exception(XipUtil.getMessage(lang, "XC_GL_ACCONT_NAME", null)+"【"+accCode+"】"+XipUtil.getMessage(lang, "XC_GL_CUSTOMER_DIMENTION4_NOT_NULL", null)) ;
								}
							}

							// 生成科目组合ID
							StringBuffer fullCode = new StringBuffer() ;
							fullCode.append(accCode).append("/")
							.append(vendorCode).append("/")
							.append(customerCode).append("/")
							.append(prodCode).append("/")
							.append(orgCode).append("/")
							.append(empCode).append("/")
							.append(deptCode).append("/")
							.append(prjCode).append("/")
							.append(cust1Code).append("/")
							.append(cust2Code).append("/")
							.append(cust3Code).append("/")
							.append(cust4Code) ;
							
							JSONObject jo = this.getCCID(fullCode.toString(), head.getLedgerId(), String.valueOf(accJo.getString("id")) , segments) ;
							line.setCcid(jo.getString("id"));
						}
					}
				}
			}
			// 凭证分录下的科目ID信息
			head.setAccIdList(accIdList);
		}
	}
	
	/**
	 * @Title: chkEntries 
	 * @Description: 处理凭证分录行信息
	 * <p>
	 * 	1.判断是否需要出纳签字
	 * 	2.处理分录行ID,处理科目计量单位和币种
	 *  3.计算新增、删除和更新的凭证分录行
	 * </p>
	 * @param head
	 * @param chktype
	 * @throws Exception    设定文件
	 */
	private void chkEntries(VHead head, String chktype) throws Exception {
		String lang = XCUtil.getLang();//语言值
		// 凭证分录所包含的科目ID列表信息
		List<String> accIdList = head.getAccIdList() ;
		
		// 查询账簿级科目信息
		List<Account> accounts = xcglCommonDAO.getLedgerAccounts(head.getLedgerId(), accIdList) ;
		// 将List转换成Map
		HashMap<String,Account> map = new HashMap<String,Account>();
		if(accounts != null && accounts.size()>0){
			for(Account account : accounts){
				String accId = account.getAccId() ;
				map.put(accId, account) ;
				accIdList.remove(accId) ;
			}
			// 判断分录中是否存在不合法科目信息
			if(accIdList != null && accIdList.size() >0){
				String accKey =  XCRedisKeys.getAccKey(head.getHrcyId()) ;
				StringBuffer str = new StringBuffer(XipUtil.getMessage(lang, "XC_GL_ACCOUNT_M", null)) ;
				for(String accId : accIdList){
					Object accJson = redisDaoImpl.hashGet(accKey, accId) ;
					if(accJson != null){
						JSONObject accJo = new JSONObject(String.valueOf(accJson)) ;
						str.append(accJo.getString("code")).append(",") ;
					}else{
						str.append(accId).append(",") ;
					}
				}
				str.append(XipUtil.getMessage(lang, "XC_GL_IN_LEDGER_NO_EFFECT", null)) ;
				
				throw new Exception(str.toString()) ;
			}
		}
		
		// 凭证更新, 计算待删除的分录信息
		List<String> delLines = new ArrayList<String>() ;
		if("update".equals(chktype)){
			// 当前凭证存在的分录信息
			 List<VLine> existsLines = voucherMapper.getVLines(head.getHeadId()) ;
			 if(existsLines != null && existsLines.size()>0){
				 for(VLine line : existsLines){
					 delLines.add(line.getLineId()) ;
				 }
			 }
		}
		
		// 判断本凭证是否需要出纳签字
		if(accounts != null && accounts.size()>0){
			head.setIsSigned("N");
			for(Account account : accounts){
				if("Y".equals(account.getIsBankAcc()) || "Y".equals(account.getIsCashAcc())){
					head.setIsSigned("Y");
					break ;
				}
			}
		}
		
		// 处理分录行ID,处理科目计量单位、数量、原币借方金额、原币贷方金额、汇率和币种
		List<VLine> lines = head.getLines() ;
		if(lines != null && lines.size()>0){
			List<VLine> addLines = new ArrayList<VLine>() ;
			List<VLine> updLines = new ArrayList<VLine>() ;
			
			for(VLine line :lines){
				// 科目ID
				String accId = line.getAccId() ;
				
				if("save".equals(chktype) || "write_off".equals(chktype)){	// 新增或冲销
					line.setLineId(UUID.randomUUID().toString());
					
					// 账簿会计科目信息
					Account account = map.get(accId) ;
					
					// 处理币种信息
					line.setCurrencyCode(account.getDefaultCny());
					
					// 如果科目为非外币核算，则设置汇率、清空原币借方和贷方金额
					if(!"Y".equals(account.getIsForeignCny())){
						line.setEdr("0");
						line.setEcr("0");
						line.setExchangeRate(1);
					}
					
					// 处理计量单位
					if("Y".equals(account.getIsAmount())){
						line.setDimId(account.getDimId());
						
					}else{
						line.setDimId(null);
						line.setAmt("0");
					}
					
					addLines.add(line) ;
					
				}else if("update".equals(chktype)){ // 更新
					String lineId = line.getLineId() ;
					
					if(lineId == null || "".equals(lineId)){
						// 新增记录行
						line.setLineId(UUID.randomUUID().toString());
						
						// 账簿会计科目信息
						Account account = map.get(accId) ;
						
						// 处理币种信息
						line.setCurrencyCode(account.getDefaultCny());
						
						// 如果科目为非外币核算，则设置汇率、清空原币借方和贷方金额
						if(!"Y".equals(account.getIsForeignCny())){
							line.setEdr("0");
							line.setEcr("0");
							line.setExchangeRate(1);
						}
						
						// 处理计量单位
						if("Y".equals(account.getIsAmount())){
							line.setDimId(account.getDimId());
							
						}else{
							line.setDimId(null);
							line.setAmt("0");
						}
						
						addLines.add(line) ;
						
					}else{
						// 更新记录行
						updLines.add(line) ;
					}
					
					// 计算待删除分录行
					if(delLines.contains(lineId)){
						delLines.remove(lineId) ;
					}
				}
			}
			
			// 记录凭证分录中新增、更新和删除的分录信息
			if(!addLines.isEmpty()){	
				// 新增分录行
				head.setAddLines(addLines);
			}
			if(!addLines.isEmpty()){
				// 更新分录行
				head.setUpdLines(updLines);
			}
			if(!addLines.isEmpty()){
				// 删除分录行
				head.setDelLines(delLines);
			}
		}
	}
	
	/**
	 * @Title: chkBalances 
	 * @Description: 判断凭证借贷是否平衡
	 * <p>
	 * 	1.同一条凭证分录的借方和贷方不能同时录入数据 
	 * 	2.外币凭证需要录入原币金额和汇率,并计算本位币折后金额
	 * 	3.计算凭证头的借方和贷方合计数并判断借贷合计数是否平衡
	 * </p>
	 * @param bean
	 * @throws Exception    设定文件
	 */
	private void chkBalances(VHead head) throws Exception {
		String lang = XCUtil.getLang();//语言值
		// 凭证模板分类: 1-金额凭证，2-数量凭证，3-外币凭证
		String templateType = head.getTemplateType() ;
		// 外部凭证
		if(head.getSrcCode() != null && !"".equals(head.getSrcCode())){
			VSource vs = xcglCommonDAO.getVSource(head.getSrcCode()) ;
			if("Y".equals(vs.getIsSys()) && "3".equals(templateType)){
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_NO_SUPPORT", null)) ;
			}
		}
		
		// 凭证分录行信息
		List<VLine> lines = head.getLines() ;
		if(lines != null && lines.size()>0){
			
			// 凭证借方和贷方合计数
			BigDecimal totalDR = new BigDecimal(0) ;
			BigDecimal totalCR = new BigDecimal(0) ;
			
			for(VLine line :lines){
				if("3".equals(templateType)){	// 外币金额式凭证
					
					// 同一条凭证分录的借方和贷方不能同时录入数据 
					double enterDR = line.getEnterDR() ;
					double enterCR = line.getEnterCR() ;
					float exchageRate = line.getExchangeRate() ;
					
					if(exchageRate <= 0){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_NO_LOWER_ZERO", null)) ;
					}
					
					if(enterDR != 0 && enterCR != 0) {
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_NO_ZERO", null)) ;
					}
					if(enterDR == 0 && enterCR == 0){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_IS_ZERO", null)) ;
					}
					
					// 外币凭证需要录入原币金额和汇率,并计算本位币折后金额
					BigDecimal edr = new BigDecimal(enterDR) ;
					BigDecimal cdr = new BigDecimal(enterCR) ;
					BigDecimal er = new BigDecimal(exchageRate) ;
					
					BigDecimal adr = edr.multiply(er).setScale(2, BigDecimal.ROUND_HALF_UP) ;
					BigDecimal acr = cdr.multiply(er).setScale(2, BigDecimal.ROUND_HALF_UP) ;
					
					line.setAccountDR(adr.doubleValue());
					line.setAccountCR(acr.doubleValue());
					
				}else{	// 金额式凭证和数量金额式凭证
					
					// 同一条凭证分录的借方和贷方不能同时录入数据 
					double accountDR = line.getAccountDR() ;
					double accountCR = line.getAccountCR() ;
					
					if(accountDR != 0 && accountCR != 0){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_CURRENCY_NO_ZERO", null)) ;
					}
					if(accountDR == 0 && accountCR == 0){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_CURRENCY_IS_ZERO", null)) ;
					}
				}
				
				// 计算凭证头的借方和贷方合计数并判断借贷合计数是否平衡,四舍五入模式
				totalDR = totalDR.add(new BigDecimal(line.getAccountDR())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
				totalCR = totalCR.add(new BigDecimal(line.getAccountCR())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
			}
			
			// 判断凭证借贷合计数是否平衡
			double ddr = totalDR.doubleValue() ;
			double dcr = totalCR.doubleValue() ;
			if(ddr == dcr){
				head.setTdr(totalDR.toPlainString());
				head.setTcr(totalCR.toPlainString());
				head.setTotalDR(ddr);
				head.setTotalCR(dcr);
			}else{
				throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_TOTAL_NO_BALANCE", null)+ddr+XipUtil.getMessage(lang, "XC_GL_CR_TOTAL", null)+dcr) ;
			}
		}
	}

	/**
	 * @Title: chkSubmitV 
	 * @Description: 凭证存在但未提交即草稿状态
	 * <p>
	 * 	1、凭证在系统中存在，且未提交
	 * 	2、需要汇总未过账余额
	 * </p>
	 * <p>
	 * 	此校验适用于凭证提交和凭证删除
	 * </p>
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> chkNotSubmitV(String jsonArray) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		String lang = XCUtil.getLang();//语言值
		// 将JSON数组转换成List对象
		List<String> list = this.jsonArray2List(jsonArray) ;
		
		if(list.isEmpty()){
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_PARAM_NO_NULL", null)) ;
		}else{
			// 根据凭证头ID，查询凭证头信息
			List<VHead> headList = voucherMapper.getVHeads(list) ;
			
			// 判断凭证在系统中是否存在且尚未提交
			if(headList != null && headList.size() >0){
				List<String> invalidList = new ArrayList<String>() ;
				List<String> validList = new ArrayList<String>() ; 	
				for(VHead head : headList){
					// 计算系统中不存在的凭证
					String headId = head.getHeadId() ;
					list.remove(headId) ;
					// 凭证状态 ：1-未提交,2-已提交
					String vs = head.getVStatus() ;
					if("1".equals(vs)){
						validList.add(headId) ;
					}else{
						invalidList.add(headId) ;
					}
				}
				// 合法的凭证
				if(!validList.isEmpty()){
					jo.put("validV", validList) ;
				}else{
					jo.put("validV", "") ;
				}
				// 不合法的凭证
				if(!invalidList.isEmpty()) {
					jo.put("invalidV", invalidList.toString());
				}else{
					jo.put("invalidV", "");
				}
				
			}
			// 系统中不存在凭证
			if(!list.isEmpty()) {
				jo.put("notExistsV", list.toString());
			}else{
				jo.put("notExistsV", "");
			}
		}
		
		return jo ;
	}

	/**
	 * @Title: chkCancelSubmitV 
	 * @Description: 凭证撤回提交、驳回参数的合法性验证
	 * <p>
	 * 	1、凭证在系统中存在、已提交但未审核、未签字
	 * 	2、需要汇总未过账余额
	 * </p>
	 * <p>
	 *  此校验适用于撤回提交、驳回凭证
	 * </p>
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> chkSubmitedAndNotVerifiedV(String jsonArray) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		String lang = XCUtil.getLang();//语言值
		// 将JSON数组转换成List对象
		List<String> list = this.jsonArray2List(jsonArray) ;
		
		if(list.isEmpty()){
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_PARAM_NO_NULL", null)) ;
		}else{
			// 根据凭证头ID，查询凭证头信息
			List<VHead> headList = voucherMapper.getVHeads(list) ;
			
			// 判断凭证在系统中是否存在且尚未提交
			if(headList != null && headList.size() >0){
				List<String> invalidList = new ArrayList<String>() ;
				List<String> validList = new ArrayList<String>() ; 	
				for(VHead head : headList){
					// 计算系统中不存在的凭证
					String headId = head.getHeadId() ;
					list.remove(headId) ;
					// 凭证状态 ：1-未提交,2-已提交
					String vs = head.getVStatus() ;
					// 审核人ID
					String verified = head.getVerifierId() ; 
					String sinnatoryId = head.getSignatoryId() ;
					// 已提交但未审核、未签字
					if("2".equals(vs) && verified == null && sinnatoryId == null){ 
						validList.add(headId) ;
					}else{
						invalidList.add(headId) ;
					}
				}
				// 合法的凭证
				if(!validList.isEmpty()) {
					jo.put("validV", validList) ;
				}else{
					jo.put("validV", "") ;
				}
				// 不合法的凭证
				if(!invalidList.isEmpty()) {
					jo.put("invalidV", invalidList.toString());
				}else{
					jo.put("invalidV", "");
				}
				
			}
			// 系统中不存在凭证
			if(!list.isEmpty()) {
				jo.put("notExistsV", list.toString());
			}else{
				jo.put("notExistsV", "");
			}
		}
		
		return jo ;
	}

	/**
	 * @Title: checkV 
	 * @Description: 凭证审核参数的合法性验证
	 * <p>
	 * 	1、凭证在系统中存在、已提交但未审核
	 * </p>
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> checkV(String jsonArray) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		String lang = XCUtil.getLang();//语言值
		// 将JSON数组转换成List对象
		List<String> list = this.jsonArray2List(jsonArray) ;
		
		if(list.isEmpty()){
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_PARAM_NO_NULL", null)) ;
		}else{
			// 根据凭证头ID，查询凭证头信息
			List<VHead> headList = voucherMapper.getVHeads(list) ;
			
			// 判断凭证在系统中是否存在且尚未提交
			if(headList != null && headList.size() >0){
				List<String> invalidList = new ArrayList<String>() ;
				List<String> validList = new ArrayList<String>() ; 	
				for(VHead head : headList){
					// 计算系统中不存在的凭证
					String headId = head.getHeadId() ;
					list.remove(headId) ;
					// 凭证状态 ：1-未提交,2-已提交
					String vs = head.getVStatus() ;
					// 审核人ID
					String verified = head.getVerifierId() ; 
					// 已提交但未审核
					if("2".equals(vs) && verified == null){ 
						validList.add(headId) ;
					}else{
						invalidList.add(headId) ;
					}
				}
				// 合法的凭证
				if(!validList.isEmpty()) {
					jo.put("validV", validList) ;
				}else{
					jo.put("validV", "") ;
				}
				// 不合法的凭证
				if(!invalidList.isEmpty()) {
					jo.put("invalidV", invalidList.toString());
				}else{
					jo.put("invalidV", "");
				}
				
			}
			// 系统中不存在凭证
			if(!list.isEmpty()) {
				jo.put("notExistsV", list.toString());
			}else{
				jo.put("notExistsV", "");
			}
		}
		
		return jo ;
	}

	/**
	 * @Title: uncheckV 
	 * @Description: 凭证取消审核参数的合法性验证
	 * <p>
	 * 	1、凭证在系统中存在，已提交、已审核但未过账
	 * </p>
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> uncheckV(String jsonArray) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		String lang = XCUtil.getLang();//语言值
		// 将JSON数组转换成List对象
		List<String> list = this.jsonArray2List(jsonArray) ;
		
		if(list.isEmpty()){
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_PARAM_NO_NULL", null)) ;
			
		}else{
			// 根据凭证头ID，查询凭证头信息
			List<VHead> headList = voucherMapper.getVHeads(list) ;
			
			// 判断凭证在系统中是否存在且尚未提交
			if(headList != null && headList.size() >0){
				List<String> invalidList = new ArrayList<String>() ;	// 不合法的凭证
				List<String> validList = new ArrayList<String>() ; 		// 合法的凭证
				for(VHead head : headList){
					// 计算系统中不存在的凭证
					String headId = head.getHeadId() ;
					list.remove(headId) ;
					// 凭证状态 ：1-未提交,2-已提交
					String vs = head.getVStatus() ;
					// 审核人ID
					String verifierId = head.getVerifierId() ; 
					// 记账人ID
					String bookkeeperId = head.getBookkeeperId() ;
					// 已提交、已审核但是未记账
					if("2".equals(vs) && verifierId != null && bookkeeperId == null){ 
						validList.add(headId) ;
					}else{
						invalidList.add(headId) ;
					}
				}
				// 合法的凭证
				if(!validList.isEmpty()) {
					jo.put("validV", validList) ;
				}else{
					jo.put("validV", "") ;
				}
				// 不合法的凭证
				if(!invalidList.isEmpty()) {
					jo.put("invalidV", invalidList.toString());
				}else{
					jo.put("invalidV", "");
				}
				
			}
			// 系统中不存在凭证
			if(!list.isEmpty()) {
				jo.put("notExistsV", list.toString());
			}else{
				jo.put("notExistsV", "");
			}
		}
		
		return jo ;
	}

	
	/**
	 * @Title: signV 
	 * @Description: 出纳签字参数的合法性验证
	 * <p>
	 * 	1、凭证在系统中存在，已提交、未签字（需要出纳签字）
	 * </p>
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> signV(String jsonArray) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		String lang = XCUtil.getLang();//语言值
		// 将JSON数组转换成List对象
		List<String> list = this.jsonArray2List(jsonArray) ;
		
		if(list.isEmpty()){
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_PARAM_NO_NULL", null)) ;
			
		}else{
			// 根据凭证头ID，查询凭证头信息
			List<VHead> headList = voucherMapper.getVHeads(list) ;
			
			// 判断凭证在系统中是否存在且尚未提交
			if(headList != null && headList.size() >0){
				List<String> invalidList = new ArrayList<String>() ;	// 不合法的凭证
				List<String> validList = new ArrayList<String>() ; 		// 合法的凭证
				for(VHead head : headList){
					// 计算系统中不存在的凭证
					String headId = head.getHeadId() ;
					list.remove(headId) ;
					// 凭证状态 ：1-未提交,2-已提交
					String vs = head.getVStatus() ;
					// 签字人ID
					String isSign = head.getIsSigned() ;
					String signatoryId = head.getSignatoryId() ; 
					// 已提交但未签字（需要出纳签字）
					if("2".equals(vs) && "Y".equals(isSign) && signatoryId == null){ 
						validList.add(headId) ;
					}else{
						invalidList.add(headId) ;
					}
				}
				// 合法的凭证
				if(!validList.isEmpty()) {
					jo.put("validV", validList) ;
				}else{
					jo.put("validV", "") ;
				}
				// 不合法的凭证
				if(!invalidList.isEmpty()) {
					jo.put("invalidV", invalidList.toString());
				}else{
					jo.put("invalidV", "");
				}
				
			}
			// 系统中不存在凭证
			if(!list.isEmpty()) {
				jo.put("notExistsV", list.toString());
			}else{
				jo.put("notExistsV", "");
			}
		}
		
		return jo ;
	}

	/**
	 * @Title: unsignV 
	 * @Description: 出纳取消签字参数的合法性验证
	 * <p>
	 * 	1、凭证在系统中存在，已提交、已签字（需要出纳签字）但未过账
	 * </p>
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> unsignV(String jsonArray) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		String lang = XCUtil.getLang();//语言值
		// 将JSON数组转换成List对象
		List<String> list = this.jsonArray2List(jsonArray) ;
		
		if(list.isEmpty()){
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_PARAM_NO_NULL", null)) ;
			
		}else{
			// 根据凭证头ID，查询凭证头信息
			List<VHead> headList = voucherMapper.getVHeads(list) ;
			
			// 判断凭证在系统中是否存在且尚未提交
			if(headList != null && headList.size() >0){
				List<String> invalidList = new ArrayList<String>() ;	// 不合法的凭证
				List<String> validList = new ArrayList<String>() ; 		// 合法的凭证
				for(VHead head : headList){
					// 计算系统中不存在的凭证
					String headId = head.getHeadId() ;
					list.remove(headId) ;
					// 凭证状态 ：1-未提交,2-已提交
					String vs = head.getVStatus() ;
					// 签字人ID
					String isSign = head.getIsSigned() ;
					String signatoryId = head.getSignatoryId() ; 
					// 记账人
					String bookkeeperId = head.getBookkeeperId() ;
					
					// // 已提交、签字（需要出纳签字）但未记账
					if("2".equals(vs) && ("Y".equals(isSign) && signatoryId != null) && bookkeeperId == null){ 
						validList.add(headId) ;
						
					}else{
						invalidList.add(headId) ;
					}
				}
				// 合法的凭证
				if(!validList.isEmpty()) {
					jo.put("validV", validList) ;
				}else{
					jo.put("validV", "") ;
				}
				// 不合法的凭证
				if(!invalidList.isEmpty()) {
					jo.put("invalidV", invalidList.toString());
				}else{
					jo.put("invalidV", "");
				}
			}
			// 系统中不存在凭证
			if(!list.isEmpty()) {
				jo.put("notExistsV", list.toString());
			}else{
				jo.put("notExistsV", "");
			}
		}
		
		return jo ;
	}

	/**
	 * @Title: accountV 
	 * @Description: 凭证过账参数的合法性验证
	 * <p>
	 * 	1、凭证在系统中存在，已提交、已审核、已签字（需要出纳签字）但未过账
	 * 	2、需要汇总过账余额
	 * </p>
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> accountV(String jsonArray) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		String lang = XCUtil.getLang();//语言值
		// 将JSON数组转换成List对象
		List<String> list = this.jsonArray2List(jsonArray) ;
		
		if(list.isEmpty()){
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_PARAM_NO_NULL", null)) ;
			
		}else{
			// 根据凭证头ID，查询凭证头信息
			List<VHead> headList = voucherMapper.getVHeads(list) ;
			
			// 判断凭证在系统中是否存在且尚未提交
			if(headList != null && headList.size() >0){
				List<String> invalidList = new ArrayList<String>() ;	// 不合法的凭证
				List<String> validList = new ArrayList<String>() ; 		// 合法的凭证
				for(VHead head : headList){
					// 排除系统存在的凭证
					String headId = head.getHeadId() ;
					list.remove(headId) ;
					// 凭证状态 ：1-未提交,2-已提交
					String vs = head.getVStatus() ;
					// 审核人ID
					String verifierId = head.getVerifierId() ; 
					// 签字人ID
					String isSign = head.getIsSigned() ;
					String signatoryId = head.getSignatoryId() ; 
					// 记账人
					String bookkeeperId = head.getBookkeeperId() ;
					if("2".equals(vs) && verifierId != null 
							&& (("Y".equals(isSign) && signatoryId != null) || "N".equals(isSign))
							&& bookkeeperId == null){ // 已提交、已审核、签字（需要出纳签字）但未过账
						validList.add(headId) ;
						
					}else{
						invalidList.add(headId) ;
					}
				}
				// 合法的凭证
				if(!validList.isEmpty()) {
					jo.put("validV", validList) ;
				}else{
					jo.put("validV", "") ;
				}
				// 不合法的凭证
				if(!invalidList.isEmpty()) {
					jo.put("invalidV", invalidList.toString());
				}else{
					jo.put("invalidV", "");
				}
			}
			// 系统中不存在凭证
			if(!list.isEmpty()) {
				jo.put("notExistsV", list.toString());
			}else{
				jo.put("notExistsV", "");
			}
		}
		return jo ;
	}
	
	/**
	 * @Title: unaccountV 
	 * @Description: 凭取消记账参数的合法性验证
	 * <p>
	 * 	1、凭证在系统中存在，已过账但未冲销
	 * 	2、需要汇总过账余额
	 * </p>
	 * @param jsonArray
	 * @return
	 * @throws Exception    设定文件
	 */
	private HashMap<String,Object> unaccountV(String jsonArray) throws Exception {
		HashMap<String,Object> jo = new HashMap<String,Object>() ;
		String lang = XCUtil.getLang();//语言值
		// 将JSON数组转换成List对象
		List<String> list = this.jsonArray2List(jsonArray) ;
		
		if(list.isEmpty()){
			throw new Exception(XipUtil.getMessage(lang, "XC_GL_PARAM_NO_NULL", null)) ;
			
		}else{
			// 根据凭证头ID，查询凭证头信息
			List<VHead> headList = voucherMapper.getVHeads(list) ;
			
			// 判断凭证在系统中是否存在且尚未提交
			if(headList != null && headList.size() >0){
				List<String> invalidList = new ArrayList<String>() ;	// 不合法的凭证
				List<String> validList = new ArrayList<String>() ; 		// 合法的凭证
				for(VHead head : headList){
					// 排除系统存在的凭证
					String headId = head.getHeadId() ;
					list.remove(headId) ;
					// 记账人
					String bookkeeperId = head.getBookkeeperId() ;
					// 冲销标志
					String isWriteOff = head.getIsWriteOff() ;
					if(bookkeeperId != null &&  !"".equals(bookkeeperId) && !"Y".equals(isWriteOff)){  // 已记账且未被冲销
						validList.add(headId) ;
					}else{
						invalidList.add(headId) ;
					}
				}
				// 合法的凭证
				if(!validList.isEmpty()) {
					jo.put("validV", validList) ;
				}else{
					jo.put("validV", "") ;
				}
				// 不合法的凭证
				if(!invalidList.isEmpty()) {
					jo.put("invalidV", invalidList.toString());
				}else{
					jo.put("invalidV", "");
				}
				
				// 判断合法凭证对应的期间是否为打开状态
				List<String> periods = voucherMapper.isOpenPeriod(validList) ;
				if(periods != null && periods.size()>0){
					// 关闭和永久关闭期间
					StringBuffer str = new StringBuffer() ;
					str.append(XipUtil.getMessage(lang, "XC_GL_PERIOD_BET", null)) ;
					for(String period : periods){
						str.append(period).append("  ") ;
					}
					str.append(XipUtil.getMessage(lang, "XC_GL_NO_CANCEL_ACCOUNT", null)) ;
					
					throw new Exception(str.toString()) ;
				}
			}
			// 系统中不存在凭证
			if(!list.isEmpty()) {
				jo.put("notExistsV", list.toString());
			}else{
				jo.put("notExistsV", "");
			}
		}
		return jo ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: checkSingleVoucher</p> 
	 * <p>Description: 单个凭证保存或提交审核时校验 </p> 
	 * @param chkType
	 * @param map
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.AbstractVoucherHandler#checkSingleVoucher(java.lang.String, java.util.HashMap)
	 */
	@Override
	public void checkSingleVoucher(HashMap<String,Object> map) throws Exception {
		String lang = XCUtil.getLang();//语言值
		try {
			// 操作类型（add-新增，copy-复制，write_off-冲销，modify-修改）
			String optype = String.valueOf(map.get("optype")) ;
			// 凭证头信息
			VHead head = (VHead) map.get("bean") ;
			
			String headId = head.getHeadId() ;
			String serialNum = head.getSerialNum(); 
			String writeOffNum = head.getWriteOffNum() ;
			
			// 执行校验
			if(headId != null && !"".equals(headId)){
				VHead vhead = voucherMapper.isSubmitedVoucher(headId) ;
				if(vhead != null){
					throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_NOT_UPDATE", null)) ;
				}
				
			}else{
				// 凭证编号是否已经使用
				if(serialNum != null && !"".equals(serialNum)){
					HashMap<String,String> paramMap = new HashMap<String,String>() ;
					paramMap.put("ledgerId", head.getLedgerId()) ;
					paramMap.put("periodCode", head.getPeriodCode()) ;
					paramMap.put("serialNum", serialNum) ;
					VHead vhead = voucherMapper.isUsedSerialNum(paramMap) ;
					if(vhead != null){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_CODE", null)+"【"+serialNum+"】"+XipUtil.getMessage(lang, "XC_GL_ALREADY_USE", null)) ;
					}
				}
				// 凭证是否已经冲销
				if("write_off".equals(optype)){
					VHead vhead = voucherMapper.isWriteOffVoucher(writeOffNum) ;
					if(vhead != null){
						throw new Exception(XipUtil.getMessage(lang, "XC_GL_VOUCHER_HAVE_WRITE", null)) ;
					}
				}
			}
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: generateSQL</p> 
	 * <p>Description: 生成SQL </p> 
	 * @param workbook
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.AbstractVoucherHandler#generateSQL(org.apache.poi.hssf.usermodel.HSSFWorkbook)
	 */
	@Override
	public HashMap<String,Object> generateSQL(HSSFWorkbook workbook) throws Exception {
		HashMap<String,Object> map = new HashMap<String,Object>() ;
		
		try {
			StringBuffer str = new StringBuffer("INSERT INTO XC_GL_VOUCHER_TMP(") ;
			// 工作页签
			HSSFSheet sheet = workbook.getSheetAt(0) ;
			// 第一行
			HSSFRow rowOne = sheet.getRow(0) ;
			// 第二行
			HSSFRow rowTwo = sheet.getRow(1) ;
			
			int fieldNum = 0 ;
			HashMap<Integer,String> colMap = new HashMap<Integer,String>() ;
			HashMap<Integer,String> fieldMap = new HashMap<Integer,String>() ;
			
			// 获取表头批注信息
			Iterator<Cell> itOne = rowOne.cellIterator() ;
			while(itOne.hasNext()){
				// 第一行批注
				Cell cell = itOne.next() ;
				Comment comment = cell.getCellComment() ;
				if(comment != null){
					String fieldName = comment.getString().toString() ;
					fieldName = fieldName.replace("\t", "").replace("\n", "").replace("\r", "") ;
					str.append(fieldName).append(",") ;
					
					fieldMap.put(cell.getColumnIndex(), fieldName) ;
					
					colMap.put(fieldNum, fieldName) ;
					fieldNum++ ;
					
				}else{
					// 如果批注不在第一行，则获取第二行批注
					int colIndex = cell.getColumnIndex() ; // 当前列号
					cell = rowTwo.getCell(colIndex) ;	
					comment = cell.getCellComment() ;
					if(comment != null){
						String fieldName = comment.getString().toString() ;
						fieldName = fieldName.replace("\t", "").replace("\n", "").replace("\r", "") ;
						str.append(fieldName).append(",") ;
						
						fieldMap.put(cell.getColumnIndex(), fieldName) ;
						
						colMap.put(fieldNum, fieldName) ;
						fieldNum++ ;
					}
				}
			}
			str.append("ROW_NUM").append(",")
				.append("CREATED_BY").append(",")
				.append("V_STATUS").append(",")
				.append("ORDERBY").append(",")
				.append("LEDGER_ID").append(",")
				.append("LEDGER_CODE").append(",")
				.append("FLAG").append(",")
				.append("ERROR_MSG").append(",")
				.append("SESSION_ID") 
				.append(")VALUES(") ;
			
			for(int i=0; i<fieldNum; i++){
				str.append(":").append(colMap.get(i)).append(",") ;
			}
			
			str.append(":ROW_NUM").append(",")
				.append(":CREATED_BY").append(",")
				.append(":V_STATUS").append(",")
				.append(":ORDERBY").append(",")
				.append(":LEDGER_ID").append(",")
				.append(":LEDGER_CODE").append(",")
				.append(":FLAG").append(",")
				.append(":ERROR_MSG").append(",")
				.append(":SESSION_ID")
				.append(")") ;
			
			String sql = str.toString() ;
			
			// 返回值
			map.put("sql", sql) ;
			map.put("fieldMap", fieldMap) ;
			
		} catch (Exception e) {
			throw e ;
		}
		
		return map ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: parseExcel</p> 
	 * <p>Description: 解析Excel文件 </p> 
	 * @param fieldMap
	 * @param workbook
	 * @param sessionId
	 * @param ledger
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.AbstractVoucherHandler#parseExcel(java.util.HashMap, org.apache.poi.hssf.usermodel.HSSFWorkbook, java.lang.String, java.lang.String)
	 */
	@Override
	public List<HashMap<String,Object>> parseExcel(HashMap<Integer,String> fieldMap,
				HSSFWorkbook workbook,String sessionId,Ledger ledger) throws Exception {
		
		List<HashMap<String,Object>> vals = new ArrayList<HashMap<String,Object>>() ;
		try {
			// 第一个页签
			HSSFSheet sheet = workbook.getSheetAt(0) ;
			for(int i=2; i<=sheet.getLastRowNum(); i++){
				HashMap<String,Object> valMap = new HashMap<String,Object>() ;
				
				HSSFRow row = sheet.getRow(i) ;
				// 行号
				int rowNum = row.getRowNum() ;
				valMap.put("ROW_NUM", rowNum+1) ;
				
				// 设置记录行的有效标志位
				valMap.put("FLAG", "Y") ;
				valMap.put("ERROR_MSG", "") ;
				
				Iterator<Cell> it = row.iterator() ;
				while(it.hasNext()){
					// 单元格
					Cell cell = it.next() ;
					
					// 列名
					String fieldName = fieldMap.get(cell.getColumnIndex()) ;
					
					// 如果列名不为空，则设置单元格值
					if(fieldName != null && !"".equals(fieldName)){ 
						Object cellV = null ;
						// 根据excel单元格类型取值
						switch(cell.getCellType())
						{
							case Cell.CELL_TYPE_NUMERIC :
								cellV = cell.getNumericCellValue() ;
								break ;
							case Cell.CELL_TYPE_STRING :
								cellV = cell.getStringCellValue() ;
								break ;
							case Cell.CELL_TYPE_FORMULA :
								cellV = cell.getCellFormula() ;
								break ;
							case Cell.CELL_TYPE_BLANK :
								cellV = cell.getStringCellValue() ;
								break ;
							case Cell.CELL_TYPE_BOOLEAN :
								cellV = cell.getBooleanCellValue() ;
								break ;
							case Cell.CELL_TYPE_ERROR :
								cellV = cell.getErrorCellValue() ;
								break ;
							default :
								break ;
						}
						
						// 根据字段类型读取单据格值
						if("CREATION_DATE".equals(fieldName)){ // 会计日期校验及转换
							if(cellV != null && !"".equals(cellV)){
								try {
									XCDateUtil.str2Date(String.valueOf(cellV).trim()) ;
									
								} catch (Exception e) {
									valMap.put("FLAG", "N") ;
									valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行会计日期【"+cellV+"】不合法:正确格式为yyyy-MM-dd;") ;
								}
								valMap.put(fieldName, cellV) ;
							}else{
								valMap.put("FLAG", "N") ;
								valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行会计日期不能为空;") ;
								valMap.put(fieldName, null) ;
							}
							
						}else if("AMOUNT".equals(fieldName)
								|| "EXCHANGE_RATE".equals(fieldName)
								|| "ENTER_DR".equals(fieldName)
								|| "ENTER_CR".equals(fieldName)
								|| "ACCOUNT_DR".equals(fieldName)
								|| "ACCOUNT_CR".equals(fieldName)){ // 数值转换
							
							if(cellV != null && !"".equals(cellV)){
								// 单元格值
								String val = String.valueOf(cellV).trim() ;
								
								if(pattern.matcher(val).matches()){ // 数值
									valMap.put(fieldName, Double.parseDouble(val)) ;
									
								}else{ // 非数值
									valMap.put("FLAG", "N") ;
									
									// 错误信息
									String msg = null ;
									if("AMOUNT".equals(fieldName)){
										msg = "数量" ;
									}else if("EXCHANGE_RATE".equals(fieldName)){
										msg = "汇率" ;
									}else if("ENTER_DR".equals(fieldName)){
										msg = "原币借方" ;
									}else if("ENTER_CR".equals(fieldName)){
										msg = "原币贷方" ;
									}else if("ACCOUNT_DR".equals(fieldName)){
										msg = "折后借方" ;
									}else if("ACCOUNT_CR".equals(fieldName)){
										msg = "折后贷方" ;
									}
									
									valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行"+msg+"为非法数值;") ;
								}
								
							}else{
								valMap.put(fieldName, 0) ;
							}
							
						}else{
							// 非空项校验
							if("PERIOD_CODE".equals(fieldName)){ // 会计日期
								if(cellV == null || "".equals(cellV)){
									valMap.put("FLAG", "N") ;
									valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行会计期不能为空;") ;
									valMap.put(fieldName, null) ;
								}else{
									if("".equals(String.valueOf(cellV).trim())){
										valMap.put("FLAG", "N") ;
										valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行会计期不能为空;") ;
										valMap.put(fieldName, null) ;
									}else{
										valMap.put(fieldName, cellV) ;
									}
								}
								
							}else if("V_CATEGORY_CODE".equals(fieldName)){ // 凭证分类
								if(cellV == null || "".equals(cellV)){
									valMap.put("FLAG", "N") ;
									valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行凭证分类不能为空;") ;
									valMap.put(fieldName, null) ;
								}else{
									if("".equals(String.valueOf(cellV).trim())){
										valMap.put("FLAG", "N") ;
										valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行凭证分类不能为空;") ;
										valMap.put(fieldName, null) ;
									}else{
										valMap.put(fieldName, cellV) ;
									}
								}
								
							}else if("SUMMARY".equals(fieldName)){ // 分录行摘要
								if(cellV == null || "".equals(cellV)){
									valMap.put("FLAG", "N") ;
									valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行行摘要不能为空;") ;
									valMap.put(fieldName, null) ;
								}else{
									if("".equals(String.valueOf(cellV).trim())){
										valMap.put("FLAG", "N") ;
										valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行行摘要不能为空;") ;
										valMap.put(fieldName, null) ;
									}else{
										valMap.put(fieldName, cellV) ;
									}
								}
								
							}else if("ACC_CODE".equals(fieldName)){ // 科目编码
								if(cellV == null || "".equals(cellV)){
									valMap.put("FLAG", "N") ;
									valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行科目编码不能为空;") ;
									valMap.put(fieldName, null) ;
									
								}else{
									String val = String.valueOf(cellV).trim() ;
									if("".equals(val)){
										valMap.put("FLAG", "N") ;
										valMap.put("ERROR_MSG", valMap.get("ERROR_MSG")+"第"+(rowNum+1)+"行科目编码不能为空;") ;
										valMap.put(fieldName, null) ;
									}
									// 如果科目编码为数值
									if(pattern.matcher(val).matches()){ 
										int idx = val.lastIndexOf(".") ;
										cellV = val.substring(0, (idx != -1 ? idx : val.length())) ;
									}
									valMap.put(fieldName, cellV) ;
								}
									
							}else if("VENDOR_CODE".equals(fieldName)
									|| "CUSTOMER_CODE".equals(fieldName)
									|| "PRODUCT_CODE".equals(fieldName)
									|| "ORG_CODE".equals(fieldName)
									|| "EMP_CODE".equals(fieldName)
									|| "PROJECT_CODE".equals(fieldName)
									|| "DEPT_CODE".equals(fieldName)
									|| "CUSTOM1_CODE".equals(fieldName)
									|| "CUSTOM2_CODE".equals(fieldName)
									|| "CUSTOM3_CODE".equals(fieldName)
									|| "CUSTOM4_CODE".equals(fieldName)){ // 辅助段编码
								
								if(cellV == null || "".equals(cellV)){
									valMap.put(fieldName, "00") ;
								}else{
									String val = String.valueOf(cellV).trim() ;
									if(pattern.matcher(val).matches()){
										int idx = val.lastIndexOf(".") ;
										cellV = val.substring(0, (idx != -1 ? idx : val.length())) ; 
									}
									if("".equals(val)){
										cellV = "00" ;
									}
									valMap.put(fieldName, cellV) ;
								}
								
							}else if("CA_CODE".equals(fieldName)){ // 现金流量项目编码
								if(cellV == null || "".equals(cellV)){
									valMap.put(fieldName, null) ;
									
								}else{
									String val = String.valueOf(cellV).trim() ;
									if(pattern.matcher(val).matches()){
										int idx = val.lastIndexOf(".") ;
										cellV = val.substring(0, (idx != -1 ? idx : val.length())) ; 
									}
									if("".equals(val)){
										cellV = "00" ;
									}
									valMap.put(fieldName, cellV) ;
								}
								
							}else{
								valMap.put(fieldName, cellV) ;
							}
						}
					}
				}
				
				valMap.put("V_STATUS", "1") ;
				valMap.put("ORDERBY", rowNum-1) ;
				valMap.put("LEDGER_ID", ledger.getLedgerId()) ;
				valMap.put("LEDGER_CODE", ledger.getLedgerCode()) ;
				valMap.put("SESSION_ID", sessionId) ;
				valMap.put("CREATED_BY", CurrentSessionVar.getUserId()) ;
				
				vals.add(valMap) ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return vals ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: checkVoucherBeforeSave</p> 
	 * <p>Description: 凭证保存前的校验</p> 
	 * @param bean
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.gl.common.AbstractVoucherHandler#checkVoucherBeforeSave(com.xzsoft.xc.gl.modal.VHead)
	 */
	@Override
	public VHead checkVoucherBeforeSave(VHead bean) throws Exception {
		return this.chkVoucher(bean,"save") ;
	}
	
}
