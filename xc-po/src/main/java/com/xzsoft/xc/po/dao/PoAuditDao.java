package com.xzsoft.xc.po.dao;
/** 
 * @ClassName: PoAuditDao
 * @Description: 更新采购模块单据流程信息Dao层 
 * @author weicw
 * @date 2016年12月9日 上午10:59:50 
 * 
 */
public interface PoAuditDao {
	/**
	 * 
	  * @Title updatePoInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param tableName	表名
	  * @param insCode		实例编码
	  * @param priKey		主键名称
	  * @param bizId		主键值
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updatePoInsCode(String tableName,String insCode,String priKey,String bizId) throws Exception;
	/**
	 * 
	  * @Title updatePoAuditStatus
	  * @Description 更新审批流程审批状态
	  * @param tableName		表名
	  * @param bizStatusCat		状态编码
	  * @param bizStatusCatDesc	状态描述
	  * @param priKey			主键名称
	  * @param bizId			主键值
	  * @param Exception	设定文件
	  * @return void	返回类型
	 */
	public void updatePoAuditStatus(String tableName,String bizStatusCat,String bizStatusCatDesc,String priKey,String bizId) throws Exception;
	/**
	 * 
	  * @Title updatePoAuditDate
	  * @Description 更新流程审批通过日期
	  * @param tableName		表名
	  * @param priKey			主键名称
	  * @param bizId			主键值
	  * @param Exception	设定文件
	  * @return void	返回类型
	 */
	public void updatePoAuditDate(String tableName,String priKey,String bizId) throws Exception;
	/**
	 * 
	  * @Title revokeCompleteProcess 方法名
	  * @Description 审批通过之后执行撤回，更新单据信息
	  * @param tableName
	  * @param priKey
	  * @param bizId
	  * @param Exception 设定文件
	  * @return void 返回类型
	 */
	public void revokeCompleteProcess(String tableName,String priKey,String bizId) throws Exception;
}
