package com.xzsoft.xc.ar.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.apar.dao.ApArDao;
import com.xzsoft.xc.apar.dao.impl.ApOrArVoucherDocCat;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentLBean;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.service.ArVoucherGroupService;
import com.xzsoft.xc.ar.util.XCARConstants;
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
  * @Description 核销单生成凭证处理
  * @author RenJianJian
  * @date 2016年7月8日 下午5:01:05
 */
@Service("arCancelDocService")
public class ArCancelDocServiceImpl extends ApOrArVoucherDocCat implements ArVoucherGroupService{
	public static final Logger log = Logger.getLogger(ArCancelDocServiceImpl.class);
	@Resource
	private XCGLCommonDAO xcglCommonDAO;
	@Resource
	private XCARCommonDao xcarCommonDao;
	@Resource
	private ApArDao apArDao;
	/*
	 * 
	  * <p>Title mergeVoucher</p>
	  * <p>Description 核销单组合凭证头信息</p>
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
			String arCancelType = arDocumentBean.getAR_CANCEL_TYPE();	//核销单类型
			List<ArDocumentLBean> arDocumentLists = arDocumentBean.getAddDocument();
			if(arDocumentLists != null && arDocumentLists.size() > 0){
				for(ArDocumentLBean arDocumentLBean : arDocumentLists){
					//通过核销源单号查找应收单（收款单）中的供应商、科目等信息
					ArDocumentHBean arBean = null;
					ApDocumentHBean apBean = null;
					String summary = null;		//摘要信息
					String accId = null;		//科目信息
					//生成凭证分录信息
					VLine line = new VLine();
					if(XCARConstants.YSHYUS.equals(arCancelType)){//应收核预收（对应的明细是收款单）
						arBean = xcarCommonDao.getArPayH(arDocumentLBean.getTARGET_ID());
						summary = arBean.getDESCRIPTION();
						line.setAccountCR(0);
						line.setAccountDR(arDocumentLBean.getTARGET_AMT());
						//通过收款单ID得到收款单明细中的科目信息
						List<ArDocumentLBean> arInvPayLLists = xcarCommonDao.getArPayL(arDocumentLBean.getTARGET_ID());
						if(arInvPayLLists != null && arInvPayLLists.size() > 0){
							for(int i = 0;i < arInvPayLLists.size();i++){
								accId = arInvPayLLists.get(i).getACC_ID();
							}
						}
						else
							throw new Exception("收款单：" + arBean.getAR_PAY_H_CODE() + "没有收款明细，请重新选择！");
					}
					
					if(XCARConstants.YSHYF.equals(arCancelType)){//应收核应付
						apBean = apArDao.getApInvGlH(arDocumentLBean.getTARGET_ID());
						summary = apBean.getDESCRIPTION();
						accId = apBean.getAP_ACC_ID_DEBT();
						line.setAccountCR(0);
						line.setAccountDR(arDocumentLBean.getTARGET_AMT());
					}
					if(XCARConstants.YSHLDC.equals(arCancelType)){//应收红蓝对冲（对应的明细是应收单）
						arBean = xcarCommonDao.getArInvGlH(arDocumentLBean.getTARGET_ID());
						summary = arBean.getDESCRIPTION();
						accId = arBean.getAR_ACC_ID_DEBT();
						line.setAccountCR(0);
						line.setAccountDR(arDocumentLBean.getTARGET_AMT()*(-1));
					}
					if(XCARConstants.YUSHLDC.equals(arCancelType)){//预收红蓝对冲（对应的明细是收款单）
						arBean = xcarCommonDao.getArPayH(arDocumentLBean.getTARGET_ID());
						summary = arBean.getDESCRIPTION();
						accId = arBean.getSALE_ACC_ID();
						line.setAccountCR(arDocumentLBean.getTARGET_AMT()*(-1));
						line.setAccountDR(0);
					}
					if(summary == null || "".equals(summary)){
						throw new Exception(XipUtil.getMessage(language, "XC_AP_CANCEL_DESC_IS_NULL_DR", null));//核销说明未填写,无法生成借方分录摘要
					}
					String customerCode = null ;
					String deptCode = null;
					String prjCode = null;
					String empCode = null;
					
					if(XCARConstants.YSHYF.equals(arCancelType)){
						//供应商编码
						customerCode = getVendorCode(apBean,ledger,language);
						//成本中心编码
						 deptCode = getDeptCode(apBean,ledger);
						//项目编码
						prjCode = getPrjCode(apBean,ledger);
						//个人往来编码
						empCode = getEmpCode(apBean);
						line.setVendorCode(customerCode);
					}else{
						//供应商编码
						customerCode = getCustomerCode(arBean,ledger,language);
						//成本中心编码
						deptCode = getDeptCode(arBean,ledger);
						//项目编码
						prjCode = getPrjCode(arBean,ledger);
						//个人往来编码
						empCode = getEmpCode(arBean);
						line.setCustomerCode(customerCode);
					}
					
					//处理科目信息
					HashMap<String,String> accMap = this.getAccMap(ledger.getAccHrcyId(),accId);
					String accCode = accMap.get("accCode");
					
					line.setSummary(summary);
					line.setCcid(null);
					
					line.setAccCode(accCode);
		
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
				throw new Exception(XipUtil.getMessage(language, "XC_AR_DTL_IS_NULL", null));//未录入单据明细信息，无法生成贷方分录行信息！
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
			//获取分录行摘要信息
			String summary = arDocumentBean.getDESCRIPTION(); 
			if(summary == null || "".equals(summary)){
				throw new Exception(XipUtil.getMessage(language, "XC_AP_CANCEL_DESC_IS_NULL_CR", null));//核销说明未填写,无法生成贷方分录摘要！
			}
			String arCancelType = arDocumentBean.getAR_CANCEL_TYPE();	//核销单类型
			//通过核销源单号查找应付单（付款单）中的供应商、科目等信息
			ArDocumentHBean arBean = null;
			String accId = null;		//科目信息
			//生成凭证分录信息
			VLine line = new VLine();
			if(XCARConstants.YSHYUS.equals(arCancelType)){//应收核预收
				arBean = xcarCommonDao.getArInvGlH(arDocumentBean.getSRC_ID());
				accId = arBean.getAR_ACC_ID_DEBT();
				line.setAccountCR(arDocumentBean.getSRC_AMT());
				line.setAccountDR(0);
			}
			if(XCARConstants.YSHYF.equals(arCancelType)){//应收核应付
				arBean = xcarCommonDao.getArInvGlH(arDocumentBean.getSRC_ID());
				accId = arBean.getAR_ACC_ID_DEBT();
				line.setAccountCR(arDocumentBean.getSRC_AMT());
				line.setAccountDR(0);
			}
			if(XCARConstants.YSHLDC.equals(arCancelType)){//应收红蓝对冲（对应的明细是应收单）
				arBean = xcarCommonDao.getArInvGlH(arDocumentBean.getSRC_ID());
				accId = arBean.getAR_ACC_ID_DEBT();
				line.setAccountCR(0);
				line.setAccountDR(-arDocumentBean.getSRC_AMT());
			}
			if(XCARConstants.YUSHLDC.equals(arCancelType)){//预收红蓝对冲（对应的明细是收款单）
				arBean = xcarCommonDao.getArPayH(arDocumentBean.getSRC_ID());
				accId = arBean.getSALE_ACC_ID();
				line.setAccountCR(-arDocumentBean.getSRC_AMT());
				line.setAccountDR(0);
			}
			//供应商编码
			String customerCode = getCustomerCode(arBean,ledger,language);
			//成本中心编码
			String deptCode = getDeptCode(arBean,ledger);
			//项目编码
			String prjCode = getPrjCode(arBean,ledger);
			//个人往来编码
			String empCode = getEmpCode(arBean);
			//科目编码
			Account account = xcglCommonDAO.getAccountById(accId);
			String accCode = account.getAccCode();
			
			line.setSummary(summary);
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