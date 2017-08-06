package com.xzsoft.xc.apar.dao;

import java.util.List;

import com.xzsoft.xc.apar.modal.ApDocumentHBean;
import com.xzsoft.xc.apar.modal.ArDocumentHBean;

/**
 * 
  * @ClassName：UpdateInvGlAmountDao
  * @Description：对应付单和应收单中的金额金额处理
  * @author：RenJianJian
  * @date：2016年9月29日 上午10:29:54
 */
public interface UpdateInvGlAmountDao {
	/**
	 * 
	  * @Title updateApInvGlAmount 方法名
	  * @Description 更新应付单中的金额
	  * @param apDocumentHList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateApInvGlAmount(List<ApDocumentHBean> apDocumentHList) throws Exception;
	
	/**
	 * 
	  * @Title updateArInvGlAmount 方法名
	  * @Description 更新应收单中的金额
	  * @param arDocumentHList
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateArInvGlAmount(List<ArDocumentHBean> arDocumentHList) throws Exception;
}
