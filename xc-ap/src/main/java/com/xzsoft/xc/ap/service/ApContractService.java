package com.xzsoft.xc.ap.service;

import java.util.List;

import org.codehaus.jettison.json.JSONObject;

import com.xzsoft.xc.ap.modal.ApContractBean;

/** 
 * @ClassName: ApContractService 
 * @Description: TODO
 * @author weicw 
 * @date 2016年8月19日 下午3:20:09 
 * 
 * 
 */
public interface ApContractService {
	/**
	 * @Title:saveContarct
	 * @Description:保存合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject saveContarct(String info,String lang) throws Exception;
	
	/**
	 * @Title:delContractInfo
	 * @Description:删除合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject delContractInfo(String idArray,String lang) throws Exception;
	
	/**
	 * @Title:closeContract
	 * @Description:关闭合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject closeContract(String idArray,String lang) throws Exception;
	
	/**
	 * @Title:openContract
	 * @Description:打开合同
	 * 参数格式:
	 * @param  java.lang.String
	 * @return org.codehaus.jettison.json.JSONObject
	 * @throws 
	 */
	public JSONObject openContract(String idArray,String lang) throws Exception;
	
}

