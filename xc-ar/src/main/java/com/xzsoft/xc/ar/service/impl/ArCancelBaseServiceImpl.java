package com.xzsoft.xc.ar.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.ar.action.ArCancelBaseAction;
import com.xzsoft.xc.ar.dao.ArCancelBaseDao;
import com.xzsoft.xc.ar.dao.ArPayBaseDao;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.modal.ArCancelHBean;
import com.xzsoft.xc.ar.modal.ArCancelLBean;
import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;
import com.xzsoft.xc.ar.service.ArCancelBaseService;
import com.xzsoft.xc.ar.service.ArDocsAndVoucherService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
import com.xzsoft.xip.wf.common.util.WFUtil;

/** 
 * @className ArCancelBaseServiceImpl
 * @Description 核销单Service实现类
 * @author 韦才文
 * @version 创建时间：2016年7月13日 上午9:58:50 
 */
@Service("arCancelBaseService")
public class ArCancelBaseServiceImpl implements ArCancelBaseService {
	@Resource
	ArCancelBaseDao dao;
	@Resource
	XCARCommonDao xcARCommondao;
	@Resource
	RuleService ruleService;
	@Resource
	ArDocsAndVoucherService voucherService;
	@Resource
	UpdateInvGlAmountService updateInvGlAmountService;
	@Resource
	ApArTransDao apArTransDao;
	@Resource
	ArPayBaseDao  arPayBaseDao;
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//日志记录器
	private static Logger log = Logger.getLogger(ArCancelBaseServiceImpl.class.getName());
/*
 * (non-Javadoc)保存明细
 * @see com.xzsoft.xc.ar.service.ArCancelBaseService#saveDtl(java.lang.String, java.lang.String)
 */
@Override
@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public void saveDtl(JSONArray jsonGrid,String arCancelType,String deleteDtl,String cancelCode,String glDate,String description) throws Exception{
	List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
	List<ApInvTransBean> AptransList = new ArrayList<ApInvTransBean>();
	List<ArInvTransBean> editTransList = new ArrayList<ArInvTransBean>();
	List<ApInvTransBean> ApeditTransList = new ArrayList<ApInvTransBean>();
	List<ArInvTransBean> delTransList = new ArrayList<ArInvTransBean>();
	List<ApInvTransBean> ApdelTransList = new ArrayList<ApInvTransBean>();
	List<String> apInvList = new ArrayList<String>();
	List<String> arInvList = new ArrayList<String>();
	List<String> payIdList = new ArrayList<String>();
	try {
		JSONArray deleteArray = new JSONArray(deleteDtl);//删除的核销行信息
		for(int i=0;i<jsonGrid.length();i++){
			JSONObject json = jsonGrid.getJSONObject(i);
			String transId = java.util.UUID.randomUUID().toString();//交易明细主ID
			String sourceId = json.getString("AR_CANCEL_H_ID");//来源ID
			String sourceDtlId = json.getString("AR_CANCEL_L_ID");//来源明细ID
			String tragetId = json.getString("TARGET_ID");//核销目标单号
			String tragetAmt = json.getString("TARGET_AMT");//TARGET_AMT
			//String description = json.getString("DESCRIPTION");
			ArInvTransBean arInvTransBean = new ArInvTransBean();
			arInvTransBean.setTRANS_ID(transId);
			arInvTransBean.setSOURCE_ID(sourceId);
			arInvTransBean.setSOURCE_DTL_ID(sourceDtlId);
			arInvTransBean.setCREATED_BY(CurrentSessionVar.getUserId());
			arInvTransBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
			arInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
			arInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
			arInvTransBean.setSOURCE_TAB("XC_AR_CANCEL_L");
			arInvTransBean.setAR_DOC_CODE(cancelCode);
			arInvTransBean.setDESCRIPTION(description);
			arInvTransBean.setGL_DATE(sdf.parse(glDate));
			ArCancelLBean cancelLBean = new ArCancelLBean();
			if(arCancelType.equals("YSHYUS")){//应收核预收
				arInvTransBean.setAR_DOC_CAT_CODE("YSHYUS");
				if(dao.getCancelLByLId(sourceDtlId)==null){//新增行表
					arInvTransBean.setDR_AMT(0);
					arInvTransBean.setCR_AMT(Double.parseDouble(tragetAmt));
					cancelLBean.setAR_CANCEL_H_ID(sourceId);
					cancelLBean.setAR_CANCEL_L_ID(sourceDtlId);
					cancelLBean.setAR_CANCEL_TYPE(arCancelType);
					cancelLBean.setTARGET_ID(tragetId);
					cancelLBean.setTARGET_AMT(Double.parseDouble(tragetAmt));
					cancelLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					cancelLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addCancelLInfo(cancelLBean);//添加核销单行表信息
					arInvTransBean.setAR_PAY_H_ID(tragetId);
					transList.add(arInvTransBean);//添加交易明细
					payIdList.add(tragetId);
					
				}else{//修改
					ArCancelLBean resultBean = dao.getCancelLByLId(sourceDtlId);
					payIdList.add(tragetId);
					arInvTransBean.setDR_AMT(0);
					arInvTransBean.setCR_AMT(Double.parseDouble(tragetAmt));
					cancelLBean.setAR_CANCEL_H_ID(sourceId);
					cancelLBean.setAR_CANCEL_L_ID(sourceDtlId);
					cancelLBean.setAR_CANCEL_TYPE(arCancelType);
					cancelLBean.setTARGET_ID(tragetId);
					cancelLBean.setTARGET_AMT(Double.parseDouble(tragetAmt));
					cancelLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					cancelLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.editCancelLInfo(cancelLBean);//添加核销单行表信息
					arInvTransBean.setAR_PAY_H_ID(tragetId);
					editTransList.add(arInvTransBean);//修改交易明细
				}
				
			}else if(arCancelType.equals("YSHLDC")){//应收红蓝对冲
				arInvTransBean.setAR_DOC_CAT_CODE("YSHLDC");
				if(dao.getCancelLByLId(sourceDtlId)==null){//新增行表
					arInvTransBean.setDR_AMT(-Double.parseDouble(tragetAmt));
					arInvTransBean.setCR_AMT(0);
					cancelLBean.setAR_CANCEL_H_ID(sourceId);
					cancelLBean.setAR_CANCEL_L_ID(sourceDtlId);
					cancelLBean.setAR_CANCEL_TYPE(arCancelType);
					cancelLBean.setTARGET_ID(tragetId);
					cancelLBean.setTARGET_AMT(Math.abs(Double.parseDouble(tragetAmt)));
					cancelLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					cancelLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addCancelLInfo(cancelLBean);//添加核销单行表信息
					arInvTransBean.setAR_INV_GL_H_ID(tragetId);
					transList.add(arInvTransBean);
					arInvList.add(tragetId);
				}else{//修改
					arInvList.add(tragetId);
					arInvTransBean.setDR_AMT(-Double.parseDouble(tragetAmt));
					arInvTransBean.setCR_AMT(0);
					cancelLBean.setAR_CANCEL_H_ID(sourceId);
					cancelLBean.setAR_CANCEL_L_ID(sourceDtlId);
					cancelLBean.setAR_CANCEL_TYPE(arCancelType);
					cancelLBean.setTARGET_ID(tragetId);
					cancelLBean.setTARGET_AMT(Math.abs(Double.parseDouble(tragetAmt)));
					cancelLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					cancelLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.editCancelLInfo(cancelLBean);//添加核销单行表信息
					arInvTransBean.setAR_INV_GL_H_ID(tragetId);
					editTransList.add(arInvTransBean);//修改交易明细
				}
				
			}else if(arCancelType.equals("YUSHLDC")){//预收红蓝对冲
				arInvTransBean.setAR_DOC_CAT_CODE("YUSHLDC");
				if(dao.getCancelLByLId(sourceDtlId)==null){//新增行表
					arInvTransBean.setDR_AMT(0);
					arInvTransBean.setCR_AMT(-Double.parseDouble(tragetAmt));
					cancelLBean.setAR_CANCEL_H_ID(sourceId);
					cancelLBean.setAR_CANCEL_L_ID(sourceDtlId);
					cancelLBean.setAR_CANCEL_TYPE(arCancelType);
					cancelLBean.setTARGET_ID(tragetId);
					cancelLBean.setTARGET_AMT(Math.abs(Double.parseDouble(tragetAmt)));
					cancelLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					cancelLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addCancelLInfo(cancelLBean);//添加核销单行表信息
					arInvTransBean.setAR_PAY_H_ID(tragetId);
					transList.add(arInvTransBean);//添加交易明细
					payIdList.add(tragetId);
				}else{//修改
					ArCancelLBean resultBean = dao.getCancelLByLId(sourceDtlId);
					payIdList.add(tragetId);
					arInvTransBean.setDR_AMT(0);
					arInvTransBean.setCR_AMT(-Double.parseDouble(tragetAmt));
					cancelLBean.setAR_CANCEL_H_ID(sourceId);
					cancelLBean.setAR_CANCEL_L_ID(sourceDtlId);
					cancelLBean.setAR_CANCEL_TYPE(arCancelType);
					cancelLBean.setTARGET_ID(tragetId);
					cancelLBean.setTARGET_AMT(Math.abs(Double.parseDouble(tragetAmt)));
					cancelLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					cancelLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.editCancelLInfo(cancelLBean);//添加核销单行表信息
					arInvTransBean.setAR_PAY_H_ID(tragetId);
					editTransList.add(arInvTransBean);//修改交易明细
				}
				
			}else if(arCancelType.equals("YSHYF")){//应收核应付
				ApInvTransBean apInvTransBean = new ApInvTransBean();
				apInvTransBean.setTRANS_ID(transId);
				apInvTransBean.setSOURCE_ID(sourceId);
				apInvTransBean.setSOURCE_DTL_ID(sourceDtlId);
				apInvTransBean.setAP_INV_GL_H_ID(tragetId);
				apInvTransBean.setDR_AMT(Double.parseDouble(tragetAmt));
				apInvTransBean.setCR_AMT(0);
				apInvTransBean.setCREATED_BY(CurrentSessionVar.getUserId());
				apInvTransBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				apInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				apInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				apInvTransBean.setSOURCE_TAB("XC_AR_CANCEL_L");
				apInvTransBean.setDESCRIPTION(description);
				apInvTransBean.setAP_DOC_CODE(cancelCode);
				apInvTransBean.setAP_DOC_CAT_CODE("YSHYF");
				apInvTransBean.setGL_DATE(sdf.parse(glDate));
				if(dao.getCancelLByLId(sourceDtlId)==null){//新增行表
					cancelLBean.setAR_CANCEL_H_ID(sourceId);
					cancelLBean.setAR_CANCEL_L_ID(sourceDtlId);
					cancelLBean.setAR_CANCEL_TYPE(arCancelType);
					cancelLBean.setTARGET_ID(tragetId);
					cancelLBean.setTARGET_AMT(Double.parseDouble(tragetAmt));
					cancelLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					cancelLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.addCancelLInfo(cancelLBean);//添加核销单行表信息
					AptransList.add(apInvTransBean);//添加交易明细
					apInvList.add(tragetId);//更新应付单金额
				}else{//修改
					apInvList.add(tragetId);//更新应付单金额
					cancelLBean.setAR_CANCEL_H_ID(sourceId);
					cancelLBean.setAR_CANCEL_L_ID(sourceDtlId);
					cancelLBean.setAR_CANCEL_TYPE(arCancelType);
					cancelLBean.setTARGET_ID(tragetId);
					cancelLBean.setTARGET_AMT(Double.parseDouble(tragetAmt));
					cancelLBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setCREATED_BY(CurrentSessionVar.getUserId());
					cancelLBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					cancelLBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					dao.editCancelLInfo(cancelLBean);//添加核销单行表信息
					ApeditTransList.add(apInvTransBean);//修改交易明细
				}
				
			}
			
		}
		for(int k=0;k<deleteArray.length();k++){
			String cancelLId = deleteArray.getJSONObject(k).getString("AR_CANCEL_L_ID");
			ArCancelLBean cancelLBean = dao.getCancelLByLId(cancelLId);
			if(cancelLBean!=null){
				if("YSHYUS".equals(cancelLBean.getAR_CANCEL_TYPE())){
					dao.delCancelLInfo(cancelLId);//删除行表信息
					payIdList.add(cancelLBean.getTARGET_ID());
					ArInvTransBean arTransBean = new ArInvTransBean();
					arTransBean.setSOURCE_ID(cancelLBean.getAR_CANCEL_H_ID());
					arTransBean.setSOURCE_DTL_ID(cancelLBean.getAR_CANCEL_L_ID());
					arTransBean.setAR_DOC_CAT_CODE("YSHYUS");
					delTransList.add(arTransBean);
				}else if("YSHLDC".endsWith(cancelLBean.getAR_CANCEL_TYPE())){
					dao.delCancelLInfo(cancelLId);//删除行表信息
					arInvList.add(cancelLBean.getTARGET_ID());
					ArInvTransBean arTransBean = new ArInvTransBean();
					arTransBean.setSOURCE_ID(cancelLBean.getAR_CANCEL_H_ID());
					arTransBean.setSOURCE_DTL_ID(cancelLBean.getAR_CANCEL_L_ID());
					arTransBean.setAR_DOC_CAT_CODE("YSHLDC");
					delTransList.add(arTransBean);
				}else if("YUSHLDC".endsWith(cancelLBean.getAR_CANCEL_TYPE())){
					dao.delCancelLInfo(cancelLId);//删除行表信息
					payIdList.add(cancelLBean.getTARGET_ID());
					ArInvTransBean arTransBean = new ArInvTransBean();
					arTransBean.setSOURCE_ID(cancelLBean.getAR_CANCEL_H_ID());
					arTransBean.setSOURCE_DTL_ID(cancelLBean.getAR_CANCEL_L_ID());
					arTransBean.setAR_DOC_CAT_CODE("YUSHLDC");
					delTransList.add(arTransBean);
				}else if("YSHYF".endsWith(cancelLBean.getAR_CANCEL_TYPE())){
					dao.delCancelLInfo(cancelLId);//删除行表信息
					apInvList.add(cancelLBean.getTARGET_ID());
					ApInvTransBean apTransBean = new ApInvTransBean();
					apTransBean.setSOURCE_ID(cancelLBean.getAR_CANCEL_H_ID());
					apTransBean.setSOURCE_DTL_ID(cancelLBean.getAR_CANCEL_L_ID());
					apTransBean.setAP_DOC_CAT_CODE("YSHYF");
					ApdelTransList.add(apTransBean);
				}
			}
			
		}
		apArTransDao.insertArTrans(transList);//插入应收交易明细
		apArTransDao.updateArTrans(editTransList);//更新应收交易明细
		apArTransDao.deleteArTrans(delTransList);//删除应收交易明细
		apArTransDao.insertApTrans(AptransList);//插入应付交易明细
		apArTransDao.updateApTrans(ApeditTransList);//更新应付交易明细
		apArTransDao.deleteApTrans(ApdelTransList);//删除应付交易明细
		updateInvGlAmountService.updateApInvGlAmount(apInvList);//更新应付单
		updateInvGlAmountService.updateArInvGlAmount(arInvList);//更新应收单
		arPayBaseDao.updateArPayAmt(payIdList);//更新收款单
	} catch (Exception e) {
		log.error(e.getMessage());
		throw e;
	}
	
	
	
}
/*
 * 删除核销单
 */
@Override
@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public JSONObject deleteCancel(String rows,String arInfo,String lang) throws Exception{
	JSONObject jo = new JSONObject();
	try {
		JSONArray ja = new JSONArray(arInfo);
		voucherService.deleteArDoc(ja,lang);
		jo.put("flag", "0");
	} catch (Exception e) {
		jo.put("msg", e.getMessage());
		jo.put("flag", "1");
		log.error(e.getMessage());
		throw e;
		}
	return jo;
}
/*
 * (non-Javadoc) 保存核销单
 * @see com.xzsoft.xc.ar.service.ArCancelBaseService#saveCancel(java.lang.String, java.lang.String, java.lang.String)
 */
@Override
@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
public JSONObject saveCancel(String str, String deleteDtl, String gridStr,String lang)
		throws Exception {
	JSONObject jo = new JSONObject();
	try {
		JSONArray jsonGrid = new JSONArray(gridStr);
		JSONObject json  = new JSONObject(str);
		String cancelHId = json.getString("AR_CANCEL_H_ID");
		String cancelHCode = json.getString("AR_CANCEL_H_CODE");
		String ledgerId = json.getString("LEDGER_ID");
		String cancelType = json.getString("AR_CANCEL_TYPE");
		String glDate = json.getString("GL_DATE");
		String srcId = json.getString("SRC_ID");
		String srcAmt = json.getString("SRC_AMT");
		String description = json.getString("DESCRIPTION");
		String arInvGlHId = json.getString("AR_INV_GL_H_ID");
		String arPayHId = json.getString("AR_PAY_H_ID");
		ArCancelHBean cancelHBean = new ArCancelHBean();
		cancelHBean.setAR_CANCEL_H_ID(cancelHId);
		cancelHBean.setAR_CANCEL_H_CODE(cancelHCode);
		cancelHBean.setLEDGER_ID(ledgerId);
		cancelHBean.setAR_CANCEL_TYPE(cancelType);
		cancelHBean.setGL_DATE(sdf.parse(glDate));
		cancelHBean.setSRC_ID(srcId);
		cancelHBean.setSRC_AMT(Double.parseDouble(srcAmt));
		cancelHBean.setDESCRIPTION(description);
		cancelHBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
		cancelHBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
		List<String> arInvList = new ArrayList<String>();
		List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
		List<ArInvTransBean> editTransList = new ArrayList<ArInvTransBean>();
		List<String> payIdList = new ArrayList<String>();
		if(dao.getArCancelInfo(cancelHId)==null){//新增
			String vHeadId = json.getString("V_HEAD_ID");
			String vStatus = json.getString("V_STATUS");
			cancelHBean.setV_HEAD_ID(vHeadId);
			cancelHBean.setV_STATUS(vStatus);
			cancelHBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
			cancelHBean.setCREATED_BY(CurrentSessionVar.getUserId());
			//获取单据编码
			String LEDGER_ID = cancelHBean.getLEDGER_ID();
			String AR_DOC_CAT_CODE = cancelHBean.getAR_CANCEL_TYPE();
			Date createDate = cancelHBean.getCREATION_DATE();
			String userId = cancelHBean.getCREATED_BY();
			//得到编码规则
			/*String ruleCode = xcARCommondao.getArCode(LEDGER_ID,AR_DOC_CAT_CODE);
			if(ruleCode == null || "".equals(ruleCode)){
				throw new Exception("该账簿级单据类型没有启用编码规则！");
			}*/
			String ruleCode = "AR_CANCEL";
			String year = sdf.format(createDate).substring(0,4);	//年
			String month = sdf.format(createDate).substring(5,7);	//月
			//得到单据编码
			String arDocCode = ruleService.getNextSerialNum(ruleCode, LEDGER_ID, "", year, month, userId);
			if(arDocCode!=null||arDocCode!=""){
				cancelHBean.setAR_CANCEL_H_CODE(arDocCode);
			}
			dao.addArCancelHInfo(cancelHBean);//添加核销单主表信息
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("TARGET_AMT", srcAmt);
				ArInvTransBean transBean = new ArInvTransBean();
				transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
				transBean.setSOURCE_ID(cancelHId);
				if(cancelType.equals("YSHYUS")){//应收核预收
					transBean.setDR_AMT(0);
					transBean.setCR_AMT(Double.parseDouble(srcAmt));
					transBean.setAR_INV_GL_H_ID(arInvGlHId);
					transBean.setAR_DOC_CAT_CODE("YSHYUS");
					arInvList.add(arInvGlHId);
				}else if(cancelType.equals("YUSHLDC")){//预收红蓝对冲
					transBean.setAR_PAY_H_ID(arPayHId);
					transBean.setAR_DOC_CAT_CODE("YUSHLDC");
					transBean.setDR_AMT(Double.parseDouble(srcAmt)*(-1));
					transBean.setCR_AMT(0);
					transBean.setAR_PAY_H_ID(arPayHId);
					payIdList.add(arPayHId);
				}else if(cancelType.equals("YSHLDC")){//应收红蓝对冲
					transBean.setAR_INV_GL_H_ID(arInvGlHId);
					transBean.setAR_DOC_CAT_CODE("YSHLDC");
					transBean.setDR_AMT(Double.parseDouble(srcAmt)*(-1));
					transBean.setCR_AMT(0);
					transBean.setAR_INV_GL_H_ID(arInvGlHId);
					arInvList.add(arInvGlHId);
				}else if(cancelType.equals("YSHYF")){//应收核应付
					transBean.setAR_INV_GL_H_ID(arInvGlHId);
					transBean.setAR_DOC_CAT_CODE("YSHYF");
					transBean.setDR_AMT(0);
					transBean.setCR_AMT(Double.parseDouble(srcAmt));
					transBean.setAR_INV_GL_H_ID(arInvGlHId);
					arInvList.add(arInvGlHId);
				}
				
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setCREATED_BY(CurrentSessionVar.getUserId());
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				transBean.setAR_DOC_CODE(arDocCode);
				transBean.setDESCRIPTION(description);
				transBean.setSOURCE_TAB("XC_AR_CANCEL_H");
				transList.add(transBean);
				saveDtl(jsonGrid,cancelType,deleteDtl,arDocCode,glDate,description);//添加明细
				ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
				avhb.setLedgerId(ledgerId);
				avhb.setArCatCode("HXD");
				avhb.setAccDate(sdf.parse(glDate));
				avhb.setArId(cancelHId);
				avhb.setAttchTotal(0);
				try {
					String returnParams = voucherService.newArVoucher(avhb,lang);
					JSONObject returnJson = new JSONObject(returnParams);
					jo.put("headId", returnJson.getString("headId"));
				} catch (Exception e) {
					jo.put("flag", "1");
					jo.put("msg", e.getMessage());
					throw e;
				}
			}else{//修改
			HashMap<String, Object> map = new HashMap<String, Object>();
			ArCancelHBean resultBean = dao.getArCancelInfo(cancelHId);
			ArInvTransBean transBean = new ArInvTransBean();
			if(cancelType.equals("YSHYF")){//应收核应付、应收核预收
				arInvList.add(arInvGlHId);
				transBean.setDR_AMT(0);
				transBean.setCR_AMT(Double.parseDouble(srcAmt));
				transBean.setAR_DOC_CAT_CODE("YSHYF");
				transBean.setAR_INV_GL_H_ID(arInvGlHId);
			}else if(cancelType.equals("YSHLDC")){//应收红蓝对冲
				arInvList.add(arInvGlHId);
				transBean.setDR_AMT(Double.parseDouble(srcAmt)*(-1));
				transBean.setCR_AMT(0);
				transBean.setAR_DOC_CAT_CODE("YSHLDC");
				transBean.setAR_INV_GL_H_ID(arInvGlHId);
			}else if(cancelType.equals("YUSHLDC")){//预收红蓝对冲
				payIdList.add(arPayHId);
				transBean.setDR_AMT(0);
				transBean.setCR_AMT(Double.parseDouble(srcAmt)*(-1));
				transBean.setAR_DOC_CAT_CODE("YUSHLDC");
				transBean.setAR_PAY_H_ID(arPayHId);
			}else if(cancelType.equals("YSHYUS")){//应收核预收
				arInvList.add(arInvGlHId);
				transBean.setDR_AMT(0);
				transBean.setCR_AMT(Double.parseDouble(srcAmt));
				transBean.setAR_DOC_CAT_CODE("YSHYUS");
				transBean.setAR_INV_GL_H_ID(arInvGlHId);
			}
			dao.editArCancelHInfo(cancelHBean);//修改核销单主表信息
			
				transBean.setSOURCE_ID(cancelHId);
				transBean.setSOURCE_TAB("XC_AR_CANCEL_H");
				transBean.setGL_DATE(sdf.parse(glDate));
				transBean.setDESCRIPTION(description);
				transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				editTransList.add(transBean);//修改交易明细表信息
				saveDtl(jsonGrid,cancelType, deleteDtl,cancelHCode,glDate,description);//添加明细		
				ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
				avhb.setLedgerId(ledgerId);
				avhb.setArCatCode("HXD");
				avhb.setAccDate(sdf.parse(glDate));
				avhb.setArId(cancelHId);
				avhb.setAttchTotal(0);
				try {
					String returnParams = voucherService.newArVoucher(avhb,lang);//生成凭证
					JSONObject returnJson = new JSONObject(returnParams);
					jo.put("headId", returnJson.getString("headId"));
				} catch (Exception e) {
					jo.put("flag", "1");
					jo.put("msg", e.getMessage());
					throw e;
				}
		}
		apArTransDao.insertArTrans(transList);//插入交易明细
		apArTransDao.updateArTransByCancel(editTransList);//修改交易明细
		updateInvGlAmountService.updateArInvGlAmount(arInvList);//更新应收单金额
		arPayBaseDao.updateArPayAmt(payIdList);//更新收款单
		jo.put("flag", "0");
	}catch(Exception e){
		jo.put("flag", "1");
		jo.put("msg", e.getMessage());
		log.error(e.getMessage());
		throw e;
	}
	return jo;
}
/*
 * (non-Javadoc) 审核
 * @see com.xzsoft.xc.ar.service.ArCancelBaseService#checkArVoucher(java.lang.String)
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
/*
 * (non-Javadoc) 取消审核
 * @see com.xzsoft.xc.ar.service.ArCancelBaseService#cancelCheckArVoucher(java.lang.String)
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
