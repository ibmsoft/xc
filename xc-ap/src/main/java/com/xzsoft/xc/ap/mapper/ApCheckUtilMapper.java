package com.xzsoft.xc.ap.mapper;


import com.xzsoft.xc.ap.modal.ApCheckUtilBean;


/**
 * 
  * @ClassName ApCheckUtilMapper
  * @Description 应付模块检查验证Mapper层接口
  * @author RenJianJian
  * @date 2016年6月24日 下午5:40:55
 */
public interface ApCheckUtilMapper {
	
	/**
	 * 
	  * @Title selectGlNoPayAmtByInvId 方法名
	  * @Description 根据发票ID，获取应付单的金额信息
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean selectGlNoPayAmtByInvId(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateInvHcByInvId 方法名
	  * @Description 根据发票ID，查询该发票是否做过红冲
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateInvHcByInvId(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateGlHcByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过红冲
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateGlHcByInvId(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateReqPayByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过付款申请
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateReqPayByInvId(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreatePayByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过付款
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreatePayByInvId(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateCancelHByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过核销单主信息
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateCancelHByInvId(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateCancelLByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过核销单明细
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateCancelLByInvId(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title checkInvGlCreateAdjByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过调整单
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateAdjByInvId(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title selectGlNoPayAmtByGlId 方法名
	  * @Description 根据应付单ID，查询应付单的金额信息
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean selectGlNoPayAmtByGlId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlVStatus 方法名
	  * @Description 根据应付单ID，应付单的凭证状态是否是审核通过状态
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlVStatus(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否由发票复核生成
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateByInvId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateInvHcByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单对应的发票是否做过红冲
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateInvHcByInvId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateGlHcByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否做过红冲
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateGlHcByInvId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreatePayReqByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否做过付款申请
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreatePayReqByInvId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreatePayByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否做过付款
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreatePayByInvId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateCancelHByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否被别的应付单核销过
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateCancelHByInvId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateCancelLByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是核销过别的应付单
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateCancelLByInvId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkGlCreateAdjByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否做过调整单
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateAdjByInvId(String AP_INV_GL_H_ID);
	
	/**
	 * 
	  * @Title checkPayVStatus 方法名
	  * @Description 根据付款单ID ，查询付款单的凭证状态是否是审核通过状态
	  * @param AP_PAY_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean  checkPayVStatus(String AP_PAY_H_ID);
	
	/**
	 * 
	  * @Title selectInvStatus 方法名
	  * @Description 根据发票ID，查询发票的流程状态和业务状态
	  * @param AP_INV_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean selectInvStatus(String AP_INV_H_ID);
	
	/**
	 * 
	  * @Title selectReqPayStatus 方法名
	  * @Description 根据付款申请ID，查询付款申请的流程状态和业务状态
	  * @param AP_PAY_REQ_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean selectReqPayStatus(String AP_PAY_REQ_H_ID);

	/**
	 * 
	  * @Title checkPayCreateHcByPayId 方法名
	  * @Description 通过付款单ID查询付款单是否做过红冲
	  * @param AP_PAY_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int  checkPayCreateHcByPayId(String AP_PAY_H_ID);
	
	/**
	 * 
	  * @Title checkPayCreateCancelByPayId 方法名
	  * @Description 通过付款单ID查询付款单是否做过核销
	  * @param AP_PAY_H_ID
	  * @return
	  * @return int 返回类型
	 */
	public int  checkPayCreateCancelByPayId(String AP_PAY_H_ID);

	/**
	 * 
	  * @Title checkArApCancelByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是做过应收核应付
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkArApCancelByInvId(String AP_INV_GL_H_ID);
}
