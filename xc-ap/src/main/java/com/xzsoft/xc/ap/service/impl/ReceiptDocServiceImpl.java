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
  * @ClassName: ReceiptDocServiceImpl
  * @Description: 付款单生成凭证处理
  * @author 任建建
  * @date 2016年4月7日 上午11:17:32
 */
@Service("receiptDocService")
public class ReceiptDocServiceImpl extends ApOrArVoucherDocCat implements ApVoucherGroupService{
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
		try {System.out.println();
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
	  * @param apDocumentHBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherGroupService#createVDebitLines(com.xzsoft.xc.ap.modal.ApDocumentHBean, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public List<VLine> createVDebitLines(ApDocumentHBean apDocumentHBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		List<VLine> lines = new ArrayList<VLine>();
		
		try {
			//供应商编码
			String vendorCode = getVendorCode(apDocumentHBean,ledger,language);
			//成本中心编码
			String deptCode = getDeptCode(apDocumentHBean,ledger);
			//项目编码
			String prjCode = getPrjCode(apDocumentHBean,ledger);
			//个人往来编码
			String empCode = getEmpCode(apDocumentHBean);
			List<ApDocumentLBean> apDocumentLists = apDocumentHBean.getApDocumentLBean();
			if(apDocumentLists != null && apDocumentLists.size() > 0){
				for(ApDocumentLBean apDocumentLBean : apDocumentLists){
					String summary = apDocumentLBean.getDESCRIPTION();	//摘要信息
					if(summary == null || "".equals(summary)){
						throw new Exception(XipUtil.getMessage(language, "XC_AP_PAY_DESC_IS_NULL_DR", null));//付款说明未填写,无法生成借方分录摘要
					}
					//处理科目信息
					HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(),apDocumentLBean.getACC_ID());
					//生成凭证分录信息
					VLine line = new VLine();
					line.setSummary(summary);
					line.setCcid(null);
					
					line.setAccCode(accMap.get("accCode"));
					
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
					line.setCaCode(null);
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
	  * @param apDocumentHBean
	  * @param ledger
	  * @param vs
	  * @param paramJson
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherGroupService#createVCreditLines(com.xzsoft.xc.ap.modal.ApDocumentHBean, com.xzsoft.xc.gl.modal.Ledger, com.xzsoft.xc.gl.modal.VSource, java.lang.String)
	 */
	@Override
	public List<VLine> createVCreditLines(ApDocumentHBean apDocumentHBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		List<VLine> lines = new ArrayList<VLine>();
		try {
			String caId = apDocumentHBean.getCA_ID();				//现金流量项目
			String summary = apDocumentHBean.getDESCRIPTION();		//摘要信息
			if(summary == null || "".equals(summary)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_PAY_DESC_IS_NULL_CR", null));//付款说明未填写,无法生成贷方分录摘要
			}
			//处理科目信息
			HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(),apDocumentHBean.getPAY_ACC_ID());
			String isCash = accMap.get("isCash");
			String accCode = accMap.get("accCode");
			//处理现金流量项目编码
			String caCode = getCaCode(caId,isCash,accCode,language);

			//供应商编码
			String vendorCode = getVendorCode(apDocumentHBean,ledger,language);
			//成本中心编码
			String deptCode = getDeptCode(apDocumentHBean,ledger);
			//项目编码
			String prjCode = getPrjCode(apDocumentHBean,ledger);
			//个人往来编码
			String empCode = getEmpCode(apDocumentHBean);
			//计算凭证贷方金额
			double accountDR = apDocumentHBean.getAMOUNT();

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
			line.setCaId(caId);
			line.setCaCode(caCode);
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
		return lines;
	}
}
