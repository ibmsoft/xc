package com.xzsoft.xc.ap.api;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
  * @ClassName: ApWebService
  * @Description: 应付模块WebService服务接口
  * @author 任建建
  * @date 2016年4月26日 上午10:53:40
 */
@WebService(targetNamespace = "http://ws.xzsoft.com")
public interface ApWebService {
	/**
	 * 
	  * @Title: updateApInsCode
	  * @Description: 更新审批流程实例编码
	  * @param @param apCatCode
	  * @param @param insCode
	  * @param @param bizId
	  * @param @param loginName
	  * @param @param password
	  * @param @return
	  * @param @throws Exception	设定文件
	  * @return String	返回类型
	 */
    public String updateApInsCode(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "apCatCode") String apCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "insCode") String insCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title: updateApAuditStatus
      * @Description: 更新审批流程审批状态
      * @param @param apCatCode
      * @param @param bizId
      * @param @param bizStatusCat
      * @param @param bizStatusCatDesc
      * @param @param loginName
      * @param @param password
      * @param @return
      * @param @throws Exception	设定文件
      * @return String	返回类型
     */
    public String updateApAuditStatus(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "apCatCode") String apCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCat") String bizStatusCat,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCatDesc") String bizStatusCatDesc,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title: updateApAuditDate
      * @Description: 更新流程审批通过日期
      * @param @param apCatCode
      * @param @param bizId
      * @param @param loginName
      * @param @param password
      * @param @return
      * @param @throws Exception	设定文件
      * @return String	返回类型
     */
    public String updateApAuditDate(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "apCatCode") String apCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    
}
