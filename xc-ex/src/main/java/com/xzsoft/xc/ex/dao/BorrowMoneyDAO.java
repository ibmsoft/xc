package com.xzsoft.xc.ex.dao;

import com.xzsoft.xc.ex.modal.IAFactBean;

/**
 * @ClassName: BorrowMoneyDAO 
 * @Description: 备用金明细数据层接口
 * @author linp
 * @date 2016年3月10日 上午9:55:17 
 *
 */
public interface BorrowMoneyDAO {
	
	/**
	 * 
	 * @title:         queryUserImprest
	 * @description:   查询个人备用金
	 * @author         tangxl
	 * @date           2016年4月6日
	 * @param          ledgerId
	 * @param          userId
	 * @return
	 * @throws Exception
	 */
	public IAFactBean queryUserImprest(String ledgerId,String userId) throws Exception;
	
	/**
	 * 
	 * @title:         addUserImprest
	 * @description:   新增个人备用金
	 * @author         tangxl
	 * @date           2016年4月6日
	 * @param          iaFactBean
	 * @throws Exception
	 */
	public void  addUserImprest(IAFactBean iaFactBean) throws Exception;
	/**
	 * 
	 * @title:         deleteUserImprest
	 * @description:   删除个人备用金
	 * @author         tangxl
	 * @date           2016年4月6日
	 * @param          iaFactBean
	 * @throws Exception
	 */
	public void  deleteUserImprest(IAFactBean iaFactBean) throws Exception;
	
}
