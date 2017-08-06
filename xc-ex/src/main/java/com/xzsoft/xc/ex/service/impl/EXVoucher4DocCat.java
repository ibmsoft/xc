package com.xzsoft.xc.ex.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.ex.dao.EXVoucherDAO;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.PayBankModeBean;
import com.xzsoft.xc.ex.modal.PayModeBean;
import com.xzsoft.xc.ex.util.XCEXConstants;
import com.xzsoft.xc.gl.common.XCGLCommonService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.Employee;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.gl.util.XCGLConstants;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;

@Service("voucherDocService")
public class EXVoucher4DocCat {
	// 日志记录器
	private static final Logger log = Logger.getLogger(EXVoucher4DocCat.class.getName()) ;
	@Resource
	private RedisDao redisDaoImpl ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	@Resource
	private EXVoucherDAO exVoucherDAO ;
	@Resource
	private XCGLCommonService xcglCommonService ;
	

	/**
	 * @Title: getPayAccId 
	 * @Description: 取得凭证贷方分录科目信息
	 * @param payType
	 * @param bankId
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public String getPayAccId(String payType, String bankId, String ledgerId) throws Exception {
		// 查询账簿收付款方式
		PayModeBean modeBean = exVoucherDAO.getPayMode(payType, ledgerId) ;
		
		// 取得凭证贷方分录付款科目信息
		String payAccId = modeBean.getPayAccId() ;
		
		// 判断当前付款方式是否为银行付款(cash-现金，bank-银行,check-支票，other-其他)
		if(XCEXConstants.PAY_TYPE_BANK.equals(modeBean.getModeType())){	// 收付款方式为银行类
			if(bankId == null || "".equals(bankId)){
				// 当前付款方式为银行类, 付款账号不能为空
				//throw new Exception("当前付款方式为【"+modeBean.getModeName()+"】属于银行类, 请选择付款账号!") ; 
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_01", null)+"【"+modeBean.getModeName()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_02", null));
				
			}else{
				// 查询账簿开户行信息,并取得开户行所绑定的付款科目信息
				PayBankModeBean bankBean = exVoucherDAO.getPayBank(bankId) ;
				payAccId = bankBean.getAccId() ;
				if(payAccId != null && !"".equals(payAccId)){
					// 判断科目是否存在或失效
					Account acc = xcglCommonDAO.getLdAcountById(payAccId, ledgerId) ;
					if(acc == null){
						//throw new Exception("付款账号【"+bankBean.getBankAccount()+"】所绑定的付款科目未分配到当前账簿或已失效!") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_03", null)+"【"+bankBean.getBankAccount()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_04", null));
					}
					
				}else{
					//throw new Exception("付款账号【"+bankBean.getBankAccount()+"】未绑定付款科目!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_03", null)+"【"+bankBean.getBankAccount()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_05", null));
				}
			}
			
		}else{	
			// 非银行方式付款
			if(payAccId != null && !"".equals(payAccId)){
				
				//  判断科目是否存在或失效
				Account acc = xcglCommonDAO.getLdAcountById(payAccId, ledgerId) ;
				if(acc == null){
					//throw new Exception("收付款方式【"+modeBean.getModeName()+"】绑定的付款科目未分配到当前账簿或已失效!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_06", null)+"【"+modeBean.getModeName()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_04", null));
				}
				
			}else{
				//throw new Exception("当前付款方式为【"+modeBean.getModeName()+"】属于非银行类, 需绑定付款科目!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_01", null)+"【"+modeBean.getModeName()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPayAccId_07", null));
			}
		}
		
		return payAccId ;
	}

	/**
	 * @Title: getAccMap 
	 * @Description: 获取科目信息
	 * @param hrcyId
	 * @param accId
	 * @return
	 * @throws Exception    设定文件
	 */
	public HashMap<String,String> getAccMap(String hrcyId,String accId) throws Exception {
		HashMap<String,String> accMap = new HashMap<String,String>() ;
		
		String accKey =  XCRedisKeys.getAccKey(hrcyId) ;
		Object accJson = redisDaoImpl.hashGet(accKey, accId) ;
		
		// 刷新缓存
		if(accJson == null){
			xcglCommonService.syncAccHrcy2Redis(hrcyId, XCGLConstants.REDIS_OP_ADD) ;
			accJson = redisDaoImpl.hashGet(accKey, accId) ;
			if(accJson == null) {
				//throw new Exception("系统中不存在ID为【"+accId+"】的科目信息，请确认") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getAccMap_01", null)+"【"+accId+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getAccMap_02", null));
			}
		}
		
		JSONObject accJo = new JSONObject(String.valueOf(accJson)) ;
		String isCash = accJo.getString("isCash") ;
		String accCode = accJo.getString("code") ;
		
		accMap.put("accCode", accCode) ;
		accMap.put("isCash", isCash) ;
		
		return accMap ;
	}

	/**
	 * @Title: getCaCode 
	 * @Description: 取得现金流量项目编码
	 * @param caId
	 * @param isCash
	 * @param accCode
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getCaCode(String caId, String isCash, String accCode) throws Exception {
		String caCode = null ;
		if("Y".equals(isCash)){
			if(caId == null || "".equals(caId)){
				//throw new Exception("凭证分录科目【"+accCode+"】为银行科目,请选择现金流量项目") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getCaCode_02", null)+"【"+accCode+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getCaCode_03", null));
			}
			
			String cashKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_CASH_ITEMS) ;
			Object valJson = redisDaoImpl.hashGet(cashKey, caId) ;
			
			// 刷新缓存
			if(valJson == null){
				xcglCommonService.syncCashItem2Redis() ;
				valJson = redisDaoImpl.hashGet(cashKey, caId) ;
				if(valJson == null) //throw new Exception("现金流量项目("+caId+")不存在或未分配到账户") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getCaCode_01", null)+"【"+caId+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getDeptCode_02", null));
			}
			
			JSONObject valJo = new JSONObject(String.valueOf(valJson)) ;
			caCode = valJo.getString("code") ;
		}
		return caCode;
	}

	/**
	 * @Title: getEmpCode 
	 * @Description: 处理成本中心
	 * @param docBean
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getEmpCode(EXDocBean docBean) throws Exception {
		String empCode = null ;
		Employee emp = xcglCommonDAO.getEmployeeByUserId(docBean.getCreatedBy()) ;
		if(emp == null){
			empCode = "00" ;
		}else{
			empCode = emp.getEmpCode() ; 
		}
		return empCode;
	}

	/**
	 * @Title: getPrjCode 
	 * @Description: 处理项目
	 * @param docBean
	 * @param ledger
	 * @return
	 * @throws JSONException    设定文件
	 */
	public String getPrjCode(EXDocBean docBean, Ledger ledger) throws Exception {
		String prjCode = "00" ;
		if(docBean.getProjectId() !=null && !"".equals(docBean.getProjectId())){
			String prjKey = XCRedisKeys.getLedgerSegKey(XConstants.XC_PM_PROJECTS, ledger.getLedgerId()) ;
			Object prjJson = redisDaoImpl.hashGet(prjKey, docBean.getProjectId()) ;
			
			// 刷新缓存
			if(prjJson == null){
				xcglCommonService.syncProjects2Redis(ledger.getLedgerId(), XConstants.XC_PM_PROJECTS, XCGLConstants.REDIS_OP_ADD) ;
				prjJson = redisDaoImpl.hashGet(prjKey, docBean.getProjectId()) ;
				if(prjJson == null) //throw new Exception("PA项目("+docBean.getProjectId()+")不存在或未分配到账户") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getPrjCode_01", null)+"【"+docBean.getProjectId()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getDeptCode_02", null));
			}
			
			JSONObject prjJo = new JSONObject(String.valueOf(prjJson)) ;
			prjCode = prjJo.getString("code") ;
		}
		return prjCode;
	}

	/**
	 * @Title: getDeptCode 
	 * @Description: 处理个人往来
	 * @param docBean
	 * @param ledger
	 * @return
	 * @throws JSONException    设定文件
	 */
	public String getDeptCode(EXDocBean docBean, Ledger ledger) throws Exception {
		String deptCode = "00" ;
		if(docBean.getDeptId() !=null && !"".equals(docBean.getDeptId())){
			String ledgerId = ledger.getLedgerId() ;
			
			String deptKey = XCRedisKeys.getLedgerSegKey(XConstants.XIP_PUB_DETPS, ledgerId) ;
			Object deptJson = redisDaoImpl.hashGet(deptKey, docBean.getDeptId()) ;
			
			// 刷新缓存
			if(deptJson == null){
				xcglCommonService.syncDepts2Redis(ledgerId, XConstants.XIP_PUB_DETPS, XCGLConstants.REDIS_OP_ADD) ;
				deptJson = redisDaoImpl.hashGet(deptKey, docBean.getDeptId()) ;
				if(deptJson == null) //throw new Exception("成本中心("+docBean.getDeptId()+")不存在或未分配到账户") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getDeptCode_01", null)+"【"+docBean.getDeptId()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_getDeptCode_02", null));
			}
			
			JSONObject deptJo = new JSONObject(String.valueOf(deptJson)) ;
			deptCode = deptJo.getString("code") ;
		}
		return deptCode;
	}
	
	/**
	 * @Title: mergeVLines 
	 * @Description: 合并凭证分录信息
	 * @param docBeans
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception    设定文件
	 */
	public VHead mergeVLines(VHead head, List<VLine> dLines, List<VLine> cLines) throws Exception {
		try {
			// 组合凭证信息并设置凭证分录序号
			List<VLine> lines = new ArrayList<VLine>() ;
			int i = 1 ;
			
			// 借方分录
			for(VLine line : dLines){
				line.setOrderBy(i);
				i++ ;
			}
			lines.addAll(dLines) ;
			
			// 贷方分录
			for(VLine line : cLines){
				line.setOrderBy(i);
				i++ ;
			}
			lines.addAll(cLines) ;
			
			// 封装凭证信息
			head.setLines(lines);
			
		} catch (Exception e) {
			throw e ;
		}
		
		return head ;
	}
	
	/**
	 * @Title: createVHead 
	 * @Description: 生成报销单凭证头信息
	 * @param docBean
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception    设定文件
	 */
	public VHead createVHead(EXDocBean docBean,Ledger ledger,VSource vs,String paramJson) throws Exception {
		VHead head = new VHead() ;
		
		try {
			JSONObject paramJo = new JSONObject(paramJson) ;
			
			String accDate = paramJo.getString("accDate") ;	// 会计日期
			head.setLedgerCode(ledger.getLedgerCode());		// 账簿编码
			head.setPeriodCode(accDate.substring(0, 7));	// 会计期
			
			// 设置凭证来源编码
			head.setSrcCode(vs.getSrcCode());
			
			// 设置来源ID
			head.setSrcId(docBean.getDocId());	
			
			// 设置凭证分类
			if(vs.getvCat() == null || "".equals(vs.getvCat())){
				//throw new Exception("编码为【"+vs.getSrcCode()+"】的凭证来源未设置凭证分类,请确认！") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_01", null)+"【"+vs.getSrcCode()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_02", null));
				
			}else{
				String catKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_V_CATEGORY) ;
				Object catJson = redisDaoImpl.hashGet(catKey, vs.getvCat()) ;
				
				// 刷新缓存
				if(catJson == null){
					xcglCommonService.syncVCategory() ;
					catJson = redisDaoImpl.hashGet(catKey, vs.getvCat()) ;
					if(catJson == null) //throw new Exception("凭证分类"+vs.getvCat()+"不存在") ; 
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_03", null)+"【"+vs.getSrcCode()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_04", null));
				}
				
				JSONObject jo = new JSONObject(String.valueOf(catJson)) ;
				head.setCategoryCode(jo.getString("code"));	
			}

			head.setAttchTotal(docBean.getAttachNum());	// 附件数
			head.setSummary(docBean.getExReason());		// 摘要
			
			head.setSerialNum(null);		// 凭证号
			head.setTemplateType("1");		// 默认金额式凭证
			head.setVStatus("1");			// 凭证状态 
			head.setCreatedBy(CurrentSessionVar.getUserId());
			head.setCreationDate(XCDateUtil.str2Date(accDate));
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return head ;
	}
	
}
