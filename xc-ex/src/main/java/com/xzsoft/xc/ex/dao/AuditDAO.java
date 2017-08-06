package com.xzsoft.xc.ex.dao;

/**
 * @ClassName: AuditDAO 
 * @Description: 报销单据流程审批处理数据层接口
 * @author linp
 * @date 2016年3月11日 上午9:53:58 
 *
 */
public interface AuditDAO {
	
	/**
	 * @Title: updateInsCode 
	 * @Description: 更新审批流程实例编码
	 * @param insCode
	 * @param bizId
	 * @param loginName
	 * @throws Exception    设定文件
	 */
	public void updateInsCode(String insCode, String bizId, String loginName) throws Exception ;
	
	/**
	 * @Title: updateAuditStatus 
	 * @Description: 更新审批流程审批状态
	 * @param bizId
	 * @param bizStatusCat
	 * @param bizStatusCatDesc
	 * @param loginName
	 * @throws Exception    设定文件
	 */
	public void updateAuditStatus(String bizId, String bizStatusCat,String bizStatusCatDesc, String loginName) throws Exception ;
	
	/**
	 * @Title: updateAuditDate 
	 * @Description: 更新流程审批通过日期
	 * @param bizId
	 * @param loginName
	 * @throws Exception    设定文件
	 */
	public void updateAuditDate(String bizId, String loginName) throws Exception ;
	
	/**
	 * @Title: updateAuditUsers 
	 * @Description: 更新当前审批人信息
	 * @param bizId
	 * @param nodeApprovers
	 * @param loginName
	 * @throws Exception    设定文件
	 */
	public void updateAuditUsers(String bizId, String nodeApprovers,String loginName) throws Exception ;
	
}
