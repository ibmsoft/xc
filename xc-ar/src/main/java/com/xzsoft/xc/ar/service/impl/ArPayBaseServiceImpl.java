/**
 * @Title:ArPayBaseServiceImpl.java
 * @package:com.xzsoft.xc.ar.service.impl
 * @Description:TODO
 * @date:2016年7月11日上午9:20:44
 * @version V1.0
 */
package com.xzsoft.xc.ar.service.impl;

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

import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.ar.dao.ArInvGlBaseDao;
import com.xzsoft.xc.ar.dao.ArInvTransDao;
import com.xzsoft.xc.ar.dao.ArPayBaseDao;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.modal.ArPayHBean;
import com.xzsoft.xc.ar.modal.ArPayLBean;
import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;
import com.xzsoft.xc.ar.service.ArDocsAndVoucherService;
import com.xzsoft.xc.ar.service.ArPayBaseService;
import com.xzsoft.xc.ar.util.XCARConstants;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: ArPayBaseServiceImpl
 * @Description: 收款单相关的操作
 * @author:zhenghy
 * @date 2016年7月11日 上午9:20:44
 */
@Service("arPayBaseService")
public class ArPayBaseServiceImpl implements ArPayBaseService {

	public static final Logger log = Logger.getLogger(ArPayBaseServiceImpl.class);
	@Resource
	private ArPayBaseDao arPayBaseDao;
	@Resource
	private XCARCommonDao xcarCommonDao;
	@Resource
	private RuleService ruleService;
	@Resource
	private ArDocsAndVoucherService arDocsAndVoucherService;
	@Resource
	private VoucherHandlerService voucherHandlerService;//凭证
	@Resource
	private ArInvTransDao arInvTransDao;//交易明细
	@Resource
	private ArInvGlBaseDao arInvGlBaseDao;//应收单
	@Resource
	private ApArTransDao aparTransDao;//交易明细
	@Resource
	private UpdateInvGlAmountService updateInvGlAmountService;//更新应付单或应收单的余额
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	/* (non-Javadoc)
	 * <p>Title: deleteArPay</p>
	 * <p>Description: 删除收款单（包括明细行）、删除凭证、删除交易明细表、修改相关单据的金额</p>
	 * @param deleteRecs
	 * @throws Exception
	 * @see com.xzsoft.xc.ar.service.ArPayBaseService#deleteArPay(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteArPay(String deleteRecs,String language) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			//处理数据
			JSONArray deleteArPays = new JSONArray(deleteRecs); 
			List<String> deleteList = new ArrayList<String>();//收款单id的集合
			JSONArray headArray = new JSONArray();//凭证头id的集合
			
			for (int i = 0; i < deleteArPays.length(); i++) {
				JSONObject json = deleteArPays.getJSONObject(i);
				deleteList.add(json.getString("AR_PAY_H_ID"));
				JSONObject headJson = new JSONObject();
				headJson.put("V_HEAD_ID", json.getString("V_HEAD_ID"));
				headArray.put(headJson);
			}
			
			//验证单据是否可以删除(已在前台完成)
			
			//查找所有行记录
			List<ArPayLBean> ArPayLBeanList = arPayBaseDao.getArPayLs(deleteList);
			
			List<String> arInvIdList = new ArrayList<String>();//应收单id集合
			List<String> arPayIdList = new ArrayList<String>();//需要修改金额的收款单id集合
			
			
			
			//修改相关单据金额（应付单金额）
			for (int i = 0; i < ArPayLBeanList.size(); i++) {
				ArPayLBean arPayLBean = ArPayLBeanList.get(i);
				String invId = arPayLBean.getAR_INV_GL_H_ID();
				String payId = arPayLBean.getL_AR_PAY_H_ID();
				if (!(invId == null || "".equals(invId))) {
					arInvIdList.add(invId);
				}
				if (!(payId == null || "".equals(payId))) {
					arPayIdList.add(payId);
				}
			}
			//删除单据（包括明细行）
			arPayBaseDao.deleteArPayH(deleteList);
			
			//根据行记录删除交易明细表记录
			arInvTransDao.deleteArInvTrans(ArPayLBeanList);
			//修改应收单金额
			if (arInvIdList != null && arInvIdList.size() > 0) {
				updateInvGlAmountService.updateArInvGlAmount(arInvIdList);
			}
			//修改预收款金额
			arPayBaseDao.updateArPayAmt(arPayIdList);
			
			
			//删除凭证
			voucherHandlerService.doDelVouchers(headArray.toString());
			jo.put("flag", "0");
			jo.put("msg", XipUtil.getMessage(language, "XC_AP_CANCEL_CGSC", null)+deleteArPays.length()+XipUtil.getMessage(language, "XC_AP_CANCEL_TDJ", null));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}



	/*
	 * (non-Javadoc)
	 * <p>Title: saveArPay</p> 
	 * <p>Description: 保存收款单</p> 
	 * @param opType
	 * @param arPayH
	 * @param newRecs
	 * @param updateRecs
	 * @param deleteRecs
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ar.service.ArPayBaseService#saveArPay(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveArPay(String opType, String arPayH, String newRecs, String updateRecs, String deleteRecs,String language) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			String current_user_id = CurrentUserUtil.getCurrentUserId();//当前登陆人
			Date current_date = CurrentUserUtil.getCurrentDate();//当前日期
			// 处理数据
			JSONObject arPayHJson = new JSONObject(arPayH);
			JSONArray newRecsJson = new JSONArray(newRecs);
			JSONArray updateRecsJson = new JSONArray(updateRecs);
			JSONArray deleteRecsJson = new JSONArray(deleteRecs);
			
			String ar_pay_h_id = arPayHJson.getString("AR_PAY_H_ID"); // id 
			String ar_pay_h_code = arPayHJson.getString("AR_PAY_H_CODE");// 编码
			String ar_doc_cat_code = arPayHJson.getString("AR_DOC_CAT_CODE");//单据类型
			String ledger_id = arPayHJson.getString("LEDGER_ID"); //账簿
			Date gl_date = sdf.parse(arPayHJson.getString("GL_DATE"));//  入账日期
			String customer_id = arPayHJson.getString("CUSTOMER_ID");  // 客户
			String project_id = arPayHJson.getString("PROJECT_ID");   //项目
			String dept_id = arPayHJson.getString("DEPT_ID");   //部门
			String ar_contract_id = arPayHJson.getString("AR_CONTRACT_ID");   //合同
			double amount = arPayHJson.getDouble("AMOUNT");//收款金额
			String source = arPayHJson.getString("SOURCE");//来源
			String description = arPayHJson.getString("DESCRIPTION");//摘要
			String pay_type = arPayHJson.getString("PAY_TYPE");//收款类型
			String pay_no = arPayHJson.getString("PAY_NO");//结算单据号
			Date pay_date = sdf.parse(arPayHJson.getString("PAY_DATE"));//收款日期
			String sale_acc_id = arPayHJson.getString("SALE_ACC_ID");//收款科目
			//String sale_ccid = arPayHJson.getString("SALE_CCID");//收款科目组合
			String ca_id = arPayHJson.getString("CA_ID");//现金流科目
			String bank_account = arPayHJson.getString("BANK_ACCOUNT");//账户

			//新增id
			if (ar_pay_h_id == null || "".equals(ar_pay_h_id)) {
				ar_pay_h_id = UUID.randomUUID().toString();
			}
			
			//获取单据编号
			if (ar_pay_h_code == null || "".equals(ar_pay_h_code)) {
				String year = sdf.format(current_date).substring(0,4);	// 年
				String month = sdf.format(current_date).substring(5,7);	// 月
				ar_pay_h_code = ruleService.getNextSerialNum(XCARConstants.RULE_CODE_AR_PAY, ledger_id, "", year, month, current_user_id);
			}
			
			//创建收款单主对象
			ArPayHBean arPayHBean = new ArPayHBean();
			arPayHBean.setAR_PAY_H_ID(ar_pay_h_id);
			arPayHBean.setAR_PAY_H_CODE(ar_pay_h_code);
			arPayHBean.setAR_DOC_CAT_CODE(ar_doc_cat_code);
			arPayHBean.setLEDGER_ID(ledger_id);
			arPayHBean.setGL_DATE(gl_date);
			arPayHBean.setCUSTOMER_ID(customer_id);
			arPayHBean.setPROJECT_ID(project_id);
			arPayHBean.setDEPT_ID(dept_id);
			arPayHBean.setAR_CONTRACT_ID(ar_contract_id);
			arPayHBean.setAMOUNT(amount);
			arPayHBean.setSOURCE(source);
			arPayHBean.setDESCRIPTION(description);
			arPayHBean.setPAY_TYPE(pay_type);
			arPayHBean.setPAY_NO(pay_no);
			arPayHBean.setPAY_DATE(pay_date);
			arPayHBean.setSALE_ACC_ID(sale_acc_id);
//			arPayHBean.setSALE_CCID(sale_ccid);
			arPayHBean.setCA_ID(ca_id);
			arPayHBean.setBANK_ACCOUNT(bank_account);
			arPayHBean.setLAST_UPDATE_DATE(current_date);
			arPayHBean.setLAST_UPDATED_BY(current_user_id);

			//创建收款单行对象集合
			List<ArPayLBean> newList = new ArrayList<ArPayLBean>();
			List<ArPayLBean> updateList = new ArrayList<ArPayLBean>();
			List<ArPayLBean> deleteList = new ArrayList<ArPayLBean>();
			for (int i = 0; i < newRecsJson.length(); i++) {
				JSONObject json = newRecsJson.getJSONObject(i);
				ArPayLBean arPayLBean = new ArPayLBean();
				arPayLBean.setAR_PAY_L_ID(UUID.randomUUID().toString());
				arPayLBean.setAR_PAY_H_ID(ar_pay_h_id);
				arPayLBean.setL_AR_PAY_H_ID(json.getString("L_AR_PAY_H_ID"));
				arPayLBean.setL_AR_PAY_L_ID(json.getString("L_AR_PAY_L_ID"));
				arPayLBean.setAR_INV_GL_H_ID(json.getString("AR_INV_GL_H_ID"));
				arPayLBean.setAR_SALE_TYPE_ID(json.getString("AR_SALE_TYPE_ID"));
				arPayLBean.setPRODUCT_ID(json.getString("PRODUCT_ID"));
				arPayLBean.setBG_ITEM_ID(json.getString("BG_ITEM_ID"));
				arPayLBean.setPROJECT_ID(json.getString("PROJECT_ID"));
				arPayLBean.setAR_CONTRACT_ID(json.getString("AR_CONTRACT_ID"));
				arPayLBean.setDEPT_ID(json.getString("DEPT_ID"));
				arPayLBean.setAR_DOC_CAT_CODE(ar_doc_cat_code);
				arPayLBean.setAMOUNT(json.getDouble("AMOUNT"));
				arPayLBean.setCANCEL_AMT(0.00);
				arPayLBean.setNO_CANCEL_AMT(json.getDouble("AMOUNT"));
				arPayLBean.setDESCRIPTION(json.getString("DESCRIPTION"));
				arPayLBean.setACC_ID(json.getString("ACC_ID"));
				arPayLBean.setCCID(json.getString("CCID"));
				arPayLBean.setAR_CREATED_BY_NAME(json.getString("AR_CREATED_BY_NAME"));
				arPayLBean.setCREATION_DATE(current_date);
				arPayLBean.setCREATED_BY(current_user_id);
				arPayLBean.setLAST_UPDATE_DATE(current_date);
				arPayLBean.setLAST_UPDATED_BY(current_user_id);
				newList.add(arPayLBean);
			}
			for (int i = 0; i < updateRecsJson.length(); i++) {
				JSONObject json = updateRecsJson.getJSONObject(i);
				ArPayLBean arPayLBean = new ArPayLBean();
				arPayLBean.setAR_PAY_L_ID(json.getString("AR_PAY_L_ID"));
				arPayLBean.setAR_PAY_H_ID(json.getString("AR_PAY_H_ID"));
				arPayLBean.setL_AR_PAY_H_ID(json.getString("L_AR_PAY_H_ID"));
				arPayLBean.setL_AR_PAY_L_ID(json.getString("L_AR_PAY_L_ID"));
				arPayLBean.setAR_INV_GL_H_ID(json.getString("AR_INV_GL_H_ID"));
				arPayLBean.setAR_SALE_TYPE_ID(json.getString("AR_SALE_TYPE_ID"));
				arPayLBean.setPRODUCT_ID(json.getString("PRODUCT_ID"));
				arPayLBean.setBG_ITEM_ID(json.getString("BG_ITEM_ID"));
				arPayLBean.setPROJECT_ID(json.getString("PROJECT_ID"));
				arPayLBean.setAR_CONTRACT_ID(json.getString("AR_CONTRACT_ID"));
				arPayLBean.setDEPT_ID(json.getString("DEPT_ID"));
				arPayLBean.setAR_DOC_CAT_CODE(ar_doc_cat_code);
				arPayLBean.setAMOUNT(json.getDouble("AMOUNT"));
				arPayLBean.setCANCEL_AMT(0.00);
				arPayLBean.setNO_CANCEL_AMT(json.getDouble("AMOUNT"));
				arPayLBean.setDESCRIPTION(json.getString("DESCRIPTION"));
				arPayLBean.setACC_ID(json.getString("ACC_ID"));
				arPayLBean.setCCID(json.getString("CCID"));
				arPayLBean.setAR_CREATED_BY_NAME(json.getString("AR_CREATED_BY_NAME"));
				arPayLBean.setLAST_UPDATE_DATE(current_date);
				arPayLBean.setLAST_UPDATED_BY(current_user_id);
				updateList.add(arPayLBean);
			}
			for (int i = 0; i < deleteRecsJson.length(); i++) {
				JSONObject json = deleteRecsJson.getJSONObject(i);
				ArPayLBean arPayLBean = new ArPayLBean();
				arPayLBean.setAR_PAY_L_ID(json.getString("AR_PAY_L_ID"));
				arPayLBean.setAR_PAY_H_ID(json.getString("AR_PAY_H_ID"));
				arPayLBean.setL_AR_PAY_H_ID(json.getString("L_AR_PAY_H_ID"));
				arPayLBean.setL_AR_PAY_L_ID(json.getString("L_AR_PAY_L_ID"));
				arPayLBean.setAR_INV_GL_H_ID(json.getString("AR_INV_GL_H_ID"));
				arPayLBean.setAR_SALE_TYPE_ID(json.getString("AR_SALE_TYPE_ID"));
				arPayLBean.setAR_DOC_CAT_CODE(ar_doc_cat_code);
				arPayLBean.setAMOUNT(json.getDouble("AMOUNT"));
				deleteList.add(arPayLBean);
			}
			
			if ("Y".equals(opType)) {
				arPayHBean.setINIT("2");
				arPayHBean.setCREATION_DATE(current_date);
				arPayHBean.setCREATED_BY(current_user_id);
				//新增主信息
				arPayBaseDao.insertArPayH2(arPayHBean);
			}else{
				//修改主信息
				arPayBaseDao.updateArPayH2(arPayHBean);
			}
	
			//新增、修改、删除行信息
			arPayBaseDao.insertArPayLs(newList);
			arPayBaseDao.updateArPayLs(updateList);
			arPayBaseDao.deleteArPayLs(deleteList);
			
			//若单据类型是 结算款（来源是应收单） 或  结算款红（来源是应收单红），则需要修改相关应收单（蓝、红）的金额，并且有交易明细
			if (XCARConstants.SKD_JSK.equals(ar_doc_cat_code)  ||  XCARConstants.SKD_JSK_H.equals(ar_doc_cat_code)) {
				//处理交易明细,应收单id集合
				List<String> arInvIdList = new ArrayList<String>();//应收单id集合
				List<ArInvTransBean> newTransList = new ArrayList<ArInvTransBean>();
				List<ArInvTransBean> updateTransList = new ArrayList<ArInvTransBean>();
				List<ArInvTransBean> deleteTransList = new ArrayList<ArInvTransBean>();
				for (int i = 0; i < newList.size(); i++) {
					ArPayLBean bean = newList.get(i);
					ArInvTransBean arInvTransBean = new ArInvTransBean();
					arInvTransBean.setTRANS_ID(UUID.randomUUID().toString()); // 明细id
					arInvTransBean.setSOURCE_ID(ar_pay_h_id); // 来源id
					arInvTransBean.setSOURCE_DTL_ID(bean.getAR_PAY_L_ID()); // 来源明细id
					arInvTransBean.setAR_INV_GL_H_ID(bean.getAR_INV_GL_H_ID()); // 应收单id
					arInvTransBean.setGL_DATE(arPayHBean.getGL_DATE()); // 入账日期
					arInvTransBean.setSOURCE_TAB("XC_AR_PAY_H"); // 来源表
					arInvTransBean.setAR_DOC_CAT_CODE(arPayHBean.getAR_DOC_CAT_CODE()); // 单据类型
					arInvTransBean.setAR_DOC_CODE(arPayHBean.getAR_PAY_H_CODE());//单据编码
					arInvTransBean.setDESCRIPTION(arPayHBean.getDESCRIPTION());//摘要
					arInvTransBean.setAR_CONTRACT_ID(arPayHBean.getAR_CONTRACT_ID()); // 合同id
					arInvTransBean.setCUSTOMER_ID(arPayHBean.getCUSTOMER_ID()); // 客户id
					arInvTransBean.setDR_AMT(0); // 借方金额
					arInvTransBean.setCR_AMT(bean.getAMOUNT()); // 贷方金额
					arInvTransBean.setTRANS_STATUS("0"); // 状态
					arInvTransBean.setCREATION_DATE(current_date); // 创建日期
					arInvTransBean.setCREATED_BY(current_user_id); // 创建人
					arInvTransBean.setLAST_UPDATE_DATE(current_date); // 修改日期
					arInvTransBean.setLAST_UPDATED_BY(current_user_id); // 修改人
					newTransList.add(arInvTransBean);
					arInvIdList.add(bean.getAR_INV_GL_H_ID());//记录应收单id
				}
				for (int i = 0; i < updateList.size(); i++) {
					ArPayLBean bean = updateList.get(i);
					ArInvTransBean arInvTransBean = new ArInvTransBean();
					arInvTransBean.setSOURCE_ID(ar_pay_h_id); // 来源id
					arInvTransBean.setSOURCE_DTL_ID(bean.getAR_PAY_L_ID()); // 来源明细id
					arInvTransBean.setAR_INV_GL_H_ID(bean.getAR_INV_GL_H_ID()); // 应收单id
					arInvTransBean.setGL_DATE(arPayHBean.getGL_DATE()); // 入账日期
					arInvTransBean.setSOURCE_TAB("XC_AR_PAY_H"); // 来源表
					arInvTransBean.setAR_DOC_CAT_CODE(arPayHBean.getAR_DOC_CAT_CODE()); // 单据类型
					arInvTransBean.setAR_DOC_CODE(arPayHBean.getAR_PAY_H_CODE());//单据编码
					arInvTransBean.setDESCRIPTION(arPayHBean.getDESCRIPTION());//摘要
					arInvTransBean.setAR_CONTRACT_ID(arPayHBean.getAR_CONTRACT_ID()); // 合同id
					arInvTransBean.setCUSTOMER_ID(arPayHBean.getCUSTOMER_ID()); // 客户id
					arInvTransBean.setDR_AMT(0); // 借方金额
					arInvTransBean.setCR_AMT(bean.getAMOUNT()); // 贷方金额
					arInvTransBean.setTRANS_STATUS("0"); // 状态
					arInvTransBean.setLAST_UPDATE_DATE(current_date); // 修改日期
					arInvTransBean.setLAST_UPDATED_BY(current_user_id); // 修改人
					updateTransList.add(arInvTransBean);
					arInvIdList.add(bean.getAR_INV_GL_H_ID());//记录应收单id
				}
				for (int i = 0; i < deleteList.size(); i++) {
					ArPayLBean bean = deleteList.get(i);
					ArInvTransBean arInvTransBean = new ArInvTransBean();
					arInvTransBean.setSOURCE_ID(ar_pay_h_id); // 来源id
					arInvTransBean.setSOURCE_DTL_ID(bean.getAR_PAY_L_ID()); // 来源明细id
					arInvTransBean.setAR_DOC_CAT_CODE(arPayHBean.getAR_DOC_CAT_CODE()); // 单据类型
					deleteTransList.add(arInvTransBean);
					arInvIdList.add(bean.getAR_INV_GL_H_ID());//记录应收单id
				}
				
				//新增、修改、删除交易明细
				aparTransDao.insertArTrans(newTransList);
				aparTransDao.updateArTrans(updateTransList);
				aparTransDao.deleteArTrans(deleteTransList);
				//修改应收交易明细（当单据主信息发生修改而行信息没有发生修改时，也可以修改交易明细的业务日期和摘要） 
				this.updateArTransByDocH(arPayHBean);
				//修改应收单金额
				updateInvGlAmountService.updateArInvGlAmount(arInvIdList);
			}
			
			//若单据类型是预收款红，则需要修改行上面预收款单的金额
			if (XCARConstants.SKD_YUSK_H.equals(ar_doc_cat_code)) {
				List<String> arPayHIdList = new ArrayList<String>();
				for (int i = 0; i < newList.size(); i++) {
					arPayHIdList.add(newList.get(i).getL_AR_PAY_H_ID());
				}
				for (int i = 0; i < deleteList.size(); i++) {
					arPayHIdList.add(deleteList.get(i).getL_AR_PAY_H_ID());
				}
				arPayBaseDao.updateArPayAmt(arPayHIdList);
			}
			
			//生成凭证
			ArVoucherHandlerBean avhb = new ArVoucherHandlerBean(); // 创建一个新的凭证
			avhb.setLedgerId(ledger_id);
			avhb.setArCatCode(XCARConstants.SKD);
			avhb.setAttchTotal(0);
			avhb.setAccDate(gl_date);
			avhb.setArId(ar_pay_h_id);
			
			String v_head_id = "";//凭证头id
			String returnParams = arDocsAndVoucherService.newArVoucher(avhb,language); // 保存凭证并获取返回参数
			JSONObject json = new JSONObject(returnParams);
			if(json.getString("flag").equals("0")){
				v_head_id = json.getString("headId");
			}
			
			jo.put("AR_PAY_H_ID", ar_pay_h_id);
			jo.put("AR_PAY_H_CODE", ar_pay_h_code);
			jo.put("V_HEAD_ID",v_head_id);
			jo.put("flag", "0");
			jo.put("msg", XipUtil.getMessage(language, "XC_AR_PAY_DJHPZBCCG", null));
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	


	/**
	 * @Title: updateArTransByDocH
	 * @Description: 修改应收交易明细（当单据主信息发生修改而行信息没有发生修改时，也可以修改交易明细的业务日期和摘要） 
	 * @return  void 返回类型 
	 * @throws
	 */
	public void updateArTransByDocH(ArPayHBean arPayHBean) throws Exception{
		ArInvTransBean arInvTransBean = new ArInvTransBean();
		arInvTransBean.setSOURCE_ID(arPayHBean.getAR_PAY_H_ID());
		arInvTransBean.setAR_DOC_CAT_CODE(arPayHBean.getAR_DOC_CAT_CODE());
		arInvTransBean.setGL_DATE(arPayHBean.getGL_DATE());
		arInvTransBean.setDESCRIPTION(arPayHBean.getDESCRIPTION());
		aparTransDao.updateArTransByDocH(arInvTransBean);
	}

}
