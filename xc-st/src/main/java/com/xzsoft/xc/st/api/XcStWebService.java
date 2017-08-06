package com.xzsoft.xc.st.api;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 
  * @ClassName XcStWebService
  * @Description 库存模块WebService服务接口
  * @author RenJianJian
  * @date 2016年12月8日 下午3:20:25
 */
@WebService(targetNamespace = "http://ws.xzsoft.com")
public interface XcStWebService {
	/**
	 * 
	  * @Title updateXcStInsCode 方法名
	  * @Description 更新审批流程实例编码
	  * @param stCatCode
	  * @param insCode
	  * @param bizId
	  * @param loginName
	  * @param password
	  * @return
	  * @throws Exception
	  * @return String 返回类型
	 */
    public String updateXcStInsCode(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "stCatCode") String stCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "insCode") String insCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title updateXcStAuditStatus 方法名
      * @Description 更新审批流程审批状态
      * @param stCatCode
      * @param bizId
      * @param bizStatusCat
      * @param bizStatusCatDesc
      * @param loginName
      * @param password
      * @return
      * @throws Exception
      * @return String 返回类型
     */
    public String updateXcStAuditStatus(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "stCatCode") String stCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCat") String bizStatusCat,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCatDesc") String bizStatusCatDesc,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title updateXcStAuditDate 方法名
      * @Description 更新流程审批通过日期
      * @param stCatCode
      * @param bizId
      * @param loginName
      * @param password
      * @return
      * @throws Exception
      * @return String 返回类型
     */
    public String updateXcStAuditDate(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "stCatCode") String stCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    
}
