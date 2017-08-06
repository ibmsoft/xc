package com.xzsoft.xc.st.service;
/**
 * 
  * @ClassName XcStAuditService
  * @Description 流程信息业务处理层
  * @author RenJianJian
  * @date 2016年12月8日 下午3:22:45
 */
public interface XcStAuditService {
	/**
	 * 
	  * @Title updateXcStInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param stCatCode		单据大类编码
	  * @param insCode			实例编码
	  * @param bizId			业务主键ID
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStInsCode(String stCatCode,String insCode,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateXcStAuditStatus
	  * @Description 更新审批流程审批状态
	  * @param stCatCode		单据大类编码
	  * @param bizStatusCat		审批状态
	  * @param bizStatusCatDesc	审批状态描述
	  * @param bizId			业务主键ID
	  * @throws Exception
	  * @return void	返回类型
	 */
	public void updateXcStAuditStatus(String stCatCode,String bizStatusCat,String bizStatusCatDesc,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateXcStAuditDate
	  * @Description 更新流程审批通过日期
	  * @param stCatCode			单据大类编码
	  * @param bizId				业务主键ID
	  * @param loginName			当前登录人
	  * @throws Exception
	  * @return void	返回类型
	 */
	public void updateXcStAuditDate(String stCatCode,String bizId) throws Exception;
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
	public void revokeXcStCompleteProcess(String docId,String docCat,String cancelReason,String language) throws Exception;
}
