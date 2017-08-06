package com.xzsoft.xc.ar.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.ar.modal.ArVoucherHandlerBean;
/**
 * 
  * @ClassName ArDocsAndVoucherMapper
  * @Description 应收模块保存单据和生成凭证的Mapper
  * @author 任建建
  * @date 2016年7月8日 下午12:15:10
 */
public interface ArDocsAndVoucherMapper {
	
	/**
	 * 
	  * @Title saveNewArVoucher 方法名
	  * @Description 凭证保存更新应付应付模块单据的凭证信息
	  * @param arVoucherHandler 设定文件
	  * @return void 返回类型
	 */
	public void saveNewArVoucher(ArVoucherHandlerBean arVoucherHandler);
	/**
	 * 
	  * @Title saveCheckArVoucher 方法名
	  * @Description 凭证审核时更新应付模块单据的凭证信息
	  * @param map 设定文件
	  * @return void 返回类型
	 */
	public void saveCheckArVoucher(HashMap<String, Object> map);
	/**
	 * 
	  * @Title saveSignArVoucher 方法名
	  * @Description 凭证签字时更新应付模块单据的凭证信息
	  * @param map 设定文件
	  * @return void 返回类型
	 */
	public void saveSignArVoucher(HashMap<String, Object> map);
	//===================应收单模块
	/**
	 * 
	  * @Title saveArInvGlH 方法名
	  * @Description 保存应收单主信息
	  * @param arDocumentH
	  * @return void 返回类型
	 */
	public void saveArInvGlH(ArDocumentHBean arDocumentH);
	
	/**
	 * 
	  * @Title saveArInvGlDtl 方法名
	  * @Description 保存应收单行信息
	  * @param map
	  * @return void 返回类型
	 */
	public void saveArInvGlDtl(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title updateArInvGlH 方法名
	  * @Description 更新应收单主信息
	  * @param arDocumentH
	  * @return void 返回类型
	 */
	public void updateArInvGlH(ArDocumentHBean arDocumentH);
	
	/**
	 * 
	  * @Title updateArInvGlDtl 方法名
	  * @Description 更新应收单行信息
	  * @param map
	  * @return void 返回类型
	 */
	public void updateArInvGlDtl(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title deleteArInvGlHDtls 方法名
	  * @Description 根据应付单主表ID批量删除应付单行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteArInvGlHDtls(List<String> lists);
	
	/**
	 * 
	  * @Title deleteArInvGlDtl 方法名
	  * @Description 根据应付单行表ID批量删除应付单行表信息
	  * @param dtlLists
	  * @return void 返回类型
	 */
	public void deleteArInvGlDtl(List<String> dtlLists);
	
	/**
	 * 
	  * @Title deleteArInvGlH 方法名
	  * @Description 批量删除应付单主表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteArInvGlH(List<String> lists);
	//===================应收单模块
	
	/**
	 * 
	  * @Title deleteArDoc 方法名
	  * @Description 删除单据明细表和单据表
	  * @param arId 设定文件
	  * @return void 返回类型
	 */
	public void deleteArDoc(ArVoucherHandlerBean arVoucherHandler);
}
