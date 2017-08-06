package com.xzsoft.xc.po.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.stereotype.Service;

import com.xzsoft.xc.po.mapper.PoCommonMapper;
import com.xzsoft.xc.po.service.PoCommonService;
import com.xzsoft.xc.util.common.XCUtil;
import com.xzsoft.xip.platform.util.XipUtil;

/** 
 * @ClassName: PoCommonServiceImpl
 * @Description: 采购管理公共Service实现类 
 * @author weicw
 * @date 2016年12月12日 上午9:41:36 
 * 
 */
@Service("poCommonService")
public class PoCommonServiceImpl implements PoCommonService {
	@Resource
	PoCommonMapper mapper;
	//记录日志
Logger log = Logger.getLogger(PoCommonServiceImpl.class.getName());
/**
 * @Title:getProcAndFormsByCat
 * @Description:获取账簿费用单据信息
 * 参数格式:
 * @param  java.lang.String
 * @return 
 * @throws 
 */
@Override
public JSONObject getProcAndFormsByCat(String ledgerId,String docCat) throws Exception{
	JSONObject jo = new JSONObject() ;
	String lang = XCUtil.getLang();//语言值
	try {
		// 获取账簿级单据类型信息
		
		/* A.LEDGER_ID    ,
	       A.EX_CAT_CODE  ,
	       A.PROCESS_ID   ,
	       B.PROCESS_CODE ,
	       A.RULE_CODE*/
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("ledgerId", ledgerId);
		map.put("docCat", docCat);
		HashMap<String,String> ldCatMap = mapper.getLdCatInfo(map) ;
		String processId = ldCatMap.get("PROCESS_ID") ;
		String processCode = ldCatMap.get("PROCESS_CODE") ;
		String ruleCode = ldCatMap.get("RULE_CODE") ;
		String attCatCode = ldCatMap.get("ATT_CAT_CODE");
		
		jo.put("PROCESS_ID", processId) ;//审批流程ID
		jo.put("PROCESS_CODE", processCode) ;//审批流程编码
		jo.put("RULE_CODE", ruleCode) ;//规则编码
		jo.put("ATT_CAT_CODE", attCatCode);//附件分类编码
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
		HashMap<String,String> gbCatMap = mapper.getGbCatInfo(docCat) ;
		if(processId == null || "".equals(processId)){
			String gbProcId = gbCatMap.get("PROCESS_ID") ;
			String gbProcCode = gbCatMap.get("PROCESS_CODE") ;
			if(gbProcId == null || "".equals(gbProcId)){
				throw new Exception("单据类型:"+docCat+"未设置审批流程，请在【单据类型设置或单据类型】功能处设置") ;
			}else{
				jo.put("PROCESS_ID", gbProcId) ;
				jo.put("PROCESS_CODE", gbProcCode) ;
			}
			String gbAttCatCode = gbCatMap.get("ATT_CAT_CODE");//附件分类编码
			if(gbAttCatCode == null || "".equals(gbAttCatCode)){
				throw new Exception("单据类型:"+docCat+"未设置附件分类，请在【单据类型设置或单据类型】功能处设置") ;
			}else{
				jo.put("ATT_CAT_CODE", gbCatMap.get("ATT_CAT_CODE"));		//附件分类编码
			}
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
		jo.put("flag", "0");
		} catch (Exception e) {
			jo.put("flag", "1");
			log.error(e.getMessage());
			throw e ;
		}
		
		return jo ;
	}
	

}
