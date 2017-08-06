package com.xzsoft.xc.ex.service;

import java.util.HashMap;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 * @ClassName: EXDocService 
 * @Description: 费用报销单据基础信息处理类
 * @author linp
 * @date 2016年5月24日 下午4:16:46 
 *
 */
public interface EXDocBaseService {

	/**
	 * @Title: getProcAndFormsByCat 
	 * @Description: 根据费用单据类型获取审批流程和审批表单信息
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject getProcAndFormsByCat(String ledgerId,String docCat) throws Exception ;
	
	/**
	 * @Title: getDocsAndUrlsByLedger 
	 * @Description: 获取账簿启用的单据类型及填报地址信息
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONArray getDocsAndUrlsByLedger(String ledgerId) throws Exception ;
	
	/**
	 * @Title: getDocPrintUrlByCat 
	 * @Description: 获取单据类型指定的打印表单信息
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject getDocPrintUrlByCat(String docCat) throws Exception ;
	
	/**
	 * @Title: getAttCatCode 
	 * @Description: 获取附件分类码
	 * @param ledgerId
	 * @param docCat
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getAttCatCode(String ledgerId, String docCat) throws Exception ; 
	
	/**
	 * @Title: saveOrupdateDoc 
	 * @Description: 保存或更新费用单据信息
	 * @param masterTabJsonStr 	单据主表信息JSON格式
	 * @param detailTabJsonArray	单据行信息JSON数组格式
	 * @return
	 * @throws Exception    设定文件
	 */
	public JSONObject saveOrupdateDoc(String masterTabJsonStr, String detailTabJsonArray) throws Exception ;
	
	/**
	 * @Title: delDoc 
	 * @Description: 删除费用单据信息
	 * @param jsonArray 单据ID信息, JSON数组格式： [{'EX_DOC_ID',''},{'EX_DOC_ID',''},....]
	 * @throws Exception    设定文件
	 */
	public void delDoc(String jsonArray) throws Exception ;
	
	/**
	 * @Title: checkBudget 
	 * @Description: 校验预算是否足够
	 * @param isChkBg
	 * @param exDocId
	 * @throws Exception    设定文件
	 */
	public JSONObject checkBudget(String isChkBg, String exDocId) throws Exception ;
	
	/**
	 * @Title: recordBudgetFact 
	 * @Description: 记录预算占用数
	 * @param docId
	 * @throws Exception    设定文件
	 */
	public void recordBudgetOccupancyAmount(String docId) throws Exception ;
	
	/**
	 * @Title: deleteBudgetOccupancyAmount 
	 * @Description: 删除预算占用数
	 * @param docId
	 * @param catCode
	 * @throws Exception    设定文件
	 */
	public void deleteBudgetOccupancyAmount(String docId,String catCode,String applyDocId) throws Exception ;
	/**
	 * 
	 * @methodName  getExItemTree
	 * @author      tangxl
	 * @date        2016年9月29日
	 * @describe    获取费用报销项目树
	 * @param 		map
	 * @return
	 * @throws 		Exception
	 * @变动说明       
	 * @version     1.0
	 */
	public String getExItemTree(HashMap<String, String> map) throws Exception;
	
	/**
	 * @Title: checkBudgetAndUpdateDoc 
	 * @Description: 预算校验并更新单据信息
	 * @param masterTabJsonStr
	 * @param detailTabJsonArray
	 * @param isChkBg
	 * @param ingoreBgWarning
	 * @return
	 * @throws Exception    设定文件
	 */
	public void checkBudgetAndUpdateDoc(String masterTabJsonStr, String detailTabJsonArray, String isChkBg, String ingoreBgWarning) throws Exception ;
	
}
