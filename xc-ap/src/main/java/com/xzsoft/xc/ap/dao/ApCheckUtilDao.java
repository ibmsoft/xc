package com.xzsoft.xc.ap.dao;

import com.xzsoft.xc.ap.modal.ApCheckUtilBean;
/**
 * 
  * @ClassName ApCheckUtilDao
  * @Description 应付模块检查验证Dao层接口
  * @author 任建建
  * @date 2016年6月28日 上午9:04:24
 */
public interface ApCheckUtilDao {
	/**
	 * 
	  * @Title selectGlNoPayAmtByInvId 方法名
	  * @Description 根据发票ID，获取应付单的金额信息
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean selectGlNoPayAmtByInvId(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateInvHcByInvId 方法名
	  * @Description 根据发票ID，查询该发票是否做过红冲
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateInvHcByInvId(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateGlHcByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过红冲
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateGlHcByInvId(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateReqPayByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过付款申请
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateReqPayByInvId(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreatePayByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过付款
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreatePayByInvId(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateCancelHByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过核销单主信息
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateCancelHByInvId(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateCancelLByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过核销单明细
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateCancelLByInvId(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateAdjByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应付单是否做过调整单
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkInvGlCreateAdjByInvId(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title selectInvStatus 方法名
	  * @Description 根据发票ID，查询发票的流程状态和业务状态
	  * @param apInvHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean selectInvStatus(String apInvHId) throws Exception;
	
	/**
	 * 
	  * @Title selectGlNoPayAmtByGlId 方法名
	  * @Description 根据应付单ID，查询应付单的金额信息
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean selectGlNoPayAmtByGlId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlVStatus 方法名
	  * @Description 根据应付单ID，应付单的凭证状态是否是审核通过状态
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlVStatus(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否由发票复核生成
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateByInvId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateInvHcByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单对应的发票是否做过红冲
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateInvHcByInvId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateGlHcByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否做过红冲
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateGlHcByInvId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreatePayReqByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否做过付款申请
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreatePayReqByInvId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreatePayByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否做过付款
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreatePayByInvId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateCancelHByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否被别的应付单核销过
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateCancelHByInvId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateCancelLByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是核销过别的应付单
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateCancelLByInvId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateAdjByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是否做过调整单
	  * @param apInvGlHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkGlCreateAdjByInvId(String apInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkPayVStatus 方法名
	  * @Description 根据付款单ID ，查询付款单的凭证状态是否是审核通过状态
	  * @param apPayHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkPayVStatus(String apPayHId) throws Exception;
	
	/**
	 * 
	  * @Title selectReqPayStatus 方法名
	  * @Description 根据付款申请ID，查询付款申请的流程状态和业务状态
	  * @param apReqPayHId
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean selectReqPayStatus(String apReqPayHId) throws Exception;

	/**
	 * 
	  * @Title checkPayCreateHcByPayId 方法名
	  * @Description 通过收款单ID查询收款单是否做过红冲
	  * @param apPayHId
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int  checkPayCreateHcByPayId(String apPayHId) throws Exception;
	
	/**
	 * 
	  * @Title checkPayCreateCancelByPayId 方法名
	  * @Description 通过收款单ID查询收款单是否做过核销
	  * @param apPayHId
	  * @return
	  * @throws Exception
	  * @return int 返回类型
	 */
	public int  checkPayCreateCancelByPayId(String apPayHId) throws Exception;

	/**
	 * 
	  * @Title checkArApCancelByInvId 方法名
	  * @Description 根据应付单ID，判断当前应付单是做过应收核应付
	  * @param AP_INV_GL_H_ID
	  * @return
	  * @throws Exception
	  * @return ApCheckUtilBean 返回类型
	 */
	public ApCheckUtilBean checkArApCancelByInvId(String AP_INV_GL_H_ID) throws Exception;
}
