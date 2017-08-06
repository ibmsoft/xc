package com.xzsoft.xc.ap.service;

import org.codehaus.jettison.json.JSONArray;

import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;

/**
 * 
  * @ClassName ApVoucherService
  * @Description 应付模块凭证处理
  * @author 任建建
  * @date 2016年4月5日 下午8:09:03
 */
public interface ApVoucherService {
	/**
	 * 
	  * @Title newVoucher 方法名
	  * @Description 生成新的凭证
	  * @param avhb			生成凭证需要的实体类
	  * @param language		当前语言
	  * @return
	  * @throws Exception 设定文件
	  * @return String 返回类型
	 */
	public String newVoucher(ApVoucherHandlerBean avhb,String language) throws Exception;
	/**
	 * 
	  * @Title checkVoucher 方法名
	  * @Description 凭证审核处理
	  * @param ja
	  * @param language		当前语言
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void checkVoucher(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title cancelCheckVoucher 方法名
	  * @Description 取消凭证审核
	  * @param ja
	  * @param language		当前语言
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void cancelCheckVoucher(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title signVoucher 方法名
	  * @Description 签字
	  * @param ja
	  * @param language		当前语言
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void signVoucher(JSONArray ja,String language) throws Exception;
	/**
	 * 
	  * @Title cancelSignVoucher 方法名
	  * @Description 取消签字
	  * @param ja
	  * @param language
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void cancelSignVoucher(JSONArray ja,String language) throws Exception;
}
