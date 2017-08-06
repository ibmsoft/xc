package com.xzsoft.xc.ar.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.apar.dao.impl.ApOrArVoucherDocCat;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentLBean;
import com.xzsoft.xc.ar.service.ArVoucherGroupService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * 
  * @ClassName ReceivableDocServiceImpl
  * @Description 应收单生成凭证处理
  * @author 任建建
  * @date 2016年7月8日 下午3:36:28
 */
@Service("receivableDocService")
public class ReceivableDocServiceImpl extends ApOrArVoucherDocCat implements ArVoucherGroupService{
	public static final Logger log = Logger.getLogger(ReceivableDocServiceImpl.class);
	@Resource
	private XCGLCommonDAO xcglCommonDAO;
	/*
	 * 
	  * <p>Title mergeVoucher</p>
	  * <p>Description 应收单组合凭证头信息</p>
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
			//获取分录行摘要信息
			if(summary == null || "".equals(summary)){
				throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DESC_IS_NULL_DR", null));//应收单说明未填写，无法生成借方分录摘要！
			}
			//科目信息
			HashMap<String,String> accMap = getAccMap(ledger.getAccHrcyId(), arDocumentBean.getAR_ACC_ID_DEBT());
			String accCode = accMap.get("accCode");
			//客户编码
			String customerCode = getCustomerCode(arDocumentBean,ledger,language);
			//成本中心编码
			String deptCode = getDeptCode(arDocumentBean,ledger);
			//项目编码
			String prjCode = getPrjCode(arDocumentBean,ledger);
			//个人往来编码
			String empCode = getEmpCode(arDocumentBean);
			
			//生成凭证分录信息
			VLine line = new VLine();
			line.setSummary(summary);
			line.setCcid(null);
			
			line.setAccCode(accCode);
			
			line.setVendorCode("00");
			line.setCustomerCode(customerCode);
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
			
			line.setAccountCR(0);
			line.setAccountDR(arDocumentBean.getINV_AMOUNT());
			
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
			//客户编码
			String customerCode = getCustomerCode(arDocumentBean,ledger,language);
			//成本中心编码
			String deptCode = getDeptCode(arDocumentBean,ledger);
			//项目编码
			String prjCode = getPrjCode(arDocumentBean,ledger);
			//个人往来编码
			String empCode = getEmpCode(arDocumentBean);
			List<ArDocumentLBean> arDocumentLists = arDocumentBean.getAddDocument();
			if(arDocumentLists != null && arDocumentLists.size() > 0){
				for(ArDocumentLBean arDocumentLBean : arDocumentLists){
					String summary = arDocumentLBean.getDESCRIPTION();//摘要信息
					if(summary == null || "".equals(summary)){
						throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DESC_IS_NULL_CR", null));//应收单明细说明未填写，无法生成贷方分录摘要！
					}
					//处理科目信息
					HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(),arDocumentLBean.getACC_ID());
					String accCode = accMap.get("accCode");
					//生成凭证分录信息
					VLine line = new VLine();
					line.setSummary(summary);
					line.setCcid(arDocumentLBean.getCCID());
					
					line.setAccCode(accCode);
					
					line.setVendorCode("00");
					line.setCustomerCode(customerCode);
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
					
					
					line.setAccountCR(arDocumentLBean.getAMOUNT());
					line.setAccountDR(0);
					
					line.setCreatedBy(CurrentUserUtil.getCurrentUserId());
					line.setCreationDate(CurrentUserUtil.getCurrentDate());
					line.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					line.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					lines.add(line);
				}
				
				//生成凭证分录信息
				String summary = arDocumentBean.getDESCRIPTION();
				
				if(arDocumentBean.getAR_ACC_ID_TAX() !=null && !"".equals(arDocumentBean.getAR_ACC_ID_TAX()) && !"".equals(arDocumentBean.getTAX_AMT())){
					//科目信息
					HashMap<String,String> accMap = getAccMap(ledger.getAccHrcyId(), arDocumentBean.getAR_ACC_ID_TAX());
					String accTaxCode = accMap.get("accCode");
					
					VLine line = new VLine();
					line.setSummary(summary);
					line.setCcid(null);
					
					line.setAccCode(accTaxCode);

					line.setVendorCode("00");
					line.setCustomerCode(customerCode);
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
					
					line.setAccountCR(arDocumentBean.getTAX_AMT());
					line.setAccountDR(0);
					
					line.setCreatedBy(CurrentUserUtil.getCurrentUserId());
					line.setCreationDate(CurrentUserUtil.getCurrentDate());
					line.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					line.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					lines.add(line);
				}
			}
			else{
				throw new Exception(XipUtil.getMessage(language, "XC_AR_DTL_IS_NULL", null));//未录入单据明细信息，无法生成贷方分录行信息！
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}	
		//返回处理
		return lines;
	}
}