/**
 * @Title:ArInvTransDao.java
 * @package:com.xzsoft.xc.ar.dao
 * @Description:TODO
 * @date:2016年7月28日上午10:21:33
 * @version V1.0
 */
package com.xzsoft.xc.ar.dao;

import java.util.List;

import com.xzsoft.xc.apar.modal.ArInvTransBean;
import com.xzsoft.xc.ar.modal.ArPayLBean;

/**
 * @ClassName: ArInvTransDao
 * @Description: 对交易明细表的相关操作
 * @author:zhenghy
 * @date: 2016年7月28日 上午10:21:33
 */
public interface ArInvTransDao {

	/**
	 * 
	 * @Title: getArInvTran 
	 * @Description: 获取一条交易明细
	 * @param source_id 来源id
	 * @param source_dtl_id 来源明细id
 	 * @param ar_doc_cat_code 单据类型
	 * @return: ArInvTransBean  明细对象
	 *
	 */
	public ArInvTransBean getArInvTran(String source_id, String source_dtl_id, String ar_doc_cat_code) throws Exception;
	/**
	 * 
	 * @Title: inserArInvTran 
	 * @Description: 新增一条交易明细
	 * @param arInvTransBean     
	 * @return: void    
	 *
	 */
	public void insertArInvTran(ArInvTransBean arInvTransBean) throws Exception;
	/**
	 * 
	 * @Title: insertArInvTrans  
	 * @Description: 批量新增交易明细对象
	 * @param arInvTransBeanList
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void insertArInvTrans(List<ArInvTransBean> arInvTransBeanList) throws Exception;
	/**
	 * 
	 * @Title: deleteArInvTrans  
	 * @Description: 根据收款单行记录删除交易明细
	 * @param arPayLBeanList
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteArInvTrans(List<ArPayLBean> arPayLBeanList) throws Exception;
	/**
	 * 
	 * @Title: deleteArInvTrans  
	 * @Description: 删除交易明细记录
	 * @param source_id
	 * @throws Exception     
	 * @return: void    
	 *
	 */
	public void deleteArInvTrans(String source_id) throws Exception;
}
