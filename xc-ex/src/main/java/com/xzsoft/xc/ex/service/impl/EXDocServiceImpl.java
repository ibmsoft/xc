package com.xzsoft.xc.ex.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xzsoft.xc.bg.api.impl.BudegetDataManageApi;
import com.xzsoft.xc.bg.modal.BgFactBean;
import com.xzsoft.xc.bg.util.XCBGConstants;
import com.xzsoft.xc.ex.dao.EXDocBaseDAO;
import com.xzsoft.xc.ex.dao.XCEXCommonDAO;
import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.EXDocDtlBean;
import com.xzsoft.xc.ex.service.EXDocBaseService;
import com.xzsoft.xc.ex.util.XCEXConstants;
import com.xzsoft.xc.ex.util.XCEXItemTreeUtil;
import com.xzsoft.xc.util.common.XCDateUtil;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xc.util.service.RuleService;
import com.xzsoft.xip.platform.session.CurrentSessionVar;
import com.xzsoft.xip.platform.util.XipUtil;
import com.xzsoft.xip.wf.common.util.CurrentUserUtil;

/**
 * @ClassName: EXDocServiceImpl 
 * @Description: 费用报销单据基础信息处理类
 * @author linp
 * @date 2016年5月24日 下午4:16:42 
 *
 */
@Service("exDocBaseService")
public class EXDocServiceImpl implements EXDocBaseService {
	@Resource
	private EXDocBaseDAO exDocBaseDAO ;
	@Resource
	private RuleService ruleService ;
	@Resource
	private XCEXCommonDAO xcexCommonDAO ;

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
			
			HashMap<String,String> ldCatMap = exDocBaseDAO.getLedgerCatInfo(ledgerId, docCat) ;
			String processId = ldCatMap.get("PROCESS_ID") ;
			String processCode = ldCatMap.get("PROCESS_CODE") ;
			String ruleCode = ldCatMap.get("RULE_CODE") ;
			String ldAttCat = ldCatMap.get("ATT_CAT_CODE") ;
			
			jo.put("PROCESS_ID", processId) ;
			jo.put("PROCESS_CODE", processCode) ;
			jo.put("RULE_CODE", ruleCode) ;
			jo.put("ATT_CAT_CODE", ldAttCat) ;
			
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
			HashMap<String,String> gbCatMap = exDocBaseDAO.getGlobalCatInfo(docCat) ;
			if(processId == null || "".equals(processId)){
				String gbProcId = gbCatMap.get("PROCESS_ID") ;
				String gbProcCode = gbCatMap.get("PROCESS_CODE") ;
				String gbAttCat = gbCatMap.get("ATT_CAT_CODE") ;
				if(gbProcId == null || "".equals(gbProcId)){
					//throw new Exception("单据类型:"+docCat+"未设置审批流程，请在【单据类型设置或单据类型】功能处设置") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT", null)+docCat+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PROCESS_IS_NOT_NULL", null));
				}else{
					jo.put("PROCESS_ID", gbProcId) ;
					jo.put("PROCESS_CODE", gbProcCode) ;
					jo.put("ATT_CAT_CODE", gbAttCat) ;
				}
			}
			jo.put("EX_CAT_CODE", gbCatMap.get("EX_CAT_CODE")) ;		// 单据类型编码
			jo.put("EX_CAT_NAME", gbCatMap.get("EX_CAT_NAME")) ;		// 单据类型名称
			jo.put("IS_SIGN", gbCatMap.get("IS_SIGN")) ;				// 是否需要签字
			jo.put("IS_PAY", gbCatMap.get("IS_PAY")) ;					// 是否需要付款
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
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getDocsAndUrlsByLedger</p> 
	 * <p>Description: 获取账簿启用的单据类型及填报地址信息 </p> 
	 * @param ledgerId
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#getDocsAndUrlsByLedger(java.lang.String)
	 */
	public JSONArray getDocsAndUrlsByLedger(String ledgerId) throws Exception {
		JSONArray ja = new JSONArray() ;
		
		try {
			// 查询账簿下已启动的单据类型信息
			
			/*LA.LEDGER_ID ,
	       		A.EX_CAT_CODE,
	       		A.EX_CAT_NAME ,
	       		A.PC_W_FORM_ID,
	       		C.FORM_URL PC_W_FORM_URL,
	       		A.M_W_FORM_ID,
	       		F.FORM_URL M_W_FORM_URL*/
			
			List<HashMap<String,String>> maps = exDocBaseDAO.getLdSecCatInfo(ledgerId) ;
			if(maps != null && maps.size()>0){
				for(HashMap<String,String> map :maps){
					JSONObject jo = new JSONObject() ;
					jo.put("EX_CAT_CODE", map.get("EX_CAT_CODE")) ;
					jo.put("EX_CAT_NAME", map.get("EX_CAT_NAME")) ;
					jo.put("PC_W_FORM_ID", map.get("PC_W_FORM_ID")) ;
					jo.put("PC_W_FORM_URL", map.get("PC_W_FORM_URL")) ;
					jo.put("M_W_FORM_ID", map.get("M_W_FORM_ID")) ;
					jo.put("M_W_FORM_URL", map.get("M_W_FORM_URL")) ;
					
					ja.put(jo) ;
				}
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return ja ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getDocPrintUrlByCat</p> 
	 * <p>Description: 获取单据类型指定的打印表单信息 </p> 
	 * @param docCat
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#getDocPrintUrlByCat(java.lang.String)
	 */
	public JSONObject getDocPrintUrlByCat(String docCat) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			HashMap<String,String> map = exDocBaseDAO.getDocPrintUrlByCat(docCat) ;
			
			String catName = map.get("EX_CAT_NAME") ;
			String formUrl = map.get("FORM_URL") ;
			
			if(formUrl != null && !"".equals(formUrl)){
				jo.put("formUrl", formUrl) ;
			}else{
				//throw new Exception(catName+"未设置打印表单，请在【单据类型】功能处设置") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT", null)+catName+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_FORM_IS_NOT_NULL", null));
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: getAttCatCode</p> 
	 * <p>Description: 获取附件分类码 </p> 
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#getAttCatCode(java.lang.String, java.lang.String)
	 */
	public String getAttCatCode(String ledgerId, String docCat) throws Exception {
		String attCat = null ; 
		
		try {
			// 获取账簿级单据类型信息
			HashMap<String,String> ldCatMap = exDocBaseDAO.getLedgerCatInfo(ledgerId, docCat) ;
			String catName = ldCatMap.get("EX_CAT_NAME") ;
			attCat = ldCatMap.get("ATT_CAT_CODE") ;
			
			if(attCat == null || "".equals(attCat)){
				// 获取全局单据类型信息
				HashMap<String,String> gbCatMap = exDocBaseDAO.getGlobalCatInfo(docCat) ;
				attCat = gbCatMap.get("ATT_CAT_CODE") ;
			}
			
			if(attCat == null || "".equals(attCat)) //throw new Exception(catName +"未设置附件分类，请在【单据类型设置或单据类型】功能处设置") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT", null)+catName+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_ENCLOSURE_IS_NOT_NULL", null));
			
		} catch (Exception e) {
			throw e ;
		}
		
		return attCat ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: saveOrupdateDoc</p> 
	 * <p>Description: 保存费用单据信息 </p> 
	 * @param masterTabJsonStr
	 * @param detailTabJsonArray
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#saveDoc(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor={Exception.class}) 
	public JSONObject saveOrupdateDoc(String masterTabJsonStr, String detailTabJsonArray) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		try {
			if(masterTabJsonStr == null || "".equals(masterTabJsonStr)) //throw new Exception("单据信息不能为空!") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_IS_NOT_NULL", null));
			
			// 存储单据信息对象
			EXDocBean masterBean = new EXDocBean() ;
			
			// 当前用户和当前日期
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 单据主信息
			JSONObject docJo = null ;
			try {
				docJo = new JSONObject(masterTabJsonStr) ;
				
			} catch (Exception e) {
				//throw new Exception("单据主数据不合法:"+e.getMessage()) ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_IS_ILLEGAL", null)+e.getMessage());
			}
			
			// 将JSON -> JavaBean
			masterBean = (EXDocBean) this.jsonObjectToBean(docJo, EXDocBean.class) ;
			
			// 处理单据ID和单据编号
			String docId = masterBean.getDocId() ;
			if(docId == null || "".equals(docId)){
				// 获取单据ID
				docId = UUID.randomUUID().toString() ;
				// 获取单据类型信息
				HashMap<String,String> catMap = exDocBaseDAO.getLedgerCatInfo(masterBean.getLedgerId(), masterBean.getDocCatCode()) ;
				String ruleCode = catMap.get("RULE_CODE") ;
				String catName = catMap.get("EX_CAT_NAME") ;
				if(ruleCode == null || "".equals(ruleCode)){
					//throw new Exception(catName+"未设置编码规则,请确认!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT", null)+catName+XipUtil.getMessage(XCUtil.getLang(), "XC_EX_CODING_RULE_IS_NOT_NULL", null));
				}
				// 获取年份和月份
				String bizDate = XCDateUtil.date2Str(masterBean.getBizDate()) ;
				String year = bizDate.substring(0, 4) ;
				String month = String.valueOf(Integer.parseInt(bizDate.substring(5, 7))) ;
				// 生成单据编号
				String docCode = ruleService.getNextSerialNum(ruleCode, masterBean.getLedgerId(), null, year, month, userId) ;
				masterBean.setDocCode(docCode);
			}
			
     		// 记录差旅报销单和费用报销单的单据明细
			if(XCEXConstants.EX_DOC_EX03.equals(masterBean.getDocCatCode()) 
					|| XCEXConstants.EX_DOC_EX04.equals(masterBean.getDocCatCode() )){ 
				
				if(detailTabJsonArray == null || "".equals(detailTabJsonArray)) //throw new Exception("单据明细行数据不能为空!") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_IS_NOT_NULL", null));
				
				JSONArray lineJa = null ;
				try {
					lineJa = new JSONArray(detailTabJsonArray) ;
				} catch (Exception e) {
					//throw new Exception("单据明细行数据不合法:"+e.getMessage()) ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_IS_ILLEGAL", null)+e.getMessage());
				}
				
				if(lineJa != null && lineJa.length()>0){
					// 存储待新增明细行
					List<EXDocDtlBean> addLines = new ArrayList<EXDocDtlBean>() ;   
					// 存储待更新明细行
					List<EXDocDtlBean> updLines = new ArrayList<EXDocDtlBean>() ; 
					// 存储待删除明细行
					List<String> delLines = new ArrayList<String>() ; 
					// 单据合计金额
					BigDecimal totalAmt = new BigDecimal(0) ;
					
					// 更新单据查询单据现有明细信息
					if(masterBean.getDocId() != null && !"".equals(masterBean.getDocId())){
						List<EXDocDtlBean> existsDocDtl = exDocBaseDAO.getDocDtls(masterBean.getDocId()) ;
						if(existsDocDtl != null && existsDocDtl.size()>0){
							for(EXDocDtlBean bean : existsDocDtl){
								delLines.add(bean.getDtlId()) ;
							}
						}
					}
					
					// 遍历明细行
					for(int i=0; i<lineJa.length(); i++){
						// JSONObject -> JavaBean
						JSONObject lineJo = lineJa.getJSONObject(i) ;
						EXDocDtlBean lineBean = (EXDocDtlBean) this.jsonObjectToBean(lineJo, EXDocDtlBean.class) ;
						
						// 明细金额
						BigDecimal bd = new BigDecimal(lineBean.getDtlAmt()) ;
						totalAmt = totalAmt.add(bd).setScale(2, BigDecimal.ROUND_HALF_UP) ;
						
						// 设置最后更新日期和更新人
						lineBean.setCreatedBy(userId);
						lineBean.setCreationDate(cdate);
						lineBean.setLastUpdateDate(cdate);
						lineBean.setLastUpdatedBy(userId);
						
						// 单据明细ID
						String dtlId = lineBean.getDtlId() ;
						if(dtlId != null && !"".equals(dtlId)){
							// 记录更新明细行
							updLines.add(lineBean) ;
							// 记录删除明细行
							delLines.remove(dtlId) ;
							
						}else{
							// 设置单据明细ID和单据ID
							lineBean.setDtlId(UUID.randomUUID().toString());
							lineBean.setDocId(docId);
							// 记录新增明细行
							addLines.add(lineBean) ;
						}
					}
					
					// 合计金额
					masterBean.setAmount(totalAmt.doubleValue());
					
					// 记录新增明细行
					if(!addLines.isEmpty()){
						masterBean.setDocDtl(addLines);
					}
					// 记录更新明细行
					if(!updLines.isEmpty()){
						masterBean.setUpdLines(updLines);
					}
					// 记录删除明细行
					if(!delLines.isEmpty()){
						masterBean.setDocDtlIds(delLines);
					}
				}
			}
			
			// 设置最后更新日期和更新人
			masterBean.setCreationDate(cdate);
			masterBean.setLastUpdateDate(cdate);
			masterBean.setLastUpdatedBy(userId);
			
			// 设置签字状态：1-待签收，2-已签收，3-拒签
			masterBean.setSignStatus("1");
			
			// 执行数据保存或更新处理
			if(masterBean.getDocId() != null && !"".equals(masterBean.getDocId())){
				exDocBaseDAO.updateDoc(masterBean);
			}else{
				// 设置业务单据ID
				masterBean.setDocId(docId);
				exDocBaseDAO.saveDoc(masterBean);
			}
			
			// 返回信息
			jo.put("docId", masterBean.getDocId()) ;
			jo.put("docCode", masterBean.getDocCode()) ;
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: jsonObjectToBean 
	 * @Description: 将JSON对象转换为JavaBean
	 * @param jsonStr
	 * @return
	 * @throws Exception    设定文件
	 */
	private Object jsonObjectToBean(JSONObject jo,Class<?> T) throws Exception {
		try{
			if("EXDocBean".equals(T.getSimpleName())){ // 单据主表信息
				EXDocBean bean = new EXDocBean() ;
				/*				
				{
					"LEDGER_ID":"4001fa86-0a5c-433f-bd5b-1de4d062375a",
					"ORG_ID":"d9923122-0689-4ee3-9db3-40e54e8526be",
					"BIZ_DATE":"2016-06-22 00:00:00.000",
					"EX_DOC_CODE":"",
					"CREATED_BY_NAME":"系统管理员",
					"DEPT_NAME":"技术支持部",
					"PROJECT_NAME":"万家灯火项目",
					"EX_REASON":"业务招待费",
					"EX_ITEM_NAME":"差旅费-活动费",
					"BG_ITEM_NAME":"销售费用-礼品费",
					"AMOUNT":2000,
					"EX_CAT_CODE":"EX01",
					"EX_DOC_ID":"",
					"INS_CODE":"",
					"AUDIT_STATUS":"",
					"CREATED_BY":"f92c688a-df48-d4e2-e040-a8c021824389",
					"DEPT_ID":"3737ba64-3656-4316-8b61-6c9055dd4148",
					"PROJECT_ID":"1b4c023c-635a-41e6-bf42-1e7bbadd4842",
					"BG_ITEM_ID":"e586e10a-32ce-4b85-82dc-83a1ccd43091",
					"EX_ITEM_ID":"ce2502f0-35b8-4954-b453-fe0e7f755771"
					"CANCEL_AMT":""
				}
 				*/		
				
				// 单据类型：EX01~EX06
				Object docCatCode = null ;
				if(!jo.has("EX_CAT_CODE")){
					//throw new Exception("单据类型不能为空") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT_IS_NOT_NULL", null));
				}else{
					docCatCode = jo.get("EX_CAT_CODE") ;
					if(docCatCode != null && !"".equals(docCatCode)){
						bean.setDocCatCode(String.valueOf(docCatCode));
					}else{
						//throw new Exception("单据类型不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_CAT_IS_NOT_NULL", null));
					}
				}
				// 账簿
				if(!jo.has("LEDGER_ID")){
					//throw new Exception("账簿不能为空");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER_IS_NOT_NULL", null));
				}else{
					Object ledgerId = jo.get("LEDGER_ID") ;
					if(ledgerId != null && !"".equals(ledgerId)){
						bean.setLedgerId(String.valueOf(ledgerId));
					}else{
						//throw new Exception("账簿不能为空");
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEDGER_IS_NOT_NULL", null));
					}
				}
				// 组织
				if(!jo.has("ORG_ID")){
					//throw new Exception("组织不能为空");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_ORG_IS_NOT_NULL", null));
				}else{
					Object orgId = jo.get("ORG_ID") ;
					if(orgId != null && !"".equals(orgId)){
						bean.setOrgId(String.valueOf(orgId));
					}else{
						//throw new Exception("组织不能为空");
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_ORG_IS_NOT_NULL", null));
					}
				}
				// 业务日期
				
				if(!jo.has("BIZ_DATE")){
					//throw new Exception("业务日期不能为空");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_BIZ_DATE_IS_NOT_NULL", null));
				}else{
					Object bizDate = jo.get("BIZ_DATE") ;
					if(bizDate != null && !"".equals(bizDate)){
						bean.setBizDate(XCDateUtil.str2Date(String.valueOf(bizDate)));
					}else{
						//throw new Exception("业务日期不能为空");
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_BIZ_DATE_IS_NOT_NULL", null));
					}
				}
				// 申请人
				
				if(!jo.has("CREATED_BY")){
					//throw new Exception("单据申请人不能为空");
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_CREATER_IS_NOT_NULL", null));
				}else{
					Object createdBy = jo.get("CREATED_BY") ;
					if(createdBy != null && !"".equals(createdBy)){
						bean.setCreatedBy(String.valueOf(createdBy));
					}else{
						//throw new Exception("单据申请人不能为空");
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_CREATER_IS_NOT_NULL", null));
					}
					if(jo.has("CREATED_BY_NAME")){
						Object empName = jo.get("CREATED_BY_NAME") ;
						bean.setEmpName(String.valueOf(empName));
					}
				}
				// 成本中心
				if(!jo.has("DEPT_ID")){
					//throw new Exception("成本中心不能为空") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DEPT_IS_NOT_NULL", null));
				}else{
					Object deptId = jo.get("DEPT_ID") ;
					if(deptId != null && !"".equals(deptId)){
						bean.setDeptId(String.valueOf(deptId));
					}else{
						//throw new Exception("成本中心不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DEPT_IS_NOT_NULL", null));
					}
					if(jo.has("DEPT_NAME")){
						Object deptName = jo.get("DEPT_NAME") ;
						bean.setDeptName(String.valueOf(deptName));
					}
				}
				// 单据事由
				if(!jo.has("EX_REASON")){
					String msg = null ;
					switch(String.valueOf(docCatCode)){
					case XCEXConstants.EX_DOC_EX01:
						//msg = "申请事由不能为空" ;
						msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_01", null);
						break;
					case XCEXConstants.EX_DOC_EX02:
						///msg = "借款事由不能为空" ;
						msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_02", null);
						break ;
					case XCEXConstants.EX_DOC_EX03:
						//msg = "报销说明不能为空" ;
						msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_03", null);
						break ;
					case XCEXConstants.EX_DOC_EX04:
						//msg = "报销说明不能为空" ;
						msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_04", null);
						break ;
					case XCEXConstants.EX_DOC_EX05:
						//msg = "还款说明不能为空" ;
						msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_05", null);
						break ;
					default:
						break ;
					}
					throw new Exception(msg) ;
					
				}else{
					Object exReason = jo.get("EX_REASON") ;
					if(exReason != null && !"".equals(exReason)){
						bean.setExReason(String.valueOf(exReason).replace("\t", "").replace("\n", ""));
					}else{
						String msg = null ;
						switch(String.valueOf(docCatCode)){
						case XCEXConstants.EX_DOC_EX01:
							//msg = "申请事由不能为空" ;
							msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_01", null);
							break;
						case XCEXConstants.EX_DOC_EX02:
							//msg = "借款事由不能为空" ;
							msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_02", null);
							break ;
						case XCEXConstants.EX_DOC_EX03:
							//msg = "报销说明不能为空" ;
							msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_03", null);
							break ;
						case XCEXConstants.EX_DOC_EX04:
							//msg = "报销说明不能为空" ;
							msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_04", null);
							break ;
						case XCEXConstants.EX_DOC_EX05:
							//msg = "还款说明不能为空" ;
							msg = XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DESC_IS_NOT_NULL_05", null);
							break ;
						default:
							break ;
						}
						throw new Exception(msg) ;
					}
				}
				// PA项目
				if(jo.has("PROJECT_ID")){
					Object projectId = jo.get("PROJECT_ID") ;
					bean.setProjectId(String.valueOf(projectId));
				}
				if(jo.has("PROJECT_NAME")){
					Object projectName = jo.get("PROJECT_NAME") ;
					bean.setProjectName(String.valueOf(projectName));
				}
				
				// 费用申请单，处理费用项目和预算项目
				if(XCEXConstants.EX_DOC_EX01.equals(docCatCode)){
					// 费用项目
					if(!jo.has("EX_ITEM_ID")){
						//throw new Exception("费用项目不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PROJECT_IS_NOT_NULL", null));
					}else{
						Object exItemId = jo.get("EX_ITEM_ID") ;
						if(exItemId != null && !"".equals(exItemId)){
							bean.setExItemId(String.valueOf(exItemId));
						}else{
							//throw new Exception("费用项目不能为空") ;
							throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PROJECT_IS_NOT_NULL", null));
						}
					}
					if(jo.has("EX_ITEM_NAME")){
						Object exItemName = jo.get("EX_ITEM_NAME") ;
						bean.setExItemName(String.valueOf(exItemName));
					}
					// 预算项目
					if(jo.has("BG_ITEM_ID")){
						Object bgItemId = jo.get("BG_ITEM_ID") ;
						bean.setBgItemId(String.valueOf(bgItemId));
					}
					if(jo.has("BG_ITEM_NAME")){
						Object bgItemName = jo.get("BG_ITEM_NAME") ;
						bean.setBgItemName(String.valueOf(bgItemName));
					}
				}

				// 差旅报销单和费用报销单，处理核销金额和费用申请单
				Object cancelAmt = null ;
				if(XCEXConstants.EX_DOC_EX03.equals(docCatCode) 
						|| XCEXConstants.EX_DOC_EX04.equals(docCatCode)){
					if(jo.has("CANCEL_AMT")){
						cancelAmt = jo.get("CANCEL_AMT") ;
					}else{
						//throw new Exception("费用报销单和差旅报销单保存时核销金额不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_CANCLE_AMT_IS_NOT_NULL", null));
					}
					if(jo.has("APPLY_EX_DOC_ID")){
						Object applyDocId = jo.get("APPLY_EX_DOC_ID") ;
						bean.setApplyDocId(String.valueOf(applyDocId));
					}
				}
				
				// 报销金额和核销金额
				if(!jo.has("AMOUNT")){
					//throw new Exception("单据保存时金额不能为空") ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_AMT_IS_NOT_NULL", null));
				}
				Object amount = jo.get("AMOUNT") ;
				switch(String.valueOf(docCatCode)){
				case XCEXConstants.EX_DOC_EX01:
					if(amount != null && !"".equals(amount) && !"null".equals(amount)){
						double amt = Double.parseDouble(String.valueOf(amount)) ;
						if(amt == 0) //throw new Exception("申请金额不能为零") ;
							throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_REQ_AMT_IS_NOT_NULL", null));
						bean.setAmount(amt) ;
					}else{
						//throw new Exception("申请金额不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_REQ_AMT_IS_NOT_NULL", null));
					}
					break;
				case XCEXConstants.EX_DOC_EX02:
					if(amount != null && !"".equals(amount) && !"null".equals(amount)){
						double amt = Double.parseDouble(String.valueOf(amount)) ;
						if(amt == 0) //throw new Exception("借款金额不能为零") ;
							throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEND_AMT_IS_NOT_NULL", null));
						bean.setAmount(amt) ;
					}else{
						//throw new Exception("借款金额不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_LEND_AMT_IS_NOT_NULL", null));
					}
					break ;
				case XCEXConstants.EX_DOC_EX03:
					if(cancelAmt != null && !"".equals(cancelAmt) && !"null".equals(cancelAmt)){
						bean.setCancelAmt(Double.parseDouble(String.valueOf(cancelAmt)));
					}else{
						//throw new Exception("核销金额不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_CANCLE_AMT_IS_NOT_NULL_01", null));
					}
					break ;
				case XCEXConstants.EX_DOC_EX04:
					if(cancelAmt != null && !"".equals(cancelAmt) && !"null".equals(cancelAmt)){
						bean.setCancelAmt(Double.parseDouble(String.valueOf(cancelAmt)));
					}else{
						//throw new Exception("核销金额不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_CANCLE_AMT_IS_NOT_NULL_01", null));
					}
					break ;
				case XCEXConstants.EX_DOC_EX05:
					if(amount != null && !"".equals(amount) && !"null".equals(amount)){
						double amt = Double.parseDouble(String.valueOf(amount)) ;
						if(amt == 0) //throw new Exception("还款金额不能为零") ;
							throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PAY_AMT_IS_NOT_NULL", null));
						bean.setAmount(amt) ;
					}else{
						//throw new Exception("还款金额不能为空") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PAY_AMT_IS_NOT_NULL", null));
					}
					break ;
				default:
					break ;
				}
				
				// 单据编号
				if(jo.has("EX_DOC_CODE")){
					Object docCode = jo.get("EX_DOC_CODE") ;
					bean.setDocCode(String.valueOf(docCode));
				}
				// 单据ID
				
				if(jo.has("EX_DOC_ID")){
					Object docId = jo.get("EX_DOC_ID") ;
					bean.setDocId(String.valueOf(docId));
				}
				// 流程实例
				if(jo.has("INS_CODE")){
					Object insCode = jo.get("INS_CODE") ;
					bean.setInsCode(String.valueOf(insCode));
				}
				// 审批状态
				if(jo.has("AUDIT_STATUS")){
					Object auditStatus = jo.get("AUDIT_STATUS") ;
					String as = String.valueOf(auditStatus) ;
					bean.setAuditStatus(as);
					if("A".equals(as)){
						bean.setAuditStatusDesc("起草");
					}
				}else{
					bean.setAuditStatus("A");
					bean.setAuditStatusDesc("起草");
				}
				
				// 角色模式
				if(jo.has("ROLE_MODE")){
					Object roleMode = jo.get("ROLE_MODE") ;
					bean.setRoleMode(String.valueOf(roleMode));
				}
				
				return bean ; 
				
			}else if("EXDocDtlBean".equals(T.getSimpleName())){ // 单据明细表信息
				EXDocDtlBean bean = new EXDocDtlBean() ;
/*				
				{
					"EX_ITEM_ID":"43000dba-d0e4-4b41-a475-d305056826f9",
					"EX_ITEM_NAME":"差旅费-火车票",
					"BG_ITEM_ID":"2e37150c-05fc-4774-8dd8-3893618572e5",
					"BG_ITEM_NAME":"销售费用-差旅费",
					"EX_DTL_AMT":1000,
					"START_DATE":"2016-06-22 00:00:00.000",
					"END_DATE":"2016-06-23 00:00:00.000",
					"START_POS":"北京",
					"END_POS":"上海",
					"DOC_NUM":1,
					"EX_DTL_DESC":"火车票"
				}				
*/				
				// 单据明细ID
				Object dtlId = null ;
				try {
					if(jo.has("EX_DTL_ID")){
						dtlId = jo.get("EX_DTL_ID");
						bean.setDtlId(String.valueOf(dtlId));
					}
				} catch (Exception e) {
					bean.setDtlId(null);
				}
				// 单据ID
				Object docId = null ;
				try {
					if(jo.has("EX_DOC_ID")){
						docId = jo.get("EX_DOC_ID");
						bean.setDocId(String.valueOf(docId));
					}
				} catch (Exception e) {
					bean.setDocId(null);
				}
				// 费用项目
				try {
					if(!jo.has("EX_ITEM_ID")){
						//throw new Exception("单据明细行中存在费用项目为空的记录，请确认") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_EX_PROJECT_IS_NOT_NULL", null));
					}else{
						Object exItemId = jo.get("EX_ITEM_ID");
						if(exItemId != null && !"".equals(exItemId)){
							bean.setExItemId(String.valueOf(exItemId));
						}else{
							//throw new Exception("单据明细行中存在费用项目为空的记录，请确认") ;
							throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_EX_PROJECT_IS_NOT_NULL", null));
						}
					}
					if(jo.has("EX_ITEM_NAME")){
						Object exItemName = jo.get("EX_ITEM_NAME") ;
						bean.setExItemName(String.valueOf(exItemName));
					}
				} catch (Exception e) {
					bean.setExItemId(null);
					bean.setExItemName(null);
					throw e ;
				}
				// 预算项目
				try {
					if(jo.has("BG_ITEM_ID")){
						Object bgItemId = jo.get("BG_ITEM_ID");
						bean.setBgItemId(String.valueOf(bgItemId));
					}
					if(jo.has("BG_ITEM_NAME")){
						Object bgItemName = jo.get("BG_ITEM_NAME") ;
						bean.setBgItemName(String.valueOf(bgItemName));
					}
				} catch (Exception e) {
					bean.setBgItemId(null);
					bean.setBgItemName(null);
				}
				// 金额
				try {
					if(!jo.has("EX_DTL_AMT")){
						//throw new Exception("单据明细行中存在金额为空的记录，请确认") ;
						throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_AMT_IS_NOT_NULL", null));
					}else{
						Object dtlAmt = jo.get("EX_DTL_AMT");
						if(dtlAmt != null && !"".equals(dtlAmt)){
							double amt = Double.parseDouble(String.valueOf(dtlAmt)) ;
							if(amt == 0) //throw new Exception("单据明细行中存在金额为空的记录，请确认") ; 
								throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_AMT_IS_NOT_NULL", null));
							bean.setDtlAmt(amt);
						}else{
							//throw new Exception("单据明细行中存在金额为空的记录，请确认") ;
							throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_AMT_IS_NOT_NULL", null));
						}
					}
				} catch (Exception e) {
					throw e ;
				}
				// 出发日期
				try {
					if(jo.has("START_DATE")){
						Object startDate = jo.get("START_DATE");
						bean.setStartDate(XCDateUtil.str2Date(String.valueOf(startDate)));
					}
				} catch (Exception e2) {
					bean.setStartDate(null);
				}
				// 到达日期
				try {
					if(jo.has("END_DATE")){
						Object endDate = jo.get("END_DATE");
						bean.setEndDate(XCDateUtil.str2Date(String.valueOf(endDate)));
					}
				} catch (Exception e2) {
					bean.setEndDate(null);
				}
				// 出发地点
				try {
					if(jo.has("START_POS")){
						Object startPos = jo.get("START_POS");
						bean.setStartPos(String.valueOf(startPos));
					}
				} catch (Exception e2) {
					bean.setStartPos(null);
				}
				// 到达地点
				try {
					if(jo.has("END_POS")){
						Object endPos = jo.get("END_POS");
						bean.setEndPos(String.valueOf(endPos));
					}
				} catch (Exception e2) {
					bean.setEndPos(null);
				}
				// 单据数量
				try {
					if(jo.has("DOC_NUM")){
						Object docNum = jo.get("DOC_NUM");
						bean.setDocNum(Integer.parseInt(String.valueOf(docNum)));
					}
				} catch (Exception e1) {
					bean.setDocNum(0);
				}
				// 说明信息
				try {
					if(jo.has("EX_DTL_DESC")){
						Object dtlDesc = jo.get("EX_DTL_DESC");
						bean.setDtlDesc(String.valueOf(dtlDesc));
					}
				} catch (Exception e) {
					bean.setDtlDesc(null);
				}
				
				return bean ;
				
			}else{
				return null ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: delDoc</p> 
	 * <p>Description: 删除费用单据信息 </p> 
	 * @param docId
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#delDocById(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED, propagation=Propagation.REQUIRED, rollbackFor={Exception.class}) 
	public void delDoc(String jsonArray) throws Exception {
		try {
			if(jsonArray == null || "".equals(jsonArray)) //throw new Exception("参数不能为空") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_PARAMETER_IS_NOT_NULL", null));
			try {
				// 参数处理
				JSONArray ja = new JSONArray(jsonArray) ;
				if(ja != null && ja.length() >0){
					List<String> docIds = new ArrayList<String>() ;
					for(int i=0; i<ja.length(); i++){
						JSONObject jo = ja.getJSONObject(i) ;
						String docId = jo.getString("EX_DOC_ID") ;
						docIds.add(docId) ;
					}
					// 执行删除处理
					if(!docIds.isEmpty()){
						exDocBaseDAO.batchDelDoc(docIds);
					}
				}
			} catch (Exception e) {
				//throw new Exception("参数格式不合法："+e.getMessage()+", 正确格式为 :[{'EX_DOC_ID',''},{'EX_DOC_ID',''},...]") ;
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_delDoc_EXCEPTION_01", null));
			}
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: checkBudget</p> 
	 * <p>Description: 校验预算是否足够 </p> 
	 * @param isChkBg
	 * @param exDocId
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#checkBudget(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject checkBudget(String isChkBg, String exDocId) throws Exception {
		JSONObject jo = new JSONObject() ;
		try {
			if("Y".equals(isChkBg)){
				
				// 查询单据信息
				EXDocBean bean = exDocBaseDAO.getEXDocBean(exDocId) ;
				if(bean != null && (XCEXConstants.EX_DOC_EX03.equals(bean.getDocCatCode())
								|| XCEXConstants.EX_DOC_EX04.equals(bean.getDocCatCode()))){
					
					// 费用申请单信息
					String applyDocId = bean.getApplyDocId() ;
					if(applyDocId != null && !"".equals(applyDocId)){
						EXDocBean applyDocBean = exDocBaseDAO.getEXDocBean(applyDocId) ;
						bean.setApplyDocBean(applyDocBean);
					}
					// 单据明细信息
					List<EXDocDtlBean> docDtl = exDocBaseDAO.getDocDtlByBgItemId(exDocId) ;
					bean.setDocDtl(docDtl);
				}
				
				// 执行校验
				try {
					String projectId = bean.getProjectId() ; 	// PA项目
					String bgItemId = bean.getBgItemId() ;		// 预算项目
					String catCode = bean.getDocCatCode() ;		// 单据类型
					
					switch(catCode){
						case XCEXConstants.EX_DOC_EX01 :   // 费用申请单
							if((projectId == null || "".equals(projectId))
									&& (bgItemId == null || "".equals(bgItemId))){
								// 如果未指定PA项目和预算项目则不需要校验预算
								jo.put("flag", "0") ;
							}else{
								jo = this.chkBudgetByDoc(bean) ;
							}
							break;
							
						case XCEXConstants.EX_DOC_EX03 :	// 差旅报销单
							jo = this.chkBudgetByDtl(bean);
							break ;
							
						case XCEXConstants.EX_DOC_EX04 :	// 费用报销单
							jo = this.chkBudgetByDtl(bean);
							break ;
							
						default :
							jo.put("flag", "0") ;
							break ;
					}
				} catch (Exception e){
					//throw new Exception("预算校验失败："+e.getMessage()) ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_AP_INV_CHECK_BUDGET_FAILED", null)+e.getMessage());
				}
				
			}else{
				jo.put("flag", "0") ;
			}
			
		} catch (Exception e) {
			throw e ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: chkBudgetByDoc 
	 * @Description: 按单据检查预算
	 * @param docJo
	 * @return
	 * @throws Exception    设定文件
	 */
	private JSONObject chkBudgetByDoc(EXDocBean bean) throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// PA项目
		String projectId = "A" ;
		if(bean.getProjectId() != null && !"".equals(bean.getProjectId())){
			projectId = bean.getProjectId() ;
		}
		// 预算项目
		String bgItemId = "A" ;
		if(bean.getBgItemId() != null && !"".equals(bean.getBgItemId())){
			bgItemId = bean.getBgItemId() ;
		}
		
		// 执行预算检查
		if("A".equals(projectId) && "A".equals(bgItemId)){
			jo.put("flag", "0") ;
			
		}else{
			net.sf.json.JSONObject resultJo = BudegetDataManageApi.checkBudgetIsValid(bean.getLedgerId(), projectId,bgItemId, 
					XCDateUtil.date2Str(bean.getBizDate()),null, bean.getDeptId(),String.valueOf(bean.getAmount()));
	
			jo.put("flag", resultJo.get("flag")) ;
			jo.put("msg", resultJo.get("msg")) ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: chkBudgetByDtl 
	 * @Description: 按单据明细检查预算
	 * @param docJo
	 * @param lineJson
	 * @throws Exception    设定文件
	 */
	private JSONObject chkBudgetByDtl(EXDocBean bean)throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// 费用申请单信息
		EXDocBean applyDocBean = bean.getApplyDocBean() ;
		
		if(applyDocBean == null){  
			// 费用(差旅)报销单与费用申请单未关联，则无须先释放费用申请单的预算占用数仅按报销单据明细校验预算即可
			jo = this.chkOnlyDocBudget(bean) ;
			
		}else{ 
			// 费用(差旅)报销单与费用申请单已关联
			String projectId = (bean.getProjectId()==null ? "A" : bean.getProjectId()) ;
			String applyProjectId = (applyDocBean.getProjectId()==null ? "A" : applyDocBean.getProjectId()) ;
			
			if(!projectId.equals(applyProjectId)){ 
				// 如果差旅(费用)报销单费用申请单的项目不相同，则无须先释放费用申请单的预算占用数仅按报销单据明细校验预算即可
				jo = this.chkOnlyDocBudget(bean) ;
				
			}else{
				// 如果差旅(费用)报销单与费用申请单的项目相同（即二者均不存在项目或二者均存在项目）
				if("A".equals(projectId)){ 
					// 如果差旅(费用)报销单与费用费用申请单的项目均为空,则须先释放符合条件的费用申请单预算占用数后再执行成本费用预算校验
					if(applyDocBean.getBgItemId() != null && !"".equals(applyDocBean.getBgItemId())){
						jo = this.chkCostBudget(bean) ;
						
					}else{
						// 如果费用申请既无PA项目，也无预算项目；则按差旅(费用)报销单校验预算
						jo = this.chkOnlyDocBudget(bean) ;
					}
				}else{
					// 如果差旅(费用)报销单与费用费用申请单的项目均不为空且相同,则须先释放符合条件的费用申请单预算占用数后再执行项目预算校验
					jo = this.chkPrjBudget(bean) ;
				}
			}
		}
		
		return jo ;
	}
	
	/**
	 * @Title: chkOnlyDocBudget 
	 * @Description: 执行费用报销单、差旅报销单预算校验处理
	 * @param bean
	 * @return
	 * @throws Exception    设定文件
	 */
	private JSONObject chkOnlyDocBudget(EXDocBean bean)throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// 合并单据明细行 
		List<EXDocDtlBean> docDtlBeans  = bean.getDocDtl() ;
		if(docDtlBeans != null && docDtlBeans.size() >0){
			
			int i = 0 ;
			int j = 0 ;
			StringBuffer str = new StringBuffer() ;
			
			// PA项目
			String projectId = "A" ;
			if(bean.getProjectId() != null && !"".equals(bean.getProjectId())){
				projectId = bean.getProjectId() ;
			}
			
			for(EXDocDtlBean dtlBean : docDtlBeans){
				// 预算项目
				String bgItemId = "A" ;
				if(dtlBean.getBgItemId() != null && !"".equals(dtlBean.getBgItemId())){
					bgItemId = dtlBean.getBgItemId() ;
				}
				
				if("A".equals(projectId) && "A".equals(bgItemId)){
					continue ;
					
				}else{
					// 执行预算校验
					net.sf.json.JSONObject resultJo = BudegetDataManageApi.checkBudgetIsValid(bean.getLedgerId(),projectId, bgItemId,
								XCDateUtil.date2Str(bean.getBizDate()), null,bean.getDeptId(),String.valueOf(dtlBean.getDtlAmt()));

					String flag = resultJo.getString("flag") ;
					String msg = resultJo.getString("msg") ;
					
					if("-1".equals(flag)){ // 预算校验不通过
						i++ ;
					}else if("1".equals(flag)){ // 预算校验警告信息
						j++ ;
					}
					str.append(msg).append("<br>") ;
				}
			}
			
			// 处理提示信息
			if(i>0){
				jo.put("flag", "-1") ;
				jo.put("msg", str.toString()) ;
				
			}else{
				if(j>0){
					jo.put("flag", "1") ;
					jo.put("msg", str.toString()) ;
				}else{
					jo.put("flag", "0") ;
				}
			}
			
		}else{
			jo.put("flag", "0") ;
		}
		
		return jo ;
	}
	
	/**
	 * @Title: chkCostBudget 
	 * @Description: 如果差旅(费用)报销单与费用费用申请单的项目均为空且相同,则须先释放符合条件的费用申请单预算占用数后再执行成本费用预算校验
	 * @param bean
	 * @return
	 * @throws Exception    设定文件
	 */
	private JSONObject chkCostBudget(EXDocBean bean)throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// 费用申请单预算占用数
		EXDocBean applyBean = bean.getApplyDocBean() ;
		
		String applyDeptId = applyBean.getDeptId() ;	// 申请单成本中心
		String applyBgItemId = applyBean.getBgItemId() ;
		double applyAmt = applyBean.getAmount() ;	// 申请金额
		
		// 预算项目控制维度 ：1-预算项目，2-预算项目+成本中心
		String ctrlDim = xcexCommonDAO.getCtrlDimByBgItem(bean.getLedgerId(), applyBgItemId) ;
		
		// 差旅（费用）报销单明细信息
		String deptId = bean.getDeptId() ;	 // 报销单成本中心
		List<EXDocDtlBean> docDtls = bean.getDocDtl() ;
		if(docDtls != null && docDtls.size() >0){
			int i = 0 ;
			int j = 0 ;
			StringBuffer str = new StringBuffer() ;
			
			for(EXDocDtlBean dtlBean : docDtls){
				String bgItemId = dtlBean.getBgItemId() ;
				double dtlAmt = dtlBean.getDtlAmt() ;
				// 处理校验金额
				switch(ctrlDim){
				case "1":
					if(applyBgItemId.equals(bgItemId)){
						dtlAmt = new BigDecimal(dtlAmt).subtract(new BigDecimal(applyAmt)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
					}
					break ;
				case "2":
					if(applyBgItemId.equals(bgItemId) && applyDeptId.equals(deptId)){
						dtlAmt = new BigDecimal(dtlAmt).subtract(new BigDecimal(applyAmt)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
					}
					break;
				default:
					break;
				}
				
				// 执行预算校验
				net.sf.json.JSONObject resultJo = BudegetDataManageApi.checkBudgetIsValid(bean.getLedgerId(),"A", bgItemId,
						XCDateUtil.date2Str(bean.getBizDate()), null,deptId,String.valueOf(dtlAmt));
				
				String flag = resultJo.getString("flag") ;
				String msg = resultJo.getString("msg") ;
				
				if("-1".equals(flag)){ // 预算校验不通过
					i++ ;
				}else if("1".equals(flag)){ // 预算校验警告信息
					j++ ;
				}
				str.append(msg).append("<br>") ;
			}
			
			// 处理提示信息
			if(i>0){
				jo.put("flag", "-1") ;
				jo.put("msg", str.toString()) ;
				
			}else{
				if(j>0){
					jo.put("flag", "1") ;
					jo.put("msg", str.toString()) ;
				}else{
					jo.put("flag", "0") ;
				}
			}
			
		}else{
			jo.put("flag", "0") ;
		}

		return jo ;
	}

	/**
	 * @Title: chkPrjBudget 
	 * @Description: 如果差旅(费用)报销单与费用费用申请单的项目均不为空且相同,则须先释放符合条件的费用申请单预算占用数后再执行项目预算校验
	 * @param bean
	 * @return
	 * @throws Exception    设定文件
	 */
	private JSONObject chkPrjBudget(EXDocBean bean)throws Exception {
		JSONObject jo = new JSONObject() ;
		
		// 费用申请单预算占用数
		EXDocBean applyBean = bean.getApplyDocBean() ;
		
		String applyProjectId = applyBean.getProjectId() ;
		String applyDeptId = applyBean.getDeptId() ;	// 申请单成本中心
		String applyBgItemId = applyBean.getBgItemId() ;
		double applyAmt = applyBean.getAmount() ;	// 申请金额
		
		// PA项目控制维度 ：1-项目，2-项目+预算项目，3-项目+预算项目+成本中心
		String ctrlDim = xcexCommonDAO.getCtrlDimByProject(bean.getLedgerId(), applyProjectId) ;
		
		
		// 差旅（费用）报销单明细信息
		String projectId = bean.getProjectId() ; // 报销单项目
		String deptId = bean.getDeptId() ;	 // 报销单成本中心
		
		if("1".equals(ctrlDim)){
			
			double dtlAmt = new BigDecimal(bean.getAmount()).subtract(new BigDecimal(applyAmt)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
			
			// 执行预算校验
			net.sf.json.JSONObject resultJo = BudegetDataManageApi.checkBudgetIsValid(bean.getLedgerId(),projectId, "A",
					XCDateUtil.date2Str(bean.getBizDate()), null,deptId,String.valueOf(dtlAmt));
			
			String flag = resultJo.getString("flag") ;
			String msg = resultJo.getString("msg") ;
			
			jo.put("flag", flag) ;
			jo.put("msg", msg) ;
			
		}else{
			
			List<EXDocDtlBean> docDtls = bean.getDocDtl() ;
			if(docDtls != null && docDtls.size() >0){
				int i = 0 ;
				int j = 0 ;
				StringBuffer str = new StringBuffer() ;
				
				for(EXDocDtlBean dtlBean : docDtls){
					String bgItemId = dtlBean.getBgItemId() ;
					double dtlAmt = dtlBean.getDtlAmt() ;
					// 处理校验金额
					switch(ctrlDim){
					case "2":
						if(projectId.equals(projectId) && applyBgItemId.equals(bgItemId)){
							dtlAmt = new BigDecimal(dtlAmt).subtract(new BigDecimal(applyAmt)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
						}
						break;
					case "3":
						if(projectId.equals(projectId) && applyBgItemId.equals(bgItemId) && deptId.equals(applyDeptId)){
							dtlAmt = new BigDecimal(dtlAmt).subtract(new BigDecimal(applyAmt)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
						}
						break ;
					default:
						break;
					}
					
					// 执行预算校验
					net.sf.json.JSONObject resultJo = BudegetDataManageApi.checkBudgetIsValid(bean.getLedgerId(),projectId, bgItemId,
							XCDateUtil.date2Str(bean.getBizDate()), null,deptId,String.valueOf(dtlAmt));
					
					String flag = resultJo.getString("flag") ;
					String msg = resultJo.getString("msg") ;
					
					if("-1".equals(flag)){ // 预算校验不通过
						i++ ;
					}else if("1".equals(flag)){ // 预算校验警告信息
						j++ ;
					}
					str.append(msg).append("<br>") ;
				}
				
				// 处理提示信息
				if(i>0){
					jo.put("flag", "-1") ;
					jo.put("msg", str.toString()) ;
					
				}else{
					if(j>0){
						jo.put("flag", "1") ;
						jo.put("msg", str.toString()) ;
					}else{
						jo.put("flag", "0") ;
					}
				}
				
			}else{
				jo.put("flag", "0") ;
			}
		}

		return jo ;
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: recordBudgetOccupancyAmount</p> 
	 * <p>Description: 记录预算占用数 </p> 
	 * @param docId
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucherService#recordBudgetOccupancyAmount(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void recordBudgetOccupancyAmount(String docId) throws Exception {
		try {
			// 当前用户ID和日期
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			// 单据基础信息
			EXDocBean docBean = exDocBaseDAO.getEXDocBean(docId) ;
			
			// 单据类型
			String catCode = docBean.getDocCatCode() ;
			
			// 费用报销单和差旅报销单，则根据单据明细处理预算占用数
			List<EXDocDtlBean> dtlBeans = null ;
			if(XCEXConstants.EX_DOC_EX03.equals(catCode) || XCEXConstants.EX_DOC_EX04.equals(catCode)) {
				dtlBeans = exDocBaseDAO.getDocDtlByBgItemId(docId) ;
			}
			// 参数
			List<BgFactBean> list = new ArrayList<BgFactBean>() ;

			// 项目ID
			String projectId = docBean.getProjectId() ;
			// 预算ID
			String bgItemId = docBean.getBgItemId();
			
			switch(catCode){
			case XCEXConstants.EX_DOC_EX01 :	// 费用申请单
				if((projectId != null && !"".equals(projectId)) 
						|| (bgItemId != null && !"".equals(bgItemId))){
					
					BgFactBean bean = new BgFactBean() ;
					
					bean.setFactId(UUID.randomUUID().toString());
					bean.setLedgerId(docBean.getLedgerId());
					bean.setProjectId(projectId);
					bean.setBgItemId(bgItemId);
					bean.setDeptId(docBean.getDeptId()==null ? "A" : docBean.getDeptId());
					bean.setFactDate(docBean.getBizDate());
					bean.setAmount(docBean.getAmount());
					bean.setFactType(XCBGConstants.BG_OCCUR_TYPE); // 预算占用
					bean.setSrcId(docId);
					bean.setSrcTab("XC_EX_DOCS");
					bean.setCreatedBy(userId);
					bean.setCreationDate(cdate);
					bean.setLastUpdateBy(userId);
					bean.setLastUpdateDate(cdate);
					
					list.add(bean) ;
				}
				break ;
			case XCEXConstants.EX_DOC_EX03 :	// 差旅报销单
				list = this.getBgFactBeanList(docBean, dtlBeans) ;
				break ;
			case XCEXConstants.EX_DOC_EX04 :	// 费用报销单
				list = this.getBgFactBeanList(docBean, dtlBeans) ;
				break ;
			default :
				break ;
			}
			
			// 记录预算占用数
			if(!list.isEmpty()){
				BudegetDataManageApi.incrOccupancyBudget(list);
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
	
	/**
	 * @Title: getBgFactBeanList 
	 * @Description: 预算数据处理
	 * @param docBean
	 * @param dtlBeans
	 * @return
	 * @throws Exception    设定文件
	 */
	private List<BgFactBean> getBgFactBeanList(EXDocBean docBean, List<EXDocDtlBean> dtlBeans) throws Exception {
		// 参数
		List<BgFactBean> list = new ArrayList<BgFactBean>() ;
		// 当前用户ID和日期
		String userId = CurrentSessionVar.getUserId() ;
		Date cdate = CurrentUserUtil.getCurrentDate() ;
		
		if(dtlBeans != null && dtlBeans.size() >0){
			// 删除费用申请单金额
			if(docBean.getApplyDocId() != null && !"".equals(docBean.getApplyDocId())){
				BudegetDataManageApi.deleteFactBudget(docBean.getApplyDocId(), XCBGConstants.BG_OCCUR_TYPE);
			}
			
			// 根据单据明细重新计算占用数
			for(EXDocDtlBean dtlBean : dtlBeans){
				String projectId = docBean.getProjectId() ;
				String bgItemId = dtlBean.getBgItemId() ;
				if((projectId != null && !"".equals(projectId)) || (bgItemId != null && !"".equals(bgItemId))){
					
					BgFactBean bean = new BgFactBean() ;
					
					bean.setFactId(UUID.randomUUID().toString());
					bean.setLedgerId(docBean.getLedgerId());
					bean.setProjectId(projectId);
					bean.setBgItemId(bgItemId);
					bean.setDeptId(docBean.getDeptId()==null ? "A" : docBean.getDeptId());
					bean.setFactDate(docBean.getBizDate());
					bean.setAmount(dtlBean.getDtlAmt());
					bean.setFactType(XCBGConstants.BG_OCCUR_TYPE); // 预算占用数增加
					bean.setSrcId(docBean.getDocId());
					bean.setSrcTab("XC_EX_DOCS");
					bean.setCreatedBy(userId);
					bean.setCreationDate(cdate);
					bean.setLastUpdateBy(userId);
					bean.setLastUpdateDate(cdate);
					
					list.add(bean) ;
				}
			}
		}
		
		return list ;
	}

	/*
	 * (非 Javadoc) 
	 * <p>Title: deleteBudgetOccupancyAmount</p> 
	 * <p>Description: 删除预算占用数 </p> 
	 * @param docId
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXVoucherService#recordBudgetOccupancyAmount(java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void deleteBudgetOccupancyAmount(String docId,String catCode,String applyDocId) throws Exception {
		try {
			// 删除预算占用数
			BudegetDataManageApi.deleteFactBudget(docId, XCBGConstants.BG_OCCUR_TYPE);
			
			// 当前用户ID和日期
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			
			// 费用报销单和差旅报销单，插入费用申请占用数
			if((XCEXConstants.EX_DOC_EX03.equals(catCode) || XCEXConstants.EX_DOC_EX04.equals(catCode))
					&& (applyDocId != null && !"".equals(applyDocId) && !"APPLY_DOC_ID".equals(applyDocId))){
				
				EXDocBean applyBean = exDocBaseDAO.getEXDocBean(applyDocId) ;
				
				List<BgFactBean> list = new ArrayList<BgFactBean>() ;
				
				if(applyBean != null && 
						((applyBean.getProjectId() != null && !"".equals(applyBean.getProjectId())) 
								|| applyBean.getBgItemId() != null && !"".equals(applyBean.getBgItemId()))) {
					
					BgFactBean bean = new BgFactBean() ;
					bean.setFactId(UUID.randomUUID().toString());
					bean.setLedgerId(applyBean.getLedgerId());
					bean.setProjectId(applyBean.getProjectId());
					bean.setBgItemId(applyBean.getBgItemId());
					bean.setDeptId(applyBean.getDeptId()==null ? "A" : applyBean.getDeptId());
					bean.setFactDate(applyBean.getBizDate());
					bean.setAmount(applyBean.getAmount());
					bean.setFactType(XCBGConstants.BG_OCCUR_TYPE); // 预算占用数增加
					bean.setSrcId(applyBean.getDocId());
					bean.setSrcTab("XC_EX_DOCS");
					bean.setCreatedBy(userId);
					bean.setCreationDate(cdate);
					bean.setLastUpdateBy(userId);
					bean.setLastUpdateDate(cdate);
					
					list.add(bean) ;
				}
				
				// 记录预算占用数
				if(!list.isEmpty()){
					BudegetDataManageApi.incrOccupancyBudget(list);
				}
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}

	/* (non-Javadoc)
	 * @name     getExItemTree
	 * @author   tangxl
	 * @date     2016年9月29日
	 * @version  1.0
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#getExItemTree(java.util.HashMap)
	 */
	@Override
	public String getExItemTree(HashMap<String, String> map) throws Exception {
		HashMap<String, List<HashMap<String, String>>> result = exDocBaseDAO.getExItemTree(map);
		List<HashMap<String, String>> allList = result.get("archyExItem");
		List<HashMap<String, String>> selList = result.get("selExItem");
		StringBuffer buffer = new StringBuffer();
		if(selList != null && selList.size()>0){
		    //合并要解析的属性数据
			HashMap<String, HashMap<String, String>> mergeMap = new HashMap<String, HashMap<String,String>>();
			for(HashMap<String, String> t:selList){
				XCEXItemTreeUtil.mergeExItemData(allList, t, mergeMap);
			}
			List<HashMap<String, String>> mergeList = new ArrayList<HashMap<String,String>>();
			Iterator<Entry<String, HashMap<String, String>>> it = mergeMap.entrySet().iterator();
			while(it.hasNext()){
				mergeList.add(it.next().getValue());
			}
			//将合并后的数据转换成 树形字符串
			buffer = XCEXItemTreeUtil.parseListToItemTree(mergeList,"-1");
		}
		return buffer.toString();
	}
	
	/*
	 * (非 Javadoc) 
	 * <p>Title: checkBudgetAndUpdateDoc</p> 
	 * <p>Description: 预算校验并更新单据信息 </p> 
	 * @param masterTabJsonStr
	 * @param detailTabJsonArray
	 * @param isChkBg
	 * @param ingoreBgWarning
	 * @return
	 * @throws Exception 
	 * @see com.xzsoft.xc.ex.service.EXDocBaseService#checkBudgetAndUpdateDoc(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.REQUIRED,rollbackFor={Exception.class})
	public void checkBudgetAndUpdateDoc(String masterTabJsonStr, String detailTabJsonArray, String isChkBg, String ingoreBgWarning) throws Exception {
		
		try {
			if(masterTabJsonStr == null || "".equals(masterTabJsonStr)) {
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_IS_NOT_NULL", null));
			}
			// 存储单据信息对象
			EXDocBean masterBean = new EXDocBean() ;
			// 当前用户和当前日期
			String userId = CurrentSessionVar.getUserId() ;
			Date cdate = CurrentUserUtil.getCurrentDate() ;
			// 单据主信息
			JSONObject docJo = null ;
			try {
				docJo = new JSONObject(masterTabJsonStr) ;
			} catch (Exception e) {
				throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_IS_ILLEGAL", null)+e.getMessage());
			}
			// 将单据主表信息转换为JavaBean
			masterBean = (EXDocBean) this.jsonObjectToBean(docJo, EXDocBean.class) ;
			
			// 如果是费用申请单，则需判断其是否被差旅(费用)报销单所引用
			if(XCEXConstants.EX_DOC_EX01.equals(masterBean.getDocCatCode())){
				EXDocBean exDocBean = exDocBaseDAO.getEXDocBeanByApplyDocId(masterBean.getLedgerId(), masterBean.getDocId()) ;
				if(exDocBean != null){
					HashMap<String,String> tokens = new HashMap<String,String>() ; 
					tokens.put("docCode", exDocBean.getDocCode()) ;
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "", tokens)) ;
				}
			}
			
			// 存储按预算项目分组统计后的明细数据
			EXDocBean docBean = new EXDocBean() ;
			BeanUtils.copyProperties(masterBean, docBean);
			List<String> bgItemIds = new ArrayList<String>() ;
			HashMap<String,EXDocDtlBean> bgMap = new HashMap<String,EXDocDtlBean>() ;
			
     		// 记录差旅报销单和费用报销单的单据明细
			if(XCEXConstants.EX_DOC_EX03.equals(masterBean.getDocCatCode()) 
					|| XCEXConstants.EX_DOC_EX04.equals(masterBean.getDocCatCode())){ 
				if(detailTabJsonArray == null || "".equals(detailTabJsonArray)){
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_IS_NOT_NULL", null));
				}
				
				JSONArray lineJa = null ;
				try {
					lineJa = new JSONArray(detailTabJsonArray) ;
				} catch (Exception e) {
					throw new Exception(XipUtil.getMessage(XCUtil.getLang(), "XC_EX_DOC_DTL_IS_ILLEGAL", null)+e.getMessage());
				}
				
				if(lineJa != null && lineJa.length()>0){
					// 存储待新增明细行
					List<EXDocDtlBean> addLines = new ArrayList<EXDocDtlBean>() ;   
					// 存储待更新明细行
					List<EXDocDtlBean> updLines = new ArrayList<EXDocDtlBean>() ; 
					// 存储待删除明细行
					List<String> delLines = new ArrayList<String>() ; 
					// 单据合计金额
					BigDecimal totalAmt = new BigDecimal(0) ;
					
					// 更新单据查询单据现有明细信息
					if(masterBean.getDocId() != null && !"".equals(masterBean.getDocId())){
						List<EXDocDtlBean> existsDocDtl = exDocBaseDAO.getDocDtls(masterBean.getDocId()) ;
						if(existsDocDtl != null && existsDocDtl.size()>0){
							for(EXDocDtlBean bean : existsDocDtl){
								delLines.add(bean.getDtlId()) ;
							}
						}
					}
					
					// 遍历明细行
					for(int i=0; i<lineJa.length(); i++){
						JSONObject lineJo = lineJa.getJSONObject(i) ;
						EXDocDtlBean lineBean = (EXDocDtlBean) this.jsonObjectToBean(lineJo, EXDocDtlBean.class) ;
						
						// 明细金额
						BigDecimal bd = new BigDecimal(lineBean.getDtlAmt()) ;
						totalAmt = totalAmt.add(bd).setScale(2, BigDecimal.ROUND_HALF_UP) ;
						
						// 统计预算校验数据
						if("Y".equals(isChkBg)){
							String bgItemId = lineBean.getBgItemId() ;
							if(bgItemId != null && !"".equals(bgItemId)){
								if(bgItemIds.contains(bgItemId)){
									EXDocDtlBean dtlBean = bgMap.get(bgItemId) ;
									BigDecimal tmpAmt = bd.add(new BigDecimal(dtlBean.getDtlAmt())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
									dtlBean.setDtlAmt(tmpAmt.doubleValue());
									
								}else{
									EXDocDtlBean dtlBean = new EXDocDtlBean() ;
									dtlBean.setBgItemId(bgItemId);
									dtlBean.setDtlAmt(lineBean.getDtlAmt());
									
									bgMap.put(bgItemId, dtlBean) ;
									bgItemIds.add(bgItemId) ;
								}
							}
						}
						
						// 设置最后更新日期和更新人
						lineBean.setCreatedBy(userId);
						lineBean.setCreationDate(cdate);
						lineBean.setLastUpdateDate(cdate);
						lineBean.setLastUpdatedBy(userId);
						
						// 单据明细ID
						String dtlId = lineBean.getDtlId() ;
						if(dtlId != null && !"".equals(dtlId)){
							// 记录更新明细行
							updLines.add(lineBean) ;
							// 记录删除明细行
							delLines.remove(dtlId) ;
							
						}else{
							// 设置单据明细ID和单据ID
							lineBean.setDtlId(UUID.randomUUID().toString());
							lineBean.setDocId(masterBean.getDocId());
							// 记录新增明细行
							addLines.add(lineBean) ;
						}
					}
					
					// 合计金额
					masterBean.setAmount(totalAmt.doubleValue());
					
					// 记录新增明细行
					if(!addLines.isEmpty()){
						masterBean.setDocDtl(addLines);
					}
					// 记录更新明细行
					if(!updLines.isEmpty()){
						masterBean.setUpdLines(updLines);
					}
					// 记录删除明细行
					if(!delLines.isEmpty()){
						masterBean.setDocDtlIds(delLines);
					}
				}
			}
			
			// 设置最后更新日期和更新人
			masterBean.setCreationDate(cdate);
			masterBean.setLastUpdateDate(cdate);
			masterBean.setLastUpdatedBy(userId);
			
			// 删除原单据预算占用数
			if(XCEXConstants.EX_DOC_EX01.equals(masterBean.getDocCatCode()) 
					|| XCEXConstants.EX_DOC_EX03.equals(masterBean.getDocCatCode()) 
					|| XCEXConstants.EX_DOC_EX04.equals(masterBean.getDocCatCode())) {
				
				BudegetDataManageApi.deleteFactBudget(masterBean.getDocId(), XCBGConstants.BG_OCCUR_TYPE);
			}
			
			// 执行预算校验处理
			if("Y".equals(isChkBg) && "N".equals(ingoreBgWarning)
					&& (XCEXConstants.EX_DOC_EX01.equals(masterBean.getDocCatCode()) 
					|| XCEXConstants.EX_DOC_EX03.equals(masterBean.getDocCatCode()) 
					|| XCEXConstants.EX_DOC_EX04.equals(masterBean.getDocCatCode()))){
				
				// 差旅或费用报销单
				if(XCEXConstants.EX_DOC_EX03.equals(masterBean.getDocCatCode())
						|| XCEXConstants.EX_DOC_EX04.equals(masterBean.getDocCatCode()) ){
					
					List<EXDocDtlBean> docDtls = new ArrayList<EXDocDtlBean>() ;
					Set<String> set = bgMap.keySet() ;
					Iterator<String> it = set.iterator() ;
					while(it.hasNext()){
						String key = it.next() ;
						docDtls.add(bgMap.get(key)) ;
					}
					docBean.setDocDtl(docDtls);
				}
				
				// -1 校验不通过或者校验异常；0 校验通过；1校验不通过，但预算不是绝对控制，给出警告提示   
				JSONObject retJo = new JSONObject() ;
				
				switch(docBean.getDocCatCode()){
				case XCEXConstants.EX_DOC_EX01 : 	// 费用申请单
					retJo = this.chkBudgetByDoc(docBean) ;
					break ;
					
				case XCEXConstants.EX_DOC_EX03 : 	// 差旅报销单
					retJo = this.chkOnlyDocBudget(docBean) ;
					break ;
					
				case XCEXConstants.EX_DOC_EX04 :	// 费用报销单
					retJo = this.chkOnlyDocBudget(docBean) ;
					break ;
					
				default :
					break ;
				}
				
				// 校验结果
				String bgFlag = retJo.getString("flag") ;
				HttpSession session = CurrentSessionVar.getSession() ;
				// -1 校验不通过或者校验异常；0 校验通过；1校验不通过，但预算不是绝对控制，给出警告提示
				if("-1".equals(bgFlag)){
					session.setAttribute("BG_CHK_FALG_".concat(session.getId()), "-1");
					throw new Exception(retJo.getString("msg")) ;
					
				}else if("1".equals(bgFlag)){
					session.setAttribute("BG_CHK_FALG_".concat(session.getId()), "1");
					throw new Exception(retJo.getString("msg")) ;
				}
			}
			
			// 执行数据更新处理
			masterBean.setRoleMode("FIN");    // 财务人员操作模式
			exDocBaseDAO.updateDoc(masterBean);
			
			// 记录预算占用数	
			if (XCEXConstants.EX_DOC_EX01.equals(masterBean.getDocCatCode())
					|| XCEXConstants.EX_DOC_EX03.equals(masterBean.getDocCatCode())
					|| XCEXConstants.EX_DOC_EX04.equals(masterBean.getDocCatCode())) {
				
				this.recordBudgetOccupancyAmount(masterBean.getDocId());
			}
			
		} catch (Exception e) {
			throw e ;
		}
	}
}
