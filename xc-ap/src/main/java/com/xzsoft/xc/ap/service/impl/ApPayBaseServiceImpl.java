/**
 * @Title:ApPayBaseServiceImpl.java
 * @package:com.xzsoft.xc.ap.service.impl
 * @Description:TODO
 * @date:2016年8月5日下午6:03:30
 * @version V1.0
 */
package com.xzsoft.xc.ap.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ap.dao.ApPayBaseDao;
import com.xzsoft.xc.ap.dao.ApPayReqCommonDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.modal.ApCancelHBean;
import com.xzsoft.xc.ap.modal.ApCancelLBean;
import com.xzsoft.xc.ap.modal.ApPayHBean;
import com.xzsoft.xc.ap.modal.ApPayLBean;
import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.modal.ApPayReqLBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.ap.service.ApPayBaseService;
import com.xzsoft.xc.ap.service.ApPayReqCommonService;
import com.xzsoft.xc.ap.service.ApVoucherService;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: ApPayBaseServiceImpl
 * @Description: 付款单相关操作
 * @author:zhenghy
 * @date: 2016年8月5日 下午6:03:30
 */
@Service("apPayBaseService")
public class ApPayBaseServiceImpl implements ApPayBaseService {
	public static final Logger log = Logger.getLogger(ApPayBaseServiceImpl.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	private XCAPCommonDao xcapCommonDao;
	@Resource
	private RuleService ruleService;
	@Resource
	private ApPayBaseDao apPayBaseDao;
	@Resource
	private ApVoucherService apVoucherService;
	@Resource
	private VoucherHandlerService voucherHandlerService;//凭证
	@Resource
	private ApArTransDao aparTransDao;//交易明细
	@Resource
	private UpdateInvGlAmountService updateInvGlAmountService;
	@Resource
	private ApPayReqCommonService apPayReqCommonService;
	@Resource
	private ApPayReqCommonDao apPayReqCommonDao;
	
	/* (non-Javadoc)
	 * <p>Title: saveApPay</p>
	 * <p>Description: 保存付款单（新增和修改）</p>
	 * @param ApPayH 付款单主信息
	 * @param newPayLs 新增的付款单行信息
	 * @param updatePayLs 修改的付款单行信息
	 * @param deletePayLs 删除的付款单行信息
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.service.ApPayBaseService#saveApPay(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveApPay(String apPayH, String newPayLs, String updatePayLs,String deletePayLs,String opType,String language) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			//参数
			JSONObject apPayHJson = new JSONObject(apPayH);
			JSONArray newPayLsJson = new JSONArray(newPayLs);
			JSONArray updatePayLsJson = new JSONArray(updatePayLs);
			JSONArray deletePayLsJson = new JSONArray(deletePayLs);
			Date currentDate = CurrentUserUtil.getCurrentDate();//当前日期 
			String currentUserId = CurrentUserUtil.getCurrentUserId();//当前用户
			String ap_pay_h_id = apPayHJson.getString("AP_PAY_H_ID");//付款单id
			String ap_pay_h_code = apPayHJson.getString("AP_PAY_H_CODE");//付款单编码
			String ap_doc_cat_code = apPayHJson.getString("AP_DOC_CAT_CODE");//单据类型
			String ledger_id = apPayHJson.getString("LEDGER_ID");//账簿
			Date gl_date = sdf.parse(apPayHJson.getString("GL_DATE"));//业务日期
			String vendor_id = apPayHJson.getString("VENDOR_ID");//供应商
			String project_id = apPayHJson.getString("PROJECT_ID");//项目
			String dept_id = apPayHJson.getString("DEPT_ID");//成本中心
			String ap_contract_id = apPayHJson.getString("AP_CONTRACT_ID");//合同
			double amount =  Double.parseDouble(apPayHJson.getString("AMOUNT")) ;//金额
			String source = apPayHJson.getString("SOURCE");//来源
			String pay_type = apPayHJson.getString("PAY_TYPE");//付款方式
			String deposit_bank_id = apPayHJson.getString("DEPOSIT_BANK_ID");//付款账户
			String pay_acc_id = apPayHJson.getString("PAY_ACC_ID");//付款科目
			//String pay_ccid = apPayHJson.getString("PAY_CCID");//付款科目组合
			String ca_id = apPayHJson.getString("CA_ID");//现金流量项
			String account_name = apPayHJson.getString("ACCOUNT_NAME");//开户名
			String deposit_bank_name = apPayHJson.getString("DEPOSIT_BANK_NAME");//开户行
			String bank_account = apPayHJson.getString("BANK_ACCOUNT");//账户
			String description = apPayHJson.getString("DESCRIPTION");//说明
			//新增id
			if (ap_pay_h_id == null || "".equals(ap_pay_h_id)) {
				ap_pay_h_id = UUID.randomUUID().toString();
			}
			
			//获取单据编号
			if (ap_pay_h_code == null || "".equals(ap_pay_h_code)) {
				String year = sdf.format(currentDate).substring(0,4);	// 年
				String month = sdf.format(currentDate).substring(5,7);	// 月
				ap_pay_h_code = ruleService.getNextSerialNum(XCAPConstants.RULE_CODE_AP_PAY , ledger_id, "", year, month, currentUserId);
			}
			
			//处理付款单主信息
			ApPayHBean apPayHBean = new ApPayHBean();
			apPayHBean.setAP_PAY_H_ID(ap_pay_h_id);
			apPayHBean.setAP_PAY_H_CODE(ap_pay_h_code);
			apPayHBean.setAP_DOC_CAT_CODE(ap_doc_cat_code);
			apPayHBean.setLEDGER_ID(ledger_id);
			apPayHBean.setGL_DATE(gl_date);
			apPayHBean.setVENDOR_ID(vendor_id);
			apPayHBean.setPROJECT_ID(project_id);
			apPayHBean.setDEPT_ID(dept_id);
			apPayHBean.setAP_CONTRACT_ID(ap_contract_id);
			apPayHBean.setAMOUNT(amount);
			apPayHBean.setSOURCE(source);
			apPayHBean.setPAY_TYPE(pay_type);
			apPayHBean.setDEPOSIT_BANK_ID(deposit_bank_id);
			apPayHBean.setPAY_ACC_ID(pay_acc_id);
//			apPayHBean.setPAY_CCID(pay_ccid);
			apPayHBean.setCA_ID(ca_id);
			apPayHBean.setACCOUNT_NAME(account_name);
			apPayHBean.setDEPOSIT_BANK_NAME(deposit_bank_name);
			apPayHBean.setBANK_ACCOUNT(bank_account);
			apPayHBean.setDESCRIPTION(description);
//			apPayHBean.setINIT("2");
			apPayHBean.setLAST_UPDATE_DATE(currentDate);
			apPayHBean.setLAST_UPDATED_BY(currentUserId);
			
			//处理付款单行信息
			List<ApPayLBean> newPayLList = new ArrayList<ApPayLBean>();//新增行信息
			for (int i = 0; i < newPayLsJson.length(); i++) {
				JSONObject payLJson = newPayLsJson.getJSONObject(i);
				String ap_pay_l_id = payLJson.getString("AP_PAY_L_ID");//行id
				ApPayLBean apPayLBean = new ApPayLBean(); 
				if(ap_pay_l_id == null || "".equals(ap_pay_l_id)){
					ap_pay_l_id = UUID.randomUUID().toString();
				}
				apPayLBean.setAP_PAY_L_ID(ap_pay_l_id);
				apPayLBean.setAP_PAY_H_ID(ap_pay_h_id);
				apPayLBean.setAP_PAY_REQ_H_ID(payLJson.getString("AP_PAY_REQ_H_ID"));
				apPayLBean.setAP_PAY_REQ_L_ID(payLJson.getString("AP_PAY_REQ_L_ID"));
				apPayLBean.setAP_INV_GL_H_ID(payLJson.getString("AP_INV_GL_H_ID"));
				apPayLBean.setAP_PUR_TYPE_ID(payLJson.getString("AP_PUR_TYPE_ID"));
				apPayLBean.setL_AP_PAY_H_ID(payLJson.getString("L_AP_PAY_H_ID"));
				apPayLBean.setL_AP_PAY_L_ID(payLJson.getString("L_AP_PAY_L_ID"));
				apPayLBean.setPROJECT_ID(payLJson.getString("PROJECT_ID"));
				apPayLBean.setDEPT_ID(dept_id);
				apPayLBean.setAP_CONTRACT_ID(ap_contract_id);
				apPayLBean.setAP_DOC_CAT_CODE(ap_doc_cat_code);
				apPayLBean.setBG_ITEM_ID(payLJson.getString("BG_ITEM_ID"));
				apPayLBean.setAMOUNT(payLJson.getDouble("AMOUNT"));
				apPayLBean.setCANCEL_AMT(0);
				apPayLBean.setNO_CANCEL_AMT(payLJson.getDouble("AMOUNT"));
				apPayLBean.setACC_ID(payLJson.getString("ACC_ID"));
				apPayLBean.setCCID(payLJson.getString("CCID"));
				apPayLBean.setDESCRIPTION(payLJson.getString("DESCRIPTION"));
				apPayLBean.setCREATION_DATE(currentDate);
				apPayLBean.setCREATED_BY(currentUserId);
				apPayLBean.setLAST_UPDATE_DATE(currentDate);
				apPayLBean.setLAST_UPDATED_BY(currentUserId);
				newPayLList.add(apPayLBean);
			}
			List<ApPayLBean> updatePayLList = new ArrayList<ApPayLBean>();//修改行信息
			for (int i = 0; i < updatePayLsJson.length(); i++) {
				JSONObject payLJson = updatePayLsJson.getJSONObject(i);
				ApPayLBean apPayLBean = new ApPayLBean(); 
				apPayLBean.setAP_PAY_L_ID(payLJson.getString("AP_PAY_L_ID"));
				apPayLBean.setAP_PAY_H_ID(payLJson.getString("AP_PAY_H_ID"));
				apPayLBean.setAP_PAY_REQ_H_ID(payLJson.getString("AP_PAY_REQ_H_ID"));
				apPayLBean.setAP_PAY_REQ_L_ID(payLJson.getString("AP_PAY_REQ_L_ID"));
				apPayLBean.setAP_INV_GL_H_ID(payLJson.getString("AP_INV_GL_H_ID"));
				apPayLBean.setAP_PUR_TYPE_ID(payLJson.getString("AP_PUR_TYPE_ID"));
				apPayLBean.setL_AP_PAY_H_ID(payLJson.getString("L_AP_PAY_H_ID"));
				apPayLBean.setL_AP_PAY_L_ID(payLJson.getString("L_AP_PAY_L_ID"));
				apPayLBean.setPROJECT_ID(payLJson.getString("PROJECT_ID"));
				apPayLBean.setDEPT_ID(dept_id);
				apPayLBean.setAP_CONTRACT_ID(ap_contract_id);
				apPayLBean.setAP_DOC_CAT_CODE(ap_doc_cat_code);
				apPayLBean.setBG_ITEM_ID(payLJson.getString("BG_ITEM_ID"));
				apPayLBean.setAMOUNT(payLJson.getDouble("AMOUNT"));
				apPayLBean.setCANCEL_AMT(0);
				apPayLBean.setNO_CANCEL_AMT(payLJson.getDouble("AMOUNT"));
				apPayLBean.setACC_ID(payLJson.getString("ACC_ID"));
				apPayLBean.setCCID(payLJson.getString("CCID"));
				apPayLBean.setDESCRIPTION(payLJson.getString("DESCRIPTION"));
				apPayLBean.setLAST_UPDATE_DATE(currentDate);
				apPayLBean.setLAST_UPDATED_BY(currentUserId);
				updatePayLList.add(apPayLBean);
			}
			List<ApPayLBean> deletePayLList = new ArrayList<ApPayLBean>();//删除行信息
			for (int i = 0; i < deletePayLsJson.length(); i++) {
				JSONObject payLJson = deletePayLsJson.getJSONObject(i);
				ApPayLBean apPayLBean = new ApPayLBean(); 
				apPayLBean.setAP_PAY_L_ID(payLJson.getString("AP_PAY_L_ID"));
				apPayLBean.setAP_PAY_H_ID(payLJson.getString("AP_PAY_H_ID"));
				apPayLBean.setAP_PAY_REQ_H_ID(payLJson.getString("AP_PAY_REQ_H_ID"));
				apPayLBean.setAP_PAY_REQ_L_ID(payLJson.getString("AP_PAY_REQ_L_ID"));
				apPayLBean.setAP_INV_GL_H_ID(payLJson.getString("AP_INV_GL_H_ID"));
				apPayLBean.setAP_PUR_TYPE_ID(payLJson.getString("AP_PUR_TYPE_ID"));
				apPayLBean.setL_AP_PAY_H_ID(payLJson.getString("L_AP_PAY_H_ID"));
				apPayLBean.setL_AP_PAY_L_ID(payLJson.getString("L_AP_PAY_L_ID"));
				apPayLBean.setAMOUNT(payLJson.getDouble("AMOUNT"));
				apPayLBean.setCANCEL_AMT(0);
				apPayLBean.setNO_CANCEL_AMT(payLJson.getDouble("AMOUNT"));
				deletePayLList.add(apPayLBean);
			}
			//判断是新增还是修改
			if ("Y".equals(opType)) {//新增付款单
				//赋值
				apPayHBean.setINIT("2");//正常单据
				apPayHBean.setSIGN_STATUS("1");//代签收
				apPayHBean.setCREATION_DATE(currentDate);
				apPayHBean.setCREATED_BY(currentUserId);
				apPayBaseDao.insertApPayH(apPayHBean);//新增付款单主信息
				apPayBaseDao.insertApPayLs(newPayLList);//新增付款单行
			}else{//修改
				apPayBaseDao.updateApPayH(apPayHBean);//修改主信息
				apPayBaseDao.insertApPayLs(newPayLList);//新增行
				apPayBaseDao.updateApPayLs(updatePayLList);//修改行
				apPayBaseDao.deleteApPayLs(deletePayLList);//删除行
			}
			
			// 新增、修改、删除 交易明细
			List<ApInvTransBean> newTransList = new ArrayList<ApInvTransBean>();
			List<ApInvTransBean> deleteTransList = new ArrayList<ApInvTransBean>();
			//新增交易明细
			for (int i = 0; i < newPayLList.size(); i++) {
				ApPayLBean apPayLBean = newPayLList.get(i);
				if (XCAPConstants.PAY_SOURCE_REQ.equals(source)) {
					ApInvTransBean apInvTransBean = this.createApTrans(apPayHBean, apPayLBean,"REQ");
					newTransList.add(apInvTransBean);
					if (XCAPConstants.FKD_CGJS.equals(ap_doc_cat_code)) {
						ApInvTransBean apInvTransBean2 = this.createApTrans(apPayHBean, apPayLBean,"INV");
						apInvTransBean2.setREQ_AMT(apPayLBean.getAMOUNT()*(-1));
						newTransList.add(apInvTransBean2);
					}
				}
				if (XCAPConstants.PAY_SOURCE_GL_H.equals(source) || XCAPConstants.PAY_SOURCE_GL_L.equals(source)) {
					ApInvTransBean apInvTransBean = this.createApTrans(apPayHBean, apPayLBean,"INV");
					newTransList.add(apInvTransBean);
				}
			}
			//删除交易明细
			for (int i = 0; i < deletePayLList.size(); i++) {
				ApPayLBean apPayLBean = deletePayLList.get(i);
				ApInvTransBean apInvTransBean = new ApInvTransBean();
				apInvTransBean.setSOURCE_ID(ap_pay_h_id);
				apInvTransBean.setSOURCE_DTL_ID(apPayLBean.getAP_PAY_L_ID());
				apInvTransBean.setAP_DOC_CAT_CODE(ap_doc_cat_code);
				apInvTransBean.setAP_INV_GL_H_ID(apPayLBean.getAP_INV_GL_H_ID());
				deleteTransList.add(apInvTransBean);
			}
			aparTransDao.deleteApTrans(deleteTransList);
			aparTransDao.insertApTrans(newTransList);
			
			
			
			List<String> invIdList = new ArrayList<String>();//应付单id集合
			List<String> reqLIdList = new ArrayList<String>();//申请单行id集合
			List<String> payHIdList = new ArrayList<String>();//付款单id集合
			
			for (int i = 0; i < newPayLList.size(); i++) {
				String invId = newPayLList.get(i).getAP_INV_GL_H_ID();
//				String reqLId = newPayLList.get(i).getAP_PAY_REQ_L_ID();
				String payHId = newPayLList.get(i).getL_AP_PAY_H_ID();
				if (!(invId == null || "".equals(invId))) {
					invIdList.add(invId);
				}
//				if (!(reqLId == null || "".equals(reqLId))) {
//					reqLIdList.add(reqLId);
//				}
				if (!(payHId == null || "".equals(payHId))) {
					payHIdList.add(payHId);
				}
			}
			for (int i = 0; i < deletePayLList.size(); i++) {
				String invId = deletePayLList.get(i).getAP_INV_GL_H_ID();
//				String reqLId = newPayLList.get(i).getAP_PAY_REQ_L_ID();
				String payHId = newPayLList.get(i).getL_AP_PAY_H_ID();
				if (!(invId == null || "".equals(invId))) {
					invIdList.add(invId);
				}
//				if (!(reqLId == null || "".equals(reqLId))) {
//					reqLIdList.add(reqLId);
//				}
				if (!(payHId == null || "".equals(payHId))) {
					payHIdList.add(payHId);
				}
			}
			updateInvGlAmountService.updateApInvGlAmount(invIdList);//修改应付单金额
			this.updatePayAmt(payHIdList);//修改付款单金额
			this.updateReqAmt(newPayLList, deletePayLList);//修改申请单金额
			apPayReqCommonService.updateOccupyAmt(reqLIdList);//修改申请单金额，待完成
		
			
			
			//凭证
			ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
			avhb.setLedgerId(ledger_id);
			avhb.setApCatCode(XCAPConstants.FKD);
			avhb.setAttchTotal(0);
			avhb.setAccDate(gl_date);
			avhb.setApId(ap_pay_h_id);

			String v_head_id = "";//凭证头id
			String returnParams = apVoucherService.newVoucher(avhb,language);
			JSONObject json = new JSONObject(returnParams);
			if(json.getString("flag").equals("0")){
				v_head_id = json.getString("headId");
			}
			
			jo.put("AP_PAY_H_ID", ap_pay_h_id);
			jo.put("AP_PAY_H_CODE", ap_pay_h_code);
			jo.put("V_HEAD_ID",v_head_id);
			jo.put("flag", "0");
			//jo.put("msg", "单据和凭证保存成功！");
			jo.put("msg", XipUtil.getMessage(language, "XC_AP_PAY_DJHPZBCCG", null));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}

	/* (non-Javadoc)
	 * <p>Title: deleteApPay</p>
	 * <p>Description: 删除付款单</p>
	 * @param apPayHIds
	 * @return
	 * @throws Exception
	 * @see com.xzsoft.xc.ap.service.ApPayBaseService#deleteApPay(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteApPay(String deleteRecs,String language) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray apPayHsJson = new JSONArray(deleteRecs);
			List<String> apPayHIdList = new ArrayList<String>();//付款单id集合
			JSONArray headArray = new JSONArray();//凭证头id的集合
			
			
			for (int i = 0; i < apPayHsJson.length(); i++) {
				JSONObject json = apPayHsJson.getJSONObject(i);
				String ap_pay_h_id = json.getString("AP_PAY_H_ID");
				apPayHIdList.add(ap_pay_h_id);
				JSONObject headJson = new JSONObject();
				headJson.put("V_HEAD_ID", json.getString("V_HEAD_ID"));
				headArray.put(headJson);
			}
			//验证单据是否可以删除（已审核的不可删除，已核销的不可删除，预付款蓝被预付款红使用的不可删除）  -- 在前端完成了

			List<ApPayLBean> apPayLBeanList = apPayBaseDao.selectApPayLsByIds(apPayHIdList);//获取付款单明细（即将被删除）
			
			List<ApPayLBean> apPayLBeanList3 = new ArrayList<ApPayLBean>();//来源是付款单
			
			for (int i = 0; i < apPayLBeanList.size(); i++) {
				ApPayLBean apPayLBean = apPayLBeanList.get(i);
				String ap_doc_cat_code = apPayLBean.getAP_DOC_CAT_CODE();//单据类型
				String l_ap_pay_h_id = apPayLBean.getL_AP_PAY_H_ID();//付款单id
				//判断来源单据类型
				if (XCAPConstants.FKD_YUFK_H.equals(ap_doc_cat_code)) {
					if (!(l_ap_pay_h_id ==null || "".equals(l_ap_pay_h_id))) {
						apPayLBeanList3.add(apPayLBean);
					}
				}
			}
			
			//删除付款单明细
			apPayBaseDao.deleteApPayLs(apPayLBeanList);
			//删除付款单
			apPayBaseDao.deleteApPayHs(apPayHIdList);
			
			
			List<ApInvTransBean> deleteTrans = new ArrayList<ApInvTransBean>();//要删除的交易明细
			List<String> invIdList = new ArrayList<String>();//应付单id集合
			List<String> reqLIdList = new ArrayList<String>();//申请单id集合
			List<String> payHIdList = new ArrayList<String>();//付款单id集合
			
			for (int i = 0; i < apPayLBeanList.size(); i++) {
				ApPayLBean apPayLBean = apPayLBeanList.get(i);
				ApInvTransBean apInvTransBean = new ApInvTransBean();//交易明细对象
				apInvTransBean.setSOURCE_ID(apPayLBean.getAP_PAY_H_ID());
				apInvTransBean.setSOURCE_DTL_ID(apPayLBean.getAP_PAY_L_ID());
				apInvTransBean.setAP_DOC_CAT_CODE(apPayLBean.getAP_DOC_CAT_CODE());
				deleteTrans.add(apInvTransBean);
				String invId = apPayLBean.getAP_INV_GL_H_ID();//应付单id
				if (!(invId == null || "".equals(invId))) {
					invIdList.add(invId);
				}
				String reqLId = apPayLBean.getAP_PAY_REQ_L_ID();//申请单行id集合
				if (!(reqLId == null || "".equals(reqLId))) {
					reqLIdList.add(reqLId);
				}
				String payHId = apPayLBean.getL_AP_PAY_H_ID();//付款单id集合
				if (!(payHId == null || "".equals(payHId))) {
					payHIdList.add(reqLId);
				}
			}
			aparTransDao.deleteApTrans(deleteTrans);//删除交易明细
			updateInvGlAmountService.updateApInvGlAmount(invIdList);//修改应付单金额
			this.updatePayAmt(payHIdList);
			this.updateReqAmt(null, apPayLBeanList);//修改申请单金额
			apPayReqCommonService.updateOccupyAmt(reqLIdList);//修改申请单金额，待完成
			
			
			//来源是付款单，修改付款单金额(忽略金额的正负)
			apPayBaseDao.updateApPayLCancelAmt("-", apPayLBeanList3);
			
			//删除凭证
			voucherHandlerService.doDelVouchers(headArray.toString());
			jo.put("flag", "0");
			jo.put("msg", XipUtil.getMessage(language, "XC_AP_PAY_CGSC", null)+apPayHsJson.length()+XipUtil.getMessage(language, "XC_AP_PAY_TDJ", null));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	
	/*
	 * (non-Javadoc)
	 * <p>Title: updatePayAmt</p> 
	 * <p>Description: 重新计算付款单金额</p> 
	 * @param list
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.service.ApPayBaseService#updatePayAmt(java.util.List)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void updatePayAmt(List<String> list) throws Exception{
		for (int i = 0; i < list.size(); i++) {
			String AP_PAY_H_ID = list.get(i);
			double cancelAmt = 0.00;
			//查询付款单单据类型
			String apDocCatCode = apPayBaseDao.selectApDocCatCodeByApPayHId(AP_PAY_H_ID);
			if (XCAPConstants.FKD_YUFK.equals(apDocCatCode)) {
				//查询该付款单有没有在付款单中被引用(预付红单会引用预付蓝单)
				List<ApPayLBean> apPayLBeanList =  apPayBaseDao.ifPayIdExistInPayL(AP_PAY_H_ID);
				if (apPayLBeanList != null && apPayLBeanList.size() > 0) {
					for (int j = 0; j < apPayLBeanList.size(); j++) {
						cancelAmt += Math.abs(apPayLBeanList.get(j).getAMOUNT());
					}
					
				}
				//查询该付款单有没有在核销单行中被引用(被核销单主引用的只能是预付款蓝单)
				List<ApCancelLBean> apCancelLBeanList = apPayBaseDao.ifPayIdExistInCancelL(AP_PAY_H_ID);
				if (apCancelLBeanList != null && apCancelLBeanList.size() > 0) {
					for (int j = 0; j < apCancelLBeanList.size(); j++) {
						cancelAmt += Math.abs(apCancelLBeanList.get(j).getTARGET_AMT());
					}
					
				}
			}else if (XCAPConstants.FKD_YUFK_H.equals(apDocCatCode)) {
				//查询该付款单有没有在核销单主中被引用（被核销单主引用的只能是预付款红单）
				List<ApCancelHBean> apCancelHBeanList = apPayBaseDao.ifPayIdExistInCancelH(AP_PAY_H_ID);
				if (apCancelHBeanList != null && apCancelHBeanList.size() > 0) {
					for (int j = 0; j < apCancelHBeanList.size(); j++) {
						cancelAmt += Math.abs(apCancelHBeanList.get(j).getSRC_AMT()) * (-1);
					}
					
				}
				//查询该付款单有没有在核销单行中被引用(被核销单主引用的只能是预付款蓝单)
				List<ApCancelLBean> apCancelLBeanList = apPayBaseDao.ifPayIdExistInCancelL(AP_PAY_H_ID);
				if (apCancelLBeanList != null && apCancelLBeanList.size() > 0) {
					for (int j = 0; j < apCancelLBeanList.size(); j++) {
						cancelAmt += Math.abs(apCancelLBeanList.get(j).getTARGET_AMT() * (-1));
					}
					
				}
			}
			
			apPayBaseDao.calculateApPayLCancelAmt(AP_PAY_H_ID, cancelAmt);
		}
	}
	
	/**
	 * @Title: createApTrans
	 * @Description: 创建交易明细
	 * @param apPayHBean
	 * @param apPayLBean
	 * @param hl
	 * @return  设定文件 
	 * @return  ApInvTransBean 返回类型 
	 * @throws
	 */
	public ApInvTransBean createApTrans(ApPayHBean apPayHBean,ApPayLBean apPayLBean,String docType){
		ApInvTransBean apInvTransBean = new ApInvTransBean();
		apInvTransBean.setTRANS_ID(UUID.randomUUID().toString());
		apInvTransBean.setSOURCE_ID(apPayLBean.getAP_PAY_H_ID());//来源id-付款单id
		apInvTransBean.setSOURCE_DTL_ID(apPayLBean.getAP_PAY_L_ID());//付款单明细id
		apInvTransBean.setSOURCE_TAB("XC_AP_PAY_H");//来源表
		apInvTransBean.setGL_DATE(apPayHBean.getGL_DATE());//业务日期
		apInvTransBean.setAP_DOC_CAT_CODE(apPayHBean.getAP_DOC_CAT_CODE());//单据类型
		apInvTransBean.setAP_DOC_CODE(apPayHBean.getAP_PAY_H_CODE());//来源单据编码-付款单编码
		apInvTransBean.setDESCRIPTION(apPayHBean.getDESCRIPTION());//摘要
		apInvTransBean.setAP_CONTRACT_ID(apPayHBean.getAP_CONTRACT_ID());//合同
		apInvTransBean.setVENDOR_ID(apPayHBean.getVENDOR_ID());//供应商
		apInvTransBean.setTRANS_STATUS("0");
		apInvTransBean.setCREATION_DATE(apPayHBean.getLAST_UPDATE_DATE());
		apInvTransBean.setCREATED_BY(apPayHBean.getLAST_UPDATED_BY());
		apInvTransBean.setLAST_UPDATE_DATE(apPayHBean.getLAST_UPDATE_DATE());
		apInvTransBean.setLAST_UPDATED_BY(apPayHBean.getLAST_UPDATED_BY());
				
		if ("REQ".equals(docType)) {
			apInvTransBean.setDR_AMT(apPayLBean.getAMOUNT());
			apInvTransBean.setCR_AMT(0);
			apInvTransBean.setREQ_AMT(apPayLBean.getAMOUNT()*(-1));
		}else if ("INV".equals(docType)) {
			apInvTransBean.setAP_INV_GL_H_ID(apPayLBean.getAP_INV_GL_H_ID());
			apInvTransBean.setDR_AMT(apPayLBean.getAMOUNT());
			apInvTransBean.setCR_AMT(0);
			apInvTransBean.setREQ_AMT(0);
		}
		
		return apInvTransBean;
	}

	/**
	 * @Title: updateReqAmt
	 * @Description: 修改申请单的金额
	 * @param newPayLList
	 * @param deletePayLList  设定文件 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateReqAmt(List<ApPayLBean> newPayLList,List<ApPayLBean> deletePayLList){
		List<ApPayReqHBean> apPayReqHBeanList = new ArrayList<ApPayReqHBean>();
		List<ApPayReqLBean> apPayReqLBeanList = new ArrayList<ApPayReqLBean>();
		if ( newPayLList != null && newPayLList.size() > 0) {
			for (int i = 0; i < newPayLList.size(); i++) {
				ApPayLBean apPayLBean = newPayLList.get(i);
				
				ApPayReqHBean apPayReqHBean = new ApPayReqHBean();
				apPayReqHBean.setAP_PAY_REQ_H_ID(apPayLBean.getAP_PAY_REQ_H_ID());
				apPayReqHBean.setAMOUNT(apPayLBean.getAMOUNT());
				apPayReqHBeanList.add(apPayReqHBean);
				
				ApPayReqLBean apPayReqLBean = new ApPayReqLBean();
				apPayReqLBean.setAP_PAY_REQ_L_ID(apPayLBean.getAP_PAY_REQ_L_ID());
				apPayReqLBean.setAMOUNT(apPayLBean.getAMOUNT());
				apPayReqLBeanList.add(apPayReqLBean);
			}
		}
		if (deletePayLList != null && deletePayLList.size() > 0) {
			for (int i = 0; i < deletePayLList.size(); i++) {
				ApPayLBean apPayLBean = deletePayLList.get(i);
				
				ApPayReqHBean apPayReqHBean = new ApPayReqHBean();
				apPayReqHBean.setAP_PAY_REQ_H_ID(apPayLBean.getAP_PAY_REQ_H_ID());
				apPayReqHBean.setAMOUNT(apPayLBean.getAMOUNT() * (-1) );
				apPayReqHBeanList.add(apPayReqHBean);
				
				ApPayReqLBean apPayReqLBean = new ApPayReqLBean();
				apPayReqLBean.setAP_PAY_REQ_L_ID(apPayLBean.getAP_PAY_REQ_L_ID());
				apPayReqLBean.setAMOUNT(apPayLBean.getAMOUNT() * (-1) );
				apPayReqLBeanList.add(apPayReqLBean);
			}
		}
		if (apPayReqHBeanList.size() > 0) {
			apPayReqCommonDao.updateReqHAmout(apPayReqHBeanList);
		}	
		if (apPayReqLBeanList.size() > 0) {
			apPayReqCommonDao.updateReqLAmount(apPayReqLBeanList);
		}
		
	}
	
}
