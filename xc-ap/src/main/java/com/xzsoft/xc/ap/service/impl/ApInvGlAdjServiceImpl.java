package com.xzsoft.xc.ap.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ap.dao.ApInvGlAdjDao;
import com.xzsoft.xc.ap.modal.ApInvGlAdj;
import com.xzsoft.xc.ap.modal.ApInvGlHBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.ap.service.ApInvGlBaseService;
import com.xzsoft.xc.ap.service.ApVoucherService;
import com.xzsoft.xc.ap.service.ApInvGlAdjService;
import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
 
/** 
 * @className ArInvGlAdjService
 * @Description 调整单Service
 * @author 张龙
 * @version 创建时间：2016年7月22日 上午9:22:58 
 */
@Service("apInvGlAdjService")
public class ApInvGlAdjServiceImpl implements ApInvGlAdjService {
	@Resource
	ApInvGlAdjDao dao;
	@Resource
	ApVoucherService voucherService;
	@Resource
	ApInvGlBaseService apInvGlBaseService;
	//日志记录
	Logger log = Logger.getLogger(ApInvGlAdjServiceImpl.class.getName());
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	ApArTransDao apArTransDao;
	@Resource
	UpdateInvGlAmountService updateInvGlAmountService;
	/**
	 * @Title saveInvAdj
	 * @Description: 保存调整单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject saveInvAdj(String params,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			
			JSONObject paramsJo = new JSONObject(params);
			String glAdjId = paramsJo.getString("GL_ADJ_ID");//得到调整单ID
			String vendorId = paramsJo.getString("VENDOR_ID");//供应商
			String invGlHId = paramsJo.getString("AP_INV_GL_H_ID");//应收单
			String glDate = paramsJo.getString("GL_DATE");//业务日期
			String toCCID = paramsJo.getString("TO_CCID");//调整科目ID
			String drOrCr = paramsJo.getString("DR_OR_CR");//借方，贷方
			String adjAmt = paramsJo.getString("ADJ_AMT");//调整金额
			String description = paramsJo.getString("DESCRIPTION");//摘要
			String drAmt = paramsJo.getString("DR_AMT");//借方金额
			String crAmt ="";//贷方金额
			if(paramsJo.getString("CR_AMT")==null){
				crAmt = "0" ;
			}else{
				crAmt = paramsJo.getString("CR_AMT");
			}
			
			
			ApInvGlAdj apInvGlAdj = new ApInvGlAdj();	
			ApInvTransBean apInvTransBean = new ApInvTransBean();//交易明细表
			
			//判断新增还是修改
			if(dao.isAddOrEdit(glAdjId)==null){//新增
				String glAdjCode = paramsJo.getString("GL_ADJ_CODE");//编码
				String ledgerId = paramsJo.getString("LEDGER_ID");//账簿
				apInvGlAdj.setGL_ADJ_ID(glAdjId);
				apInvGlAdj.setGL_ADJ_CODE(glAdjCode);
				apInvGlAdj.setLEDGER_ID(ledgerId);
				apInvGlAdj.setVENDOR_ID(vendorId);
				apInvGlAdj.setAP_INV_GL_H_ID(invGlHId);
				apInvGlAdj.setGL_DATE(sdf.parse(glDate));
				apInvGlAdj.setTO_CCID(toCCID);
				apInvGlAdj.setDR_OR_CR(drOrCr);
				apInvGlAdj.setADJ_AMT(Double.valueOf(adjAmt));
				//apInvGlAdj.setV_HEAD_ID(vHeadId);
				apInvGlAdj.setV_STATUS("1");
				apInvGlAdj.setDESCRIPTION(description);
				apInvGlAdj.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				apInvGlAdj.setCREATED_BY(CurrentSessionVar.getUserId());
				apInvGlAdj.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				apInvGlAdj.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());	
				dao.addApAdjInfo(apInvGlAdj);//新增调整单
				
			
				String transId = java.util.UUID.randomUUID().toString();
				
				apInvTransBean.setTRANS_ID(transId);
				apInvTransBean.setSOURCE_ID(glAdjId);
				apInvTransBean.setAP_INV_GL_H_ID(invGlHId);
				apInvTransBean.setGL_DATE(sdf.parse(glDate));
				apInvTransBean.setAP_DOC_CAT_CODE("YUETZD");
				apInvTransBean.setDR_AMT(Double.valueOf(drAmt));
				apInvTransBean.setCR_AMT(Double.valueOf(crAmt));
				apInvTransBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				apInvTransBean.setCREATED_BY(CurrentSessionVar.getUserId());
				apInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				apInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				apInvTransBean.setAP_DOC_CODE(glAdjCode);
				apInvTransBean.setDESCRIPTION(description);
				apInvTransBean.setSOURCE_TAB("XC_AP_INV_GL_ADJ");
				List<ApInvTransBean> list = new ArrayList<ApInvTransBean>();
				list.add(apInvTransBean);
				apArTransDao.insertApTrans(list);//添加交易明细
				HashMap<String, Object> map = new HashMap<String, Object>();
				List<String> apInvGlIdList = new ArrayList<String>();
				apInvGlIdList.add(invGlHId);
				updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);//更新应付单
				ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
				avhb.setLedgerId(ledgerId);
				avhb.setApCatCode("YUETZD");
				avhb.setAccDate(sdf.parse(glDate));
				avhb.setApId(glAdjId);
				avhb.setAttchTotal(0);
				try {
					String returnParams = voucherService.newVoucher(avhb,lang);
					JSONObject returnJson = new JSONObject(returnParams);
					jo.put("headId", returnJson.getString("headId"));
				} catch (Exception e) {
					jo.put("flag", "1");
					throw e;
				}
				
				jo.put("flag", "0");
			}else {//修改调整单
				String gladjcode = paramsJo.getString("GL_ADJ_CODE");//调整单编号
				String ledgerid = paramsJo.getString("LEDGER_ID");//账簿ID
				apInvGlAdj.setGL_ADJ_ID(glAdjId);
				apInvGlAdj.setGL_ADJ_CODE(gladjcode);
				apInvGlAdj.setLEDGER_ID(ledgerid);
				apInvGlAdj.setVENDOR_ID(vendorId);
				apInvGlAdj.setAP_INV_GL_H_ID(invGlHId);
				apInvGlAdj.setGL_DATE(sdf.parse(glDate));
				apInvGlAdj.setTO_CCID(toCCID);
				apInvGlAdj.setDR_OR_CR(drOrCr);
				apInvGlAdj.setADJ_AMT(Double.valueOf(adjAmt));
				apInvGlAdj.setDESCRIPTION(description);
				apInvGlAdj.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				apInvGlAdj.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				
				
			  //修改往来明细
				String dramt = paramsJo.getString("DR_AMT");//贷方金额
				String cramt ="";//借方金额
				if(paramsJo.getString("CR_AMT")==null){
					cramt = "0" ;
				}else{
					cramt = paramsJo.getString("CR_AMT");
				}
				String ccid = paramsJo.getString("TO_CCID");//调整科目ID
				apInvTransBean.setSOURCE_ID(glAdjId);
				apInvTransBean.setAP_INV_GL_H_ID(invGlHId);
				apInvTransBean.setDR_AMT(0);
				apInvTransBean.setCR_AMT(Double.valueOf(crAmt));
				apInvTransBean.setCCID(ccid);
				apInvTransBean.setAP_DOC_CAT_CODE("YUETZD");
				apInvTransBean.setDESCRIPTION(description);
				apInvTransBean.setGL_DATE(sdf.parse(glDate));
				apInvTransBean.setSOURCE_TAB("XC_AP_INV_GL_ADJ");
				apInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				apInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				dao.updateInvGlhadj(apInvGlAdj);//更新调整单
				List<ApInvTransBean> list = new ArrayList<ApInvTransBean>();
				list.add(apInvTransBean);
				apArTransDao.updateApTrans(list);//添加交易明细
				HashMap<String, Object> map = new HashMap<String, Object>();
				List<String> apInvGlIdList = new ArrayList<String>();
				apInvGlIdList.add(invGlHId);
				updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);//更新应付单
				ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
				avhb.setLedgerId(ledgerid);
				avhb.setApCatCode("YUETZD");
				avhb.setAccDate(sdf.parse(glDate));
				avhb.setApId(glAdjId);
				avhb.setAttchTotal(0);
				try {
					String returnParams = voucherService.newVoucher(avhb,lang);
					JSONObject returnJson = new JSONObject(returnParams);
					jo.put("headId", returnJson.getString("headId"));
				} catch (Exception e) {
					jo.put("flag", "1");
					throw e;
				}
				jo.put("flag", "0");
							
			}
			
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		
		return jo;
	}

	/**
	 * @Title deleteAdj
	 * @Description: 删除调整单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject deleteAdj(String rows,String apInfo,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray ja = new JSONArray(apInfo);
			apInvGlBaseService.deleteApDoc(ja,lang);
			/*JSONArray arrayJo = new JSONArray(rows);
			List<String> list = new ArrayList<String>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
			List<String> apInvGlIdList = new ArrayList<String>();
			for(int i=0;i<arrayJo.length();i++){
				JSONObject ob = arrayJo.getJSONObject(i);
				list.add(ob.getString("GL_ADJ_ID"));
				ApInvTransBean transBean = new ApInvTransBean();
				transBean.setSOURCE_ID(ob.getString("GL_ADJ_ID"));
				transBean.setAP_DOC_CAT_CODE(ob.getString("AP_DOC_CAT_CODE"));
				transList.add(transBean);
				apInvGlIdList.add(ob.getString("AP_INV_GL_H_ID"));
			}
			dao.delApInvAdj(list);
			apArTransDao.deleteApTrans(transList);
			updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);*/
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("msg", e.getMessage());
			jo.put("flag", "1");
			throw e;
			}
		return jo;
	}
	/**
	 * @Title checkArVoucher
	 * @Description: 审核
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	public JSONObject checkArVoucher(String apInfo,String lang) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray ja = new JSONArray(apInfo);
			voucherService.checkVoucher(ja,lang);
			jo.put("flag", "0");
			jo.put("msg", XipUtil.getMessage(lang, "XC_AP_ADJ_CHECKVOUCHER", null));
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title cancelCheckArVoucher
	 * @Description: 取消审核
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	public JSONObject cancelCheckArVoucher(String apInfo,String lang) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray ja = new JSONArray(apInfo);
			voucherService.cancelCheckVoucher(ja,lang);
			jo.put("flag", "0");
			jo.put("msg", XipUtil.getMessage(lang, "XC_AP_ADJ_CANCELVOUCHER", null));
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
			}
			return jo;
		}
}

