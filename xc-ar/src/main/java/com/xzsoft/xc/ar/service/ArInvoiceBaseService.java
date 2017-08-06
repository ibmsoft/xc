package com.xzsoft.xc.ar.service;

import org.codehaus.jettison.json.JSONObject;

/**
 * 
  * @ClassName ArInvoiceBaseService
  * @Description 应收模块发票基础信息处理类
  * @author 任建建
  * @date 2016年7月6日 下午1:33:27
 */
public interface ArInvoiceBaseService {
	/**
	 * 
	  * @Title getArAttCatCode 方法名
	  * @Description 得到应收模块的附件分类编码
	  * @param arDocCatCode
	  * @param ledgerId
	  * @param language
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getArAttCatCode(String arDocCatCode,String ledgerId,String language) throws Exception;
	/**
	 * 
	  * @Title saveOrUpdateInvoice 方法名
	  * @Description 采购销售发票保存和更新方法
	  * @param mainTabInfoJson
	  * @param rowTabInfoTabJson
	  * @param deleteDtlInfo
	  * @param language
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject saveOrUpdateInvoice(String mainTabInfoJson,String rowTabInfoTabJson,String deleteDtlInfo,String language) throws Exception;
	/**
	 * 
	  * @Title deleteInvoice 方法名
	  * @Description 删除销售发票
	  * @param jsonArray
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteInvoice(String jsonArray,String language) throws Exception;
	/**
	 * 
	  * @Title printArInvoice 方法名
	  * @Description 对销售发票进行开票处理
	  * @param jsonArray
	  * @param opType
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void printArInvoice(String jsonArray,String opType,String language) throws Exception;
	/**
	 * 
	  * @Title getInvoiceProcAndForms 方法名
	  * @Description 得到销售发票模块中的流程相关信息
	  * @param arDocCatCode
	  * @param ledgerId
	  * @param language
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject getInvoiceProcAndForms(String arDocCatCode,String ledgerId,String language) throws Exception;
}