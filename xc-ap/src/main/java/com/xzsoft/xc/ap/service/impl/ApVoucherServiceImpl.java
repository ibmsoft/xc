package com.xzsoft.xc.ap.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.ap.dao.ApCheckUtilDao;
import com.xzsoft.xc.ap.dao.ApVoucherDao;
import com.xzsoft.xc.ap.dao.XCAPCommonDao;
import com.xzsoft.xc.ap.modal.ApCheckUtilBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.ap.service.ApInvGlBaseService;
import com.xzsoft.xc.ap.service.ApVoucherGroupService;
import com.xzsoft.xc.ap.service.ApVoucherService;
import com.xzsoft.xc.ap.util.XCAPConstants;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.gl.common.impl.VoucherHandlerCommonService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * 
  * @ClassName ApVoucherServiceImpl
  * @Description 应付模块凭证处理
  * @author 任建建
  * @date 2016年4月5日 下午8:09:11
 */
@Service("apVoucherService")
public class ApVoucherServiceImpl implements ApVoucherService{
	public static final Logger log = Logger.getLogger(ApVoucherServiceImpl.class);
	@Resource
	private VoucherHandlerCommonService voucherHandlerCommonService;
	@Resource
	private VoucherHandlerService voucherHandlerService;
	@Resource
	private XCGLCommonDAO xcglCommonDAO;
	@Resource
	private XCAPCommonDao xcapCommonDao;
	@Resource
	private ApVoucherDao apVoucherDao;
	@Resource
	private ApCheckUtilDao apCheckUtilDao;
	@Resource
	private ApInvGlBaseService apInvGlBaseService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * 
	  * <p>Title newVoucher</p>
	  * <p>Description 生成凭证处理</p>
	  * @param avhb
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherService#newVoucher(com.xzsoft.xc.ap.modal.ApVoucherHandlerBean, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String newVoucher(ApVoucherHandlerBean avhb,String language) throws Exception {
		String returnParams = null;	//返回值
		String tableName = null;	//表名
		String primaryKey = null;	//主键
		try {
			//账簿信息
			Ledger ledger = xcglCommonDAO.getLedger(avhb.getLedgerId());
			//凭证来源
			VSource vs = xcglCommonDAO.getVSource(XCAPConstants.XC_AP_PAY);
			
			// 辅助参数
			JSONObject paramJo = new JSONObject();
			paramJo.put("accDate",sdf.format(avhb.getAccDate()));//凭证日期
			paramJo.put("sourceId",avhb.getApId());				//凭证来源ID
			paramJo.put("attchTotal",avhb.getAttchTotal());		//附件张数（只有应付单有，其他单据传的附件张数0）
			String paramJson = paramJo.toString();
			JSONArray headArray = new JSONArray();
			JSONObject headJson = new JSONObject();
			VHead head = new VHead();
			//获取参数信息 
			String apCatCode = avhb.getApCatCode();				//单据类型
			if(XCAPConstants.YFD.equals(apCatCode)){			//应付单
				tableName = "xc_ap_inv_gl_h";					//应付单主表xc_ap_inv_gl_h
				primaryKey = "AP_INV_GL_H_ID";					//应付单主键IDAP_INV_GL_H_ID
				//应付单据主信息
				ApDocumentHBean apInvGlH = xcapCommonDao.getApInvGlH(avhb.getApId());
				String headId = apInvGlH.getV_HEAD_ID();//凭证头ID
				String vStatus = apInvGlH.getV_STATUS();//凭证状态
				if(vStatus == null || "".equals(vStatus) || "1".equals(vStatus)){
					if(headId != null && !"".equals(headId)){//凭证已经存在，但是草稿状态，则删除凭证
						headJson.put("V_HEAD_ID",headId);
						headArray.put(headJson);
					}
					ApVoucherGroupService handleDocService = (ApVoucherGroupService)AppContext.getApplicationContext().getBean("handleDocService");
					head = handleDocService.mergeVoucher(apInvGlH,ledger,vs,paramJson,language);
				}
			}
			else if(XCAPConstants.FKD.equals(apCatCode)){//付款单
				tableName = "xc_ap_pay_h";
				primaryKey = "AP_PAY_H_ID";
				//付款单据主信息
				ApDocumentHBean apPayH = xcapCommonDao.getApPayH(avhb.getApId());
				String headId = apPayH.getV_HEAD_ID();//凭证头ID
				String vStatus = apPayH.getV_STATUS();//凭证状态
				if(vStatus == null || "".equals(vStatus) || "1".equals(vStatus)){
					if(headId != null && !"".equals(headId)){//凭证已经存在，但是草稿状态，则删除凭证
						headJson.put("V_HEAD_ID",headId);
						headArray.put(headJson);
					}
					ApVoucherGroupService receiptDocService = (ApVoucherGroupService)AppContext.getApplicationContext().getBean("receiptDocService");
					head = receiptDocService.mergeVoucher(apPayH,ledger,vs,paramJson,language);
				}
			}
			else if(XCAPConstants.HXD.equals(apCatCode)){//核销单
				tableName = "xc_ap_cancel_h";
				primaryKey = "AP_CANCEL_H_ID";
				//核销单据主信息
				ApDocumentHBean apCancelH = xcapCommonDao.getApCancelH(avhb.getApId());
				String headId = apCancelH.getV_HEAD_ID();//凭证头ID
				String vStatus = apCancelH.getV_STATUS();//凭证状态
				if(vStatus == null || "".equals(vStatus) || "1".equals(vStatus)){
					if(headId != null && !"".equals(headId)){//凭证已经存在，但是草稿状态，则删除凭证
						headJson.put("V_HEAD_ID",headId);
						headArray.put(headJson);
					}
					ApVoucherGroupService verificationDocService = (ApVoucherGroupService)AppContext.getApplicationContext().getBean("verificationDocService");
					head = verificationDocService.mergeVoucher(apCancelH,ledger,vs,paramJson,language);
				}
			}
			else if(XCAPConstants.YUETZD.equals(apCatCode)){//余额调整单
				tableName = "xc_ap_inv_gl_adj";
				primaryKey = "GL_ADJ_ID";
				//余额调整单据信息
				ApDocumentHBean apInvAdj = xcapCommonDao.getApInvAdj(avhb.getApId());
				String headId = apInvAdj.getV_HEAD_ID();//凭证头ID
				String vStatus = apInvAdj.getV_STATUS();//凭证状态
				if(vStatus == null || "".equals(vStatus) || "1".equals(vStatus)){
					if(headId != null && !"".equals(headId)){//凭证已经存在，但是草稿状态，则删除凭证
						headJson.put("V_HEAD_ID",headId);
						headArray.put(headJson);
					}
					ApVoucherGroupService balAdjDocService = (ApVoucherGroupService)AppContext.getApplicationContext().getBean("balAdjDocService");
					head = balAdjDocService.mergeVoucher(apInvAdj,ledger,vs,paramJson,language);
				}
			}
			if(headArray.length() > 0 && headArray != null)
				//保存凭证之前先删除之前的凭证
				voucherHandlerService.doDelVouchers(headArray.toString());
			//将凭证信息转换成JSON串
			String jsonObject = voucherHandlerCommonService.headBean2JsonStr(head,null);
			//保存凭证信息
			returnParams = voucherHandlerService.doSaveVoucher(jsonObject);
			JSONObject returnJo = new JSONObject(returnParams);
			if(returnJo.getString("flag").equals("0")){
				//得到参数
				avhb.setTableName(tableName);
				avhb.setHeadId(returnJo.getString("headId"));
				avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
				avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
				avhb.setPriKey(primaryKey);
				avhb.setApId(avhb.getApId());
				//更新单据中的凭证信息
				apVoucherDao.saveNewApVoucher(avhb);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			JSONObject jo = new JSONObject();
			jo.put("flag","1");
			jo.put("msg",XipUtil.getMessage(language, "XC_VOUCEHR_SAVE_FAILED", null)+e.getMessage());//生成凭证失败！原因：
			jo.put("headId","");
			returnParams = jo.toString();
			throw e;
		}
		return returnParams;
	}
	/*
	 * 
	  * <p>Title checkVoucher</p>
	  * <p>Description 凭证审核处理</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherService#checkVoucher(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void checkVoucher(JSONArray ja,String language) throws Exception {
		String tableName = null;										//表名
		String primaryKey = null;										//主键
		JSONArray headArray = new JSONArray();
		List<ApVoucherHandlerBean> avhbList = new ArrayList<ApVoucherHandlerBean>();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String apId = jo.getString("apId");					//单据ID
					String apCatCode = jo.getString("apCatCode");		//单据类型
					String headId = jo.getString("headId");
					JSONObject headJson = new JSONObject();
					headJson.put("V_HEAD_ID",headId);
					headArray.put(headJson);
					
					if(XCAPConstants.YFD.equals(apCatCode)){			//应付单
						tableName = "xc_ap_inv_gl_h";					//应付单主表xc_ap_inv_gl_h
						primaryKey = "AP_INV_GL_H_ID";					//应付单主键IDAP_INV_GL_H_ID
						//通过应付单ID得到应付单信息
						ApDocumentHBean apInvGlH = xcapCommonDao.getApInvGlH(apId);
						if(!"INV".equals(apInvGlH.getSOURCE()))
							//记录预算发生数
							apInvGlBaseService.recordBudgetOccupancyAmount(apId,apCatCode);
					}
					else if(XCAPConstants.FKD.equals(apCatCode)){		//付款单
						tableName = "xc_ap_pay_h";
						primaryKey = "AP_PAY_H_ID";
						//通过付款单ID得到付款单信息
						ApDocumentHBean apPayH = xcapCommonDao.getApPayH(apId);
						//付款单-其他付款和付款单-其他付款红字才能进行预算控制
						if(XCAPConstants.FKD_QTFK.equals(apPayH.getAP_DOC_CAT_CODE()) || XCAPConstants.FKD_QTFK_H.equals(apPayH.getAP_DOC_CAT_CODE()))
							//记录预算发生数
							apInvGlBaseService.recordBudgetOccupancyAmount(apId,apCatCode);
					}
					else if(XCAPConstants.HXD.equals(apCatCode)){		//核销单
						tableName = "xc_ap_cancel_h";
						primaryKey = "AP_CANCEL_H_ID";
					}
					else if(XCAPConstants.YUETZD.equals(apCatCode)){	//余额调整单
						tableName = "xc_ap_inv_gl_adj";
						primaryKey = "GL_ADJ_ID";
					}
					ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
					//得到参数
					avhb.setTableName(tableName);
					avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					avhb.setVerifierId(CurrentUserUtil.getCurrentUserId());
					avhb.setVerifyDate(CurrentUserUtil.getCurrentDate());
					avhb.setvStatus("3");
					avhb.setPriKey(primaryKey);
					avhb.setApId(apId);
					avhbList.add(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_CANCEL_FIN_08", null));//参数格式不正确！
				}
			}
			//执行凭证提交
			voucherHandlerService.doSubmitVouchers(headArray.toString());
			//凭证提交成功之后再提交审核
			voucherHandlerService.doCheckVouchers(headArray.toString());
			//凭证审核通过之后，执行单据中的凭证状态信息更新
			apVoucherDao.saveCheckApVoucher(avhbList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title cancelCheckVoucher</p>
	  * <p>Description 取消凭证审核</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherService#cancelCheckVoucher(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void cancelCheckVoucher(JSONArray ja,String language) throws Exception {
		String tableName = null;										//表名
		String primaryKey = null;										//主键
		JSONArray headArray = new JSONArray();
		List<ApVoucherHandlerBean> avhbList = new ArrayList<ApVoucherHandlerBean>();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String headId = jo.getString("headId");
					JSONObject headJson = new JSONObject();
					//通过凭证头id查询凭证信息
					VHead vHead = voucherHandlerService.getVHead(headId);
					if(!"".equals(vHead.getSignatoryId()) && vHead.getSignatoryId() != null){//单据已经签字
						throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_01", null));//所选单据已经签字，不能取消审核！
					}
					if(!"".equals(vHead.getBookkeeperId()) && vHead.getBookkeeperId() != null){//单据已经记账
						throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_02", null));//所选单据已经记账，不能取消审核！
					}
					headJson.put("V_HEAD_ID",headId);
					headArray.put(headJson);
					
					String apId = jo.getString("apId");					//单据ID
					String apCatCode = jo.getString("apCatCode");		//单据类型
					if(XCAPConstants.YFD.equals(apCatCode)){			//应付单
						//1、根据应付单ID，判断当前应付单对应的发票是否做过红冲
						ApCheckUtilBean glInvHcNextCount = apCheckUtilDao.checkGlCreateInvHcByInvId(apId);
						if(glInvHcNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_03", null));//该应付单对应的采购发票已做红冲，不能取消审核！
								
						//2、根据应付单ID，判断当前应付单是否做过红冲
						ApCheckUtilBean glHcNextCount = apCheckUtilDao.checkGlCreateGlHcByInvId(apId);
						if(glHcNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_04", null));//该应付单已做红冲，不能取消审核！
								
						//3、根据应付单ID，判断当前应付单是否做过付款
						ApCheckUtilBean payNextCount = apCheckUtilDao.checkGlCreatePayByInvId(apId);
						if(payNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_05", null));//该应付单已做付款，不能取消审核！
						
						//4、根据应付单ID，判断当前应付单是否被别的应付单核销过
						ApCheckUtilBean cancelHNextCount = apCheckUtilDao.checkGlCreateCancelHByInvId(apId);
						if(cancelHNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_06", null));//该应付单已做核销，不能取消审核！
						
						//5、根据应付单ID，判断当前应付单是核销过别的应付单
						ApCheckUtilBean cancelLNextCount = apCheckUtilDao.checkGlCreateCancelLByInvId(apId);
						if(cancelLNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_07", null));//该应付单已做核销，不能取消审核！
						
						//6、根据应付单ID，判断当前应付单是否做过调整单
						ApCheckUtilBean adjNextCount = apCheckUtilDao.checkGlCreateAdjByInvId(apId);
						if(adjNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_08", null));//该应付单已做调整，不能取消审核！

						//7、根据应付单ID，判断当前应付单是否被申请单占用
						ApCheckUtilBean reqNextCount = apCheckUtilDao.checkGlCreatePayReqByInvId(apId);
						if(reqNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_09", null));//该应付单已被付款申请单引用，不能取消审核！

						//8、根据应付单ID，判断当前应付单是做过应收核应付
						ApCheckUtilBean arApCancelNextCount = apCheckUtilDao.checkArApCancelByInvId(apId);
						if(arApCancelNextCount.getNEXT_COUNT() > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_12", null));//该应付单已经做过应收核应付，不能取消审核！
						
						tableName = "xc_ap_inv_gl_h";					//应付单主表xc_ap_inv_gl_h
						primaryKey = "AP_INV_GL_H_ID";					//应付单主键IDAP_INV_GL_H_ID
						//通过应付单ID得到应付单信息
						ApDocumentHBean apInvGlH = xcapCommonDao.getApInvGlH(apId);
						if(!"INV".equals(apInvGlH.getSOURCE()))
							//删除预算占用数
							apInvGlBaseService.deleteBudgetOccupancyAmount(apId,apCatCode);
					}
					else if(XCAPConstants.FKD.equals(apCatCode)){		//付款单
						//1、根据付款单ID，判断当前付款单是否做过红冲
						int payHcNextCount = apCheckUtilDao.checkPayCreateHcByPayId(apId);
						if(payHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_10", null));//该付款单已做红冲，不能取消审核！
						
						//2、根据付款单ID，判断当前付款单是否做过核销
						int payCancelNextCount = apCheckUtilDao.checkPayCreateCancelByPayId(apId);
						if(payCancelNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_CHECK_11", null));//该付款单已做核销，不能取消审核！
						tableName = "xc_ap_pay_h";
						primaryKey = "AP_PAY_H_ID";
						//通过付款单ID得到付款单信息
						ApDocumentHBean apPayH = xcapCommonDao.getApPayH(apId);
						//付款单-其他付款和付款单-其他付款红字才能进行预算控制
						if(XCAPConstants.FKD_QTFK.equals(apPayH.getAP_DOC_CAT_CODE()) || XCAPConstants.FKD_QTFK_H.equals(apPayH.getAP_DOC_CAT_CODE()))
							//删除预算占用数
							apInvGlBaseService.deleteBudgetOccupancyAmount(apId,apCatCode);
					}
					else if(XCAPConstants.HXD.equals(apCatCode)){		//核销单
						tableName = "xc_ap_cancel_h";
						primaryKey = "AP_CANCEL_H_ID";
					}
					else if(XCAPConstants.YUETZD.equals(apCatCode)){	//余额调整单
						tableName = "xc_ap_inv_gl_adj";
						primaryKey = "GL_ADJ_ID";
					}
					ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
					//得到参数
					avhb.setTableName(tableName);
					avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					avhb.setVerifierId(null);
					avhb.setVerifyDate(null);
					avhb.setvStatus("1");
					avhb.setPriKey(primaryKey);
					avhb.setApId(apId);
					avhbList.add(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			//执行取消凭证审核
			voucherHandlerService.doUncheckVouchers(headArray.toString());
			//取消凭证审核之后取消提交
			voucherHandlerService.doCancelSubmitVouchers(headArray.toString());
			//凭证取消提交之后，更新单据中的凭证状态
			apVoucherDao.saveCheckApVoucher(avhbList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title signVoucher</p>
	  * <p>Description 签字</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherService#signVoucher(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void signVoucher(JSONArray ja,String language) throws Exception {
		JSONArray headArray = new JSONArray();
		List<ApVoucherHandlerBean> avhbList = new ArrayList<ApVoucherHandlerBean>();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String headId = jo.getString("headId");
					JSONObject headJson = new JSONObject();
					headJson.put("V_HEAD_ID",headId);
					headArray.put(headJson);
					
					String apId = jo.getString("apId");					//单据ID
					ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
					//得到参数
					avhb.setSignUserId(CurrentUserUtil.getCurrentUserId());
					avhb.setSignDate(CurrentUserUtil.getCurrentDate());
					avhb.setvStatus("3");
					avhb.setSignStatus("2");
					avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					avhb.setApId(apId);
					avhbList.add(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			//执行凭证签字
			voucherHandlerService.doSignVouchers(headArray.toString());
			//签字成功之后，保存签字后的单据信息
			apVoucherDao.saveSignApVoucher(avhbList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title cancelSignVoucher</p>
	  * <p>Description 取消签字</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ap.service.ApVoucherService#cancelSignVoucher(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void cancelSignVoucher(JSONArray ja,String language) throws Exception {
		JSONArray headArray = new JSONArray();
		List<ApVoucherHandlerBean> avhbList = new ArrayList<ApVoucherHandlerBean>();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String headId = jo.getString("headId");
					JSONObject headJson = new JSONObject();
					//通过凭证头id查询凭证信息
					VHead vHead = voucherHandlerService.getVHead(headId);
					if(!"".equals(vHead.getBookkeeperId()) && vHead.getBookkeeperId() != null){//单据已经记账
						throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_SIGN_01", null));//所选单据已经记账，不能取消签字！
					}
					headJson.put("V_HEAD_ID",headId);
					headArray.put(headJson);
					
					String apId = jo.getString("apId");					//单据ID
					//得到参数
					ApVoucherHandlerBean avhb = new ApVoucherHandlerBean();
					avhb.setSignUserId(null);
					avhb.setSignDate(null);
					avhb.setvStatus("3");
					avhb.setSignStatus("3");
					avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					avhb.setApId(apId);
					avhbList.add(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			//执行取消凭证签字
			voucherHandlerService.doUnsignVouchers(headArray.toString());
			//取消签字成功之后，保存签字后的单据信息
			apVoucherDao.saveSignApVoucher(avhbList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}