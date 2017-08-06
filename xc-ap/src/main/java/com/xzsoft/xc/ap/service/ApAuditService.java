package com.xzsoft.xc.ap.service;
/**
 * 
  * @ClassName ApAuditService
  * @Description 流程信息业务处理层
  * @author 任建建
  * @date 2016年4月26日 上午11:13:59
 */
public interface ApAuditService {
	/**
	 * 
	  * @Title updateApInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param apCatCode		单据大类编码
	  * @param insCode			实例编码
	  * @param bizId			业务主键ID
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void updateApInsCode(String apCatCode,String insCode,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateApAuditStatus
	  * @Description 更新审批流程审批状态
	  * @param apCatCode		单据大类编码
	  * @param bizStatusCat		审批状态
	  * @param bizStatusCatDesc	审批状态描述
	  * @param bizId			业务主键ID
	  * @param Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateApAuditStatus(String apCatCode,String bizStatusCat,String bizStatusCatDesc,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateApAuditDate
	  * @Description 更新流程审批通过日期
	  * @param apCatCode		单据大类编码
	  * @param bizId			业务主键ID
	  * @param loginName		当前登录人
	  * @param Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateApAuditDate(String apCatCode,String bizId) throws Exception;
	/**
	 * 
	  * @Title revokeCompleteProcess 方法名
	  * @Description 流程审批通过之后再次撤回
	  * @param docId
	  * @param docCat
	  * @param cancelReason
	  * @param language			当前语言
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void revokeCompleteProcess(String docId,String docCat,String cancelReason,String language) throws Exception;
}
