package com.xzsoft.xc.ap.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.service.ApVoucherGroupService;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.dao.ApArDao;
import com.xzsoft.xc.apar.dao.impl.ApOrArVoucherDocCat;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
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
  * @ClassName: VerificationDocServiceImpl
  * @Description: 核销单生成凭证处理
  * @author 任建建
  * @date 2016年4月7日 下午11:43:48
 */
@Service("verificationDocService")
public class VerificationDocServiceImpl extends ApOrArVoucherDocCat implements ApVoucherGroupService{
	public static final Logger log = Logger.getLogger(HandleDocServiceImpl.class);
	@Resource
	private XCGLCommonDAO xcglCommonDAO;
	@Resource
	private XCAPCommonDao xcapCommonDao;
	@Resource
	private ApArDao aparDao;
	/*
	 * 
	  * <p>Title: mergeVoucher</p>
	  * <p>Description: 核销单组合凭证头信息</p>
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
			List<VLine> lLines = this.createVDebitLines(apDocumentBean,ledger,vs,paramJson,language);//行分录信息
			List<VLine> hLines = this.createVCreditLines(apDocumentBean,ledger,vs,paramJson,language);//头分录信息
			head = this.mergeVLines(head,lLines,hLines);
			
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
	  * <p>Description: 生成凭证借方分录信息(行)</p>
	  * @param apDocumentBeans 	单据主表信息
	  * @param ledger			账簿信息
	  * @param vs				凭证来源
	  * @param paramJson		参数
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherGroupService#createVDebitLines(java.util.List,com.xzsoft.xc.gl.modal.Ledger,com.xzsoft.xc.gl.modal.VSource,java.lang.String)
	 */
	@Override
	public List<VLine> createVDebitLines(ApDocumentHBean apDocumentHBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		List<VLine> lines = new ArrayList<VLine>();
		try {
			String apCancelType = apDocumentHBean.getAP_CANCEL_TYPE();	//核销单类型
			List<ApDocumentLBean> apDocumentLists = apDocumentHBean.getApDocumentLBean();
			
			if(apDocumentLists != null && apDocumentLists.size() > 0){
				for(ApDocumentLBean apDocumentLBean : apDocumentLists){
					VLine line = new VLine();//生成凭证分录信息
					//通过核销源单号查找应付单（付款单）中的供应商、科目等信息
					ApDocumentHBean apBean = null;
					ArDocumentHBean arBean = null;
					String summary = null;		//摘要信息
					String accId = null;		//科目信息
					if(XCAPConstants.HXD_YFHYUF.equals(apCancelType)){//应付核预付（对应的明细是付款单）
						apBean = xcapCommonDao.getApPayH(apDocumentLBean.getTARGET_ID());
						summary = apBean.getDESCRIPTION();
						//通过付款单ID得到付款单明细中的科目信息
						List<ApDocumentLBean> apInvPayLLists = xcapCommonDao.getApPayL(apDocumentLBean.getTARGET_ID());
						for(int i = 0;i < apInvPayLLists.size();i++){
							accId = apInvPayLLists.get(i).getACC_ID();
						}
						line.setAccountCR(apDocumentLBean.getTARGET_AMT());
						line.setAccountDR(0);
					}
					if(XCAPConstants.HXD_YFHYS.equals(apCancelType)){//应付核应收
						arBean = aparDao.getArInvGlH(apDocumentLBean.getTARGET_ID());
						summary = arBean.getDESCRIPTION();
						accId = arBean.getAR_ACC_ID_DEBT();
						line.setAccountCR(apDocumentLBean.getTARGET_AMT());
						line.setAccountDR(0);
					}
					if(XCAPConstants.HXD_YFHLDC.equals(apCancelType)){//应付红蓝对冲（对应的明细是应付单）
						apBean = xcapCommonDao.getApInvGlH(apDocumentLBean.getTARGET_ID());
						summary = apBean.getDESCRIPTION();
						accId = apBean.getAP_ACC_ID_DEBT();
						line.setAccountCR(Math.abs(apDocumentLBean.getTARGET_AMT())*(-1));
						line.setAccountDR(0);
					}
					if(XCAPConstants.HXD_YUFHLDC.equals(apCancelType)){//预付红蓝对冲（对应的明细是付款单）
						apBean = xcapCommonDao.getApPayH(apDocumentLBean.getTARGET_ID());
						summary = apBean.getDESCRIPTION();
						accId = apBean.getPAY_ACC_ID();
						line.setAccountCR(0);
						line.setAccountDR(Math.abs(apDocumentLBean.getTARGET_AMT())*(-1));
					}
					if(summary == null || "".equals(summary)){
						throw new Exception(XipUtil.getMessage(language, "XC_AP_CANCEL_DESC_IS_NULL_DR", null));//核销说明未填写,无法生成借方分录摘要
					}
					
					//供应商编码
					String vendorCode = "";
					//成本中心编码
					String deptCode = "";
					//项目编码
					String prjCode = "";
					//个人往来编码
					String empCode = "";
					//客户编码
					String customerCode = "";
					
					if (XCAPConstants.HXD_YFHYS.equals(apCancelType)) {
						customerCode = getCustomerCode(arBean, ledger,language);
						deptCode = getDeptCode(arBean,ledger);
						prjCode = getPrjCode(arBean,ledger);
						
					}else{
						vendorCode = getVendorCode(apBean,ledger,language);
						deptCode = getDeptCode(apBean,ledger);
						prjCode = getPrjCode(apBean,ledger);
					}
					
					empCode = getEmpCode(apDocumentHBean);
					
					//处理科目信息
					HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(),accId);
					String accCode = accMap.get("accCode");
					//生成凭证分录信息
					line.setSummary(summary);
					line.setCcid(null);
					
					line.setAccCode(accCode);
					
					if (XCAPConstants.HXD_YFHYS.equals(apCancelType)) {
						line.setVendorCode("00");
						line.setCustomerCode(customerCode);
					}else{
						line.setVendorCode(vendorCode);
						line.setCustomerCode("00");
					}
					
					
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
		// 返回处理
		return lines;
		
	}
	/*
	 * 
	  * <p>Title: createVCreditLines</p>
	  * <p>Description: 生成凭证贷方分录信息（头）</p>
	  * @param apDocumentBeans 	单据主表信息
	  * @param ledger			账簿信息
	  * @param vs				凭证来源
	  * @param paramJson		参数
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherGroupService#createVCreditLines(java.util.List,com.xzsoft.xc.gl.modal.Ledger,com.xzsoft.xc.gl.modal.VSource,java.lang.String)
	 */
	@Override
	public List<VLine> createVCreditLines(ApDocumentHBean apDocumentHBean,Ledger ledger,VSource vs,String paramJson,String language) throws Exception {
		List<VLine> lines = new ArrayList<VLine>();
		try {
			VLine line = new VLine();//生成凭证分录信息
			//获取分录行摘要信息
			String summary = apDocumentHBean.getDESCRIPTION(); 
			if(summary == null || "".equals(summary)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_CANCEL_DESC_IS_NULL_CR", null));//核销说明未填写,无法生成贷方分录摘要！
			}
			String apCancelType = apDocumentHBean.getAP_CANCEL_TYPE();	//核销单类型
			//通过核销源单号查找应付单（付款单）中的供应商、科目等信息
			ApDocumentHBean apBean = null;
			String accId = null;		//科目信息
			if(XCAPConstants.HXD_YFHYUF.equals(apCancelType)){//应付核预付（对应的来源ID是应付单中的）
				apBean = xcapCommonDao.getApInvGlH(apDocumentHBean.getSRC_ID());
				accId = apBean.getAP_ACC_ID_DEBT();
				line.setAccountCR(0);
				line.setAccountDR(apDocumentHBean.getSRC_AMT());
			}
			if(XCAPConstants.HXD_YFHYS.equals(apCancelType)){//应付核应收
				apBean = xcapCommonDao.getApInvGlH(apDocumentHBean.getSRC_ID());
				accId = apBean.getAP_ACC_ID_DEBT();
				line.setAccountCR(0);
				line.setAccountDR(apDocumentHBean.getSRC_AMT());
			}
			if(XCAPConstants.HXD_YFHLDC.equals(apCancelType)){//应付红蓝对冲（对应的来源ID是应付单中的）
				apBean = xcapCommonDao.getApInvGlH(apDocumentHBean.getSRC_ID());
				accId = apBean.getAP_ACC_ID_DEBT();
				line.setAccountCR(apDocumentHBean.getSRC_AMT());
				line.setAccountDR(0);
			}
			if(XCAPConstants.HXD_YUFHLDC.equals(apCancelType)){//预付红蓝对冲（对应的来源ID是付款中中的）
				apBean = xcapCommonDao.getApPayH(apDocumentHBean.getSRC_ID());
				accId = apBean.getPAY_ACC_ID();
				line.setAccountCR(0);
				line.setAccountDR(apDocumentHBean.getSRC_AMT());
			}
			//供应商编码
			String vendorCode = getVendorCode(apBean,ledger,language);
			//成本中心编码
			String deptCode = getDeptCode(apBean,ledger);
			//项目编码
			String prjCode = getPrjCode(apBean,ledger);
			//个人往来编码
			String empCode = getEmpCode(apBean);
			//科目编码
			Account account = xcglCommonDAO.getAccountById(accId);
			String accCode = account.getAccCode();
			//处理凭证贷方金额
			//double accountDR = apDocumentHBean.getSRC_AMT();
			
			
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