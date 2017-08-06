package com.xzsoft.xc.po.api;
import javax.jws.WebParam;
import javax.jws.WebService;

/** 
 * @ClassName: PoWebService
 * @Description: 采购模块webService服务接口
 * @author weicw
 * @date 2016年12月9日 上午10:32:01 
 * 
 */
@WebService(targetNamespace = "http://ws.xzsoft.com")
public interface PoWebService {
	/**
	 * 
	  * @Title: updatePoInsCode
	  * @Description: 更新审批流程实例编码
	  * @param @param poCatCode
	  * @param @param insCode
	  * @param @param bizId
	  * @param @param loginName
	  * @param @param password
	  * @param @return
	  * @param @throws Exception	设定文件
	  * @return String	返回类型
	 */
    public String updatePoInsCode(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "poCatCode") String poCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "insCode") String insCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title: updatePoAuditStatus
      * @Description: 更新审批流程审批状态
      * @param @param poCatCode
      * @param @param bizId
      * @param @param bizStatusCat
      * @param @param bizStatusCatDesc
      * @param @param loginName
      * @param @param password
      * @param @return
      * @param @throws Exception	设定文件
      * @return String	返回类型
     */
    public String updatePoAuditStatus(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "poCatCode") String poCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCat") String bizStatusCat,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizStatusCatDesc") String bizStatusCatDesc,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
    /**
     * 
      * @Title: updatePoAuditDate
      * @Description: 更新流程审批通过日期
      * @param @param poCatCode
      * @param @param bizId
      * @param @param loginName
      * @param @param password
      * @param @return
      * @param @throws Exception	设定文件
      * @return String	返回类型
     */
    public String updatePoAuditDate(
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "poCatCode") String poCatCode,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "bizId") String bizId,
    		@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "loginName") String loginName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "password") String password) throws Exception;
}
