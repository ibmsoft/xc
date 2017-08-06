package com.xzsoft.xc.ar.api;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
  * @ClassName ArWebService
  * @Description 应收模块WebService服务接口
  * @author 任建建
  * @date 2016年7月7日 下午12:49:07
 */
@WebService(targetNamespace = "http://ws.xzsoft.com")
public interface ArWebService {
	/**
	 * 
	  * @Title updateArInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param arCatCode
	  * @param insCode
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
    public String updateArInsCode(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "arCatCode") String arCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "insCode") String insCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title updateArAuditStatus 方法名
      * @Description 更新审批流程审批状态
      * @param arCatCode
      * @param bizId
      * @param bizStatusCat
      * @param bizStatusCatDesc
      * @param loginName
      * @param password
      * @return
      * @throws Exception
      * @return String 返回类型
     */
    public String updateArAuditStatus(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "arCatCode") String arCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCat") String bizStatusCat,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCatDesc") String bizStatusCatDesc,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title updateArAuditDate 方法名
      * @Description 更新流程审批通过日期
      * @param arCatCode
      * @param bizId
      * @param loginName
      * @param password
      * @return
      * @throws Exception
      * @return String 返回类型
     */
    public String updateArAuditDate(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "arCatCode") String arCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    
}
