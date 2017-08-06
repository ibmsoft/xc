package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ap.modal.ApInvoiceHBean;

/**
 * 
  * @ClassName ApInvoiceBaseMapper
  * @Description 应付模块发票基础信息Mapper
  * @author 任建建
  * @date 2016年6月14日 下午2:16:09
 */
public interface ApInvoiceBaseMapper {
	/**
	 * 
	  * @Title saveInvoice 方法名
	  * @Description 保存采购发票主表信息
	  * @param apInvoiceH
	  * @return void 返回类型
	 */
	public void saveInvoice(ApInvoiceHBean apInvoiceH);
	
	/**
	 * 
	  * @Title saveInvoiceDtl 方法名
	  * @Description 保存采购发票行表信息
	  * @param map
	  * @return void 返回类型
	 */
	public void saveInvoiceDtl(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title updateInvoice 方法名
	  * @Description 更新采购发票主表信息
	  * @param apInvoiceH
	  * @return void 返回类型
	 */
	public void updateInvoice(ApInvoiceHBean apInvoiceH);
	
	/**
	 * 
	  * @Title updateInvoiceDtl 方法名
	  * @Description 更新采购发票行表信息
	  * @param map
	  * @return void 返回类型
	 */
	public void updateInvoiceDtl(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title deleteInvoiceDtls 方法名
	  * @Description 根据采购发票主表ID批量删除采购发票行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteInvoiceDtls(List<String> lists);
	
	/**
	 * 
	  * @Title deleteInvoiceDtl 方法名
	  * @Description 根据采购发票行表ID批量删除采购发票行表信息
	  * @param dtlLists
	  * @return void 返回类型
	 */
	public void deleteInvoiceDtl(List<String> dtlLists);
	
	/**
	 * 
	  * @Title deleteInvoices 方法名
	  * @Description 批量删除采购发票主表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteInvoices(List<String> lists);
	
	/**
	 * 
	  * @Title getInvoicePreCount 方法名
	  * @Description 判断采购发票是否核销预付过
	  * @param apInvHId
	  * @return
	  * @return int 返回类型
	 */
	public int getInvoicePreCount(String apInvHId);
	
	/**
	 * 
	  * @Title getLInvoiceCount 方法名
	  * @Description 判断采购发票是否已经成为另一个红字发票的蓝字发票
	  * @param apInvHId
	  * @return
	  * @return int 返回类型
	 */
	public int getLInvoiceCount(String apInvHId);
	
	/**
	 * 
	  * @Title getInvGlHCount 方法名
	  * @Description 判断采购发票是否已经被应付单引用
	  * @param apInvHId
	  * @return
	  * @return int 返回类型
	 */
	public int getInvGlHCount(String apInvHId);
	
	/**
	 * 
	  * @Title getInvoiceLdProcessInfo 方法名
	  * @Description 查询账簿级采购发票相关流程信息
	  * @param map
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getInvoiceLdProcessInfo(HashMap<String,String> map);
	
	/**
	 * 
	  * @Title getInvoiceProcessInfo 方法名
	  * @Description 查询全局采购发票的相关流程信息
	  * @param apCatCode
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getInvoiceProcessInfo(String apCatCode);
	
	/**
	 * 
	  * @Title signOrCancelApInvoice 方法名
	  * @Description 对采购发票进行签收或者取消签收处理
	  * @param map
	  * @return void 返回类型
	 */
	public void signOrCancelApInvoice(HashMap<String, Object> map);
	
	/**
	 * 
	  * @Title cancelFinApInvoice 方法名
	  * @Description 对采购发票进行取消复核处理
	  * @param map
	  * @return void 返回类型
	 */
	public void cancelFinApInvoice(HashMap<String, Object> map);
}
