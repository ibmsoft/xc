package com.xzsoft.xc.bg.service;

import java.util.List;

import com.xzsoft.xc.bg.modal.BgFactBean;

/**
 * 
 * @ClassName: BudgetDataHandlerService 
 * @Description: 预算数据处理服务接口：包括预算余额及预算发生明细表(占用与发生数)
 * @author linp
 * @date 2016年3月10日 上午9:41:22 
 *
 */
public interface BudgetDataHandlerService {
	
	/**
	 * @Title: budgetDataInsert 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param bizId
	 * @param loginName //预算单ID
	 * @param opType //操作类型: 1-插入费用预算余额表, 2-插入项目预算余额表
	 * @return
	 * @throws Exception    设定文件
	 */
	public String budgetDataInsert(String bizId,String loginName,String opType) throws Exception;
	
	/**
	 * @Title: incrOccupancyBudget 
	 * @Description: 增加预算占用数
	 * @param  list
	 * @throws Exception    设定文件
	 */
	public void incrOccupancyBudget(List<BgFactBean> list) throws Exception ;
	
	/**
	 * @Title: updOccupancyBudget 
	 * @Description:         修改预算占用数
	 * @param   srcId
	 * @param   targetType    修改后的状态
	 * @param   originalType  要修改的状态
	 * @throws Exception      设定文件
	 */
	public void updateFactBudget(String srcId,String targetType,String originalType) throws Exception ;
	
	/**
	 * @Title: delOccupancyBudget 
	 * @Description: 删除预算占用数
	 * @param srcId
	 * @param factType   要删除的预算数类型
	 * @throws Exception    设定文件
	 */
	public void deleteFactBudget(String srcId,String factType) throws Exception ;
	
}
