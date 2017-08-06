package com.xzsoft.xc.ap.service.impl;
/**
 * 
  * @ClassName: ApPayReqCommonServiceImpl
  * @Description: 操作付款申请单Service实现类
  * @author 韦才文
  * @date 2016年6月14日 上午16:15:15
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



























import com.xzsoft.xc.ap.action.ApPayReqCommonAction;
import com.xzsoft.xc.ap.dao.ApPayReqCommonDao;
import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.modal.ApPayReqLBean;
import com.xzsoft.xc.ap.service.ApPayReqCommonService;
import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
@Service("apPayReqCommonService")
public class ApPayReqCommonServiceImpl implements ApPayReqCommonService{
	@Resource
	private ApPayReqCommonDao payReqDao;
	//日期格式化
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//日志记录器
	private static Logger log = Logger.getLogger(ApPayReqCommonAction.class.getName());
	@Resource
	RuleService ruleService;
	@Resource
	UpdateInvGlAmountService updateInvGlAmountService;
	@Resource
	ApArTransDao apArTransDao;
	/**
	 * @Title savePay
	 * @Description:保存申请单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject savePay(String reqHMsg, String reqLMsg,String deleteDtl) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			JSONObject reqHJo = new JSONObject(reqHMsg);//转成json
			String docCatCode = reqHJo.getString("AP_DOC_CAT_CODE");//申请单类型
			String payReqHId = reqHJo.getString("AP_PAY_REQ_H_ID");
			ApPayReqHBean reqHBean = payReqDao.getPayReqHById(payReqHId);
			
			if(docCatCode.equals("CGJS")){//申请单类型为采购结算
				JSONArray reqLJoArray = new JSONArray(reqLMsg);
				JSONArray deleteDtlJo = new JSONArray(deleteDtl);//删除的明细数据
				List<String> apInvGlIdList = new ArrayList<String>();
				List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
				if(reqHBean==null){//新增
					String reqHId= addPayReqH(reqHMsg);//付款申请单主表添加数据
					ApPayReqHBean hbean = payReqDao.getPayReqHById(reqHId);
					String reqHCode = hbean.getAP_PAY_REQ_H_CODE();
					String description = hbean.getDESCRIPTION();
				
					if(!reqHId.equals("")&&reqHId!=null){
						for(int i=0;i<reqLJoArray.length();i++){
							JSONObject reqLJo = reqLJoArray.getJSONObject(i);
							String amount = reqLJo.getString("AMOUNT");//申请金额
							String invGlHId = reqLJo.getString("AP_INV_GL_H_ID");//应付单主ID
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("AP_INV_GL_H_ID", invGlHId);
							map.put("AMOUNT", amount);
							
								String reqLId = addPayReqL(reqLJo);//付款申请单行表添加数据
								if(!reqLId.equals("")||reqLId!=null){
									reqLJo.remove("AP_PAY_REQ_L_ID");
									reqLJo.put("AP_PAY_REQ_L_ID",reqLId);
									apInvGlIdList.add(invGlHId);
									ApInvTransBean apInvTransBean = new ApInvTransBean();
									apInvTransBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
									apInvTransBean.setSOURCE_ID(payReqHId);
									apInvTransBean.setSOURCE_DTL_ID(reqLId);
									apInvTransBean.setAP_INV_GL_H_ID(invGlHId);
									apInvTransBean.setAP_DOC_CODE(reqHCode);
									apInvTransBean.setGL_DATE(hbean.getBIZ_DATE());
									apInvTransBean.setDESCRIPTION(description);
									apInvTransBean.setSOURCE_TAB("XC_AP_PAY_REQ_H");
									apInvTransBean.setAP_DOC_CAT_CODE(docCatCode);
									apInvTransBean.setREQ_AMT(Double.parseDouble(amount));
									apInvTransBean.setTRANS_STATUS("0");
									apInvTransBean.setCREATED_BY(CurrentSessionVar.getUserId());
									apInvTransBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
									apInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
									apInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
									transList.add(apInvTransBean);
								}
								jo.put("flag", "0");								
							
						}
						apArTransDao.insertApTrans(transList);//更新交易明细
						updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);//更新应付单
					}
					
				}else{//修改
						editPayReqH(reqHMsg);//修改付款申请单主表
						List<ApInvTransBean> delList = new ArrayList<ApInvTransBean>();
						List<ApInvTransBean> editList = new ArrayList<ApInvTransBean>();
						for(int i=0;i<deleteDtlJo.length();i++){
							String dtlId = deleteDtlJo.getJSONObject(i).getString("AP_PAY_REQ_L_ID");
							ApPayReqLBean bean = getPayReqL(dtlId);
							delPayReqL(dtlId);//删除申请单行信息
							ApInvTransBean transBean = new ApInvTransBean();
							transBean.setSOURCE_ID(payReqHId);
							transBean.setSOURCE_DTL_ID(dtlId);
							transBean.setAP_INV_GL_H_ID(bean.getAP_INV_GL_H_ID());
							transBean.setAP_DOC_CAT_CODE(docCatCode);
							apInvGlIdList.add(bean.getAP_INV_GL_H_ID());
							delList.add(transBean);
							
						}
						apArTransDao.deleteApTrans(delList);
						ApPayReqHBean hbean = payReqDao.getPayReqHById(payReqHId);
						String reqHCode = hbean.getAP_PAY_REQ_H_CODE();
						String description = hbean.getDESCRIPTION();
						for(int j=0;j<reqLJoArray.length();j++){
							JSONObject reqLJo = new JSONObject();
							reqLJo = reqLJoArray.getJSONObject(j);
							String amount = reqLJo.getString("AMOUNT");//申请金额
							String invGlHId = reqLJo.getString("AP_INV_GL_H_ID");//应付单主ID
							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("AP_INV_GL_H_ID", invGlHId);
							map.put("AMOUNT", amount);
								String payReqLId = reqLJo.getString("AP_PAY_REQ_L_ID");
								ApPayReqLBean bean = payReqDao.getPayReqL(payReqLId);
								if(bean==null){//添加
									String reqLId = addPayReqL(reqLJo);//添加明细信息
									reqLJo.remove("AP_PAY_REQ_L_ID");
									reqLJo.put("AP_PAY_REQ_L_ID",reqLId);
									apInvGlIdList.add(invGlHId);
									ApInvTransBean apInvTransBean = new ApInvTransBean();
									apInvTransBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
									apInvTransBean.setSOURCE_ID(payReqHId);
									apInvTransBean.setSOURCE_DTL_ID(reqLId);
									apInvTransBean.setAP_INV_GL_H_ID(invGlHId);
									apInvTransBean.setAP_DOC_CODE(reqHCode);
									apInvTransBean.setGL_DATE(hbean.getBIZ_DATE());
									apInvTransBean.setDESCRIPTION(description);
									apInvTransBean.setSOURCE_TAB("XC_AP_PAY_REQ_H");
									apInvTransBean.setAP_DOC_CAT_CODE(docCatCode);
									apInvTransBean.setREQ_AMT(Double.parseDouble(amount));
									apInvTransBean.setTRANS_STATUS("0");
									apInvTransBean.setCREATED_BY(CurrentSessionVar.getUserId());
									apInvTransBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
									apInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
									apInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
									transList.add(apInvTransBean);
								}else{//修改
									ApPayReqLBean vbean = getPayReqL(reqLJo.getString("AP_PAY_REQ_L_ID"));
									apInvGlIdList.add(invGlHId);
									ApInvTransBean transBean = new ApInvTransBean();
									transBean.setSOURCE_DTL_ID(reqLJo.getString("AP_PAY_REQ_L_ID"));
									transBean.setSOURCE_ID(reqLJo.getString("AP_PAY_REQ_H_ID"));
									transBean.setDESCRIPTION(reqLJo.getString("DESCRIPTION"));
									transBean.setGL_DATE(sdf.parse(reqHJo.getString("BIZ_DATE")));
									transBean.setREQ_AMT(Double.parseDouble(reqLJo.getString("AMOUNT")));
									transBean.setAP_DOC_CAT_CODE(reqHJo.getString("AP_DOC_CAT_CODE"));
									transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
									transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
									editList.add(transBean);
									editPayReqL(reqLJo);//修改明细信息
								}
								jo.put("flag", "0");
							
						}
						apArTransDao.updateApTrans(editList);//修改交易明细
						apArTransDao.insertApTrans(transList);//插入交易明细
						updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);//更改应付单
				}
				
			}else if(docCatCode.equals("YUFK")){
				List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
				ApInvTransBean trans = new ApInvTransBean();
				trans.setSOURCE_ID(reqHJo.getString("AP_PAY_REQ_H_ID"));
				trans.setREQ_AMT(Double.parseDouble(reqHJo.getString("AMOUNT")));
				trans.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
				trans.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
				if(reqHBean==null){//新增
					addPayReqH(reqHMsg);//付款申请单主表添加数据
					JSONObject reqLJo = new JSONObject();
					reqLJo.put("AP_PAY_REQ_L_ID", java.util.UUID.randomUUID());
					reqLJo.put("AP_PAY_REQ_H_ID", reqHJo.getString("AP_PAY_REQ_H_ID"));
					reqLJo.put("AP_INV_GL_H_ID", "");
					reqLJo.put("AP_PUR_TYPE_ID", "");
					reqLJo.put("AP_CONTRACT_ID", reqHJo.getString("AP_CONTRACT_ID"));
					reqLJo.put("AMOUNT", Double.parseDouble(reqHJo.getString("AMOUNT")));
					reqLJo.put("DESCRIPTION",  reqHJo.getString("DESCRIPTION"));
					addPayReqL(reqLJo);
					ApPayReqHBean bean = payReqDao.getPayReqHById(payReqHId);
					trans.setTRANS_ID(java.util.UUID.randomUUID().toString());
					trans.setGL_DATE(sdf.parse(reqHJo.getString("BIZ_DATE")));
					trans.setSOURCE_TAB("XC_AP_PAY_REQ_H");
					trans.setAP_DOC_CAT_CODE("YUFK");
					trans.setSOURCE_DTL_ID(reqLJo.getString("AP_PAY_REQ_L_ID"));
					trans.setAP_DOC_CODE(bean.getAP_PAY_REQ_H_CODE());
					trans.setDESCRIPTION(reqHJo.getString("DESCRIPTION"));
					trans.setAP_CONTRACT_ID(reqHJo.getString("AP_CONTRACT_ID"));
					trans.setVENDOR_ID(reqHJo.getString("VENDOR_ID"));
					trans.setTRANS_STATUS("0");
					trans.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					trans.setCREATED_BY(CurrentSessionVar.getUserId());
					transList.add(trans);
					apArTransDao.insertApTrans(transList);//插入交易明细
					
				}else{//修改
					editPayReqH(reqHMsg);//修改付款申请单主表
					List<HashMap<String, Object>> resultList = getPayReqLByHId(reqHJo.getString("AP_PAY_REQ_H_ID"));
					for(int i=0;i<resultList.size();i++){
						String reqLId = resultList.get(i).get("AP_PAY_REQ_L_ID").toString();
						JSONObject reqLJo = new JSONObject();
						reqLJo.put("AP_PAY_REQ_L_ID", reqLId);
						reqLJo.put("DESCRIPTION", reqHJo.getString("DESCRIPTION"));
						reqLJo.put("AMOUNT", reqHJo.getString("AMOUNT"));
						reqLJo.put("AP_DOC_CAT_CODE", "YUFK");
						editPayReqL(reqLJo);
						ApInvTransBean transBean = new ApInvTransBean();
						transBean.setSOURCE_DTL_ID(reqLId);
						transBean.setSOURCE_ID(reqHJo.getString("AP_PAY_REQ_H_ID"));
						transBean.setDESCRIPTION(reqHJo.getString("DESCRIPTION"));
						transBean.setGL_DATE(sdf.parse(reqHJo.getString("BIZ_DATE")));
						transBean.setAP_DOC_CAT_CODE(reqLJo.getString("AP_DOC_CAT_CODE"));
						transBean.setREQ_AMT(Double.parseDouble(reqHJo.getString("AMOUNT")));
						transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						transList.add(transBean);
						apArTransDao.updateApTrans(transList);//修改交易明细
					}
					
				}
				jo.put("flag", "0");
			}else if(docCatCode.equals("QTFK")){
				JSONArray reqLJoArray = new JSONArray(reqLMsg);
				JSONArray deleteDtlJo = new JSONArray(deleteDtl);//删除的明细数据
				List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
				if(reqHBean==null){//新增
					String reqHId= addPayReqH(reqHMsg);//付款申请单主表添加数据
					ApPayReqHBean hbean = payReqDao.getPayReqHById(reqHId);
					String reqHCode = hbean.getAP_PAY_REQ_H_CODE();
					String description = hbean.getDESCRIPTION();
					if(!"".equals(reqHId)&&reqHId!=null){
						for(int i=0;i<reqLJoArray.length();i++){
							JSONObject reqLJo = reqLJoArray.getJSONObject(i);
							String reqLId = addPayReqL(reqLJo);//添加付款单行信息
							if(!reqLId.equals("")&&reqLId!=null){
								reqLJo.remove("AP_PAY_REQ_L_ID");
								reqLJo.put("AP_PAY_REQ_L_ID",reqLId);
								ApInvTransBean apInvTransBean = new ApInvTransBean();
								apInvTransBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
								apInvTransBean.setSOURCE_ID(payReqHId);
								apInvTransBean.setSOURCE_DTL_ID(reqLId);
								apInvTransBean.setAP_INV_GL_H_ID("");
								apInvTransBean.setAP_DOC_CODE(reqHCode);
								apInvTransBean.setGL_DATE(hbean.getBIZ_DATE());
								apInvTransBean.setDESCRIPTION(description);
								apInvTransBean.setSOURCE_TAB("XC_AP_PAY_REQ_H");
								apInvTransBean.setAP_DOC_CAT_CODE(docCatCode);
								apInvTransBean.setREQ_AMT(hbean.getAMOUNT());
								apInvTransBean.setTRANS_STATUS("0");
								apInvTransBean.setCREATED_BY(CurrentSessionVar.getUserId());
								apInvTransBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
								apInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
								apInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
								transList.add(apInvTransBean);
							}
							jo.put("flag", "0");
						}
						apArTransDao.insertApTrans(transList);
					}
				}else{//修改
					editPayReqH(reqHMsg);//修改付款申请单主表
					List<ApInvTransBean> delList = new ArrayList<ApInvTransBean>();
					List<ApInvTransBean> editList = new ArrayList<ApInvTransBean>();
					for(int i=0;i<deleteDtlJo.length();i++){
						String dtlId = deleteDtlJo.getJSONObject(i).getString("AP_PAY_REQ_L_ID");
						ApPayReqLBean bean = getPayReqL(dtlId);
						delPayReqL(dtlId);//删除明细数据
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("SOURCE_ID", payReqHId);
						map.put("SOURCE_DTL_ID", dtlId);
						map.put("AP_INV_GL_H_ID", bean.getAP_INV_GL_H_ID());
						map.put("AP_DOC_CAT_CODE", docCatCode);
						ApInvTransBean transBean = new ApInvTransBean();
						transBean.setSOURCE_ID(payReqHId);
						transBean.setSOURCE_DTL_ID(dtlId);
						transBean.setAP_INV_GL_H_ID(bean.getAP_INV_GL_H_ID());
						transBean.setAP_DOC_CAT_CODE(docCatCode);
						delList.add(transBean);
					}
					apArTransDao.deleteApTrans(delList);
					ApPayReqHBean hbean = payReqDao.getPayReqHById(payReqHId);
					String reqHCode = hbean.getAP_PAY_REQ_H_CODE();
					String description = hbean.getDESCRIPTION();
					for(int j=0;j<reqLJoArray.length();j++){
						JSONObject reqLJo = new JSONObject();
						reqLJo = reqLJoArray.getJSONObject(j);
						String amount = reqLJo.getString("AMOUNT");//申请金额
						String payReqLId = reqLJo.getString("AP_PAY_REQ_L_ID");
						ApPayReqLBean bean = payReqDao.getPayReqL(payReqLId);
						if(bean==null){//添加
								String reqLId = addPayReqL(reqLJo);//添加明细信息
								ApInvTransBean apInvTransBean = new ApInvTransBean();
								apInvTransBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
								apInvTransBean.setSOURCE_ID(payReqHId);
								apInvTransBean.setSOURCE_DTL_ID(reqLId);
								apInvTransBean.setAP_INV_GL_H_ID("");
								apInvTransBean.setAP_DOC_CODE(reqHCode);
								apInvTransBean.setGL_DATE(hbean.getBIZ_DATE());
								apInvTransBean.setDESCRIPTION(description);
								apInvTransBean.setSOURCE_TAB("XC_AP_PAY_REQ_H");
								apInvTransBean.setAP_DOC_CAT_CODE(docCatCode);
								apInvTransBean.setREQ_AMT(hbean.getAMOUNT());
								apInvTransBean.setTRANS_STATUS("0");
								apInvTransBean.setCREATED_BY(CurrentSessionVar.getUserId());
								apInvTransBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
								apInvTransBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
								apInvTransBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
								transList.add(apInvTransBean);
							}else{//修改
								editPayReqL(reqLJo);//修改明细信息
								HashMap<String, Object> map1 = new HashMap<String, Object>();
								map1.put("SOURCE_ID", reqLJo.getString("AP_PAY_REQ_H_ID"));
								map1.put("REQ_AMT", Double.parseDouble(reqLJo.getString("AMOUNT")));
								map1.put("LAST_UPDATE_DATE", CurrentUserUtil.getCurrentDate());
								map1.put("LAST_UPDATED_BY",CurrentSessionVar.getUserId());
								map1.put("SOURCE_DTL_ID", payReqLId);
								map1.put("AP_INV_GL_H_ID", null);
								ApInvTransBean transBean = new ApInvTransBean();
								transBean.setSOURCE_DTL_ID(reqLJo.getString("AP_PAY_REQ_L_ID"));
								transBean.setSOURCE_ID(reqHJo.getString("AP_PAY_REQ_H_ID"));
								transBean.setDESCRIPTION(reqHJo.getString("DESCRIPTION"));
								transBean.setGL_DATE(sdf.parse(reqHJo.getString("BIZ_DATE")));
								transBean.setAP_DOC_CAT_CODE("QTFK");
								transBean.setREQ_AMT(Double.parseDouble(reqHJo.getString("AMOUNT")));
								transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
								transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
								editList.add(transBean);
							}
							jo.put("flag", "0");
						
						
					}
					apArTransDao.updateApTrans(editList);//修改交易明细
					apArTransDao.insertApTrans(transList);
				}
				
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
	 * @Title addPayReqH
	 * @Description:添加申请单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String addPayReqH(String reqHMsg) throws Exception {
		JSONObject reqHJo = new JSONObject(reqHMsg);//转成json
		ApPayReqHBean payReqHBean = new ApPayReqHBean();//定义付款申请单主表对象
		String docCatCode = reqHJo.getString("AP_DOC_CAT_CODE");
		String payReqHId = reqHJo.getString("AP_PAY_REQ_H_ID");
		String ledgerId = reqHJo.getString("LEDGER_ID");
		String bizDate = reqHJo.getString("BIZ_DATE");
		String vendorId = reqHJo.getString("VENDOR_ID");
		String projectId = reqHJo.getString("PROJECT_ID");
		String deptId = reqHJo.getString("DEPT_ID");
		String contractId = reqHJo.getString("AP_CONTRACT_ID");
		String amount = reqHJo.getString("AMOUNT");
		String accountName = reqHJo.getString("ACCOUNT_NAME");
		String bankName = reqHJo.getString("DEPOSIT_BANK_NAME");
		String bankAccount = reqHJo.getString("BANK_ACCOUNT");
		String description = reqHJo.getString("DESCRIPTION");
		String createdBy = CurrentSessionVar.getUserId();
		Date createDate = CurrentUserUtil.getCurrentDate();
		String lastCreatedBy = CurrentSessionVar.getUserId();
		Date lastUpdateDate = CurrentUserUtil.getCurrentDate();
		payReqHBean.setAP_DOC_CAT_CODE(docCatCode);
		payReqHBean.setAP_PAY_REQ_H_ID(payReqHId);
		payReqHBean.setLEDGER_ID(ledgerId);
		payReqHBean.setBIZ_DATE(sdf.parse(bizDate));
		payReqHBean.setVENDOR_ID(vendorId);
		payReqHBean.setPROJECT_ID(projectId);
		payReqHBean.setDEPT_ID(deptId);
		payReqHBean.setAP_CONTRACT_ID(contractId);
		payReqHBean.setAMOUNT(Double.valueOf(amount));
		payReqHBean.setPAID_AMT(Double.valueOf("0"));
		payReqHBean.setNO_PAY_AMT(Double.valueOf(amount));
		payReqHBean.setOCCUPY_AMT(Double.valueOf("0"));
		payReqHBean.setACCOUNT_NAME(accountName);
		payReqHBean.setDEPOSIT_BANK_NAME(bankName);
		payReqHBean.setBANK_ACCOUNT(bankAccount);
		payReqHBean.setDESCRIPTION(description);
		payReqHBean.setCREATED_BY(createdBy);
		payReqHBean.setCREATION_DATE(createDate);
		payReqHBean.setLAST_UPDATED_BY(lastCreatedBy);
		payReqHBean.setLAST_UPDATE_DATE(lastUpdateDate);
		String ruleCode = "AP_PAY_REQ";
		String year = sdf.format(createDate).substring(0,4);	//年
		String month = sdf.format(createDate).substring(5,7);	//月
		//得到单据编码
		String apDocCode = ruleService.getNextSerialNum(ruleCode, ledgerId, "", year, month, createdBy);
		if(apDocCode!=null||apDocCode!=""){
			payReqHBean.setAP_PAY_REQ_H_CODE(apDocCode);
		}
		payReqDao.addPayReqH(payReqHBean);
		return payReqHId;
		
	}
	/**
	 * @Title editPayReqH
	 * @Description:修改申请单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return 
	 * @throws 
	 */
	@Override
	public void editPayReqH(String reqHMsg) throws Exception{
		JSONObject reqHJo = new JSONObject(reqHMsg);//转成json
		ApPayReqHBean payReqHBean = new ApPayReqHBean();//定义付款申请单主表对象
		String payReqHId = reqHJo.getString("AP_PAY_REQ_H_ID");
		String payReqHCode = reqHJo.getString("AP_PAY_REQ_H_CODE");
		String bizDate = reqHJo.getString("BIZ_DATE");
		String vendorId = reqHJo.getString("VENDOR_ID");
		String projectId = reqHJo.getString("PROJECT_ID");
		String deptId = reqHJo.getString("DEPT_ID");
		String contractId = reqHJo.getString("AP_CONTRACT_ID");
		String amount = reqHJo.getString("AMOUNT");
		String accountName = reqHJo.getString("ACCOUNT_NAME");
		String bankName = reqHJo.getString("DEPOSIT_BANK_NAME");
		String bankAccount = reqHJo.getString("BANK_ACCOUNT");
		String description = reqHJo.getString("DESCRIPTION");
		//String auditDate = CurrentUserUtil.getCurrentDate().toString();
		String lastCreatedBy = CurrentSessionVar.getUserId();
		Date lastUpdateDate = CurrentUserUtil.getCurrentDate();
		payReqHBean.setAP_PAY_REQ_H_ID(payReqHId);
		payReqHBean.setAP_PAY_REQ_H_CODE(payReqHCode);
		payReqHBean.setBIZ_DATE(sdf.parse(bizDate));
		payReqHBean.setVENDOR_ID(vendorId);
		payReqHBean.setPROJECT_ID(projectId);
		payReqHBean.setPROJECT_ID(projectId);
		payReqHBean.setDEPT_ID(deptId);
		payReqHBean.setAP_CONTRACT_ID(contractId);
		payReqHBean.setAMOUNT(Double.parseDouble(amount));
		payReqHBean.setACCOUNT_NAME(accountName);
		payReqHBean.setDEPOSIT_BANK_NAME(bankName);
		payReqHBean.setBANK_ACCOUNT(bankAccount);
		payReqHBean.setDESCRIPTION(description);
		//payReqHBean.setAUDIT_DATE(sdf.parse(auditDate));
		payReqHBean.setLAST_UPDATED_BY(lastCreatedBy);
		payReqHBean.setLAST_UPDATE_DATE(lastUpdateDate);
		payReqDao.editPayReqH(payReqHBean);
		
	}
	/**
	 * @Title delPayReqH
	 * @Description:删除申请单
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject delPayReqH(String payReqHId) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			List<HashMap<String, Object>> resultlist  = payReqDao.getPayReqLByHId(payReqHId);
			ApPayReqHBean bean = payReqDao.getPayReqHById(payReqHId);
			for(int i=0;i<resultlist.size();i++){
				String payReqLId = resultlist.get(i).get("AP_PAY_REQ_L_ID").toString();
				payReqDao.delPayReqL(payReqLId);
				ApInvTransBean transBean = new ApInvTransBean();
				List<ApInvTransBean> list = new ArrayList<ApInvTransBean>();
				transBean.setSOURCE_ID(payReqHId);
				transBean.setSOURCE_DTL_ID(payReqLId);
				transBean.setAP_DOC_CAT_CODE(bean.getAP_DOC_CAT_CODE());
				list.add(transBean);
				apArTransDao.deleteApTrans(list);
			}
			
			payReqDao.delPayReqH(payReqHId);
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		return jo;
	}
	/**
	 * @Title addPayReqL
	 * @Description:添加申请单行表信息
	 * 参数格式:
	 * @param  org.codehaus.jettison.json.JSONObject
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public String addPayReqL(JSONObject reqLJo) throws Exception {
			String payReqLId = reqLJo.getString("AP_PAY_REQ_L_ID");
			String payReqHId = reqLJo.getString("AP_PAY_REQ_H_ID");
			String invHId = reqLJo.getString("AP_INV_GL_H_ID");
			String purTypeId = reqLJo.getString("AP_PUR_TYPE_ID");
			String contractId = reqLJo.getString("AP_CONTRACT_ID");
			String amount = reqLJo.getString("AMOUNT");
			String description = reqLJo.getString("DESCRIPTION");
			String createdBy = CurrentSessionVar.getUserId();
			Date createDate = CurrentUserUtil.getCurrentDate();
			String lastCreatedBy = CurrentSessionVar.getUserId();
			Date lastUpdateDate = CurrentUserUtil.getCurrentDate();
			ApPayReqLBean payReqLBean = new ApPayReqLBean();//新建付款申请单行表对象
			payReqLBean.setAP_PAY_REQ_L_ID(payReqLId);
			payReqLBean.setAP_PAY_REQ_H_ID(payReqHId);
			payReqLBean.setAP_INV_GL_H_ID(invHId);
			payReqLBean.setAP_PUR_TYPE_ID(purTypeId);
			payReqLBean.setAP_CONTRACT_ID(contractId);
			payReqLBean.setAMOUNT(Double.parseDouble(amount));
			payReqLBean.setPAID_AMT(Double.valueOf(0));
			payReqLBean.setNO_PAY_AMT(Double.parseDouble(amount));
			payReqLBean.setDESCRIPTION(description);
			payReqLBean.setCREATED_BY(createdBy);
			payReqLBean.setCREATION_DATE(createDate);
			payReqLBean.setLAST_UPDATED_BY(lastCreatedBy);
			payReqLBean.setLAST_UPDATE_DATE(lastUpdateDate);
			payReqDao.addPayReqL(payReqLBean);	
			return payReqLId;
	}
	/**
	 * @Title editPayReqL
	 * @Description:更新申请单行表信息
	 * 参数格式:
	 * @param  org.codehaus.jettison.json.JSONObject
	 * @return java.lang.String
	 * @throws 
	 */
	@Override
	public void editPayReqL(JSONObject reqLJo) throws Exception {
			String payReqLId = reqLJo.getString("AP_PAY_REQ_L_ID");
			String amount = reqLJo.getString("AMOUNT");
			String description = reqLJo.getString("DESCRIPTION");
			String lastCreatedBy = CurrentSessionVar.getUserId();
			Date lastUpdateDate = CurrentUserUtil.getCurrentDate();
			ApPayReqLBean payReqLBean = new ApPayReqLBean();//新建付款申请单行表对象
			payReqLBean.setAP_PAY_REQ_L_ID(payReqLId);
			payReqLBean.setAMOUNT(Double.parseDouble(amount));
			payReqLBean.setPAID_AMT(Double.valueOf(0));
			payReqLBean.setNO_PAY_AMT(Double.parseDouble(amount));
			payReqLBean.setDESCRIPTION(description);
			payReqLBean.setLAST_UPDATED_BY(lastCreatedBy);
			payReqLBean.setLAST_UPDATE_DATE(lastUpdateDate);
			payReqDao.editPayReqL(payReqLBean);
		
	}
	/**
	 * @Title delPayReqL
	 * @Description:删除申请单行表信息
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Override
	public JSONObject delPayReqL(String payReqLId) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			payReqDao.delPayReqL(payReqLId);
			jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
	
		return jo;
	}
	
	/**
	 * @Title getPayReqL
	 * @Description:查询付款申请单行表信息 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return com.xzsoft.xc.ap.modal.ApPayReqLBean
	 * @throws 
	 */
	@Override
	public ApPayReqLBean getPayReqL(String payReqLId) throws Exception {
		return payReqDao.getPayReqL(payReqLId);
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getProcAndFormsByCat</p> 
	 * <p>Description: 根据费用单据类型获取审批流程和审批表单信息 </p> 
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#getProcAndFormsByCat(java.lang.String, java.lang.String)
	 */
	public JSONObject getProcAndFormsByCat(String ledgerId,String docCat) throws Exception {
		JSONObject jo = new JSONObject() ;
		try {
			// 获取账簿级单据类型信息
			
			/* A.LEDGER_ID    ,
		       A.EX_CAT_CODE  ,
		       A.PROCESS_ID   ,
		       B.PROCESS_CODE ,
		       A.RULE_CODE*/
			
			HashMap<String,String> ldCatMap = payReqDao.getLedgerCatInfo(ledgerId, docCat) ;
			String processId = ldCatMap.get("PROCESS_ID") ;
			String processCode = ldCatMap.get("PROCESS_CODE") ;
			String ruleCode = ldCatMap.get("RULE_CODE") ;
			String attCatCode = ldCatMap.get("ATT_CAT_CODE");
			
			jo.put("PROCESS_ID", processId) ;
			jo.put("PROCESS_CODE", processCode) ;
			jo.put("RULE_CODE", ruleCode) ;
			jo.put("ATT_CAT_CODE", attCatCode);
			// 获取全局单据类型信息
			/*A.EX_CAT_CODE,
		       A.EX_CAT_NAME,
		       A.IS_SIGN,
		       A.IS_PAY,
		       A.PROCESS_ID,
		       B.PROCESS_CODE,
		       A.PC_W_FORM_ID,
		       C.FORM_URL PC_W_FORM_URL,
		       A.PC_A_FORM_ID,
		       D.FORM_URL PC_A_FORM_URL,
		       A.PC_P_FORM_ID,
		       E.FORM_URL PC_P_FORM_URL,
		       A.M_W_FORM_ID,
		       F.FORM_URL M_W_FORM_URL,
		       A.M_A_FORM_ID,
		       G.FORM_URL M_A_FORM_URL*/
			HashMap<String,String> gbCatMap = payReqDao.getGlobalCatInfo(docCat) ;
			if(processId == null || "".equals(processId)){
				String gbProcId = gbCatMap.get("PROCESS_ID") ;
				String gbProcCode = gbCatMap.get("PROCESS_CODE") ;
				if(gbProcId == null || "".equals(gbProcId)){
					throw new Exception("单据类型:"+docCat+"未设置审批流程，请在【单据类型设置或单据类型】功能处设置") ;
				}else{
					jo.put("PROCESS_ID", gbProcId) ;
					jo.put("PROCESS_CODE", gbProcCode) ;
				}	
			}
			String gbAttCatCode = gbCatMap.get("ATT_CAT_CODE");
			if(gbAttCatCode == null || "".equals(gbAttCatCode)){
				throw new Exception("单据类型:"+docCat+"未设置附件分类，请在【单据类型设置或单据类型】功能处设置") ;
			}else{
				jo.put("ATT_CAT_CODE", gbAttCatCode) ;
			}
			jo.put("AP_CAT_CODE", gbCatMap.get("AP_CAT_CODE")) ;		// 单据类型编码
			jo.put("AP_CAT_NAME", gbCatMap.get("AP_CAT_NAME")) ;		// 单据类型名称
			jo.put("IS_SIGN", gbCatMap.get("IS_SIGN")) ;				// 是否需要签字
			jo.put("PC_W_FORM_ID", gbCatMap.get("PC_W_FORM_ID")) ;		// 电脑端填报表单ID
			jo.put("PC_W_FORM_URL", gbCatMap.get("PC_W_FORM_URL")) ; 	// 电脑端填报表单地址
			jo.put("PC_A_FORM_ID", gbCatMap.get("PC_A_FORM_ID")) ;  	// 电脑端审批表单ID
			jo.put("PC_A_FORM_URL", gbCatMap.get("PC_A_FORM_URL")) ;	// 电脑端审批表单地址
			jo.put("PC_P_FORM_ID", gbCatMap.get("PC_P_FORM_ID")) ;		// 电脑端打印表单ID
			jo.put("PC_P_FORM_URL", gbCatMap.get("PC_P_FORM_URL")) ;	// 电脑端打印表单地址
			jo.put("M_W_FORM_ID", gbCatMap.get("M_W_FORM_ID")) ;		// 移动端填报表单ID
			jo.put("M_W_FORM_URL", gbCatMap.get("M_W_FORM_URL")) ;		// 移动端填报表单地址
			jo.put("M_A_FORM_ID", gbCatMap.get("M_A_FORM_ID")) ;		// 移动端审批表单ID
			jo.put("M_A_FORM_URL", gbCatMap.get("M_A_FORM_URL")) ;		// 移动端审批表单地址
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo ;
	}
	/**
	 * @Title getPayReqLByHId
	 * @Description:通过申请单主表ID查询行表信息 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return java.util.List
	 * @throws 
	 */
	@Override
	public List<HashMap<String, Object>> getPayReqLByHId(String payReqHId)
			throws Exception {
		
		return payReqDao.getPayReqLByHId(payReqHId);
	}
	/**
	 * @Title closeDoc
	 * @Description:关闭单据 
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject closeDoc(String ids,String lang) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray idArray = new JSONArray(ids);
			List<String> apInvGlIdList = new ArrayList<String>();
			for (int i = 0; i < idArray.length(); i++) {
				JSONObject ob = idArray.getJSONObject(i);
				String id = ob.getString("id");
				ApPayReqHBean bean1 = payReqDao.getPayReqHById(id);
				HashMap<String, Object> map1 = new HashMap<String, Object>();
				map1.put("IS_CLOSE", "Y");
				map1.put("AP_PAY_REQ_H_ID", id);
				map1.put("LAST_UPDATE_DATE", CurrentUserUtil.getCurrentDate());
				map1.put("LAST_UPDATED_BY", CurrentSessionVar.getUserId());
				payReqDao.updatePayReqByClose(map1);//更新申请单
				List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
				if("CGJS".equals(bean1.getAP_DOC_CAT_CODE())){
					List<HashMap<String, Object>> list = payReqDao.getPayReqLByHId(id);
					for (int j = 0; j < list.size(); j++) {
						HashMap<String, Object> map2 = list.get(j);
						String reqLId = map2.get("AP_PAY_REQ_L_ID").toString();
						ApPayReqLBean bean = payReqDao.getPayReqL(reqLId);
						Double noPayAmt = bean.getNO_PAY_AMT();
						String invHId = bean.getAP_INV_GL_H_ID();
						apInvGlIdList.add(invHId);
						ApInvTransBean transBean = new ApInvTransBean();
						transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
						transBean.setSOURCE_ID(id);
						transBean.setSOURCE_DTL_ID(reqLId);
						transBean.setAP_INV_GL_H_ID(invHId);
						transBean.setGL_DATE(CurrentUserUtil.getCurrentDate());
						transBean.setSOURCE_TAB("XC_AP_PAY_REQ_H");
						transBean.setAP_DOC_CAT_CODE("CGJS");
						transBean.setAP_DOC_CODE(bean1.getAP_PAY_REQ_H_CODE());
						transBean.setDESCRIPTION(XipUtil.getMessage(lang, "XC_AP_REQ_CLOSE", null));
						transBean.setAP_CONTRACT_ID(bean1.getAP_CONTRACT_ID());
						transBean.setVENDOR_ID(bean1.getVENDOR_ID());
						transBean.setREQ_AMT(noPayAmt*(-1));
						transBean.setTRANS_STATUS("1");
						transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						transBean.setCREATED_BY(CurrentSessionVar.getUserId());
						transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						transList.add(transBean);	
					}
				}else if("YUFK".equals(bean1.getAP_DOC_CAT_CODE())){
					ApInvTransBean transBean = new ApInvTransBean();
					transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
					transBean.setSOURCE_ID(id);
					transBean.setGL_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setSOURCE_TAB("XC_AP_PAY_REQ_H");
					transBean.setAP_DOC_CAT_CODE("YUFK");
					transBean.setAP_DOC_CODE(bean1.getAP_PAY_REQ_H_CODE());
					transBean.setDESCRIPTION(XipUtil.getMessage(lang, "XC_AP_REQ_CLOSE", null));
					transBean.setAP_CONTRACT_ID(bean1.getAP_CONTRACT_ID());
					transBean.setVENDOR_ID(bean1.getVENDOR_ID());
					transBean.setREQ_AMT(bean1.getAMOUNT());
					transBean.setTRANS_STATUS("1");
					transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setCREATED_BY(CurrentSessionVar.getUserId());
					transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					transList.add(transBean);
				}else if("QTFK".equals(bean1.getAP_DOC_CAT_CODE())){
					ApInvTransBean transBean = new ApInvTransBean();
					transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
					transBean.setSOURCE_ID(id);
					transBean.setGL_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setSOURCE_TAB("XC_AP_PAY_REQ_H");
					transBean.setAP_DOC_CAT_CODE("QTFK");
					transBean.setAP_DOC_CODE(bean1.getAP_PAY_REQ_H_CODE());
					transBean.setDESCRIPTION(XipUtil.getMessage(lang, "XC_AP_REQ_CLOSE", null));
					transBean.setAP_CONTRACT_ID(bean1.getAP_CONTRACT_ID());
					transBean.setVENDOR_ID(bean1.getVENDOR_ID());
					transBean.setREQ_AMT(bean1.getAMOUNT());
					transBean.setTRANS_STATUS("1");
					transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setCREATED_BY(CurrentSessionVar.getUserId());
					transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
					transList.add(transBean);
				}
				apArTransDao.insertApTrans(transList);//插入交易明细
				if(apInvGlIdList.size()!=0){
					updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);//更新应付单
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
	 * @Title updateOccupyAmt
	 * @Description:更新占用金额
	 * 参数格式:
	 * @param  java.util.List
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject updateOccupyAmt(List<String> list) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			if(list!=null){
				List<String> apInvGlIdList = new ArrayList<String>();
				List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
				List<ApInvTransBean> delTransList = new ArrayList<ApInvTransBean>();
				for (int i = 0; i < list.size(); i++) {
					ApPayReqLBean apPayReqLBean = payReqDao.getPayReqL(list.get(i));
					Double noPayAmt = apPayReqLBean.getNO_PAY_AMT();
					String invHId = apPayReqLBean.getAP_INV_GL_H_ID();
					ApPayReqHBean apPayReqHBean = payReqDao.getPayReqHById(apPayReqLBean.getAP_PAY_REQ_H_ID());
					if("Y".equals(apPayReqHBean.getIS_CLOSE())){
						apInvGlIdList.add(invHId);
						ApInvTransBean transBean = new ApInvTransBean();
						transBean.setTRANS_ID(java.util.UUID.randomUUID().toString());
						transBean.setSOURCE_ID(apPayReqLBean.getAP_PAY_REQ_H_ID());
						transBean.setSOURCE_DTL_ID(apPayReqLBean.getAP_PAY_REQ_L_ID());
						transBean.setAP_INV_GL_H_ID(invHId);
						transBean.setGL_DATE(CurrentUserUtil.getCurrentDate());
						transBean.setSOURCE_TAB("XC_AP_PAY_REQ_H");
						transBean.setAP_DOC_CAT_CODE("CGJS");
						transBean.setAP_DOC_CODE(apPayReqHBean.getAP_PAY_REQ_H_CODE());
						transBean.setDESCRIPTION("关闭申请单");
						transBean.setAP_CONTRACT_ID(apPayReqLBean.getAP_CONTRACT_ID());
						transBean.setVENDOR_ID("");
						transBean.setREQ_AMT(noPayAmt*(-1));
						transBean.setTRANS_STATUS("");
						transBean.setCREATION_DATE(CurrentUserUtil.getCurrentDate());
						transBean.setCREATED_BY(CurrentSessionVar.getUserId());
						transBean.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
						transBean.setLAST_UPDATED_BY(CurrentSessionVar.getUserId());
						transList.add(transBean);
						ApInvTransBean delBean = new ApInvTransBean();
						delBean.setSOURCE_ID(apPayReqLBean.getAP_PAY_REQ_H_ID());
						delBean.setSOURCE_DTL_ID(apPayReqLBean.getAP_PAY_REQ_L_ID());
						delBean.setTRANS_STATUS("1");
						delTransList.add(delBean);
					}
						
				}
				apArTransDao.deleteApTrans2(delTransList);//删除交易明细
				apArTransDao.insertApTrans(transList);//插入交易明细
				updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);//更新应付单
				jo.put("flag", "0");
			}
			
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg",e.getMessage());
			log.error(e.getMessage());
			throw e;
		}
		
		return jo;
	}
	/**
	 * @Title delPayReq
	 * @Description:删除付款申请
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	@Override
	public JSONObject delPayReq(String idArray) throws Exception{
		JSONObject jo = new JSONObject();
		try {
			JSONArray array = new JSONArray(idArray);
			List<String> list = new ArrayList<String>();
			List<ApInvTransBean> delTransList = new ArrayList<ApInvTransBean>();
			for(int i=0;i<array.length();i++){
				JSONObject ob = array.getJSONObject(i);
				String payReqHId = ob.getString("id");
				List<HashMap<String,Object>> maps = payReqDao.getPayReqLByHId(payReqHId);
				String apDocCatCode = payReqDao.getPayReqHById(payReqHId).getAP_DOC_CAT_CODE();
				for(HashMap<String,Object> map : maps){
					String payReqLId = map.get("AP_PAY_REQ_L_ID").toString();
					String invHId = map.get("AP_INV_GL_H_ID").toString();
					String amount  = map.get("AMOUNT").toString();
					ApPayReqLBean apPayReqLBean = new ApPayReqLBean();
					delPayReqL(payReqLId);
					apPayReqLBean.setAP_PAY_REQ_L_ID(payReqLId);
					apPayReqLBean.setAP_INV_GL_H_ID(invHId);
					apPayReqLBean.setAMOUNT(Double.valueOf(amount));
					ApInvTransBean transBean = new ApInvTransBean();
					transBean.setSOURCE_ID(payReqHId);
					transBean.setSOURCE_DTL_ID(payReqLId);
					transBean.setAP_INV_GL_H_ID(invHId);
					transBean.setAP_DOC_CAT_CODE(apDocCatCode);
					delTransList.add(transBean);
					if(!"".equals(invHId)&&invHId!=null){
						list.add(invHId);
					}else{
						list = null;
					}
					
				}
				jo = delPayReqH(payReqHId);
				
			}
			apArTransDao.deleteApTrans(delTransList);
			if(list!=null){
				updateInvGlAmountService.updateApInvGlAmount(list);
			}
		} catch (Exception e) {
			jo.put("flag", "1");
			jo.put("msg", e.getMessage());
			throw e;
		}
		return jo;
	}
}
