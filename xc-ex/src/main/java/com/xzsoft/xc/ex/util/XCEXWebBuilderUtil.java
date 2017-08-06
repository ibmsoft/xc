package com.xzsoft.xc.ex.util;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ex.dao.XCEXCommonDAO;
import com.xzsoft.xc.ex.modal.ACCDocBean;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.PayDocBean;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xip.framework.util.AppContext;

/**
 * @ClassName: XCEXWebBuilderUtil 
 * @Description: 查询费用报销单信息
 * @author linp
 * @date 2016年4月3日 下午11:17:41 
 *
 */
public class XCEXWebBuilderUtil {
	// 日志记录器
	private static final Logger log = Logger.getLogger(XCEXWebBuilderUtil.class.getName()) ;

	/**
	 * @Title: getPayDocBeanById 
	 * @Description: 查询付款基础信息
	 * @param payId
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String getPayDocBeanById(String payId) throws Exception {
		String json = "{}" ;
		try {
			JSONObject jo = new JSONObject() ;
			
			// 执行查询处理
			XCEXCommonDAO xcexCommonDAO = (XCEXCommonDAO) AppContext.getApplicationContext().getBean("xcexCommonDAO") ;
			
			PayDocBean bean = xcexCommonDAO.getPayDoc(payId, false) ;
			
			// 将JavaBean转换成JSON串
			jo.put("EX_PAY_ID", bean.getExPayId()) ;
			jo.put("EX_PAY_CODE", bean.getExPayCode()) ;
			jo.put("CREATED_BY", bean.getCreatedBy()) ;
			jo.put("EMP_NAME", bean.getFinUserName()) ;
			jo.put("EX_PAY_DATE", XCDateUtil.date2StrBy24(bean.getExPayDate())) ;
			jo.put("LEDGER_ID", bean.getLedgerId()) ;
			jo.put("PAY_TYPE", bean.getPayType()) ;
			jo.put("DEPOSIT_BANK_ID", bean.getDepositBankId()) ;
			jo.put("V_STATUS", bean.getvStatus()) ;
			jo.put("PAY_STATUS", bean.getPayStatus()) ;
			jo.put("EX_PAY_DESC", bean.getExPayDesc()) ;
			jo.put("EX_PAY_T_AMT", bean.getExPayTAmt()) ;
			jo.put("CA_ID", bean.getCaId()) ;
			
			json = jo.toString() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			json = "{}" ;
		}
		
		
		return json ;
	}
	
	/**
	 * @Title: getAccDocDtlsById
	 * @Description: 查询费用报销单基础信息
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	public static String getAccDocDtlsById(String docId)throws Exception{
		
		String json = "{}" ;
		try {
			JSONObject jo = new JSONObject() ;
			
			// 执行查询处理
			XCEXCommonDAO xcexCommonDAO = (XCEXCommonDAO) AppContext.getApplicationContext().getBean("xcexCommonDAO") ;
			
			ACCDocBean bean = xcexCommonDAO.getAccDoc(docId);
			
			// 将JavaBean转换成JSON串
			jo.put("ex_doc_id", bean.getExDocId());
			jo.put("ledger_id",bean.getLedgerId());
			jo.put("ledger_name", bean.getLedgerName());
			jo.put("org_id", bean.getOrgId());
			jo.put("org_name", bean.getOrgName());
			jo.put("apply_ex_doc_id", bean.getApplyExDocId());
			jo.put("apply_ex_doc_code", bean.getApplyExDocCode());
			jo.put("ex_doc_code", bean.getExDocCode());
			jo.put("emp_name", bean.getEmpName());
			jo.put("biz_date", XCDateUtil.date2StrBy24(bean.getBizDate()));
			jo.put("dept_id", bean.getDeptId());
			jo.put("dept_name", bean.getDeptName());
			jo.put("project_id", bean.getProjectId());
			jo.put("project_name", bean.getProjectName());
			jo.put("ex_reason", bean.getExReason());
			jo.put("amount", bean.getAmount());
			jo.put("jkye", 0);
			jo.put("cancel_amt", bean.getCancelAmt());
			json = jo.toString() ;
			
		} catch (Exception e) {
			log.error(e.getMessage());
			json = "{}" ;
		}
		
		
		return json ;
	}
	/**
	 * @Title: getExDocById 
	 * @Description: 查询单据基础信息
	 * @param docId
	 * @return
	 * @throws Exception    设定文件
	 */
	public static String getExDocById(String docId) throws Exception{
		String json ="{}";
		try {
			JSONObject jo = new JSONObject();
			// 执行查询处理s
			
			XCEXCommonDAO xcexCommonDAO = (XCEXCommonDAO) AppContext.getApplicationContext().getBean("xcexCommonDAO") ;
			
			EXDocBean exBean = xcexCommonDAO.getExDocV(docId);
			jo.put("LEDGER_ID", exBean.getLedgerId());
			jo.put("ORG_ID", exBean.getOrgId());
			jo.put("BIZ_DATE",XCDateUtil.date2StrBy24(exBean.getBizDate()));
			jo.put("EX_DOC_CODE", exBean.getDocCode());
			jo.put("CREATED_BY_NAME",exBean.getEmpName());
			jo.put("DEPT_NAME", exBean.getDeptName());
			jo.put("PROJECT_NAME", exBean.getProjectName());
			jo.put("EX_REASON", exBean.getExReason());
			jo.put("EX_ITEM_NAME", exBean.getExItemName());
			jo.put("BG_ITEM_NAME", exBean.getBgItemName());
			jo.put("AMOUNT", exBean.getAmount());
			jo.put("CANCEL_AMT", exBean.getCancelAmt()) ;
			jo.put("EX_CAT_CODE", exBean.getDocCatCode());
			jo.put("EX_DOC_ID", exBean.getDocId());
			jo.put("INS_CODE", exBean.getInsCode());
			jo.put("AUDIT_STATUS", exBean.getAuditStatus());
			jo.put("CREATED_BY", exBean.getCreatedBy());
			jo.put("DEPT_ID", exBean.getDeptId());
			jo.put("PROJECT_ID", exBean.getProjectId());
			jo.put("EX_ITEM_ID", exBean.getExItemId());
			jo.put("BG_ITEM_ID", exBean.getBgItemId());
			jo.put("APPLY_EX_DOC_ID", exBean.getApplyDocId());
			jo.put("APPLY_EX_DOC_CODE", exBean.getApplyDocCode());
			
			jo.put("ORG_NAME", exBean.getOrgName());
			jo.put("LEDGER_NAME", exBean.getLedgerName());
			
			json = jo.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			json = "{}";
		}
		
		return json;
	}
}
