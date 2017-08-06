package com.xzsoft.xc.bg.service;
/**
 * 
  * @ClassName: BgAuditService
  * @Description: 预算单流程信息业务处理层
  * @author 任建建
  * @date 2016年3月28日 上午9:16:36
 */
public interface BgAuditService {
	/**
	 * 
	  * @Title: updateBgInsCode
	  * @Description: 更新审批流程实例编码
	  * @param @param insCode			实例编码
	  * @param @param bizId				业务主键ID（预算单ID）
	  * @param @param loginName			当前登录人
	  * @param @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateBgInsCode(String insCode,String bizId,String loginName) throws Exception;
	/**
	 * 
	  * @Title: updateBgAuditStatus
	  * @Description: 更新审批流程审批状态
	  * @param @param bizStatusCat		审批状态
	  * @param @param bizStatusCatDesc	审批状态描述
	  * @param @param bizId				业务主键ID（预算单ID）
	  * @param @param loginName			当前登录人
	  * @param @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateBgAuditStatus(String bizId,String bizStatusCat,String bizStatusCatDesc,String loginName) throws Exception;
	/**
	 * 
	  * @Title: updateBgAuditDate
	  * @Description: 更新流程审批通过日期
	  * @param @param bizId				业务主键ID（预算单ID）
	  * @param @param loginName			当前登录人
	  * @param @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateBgAuditDate(String bizId,String loginName) throws Exception;
	/**
	 * 
	  * @Title: updateBgAuditUsers
	  * @Description: 更新当前审批人信息
	  * @param @param bizId				业务主键ID（预算单ID）
	  * @param @param nodeApprovers		节点执行人
	  * @param @param loginName			当前登录人
	  * @param @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateBgAuditUsers(String bizId,String nodeApprovers,String loginName) throws Exception;
}
