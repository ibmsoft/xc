package com.xzsoft.xc.ar.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ar.modal.ArInvoiceHBean;

/**
 * 
  * @ClassName ArInvoiceBaseDao
  * @Description 应收模块发票基础信息数据层接口
  * @author 任建建
  * @date 2016年7月6日 下午1:19:56
 */
public interface ArInvoiceBaseDao {
	/**
	 * 
	  * @Title saveInvoice 方法名
	  * @Description 保存销售发票信息
	  * @param arInvoiceH
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void saveInvoice(ArInvoiceHBean arInvoiceH) throws Exception;
	
	/**
	 * 
	  * @Title updateInvoice 方法名
	  * @Description 更新销售发票信息
	  * @param arInvoiceH
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateInvoice(ArInvoiceHBean arInvoiceH) throws Exception;
	
	/**
	 * 
	  * @Title deleteInvoiceDtl 方法名
	  * @Description 根据销售发票行表ID批量删除销售发票行表信息
	  * @param dtlLists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteInvoiceDtl(List<String> dtlLists) throws Exception;
	
	/**
	 * 
	  * @Title deleteInvoices 方法名
	  * @Description 批量删除销售发票信息
	  * @param lists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteInvoices(List<String> lists) throws Exception;
	
	/**
	 * 
	  * @Title printArInvoice 方法名
	  * @Description 对销售发票进行开票处理
	  * @param arInvoiceHList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void printArInvoice(List<ArInvoiceHBean> arInvoiceHList) throws Exception;
	
	/**
	 * 
	  * @Title finArInvoice 方法名
	  * @Description 对销售发票进行复核（取消复核）处理
	  * @param arInvoiceHList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void finArInvoice(List<ArInvoiceHBean> arInvoiceHList) throws Exception;
	
	/**
	 * 
	  * @Title getInvoicePreCount 方法名
	  * @Description 判断销售发票是否核销预付过
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int getInvoicePreCount(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title getLInvoiceCount 方法名
	  * @Description 判断销售发票是否已经成为另一个红字发票的蓝字发票
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int getLInvoiceCount(String arInvHId) throws Exception;

	/**
	 * 
	  * @Title getInvGlHCount 方法名
	  * @Description 判断销售发票是否已经被应付单引用
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int getInvGlHCount(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title getInvoiceLdProcessInfo 方法名
	  * @Description 查询账簿级销售发票相关流程信息
	  * @param arCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getInvoiceLdProcessInfo(String arCatCode,String ledgerId) throws Exception;
	
	/**
	 * 
	  * @Title getInvoiceProcessInfo 方法名
	  * @Description 查询销售发票相关流程信息
	  * @param arCatCode
	  * @return
	  * @throws Exception
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getInvoiceProcessInfo(String arCatCode) throws Exception;
}