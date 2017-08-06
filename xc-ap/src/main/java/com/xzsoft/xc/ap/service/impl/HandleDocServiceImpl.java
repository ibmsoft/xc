package com.xzsoft.xc.ap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.ap.service.ApVoucherGroupService;
import com.xzsoft.xc.apar.dao.impl.ApOrArVoucherDocCat;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VLine;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * 
  * @ClassName: HandleDocServiceImpl
  * @Description: 应付单生成凭证处理
  * @author 任建建
  * @date 2016年4月7日 上午11:25:02
 */
@Service("handleDocService")
public class HandleDocServiceImpl extends ApOrArVoucherDocCat implements ApVoucherGroupService{
	public static final Logger log = Logger.getLogger(HandleDocServiceImpl.class);
	@Resource
	private XCGLCommonDAO xcglCommonDAO;
	/*
	 * 
	  * <p>Title: mergeVoucher</p>
	  * <p>Description: 应付单组合凭证头信息</p>
	  * @param apDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherGroupService#mergeVoucher(com.xzsoft.xc.ap.modal.ApDocumentHBean, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public VHead mergeVoucher(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		VHead head = new VHead();
		try {
			head = this.createVHead(apDocumentBean,ledger,vs,paramJson,language);//凭证头信息
			List<VLine> dLines = this.createVDebitLines(apDocumentBean,ledger,vs,paramJson,language);//借方分录信息
			List<VLine> cLines = this.createVCreditLines(apDocumentBean,ledger,vs,paramJson,language);//贷方分录信息
			head = this.mergeVLines(head,dLines,cLines);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return head;
	}
	/*
	 * 
	  * <p>Title: createVHead</p>
	  * <p>Description: 生成凭证头信息</p>
	  * @param apDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.impl.APVoucherDocCat#createVHead(com.xzsoft.xc.ap.modal.ApDocumentHBean, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public VHead createVHead(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		return super.createVHead(apDocumentBean,ledger,vs,paramJson,language);
	}
	/*
	 * 
	  * <p>Title: createVDebitLines</p>
	  * <p>Description: 生成凭证借方分录信息</p>
	  * @param apDocumentBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherGroupService#createVDebitLines(com.xzsoft.xc.ap.modal.ApDocumentHBean, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public List<VLine> createVDebitLines(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		List<VLine> lines = new ArrayList<VLine>();
		try {
			//供应商编码
			String vendorCode = getVendorCode(apDocumentBean,ledger,language);
			//成本中心编码
			String deptCode = getDeptCode(apDocumentBean,ledger);
			//项目编码
			String prjCode = getPrjCode(apDocumentBean,ledger);
			//个人往来编码
			String empCode = getEmpCode(apDocumentBean);
			List<ApDocumentLBean> apDocumentLists = apDocumentBean.getApDocumentLBean();
			if(apDocumentLists != null && apDocumentLists.size() > 0){
				for(ApDocumentLBean apDocumentLBean : apDocumentLists){
					String summary = apDocumentLBean.getDESCRIPTION();//摘要信息
					if(summary == null || "".equals(summary)){
						throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DESC_IS_NULL_DR", null));//应付单说明未填写，无法生成借方分录摘要
					}
					//处理科目信息
					HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(),apDocumentLBean.getACC_ID());
					String accCode = accMap.get("accCode");
					//生成凭证分录信息
					VLine line = new VLine();
					line.setSummary(summary);
					line.setCcid(apDocumentLBean.getCCID());
					
					line.setAccCode(accCode);
					
					line.setVendorCode(vendorCode);
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
					
					
					line.setAccountCR(0);
					line.setAccountDR(apDocumentLBean.getAMOUNT());
					
					line.setCreatedBy(CurrentUserUtil.getCurrentUserId());
					line.setCreationDate(CurrentUserUtil.getCurrentDate());
					line.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					line.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					lines.add(line);
				}
				
				//生成凭证分录信息
				String summary = apDocumentBean.getDESCRIPTION();
				if(apDocumentBean.getAP_ACC_ID_TAX() !=null && !"".equals(apDocumentBean.getAP_ACC_ID_TAX()) && !"".equals(apDocumentBean.getTAX_AMT())){
					//科目信息
					HashMap<String,String> accMap = getAccMap(ledger.getAccHrcyId(), apDocumentBean.getAP_ACC_ID_TAX());
					String accTaxCode = accMap.get("accCode");
					
					VLine line = new VLine();
					line.setSummary(summary);
					line.setCcid(null);
					
					line.setAccCode(accTaxCode);
					
					line.setVendorCode(vendorCode);
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
					
					line.setAccountCR(0);
					line.setAccountDR(apDocumentBean.getTAX_AMT());
					
					line.setCreatedBy(CurrentUserUtil.getCurrentUserId());
					line.setCreationDate(CurrentUserUtil.getCurrentDate());
					line.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					line.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					lines.add(line);
				}
			}
			else{
				throw new Exception(XipUtil.getMessage(language, "XC_AP_DTL_IS_NULL", null));//未录入单据明细信息，无法生成借方分录行信息！
			}
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}	
		//返回处理
		return lines;
	}
	/*
	 * 
	  * <p>Title: createVCreditLines</p>
	  * <p>Description: 生成凭证贷方分录信息</p>
	  * @param apDocumentBeans 	单据主表信息
	  * @param ledger			账簿信息
	  * @param vs				凭证来源
	  * @param paramJson		参数
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherGroupService#createVCreditLines(java.util.List,com.xzsoft.xc.gl.modal.Ledger,com.xzsoft.xc.gl.modal.VSource,java.lang.String)
	 */
	@Override
	public List<VLine> createVCreditLines(ApDocumentHBean apDocumentBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		
		List<VLine> lines = new ArrayList<VLine>();
		try {
			//获取分录行摘要信息
			String summary = apDocumentBean.getDESCRIPTION();
			//获取分录行摘要信息
			if(summary == null || "".equals(summary)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DESC_IS_NULL_CR", null));//应付单说明未填写，无法生成借方分录摘要
			}
			//科目信息
			HashMap<String,String> accMap = getAccMap(ledger.getAccHrcyId(), apDocumentBean.getAP_ACC_ID_DEBT());
			String accCode = accMap.get("accCode");
			//供应商编码
			String vendorCode = getVendorCode(apDocumentBean,ledger,language);
			//成本中心编码
			String deptCode = getDeptCode(apDocumentBean,ledger);
			//项目编码
			String prjCode = getPrjCode(apDocumentBean,ledger);
			//个人往来编码
			String empCode = getEmpCode(apDocumentBean);
			
			//生成凭证分录信息
			VLine line = new VLine();
			line.setSummary(summary);
			line.setCcid(null);
			
			line.setAccCode(accCode);
			
			line.setVendorCode(vendorCode);
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
			
			line.setAccountCR(apDocumentBean.getINV_AMOUNT());
			line.setAccountDR(0);
			
			line.setCreatedBy(CurrentUserUtil.getCurrentUserId());
			line.setCreationDate(apDocumentBean.getGL_DATE());
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