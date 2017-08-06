package com.xzsoft.xc.po.service;
/** 
 * @ClassName: PoAuditService
 * @Description: 流程信息业务处理层
 * @author weicw
 * @date 2016年12月9日 上午10:39:42 
 * 
 */
public interface PoAuditService {
	/**
	 * 
	  * @Title updatePoInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param poCatCode		单据大类编码
	  * @param insCode			实例编码
	  * @param bizId			业务主键ID
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void updatePoInsCode(String poCatCode,String insCode,String bizId) throws Exception;
	/**
	 * 
	  * @Title updatePoAuditStatus
	  * @Description 更新审批流程审批状态
	  * @param poCatCode		单据大类编码
	  * @param bizStatusCat		审批状态
	  * @param bizStatusCatDesc	审批状态描述
	  * @param bizId			业务主键ID
	  * @param Exception	设定文件
	  * @return void	返回类型
	 */
	public void updatePoAuditStatus(String poCatCode,String bizStatusCat,String bizStatusCatDesc,String bizId) throws Exception;
	/**
	 * 
	  * @Title updatePoAuditDate
	  * @Description 更新流程审批通过日期
	  * @param poCatCode		单据大类编码
	  * @param bizId			业务主键ID
	  * @param loginName		当前登录人
	  * @param Exception	设定文件
	  * @return void	返回类型
	 */
	public void updatePoAuditDate(String poCatCode,String bizId) throws Exception;
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
