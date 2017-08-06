package com.xzsoft.xc.ex.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.service.EXVoucher4DocService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.util.XipUtil;

/**
 * @ClassName: RepaymentDocServiceImpl 
 * @Description: 还款单凭证处理
 * @author linp
 * @date 2016年4月1日 上午9:32:15 
 *
 */
@Service("repaymentDocService")
public class RepaymentDocServiceImpl extends EXVoucher4DocCat implements EXVoucher4DocService {
	// 日志记录器
	private static final Logger log = Logger.getLogger(RepaymentDocServiceImpl.class.getName()) ;

	@Resource
	private XCGLCommonDAO xcglCommonDAO ;
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: mergeVoucher</p> 
	 * <p>Description: 组合凭证信息 </p> 
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucher4DocService#mergeVoucher()
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
	 * <p>Description: 生成还款单凭证头信息 </p> 
	 * @param docBeans
	 * @param ledger
	 * @param vs
	 * @param paramJson
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.impl.EXVoucher4DocCat#createVHead(java.util.List, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public VHead createVHead(List<EXDocBean> docBeans,Ledger ledger,VSource vs,String paramJson) throws Exception {
		return super.createVHead(docBeans.get(0), ledger, vs, paramJson) ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: createVDebitLines</p> 
	 * <p>Description: 生成还款单凭证借方分录信息 </p> 
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
			// 辅助参数
			JSONObject paramJo = new JSONObject(paramJson) ;
			
			String payMethod = paramJo.getString("payMethod") ;	// 支付方式
			String payType = paramJo.getString("payType") ;		// 付款方式
			String bankId = paramJo.getString("bankId") ;		// 银行账号
			String caId = paramJo.getString("caId") ;			// 现金流量项目
			
			// 报销凭证每次只针对一个单据执行复核处理
			EXDocBean docBean = docBeans.get(0) ;
			
			// 获取分录行摘要信息
			String summary = docBean.getExReason(); 
			if(summary == null || "".equals(summary)){
				//throw new Exception("还款单【"+docBean.getDocCode()+"】未填写还款说明,无法生成借方分录摘要") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PAY_DOC_01", null)+"【"+docBean.getDocCode()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PAY_DOC_DESC_IS_NULL_DR", null));
			}
			
			// 获取报销凭证贷方科目信息
			String crAccId = null ;
			if("1".equals(payMethod)){	// 立即支付
				
				// 如果支付方式为立即支付, 根据付款方式或付款账号所绑定的科目生成贷方分录
				crAccId = this.getPayAccId(payType, bankId, ledger.getLedgerId());
				
			}else if("2".equals(payMethod)){ // 挂账支付
				
				//  如果支付方式为挂账支付, 根据挂账科目生成贷方分录
				crAccId  = ledger.getExCrAccId() ;
				if(crAccId == null){
					//throw new Exception("账簿【"+ledger.getLedgerName()+"】未设置挂账科目!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER_ACCOUNT_IS_NULL_02", null));
				}else{
					Account ldAcc = xcglCommonDAO.getLdAcountById(crAccId, ledger.getLedgerId()) ;
					if(ldAcc == null){
						//throw new Exception("账簿的挂账科目未分配到当前账簿或已失效") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER_ACCOUNT_IS_INVALID_02", null));
					}
				}
				
			}else{ // 其他支付方式
			}
			
			// 处理科目信息
			HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(), crAccId) ;
			String isCash = accMap.get("isCash") ;
			String accCode = accMap.get("accCode") ;
			
			// 成本中心编码
			String deptCode = getDeptCode(docBean, ledger);
			
			// 项目编码
			String prjCode = getPrjCode(docBean, ledger);
			
			// 个人往来编码
			String empCode = getEmpCode(docBean);
			
			// 处理现金流量项目(如果当前贷方科目为银行或现金科目则需要指定现金流量项目)
			String caCode = getCaCode(caId, isCash, accCode);
			
			// 处理凭证贷方金额
			double accountDR = docBean.getAmount() ;
			
			// 生成凭证分录信息
			VLine line = new VLine() ;
			line.setSummary(summary);
			line.setCcid(null);
			
			line.setAccCode(accCode);
			
			line.setVendorCode("00");
			line.setCustomerCode("00");
			line.setProdCode("00");
			line.setOrgCode("00");
			
			line.setEmpCode(empCode);
			line.setDeptCode(deptCode);
			line.setPrjCode(prjCode);
			
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
		
		// 返回处理
		return lines ;

	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: createVCreditLines</p> 
	 * <p>Description: 生成还款单凭证贷方分录信息 </p> 
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
			// 报销单只对单个单据进行复核处理
			EXDocBean docBean = docBeans.get(0) ;
			
			// 获取分录行摘要信息
			String summary = docBean.getExReason(); 
			if(summary == null || "".equals(summary)){
				//throw new Exception("还款单【"+docBean.getDocCode()+"】未填写还款说明,无法生成贷方分录摘要") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PAY_DOC_01", null)+"【"+docBean.getDocCode()+"】"+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PAY_DOC_DESC_IS_NULL_CR", null));
			}
			
			// 成本中心编码
			String deptCode = getDeptCode(docBean, ledger);
			// 项目编码
			String prjCode = getPrjCode(docBean, ledger);
			// 个人往来编码
			String empCode = getEmpCode(docBean);
			
			// 如果单据为借款单或还款单, 则采用备用金科目生成借方分录
			String drAccId = ledger.getExIaAccId() ;
			if(drAccId == null) {
				//throw new Exception("账簿未设置备用金科目,请确认!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER_ACCOUNT_IS_NULL_01", null));
			}
			
			// 处理科目
			HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(), drAccId) ;
			String accCode = accMap.get("accCode") ;
			String isCash = accMap.get("isCash") ;	//是否现金或银行科目
			if("Y".equals(isCash)){
				//throw new Exception("备用金科目为现金或银行科目,系统未指定现金流量项目!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEND_ACCOUNT_IS_BANKACCOUNT_CR", null));
			}
			
			// 处理凭证借方金额
			double accountDR = docBean.getAmount() ;
			
			// 生成凭证分录信息
			VLine line = new VLine() ;
			line.setSummary(summary);
			line.setCcid(null);
			
			line.setAccCode(accCode);
			
			line.setVendorCode("00");
			line.setCustomerCode("00");
			line.setProdCode("00");
			line.setOrgCode("00");
			
			line.setEmpCode(empCode);
			line.setDeptCode(deptCode);
			line.setPrjCode(prjCode);
			
			line.setCust1Code("00");
			line.setCust2Code("00");
			line.setCust3Code("00");
			line.setCust4Code("00");
			line.setCaId(null);
			line.setCaCode("00");
			line.setSrcDtlId(null);
			line.setOrderBy(0);
			line.setAmount(0);
			line.setExchangeRate(1);
			line.setEnterCR(0);
			line.setEnterDR(0);
			
			line.setAccountCR(accountDR);
			line.setAccountDR(0);
			
			lines.add(line) ;
			
		} catch (Exception e) {
			throw e ;
		}
		
		return lines ;
	}

}
