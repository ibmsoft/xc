package com.xzsoft.xsr.core.webservice.internal;

import javax.jws.WebParam;
import javax.jws.WebService;

import net.sf.json.JSONArray;

/**
 * 报表系统内部接口-基于cxf
 * 
 * @author ZhouSuRong 2016-4-6
 */
// targetNamespace不设置导致方法找不到
@WebService(targetNamespace = "http://ws.xsr.xzsoft.com")
public interface InternalDataService {

	public String sendData(@WebParam(targetNamespace = "http://ws.xsr.xzsoft.com", name = "json") String json,
			@WebParam(targetNamespace = "http://ws.xsr.xzsoft.com", name = "userName") String userName,
			@WebParam(targetNamespace = "http://ws.xsr.xzsoft.com", name = "userPwd") String userPwd);

}
