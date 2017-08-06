package com.xzsoft.xc.st.dao;
/**
 * 
  * @ClassName XcStAuditDao
  * @Description 库存模块单据流程信息Dao
  * @author RenJianJian
  * @date 2016年12月8日 下午3:26:27
 */
public interface XcStAuditDao {
	/**
	 * 
	  * @Title updateXcStInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param insCode		实例编码
	  * @param bizId		主键值
	  * @throws Exception
	  * @return void 返回类型
	 */
	public void updateXcStInsCode(String insCode,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateXcStAuditStatus
	  * @Description 更新审批流程审批状态
	  * @param bizStatusCat		状态编码
	  * @param bizStatusCatDesc	状态描述
	  * @param bizId			主键值
	  * @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateXcStAuditStatus(String bizStatusCat,String bizStatusCatDesc,String bizId) throws Exception;
	/**
	 * 
	  * @Title updateXcStAuditDate
	  * @Description 更新流程审批通过日期
	  * @param bizId			主键值
	  * @throws Exception	设定文件
	  * @return void	返回类型
	 */
	public void updateXcStAuditDate(String bizId) throws Exception;
	/**
	 * 
	  * @Title revokeXcStCompleteProcess 方法名
	  * @Description 审批通过之后执行撤回，更新单据信息
	  * @param bizId
	  * @throws Exception 设定文件
	  * @return void 返回类型
	 */
	public void revokeXcStCompleteProcess(String bizId) throws Exception;
}
