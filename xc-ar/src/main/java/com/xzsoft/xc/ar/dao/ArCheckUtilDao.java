package com.xzsoft.xc.ar.dao;

import com.xzsoft.xc.ar.modal.ArInvGlHBean;
import com.xzsoft.xc.ar.modal.ArInvoiceHBean;
/**
 * 
  * @ClassName ArCheckUtilDao
  * @Description 应收模块检查验证Dao层接口
  * @author RenJianJian
  * @date 2016年7月11日 下午4:05:36
 */
public interface ArCheckUtilDao {
	/**
	 * 
	  * @Title selectGlNoPayAmtByInvId 方法名
	  * @Description 根据发票ID，获取应收单的金额信息
	  * @param arInvHId
	  * @return
	  * @throws Exception
	  * @return ArInvGlHBean 返回类型
	 */
	public ArInvGlHBean selectGlNoPayAmtByInvId(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateInvHcByInvId 方法名
	  * @Description 根据发票ID，查询该发票是否做过红冲
	  * @param arInvHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkInvGlCreateInvHcByInvId(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateGlHcByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过红冲
	  * @param arInvHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkInvGlCreateGlHcByInvId(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreatePayByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过付款
	  * @param arInvHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkInvGlCreatePayByInvId(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateCancelHByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过核销单主信息
	  * @param arInvHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkInvGlCreateCancelHByInvId(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateCancelLByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过核销单明细
	  * @param arInvHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkInvGlCreateCancelLByInvId(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title checkInvGlCreateAdjByInvId 方法名
	  * @Description 根据发票ID，查询该发票生成的应收单是否做过调整单
	  * @param arInvHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkInvGlCreateAdjByInvId(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title selectInvStatus 方法名
	  * @Description 根据发票ID，查询发票的流程状态和业务状态
	  * @param arInvHId
	  * @return
	  * @throws Exception 设定文件
	  * @return ArInvoiceHBean 返回类型
	 */
	public ArInvoiceHBean selectInvStatus(String arInvHId) throws Exception;
	
	/**
	 * 
	  * @Title selectGlNoPayAmtByGlId 方法名
	  * @Description 根据应收单ID，查询应收单的金额信息
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return ArInvGlHBean 返回类型
	 */
	public ArInvGlHBean selectGlNoPayAmtByGlId(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlVStatus 方法名
	  * @Description 根据应收单ID，应收单的凭证状态是否是审核通过状态
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkGlVStatus(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否由发票复核生成
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkGlCreateByInvId(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateInvHcByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单对应的发票是否做过红冲
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkGlCreateInvHcByInvId(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateGlHcByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否做过红冲
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkGlCreateGlHcByInvId(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreatePayByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否做过付款
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkGlCreatePayByInvId(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateCancelHByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否被别的应收单核销过
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkGlCreateCancelHByInvId(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateCancelLByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是核销过别的应收单
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkGlCreateCancelLByInvId(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkGlCreateAdjByInvId 方法名
	  * @Description 根据应收单ID，判断当前应收单是否做过调整单
	  * @param arInvGlHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkGlCreateAdjByInvId(String arInvGlHId) throws Exception;
	
	/**
	 * 
	  * @Title checkPayVStatus 方法名
	  * @Description 根据收款单ID ，查询收款单的凭证状态是否是审核通过状态
	  * @param arPayHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkPayVStatus(String arPayHId) throws Exception;

	/**
	 * 
	  * @Title checkPayCreateHcByPayId 方法名
	  * @Description 通过收款单ID查询收款单是否做过红冲
	  * @param arPayHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int  checkPayCreateHcByPayId(String arPayHId) throws Exception;
	
	/**
	 * 
	  * @Title checkPayCreateCancelByPayId 方法名
	  * @Description 通过收款单ID查询收款单是否做过核销
	  * @param arPayHId
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int  checkPayCreateCancelByPayId(String arPayHId) throws Exception;

	/**
	 * 
	  * @Title checkApArCancelByInvId 方法名
	  * @Description 根据应收单ID，判断当前应付单是做过应付核应收
	  * @param AR_INV_GL_H_ID
	  * @return
	  * @throws Exception 设定文件
	  * @return int 返回类型
	 */
	public int checkApArCancelByInvId(String AR_INV_GL_H_ID) throws Exception;
	
}
