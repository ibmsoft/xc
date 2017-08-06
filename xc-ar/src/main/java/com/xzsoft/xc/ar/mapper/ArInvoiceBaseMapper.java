package com.xzsoft.xc.ar.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ar.modal.ArInvoiceHBean;

/**
 * 
  * @ClassName ArInvoiceBaseMapper
  * @Description 应收模块发票基础信息Mapper
  * @author 任建建
  * @date 2016年7月6日 下午1:12:07
 */
public interface ArInvoiceBaseMapper {
	/**
	 * 
	  * @Title saveArInvoice 方法名
	  * @Description 保存销售发票主表信息
	  * @param arInvoiceH
	  * @return void 返回类型
	 */
	public void saveArInvoice(ArInvoiceHBean arInvoiceH);
	
	/**
	 * 
	  * @Title saveArInvoiceDtl 方法名
	  * @Description 保存销售发票行表信息
	  * @param map
	  * @return void 返回类型
	 */
	public void saveArInvoiceDtl(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title updateArInvoice 方法名
	  * @Description 更新销售发票主表信息
	  * @param arInvoiceH
	  * @return void 返回类型
	 */
	public void updateArInvoice(ArInvoiceHBean arInvoiceH);
	
	/**
	 * 
	  * @Title updateArInvoiceDtl 方法名
	  * @Description 更新销售发票行表信息
	  * @param map
	  * @return void 返回类型
	 */
	public void updateArInvoiceDtl(HashMap<String,Object> map);
	
	/**
	 * 
	  * @Title deleteArInvoiceDtls 方法名
	  * @Description 根据销售发票主表ID批量删除销售发票行表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteArInvoiceDtls(List<String> lists);
	
	/**
	 * 
	  * @Title deleteArInvoiceDtl 方法名
	  * @Description 根据销售发票行表ID批量删除销售发票行表信息
	  * @param dtlLists
	  * @return void 返回类型
	 */
	public void deleteArInvoiceDtl(List<String> dtlLists);
	
	/**
	 * 
	  * @Title deleteArInvoices 方法名
	  * @Description 批量删除销售发票主表信息
	  * @param lists
	  * @return void 返回类型
	 */
	public void deleteArInvoices(List<String> lists);
	
	/**
	 * 
	  * @Title printArInvoice 方法名
	  * @Description 对销售发票开票处理
	  * @param map
	  * @return void 返回类型
	 */
	public void printArInvoice(HashMap<String, Object> map);
	
	/**
	 * 
	  * @Title finArInvoice 方法名
	  * @Description 对销售发票进行复核（取消复核）处理
	  * @param map
	  * @return void 返回类型
	 */
	public void finArInvoice(HashMap<String, Object> map);
	
	/**
	 * 
	  * @Title getInvoicePreCount 方法名
	  * @Description 判断销售发票是否核销预付过
	  * @param arInvHId
	  * @return
	  * @return int 返回类型
	 */
	public int getInvoicePreCount(String arInvHId);
	
	/**
	 * 
	  * @Title getLInvoiceCount 方法名
	  * @Description 判断销售发票是否已经成为另一个红字发票的蓝字发票
	  * @param arInvHId
	  * @return
	  * @return int 返回类型
	 */
	public int getLInvoiceCount(String arInvHId);
	
	/**
	 * 
	  * @Title getInvGlHCount 方法名
	  * @Description 判断销售发票是否已经被应付单引用
	  * @param arInvHId
	  * @return
	  * @return int 返回类型
	 */
	public int getInvGlHCount(String arInvHId);
	
	/**
	 * 
	  * @Title getInvoiceLdProcessInfo 方法名
	  * @Description 查询账簿级销售发票相关流程信息
	  * @param map
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getInvoiceLdProcessInfo(HashMap<String,String> map);
	
	/**
	 * 
	  * @Title getInvoiceProcessInfo 方法名
	  * @Description 查询全局销售发票的相关流程信息
	  * @param arCatCode
	  * @return
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getInvoiceProcessInfo(String arCatCode);
}
