package com.xzsoft.xc.ar.service;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;
/**
 * 
  * @ClassName ArDocsAndVoucherService
  * @Description 应收模块保存单据和生成凭证的业务处理层
  * @author 任建建
  * @date 2016年7月8日 上午11:10:18
 */
public interface ArDocsAndVoucherService {
	/**
	 * 
	  * @Title createDocsAndVoucher 方法名
	  * @Description 保存单据和生成凭证的方法
	  * @param mainTabInfoJson	单据主信息
	  * @param rowTabInfoJson	单据行信息
	  * @param deleteDtlInfo	是否有单据行信息删除
	  * @param language
	  * @return
	  * @throws Exception
	  * @return JSONObject 返回类型
	 */
	public JSONObject createDocsAndVoucher(String mainTabInfoJson,String rowTabInfoJson,String deleteDtlInfo,String language) throws Exception;
	/**
	 * 
	  * @Title newArVoucher 方法名
	  * @Description 应收模块生成凭证的方法
	  * 参数：ledgerId: 账簿ID,
		    'apCatCode': 单据大类：'YSD',
		    'accDate': 会计日期：年月日,
		    'arId': 主键ID,
		    attchTotal: 附件张数
	  * @param avhb
	  * @param language
	  * @return
	  * @throws Exception 设定文件
	  * @return String 返回类型
	 */
	public String newArVoucher(ArVoucherHandlerBean avhb,String language) throws Exception;
	/**
	 * 
	  * @Title checkArVoucher 方法名
	  * @Description 凭证审核处理
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void checkArVoucher(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title cancelCheckArVoucher 方法名
	  * @Description 取消凭证审核
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void cancelCheckArVoucher(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title signArVoucher 方法名
	  * @Description 签字
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void signArVoucher(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title cancelSignArVoucher 方法名
	  * @Description 取消签字
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void cancelSignArVoucher(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title cancelFinArInvGl 方法名
	  * @Description 对应收单进行取消复核处理
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void cancelFinArInvGl(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title deleteArDoc 方法名
	  * @Description 删除单据
	  * @param ja
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteArDoc(JSONArray ja,String language) throws Exception;
}
