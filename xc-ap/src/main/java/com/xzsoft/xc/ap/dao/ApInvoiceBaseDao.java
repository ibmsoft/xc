package com.xzsoft.xc.ap.dao;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ap.modal.ApInvoiceHBean;

/**
 * 
  * @ClassName ApInvoiceBaseDao
  * @Description 应付模块发票基础信息数据层接口
  * @author 任建建
  * @date 2016年6月14日 下午2:17:10
 */
public interface ApInvoiceBaseDao {
	/**
	 * 
	  * @Title saveInvoice 方法名
	  * @Description 保存采购发票信息
	  * @param apInvoiceH
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void saveInvoice(ApInvoiceHBean apInvoiceH) throws Exception;
	
	/**
	 * 
	  * @Title updateInvoice 方法名
	  * @Description 更新采购发票信息
	  * @param apInvoiceH
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateInvoice(ApInvoiceHBean apInvoiceH) throws Exception;
	
	/**
	 * 
	  * @Title deleteInvoiceDtl 方法名
	  * @Description 根据采购发票行表ID批量删除采购发票行表信息
	  * @param dtlLists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteInvoiceDtl(List<String> dtlLists) throws Exception;
	
	/**
	 * 
	  * @Title deleteInvoices 方法名
	  * @Description 批量删除采购发票信息
	  * @param lists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteInvoices(List<String> lists) throws Exception;
	
	/**
	 * 
	  * @Title getInvoicePreCount 方法名
	  * @Description 判断采购发票是否核销预付过
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int getInvoicePreCount(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title getLInvoiceCount 方法名
	  * @Description 判断采购发票是否已经成为另一个红字发票的蓝字发票
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int getLInvoiceCount(String apInvHId) throws Exception;

	/**
	 * 
	  * @Title getInvGlHCount 方法名
	  * @Description 判断采购发票是否已经被应付单引用
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int getInvGlHCount(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title getInvoiceLdProcessInfo 方法名
	  * @Description 查询账簿级采购发票相关流程信息
	  * @param apCatCode
	  * @param ledgerId
	  * @return
	  * @throws Exception
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getInvoiceLdProcessInfo(String apCatCode,String ledgerId) throws Exception;
	
	/**
	 * 
	  * @Title getInvoiceProcessInfo 方法名
	  * @Description 查询采购发票相关流程信息
	  * @param apCatCode
	  * @return
	  * @throws Exception
	  * @return HashMap<String,String> 返回类型
	 */
	public HashMap<String,String> getInvoiceProcessInfo(String apCatCode) throws Exception;
	
	/**
	 * 
	  * @Title signOrCancelApInvoice 方法名
	  * @Description 对采购发票进行签收或者取消签收处理
	  * @param apInvoiceHList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void signOrCancelApInvoice(List<ApInvoiceHBean> apInvoiceHList) throws Exception;
	
	/**
	 * 
	  * @Title cancelFinApInvoice 方法名
	  * @Description 对采购发票进行取消复核处理
	  * @param apInvoiceHList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void cancelFinApInvoice(List<ApInvoiceHBean> apInvoiceHList) throws Exception;
}
