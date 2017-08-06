/**
 * @Title:ArInvTransMapper.java
 * @package:com.xzsoft.xc.ar.mapper
 * @Description:TODO
 * @date:2016年7月27日下午3:46:22
 * @version V1.0
 */
package com.xzsoft.xc.ar.mapper;

import java.util.HashMap;

import com.xzsoft.xc.apar.modal.ArInvTransBean;


/**
 * @ClassName: ArInvTransMapper
 * @Description: 对交易明细表进行相关操作
 * @author:zhenghy
 * @date: 2016年7月27日 下午3:46:22
 */
public interface ArInvTransMapper {

	/**
	 * 
	 * @Title: selectArInvTran
	 * @Description: 查询一条交易明细对象
	 * @param map 来源id 来源明细id 单据类型
	 * @return: ArInvTransBean    
	 *
	 */
	public ArInvTransBean selectArInvTran(HashMap<String, String> map);
	
	/**
	 * 
	 * @Title: insertArInvTran  
	 * @Description: 新增一条交易明细对象
	 * @param arInvTransBean 交易明细对象
	 * @return: void    
	 *
	 */
	public void insertArInvTran(ArInvTransBean arInvTransBean);

	/**
	 * 
	 * @Title: insertArInvTrans  
	 * @Description: 批量新增交易明细对象
	 * @param map   数据库类型  交易明细对象集合  
	 * @return: void    
	 *
	 */
	public void insertArInvTrans(HashMap<String, Object> map);
	
	/**
	 * 
	 * @Title: deleteArInvTrans  
	 * @Description: 批量删除交易明细对象
	 * @param map  数据库类型 某种集合
	 * @return: void    
	 *
	 */
	public void deleteArInvTransByPay(HashMap<String, Object> map);
	/**
	 * 
	 * @Title: deleteArInvTransBySourceId  
	 * @Description: 删除交易明细记录
	 * @param source_id 来源id     
	 * @return: void    
	 *
	 */
	public void deleteArInvTransBySourceId(String source_id);
}
