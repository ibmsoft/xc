package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;

import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;

/**
 * 
  * @ClassName APVoucherMapper
  * @Description 应付模块凭证相关处理
  * @author 任建建
  * @date 2016年4月5日 下午7:06:00
 */
public interface APVoucherMapper {
	/**
	 * 
	  * @Title saveNewApVoucher 方法名
	  * @Description 凭证保存更新应付应付模块单据的凭证信息
	  * @param apVoucherHandler 设定文件
	  * @return void 返回类型
	 */
	public void saveNewApVoucher(ApVoucherHandlerBean apVoucherHandler);
	/**
	 * 
	  * @Title saveCheckApVoucher 方法名
	  * @Description 凭证审核时更新应付模块单据的凭证信息
	  * @param map 设定文件
	  * @return void 返回类型
	 */
	public void saveCheckApVoucher(HashMap<String, Object> map);
	/**
	 * 
	  * @Title saveSignApVoucher 方法名
	  * @Description 凭证签字时更新应付模块单据的凭证信息
	  * @param map 设定文件
	  * @return void 返回类型
	 */
	public void saveSignApVoucher(HashMap<String, Object> map);
}
