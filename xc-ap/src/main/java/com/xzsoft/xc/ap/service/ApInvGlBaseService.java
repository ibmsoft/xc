package com.xzsoft.xc.ap.service;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
/**
 * 
  * @ClassName ApInvGlBaseService
  * @Description 应付模块保存应付单和生成凭证的service方法
  * @author RenJianJian
  * @date 2016年8月8日 下午55204
 */
public interface ApInvGlBaseService {
	/**
	 * 
	  * @Title saveApInvGlAndVoucher 方法名
	  * @Description 应付单保存的方法
	  * @param mainTabInfoJson	单据主信息
	  * @param rowTabInfoJson	单据行信息
	  * @param deleteDtlInfo	是否有单据行信息删除
	  * @param language			当前语言
	  * @return
	  * @throws Exception 设定文件
	  * @return JSONObject 返回类型
	 */
	public JSONObject saveApInvGlAndVoucher(String mainTabInfoJson,String rowTabInfoJson,String deleteDtlInfo,String language) throws Exception;
	/**
	 * 
	  * @Title cancelFinApInvGl 方法名
	  * @Description 对应付单进行取消复核处理
	  * @param ja
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void cancelFinApInvGl(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title deleteApDoc 方法名
	  * @Description 删除应付单
	  * @param ja
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void deleteApDoc(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title checkBudget 方法名
	  * @Description 应付模块检查预算是否足够（采购发票、应付单、付款单-其他付款）
	  * 				isChkBg-预算检查：Y-是，N-否、
	  * 				apDocId-应付模块ID（采购发票ID、应付单ID、付款单ID）、
	  * 				apDocCat-单据类型（采购发票：CGFP、应付单：YFD、付款单：FKD）
	  * @param ja
	  * @param language			当前语言
	  * @return
	  * @throws Exception 设定文件
	  * @return JSONObject 返回类型
	 */
	public JSONObject checkBudget(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title recordBudgetOccupancyAmount 方法名
	  * @Description 记录预算占用数
	  * @param apDocId
	  * @param apDocCat
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void recordBudgetOccupancyAmount(String apDocId,String apDocCat) throws Exception;
	/**
	 * 
	  * @Title deleteBudgetOccupancyAmount 方法名
	  * @Description 删除预算占用数
	  * @param apDocId
	  * @param apDocCat
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void deleteBudgetOccupancyAmount(String apDocId,String apDocCat) throws Exception;
}
