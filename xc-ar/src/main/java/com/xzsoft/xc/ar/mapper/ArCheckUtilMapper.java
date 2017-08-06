package com.xzsoft.xc.ar.mapper;


import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;


/**
 * 
  * @ClassName ArCheckUtilMapper
  * @Description 应收模块检查验证Mapper层接口
  * @author RenJianJian
  * @date 2016年7月11日 下午4:05:24
 */
public interface ArCheckUtilMapper {
	
	/**
	 * 
	  * @Title selectGlNoPayAmtByInvId 方法名
	  * @Description 根据发票ID，获取应收单的金额信息
	  * @param AR_INV_H_ID
	  * @return
	  * @return ArInvGlHBean 返回类型
	 */
	public ArInvGlHBean selectGlNoPayAmtByInvId(String AR_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateInvHcByInvId 方法名
	  * @Description 根据发票ID，查询该发票是否做过红冲
	  * @param AR_INV_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkInvGlCreateInvHcByInvId(String AR_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateGlHcByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过红冲
	  * @param AR_INV_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkInvGlCreateGlHcByInvId(String AR_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreatePayByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过付款
	  * @param AR_INV_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkInvGlCreatePayByInvId(String AR_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateCancelHByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过核销单主信息
	  * @param AR_INV_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkInvGlCreateCancelHByInvId(String AR_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateCancelLByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过核销单明细
	  * @param AR_INV_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkInvGlCreateCancelLByInvId(String AR_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateAdjByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过调整单
	  * @param AR_INV_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkInvGlCreateAdjByInvId(String AR_INV_H_ID);
	
	/**
	 * 
	  * @Title selectGlNoPayAmtByGlId 方法名
	  * @Description 根据应收单ID，查询应收单的金额信息
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return ArInvGlHBean 返回类型
	 */
	public ArInvGlHBean selectGlNoPayAmtByGlId(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlVStatus 方法名
	  * @Description 根据应收单ID，应收单的凭证状态是否是审核通过状态
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkGlVStatus(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否由发票复核生成
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkGlCreateByInvId(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateInvHcByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单对应的发票是否做过红冲
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkGlCreateInvHcByInvId(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateGlHcByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否做过红冲
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkGlCreateGlHcByInvId(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreatePayByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否做过付款
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkGlCreatePayByInvId(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateCancelHByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否被别的应收单核销过
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkGlCreateCancelHByInvId(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateCancelLByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是核销过别的应收单
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkGlCreateCancelLByInvId(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateAdjByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否做过调整单
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkGlCreateAdjByInvId(String AR_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkPayVStatus 方法名
	  * @Description 根据收款单ID ，查询收款单的凭证状态是否是审核通过状态
	  * @param AR_PAY_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkPayVStatus(String AR_PAY_H_ID);

	/**
	 * 
	  * @Title checkPayCreateHcByPayId 方法名
	  * @Description 通过收款单ID查询收款单是否做过红冲
	  * @param AR_PAY_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkPayCreateHcByPayId(String AR_PAY_H_ID);
	
	/**
	 * 
	  * @Title checkPayCreateCancelByPayId 方法名
	  * @Description 通过收款单ID查询收款单是否做过核销
	  * @param AR_PAY_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkPayCreateCancelByPayId(String AR_PAY_H_ID);
	
	/**
	 * 
	  * @Title selectInvStatus 方法名
	  * @Description 根据发票ID，查询发票的流程状态和业务状态
	  * @param AR_INV_H_ID
	  * @return
	  * @return ArInvoiceHBean 返回类型
	 */
	public ArInvoiceHBean selectInvStatus(String AR_INV_H_ID);

	/**
	 * 
	  * @Title checkApArCancelByInvId 方法名
	  * @Description 根据应收单ID，判断当前应付单是做过应付核应收
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int checkApArCancelByInvId(String AR_INV_GL_H_ID);
}
