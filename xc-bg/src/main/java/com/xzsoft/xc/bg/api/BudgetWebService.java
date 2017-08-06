package com.xzsoft.xc.bg.api;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @ClassName: BudgetWebService 
 * @Description: 预算管理WebService服务接口
 * @author linp
 * @date 2016年3月10日 上午9:43:05 
 *
 */
@WebService(targetNamespace = "http://ws.xzsoft.com")
public interface BudgetWebService {
	/**
	 * 
	  * @Title: budgetDataInsertAndAuditDate
	  * @Description: 预算余额汇总和更新审批通过日期
	  * @param @param bizId		预算单ID
	  * @param @param opType	操作类型: 1-插入费用预算余额表, 2-插入项目预算余额表
	  * @param @param loginName	登录人
	  * @param @param password	密码
	  * @param @return
	  * @param @throws Exception	设定文件
	  * @return String	返回类型
	 */
	public String budgetDataInsertAndAuditDate(
			@WebParam(targetNamespace = "http://ws.xzsoft.com",name = "bizId") String bizId,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password,
			@WebParam(targetNamespace = "http://ws.xzsoft.com",name = "opType") String opType) throws Exception;
	/**
	 * 
	  * @Title: updateBgInsCode
	  * @Description: 更新审批流程实例编码
	  * @param @param insCode
	  * @param @param bizId
	  * @param @param loginName
	  * @param @param password
	  * @param @return	设定文件
	  * @return String	返回类型
	 */
    public String updateBgInsCode(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "insCode") String insCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title: updateBgAuditStatus
      * @Description: 更新审批流程审批状态
      * @param @param bizId
      * @param @param bizStatusCat
      * @param @param bizStatusCatDesc
      * @param @param loginName
      * @param @param password
      * @param @return	设定文件
      * @return String	返回类型
     */
    public String updateBgAuditStatus(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCat") String bizStatusCat,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCatDesc") String bizStatusCatDesc,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title: updateBgAuditDate
      * @Description: 更新流程审批通过日期
      * @param @param bizId
      * @param @param loginName
      * @param @param password
      * @param @return	设定文件
      * @return String	返回类型
     */
    public String updateBgAuditDate(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    
    /**
     * 
      * @Title: updateBgAuditUsers
      * @Description: 更新当前审批人信息
      * @param @param bizId
      * @param @param nodeApprovers
      * @param @param loginName
      * @param @param password
      * @param @return	设定文件
      * @return String	返回类型
     */
    public String updateBgAuditUsers(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "nodeApprovers") String nodeApprovers,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
}
