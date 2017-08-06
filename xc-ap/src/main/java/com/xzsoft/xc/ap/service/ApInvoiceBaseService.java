package com.xzsoft.xc.ap.service;

import org.codehaus.jettison.json.JSONObject;

/**
 * 
  * @ClassName ApInvoiceBaseService
  * @Description 应付模块发票基础信息处理类
  * @author 任建建
  * @date 2016年6月14日 下午2:19:22
 */
public interface ApInvoiceBaseService {
	/**
	 * 
	  * @Title getApAttCatCode 方法名
	  * @Description 得到应付模块的附件分类编码
	  * @param apDocCatCode		单据类型
	  * @param ledgerId			账簿ID
	  * @param language			当前语言
	  * @return
	  * @throws Exception 设定文件
	  * @return String 返回类型
	 */
	public String getApAttCatCode(String apDocCatCode,String ledgerId,String language) throws Exception;
	/**
	 * 
	  * @Title saveOrUpdateInvoice 方法名
	  * @Description 采购发票保存和更新方法
	  * @param mainTabInfoJson
	  * @param rowTabInfoTabJson
	  * @param deleteDtlInfo
	  * @param language			当前语言
	  * @return
	  * @throws Exception 设定文件
	  * @return JSONObject 返回类型
	 */
	public JSONObject saveOrUpdateInvoice(String mainTabInfoJson,String rowTabInfoTabJson,String deleteDtlInfo,String language) throws Exception;
	/**
	 * 
	  * @Title deleteInvoice 方法名
	  * @Description 删除采购发票
	  * @param jsonArray
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void deleteInvoice(String jsonArray,String language) throws Exception;
	/**
	 * 
	  * @Title getInvoiceProcAndForms 方法名
	  * @Description 得到采购发票模块中的流程相关信息
	  * @param apDocCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception 设定文件
	  * @return JSONObject 返回类型
	 */
	public JSONObject getInvoiceProcAndForms(String apDocCatCode,String ledgerId) throws Exception;
	/**
	 * 
	  * @Title signOrCancelApInvoice 方法名
	  * @Description 对采购发票签收或者取消签收处理
	  * @param jsonArray			参数
	  * @param opType			操作类型：1--未签收进行签收处理、2--已经签收进行取消签收处理、3--对采购发票进行拒签处理
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void signOrCancelApInvoice(String jsonArray,String opType,String language) throws Exception;
}
