package com.xzsoft.xc.ex.service;

import com.xzsoft.xc.ex.modal.IAFactBean;

/**
 * @ClassName: BorrowMoneyService 
 * @Description: 备用金明细处理服务接口 
 * @author linp
 * @date 2016年3月10日 上午9:54:26 
 *
 */
public interface BorrowMoneyService {
	
	/**
	 * 
	 * @title:       handlerUserImprest
	 * @description: 处理个人备用金，同步操作
	 * @author       tangxl
	 * @date         2016年4月6日
	 * @param        IAFactBean iaFactBean
	 * @param        billType 备用金类型：1-核销款，2-借款，3-还款<必传>
	 * @param        operationType 操作类型:1-查询备用金 ,2-新增操作,3-修改操作,4-删除操作
	 * @return       返回处理后的备用金信息<这里的处理操作指  查询和修改>,返回对象信息请参见 com.xzsoft.xc.ex.modal.IAFactBean
	 * @throws Exception
	 */
	
	public void handlerUserImprest(IAFactBean iaFactBean) throws Exception;
	
}
