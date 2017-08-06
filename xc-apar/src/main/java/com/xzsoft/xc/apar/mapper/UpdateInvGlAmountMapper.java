package com.xzsoft.xc.apar.mapper;

import java.util.HashMap;
/**
 * 
  * @ClassName UpdateInvGlAmountMapper
  * @Description 对应付单和应收单中的金额金额处理
  * @author RenJianJian
  * @date 2016年9月29日 上午10:27:52
 */
public interface UpdateInvGlAmountMapper {
	/**
	 * 
	  * @Title updateApInvGlAmount 方法名
	  * @Description 更新应付单中的金额
	  * @param map
	  * @return void 返回类型
	 */
	public void updateApInvGlAmount(HashMap<String,Object> map);
	/**
	 * 
	  * @Title updateArInvGlAmount 方法名
	  * @Description 更新应收单中的金额
	  * @param map
	  * @return void 返回类型
	 */
	public void updateArInvGlAmount(HashMap<String,Object> map);
}
