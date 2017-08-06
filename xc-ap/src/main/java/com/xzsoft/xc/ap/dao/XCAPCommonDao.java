package com.xzsoft.xc.ap.dao;

import java.util.List;

import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;

/**
 * 
  * @ClassName XCAPCommonDao
  * @Description 应付模块公共Dao层
  * @author 任建建
  * @date 2016年4月5日 上午9:28:31
 */
public interface XCAPCommonDao {
	/**
	 * 
	  * @Title getApCode 方法名
	  * @Description 获得单据编码
	  * @param ledgerId
	  * @param apDocCatCode
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getApCode(String ledgerId,String apDocCatCode) throws Exception;
	/**
	 * 
	  * @Title getApInvGlH 方法名
	  * @Description 查询应付单主表信息
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApDocumentHBean 返回类型
	 */
	public ApDocumentHBean getApInvGlH(String apInvGlHId) throws Exception;
	/**
	 * 
	  * @Title getApInvGlHByInvId 方法名
	  * @Description 查询应付单主表信息（根据采购发票ID）
	  * @param apInvId
	  * @return
	  * @throws Exception
	  * @return ApDocumentHBean 返回类型
	 */
	public ApDocumentHBean getApInvGlHByInvId(String apInvId) throws Exception;
	/**
	 * 
	  * @Title getApInvGlL 方法名
	  * @Description 查询应付单行表信息
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return List<ApDocumentLBean> 返回类型
	 */
	public List<ApDocumentLBean> getApInvGlL(String apInvGlHId) throws Exception;
	/**
	 * 
	  * @Title getApCancelH 方法名
	  * @Description 查询核销单主表信息
	  * @param apCancelHId
	  * @return
	  * @throws Exception
	  * @return ApDocumentHBean 返回类型
	 */
	public ApDocumentHBean getApCancelH(String apCancelHId) throws Exception;
	/**
	 * 
	  * @Title getApPayH 方法名
	  * @Description 查询付款单主表信息
	  * @param apPayHId
	  * @return
	  * @throws Exception
	  * @return ApDocumentHBean 返回类型
	 */
	public ApDocumentHBean getApPayH(String apPayHId) throws Exception;
	/**
	 * 
	  * @Title getApPayL 方法名
	  * @Description 查询付款单行表信息
	  * @param apPayHId
	  * @return
	  * @throws Exception
	  * @return List<ApDocumentLBean> 返回类型
	 */
	public List<ApDocumentLBean> getApPayL(String apPayHId) throws Exception;
	/**
	 * 
	  * @Title getApInvAdj 方法名
	  * @Description 查询余额调整单信息
	  * @param glAdjId
	  * @return
	  * @throws Exception
	  * @return ApDocumentHBean 返回类型
	 */
	public ApDocumentHBean getApInvAdj(String glAdjId) throws Exception;
	/**
	 * 
	  * @Title getApPayReqH 方法名
	  * @Description 查询付款申请单主表信息
	  * @param apPayReqGId
	  * @return
	  * @throws Exception
	  * @return ApPayReqHBean 返回类型
	 */
	public ApPayReqHBean getApPayReqH(String apPayReqGId) throws Exception;
	/**
	 * 
	  * @Title getApInvoiceH 方法名
	  * @Description 查询采购发票主表信息
	  * @param apInvoiceHId
	  * @return
	  * @throws Exception
	  * @return ApDocumentHBean 返回类型
	 */
	public ApDocumentHBean getApInvoiceH(String apInvoiceHId) throws Exception;
	/**
	 * 
	  * @Title getApInvoiceL 方法名
	  * @Description 查询采购发票行表信息
	  * @param apInvoiceHId
	  * @return
	  * @throws Exception
	  * @return List<ApDocumentLBean> 返回类型
	 */
	public List<ApDocumentLBean> getApInvoiceL(String apInvoiceHId) throws Exception;
	/**
	 * 
	  * @Title getApInvoiceLById 方法名
	  * @Description 查询采购发票行表信息(根据采购发票行表ID)
	  * @param apInvoiceLId
	  * @return
	  * @throws Exception
	  * @return ApDocumentLBean 返回类型
	 */
	public ApDocumentLBean getApInvoiceLById(String apInvoiceLId) throws Exception;
	/**
	 * 
	  * @Title deleteApDoc 方法名
	  * @Description 删除单据表
	  * @param apVoucherHandler
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void deleteApDoc(ApVoucherHandlerBean apVoucherHandler) throws Exception;
	/**
	 * 
	  * @Title getAccId 方法名
	  * @Description 通过科目组合ID得到科目ID
	  * @param ccId
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String getAccId(String ccId) throws Exception;
	/**
	 * 
	  * @Title judgmentInvAmount 方法名
	  * @Description 红字发票需要判断应付单的未付金额是否还够冲销当前红色发票
	  * @param lApInvHId
	  * @param invAmount
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String judgmentInvAmount(String lApInvHId,double invAmount) throws Exception;
	/**
	 * 
	  * @Title judgmentInvoiceAmount 方法名
	  * @Description 红字发票需要判断应付单的未付金额是否还够冲销当前红色发票（通过蓝字发票ID）
	  * @param lApInvHId
	  * @param invAmount
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
	public String judgmentInvoiceAmount(String lApInvHId,double invAmount) throws Exception;
	
	/**
	 * 
	  * @Title getApDocLByBgItemId 方法名
	  * @Description 按预算项目统计单据明细信息
	  * @param apDocId
	  * @param apDocCat
	  * @return
	  * @throws Exception
	  * @return List<ApDocumentLBean> 返回类型
	 */
	public List<ApDocumentLBean> getApDocLByBgItemId(String apDocId,String apDocCat) throws Exception ;
	/**
	 * 
	  * @Title updatePoOrderLAmtInfo 方法名
	  * @Description 采购发票选择采购订单保存时回写采购订单对应的已已开票金额等信息
	  * @param lists
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updatePoOrderLAmtInfo(List<ApVoucherHandlerBean> lists) throws Exception;
}
