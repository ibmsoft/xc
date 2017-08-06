package com.xzsoft.xc.ap.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ap.modal.ApPayReqHBean;
import com.xzsoft.xc.ap.modal.ApVoucherHandlerBean;
import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ApDocumentLBean;


/**
 * 
  * @ClassName XCAPCommonMapper
  * @Description 应付模块公共Mapper
  * @author 任建建
  * @date 2016年4月5日 上午9:26:02
 */
public interface XCAPCommonMapper {
	/**
	 * 
	  * @Title getApCode
	  * @Description 获得单据编码
	  * @param map
	  * @param 设定文件
	  * @return String	返回类型
	 */
	public String getApCode(HashMap<String,String> map);
	/**
	 * 
	  * @Title getApInvGlH
	  * @Description 查询应付单主表信息
	  * @param apInvGlHId
	  * @param 设定文件
	  * @return ApDocumentHBean	返回类型
	 */
	public ApDocumentHBean getApInvGlH(String apInvGlHId);
	/**
	 * 
	  * @Title getApInvGlHByInvId 方法名
	  * @Description 查询应付单主表信息（根据采购发票ID）
	  * @param apInvId
	  * @return 设定文件
	  * @return ApDocumentHBean 返回类型
	 */
	public ApDocumentHBean getApInvGlHByInvId(String apInvId);
	/**
	 * 
	  * @Title getApInvGlL
	  * @Description 查询应付单行表信息
	  * @param apInvGlHId
	  * @param 设定文件
	  * @return List<ApDocumentLBean>	返回类型
	 */
	public List<ApDocumentLBean> getApInvGlL(String apInvGlHId);
	/**
	 * 
	  * @Title getApCancelH
	  * @Description 查询核销单主表信息
	  * @param apCancelHId
	  * @param 设定文件
	  * @return ApDocumentHBean	返回类型
	 */
	public ApDocumentHBean getApCancelH(String apCancelHId);
	/**
	 * 
	  * @Title getApCancelL
	  * @Description 查询核销单行表信息
	  * @param apCancelHId
	  * @param 设定文件
	  * @return List<ApDocumentLBean>	返回类型
	 */
	public List<ApDocumentLBean> getApCancelL(String apCancelHId);
	/**
	 * 
	  * @Title getApCancelLAndGlL
	  * @Description 查询核销单行表信息
	  * @param apCancelHId
	  * @param 设定文件
	  * @return List<ApDocumentLBean>	返回类型
	 */
	public List<ApDocumentLBean> getApCancelLAndGlL(String apCancelHId);
	/**
	 * 
	  * @Title getApPayH
	  * @Description 查询付款单主表信息
	  * @param apPayHId
	  * @param 设定文件
	  * @return ApDocumentHBean	返回类型
	 */
	public ApDocumentHBean getApPayH(String apPayHId);
	/**
	 * 
	  * @Title getApPayL
	  * @Description 查询付款单行表信息
	  * @param apPayHId
	  * @param 设定文件
	  * @return List<ApDocumentLBean>	返回类型
	 */
	public List<ApDocumentLBean> getApPayL(String apPayHId);
	/**
	 * 
	  * @Title getApInvAdj
	  * @Description 查询余额调整单信息
	  * @param glAdjId
	  * @param 设定文件
	  * @return ApDocumentHBean	返回类型
	 */
	public ApDocumentHBean getApInvAdj(String glAdjId);
	/**
	 * 
	  * @Title getApPayReqH 方法名
	  * @Description 查询付款申请单主表信息
	  * @param apPayReqGId
	  * @return 设定文件
	  * @return ApPayReqHBean 返回类型
	 */
	public ApPayReqHBean getApPayReqH(String apPayReqGId);
	/**
	 * 
	  * @Title getApInvoiceH 方法名
	  * @Description 查询采购发票主表信息
	  * @param apInvoiceHId
	  * @return 设定文件
	  * @return ApDocumentHBean 返回类型
	 */
	public ApDocumentHBean getApInvoiceH(String apInvoiceHId);
	/**
	 * 
	  * @Title getApInvoiceL 方法名
	  * @Description 查询采购发票行表信息
	  * @param apInvoiceHId
	  * @return 设定文件
	  * @return List<ApDocumentLBean> 返回类型
	 */
	public List<ApDocumentLBean> getApInvoiceL(String apInvoiceHId);
	/**
	 * 
	  * @Title getApInvoiceLById 方法名
	  * @Description 查询采购发票行表信息(根据采购发票行表ID)
	  * @param apInvoiceHId
	  * @return 设定文件
	  * @return ApDocumentLBean 返回类型
	 */
	public ApDocumentLBean getApInvoiceLById(String apInvoiceLId);
	/**
	 * 
	  * @Title deleteApDoc
	  * @Description 删除单据表
	  * @param apVoucherHandler	设定文件
	  * @return void	返回类型
	 */
	public void deleteApDoc(ApVoucherHandlerBean apVoucherHandler);
	/**
	 * 
	  * @Title getAccId 方法名
	  * @Description 通过科目组合ID得到科目ID
	  * @param ccId
	  * @return 设定文件
	  * @return String 返回类型
	 */
	public String getAccId(String ccId);
	/**
	 * 
	  * @Title judgmentInvAmount 方法名
	  * @Description 红字发票需要判断应付单的未付金额是否还够冲销当前红色发票
	  * @param map
	  * @return 设定文件
	  * @return String 返回类型
	 */
	public String judgmentInvAmount(HashMap<String,Object> map);
	/**
	 * 
	  * @Title judgmentInvAmount 方法名
	  * @Description 红字发票需要判断应付单的未付金额是否还够冲销当前红色发票（通过蓝字发票ID）
	  * @param map
	  * @return 设定文件
	  * @return String 返回类型
	 */
	public String judgmentInvoiceAmount(HashMap<String,Object> map);

	/**
	 * 
	  * @Title getApDocLByBgItemId 方法名
	  * @Description 按预算项目统计单据明细信息
	  * @param map
	  * @return 设定文件
	  * @return List<ApDocumentLBean> 返回类型
	 */
	public List<ApDocumentLBean> getApDocLByBgItemId(HashMap<String,String> map);
	/**
	 * 
	  * @Title updatePoOrderLAmtInfo 方法名
	  * @Description 采购发票选择采购订单保存时回写采购订单对应的已已开票金额等信息
	  * @param map
	  * @return void 返回类型
	 */
	public void updatePoOrderLAmtInfo(HashMap<String,Object> map);
}
