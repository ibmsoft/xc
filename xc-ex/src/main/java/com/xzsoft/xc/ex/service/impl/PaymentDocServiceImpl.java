package com.xzsoft.xc.ex.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.ex.dao.EXVoucherDAO;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.service.EXVoucher4DocService;
import com.xzsoft.xc.gl.common.XCGLCommonService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.util.common.XCRedisKeys;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.common.XConstants;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.dao.RedisDao;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: PaymentDocServiceImpl 
 * @Description: 付款单凭证处理
 * @author linp
 * @date 2016年4月1日 上午9:34:17 
 * 
 */
@Service("paymentDocService")
public class PaymentDocServiceImpl extends EXVoucher4DocCat implements EXVoucher4DocService {
	// 日志记录器
	private static final Logger log = Logger.getLogger(PaymentDocServiceImpl.class.getName()) ;

	@Resource
	private RedisDao redisDaoImpl ;
	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	@Resource
	private EXVoucherDAO exVoucherDAO ;
	@Resource
	private XCGLCommonService xcglCommonService ;
	
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: mergeVoucher</p> 
	 * <p>Description: 组合凭证信息 </p> 
	 * @param docBeans
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucher4DocService#mergeVoucher(java.util.List, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public VHead mergeVoucher(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception {
		VHead head = new VHead() ;
		
		try {
			// 生成凭证头信息
			head = this.createVHead(docBeans, ledger, vs, paramJson) ;
			
			// 生成凭证借方分录信息
			List<VLine> dLines = this.createVDebitLines(docBeans, ledger, vs, paramJson) ;
			
			// 生成凭证贷方分录信息
			List<VLine> cLines = this.createVCreditLines(docBeans, ledger, vs, paramJson) ;
			
			// 合并分录信息
			head = this.mergeVLines(head, dLines, cLines) ;
			
		} catch (Exception e) {
			throw e ;
		}
		
		return head ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: createVHead</p> 
	 * <p>Description: 生成付款单凭证头信息 </p> 
	 * @param docBeans
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucher4DocService#createVHead(java.util.List, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public VHead createVHead(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception {
		VHead head = new VHead() ;
		
		try {
			JSONObject paramJo = new JSONObject(paramJson) ;
			
			String accDate = paramJo.getString("accDate") ; // 会计日期
			String exPayId = paramJo.getString("exPayId") ;	// 支付单ID
			String summary = paramJo.getString("summary") ;	// 摘要
			
			head.setLedgerCode(ledger.getLedgerCode());		// 账簿编码
			head.setPeriodCode(accDate.substring(0, 7));	// 会计期
			
			// 设置凭证来源编码
			head.setSrcCode(vs.getSrcCode());
			
			// 设置来源ID
			head.setSrcId(exPayId);	
			
			// 设置凭证分类
			if(vs.getvCat() == null || "".equals(vs.getvCat())){
				//throw new Exception("编码为【"+vs.getSrcCode()+"】凭证来源未设置凭证分类!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_01", null)+"【"+vs.getSrcCode()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_02", null));
			}
			String catKey = XCRedisKeys.getGlobalSegKey(XConstants.XC_GL_V_CATEGORY) ;
			Object catJson = redisDaoImpl.hashGet(catKey, vs.getvCat()) ;
			
			// 刷新缓存
			if(catJson == null){
				xcglCommonService.syncVCategory() ;
				catJson = redisDaoImpl.hashGet(catKey, vs.getvCat()) ;
				if(catJson == null) //throw new Exception("编码为【"+vs.getSrcCode()+"】凭证来源绑定的凭证分类在系统中不存在!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_03", null)+"【"+vs.getSrcCode()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_createVHead_04", null));
			}
			
			JSONObject jo = new JSONObject(String.valueOf(catJson)) ;
			head.setCategoryCode(jo.getString("code"));	
			
			// 计算附件数
			int totalAttachNum = 0 ;
			for(EXDocBean docBean : docBeans){
				totalAttachNum += docBean.getAttachNum() ;
			}
			head.setAttchTotal(totalAttachNum);		// 附件张数
			head.setSummary(summary);				// 摘要
			
			head.setSerialNum(null);				// 凭证号
			head.setTemplateType("1");				// 默认金额式凭证
			head.setVStatus("1");					// 凭证状态 
			head.setCreatedBy(CurrentSessionVar.getUserId());
			head.setCreationDate(java.sql.Date.valueOf(accDate));
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
		
		return head ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: createVDebitLines</p> 
	 * <p>Description: 生成付款单凭证借方分录信息 </p> 
	 * @param docBeans
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucher4DocService#createVDebitLines(java.util.List, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public List<VLine> createVDebitLines(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception {
		List<VLine> lines = new ArrayList<VLine>() ;
		
		try {
			JSONObject paramJo = new JSONObject(paramJson) ;
			
			String caId = paramJo.getString("caId") ;			// 现金流量项目
			String summary = paramJo.getString("summary") ;		// 摘要信息
			if(summary == null || "".equals(summary)){
				//throw new Exception("付款说明未填写,无法生成借方分录摘要") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_REPAY_DOC_DESC_IS_NULL_DR", null));
			}
			
			// 挂账科目
			String crAccId = ledger.getExCrAccId() ;
			if(crAccId == null){
				//throw new Exception("账簿【"+ledger.getLedgerName()+"】未设置挂账科目!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER_ACCOUNT_IS_NULL_02", null));
			}else{
				Account ldAcc = xcglCommonDAO.getLdAcountById(crAccId, ledger.getLedgerId()) ;
				if(ldAcc == null){
					//throw new Exception("账簿的挂账科目未分配到账簿下或已失效!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER_ACCOUNT_IS_INVALID_02", null));
				}
			}
			
			// 处理科目信息
			HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(), crAccId) ;
			String isCash = accMap.get("isCash") ;
			String accCode = accMap.get("accCode") ;
			
			// 处理现金流量项目(如果当前贷方科目为银行或现金科目则需要指定现金流量项目)
			String caCode = getCaCode(caId, isCash, accCode);
			
			// 计算凭证借方金额
			float accountDR = 0 ;
			for(EXDocBean docBean : docBeans){
				// 单据金额
				double amount = docBean.getAmount() ;
				// 核销金额
				double cancelAmt = docBean.getCancelAmt() ;
				// 本次支付金额
				double payAmt = amount - cancelAmt ;
				
				accountDR += payAmt ;
			}
			
			
			// 生成凭证分录信息
			VLine line = new VLine() ;
			line.setSummary(summary);
			line.setCcid(null);
			
			line.setAccCode(accCode);
			
			line.setVendorCode("00");
			line.setCustomerCode("00");
			line.setProdCode("00");
			line.setOrgCode("00");
			
			line.setEmpCode("00");
			line.setDeptCode("00");
			line.setPrjCode("00");
			
			line.setCust1Code("00");
			line.setCust2Code("00");
			line.setCust3Code("00");
			line.setCust4Code("00");
			line.setCaId(caId);
			line.setCaCode(caCode);
			line.setSrcDtlId(null);
			line.setOrderBy(0);
			line.setAmount(0);
			line.setExchangeRate(1);
			line.setEnterCR(0);
			line.setEnterDR(0);
			
			line.setAccountCR(0);
			line.setAccountDR(accountDR);
			
			lines.add(line) ;
						
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e ;
		}
	
		return lines ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: createVCreditLines</p> 
	 * <p>Description: 生成付款单凭证贷方分录信息 </p> 
	 * @param docBeans
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.impl.EXVoucher4DocCat#createVCreditLines(java.util.List, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public List<VLine> createVCreditLines(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception {
		List<VLine> lines = new ArrayList<VLine>() ;
		
		try {
			JSONObject paramJo = new JSONObject(paramJson) ;
			
			String payType = paramJo.getString("payType") ;		// 付款方式
			String bankId = paramJo.getString("bankId");
			String caId = paramJo.getString("caId") ;			// 现金流量项目
			String summary = paramJo.getString("summary") ;		// 摘要信息
			if(summary == null || "".equals(summary)){
				//throw new Exception("付款说明未填写,无法生成贷方分录摘要") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_REPAY_DOC_DESC_IS_NULL_CR", null));
			}
			
			// 获取付款凭证的贷方科目信息
			String crAccId = this.getPayAccId(payType, bankId, ledger.getLedgerId());
			
			// 处理科目信息
			HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(), crAccId) ;
			String isCash = accMap.get("isCash") ;
			String accCode = accMap.get("accCode") ;
			
			// 处理现金流量项目编码
			String caCode = getCaCode(caId, isCash, accCode);
			
			// 计算凭证贷方金额
			float accountCR = 0 ;
			for(EXDocBean docBean : docBeans){
				// 单据金额
				double amount = docBean.getAmount() ;
				// 核销金额
				double cancelAmt = docBean.getCancelAmt() ;
				// 本次支付金额
				double payAmt = amount - cancelAmt ;
				
				accountCR += payAmt ;
			}
			
			// 生成凭证分录信息
			VLine line = new VLine() ;
			line.setSummary(summary);
			line.setCcid(null);
			
			line.setAccCode(accCode);
			
			line.setVendorCode("00");
			line.setCustomerCode("00");
			line.setProdCode("00");
			line.setOrgCode("00");
			
			line.setEmpCode("00");
			line.setDeptCode("00");
			line.setPrjCode("00");
			
			line.setCust1Code("00");
			line.setCust2Code("00");
			line.setCust3Code("00");
			line.setCust4Code("00");
			
			line.setCaId(caId);
			line.setCaCode(caCode);
			line.setSrcDtlId(null);
			
			line.setOrderBy(0);
			line.setAmount(0);
			line.setExchangeRate(1);
			line.setEnterCR(0);
			line.setEnterDR(0);
			
			line.setAccountCR(accountCR);
			line.setAccountDR(0);
			
			lines.add(line) ;	
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}	
		
		// 返回处理
		return lines ;
	}

}
