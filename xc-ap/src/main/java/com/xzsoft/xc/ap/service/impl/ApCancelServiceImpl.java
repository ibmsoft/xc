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

import com.xzsoft.xc.ap.dao.ApCancelDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.modal.ApCancelHBean;
import com.xzsoft.xc.ap.modal.ApCancelLBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.ap.service.ApCancelService;
import com.xzsoft.xc.ap.service.ApPayBaseService;
import com.xzsoft.xc.ap.service.ApVoucherService;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

@Service("apCancelService")
public class ApCancelServiceImpl implements ApCancelService {
	public static final Logger log = Logger.getLogger(ApCancelServiceImpl.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	private XCAPCommonDao xcapCommonDao;
	@Resource
	private RuleService ruleService;
	@Resource
	private ApCancelDao apCancelDao;
	@Resource
	private ApVoucherService apVoucherService;
	@Resource
	private VoucherHandlerService voucherHandlerService;//凭证
	@Resource
	private ApArTransDao aparTransDao;//交易明细
	@Resource
	private UpdateInvGlAmountService updateInvGlAmountService;
	@Resource
	private ApPayBaseService apPayBaseService;
	/*
	 * (non-Javadoc)
	 * <p>Title: saveCancel</p> 
	 * <p>Description: 保存核销单</p> 
	 * @param cancelH
	 * @param newRecs
	 * @param updateRecs
	 * @param deleteRecs
	 * @param opType
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.service.ApCancelService#saveCancel(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveCancel(String cancelH, String newRecs, String updateRecs, String deleteRecs, String opType,String language)	throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONObject cancelHJson = new JSONObject(cancelH);
			JSONArray newRecsJson = new JSONArray(newRecs);
			JSONArray updateRecsJson = new JSONArray(updateRecs);
			JSONArray deleteRecsJson = new JSONArray(deleteRecs);
			String currentUserId = CurrentUserUtil.getCurrentUserId();//当前用户
			Date currentDate = CurrentUserUtil.getCurrentDate();//当前日期 
			
			String ap_cancel_h_id = cancelHJson.getString("AP_CANCEL_H_ID");//核销单id
			String ap_cancel_h_code = cancelHJson.getString("AP_CANCEL_H_CODE");//核销单编码
			String ledger_id = cancelHJson.getString("LEDGER_ID");//账簿id
			String ap_cancel_type = cancelHJson.getString("AP_CANCEL_TYPE");//核销类型
			Date gl_date = sdf.parse(cancelHJson.getString("GL_DATE"));//业务日期
			String src_id = cancelHJson.getString("SRC_ID");//来源单据id
			double src_amt = Double.parseDouble(cancelHJson.getString("SRC_AMT"));//核销源金额
			String description = cancelHJson.getString("DESCRIPTION");//摘要
			
			//判断是否有主id
			if ("".equals(ap_cancel_h_id) || ap_cancel_h_id == null) {
				ap_cancel_h_id = UUID.randomUUID().toString();
			}
			
			//判断是否有编码
			if ("".equals(ap_cancel_h_code) || ap_cancel_h_code == null) {
				String year = sdf.format(currentDate).substring(0,4);	// 年
				String month = sdf.format(currentDate).substring(5,7);	// 月
				ap_cancel_h_code = ruleService.getNextSerialNum(XCAPConstants.RULE_CODE_AP_CANCEL, ledger_id, "", year, month, currentUserId);
			}
			
			//核销单主
			ApCancelHBean apCancelHBean = new ApCancelHBean();
			apCancelHBean.setAP_CANCEL_H_ID(ap_cancel_h_id);
			apCancelHBean.setAP_CANCEL_H_CODE(ap_cancel_h_code);
			apCancelHBean.setLEDGER_ID(ledger_id);
			apCancelHBean.setAP_CANCEL_TYPE(ap_cancel_type);
			apCancelHBean.setGL_DATE(gl_date);
			apCancelHBean.setSRC_ID(src_id);
			apCancelHBean.setSRC_AMT(src_amt);
			apCancelHBean.setDESCRIPTION(description);
			apCancelHBean.setLAST_UPDATE_DATE(currentDate);
			apCancelHBean.setLAST_UPDATED_BY(currentUserId);
			
			//核销单行
			List<ApCancelLBean> newCancelLs = new ArrayList<ApCancelLBean>();
			List<ApCancelLBean> updateCancelLs = new ArrayList<ApCancelLBean>();
			List<ApCancelLBean> deleteCancelLs = new ArrayList<ApCancelLBean>();
			for (int i = 0; i < newRecsJson.length(); i++) {
				JSONObject json = newRecsJson.getJSONObject(i);
				ApCancelLBean bean = new ApCancelLBean();
				String id = json.getString("AP_CANCEL_L_ID");
				if ("".equals(id) || id == null) {
					id = UUID.randomUUID().toString();
				}
				bean.setAP_CANCEL_L_ID(id);
				bean.setAP_CANCEL_H_ID(ap_cancel_h_id);
				bean.setAP_CANCEL_TYPE(ap_cancel_type);
				bean.setTARGET_ID(json.getString("TARGET_ID"));
				bean.setTARGET_AMT(Double.parseDouble(json.getString("TARGET_AMT")));
				bean.setCREATION_DATE(currentDate);
				bean.setCREATED_BY(currentUserId);
				bean.setLAST_UPDATE_DATE(currentDate);
				bean.setLAST_UPDATED_BY(currentUserId);
				newCancelLs.add(bean);
			}
			for (int i = 0; i < updateRecsJson.length(); i++) {
				JSONObject json = updateRecsJson.getJSONObject(i);
				ApCancelLBean bean = new ApCancelLBean();
				bean.setAP_CANCEL_L_ID(json.getString("AP_CANCEL_L_ID"));
				bean.setAP_CANCEL_H_ID(ap_cancel_h_id);
				bean.setAP_CANCEL_TYPE(ap_cancel_type);
				bean.setTARGET_ID(json.getString("TARGET_ID"));
				bean.setTARGET_AMT(Double.parseDouble(json.getString("TARGET_AMT")));
				bean.setLAST_UPDATE_DATE(currentDate);
				bean.setLAST_UPDATED_BY(currentUserId);
				updateCancelLs.add(bean);
			}
			for (int i = 0; i < deleteRecsJson.length(); i++) {
				JSONObject json = deleteRecsJson.getJSONObject(i);
				ApCancelLBean bean = new ApCancelLBean();
				bean.setAP_CANCEL_L_ID(json.getString("AP_CANCEL_L_ID"));
				bean.setAP_CANCEL_H_ID(json.getString("AP_CANCEL_H_ID"));
				bean.setTARGET_ID(json.getString("TARGET_ID"));
				bean.setTARGET_AMT(Double.parseDouble(json.getString("TARGET_AMT")));
				bean.setAP_CANCEL_TYPE(ap_cancel_type);
				deleteCancelLs.add(bean);
			}
			
			//新增
			if ("Y".equals(opType)) {
				apCancelHBean.setCREATION_DATE(currentDate);
				apCancelHBean.setCREATED_BY(currentUserId);
				apCancelDao.insertCancelH(apCancelHBean);//新增
				apCancelDao.insertCancelLs(newCancelLs);//新增行
			}
			//修改
			if ("N".equals(opType)) {
				apCancelDao.updateCancelH(apCancelHBean);
				apCancelDao.insertCancelLs(newCancelLs);
				apCancelDao.updateCancelLs(updateCancelLs);
				apCancelDao.deleteCancelLs(deleteCancelLs);
			}
			//新增、修改、删除 交易明细
			List<ApInvTransBean> newApTrans = new ArrayList<ApInvTransBean>();
			List<ApInvTransBean> deleteApTrans = new ArrayList<ApInvTransBean>();
			List<ArInvTransBean> newArTrans = new ArrayList<ArInvTransBean>();
			List<ArInvTransBean> deleteArTrans = new ArrayList<ArInvTransBean>();
			//新增交易明细
			for (int i = 0; i < newCancelLs.size(); i++) {
				ApCancelLBean apCancelLBean = newCancelLs.get(i);
				if (XCAPConstants.HXD_YFHYUF.equals(ap_cancel_type)) {
					ApInvTransBean apInvTransBean = this.createApTrans(apCancelHBean, apCancelLBean, "H");
					newApTrans.add(apInvTransBean);
				}else if (XCAPConstants.HXD_YFHYS.equals(ap_cancel_type)) {
					ApInvTransBean apInvTransBean = this.createApTrans(apCancelHBean, apCancelLBean, "H");
					newApTrans.add(apInvTransBean);
					ArInvTransBean arInvTransBean = this.createArTrans(apCancelHBean, apCancelLBean);
					newArTrans.add(arInvTransBean);
				}else if (XCAPConstants.HXD_YFHLDC.equals(ap_cancel_type)) {
					ApInvTransBean apInvTransBean = this.createApTrans(apCancelHBean, apCancelLBean, "H");
					newApTrans.add(apInvTransBean);
					ApInvTransBean apInvTransBean2 = this.createApTrans(apCancelHBean, apCancelLBean, "L");
					newApTrans.add(apInvTransBean2);
				}
				
			}
			
			for (int i = 0; i < deleteCancelLs.size(); i++) {
				ApCancelLBean apCancelLBean = deleteCancelLs.get(i);
				if (XCAPConstants.HXD_YFHYUF.equals(ap_cancel_type)) {
					ApInvTransBean apInvTransBean = new ApInvTransBean();
					apInvTransBean.setSOURCE_ID(apCancelLBean.getAP_CANCEL_H_ID());
					apInvTransBean.setSOURCE_DTL_ID(apCancelLBean.getAP_CANCEL_L_ID());
					apInvTransBean.setAP_DOC_CAT_CODE(apCancelLBean.getAP_CANCEL_TYPE());
					deleteApTrans.add(apInvTransBean);
				}else if (XCAPConstants.HXD_YFHYS.equals(ap_cancel_type)) {
					ApInvTransBean apInvTransBean = new ApInvTransBean();
					apInvTransBean.setSOURCE_ID(apCancelLBean.getAP_CANCEL_H_ID());
					apInvTransBean.setSOURCE_DTL_ID(apCancelLBean.getAP_CANCEL_L_ID());
					apInvTransBean.setAP_DOC_CAT_CODE(apCancelLBean.getAP_CANCEL_TYPE());
					deleteApTrans.add(apInvTransBean);
					ArInvTransBean arInvTransBean = new ArInvTransBean();
					arInvTransBean.setSOURCE_ID(apCancelLBean.getAP_CANCEL_H_ID());
					arInvTransBean.setSOURCE_DTL_ID(apCancelLBean.getAP_CANCEL_L_ID());
					arInvTransBean.setAR_DOC_CAT_CODE(apCancelLBean.getAP_CANCEL_TYPE());
					deleteArTrans.add(arInvTransBean);
				}else if (XCAPConstants.HXD_YFHLDC.equals(ap_cancel_type)) {
					ApInvTransBean apInvTransBean = new ApInvTransBean();
					apInvTransBean.setSOURCE_ID(apCancelLBean.getAP_CANCEL_H_ID());
					apInvTransBean.setSOURCE_DTL_ID(apCancelLBean.getAP_CANCEL_L_ID());
					apInvTransBean.setAP_DOC_CAT_CODE(apCancelLBean.getAP_CANCEL_TYPE());
					deleteApTrans.add(apInvTransBean);
				}	
			}
			
			//新增交易明细
			aparTransDao.insertApTrans(newApTrans);
			aparTransDao.insertArTrans(newArTrans);
			
			//删除交易明细
			aparTransDao.deleteApTrans(deleteApTrans);
			aparTransDao.deleteArTrans(deleteArTrans);
	
			
			
			List<String> invIdList = new ArrayList<String>();//应付单id集合
			List<String> arInvIdList = new ArrayList<String>();//应收单id集合
			List<String> payHIdList = new ArrayList<String>();//付款单id集合
			
			if (XCAPConstants.HXD_YFHYUF.equals(ap_cancel_type)  ) {
				invIdList.add(apCancelHBean.getSRC_ID());
			}else if (XCAPConstants.HXD_YFHYS.equals(ap_cancel_type)) {
				invIdList.add(apCancelHBean.getSRC_ID());
			}else if (XCAPConstants.HXD_YFHLDC.equals(ap_cancel_type)) {
				invIdList.add(apCancelHBean.getSRC_ID());
				
			}else if (XCAPConstants.HXD_YUFHLDC.equals(ap_cancel_type)) {
				payHIdList.add(apCancelHBean.getSRC_ID());
			}
			for (int i = 0; i < newCancelLs.size(); i++) {
				ApCancelLBean apCancelLBean = newCancelLs.get(i);
				if (XCAPConstants.HXD_YFHYUF.equals(ap_cancel_type)  ) {
					payHIdList.add(apCancelLBean.getTARGET_ID());
				}else if (XCAPConstants.HXD_YFHYS.equals(ap_cancel_type)) {
					arInvIdList.add(apCancelLBean.getTARGET_ID());
				}else if (XCAPConstants.HXD_YFHLDC.equals(ap_cancel_type)) {
					invIdList.add(apCancelLBean.getTARGET_ID());
				}else if (XCAPConstants.HXD_YUFHLDC.equals(ap_cancel_type)) {
					payHIdList.add(apCancelLBean.getTARGET_ID());
				}
			}
			for (int i = 0; i < deleteCancelLs.size(); i++) {
				ApCancelLBean apCancelLBean = deleteCancelLs.get(i);
				if (XCAPConstants.HXD_YFHYUF.equals(ap_cancel_type)  ) {
					payHIdList.add(apCancelLBean.getTARGET_ID());
				}else if (XCAPConstants.HXD_YFHYS.equals(ap_cancel_type)) {
					arInvIdList.add(apCancelLBean.getTARGET_ID());
				}else if (XCAPConstants.HXD_YFHLDC.equals(ap_cancel_type)) {
					invIdList.add(apCancelLBean.getTARGET_ID());
				}else if (XCAPConstants.HXD_YUFHLDC.equals(ap_cancel_type)) {
					payHIdList.add(apCancelLBean.getTARGET_ID());
				}
			}
			
			updateInvGlAmountService.updateApInvGlAmount(invIdList);//修改应付单金额
			updateInvGlAmountService.updateArInvGlAmount(arInvIdList);//修改应收单金额
			apPayBaseService.updatePayAmt(payHIdList);//修改付款单金额
			
			
			//生成凭证
			ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
			avhb.setLedgerId(ledger_id);
			avhb.setApCatCode(XCAPConstants.HXD);
			avhb.setAttchTotal(0);
			avhb.setAccDate(gl_date);
			avhb.setApId(ap_cancel_h_id);

			String v_head_id = "";//凭证头id
			String returnParams = apVoucherService.newVoucher(avhb,language);
			JSONObject json = new JSONObject(returnParams);
			if(json.getString("flag").equals("0")){
				v_head_id = json.getString("headId");
			}
			
			jo.put("AP_CANCEL_H_ID", ap_cancel_h_id);
			jo.put("AP_CANCEL_H_CODE", ap_cancel_h_code);
			jo.put("V_HEAD_ID",v_head_id);
			jo.put("flag", "0");
			//jo.put("msg", "单据和凭证保存成功！");
			jo.put("msg", XipUtil.getMessage(language, "XC_AP_CANCEL_DJHPZBCCG", null));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}

	/*
	 * (non-Javadoc)
	 * <p>Title: deleteCancel</p> 
	 * <p>Description: 删除核销单</p> 
	 * @param deleteRecs
	 * @throws Exception 
	 * @see com.xzsoft.xc.ap.service.ApCancelService#deleteCancel(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteCancel(String deleteRecs,String language) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray deleteRecsJson = new JSONArray(deleteRecs);
			List<String> cancelHIdList = new ArrayList<String>(); //核销单id集合
			List<ApCancelHBean> apCancelHBeanList;//核销单主信息
			
			for (int i = 0; i < deleteRecsJson.length(); i++) {
				JSONObject json = deleteRecsJson.getJSONObject(i);
				cancelHIdList.add(json.getString("AP_CANCEL_H_ID"));
			}
			apCancelHBeanList = apCancelDao.selectCancelH(cancelHIdList);
			
			//验证是否可以删除(已审核的单据不可以删除)
			for (int i = 0; i < apCancelHBeanList.size(); i++) {
				ApCancelHBean bean = apCancelHBeanList.get(i);
				if ("3".equals(bean.getV_STATUS())) {// 3 是审核通过
					apCancelHBeanList.remove(i);
				}
			}
			if (apCancelHBeanList.size() == 0) {
				//throw new Exception("已审核的单据不可删除！");
				throw new Exception(XipUtil.getMessage(language, "XC_AP_CANCEL_YSHDDJBKSC", null));
			}
			
			
			List<String> invHIdList = new ArrayList<String>();
			List<String> arInvHIdList = new ArrayList<String>();
			List<String> payHIdList = new ArrayList<String>();
			List<ApInvTransBean> deleteApTrans = new ArrayList<ApInvTransBean>();
			List<ArInvTransBean> deleteArTrans = new ArrayList<ArInvTransBean>();
			//修改相应的单据的金额
			for (int i = 0; i < apCancelHBeanList.size(); i++) {
				ApCancelHBean apCancelHBean = apCancelHBeanList.get(i);
				String ap_cancel_h_id = apCancelHBean.getAP_CANCEL_H_ID();//核销单id
				String ap_cancel_type = apCancelHBean.getAP_CANCEL_TYPE();//核销类型
				List<ApCancelLBean> apCancelLBeanList = apCancelDao.selectCancelLByCancelHId(ap_cancel_h_id);//查找核销单行信息
				
				if (XCAPConstants.HXD_YFHYUF.equals(ap_cancel_type)  ) {
					invHIdList.add(apCancelHBean.getSRC_ID());
				}else if (XCAPConstants.HXD_YFHYS.equals(ap_cancel_type)) {
					invHIdList.add(apCancelHBean.getSRC_ID());
				}else if (XCAPConstants.HXD_YFHLDC.equals(ap_cancel_type)) {
					invHIdList.add(apCancelHBean.getSRC_ID());
				}else if (XCAPConstants.HXD_YUFHLDC.equals(ap_cancel_type)) {
					payHIdList.add(apCancelHBean.getSRC_ID());
				}
				
				for (int j = 0; j < apCancelLBeanList.size(); j++) {
					ApCancelLBean apCancelLBean = apCancelLBeanList.get(i);
					if (XCAPConstants.HXD_YFHYUF.equals(ap_cancel_type)  ) {
						payHIdList.add(apCancelLBean.getTARGET_ID());
					}else if (XCAPConstants.HXD_YFHYS.equals(ap_cancel_type)) {
						arInvHIdList.add(apCancelLBean.getTARGET_ID());
					}else if (XCAPConstants.HXD_YFHLDC.equals(ap_cancel_type)) {
						invHIdList.add(apCancelLBean.getTARGET_ID());
					}else if (XCAPConstants.HXD_YUFHLDC.equals(ap_cancel_type)) {
						payHIdList.add(apCancelLBean.getTARGET_ID());
					}
				}
				
				//创建要删除的交易明细
				for (int j = 0; j < apCancelLBeanList.size(); j++) {
					ApInvTransBean apInvTransBean = new ApInvTransBean();
					apInvTransBean.setSOURCE_ID(apCancelLBeanList.get(j).getAP_CANCEL_H_ID());
					apInvTransBean.setSOURCE_DTL_ID(apCancelLBeanList.get(j).getAP_CANCEL_L_ID());
					apInvTransBean.setAP_DOC_CAT_CODE(apCancelLBeanList.get(j).getAP_CANCEL_TYPE());
					deleteApTrans.add(apInvTransBean);
					if (XCAPConstants.HXD_YFHYS.equals(ap_cancel_type)) {
						ArInvTransBean arInvTransBean = new ArInvTransBean();
						arInvTransBean.setSOURCE_ID(apCancelLBeanList.get(j).getAP_CANCEL_H_ID());
						arInvTransBean.setSOURCE_DTL_ID(apCancelLBeanList.get(j).getAP_CANCEL_L_ID());
						arInvTransBean.setAR_DOC_CAT_CODE(apCancelLBeanList.get(j).getAP_CANCEL_TYPE());
						deleteArTrans.add(arInvTransBean);
					}
				}
				
			}
			
			//删除核销单行
			apCancelDao.deleteCancelLsByCancelHIds(cancelHIdList);
			//删除核销单
			apCancelDao.deleteCancelHs(cancelHIdList);
			
			//根据删除交易明细
			aparTransDao.deleteApTrans(deleteApTrans);
			aparTransDao.deleteArTrans(deleteArTrans);
			
			updateInvGlAmountService.updateApInvGlAmount(invHIdList);//修改应付单金额
			updateInvGlAmountService.updateArInvGlAmount(arInvHIdList);//修改应收单金额
			apPayBaseService.updatePayAmt(payHIdList);//修改付款单金额
			
			//删除凭证
			JSONArray headArray = new JSONArray();//凭证头id的集合
			for (int i = 0; i < apCancelHBeanList.size(); i++) {
				JSONObject headJson = new JSONObject();
				headJson.put("V_HEAD_ID", apCancelHBeanList.get(i).getV_HEAD_ID());
				headArray.put(headJson);
			}
			voucherHandlerService.doDelVouchers(headArray.toString());
			
			jo.put("flag", "0");
			jo.put("msg", XipUtil.getMessage(language, "XC_AP_CANCEL_CGSC", null)+apCancelHBeanList.size()+XipUtil.getMessage(language, "XC_AP_CANCEL_TDJ", null));
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return jo;

	}

	/**
	 * @Title: createApTrans
	 * @Description: 创建交易明细对象
	 * @param hBean 核销单主信息
	 * @param lBean 核销单行信息
	 * @param hl 对于主还是对于行的交易明细（H、L）
	 * @return  设定文件 
	 * @return  ApInvTransBean 返回类型 
	 * @throws
	 */
	public ApInvTransBean createApTrans(ApCancelHBean hBean,ApCancelLBean lBean,String hl){
		String cancelType = hBean.getAP_CANCEL_TYPE();
		
		ApInvTransBean apInvTransBean = new ApInvTransBean();
		
		apInvTransBean.setTRANS_ID(UUID.randomUUID().toString());
		apInvTransBean.setSOURCE_ID(lBean.getAP_CANCEL_H_ID());
		apInvTransBean.setSOURCE_DTL_ID(lBean.getAP_CANCEL_L_ID());
		apInvTransBean.setGL_DATE(hBean.getGL_DATE());
		apInvTransBean.setSOURCE_TAB("XC_AP_CANCEL_L");
		apInvTransBean.setAP_DOC_CAT_CODE(hBean.getAP_CANCEL_TYPE());
		apInvTransBean.setAP_DOC_CODE(hBean.getAP_CANCEL_H_CODE());
		apInvTransBean.setDESCRIPTION(hBean.getDESCRIPTION());
		
		if ("H".equals(hl)) {
			if(XCAPConstants.HXD_YFHYUF.equals(cancelType) || XCAPConstants.HXD_YFHYS.equals(cancelType) || XCAPConstants.HXD_YFHLDC.equals(cancelType)){
				apInvTransBean.setAP_INV_GL_H_ID(hBean.getSRC_ID());
			}
		}else if ("L".equals(hl)){
			if (XCAPConstants.HXD_YFHLDC.equals(cancelType)) {
				apInvTransBean.setAP_INV_GL_H_ID(lBean.getTARGET_ID());
			}//应付核应收对L的不在此处
		}
		
		if ("H".equals(hl)) {
			if (XCAPConstants.HXD_YFHYUF.equals(cancelType)) {
				apInvTransBean.setDR_AMT(lBean.getTARGET_AMT());
				apInvTransBean.setCR_AMT(0.00);
				apInvTransBean.setREQ_AMT(0.00);
			}else if (XCAPConstants.HXD_YFHYS.equals(cancelType)) {
				apInvTransBean.setDR_AMT(lBean.getTARGET_AMT());
				apInvTransBean.setCR_AMT(0.00);
				apInvTransBean.setREQ_AMT(0.00);
			}else if (XCAPConstants.HXD_YFHLDC.equals(cancelType)) {
				apInvTransBean.setDR_AMT(0.00);
				apInvTransBean.setCR_AMT(lBean.getTARGET_AMT());
				apInvTransBean.setREQ_AMT(0.00);
			}else if (XCAPConstants.HXD_YUFHLDC.equals(cancelType)) {
				apInvTransBean.setDR_AMT(0.00);
				apInvTransBean.setCR_AMT(lBean.getTARGET_AMT());
				apInvTransBean.setREQ_AMT(0.00);
			}
		}else{
			if (XCAPConstants.HXD_YFHLDC.equals(cancelType)) {
				apInvTransBean.setDR_AMT(0.00);
				apInvTransBean.setCR_AMT(Math.abs(lBean.getTARGET_AMT())*(-1));
				apInvTransBean.setREQ_AMT(0.00);
			}
		}
		
		apInvTransBean.setTRANS_STATUS("0");
		apInvTransBean.setCREATION_DATE(hBean.getLAST_UPDATE_DATE());
		apInvTransBean.setCREATED_BY(hBean.getLAST_UPDATED_BY());
		apInvTransBean.setLAST_UPDATE_DATE(hBean.getLAST_UPDATE_DATE());
		apInvTransBean.setLAST_UPDATED_BY(hBean.getLAST_UPDATED_BY());
		return apInvTransBean;
	}
	
	/**
	 * @Title: createArTrans
	 * @Description: 创建应收交易明细
	 * @param hBean 核销单主信息
	 * @param lBean 核销单行信息
	 * @return  设定文件 
	 * @return  ArInvTransBean 返回类型 
	 * @throws
	 */
	public ArInvTransBean createArTrans(ApCancelHBean hBean,ApCancelLBean lBean){
		ArInvTransBean arInvTransBean = new ArInvTransBean();
		arInvTransBean.setTRANS_ID(UUID.randomUUID().toString()); // 明细id
		arInvTransBean.setSOURCE_ID(hBean.getAP_CANCEL_H_ID()); // 来源id
		arInvTransBean.setSOURCE_DTL_ID(lBean.getAP_CANCEL_L_ID()); // 来源明细id
		arInvTransBean.setAR_INV_GL_H_ID(lBean.getTARGET_ID()); // 应收单id
		arInvTransBean.setAR_PAY_H_ID(""); // 收款单id
		arInvTransBean.setGL_DATE(hBean.getGL_DATE()); // 入账日期
		arInvTransBean.setSOURCE_TAB("XC_AP_CANCEL_L"); // 来源表
		arInvTransBean.setAR_DOC_CAT_CODE(hBean.getAP_CANCEL_TYPE()); // 单据类型
		arInvTransBean.setAR_CONTRACT_ID(""); // 合同id
		arInvTransBean.setCUSTOMER_ID(""); // 客户id
		arInvTransBean.setDR_AMT(0); // 借方金额
		arInvTransBean.setCR_AMT(lBean.getTARGET_AMT()); // 贷方金额
		arInvTransBean.setAMOUNT(0); // 应收余额
		arInvTransBean.setTRANS_STATUS("0"); // 状态
		arInvTransBean.setCCID(""); // 应收组合科目id
		arInvTransBean.setCREATION_DATE(hBean.getLAST_UPDATE_DATE()); // 创建日期
		arInvTransBean.setCREATED_BY(hBean.getLAST_UPDATED_BY()); // 创建人
		arInvTransBean.setLAST_UPDATE_DATE(hBean.getLAST_UPDATE_DATE()); // 修改日期
		arInvTransBean.setLAST_UPDATED_BY(hBean.getLAST_UPDATED_BY()); // 修改人
		return arInvTransBean;
	}
}
