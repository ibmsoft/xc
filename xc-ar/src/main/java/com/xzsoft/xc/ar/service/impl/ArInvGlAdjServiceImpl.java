package com.xzsoft.xc.ar.service.impl;

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

import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.ar.dao.ArInvGlAdjDao;
import com.xzsoft.xc.ar.modal.ArInvGlAdj;
import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;
import com.xzsoft.xc.ar.service.ArDocsAndVoucherService;
import com.xzsoft.xc.ar.service.ArInvGlAdjService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
 
/** 
 * @className ArInvGlAdjService
 * @Description 调整单Service
 * @author 张龙
 * @version 创建时间：2016年7月22日 上午9:22:58 
 */
@Service("arInvGlAdjService")
public class ArInvGlAdjServiceImpl implements ArInvGlAdjService {
	@Resource
	ArInvGlAdjDao dao;
	@Resource
	ArDocsAndVoucherService voucherService;
	//日志记录
	Logger log = Logger.getLogger(ArInvGlAdjServiceImpl.class.getName());
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	@Resource
	ApArTransDao apArTransDao;
	@Resource
	UpdateInvGlAmountService updateInvGlAmountService;
	/**
	 * @Title:saveInvAdj
	 * @Description: 调整单保存
	 * 参数格式:
	 * @param java.lang.String
	 * @return import org.codehaus.jettison.json.JSONObject;
	 * @throws java.lang.Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveInvAdj(String params,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			
			JSONObject paramsJo = new JSONObject(params);
			String glAdjId = paramsJo.getString("GL_ADJ_ID");//得到调整单ID
			String customerId = paramsJo.getString("CUSTOMER_ID");//客户
			String invGlHId = paramsJo.getString("AR_INV_GL_H_ID");//应收单
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
			
			
			ArInvGlAdj arInvGlAdj = new ArInvGlAdj();	
			ArInvTransBean arInvTransBean = new ArInvTransBean();//交易明细表
			List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
			List<String> arInvGlIdList = new ArrayList<String>();
			//判断新增还是修改
			if(dao.isAddOrEdit(glAdjId)==null){//新增
				String glAdjCode = paramsJo.getString("GL_ADJ_CODE");//编码
				String ledgerId = paramsJo.getString("LEDGER_ID");//账簿
				arInvGlAdj.setGL_ADJ_ID(glAdjId);
				arInvGlAdj.setGL_ADJ_CODE(glAdjCode);
				arInvGlAdj.setLEDGER_ID(ledgerId);
				arInvGlAdj.setCUSTOMER_ID(customerId);
				arInvGlAdj.setAR_INV_GL_H_ID(invGlHId);
				arInvGlAdj.setGL_DATE(sdf.parse(glDate));
				arInvGlAdj.setTO_CCID(toCCID);
				arInvGlAdj.setDR_OR_CR(drOrCr);
				arInvGlAdj.setADJ_AMT(Double.parseDouble(adjAmt));
				arInvGlAdj.setV_STATUS("1");
				arInvGlAdj.setDESCRIPTION(description);
				arInvGlAdj.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				arInvGlAdj.setCREATED_BY(CurrentSessionVar.getUserId());
				arInvGlAdj.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				arInvGlAdj.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());	
				dao.addArAdjInfo(arInvGlAdj);//新增调整单
				
			
				String transId = java.util.UUID.randomUUID().toString();
				
				arInvTransBean.setTRANS_ID(transId);
				arInvTransBean.setSOURCE_ID(glAdjId);
				arInvTransBean.setAR_INV_GL_H_ID(invGlHId);
				arInvTransBean.setAR_DOC_CAT_CODE("YUETZD");
				arInvTransBean.setGL_DATE(sdf.parse(glDate));
				arInvTransBean.setDR_AMT(Double.valueOf(drAmt));
				arInvTransBean.setCR_AMT(Double.valueOf(crAmt));
				arInvTransBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				arInvTransBean.setCREATED_BY(CurrentSessionVar.getUserId());
				arInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				arInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				arInvTransBean.setAR_DOC_CODE(glAdjCode);
				arInvTransBean.setDESCRIPTION(description);
				arInvTransBean.setSOURCE_TAB("XC_AR_INV_GL_ADJ");
				transList.add(arInvTransBean);
				apArTransDao.insertArTrans(transList);
				arInvGlIdList.add(invGlHId);
				updateInvGlAmountService.updateArInvGlAmount(arInvGlIdList);
				ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
				avhb.setLedgerId(ledgerId);
				avhb.setArCatCode("YUETZD");
				avhb.setAccDate(sdf.parse(glDate));
				avhb.setArId(glAdjId);
				avhb.setAttchTotal(0);
				try {
					String returnParams = voucherService.newArVoucher(avhb,lang);
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
				arInvGlAdj.setGL_ADJ_ID(glAdjId);
				arInvGlAdj.setGL_ADJ_CODE(gladjcode);
				arInvGlAdj.setLEDGER_ID(ledgerid);
				arInvGlAdj.setCUSTOMER_ID(customerId);
				arInvGlAdj.setAR_INV_GL_H_ID(invGlHId);
				arInvGlAdj.setGL_DATE(sdf.parse(glDate));
				arInvGlAdj.setTO_CCID(toCCID);
				arInvGlAdj.setDR_OR_CR(drOrCr);
				arInvGlAdj.setADJ_AMT(Double.parseDouble(adjAmt));
				arInvGlAdj.setDESCRIPTION(description);
				arInvGlAdj.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				arInvGlAdj.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				
			  //修改往来明细
				String dramt = paramsJo.getString("DR_AMT");//贷方金额
				String cramt ="";//借方金额
				if(paramsJo.getString("CR_AMT")==null){
					cramt = "0" ;
				}else{
					cramt = paramsJo.getString("CR_AMT");
				}
				String ccid = paramsJo.getString("TO_CCID");//调整科目ID
				arInvTransBean.setSOURCE_ID(glAdjId);
				arInvTransBean.setAR_INV_GL_H_ID(invGlHId);
				arInvTransBean.setDR_AMT(Double.valueOf(dramt));
				arInvTransBean.setCR_AMT(Double.valueOf(cramt));
				arInvTransBean.setAR_DOC_CAT_CODE("YUETZD");
				arInvTransBean.setGL_DATE(sdf.parse(glDate));
				arInvTransBean.setCCID(ccid);
				arInvTransBean.setDESCRIPTION(description);
				arInvTransBean.setSOURCE_TAB("XC_AR_INV_GL_ADJ");
				arInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				arInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				transList.add(arInvTransBean);
				
			
				//修改调整单
				dao.updateInvGlhadj(arInvGlAdj);
				apArTransDao.updateArTrans(transList);//更新交易明细
				arInvGlIdList.add(invGlHId);
				//修改应收单金额
				updateInvGlAmountService.updateArInvGlAmount(arInvGlIdList);
				ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
				avhb.setLedgerId(ledgerid);
				avhb.setArCatCode("YUETZD");
				avhb.setAccDate(sdf.parse(glDate));
				avhb.setArId(glAdjId);
				avhb.setAttchTotal(0);
				try {
					String returnParams = voucherService.newArVoucher(avhb,lang);
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
	 * @Title:deleteAdj
	 * @Description: 删除调整单
	 * 参数格式:
	 * @param （java.lang.String,java.lang.String）
	 * @return import org.codehaus.jettison.json.JSONObject;
	 * @throws java.lang.Exception
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject deleteAdj(String rows,String arInfo,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray ja = new JSONArray(arInfo);
			voucherService.deleteArDoc(ja,lang);
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("msg", e.getMessage());
			jo.put("flag", "1");
			throw e;
			}
		return jo;
	}
	/**
	 * @Title:checkArVoucher
	 * @Description: 审核
	 * 参数格式:
	 * @param java.lang.String）
	 * @return import org.codehaus.jettison.json.JSONObject;
	 * @throws java.lang.Exception
	 */
	@Override
	public JSONObject checkArVoucher(String arInfo,String lang) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray ja = new JSONArray(arInfo);
			voucherService.checkArVoucher(ja,lang);
			jo.put("flag", "0");
			jo.put("msg", XipUtil.getMessage(lang, "XC_AR_CHECKVOUCHER", null));
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * cancelCheckArVoucher
	 * @Description: 取消审核
	 * 参数格式:
	 * @param java.lang.String）
	 * @return import org.codehaus.jettison.json.JSONObject;
	 * @throws java.lang.Exception
	 */
	@Override
	public JSONObject cancelCheckArVoucher(String arInfo,String lang) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONArray ja = new JSONArray(arInfo);
			voucherService.cancelCheckArVoucher(ja,lang);
			jo.put("flag", "0");
			jo.put("msg", XipUtil.getMessage(lang, "XC_AR_CANCHEVOUCHER", null));
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
			}
			return jo;
		}
}

