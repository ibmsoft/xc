package com.xzsoft.xc.ap.dao;

import java.util.List;

import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
/**
 * 
  * @ClassName ApVoucherDao
  * @Description 应付模块凭证相关处理Dao层
  * @author 任建建
  * @date 2016年4月6日 下午8:41:29
 */
public interface ApVoucherDao {
	/**
	 * 
	  * @Title saveNewApVoucher 方法名
	  * @Description 凭证保存更新应付应付模块单据的凭证信息
	  * @param apVoucherHandler
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void saveNewApVoucher(ApVoucherHandlerBean apVoucherHandler) throws Exception;
	/**
	 * 
	  * @Title saveCheckApVoucher 方法名
	  * @Description 凭证（审核：起草状态--提交状态--审核通过状态，取消审核：审核通过--提交--起草）时更新应付模块单据的凭证信息
	  * @param avhbList
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void saveCheckApVoucher(List<ApVoucherHandlerBean> avhbList) throws Exception;
	/**
	 * 
	  * @Title saveSignApVoucher 方法名
	  * @Description 凭证签字（取消签字）时更新应付模块单据的凭证信息
	  * @param avhbList
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void saveSignApVoucher(List<ApVoucherHandlerBean> avhbList) throws Exception;
}
