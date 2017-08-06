package com.xzsoft.xc.ar.service;
/**
 * 
  * @ClassName ArAuditService
  * @Description 流程信息业务处理层
  * @author 任建建
  * @date 2016年7月7日 下午12:41:34
 */
public interface ArAuditService {
	/**
	 * 
	  * @Title updateArInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param apCatCode		单据大类编码
	  * @param insCode			实例编码
	  * @param bizId			业务主键ID
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateArInsCode(String apCatCode,String insCode,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateArAuditStatus
	  * @Description 更新审批流程审批状态
	  * @param apCatCode		单据大类编码
	  * @param bizStatusCat		审批状态
	  * @param bizStatusCatDesc	审批状态描述
	  * @param bizId			业务主键ID
	  * @throws Exception
	  * @return void	返回类型
	 */
	public void updateArAuditStatus(String apCatCode,String bizStatusCat,String bizStatusCatDesc,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateArAuditDate
	  * @Description 更新流程审批通过日期
	  * @param apCatCode			单据大类编码
	  * @param bizId				业务主键ID
	  * @param loginName			当前登录人
	  * @throws Exception
	  * @return void	返回类型
	 */
	public void updateArAuditDate(String apCatCode,String bizId) throws Exception;
	/**
	 * 
	  * @Title revokeCompleteProcess 方法名
	  * @Description 流程审批通过之后再次撤回
	  * @param docId
	  * @param docCat
	  * @param cancelReason
	  * @param language
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void revokeCompleteProcess(String docId,String docCat,String cancelReason,String language) throws Exception;
}
