package com.xzsoft.xc.ex.dao;

import java.util.List;

import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.PayDocBean;

/**
 * @ClassName: EXCheckAndPayDAO 
 * @Description: 单据复核、单据支付的数据处理层接口
 * @author linp
 * @date 2016年3月25日 下午2:38:23 
 *
 */
public interface EXCheckAndPayDAO {

	/**
	 * @Title: dePayDocs 
	 * @Description: 批量删除付款单
	 * <p>
	 * 	删除支付单主体信息
	 * 	删除支付单明细信息
	 * <p>
	 * @param payIds
	 * @throws Exception    设定文件
	 */
	public void dePayDocs(List<String> payIds) throws Exception ;
	
	/**
	 * @Title: createPayDoc 
	 * @Description: 创建付款单
	 * @param payDocBean
	 * @throws Exception    设定文件
	 */
	public void createPayDoc(PayDocBean payDocBean) throws Exception ;
	
	/**
	 * @Title: updatePayDoc 
	 * @Description: 修改付款单信息
	 * @param payDocBean
	 * @throws Exception    设定文件
	 */
	public void updatePayDoc(PayDocBean payDocBean) throws Exception ;
	
	/**
	 * @Title: getRuleCode 
	 * @Description: 取得单据的编码规则
	 * @param docCat
	 * @param ledgerId
	 * @return
	 * @throws Exception    设定文件
	 */
	public String getRuleCode(String docCat,String ledgerId) throws Exception ;
	
	/**
	 * @Title: updEXDoc 
	 * @Description: 更新单张报销单与凭证和支付单的对应关系
	 *  更新模式分为三大类：
	 *  	once   - 立即支付时更新报销单：		付款单ID、支付方式和核销金额	
	 *  	credit - 挂账支付时更新报销单：		凭证头ID、凭证状态、复核人（凭证制单人）、复核日期（凭证制制单日期）和支付方式
	 *  	payment- 新建付款单时更新报销单：	付款单ID
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void updEXDoc(EXDocBean docBean) throws Exception ;
	
	/**
	 * @Title: batchUpdEXDocs 
	 * @Description: 新建付款单时; 批量更新报销单与支付单的对应关系
	 * @param exDocIds
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void batchUpdEXDocs(List<String> exDocIds,EXDocBean docBean) throws Exception ;
	
	/**
	 * @Title: batchClearEXDocs 
	 * @Description: 批量清除费用报销单与凭证和支付的对应关系
	 * <p>
	 * 	将费用报销单与凭证相关的信息, 以及与支付单相关的信息全部清空
	 *  注：
	 *  	与凭证相关的信息包括：凭证头ID、凭证状态、复核人、复核日期、审核人、审核日期 。
	 *  	与支付单相关的信息包括：支付单ID
	 * </p>
	 * @param docIds
	 * @throws Exception    设定文件
	 */
	public void batchClearEXDocs(List<String> docIds) throws Exception ;
	
	/**
	 * @Title: updEXDocVoucher 
	 * @Description: 更新报销单凭证审核信息
	 * @param docBean
	 * @throws Exception    设定文件
	 */
	public void updEXDocVoucher(EXDocBean docBean) throws Exception ;
	
	/**
	 * @Title: updPayDocVoucher 
	 * @Description: 更新付款单凭证审核信息
	 * @param payDocBean
	 * @throws Exception    设定文件
	 */
	public void updPayDocVoucher(PayDocBean payDocBean) throws Exception ;
	
	/**
	 * @Title: upEXDocAuditInfo 
	 * @Description: 修改业务单据审批信息
	 * @param bean
	 * @throws Exception    设定文件
	 */
	public void upEXDocAuditInfo(EXDocBean bean) throws Exception ;
	
}
