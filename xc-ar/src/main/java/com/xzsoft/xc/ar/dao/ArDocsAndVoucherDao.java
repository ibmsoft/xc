package com.xzsoft.xc.ar.dao;

import java.util.List;

import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;

/**
 * 
  * @ClassName ArDocsAndVoucherDao
  * @Description 应收模块保存单据和生成凭证的Dao层
  * @author 任建建
  * @date 2016年7月8日 下午12:18:58
 */
public interface ArDocsAndVoucherDao {
	
	/**
	 * 
	  * @Title saveNewArVoucher 方法名
	  * @Description 凭证保存更新应付应付模块单据的凭证信息
	  * @param arVoucherHandler
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void saveNewArVoucher(ArVoucherHandlerBean arVoucherHandler) throws Exception;
	/**
	 * 
	  * @Title saveCheckArVoucher 方法名
	  * @Description 凭证（审核：起草状态--提交状态--审核通过状态，取消审核：审核通过--提交--起草）时更新应付模块单据的凭证信息
	  * @param avhbList
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void saveCheckArVoucher(List<ArVoucherHandlerBean> avhbList) throws Exception;
	/**
	 * 
	  * @Title saveSignArVoucher 方法名
	  * @Description 凭证签字（取消签字）时更新应付模块单据的凭证信息
	  * @param avhbList
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void saveSignArVoucher(List<ArVoucherHandlerBean> avhbList) throws Exception;
	//===================应收单模块
	/**
	 * 
	  * @Title saveArInvGl 方法名
	  * @Description 保存应收单信息
	  * @param arDocumentH
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void saveArInvGl(ArDocumentHBean arDocumentH) throws Exception;
	
	/**
	 * 
	  * @Title updateArInvGl 方法名
	  * @Description 更新应收单信息
	  * @param arDocumentH
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void updateArInvGl(ArDocumentHBean arDocumentH) throws Exception;
	
	/**
	 * 
	  * @Title deleteArInvGlDtl 方法名
	  * @Description 根据应付单行表ID批量删除应付单行表信息
	  * @param dtlLists
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void deleteArInvGlDtl(List<String> dtlLists) throws Exception;
	
	/**
	 * 
	  * @Title deleteArInvGlH 方法名
	  * @Description 批量删除应付单
	  * @param lists
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void deleteArInvGlH(List<String> lists) throws Exception;
	//===================应收单模块
	
	/**
	 * 
	  * @Title deleteArDoc 方法名
	  * @Description 删除单据明细表和单据表
	  * @param arVoucherHandler
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void deleteArDoc(ArVoucherHandlerBean arVoucherHandler) throws Exception;
}
