package com.xzsoft.xc.ar.dao;
/**
 * 
  * @ClassName ArAuditDao
  * @Description 应收模块单据流程信息Dao
  * @author 任建建
  * @date 2016年7月7日 下午12:39:25
 */
public interface ArAuditDao {
	/**
	 * 
	  * @Title updateArInsCode
	  * @Description 更新审批流程实例编码
	  * @param tableName		表名
	  * @param insCode		实例编码
	  * @param priKey		主键名称
	  * @param bizId			主键值
	  * @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateArInsCode(String tableName,String insCode,String priKey,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateArAuditStatus
	  * @Description 更新审批流程审批状态
	  * @param tableName			表名
	  * @param bizStatusCat		状态编码
	  * @param bizStatusCatDesc	状态描述
	  * @param priKey			主键名称
	  * @param bizId				主键值
	  * @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateArAuditStatus(String tableName,String bizStatusCat,String bizStatusCatDesc,String priKey,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateArAuditDate
	  * @Description 更新流程审批通过日期
	  * @param tableName			表名
	  * @param priKey			主键名称
	  * @param bizId				主键值
	  * @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateArAuditDate(String tableName,String priKey,String bizId) throws Exception;
	/**
	 * 
	  * @Title revokeCompleteProcess 方法名
	  * @Description 审批通过之后执行撤回，更新单据信息
	  * @param tableName
	  * @param priKey
	  * @param bizId
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void revokeCompleteProcess(String tableName,String priKey,String bizId) throws Exception;
}
