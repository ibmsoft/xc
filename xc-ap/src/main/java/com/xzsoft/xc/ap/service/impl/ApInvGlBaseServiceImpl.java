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

import com.xzsoft.xc.ap.dao.ApCheckUtilDao;
import com.xzsoft.xc.ap.dao.ApInvGlBaseDao;
import com.xzsoft.xc.ap.dao.ApInvoiceBaseDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.modal.ApCheckUtilBean;
import com.xzsoft.xc.ap.modal.ApInvoiceHBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.ap.service.ApInvGlBaseService;
import com.xzsoft.xc.ap.service.ApVoucherService;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.dao.ApArTransDao;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.bg.api.impl.BudegetDataManageApi;
import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName：ApInvGlBaseServiceImpl
  * @Description：应付模块保存应付单和生成凭证的service实现类
  * @author：RenJianJian
  * @date：2016年8月8日 下午5:53:43
 */
@Service("apInvGlBaseService")
public class ApInvGlBaseServiceImpl implements ApInvGlBaseService {
	public static final Logger log = Logger.getLogger(ApInvGlBaseServiceImpl.class);
	@Resource
	private XCAPCommonDao xcapCommonDao;
	@Resource
	private ApInvGlBaseDao apInvGlBaseDao;
	@Resource
	private RuleService ruleService;
	@Resource
	private ApVoucherService apVoucherService;
	@Resource
	private ApInvoiceBaseDao apInvoiceBaseDao;
	@Resource
	private ApCheckUtilDao apCheckUtilDao;
	@Resource
	private VoucherHandlerService voucherHandlerService;
	@Resource
	private UpdateInvGlAmountService updateInvGlAmountService;
	@Resource
	private ApArTransDao apArTransDao;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * 
	  * <p>Title: saveApInvGlAndVoucher</p>
	  * <p>Description: 保存应付单的方法</p>
	  * @param mainTabInfoJson
	  * @param rowTabInfoJson
	  * @param deleteDtlInfo
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvGlBaseService#saveApInvGlAndVoucher(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject saveApInvGlAndVoucher(String mainTabInfoJson,String rowTabInfoJson, String deleteDtlInfo,String language) throws Exception {
		JSONObject json = new JSONObject();	//返回值
		String apDocCode = "";
		String headId = "";
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			if(mainTabInfoJson == null || "".equals(mainTabInfoJson))
				throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_IS_NOT_NULL", null));//单据信息信息不能为空！
			//单据主信息
			ApDocumentHBean apDocumentH = new ApDocumentHBean();
			JSONObject mainTabInfo = new JSONObject(mainTabInfoJson);
			//对应付单进行保存处理
			String AP_INV_GL_H_ID = mainTabInfo.getString("AP_INV_GL_H_ID");
			//蓝字应付单
			String L_AP_INV_GL_H_ID = mainTabInfo.getString("L_AP_INV_GL_H_ID");
			String LEDGER_ID = mainTabInfo.getString("LEDGER_ID");
			String AP_DOC_CAT_CODE = mainTabInfo.getString("AP_DOC_CAT_CODE");
			String INV_AMOUNT = mainTabInfo.getString("INV_AMOUNT");
			String AP_INV_H_ID = mainTabInfo.getString("AP_INV_H_ID");

			//需要生成凭证的实体类
			ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
			avhb.setLedgerId(LEDGER_ID);
			avhb.setApCatCode("YFD");
			avhb.setAttchTotal(mainTabInfo.getInt("V_ATTCH_TOTAL"));
			avhb.setAccDate(sdf.parse(mainTabInfo.getString("GL_DATE")));
			avhb.setApId(AP_INV_GL_H_ID);
			
			//对单据赋值处理（几个单据共同的）
			apDocumentH.setAP_INV_GL_H_ID(AP_INV_GL_H_ID);
			apDocumentH.setAP_INV_H_ID(mainTabInfo.getString("AP_INV_H_ID"));
			apDocumentH.setL_AP_INV_GL_H_ID(L_AP_INV_GL_H_ID);
			apDocumentH.setLEDGER_ID(LEDGER_ID);
			apDocumentH.setAP_DOC_CAT_CODE(AP_DOC_CAT_CODE);
			apDocumentH.setGL_DATE(sdf.parse(mainTabInfo.getString("GL_DATE")));
			apDocumentH.setVENDOR_ID(mainTabInfo.getString("VENDOR_ID"));
			apDocumentH.setPROJECT_ID(mainTabInfo.getString("PROJECT_ID"));
			apDocumentH.setDEPT_ID(mainTabInfo.getString("DEPT_ID"));
			apDocumentH.setAP_CONTRACT_ID(mainTabInfo.getString("AP_CONTRACT_ID"));

			if(mainTabInfo.getString("AMOUNT") != null && !"".equals(mainTabInfo.getString("AMOUNT")))
				apDocumentH.setAMOUNT(Double.parseDouble(mainTabInfo.getString("AMOUNT")));
			if(mainTabInfo.getString("TAX_AMT") != null && !"".equals(mainTabInfo.getString("TAX_AMT")))
				apDocumentH.setTAX_AMT(Double.parseDouble(mainTabInfo.getString("TAX_AMT")));
			if(mainTabInfo.getString("TAX_RATE") != null && !"".equals(mainTabInfo.getString("TAX_RATE")))
				apDocumentH.setTAX_RATE(Double.parseDouble(mainTabInfo.getString("TAX_RATE")));
			if(INV_AMOUNT != null && !"".equals(INV_AMOUNT))
				apDocumentH.setINV_AMOUNT(Double.parseDouble(INV_AMOUNT));
			if(mainTabInfo.getString("CANCEL_AMT") != null && !"".equals(mainTabInfo.getString("CANCEL_AMT")))
				apDocumentH.setCANCEL_AMT(Double.parseDouble(mainTabInfo.getString("CANCEL_AMT")));
			if(mainTabInfo.getString("PAID_AMT") != null && !"".equals(mainTabInfo.getString("PAID_AMT")))
				apDocumentH.setPAID_AMT(Double.parseDouble(mainTabInfo.getString("PAID_AMT")));
			if(mainTabInfo.getString("REQ_AMT") != null && !"".equals(mainTabInfo.getString("REQ_AMT")))
				apDocumentH.setREQ_AMT(Double.parseDouble(mainTabInfo.getString("REQ_AMT")));
			if(mainTabInfo.getString("ADJ_AMT") != null && !"".equals(mainTabInfo.getString("ADJ_AMT")))
				apDocumentH.setADJ_AMT(Double.parseDouble(mainTabInfo.getString("ADJ_AMT")));
			
			apDocumentH.setAP_ACC_ID_DEBT(mainTabInfo.getString("AP_ACC_ID_DEBT"));
			apDocumentH.setAP_ACC_ID_TAX(mainTabInfo.getString("AP_ACC_ID_TAX"));
			apDocumentH.setSOURCE(mainTabInfo.getString("SOURCE"));
			apDocumentH.setDESCRIPTION(mainTabInfo.getString("DESCRIPTION"));
			apDocumentH.setCREATION_DATE(createDate);
			apDocumentH.setCREATED_BY(userId);
			apDocumentH.setLAST_UPDATE_DATE(createDate);
			apDocumentH.setLAST_UPDATED_BY(userId);
			
			//单据行信息
			JSONArray rowInfoJson = new JSONArray(rowTabInfoJson);
			ApDocumentLBean apDocumentL = null;
			//单据  删除行
			JSONArray deleteDtlJson = new JSONArray(deleteDtlInfo);
			List<ApDocumentLBean> addDocument = new ArrayList<ApDocumentLBean>();
			List<ApDocumentLBean> updateDocument = new ArrayList<ApDocumentLBean>();
			List<String> deleteDocument = new ArrayList<String>();
			if(deleteDtlJson != null && deleteDtlJson.length() > 0){
				for(int i = 0;i < deleteDtlJson.length();i++){
					JSONObject delJson = deleteDtlJson.getJSONObject(i);
					String delDtlId = delJson.getString("AP_INV_GL_L_ID");
					deleteDocument.add(delDtlId);
				}
				apDocumentH.setDeleteDocument(deleteDocument);
			}
			if(rowInfoJson != null && rowInfoJson.length() > 0){
				for(int i = 0;i < rowInfoJson.length();i++){
					JSONObject lJson = rowInfoJson.getJSONObject(i);
					apDocumentL = new ApDocumentLBean();
					apDocumentL.setAP_INV_GL_H_ID(AP_INV_GL_H_ID);
					apDocumentL.setL_AP_INV_GL_L_ID(lJson.getString("L_AP_INV_GL_L_ID"));
					apDocumentL.setL_AP_INV_GL_H_ID(lJson.getString("L_AP_INV_GL_H_ID"));
					apDocumentL.setAP_PUR_TYPE_ID(lJson.getString("AP_PUR_TYPE_ID"));
					apDocumentL.setORDER_H_ID(lJson.getString("ORDER_H_ID"));
					apDocumentL.setORDER_L_ID(lJson.getString("ORDER_L_ID"));
					apDocumentL.setMATERIAL_ID(lJson.getString("MATERIAL_ID"));
					apDocumentL.setBG_ITEM_ID(lJson.getString("BG_ITEM_ID"));
					
					if(lJson.getString("AMOUNT") != null && !"".equals(lJson.getString("AMOUNT")))
						apDocumentL.setAMOUNT(Double.parseDouble(lJson.getString("AMOUNT")));

					apDocumentL.setQTY(lJson.getInt("QTY"));
					apDocumentL.setACC_ID(lJson.getString("ACC_ID"));
					apDocumentL.setCCID(lJson.getString("CCID"));
					apDocumentL.setDIM_CODE(lJson.getString("DIM_CODE"));
					apDocumentL.setCCID(lJson.getString("CCID"));
					apDocumentL.setDESCRIPTION(lJson.getString("DESCRIPTION"));
					apDocumentL.setCREATION_DATE(createDate);
					apDocumentL.setCREATED_BY(userId);
					apDocumentL.setLAST_UPDATE_DATE(createDate);
					apDocumentL.setLAST_UPDATED_BY(userId);
					
					String AP_INV_GL_L_ID = lJson.getString("AP_INV_GL_L_ID");
					if(AP_INV_GL_L_ID != null && !"".equals(AP_INV_GL_L_ID)){//更新处理
						apDocumentL.setAP_INV_GL_L_ID(AP_INV_GL_L_ID);
						updateDocument.add(apDocumentL);
					}
					else{//新增处理
						apDocumentL.setAP_INV_GL_L_ID(java.util.UUID.randomUUID().toString());
						addDocument.add(apDocumentL);
					}
				}
				apDocumentH.setAddDocument(addDocument);
				apDocumentH.setUpdateDocument(updateDocument);
			}
			else{
				throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DTL_IS_NOT_NULL", null));//应付单明细不能为空，请添加应付单明细！
			}
			//通过应付单ID得到应付单信息
			ApDocumentHBean apInvGlHBean = xcapCommonDao.getApInvGlH(AP_INV_GL_H_ID);
			//交易明细表
			ApInvTransBean apInvTrans = new ApInvTransBean();
			//交易明细表赋值
			apInvTrans.setSOURCE_ID(AP_INV_GL_H_ID);
			
			if(L_AP_INV_GL_H_ID != null && !"".equals(L_AP_INV_GL_H_ID)){
				apInvTrans.setAP_INV_GL_H_ID(L_AP_INV_GL_H_ID);
			}
			else{
				apInvTrans.setAP_INV_GL_H_ID(AP_INV_GL_H_ID);
			}
			apInvTrans.setTRANS_ID(java.util.UUID.randomUUID().toString());
			apInvTrans.setSOURCE_DTL_ID(null);
			apInvTrans.setAP_PAY_H_ID(null);
			apInvTrans.setSOURCE_TAB("XC_AP_INV_GL_H");
			apInvTrans.setAP_DOC_CAT_CODE(AP_DOC_CAT_CODE);
			apInvTrans.setDESCRIPTION(mainTabInfo.getString("DESCRIPTION"));
			apInvTrans.setGL_DATE(sdf.parse(mainTabInfo.getString("GL_DATE")));
			apInvTrans.setAP_CONTRACT_ID(mainTabInfo.getString("AP_CONTRACT_ID"));
			apInvTrans.setVENDOR_ID(mainTabInfo.getString("VENDOR_ID"));
			apInvTrans.setCR_AMT(Double.parseDouble(INV_AMOUNT));
			apInvTrans.setDR_AMT(0);
			apInvTrans.setTRANS_STATUS("0");
			apInvTrans.setCCID(null);
			apInvTrans.setCREATED_BY(userId);
			apInvTrans.setCREATION_DATE(createDate);
			apInvTrans.setLAST_UPDATE_DATE(createDate);
			apInvTrans.setLAST_UPDATED_BY(userId);
			
			//根据数据库中是否存在当前操作的应付单，来判断执行数据保存或更新处理
			if(apInvGlHBean != null && !"".equals(apInvGlHBean)){
				if("YFD-H".equals(AP_DOC_CAT_CODE) && L_AP_INV_GL_H_ID != null && !"".equals(L_AP_INV_GL_H_ID)){
					apDocumentH.setNO_PAY_AMT(0);
					apDocumentH.setNO_REQ_AMT(0);
				}
				else{
					if(Double.parseDouble(INV_AMOUNT) != apInvGlHBean.getINV_AMOUNT()){
						apDocumentH.setNO_PAY_AMT(Double.parseDouble(INV_AMOUNT));
						apDocumentH.setNO_REQ_AMT(Double.parseDouble(INV_AMOUNT));
					}
					else{
						apDocumentH.setNO_PAY_AMT(apInvGlHBean.getNO_PAY_AMT());
						apDocumentH.setNO_REQ_AMT(apInvGlHBean.getNO_REQ_AMT());
					}
				}
				apInvGlBaseDao.updateApInvGl(apDocumentH);
				apDocCode = apInvGlHBean.getAP_INV_GL_H_CODE();
				headId = apInvGlHBean.getV_HEAD_ID();
				apInvTrans.setAP_DOC_CODE(apDocCode);
				//更新交易明细表
				List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
				transList.add(apInvTrans);
				apArTransDao.updateApTrans(transList);
				
			}else{
				String year = sdf.format(createDate).substring(0,4);	//年
				String month = sdf.format(createDate).substring(5,7);	//月
				//得到单据编码
				apDocCode = ruleService.getNextSerialNum("AP_INV_GL", LEDGER_ID, "", year, month, userId);

				if(INV_AMOUNT != null && !"".equals(INV_AMOUNT)){
					if("YFD-H".equals(AP_DOC_CAT_CODE) && L_AP_INV_GL_H_ID != null && !"".equals(L_AP_INV_GL_H_ID)){
						apDocumentH.setNO_PAY_AMT(0);
						apDocumentH.setNO_REQ_AMT(0);
					}
					else{
						apDocumentH.setNO_PAY_AMT(Double.parseDouble(INV_AMOUNT));
						apDocumentH.setNO_REQ_AMT(Double.parseDouble(INV_AMOUNT));
					}
				}
				apDocumentH.setAP_INV_GL_H_CODE(apDocCode);
				apInvGlBaseDao.saveApInvGl(apDocumentH);
				apInvTrans.setAP_DOC_CODE(apDocCode);
				//向交易明细表插入数据
				List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
				transList.add(apInvTrans);
				apArTransDao.insertApTrans(transList);
			}
			//应付单红字
			if("YFD-H".equals(AP_DOC_CAT_CODE) && (L_AP_INV_GL_H_ID != null && !"".equals(L_AP_INV_GL_H_ID))){
				//红字发票需要判断应付单的未付金额是否还够冲销当前红色应付单。如果不够需要提示，如果够冲销，需要在冲销完后修改蓝色应付单的未付金额和未申请金额。
				String judReturn = xcapCommonDao.judgmentInvAmount(L_AP_INV_GL_H_ID,Double.parseDouble(INV_AMOUNT) - ((apInvGlHBean == null) ? 0 : apInvGlHBean.getINV_AMOUNT()));
				if(judReturn == null || "".equals(judReturn)){
					List<String> apInvGlIdList = new ArrayList<String>();
					apInvGlIdList.add(L_AP_INV_GL_H_ID);
					updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_SAVE_AMOUNT_FAILED", null));//红冲金额超过未付金额，不能进行冲销！
				}
			}
			//调用生成凭证的方法
			String returnParams = apVoucherService.newVoucher(avhb,language);
			JSONObject jo = new JSONObject(returnParams);
			if("0".equals(jo.getString("flag"))){
				headId = jo.getString("headId");
				if(AP_INV_H_ID != null && !"".equals(AP_INV_H_ID)){
					//调用发票复核的方法
					ApInvoiceHBean apInvoiceH = new ApInvoiceHBean();
					apInvoiceH.setFIN_USER_ID(userId);
					apInvoiceH.setFIN_DATE(createDate);
					apInvoiceH.setSYS_AUDIT_STATUS("F");
					apInvoiceH.setSYS_AUDIT_STATUS_DESC("已复核");
					apInvoiceH.setLAST_UPDATED_BY(userId);
					apInvoiceH.setLAST_UPDATE_DATE(createDate);
					apInvoiceH.setAP_INV_H_ID(mainTabInfo.getString("AP_INV_H_ID"));
					//存储取消复核的单据信息
					List<ApInvoiceHBean> apInvoiceHList = new ArrayList<ApInvoiceHBean>();
					apInvoiceHList.add(apInvoiceH);
					apInvoiceBaseDao.cancelFinApInvoice(apInvoiceHList);
				}
			}
			else{
				throw new Exception(jo.getString("msg"));
			}
			json.put("apDocCode",apDocCode);
			json.put("headId",headId);
			json.put("flag","0");
			json.put("msg",XipUtil.getMessage(language, "XC_AP_INV_GL_SAVE_SUCCESS", null));//应付单保存成功！
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}

	/*
	 * 
	  * <p>Title: cancelFinApInvGl</p>
	  * <p>Description: 对应付单进行取消复核处理</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvGlBaseService#cancelFinApInvGl(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void cancelFinApInvGl(JSONArray ja,String language) throws Exception {
		try {
			ApDocumentHBean apInvGlHBean = null;
			//存储应付单ID信息
			List<String> lists = new ArrayList<String>();
			//存储交易明细表
			List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
			//存储取消复核的单据信息
			List<ApInvoiceHBean> apInvoiceHList = new ArrayList<ApInvoiceHBean>();
			//更新应付单金额
			List<String> apInvGlIdList = new ArrayList<String>();
			//存储凭证头ID信息
			JSONArray headArray = new JSONArray();
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String AP_INV_H_ID = jo.getString("AP_INV_H_ID");//发票ID
					String AP_INV_GL_H_ID = jo.getString("AP_INV_GL_H_ID");//应付单ID
					
					//根据应付单ID查询应付单信息
					apInvGlHBean = xcapCommonDao.getApInvGlH(AP_INV_GL_H_ID);
					
					//判断发票是否被引用
					//6、根据应付单ID，查询该发票生成的应付单凭证状态
					ApCheckUtilBean vStatusNextCount = apCheckUtilDao.checkGlVStatus(AP_INV_GL_H_ID);
					if(vStatusNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_01", null));//该采购发票生成的应付单已经审核通过，请先取消审核！
					
					//1、根据发票ID，查询该发票是否做过红冲
					ApCheckUtilBean glInvHcNextCount = apCheckUtilDao.checkInvGlCreateInvHcByInvId(AP_INV_H_ID);
					if(glInvHcNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_02", null));//该采购发票已做过红冲，不能取消复核！
					
					//2、根据应付单ID，判断当前应付单是否做过付款
					ApCheckUtilBean payNextCount = apCheckUtilDao.checkGlCreatePayByInvId(AP_INV_GL_H_ID);
					if(payNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_03", null));//该采购发票生成的应付单已做过付款，不能取消复核！
			
					//3、根据应付单ID，查询该应付单是否做过核销单主信息
					ApCheckUtilBean cancelHNextCount = apCheckUtilDao.checkGlCreateCancelHByInvId(AP_INV_GL_H_ID);
					if(cancelHNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_04", null));//该采购发票生成的应付单已做过核销单主信息，不能取消复核！
					
					//4、根据应付单ID，判断当前应付单是核销过别的应付单
					ApCheckUtilBean cancelLNextCount = apCheckUtilDao.checkGlCreateCancelLByInvId(AP_INV_GL_H_ID);
					if(cancelLNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_05", null));//该采购发票生成的应付单已做过核销单行信息，不能取消复核！
					
					//5、根据应付单ID，判断当前应付单是否做过红冲
					ApCheckUtilBean glHcCount = apCheckUtilDao.checkGlCreateGlHcByInvId(AP_INV_GL_H_ID);
					if(glHcCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_06", null));//该采购发票生成的应付单已做过红冲，不能取消复核！
					
					//7、根据应付单ID，判断当前应付单是否被申请单占用
					ApCheckUtilBean reqNextCount = apCheckUtilDao.checkGlCreatePayReqByInvId(AP_INV_GL_H_ID);
					if(reqNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_07", null));//该应付单已被付款申请单引用，不能取消复核！

					//8、根据应付单ID，判断当前应付单是做过应收核应付
					ApCheckUtilBean arApCancelNextCount = apCheckUtilDao.checkArApCancelByInvId(AP_INV_GL_H_ID);
					if(arApCancelNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_08", null));//该应付单已经做过应收核应付，不能取消复核！
					
					//调用发票取消复核的方法
					ApInvoiceHBean apInvoiceH = new ApInvoiceHBean();
					apInvoiceH.setFIN_USER_ID(null);
					apInvoiceH.setFIN_DATE(null);
					apInvoiceH.setSYS_AUDIT_STATUS("E");
					apInvoiceH.setSYS_AUDIT_STATUS_DESC("审批通过");
					apInvoiceH.setLAST_UPDATED_BY(CurrentUserUtil.getCurrentUserId());
					apInvoiceH.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					apInvoiceH.setAP_INV_H_ID(AP_INV_H_ID);
					apInvoiceHList.add(apInvoiceH);
					//将单据预算发生数转换为预算占用数
					BudegetDataManageApi.updateFactBudget(AP_INV_H_ID,XCBGConstants.BG_FACT_TYPE);
					
					ApInvTransBean apInvTrans = new ApInvTransBean();
					apInvTrans.setSOURCE_ID(AP_INV_GL_H_ID);
					apInvTrans.setSOURCE_DTL_ID(null);
					apInvTrans.setAP_DOC_CAT_CODE(apInvGlHBean.getAP_DOC_CAT_CODE());
					transList.add(apInvTrans);

					if(apInvGlHBean.getL_AP_INV_GL_H_ID() != null && !"".equals(apInvGlHBean.getL_AP_INV_GL_H_ID()))
						apInvGlIdList.add(apInvGlHBean.getL_AP_INV_GL_H_ID());
					
					lists.add(AP_INV_GL_H_ID);
					
					JSONObject headJson = new JSONObject();
					headJson.put("V_HEAD_ID",apInvGlHBean.getV_HEAD_ID());
					headArray.put(headJson);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//"参数格式不正确！"
				}
			}
			if(transList.size() > 0 && transList != null)
				//删除交易明细表信息
				apArTransDao.deleteApTrans(transList);
			if(apInvoiceHList.size() > 0 && apInvoiceHList != null)
				//取消复核处理
				apInvoiceBaseDao.cancelFinApInvoice(apInvoiceHList);
			if(apInvGlIdList != null && apInvGlIdList.size() > 0)
				//更新应付单金额
				updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);
			if(lists.size() > 0 && lists != null)
				//删除应付单主表和行表
				apInvGlBaseDao.deleteApInvGlH(lists);
			if(headArray.length() > 0 && headArray != null)
				//删除凭证
				voucherHandlerService.doDelVouchers(headArray.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title: deleteApDoc</p>
	  * <p>Description: 删除单据（包括验证是否有凭证（删除凭证），是否存在明细（存在明细不能删除），凭证审核是否通过（审核通过也不能删除），交易明细表也要删除）</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvGlBaseService#deleteApDoc(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteApDoc(JSONArray ja,String language) throws Exception {
		ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
		ApVoucherHandlerBean avlb = new ApVoucherHandlerBean();
		JSONArray headArray = new JSONArray();
		String tableName = null;										//表名
		String lTableName = null;		                                //行表名
		String primaryKey = null;										//主键
		//存储交易明细表
		List<ApInvTransBean> transList = new ArrayList<ApInvTransBean>();
		//更新应付单金额
		List<String> apInvGlIdList = new ArrayList<String>();
		//需要对应付单金额更新操作的应付单ID
		ApDocumentHBean apDocumentH = null;
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String apCatCode = jo.getString("apCatCode");		//单据类型
					String apId = jo.getString("apId");					//单据ID
					if(XCAPConstants.YFD.equals(apCatCode)){			//应付单
						//1、根据应付单ID，判断当前应付单对应的发票是否做过红冲
						ApCheckUtilBean glInvHcNextCount = apCheckUtilDao.checkGlCreateInvHcByInvId(apId);
						if(glInvHcNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_01", null));//该应付单对应的采购发票已做红冲，不能删除！
								
						//2、根据应付单ID，判断当前应付单是否做过红冲
						ApCheckUtilBean glHcNextCount = apCheckUtilDao.checkGlCreateGlHcByInvId(apId);
						if(glHcNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_02", null));//该应付单已做红冲，不能删除！
								
						//3、根据应付单ID，判断当前应付单是否做过付款
						ApCheckUtilBean payNextCount = apCheckUtilDao.checkGlCreatePayByInvId(apId);
						if(payNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_03", null));//该应付单已做付款，不能删除！
						
						//4、根据应付单ID，判断当前应付单是否被别的应付单核销过
						ApCheckUtilBean cancelHNextCount = apCheckUtilDao.checkGlCreateCancelHByInvId(apId);
						if(cancelHNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_04", null));//该应付单已做核销，不能删除！
						
						//5、根据应付单ID，判断当前应付单是核销过别的应付单
						ApCheckUtilBean cancelLNextCount = apCheckUtilDao.checkGlCreateCancelLByInvId(apId);
						if(cancelLNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_04", null));//该应付单已做核销，不能删除！
						
						//6、根据应付单ID，判断当前应付单是否做过调整单
						ApCheckUtilBean adjNextCount = apCheckUtilDao.checkGlCreateAdjByInvId(apId);
						if(adjNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_05", null));//该应付单已做调整，不能删除！
						
						//7、根据应付单ID，判断当前应付单是否被申请单占用
						ApCheckUtilBean reqNextCount = apCheckUtilDao.checkGlCreatePayReqByInvId(apId);
						if(reqNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_06", null));//该应付单已被付款申请单引用，不能删除！

						//8、根据应付单ID，判断当前应付单是做过应收核应付
						ApCheckUtilBean arApCancelNextCount = apCheckUtilDao.checkArApCancelByInvId(apId);
						if(arApCancelNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_10", null));//该应付单已做过应收核应付，不能删除！
						
						tableName = "xc_ap_inv_gl_h";					//应付单主表xc_ap_inv_gl_h
						primaryKey = "AP_INV_GL_H_ID";					//应付单主键IDAP_INV_GL_H_ID
						//应付单据主信息
						apDocumentH = xcapCommonDao.getApInvGlH(apId);
						lTableName = "xc_ap_inv_gl_l";
						if(apDocumentH.getL_AP_INV_GL_H_ID() != null && !"".equals(apDocumentH.getL_AP_INV_GL_H_ID()))
							apInvGlIdList.add(apDocumentH.getL_AP_INV_GL_H_ID());
					}
					else if(XCAPConstants.FKD.equals(apCatCode)){		//付款单
						//1、根据付款单ID，判断当前付款单是否做过红冲
						int payHcNextCount = apCheckUtilDao.checkPayCreateHcByPayId(apId);
						if(payHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_07", null));//该付款单已做红冲，不能删除！
						
						//2、根据付款单ID，判断当前付款单是否做过核销
						int payCancelNextCount = apCheckUtilDao.checkPayCreateCancelByPayId(apId);
						if(payCancelNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_08", null));//该付款单已做核销，不能删除！
						tableName = "xc_ap_pay_h";
						primaryKey = "AP_PAY_H_ID";
						//付款单主信息
						apDocumentH = xcapCommonDao.getApPayH(apId);
						lTableName = "xc_ap_pay_l";
						
						//通过付款单ID查询付款单行表信息
						List<ApDocumentLBean> apDocumentLList = xcapCommonDao.getApPayL(apId);
						for(ApDocumentLBean apDocumentL : apDocumentLList){
							apInvGlIdList.add(apDocumentL.getAP_INV_GL_H_ID());
						}
					}
					else if(XCAPConstants.HXD.equals(apCatCode)){		//核销单
						tableName = "xc_ap_cancel_h";
						primaryKey = "AP_CANCEL_H_ID";
						//核销单主信息
						apDocumentH = xcapCommonDao.getApCancelH(apId);
						lTableName = "xc_ap_cancel_l";
						if(XCAPConstants.HXD_YFHYUF.equals(apDocumentH.getAP_CANCEL_TYPE())){//应收核预收
							apInvGlIdList.add(apDocumentH.getSRC_ID());
						}
					}
					else if(XCAPConstants.YUETZD.equals(apCatCode)){		//余额调整单
						tableName = "xc_ap_inv_gl_adj";
						primaryKey = "GL_ADJ_ID";
						//余额调整单信息
						apDocumentH = xcapCommonDao.getApInvAdj(apId);
						apInvGlIdList.add(apDocumentH.getAP_INV_GL_H_ID());
					}
					if(apDocumentH.getVERIFIER_ID() != null && !"".equals(apDocumentH.getVERIFIER_ID())){//单据已审核，审核人不为空
						throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_09", null));//单据已经审核，不能删除！
					}
					else{
						if(apDocumentH.getV_HEAD_ID() != null && !"".equals(apDocumentH.getV_HEAD_ID())){//凭证已经存在，则删除凭证
							JSONObject headJson = new JSONObject();
							headJson.put("V_HEAD_ID",apDocumentH.getV_HEAD_ID());
							headArray.put(headJson);
						}
					}
					//得到参数
					avhb.setTableName(tableName);
					avhb.setPriKey(primaryKey);
					avhb.setApId(apId);
					
					avlb.setTableName(lTableName);
					avlb.setPriKey(primaryKey);
					avlb.setApId(apId);
					
					//删除交易明细表
					ApInvTransBean apInvTrans = new ApInvTransBean();
					apInvTrans.setSOURCE_ID(apId);
					apInvTrans.setSOURCE_DTL_ID(null);
					apInvTrans.setAP_DOC_CAT_CODE((apDocumentH.getAP_DOC_CAT_CODE() == null || "".equals(apDocumentH.getAP_DOC_CAT_CODE())) ? ((apDocumentH.getAP_CANCEL_TYPE() != null && !"".equals(apDocumentH.getAP_CANCEL_TYPE())) ? apDocumentH.getAP_CANCEL_TYPE() : "YUETZD") : apDocumentH.getAP_DOC_CAT_CODE());
					transList.add(apInvTrans);
					
					if(!XCAPConstants.YUETZD.equals(apCatCode)){
						//删除业务明细表
						xcapCommonDao.deleteApDoc(avlb);
					}
					//删除单据表信息
					xcapCommonDao.deleteApDoc(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			if(transList.size() > 0 && transList != null)
				//删除交易明细表信息
				apArTransDao.deleteApTrans(transList);
			if(apInvGlIdList != null && apInvGlIdList.size() > 0)
				//更新应付单金额
				updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);
			if(headArray.length() > 0 && headArray != null)
				//删除凭证
				voucherHandlerService.doDelVouchers(headArray.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title: checkBudget</p>
	  * <p>Description: 应付模块检查预算是否足够（采购发票、应付单、付款单-其他付款）</p>
	  * 				isChkBg-预算检查：Y-是，N-否
	  * 				apDocId-应付模块ID（采购发票ID、应付单ID、付款单ID）
	  * 				apDocCat-单据类型（采购发票：CGFP、应付单：YFD、付款单：FKD）
	  * @param ja
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvGlBaseService#checkBudget(org.codehaus.jettison.json.JSONArray)
	 */
	@Override
	public JSONObject checkBudget(JSONArray ja,String language) throws Exception {
		JSONObject jo = new JSONObject();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject json = new JSONObject(String.valueOf(obj));
					String isChkBg = json.getString("isChkBg");				//预算检查
					String apDocId = json.getString("apDocId");				//单据ID
					String apDocCat = json.getString("apDocCat");			//单据类型
					if("Y".equals(isChkBg)){
						ApDocumentHBean apDocumentH = null;
						//查询单据主信息
						if(XCAPConstants.CGFP.equals(apDocCat)){//采购发票
							apDocumentH = xcapCommonDao.getApInvoiceH(apDocId);
						}
						if(XCAPConstants.YFD.equals(apDocCat)){//应付单
							apDocumentH = xcapCommonDao.getApInvGlH(apDocId);
						}
						if(XCAPConstants.FKD.equals(apDocCat)){//付款单
							apDocumentH = xcapCommonDao.getApPayH(apDocId);
						}
						//行信息
						List<ApDocumentLBean> apDocumentLList = xcapCommonDao.getApDocLByBgItemId(apDocId,apDocCat);
						apDocumentH.setApDocumentLBean(apDocumentLList);
						//执行校验
						try {
							jo = this.chkBudgetByDtl(apDocumentH);
						} catch (Exception e){
							throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_CHECK_BUDGET_FAILED", null)+e.getMessage());//预算校验失败：
						}
						
					}else{
						jo.put("flag", "0");
					}
				}
			}
					
		} catch (Exception e) {
			throw e;
		}
		return jo;
	}
	/**
	 * 
	  * @Title: chkBudgetByDtl 方法名
	  * @Description：按单据明细检查预算
	  * @param：@param apDocumentH
	  * @param：@return
	  * @param：@throws Exception 设定文件
	  * @return JSONObject 返回类型
	 */
	private JSONObject chkBudgetByDtl(ApDocumentHBean apDocumentH) throws Exception {
		JSONObject jo = new JSONObject();
		//合并单据明细行 
		List<ApDocumentLBean> docDtlBeans  = apDocumentH.getApDocumentLBean();
		if(docDtlBeans != null && docDtlBeans.size() >0){
			int i = 0;
			int j = 0;
			StringBuffer str = new StringBuffer();
			//PA项目
			String projectId = "A";
			if(apDocumentH.getPROJECT_ID() != null && !"".equals(apDocumentH.getPROJECT_ID() )){
				projectId = apDocumentH.getPROJECT_ID();
			}
			for(ApDocumentLBean dtlBean : docDtlBeans){
				//预算项目
				String bgItemId = "A";
				if(dtlBean.getBG_ITEM_ID() != null && !"".equals(dtlBean.getBG_ITEM_ID() )){
					bgItemId = dtlBean.getBG_ITEM_ID();
				}
				if((projectId == null || "".equals(projectId)) && (bgItemId == null || "".equals(bgItemId))){
					continue;
				}else{
					//执行预算校验
					net.sf.json.JSONObject resultJo = BudegetDataManageApi.checkBudgetIsValid(apDocumentH.getLEDGER_ID(),projectId,bgItemId,
								XCDateUtil.date2Str((apDocumentH.getBIZ_DATE() == null) ? apDocumentH.getGL_DATE() : apDocumentH.getBIZ_DATE()),
								null,apDocumentH.getDEPT_ID(),String.valueOf(dtlBean.getAMOUNT()));
					String flag = resultJo.getString("flag");
					String msg = resultJo.getString("msg");
					if("-1".equals(flag)){//预算校验不通过
						i++;
					}else if("1".equals(flag)){//预算校验警告信息
						j++;
					}
					str.append(msg).append("<br>");
				}
			}
			
			//处理提示信息
			if(i > 0){
				jo.put("flag", "-1");
				jo.put("msg", str.toString());
				
			}else{
				if(j > 0){
					jo.put("flag", "1");
					jo.put("msg", str.toString());
				}
				else{
					jo.put("flag", "0");
				}
			}
		}
		return jo;
	}
	/*
	 * 
	  * <p>Title: recordBudgetOccupancyAmount</p>
	  * <p>Description: 记录预算占用数</p>
	  * @param apDocId
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvGlBaseService#recordBudgetOccupancyAmount(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void recordBudgetOccupancyAmount(String apDocId,String apDocCat) throws Exception {
		try {
			ApDocumentHBean apDocumentH = null;
			//查询单据主信息
			if(XCAPConstants.CGFP.equals(apDocCat)){//采购发票
				apDocumentH = xcapCommonDao.getApInvoiceH(apDocId);
			}
			if(XCAPConstants.YFD.equals(apDocCat)){//应付单
				apDocumentH = xcapCommonDao.getApInvGlH(apDocId);
				if(apDocumentH.getAP_INV_H_ID() != null && !"".equals(apDocumentH.getAP_INV_H_ID()))
					//删除预算占用数
					this.deleteBudgetOccupancyAmount(apDocumentH.getAP_INV_H_ID(),"CGFP");
			}
			if(XCAPConstants.FKD.equals(apDocCat)){//付款单
				apDocumentH = xcapCommonDao.getApPayH(apDocId);
			}
			//根据单据明细处理预算占用数
			List<ApDocumentLBean> apDocumentLList = xcapCommonDao.getApDocLByBgItemId(apDocId,apDocCat);
			//参数
			List<BgFactBean> list = new ArrayList<BgFactBean>();
			list = this.getBgFactBeanList(apDocumentH,apDocumentLList);
			
			//记录预算占用数
			if(!list.isEmpty()){
				BudegetDataManageApi.incrOccupancyBudget(list);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * @Title: getBgFactBeanList 
	 * @Description: 预算数据处理
	 * @param apDocumentH
	 * @param dtlBeans
	 * @return
	 * @throws Exception    设定文件
	 */
	private List<BgFactBean> getBgFactBeanList(ApDocumentHBean apDocumentH, List<ApDocumentLBean> dtlBeans) throws Exception {
		//参数
		List<BgFactBean> list = new ArrayList<BgFactBean>();
		//当前用户ID和日期
		String userId = CurrentSessionVar.getUserId();
		Date cdate = CurrentUserUtil.getCurrentDate();
		
		if(dtlBeans != null && dtlBeans.size() >0){
			//根据单据明细重新计算占用数
			for(ApDocumentLBean dtlBean : dtlBeans){
				String projectId = apDocumentH.getPROJECT_ID();
				String bgItemId = dtlBean.getBG_ITEM_ID();
				if((projectId != null && !"".equals(projectId)) || (bgItemId != null && !"".equals(bgItemId))){
					BgFactBean bean = new BgFactBean();
					bean.setFactId(UUID.randomUUID().toString());
					bean.setLedgerId(apDocumentH.getLEDGER_ID());
					bean.setProjectId(projectId);
					bean.setBgItemId(bgItemId);
					bean.setDeptId(apDocumentH.getDEPT_ID()==null ? "A" : apDocumentH.getDEPT_ID());
					bean.setFactDate((apDocumentH.getBIZ_DATE() == null) ? apDocumentH.getGL_DATE() : apDocumentH.getBIZ_DATE());
					bean.setAmount(dtlBean.getAMOUNT());
					if((apDocumentH.getAP_INV_H_ID() != null && !"".equals(apDocumentH.getAP_INV_H_ID())) 
						&& (apDocumentH.getAP_INV_GL_H_ID() == null || "".equals(apDocumentH.getAP_INV_GL_H_ID()))){//来源是发票的时候，是预算占用
						bean.setFactType(XCBGConstants.BG_OCCUR_TYPE);
						bean.setSrcId(apDocumentH.getAP_INV_H_ID());
						bean.setSrcTab("XC_AP_INVOICE_H");
					}
					else if((apDocumentH.getAP_INV_H_ID() != null && !"".equals(apDocumentH.getAP_INV_H_ID())) 
						|| (apDocumentH.getAP_INV_GL_H_ID() != null && !"".equals(apDocumentH.getAP_INV_GL_H_ID()))){//来源是发票复核生成的应付单的时候，是预算发生
						bean.setFactType(XCBGConstants.BG_FACT_TYPE);
						bean.setSrcId(apDocumentH.getAP_INV_GL_H_ID());
						bean.setSrcTab("XC_AP_INV_GL_H");
					}
					else if((apDocumentH.getAP_INV_H_ID() == null || "".equals(apDocumentH.getAP_INV_H_ID())) 
						&& (apDocumentH.getAP_INV_GL_H_ID() != null && !"".equals(apDocumentH.getAP_INV_GL_H_ID()))){//来源是手工录入的应付单的时候，是预算发生
						bean.setFactType(XCBGConstants.BG_FACT_TYPE);
						bean.setSrcId(apDocumentH.getAP_INV_GL_H_ID());
						bean.setSrcTab("XC_AP_INV_GL_H");
					}
					else{//来源是付款，是预算发生
						bean.setFactType(XCBGConstants.BG_FACT_TYPE);
						bean.setSrcId(apDocumentH.getAP_PAY_H_ID());
						bean.setSrcTab("XC_AP_PAY_H");
					}
//					bean.setFactType((apDocumentH.getAP_INV_H_ID() == null || "".equals(apDocumentH.getAP_INV_H_ID())) ? XCBGConstants.BG_FACT_TYPE : XCBGConstants.BG_OCCUR_TYPE); //预算占用数增加
//					bean.setSrcId((apDocumentH.getAP_INV_H_ID() == null || "".equals(apDocumentH.getAP_INV_H_ID())) ? ((apDocumentH.getAP_INV_GL_H_ID() == null || "".equals(apDocumentH.getAP_INV_GL_H_ID())) ? apDocumentH.getAP_PAY_H_ID() : apDocumentH.getAP_INV_GL_H_ID()) : apDocumentH.getAP_INV_H_ID());
//					bean.setSrcTab((apDocumentH.getAP_INV_H_ID() == null || "".equals(apDocumentH.getAP_INV_H_ID())) ? ((apDocumentH.getAP_INV_GL_H_ID() == null || "".equals(apDocumentH.getAP_INV_GL_H_ID())) ? "XC_AP_PAY_H" : "XC_AP_INV_GL_H") : "XC_AP_INVOICE_H");
					bean.setCreatedBy(userId);
					bean.setCreationDate(cdate);
					bean.setLastUpdateBy(userId);
					bean.setLastUpdateDate(cdate);
					list.add(bean);
				}
			}
		}
		return list;
	}
	/*
	 * 
	  * <p>Title: deleteBudgetOccupancyAmount</p>
	  * <p>Description: 删除预算占用数</p>
	  * @param docId
	  * @param catCode
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApInvGlBaseService#deleteBudgetOccupancyAmount(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteBudgetOccupancyAmount(String apDocId,String apDocCat) throws Exception {
		try {
			if(XCAPConstants.CGFP.equals(apDocCat)){//采购发票
				//删除预算占用数
				BudegetDataManageApi.deleteFactBudget(apDocId,XCBGConstants.BG_OCCUR_TYPE);
			}
			if(XCAPConstants.YFD.equals(apDocCat)){//应付单
				ApDocumentHBean apInvGlH = xcapCommonDao.getApInvGlH(apDocId);
				if(apInvGlH.getAP_INV_H_ID() != null && !"".equals(apInvGlH.getAP_INV_H_ID()))
					//记录预算占用数
					this.recordBudgetOccupancyAmount(apInvGlH.getAP_INV_H_ID(),"CGFP");
				//删除预算发生数
				BudegetDataManageApi.deleteFactBudget(apDocId,XCBGConstants.BG_FACT_TYPE);
			}
			if(XCAPConstants.FKD.equals(apDocCat)){//付款单
				//删除预算发生数
				BudegetDataManageApi.deleteFactBudget(apDocId,XCBGConstants.BG_FACT_TYPE);
			}
		} catch (Exception e) {
			throw e;
		}
	}
}