package com.xzsoft.xc.apar.service;

import java.util.List;

/**
 * 
  * @ClassName UpdateInvGlAmountService
  * @Description 更新应付单和应收单中的相应金额
  * @author RenJianJian
  * @date 2016年9月29日 上午9:43:17
 */
public interface UpdateInvGlAmountService {
	/**
	 * 
	  * @Title updateApInvGlAmount 方法名
	  * @Description 通过应付单ID查询交易明细表中的金额数据，并更新应付单中相应的金额
	  * @param apInvGlIdList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateApInvGlAmount(List<String> apInvGlIdList) throws Exception;

	/**
	 * 
	  * @Title updateArInvGlAmount 方法名
	  * @Description 通过应收单ID查询交易明细表中的金额数据，并更新应收单中相应的金额
	  * @param arInvGlIdList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateArInvGlAmount(List<String> arInvGlIdList) throws Exception;
}
