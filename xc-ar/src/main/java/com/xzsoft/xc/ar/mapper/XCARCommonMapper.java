package com.xzsoft.xc.ar.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.apar.modal.ArDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentLBean;
import com.xzsoft.xc.ar.modal.ArCustomerBean;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;
/**
 * 
  * @ClassName XCARCommonMapper
  * @Description 应收模块公共MApper
  * @author 任建建
  * @date 2016年7月6日 上午11:32:56
 */
public interface XCARCommonMapper {
	/**
	 * 
	  * @Title getArCode 方法名
	  * @Description 获得单据编码
	  * @param map
	  * @return
	  * @return String 返回类型
	 */
	public String getArCode(HashMap<String,String> map);
	/**
	 * 
	  * @Title getArInvoiceH 方法名
	  * @Description 通过销售发票ID得到销售发票信息
	  * @param AR_INV_H_ID
	  * @return
	  * @return ArInvoiceHBean 返回类型
	 */
	public ArInvoiceHBean getArInvoiceH(String AR_INV_H_ID);
	/**
	 * 
	  * @Title getArInvGlHByInvId 方法名
	  * @Description 通过销售发票ID查询应收单主表信息
	  * @param AR_INV_H_ID
	  * @return
	  * @return ArDocumentHBean 返回类型
	 */
	public ArDocumentHBean getArInvGlHByInvId(String AR_INV_H_ID);
	/**
	 * 
	  * @Title getArInvGlH 方法名
	  * @Description 通过应收单ID查询应收单信息
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return ArDocumentHBean 返回类型
	 */
	public ArDocumentHBean getArInvGlH(String AR_INV_GL_H_ID);
	/**
	 * 
	  * @Title getArInvGlL 方法名
	  * @Description 通过应收单ID查询应收单行表信息
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return List<ArDocumentLBean> 返回类型
	 */
	public List<ArDocumentLBean> getArInvGlL(String AR_INV_GL_H_ID);
	/**
	 * 
	  * @Title getArPayH 方法名
	  * @Description 通过收款单ID查询收款单信息
	  * @param AR_PAY_H_ID
	  * @return
	  * @return ArDocumentHBean 返回类型
	 */
	public ArDocumentHBean getArPayH(String AR_PAY_H_ID);
	/**
	 * 
	  * @Title getArPayL 方法名
	  * @Description 通过收款单ID查询收款单行表信息
	  * @param AR_PAY_H_ID
	  * @return
	  * @return List<ArDocumentLBean> 返回类型
	 */
	public List<ArDocumentLBean> getArPayL(String AR_PAY_H_ID);
	/**
	 * 
	  * @Title getArCancelH 方法名
	  * @Description 通过核销单ID查询核销单信息
	  * @param AR_CANCEL_H_ID
	  * @return
	  * @return ArDocumentHBean 返回类型
	 */
	public ArDocumentHBean getArCancelH(String AR_CANCEL_H_ID);
	/**
	 * 
	  * @Title getArCancelL 方法名
	  * @Description 通过核销单ID查询核销单行表信息
	  * @param AR_CANCEL_H_ID
	  * @return
	  * @return List<ArDocumentLBean> 返回类型
	 */
	public List<ArDocumentLBean> getArCancelL(String AR_CANCEL_H_ID);
	/**
	 * 
	  * @Title getArInvGlAdj 方法名
	  * @Description 通过余额调整单ID查询余额调整单表信息
	  * @param GL_ADJ_ID
	  * @return
	  * @return ArDocumentHBean 返回类型
	 */
	public ArDocumentHBean getArInvGlAdj(String GL_ADJ_ID);
	
	/**
	 * 
	  * @Title getAccId 方法名
	  * @Description 通过科目组合ID得到科目ID
	  * @param ccId
	  * @return
	  * @return String 返回类型
	 */
	public String getAccId(String ccId);

	/**
	 * 
	  * @Title judgmentInvAmount 方法名
	  * @Description 红字发票需要判断应收单的未付金额是否还够冲销当前红色发票
	  * @param map
	  * @return
	  * @return String 返回类型
	 */
	public String judgmentInvAmount(HashMap<String,Object> map);
	/**
	 * 
	  * @Title judgmentInvoiceAmount 方法名
	  * @Description 红字发票需要判断应付单的未付金额是否还够冲销当前红色发票
	  * @param map
	  * @return
	  * @return String 返回类型
	 */
	public String judgmentInvoiceAmount(HashMap<String,Object> map);
	/**
	 * 
	  * @Title getArCustomer 方法名
	  * @Description 通过客户ID查询客户信息
	  * @param CUSTOMER_ID
	  * @return
	  * @return ArCustomerBean 返回类型
	 */
	public ArCustomerBean getArCustomer(String CUSTOMER_ID);
}
