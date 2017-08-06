package com.xzsoft.xc.ex.api;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @ClassName: XCEXWebService 
 * @Description: 费用报销对外服务API接口
 * @author linp
 * @date 2016年3月10日 下午5:22:36 
 *
 */
@WebService(targetNamespace = "http://ws.xzsoft.com")
public interface XCEXWebService {

	/**
     * @Title: updateInsCode 
     * @Description: 更新单据审批流程实例编码 
     * @param insCode
     * @param bizId
     * @param loginName
     * @param password
     * @return    设定文件
     */
	@WebMethod
    public String updateInsCode(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "insCode") String insCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password);
	
    /**
     * 
     * @Title: updateAuditStatus 
     * @Description: 更新单据审批状态信息
     * @param bizId
     * @param bizStatusCat
     * @param bizStatusCatDesc
     * @param catCode
     * @param applyDocId
     * @param loginName
     * @param password
     * @return    设定文件
     */
	@WebMethod
    public String updateAuditStatus(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCat") String bizStatusCat,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCatDesc") String bizStatusCatDesc,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "catCode") String catCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "applyDocId") String applyDocId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password);
    
    /**
     * @Title: updateAuditDate 
     * @Description: 更新单据审批通过日期
     * @param bizId
     * @param loginName
     * @param password
     * @return    设定文件
     */
	@WebMethod
    public String updateAuditDate(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password);
	
	/**
	 * @Title: updateAuditDateAndBudgetFact 
	 * @Description: 更新单据审批通过日期并记录预算占用数
	 * @param bizId
	 * @param loginName
	 * @param password
	 * @return    设定文件
	 */
	@WebMethod
	public String updateAuditDateAndBudgetFact(    		
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) ;
    
    /**
     * @Title: updateAuditUsers 
     * @Description: 更新单据当前审批人信息
     * @param bizId
     * @param nodeApprovers
     * @param loginName
     * @param password
     * @return    设定文件
     */
	@WebMethod
    public String updateAuditUsers(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "nodeApprovers") String nodeApprovers,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password);
	
	/**
	 * @Title: recordBudgetFact 
	 * @Description: 记录预算占用数
	 * @param bizId
	 * @param loginName
	 * @param password
	 * @return    设定文件
	 */
	@WebMethod
	public String recordBudgetFact(    		
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) ;
    
}
