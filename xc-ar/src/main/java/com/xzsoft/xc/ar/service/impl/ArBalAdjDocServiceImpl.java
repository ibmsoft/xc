package com.xzsoft.xc.ar.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.apar.dao.impl.ApOrArVoucherDocCat;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.service.ArVoucherGroupService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.Account;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * 
  * @ClassName ArCancelDocServiceImpl
  * @Description 余额调整单生成凭证处理
  * @author RenJianJian
  * @date 2016年7月8日 下午5:01:05
 */
@Service("arBalAdjDocService")
public class ArBalAdjDocServiceImpl extends ApOrArVoucherDocCat implements ArVoucherGroupService{
	public static final Logger log = Logger.getLogger(ArBalAdjDocServiceImpl.class);
	@Resource
	private XCGLCommonDAO xcglCommonDAO;
	@Resource
	private XCARCommonDao xcarCommonDao;
	/*
	 * 
	  * <p>Title mergeVoucher</p>
	  * <p>Description 余额调整单组合凭证头信息</p>
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArVoucherGroupService#mergeVoucher(com.xzsoft.xc.apar.modal.ArDocumentHBean, com.xzsoft.xc.util.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String, java.lang.String)
	 */
	@Override
	public VHead mergeVoucher(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		VHead head = new VHead();
		try {
			head = this.createVHead(arDocumentBean,ledger,vs,paramJson,language);//凭证头信息
			List<VLine> dLines = this.createVDebitLines(arDocumentBean,ledger,vs,paramJson,language);//借方分录信息
			List<VLine> cLines = this.createVCreditLines(arDocumentBean,ledger,vs,paramJson,language);//贷方分录信息
			head = this.mergeVLines(head,dLines,cLines);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return head;
	}
	/*
	 * 
	  * <p>Title createVHead</p>
	  * <p>Description 生成凭证头信息</p>
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArVoucherGroupService#createVHead(com.xzsoft.xc.apar.modal.ArDocumentHBean, com.xzsoft.xc.util.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String, java.lang.String)
	 */
	@Override
	public VHead createVHead(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		return super.createVHead(arDocumentBean,ledger,vs,paramJson);
	}
	/*
	 * 
	  * <p>Title createVDebitLines</p>
	  * <p>Description 生成凭证借方分录信息</p>
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArVoucherGroupService#createVDebitLines(com.xzsoft.xc.apar.modal.ArDocumentHBean, com.xzsoft.xc.util.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String, java.lang.String)
	 */
	@Override
	public List<VLine> createVDebitLines(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		List<VLine> lines = new ArrayList<VLine>();
		try {
			//获取分录行摘要信息
			String summary = arDocumentBean.getDESCRIPTION(); 
			if(summary == null || "".equals(summary)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_ADJ_DESC_IS_NULL_DR", null));//余额调整说明未填写,无法生成借方分录摘要！
			}
			//客户编码
			String customerCode = getCustomerCode(arDocumentBean,ledger,language);
			if(arDocumentBean.getTO_CCID() == null || "".equals(arDocumentBean.getTO_CCID()))
				throw new Exception(XipUtil.getMessage(language, "XC_AP_ADJ_ACCOUNT_IS_NULL", null));//调整科目不能为空，请选择调整科目！
			//通过科目组合ID找到科目ID
			String accId = xcarCommonDao.getAccId(arDocumentBean.getTO_CCID());
			//科目编码
			Account account = xcglCommonDAO.getAccountById(accId);
			String accCode = account.getAccCode();
			//处理凭证借方金额（调整金额）
			double accountDR = arDocumentBean.getADJ_AMT();
			if(accountDR == 0){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_ADJ_AMOUNT_IS_NULL_DR", null));//调整余额为0，无法生成借方金额！
			}
			//生成凭证分录信息
			VLine line = new VLine();
			line.setSummary(summary);
			line.setCcid(null);
			
			line.setAccCode(accCode);
			
			line.setVendorCode("00");
			line.setCustomerCode(customerCode);
			line.setProdCode("00");
			line.setOrgCode("00");

			line.setEmpCode("00");
			line.setDeptCode("00");
			line.setPrjCode("00");
			
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
			
			line.setCreatedBy(CurrentUserUtil.getCurrentUserId());
			line.setCreationDate(CurrentUserUtil.getCurrentDate());
			line.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
			line.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
			lines.add(line);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}	
		//返回处理
		return lines;
	}
	/*
	 * 
	  * <p>Title createVCreditLines</p>
	  * <p>Description 生成凭证贷方分录信息</p>
	  * @param arDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArVoucherGroupService#createVCreditLines(com.xzsoft.xc.apar.modal.ArDocumentHBean, com.xzsoft.xc.util.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String, java.lang.String)
	 */
	@Override
	public List<VLine> createVCreditLines(ArDocumentHBean arDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		
		List<VLine> lines = new ArrayList<VLine>();
		try {
			// 获取分录行摘要信息
			String summary = arDocumentBean.getDESCRIPTION(); 
			if(summary == null || "".equals(summary)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_ADJ_DESC_IS_NULL_CR", null));//余额调整说明未填写,无法生成贷方分录摘要！
			}
			//通过应收单号查找应收单中的客户、科目等信息信息
			ArDocumentHBean arInvGlH = xcarCommonDao.getArInvGlH(arDocumentBean.getAR_INV_GL_H_ID());
			//客户编码
			String customerCode = getCustomerCode(arDocumentBean,ledger,language);
			//处理科目信息
			HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(),arInvGlH.getAR_ACC_ID_DEBT());
			String accCode = accMap.get("accCode");
			//处理凭证贷方金额
			double accountCR = arDocumentBean.getADJ_AMT();
			if(accountCR == 0){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_ADJ_AMOUNT_IS_NULL_CR", null));//调整余额为0，无法生成贷方金额！
			}
			//生成凭证分录信息
			VLine line = new VLine();
			line.setSummary(summary);
			line.setCcid(null);
			
			line.setAccCode(accCode);
			
			line.setVendorCode("00");
			line.setCustomerCode(customerCode);
			line.setProdCode("00");
			line.setOrgCode("00");

			line.setEmpCode("00");
			line.setDeptCode("00");
			line.setPrjCode("00");
			
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
			
			line.setAccountCR(0);
			line.setAccountDR(accountCR);
			
			line.setCreatedBy(CurrentUserUtil.getCurrentUserId());
			line.setCreationDate(CurrentUserUtil.getCurrentDate());
			line.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
			line.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
			lines.add(line);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return lines;
	}
}