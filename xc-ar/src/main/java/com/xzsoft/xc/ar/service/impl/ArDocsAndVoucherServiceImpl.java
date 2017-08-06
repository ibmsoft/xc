package com.xzsoft.xc.ar.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.xzsoft.xc.apar.modal.ApInvTransBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentLBean;
import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.apar.service.UpdateInvGlAmountService;
import com.xzsoft.xc.ar.dao.ArCancelBaseDao;
import com.xzsoft.xc.ar.dao.ArCheckUtilDao;
import com.xzsoft.xc.ar.dao.ArDocsAndVoucherDao;
import com.xzsoft.xc.ar.dao.ArInvoiceBaseDao;
import com.xzsoft.xc.ar.dao.ArPayBaseDao;
import com.xzsoft.xc.ar.dao.XCARCommonDao;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;
import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;
import com.xzsoft.xc.ar.service.ArDocsAndVoucherService;
import com.xzsoft.xc.ar.service.ArVoucherGroupService;
import com.xzsoft.xc.ar.util.XCARConstants;
import com.xzsoft.xc.gl.common.impl.VoucherHandlerCommonService;
import com.xzsoft.xc.gl.dao.XCGLCommonDAO;
import com.xzsoft.xc.gl.modal.VHead;
import com.xzsoft.xc.gl.modal.VSource;
import com.xzsoft.xc.gl.service.VoucherHandlerService;
import com.xzsoft.xc.util.modal.Ledger;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.framework.util.AppContext;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;
/**
 * 
  * @ClassName ArDocsAndVoucherServiceImpl
  * @Description 应收模块保存单据和生成凭证的业务处理层实现类
  * @author 任建建
  * @date 2016年7月8日 上午11:10:49
 */
@Service("arDocsAndVoucherService")
public class ArDocsAndVoucherServiceImpl implements ArDocsAndVoucherService{
	public static final Logger log = Logger.getLogger(ArDocsAndVoucherServiceImpl.class);
	@Resource
	private XCGLCommonDAO xcglCommonDAO;
	@Resource
	private VoucherHandlerService voucherHandlerService;
	@Resource
	private VoucherHandlerCommonService voucherHandlerCommonService;
	@Resource
	private RuleService ruleService;
	@Resource
	private XCARCommonDao xcarCommonDao;
	@Resource
	private ArDocsAndVoucherDao arDocsAndVoucherDao;
	@Resource
	private ArCheckUtilDao arCheckUtilDao;
	@Resource
	private ArInvoiceBaseDao arInvoiceBaseDao;
	@Resource
	private ArCancelBaseDao arCancelBaseDao;
	@Resource
	private UpdateInvGlAmountService updateInvGlAmountService;
	@Resource
	private ApArTransDao apArTransDao;
	@Resource
	ArPayBaseDao arPayBaseDao;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/*
	 * 
	  * <p>Title createDocsAndVoucher</p>
	  * <p>Description </p>
	  * 操作步骤：1、根据传过来的单据大类编码，判断需要对那个表进行操作；2、先保存单据主信息；3、保存单据行信息；4、进行生成凭证处理</p>
	  * @param mainTabInfoJson	单据主信息
	  * @param rowTabInfoTabJson单据行信息
	  * @param deleteDtlInfo	是否有单据行信息删除
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArDocsAndVoucherService#createDocsAndVoucher(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public JSONObject createDocsAndVoucher(String mainTabInfoJson, String rowTabInfoJson,String deleteDtlInfo,String language) throws Exception {
		JSONObject json = new JSONObject();	//返回值
		String arDocCode = "";
		String headId = "";
		try {
			String userId = CurrentUserUtil.getCurrentUserId();//当前登录人id
			Date createDate = CurrentUserUtil.getCurrentDate();//当前创建日期
			if(mainTabInfoJson == null || "".equals(mainTabInfoJson))
				throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_IS_NOT_NULL", null));//单据信息信息不能为空！
			//单据主信息
			ArDocumentHBean arDocumentH = new ArDocumentHBean();
			JSONObject mainTabInfo = new JSONObject(mainTabInfoJson);
			//对应收单进行保存处理
			String AR_INV_GL_H_ID = mainTabInfo.getString("AR_INV_GL_H_ID");
			//蓝字应收单
			String L_AR_INV_GL_H_ID = mainTabInfo.getString("L_AR_INV_GL_H_ID");
			String LEDGER_ID = mainTabInfo.getString("LEDGER_ID");
			String AR_DOC_CAT_CODE = mainTabInfo.getString("AR_DOC_CAT_CODE");
			String INV_AMOUNT = mainTabInfo.getString("INV_AMOUNT");
			String AR_INV_H_ID = mainTabInfo.getString("AR_INV_H_ID");

			//需要生成凭证的实体类
			ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
			avhb.setLedgerId(LEDGER_ID);
			avhb.setArCatCode("YSD");
			avhb.setAttchTotal(mainTabInfo.getInt("V_ATTCH_TOTAL"));
			avhb.setAccDate(sdf.parse(mainTabInfo.getString("GL_DATE")));
			avhb.setArId(AR_INV_GL_H_ID);
			
			//对单据赋值处理（几个单据共同的）
			arDocumentH.setAR_INV_GL_H_ID(AR_INV_GL_H_ID);
			arDocumentH.setAR_INV_H_ID(mainTabInfo.getString("AR_INV_H_ID"));
			arDocumentH.setL_AR_INV_GL_H_ID(L_AR_INV_GL_H_ID);
			arDocumentH.setLEDGER_ID(LEDGER_ID);
			arDocumentH.setAR_DOC_CAT_CODE(AR_DOC_CAT_CODE);
			arDocumentH.setGL_DATE(sdf.parse(mainTabInfo.getString("GL_DATE")));
			arDocumentH.setCUSTOMER_ID(mainTabInfo.getString("CUSTOMER_ID"));
			arDocumentH.setPROJECT_ID(mainTabInfo.getString("PROJECT_ID"));
			arDocumentH.setDEPT_ID(mainTabInfo.getString("DEPT_ID"));
			arDocumentH.setAR_CONTRACT_ID(mainTabInfo.getString("AR_CONTRACT_ID"));

			if(mainTabInfo.getString("AMOUNT") != null && !"".equals(mainTabInfo.getString("AMOUNT")))
				arDocumentH.setAMOUNT(Double.parseDouble(mainTabInfo.getString("AMOUNT")));
			if(mainTabInfo.getString("TAX_AMT") != null && !"".equals(mainTabInfo.getString("TAX_AMT")))
				arDocumentH.setTAX_AMT(Double.parseDouble(mainTabInfo.getString("TAX_AMT")));
			if(mainTabInfo.getString("TAX_RATE") != null && !"".equals(mainTabInfo.getString("TAX_RATE")))
				arDocumentH.setTAX_RATE(Double.parseDouble(mainTabInfo.getString("TAX_RATE")));
			if(INV_AMOUNT != null && !"".equals(INV_AMOUNT))
				arDocumentH.setINV_AMOUNT(Double.parseDouble(INV_AMOUNT));
			if(mainTabInfo.getString("CANCEL_AMT") != null && !"".equals(mainTabInfo.getString("CANCEL_AMT")))
				arDocumentH.setCANCEL_AMT(Double.parseDouble(mainTabInfo.getString("CANCEL_AMT")));
			if(mainTabInfo.getString("PAID_AMT") != null && !"".equals(mainTabInfo.getString("PAID_AMT")))
				arDocumentH.setPAID_AMT(Double.parseDouble(mainTabInfo.getString("PAID_AMT")));
			if(mainTabInfo.getString("ADJ_AMT") != null && !"".equals(mainTabInfo.getString("ADJ_AMT")))
				arDocumentH.setADJ_AMT(Double.parseDouble(mainTabInfo.getString("ADJ_AMT")));
			
			arDocumentH.setAR_ACC_ID_DEBT(mainTabInfo.getString("AR_ACC_ID_DEBT"));
			arDocumentH.setAR_ACC_ID_TAX(mainTabInfo.getString("AR_ACC_ID_TAX"));
			arDocumentH.setSOURCE(mainTabInfo.getString("SOURCE"));
			arDocumentH.setDESCRIPTION(mainTabInfo.getString("DESCRIPTION"));
			arDocumentH.setCREATION_DATE(createDate);
			arDocumentH.setCREATED_BY(userId);
			arDocumentH.setLAST_UPDATE_DATE(createDate);
			arDocumentH.setLAST_UPDATED_BY(userId);
			
			//单据行信息
			JSONArray rowInfoJson = new JSONArray(rowTabInfoJson);
			ArDocumentLBean arDocumentL = null;
			//单据  删除行
			JSONArray deleteDtlJson = new JSONArray(deleteDtlInfo);
			List<ArDocumentLBean> addDocument = new ArrayList<ArDocumentLBean>();
			List<ArDocumentLBean> updateDocument = new ArrayList<ArDocumentLBean>();
			List<String> deleteDocument = new ArrayList<String>();
			if(deleteDtlJson != null && deleteDtlJson.length() > 0){
				for(int i = 0;i < deleteDtlJson.length();i++){
					JSONObject delJson = deleteDtlJson.getJSONObject(i);
					String delDtlId = delJson.getString("AR_INV_GL_L_ID");
					deleteDocument.add(delDtlId);
				}
				arDocumentH.setDeleteDocument(deleteDocument);
			}
			if(rowInfoJson != null && rowInfoJson.length() > 0){
				for(int i = 0;i < rowInfoJson.length();i++){
					JSONObject lJson = rowInfoJson.getJSONObject(i);
					arDocumentL = new ArDocumentLBean();
					arDocumentL.setAR_INV_GL_H_ID(AR_INV_GL_H_ID);
					arDocumentL.setL_AR_INV_GL_L_ID(lJson.getString("L_AR_INV_GL_L_ID"));
					arDocumentL.setL_AR_INV_GL_H_ID(lJson.getString("L_AR_INV_GL_H_ID"));
					arDocumentL.setAR_SALE_TYPE_ID(lJson.getString("AR_SALE_TYPE_ID"));
					arDocumentL.setPRODUCT_ID(lJson.getString("PRODUCT_ID"));
					
					if(lJson.getString("PRICE") != null && !"".equals(lJson.getString("PRICE")))
						arDocumentL.setPRICE(Double.parseDouble(lJson.getString("PRICE")));
					if(lJson.getString("INV_PRICE") != null && !"".equals(lJson.getString("INV_PRICE")))
						arDocumentL.setINV_PRICE(Double.parseDouble(lJson.getString("INV_PRICE")));
					if(lJson.getString("AMOUNT") != null && !"".equals(lJson.getString("AMOUNT")))
						arDocumentL.setAMOUNT(Double.parseDouble(lJson.getString("AMOUNT")));
					if(lJson.getString("INV_AMOUNT") != null && !"".equals(lJson.getString("INV_AMOUNT")))
						arDocumentL.setINV_AMOUNT(Double.parseDouble(lJson.getString("INV_AMOUNT")));
					if(lJson.getString("TAX_AMT") != null && !"".equals(lJson.getString("TAX_AMT")))
						arDocumentL.setTAX_AMT(Double.parseDouble(lJson.getString("TAX_AMT")));
					if(lJson.getString("TAX") != null && !"".equals(lJson.getString("TAX")))
						arDocumentL.setTAX(Double.parseDouble(lJson.getString("TAX")));

					arDocumentL.setQTY(lJson.getInt("QTY"));
					arDocumentL.setACC_ID(lJson.getString("ACC_ID"));
					arDocumentL.setCCID(lJson.getString("CCID"));
					arDocumentL.setDIM_CODE(lJson.getString("DIM_CODE"));
					arDocumentL.setCCID(lJson.getString("CCID"));
					arDocumentL.setDESCRIPTION(lJson.getString("DESCRIPTION"));
					arDocumentL.setCREATION_DATE(createDate);
					arDocumentL.setCREATED_BY(userId);
					arDocumentL.setLAST_UPDATE_DATE(createDate);
					arDocumentL.setLAST_UPDATED_BY(userId);
					
					String AR_INV_GL_L_ID = lJson.getString("AR_INV_GL_L_ID");
					if(AR_INV_GL_L_ID != null && !"".equals(AR_INV_GL_L_ID)){//更新处理
						arDocumentL.setAR_INV_GL_L_ID(AR_INV_GL_L_ID);
						updateDocument.add(arDocumentL);
					}
					else{//新增处理
						arDocumentL.setAR_INV_GL_L_ID(java.util.UUID.randomUUID().toString());
						addDocument.add(arDocumentL);
					}
				}
				arDocumentH.setAddDocument(addDocument);
				arDocumentH.setUpdateDocument(updateDocument);
			}
			else{
				throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DTL_IS_NOT_NULL", null));//应收单明细不能为空，请添加应收单明细！
			}
			//通过应收单ID得到应收单信息
			ArDocumentHBean arInvGlHBean = xcarCommonDao.getArInvGlH(AR_INV_GL_H_ID);
			//交易明细表
			ArInvTransBean arInvTrans = new ArInvTransBean();
			//交易明细表赋值
			arInvTrans.setSOURCE_ID(AR_INV_GL_H_ID);
			
			if(L_AR_INV_GL_H_ID != null && !"".equals(L_AR_INV_GL_H_ID)){
				//通过红字应收单对应的蓝字应收单ID得到应收单信息
				ArDocumentHBean arInvGlHBeans = xcarCommonDao.getArInvGlH(L_AR_INV_GL_H_ID);
				arInvTrans.setAR_INV_GL_H_ID(L_AR_INV_GL_H_ID);
				arInvTrans.setAMOUNT(arInvGlHBeans.getNO_PAY_AMT());
			}
			else{
				arInvTrans.setAR_INV_GL_H_ID(AR_INV_GL_H_ID);
				arInvTrans.setAMOUNT(Double.parseDouble(INV_AMOUNT));
			}
			arInvTrans.setTRANS_ID(java.util.UUID.randomUUID().toString());
			arInvTrans.setSOURCE_DTL_ID(null);
			arInvTrans.setAR_PAY_H_ID(null);
			arInvTrans.setSOURCE_TAB("XC_AR_INV_GL_H");
			arInvTrans.setAR_DOC_CAT_CODE(AR_DOC_CAT_CODE);
			arInvTrans.setDESCRIPTION(mainTabInfo.getString("DESCRIPTION"));
			arInvTrans.setGL_DATE(sdf.parse(mainTabInfo.getString("GL_DATE")));
			arInvTrans.setAR_CONTRACT_ID(mainTabInfo.getString("AR_CONTRACT_ID"));
			arInvTrans.setCUSTOMER_ID(mainTabInfo.getString("CUSTOMER_ID"));
			arInvTrans.setDR_AMT(Double.parseDouble(INV_AMOUNT));
			arInvTrans.setCR_AMT(0);
			arInvTrans.setTRANS_STATUS("0");
			arInvTrans.setCCID(null);
			arInvTrans.setCREATED_BY(userId);
			arInvTrans.setCREATION_DATE(createDate);
			arInvTrans.setLAST_UPDATE_DATE(createDate);
			arInvTrans.setLAST_UPDATED_BY(userId);
			
			//根据数据库中是否存在当前操作的应收单，来判断执行数据保存或更新处理
			if(arInvGlHBean != null && !"".equals(arInvGlHBean)){
				if(XCARConstants.YSD_HZ.equals(AR_DOC_CAT_CODE) && L_AR_INV_GL_H_ID != null && !"".equals(L_AR_INV_GL_H_ID)){
					arDocumentH.setNO_PAY_AMT(0);
				}
				else{
					if(Double.parseDouble(INV_AMOUNT) != arInvGlHBean.getINV_AMOUNT())
						arDocumentH.setNO_PAY_AMT(Double.parseDouble(INV_AMOUNT));
					else
						arDocumentH.setNO_PAY_AMT(arInvGlHBean.getNO_PAY_AMT());
				}
				arDocsAndVoucherDao.updateArInvGl(arDocumentH);

				arDocCode = arInvGlHBean.getAR_INV_GL_H_CODE();
				headId = arInvGlHBean.getV_HEAD_ID();
				arInvTrans.setAR_DOC_CODE(arDocCode);
				//向交易明细表插入数据
				List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
				transList.add(arInvTrans);
				apArTransDao.updateArTrans(transList);
				
			}else{
				String year = sdf.format(createDate).substring(0,4);	//年
				String month = sdf.format(createDate).substring(5,7);	//月
				//得到单据编码
				arDocCode = ruleService.getNextSerialNum("AR_INV_GL", LEDGER_ID, "", year, month, userId);

				if(INV_AMOUNT != null && !"".equals(INV_AMOUNT)){
					if(XCARConstants.YSD_HZ.equals(AR_DOC_CAT_CODE) && L_AR_INV_GL_H_ID != null && !"".equals(L_AR_INV_GL_H_ID)){
						arDocumentH.setNO_PAY_AMT(0);
					}
					else{
						arDocumentH.setNO_PAY_AMT(Double.parseDouble(INV_AMOUNT));
					}
				}
				arDocumentH.setAR_INV_GL_H_CODE(arDocCode);;
				arDocsAndVoucherDao.saveArInvGl(arDocumentH);
				arInvTrans.setAR_DOC_CODE(arDocCode);
				//向交易明细表插入数据
				List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
				transList.add(arInvTrans);
				apArTransDao.insertArTrans(transList);
			}
			//应收单红字
			if(XCARConstants.YSD_HZ.equals(AR_DOC_CAT_CODE) && (L_AR_INV_GL_H_ID != null && !"".equals(L_AR_INV_GL_H_ID))){
				//红字发票需要判断应收单的未收金额是否还够冲销当前红色应收单。如果不够需要提示，如果够冲销，需要在冲销完后修改蓝色应收单的未收金额和未申请金额。
				String judReturn = xcarCommonDao.judgmentInvAmount(L_AR_INV_GL_H_ID,Double.parseDouble(INV_AMOUNT) - ((arInvGlHBean == null) ? 0 : arInvGlHBean.getINV_AMOUNT()));
				if(judReturn == null || "".equals(judReturn)){
					//xcarCommonDao.updateInvAmount(Double.parseDouble(INV_AMOUNT) - ((arInvGlHBean == null) ? 0 : arInvGlHBean.getINV_AMOUNT()),L_AR_INV_GL_H_ID,"+");
					List<String> arInvGlIdList = new ArrayList<String>();
					arInvGlIdList.add(L_AR_INV_GL_H_ID);
					updateInvGlAmountService.updateArInvGlAmount(arInvGlIdList);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_SAVE_AMOUNT_FAILED", null));//红冲金额超过未收金额，不能进行冲销！
				}
			}
			//调用生成凭证的方法
			String returnParams = this.newArVoucher(avhb,language);
			JSONObject jo = new JSONObject(returnParams);
			if("0".equals(jo.getString("flag"))){
				headId = jo.getString("headId");
				if(AR_INV_H_ID != null && !"".equals(AR_INV_H_ID)){
					//调用发票复核的方法
					ArInvoiceHBean arInvoiceH = new ArInvoiceHBean();
					arInvoiceH.setFIN_USER_ID(userId);
					arInvoiceH.setFIN_DATE(createDate);
					arInvoiceH.setSYS_AUDIT_STATUS("F");
					arInvoiceH.setSYS_AUDIT_STATUS_DESC("已复核");
					arInvoiceH.setLAST_UPDATED_BY(userId);
					arInvoiceH.setLAST_UPDATE_DATE(createDate);
					arInvoiceH.setAR_INV_H_ID(mainTabInfo.getString("AR_INV_H_ID"));
					List<ArInvoiceHBean> arInvoiceHList = new ArrayList<ArInvoiceHBean>();
					arInvoiceHList.add(arInvoiceH);
					arInvoiceBaseDao.finArInvoice(arInvoiceHList);
				}
			}
			else{
				throw new Exception(jo.getString("msg"));
			}
			json.put("arDocCode",arDocCode);
			json.put("headId",headId);
			json.put("flag","0");
			json.put("msg",XipUtil.getMessage(language, "XC_AR_INV_GL_SAVE_SUCCESS", null));//应收单保存成功！
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return json;
	}
	
	/*
	 * 
	  * <p>Title newArVoucher</p>
	  * <p>Description 应收模块生成凭证的方法</p>
	  * @param avhb
	  * @param language
	  * @return
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArDocsAndVoucherService#newArVoucher(com.xzsoft.xc.ar.modal.ArVoucherHandlerBean, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public String newArVoucher(ArVoucherHandlerBean avhb,String language) throws Exception {
		String returnParams = null;
		String tableName = null;	//表名
		String primaryKey = null;	//主键
		try {
			//账簿信息
			Ledger ledger = xcglCommonDAO.getLedger(avhb.getLedgerId());
			//凭证来源
			VSource vs = xcglCommonDAO.getVSource(XCARConstants.XC_AR_PAY);
			
			// 辅助参数
			JSONObject paramJo = new JSONObject();
			paramJo.put("accDate",sdf.format(avhb.getAccDate()));//凭证日期
			paramJo.put("sourceId",avhb.getArId());				//凭证来源ID
			paramJo.put("attchTotal",avhb.getAttchTotal());		//附件张数（只有应收单有，其他单据传的附件张数0）
			String paramJson = paramJo.toString();
			JSONArray headArray = new JSONArray();
			JSONObject headJson = new JSONObject();
			VHead head = new VHead();
			//获取参数信息 
			String arCatCode = avhb.getArCatCode();				//单据类型
			if(XCARConstants.YSD.equals(arCatCode)){			//应收单
				tableName = "xc_ar_inv_gl_h";					//应收单主表xc_ar_inv_gl_h
				primaryKey = "AR_INV_GL_H_ID";					//应收单主键ID AR_INV_GL_H_ID
				//应收单据主信息
				ArDocumentHBean arInvGlHBean = xcarCommonDao.getArInvGlH(avhb.getArId());
				String headId = arInvGlHBean.getV_HEAD_ID();//凭证头ID
				String vStatus = arInvGlHBean.getV_STATUS();//凭证状态
				if(vStatus == null || "".equals(vStatus) || "1".equals(vStatus)){
					if(headId != null && !"".equals(headId)){//凭证已经存在，但是草稿状态，则删除凭证
						headJson.put("V_HEAD_ID",headId);
						headArray.put(headJson);
					}
					ArVoucherGroupService receivableDocService = (ArVoucherGroupService)AppContext.getApplicationContext().getBean("receivableDocService");
					head = receivableDocService.mergeVoucher(arInvGlHBean,ledger,vs,paramJson,language);
				}
			}
			else if(XCARConstants.SKD.equals(arCatCode)){//收款单
				tableName = "xc_ar_pay_h";
				primaryKey = "AR_PAY_H_ID";
				//收款单据主信息
				ArDocumentHBean arPayH = xcarCommonDao.getArPayH(avhb.getArId());
				String headId = arPayH.getV_HEAD_ID();//凭证头ID
				String vStatus = arPayH.getV_STATUS();//凭证状态
				if(vStatus == null || "".equals(vStatus) || "1".equals(vStatus)){
					if(headId != null && !"".equals(headId)){//凭证已经存在，但是草稿状态，则删除凭证
						headJson.put("V_HEAD_ID",headId);
						headArray.put(headJson);
					}
					ArVoucherGroupService arReceiptDocService = (ArVoucherGroupService)AppContext.getApplicationContext().getBean("arReceiptDocServiceImpl");
					head = arReceiptDocService.mergeVoucher(arPayH,ledger,vs,paramJson,language);
				}
			}
			else if(XCARConstants.HXD.equals(arCatCode)){//核销单
				tableName = "xc_ar_cancel_h";
				primaryKey = "AR_CANCEL_H_ID";
				//核销单据主信息
				ArDocumentHBean arCancelH = xcarCommonDao.getArCancelH(avhb.getArId());
				String headId = arCancelH.getV_HEAD_ID();//凭证头ID
				String vStatus = arCancelH.getV_STATUS();//凭证状态
				if(vStatus == null || "".equals(vStatus) || "1".equals(vStatus)){
					if(headId != null && !"".equals(headId)){//凭证已经存在，但是草稿状态，则删除凭证
						headJson.put("V_HEAD_ID",headId);
						headArray.put(headJson);
					}
					ArVoucherGroupService arCancelDocService = (ArVoucherGroupService)AppContext.getApplicationContext().getBean("arCancelDocService");
					head = arCancelDocService.mergeVoucher(arCancelH,ledger,vs,paramJson,language);
				}
			}
			else if(XCARConstants.YUETZD.equals(arCatCode)){//余额调整单
				tableName = "xc_ar_inv_gl_adj";
				primaryKey = "GL_ADJ_ID";
				//余额调整单据信息
				ArDocumentHBean arInvAdj = xcarCommonDao.getArInvGlAdj(avhb.getArId());
				String headId = arInvAdj.getV_HEAD_ID();//凭证头ID
				String vStatus = arInvAdj.getV_STATUS();//凭证状态
				if(vStatus == null || "".equals(vStatus) || "1".equals(vStatus)){
					if(headId != null && !"".equals(headId)){//凭证已经存在，但是草稿状态，则删除凭证
						headJson.put("V_HEAD_ID",headId);
						headArray.put(headJson);
					}
					ArVoucherGroupService arBalAdjDocService = (ArVoucherGroupService)AppContext.getApplicationContext().getBean("arBalAdjDocService");
					head = arBalAdjDocService.mergeVoucher(arInvAdj,ledger,vs,paramJson,language);
				}
			}
			if(headArray.length() > 0 && headArray != null)
				//保存凭证之前先删除之前的凭证
				voucherHandlerService.doDelVouchers(headArray.toString());
			// 将凭证信息转换成JSON串
			String jsonObject = voucherHandlerCommonService.headBean2JsonStr(head,null);
			
			// 保存凭证信息
			returnParams = voucherHandlerService.doSaveVoucher(jsonObject);
			JSONObject returnJo = new JSONObject(returnParams);
			//得到参数
			avhb.setTableName(tableName);
			avhb.setHeadId(returnJo.getString("headId"));
			avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
			avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
			avhb.setPriKey(primaryKey);
			avhb.setArId(avhb.getArId());
			//更新单据中的凭证信息
			arDocsAndVoucherDao.saveNewArVoucher(avhb);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
		return returnParams;
	}
	/*
	 * 
	  * <p>Title checkArVoucher</p>
	  * <p>Description 凭证审核处理</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArDocsAndVoucherService#checkArVoucher(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void checkArVoucher(JSONArray ja,String language) throws Exception {
		String tableName = null;										//表名
		String primaryKey = null;										//主键
		JSONArray headArray = new JSONArray();
		List<ArVoucherHandlerBean> avhbList = new ArrayList<ArVoucherHandlerBean>();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String arId = jo.getString("arId");					//单据ID
					String arCatCode = jo.getString("arCatCode");		//单据类型

					ArDocumentHBean arDocumentH = null;
					if(XCARConstants.YSD.equals(arCatCode)){			//应收单
						tableName = "xc_ar_inv_gl_h";					//应收单主表xc_ar_inv_gl_h
						primaryKey = "AR_INV_GL_H_ID";					//应收单主键ID AR_INV_GL_H_ID
						arDocumentH = xcarCommonDao.getArInvGlH(arId);
					}
					else if(XCARConstants.SKD.equals(arCatCode)){		//收款单
						tableName = "xc_ar_pay_h";
						primaryKey = "AR_PAY_H_ID";
						arDocumentH = xcarCommonDao.getArPayH(arId);
					}
					else if(XCARConstants.HXD.equals(arCatCode)){		//核销单
						tableName = "xc_ar_cancel_h";
						primaryKey = "AR_CANCEL_H_ID";
						arDocumentH = xcarCommonDao.getArCancelH(arId);
					}
					else if(XCARConstants.TZD.equals(arCatCode)){		//余额调整单
						tableName = "xc_ar_inv_gl_adj";
						primaryKey = "GL_ADJ_ID";
						arDocumentH = xcarCommonDao.getArInvGlAdj(arId);
					}
					String headId = arDocumentH.getV_HEAD_ID();
					if("3".equals(arDocumentH.getV_STATUS())){			//单据已经审核通过
						throw new Exception(XipUtil.getMessage(language, "XC_VOUCHER_ALREADY_CHECK", null));//单据已经审核通过！
					}
					JSONObject headJson = new JSONObject();
					headJson.put("V_HEAD_ID",headId);
					headArray.put(headJson);

					ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
					//得到参数
					avhb.setTableName(tableName);
					avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					avhb.setVerifierId(CurrentUserUtil.getCurrentUserId());
					avhb.setVerifyDate(CurrentUserUtil.getCurrentDate());
					avhb.setvStatus("3");
					avhb.setPriKey(primaryKey);
					avhb.setArId(arId);
					avhbList.add(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			//执行凭证提交
			voucherHandlerService.doSubmitVouchers(headArray.toString());
			//凭证提交成功之后再提交审核
			voucherHandlerService.doCheckVouchers(headArray.toString());
			//凭证审核通过之后，执行单据中的凭证状态信息更新
			arDocsAndVoucherDao.saveCheckArVoucher(avhbList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title cancelCheckArVoucher</p>
	  * <p>Description 取消凭证审核</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArDocsAndVoucherService#cancelCheckArVoucher(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void cancelCheckArVoucher(JSONArray ja,String language) throws Exception {
		String tableName = null;										//表名
		String primaryKey = null;										//主键
		JSONArray headArray = new JSONArray();
		List<ArVoucherHandlerBean> avhbList = new ArrayList<ArVoucherHandlerBean>();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String arId = jo.getString("arId");					//单据ID
					String arCatCode = jo.getString("arCatCode");		//单据类型

					ArDocumentHBean arDocumentH = null;
					if(XCARConstants.YSD.equals(arCatCode)){			//应收单
						//3、根据应收单ID，判断当前应收单对应的发票是否做过红冲
						int glInvHcNextCount = arCheckUtilDao.checkGlCreateInvHcByInvId(arId);
						if(glInvHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_01", null));//该应收单对应的销售发票已做红冲，不能取消审核！
								
						//4、根据应收单ID，判断当前应收单是否做过红冲
						int glHcNextCount = arCheckUtilDao.checkGlCreateGlHcByInvId(arId);
						if(glHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_02", null));//该应收单已做红冲，不能取消审核！
								
						//5、根据应收单ID，判断当前应收单是否做过收款
						int payNextCount = arCheckUtilDao.checkGlCreatePayByInvId(arId);
						if(payNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_03", null));//该应收单已做收款，不能取消审核！
						
						//6、根据应收单ID，判断当前应收单是否被别的应收单核销过
						int cancelHNextCount = arCheckUtilDao.checkGlCreateCancelHByInvId(arId);
						if(cancelHNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_04", null));//该应收单已做核销，不能取消审核！
						
						//7、根据应收单ID，判断当前应收单是核销过别的应收单
						int cancelLNextCount = arCheckUtilDao.checkGlCreateCancelLByInvId(arId);
						if(cancelLNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_05", null));//该应收单已做核销，不能取消审核！
						
						//8、根据应收单ID，判断当前应收单是否做过调整单
						int adjNextCount = arCheckUtilDao.checkGlCreateAdjByInvId(arId);
						if(adjNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_06", null));//该应收单已做调整，不能取消审核！

						//9、根据应收单ID，判断当前应收单是做过应付核应收
						int arApCancelNextCount = arCheckUtilDao.checkApArCancelByInvId(arId);
						if(arApCancelNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_11", null));//该应收单已做过应付核应收，不能取消审核！
						tableName = "xc_ar_inv_gl_h";					//应收单主表xc_ar_inv_gl_h
						primaryKey = "AR_INV_GL_H_ID";					//应收单主键ID AR_INV_GL_H_ID
						arDocumentH = xcarCommonDao.getArInvGlH(arId);
					}
					else if(XCARConstants.SKD.equals(arCatCode)){		//收款单
						//1、根据收款单ID，判断当前收款单是否做过红冲
						int payHcNextCount = arCheckUtilDao.checkPayCreateHcByPayId(arId);
						if(payHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_07", null));//该收款单已做红冲，不能取消审核！
						
						//2、根据收款单ID，判断当前收款单是否做过核销
						int payCancelNextCount = arCheckUtilDao.checkPayCreateCancelByPayId(arId);
						if(payCancelNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_08", null));//该收款单已做核销，不能取消审核！
						
						tableName = "xc_ar_pay_h";
						primaryKey = "AR_PAY_H_ID";
						arDocumentH = xcarCommonDao.getArPayH(arId);
					}
					else if(XCARConstants.HXD.equals(arCatCode)){		//核销单
						tableName = "xc_ar_cancel_h";
						primaryKey = "AR_CANCEL_H_ID";
						arDocumentH = xcarCommonDao.getArCancelH(arId);
					}
					else if(XCARConstants.TZD.equals(arCatCode)){		//余额调整单
						tableName = "xc_ar_inv_gl_adj";
						primaryKey = "GL_ADJ_ID";
						arDocumentH = xcarCommonDao.getArInvGlAdj(arId);
					}
					String headId = arDocumentH.getV_HEAD_ID();
					//通过凭证头id查询凭证信息
					VHead vHead = voucherHandlerService.getVHead(headId);
					if(!"".equals(vHead.getSignatoryId()) && vHead.getSignatoryId() != null){//单据已经签字
						throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_09", null));//所选单据已经签字，不能取消审核！
					}
					if(!"".equals(vHead.getBookkeeperId()) && vHead.getBookkeeperId() != null){//单据已经记账
						throw new Exception(XipUtil.getMessage(language, "XC_AR_VOUCEHR_CANCEL_CHECK_10", null));//所选单据已经记账，不能取消审核！
					}
					JSONObject headJson = new JSONObject();
					headJson.put("V_HEAD_ID",headId);
					headArray.put(headJson);

					ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
					//得到参数
					avhb.setTableName(tableName);
					avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					avhb.setVerifierId(null);
					avhb.setVerifyDate(null);
					avhb.setvStatus("1");
					avhb.setPriKey(primaryKey);
					avhb.setArId(arId);
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
			arDocsAndVoucherDao.saveCheckArVoucher(avhbList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title signArVoucher</p>
	  * <p>Description 签字</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArDocsAndVoucherService#signArVoucher(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void signArVoucher(JSONArray ja,String language) throws Exception {
		JSONArray headArray = new JSONArray();
		List<ArVoucherHandlerBean> avhbList = new ArrayList<ArVoucherHandlerBean>();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String arId = jo.getString("arId");					//单据ID
					ArDocumentHBean arDocumentH = xcarCommonDao.getArPayH(arId);
					String headId = arDocumentH.getV_HEAD_ID();
					JSONObject headJson = new JSONObject();
					headJson.put("V_HEAD_ID",headId);
					headArray.put(headJson);

					ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
					//得到参数
					avhb.setSignUserId(CurrentUserUtil.getCurrentUserId());
					avhb.setSignDate(CurrentUserUtil.getCurrentDate());
					avhb.setvStatus("3");
					avhb.setSignatory(CurrentSessionVar.getEmpName());
					avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					avhb.setArId(arId);
					avhbList.add(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			//执行凭证签字
			voucherHandlerService.doSignVouchers(headArray.toString());
			//签字成功之后，保存签字后的单据信息
			arDocsAndVoucherDao.saveSignArVoucher(avhbList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	/*
	 * 
	  * <p>Title cancelSignArVoucher</p>
	  * <p>Description 取消签字</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArDocsAndVoucherService#cancelSignArVoucher(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void cancelSignArVoucher(JSONArray ja,String language) throws Exception {
		JSONArray headArray = new JSONArray();
		List<ArVoucherHandlerBean> avhbList = new ArrayList<ArVoucherHandlerBean>();
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String arId = jo.getString("arId");					//单据ID
					ArDocumentHBean arDocumentH = xcarCommonDao.getArPayH(arId);
					String headId = arDocumentH.getV_HEAD_ID();
					//通过凭证头id查询凭证信息
					VHead vHead = voucherHandlerService.getVHead(headId);
					if(!"".equals(vHead.getBookkeeperId()) && vHead.getBookkeeperId() != null){//单据已经记账
						throw new Exception(XipUtil.getMessage(language, "XC_AP_VOUCEHR_CANCEL_SIGN_01", null));//所选单据已经记账，不能取消签字！
					}
					JSONObject headJson = new JSONObject();
					headJson.put("V_HEAD_ID",headId);
					headArray.put(headJson);

					ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
					//得到参数
					avhb.setSignUserId(null);
					avhb.setSignDate(null);
					avhb.setvStatus("3");
					avhb.setSignatory(null);
					avhb.setLastUpdateDate(CurrentUserUtil.getCurrentDate());
					avhb.setLastUpdatedBy(CurrentUserUtil.getCurrentUserId());
					avhb.setArId(arId);
					avhbList.add(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			//执行取消凭证签字
			voucherHandlerService.doUnsignVouchers(headArray.toString());
			//取消签字成功之后，保存签字后的单据信息
			arDocsAndVoucherDao.saveSignArVoucher(avhbList);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	/*
	 * 
	  * <p>Title cancelFinArInvGl</p>
	  * <p>Description 对应收单进行取消复核处理</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArDocsAndVoucherService#cancelFinArInvGl(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void cancelFinArInvGl(JSONArray ja,String language) throws Exception {
		try {
			//存储应收单ID信息
			List<String> lists = new ArrayList<String>();
			//存储凭证头ID信息
			JSONArray headArray = new JSONArray();
			List<ArInvoiceHBean> arInvoiceHList = new ArrayList<ArInvoiceHBean>();
			List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
			List<String> arInvGlIdList = new ArrayList<String>();
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String AR_INV_H_ID = jo.getString("AR_INV_H_ID");//发票ID
					String AR_INV_GL_H_ID = jo.getString("AR_INV_GL_H_ID");//应收单ID
					
					//根据应收单ID查询应收单信息
					ArDocumentHBean arInvGlHBean = xcarCommonDao.getArInvGlH(AR_INV_GL_H_ID);
					
					//判断发票是否被引用
					//6、根据应收单ID，查询该发票生成的应收单凭证状态
					int vStatusNextCount = arCheckUtilDao.checkGlVStatus(AR_INV_GL_H_ID);
					if(vStatusNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_CANCEL_FIN_01", null));//该销售发票生成的应收单已经审核通过，请先取消审核！
					
					//1、根据发票ID，查询该发票是否做过红冲
					int glInvHcNextCount = arCheckUtilDao.checkInvGlCreateInvHcByInvId(AR_INV_H_ID);
					if(glInvHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_CANCEL_FIN_02", null));//该销售发票已做过红冲，不能取消复核！
					
					//2、根据应收单ID，判断当前应收单是否做过收款
					int payNextCount = arCheckUtilDao.checkGlCreatePayByInvId(AR_INV_GL_H_ID);
					if(payNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_CANCEL_FIN_03", null));//该销售发票生成的应收单已做过收款，不能取消复核！
			
					//3、根据应收单ID，查询该应收单是否做过核销单主信息
					int cancelHNextCount = arCheckUtilDao.checkGlCreateCancelHByInvId(AR_INV_GL_H_ID);
					if(cancelHNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_CANCEL_FIN_04", null));//该销售发票生成的应收单已做过核销单主信息，不能取消复核！
					
					//4、根据应收单ID，判断当前应收单是核销过别的应收单
					int cancelLNextCount = arCheckUtilDao.checkGlCreateCancelLByInvId(AR_INV_GL_H_ID);
					if(cancelLNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_CANCEL_FIN_05", null));//该销售发票生成的应收单已做过核销单行信息，不能取消复核！
					
					//5、根据应收单ID，判断当前应收单是否做过红冲
					int glHcCount = arCheckUtilDao.checkGlCreateGlHcByInvId(AR_INV_GL_H_ID);
					if(glHcCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_CANCEL_FIN_06", null));//该销售发票生成的应收单已做过红冲，不能取消复核！
					
					//9、根据应收单ID，判断当前应收单是做过应付核应收
					int arApCancelNextCount = arCheckUtilDao.checkApArCancelByInvId(AR_INV_GL_H_ID);
					if(arApCancelNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_CANCEL_FIN_07", null));//该应收单已做过应付核应收，不能取消复核！
					
					//先更新发票的复核状态，在删除应收单信息
					//调用发票取消复核的方法
					ArInvoiceHBean arInvoiceH = new ArInvoiceHBean();
					arInvoiceH.setFIN_USER_ID(null);
					arInvoiceH.setFIN_DATE(null);
					arInvoiceH.setSYS_AUDIT_STATUS("E");
					arInvoiceH.setSYS_AUDIT_STATUS_DESC("审批通过");
					arInvoiceH.setLAST_UPDATED_BY(CurrentUserUtil.getCurrentUserId());
					arInvoiceH.setLAST_UPDATE_DATE(CurrentUserUtil.getCurrentDate());
					arInvoiceH.setAR_INV_H_ID(AR_INV_H_ID);
					arInvoiceHList.add(arInvoiceH);
					
					//删除交易明细表
					ArInvTransBean arInvTrans = new ArInvTransBean();
					arInvTrans.setSOURCE_ID(AR_INV_GL_H_ID);
					arInvTrans.setSOURCE_DTL_ID(null);
					arInvTrans.setAR_DOC_CAT_CODE(arInvGlHBean.getAR_DOC_CAT_CODE());
					transList.add(arInvTrans);
					if(arInvGlHBean.getL_AR_INV_GL_H_ID() != null && !"".equals(arInvGlHBean.getL_AR_INV_GL_H_ID()))
						arInvGlIdList.add(arInvGlHBean.getL_AR_INV_GL_H_ID());
					
					lists.add(AR_INV_GL_H_ID);
					
					JSONObject headJson = new JSONObject();
					headJson.put("V_HEAD_ID",arInvGlHBean.getV_HEAD_ID());
					headArray.put(headJson);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			if(transList.size() > 0 && transList != null)
				//删除交易明细
				apArTransDao.deleteArTrans(transList);
			if(arInvoiceHList.size() > 0 && arInvoiceHList != null)
				//取消复核处理
				arInvoiceBaseDao.finArInvoice(arInvoiceHList);
			if(arInvGlIdList.size() > 0 && arInvGlIdList != null)
				//更新应收单金额
				updateInvGlAmountService.updateArInvGlAmount(arInvGlIdList);
			if(lists.size() > 0 && lists != null)
				//删除应收单主表和行表
				arDocsAndVoucherDao.deleteArInvGlH(lists);
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
	  * <p>Title deleteArDoc</p>
	  * <p>Description 删除单据（包括验证是否有凭证（删除凭证），是否存在明细（存在明细不能删除），凭证审核是否通过（审核通过也不能删除），交易明细表也要删除）</p>
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @see com.xzsoft.xc.ar.service.ArDocsAndVoucherService#deleteArDoc(org.codehaus.jettison.json.JSONArray, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteArDoc(JSONArray ja,String language) throws Exception {
		ArVoucherHandlerBean avhb = new ArVoucherHandlerBean();
		ArVoucherHandlerBean avlb = new ArVoucherHandlerBean();
		JSONArray headArray = new JSONArray();
		String tableName = null;										//表名
		String lTableName = null;		                                //行表名
		String primaryKey = null;										//主键
		List<String> arInvGlIdList = new ArrayList<String>();
		List<String> apInvGlIdList = new ArrayList<String>();
		List<String> payIdList = new ArrayList<String>();
		//删除应收交易明细表
		List<ArInvTransBean> transList = new ArrayList<ArInvTransBean>();
		//删除应付交易明细表
		List<ApInvTransBean> apTransList = new ArrayList<ApInvTransBean>();
		//需要对应收单金额更新操作的应收单ID
		ArDocumentHBean arDocumentH = null;
		try {
			for(int i = 0;i < ja.length();i++){							//遍历传过来的数组
				Object obj = ja.get(i);
				if(obj != null){
					JSONObject jo = new JSONObject(String.valueOf(obj));
					String arCatCode = jo.getString("arCatCode");		//单据类型
					String arId = jo.getString("arId");					//单据ID
					
					if(XCARConstants.YSD.equals(arCatCode)){			//应收单
						//1、根据应收单ID，判断当前应收单对应的发票是否做过红冲
						int glInvHcNextCount = arCheckUtilDao.checkGlCreateInvHcByInvId(arId);
						if(glInvHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_01", null));//该应收单对应的销售发票已做红冲，不能删除！
								
						//2、根据应收单ID，判断当前应收单是否做过红冲
						int glHcNextCount = arCheckUtilDao.checkGlCreateGlHcByInvId(arId);
						if(glHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_02", null));//该应收单已做红冲，不能删除！
								
						//3、根据应收单ID，判断当前应收单是否做过收款
						int payNextCount = arCheckUtilDao.checkGlCreatePayByInvId(arId);
						if(payNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_03", null));//该应收单已做收款，不能删除！
						
						//4、根据应收单ID，判断当前应收单是否被别的应收单核销过
						int cancelHNextCount = arCheckUtilDao.checkGlCreateCancelHByInvId(arId);
						if(cancelHNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_04", null));//该应收单已做核销，不能删除！
						
						//5、根据应收单ID，判断当前应收单是核销过别的应收单
						int cancelLNextCount = arCheckUtilDao.checkGlCreateCancelLByInvId(arId);
						if(cancelLNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_04", null));//该应收单已做核销，不能删除！
						
						//6、根据应收单ID，判断当前应收单是否做过调整单
						int adjNextCount = arCheckUtilDao.checkGlCreateAdjByInvId(arId);
						if(adjNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_05", null));//该应收单已做调整，不能删除！

						//9、根据应收单ID，判断当前应收单是做过应付核应收
						int arApCancelNextCount = arCheckUtilDao.checkApArCancelByInvId(arId);
						if(arApCancelNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_08", null));//该应收单已做过应付核应收，不能删除！
						tableName = "xc_ar_inv_gl_h";					//应收单主表xc_ar_inv_gl_h
						primaryKey = "AR_INV_GL_H_ID";					//应收单主键IDAR_INV_GL_H_ID
						//应收单据主信息
						arDocumentH = xcarCommonDao.getArInvGlH(arId);
						lTableName = "xc_ar_inv_gl_l";
						if(arDocumentH.getL_AR_INV_GL_H_ID() != null && !"".equals(arDocumentH.getL_AR_INV_GL_H_ID()))
							arInvGlIdList.add(arDocumentH.getL_AR_INV_GL_H_ID());

						//交易明细实体类
						ArInvTransBean arInvTrans = new ArInvTransBean();
						arInvTrans.setSOURCE_ID(arId);
						arInvTrans.setSOURCE_DTL_ID(null);
						arInvTrans.setAR_DOC_CAT_CODE(arDocumentH.getAR_DOC_CAT_CODE());
						transList.add(arInvTrans);
					}
					else if(XCARConstants.SKD.equals(arCatCode)){		//收款单
						//1、根据收款单ID，判断当前收款单是否做过红冲
						int payHcNextCount = arCheckUtilDao.checkPayCreateHcByPayId(arId);
						if(payHcNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_06", null));//该收款单已做红冲，不能删除！
						
						//2、根据收款单ID，判断当前收款单是否做过核销
						int payCancelNextCount = arCheckUtilDao.checkPayCreateCancelByPayId(arId);
						if(payCancelNextCount > 0) throw new Exception(XipUtil.getMessage(language, "XC_AR_INV_GL_DELETE_07", null));//该收款单已做核销，不能删除！
						tableName = "xc_ar_pay_h";
						primaryKey = "AR_PAY_H_ID";
						//收款单主信息
						arDocumentH = xcarCommonDao.getArPayH(arId);
						lTableName = "xc_ar_pay_l";
						//通过收款单ID查询收款单行表信息
						List<ArDocumentLBean> arDocumentLList = xcarCommonDao.getArPayL(arId);
						for(ArDocumentLBean arDocumentL : arDocumentLList){
							arInvGlIdList.add(arDocumentL.getAR_INV_GL_H_ID());
							payIdList.add(arDocumentL.getL_AR_PAY_H_ID());
							//交易明细实体类
							ArInvTransBean arInvTrans = new ArInvTransBean();
							arInvTrans.setSOURCE_ID(arId);
							arInvTrans.setSOURCE_DTL_ID(arDocumentL.getAR_PAY_L_ID());
							arInvTrans.setAR_DOC_CAT_CODE(arDocumentH.getAR_DOC_CAT_CODE());
							transList.add(arInvTrans);
						}
					}
					else if(XCARConstants.HXD.equals(arCatCode)){		//核销单
						tableName = "xc_ar_cancel_h";
						primaryKey = "AR_CANCEL_H_ID";
						//核销单主信息
						arDocumentH = xcarCommonDao.getArCancelH(arId);
						lTableName = "xc_ar_cancel_l";
						//核销单主表交易明细（没有来源明细ID）
						ArInvTransBean arInvTranH = new ArInvTransBean();
						arInvTranH.setSOURCE_ID(arId);
						arInvTranH.setSOURCE_DTL_ID(null);
						arInvTranH.setAR_DOC_CAT_CODE(arDocumentH.getAR_CANCEL_TYPE());
						transList.add(arInvTranH);
						
						if(XCARConstants.YSHYUS.equals(arDocumentH.getAR_CANCEL_TYPE())){//应收核预收
							arInvGlIdList.add(arDocumentH.getSRC_ID());
							//更新预收单金额
							List<ArDocumentLBean> arDocumentLList = xcarCommonDao.getArCancelL(arId);
							for(ArDocumentLBean arDocumentL : arDocumentLList){
								payIdList.add(arDocumentL.getTARGET_ID());
								//核销单行表交易明细（有来源明细ID）
								ArInvTransBean arInvTrans = new ArInvTransBean();
								arInvTrans.setSOURCE_ID(arId);
								arInvTrans.setSOURCE_DTL_ID(arDocumentL.getAR_CANCEL_L_ID());
								arInvTrans.setAR_DOC_CAT_CODE(arDocumentH.getAR_CANCEL_TYPE());
								transList.add(arInvTrans);
							}
						}
						if(XCARConstants.YSHLDC.equals(arDocumentH.getAR_CANCEL_TYPE())){//应收红蓝对冲
							arInvGlIdList.add(arDocumentH.getSRC_ID());
							//通过核销单ID查询核销单行表信息
							List<ArDocumentLBean> arDocumentLList = xcarCommonDao.getArCancelL(arId);
							for(ArDocumentLBean arDocumentL : arDocumentLList){
								arInvGlIdList.add(arDocumentL.getTARGET_ID());
								//交易明细实体类
								ArInvTransBean arInvTrans = new ArInvTransBean();
								arInvTrans.setSOURCE_ID(arId);
								arInvTrans.setSOURCE_DTL_ID(arDocumentL.getAR_CANCEL_L_ID());
								arInvTrans.setAR_DOC_CAT_CODE(arDocumentH.getAR_CANCEL_TYPE());
								transList.add(arInvTrans);
							}
						}
						if(XCARConstants.YUSHLDC.equals(arDocumentH.getAR_CANCEL_TYPE())){//预收红蓝对冲
							payIdList.add(arDocumentH.getSRC_ID());
							//更新预收单金额
							List<ArDocumentLBean> arDocumentLList = xcarCommonDao.getArCancelL(arId);
							for(ArDocumentLBean arDocumentL : arDocumentLList){
								payIdList.add(arDocumentL.getTARGET_ID());
								//交易明细实体类
								ArInvTransBean arInvTrans = new ArInvTransBean();
								arInvTrans.setSOURCE_ID(arId);
								arInvTrans.setSOURCE_DTL_ID(arDocumentL.getAR_CANCEL_L_ID());
								arInvTrans.setAR_DOC_CAT_CODE(arDocumentH.getAR_CANCEL_TYPE());
								transList.add(arInvTrans);
							}
						}
						if(XCARConstants.YSHYF.equals(arDocumentH.getAR_CANCEL_TYPE())){//应收核应付
							arInvGlIdList.add(arDocumentH.getSRC_ID());
							//通过核销单ID查询核销单行表信息
							List<ArDocumentLBean> arDocumentLList = xcarCommonDao.getArCancelL(arId);
							for(ArDocumentLBean arDocumentL : arDocumentLList){
								apInvGlIdList.add(arDocumentL.getTARGET_ID());
								//交易明细实体类
								ArInvTransBean arInvTrans = new ArInvTransBean();
								arInvTrans.setSOURCE_ID(arId);
								arInvTrans.setSOURCE_DTL_ID(arDocumentL.getAR_CANCEL_L_ID());
								arInvTrans.setAR_DOC_CAT_CODE(arDocumentH.getAR_CANCEL_TYPE());
								transList.add(arInvTrans);
								
								ApInvTransBean apInvTrans = new ApInvTransBean();
								apInvTrans.setSOURCE_ID(arId);
								apInvTrans.setSOURCE_DTL_ID(arDocumentL.getAR_CANCEL_L_ID());
								apInvTrans.setAP_DOC_CAT_CODE(arDocumentH.getAR_CANCEL_TYPE());
								apTransList.add(apInvTrans);
							}
						
						}
					}
					else if(XCARConstants.TZD.equals(arCatCode)){		//余额调整单
						tableName = "xc_ar_inv_gl_adj";
						primaryKey = "GL_ADJ_ID";
						//余额调整单信息
						arDocumentH = xcarCommonDao.getArInvGlAdj(arId);
						arInvGlIdList.add(arDocumentH.getAR_INV_GL_H_ID());

						//交易明细实体类
						ArInvTransBean arInvTrans = new ArInvTransBean();
						arInvTrans.setSOURCE_ID(arId);
						arInvTrans.setSOURCE_DTL_ID(null);
						arInvTrans.setAR_DOC_CAT_CODE("YUETZD");
						transList.add(arInvTrans);
					}
					if(arDocumentH.getVERIFIER_ID() != null && !"".equals(arDocumentH.getVERIFIER_ID())){//单据已审核，审核人不为空
						throw new Exception(XipUtil.getMessage(language, "XC_AP_INV_GL_DELETE_09", null));//单据已经审核，不能删除！
					}
					else{
						if(arDocumentH.getV_HEAD_ID() != null && !"".equals(arDocumentH.getV_HEAD_ID())){//凭证已经存在，则删除凭证
							JSONObject headJson = new JSONObject();
							headJson.put("V_HEAD_ID",arDocumentH.getV_HEAD_ID());
							headArray.put(headJson);
						}
					}
					//得到参数
					avhb.setTableName(tableName);
					avhb.setPriKey(primaryKey);
					avhb.setArId(arId);
					avlb.setTableName(lTableName);
					avlb.setPriKey(primaryKey);
					avlb.setArId(arId);
					
					if(!XCARConstants.TZD.equals(arCatCode)){
						//删除业务明细表
						arDocsAndVoucherDao.deleteArDoc(avlb);
					}
					//删除单据表信息
					arDocsAndVoucherDao.deleteArDoc(avhb);
				}
				else{
					throw new Exception(XipUtil.getMessage(language, "XC_AP_PARAMETER_FORMAT_FAILED", null));//参数格式不正确！
				}
			}
			//删除应收交易明细
			if(transList != null && transList.size() > 0)
				apArTransDao.deleteArTrans(transList);
			//删除应付交易明细
			if(apTransList!= null && apTransList.size() > 0){
				apArTransDao.deleteApTrans(apTransList);
			}
			//更新应收单余额
			if(arInvGlIdList != null && arInvGlIdList.size() > 0)
				updateInvGlAmountService.updateArInvGlAmount(arInvGlIdList);
			//更新应付单余额
			if(apInvGlIdList != null && apInvGlIdList.size() > 0)
				updateInvGlAmountService.updateApInvGlAmount(apInvGlIdList);
			//更新收款单余额
			if(payIdList != null && payIdList.size() > 0)
				arPayBaseDao.updateArPayAmt(payIdList);
			//删除凭证数据
			if(headArray.length() > 0 && headArray != null)
				voucherHandlerService.doDelVouchers(headArray.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
}