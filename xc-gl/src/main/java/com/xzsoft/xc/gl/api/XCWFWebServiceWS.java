package com.xzsoft.xc.gl.api;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @ClassName: WFWebService 
 * @Description: 云ERP单据审批流程订制webservice服务
 * @author tangxl
 * @date 2016年5月23日 上午10:30:13 
 *
 */
@WebService(targetNamespace = "http://ws.xzsoft.com")
public interface XCWFWebServiceWS {
	
	/**
	 * @methodName      getBillQuota
	 * @methodDescribe  获取云ERP内置流程审批节点条件阈值
	 * @param ledgerId  账簿ID
	 * @param billType  单据类型
	 * @param actCode   节点编码
	 * @param userName  用户名
	 * @param userPwd   密码
	 * @return
	 */
	@WebMethod
	public String getBillQuota(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "ledgerId") String ledgerId,
			            @WebParam(targetNamespace = "http://ws.xzsoft.com", name = "billType") String billType,
			            @WebParam(targetNamespace = "http://ws.xzsoft.com", name = "actCode") String actCode,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
						@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	/**
	 * @methodName      getPassFlag
	 * @methodDescribe  获取云ERP内置流程审批节点是否跳过属性
	 * @param ledgerId  账簿ID
	 * @param billType  单据类型
	 * @param actCode   节点编码
	 * @param userName  用户名
	 * @param userPwd   密码
	 * @return
	 */
	@WebMethod
	public String getPassFlag(@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "ledgerId") String ledgerId,
            @WebParam(targetNamespace = "http://ws.xzsoft.com", name = "billType") String billType,
            @WebParam(targetNamespace = "http://ws.xzsoft.com", name = "actCode") String actCode,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userName") String userName,
			@WebParam(targetNamespace = "http://ws.xzsoft.com", name = "userPwd") String userPwd);
	
}
