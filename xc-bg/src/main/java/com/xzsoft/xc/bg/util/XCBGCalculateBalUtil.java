package com.xzsoft.xc.bg.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.bg.modal.BgBillBalance;
import com.xzsoft.xc.bg.modal.BgDocDtl;
import com.xzsoft.xc.bg.modal.BgFactBalance;
import com.xzsoft.xip.platform.session.CurrentSessionVar;

/**
 * @className XCBGCalculateBalUtil
 * @author    tangxl
 * @date      2016年6月17日
 * @describe  合并汇总预算明细余额
 * @变动说明
 */
public class XCBGCalculateBalUtil {
	/**
	 * 
	 * @methodName  sumBudgetItemBal
	 * @author      tangxl
	 * @date        2016年6月20日
	 * @describe    合并预算单明细集合
	 * @param list
	 * @param bgItemType
	 * @return
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static List<BgBillBalance> sumBudgetItemBal(List<BgDocDtl> list,String bgType) throws Exception{
		HashMap<String, BgDocDtl> map = new HashMap<String, BgDocDtl>();
		List<BgBillBalance> insertList = new ArrayList<BgBillBalance>();
		String mapKey = "";
		BigDecimal firstDecimal = null;
		BigDecimal secondDecimal = null;
		for(BgDocDtl t:list){
			mapKey = t.getBgItemId().concat(t.getDeptId());
			if(map.containsKey(mapKey)){
				firstDecimal = new BigDecimal(map.get(mapKey).getAmount());
				secondDecimal = new BigDecimal(t.getAmount());
				BigDecimal sumDecimal = firstDecimal.add(secondDecimal);
				map.get(mapKey).setAmount(sumDecimal.toString());
			}else{
				map.put(mapKey, t);
			}
		}
		list.clear();
		list.addAll(map.values());
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(bgType)){
		    //预算单明细表记录里剩余的记录即是需要新增的记录
		    if(list.size()>0){
		    	for(BgDocDtl item:list){
		    		BgBillBalance newBalance = new BgBillBalance();
		    		newBalance.setLedgerId(item.getLedgerId());
		    		newBalance.setPeriodCode(item.getPeriodCode());
		    		newBalance.setBgYear(item.getPeriodCode().substring(0,4));
		    		newBalance.setBgItemId(item.getBgItemId());
		    		newBalance.setDeptId(item.getDeptId() == null?"A":item.getDeptId());
		    		newBalance.setBgAmount(new BigDecimal(item.getAmount()));
		    		newBalance.setCreationDate(new Date());
		    		newBalance.setCreatedBy(CurrentSessionVar.getUserId());
		    		newBalance.setLastUpdateDate(new Date());
		    		newBalance.setLastUpdatedBy(CurrentSessionVar.getUserId());
		    		insertList.add(newBalance);
		    	}
		    }
		}else{
		    //预算单明细表记录里剩余的记录即是需要新增的记录
		    if(list.size()>0){
		    	for(BgDocDtl item:list){
		    		BgBillBalance newBalance = new BgBillBalance();
		    		newBalance.setLedgerId(item.getLedgerId());
		    		newBalance.setProjectId(item.getProjectId());
		    		newBalance.setBgItemId(item.getBgItemId());
		    		newBalance.setDeptId(item.getDeptId() == null?"A":item.getDeptId());
		    		newBalance.setBgAmount(new BigDecimal(item.getAmount()));
		    		newBalance.setCreationDate(new Date());
		    		newBalance.setCreatedBy(CurrentSessionVar.getUserId());
		    		newBalance.setLastUpdateDate(new Date());
		    		newBalance.setLastUpdatedBy(CurrentSessionVar.getUserId());
		    		insertList.add(newBalance);
		    	}
		    }
		}
		return insertList;
	}
	/**
	 * 
	 * @methodName  summaryBgBalances
	 * @author      tangxl
	 * @date        2016年6月20日
	 * @describe    合并并计算余额
	 * @param businessList
	 * @param balancesList
	 * @param bgType
	 * @return updateList
	 * @throws Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static List<BgBillBalance> summaryBgBalances(List<BgBillBalance> businessList,List<BgBillBalance> balancesList,String bgType) throws Exception{
		List<BgBillBalance> updateList = new ArrayList<BgBillBalance>();
		//1 -- 费用预算 项目预算 合并已经存在的预算余额明细记录
		int size = businessList.size();
		for(int i=0;i<size;i++){
			BgBillBalance t = businessList.get(i);
	    	for(BgBillBalance a:balancesList){
	    		if(a.getBgItemId().equals(t.getBgItemId()) && a.getDeptId().equals(t.getDeptId())){
	    			a.setBgAmount(a.getBgAmount().add(t.getBgAmount()));
	    			updateList.add(a);
//	    			if(a.getBgAmount().compareTo(new BigDecimal(0)) <0){
//	    				 throw new Exception("【"+a.getBgItemName()+"】的预算减少数已超出【预算总额】！");
//	    			}
	    			businessList.remove(t);
	    		}
	    	}
	    }
		return updateList;
	}
	/**
	 * 
	 * @methodName  judgeBgIsValid
	 * @author      tangxl
	 * @date        2016年6月30日
	 * @describe    判断预算减少数+预算实际发生数 是否 >预算汇总余额数
	 * @param       updateList    【预算申请余额数】 + 【预算汇总余额数】
	 * @param       factList      【预算实际发生数】
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static void judgeBgIsValid(List<BgBillBalance> updateList,List<BgFactBalance> factList,String bgItemType) throws Exception{
		if(XCBGConstants.BG_COST_TYPE.equalsIgnoreCase(bgItemType)){
			for(BgBillBalance t:updateList){
				for(BgFactBalance p:factList){
					if(t.getBgItemId().equals(p.getBgItemId()) && t.getDeptId().equals(p.getDeptId())){
						if(t.getBgAmount().compareTo(p.getBgAmount()) <0){
							 throw new Exception("【"+t.getBgItemName()+"】的预算减少数已超出【预算总额】和【预算实际发生数】的差额！");
						}
					}
				}
			}
		}else{
			for(BgBillBalance t:updateList){
				for(BgFactBalance p:factList){
					if(t.getBgItemId().equals(p.getBgItemId()) && t.getDeptId().equals(p.getDeptId()) && t.getProjectId().equals(p.getProjectId())){
						if(t.getBgAmount().compareTo(p.getBgAmount()) <0){
							 throw new Exception("【"+t.getBgItemName()+"】的预算减少数已超出【预算总额】和【预算实际发生数】的差额！");
						}
					}
				}
			}
		}
	}
	/**
	 * 
	 * @methodName  summaryBgBalance
	 * @author      tangxl
	 * @date        2016年7月1日
	 * @describe    切换费用预算版本，汇总新的预算版本余额
	 * @param       summaryList
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static void summaryBgBalance(List<BgBillBalance> summaryList) throws Exception{
		//根据【账簿】、【期间】、【预算年】、【预算项目】、【成本中心】 对单据明细进行汇总处理
		HashMap<String,BgBillBalance> summaryMap = new HashMap<String, BgBillBalance>();
		StringBuffer  buffer = new StringBuffer();
		String bgYear = "";
		for(BgBillBalance t:summaryList){
			buffer.setLength(0);
			bgYear = t.getPeriodCode().substring(0, 4);
			t.setBgYear(bgYear);
			String distinctFlag = buffer.append(t.getLedgerId())
					.append(t.getPeriodCode()).append(bgYear)
					.append(t.getBgItemId()).append(t.getDeptId()).toString();
			if(summaryMap.containsKey(distinctFlag)){
				BgBillBalance existBean = summaryMap.get(distinctFlag);
				existBean.setBgAmount(existBean.getBgAmount().add(t.getBgAmount()));
			}else{
				summaryMap.put(distinctFlag, t);
			}
		}
		summaryList.clear();
		summaryList.addAll(summaryMap.values());
	}
	/**
	 * 
	 * @methodName  getBudgetBillSql
	 * @author      tangxl
	 * @date        2016年7月6日
	 * @describe    获取单据类型的Sql
	 * @param       paramsMap
	 * @param       billTypeList
	 * @return
	 * @throws      Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public static String getBudgetBillSql(HashMap<String, String> paramsMap,List<String> billTypeList) throws Exception{
		StringBuffer buffer = new StringBuffer();
		String projectId = paramsMap.get("projectId");
		String tableKey = "";
		String tmpSql = "";
		if(!"".equals(projectId)){
			for(String t:billTypeList){
				tableKey = t.substring(0, 5);
				if(buffer.length()>0){
					buffer.append(" union ");
				}
				//总账类单据
				if("XC_GL".equalsIgnoreCase(tableKey)){
					buffer.append("SELECT \\'").append(XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)).append("\\' AS BILL_CATEGORY,")
					       .append(" t.V_TEMPLATE_TYPE AS TEMPLATE_TYPE,\\'\\' AS DEPT_NAME,\\'GL\\' AS BILL_TYPE_CODE,") 
					       .append(" (select p.V_CATEGORY_NAME from xc_gl_v_category p where p.V_CATEGORY_ID = t.V_CATEGORY_ID) AS BILL_TYPE,")
					        .append(" t.V_HEAD_ID AS BILL_ID,")
					         .append(" t.V_SERIAL_NUM AS BILL_CODE, t.SUMMARY AS BILL_NAME,")
					          .append(" (select SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.V_HEAD_ID) AS BILL_AMOUNT, t.CREATION_DATE AS BILL_DATE,")
					           .append(" t.CREATED_NAME AS BILL_CREATOR, CASE t.V_STATUS WHEN \\'1\\' THEN \\'草稿\\' ELSE \\'审批中\\' END AS BILL_STATUS ")
					            .append(" FROM xc_gl_v_heads t WHERE t.V_HEAD_ID IN ")
					             .append(" ( SELECT f.SRC_ID FROM xc_bg_fact f ")
					              .append(" WHERE f.LEDGER_ID = \\'").append(paramsMap.get("ledgerId")).append("\\'")
					               .append(" AND f.PROJECT_ID = \\'").append(paramsMap.get("projectId")).append("\\'")
					                .append(" AND f.FACT_TYPE = \\'").append(paramsMap.get("factType")).append("\\'");
				}else if("XC_EX".equalsIgnoreCase(tableKey)){
				//报销类单据
					tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
							      + " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.EX_CAT_CODE AS BILL_TYPE_CODE,"
							      + " t.EX_DOC_ID AS BILL_ID, t.EX_DOC_CODE AS BILL_CODE, "
							      + " (SELECT p.EX_CAT_NAME FROM xc_ex_cat p WHERE p.EX_CAT_CODE = t.EX_CAT_CODE ) AS BILL_TYPE, "
							      + " t.EX_REASON AS BILL_NAME, (select SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.EX_DOC_ID) AS BILL_AMOUNT, t.BIZ_DATE AS BILL_DATE, "
							      + " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID ) AS BILL_CREATOR, "
							      + " t.AUDIT_STATUS_DESC AS BILL_STATUS FROM xc_ex_docs t, xip_pub_users u "
							      + " WHERE t.CREATED_BY = u.USER_ID AND t.EX_DOC_ID IN "
							      + " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\'"
							      + " AND f.PROJECT_ID = \\'"+paramsMap.get("projectId")+"\\'"
							      + " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
							      + " )";
					buffer.append(tmpSql);
				}else if("XC_AP".equalsIgnoreCase(tableKey)){
				//应付类单据  
						//1-- 付款申请单
						if("XC_AP_PAY_REQ_H".equalsIgnoreCase(t)){
							tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
									+ " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.AP_DOC_CAT_CODE AS BILL_TYPE_CODE,"
									+ " t.AP_PAY_REQ_H_ID AS BILL_ID, "
									+ " t.AP_PAY_REQ_H_CODE AS BILL_CODE, "
									+ " c.AP_CAT_NAME AS BILL_TYPE, "
									+ " t.DESCRIPTION AS BILL_NAME, "
									+ " (SELECT SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.AP_PAY_REQ_H_ID ) AS BILL_AMOUNT,"
									+ " t.BIZ_DATE AS BILL_DATE, "
									+ " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID) AS BILL_CREATOR,"
									+ " t.AUDIT_STATUS_DESC AS BILL_STATUS FROM xc_ap_pay_req_h t, xc_ap_doc_cats c, xip_pub_users u "
									+ " WHERE t.AP_DOC_CAT_CODE = c.AP_CAT_CODE AND t.CREATED_BY = u.USER_ID "
									+ " AND t.AP_PAY_REQ_H_ID IN "
									+ " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\' "
									+ " AND f.PROJECT_ID = \\'"+paramsMap.get("projectId")+"\\'"
									+ " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
								    + ")";
						}else if("XC_AP_INVOICE_H".equalsIgnoreCase(t)){
							tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
									+ " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.AP_DOC_CAT_CODE AS BILL_TYPE_CODE,"
									+ " t.AP_INV_H_ID AS BILL_ID, "
									+ " t.AP_INV_H_CODE AS BILL_CODE, "
									+ " c.AP_CAT_NAME AS BILL_TYPE, "
									+ " t.DESCRIPTION AS BILL_NAME, "
									+ " (SELECT SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.AP_INV_H_ID ) AS BILL_AMOUNT,"
									+ " t.BIZ_DATE AS BILL_DATE, "
									+ " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID) AS BILL_CREATOR,"
									+ " t.AUDIT_STATUS_DESC AS BILL_STATUS FROM xc_ap_invoice_h t, xc_ap_doc_cats c, xip_pub_users u "
									+ " WHERE t.AP_DOC_CAT_CODE = c.AP_CAT_CODE AND t.CREATED_BY = u.USER_ID "
									+ " AND t.AP_INV_H_ID IN "
									+ " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\' "
									+ " AND f.PROJECT_ID = \\'"+paramsMap.get("projectId")+"\\'"
								    + " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
								    + ")";
					    //2--发票单
						}else if("XC_AP_INV_GL_H".equalsIgnoreCase(t)){
							//3--应付单 
							tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
									+ " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.AP_DOC_CAT_CODE AS BILL_TYPE_CODE,"
									+ " t.AP_INV_GL_H_ID AS BILL_ID, "
									+ " t.AP_INV_GL_H_CODE AS BILL_CODE, "
									+ " c.AP_CAT_NAME AS BILL_TYPE, "
									+ " t.DESCRIPTION AS BILL_NAME, "
									+ " (SELECT SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.AP_INV_GL_H_ID ) AS BILL_AMOUNT,"
									+ " t.GL_DATE AS BILL_DATE, "
									+ " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID) AS BILL_CREATOR,"
									+ " t.V_STATUS AS BILL_STATUS FROM xc_ap_inv_gl_h t, xc_ap_doc_cats c, xip_pub_users u "
									+ " WHERE t.AP_DOC_CAT_CODE = c.AP_CAT_CODE AND t.CREATED_BY = u.USER_ID "
									+ " AND t.AP_INV_GL_H_ID IN "
									+ " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\' "
									+ " AND f.PROJECT_ID = \\'"+paramsMap.get("projectId")+"\\'"
									+ " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
								    + ")";
						}else if("XC_AP_PAY_H".equalsIgnoreCase(t)){
						//4--付款表 
							tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
									+ " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.AP_DOC_CAT_CODE AS BILL_TYPE_CODE,"
									+ " t.AP_PAY_H_ID AS BILL_ID, "
									+ " t.AP_PAY_H_CODE AS BILL_CODE, "
									+ " c.AP_CAT_NAME AS BILL_TYPE, "
									+ " t.DESCRIPTION AS BILL_NAME, "
									+ " (SELECT SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.AP_PAY_H_ID ) AS BILL_AMOUNT,"
									+ " t.GL_DATE AS BILL_DATE, "
									+ " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID) AS BILL_CREATOR,"
									+ " t.V_STATUS AS BILL_STATUS FROM xc_ap_pay_h t, xc_ap_doc_cats c, xip_pub_users u "
									+ " WHERE t.AP_DOC_CAT_CODE = c.AP_CAT_CODE AND t.CREATED_BY = u.USER_ID "
									+ " AND t.AP_PAY_H_ID IN "
									+ " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\' "
									+ " AND f.PROJECT_ID = \\'"+paramsMap.get("projectId")+"\\'"
								    + " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
								    + ")";
						}
						buffer.append(tmpSql);
				}else if("XC_AR".equalsIgnoreCase(tableKey)){
				//应收类单据	
				}    
			}
		}else{
			for(String t:billTypeList){
				tableKey = t.substring(0, 5);
				if(buffer.length()>0){
					buffer.append(" union ");
				}
				//总账类单据
				if("XC_GL".equalsIgnoreCase(tableKey)){
					buffer.append("SELECT \\'").append(XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)).append("\\' AS BILL_CATEGORY,")
					      .append(" t.V_TEMPLATE_TYPE AS TEMPLATE_TYPE,\\'\\' AS DEPT_NAME,\\'GL\\' AS BILL_TYPE_CODE,")
					       .append(" (select p.V_CATEGORY_NAME from xc_gl_v_category p where p.V_CATEGORY_ID = t.V_CATEGORY_ID) AS BILL_TYPE,")
					        .append(" t.V_HEAD_ID AS BILL_ID,")
					         .append(" t.V_SERIAL_NUM AS BILL_CODE, t.SUMMARY AS BILL_NAME,")
					          .append(" (select SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.V_HEAD_ID) AS BILL_AMOUNT, t.CREATION_DATE AS BILL_DATE,")
					           .append(" t.CREATED_NAME AS BILL_CREATOR, CASE t.V_STATUS WHEN \\'1\\' THEN \\'草稿\\' ELSE \\'审批中\\' END AS BILL_STATUS ")
					            .append(" FROM xc_gl_v_heads t WHERE t.V_HEAD_ID IN ")
					             .append(" ( SELECT f.SRC_ID FROM xc_bg_fact f ")
					              .append(" WHERE f.LEDGER_ID = \\'").append(paramsMap.get("ledgerId")).append("\\'")
					               .append(" AND f.BG_ITEM_ID = \\'").append(paramsMap.get("bgItemId")).append("\\'")
					                .append(" AND SUBSTR(f.FACT_DATE,1,4) = \\'").append(paramsMap.get("bgYear")).append("\\'")
					                 .append(" AND f.FACT_TYPE = \\'").append(paramsMap.get("factType")).append("\\'");
				}else if("XC_EX".equalsIgnoreCase(tableKey)){
				//报销类单据
					tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
							 + " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.EX_CAT_CODE AS BILL_TYPE_CODE,"
							      + " t.EX_DOC_ID AS BILL_ID, t.EX_DOC_CODE AS BILL_CODE, "
							      + " (SELECT p.EX_CAT_NAME FROM xc_ex_cat p WHERE p.EX_CAT_CODE = t.EX_CAT_CODE ) AS BILL_TYPE, "
							      + " t.EX_REASON AS BILL_NAME, (select SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.EX_DOC_ID) AS BILL_AMOUNT, t.BIZ_DATE AS BILL_DATE, "
							      + " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID ) AS BILL_CREATOR, "
							      + " t.AUDIT_STATUS_DESC AS BILL_STATUS FROM xc_ex_docs t, xip_pub_users u "
							      + " WHERE t.CREATED_BY = u.USER_ID AND t.EX_DOC_ID IN "
							      + " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\'"
							      + " AND f.BG_ITEM_ID = \\'"+paramsMap.get("bgItemId")+"\\'"
					              + " AND SUBSTR(f.FACT_DATE,1,4) = \\'"+paramsMap.get("bgYear")+"\\'"
					              + " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
							      + " )";
					buffer.append(tmpSql);
				}else if("XC_AP".equalsIgnoreCase(tableKey)){
				//应付类单据
					//1-- 付款申请单
					if("XC_AP_PAY_REQ_H".equalsIgnoreCase(t)){
						tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
								+ " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.AP_DOC_CAT_CODE AS BILL_TYPE_CODE,"
								+ " t.AP_PAY_REQ_H_ID AS BILL_ID, "
								+ " t.AP_PAY_REQ_H_CODE AS BILL_CODE, "
								+ " c.AP_CAT_NAME AS BILL_TYPE, "
								+ " t.DESCRIPTION AS BILL_NAME, "
								+ " (SELECT SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.AP_PAY_REQ_H_ID ) AS BILL_AMOUNT,"
								+ " t.BIZ_DATE AS BILL_DATE, "
								+ " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID) AS BILL_CREATOR,"
								+ " t.AUDIT_STATUS_DESC AS BILL_STATUS FROM xc_ap_pay_req_h t, xc_ap_doc_cats c, xip_pub_users u "
								+ " WHERE t.AP_DOC_CAT_CODE = c.AP_CAT_CODE AND t.CREATED_BY = u.USER_ID "
								+ " AND t.AP_PAY_REQ_H_ID IN "
								+ " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\' "
								+ " AND f.BG_ITEM_ID = \\'"+paramsMap.get("bgItemId")+"\\'"
								+ " AND SUBSTR(f.FACT_DATE,1,4) = \\'"+paramsMap.get("bgYear")+"\\'"
							    + " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
							    + " )";
					}else if("XC_AP_INVOICE_H".equalsIgnoreCase(t)){
						tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
								+ " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.AP_DOC_CAT_CODE AS BILL_TYPE_CODE,"
								+ " t.AP_INV_H_ID AS BILL_ID, "
								+ " t.AP_INV_H_CODE AS BILL_CODE, "
								+ " c.AP_CAT_NAME AS BILL_TYPE, "
								+ " t.DESCRIPTION AS BILL_NAME, "
								+ " (SELECT SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.AP_INV_H_ID ) AS BILL_AMOUNT,"
								+ " t.BIZ_DATE AS BILL_DATE, "
								+ " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID) AS BILL_CREATOR,"
								+ " t.AUDIT_STATUS_DESC AS BILL_STATUS FROM xc_ap_invoice_h t, xc_ap_doc_cats c, xip_pub_users u "
								+ " WHERE t.AP_DOC_CAT_CODE = c.AP_CAT_CODE AND t.CREATED_BY = u.USER_ID "
								+ " AND t.AP_INV_H_ID IN "
								+ " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\' "
								+ " AND f.BG_ITEM_ID = \\'"+paramsMap.get("bgItemId")+"\\'"
								+ " AND SUBSTR(f.FACT_DATE,1,4) = \\'"+paramsMap.get("bgYear")+"\\'"
							    + " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
					    		+ ")";
				    //2--发票单
					}else if("XC_AP_INV_GL_H".equalsIgnoreCase(t)){
						//3--应付单 
						tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
								+ " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.AP_DOC_CAT_CODE AS BILL_TYPE_CODE,"
								+ " t.AP_INV_GL_H_ID AS BILL_ID, "
								+ " t.AP_INV_GL_H_CODE AS BILL_CODE, "
								+ " c.AP_CAT_NAME AS BILL_TYPE, "
								+ " t.DESCRIPTION AS BILL_NAME, "
								+ " (SELECT SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.AP_INV_GL_H_ID ) AS BILL_AMOUNT,"
								+ " t.GL_DATE AS BILL_DATE, "
								+ " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID) AS BILL_CREATOR,"
								+ " t.V_STATUS AS BILL_STATUS FROM xc_ap_inv_gl_h t, xc_ap_doc_cats c, xip_pub_users u "
								+ " WHERE t.AP_DOC_CAT_CODE = c.AP_CAT_CODE AND t.CREATED_BY = u.USER_ID "
								+ " AND t.AP_INV_GL_H_ID IN "
								+ " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\' "
								+ " AND f.BG_ITEM_ID = \\'"+paramsMap.get("bgItemId")+"\\'"
								+ " AND SUBSTR(f.FACT_DATE,1,4) = \\'"+paramsMap.get("bgYear")+"\\'"
							    + " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
					    		+ " )";
					}else if("XC_AP_PAY_H".equalsIgnoreCase(t)){
					//4--付款表 
						tmpSql = "SELECT \\'"+XCBGConstants.BUDGET_BILL_TYPE.get(tableKey)+"\\' AS BILL_CATEGORY,"
								+ " \\'\\' AS TEMPLATE_TYPE, (select d.DEPT_NAME FROM xip_pub_depts d WHERE d.DEPT_ID = t.DEPT_ID)  AS DEPT_NAME,t.AP_DOC_CAT_CODE AS BILL_TYPE_CODE,"
								+ " t.AP_PAY_H_ID AS BILL_ID, "
								+ " t.AP_PAY_H_CODE AS BILL_CODE, "
								+ " c.AP_CAT_NAME AS BILL_TYPE, "
								+ " t.DESCRIPTION AS BILL_NAME, "
								+ " (SELECT SUM(l.AMOUNT) AS BILL_AMOUNT FROM xc_bg_fact l WHERE l.SRC_ID = t.AP_PAY_H_ID ) AS BILL_AMOUNT,"
								+ " t.GL_DATE AS BILL_DATE, "
								+ " (SELECT p.EMP_NAME FROM xip_pub_emps p WHERE p.EMP_ID = u.EMP_ID) AS BILL_CREATOR,"
								+ " t.V_STATUS AS BILL_STATUS FROM xc_ap_pay_h t, xc_ap_doc_cats c, xip_pub_users u "
								+ " WHERE t.AP_DOC_CAT_CODE = c.AP_CAT_CODE AND t.CREATED_BY = u.USER_ID "
								+ " AND t.AP_PAY_H_ID IN "
								+ " (SELECT f.SRC_ID FROM xc_bg_fact f WHERE f.LEDGER_ID = \\'"+paramsMap.get("ledgerId")+"\\' "
								+ " AND f.BG_ITEM_ID = \\'"+paramsMap.get("bgItemId")+"\\'"
								+ " AND SUBSTR(f.FACT_DATE,1,4) = \\'"+paramsMap.get("bgYear")+"\\'"
								+ " AND f.FACT_TYPE = \\'"+paramsMap.get("factType")+"\\'"
					    		+ ")";
					}
					buffer.append(tmpSql);
				}else if("XC_AR".equalsIgnoreCase(tableKey)){
				//应收类单据	
				}    
			}
		}
		return buffer.toString();
	}
}
