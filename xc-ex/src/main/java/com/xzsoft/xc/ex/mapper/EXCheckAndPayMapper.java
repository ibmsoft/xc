package com.xzsoft.xc.ex.mapper;

import java.util.HashMap;
import java.util.List;

import com.xzsoft.xc.ex.modal.EXDocBean;
import com.xzsoft.xc.ex.modal.PayDocBean;
import com.xzsoft.xc.ex.modal.PayDocDtlBean;

/**
 * @ClassName: EXCheckAndPayMapper 
 * @Description: 单据复核、单据支付 
 * @author linp
 * @date 2016年3月25日 下午6:26:35 
 *
 */
public interface EXCheckAndPayMapper {

	/**
	 * @Title: savePayDoc 
	 * @Description: 生成付款单头信息
	 * @param payDocBean    设定文件
	 */
	public void savePayDoc(PayDocBean payDocBean) ;
	
	/**
	 * @Title: updPayDoc 
	 * @Description: 更新付款单信息
	 * @param payDocBean    设定文件
	 */
	public void updPayDoc(PayDocBean payDocBean) ;
	
	/**
	 * @Title: savePayDocDtl 
	 * @Description: 生成付款单明细信息
	 * @param payDocDtlBean    设定文件
	 */
	public void savePayDocDtl(PayDocDtlBean payDocDtlBean) ;
	
	/**
	 * @Title: delPayDocDtl 
	 * @Description: 按付款单ID, 删除付款单明细
	 * @param payId    设定文件
	 */
	public void delPayDocDtl(String payId) ;
	
	/**
	 * @Title: delPayDoc 
	 * @Description: 按付款单ID, 删除付款单头信息
	 * @param payId    设定文件
	 */
	public void delPayDoc(String payId) ;
	
	/**
	 * @Title: delPayDocs 
	 * @Description: 批量删除付款单信息
	 * @param payIds    设定文件
	 */
	public void delPayDocs(List<String> payIds) ;
	
	/**
	 * @Title: batchDelPayDocDtl 
	 * @Description: 批量删除付款单明细
	 * @param payIds    设定文件
	 */
	public void batchDelPayDocDtl(List<String> payIds) ;
	
	/**
	 * @Title: batchDelPayDoc 
	 * @Description: 批量删除付款头信息
	 * @param payIds    设定文件
	 */
	public void batchDelPayDoc(List<String> payIds) ;
	
	/**
	 * @Title: getRuleCode 
	 * @Description: 获取单据类型的编码规则
	 * @param map
	 * @return    设定文件
	 */
	public String getRuleCode(HashMap<String,String> map) ;
	
	/**
	 * @Title: updEXDoc 
	 * @Description: 更新报销单信息
	 * @param docBean    设定文件
	 */
	public void updEXDoc(EXDocBean docBean) ;
	
	/**
	 * @Title: batchUpdEXDocs 
	 * @Description: 批量更新费用报销单与支付单的对应关系
	 * @param map    设定文件
	 */
	public void batchUpdEXDocs(HashMap<String,Object> map) ;
	
	/**
	 * @Title: batchClearEXDocs 
	 * @Description: 批量清除费用报销单与凭证和支付的对应关系
	 * @param map    设定文件
	 */
	public void batchClearEXDocs(HashMap<String,Object> map) ;
	
	/**
	 * @Title: updEXDocVoucher 
	 * @Description: 更新报销单凭证审核信息
	 * @param bean    设定文件
	 */
	public void updEXDocVoucher(EXDocBean bean) ;
	
	/**
	 * @Title: updPayDocVoucher 
	 * @Description: 更新付款单凭证审核信息
	 * @param payDocBean    设定文件
	 */
	public void updPayDocVoucher(PayDocBean payDocBean) ;
	
	/**
	 * @Title: upEXDocAuditInfo 
	 * @Description: 修改业务单据审批信息
	 * @param bean    设定文件
	 */
	public void upEXDocAuditInfo(EXDocBean bean) ;
	
}
